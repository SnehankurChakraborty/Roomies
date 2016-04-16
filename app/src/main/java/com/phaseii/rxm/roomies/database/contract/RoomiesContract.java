package com.phaseii.rxm.roomies.database.contract;

import android.provider.BaseColumns;

/**
 * Created by Snehankur on 3/3/2015.
 */
public class RoomiesContract {

    public static final String FOREIGN_KEY = "FOREIGN KEY (";
    public static final String REFERENCES = "REFERENCES ";
    public static final String UNIQUE_KEY = " UNIQUE";
    public static final String DATABASE_NAME = "Roomies.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TEXT_TYPE = " TEXT";
    public static final String DATETIME_TYPE = " INTEGER";
    public static final String COMMA_SEP = ", ";
    public static final String NOT_NULL = " NOT NULL";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String DEFAULT0 = " DEFAULT 0";
    public static final String TOTAL = "total";
    public static final String INTEGER_PRIMARY_KEY = " INTEGER PRIMARY KEY";
    public static final String INTEGER_PRIMARY_KEY_AUTOINCREMENT = " INTEGER PRIMARY KEY " +
            "AUTOINCREMENT";
    public static final String VIEW_NAME = "room_user_stat_view";
    /**
     * ****** R O O M  U S E R  S T A T  V I E W **********
     */

    public static final String SQL_CREATE_VIEW = "CREATE VIEW IF NOT EXISTS " +
            VIEW_NAME + " AS SELECT DISTINCT " +
            "USER." + UserDetails.USER_USERNAME + COMMA_SEP +
            "USER." + UserDetails.USER_USER_ALIAS + COMMA_SEP +
            "USER." + UserDetails.USER_SENDER_ID + COMMA_SEP +
            "ROOM." + RoomDetails.DETAILS_ROOM_ID + COMMA_SEP +
            "ROOM." + RoomDetails.DETAILS_ROOM_ALIAS + COMMA_SEP +
            "ROOM." + RoomDetails.DETAILS_NO_OF_PERSONS + COMMA_SEP +
            "STATS." + RoomStats.STATS_MONTH_YEAR + COMMA_SEP +
            "STATS." + RoomStats.STATS_RENT_MARGIN + COMMA_SEP +
            "STATS." + RoomStats.STATS_MAID_MARGIN + COMMA_SEP +
            "STATS." + RoomStats.STATS_ELECTRICITY_MARGIN + COMMA_SEP +
            "STATS." + RoomStats.STATS_MISCELLANEOUS_MARGIN + COMMA_SEP +
            "STATS." + RoomStats.STATS_RENT_SPENT + COMMA_SEP +
            "STATS." + RoomStats.STATS_MAID_SPENT + COMMA_SEP +
            "STATS." + RoomStats.STATS_ELECTRICITY_SPENT + COMMA_SEP +
            "STATS." + RoomStats.STATS_MISCELLANEOUS_SPENT + COMMA_SEP +
            " (STATS." + RoomStats.STATS_RENT_SPENT + "+ STATS." + RoomStats.STATS_MAID_SPENT +
            "+ STATS." + RoomStats.STATS_ELECTRICITY_SPENT + "+ STATS." + RoomStats
            .STATS_MISCELLANEOUS_SPENT + ") AS " + TOTAL +
            " FROM " + UserDetails.USER_TABLE_NAME + " USER " +
            "JOIN " + RoomUserMap.ROOM_USER_MAP_TABLE_NAME + " MAP " +
            "ON ( MAP." + RoomUserMap.ROOM_USER_USER_ID + " = USER." + UserDetails._ID + " ) " +
            "JOIN " + RoomDetails.DETAILS_TABLE_NAME + " ROOM " +
            "ON ( ROOM." + RoomDetails.DETAILS_ROOM_ID + " = MAP." +
            RoomUserMap.ROOM_USER_ROOM_ID + " ) " +
            "JOIN " + RoomStats.STATS_TABLE_NAME + " STATS " +
            "ON ( STATS." + RoomStats.STATS_ROOM_ID + " = ROOM." + RoomDetails.DETAILS_ROOM_ID + ");";
    public static final String SQL_DROP_VIEW = "DROP VIEW " + VIEW_NAME;
    public static String INTEGER_UNIQUE_KEY_AUTOINCREMENT = "INTEGER UNIQUE KEY AUTOINCREMENT";

