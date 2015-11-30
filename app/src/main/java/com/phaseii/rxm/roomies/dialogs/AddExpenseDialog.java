package com.phaseii.rxm.roomies.dialogs;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.fragments.HomeFragment;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.fragments.RoommatesFragment;
import com.phaseii.rxm.roomies.logging.RoomiesLogger;
import com.phaseii.rxm.roomies.manager.RoomExpensesManager;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.tabs.DashboardTab;
import com.phaseii.rxm.roomies.util.Category;
import com.phaseii.rxm.roomies.util.DateUtils;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.util.SubCategory;

import java.util.Date;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ELECTRICITY_SPENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_MAID_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_MAID_SPENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_MISCELLANEOUS_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_MISCELLANEOUS_SPENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_RENT_MARGIN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_RENT_SPENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOM_ID;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_USER_ID;

/**
 * Created by Snehankur on 4/5/2015.
 */
public class AddExpenseDialog extends DialogFragment {
    
    private static final int AMOUNTPAD = 0;
    private static final int QUANTITYPAD = 1;
    private RoomiesLogger log;
    private static Context mContext;
    private ArrayAdapter<CharSequence> mCategoryAdapter;
    private ArrayAdapter<CharSequence> mSubCategoryAdapter;
    private TextView amount;
    private EditText description;
    private TextView quantity;
    private ImageView rent;
    private ImageView electricity;
    private ImageView maid;
    private ImageView miscellaneous;
    private ImageView bills;
    private ImageView grocery;
    private ImageView vegetables;
    private ImageView others;
    private LinearLayout miscRow;
    private Dialog dialog;
    private View dialogView;

    private String username;
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;
    private Category category;
    private SubCategory subCategory;
    private RoomExpensesManager manager;
    private boolean isExceedConfirmed;
    private OnSubmitListener mOnSubmitListener;
    private RelativeLayout descriptionLayout;
    private RelativeLayout quantityLayout;
    private TextView quantityUnit;
    private RelativeLayout descriptionDivider;
    
