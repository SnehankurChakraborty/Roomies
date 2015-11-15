package com.phaseii.rxm.roomies.util;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.DELAY_MILLIS;

/**
 * Created by Snehankur on 11/8/2015.
 */
public class ToastUtils {

    /**
     *
     * @param context
     * @param text
     * @param mToast
     */
    public static void createToast(Context context, String text, Toast mToast) {
        ToastUtils toaster = new ToastUtils();
        if (mToast != null) {
            mToast.cancel();
        }
        int duration = Toast.LENGTH_SHORT;
        mToast = Toast.makeText(context, text, duration);
        mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
        mToast.show();
        toaster.delayToast(mToast);
    }

    /**
     *
     * @param mToast
     */
    private void delayToast(final Toast mToast) {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mToast.cancel();
            }
        }, DELAY_MILLIS);
    }
}
