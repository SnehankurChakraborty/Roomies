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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import com.phaseii.rxm.roomies.util.RoomiesHelper;
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
    private SharedPreferences mSharedPref;
    private SharedPreferences.Editor mEditor;
    private Category category;
    private SubCategory subCategory;
    private RoomExpensesManager manager;
    private boolean isExceedConfirmed;
    private OnSubmitListener mOnSubmitListener;
    private View amountPad;
    private View quantitypad;

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
        final View dialogView = inflater.inflate(R.layout.add_expense, container, false);
        log = RoomiesLogger.getInstance();
        log.createEntryLoggingMessage(AddExpenseDialog.class.getName(), "onCreateDialog", null);

        mSharedPref = mContext.getSharedPreferences(RoomiesConstants.PREF_ROOMIES_KEY,
                                                    Context.MODE_PRIVATE);

        mEditor = mSharedPref.edit();
        manager = new RoomExpensesManager(mContext);

        final EditText description = (EditText) dialogView.findViewById(R.id.description);
        amountPad = dialogView.findViewById(R.id.amount_Pad);
        final RelativeLayout descriptionLayout = (RelativeLayout) dialogView.findViewById(R.id.description_layout);
        quantitypad = dialogView.findViewById(R.id.quantity_Pad);
        final RelativeLayout descriptionDivider = (RelativeLayout) dialogView.findViewById(R.id.description_divider);
        final TextView amount = (TextView) dialogView.findViewById(R.id.amount);
        final TextView quantity = (TextView) dialogView.findViewById(R.id.quantity);
        View amountLayout = dialogView.findViewById(R.id.amount_layout);
        amountLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showNumpad(dialogView, AMOUNTPAD, amount, quantity);
                } else {
                    hideNumpad(dialogView, AMOUNTPAD);
                }
            }
        });
        amountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumpad(dialogView, AMOUNTPAD, amount, quantity);
            }
        });

        final TextView quantityUnit = (TextView) dialogView.findViewById(R.id.quantity_unit);
        final RelativeLayout quantityLayout = (RelativeLayout) dialogView.findViewById(R.id.quantity_number);

        final View quantityUnitDialog = dialogView.findViewById(R.id.quantity_unit_chooser);

        quantityLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showNumpad(dialogView, QUANTITYPAD, amount, quantity);
                } else {
                    hideNumpad(dialogView, QUANTITYPAD);
                }
            }
        });
        quantityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNumpad(dialogView, QUANTITYPAD, amount, quantity);
            }
        });


        quantityUnit.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showQuantityUnitDialog(quantityUnitDialog, quantityUnit);
                } else {
                    dismissQuantityUnitDialog(quantityUnit);
                }
            }
        });

        quantityUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (View.INVISIBLE == quantityUnitDialog.getVisibility()) {
                    showQuantityUnitDialog(quantityUnitDialog, quantityUnit);
                }

            }
        });

        final ImageView rent = (ImageView) dialogView.findViewById(R.id.rent_icon);
        final ImageView electricity = (ImageView) dialogView.findViewById(
                R.id.electricity_icon);
        final ImageView maid = (ImageView) dialogView.findViewById(R.id.maid_icon);
        final ImageView miscellaneous = (ImageView) dialogView.findViewById(R.id.misc_icon);

        final ImageView bills = (ImageView) dialogView.findViewById(R.id.bills_icon);
        final ImageView grocery = (ImageView) dialogView.findViewById(R.id.grocery_icon);
        final ImageView vegetables = (ImageView) dialogView.findViewById(R.id.vegetable_icon);
        final ImageView others = (ImageView) dialogView.findViewById(R.id.others_icon);
        final LinearLayout miscRow = (LinearLayout) dialogView.findViewById(R.id.misc_row);
        defaultSettings(miscellaneous, miscRow, quantityLayout, descriptionLayout,
                        descriptionDivider, quantityUnit);
        clearUI(grocery, bills, vegetables, others, electricity, rent, maid, miscellaneous);
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
                clearUI(grocery, bills, vegetables, others, electricity, rent, maid, miscellaneous);
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
                setupOnClickListener(Controls.GROCERY, grocery, bills, vegetables, others,
                                     electricity, rent, maid, miscellaneous, miscRow,
                                     quantityLayout, quantityUnit, descriptionLayout,
                                     descriptionDivider);
            }
        });
        dialogView.findViewById(R.id.bills_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI(grocery, bills, vegetables, others, electricity, rent, maid, miscellaneous);
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
                setupOnClickListener(Controls.BILLS, grocery, bills, vegetables, others,
                                     electricity, rent, maid, miscellaneous, miscRow,
                                     quantityLayout, quantityUnit, descriptionLayout,
                                     descriptionDivider);
            }
        });
        dialogView.findViewById(R.id.vegetable_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI(grocery, bills, vegetables, others, electricity, rent, maid, miscellaneous);
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
                setupOnClickListener(Controls.VEGETABLES, grocery, bills, vegetables, others,
                                     electricity, rent, maid, miscellaneous, miscRow,
                                     quantityLayout, quantityUnit, descriptionLayout,
                                     descriptionDivider);
            }
        });
        dialogView.findViewById(R.id.others_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI(grocery, bills, vegetables, others, electricity, rent, maid, miscellaneous);
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
                setupOnClickListener(Controls.OTHERS, grocery, bills, vegetables, others,
                                     electricity, rent, maid, miscellaneous, miscRow,
                                     quantityLayout, quantityUnit, descriptionLayout,
                                     descriptionDivider);
            }
        });
        dialogView.findViewById(R.id.rent_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI(grocery, bills, vegetables, others, electricity, rent, maid, miscellaneous);
                dialogView.requestFocus();
                ShapeDrawable oval = new ShapeDrawable();
                oval.setShape(new OvalShape());
                oval.getPaint().setColor(getResources().getColor(R.color.rent));
                rent.setBackgroundDrawable(oval);
                Drawable rentIcon = getResources().getDrawable(R.drawable.ic_rent);
                rentIcon.clearColorFilter();
                rent.setImageDrawable(rentIcon);
                rent.setSelected(true);
                setupOnClickListener(Controls.RENT, grocery, bills, vegetables, others,
                                     electricity, rent, maid, miscellaneous, miscRow,
                                     quantityLayout, quantityUnit, descriptionLayout,
                                     descriptionDivider);
            }
        });
        dialogView.findViewById(R.id.electricity_btn).setOnClickListener(new View.OnClickListener
                () {
            @Override
            public void onClick(View v) {
                clearUI(grocery, bills, vegetables, others, electricity, rent, maid, miscellaneous);
                dialogView.requestFocus();
                ShapeDrawable oval = new ShapeDrawable();
                oval.setShape(new OvalShape());
                oval.getPaint().setColor(getResources().getColor(R.color.electricity));
                electricity.setBackgroundDrawable(oval);
                Drawable electricityIcon = getResources().getDrawable(R.drawable.ic_electricity);
                electricityIcon.clearColorFilter();
                electricity.setImageDrawable(electricityIcon);
                electricity.setSelected(true);
                setupOnClickListener(Controls.ELEC, grocery, bills, vegetables, others,
                                     electricity, rent, maid, miscellaneous, miscRow,
                                     quantityLayout, quantityUnit, descriptionLayout,
                                     descriptionDivider);
            }
        });
        dialogView.findViewById(R.id.maid_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI(grocery, bills, vegetables, others, electricity, rent, maid, miscellaneous);
                dialogView.requestFocus();
                ShapeDrawable oval = new ShapeDrawable();
                oval.setShape(new OvalShape());
                oval.getPaint().setColor(getResources().getColor(R.color.maid));
                maid.setBackgroundDrawable(oval);
                Drawable maidIcon = getResources().getDrawable(R.drawable.ic_maid);
                maidIcon.clearColorFilter();
                maid.setImageDrawable(maidIcon);
                maid.setSelected(true);
                setupOnClickListener(Controls.MAID, grocery, bills, vegetables, others,
                                     electricity, rent, maid, miscellaneous, miscRow,
                                     quantityLayout, quantityUnit, descriptionLayout,
                                     descriptionDivider);
            }
        });
        dialogView.findViewById(R.id.misc_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearUI(grocery, bills, vegetables, others, electricity, rent, maid, miscellaneous);
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
                setupOnClickListener(Controls.OTHERS, grocery, bills, vegetables, others,
                                     electricity, rent, maid, miscellaneous, miscRow,
                                     quantityLayout, quantityUnit, descriptionLayout,
                                     descriptionDivider);
            }
        });


        TextView positive = (TextView) dialogView.findViewById(R.id.positiveButton);
        ImageView negative = (ImageView) dialogView.findViewById(R.id.negativeButton);

       /* builder.setTitle("Add Expenses").setView(dialogView);
        dialog = builder.create();*/
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
                if (quantityLayout.getVisibility() == View.VISIBLE) {
                    isValidQuantity = RoomiesHelper.setNumericError("quantity",
                                                                    mContext, dialogView);
                } else {
                    isValidQuantity = true;
                }
                if (isValidAmount && isValidQuantity && isValidDescription) {
                    submitExpenses(amount, quantityLayout, quantity, descriptionLayout,
                                   description);
                }
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        log.createExitLoggingMessage(AddExpenseDialog.class.getName(), "onCreateDialog", null);
        return dialogView;
    }

    private void showNumpad(View dialogView, int numPadType, TextView amount, TextView quantity) {
        View amountDivider = dialogView.findViewById(R.id.amount_divider);
        View showAmountPad = dialogView.findViewById(R.id.show_amountpad);
        View showQuantityPad = dialogView.findViewById(R.id.show_quantitypad);
        View quantityLayout = dialogView.findViewById(R.id.quantity_layout);
        switch (numPadType) {
            case AMOUNTPAD:
                if (View.INVISIBLE == amountPad.getVisibility()) {
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
                    setupKeypad(amountPad, amount);
                }
                break;
            case QUANTITYPAD:
                if (View.INVISIBLE == quantitypad.getVisibility()) {
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
                    setupKeypad(quantitypad, quantity);
                }
                break;
        }


    }

    private void hideNumpad(View dialogView, int numpadType) {
        final View amountPad = dialogView.findViewById(R.id.amount_Pad);
        View amountDivider = dialogView.findViewById(R.id.amount_divider);
        View showAmountPad = dialogView.findViewById(R.id.show_amountpad);
        final View quantitypad = dialogView.findViewById(R.id.quantity_Pad);
        View showQuantityPad = dialogView.findViewById(R.id.show_quantitypad);
        View quantityLayout = dialogView.findViewById(R.id.quantity_layout);
        switch (numpadType) {
            case AMOUNTPAD:
                if (View.VISIBLE == amountPad.getVisibility()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        int cx = amountDivider.getWidth() - showAmountPad.getWidth();
                        int cy = amountDivider.getHeight();
                        int finalRadius = Math.max(amountPad.getWidth(), amountPad.getHeight());
                        Animator anim =
                                ViewAnimationUtils.createCircularReveal(amountPad, cx, cy,
                                                                        finalRadius,

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
                }
                break;
            case QUANTITYPAD:
                if (View.VISIBLE == quantitypad.getVisibility()) {
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
                }
                break;
        }

    }

    private void showQuantityUnitDialog(final View quantityUnitDialog, final TextView
            quantityUnit) {

        if (View.INVISIBLE == quantityUnitDialog.getVisibility()) {
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
    }

    private void dismissQuantityUnitDialog(final View quantityUnitDialog) {
        if (View.VISIBLE == quantityUnitDialog.getVisibility()) {
            quantityUnitDialog.setVisibility(View.INVISIBLE);
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

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (View.VISIBLE == amountPad.getVisibility()) {
                        amountPad.setVisibility(View.INVISIBLE);
                        return false;
                    } else if (View.VISIBLE == quantitypad.getVisibility()) {
                        quantitypad.setVisibility(View.INVISIBLE);
                        return true;
                    } else {
                        dismiss();
                        return false;
                    }
                } else {
                    return false;
                }
            }
        });
    }

    /**
     * Submit expenses.
     */
    public void submitExpenses(TextView amount, View quantityLayout, TextView quantity, View
            descriptionLayout, TextView description) {

        log.createEntryLoggingMessage(AddExpenseDialog.class.getName(), "submitExpenses",
                                      null);

        float amountVal = Float.valueOf(amount.getText().toString());
        String quantityVal = null;
        String descriptionVal = null;
        if (View.VISIBLE == quantityLayout.getVisibility()) {
            quantityVal = Float.valueOf(quantity.getText().toString())
                          + "KG";
        }
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
                                             quantity, descriptionLayout, description);
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
                                             String.valueOf(maidSpent), amount, quantityLayout,
                                             quantity, descriptionLayout, description);
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
                                             String.valueOf(elecSpent), amount, quantityLayout,
                                             quantity, descriptionLayout, description);
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
                                             String.valueOf(miscSpent), amount, quantityLayout,
                                             quantity, descriptionLayout, description);
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

        if (isExpenseAdded) {
            updateGraphs(description, quantity, amount);
            mOnSubmitListener.onSubmit();
            dismiss();
        }
        log.createExitLoggingMessage(AddExpenseDialog.class.getName(), "submitExpenses", null);

    }

    /**
     * Update the UI with the recently added expense.
     */
    private void updateGraphs(TextView description, TextView quantity, TextView amount) {
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
        dismiss();
    }


    /**
     * set up the default setting for the options
     */
    private void defaultSettings(ImageView miscellaneous, View miscRow, View quantityLayout, View
            descriptionLayout, View descriptionDivider, View quantityUnit) {

        category = Category.MISCELLANEOUS;
        subCategory = SubCategory.OTHERS;
        miscellaneous.setSelected(true);
        miscRow.setVisibility(View.VISIBLE);
        quantityLayout.setVisibility(View.VISIBLE);
        descriptionLayout.setVisibility(View.VISIBLE);
        descriptionDivider.setVisibility(View.VISIBLE);
        quantityUnit.setVisibility(View.VISIBLE);
    }

    private void clearUI(ImageView grocery, ImageView bills, ImageView vegetables, ImageView
            others, ImageView electricity, ImageView rent, ImageView maid, ImageView
                                 miscellaneous) {
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
    private void setupOnClickListener(Controls controls, ImageView grocery, ImageView bills,
                                      ImageView vegetables, ImageView others, ImageView
                                              electricity, ImageView rent, ImageView maid,
                                      ImageView miscellaneous, View miscRow, View quantityLayout,
                                      View quantityUnit, View descriptionLayout, View
                                              descriptionDivider) {

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
                defaultSettings(miscellaneous, miscRow, quantityLayout, descriptionLayout,
                                descriptionDivider, quantityUnit);
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
                defaultSettings(miscellaneous, miscRow, quantityLayout, descriptionLayout,
                                descriptionDivider, quantityUnit);
                break;
            default:
                defaultSettings(miscellaneous, miscRow, quantityLayout, descriptionLayout,
                                descriptionDivider, quantityUnit);
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
            spent, final TextView amount, final View quantityLayout, final TextView quantity,
                                          final View descriptionLayout, final TextView
                                                  description) {

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
                                                                                      quantityLayout, quantity, descriptionLayout, description);
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


    private void setupKeypad(final View keypad, final TextView textView) {
        keypad.findViewById(R.id.one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClicked(1, textView);
            }
        });
        keypad.findViewById(R.id.two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClicked(2, textView);
            }
        });
        keypad.findViewById(R.id.three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClicked(3, textView);
            }
        });
        keypad.findViewById(R.id.four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClicked(4, textView);
            }
        });
        keypad.findViewById(R.id.five).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClicked(5, textView);
            }
        });
        keypad.findViewById(R.id.six).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClicked(6, textView);
            }
        });
        keypad.findViewById(R.id.seven).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClicked(7, textView);
            }
        });
        keypad.findViewById(R.id.eight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClicked(8, textView);
            }
        });
        keypad.findViewById(R.id.nine).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClicked(9, textView);
            }
        });
        keypad.findViewById(R.id.zero).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClicked(0, textView);
            }
        });
        keypad.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKeyClicked(10, textView);
            }
        });
        keypad.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keypad.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void onKeyClicked(int key, TextView textView) {
        switch (key) {
            case 0:
                String str = textView.getText().toString();
                if (!str.equals("")) {
                    textView.append("0");
                }
                break;
            case 1:
                textView.append("1");
                break;
            case 2:
                textView.append("2");
                break;
            case 3:
                textView.append("3");
                break;
            case 4:
                textView.append("4");
                break;
            case 5:
                textView.append("5");
                break;
            case 6:
                textView.append("6");
                break;
            case 7:
                textView.append("7");
                break;
            case 8:
                textView.append("8");
                break;
            case 9:
                textView.append("9");
                break;
            case 10:
                String text = textView.getText().toString();
                if (text.length() > 0) {
                    String textStr = new StringBuilder(text)
                            .deleteCharAt(text.length() - 1).toString();
                    textView.setText(textStr);
                }
                break;

        }
    }
}