    /**
     * Creates a new instance of the Dialog for adding transaction.
     *
     * @return
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

    public interface OnSubmitListener {
        void onSubmit();
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
        dialogView = inflater.inflate(R.layout.add_expense, container, false);
        log = RoomiesLogger.getInstance();
        log.createEntryLoggingMessage(AddExpenseDialog.class.getName(), "onCreateDialog", null);

        mSharedPref = mContext.getSharedPreferences(RoomiesConstants.PREF_ROOMIES_KEY,
                                                    Context.MODE_PRIVATE);

        mEditor = mSharedPref.edit();
        username = mSharedPref.getString(RoomiesConstants.PREF_USERNAME, null);
        manager = new RoomExpensesManager(mContext);

        description = (EditText) dialogView.findViewById(R.id.description);

        descriptionLayout = (RelativeLayout) dialogView.findViewById(R.id.description_layout);
        descriptionDivider = (RelativeLayout) dialogView.findViewById(R.id.description_divider);
        amount = (TextView) dialogView.findViewById(R.id.amount);
        View amountLayout = dialogView.findViewById(R.id.amount_layout);
        amountLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showNumpad(AMOUNTPAD);
                } else {
                    hideNumpad(AMOUNTPAD);
                }
            }
        });

        quantity = (TextView) dialogView.findViewById(R.id.quantity);
        quantityUnit = (TextView) dialogView.findViewById(R.id.quantity_unit);
        quantityLayout = (RelativeLayout) dialogView.findViewById(R.id.quantity_number);

        quantityLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showNumpad(QUANTITYPAD);
                } else {
                    hideNumpad(QUANTITYPAD);
                }
            }
        });

        quantityUnit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        showQuantityUnitDialog();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }

            private void showQuantityUnitDialog() {

                final View quantityUnitDialog = dialogView.findViewById(R.id.quantity_unit_chooser);
                quantityUnitDialog.setVisibility(View.VISIBLE);

                quantityUnitDialog.findViewById(R.id.ltr).setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantityUnit.setText("LTR");
                        quantityUnitDialog.setVisibility(View.INVISIBLE);
                    }
                });
                quantityUnitDialog.findViewById(R.id.kg).setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantityUnit.setText("KG");
                        quantityUnitDialog.setVisibility(View.INVISIBLE);
                    }
                });
                quantityUnitDialog.findViewById(R.id.pcs).setOnClickListener(new View
                        .OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quantityUnit.setText("PCS");
                        quantityUnitDialog.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        rent = (ImageView) dialogView.findViewById(R.id.rent_icon);
        electricity = (ImageView) dialogView.findViewById(
                R.id.electricity_icon);
        maid = (ImageView) dialogView.findViewById(R.id.maid_icon);
        miscellaneous = (ImageView) dialogView.findViewById(R.id.misc_icon);

        bills = (ImageView) dialogView.findViewById(R.id.bills_icon);
        grocery = (ImageView) dialogView.findViewById(R.id.grocery_icon);
        vegetables = (ImageView) dialogView.findViewById(R.id.vegetable_icon);
        others = (ImageView) dialogView.findViewById(R.id.others_icon);
        miscRow = (LinearLayout) dialogView.findViewById(R.id.misc_row);
        defaultSettings();
        clearUI();
        ShapeDrawable ovalShape = new ShapeDrawable();
        ovalShape.setShape(new OvalShape());
        ovalShape.getPaint().setColor(getResources().getColor(R.color.misc));
        miscellaneous.setBackgroundDrawable(ovalShape);
        Drawable miscIcon = getResources().getDrawable(R.drawable.ic_misc);
        miscIcon.clearColorFilter();
        miscellaneous.setImageDrawable(miscIcon);
        miscellaneous.setSelected(true);
        ShapeDrawable oval = new ShapeDrawable();
        oval.setShape(new OvalShape());
        oval.getPaint().setColor(getResources().getColor(R.color.others));
        others.setBackgroundDrawable(oval);
        Drawable othersIcon = getResources().getDrawable(R.drawable.ic_others);
        othersIcon.clearColorFilter();
        others.setImageDrawable(othersIcon);
        others.setSelected(true);


        dialogView.findViewById(R.id.grocery_btn).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI();
                dialogView.requestFocus();
                ShapeDrawable oval = new ShapeDrawable();
                oval.setShape(new OvalShape());
                oval.getPaint().setColor(getResources().getColor(R.color.grocery));
                grocery.setBackgroundDrawable(oval);
                Drawable groceryIcon = getResources().getDrawable(R.drawable.ic_groceries);
                groceryIcon.clearColorFilter();
                grocery.setImageDrawable(groceryIcon);
                ShapeDrawable ovalShape = new ShapeDrawable();
                ovalShape.setShape(new OvalShape());
                ovalShape.getPaint().setColor(getResources().getColor(R.color.misc));
                miscellaneous.setBackgroundDrawable(ovalShape);
                Drawable miscIcon = getResources().getDrawable(R.drawable.ic_misc);
                miscIcon.clearColorFilter();
                miscellaneous.setImageDrawable(miscIcon);
                miscellaneous.setSelected(true);
                grocery.setSelected(true);
                setupOnClickListener(Controls.GROCERY);
            }
        });
        dialogView.findViewById(R.id.bills_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI();
                dialogView.requestFocus();
                ShapeDrawable oval = new ShapeDrawable();
                oval.setShape(new OvalShape());
                oval.getPaint().setColor(getResources().getColor(R.color.bills));
                bills.setBackgroundDrawable(oval);
                Drawable billsIcon = getResources().getDrawable(R.drawable.ic_bills);
                billsIcon.clearColorFilter();
                bills.setImageDrawable(billsIcon);
                ShapeDrawable ovalShape = new ShapeDrawable();
                ovalShape.setShape(new OvalShape());
                ovalShape.getPaint().setColor(getResources().getColor(R.color.misc));
                miscellaneous.setBackgroundDrawable(ovalShape);
                Drawable miscIcon = getResources().getDrawable(R.drawable.ic_misc);
                miscIcon.clearColorFilter();
                miscellaneous.setImageDrawable(miscIcon);
                miscellaneous.setSelected(true);
                bills.setSelected(true);
                setupOnClickListener(Controls.BILLS);
            }
        });
        dialogView.findViewById(R.id.vegetable_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI();
                dialogView.requestFocus();
                ShapeDrawable oval = new ShapeDrawable();
                oval.setShape(new OvalShape());
                oval.getPaint().setColor(getResources().getColor(R.color.food));
                vegetables.setBackgroundDrawable(oval);
                Drawable vegetableIcon = getResources().getDrawable(R.drawable.ic_restaurant);
                vegetableIcon.clearColorFilter();
                vegetables.setImageDrawable(vegetableIcon);
                ShapeDrawable ovalShape = new ShapeDrawable();
                ovalShape.setShape(new OvalShape());
                ovalShape.getPaint().setColor(getResources().getColor(R.color.misc));
                miscellaneous.setBackgroundDrawable(ovalShape);
                Drawable miscIcon = getResources().getDrawable(R.drawable.ic_misc);
                miscIcon.clearColorFilter();
                miscellaneous.setImageDrawable(miscIcon);
                miscellaneous.setSelected(true);
                vegetables.setSelected(true);
                setupOnClickListener(Controls.VEGETABLES);
            }
        });
        dialogView.findViewById(R.id.others_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI();
                dialogView.requestFocus();
                ShapeDrawable oval = new ShapeDrawable();
                oval.setShape(new OvalShape());
                oval.getPaint().setColor(getResources().getColor(R.color.others));
                others.setBackgroundDrawable(oval);
                Drawable othersIcon = getResources().getDrawable(R.drawable.ic_others);
                othersIcon.clearColorFilter();
                others.setImageDrawable(othersIcon);
                ShapeDrawable ovalShape = new ShapeDrawable();
                ovalShape.setShape(new OvalShape());
                ovalShape.getPaint().setColor(getResources().getColor(R.color.misc));
                miscellaneous.setBackgroundDrawable(ovalShape);
                Drawable miscIcon = getResources().getDrawable(R.drawable.ic_misc);
                miscIcon.clearColorFilter();
                miscellaneous.setImageDrawable(miscIcon);
                miscellaneous.setSelected(true);
                others.setSelected(true);
                setupOnClickListener(Controls.OTHERS);
            }
        });
        dialogView.findViewById(R.id.rent_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI();
                dialogView.requestFocus();
                ShapeDrawable oval = new ShapeDrawable();
                oval.setShape(new OvalShape());
                oval.getPaint().setColor(getResources().getColor(R.color.rent));
                rent.setBackgroundDrawable(oval);
                Drawable rentIcon = getResources().getDrawable(R.drawable.ic_rent);
                rentIcon.clearColorFilter();
                rent.setImageDrawable(rentIcon);
                rent.setSelected(true);
                setupOnClickListener(Controls.RENT);
            }
        });
        dialogView.findViewById(R.id.electricity_btn).setOnClickListener(new View.OnClickListener
                () {
            @Override
            public void onClick(View v) {
                clearUI();
                dialogView.requestFocus();
                ShapeDrawable oval = new ShapeDrawable();
                oval.setShape(new OvalShape());
                oval.getPaint().setColor(getResources().getColor(R.color.electricity));
                electricity.setBackgroundDrawable(oval);
                Drawable electricityIcon = getResources().getDrawable(R.drawable.ic_electricity);
                electricityIcon.clearColorFilter();
                electricity.setImageDrawable(electricityIcon);
                electricity.setSelected(true);
                setupOnClickListener(Controls.ELEC);
            }
        });
        dialogView.findViewById(R.id.maid_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI();
                dialogView.requestFocus();
                ShapeDrawable oval = new ShapeDrawable();
                oval.setShape(new OvalShape());
                oval.getPaint().setColor(getResources().getColor(R.color.maid));
                maid.setBackgroundDrawable(oval);
                Drawable maidIcon = getResources().getDrawable(R.drawable.ic_maid);
                maidIcon.clearColorFilter();
                maid.setImageDrawable(maidIcon);
                maid.setSelected(true);
                setupOnClickListener(Controls.MAID);
            }
        });
        dialogView.findViewById(R.id.misc_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI();
                dialogView.requestFocus();
                ShapeDrawable ovalShape = new ShapeDrawable();
                ovalShape.setShape(new OvalShape());
                ovalShape.getPaint().setColor(getResources().getColor(R.color.misc));
                miscellaneous.setBackgroundDrawable(ovalShape);
                Drawable miscIcon = getResources().getDrawable(R.drawable.ic_misc);
                miscIcon.clearColorFilter();
                miscellaneous.setImageDrawable(miscIcon);
                miscellaneous.setSelected(true);
                ShapeDrawable oval = new ShapeDrawable();
                oval.setShape(new OvalShape());
                oval.getPaint().setColor(getResources().getColor(R.color.others));
                others.setBackgroundDrawable(oval);
                Drawable othersIcon = getResources().getDrawable(R.drawable.ic_others);
                othersIcon.clearColorFilter();
                others.setImageDrawable(othersIcon);
                others.setSelected(true);
                setupOnClickListener(Controls.OTHERS);
            }
        });


        /*Button positive = (Button) dialogView.findViewById(R.id.positiveButton);
        Button negative = (Button) dialogView.findViewById(R.id.negativeButton);*/

