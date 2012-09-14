package org.liab;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.http.ParseException;

/**
 * Just a container for a Google Play In-App Product.
 * 
 * @author Irwin Billing
 *
 */
public final class Product {

	public final static Locale FALLBACK_LOCALE = Locale.US;
	
	public final static int NUM_COLUMNS = 7;

	private Locale locale_;

	private String product_id_;
	private String publish_state_;
	private String purchase_type_;
	private String title_;
	private String description_;
	
	private String formatted_price_;
	
	private boolean auto_translate_;
	private boolean auto_fill_;
	
	private double price_micro_;
	
	/**
	 * @param csv_line 	a line from the csv output by the Google Play
	 * 					developer's console to parse into a product.
	 */
	public Product( String csv_line, Locale locale ) {
		locale_ = locale;
		title_ = null;
		description_ = null;
		formatted_price_ = null;
		parseCsvLine( csv_line );
	}
	
	/**
	 * Fill the product with values read form a line in a csv file.
	 * 
	 * @param line line to read from.
	 */
	private void parseCsvLine( String line ) 
			throws ParseException {
		CSVParser parser = new CSVParser();
		String[] columns = parser.parseLine( line );
		
		if ( columns == null || columns.length < NUM_COLUMNS )
			throw new ParseException( "Invalid csv line." );
		
		product_id_ 	= columns[ 0 ];
		publish_state_ 	= columns[ 1 ];
		purchase_type_ 	= columns[ 2 ];
		auto_translate_	= Boolean.valueOf( columns[ 3 ] );
		auto_fill_ 		= Boolean.valueOf( columns[ 5 ] );
		
		parseTitleAndDescription( columns[ 4 ] );
		parsePrice( columns[ 6 ] );
	}
	
	/**
	 * Load the proper title and description based on the locale or fall back
	 * to LOCALE_TITLE_FALLBACK if not found.
	 * 
	 * @param column	the column to parse.
	 * 
	 * @throws ParseException	if a title and description can't be found based
	 * 							on the locale.
	 */
	private void parseTitleAndDescription( String column ) 
			throws ParseException {
		String[] tokens = column.split( "; " );
		
		for( int i = 0; i < tokens.length; ) {
			String l = tokens[ i++ ];
			String t = tokens[ i++ ];
			String d = tokens[ i++ ];
			
			if ( l.equals( locale_.toString() ) ) {
				title_ = t;
				description_ = d;
				return;
			} else if ( l.equals( FALLBACK_LOCALE.toString() ) ) {
				title_ = t;
				description_ = d;
			}
		}
		
		if ( title_ == null ) {
			throw new ParseException( "The requested or fallback locale's title " +
					"& description for " + getProductId() + " was not found." );
		}
	}
	
	/**
	 * Load the proper price based on the locale or fall back to
	 * LOCALE_PRICE_FALLBACK if not found.
	 * 
	 * @param column	the column to parse.
	 * 
	 * @throws ParseException	if a price can't be found based on the locale.
	 */
	private void parsePrice( String column  )
			throws ParseException {
		String[] tokens = column.split( "; " );
		
		for ( int i = 0; i < tokens.length; ) {
			String l = tokens[ i++ ];
			String p = tokens[ i++ ];
			
			if ( l.equals( locale_.getCountry() ) ) {
				price_micro_ = Long.parseLong( p );
				formatted_price_ = NumberFormat.getCurrencyInstance( 
						locale_ ).format( price_micro_ / 1000000 );
				return;
			} else if ( l.equals( FALLBACK_LOCALE.getCountry() ) ) {
				price_micro_ = Long.parseLong( p );
				formatted_price_ = NumberFormat.getCurrencyInstance( 
						FALLBACK_LOCALE ).format( price_micro_ / 1000000 );
			}
		}
		
		if ( formatted_price_ == null ) {
			throw new ParseException( "The requested or fallback locale's price " +
					"for " + getProductId() + " was not found." );
		}
	}
	
	public final Locale getLocale() {
		return locale_;
	}

	public final String getPublishState() {
		return publish_state_;
	}

	public final String getPurchaseType() {
		return purchase_type_;
	}

	public final String getTitile() {
		return title_;
	}

	public final String getDescription() {
		return description_;
	}

	public final String getProductId() {
		return product_id_;
	}
	
	public String getFormattedPrice() {
		return formatted_price_;
	}

	public final boolean isAutoTranslate() {
		return auto_translate_;
	}

	public final boolean isAutoFill() {
		return auto_fill_;
	}

	public final double getPriceAsMicroUnits() {
		return price_micro_;
	}
	
	public final String getLocalizedPrice() {
		return "";
	}
}
