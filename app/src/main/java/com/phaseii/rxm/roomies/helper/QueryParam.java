package com.phaseii.rxm.roomies.helper;

/**
 * Created by Snehankur on 7/1/2015.
 */
public enum QueryParam {
    ROOMID("room_id"),
    USERALIAS("user_alias"),
    USERNAME("username"),
    PASSWORD("password"),
    SENDER_ID("sender_id"),
    USER_ID("user_id"),
    EXPENSE_CATEGORY("expense_category"),
    EXPENSE_SUBCATEGORY("expense_subcategory"),
    EXPENSE_DESCRIPTION("expense_description"),
    EXPENSE_AMOUNT("expense_amount"),
    EXPENSE_QUANTITY("expense_quantity"),
    EXPENSE_DATE("expense_date"),
    ROOM_ALIAS("room_alias"),
    NO_OF_PERSONS("no_of_persons"),
    MONTH_YEAR("month_year"),
    RENT_MARGIN("rent_margin"),
    MAID_MARGIN("maid_margin"),
    ELECTRICITY_MARGIN("elec_margin"),
    MISCELLANEOUS_MARGIN("misc_margin"),
    TOTAL("total");

    private String value;

    QueryParam(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
