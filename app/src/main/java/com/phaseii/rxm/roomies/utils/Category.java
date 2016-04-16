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
public enum Category {
    RENT("rent"),
    MAID("maid"),
    ELECTRICITY("electricity"),
    MISCELLANEOUS("miscellaneous");

    private String value;

    Category(String value) {
        this.value = value;
    }

    public static Category getCategory(String value) {
        if ("rent".equals(value)) {
            return Category.RENT;
        } else if ("maid".equals(value)) {
            return Category.MAID;
        } else if ("electricity".equals(value)) {
            return Category.ELECTRICITY;
        } else if ("miscellaneous".equals(value)) {
            return Category.MISCELLANEOUS;
        } else {
            throw new IllegalArgumentException("Unknown Category " + value);
        }
    }

    public static String[] getCategories() {
        return new String[]{RENT.value, MAID.value, ELECTRICITY.value, "MISC."};
    }

    @Override
    public String toString() {
        return value;
    }

}
