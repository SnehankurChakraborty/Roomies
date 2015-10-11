package com.phaseii.rxm.roomies.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.dao.RoomExpensesDaoImpl;
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.model.SortType;
import com.phaseii.rxm.roomies.util.Category;
import com.phaseii.rxm.roomies.util.QueryParam;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.util.RoomiesHelper;
import com.phaseii.rxm.roomies.util.ServiceParam;
import com.phaseii.rxm.roomies.util.SubCategory;
import com.phaseii.rxm.roomies.view.DetailExpenseAdapter;
import com.phaseii.rxm.roomies.view.ScrollableLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;

public class DashboardFragment extends RoomiesFragment
        implements RoomiesFragment.UpdatableFragment {

    private Context mContext;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayout sortFilterTab;
    private RelativeLayout toolbarContainer;
    private SharedPreferences msharedPref;
    private List<RoomExpenses> roomExpensesList;
    private RoomiesDao service;
    private String roomId;

    public static DashboardFragment getInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mContext = getActivity().getBaseContext();
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

        /**
         * Sort in descending order of transaction dates
         */
        roomExpensesList = sortDate(roomExpensesList, false, SortType.DATE_DESC);

        /**
         * Setting up the recycler view for timeline and expense graph
         */
        recyclerView = (RecyclerView) rootView.findViewById(R.id.expense_detail_view);
        adapter = new DetailExpenseAdapter(roomExpensesList, SortType.DATE_DESC,
                getActivity().getBaseContext());
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new ScrollableLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        /**
         * Setting up sort/filter bar
         */
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
        final TextView filterBar = (TextView) rootView.findViewById(R.id.filter);
        final RelativeLayout sortMenu = (RelativeLayout) rootView.findViewById(R.id.sort_menu);
        final RelativeLayout filterMenu = (RelativeLayout) rootView.findViewById(
                R.id.filter_menu);

        /**
         * Show the sort menu on clicking the sort bar on the bottom of the screen
         */
        sortBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sortMenu.getVisibility() == View.GONE) {
                    if (filterMenu.getVisibility() == View.VISIBLE) {
                        TranslateAnimation translate = new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, -1,
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, 0);
                        translate.setDuration(500);
                        filterMenu.startAnimation(translate);
                        filterMenu.setVisibility(View.GONE);
                    }
                    sortMenu.setVisibility(View.VISIBLE);
                    TranslateAnimation translate = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 1,
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0);
                    translate.setDuration(500);
                    sortMenu.startAnimation(translate);
                    filterMenu.setVisibility(View.GONE);
                } else {
                    TranslateAnimation translate = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 1,
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0);
                    translate.setDuration(500);
                    sortMenu.startAnimation(translate);
                    sortMenu.setVisibility(View.GONE);
                }
            }
        });

        /**
         * Show the filter menu on clicking the filter bar on the bottom of the screen
         */
        filterBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterMenu.getVisibility() == View.GONE) {
                    if (sortMenu.getVisibility() == View.VISIBLE) {
                        TranslateAnimation translate = new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, 1,
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, 0);
                        translate.setDuration(500);
                        sortMenu.startAnimation(translate);
                        sortMenu.setVisibility(View.GONE);
                    }
                    filterMenu.setVisibility(View.VISIBLE);
                    TranslateAnimation translate = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, -1,
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0);
                    translate.setDuration(500);
                    filterMenu.startAnimation(translate);
                    sortMenu.setVisibility(View.GONE);
                } else {
                    TranslateAnimation translate = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, -1,
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0);
                    translate.setDuration(500);
                    filterMenu.startAnimation(translate);
                    filterMenu.setVisibility(View.GONE);
                }
            }
        });

        /**
         * Sort the room expenses list in ascending or descending order of amount.
         */
        amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBar.setTextColor(getResources().getColor(R.color.primary_dark2));
                filterBar.setTextColor(Color.WHITE);
                roomExpensesList = sortAmount(roomExpensesList, true);
                TranslateAnimation translate = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 1,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0);
                translate.setDuration(500);
                sortMenu.startAnimation(translate);
                sortMenu.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

        });

        /**
         * Sort the room expenses list in ascending or descending order of quantity.
         */
        quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBar.setTextColor(getResources().getColor(R.color.primary_dark2));
                filterBar.setTextColor(Color.WHITE);
                roomExpensesList = sortQuantity(roomExpensesList, true);
                TranslateAnimation translate = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 1,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0);
                translate.setDuration(500);
                sortMenu.startAnimation(translate);
                sortMenu.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        });

        /**
         * Sort the room expenses list in ascending or descending order of date.
         */
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBar.setTextColor(getResources().getColor(R.color.primary_dark2));
                filterBar.setTextColor(Color.WHITE);
                roomExpensesList = sortDate(roomExpensesList, true, SortType.NONE);
                adapter.notifyDataSetChanged();
                TranslateAnimation translate = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 1,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0);
                translate.setDuration(500);
                sortMenu.startAnimation(translate);
                sortMenu.setVisibility(View.GONE);
            }
        });

        /**
         * Show only the transactions made on miscellaneous bills
         */
        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBar.setTextColor(getResources().getColor(R.color.primary_dark2));
                sortBar.setTextColor(Color.WHITE);
                List<RoomExpenses> expensesList = new ArrayList<RoomExpenses>();
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (Category.getCategory(roomExpenses.getExpenseCategory()).equals(
                            Category.MISCELLANEOUS)) {
                        if (SubCategory.getSubcategory(roomExpenses.getExpenseSubcategory()).equals
                                (SubCategory.BILLS)) {
                            expensesList.add(roomExpenses);
                        }
                    }
                }
                sortDate(expensesList, true, SortType.DATE_DESC);
                adapter.notifyDataSetChanged();
                TranslateAnimation translate = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, -1,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0);
                translate.setDuration(500);
                filterMenu.startAnimation(translate);
                filterMenu.setVisibility(View.GONE);
            }
        });

        /**
         * Show only the transactions made on miscellaneous grocery
         */
        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBar.setTextColor(getResources().getColor(R.color.primary_dark2));
                sortBar.setTextColor(Color.WHITE);
                List<RoomExpenses> expensesList = new ArrayList<RoomExpenses>();
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (Category.getCategory(roomExpenses.getExpenseCategory()).equals(
                            Category.MISCELLANEOUS)) {
                        if (SubCategory.getSubcategory(roomExpenses.getExpenseSubcategory()).equals
                                (SubCategory.GROCERY)) {
                            expensesList.add(roomExpenses);
                        }
                    }
                }
                sortDate(expensesList, true, SortType.DATE_DESC);
                adapter.notifyDataSetChanged();
                TranslateAnimation translate = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, -1,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0);
                translate.setDuration(500);
                filterMenu.startAnimation(translate);
                filterMenu.setVisibility(View.GONE);
            }
        });

        /**
         * Show only the transactions made on miscellaneous vegetables
         */
        vegetables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBar.setTextColor(getResources().getColor(R.color.primary_dark2));
                sortBar.setTextColor(Color.WHITE);
                List<RoomExpenses> expensesList = new ArrayList<RoomExpenses>();
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (Category.getCategory(roomExpenses.getExpenseCategory()).equals(
                            Category.MISCELLANEOUS)) {
                        if (SubCategory.getSubcategory(roomExpenses.getExpenseSubcategory()).equals
                                (SubCategory.VEGETABLES)) {
                            expensesList.add(roomExpenses);
                        }
                    }
                }
                sortDate(expensesList, true, SortType.DATE_DESC);
                adapter.notifyDataSetChanged();
                TranslateAnimation translate = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, -1,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0);
                translate.setDuration(500);
                filterMenu.startAnimation(translate);
                filterMenu.setVisibility(View.GONE);
            }
        });

        /**
         * Show only the transactions made on miscellaneous others
         */
        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBar.setTextColor(getResources().getColor(R.color.primary_dark2));
                sortBar.setTextColor(Color.WHITE);
                List<RoomExpenses> expensesList = new ArrayList<RoomExpenses>();
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (Category.getCategory(roomExpenses.getExpenseCategory()).equals(
                            Category.MISCELLANEOUS)) {
                        if (SubCategory.getSubcategory(roomExpenses.getExpenseSubcategory()).equals
                                (SubCategory.OTHERS)) {
                            expensesList.add(roomExpenses);
                        }
                    }
                }
                sortDate(expensesList, true, SortType.DATE_DESC);
                adapter.notifyDataSetChanged();
                TranslateAnimation translate = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, -1,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0);
                translate.setDuration(500);
                filterMenu.startAnimation(translate);
                filterMenu.setVisibility(View.GONE);
            }
        });

        /**
         * Add on scroll listener to recycler view for smooth scrolling of the filter/sort menu.
         * The menu should disappear to bottom of screen while scrolling down and should animate
         * up(and remain in initial position) while scrolling up the screen.
         */
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

    /**
     * Callback to update the Dashboard screen on any new transaction
     *
     * @param username
     */
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
            ((DetailExpenseAdapter) adapter).addItemsToList(roomExpensesList,
                    SortType.DATE_DESC);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Sorts the expenses list by quantity.
     * In case of list being is already sorted in ascending order of quantity, then it is sorted in
     * reverse order and vise versa.
     *
     * @return RoomExpenses list sorted by quantity
     */
    private List<RoomExpenses> sortQuantity(List<RoomExpenses> roomExpensesList, boolean
            setAdapter) {
        boolean isSorted = true;
        float next = 0f;
        for (RoomExpenses roomExpenses : roomExpensesList) {
            if (null != roomExpenses.getQuantity()) {
                if (Float.valueOf(
                        roomExpenses.getQuantity().substring(0,
                                roomExpenses.getQuantity().length() - 2)) < next) {
                    isSorted = false;
                    break;
                }
                next = roomExpenses.getAmount();
            }
        }
        List<RoomExpenses> expenseList = new ArrayList<>();
        List<RoomExpenses> expenseListAlt = new ArrayList<>();
        for (RoomExpenses expenses : roomExpensesList) {
            if (null == expenses.getQuantity()) {
                expenseListAlt.add(expenses);
            } else {
                expenseList.add(expenses);
            }
        }
        Comparator<RoomExpenses> comparator = new Comparator<RoomExpenses>() {
            @Override
            public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                float lhsQuantity = Float.valueOf(
                        lhs.getQuantity().substring(0,
                                lhs.getQuantity().length() - 2));
                float rhsQuantity = Float.valueOf(
                        rhs.getQuantity().substring(0,
                                rhs.getQuantity().length() - 2));

                if (lhsQuantity < rhsQuantity) {
                    return -1;
                } else if (lhsQuantity == rhsQuantity) {
                    return 0;
                } else {
                    return 1;
                }


            }
        };
        if (!isSorted) {
            Collections.sort(expenseList, comparator);
        } else {
            Collections.sort(expenseList, Collections.reverseOrder(comparator));
        }
        expenseList.addAll(expenseListAlt);
        if (setAdapter) {
            ((DetailExpenseAdapter) adapter).addItemsToList(expenseList,
                    SortType.QUANTITY);
        }
        return expenseList;
    }

    /**
     * Sorts the expenses list by amount.
     * In case of list being is already sorted in ascending order of amount, then it is sorted in
     * reverse order and vise versa.
     *
     * @return RoomExpenses list sorted by amount
     */
    private List<RoomExpenses> sortAmount(List<RoomExpenses> roomExpensesList, boolean setAdapter) {
        boolean isSorted = true;
        float next = 0f;
        for (RoomExpenses roomExpenses : roomExpensesList) {
            if (roomExpenses.getAmount() < next) {
                isSorted = false;
                break;
            }
            next = roomExpenses.getAmount();
        }
        List<RoomExpenses> expenseList = new ArrayList<>(roomExpensesList);
        Comparator<RoomExpenses> comparator = new Comparator<RoomExpenses>() {
            @Override
            public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                if (lhs.getAmount() > rhs.getAmount()) {
                    return 1;
                } else if (lhs.getAmount() == rhs.getAmount()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        };

        if (!isSorted) {
            Collections.sort(expenseList, comparator);
        } else {
            Collections.sort(expenseList, Collections.reverseOrder(comparator));
        }
        if (setAdapter) {
            ((DetailExpenseAdapter) adapter).addItemsToList(expenseList,
                    SortType.AMOUNT);
        }
        return expenseList;
    }

    /**
     * Sorts the expenses list by date.
     * In case of list being is already sorted in ascending order of date, then it is sorted in
     * reverse order and vise versa.
     *
     * @return RoomExpenses list sorted by date.
     */
    private List<RoomExpenses> sortDate(List<RoomExpenses> roomExpensesList, boolean setAdapter,
                                        SortType sortType) {

        List<RoomExpenses> expenseList = new ArrayList<>(roomExpensesList);
        Comparator<RoomExpenses> comparator = new Comparator<RoomExpenses>() {
            @Override
            public int compare(RoomExpenses lhs, RoomExpenses rhs) {
                if (lhs.getExpenseDate().after(rhs.getExpenseDate())) {
                    return 1;
                } else if (lhs.getExpenseDate().equals(rhs.getExpenseDate())) {
                    return 0;
                } else {
                    return -1;
                }
            }
        };
        switch (sortType) {
            case DATE_ASC:
                Collections.sort(expenseList, comparator);
                if (setAdapter) {
                    ((DetailExpenseAdapter) adapter).addItemsToList(expenseList,
                            SortType.DATE_ASC);
                }
                break;
            case DATE_DESC:
                Collections.sort(expenseList, Collections.reverseOrder(comparator));
                if (setAdapter) {
                    ((DetailExpenseAdapter) adapter).addItemsToList(expenseList,
                            SortType.DATE_DESC);
                }
                break;
            default:
                boolean isSorted = true;
                Date next = null;
                if (roomExpensesList.size() > 0) {
                    next = roomExpensesList.get(0).getExpenseDate();
                }
                for (RoomExpenses roomExpenses : roomExpensesList) {
                    if (roomExpenses.getExpenseDate().before(next)) {
                        isSorted = false;
                        break;
                    }
                    next = roomExpenses.getExpenseDate();
                }
                if (!isSorted) {
                    Collections.sort(expenseList, comparator);
                    if (setAdapter) {
                        ((DetailExpenseAdapter) adapter).addItemsToList(expenseList,
                                SortType.DATE_ASC);
                    }
                } else {
                    Collections.sort(expenseList, Collections.reverseOrder(comparator));
                    if (setAdapter) {
                        ((DetailExpenseAdapter) adapter).addItemsToList(expenseList,
                                SortType.DATE_DESC);
                    }
                }
                break;
        }
        return expenseList;
    }
}
