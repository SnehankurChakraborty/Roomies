package com.phaseii.rxm.roomies.utils;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.background.RoomiesReceiver;
import com.phaseii.rxm.roomies.database.model.RoomDetails;
import com.phaseii.rxm.roomies.database.model.RoomStats;
import com.phaseii.rxm.roomies.database.model.RoomUserStatData;
import com.phaseii.rxm.roomies.database.model.UserDetails;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.phaseii.rxm.roomies.utils.Constants.IS_GOOGLE_FB_LOGIN;
import static com.phaseii.rxm.roomies.utils.Constants.IS_LOGGED_IN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ELECTRICITY_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MAID_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MAID_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MISCELLANEOUS_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MISCELLANEOUS_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MONTH_YEAR;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_NO_OF_MEMBERS;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_RENT_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_RENT_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOM_ALIAS;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOM_ID;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_SENDER_ID;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_SETUP_COMPLETED;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_TOTAL_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USERNAME;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_ABOUT_ME;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_ALIAS;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_ID;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_LOCATION;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_PHONE;

/**
 * Created by Snehankur on 2/23/2015.
 */
public class RoomiesHelper {

    public static final String TAG = "RoomiesHelper";

    /**
     * @param view
     * @return
     */
    public static boolean isFieldBlankOrEmpty(View view) {
        boolean isBlankOrEmpty = false;
        String textViewData = ((TextView) view).getText().toString();
        if (textViewData.trim().equals("") || textViewData.length
                () == 0) {
            isBlankOrEmpty = true;
        }

        return isBlankOrEmpty;
    }

    /**
     * @param feildId
     * @param context
     * @param view
     * @return
     */
    public static boolean setError(String feildId, Context context, View view) {
        boolean isValid = true;
        String errorId = feildId + "_error";
        String togglId = feildId + "_toggle";
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        int resId = resources.getIdentifier(feildId, "id", packageName);
        int resErrorId = resources.getIdentifier(errorId, "id", packageName);
        int resToggleId = resources.getIdentifier(togglId, "id", packageName);
        View feild = view.findViewById(resId);
        TextView errorFeild = (TextView) view.findViewById(resErrorId);
        if (resToggleId > 0) {
            ToggleButton toggleButton = (ToggleButton) view.findViewById(resToggleId);
            if (toggleButton.isChecked()) {
                if (isFieldBlankOrEmpty(feild)) {
                    isValid = false;
                    errorFeild.setVisibility(View.VISIBLE);
                } else {
                    errorFeild.setVisibility(View.INVISIBLE);
                }
            } else {
                errorFeild.setVisibility(View.INVISIBLE);
            }
        } else {
            if (isFieldBlankOrEmpty(feild)) {
                isValid = false;
                errorFeild.setVisibility(View.VISIBLE);
            } else {
                errorFeild.setVisibility(View.INVISIBLE);
            }
        }
        return isValid;
    }

    /**
     * @param feildId
     * @param context
     * @param view
     * @return
     */
    public static boolean setNumericError(String feildId, Context context, View view) {
        boolean isValid = true;
        String errorId = feildId + "_error";
        String togglId = feildId + "_toggle";
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        int resId = resources.getIdentifier(feildId, "id", packageName);
        int resErrorId = resources.getIdentifier(errorId, "id", packageName);
        int resToggleId = resources.getIdentifier(togglId, "id", packageName);
        View feild = view.findViewById(resId);
        TextView errorFeild = (TextView) view.findViewById(resErrorId);
        if (resToggleId > 0) {
            ToggleButton toggleButton = (ToggleButton) view.findViewById(resToggleId);
            if (toggleButton.isChecked()) {
                if (isFieldBlankOrEmpty(feild) || ((TextView) feild).getText().toString
                        () == "" || Integer.valueOf(((TextView) feild).getText().toString()) <= 0) {
                    isValid = false;
                    errorFeild.setVisibility(View.VISIBLE);
                } else {
                    errorFeild.setVisibility(View.INVISIBLE);
                }
            } else {
                errorFeild.setVisibility(View.INVISIBLE);
            }
        } else {
            if (isFieldBlankOrEmpty(feild) || ((TextView) feild).getText().toString
                    () == "" || Integer.valueOf(((TextView) feild).getText().toString()) <= 0) {
                isValid = false;
                errorFeild.setVisibility(View.VISIBLE);
            } else {
                errorFeild.setVisibility(View.INVISIBLE);
            }
        }
        return isValid;
    }

