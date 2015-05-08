package com.phaseii.rxm.roomies.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.model.MiscExpense;
import com.phaseii.rxm.roomies.view.DetailExpenseDataAdapter;
import com.phaseii.rxm.roomies.view.ScrollableLayoutManager;

import java.util.List;

/**
 * Created by Snehankur on 5/9/2015.
 */
public class DetailExpenseDialog extends DialogFragment {

	private static List<MiscExpense> miscExpenses;
	private static Context mContext;

	public static DetailExpenseDialog getInstance(List<MiscExpense> miscExpenses, Context mContext){
		DetailExpenseDialog.miscExpenses=miscExpenses;
		DetailExpenseDialog.mContext=mContext;
		return new DetailExpenseDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.detail_expense_dialog, null);
		RecyclerView recyclerView=(RecyclerView)dialogView.findViewById(R.id.expense_detail_view);
		RecyclerView.Adapter adapter=new DetailExpenseDataAdapter(miscExpenses,
				getActivity().getBaseContext());
		recyclerView.setAdapter(adapter);
		RecyclerView.LayoutManager layoutManager=new ScrollableLayoutManager(mContext,
				LinearLayoutManager.VERTICAL,false);

		recyclerView.setLayoutManager(layoutManager);
		return builder.setView(dialogView).create();
	}
}
