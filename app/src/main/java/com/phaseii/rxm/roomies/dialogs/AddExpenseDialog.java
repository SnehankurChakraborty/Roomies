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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.fragments.HomeFragment;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.service.MiscService;
import com.phaseii.rxm.roomies.service.MiscServiceImpl;
import com.phaseii.rxm.roomies.service.RoomService;
import com.phaseii.rxm.roomies.service.RoomServiceImpl;
import com.phaseii.rxm.roomies.tabs.DetailExpenseTab;

/**
 * Created by Snehankur on 4/5/2015.
 */
public class AddExpenseDialog extends DialogFragment implements DialogInterface.OnShowListener {

	Context mContext;
	ArrayAdapter<CharSequence> mCategoryAdapter;
	ArrayAdapter<CharSequence> mSubCategoryAdapter;

	String category = RoomiesConstants.MISCELLANEOUS;
	String subCategory = RoomiesConstants.OTHERS;
	EditText amount;
	EditText description;
	EditText quantity;
	Button positive;
	Button negative;
	RadioGroup quantityRadio;
	static int pagerId;
	String username;


	public static AddExpenseDialog getInstance(int pagerId) {
		AddExpenseDialog.pagerId = pagerId;
		return new AddExpenseDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity().getApplicationContext();
		username = mContext.getSharedPreferences(RoomiesConstants.ROOM_INFO_FILE_KEY,
				Context.MODE_PRIVATE).getString(RoomiesConstants.NAME, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.add_expense_dilog, null);
		description = (EditText) dialogView.findViewById(R.id.description);
		amount = (EditText) dialogView.findViewById(R.id.amount);
		quantity = (EditText) dialogView.findViewById(R.id.quantity);
		quantityRadio=(RadioGroup)dialogView.findViewById(R.id.quantity_radio);
		final ToggleButton rent = (ToggleButton) dialogView.findViewById(R.id.rent_icon);
		final ToggleButton electricity = (ToggleButton) dialogView.findViewById(
				R.id.electricity_icon);
		final ToggleButton maid = (ToggleButton) dialogView.findViewById(R.id.maid_icon);
		final ToggleButton miscellaneous = (ToggleButton) dialogView.findViewById(R.id.misc_icon);
		final ToggleButton bills = (ToggleButton) dialogView.findViewById(R.id.bills_icon);
		final ToggleButton grocery = (ToggleButton) dialogView.findViewById(R.id.grocery_icon);
		final ToggleButton vegetables = (ToggleButton) dialogView.findViewById(R.id.vegetable_icon);
		final ToggleButton others = (ToggleButton) dialogView.findViewById(R.id.others_icon);
		final TableRow miscRow = (TableRow) dialogView.findViewById(R.id.misc_row);
		miscellaneous.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (miscellaneous.isChecked()) {
					category = RoomiesConstants.MISCELLANEOUS;

					rent.setChecked(false);
					maid.setChecked(false);
					electricity.setChecked(false);

					if (miscRow.getVisibility() == View.GONE) {
						miscRow.setVisibility(View.VISIBLE);
						others.setChecked(true);
						bills.setChecked(false);
						vegetables.setChecked(false);
						grocery.setChecked(false);
						category = RoomiesConstants.OTHERS;
					}

					if (description.getVisibility() == View.GONE) {
						description.setVisibility(View.VISIBLE);
					}
					if (quantity.getVisibility() == View.GONE) {
						quantity.setVisibility(View.VISIBLE);
						quantityRadio.setVisibility(View.VISIBLE);
					}
				} else {
					miscellaneous.setChecked(true);
/*
					if (description.getVisibility() == View.VISIBLE) {
						description.setVisibility(View.GONE);
					}
					if (quantity.getVisibility() == View.VISIBLE) {
						quantity.setVisibility(View.GONE);
					}
					if (miscRow.getVisibility() == View.VISIBLE) {
						miscRow.setVisibility(View.GONE);
					}*/
				}
			}
		});

		miscellaneous.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					category = RoomiesConstants.MISCELLANEOUS;

					rent.setChecked(false);
					maid.setChecked(false);
					electricity.setChecked(false);

					if (miscRow.getVisibility() == View.GONE) {
						miscRow.setVisibility(View.VISIBLE);
						others.setChecked(true);
						bills.setChecked(false);
						vegetables.setChecked(false);
						grocery.setChecked(false);
						category = RoomiesConstants.OTHERS;
					}

					if (description.getVisibility() == View.GONE) {
						description.setVisibility(View.VISIBLE);
					}
					if (quantity.getVisibility() == View.GONE) {
						quantity.setVisibility(View.VISIBLE);
						quantityRadio.setVisibility(View.VISIBLE);
					}
				}
			}
		});


		rent.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (rent.isChecked()) {
					category = RoomiesConstants.RENT;

					miscellaneous.setChecked(false);
					maid.setChecked(false);
					electricity.setChecked(false);

					if (description.getVisibility() == View.VISIBLE) {
						description.setVisibility(View.GONE);
					}
					if (quantity.getVisibility() == View.VISIBLE) {
						quantity.setVisibility(View.GONE);
						quantityRadio.setVisibility(View.GONE);
					}
					if (miscRow.getVisibility() == View.VISIBLE) {
						miscRow.setVisibility(View.GONE);
					}
				} else {
					miscellaneous.setChecked(true);
				}
			}
		});

		electricity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (electricity.isChecked()) {
					category = RoomiesConstants.ELECTRICITY;

					rent.setChecked(false);
					maid.setChecked(false);
					miscellaneous.setChecked(false);

					if (description.getVisibility() == View.VISIBLE) {
						description.setVisibility(View.GONE);
					}
					if (quantity.getVisibility() == View.VISIBLE) {
						quantity.setVisibility(View.GONE);
						quantityRadio.setVisibility(View.GONE);
					}
					if (miscRow.getVisibility() == View.VISIBLE) {
						miscRow.setVisibility(View.GONE);
					}
				} else {
					miscellaneous.setChecked(true);
				}
			}
		});
		maid.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (maid.isChecked()) {
					category = RoomiesConstants.MAID;

					rent.setChecked(false);
					electricity.setChecked(false);
					miscellaneous.setChecked(false);

					if (description.getVisibility() == View.VISIBLE) {
						description.setVisibility(View.GONE);
					}
					if (quantity.getVisibility() == View.VISIBLE) {
						quantity.setVisibility(View.GONE);
						quantityRadio.setVisibility(View.GONE);
					}
					if (miscRow.getVisibility() == View.VISIBLE) {
						miscRow.setVisibility(View.GONE);
					}
				} else {
					miscellaneous.setChecked(true);
				}
			}
		});

		bills.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bills.isChecked()) {
					subCategory = RoomiesConstants.BILLS;
					grocery.setChecked(false);
					vegetables.setChecked(false);
					others.setChecked(false);
					if (quantity.getVisibility() == View.VISIBLE) {
						quantity.setVisibility(View.GONE);
						quantityRadio.setVisibility(View.GONE);
					}
				} else {
					if (quantity.getVisibility() == View.GONE) {
						quantity.setVisibility(View.VISIBLE);
						quantityRadio.setVisibility(View.GONE);
					}
					others.setChecked(true);
				}
			}
		});

		grocery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (grocery.isChecked()) {
					subCategory = RoomiesConstants.GROCERY;
					bills.setChecked(false);
					vegetables.setChecked(false);
					others.setChecked(false);
					if (quantity.getVisibility() == View.GONE) {
						quantity.setVisibility(View.VISIBLE);
						quantityRadio.setVisibility(View.VISIBLE);
					}
				} else {
					others.setChecked(true);
				}

			}
		});

		vegetables.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (vegetables.isChecked()) {
					subCategory = RoomiesConstants.VEGETABLES;
					grocery.setChecked(false);
					bills.setChecked(false);
					others.setChecked(false);
					if (quantity.getVisibility() == View.GONE) {
						quantity.setVisibility(View.VISIBLE);
						quantityRadio.setVisibility(View.VISIBLE);
					}
				} else {
					others.setChecked(true);
				}
			}
		});

		others.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (others.isChecked()) {
					subCategory = RoomiesConstants.OTHERS;
					grocery.setChecked(false);
					vegetables.setChecked(false);
					bills.setChecked(false);
					if (quantity.getVisibility() == View.GONE) {
						quantity.setVisibility(View.VISIBLE);
						quantityRadio.setVisibility(View.VISIBLE);
					}
				} else {
					others.setChecked(true);
				}
			}
		});

		others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					subCategory = RoomiesConstants.OTHERS;
					grocery.setChecked(false);
					vegetables.setChecked(false);
					bills.setChecked(false);
					if (quantity.getVisibility() == View.GONE) {
						quantity.setVisibility(View.VISIBLE);
						quantityRadio.setVisibility(View.VISIBLE);
					}
				}
			}
		});


		/*	@Override
			public void onItemSelected(AdapterView<?> parent, View view,
			                           int position, long id) {
				category = (String) parent.getItemAtPosition(position);
				String categories[] = getResources().getStringArray(R.array.category);
				if (!category.equals(categories[categories.length - 1])) {
					subCategorySpinner.setEnabled(false);
					description.setEnabled(false);
					quantity.setEnabled(false);
					TextView descriptionError = (TextView) dialogView.findViewById(R.id
							.description_error);
					if (descriptionError.getVisibility() == View.VISIBLE) {
						descriptionError.setVisibility(View.INVISIBLE);
					}
					TextView quantityError = (TextView) dialogView.findViewById(R.id
							.quantity_error);
					if (quantityError.getVisibility() == View.VISIBLE) {
						quantityError.setVisibility(View.INVISIBLE);
					}
				} else {
					subCategorySpinner.setEnabled(true);
					description.setEnabled(true);
					quantity.setEnabled(true);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});*/


		/*
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
			                           int position, long id) {
				subCategory = (String) parent.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});*/

		positive = (Button) dialogView.findViewById(R.id.positiveButton);
		negative = (Button) dialogView.findViewById(R.id.negativeButton);

		builder.setTitle("Add Expenses")
				.setView(dialogView);
		final AlertDialog dialog = builder.create();
		positive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences mSharedPreferences = getActivity().getSharedPreferences
						(RoomiesConstants.ROOM_BUDGET_FILE_KEY, Context.MODE_PRIVATE);
				final RoomService room = new RoomServiceImpl(mContext);
				final MiscService misc = new MiscServiceImpl(mContext);
				Toast mToast = null;
				boolean isValidDescription = false;
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
				if (isValidAmount) {
					float amountVal = Float.valueOf(amount.getText().toString());
					if (RoomiesConstants.RENT.equals(category)) {
						if (!mSharedPreferences.getBoolean(RoomiesConstants.IS_RENT_PAID, false)) {
							if (amountVal > Float.valueOf(
									mSharedPreferences.getString(RoomiesConstants
											.RENT_MARGIN, "0"))) {
								confirmMarginExceedAlert(room, dialog, "rent");
							} else {
								room.updateRoomExpenses(amount.getText().toString(), null, null,
										username);
								updateGraphs(dialog);
								RoomiesHelper.createToast(getActivity(),
										"Rent paid", mToast);
							}
						} else {
							RoomiesHelper.createToast(getActivity(),
									"Category already paid for this " +
											"month", mToast);
						}
					} else if (RoomiesConstants.MAID.equals(category)) {
						if (!mSharedPreferences.getBoolean(RoomiesConstants.IS_MAID_PAID, false)) {
							if (amountVal > Float.valueOf(
									mSharedPreferences.getString(RoomiesConstants
											.MAID_MARGIN, "0"))) {
								confirmMarginExceedAlert(room, dialog, "maid");
							} else {

								room.updateRoomExpenses(null, amount.getText().toString(), null,
										username);
								updateGraphs(dialog);
								RoomiesHelper.createToast(getActivity(),
										"Maid expenses paid", mToast);
							}
						} else {
							RoomiesHelper.createToast(getActivity(),
									"Category already paid for this " +
											"month", mToast);
						}
					} else if (RoomiesConstants.ELECTRICITY.equals(category)) {
						if (!mSharedPreferences.getBoolean(RoomiesConstants.IS_ELEC_PAID, false)) {
							if (amountVal > Float.valueOf(
									mSharedPreferences.getString(RoomiesConstants
											.ELECTRICITY_MARGIN, "0"))) {
								confirmMarginExceedAlert(room, dialog, "electricity");
							} else {
								room.updateRoomExpenses(null, null, amount.getText().toString(),
										username);
								updateGraphs(dialog);
								RoomiesHelper.createToast(getActivity(),
										"Electricity expenses paid", mToast);
							}
						} else {
							RoomiesHelper.createToast(getActivity(),
									"Category already paid for this " +
											"month", mToast);
						}
					} else if (RoomiesConstants.MISCELLANEOUS.equals(category)) {
						if (isValidDescription && isValidQuantity) {
							if (amountVal > Float.valueOf(
									mSharedPreferences.getString(RoomiesConstants
											.MISC_MARGIN, "0"))) {
								final AlertDialog.Builder confirmBuilder = new AlertDialog.Builder
										(getActivity());
								confirmBuilder.setMessage(
										"You are exceeding the miscellaneous limit. Do you want" +
												" to continue ?").setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											Toast mToast;

											@Override
											public void onClick(DialogInterface dialogInterface,
											                    int which) {
												misc.insertMiscExpenses(description, quantity,
														amount, subCategory,
														username);
												updateGraphs(dialog);
												RoomiesHelper.createToast(getActivity(),
														"Miscellaneous expense added", mToast);
											}
										}).setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialogInterface,
											                    int which) {
												dialogInterface.dismiss();
											}
										}).create();
								confirmBuilder.show();
							} else {
								misc.insertMiscExpenses(description, quantity, amount, subCategory,
										username);
								updateGraphs(dialog);
								RoomiesHelper.createToast(getActivity(),
										"Miscellaneous expense added", mToast);
							}
						}
					}
				}
			}
		});
		negative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		return dialog;
	}


	@Override
	public void onShow(DialogInterface dialog) {

	}

	private void updateGraphs(AlertDialog dialog) {
		RoomiesFragment fragment = (RoomiesFragment) getActivity().getSupportFragmentManager()
				.getFragments().get(0);
		if (fragment instanceof HomeFragment) {
			((RoomiesFragment.UpdatableFragment) fragment.getChildFragmentManager().getFragments()
					.get(0))
					.update();
			((RoomiesFragment.UpdatableFragment) fragment.getChildFragmentManager().getFragments().get
					(1))
					.update();
		} else if (fragment instanceof DetailExpenseTab) {
			((DetailExpenseTab) fragment).update();
		}
		dialog.dismiss();
	}

	private void confirmMarginExceedAlert(final RoomService room, final AlertDialog dialog,
	                                      final String category) {
		final AlertDialog.Builder confirmBuilder = new AlertDialog.Builder
				(getActivity());
		confirmBuilder.setMessage(
				"You are exceeding the " + category + " limit. Do you want" +
						" to continue ?").setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					Toast mToast;

					@Override
					public void onClick(DialogInterface dialogInterface,
					                    int which) {
						if ("rent".equalsIgnoreCase(category)) {
							room.updateRoomExpenses(amount.getText().toString(),
									null, null, username);
						} else if ("maid".equalsIgnoreCase(category)) {
							room.updateRoomExpenses(null, amount.getText().toString(),
									null, username);
						} else if ("electricity".equalsIgnoreCase(category)) {
							room.updateRoomExpenses(null, null, amount.getText().toString(),
									username);
						}
						updateGraphs(dialog);
						RoomiesHelper.createToast(getActivity(),
								category + " paid", mToast);
					}
				}).setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface,
					                    int which) {
						dialogInterface.dismiss();
					}
				}).create();
		confirmBuilder.show();
	}
}