    /**
     * @param context
     * @param roomUserStat
     * @param userDetails
     * @param roomDetails
     * @param roomStats
     * @param isGoogleFBLogin
     * @return
     */
    public static boolean cacheDBtoPreferences(Context context, RoomUserStatData roomUserStat,
            UserDetails userDetails,
            RoomDetails roomDetails, RoomStats roomStats,
            boolean isGoogleFBLogin) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_ROOMIES_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        if (null != roomUserStat) {
            mEditor.putString(PREF_USERNAME, roomUserStat.getUsername());
            mEditor.putString(PREF_USER_ALIAS, roomUserStat.getUserAlias());
            mEditor.putString(PREF_SENDER_ID, roomUserStat.getSenderId());

            mEditor.putString(PREF_ROOM_ID, String.valueOf(roomUserStat.getRoomId()));
            mEditor.putString(PREF_ROOM_ALIAS, roomUserStat.getRoomAlias());
            mEditor.putString(PREF_NO_OF_MEMBERS, String.valueOf(roomUserStat.getNoOfMembers()));

            mEditor.putString(PREF_RENT_MARGIN, String.valueOf(roomUserStat.getRentMargin()));
            mEditor.putString(PREF_MAID_MARGIN, String.valueOf(roomUserStat.getMaidMargin()));
            mEditor.putString(PREF_ELECTRICITY_MARGIN,
                    String.valueOf(roomUserStat.getElectricityMargin()));
            mEditor.putString(PREF_MISCELLANEOUS_MARGIN,
                    String.valueOf(roomUserStat.getMiscellaneousMargin()));
            mEditor.putString(PREF_TOTAL_MARGIN,
                    String.valueOf(roomUserStat.getRentMargin() + roomUserStat
                            .getMaidMargin()
                            + roomUserStat.getMiscellaneousMargin() +
                            roomUserStat.getElectricityMargin()));
            mEditor.putString(PREF_RENT_SPENT, String.valueOf(roomUserStat.getRentSpent()));
            mEditor.putString(PREF_MAID_SPENT, String.valueOf(roomUserStat.getMaidSpent()));
            mEditor.putString(PREF_ELECTRICITY_SPENT,
                    String.valueOf(roomUserStat.getElectricitySpent()));
            mEditor.putString(PREF_MISCELLANEOUS_SPENT,
                    String.valueOf(roomUserStat.getMiscellaneousSpent()));
            mEditor.putString(PREF_MONTH_YEAR, roomUserStat.getMonthYear());
            mEditor.putBoolean(PREF_SETUP_COMPLETED, true);
            mEditor.putBoolean(IS_LOGGED_IN, true);

        } else {

            if (null != userDetails) {
                if (userDetails.getUserId() >= 0) {
                    mEditor.putString(PREF_USER_ID, String.valueOf(userDetails.getUserId()));
                }
                if (null != userDetails.getUsername()) {
                    mEditor.putString(PREF_USERNAME, userDetails.getUsername());
                }
                if (null != userDetails.getUserAlias()) {
                    mEditor.putString(PREF_USER_ALIAS, userDetails.getUserAlias());
                }
                if (null != userDetails.getPhone()) {
                    mEditor.putString(PREF_USER_PHONE, String.valueOf(userDetails.getPhone()));
                }
                if (null != userDetails.getLocation()) {
                    mEditor.putString(PREF_USER_LOCATION, userDetails.getLocation());
                }
                if (null != userDetails.getAboutMe()) {
                    mEditor.putString(PREF_USER_ABOUT_ME, userDetails.getAboutMe());
                }
                if (null != userDetails.getSenderId()) {
                    mEditor.putString(PREF_SENDER_ID, userDetails.getSenderId());
                }
                mEditor.putBoolean(IS_LOGGED_IN, true);
            }

            if (null != roomDetails) {
                mEditor.putString(PREF_ROOM_ID, String.valueOf(roomDetails.getRoomId()));
                mEditor.putString(PREF_ROOM_ALIAS, roomDetails.getRoomAlias());
                mEditor.putString(PREF_NO_OF_MEMBERS, String.valueOf(roomDetails.getNoOfPersons()));
            }

            if (null != roomStats) {
                mEditor.putString(PREF_RENT_MARGIN, String.valueOf(roomStats.getRentMargin()));
                mEditor.putString(PREF_MAID_MARGIN, String.valueOf(roomStats.getMaidMargin()));
                mEditor.putString(PREF_ELECTRICITY_MARGIN,
                        String.valueOf(roomStats.getElectricityMargin()));
                mEditor.putString(PREF_MISCELLANEOUS_MARGIN,
                        String.valueOf(roomStats.getMiscellaneousMargin()));
                mEditor.putString(PREF_MONTH_YEAR, roomStats.getMonthYear());
                mEditor.putString(PREF_TOTAL_MARGIN,
                        String.valueOf(roomStats.getRentMargin() + roomStats
                                .getMaidMargin()
                                + roomStats
                                .getMiscellaneousMargin() + roomStats
                                .getElectricityMargin()));
                mEditor.putBoolean(PREF_SETUP_COMPLETED, true);
            }
        }
        if (isGoogleFBLogin) {
            mEditor.putBoolean(IS_GOOGLE_FB_LOGIN, true);
        }
        boolean isCommitted = mEditor.commit();
        return true;

    }

    /**
     * @param array
     * @param element
     * @return
     */
    public static String[] addElement(String[] array, String element) {
        List<String> result = new ArrayList<>();
        for (String s : array) {
            result.add(s);
        }
        result.add(element);
        return result.toArray(new String[array.length + 1]);
    }

    /**
     * Takes string value of QueryParam from projectionList and creates a string array.
     */
    public static String[] listToProjection(List<QueryParam> projectionList) {

        List<String> projectionStringList = new ArrayList<>();
        String projection[] = null;

        if (null != projectionList && projectionList.size() > 0) {

            for (QueryParam query : projectionList) {
                projectionStringList.add(query.toString());
            }
            projection = new String[projectionStringList.size()];
            projectionStringList.toArray(projection);
        }

        return projection;
    }

    /**
     * @param mContext
     */
    public static void setupAlarm(Context mContext) {

        Intent intent = new Intent(mContext, RoomiesReceiver.class);
        intent.setAction(RoomiesReceiver.ROOMIES_ALARM);
        if (null == PendingIntent.getBroadcast(mContext, 1001, intent,
                PendingIntent.FLAG_NO_CREATE)) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    mContext, 1001, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH,
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 1);
            calendar.set(Calendar.SECOND, 0);
            AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public static void setupTextInputLayout(final TextInputLayout textInputLayout,
            final EditText editText, final String msg) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textInputLayout.isErrorEnabled()) {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (null == s || s.toString().trim()
                        .length() == 0) {
                    textInputLayout.setError(msg);
                }
            }
        });

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static SpannableString generateCenterSpannableText(Context mContext, int percent,
            boolean isSpent) {

        String action = "left";
        if (isSpent) {
            action = "spent";
        }
        SpannableString s = new SpannableString(
                (int) percent + "%\n" + action + " from \nTotal Budget");
        s.setSpan(new RelativeSizeSpan(1.8f), 0, s.length() - 24, 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length() - 24, 0);
        s.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.primary_home)),
                0, s.length() - 24, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), s.length() - 24, s.length() - 12, 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), s.length() - 12, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(Color.BLACK), s.length() - 24, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(.6f), s.length() - 24, s.length() - 12, 0);
        s.setSpan(new RelativeSizeSpan(1.0f), s.length() - 12, s.length(), 0);
        return s;
    }
}
