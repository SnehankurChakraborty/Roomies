package com.phaseii.rxm.roomies.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.manager.MemberDetailsManager;
import com.phaseii.rxm.roomies.manager.RoomExpensesManager;
import com.phaseii.rxm.roomies.model.MemberDetail;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.util.RoomiesHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Snehankur on 9/20/2015.
 */
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {
    private final Typeface typeface;
    private final Context mContext;
    private final List<MemberDetail> members;
    private List<RoomExpenses> expenses;
    private int index;

    private float totalMargin;
    private static final int HEADER = 0;

    public MembersAdapter(Context mContext) {
        this.mContext = mContext;
        members = new MemberDetailsManager(mContext).getMemberDetails();
        totalMargin = Float.valueOf(
                mContext.getSharedPreferences(RoomiesConstants.PREF_ROOMIES_KEY,
                        Context.MODE_PRIVATE).getString(RoomiesConstants.PREF_TOTAL_MARGIN, "0"));
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular" +
                ".ttf");
        expenses = new RoomExpensesManager(mContext).getRoomExpenses();
        index = 0;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (HEADER == viewType) {
            view = LayoutInflater.from(mContext).inflate(R.layout.member_header, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.member_list, parent, false);
        }

        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (HEADER == holder.viewType) {
            createLineChart(holder.lineChart);
        } else {
            if (index <= members.size() - 1) {
                holder.memberCardLeft.setVisibility(View.VISIBLE);
                holder.usernameLeft.setText(members.get(index).getUsername());
                holder.spentPercentLeft.setText(
                        String.valueOf((members.get(index).getTotalExpense()
                                / totalMargin) * 100) + "%");
                holder.spentLeft.setText(String.valueOf(members.get(index).getTotalExpense()));
                holder.dateLeft.setText(RoomiesHelper.getCurrentMonthYear());
                holder.adminLeft.setVisibility(View.VISIBLE);
                index++;
            }
            if (index <= members.size() - 1) {
                holder.memberCardRight.setVisibility(View.VISIBLE);
                holder.usernameRight.setText(members.get(index).getUsername());
                holder.spentPercentRight.setText(
                        String.valueOf((members.get(index).getTotalExpense()
                                / totalMargin) * 100) + "%");
                holder.spentRight.setText(String.valueOf(members.get(index).getTotalExpense()));
                holder.dateRight.setText(RoomiesHelper.getCurrentMonthYear());
                holder.adminRight.setVisibility(View.VISIBLE);
                index++;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (members.size() % 2 == 0) {
            return members.size() / 2 + 1;
        } else {
            return (members.size() + 1) / 2 + 1;
        }
    }

    private void createLineChart(LineChart lineChart) {

        lineChart.setDescription("");
        lineChart.setDrawGridBackground(false);
        lineChart.setHighlightEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setData(getLineChart(expenses));
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.animateY(500);
        lineChart.animateX(500);
        lineChart.setPinchZoom(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setNoDataText("No Room expenses yet");
        /*lineChart.setBackgroundColor(mContext.getResources().getColor(R.color.primary_dark5));*/
        lineChart.setBackgroundColor(Color.WHITE);
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
        xAxis.setTextColor(mContext.getResources().getColor(R.color.primary_dark5));
        xAxis.setTypeface(typeface);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else return position % 2;
    }

    private LineData getLineChart(List<RoomExpenses> expenses) {
        Set<Integer> userId = new HashSet<>();
        List<String> labels = new ArrayList<>();
        List<Entry> entries = new ArrayList<>();
        List<LineDataSet> lineDataSets = new ArrayList<>();

        for (int i = 1; i <= Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            entries.add(i - 1, new Entry(0f, i-1));
            labels.add(String.valueOf(i));
        }

        for (RoomExpenses expense : expenses) {
            userId.add(expense.getUserId());
        }

        for (Integer user : userId) {
            List<Entry> entriesSet = new ArrayList<>(entries);
            for (RoomExpenses expense : expenses) {
                if (expense.getUserId() == user) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(expense.getExpenseDate());
                    entriesSet.remove(cal.get(Calendar.DAY_OF_MONTH) - 1);
                    entriesSet.add(cal.get(Calendar.DAY_OF_MONTH) - 1, new Entry(expense.getAmount()
                            , cal.get(Calendar.DAY_OF_MONTH)));
                }
            }
            LineDataSet set = new LineDataSet(entriesSet, "Ramesh");
            set.setLineWidth(1f);
            set.setCircleColor(mContext.getResources().getColor(R.color.primary5));
            set.setCircleSize(3f);
            set.setDrawValues(false);
            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setColor(mContext.getResources().getColor(R.color.primary_dark5));
            lineDataSets.add(set);

        }


        LineData lineData = new LineData(labels, lineDataSets);
        return lineData;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private int viewType;
        private CardView memberCardLeft;
        private CardView memberCardRight;
        private TextView usernameLeft;
        private TextView usernameRight;
        private Button adminLeft;
        private Button adminRight;
        private TextView spentPercentLeft;
        private TextView spentPercentRight;
        private TextView spentLeft;
        private TextView spentRight;
        private TextView dateLeft;
        private TextView dateRight;
        private LineChart lineChart;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.itemView = itemView;
            this.viewType = viewType;
            if (HEADER == viewType) {
                lineChart = (LineChart) itemView.findViewById(R.id.member_chart);
            } else {
                memberCardLeft = (CardView) itemView.findViewById(R.id.member1);
                memberCardRight = (CardView) itemView.findViewById(R.id.member2);
                usernameLeft = (TextView) itemView.findViewById(R.id.user_name1);
                usernameRight = (TextView) itemView.findViewById(R.id.user_name2);
                spentPercentLeft = (TextView) itemView.findViewById(R.id.spent_percent1);
                spentPercentRight = (TextView) itemView.findViewById(R.id.spent_percent2);
                spentLeft = (TextView) itemView.findViewById(R.id.spent1);
                spentRight = (TextView) itemView.findViewById(R.id.spent2);
                dateLeft = (TextView) itemView.findViewById(R.id.date1);
                dateRight = (TextView) itemView.findViewById(R.id.date2);
                adminLeft = (Button) itemView.findViewById(R.id.admin_btn1);
                adminRight = (Button) itemView.findViewById(R.id.admin_btn2);
            }

        }
    }
}
