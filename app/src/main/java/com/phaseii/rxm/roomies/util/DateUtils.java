package com.phaseii.rxm.roomies.util;

import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.DATE_FORMAT;

/**
 * Created by Snehankur on 11/2/2015.
 */
public class DateUtils {
    private static final String TAG = "DateUtils";

    /**
     * Returns the current month year in format 'April2015'
     */
    public static String getCurrentMonthYear() {

        Calendar calendar = Calendar.getInstance();
        String month = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)];
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return month + year;
    }

    /**
     * Returns the next month year in format 'April2015'
     */
    public static String getNextMonthYear() {

        Calendar calendar = Calendar.getInstance();
        String month = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH) + 1];
        String year = String.valueOf(
                month.equals("January") ? calendar.get(Calendar.YEAR) + 1 : calendar.get(
                        Calendar.YEAR));
        return month + year;
    }

    /**
     * @param dateTime
     * @return
     */
    public static Date stringToDateParser(String dateTime) {
        Date date = null;
        try {
            date = new SimpleDateFormat(DATE_FORMAT).parse(dateTime);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return date;
    }

    /**
     * @param date
     * @return
     */
    public static String dateToStringFormatter(Date date) {
        String dateString = new SimpleDateFormat(DATE_FORMAT).format(date);
        return dateString;
    }

}
