package com.phaseii.rxm.roomies.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.util.TypefaceUtils;

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
