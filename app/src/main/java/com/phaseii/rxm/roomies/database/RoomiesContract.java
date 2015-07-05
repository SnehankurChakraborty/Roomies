package com.phaseii.rxm.roomies.database;

import android.provider.BaseColumns;

/**
 * Created by Snehankur on 3/3/2015.
 */
public class RoomiesContract {


	public static final String FOREIGN_KEY = "FOREIGN KEY (";
	public static final String REFERENCES = "REFERENCES ";
	public static final String UNIQUE_KEY = " UNIQUE";

	/*empty constructor to prevent someone from accidentally instantiating the contract class,*/
	private RoomiesContract() {
	}

	public static final String DATABASE_NAME = "Roomies.db";
	public static final int DATABASE_VERSION = 1;
	public static final String TEXT_TYPE = " TEXT";
	public static final String DATETIME_TYPE = " INTEGER";
	public static final String COMMA_SEP = ",";
	public static final String NOT_NULL = " NOT NULL";
	public static final String INTEGER_TYPE = " INTEGER";
	public static final String INTEGER_PRIMARY_KEY_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT";

	/**
	 * ***********************************************************************************
	 * U S E R   C R E D E N T I A L S
	 * *************************************************************************************
	 */

	public static abstract class UserCredentials implements BaseColumns {
		public static final String TABLE_NAME = "user_credentials";
		public static final String COLUMN_PERSON_ID = "person_id";
		public static final String COLUMN_NAME_EMAIL_ID = "email_id";
		public static final String COLUMN_NAME_USERNAME = "username";
		public static final String COLUMN_NAME_PASSWORD = "password";
		public static final String COLUMN_SETUP_COMPLETED = "setup_completed";
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME +
				" (" + _ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP +
				/*COLUMN_PERSON_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +*/
				COLUMN_NAME_USERNAME + TEXT_TYPE + UNIQUE_KEY + COMMA_SEP +
				COLUMN_NAME_EMAIL_ID + TEXT_TYPE + COMMA_SEP +
				COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
				COLUMN_SETUP_COMPLETED + TEXT_TYPE +/*COMMA_SEP +
				"FOREIGN KEY (" + COLUMN_PERSON_ID + ") " +
				"REFERENCES " + Person.EXPENSE_TABLE_NAME + "(" + Person._ID + ") "+*/" )";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
	}

	public static final class UserDetails implements BaseColumns {
		public static final String USER_TABLE_NAME = "user_details";
		public static final String USER_USERNAME = "username";
		public static final String USER_PASSWORD = "password";
		public static final String USER_USER_ALIAS = "user_alias";
		public static final String USER_SENDER_ID = "sender_id";
		public static final String USER_ROOM_ID = "room_id";
		public static final String USER_SETUP_COMPLETED = "setup_completed";
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + USER_TABLE_NAME +
				" (" + _ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP +
				USER_USERNAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
				USER_PASSWORD + TEXT_TYPE + COMMA_SEP +
				USER_USER_ALIAS + TEXT_TYPE + NOT_NULL + COMMA_SEP +
				USER_SENDER_ID + TEXT_TYPE + COMMA_SEP +
				USER_ROOM_ID + TEXT_TYPE + NOT_NULL + COMMA_SEP +
				USER_SETUP_COMPLETED + TEXT_TYPE + NOT_NULL + COMMA_SEP +
				"FOREIGN KEY (" + USER_ROOM_ID + ") " +
				"REFERENCES " + RoomDetails.DETAILS_TABLE_NAME + "(" + RoomDetails._ID + ") " + " )";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;

	}

	/**
	 * ************************************************************************************
	 * P E R S O N   D E T A I L S
	 * *************************************************************************************
	 */

	public static abstract class Person implements BaseColumns {
		public static final String TABLE_NAME = "person";
		public static final String COLUMN_ROOM_ID = "room_id";
		public static final String COLUMN_NAME_PHONE = "phone";
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME +
				" (" + _ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT +
				COLUMN_ROOM_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_NAME_PHONE + INTEGER_TYPE + COMMA_SEP +
				"FOREIGN KEY (" + COLUMN_ROOM_ID + ") " +
				"REFERENCES " + Room.TABLE_NAME + "(" + Room._ID + ") )";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
	}

	/**
	 * ************************************************************************************
	 * R O O M   D E T A I L S
	 * *************************************************************************************
	 */
	public static abstract class Room implements BaseColumns {
		public static final String TABLE_NAME = "room";
		public static final String COLUMN_ROOM_ALIAS = "room_alias";
		public static final String COLUMN_NO_OF_PERSONS = "no_of_persons";
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME +
				" (" + _ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT +
				_ID + INTEGER_TYPE + COMMA_SEP +
				COLUMN_ROOM_ALIAS + TEXT_TYPE + COMMA_SEP +
				COLUMN_NO_OF_PERSONS + INTEGER_TYPE + COMMA_SEP + " )";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
	}

