/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phaseii.rxm.roomies.ui.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.database.model.SortType;
import com.phaseii.rxm.roomies.utils.Category;
import com.phaseii.rxm.roomies.utils.DateUtils;
import com.phaseii.rxm.roomies.utils.SubCategory;
import com.phaseii.rxm.roomies.utils.TypefaceUtils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Snehankur on 5/6/2015.
 */
public class DetailExpenseAdapter extends RecyclerView.Adapter<DetailExpenseAdapter
        .ViewHolder> {

    private static final int HEADER = 3;
    private Context mContext;
    private List<RoomExpenses> roomExpensesList;
    private int LIST = 0;
    private int ALT_LIST = 1;
    private int start;
    private int day;
    private boolean isSorted;
    private SortType sortType;
    private Calendar cal = Calendar.getInstance();
    private Typeface typeface;

    public DetailExpenseAdapter(List<RoomExpenses> roomExpensesList, SortType sortType,
            Context mContext) {
        this.roomExpensesList = roomExpensesList;
        this.mContext = mContext;
        this.sortType = sortType;
        this.typeface = TypefaceUtils.getInstance(mContext).getFont(TypefaceUtils.VARELAROUND);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == HEADER) {
            view = LayoutInflater.from(mContext).inflate(R.layout.dashboard_header, parent,
                    false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.dashboard_expense_list, parent,
                    false);
        }
        ViewHolder vhItem = new ViewHolder(view, viewType);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.holderId == HEADER) {
            createLineChart(holder.lineChart);
            holder.month.setTypeface(Typeface.createFromAsset(mContext.getAssets(),
                    "fonts/VarelaRound-Regular.ttf"));
            holder.month.setText(new DateFormatSymbols().getMonths()[Calendar
                    .getInstance().get(Calendar.MONTH)]);
        } else {
            holder.amount.setText(mContext.getResources().getString(R.string.Rs) + String.valueOf
                    (roomExpensesList.get(position - 1).getAmount()));
            holder.description.setText(roomExpensesList.get(position - 1).getDescription());
            holder.quantity.setText(roomExpensesList.get(position - 1).getQuantity());
            holder.date.setText(DateUtils.dateToStringFormatter(roomExpensesList.get(position
                    - 1).getExpenseDate()));
            ShapeDrawable shapeDrawable = new ShapeDrawable();
            Paint paint = shapeDrawable.getPaint();
            ShapeDrawable time = new ShapeDrawable(new OvalShape());
            Category category = Category.getCategory(roomExpensesList.get(position - 1)
                    .getExpenseCategory());
            switch (category) {
                case RENT:
                    holder.expenseImage.setBackgroundResource(R.drawable.rent_drawable);
                    holder.expenseImage.setSelected(true);
                    break;
                case ELECTRICITY:
                    holder.expenseImage.setBackgroundResource(R.drawable.electricity_drawable);
                    holder.expenseImage.setSelected(true);
                    break;
                case MAID:
                    holder.expenseImage.setBackgroundResource(R.drawable.maid_drawable);
                    holder.expenseImage.setSelected(true);
                    break;
                case MISCELLANEOUS:
                    SubCategory subCategory = SubCategory.getSubcategory(
                            roomExpensesList.get(position - 1).getExpenseSubcategory());
                    switch (subCategory) {
                        case BILLS:
                            holder.expenseImage.setBackgroundResource(R.drawable.bills_drawable);
                            holder.expenseImage.setSelected(true);
                            break;
                        case GROCERY:
                            holder.expenseImage.setBackgroundResource(R.drawable.grocery_drawable);
                            holder.expenseImage.setSelected(true);
                            break;
                        case FOOD:
                            holder.expenseImage.setBackgroundResource(R.drawable.food_drawable);
                            holder.expenseImage.setSelected(true);
                            break;
                        case ENTERTAINMENT:
                            holder.expenseImage
                                    .setBackgroundResource(R.drawable.entertaintment_drawable);
                            holder.expenseImage.setSelected(true);
                            break;
                        case OTHERS:
                            holder.expenseImage.setBackgroundResource(R.drawable.others_drawable);
                            holder.expenseImage.setSelected(true);
                            break;
                    }

            }
        }
    }

    @Override
    public int getItemCount() {
        return roomExpensesList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return HEADER;
        }

        return position % 2;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void createLineChart(LineChart lineChart) {
        lineChart.setDescription("");
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightPerTapEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setData(getLineChart(roomExpensesList));
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.animateY(500);
        lineChart.animateX(500);
        lineChart.setPinchZoom(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setNoDataText("No Room expenses yet");
        lineChart.setBackgroundColor(mContext.getResources().getColor(R.color.primary_dark2_home));
        lineChart.setMarkerView(new CustomMarkerView(mContext, R.layout.custom_marker_view));

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setEnabled(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTypeface(typeface);
    }

    private LineData getLineChart(List<RoomExpenses> expenses) {

        List<RoomExpenses> roomExpensesList = new ArrayList<>();
        roomExpensesList.addAll(expenses);
        int index = 0;
        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<>();
        if (roomExpensesList.size() > 0) {
            switch (sortType) {
                case DATE_ASC:
                    cal.setTime(new Date());
                    int end = cal.get(Calendar.DAY_OF_MONTH);
                    for (int i = 1; i <= end; i++) {
                        labels.add(String.valueOf(i));
                        float val = 0;
                        for (RoomExpenses roomExpenses : expenses) {
                            cal.setTime(roomExpenses.getExpenseDate());
                            if (i == cal.get(Calendar.DAY_OF_MONTH)) {
                                val = val + roomExpenses.getAmount();
                            }
                        }
                        entries.add(new Entry(val, i - 1));
                    }
                    break;
                case DATE_DESC:
                    cal.setTime(new Date());
                    int start = cal.get(Calendar.DAY_OF_MONTH);
                    for (int i = start; i > 0; i--) {
                        labels.add(String.valueOf(i));
                        float val = 0;
                        for (RoomExpenses roomExpenses : expenses) {
                            cal.setTime(roomExpenses.getExpenseDate());
                            if (i == cal.get(Calendar.DAY_OF_MONTH)) {
                                val = val + roomExpenses.getAmount();
                            }
                        }
                        entries.add(new Entry(val, start - i));
                    }
                    break;
                case AMOUNT:
                    for (int i = 0; i < roomExpensesList.size(); i++) {
                        cal.setTime(roomExpensesList.get(i).getExpenseDate());
                        entries.add(new Entry(roomExpensesList.get(i).getAmount(), i));
                        labels.add(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
                    }
                    break;

                case QUANTITY:
                    for (int i = 0; i < roomExpensesList.size(); i++) {
                        if (null != roomExpensesList.get(i).getQuantity()) {
                            cal.setTime(roomExpensesList.get(i).getExpenseDate());
                            entries.add(new Entry(Float.valueOf(
                                    roomExpensesList.get(i).getQuantity().substring(0,
                                            roomExpensesList.get(i).getQuantity().length() - 2)),
                                    i));
                            labels.add(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        LineDataSet set = new LineDataSet(entries, "Daily Expense Report");
        set.setLineWidth(3f);
        set.setCircleColor(mContext.getResources().getColor(R.color.primary_dark2_home));
        set.setCircleSize(5f);
        set.setFillColor(mContext.getResources().getColor(R.color.primary2_home));
        set.setDrawValues(true);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setDrawFilled(true);
        set.setColor(Color.WHITE);
        /*set.enableDashedLine(10f, 5f, 0f);*/
        set.setValueTypeface(typeface);
        set.setValueTextColor(Color.WHITE);
        set.setDrawCubic(false);
        set.setFillColor(mContext.getResources().getColor(R.color.primary2_home));
        LineData lineData = new LineData(labels, set);
        return lineData;
    }

    public void addItemsToList(List<RoomExpenses> roomExpensesList, SortType sortType) {
        this.roomExpensesList = roomExpensesList;
        this.sortType = sortType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private int holderId;
        private TextView date;
        private TextView description;
        private TextView quantity;
        private TextView amount;
        private View itemView;
        private ImageButton expenseImage;
        private LineChart lineChart;
        private TextView month;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.itemView = itemView;
            this.holderId = viewType;
            if (viewType == HEADER) {
                lineChart = (LineChart) itemView.findViewById(R.id.trend_chart);
                month = (TextView) itemView.findViewById(R.id.month);
            } else {
                date = (TextView) itemView.findViewById(R.id.date);
                quantity = (TextView) itemView.findViewById(R.id.quantity);
                description = (TextView) itemView.findViewById(R.id.description);
                amount = (TextView) itemView.findViewById(R.id.amount);
                expenseImage = (ImageButton) itemView.findViewById(R.id.expense_icon);
            }
        }
    }
}