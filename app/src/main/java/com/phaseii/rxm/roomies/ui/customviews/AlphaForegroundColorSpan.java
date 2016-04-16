package com.phaseii.rxm.roomies.ui.customviews;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

/**
 * Created by Snehankur on 4/27/2015.
 */
public class AlphaForegroundColorSpan extends ForegroundColorSpan {
    private float mAlpha;

    public AlphaForegroundColorSpan(int color) {
        super(color);
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setColor(getAlphaColor());
    }

    public void setAlpha(float alpha) {
        mAlpha = alpha;
    }

    private int getAlphaColor() {
        int foregroundColor = getForegroundColor();
        return Color.argb((int) (mAlpha * 255), Color.red(foregroundColor),
                Color.green(foregroundColor), Color.blue(foregroundColor));
    }
}