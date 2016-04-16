/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phaseii.rxm.roomies.utils;

import android.content.Context;
import android.graphics.Color;

import com.phaseii.rxm.roomies.R;

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

    public static int[] getCategoryColor(Context mContext) {
        int[] categoryTemplate = {mContext.getResources().getColor(R.color.rent),
                mContext.getResources().getColor(R.color.maid),
                mContext.getResources().getColor(R.color.electricity),
                mContext.getResources().getColor(R.color.misc)};
        return categoryTemplate;
    }

    public static int[] getSubcategoryColor(Context mContext) {
        int[] subCategoryTemplate = {mContext.getResources().getColor(R.color.bills),
                mContext.getResources().getColor(R.color.grocery),
                mContext.getResources().getColor(R.color.food),
                mContext.getResources().getColor(R.color.entertaintment),
                mContext.getResources().getColor(R.color.others)};
        return subCategoryTemplate;
    }

    public static int[] getBlankColor(Context mContext) {
        int[] blankColor = {mContext.getResources().getColor(R.color.blank_color)};
        return blankColor;
    }

    public static int[] getSummaryColor(Context mContext) {
        int[] summaryColor = {mContext.getResources().getColor(R.color.primary),
                mContext.getResources().getColor(R.color.blank_color)};
        return summaryColor;
    }
}