    /**
     * empty constructor to prevent someone from accidentally instantiating the contract class
     */
    private RoomiesContract() {
    }

    /**
     * ******* U S E R   D E T A I L S ****************
     */
    public static final class UserDetails implements BaseColumns {

        public static final String USER_TABLE_NAME = "user_details";
        public static final String USER_USERNAME = "username";
        public static final String USER_USER_ALIAS = "user_alias";
        public static final String USER_PHONE = "phone";
        public static final String USER_LOCATION = "location";
        public static final String USER_ABOUT_ME = "about_me";
        public static final String USER_SENDER_ID = "sender_id";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + USER_TABLE_NAME +
                " (" + _ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
                USER_USERNAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                USER_USER_ALIAS + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                USER_PHONE + INTEGER_TYPE + COMMA_SEP +
                USER_LOCATION + TEXT_TYPE + COMMA_SEP +
                USER_ABOUT_ME + TEXT_TYPE + COMMA_SEP +
                USER_SENDER_ID + TEXT_TYPE + " )";

        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;
    }

    /**
     * ****************** R O O M  E X P E N S E S **************************
     */

    public static final class RoomExpenses implements BaseColumns {

        public static final String EXPENSE_TABLE_NAME = "room_expenses";
        public static final String RENT_SPENT_TRIGGER_NAME = "UPDATE_STAT_RENT_SPENT_TRG";
        public static final String MAID_SPENT_TRIGGER_NAME = "UPDATE_STAT_MAID_SPENT_TRG";
        public static final String ELEC_SPENT_TRIGGER_NAME = "UPDATE_STAT_ELEC_SPENT_TRG";
        public static final String MISC_SPENT_TRIGGER_NAME = "UPDATE_STAT_MISC_SPENT_TRG";

        public static final String EXPENSE_ROOM_ID = "room_id";
        public static final String EXPENSE_USER_ID = "user_id";
        public static final String EXPENSE_CATEGORY = "expense_category";
        public static final String EXPENSE_SUBCATEGORY = "expense_subcategory";
        public static final String EXPENSE_DESCRIPTION = "expense_desc";
        public static final String EXPENSE_AMOUNT = "expense_amount";
        public static final String EXPENSE_QUANTITY = "expense_quantity";
        public static final String EXPENSE_DATE = "expense_date";
        public static final String EXPENSE_MONTH_YEAR = "month_year";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + EXPENSE_TABLE_NAME + " ("
                + _ID + INTEGER_PRIMARY_KEY + NOT_NULL + COMMA_SEP +
                EXPENSE_ROOM_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                EXPENSE_USER_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                EXPENSE_CATEGORY + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                EXPENSE_SUBCATEGORY + TEXT_TYPE + COMMA_SEP +
                EXPENSE_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                EXPENSE_QUANTITY + INTEGER_TYPE + COMMA_SEP +
                EXPENSE_AMOUNT + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                EXPENSE_MONTH_YEAR + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                EXPENSE_DATE + DATETIME_TYPE + NOT_NULL + COMMA_SEP +
                FOREIGN_KEY + EXPENSE_ROOM_ID + ") " +
                "REFERENCES " + RoomDetails.DETAILS_TABLE_NAME +
                "(" + RoomDetails._ID + ") " + COMMA_SEP +
                FOREIGN_KEY + EXPENSE_USER_ID + ") " +
                "REFERENCES " + UserDetails.USER_TABLE_NAME +
                "(" + UserDetails._ID + ") " + " )";
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + EXPENSE_TABLE_NAME;

