package com.phaseii.rxm.roomies.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.model.MiscExpense;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.*;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Snehankur on 5/6/2015.
 */
public class DetailExpenseDataAdapter extends RecyclerView.Adapter<DetailExpenseDataAdapter
		.ViewHolder> {

	private Context mContext;
	private List<MiscExpense> miscExpenses;

	public DetailExpenseDataAdapter(List<MiscExpense> miscExpenses, Context mContext) {
		this.miscExpenses = miscExpenses;
		this.mContext = mContext;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.detail_expense_list, parent,
				false);
		ViewHolder vhItem = new ViewHolder(view);
		return vhItem;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.amount.setText(String.valueOf(miscExpenses.get(position).getAmount()));
		holder.description.setText(miscExpenses.get(position).getDescription());
		holder.quantity.setText(String.valueOf(miscExpenses.get(position).getQuantity()));
		holder.date.setText(new SimpleDateFormat("dd-MMM").format(miscExpenses.get(position)
				.getTransactionDate()));

		switch (miscExpenses.get(position).getType()) {
			case BILLS:
				holder.expenseImage.setImageResource(R.drawable.ic_bills_selected);
				break;
			case GROCERY:holder.expenseImage.setImageResource(R.drawable.ic_grocery_selected);
				break;
			case VEGETABLES:holder.expenseImage.setImageResource(R.drawable.ic_vegetable_selected);
				break;
			case OTHERS:holder.expenseImage.setImageResource(R.drawable.ic_others_selected);
				break;
		}


		if (position == miscExpenses.size()) {
			holder.divider.setVisibility(View.GONE);
		} else {
			holder.divider.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public int getItemCount() {
		return miscExpenses.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private TextView date;
		private TextView description;
		private TextView quantity;
		private TextView amount;
		private View itemView;
		private View divider;
		private ImageView expenseImage;

		public ViewHolder(View itemView) {
			super(itemView);
			this.itemView = itemView;
			date = (TextView) itemView.findViewById(R.id.date);
			quantity = (TextView) itemView.findViewById(R.id.quantity);
			description = (TextView) itemView.findViewById(R.id.description);
			amount = (TextView) itemView.findViewById(R.id.amount);
			divider = itemView.findViewById(R.id.divider);
			expenseImage = (ImageView) itemView.findViewById(R.id.expense_icon);
		}
	}
}