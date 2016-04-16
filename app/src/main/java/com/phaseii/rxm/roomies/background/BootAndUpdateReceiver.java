package com.phaseii.rxm.roomies.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.phaseii.rxm.roomies.utils.RoomiesHelper;

/**
 * Created by Snehankur on 9/6/2015.
 */
public class BootAndUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent bootIntent) {
        RoomiesHelper.setupAlarm(context);
    }
}
