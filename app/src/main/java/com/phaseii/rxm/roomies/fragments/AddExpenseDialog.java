package com.phaseii.rxm.roomies.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.activity.HomeScreenActivity;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.service.RoomiesService;
import com.phaseii.rxm.roomies.service.RoomiesServiceImpl;
import com.phaseii.rxm.roomies.view.RoomiesPagerAdapter;

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
	Button positive;
	Button negative;
	static int pagerId;

	public static AddExpenseDialog getInstance(int pagerId){
		AddExpenseDialog.pagerId=pagerId;
		return new AddExpenseDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity().getApplicationContext();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.add_expense_dilog, null);
		description = (EditText) dialogView.findViewById(R.id.description);
		amount = (EditText) dialogView.findViewById(R.id.amount);
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
				} else {
					subCategorySpinner.setEnabled(true);
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
				String subCategories[] = getResources().getStringArray(R.array.subcategory);
				if (!subCategory.equals(subCategories[subCategories.length - 1])) {
					description.setEnabled(false);
				} else {
					description.setEnabled(true);
				}
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
				RoomiesService room = new RoomiesServiceImpl(mContext);
				if (RoomiesConstants.RENT.equals(category)) {
					room.updateRoomExpenses(amount.getText().toString(), null, null);
				}else if(RoomiesConstants.MAID.equals(category)){
					room.updateRoomExpenses(null, amount.getText().toString(), null);
				}else if(RoomiesConstants.ELECTRICITY.equals(category)){
					room.updateRoomExpenses(null, null, amount.getText().toString());
				}
				((RoomiesFragment.UpdatableFragment)getActivity().getSupportFragmentManager()
						.getFragments().get(0)).update();
				((RoomiesFragment.UpdatableFragment)getActivity().getSupportFragmentManager()
						.getFragments().get(1)).update();
				dialog.dismiss();
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
}
