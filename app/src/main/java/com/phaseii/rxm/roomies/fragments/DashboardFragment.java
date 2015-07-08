package com.phaseii.rxm.roomies.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.dao.RoomExpensesDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.helper.QueryParam;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.helper.ServiceParam;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.model.SortType;
import com.phaseii.rxm.roomies.view.DetailExpenseDataAdapter;
import com.phaseii.rxm.roomies.view.ScrollableLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ROOMIES_KEY;

public class DashboardFragment extends RoomiesFragment
        implements RoomiesFragment.UpdatableFragment {

    private static Context mContext;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayout sortFilterTab;
    private RelativeLayout toolbarContainer;
    private SharedPreferences msharedPref;
    private List<RoomExpenses> roomExpensesList;
    private RoomiesDao service;
    private String roomId;

    public static DashboardFragment getInstance(Context mContext) {
        DashboardFragment.mContext = mContext;
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        msharedPref = mContext.getSharedPreferences(PREF_ROOMIES_KEY, Context.MODE_PRIVATE);
        roomId = msharedPref.getString(RoomiesConstants.PREF_ROOM_ID, null);

        Map<ServiceParam, Object> paramMap = new HashMap<>();
        List<QueryParam> params = new ArrayList<QueryParam>();
        List<String> selectionArgs = new ArrayList<String>();

        params.add(QueryParam.MONTH_YEAR);
        paramMap.put(ServiceParam.SELECTION, params);

        selectionArgs.add(RoomiesHelper.getCurrentMonthYear());
        paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);

        paramMap.put(ServiceParam.QUERYARGS, QueryParam.ROOMID);

        service = new RoomExpensesDaoImpl(mContext);
        roomExpensesList = (List<RoomExpenses>) service.getDetails(paramMap);

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

        recyclerView = (RecyclerView) rootView.findViewById(
                R.id.expense_detail_view);
        adapter = new DetailExpenseDataAdapter(roomExpensesList, SortType.DATE_DESC,
                getActivity().getBaseContext());
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new ScrollableLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        sortFilterTab = (LinearLayout) rootView.findViewById(R.id.sort_filter_tab);
        toolbarContainer = (RelativeLayout) rootView.findViewById(R.id.toolbar_container);

        final TextView amount = (TextView) rootView.findViewById(R.id.amount);
        final TextView quantity = (TextView) rootView.findViewById(R.id.quantity);
        final TextView date = (TextView) rootView.findViewById(R.id.date);
        final TextView bills = (TextView) rootView.findViewById(R.id.bills);
        final TextView grocery = (TextView) rootView.findViewById(R.id.grocery);
        final TextView vegetables = (TextView) rootView.findViewById(R.id.vegetables);
        final TextView others = (TextView) rootView.findViewById(R.id.others);

        final TextView sortBar = (TextView) rootView.findViewById(R.id.sort);
        TextView filterBar = (TextView) rootView.findViewById(R.id.filter);
        final RelativeLayout sortMenu = (RelativeLayout) rootView.findViewById(R.id.sort_menu);
        final RelativeLayout filterMenu = (RelativeLayout) rootView.findViewById(
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
                if (!isNotSorted) {
                    Collections.sort(roomExpensesList, new Comparator<RoomExpenses>() {
                        @Override
                        public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                            if (lhs.getAmount() < rhs.getAmount()) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    ((DetailExpenseDataAdapter) adapter).addItemsToList(roomExpensesList,
                            SortType.AMOUNT);
                } else {
                    Collections.sort(roomExpensesList, new Comparator<RoomExpenses>() {
                        @Override
                        public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                            if (lhs.getAmount() > rhs.getAmount()) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    ((DetailExpenseDataAdapter) adapter).addItemsToList(roomExpensesList,
                            SortType.AMOUNT);
                }
                sortMenu.setVisibility(View.GONE);

                adapter.notifyDataSetChanged();
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
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    ((DetailExpenseDataAdapter) adapter).addItemsToList(roomExpensesList,
                            SortType.QUANTITY);
                } else {
                    Collections.sort(roomExpensesList, new Comparator<RoomExpenses>() {
                        @Override
                        public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                            if (Float.valueOf(
                                    lhs.getQuantity().substring(0,
                                            lhs.getQuantity().length() - 2)) > Float.valueOf(
                                    rhs.getQuantity().substring(0,
                                            rhs.getQuantity().length() - 2))) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    ((DetailExpenseDataAdapter) adapter).addItemsToList(roomExpensesList,
                            SortType.QUANTITY);
                }
                sortMenu.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
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
                    ((DetailExpenseDataAdapter) adapter).addItemsToList(roomExpensesList,
                            SortType.DATE_DESC);
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
                    ((DetailExpenseDataAdapter) adapter).addItemsToList(roomExpensesList,
                            SortType.DATE_ASC);
                }
                adapter.notifyDataSetChanged();
                sortMenu.setVisibility(View.GONE);
            }
        });

        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RoomExpenses> roomExpensesList = new ArrayList<RoomExpenses>();
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (roomExpenses.getExpenseSubcategory().equals(
                            getResources().getStringArray(R.array
                                    .subcategory)[0])) {
                        roomExpensesList.add(roomExpenses);
                    }
                }
                ((DetailExpenseDataAdapter) adapter).addItemsToList(roomExpensesList,
                        SortType.BILLS);
                adapter.notifyDataSetChanged();
                filterMenu.setVisibility(View.GONE);
            }
        });

        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RoomExpenses> roomExpensesList = new ArrayList<RoomExpenses>();
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (roomExpenses.getExpenseSubcategory().equals(
                            getResources().getStringArray(R.array
                                    .subcategory)[1])) {
                        roomExpensesList.add(roomExpenses);
                    }
                }
                ((DetailExpenseDataAdapter) adapter).addItemsToList(roomExpensesList,
                        SortType.GROCERY);
                adapter.notifyDataSetChanged();
                filterMenu.setVisibility(View.GONE);
            }
        });

        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RoomExpenses> roomExpensesList = new ArrayList<RoomExpenses>();
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (roomExpenses.getExpenseSubcategory().equals(
                            getResources().getStringArray(R.array
                                    .subcategory)[2])) {
                        roomExpensesList.add(roomExpenses);
                    }
                }
                ((DetailExpenseDataAdapter) adapter).addItemsToList(roomExpensesList,
                        SortType.VEGETABLES);
                adapter.notifyDataSetChanged();
                filterMenu.setVisibility(View.GONE);
            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RoomExpenses> roomExpensesList = new ArrayList<RoomExpenses>();
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (roomExpenses.getExpenseSubcategory().equals(
                            getResources().getStringArray(R.array
                                    .subcategory)[3])) {
                        roomExpensesList.add(roomExpenses);
                    }
                }
                ((DetailExpenseDataAdapter) adapter).addItemsToList(roomExpensesList,
                        SortType.OTHERS);
                adapter.notifyDataSetChanged();
                filterMenu.setVisibility(View.GONE);
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (Math.abs(
                            toolbarContainer.getTranslationY()) < sortFilterTab.getBottom()) {
                        showToolbar();
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
                            .translationY(sortFilterTab.getHeight())
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
                    toolbarContainer.setTranslationY(sortFilterTab.getBottom());

                } else {
                    toolbarContainer.setTranslationY(
                            toolbarContainer.getTranslationY() + dy);
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

        return rootView;

    }


    @Override
    public View getFragmentView() {
        return null;
    }

    @Override
    public void update(String username) {
        if (null != adapter) {

            Map<ServiceParam, Object> paramMap = new HashMap<>();
            List<QueryParam> params = new ArrayList<QueryParam>();
            List<String> selectionArgs = new ArrayList<String>();

            params.add(QueryParam.ROOMID);
            paramMap.put(ServiceParam.SELECTION, params);

            selectionArgs.add(roomId);
            paramMap.put(ServiceParam.SELECTIONARGS, selectionArgs);

            paramMap.put(ServiceParam.QUERYARGS, QueryParam.MONTH_YEAR);

            roomExpensesList = (List<RoomExpenses>) service.getDetails(paramMap);
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
            ((DetailExpenseDataAdapter) adapter).addItemsToList(roomExpensesList,
                    SortType.DATE_DESC);
            adapter.notifyDataSetChanged();
        }
    }
}