        public static final String SQL_CREATE_RENT_SPENT_TRIGGER = "CREATE TRIGGER " +
                RENT_SPENT_TRIGGER_NAME
                + " AFTER INSERT ON " + EXPENSE_TABLE_NAME + " WHEN new." + EXPENSE_CATEGORY + " = " +
                "'rent'" +
                " BEGIN" +
                " UPDATE " + RoomStats.STATS_TABLE_NAME + " SET " + RoomStats.STATS_RENT_SPENT
                + " = " + RoomStats.STATS_RENT_SPENT + "+new." + EXPENSE_AMOUNT +
                " WHERE " + RoomStats.STATS_ROOM_ID + " = new." + EXPENSE_ROOM_ID +
                " AND " + RoomStats.STATS_MONTH_YEAR + " = new." + EXPENSE_MONTH_YEAR + "; END;";

        public static final String SQL_DROP_RENT_SPENT_TRIGGER = "DROP TRIGGER " +
                RENT_SPENT_TRIGGER_NAME;

        public static final String SQL_CREATE_MAID_SPENT_TRIGGER = "CREATE TRIGGER " +
                MAID_SPENT_TRIGGER_NAME
                + " AFTER INSERT ON " + EXPENSE_TABLE_NAME + " WHEN new." + EXPENSE_CATEGORY + " = " +
                "'maid'" +
                " BEGIN" +
                " UPDATE " + RoomStats.STATS_TABLE_NAME + " SET " + RoomStats.STATS_MAID_SPENT
                + " = " + RoomStats.STATS_MAID_SPENT + "+new." + EXPENSE_AMOUNT +
                " WHERE " + RoomStats.STATS_ROOM_ID + " = new." + EXPENSE_ROOM_ID +
                " AND " + RoomStats.STATS_MONTH_YEAR + " = new." + EXPENSE_MONTH_YEAR + "; END;";

        public static final String SQL_DROP_MAID_SPENT_TRIGGER = "DROP TRIGGER " +
                MAID_SPENT_TRIGGER_NAME;

        public static final String SQL_CREATE_ELEC_SPENT_TRIGGER = "CREATE TRIGGER " +
                ELEC_SPENT_TRIGGER_NAME
                + " AFTER INSERT ON " + EXPENSE_TABLE_NAME + " WHEN new." + EXPENSE_CATEGORY + " = " +
                "'electricity'" +
                " BEGIN" +
                " UPDATE " + RoomStats.STATS_TABLE_NAME + " SET " + RoomStats.STATS_ELECTRICITY_SPENT
                + " = " + RoomStats.STATS_ELECTRICITY_SPENT + "+new." + EXPENSE_AMOUNT +
                " WHERE " + RoomStats.STATS_ROOM_ID + " = new." + EXPENSE_ROOM_ID +
                " AND " + RoomStats.STATS_MONTH_YEAR + " = new." + EXPENSE_MONTH_YEAR + "; END;";

        public static final String SQL_DROP_ELEC_SPENT_TRIGGER = "DROP TRIGGER " +
                ELEC_SPENT_TRIGGER_NAME;

        public static final String SQL_CREATE_MISC_SPENT_TRIGGER = "CREATE TRIGGER " +
                MISC_SPENT_TRIGGER_NAME
                + " AFTER INSERT ON " + EXPENSE_TABLE_NAME + " WHEN new." + EXPENSE_CATEGORY + " = " +
                "'miscellaneous'" +
                " BEGIN" +
                " UPDATE " + RoomStats.STATS_TABLE_NAME + " SET " + RoomStats.STATS_MISCELLANEOUS_SPENT
                + " = " + RoomStats.STATS_MISCELLANEOUS_SPENT + "+new." + EXPENSE_AMOUNT +
                " WHERE " + RoomStats.STATS_ROOM_ID + " = new." + EXPENSE_ROOM_ID +
                " AND " + RoomStats.STATS_MONTH_YEAR + " = new." + EXPENSE_MONTH_YEAR + "; END;";

        public static final String SQL_DROP_MISC_SPENT_TRIGGER = "DROP TRIGGER " +
                MISC_SPENT_TRIGGER_NAME;
    }

    /**
     * ***************** R O O M  D E T A I L S *****************
     */

