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

/**
 * Created by Snehankur on 7/1/2015.
 */
public enum QueryParam {
    ROOM_ID("room_id"),
    USER_ALIAS("user_alias"),
    USERNAME("username"),
    PASSWORD("password"),
    SENDER_ID("sender_id"),
    USER_ID("user_id"),
    EXPENSE_CATEGORY("expense_category"),
    EXPENSE_SUBCATEGORY("expense_subcategory"),
    EXPENSE_DESCRIPTION("expense_desc"),
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
    RENT_SPENT("rent_spent"),
    MAID_SPENT("maid_spent"),
    ELECTRICITY_SPENT("elec_spent"),
    MISCELLANEOUS_SPENT("misc_spent"),
    TOTAL("total"), ID("_ID");

    private String value;

    QueryParam(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
