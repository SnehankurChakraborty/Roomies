package com.phaseii.rxm.roomies.helper;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Constant class for RoomXpnseMngr
 */
public final class RoomiesConstants {

    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String EMAIL_ID = "EMAIL_ID";
    public static final String PHONE_NO = "PHONE_NO";
    public static final String BLANK_USERNAME_PASSWORD = "Please enter both username and password";
    public static final String INVALID_USER_CREDENTIALS = "Invalid User Credentials";
    public static final String PLEASE_SIGN_UP_TO_USE_ROOMIES = "Please signup to use Roomies";
    public static final String PRESS_BACK_AGAIN_TO_EXIT = "Press back again to exit";
    public static final String ENTER_ALL_FEILDS = "Please enter all the fields ";
    public static final String APP_ERROR = "Application Error: Please reinstall from play store";
    public static final long DELAY_MILLIS = 1000;
    public static final String TAG_ROOM_INFO = "room_info";
    public static final String TAG_ROOM_EXPENSE = "room_expense";
    public static final String ROOM_RENT = "room_rent";
    public static final String ROOM_MAID = "room_maid";
    public static final String ROOM_ELECTRICITY = "room_electricity";
    public static final String ROOM_MISC = "room_misc";
    public static final String ROOM_NAME = "room_name";
    public static final String ROOM_NO_OF_MEMBERS = "no_of_members";
    public static final String RENT = "RENT";
    public static final String RENT_MARGIN = "RENT_MARGIN";
    public static final String MAID = "MAID";
    public static final String MAID_MARGIN = "MAID_MARGIN";
    public static final String ELECTRICITY = "ELECTRICITY";
    public static final String ELECTRICITY_MARGIN = "ELECTRICITY_MARGIN";
    public static final String MISC = "MISC";
    public static final String MISC_MARGIN = "MISC_MARGIN";
    public static final String SPENT = "SPENT";
    public static final String TOTAL = "TOTAL";
    public static final String ROOM_INFO_FILE_KEY = "ROOM_INFO";
    public static final String ROOM_BUDGET_FILE_KEY = "ROOMIES_BUDGET";
    public static final String ROOM_EXPENDITURE_FILE_KEY = "ROOMIES_EXPENDITURE";
    public static final String ROOM_ALIAS = "ROOM_ALIAS";
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String NAME = "NAME";
    public static final String TOTAL_MARGIN = "TOTAL_MARGIN";
    public static final String IS_RENT_PAID = "isRentPaid";
    public static final String IS_MAID_PAID = "isMaidPaid";
    public static final String IS_ELEC_PAID = "isElecPaid";
    public static final String HOME_FRAGMENT = "HOME_FRAGMENT";
    public static final String DASHBOARD_FRAGMENT = "DASHBOARD_FRAGMENT";
    public static final String PROFILE_FRAGMENT = "PROFILE_FRAGMENT";
    public static final String TREND_FRAGMENT = "TREND_FRAGMENT";
    public static final String SAVINGS_FRAGMENT = "SAVINGS_FRAGMENT";
    public static final String MISCELLANEOUS = "MISCELLANEOUS";
    public static final String BILLS = "BILLS";
    public static final String GROCERY = "GROCERY";
    public static final String VEGETABLES = "VEGETABLES";
    public static final String OTHERS = "OTHERS";
    public static final int REQUEST_CODE = 1;
    public static final String DUMMY_PASSWORD = "dummy";
    public static final String IS_GOOGLE_FB_LOGIN = "IS_GOOGLE_FB_LOGIN";
    public static final String IS_SETUP_COMPLETED = "IS_SETUP_COMPLETED";
    public static final String PREVIOUS_MONTH = "PREVIOUS_MONTH";
    public static final String SENT_TOKEN_TO_SERVER = "sent_token_to_server";
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    /**
     * ROOMIES PREFERENCES KEYS
     */
    public static final String PREF_ROOMIES_KEY = "ROOMIES_PREFERENCES_KEY";
    public static final String PREF_USER_ALIAS = "USER_ALIAS";
    public static final String PREF_USER_ID = "USER_ID";
    public static final String PREF_USERNAME = "USERNAME";
    public static final String PREF_SENDER_ID = "SENDER_ID";
    public static final String PREF_SETUP_COMPLETED = "IS_SETUP_COMPLETED";
    public static final String PREF_ROOM_ALIAS = "ROOM_ALIAS";
    public static final String PREF_ROOM_ID = "ROOM_ID";
    public static final String PREF_NO_OF_MEMBERS = "NO_OF_MEMBERS";
    public static final String PREF_MONTH_YEAR = "MONTH_YEAR";
    public static final String PREF_RENT_MARGIN = "RENT_MARGIN";
    public static final String PREF_MAID_MARGIN = "MAID_MARGIN";
    public static final String PREF_ELECTRICITY_MARGIN = "ELECTRICITY_MARGIN";
    public static final String PREF_MISCELLANEOUS_MARGIN = "MISCELLANEOUS_MARGIN";
    public static final String PREF_RENT_SPENT = "RENT_SPENT";
    public static final String PREF_MAID_SPENT = "MAID_SPENT";
    public static final String PREF_ELECTRICITY_SPENT = "ELECTRICITY_SPENT";
    public static final String PREF_MISCELLANEOUS_SPENT = "MISCELLANEOUS_SPENT";
    /*
    static feilds
     */
    public static GoogleApiClient googleApiClient;
    public static String token;

    public static String getToken() {
        return RoomiesConstants.token;
    }

    public static void setToken(String token) {
        RoomiesConstants.token = token;
    }
}

