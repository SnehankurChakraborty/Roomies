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
 * Created by Snehankur on 8/16/2015.
 */
public enum SubCategory {
    BILLS("Bills"),
    GROCERY("Grocery"),
    FOOD("Food"),
    ENTERTAINMENT("Entertainment"),
    OTHERS("Others");

    private String value;

    SubCategory(String value) {
        this.value = value;
    }

    public static SubCategory getSubcategory(String value) {
        if ("Bills".equals(value)) {
            return SubCategory.BILLS;
        } else if ("Grocery".equals(value)) {
            return SubCategory.GROCERY;
        } else if ("Food".equals(value)) {
            return SubCategory.FOOD;
        } else if ("Others".equals(value)) {
            return SubCategory.OTHERS;
        } else if ("Entertainment".equals(value)) {
            return SubCategory.ENTERTAINMENT;
        } else {
            throw new IllegalArgumentException("Unknown subcategory " + value);
        }
    }

    public static String[] getSubCategories() {
        return new String[]{BILLS.value.toUpperCase(), GROCERY.value.toUpperCase(),
                FOOD.value.toUpperCase(), "ENT.",
                OTHERS.value.toUpperCase()};
    }

    @Override
    public String toString() {
        return value;
    }
}
