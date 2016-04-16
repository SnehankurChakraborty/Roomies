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
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import static com.phaseii.rxm.roomies.utils.Constants.DELAY_MILLIS;

/**
 * Created by Snehankur on 11/8/2015.
 */
public class ToastUtils {

    /**
     * @param context
     * @param text
     * @param toast
     */
    public static void createToast(Context context, String text, Toast toast) {
        ToastUtils toaster = new ToastUtils();
        if (toast != null) {
            toast.cancel();
        }
        int duration = Toast.LENGTH_SHORT;
        Toast mToast = Toast.makeText(context, text, duration);
        mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
        mToast.show();
        toaster.delayToast(mToast);
    }

    /**
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
