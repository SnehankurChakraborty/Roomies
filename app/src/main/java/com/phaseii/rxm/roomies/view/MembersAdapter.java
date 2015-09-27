package com.phaseii.rxm.roomies.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.manager.MemberDetailsManager;
import com.phaseii.rxm.roomies.model.MemberDetail;
import com.phaseii.rxm.roomies.util.RoomiesConstants;

import java.util.List;

/**
 * Created by Snehankur on 9/20/2015.
 */
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {
    private Context mContext;
    private List<MemberDetail> members;
    private float totalMargin;


    public MembersAdapter(Context mContext) {
        this.mContext = mContext;
        this.members = new MemberDetailsManager(mContext).getMemberDetails();
        this.totalMargin = Float.valueOf(
                mContext.getSharedPreferences(RoomiesConstants.PREF_ROOMIES_KEY,
                        Context.MODE_PRIVATE).getString(RoomiesConstants.PREF_TOTAL_MARGIN, "0"));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.members_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.username.setText(members.get(position).getUsername());
        holder.userStatus.setText(members.get(position).getUserStatus());
        holder.spentPercent.setText(String.valueOf((members.get(position).getTotalExpense()
                / totalMargin) * 100));
        holder.spentRatio.setText(
                String.valueOf(members.get(position).getTotalExpense()) + "/" + String.valueOf(totalMargin));
        holder.spentProgress.setMax((int) totalMargin);
        holder.spentProgress.setProgress((int) members.get(position).getTotalExpense());

    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView username;
        private TextView userStatus;
        private Button admin;
        private TextView spentPercent;
        private TextView spentRatio;
        private ProgressBar spentProgress;
        private TextView spentRecent;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            username = (TextView) itemView.findViewById(R.id.user_name);
            userStatus = (TextView) itemView.findViewById(R.id.user_status);
            spentPercent = (TextView) itemView.findViewById(R.id.spent_percent);
            spentRatio = (TextView) itemView.findViewById(R.id.spent_ratio);
            spentRecent = (TextView) itemView.findViewById(R.id.spent_recent);
            spentProgress = (ProgressBar) itemView.findViewById(R.id.spent_progress);
        }
    }
}