       /* builder.setTitle("Add Expenses").setView(dialogView);
        dialog = builder.create();*/
        /*positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isValidDescription = true;
                boolean isValidQuantity = false;
                boolean isValidAmount = RoomiesHelper.setNumericError("amount",
                                                                      getActivity()
                                                                              .getBaseContext(),
                                                                      dialogView);
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
        });*/

        log.createExitLoggingMessage(AddExpenseDialog.class.getName(), "onCreateDialog", null);
        return dialogView;
    }

    private void showNumpad(int numPadType) {
        final View amountPad = dialogView.findViewById(R.id.amount_Pad);
        View amountDivider = dialogView.findViewById(R.id.amount_divider);
        View showAmountPad = dialogView.findViewById(R.id.show_amountpad);
        final View quantitypad = dialogView.findViewById(R.id.quantity_Pad);
        View showQuantityPad = dialogView.findViewById(R.id.show_quantitypad);
        switch (numPadType) {
            case AMOUNTPAD:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = amountDivider.getWidth() - showAmountPad.getWidth();
                    int cy = amountDivider.getHeight();
                    int finalRadius = Math.max(amountPad.getWidth(), amountPad.getHeight());
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(amountPad, cx, cy, 0,
                                                                    finalRadius);
                    anim.setDuration(200);
                    amountPad.setVisibility(View.VISIBLE);
                    anim.start();
                } else {
                    AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
                    alpha.setDuration(200);
                    amountPad.setAnimation(alpha);
                    alpha.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            amountPad.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
                break;
            case QUANTITYPAD:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = quantityLayout.getWidth() - showQuantityPad.getWidth();
                    int cy = quantityLayout.getBottom();
                    int finalRadius = Math.max(quantitypad.getWidth(), quantitypad.getHeight());
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(quantitypad, cx, cy, 0,
                                                                    finalRadius);
                    anim.setDuration(200);
                    quantitypad.setVisibility(View.VISIBLE);
                    anim.start();
                } else {
                    AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
                    alpha.setDuration(200);
                    quantitypad.setAnimation(alpha);
                    alpha.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            quantitypad.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
                break;
        }


    }

    private void hideNumpad(int numpadType) {
        final View amountPad = dialogView.findViewById(R.id.amount_Pad);
        View amountDivider = dialogView.findViewById(R.id.amount_divider);
        View showAmountPad = dialogView.findViewById(R.id.show_amountpad);
        final View quantitypad = dialogView.findViewById(R.id.quantity_Pad);
        View showQuantityPad = dialogView.findViewById(R.id.show_quantitypad);
        switch (numpadType) {
            case AMOUNTPAD:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = amountDivider.getWidth() - showAmountPad.getWidth();
                    int cy = amountDivider.getHeight();
                    int finalRadius = Math.max(amountPad.getWidth(), amountPad.getHeight());
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(amountPad, cx, cy, finalRadius,
                                                                    0);
                    anim.setDuration(200);
                    amountPad.setVisibility(View.INVISIBLE);
                    anim.start();
                } else {
                    AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
                    alpha.setDuration(200);
                    amountPad.setAnimation(alpha);
                    alpha.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            amountPad.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
                break;
            case QUANTITYPAD:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = quantityLayout.getWidth() - showQuantityPad.getWidth();
                    int cy = quantityLayout.getBottom();
                    int finalRadius = Math.max(quantitypad.getWidth(), quantitypad.getHeight());
                    Animator anim =
                            ViewAnimationUtils.createCircularReveal(quantitypad, cx, cy,
                                                                    finalRadius,
                                                                    0);
                    anim.setDuration(200);
                    quantitypad.setVisibility(View.INVISIBLE);
                    anim.start();
                } else {
                    AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
                    alpha.setDuration(200);
                    quantitypad.setAnimation(alpha);
                    alpha.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            quantitypad.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
                break;
        }

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
    public void submitExpenses() {

        log.createEntryLoggingMessage(AddExpenseDialog.class.getName(), "submitExpenses",
                                      null);

        float amountVal = Float.valueOf(amount.getText().toString());
        String quantityVal = null;
        String descriptionVal = null;
        if (View.VISIBLE == quantity.getVisibility()) {
            quantityVal = Float.valueOf(quantity.getText().toString())
                          + "KG";
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
                                                            subCategory, descriptionVal,
                                                            quantityVal, amountVal);
                    if (isExpenseAdded) {
                        mEditor.putString(PREF_MISCELLANEOUS_SPENT, String.valueOf(miscSpent +
                                                                                   amountVal));
                        mEditor.apply();
                    }
                }
        }

        if (isExpenseAdded && null != dialog) {
            updateGraphs(dialog);
            mOnSubmitListener.onSubmit();
            dialog.dismiss();
        }
        log.createExitLoggingMessage(AddExpenseDialog.class.getName(), "submitExpenses", null);

    }

    /**
     * Update the UI with the recently added expense.
     *
     * @param dialog
     */
    private void updateGraphs(Dialog dialog) {
        RoomExpenses roomExpenses = new RoomExpenses();
        roomExpenses.setExpenseCategory(category.toString());
        if (null != subCategory) {
            roomExpenses.setExpenseSubcategory(subCategory.toString());
        }
        if (null != description) {
            roomExpenses.setDescription(description.getText().toString());
        }
        if (null != quantity) {
            roomExpenses.setQuantity(quantity.getText().toString());
        }
        roomExpenses.setAmount(Float.valueOf(amount.getText().toString()));
        roomExpenses.setExpenseDate(new Date());
        roomExpenses.setMonthYear(DateUtils.getCurrentMonthYear());
        roomExpenses.setUserId(Integer.parseInt(mSharedPref.getString(PREF_USER_ID, "0")));
        roomExpenses.setRoomId(Integer.parseInt(mSharedPref.getString(PREF_ROOM_ID, "0")));
        RoomiesFragment fragment = (RoomiesFragment) getActivity().getSupportFragmentManager()
                .getFragments().get(0);
        if (fragment instanceof HomeFragment) {
            HomeFragment.RoomiesHomePagerAdapter adapter = ((HomeFragment.RoomiesHomePagerAdapter)
                    ((HomeFragment) fragment).getTab().getViewPager().getAdapter());
            ((DashboardTab) adapter.getRegisteredFragment(1)).update(roomExpenses);

        } else if (fragment instanceof RoommatesFragment) {

        }
        dialog.dismiss();
    }


    /**
     * set up the default setting for the options
     */
    private void defaultSettings() {

        category = Category.MISCELLANEOUS;
        subCategory = SubCategory.OTHERS;
        miscellaneous.setSelected(true);
        miscRow.setVisibility(View.VISIBLE);
        quantityLayout.setVisibility(View.VISIBLE);
        descriptionLayout.setVisibility(View.VISIBLE);
        descriptionDivider.setVisibility(View.VISIBLE);
        quantityUnit.setVisibility(View.VISIBLE);
    }

    private void clearUI() {
        Drawable groceryIcon = getResources().getDrawable(R.drawable.ic_groceries);
        groceryIcon.setColorFilter(getResources().getColor(R.color.grocery), PorterDuff.Mode
                .MULTIPLY);
        grocery.setImageDrawable(groceryIcon);
        grocery.setBackgroundDrawable(getResources().getDrawable(R.drawable.grocery_toggle));

        Drawable billsIcon = getResources().getDrawable(R.drawable.ic_bills);
        billsIcon.setColorFilter(getResources().getColor(R.color.bills), PorterDuff.Mode.MULTIPLY);
        bills.setImageDrawable(billsIcon);
        bills.setBackgroundDrawable(getResources().getDrawable(R.drawable.bills_toggle));

        Drawable vegetablesIcon = getResources().getDrawable(R.drawable.ic_restaurant);
        vegetablesIcon.setColorFilter(getResources().getColor(R.color.food), PorterDuff.Mode
                .MULTIPLY);
        vegetables.setImageDrawable(vegetablesIcon);
        vegetables.setBackgroundDrawable(getResources().getDrawable(R.drawable.vegetables_toggle));

        Drawable othersIcon = getResources().getDrawable(R.drawable.ic_others);
        othersIcon.setColorFilter(getResources().getColor(R.color.others), PorterDuff.Mode
                .MULTIPLY);
        others.setImageDrawable(othersIcon);
        others.setBackgroundDrawable(getResources().getDrawable(R.drawable.others_toggle));

        Drawable electricityIcon = getResources().getDrawable(R.drawable.ic_electricity);
        electricityIcon.setColorFilter(getResources().getColor(R.color.electricity), PorterDuff
                .Mode.MULTIPLY);
        electricity.setImageDrawable(electricityIcon);
        electricity.setBackgroundDrawable(getResources().getDrawable(R.drawable
                                                                             .electricity_toggle));

        Drawable rentIcon = getResources().getDrawable(R.drawable.ic_rent);
        rentIcon.setColorFilter(getResources().getColor(R.color.rent), PorterDuff.Mode.MULTIPLY);
        rent.setImageDrawable(rentIcon);
        rent.setBackgroundDrawable(getResources().getDrawable(R.drawable.rent_toggle));

        Drawable maidIcon = getResources().getDrawable(R.drawable.ic_maid);
        maidIcon.setColorFilter(getResources().getColor(R.color.maid), PorterDuff.Mode.MULTIPLY);
        maid.setImageDrawable(maidIcon);
        maid.setBackgroundDrawable(getResources().getDrawable(R.drawable.maid_toggle));

        Drawable miscIcon = getResources().getDrawable(R.drawable.ic_misc);
        miscIcon.setColorFilter(getResources().getColor(R.color.misc), PorterDuff.Mode.MULTIPLY);
        miscellaneous.setImageDrawable(miscIcon);
        miscellaneous.setBackgroundDrawable(getResources().getDrawable(R.drawable.misc_toggle));
    }

    /**
     * set up the on click listeners for the buttons.
     *
     * @param controls
     */
    private void setupOnClickListener(Controls controls) {

        category = null;
        subCategory = null;
        rent.setSelected(false);
        maid.setSelected(false);
        electricity.setSelected(false);
        miscellaneous.setSelected(false);
        others.setSelected(true);
        bills.setSelected(false);
        vegetables.setSelected(false);
        grocery.setSelected(false);

        switch (controls) {
            case RENT:
                if (!rent.isSelected()) {
                    rent.setSelected(true);
                    category = Category.RENT;
                    miscRow.setVisibility(View.GONE);
                    quantityLayout.setVisibility(View.GONE);
                    quantityUnit.setVisibility(View.GONE);
                    descriptionLayout.setVisibility(View.GONE);
                    descriptionDivider.setVisibility(View.GONE);
                    break;
                }
            case MAID:
                if (!maid.isSelected()) {
                    maid.setSelected(true);
                    category = Category.MAID;
                    miscRow.setVisibility(View.GONE);
                    quantityLayout.setVisibility(View.GONE);
                    quantityUnit.setVisibility(View.GONE);
                    descriptionLayout.setVisibility(View.GONE);
                    descriptionDivider.setVisibility(View.GONE);
                    break;
                }
            case ELEC:
                if (!electricity.isSelected()) {
                    electricity.setSelected(true);
                    category = Category.ELECTRICITY;
                    miscRow.setVisibility(View.GONE);
                    quantityLayout.setVisibility(View.GONE);
                    quantityUnit.setVisibility(View.GONE);
                    descriptionLayout.setVisibility(View.GONE);
                    descriptionDivider.setVisibility(View.GONE);
                    break;
                }
            case MISC:
                defaultSettings();
                break;
            case BILLS:
                if (!bills.isSelected()) {
                    category = Category.MISCELLANEOUS;
                    subCategory = SubCategory.BILLS;
                    miscellaneous.setSelected(true);
                    others.setSelected(false);
                    bills.setSelected(true);
                    break;
                }
            case GROCERY:
                if (!grocery.isSelected()) {
                    category = Category.MISCELLANEOUS;
                    subCategory = SubCategory.GROCERY;
                    miscellaneous.setSelected(true);
                    others.setSelected(false);
                    grocery.setSelected(true);
                    break;
                }
            case VEGETABLES:
                if (!vegetables.isSelected()) {
                    category = Category.MISCELLANEOUS;
                    subCategory = SubCategory.VEGETABLES;
                    miscellaneous.setSelected(true);
                    others.setSelected(false);
                    vegetables.setSelected(true);
                    break;
                }
            case OTHERS:
                defaultSettings();
                break;
            default:
                defaultSettings();
        }

    }

    /**
     * Check if the expenses are within the pre defined range.
     *
     * @param category
     * @param margin
     * @param spent
     */
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
                                                                             submitExpenses();
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

    /**
     *
     */
    public enum Controls {
        RENT, MAID, ELEC, MISC, BILLS, GROCERY, VEGETABLES, OTHERS
    }
}
