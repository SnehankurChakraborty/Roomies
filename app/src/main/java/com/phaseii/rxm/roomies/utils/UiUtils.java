package com.phaseii.rxm.roomies.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Snehankur on 2/7/2016.
 */
public class UiUtils {

    /**
     * @param view
     * @return
     */
    public static boolean isFieldBlankOrEmpty(View view) {
        boolean isBlankOrEmpty = false;
        String textViewData = ((TextView) view).getText().toString();
        if (textViewData.trim().equals("") || textViewData.length
                () == 0) {
            isBlankOrEmpty = true;
        }

        return isBlankOrEmpty;
    }

    /**
     * @param feildId
     * @param context
     * @param view
     * @return
     */
    public static boolean setError(String feildId, Context context, View view) {
        boolean isValid = true;
        String errorId = feildId + "_error";
        String togglId = feildId + "_toggle";
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        int resId = resources.getIdentifier(feildId, "id", packageName);
        int resErrorId = resources.getIdentifier(errorId, "id", packageName);
        int resToggleId = resources.getIdentifier(togglId, "id", packageName);
        View feild = view.findViewById(resId);
        TextView errorFeild = (TextView) view.findViewById(resErrorId);
        if (resToggleId > 0) {
            ToggleButton toggleButton = (ToggleButton) view.findViewById(resToggleId);
            if (toggleButton.isChecked()) {
                if (isFieldBlankOrEmpty(feild)) {
                    isValid = false;
                    errorFeild.setVisibility(View.VISIBLE);
                } else {
                    errorFeild.setVisibility(View.INVISIBLE);
                }
            } else {
                errorFeild.setVisibility(View.INVISIBLE);
            }
        } else {
            if (isFieldBlankOrEmpty(feild)) {
                isValid = false;
                errorFeild.setVisibility(View.VISIBLE);
            } else {
                errorFeild.setVisibility(View.INVISIBLE);
            }
        }
        return isValid;
    }

    /**
     * @param feildId
     * @param context
     * @param view
     * @return
     */
    public static boolean setNumericError(String feildId, Context context, View view) {
        boolean isValid = true;
        String errorId = feildId + "_error";
        String togglId = feildId + "_toggle";
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        int resId = resources.getIdentifier(feildId, "id", packageName);
        int resErrorId = resources.getIdentifier(errorId, "id", packageName);
        int resToggleId = resources.getIdentifier(togglId, "id", packageName);
        View feild = view.findViewById(resId);
        TextView errorFeild = (TextView) view.findViewById(resErrorId);
        if (resToggleId > 0) {
            ToggleButton toggleButton = (ToggleButton) view.findViewById(resToggleId);
            if (toggleButton.isChecked()) {
                if (isFieldBlankOrEmpty(feild) || ""
                        .equals(((TextView) feild).getText().toString()) || Integer
                        .valueOf(((TextView) feild).getText().toString()) <= 0) {
                    isValid = false;
                    errorFeild.setVisibility(View.VISIBLE);
                } else {
                    errorFeild.setVisibility(View.INVISIBLE);
                }
            } else {
                errorFeild.setVisibility(View.INVISIBLE);
            }
        } else {
            if (isFieldBlankOrEmpty(feild) || "".equals(((TextView) feild).getText()
                    .toString()) || Integer.valueOf(((TextView) feild).getText().toString()) <= 0) {
                isValid = false;
                errorFeild.setVisibility(View.VISIBLE);
            } else {
                errorFeild.setVisibility(View.INVISIBLE);
            }
        }
        return isValid;
    }

    /**
     *
     * @param textInputLayout
     * @param editText
     * @param msg
     */
    public static void setupTextInputLayout(final TextInputLayout textInputLayout,
            final EditText editText, final String msg) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textInputLayout.isErrorEnabled()) {
                    textInputLayout.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null == s || s.toString().trim().length() == 0) {
                    textInputLayout.setError(msg);
                }
            }
        });

    }
}
