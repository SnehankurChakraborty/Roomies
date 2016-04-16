package com.phaseii.rxm.roomies.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Snehankur on 11/25/2015.
 */
public class TypefaceUtils {

    public static final int ROBOTO = 0;
    public static final int LATO_BLACK = 1;
    public static final int LATO_BOLD = 2;
    public static final int LATO_HAIRLINE = 3;
    public static final int LATO_HEAVY = 4;
    public static final int LATO_MEDIUM = 5;
    public static final int LATO_REGULAR = 6;
    public static final int LATO_SEMIBOLD = 7;
    public static final int LATO_THIN = 8;
    public static final int LILYSCRIPTONE = 9;
    public static final int VARELAROUND = 10;


    private static TypefaceUtils mInstance = null;
    private Typeface roboto = null;
    private Typeface latoBlack = null;
    private Typeface latoBold = null;
    private Typeface latoHairline = null;
    private Typeface latoHeavy = null;
    private Typeface latoMedium = null;
    private Typeface latoRegular = null;
    private Typeface latoSemiBold = null;
    private Typeface latoThin = null;
    private Typeface lilyScriptOne = null;
    private Typeface varelaRound = null;

    private TypefaceUtils(Context mContext) {

        latoBlack = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Black.ttf");
        latoBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Bold.ttf");
        latoHairline = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Hairline.ttf");
        latoHeavy = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Heavy.ttf");
        latoMedium = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Light.ttf");
        latoRegular = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Regular.ttf");
        latoSemiBold = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Semibold.ttf");
        latoThin = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Thin.ttf");
        lilyScriptOne = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/LilyScriptOne-Regular.otf");
        varelaRound = Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular" +
                ".ttf");
    }

    public static TypefaceUtils getInstance(Context mContext) {
        if (null == mInstance) {
            mInstance = new TypefaceUtils(mContext);
        }
        return mInstance;
    }

    public Typeface getFont(int font) {
        switch (font) {
            case ROBOTO:
                return roboto;
            case LATO_BLACK:
                return latoBlack;
            case LATO_BOLD:
                return latoBold;
            case LATO_HAIRLINE:
                return latoHairline;
            case LATO_HEAVY:
                return latoHeavy;
            case LATO_MEDIUM:
                return latoMedium;
            case LATO_REGULAR:
                return latoRegular;
            case LATO_SEMIBOLD:
                return latoSemiBold;
            case LATO_THIN:
                return latoThin;
            case LILYSCRIPTONE:
                return lilyScriptOne;
            case VARELAROUND:
                return varelaRound;
            default:
                return roboto;
        }
    }

}
