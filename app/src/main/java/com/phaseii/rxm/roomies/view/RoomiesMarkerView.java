package com.phaseii.rxm.roomies.view;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.Utils;
import com.phaseii.rxm.roomies.R;

/**
 * Created by Snehankur on 4/18/2015.
 */
public class RoomiesMarkerView extends MarkerView {


	private TextView tvContent;

	public RoomiesMarkerView(Context context, int layoutResource) {
		super(context, layoutResource);
		tvContent = (TextView) findViewById(R.id.tvContent);
	}

	@Override
	public void refreshContent(Entry e, int dataSetIndex) {
		tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));

	}

	@Override
	public int getXOffset() {
		// this will center the marker-view horizontally
		return -(getWidth() / 2);
	}

	@Override
	public int getYOffset() {
		// this will cause the marker-view to be above the selected value
		return -getHeight();
	}
}

