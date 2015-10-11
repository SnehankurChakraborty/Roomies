package com.phaseii.rxm.roomies.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.model.SortType;
import com.phaseii.rxm.roomies.view.DetailExpenseAdapter;
import com.phaseii.rxm.roomies.view.ScrollableLayoutManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Snehankur on 5/9/2015.
 */
public class DetailExpenseDialog extends DialogFragment {

    private static List<RoomExpenses> roomExpensesList;
    private static Context mContext;
    private RelativeLayout toolbarContainer;
    private LinearLayout sortFilterTab;
    private RecyclerView recyclerView;

    public static DetailExpenseDialog getInstance(List<RoomExpenses> roomExpensesList,
                                                  Context mContext) {
        DetailExpenseDialog.roomExpensesList = roomExpensesList;
        DetailExpenseDialog.mContext = mContext;
        return new DetailExpenseDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.detail_expense_dialog, null);
        recyclerView = (RecyclerView) dialogView.findViewById(
                R.id.expense_detail_view);
        toolbarContainer = (RelativeLayout) dialogView.findViewById(R.id.toolbar_container);
        sortFilterTab = (LinearLayout) dialogView.findViewById(R.id.sort_filter_tab);

        final TextView amount = (TextView) dialogView.findViewById(R.id.amount);
        final TextView quantity = (TextView) dialogView.findViewById(R.id.quantity);
        final TextView date = (TextView) dialogView.findViewById(R.id.date);
        final TextView bills = (TextView) dialogView.findViewById(R.id.bills);
        final TextView grocery = (TextView) dialogView.findViewById(R.id.grocery);
        final TextView vegetables = (TextView) dialogView.findViewById(R.id.vegetables);
        final TextView others = (TextView) dialogView.findViewById(R.id.others);

        final TextView sortBar = (TextView) dialogView.findViewById(R.id.sort);
        TextView filterBar = (TextView) dialogView.findViewById(R.id.filter);
        final RelativeLayout sortMenu = (RelativeLayout) dialogView.findViewById(R.id.sort_menu);
        final RelativeLayout filterMenu = (RelativeLayout) dialogView.findViewById(
                R.id.filter_menu);

        sortBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortMenu.getVisibility() == View.GONE) {
                    sortMenu.setVisibility(View.VISIBLE);
                    filterMenu.setVisibility(View.GONE);
                } else {
                    sortMenu.setVisibility(View.GONE);
                }
            }
        });

        filterBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterMenu.getVisibility() == View.GONE) {
                    filterMenu.setVisibility(View.VISIBLE);
                    sortMenu.setVisibility(View.GONE);
                } else {
                    filterMenu.setVisibility(View.GONE);
                }
            }
        });

        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNotSorted = false;
                float next = 0f;
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (roomExpenses.getAmount() < next) {
                        isNotSorted = true;
                        break;
                    }
                    next = roomExpenses.getAmount();
                }
                if (isNotSorted) {
                    Collections.sort(roomExpensesList, new Comparator<RoomExpenses>() {
                        @Override
                        public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                            if (lhs.getAmount() < rhs.getAmount()) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    });
                } else {
                    Collections.sort(roomExpensesList, new Comparator<RoomExpenses>() {
                        @Override
                        public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                            if (lhs.getAmount() > rhs.getAmount()) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    });
                }
                setDetails();
                dialogView.invalidate();
                sortMenu.setVisibility(View.GONE);
            }
        });

        quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNotSorted = false;
                float next = 0f;
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (Float.valueOf(
                            roomExpenses.getQuantity().substring(0,
                                    roomExpenses.getQuantity().length() - 2)) < next) {
                        isNotSorted = true;
                        break;
                    }
                    next = roomExpenses.getAmount();
                }
                if (isNotSorted) {
                    Collections.sort(roomExpensesList, new Comparator<RoomExpenses>() {
                        @Override
                        public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                            if (Float.valueOf(
                                    lhs.getQuantity().substring(0,
                                            lhs.getQuantity().length() - 2)) < Float.valueOf(
                                    rhs.getQuantity().substring(0,
                                            rhs.getQuantity().length() - 2))) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    });
                } else {
                    Collections.sort(roomExpensesList, new Comparator<RoomExpenses>() {
                        @Override
                        public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                            if (Float.valueOf(
                                    lhs.getQuantity().substring(0,
                                            lhs.getQuantity().length() - 2)) > Float.valueOf(
                                    rhs.getQuantity().substring(0,
                                            rhs.getQuantity().length() - 2))) {
                                return -1;
                            } else {
                                return 1;
                            }
                        }
                    });
                }
                setDetails();
                dialogView.invalidate();
                sortMenu.setVisibility(View.GONE);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNotSorted = false;
                Date next = new Date();
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (roomExpenses.getExpenseDate().after(next)) {
                        isNotSorted = true;
                        break;
                    }
                    next = roomExpenses.getExpenseDate();
                }
                if (isNotSorted) {
                    Collections.sort(roomExpensesList, new Comparator<RoomExpenses>() {
                        @Override
                        public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                            if (lhs.getExpenseDate().before(rhs.getExpenseDate())) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                } else {
                    Collections.sort(roomExpensesList, new Comparator<RoomExpenses>() {
                        @Override
                        public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                            if (lhs.getExpenseDate().after(rhs.getExpenseDate())) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                }
                setDetails();
                dialogView.invalidate();
                sortMenu.setVisibility(View.GONE);
            }
        });
        setDetails();
        return builder.setView(dialogView).create();
    }

    private void setDetails() {
        RecyclerView.Adapter adapter = new DetailExpenseAdapter(roomExpensesList,
                SortType.DATE_DESC, getActivity().getBaseContext());
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new ScrollableLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        if (roomExpensesList.size() != 1) {
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
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        toolbarContainer
                                .animate()
                                .translationY(0)
                                .start();
                    }
                }

                private void hideToolbar() {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        toolbarContainer
                                .animate()
                                .translationY(-sortFilterTab.getHeight())
                                .start();
                    }
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

                private void hideToolbarBy(int dy) {
                    if (cannotHideMore(dy)) {
                        toolbarContainer.setTranslationY(-sortFilterTab.getBottom());

                    } else {
                        toolbarContainer.setTranslationY(
                                toolbarContainer.getTranslationY() - dy);
                    }
                }

                private boolean cannotHideMore(int dy) {
                    return Math.abs(toolbarContainer.getTranslationY() - dy) >
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
    }
}
