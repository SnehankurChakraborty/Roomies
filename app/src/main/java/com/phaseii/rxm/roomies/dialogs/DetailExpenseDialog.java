package com.phaseii.rxm.roomies.dialogs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

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
	private LinearLayout toolbarContainer;
	private LinearLayout sortFilterTab;

	public static DetailExpenseDialog getInstance(List<MiscExpense> miscExpenses,
	                                              Context mContext) {
		DetailExpenseDialog.miscExpenses = miscExpenses;
		DetailExpenseDialog.mContext = mContext;
		return new DetailExpenseDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View dialogView = inflater.inflate(R.layout.detail_expense_dialog, null);
		RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(
				R.id.expense_detail_view);
		toolbarContainer = (LinearLayout) dialogView.findViewById(R.id.toolbar_container);
		sortFilterTab = (LinearLayout) dialogView.findViewById(R.id.sort_filter_tab);
		RecyclerView.Adapter adapter = new DetailExpenseDataAdapter(miscExpenses,
				getActivity().getBaseContext());
		recyclerView.setAdapter(adapter);
		RecyclerView.LayoutManager layoutManager = new ScrollableLayoutManager(mContext,
				LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layoutManager);
		if (miscExpenses.size() != 1) {
			recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
				@Override
				public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
					super.onScrollStateChanged(recyclerView, newState);
					if (newState == RecyclerView.SCROLL_STATE_IDLE) {
						if (Math.abs(
								toolbarContainer.getTranslationY()) > sortFilterTab.getHeight()) {
							hideToolbar();
						} else {
							showToolbar();
						}
					}
				}

				private void showToolbar() {
					toolbarContainer
							.animate()
							.translationY(0)
							.start();
				}

				private void hideToolbar() {
					toolbarContainer
							.animate()
							.translationY(-sortFilterTab.getHeight())
							.start();
				}

				@Override
				public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					super.onScrolled(recyclerView, dx, dy);
					if (dy > 0) {
						hideToolbarBy(dy);
					} else {
						showToolbarBy(dy);
					}
				}

				/*private void scrollColoredViewParallax(int dy) {
				    coloredBackgroundView.setTranslationY(coloredBackgroundView.getTranslationY() — dy / 3);
			    }*/
				private void hideToolbarBy(int dy) {
					if (cannotHideMore(dy)) {
						toolbarContainer.setTranslationY(-sortFilterTab.getBottom());
					} else {
						toolbarContainer.setTranslationY(toolbarContainer.getTranslationY() - dy);
					}
				}

				private boolean cannotHideMore(int dy) {
					return Math.abs(
							toolbarContainer.getTranslationY() - dy) >
							sortFilterTab
									.getHeight();
				}

				private void showToolbarBy(int dy) {
					if (cannotShowMore(dy)) {
						toolbarContainer.setTranslationY(0);
					} else {
						toolbarContainer.setTranslationY(toolbarContainer.getTranslationY() - dy);
					}
				}

				private boolean cannotShowMore(int dy) {
					return toolbarContainer.getTranslationY() - dy > 0;
				}
			});
		} else {
			sortFilterTab.setEnabled(false);
		}
		return builder.setView(dialogView).create();
	}
}
