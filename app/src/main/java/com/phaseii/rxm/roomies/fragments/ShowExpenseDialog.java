package com.phaseii.rxm.roomies.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.model.MiscExpense;

import java.util.List;

/**
 * Created by Snehankur on 4/15/2015.
 */
public class ShowExpenseDialog extends DialogFragment {

	Context mContext;
	static List<MiscExpense> miscExpenses;
	Adapter listAdapter;
	private int listItems;

	public static ShowExpenseDialog getInstance(List<MiscExpense> miscExpenses) {
		ShowExpenseDialog.miscExpenses=miscExpenses;
		return new ShowExpenseDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity().getApplicationContext();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.show_expense_dilog, null);
		/*ListView listView = (ListView) dialogView.findViewById(R.id.show_expense_list);
		listAdapter=new ArrayAdapter(this,
				R.layout.show_expense_layout,
				listItems);*/
		return null;
	}
}
