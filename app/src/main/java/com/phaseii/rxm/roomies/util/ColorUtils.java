package com.phaseii.rxm.roomies.util;

import android.graphics.Color;

/**
 * Created by Snehankur on 10/29/2015.
 */
public class ColorUtils {

    public static int blendColors(int from, int to, float ratio) {
        final float inverseRation = 1f - ratio;
        final float r = Color.red(from) * ratio + Color.red(to) * inverseRation;
        final float g = Color.green(from) * ratio + Color.green(to) * inverseRation;
        final float b = Color.blue(from) * ratio + Color.blue(to) * inverseRation;
        return Color.rgb((int) r, (int) g, (int) b);
    }
}
