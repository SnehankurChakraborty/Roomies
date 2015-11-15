package com.phaseii.rxm.roomies.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Snehankur on 11/2/2015.
 */
public class ActivityUtils {

    /**
     * Enum for putting extras in intent
     */
    public enum Extras {
        ROOM_STATS("room_stats"),
        ROOM_EXPENSES("room_expenses");
        private String value;

        Extras(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    /**
     * @param context
     * @param activity
     * @param extras
     * @param isFinish
     * @throws RoomXpnseMngrException
     */
    public static void startActivityHelper(Context context, String activity, Map<Extras,
            List<? extends Parcelable>> extras, boolean isFinish) throws
            RoomXpnseMngrException {
        try {
            Class activityClass = Class.forName(activity);
            Intent intent = new Intent(context, activityClass);
            if (null != extras) {
                Set<Extras> keySet = extras.keySet();
                for (Extras key : keySet) {
                    intent.putParcelableArrayListExtra(key.getValue(), (ArrayList<? extends
                            Parcelable>) extras.get(key));
                }
            }
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.animator.translate_right_to_left_enter,
                    R.animator.translate_right_to_left_exit);

            if (isFinish) {
                ActivityCompat.finishAffinity((Activity) context);
            }
        } catch (ClassNotFoundException e) {
            throw new RoomXpnseMngrException(activity + " class not found", e);
        }
    }

    /**
     * @param tag
     * @param activity
     * @param fragment
     * @param layoutId
     */
    public static void replaceFragmentWithTag(String tag,
                                              FragmentActivity activity, RoomiesFragment
                                                      fragment, int layoutId) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.replace(layoutId, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * @param activity
     * @param fragment
     * @param layoutId
     */
    public static void replaceFragment(FragmentActivity activity, RoomiesFragment fragment, int
            layoutId) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.replace(layoutId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * @param activity
     * @param fragment
     * @param layoutId
     * @param arguments
     */
    public static void replaceFragmentWithBundle(FragmentActivity activity, RoomiesFragment
            fragment, int layoutId, Map<Extras, List<? extends Parcelable>> arguments) {
        if (null != arguments) {
            Bundle bundle = new Bundle();
            Set<Extras> keySet = arguments.keySet();
            for (Extras key : keySet) {
                bundle.putParcelableArrayList(key.getValue(), (ArrayList) arguments.get(key));
            }
            fragment.setParceableBundle(bundle);
        }
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.replace(layoutId, fragment);

        transaction.addToBackStack(null);
        transaction.commit();
    }
}