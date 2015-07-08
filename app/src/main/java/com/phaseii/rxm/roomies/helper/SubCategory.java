package com.phaseii.rxm.roomies.helper;

/**
 * Created by Snehankur on 8/16/2015.
 */
public enum SubCategory {
    BILLS("bills"),
    GROCERY("grocery"),
    VEGETABLES("vegetables"),
    OTHERS("others");

    private String value;

    SubCategory(String value) {
        this.value = value;
    }

    public static SubCategory getSubcategory(String value) {
        if ("bills".equals(value)) {
            return SubCategory.BILLS;
        } else if ("grocery".equals(value)) {
            return SubCategory.GROCERY;
        } else if ("vegetables".equals(value)) {
            return SubCategory.VEGETABLES;
        } else if ("others".equals(value)) {
            return SubCategory.OTHERS;
        } else {
            throw new IllegalArgumentException("Unknown subcategory " + value);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
