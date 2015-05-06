package com.phaseii.rxm.roomies.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.phaseii.rxm.roomies.R;

/**
 * Created by Snehankur on 5/6/2015.
 */
public class DetailExpenseDataTableAdapter extends DialogFragment{

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Context mContext=getActivity().getBaseContext();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.expense_data_table, null);
		TableLayout dataTable=(TableLayout)dialogView.findViewById(R.id.expense_data_table);
		TableRow newRow=new TableRow(mContext);

		TextView date=new TextView(mContext);
		date.setText("4-may-2015");
		newRow.addView(date);

		TextView type=new TextView(mContext);
		type.setText("Grocery");
		newRow.addView(type);

		dataTable.addView(newRow);
		return builder.setView(dialogView).create();
	}
}
