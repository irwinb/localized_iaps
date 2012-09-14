package org.liab.sample;

import org.liab.Product;

import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;

public class ProductView extends LinearLayout {
	
	TextView title;
	TextView price;
	
	public ProductView( Context context ) {
		super( context );
	}

	public ProductView( Context context, AttributeSet attrs ) {
		super( context, attrs );
	}

	public ProductView( Context context, AttributeSet attrs, int defStyle ) {
		super( context, attrs, defStyle );
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		title = (TextView) findViewById( R.id.product_title );
		price = (TextView) findViewById( R.id.product_price );
	}
	
	public void setProduct( Product p ) {
		title.setText( p.getTitile() + ": " );
		price.setText( p.getFormattedPrice() );
	}
}
