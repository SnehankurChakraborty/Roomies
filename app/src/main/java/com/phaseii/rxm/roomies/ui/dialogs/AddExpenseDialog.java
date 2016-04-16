package com.phaseii.rxm.roomies.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.logging.RoomiesLogger;
import com.phaseii.rxm.roomies.service.RoomExpensesManager;
import com.phaseii.rxm.roomies.utils.Category;
import com.phaseii.rxm.roomies.utils.Constants;
import com.phaseii.rxm.roomies.utils.DateUtils;
import com.phaseii.rxm.roomies.utils.RoomiesHelper;
import com.phaseii.rxm.roomies.utils.SubCategory;

import java.util.Date;

import static com.phaseii.rxm.roomies.utils.Constants.PREF_ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ELECTRICITY_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MAID_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MAID_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MISCELLANEOUS_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_MISCELLANEOUS_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_RENT_MARGIN;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_RENT_SPENT;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_ROOM_ID;
import static com.phaseii.rxm.roomies.utils.Constants.PREF_USER_ID;

/**
 * Created by Snehankur on 4/5/2015.
 */
public class AddExpenseDialog extends DialogFragment {

    private static Context mContext;
    private RoomiesLogger log;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;
    private Category category;
    private SubCategory subCategory;
    private RoomExpensesManager manager;
    private boolean isExceedConfirmed;
    private OnSubmitListener mOnSubmitListener;
    private CoordinatorLayout coordinatorLayout;
    private ImageView rent;
    private ImageView maid;
    private ImageView electricity;
    private ImageView entertainment;
    private ImageView bills;
    private ImageView grocery;
    private ImageView food;
    private ImageView others;
    private ImageView expenseImage;

