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
