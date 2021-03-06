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

package com.phaseii.rxm.roomies.ui.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.utils.TypefaceUtils;

/**
 * Created by Snehankur on 11/25/2015.
 */
public class RoomiesEditText extends EditText {

    public RoomiesEditText(Context context) {
        super(context);
    }

    public RoomiesEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        parseAttributes(context, attrs);
    }

    public RoomiesEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()) {
            return;
        }
        parseAttributes(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoomiesEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if (isInEditMode()) {
            return;
        }
        parseAttributes(context, attrs);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.RoomiesEditText);

        //The value 0 is a default, but shouldn't ever be used since the attr is an enum
        int typeface = values.getInt(R.styleable.RoomiesEditText_typeface, 0);
        setTypeface(TypefaceUtils.getInstance(context).getFont(typeface));

        values.recycle();
    }
}
