package org.liab.sample;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.liab.LocalizedIAPs;
import org.liab.Product;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	
	ListView purchaseView;
	AlertDialog localeChooser;
	Locale[] locales = Locale.getAvailableLocales();
	
    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main_layout );
        
        initializeProductList( Product.FALLBACK_LOCALE );
        initializeLocaleChooser();
    }
    
    private void initializeProductList( Locale locale ) {
    	try {
    		LocalizedIAPs iaps = new LocalizedIAPs( this, "in_app_products.csv", locale );
    		
    		List< Product > items = new ArrayList< Product >();
    		
    		for ( Product p :  iaps.getProducts().values() ){
    			items.add( p );
    		}
    		
    		purchaseView = (ListView) findViewById( R.id.purchaseList );
    		purchaseView.setAdapter( new GameShopListAdapter( this, items ) );
		} catch ( IOException e ) {
			Log.e( TAG, "", e );
		} catch ( ParseException e ) {
			Log.e( TAG, "", e );
		}
    }
    
    private void initializeLocaleChooser() {
    	initializePopup();
        Button btn = (Button) findViewById( R.id.localeChooser );
        btn.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				localeChooser.show();
			}
		} );
    }
    
    private void initializePopup() {
    	final CharSequence[] items = new CharSequence[locales.length];
    	for ( int i = 0; i < items.length; i++ ) {
    		items[ i ] = locales [ i ].getDisplayName();
    	}
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder( this );
    	builder.setTitle( "Choose Locale" );
    	builder.setItems( items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick( DialogInterface dialog, int which ) {
				initializeProductList( locales[ which ] );
			}
		} );
    	
    	localeChooser = builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate( R.menu.activity_main, menu );
        return true;
    }
	
	private static class GameShopListAdapter extends ArrayAdapter< Product > {

		LayoutInflater inflator;
		
		public GameShopListAdapter( Context context, List< Product > products ) {
			super( context, android.R.layout.simple_list_item_2, products );
			
			inflator = LayoutInflater.from( context );
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView( int position, View convertView, ViewGroup parent ) {
			
			if (convertView == null) {
				convertView = inflator.inflate( R.layout.product_view, null );
			}

			Product p = getItem( position );
			ProductView item = (ProductView)convertView;
			item.setProduct( p );
			
			return convertView;
		}
		
	}
}