	public static final class RoomDetails implements BaseColumns {
		public static final String DETAILS_TABLE_NAME = "room_details";
		public static final String DETAILS_ROOM_ALIAS = "room_alias";
		public static final String DETAILS_NO_OF_PERSONS = "no_of_persons";
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DETAILS_TABLE_NAME +
				" (" + _ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT +
				DETAILS_ROOM_ALIAS + TEXT_TYPE + COMMA_SEP +
				DETAILS_NO_OF_PERSONS + INTEGER_TYPE + COMMA_SEP + " )";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DETAILS_TABLE_NAME;
	}


	/**
	 * ************************************************************************************
	 * P E R S O N A L  E X P E N S E S
	 * *************************************************************************************
	 */
	public static abstract class PersonalExpenses implements BaseColumns {
		public static final String TABLE_NAME = "personal_expenses";
		public static final String COLUMN_NAME_PERSON_ID = "person_id";
		public static final String COLUMN_NAME_MONTH = "month";
		public static final String COLUMN_NAME_YEAR = "year";
		public static final String COLUMN_NAME_RENT = "rent";
		public static final String COLUMN_NAME_MAID = "maid";
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " " +
				"(" + _ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT +
				COLUMN_NAME_PERSON_ID + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_NAME_MONTH + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_NAME_YEAR + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_NAME_RENT + INTEGER_TYPE + COMMA_SEP +
				COLUMN_NAME_MAID + INTEGER_TYPE + COMMA_SEP +
				FOREIGN_KEY + COLUMN_NAME_PERSON_ID + ") " +
				REFERENCES + Person.TABLE_NAME + "(" + Person._ID + ") )";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
	}


	/**
	 * ************************************************************************************
	 * R O O M   E X P E N S E S
	 * *************************************************************************************
	 */
	public static abstract class Room_Expenses implements BaseColumns {
		public static final String TABLE_NAME = "room_expenses";
		public static final String TRIGGER_NAME = "UPDATE_COLUMN_TOTAL_TRG";
		public static final String COLUMN_NO_OF_MEMBERS = "no_of_members";
		public static final String COLUMN_MONTH = "month";
		public static final String COLUMN_RENT = "rent";
		public static final String COLUMN_RENT_MARGIN = "rent_margin";
		public static final String COLUMN_MAID = "maid";
		public static final String COLUMN_MAID_MARGIN = "maid_margin";
		public static final String COLUMN_ELECTRICITY = "elec";
		public static final String COLUMN_ELECTRICITY_MARGIN = "elec_margin";
		public static final String COLUMN_MISCELLANEOUS = "misc";
		public static final String COLUMN_MISCELLANEOUS_MARGIN = "misc_margin";
		public static final String COLUMN_TOTAL = "total";
		public static final String COLUMN_USERNAME = "username";
		public static final String COLUMN_ROOM_ALIAS = "room_alias";

		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " ("
				+ _ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + COMMA_SEP +
				COLUMN_USERNAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_ROOM_ALIAS + TEXT_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_NO_OF_MEMBERS + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_MONTH + TEXT_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_RENT + INTEGER_TYPE + COMMA_SEP +
				COLUMN_RENT_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_MAID + INTEGER_TYPE + COMMA_SEP +
				COLUMN_MAID_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_ELECTRICITY + INTEGER_TYPE + COMMA_SEP +
				COLUMN_ELECTRICITY_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_MISCELLANEOUS + INTEGER_TYPE + COMMA_SEP +
				COLUMN_MISCELLANEOUS_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				COLUMN_TOTAL + INTEGER_TYPE + NOT_NULL/*+ COMMA_SEP +
				FOREIGN_KEY + EXPENSE_ROOM_ID + ") " +
				REFERENCES + Room.EXPENSE_TABLE_NAME + " (" + Room._ID + ")"*/ + " )";
		public static final String SQL_CREATE_TRIGGER = "CREATE TRIGGER " + TRIGGER_NAME
				+ " BEFORE UPDATE ON " + TABLE_NAME +
				" BEGIN" +
				" UPDATE " + TABLE_NAME + " SET " + COLUMN_TOTAL + " = " + "new." + COLUMN_RENT + "+ " +
				"new." + COLUMN_MAID + "+ new." + COLUMN_MISCELLANEOUS + "+ new." + COLUMN_ELECTRICITY +
				" WHERE _ID = new._ID; END;";

		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
		public static final String SQL_DROP_TRIGGER = "DROP TRIGGER " + TRIGGER_NAME;
	}

