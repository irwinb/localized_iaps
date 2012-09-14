package org.liab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;


/**
 * Reads a CSV file from the assets folder output by the Google Play
 * developer's console.
 * 
 * See <a href="http://tinyurl.com/9oukmdr">Adding a batch of items to a product list</a>
 * 
 * @author Irwin Billing
 *
 */
public class LocalizedIAPs {
	
	/* Enable/disable debug output. */
	public static final boolean DEBUG = true;

	private Map< String, Product > products_map_;
	
	private Locale locale_;
	
	/**
	 * Load localized IAPs, using the default locale for the current system.
	 * 
	 * @param csv_path	path to CSV file with IAP information output by the
	 * 					Google Play developer's console.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public LocalizedIAPs( Context ctx, String csv_path ) 
			throws IOException, ParseException {
		this( ctx, csv_path, Locale.getDefault() );
	}
	
	/**
	 * Load localized IAPs, specifying a locale.
	 * 
	 * @param csv_path	path to CSV file with IAP information output by the
	 * 					Google Play developer's console.
	 * @param locale	which locale to load from the CSV file.
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public LocalizedIAPs( Context ctx, String csv_path, Locale locale )
			throws IOException, ParseException {
		locale_ = locale;
		products_map_ = new HashMap< String, Product >();
		
		parseCsv( ctx, csv_path );
	}
	
	/**
	 * @return	an immutable products map.
	 */
	public Map< String, Product > getProducts() {
		return Collections.unmodifiableMap( products_map_ );
	}
	
	/**
	 * @return	the requested product, or null if it doesn't exist
	 * 			in the current locale.
	 */
	public Product getProduct( String product_id ) {
		return products_map_.get( product_id );
	}
	
	/**
	 * Read the csv file from the assets folder and 
	 * 
	 * @param csv_path
	 * @throws IOException
	 * @throws ParseException 
	 */
	private void parseCsv( Context ctx, String csv_path ) 
			throws IOException, ParseException {
		AssetManager am = ctx.getAssets();
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader( new InputStreamReader(
					am.open( csv_path, AssetManager.ACCESS_STREAMING ) ) );
			
			// Ignore column defining names.
			br.readLine();
			
			String line = null;
			while( ( line = br.readLine() ) != null ) {
				Product prod = new Product( line, locale_ );
				
				products_map_.put( prod.getProductId(), prod );
			}
		} finally {
			if ( br != null )
				br.close();
		}
	}
}
