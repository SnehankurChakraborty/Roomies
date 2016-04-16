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
import android.content.SharedPreferences;

import com.phaseii.rxm.roomies.database.model.RoomDetails;
import com.phaseii.rxm.roomies.database.model.RoomStats;
import com.phaseii.rxm.roomies.database.model.RoomUserStatData;
import com.phaseii.rxm.roomies.database.model.UserDetails;

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
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_ALIAS;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_ID;

/**
 * Created by Snehankur on 1/23/2016.
 */
public class PrefUtils {

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
                mEditor.putString(PREF_USER_ID, String.valueOf(userDetails.getUserId()));
                mEditor.putString(PREF_USERNAME, userDetails.getUsername());
                mEditor.putString(PREF_USER_ALIAS, userDetails.getUserAlias());
                mEditor.putString(PREF_SENDER_ID, userDetails.getSenderId());
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
}