    /**
     * Creates a new instance of the Dialog for adding transaction.
     */
    public static AddExpenseDialog getInstance(Context context) {
        mContext = context;
        return new AddExpenseDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style
                .Theme_Holo_Light);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnSubmitListener = (OnSubmitListener) activity;
        } catch (ClassCastException cce) {
            throw new ClassCastException(activity.toString() + " must implement OnSubmitListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle
            savedInstanceState) {
        final View dialogView = inflater.inflate(R.layout.add_expense, container, false);
        log = RoomiesLogger.getInstance();
        log.createEntryLoggingMessage(AddExpenseDialog.class.getName(), "onCreateDialog", null);

        mSharedPref = mContext
                .getSharedPreferences(Constants.PREF_ROOMIES_KEY, Context.MODE_PRIVATE);

        mEditor = mSharedPref.edit();
        manager = new RoomExpensesManager(mContext);
        coordinatorLayout = (CoordinatorLayout) dialogView.findViewById(R.id.coordinatorLayout);
        expenseImage = (ImageView) dialogView.findViewById(R.id.expense_image);
        final RelativeLayout rentLayout = (RelativeLayout) dialogView
                .findViewById(R.id.rent_layout);
        final RelativeLayout maidLayout = (RelativeLayout) dialogView
                .findViewById(R.id.maid_layout);
        final RelativeLayout electricityLayout = (RelativeLayout) dialogView
                .findViewById(R.id.elec_layout);
        final RelativeLayout entertainmentLayout = (RelativeLayout) dialogView
                .findViewById(R.id.ent_layout);
        final RelativeLayout billsLayout = (RelativeLayout) dialogView
                .findViewById(R.id.bills_layout);
        final RelativeLayout groceryLayout = (RelativeLayout) dialogView
                .findViewById(R.id.grocery_layout);
        final RelativeLayout foodLayout = (RelativeLayout) dialogView
                .findViewById(R.id.food_layout);
        final RelativeLayout othersLayout = (RelativeLayout) dialogView
                .findViewById(R.id.others_layout);
        final RelativeLayout dateTimeLayout = (RelativeLayout) dialogView
                .findViewById(R.id.date_time_layout);

        rent = (ImageView) dialogView.findViewById(R.id.rent_btn);
        maid = (ImageView) dialogView.findViewById(R.id.maid_btn);
        electricity = (ImageView) dialogView.findViewById(
                R.id.elec_btn);
        entertainment = (ImageView) dialogView.findViewById(R.id.ent_btn);
        bills = (ImageView) dialogView.findViewById(R.id.bills_btn);
        grocery = (ImageView) dialogView.findViewById(R.id.grocery_btn);
        food = (ImageView) dialogView.findViewById(R.id.food_btn);
        others = (ImageView) dialogView.findViewById(R.id.others_btn);
        final AppCompatSpinner quantityUnitSpinner = (AppCompatSpinner) dialogView
                .findViewById(R.id.quantity_unit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(mContext, R.array.quantity_unit_array,
                        R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        quantityUnitSpinner.setAdapter(adapter);

        final EditText quantity = (EditText) dialogView.findViewById(R.id.quantity);

        // Since the host view actually supports clicks, you can return false
        // from your touch listener and continue to receive events.
        rent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return setOnTouchListeners(v, e, rentLayout, Category.RENT, null, quantity,
                        quantityUnitSpinner);
            }
        });
        maid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return setOnTouchListeners(v, e, maidLayout, Category.MAID, null, quantity,
                        quantityUnitSpinner);
            }
        });
        electricity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return setOnTouchListeners(v, e, electricityLayout, Category.ELECTRICITY, null,
                        quantity, quantityUnitSpinner);
            }
        });
        entertainment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return setOnTouchListeners(v, e, entertainmentLayout, Category.MISCELLANEOUS,
                        SubCategory.ENTERTAINMENT, quantity, quantityUnitSpinner);
            }
        });
        bills.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return setOnTouchListeners(v, e, billsLayout, Category.MISCELLANEOUS,
                        SubCategory.BILLS, quantity, quantityUnitSpinner);
            }
        });
        food.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return setOnTouchListeners(v, e, foodLayout, Category.MISCELLANEOUS,
                        SubCategory.FOOD, quantity, quantityUnitSpinner);
            }
        });
        grocery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return setOnTouchListeners(v, e, groceryLayout, Category.MISCELLANEOUS,
                        SubCategory.GROCERY, quantity, quantityUnitSpinner);
            }
        });
        others.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                return setOnTouchListeners(v, e, othersLayout, Category.MISCELLANEOUS,
                        SubCategory.OTHERS, quantity, quantityUnitSpinner);
            }
        });
        rentLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setOnTouchListeners(rent, null, rentLayout, Category.RENT, null, quantity,
                        quantityUnitSpinner);
            }
        });
        maidLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setOnTouchListeners(maid, null, rentLayout, Category.MAID, null, quantity,
                        quantityUnitSpinner);
            }
        });
        electricityLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setOnTouchListeners(electricity, null, rentLayout, Category.ELECTRICITY, null,
                        quantity, quantityUnitSpinner);
            }
        });
        entertainmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setOnTouchListeners(entertainment, null, rentLayout, Category.MISCELLANEOUS,
                        SubCategory.ENTERTAINMENT, quantity, quantityUnitSpinner);
            }
        });
        billsLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setOnTouchListeners(bills, null, rentLayout, Category.MISCELLANEOUS,
                        SubCategory.BILLS, quantity, quantityUnitSpinner);
                ;
            }
        });
        groceryLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setOnTouchListeners(grocery, null, rentLayout, Category.MISCELLANEOUS,
                        SubCategory.GROCERY, quantity, quantityUnitSpinner);
            }
        });
        foodLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setOnTouchListeners(food, null, rentLayout, Category.MISCELLANEOUS,
                        SubCategory.FOOD, quantity, quantityUnitSpinner);
            }
        });
        othersLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                setOnTouchListeners(others, null, rentLayout, Category.MISCELLANEOUS,
                        SubCategory.OTHERS, quantity, quantityUnitSpinner);
            }
        });

        final TextView dateTime = (TextView) dialogView.findViewById(R.id.date_time);
        dateTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                    int dayOfMonth) {
                                showTimePicker(year, monthOfYear, dayOfMonth, dateTime);
                            }
                        }, DateUtils.getCurrentYear(), DateUtils.getCurrentMonth(),
                        DateUtils.getCurrentDay()) {

                };

                dialog.getDatePicker().setMinDate(DateUtils.getMonthStartDate());
                dialog.getDatePicker().setMaxDate(new Date().getTime());
                dialog.show();
            }
        });

        final EditText description = (EditText) dialogView.findViewById(R.id.description);

        final RelativeLayout descriptionLayout = (RelativeLayout) dialogView
                .findViewById(R.id.description_layout);

        final RelativeLayout descriptionDivider = (RelativeLayout) dialogView
                .findViewById(R.id.description_divider);
        final EditText amount = (EditText) dialogView.findViewById(R.id.amount);

        final RelativeLayout quantityLayout = (RelativeLayout) dialogView
                .findViewById(R.id.quantity_number);

        defaultSettings(quantityLayout, descriptionLayout, descriptionDivider);
        clearUI(grocery, bills, food, others, electricity, rent, maid, entertainment);

        ImageButton positive = (ImageButton) dialogView.findViewById(R.id.positiveButton);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValidDescription = true;
                boolean isValidQuantity = false;
                boolean isValidAmount = RoomiesHelper.setNumericError("amount", mContext,
                        dialogView);
                if (descriptionLayout.getVisibility() == View.VISIBLE) {
                    isValidDescription = RoomiesHelper.setError("description", mContext,
                            dialogView);
                }
                if (Category.MISCELLANEOUS == category) {
                    isValidQuantity = RoomiesHelper.setNumericError("quantity",
                            mContext, dialogView);
                } else {
                    isValidQuantity = true;
                }
                if (isValidAmount && isValidQuantity && isValidDescription) {
                    submitExpenses(amount, quantityLayout, quantity, descriptionLayout,
                            description, quantityUnitSpinner.getSelectedItem().toString(),
                            dateTime.getText().toString());
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Please complete the mandatory field(s)",
                                    Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        log.createExitLoggingMessage(AddExpenseDialog.class.getName(), "onCreateDialog", null);
        return dialogView;
    }

    private void showTimePicker(final int year, final int month, final int day,
            final TextView dateTime) {
        TimePickerDialog timePicker = new TimePickerDialog(mContext,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        dateTime.setText(DateUtils
                                .dateToStringFormatter(year, month, day, hourOfDay, minute));
                    }
                }, DateUtils.getCurrentHour(), DateUtils.getCurrentMinutes(),
                DateFormat.is24HourFormat(mContext));
        timePicker.show();
    }

    /**
     * Decides what happens when any category is selected
     */
    private boolean setOnTouchListeners(View categoryView, MotionEvent motionEvent,
            RelativeLayout parentLayout,
            Category category, SubCategory subCategory, EditText quantity,
            AppCompatSpinner quantitySpinner) {
        clearUI(grocery, bills, food, others, electricity, rent, maid, entertainment);
        categoryView.setSelected(true);
        if (null != motionEvent) {
            float x = motionEvent.getX() + categoryView.getLeft();
            float y = motionEvent.getY() + categoryView.getTop();

            parentLayout.drawableHotspotChanged(x, y);

            // Simulate pressed state on the card view.
            switch (motionEvent.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    parentLayout.setPressed(true);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    parentLayout.setPressed(false);
                    break;
            }
        }
        switch (category) {
            case RENT:
                this.category = Category.RENT;
                this.subCategory = null;
                expenseImage.setImageResource(R.drawable.ic_rent_selected);
                expenseImage.setBackgroundColor(mContext.getResources().getColor(R.color.rent));
                quantity.setText("");
                quantity.setFocusable(false);
                quantity.setFocusableInTouchMode(false);
                quantitySpinner.setEnabled(false);
                break;
            case ELECTRICITY:
                this.category = Category.ELECTRICITY;
                this.subCategory = null;
                expenseImage.setImageResource(R.drawable.ic_electricity_selected);
                expenseImage
                        .setBackgroundColor(mContext.getResources().getColor(R.color.electricity));
                quantity.setText("");
                quantity.setFocusable(false);
                quantity.setFocusableInTouchMode(false);
                quantitySpinner.setEnabled(false);
                break;
            case MAID:
                this.category = Category.MAID;
                this.subCategory = null;
                expenseImage.setImageResource(R.drawable.ic_maid_selected);
                expenseImage.setBackgroundColor(mContext.getResources().getColor(R.color.maid));
                quantity.setText("");
                quantity.setFocusable(false);
                quantity.setFocusableInTouchMode(false);
                quantitySpinner.setEnabled(false);
                break;
            case MISCELLANEOUS:
                switch (subCategory) {
                    case BILLS:
                        this.category = Category.MISCELLANEOUS;
                        this.subCategory = SubCategory.BILLS;
                        expenseImage.setImageResource(R.drawable.ic_bills_selected);
                        expenseImage.setBackgroundColor(
                                mContext.getResources().getColor(R.color.bills));
                        quantity.setFocusable(true);
                        quantity.setFocusableInTouchMode(true);
                        quantitySpinner.setEnabled(true);
                        break;
                    case GROCERY:
                        this.category = Category.MISCELLANEOUS;
                        this.subCategory = SubCategory.GROCERY;
                        expenseImage.setImageResource(R.drawable.ic_groceries_selected);
                        expenseImage.setBackgroundColor(
                                mContext.getResources().getColor(R.color.grocery));
                        quantity.setFocusable(true);
                        quantity.setFocusableInTouchMode(true);
                        quantitySpinner.setEnabled(true);
                        break;
                    case FOOD:
                        this.category = Category.MISCELLANEOUS;
                        this.subCategory = SubCategory.FOOD;
                        expenseImage.setImageResource(R.drawable.ic_food_selected);
                        expenseImage
                                .setBackgroundColor(mContext.getResources().getColor(R.color.food));
                        quantity.setFocusable(true);
                        quantity.setFocusableInTouchMode(true);
                        quantitySpinner.setEnabled(true);
                        break;
                    case ENTERTAINMENT:
                        this.category = Category.MISCELLANEOUS;
                        this.subCategory = SubCategory.ENTERTAINMENT;
                        expenseImage.setImageResource(R.drawable.ic_entertaintment_selected);
                        expenseImage.setBackgroundColor(
                                mContext.getResources().getColor(R.color.entertaintment));
                        quantity.setFocusable(true);
                        quantity.setFocusableInTouchMode(true);
                        quantitySpinner.setEnabled(true);
                        break;
                    case OTHERS:
                        this.category = Category.MISCELLANEOUS;
                        this.subCategory = SubCategory.OTHERS;
                        expenseImage.setImageResource(R.drawable.ic_others_selected);
                        expenseImage.setBackgroundColor(
                                mContext.getResources().getColor(R.color.others));
                        quantity.setFocusable(true);
                        quantity.setFocusableInTouchMode(true);
                        quantitySpinner.setEnabled(true);
                        break;
                }
        }

        // Pass all events through to the host view.
        return false;
    }

    /**
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    /**
     * Submit expenses.
     */
    public void submitExpenses(TextView amount, View quantityLayout, TextView quantity, View
            descriptionLayout, TextView description, String quantityUnit, String dateTime) {

        log.createEntryLoggingMessage(AddExpenseDialog.class.getName(), "submitExpenses",
                null);

        int amountVal = Integer.parseInt(amount.getText().toString());

        Date dateVal = new Date();
        if (!mContext.getResources().getString(R.string.now).equals(dateTime)) {
            dateVal = DateUtils.stringToDateParser(dateTime);
        }

        String quantityVal = null;
        String descriptionVal = null;
        if (View.VISIBLE == descriptionLayout.getVisibility()) {
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
                            String.valueOf(rentSpent), amount, quantityLayout,
                            quantity, descriptionLayout, description, quantityUnit, dateTime);
                } else {
                    isExceedConfirmed = false;
                    isExpenseAdded = manager.addRoomExpense(category,
                            null, null, null, amountVal, dateVal);
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
                            String.valueOf(maidSpent), amount, quantityLayout,
                            quantity, descriptionLayout, description, quantityUnit, dateTime);
                } else {
                    isExceedConfirmed = false;
                    isExpenseAdded = manager.addRoomExpense(category,
                            null, null, null, amountVal, dateVal);
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
                            String.valueOf(elecSpent), amount, quantityLayout,
                            quantity, descriptionLayout, description, quantityUnit, dateTime);
                } else {
                    isExceedConfirmed = false;
                    isExpenseAdded = manager.addRoomExpense(category,
                            null, null, null, amountVal, dateVal);
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
                            String.valueOf(miscSpent), amount, quantityLayout,
                            quantity, descriptionLayout, description, quantityUnit, dateTime);
                } else {
                    isExceedConfirmed = false;
                    quantityVal = Integer.parseInt(quantity.getText().toString())
                            + quantityUnit;
                    isExpenseAdded = manager.addRoomExpense(category,
                            subCategory, descriptionVal,
                            quantityVal, amountVal, dateVal);
                    if (isExpenseAdded) {
                        mEditor.putString(PREF_MISCELLANEOUS_SPENT, String.valueOf(miscSpent +
                                amountVal));
                        mEditor.apply();
                    }
                }
        }

        if (isExpenseAdded) {
            updateGraphs(description, quantityVal, amount, dateTime);
            dismiss();
        }
        log.createExitLoggingMessage(AddExpenseDialog.class.getName(), "submitExpenses", null);

    }

    /**
     * Update the UI with the recently added expense.
     */
    private void updateGraphs(TextView description, String quantity, TextView amount,
            String dateTime) {
        RoomExpenses roomExpenses = new RoomExpenses();
        roomExpenses.setExpenseCategory(category.toString());
        if (null != subCategory) {
            roomExpenses.setExpenseSubcategory(subCategory.toString());
        }
        if (null != description) {
            roomExpenses.setDescription(description.getText().toString());
        }
        if (null != quantity) {
            roomExpenses.setQuantity(quantity);
        }
        roomExpenses.setAmount(Integer.parseInt(amount.getText().toString()));
        roomExpenses.setExpenseDate(DateUtils.stringToDateParser(dateTime));
        roomExpenses.setMonthYear(DateUtils.getCurrentMonthYear());
        roomExpenses.setUserId(Integer.parseInt(mSharedPref.getString(PREF_USER_ID, "0")));
        roomExpenses.setRoomId(Integer.parseInt(mSharedPref.getString(PREF_ROOM_ID, "0")));
        mOnSubmitListener.onSubmit(roomExpenses);

        dismiss();
    }

    /**
     * set up the default setting for the options
     */
    private void defaultSettings(View quantityLayout, View
            descriptionLayout, View descriptionDivider) {

        category = Category.MISCELLANEOUS;
        subCategory = SubCategory.OTHERS;
        quantityLayout.setVisibility(View.VISIBLE);
        descriptionLayout.setVisibility(View.VISIBLE);
        descriptionDivider.setVisibility(View.VISIBLE);
    }

    private void clearUI(ImageView grocery, ImageView bills, ImageView vegetables, ImageView
            others, ImageView electricity, ImageView rent, ImageView maid, ImageView
            entertaintment) {
        grocery.setSelected(false);
        bills.setSelected(false);
        vegetables.setSelected(false);
        others.setSelected(false);
        electricity.setSelected(false);
        rent.setSelected(false);
        maid.setSelected(false);
        entertaintment.setSelected(false);
    }

    /**
     * Check if the expenses are within the pre defined range.
     */
    private void confirmMarginExceedAlert(final String category, final String margin, final String
            spent, final TextView amount, final View quantityLayout, final TextView quantity,
            final View descriptionLayout, final TextView description, final String quantityUnit,
            final String dateTime) {

        log.info(category.toString() + " margin: " + margin);
        final AlertDialog.Builder confirmBuilder = new AlertDialog.Builder
                (getActivity());
        confirmBuilder.setMessage(
                "You are exceeding the " + category + " limit (" + margin + " INR )" +
                        " .You have already spent " + spent + " INR" +
                        "on" + category + ". " +
                        "Do you still want to continue ?").setPositiveButton("Yes",
                new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick
                            (DialogInterface
                                    dialogInterface, int which) {
                        isExceedConfirmed =
                                true;
                        dialogInterface
                                .dismiss();
                        submitExpenses
                                (amount,
                                        quantityLayout, quantity, descriptionLayout, description,
                                        quantityUnit, dateTime);
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        confirmBuilder.show();
    }


    public interface OnSubmitListener {
        void onSubmit(RoomExpenses expenses);
    }


}