    public static final class RoomDetails implements BaseColumns {
        public static final String DETAILS_TABLE_NAME = "room_details";
        public static final String DETAILS_ROOM_ID = "room_id";
        public static final String DETAILS_ROOM_ALIAS = "room_alias";
        public static final String DETAILS_NO_OF_PERSONS = "no_of_persons";
        public static final String ROOM_SETUP_COMPLETED = "setup_completed";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DETAILS_TABLE_NAME +
                " (" + DETAILS_ROOM_ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
                DETAILS_ROOM_ALIAS + TEXT_TYPE + COMMA_SEP +
                DETAILS_NO_OF_PERSONS + INTEGER_TYPE + " )";
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DETAILS_TABLE_NAME;
    }

    /**
     * *************** R O O M  U S E R   M A P ***************
     */

    public static final class RoomUserMap implements BaseColumns {
        public static final String ROOM_USER_MAP_TABLE_NAME = "room_user";

        public static final String ROOM_USER_ROOM_ID = "room_id";
        public static final String ROOM_USER_USER_ID = "user_id";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + ROOM_USER_MAP_TABLE_NAME +
                " (" + _ID + INTEGER_PRIMARY_KEY + COMMA_SEP +
                ROOM_USER_ROOM_ID + INTEGER_TYPE + COMMA_SEP +
                ROOM_USER_USER_ID + INTEGER_TYPE + COMMA_SEP +
                FOREIGN_KEY + ROOM_USER_ROOM_ID + ") " + REFERENCES + RoomDetails
                .DETAILS_TABLE_NAME + "(" + RoomDetails.DETAILS_ROOM_ID + ")" + COMMA_SEP +
                FOREIGN_KEY + ROOM_USER_USER_ID + ") " + REFERENCES + UserDetails
                .USER_TABLE_NAME + "(" + UserDetails._ID + ")" + " )";
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + ROOM_USER_MAP_TABLE_NAME;
    }

    /**
     * *********************** R O O M   S T A T I S T I C S ************************
     */
    public static final class RoomStats implements BaseColumns {
        public static final String STATS_TABLE_NAME = "room_stats";
        public static final String STATS_ROOM_ID = "room_id";
        public static final String STATS_MONTH_YEAR = "month_year";
        public static final String STATS_RENT_MARGIN = "rent_margin";
        public static final String STATS_RENT_SPENT = "rent_spent";
        public static final String STATS_MAID_MARGIN = "maid_margin";
        public static final String STATS_MAID_SPENT = "maid_spent";
        public static final String STATS_ELECTRICITY_MARGIN = "elec_margin";
        public static final String STATS_ELECTRICITY_SPENT = "elec_spent";
        public static final String STATS_MISCELLANEOUS_MARGIN = "misc_margin";
        public static final String STATS_MISCELLANEOUS_SPENT = "misc_spent";
        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + STATS_TABLE_NAME + " ("
                + _ID + INTEGER_PRIMARY_KEY + NOT_NULL + COMMA_SEP +
                STATS_ROOM_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                STATS_RENT_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                STATS_MAID_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                STATS_ELECTRICITY_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                STATS_MISCELLANEOUS_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                STATS_RENT_SPENT + INTEGER_TYPE + NOT_NULL + DEFAULT0 + COMMA_SEP +
                STATS_MAID_SPENT + INTEGER_TYPE + NOT_NULL + DEFAULT0 + COMMA_SEP +
                STATS_ELECTRICITY_SPENT + INTEGER_TYPE + NOT_NULL + DEFAULT0 + COMMA_SEP +
                STATS_MISCELLANEOUS_SPENT + INTEGER_TYPE + NOT_NULL + DEFAULT0 + COMMA_SEP +
                STATS_MONTH_YEAR + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                "FOREIGN KEY (" + STATS_ROOM_ID + ") " +
                "REFERENCES " + RoomDetails.DETAILS_TABLE_NAME +
                "(" + RoomDetails.DETAILS_ROOM_ID + ") " + " )";
        public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + STATS_TABLE_NAME;
        private static final String TRIGGER_NAME = "SETUP_COMPLETED_TRIGGER";


    }
}
