package com.phaseii.rxm.roomies.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.manager.MemberDetailsManager;
import com.phaseii.rxm.roomies.model.MemberDetail;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.util.DateUtils;
import com.phaseii.rxm.roomies.util.RoomiesConstants;

import java.util.List;

/**
 * Created by Snehankur on 9/20/2015.
 */
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    private View view;
    private final Typeface typeface;
    private final Context mContext;
    private final List<MemberDetail> members;
    private List<RoomExpenses> expenses;
    private int index;
    private final String titles[] = {"Day", "Week", "Month"};
    private final int numOfTabs = 3;
    private MemberPagerAdapter adapter;
    private FragmentManager mFragmentManager;

    private float totalMargin;
    private static final int HEADER = 0;

    public MembersAdapter(Context mContext, FragmentManager fragmentManager, List<RoomExpenses>
            expenses) {
        this.mContext = mContext;
        this.members = new MemberDetailsManager(mContext).getMemberDetails();
        totalMargin = Float.valueOf(
                mContext.getSharedPreferences(RoomiesConstants.PREF_ROOMIES_KEY,
                        Context.MODE_PRIVATE).getString(RoomiesConstants.PREF_TOTAL_MARGIN, "0"));
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/VarelaRound-Regular" +
                ".ttf");
        this.expenses = expenses;
        mFragmentManager = fragmentManager;
        index = 0;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.member_list, parent, false);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (index <= members.size() - 1) {
            holder.memberCardLeft.setVisibility(View.VISIBLE);
            holder.usernameLeft.setText(members.get(index).getUsername());
            holder.spentPercentLeft.setText(
                    String.valueOf((members.get(index).getTotalExpense()
                            / totalMargin) * 100) + "%");
            holder.spentLeft.setText(String.valueOf(members.get(index).getTotalExpense()));
            holder.dateLeft.setText(DateUtils.getCurrentMonthYear());
            holder.adminLeft.setVisibility(View.VISIBLE);
            adapter = new MemberPagerAdapter(mContext, mFragmentManager, titles, numOfTabs,
                    expenses);
            holder.pagerLeft.setAdapter(adapter);
            holder.tabLeft.setDistributeEvenly(true);
            holder.tabLeft.setCustomTabColorizer(new RoomiesSlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return mContext.getResources().getColor(R.color.primary_dark5);
                }
            });
            holder.tabLeft.setViewPager(holder.pagerLeft);
            index++;
        }
        if (index <= members.size() - 1) {
            holder.memberCardRight.setVisibility(View.VISIBLE);
            holder.usernameRight.setText(members.get(index).getUsername());
            holder.spentPercentRight.setText(
                    String.valueOf((members.get(index).getTotalExpense()
                            / totalMargin) * 100) + "%");
            holder.spentRight.setText(String.valueOf(members.get(index).getTotalExpense()));
            holder.dateRight.setText(DateUtils.getCurrentMonthYear());
            holder.adminRight.setVisibility(View.VISIBLE);
            index++;
        }

    }

    @Override
    public int getItemCount() {
        if (members.size() % 2 == 0) {
            return members.size() / 2;
        } else {
            return (members.size() + 1) / 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else return position % 2;
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
        private ViewPager pagerLeft;
        private ViewPager pagerRight;
        private RoomiesSlidingTabLayout tabLeft;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.itemView = itemView;
            this.viewType = viewType;

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
            pagerLeft = (ViewPager) itemView.findViewById(R.id.member_pager);
            tabLeft = (RoomiesSlidingTabLayout) itemView.findViewById(R.id.tabs);
            /*pagerRight = (ViewPager) itemView.findViewById(R.id.pager2);*/
        }
    }
}
