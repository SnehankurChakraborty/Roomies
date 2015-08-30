package com.phaseii.rxm.roomies.util;

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

    @Override
    public String toString() {
        return value;
    }
}
