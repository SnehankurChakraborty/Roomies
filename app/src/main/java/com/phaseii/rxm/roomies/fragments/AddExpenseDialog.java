package com.phaseii.rxm.roomies.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.service.MiscServiceImpl;
import com.phaseii.rxm.roomies.service.RoomiesService;
import com.phaseii.rxm.roomies.service.RoomiesServiceImpl;

/**
 * Created by Snehankur on 4/5/2015.
 */
public class AddExpenseDialog extends DialogFragment implements DialogInterface.OnShowListener {

	Context mContext;
	ArrayAdapter<CharSequence> mCategoryAdapter;
	ArrayAdapter<CharSequence> mSubCategoryAdapter;
	Spinner categorySpinner;
	Spinner subCategorySpinner;
	String category;
	String subCategory;
	EditText amount;
	EditText description;
	EditText quantity;
	Button positive;
	Button negative;
	static int pagerId;
	String username;


	public static AddExpenseDialog getInstance(int pagerId) {
		AddExpenseDialog.pagerId = pagerId;
		return new AddExpenseDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity().getApplicationContext();
		username=mContext.getSharedPreferences(RoomiesConstants.ROOM_INFO_FILE_KEY,
				Context.MODE_PRIVATE).getString(RoomiesConstants.NAME,null);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.add_expense_dilog, null);
		description = (EditText) dialogView.findViewById(R.id.description);
		amount = (EditText) dialogView.findViewById(R.id.amount);
		quantity = (EditText) dialogView.findViewById(R.id.quantity);
		categorySpinner = (Spinner) dialogView.findViewById(R.id
				.categoryspinner);
		subCategorySpinner = (Spinner) dialogView.findViewById(R.id
				.subcategoryspinner);
		subCategorySpinner.setEnabled(false);
		mCategoryAdapter = ArrayAdapter.createFromResource(mContext,
				R.array.category, R.layout.roomies_spinner_item);
		mCategoryAdapter.setDropDownViewResource(R.layout.roomies_spinner_dropdown);
		categorySpinner.setAdapter(mCategoryAdapter);
		categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
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
		});


		mSubCategoryAdapter = ArrayAdapter.createFromResource(mContext,
				R.array.subcategory, R.layout.roomies_spinner_item);
		mSubCategoryAdapter.setDropDownViewResource(R.layout.roomies_spinner_dropdown);
		subCategorySpinner.setAdapter(mSubCategoryAdapter);
		subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
			                           int position, long id) {
				subCategory = (String) parent.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

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
				RoomiesService room = new RoomiesServiceImpl(mContext);
				MiscServiceImpl misc = new MiscServiceImpl(mContext);
				Toast mToast = null;
				boolean isValidDescription = false;
				boolean isValidQuantity = false;
				boolean isValidAmount = RoomiesHelper.setNumericError("amount",
						getActivity().getBaseContext(), dialogView);
				if (description.isEnabled()) {
					isValidDescription = RoomiesHelper.setError("description", mContext,
							dialogView);
				}
				if (quantity.isEnabled()) {
					isValidQuantity = RoomiesHelper.setNumericError("quantity",
							mContext, dialogView);
				}
				if (isValidAmount) {
					if (RoomiesConstants.RENT.equals(category)) {
						if (!mSharedPreferences.getBoolean(RoomiesConstants.IS_RENT_PAID, false)) {
							room.updateRoomExpenses(amount.getText().toString(), null, null,
									username);
							updateGraphs(dialog);
							RoomiesHelper.createToast(getActivity(),
									"Rent paid", mToast);
						} else {
							RoomiesHelper.createToast(getActivity(),
									"Category already paid for this " +
											"month", mToast);
						}
					} else if (RoomiesConstants.MAID.equals(category)) {
						if (!mSharedPreferences.getBoolean(RoomiesConstants.IS_MAID_PAID, false)) {
							room.updateRoomExpenses(null, amount.getText().toString(), null,
									username);
							updateGraphs(dialog);
							RoomiesHelper.createToast(getActivity(),
									"Maid expenses paid", mToast);
						} else {
							RoomiesHelper.createToast(getActivity(),
									"Category already paid for this " +
											"month", mToast);
						}
					} else if (RoomiesConstants.ELECTRICITY.equals(category)) {
						if (!mSharedPreferences.getBoolean(RoomiesConstants.IS_ELEC_PAID, false)) {
							room.updateRoomExpenses(null, null, amount.getText().toString(),
									username);
							updateGraphs(dialog);
							RoomiesHelper.createToast(getActivity(),
									"Electricity expenses paid", mToast);
						} else {
							RoomiesHelper.createToast(getActivity(),
									"Category already paid for this " +
											"month", mToast);
						}
					} else if (RoomiesConstants.MISCELLANEOUS.equals(category)) {


						if (isValidDescription && isValidQuantity) {
							misc.insertMiscExpenses(description, quantity, amount, subCategory,
									username);
							updateGraphs(dialog);
							RoomiesHelper.createToast(getActivity(),
									"Miscellaneous expense added", mToast);
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
		} else if (fragment instanceof TrendFragment) {
			((TrendFragment) fragment).update();
		}
		dialog.dismiss();
	}
}
