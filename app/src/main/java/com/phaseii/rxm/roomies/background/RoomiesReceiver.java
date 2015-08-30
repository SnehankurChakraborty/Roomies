package com.phaseii.rxm.roomies.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Snehankur on 9/6/2015.
 */
public class RoomiesReceiver extends BroadcastReceiver {

    public static final String ROOMIES_ALARM = "roomies_alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ROOMIES_ALARM.equals(intent.getAction())) {
            Intent service = new Intent(context, AlarmService.class);
            context.startService(service);
        }
    }
}
