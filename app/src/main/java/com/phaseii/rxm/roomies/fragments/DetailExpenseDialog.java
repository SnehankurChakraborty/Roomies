package com.phaseii.rxm.roomies.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.model.MiscExpense;
import com.phaseii.rxm.roomies.view.DetailMiscellaneousAdapter;

import java.util.List;

/**
 * Created by Snehankur on 4/16/2015.
 */
public class DetailExpenseDialog extends DialogFragment {

	private static List<MiscExpense> miscExpenses;
	private DetailMiscellaneousAdapter detailMiscellaneousAdapter;
	Context mContext;

	public static DetailExpenseDialog getInstance(List<MiscExpense> miscExpenses){
		DetailExpenseDialog.miscExpenses=miscExpenses;
		return new DetailExpenseDialog();
	}


	/*@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_expense_dilog);
		miscExpenses=new MiscServiceImpl(this).getCurrentTotalMiscExpense();
		this.miscAdapter=new MiscAdapter(this,R.layout.show_expense_layout,miscExpenses);
		setListAdapter(this.miscAdapter);
		for(MiscExpense miscExpense: miscExpenses){
			miscAdapter.add(miscExpense);
			miscAdapter.notifyDataSetChanged();
		}

	}*/
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mContext = getActivity().getApplicationContext();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.show_expense_dilog, null);
		detailMiscellaneousAdapter =new DetailMiscellaneousAdapter(mContext,R.layout.show_expense_layout,miscExpenses);
		ListView listView=(ListView)dialogView.findViewById(R.id.expense_list);
		listView.setAdapter(detailMiscellaneousAdapter);
		for(MiscExpense miscExpense: miscExpenses){
			detailMiscellaneousAdapter.add(miscExpense);
			detailMiscellaneousAdapter.notifyDataSetChanged();
		}
		builder.setTitle("Expense Report").setView(dialogView);
		final AlertDialog dialog=builder.create();
		return dialog;
	}


}