	/**
	 * ************************************************************************************
	 * M I S C E L L A N E O U S   E X P E N S E S
	 * *************************************************************************************
	 */
	public static abstract class Misc_Expenses implements BaseColumns {
		public static final String TABLE_NAME = "misc_expenses";
		public static final String TRIGGER_NAME = "UPDATE_TOTAL_MISC_TRG ";
		public static final String COLUMN_DESCRIPTION = "misc_desc";
		public static final String COLUMN_TYPE = "misc_type";
		public static final String COLUMN_AMOUNT = "misc_amount";
		public static final String COLUMN_QUANTITY = "misc_quantity";
		public static final String COLUMN_DATE = "misc_date";
		public static final String COLUMN_MONTH = "month";
		public static final String COLUMN_USERNAME = "username";
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " ("
				+ _ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + NOT_NULL + COMMA_SEP +
				COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
				COLUMN_TYPE + TEXT_TYPE + COMMA_SEP +
				COLUMN_QUANTITY + INTEGER_TYPE + COMMA_SEP +
				COLUMN_AMOUNT + INTEGER_TYPE + COMMA_SEP +
				COLUMN_MONTH + TEXT_TYPE + COMMA_SEP +
				COLUMN_USERNAME + TEXT_TYPE + COMMA_SEP +
				COLUMN_DATE + DATETIME_TYPE + NOT_NULL + " )";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
		public static final String SQL_CREATE_TRIGGER = "CREATE TRIGGER " + TRIGGER_NAME +
				"AFTER INSERT ON " + TABLE_NAME + " BEGIN " +
				"UPDATE " + Room_Expenses.TABLE_NAME + " SET " + Room_Expenses
				.COLUMN_MISCELLANEOUS + "= (SELECT SUM(" + COLUMN_AMOUNT + ") FROM " +
				TABLE_NAME + " WHERE " + TABLE_NAME + "." + COLUMN_MONTH + " = new."
				+ COLUMN_MONTH + " AND " + TABLE_NAME + "." + COLUMN_USERNAME + " = new." +
				COLUMN_USERNAME + " )" + " WHERE " + Room_Expenses.COLUMN_MONTH +
				" = new." + COLUMN_MONTH + " AND " + Room_Expenses.COLUMN_USERNAME + " = new." +
				COLUMN_USERNAME + "; END;";
		public static final String SQL_DROP_TRIGGER = "DROP TRIGGER " + TRIGGER_NAME;
	}

	public static final class RoomExpenses implements BaseColumns {
		public static final String EXPENSE_TABLE_NAME = "room_expenses";
		public static final String TRIGGER_NAME = "UPDATE_TOTAL_EXPENSE_TRG ";
		public static final String EXPENSE_ROOM_ID = "room_id";
		public static final String EXPENSE_USER_ID = "user_id";
		public static final String EXPENSE_CATEGORY = "expense_category";
		public static final String EXPENSE_SUBCATEGORY = "expense_subcategory";
		public static final String EXPENSE_DESCRIPTION = "expense_desc";
		public static final String EXPENSE_AMOUNT = "expense_amount";
		public static final String EXPENSE_QUANTITY = "expense_quantity";
		public static final String EXPENSE_DATE = "expense_date";

		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + EXPENSE_TABLE_NAME + " ("
				+ _ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + NOT_NULL + COMMA_SEP +
				EXPENSE_ROOM_ID + NOT_NULL + COMMA_SEP +
				EXPENSE_USER_ID + NOT_NULL + COMMA_SEP +
				EXPENSE_CATEGORY + TEXT_TYPE + NOT_NULL + COMMA_SEP +
				EXPENSE_SUBCATEGORY + TEXT_TYPE + COMMA_SEP +
				EXPENSE_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
				EXPENSE_QUANTITY + INTEGER_TYPE + COMMA_SEP +
				EXPENSE_AMOUNT + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				EXPENSE_DATE + DATETIME_TYPE + NOT_NULL + COMMA_SEP +
				"FOREIGN KEY (" + EXPENSE_ROOM_ID + ") " +
				"REFERENCES " + RoomDetails.DETAILS_TABLE_NAME +
				"(" + RoomDetails._ID + ") " + COMMA_SEP +
				"FOREIGN KEY (" + EXPENSE_USER_ID + ") " +
				"REFERENCES " + UserDetails.USER_TABLE_NAME +
				"(" + UserDetails._ID + ") " + " )";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + EXPENSE_TABLE_NAME;

	}

	/**
	 * ************************************************************************************
	 * R O O M   S T A T I S T I C S
	 * *************************************************************************************
	 */
	public static final class RoomStats implements BaseColumns {
		public static final String STATS_TABLE_NAME = "room_stats";
		public static final String STATS_ROOM_ID = "room_id";
		public static final String STATS_MONTH_YEAR = "month_year";
		public static final String STATS_RENT_MARGIN = "rent_margin";
		public static final String STATS_MAID_MARGIN = "maid_margin";
		public static final String STATS_ELECTRICITY_MARGIN = "elec_margin";
		public static final String STATS_MISCELLANEOUS_MARGIN = "misc_margin";
		public static final String STATS_TOTAL = "total";
		public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + STATS_TABLE_NAME + " ("
				+ _ID + INTEGER_PRIMARY_KEY_AUTOINCREMENT + NOT_NULL + COMMA_SEP +
				STATS_RENT_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				STATS_MAID_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				STATS_ELECTRICITY_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				STATS_MISCELLANEOUS_MARGIN + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
				STATS_MONTH_YEAR + TEXT_TYPE + NOT_NULL + COMMA_SEP +
				"FOREIGN KEY (" + STATS_ROOM_ID + ") " +
				"REFERENCES " + RoomDetails.DETAILS_TABLE_NAME +
				"(" + RoomDetails._ID + ") " + " )";
		public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + STATS_TABLE_NAME;
	}
}
