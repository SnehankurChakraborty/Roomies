package com.phaseii.rxm.roomies.background;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.manager.RoomStatManager;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Snehankur on 9/6/2015.
 */
public class AlarmService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Calendar calendar = Calendar.getInstance();

        if (calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(
                Calendar.DAY_OF_MONTH)) {

            RoomStatManager manager = new RoomStatManager(this);
            if (manager.addNewMonthData()) {

                Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.roomies_launcher);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(largeIcon)
                        .setContentTitle("Welcome to " + new DateFormatSymbols(Locale
                                .ENGLISH).getMonths()[calendar.get(Calendar.MONTH)] + " " +
                                "with Roomies.")
                        .setContentText("We have set up the budget details for you.")
                        .setSmallIcon(R.drawable.ic_stat_action_assessment)
                        .setColor(R.color.accent);

        int mNotificationId = 101;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}