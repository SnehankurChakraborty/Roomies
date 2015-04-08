package com.phaseii.rxm.roomies.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.phaseii.rxm.roomies.R;


/**
 * @author Snehankur Chakraborty
 * @created 3/14/2015
 * .
 */
public class BannerView extends TextView {
	public BannerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setType(context);
	}


	public BannerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setType(context);
	}

	public BannerView(Context context) {
		super(context);
		setType(context);
	}

	private void setType(Context context) {
		if (!this.isInEditMode()) {
			this.setTypeface(Typeface.createFromAsset(context.getAssets(),
					"fonts/LilyScriptOne-Regular.otf"), Typeface.BOLD);
		}
		this.setTextColor(Color.WHITE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			this.setTextIsSelectable(false);
		}
		this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);

	}
}
