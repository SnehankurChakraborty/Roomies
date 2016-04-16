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

import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.phaseii.rxm.roomies.utils.Constants.DATE_FORMAT;

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
        return new SimpleDateFormat(DATE_FORMAT, Locale.US).format(date);
    }

    public static String dateToStringFormatter(int year, int month, int day, int hour,
            int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minutes);
        return new SimpleDateFormat(DATE_FORMAT, Locale.US).format(calendar.getTimeInMillis());
    }

    public static List<String> getLastTwoMonths() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

        List<String> months = new ArrayList<>();
        months.add(new DateFormatSymbols().getMonths()[currentMonth] + String.valueOf(
                currentYear));
        if ((currentMonth - 1) >= 0) {
            months.add(new DateFormatSymbols().getMonths()[currentMonth - 1] + String.valueOf(
                    currentYear));
            if ((currentMonth - 2) >= 0) {
                months.add(new DateFormatSymbols().getMonths()[currentMonth - 2] + String.valueOf(
                        currentYear));
            } else {
                months.add(new DateFormatSymbols().getMonths()[11] + String.valueOf(
                        currentYear - 1));
            }
        } else {
            months.add(new DateFormatSymbols().getMonths()[11] + String.valueOf(currentYear -
                    1));
            months.add(new DateFormatSymbols().getMonths()[10] + String.valueOf(
                    currentYear - 1));
        }

        return months;
    }

    public static long getMonthStartDate() {
        Calendar calendar = Calendar.getInstance();
        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return startOfMonth.getTimeInMillis();
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinutes() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }
}
