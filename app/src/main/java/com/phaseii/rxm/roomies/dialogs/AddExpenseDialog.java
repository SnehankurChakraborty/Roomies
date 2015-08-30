package com.phaseii.rxm.roomies.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.ToggleButton;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.fragments.DashboardFragment;
import com.phaseii.rxm.roomies.fragments.HomeFragment;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.helper.Category;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.helper.SubCategory;
import com.phaseii.rxm.roomies.logging.RoomiesLogger;
import com.phaseii.rxm.roomies.manager.RoomExpensesManager;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ELECTRICITY_SPENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_MAID_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_MAID_SPENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_MISCELLANEOUS_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_MISCELLANEOUS_SPENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_RENT_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_RENT_SPENT;

/**
 * Created by Snehankur on 4/5/2015.
 */
public class AddExpenseDialog extends DialogFragment implements DialogInterface.OnShowListener {

    private static int pagerId;
    private RoomiesLogger log;
    private Context mContext;
    private ArrayAdapter<CharSequence> mCategoryAdapter;
    private ArrayAdapter<CharSequence> mSubCategoryAdapter;
    private EditText amount;
    private EditText description;
    private EditText quantity;
    private Button positive;
    private Button negative;
    private RadioGroup quantityRadio;
    private ToggleButton rent;
    private ToggleButton electricity;
    private ToggleButton maid;
    private ToggleButton miscellaneous;
    private ToggleButton bills;
    private ToggleButton grocery;
    private ToggleButton vegetables;
    private ToggleButton others;
    private TableRow miscRow;
    private AlertDialog dialog;
    private View dialogView;

    private String username;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;
    private Category category;
    private SubCategory subCategory;
    private RoomExpensesManager manager;
    private boolean isExceedConfirmed;

    public static AddExpenseDialog getInstance(int pagerId) {
        AddExpenseDialog.pagerId = pagerId;
        return new AddExpenseDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        log = RoomiesLogger.getInstance();
        log.createEntryLoggingMessage(AddExpenseDialog.class.getName(), "onCreateDialog", null);

        mContext = getActivity().getApplicationContext();
        mSharedPref = mContext.getSharedPreferences(RoomiesConstants.PREF_ROOMIES_KEY,
                Context.MODE_PRIVATE);
        mEditor = mSharedPref.edit();
        username = mSharedPref.getString(RoomiesConstants.PREF_USERNAME, null);
        manager = new RoomExpensesManager(mContext);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_expense_dilog, null);

        description = (EditText) dialogView.findViewById(R.id.description);
        amount = (EditText) dialogView.findViewById(R.id.amount);
        quantity = (EditText) dialogView.findViewById(R.id.quantity);
        quantityRadio = (RadioGroup) dialogView.findViewById(R.id.quantity_radio);

        rent = (ToggleButton) dialogView.findViewById(R.id.rent_icon);
        electricity = (ToggleButton) dialogView.findViewById(
                R.id.electricity_icon);
        maid = (ToggleButton) dialogView.findViewById(R.id.maid_icon);
        miscellaneous = (ToggleButton) dialogView.findViewById(R.id.misc_icon);

        bills = (ToggleButton) dialogView.findViewById(R.id.bills_icon);
        grocery = (ToggleButton) dialogView.findViewById(R.id.grocery_icon);
        vegetables = (ToggleButton) dialogView.findViewById(R.id.vegetable_icon);
        others = (ToggleButton) dialogView.findViewById(R.id.others_icon);
        miscRow = (TableRow) dialogView.findViewById(R.id.misc_row);
        defaultSettings();

        miscellaneous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupOnClickListener(Controls.MISC);
            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupOnClickListener(Controls.RENT);
            }
        });

        electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupOnClickListener(Controls.ELEC);

            }
        });

        maid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupOnClickListener(Controls.MAID);

            }
        });

        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupOnClickListener(Controls.BILLS);
            }
        });

        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupOnClickListener(Controls.GROCERY);
            }
        });

        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupOnClickListener(Controls.VEGETABLES);
            }

        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupOnClickListener(Controls.OTHERS);
            }

        });


        positive = (Button) dialogView.findViewById(R.id.positiveButton);
        negative = (Button) dialogView.findViewById(R.id.negativeButton);

        builder.setTitle("Add Expenses").setView(dialogView);
        dialog = builder.create();
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValidDescription = true;
                boolean isValidQuantity = false;
                boolean isValidAmount = RoomiesHelper.setNumericError("amount",
                        getActivity().getBaseContext(), dialogView);
                if (description.getVisibility() == View.VISIBLE) {
                    isValidDescription = RoomiesHelper.setError("description", mContext,
                            dialogView);
                }
                if (quantity.getVisibility() == View.VISIBLE) {
                    isValidQuantity = RoomiesHelper.setNumericError("quantity",
                            mContext, dialogView);
                } else {
                    isValidQuantity = true;
                }
                if (isValidAmount && isValidQuantity && isValidDescription) {
                    submitExpenses();
                }
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        log.createExitLoggingMessage(AddExpenseDialog.class.getName(), "onCreateDialog", null);
        return dialog;
    }

    public void submitExpenses() {

        log.createEntryLoggingMessage(AddExpenseDialog.class.getName(), "submitExpenses",
                null);

        float amountVal = Float.valueOf(amount.getText().toString());
        String quantityVal = null;
        String descriptionVal = null;
        if (View.VISIBLE == quantity.getVisibility()) {
            quantityVal = Float.valueOf(quantity.getText().toString())
                    + ((RadioButton) dialogView.findViewById(quantityRadio
                    .getCheckedRadioButtonId())).getText().toString();
        }
        if (View.VISIBLE == description.getVisibility()) {
            descriptionVal = description.getText().toString();
        }
        boolean isExpenseAdded = false;

        switch (category) {
            case RENT:
                float rentSpent = Float.valueOf(
                        mSharedPref.getString(PREF_RENT_SPENT, "0"));
                float rentMargin = Float.valueOf(
                        mSharedPref.getString(PREF_RENT_MARGIN, "0"));
                if (amountVal + rentSpent > rentMargin && !isExceedConfirmed) {
                    confirmMarginExceedAlert("Rent", String.valueOf(rentMargin),
                            String.valueOf(rentSpent));
                } else {
                    isExceedConfirmed = false;
                    isExpenseAdded = manager.addRoomExpense(category,
                            null, null, null, amountVal);
                    if (isExpenseAdded) {
                        mEditor.putString(PREF_RENT_SPENT, String.valueOf(rentSpent + amountVal));
                        mEditor.apply();
                    }

                }

                break;
            case MAID:
                float maidSpent = Float.valueOf(
                        mSharedPref.getString(PREF_MAID_SPENT, "0"));
                float maidMargin = Float.valueOf(
                        mSharedPref.getString(PREF_MAID_MARGIN, "0"));
                if (amountVal + maidSpent > maidMargin && !isExceedConfirmed) {
                    confirmMarginExceedAlert("Maid", String.valueOf(maidMargin),
                            String.valueOf(maidSpent));
                } else {
                    isExceedConfirmed = false;
                    isExpenseAdded = manager.addRoomExpense(category,
                            null, null, null, amountVal);
                    if (isExpenseAdded) {
                        mEditor.putString(PREF_MAID_SPENT, String.valueOf(maidSpent + amountVal));
                        mEditor.apply();
                    }
                }
                break;
            case ELECTRICITY:
                float elecSpent = Float.valueOf(
                        mSharedPref.getString(PREF_ELECTRICITY_SPENT, "0"));
                float elecMargin = Float.valueOf(
                        mSharedPref.getString(PREF_ELECTRICITY_MARGIN, "0"));
                if (amountVal + elecSpent > elecMargin && !isExceedConfirmed) {
                    confirmMarginExceedAlert("Electricity", String.valueOf(elecMargin),
                            String.valueOf(elecSpent));
                } else {
                    isExceedConfirmed = false;
                    isExpenseAdded = manager.addRoomExpense(category,
                            null, null, null, amountVal);
                    if (isExpenseAdded) {
                        mEditor.putString(PREF_ELECTRICITY_SPENT, String.valueOf(elecSpent +
                                amountVal));
                        mEditor.apply();
                    }
                }
                break;
            case MISCELLANEOUS:
                float miscSpent = Float.valueOf(
                        mSharedPref.getString(PREF_MISCELLANEOUS_SPENT, "0"));
                float miscMargin = Float.valueOf(
                        mSharedPref.getString(PREF_MISCELLANEOUS_MARGIN, "0"));
                if (amountVal + miscSpent > miscMargin && !isExceedConfirmed) {
                    confirmMarginExceedAlert("Miscellaneous", String.valueOf(miscMargin),
                            String.valueOf(miscSpent));
                } else {
                    isExceedConfirmed = false;
                    isExpenseAdded = manager.addRoomExpense(category,
                            subCategory, descriptionVal, quantityVal, amountVal);
                    if (isExpenseAdded) {
                        mEditor.putString(PREF_MISCELLANEOUS_SPENT, String.valueOf(miscSpent +
                                amountVal));
                        mEditor.apply();
                    }
                }
        }

        if (isExpenseAdded && null != dialog) {
            updateGraphs(dialog);
            dialog.dismiss();
        }
        log.createExitLoggingMessage(AddExpenseDialog.class.getName(), "submitExpenses", null);

    }

    @Override
    public void onShow(DialogInterface dialog) {

    }

    private void updateGraphs(AlertDialog dialog) {
        RoomiesFragment fragment = (RoomiesFragment) getActivity().getSupportFragmentManager()
                .getFragments().get(0);
        if (fragment instanceof HomeFragment) {
            ((RoomiesFragment.UpdatableFragment) fragment).update(null);
            ((RoomiesFragment.UpdatableFragment) fragment).update(null);
        } else if (fragment instanceof DashboardFragment) {
            ((DashboardFragment) fragment).update(username);
        }
        dialog.dismiss();
    }

    private void defaultSettings() {

        category = Category.MISCELLANEOUS;
        subCategory = SubCategory.OTHERS;
        miscellaneous.setChecked(true);
        miscRow.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        quantity.setVisibility(View.VISIBLE);
        quantityRadio.setVisibility(View.VISIBLE);
    }

    private void setupOnClickListener(Controls controls) {

        category = null;
        subCategory = null;
        rent.setChecked(false);
        maid.setChecked(false);
        electricity.setChecked(false);
        miscellaneous.setChecked(false);
        miscRow.setVisibility(View.GONE);
        others.setChecked(true);
        bills.setChecked(false);
        vegetables.setChecked(false);
        grocery.setChecked(false);
        description.setVisibility(View.GONE);
        quantity.setVisibility(View.GONE);
        quantityRadio.setVisibility(View.GONE);
        amount.setVisibility(View.VISIBLE);

        switch (controls) {
            case RENT:
                if (!rent.isChecked()) {
                    rent.setChecked(true);
                    category = Category.RENT;
                    break;
                }
            case MAID:
                if (!maid.isChecked()) {
                    maid.setChecked(true);
                    category = Category.MAID;
                    break;
                }
            case ELEC:
                if (!electricity.isChecked()) {
                    electricity.setChecked(true);
                    category = Category.ELECTRICITY;
                    break;
                }
            case MISC:
                defaultSettings();
                break;
            case BILLS:
                if (!bills.isChecked()) {
                    category = Category.MISCELLANEOUS;
                    subCategory = SubCategory.BILLS;
                    miscellaneous.setChecked(true);
                    miscRow.setVisibility(View.VISIBLE);
                    others.setChecked(false);
                    bills.setChecked(true);
                    description.setVisibility(View.VISIBLE);
                    break;
                }
            case GROCERY:
                if (!grocery.isChecked()) {
                    category = Category.MISCELLANEOUS;
                    subCategory = SubCategory.GROCERY;
                    miscellaneous.setChecked(true);
                    miscRow.setVisibility(View.VISIBLE);
                    others.setChecked(false);
                    grocery.setChecked(true);
                    description.setVisibility(View.VISIBLE);
                    quantity.setVisibility(View.VISIBLE);
                    quantityRadio.setVisibility(View.VISIBLE);
                    break;
                }
            case VEGETABLES:
                if (!vegetables.isChecked()) {
                    category = Category.MISCELLANEOUS;
                    subCategory = SubCategory.VEGETABLES;
                    miscellaneous.setChecked(true);
                    miscRow.setVisibility(View.VISIBLE);
                    others.setChecked(false);
                    vegetables.setChecked(true);
                    description.setVisibility(View.VISIBLE);
                    quantity.setVisibility(View.VISIBLE);
                    quantityRadio.setVisibility(View.VISIBLE);
                    break;
                }
            case OTHERS:
                defaultSettings();
                break;
            default:
                defaultSettings();
        }

    }

    private void confirmMarginExceedAlert(final String category, final String margin, final String
            spent) {

        log.info(category.toString() + " margin: " + margin);
        final AlertDialog.Builder confirmBuilder = new AlertDialog.Builder
                (getActivity());
        confirmBuilder.setMessage(
                "You are exceeding the " + category + " limit (" + margin + " INR )" +
                        " .You have already spent " + spent + " INR" +
                        "on" + category + ". " +
                        "Do you still want to continue ?").setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        isExceedConfirmed = true;
                        dialogInterface.dismiss();
                        submitExpenses();
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                }).create();
        confirmBuilder.show();
    }

    public enum Controls {RENT, MAID, ELEC, MISC, BILLS, GROCERY, VEGETABLES, OTHERS}

}
