package com.phaseii.rxm.roomies.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.RoomStats;
import com.phaseii.rxm.roomies.service.RoomStatManager;
import com.phaseii.rxm.roomies.utils.Constants;

import java.util.List;

public class ManageRoomActivity extends RoomiesBaseActivity {

    private RecyclerView.Adapter mRecylerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void configureView(Bundle savedInstanceStat) {
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        mtoolbar.setTitle("Manage Room");
        setSupportActionBar(mtoolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        new RetrieveUserRooms().execute(
                getSharedPreferences(Constants.PREF_ROOMIES_KEY, MODE_PRIVATE)
                        .getString(Constants.PREF_USER_ID, "-1"));
    }

    private void setUpRoomLists(List<RoomStats> roomStatsList) {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.room_listView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecylerAdapter = new ManageRoomAdapter(roomStatsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setAdapter(mRecylerAdapter);
    }

    private class RetrieveUserRooms extends AsyncTask<String, Void, List<RoomStats>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Override this method to perform a computation on a background thread. The specified
         * parameters are the parameters passed to {@link #execute} by the caller of this task.
         *
         * This method can call {@link #publishProgress} to publish updates on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected List<RoomStats> doInBackground(String... params) {
            RoomStatManager manager = new RoomStatManager(ManageRoomActivity.this);
            List<RoomStats> roomStatsList = manager.getAllRoomStats(params[0]);
            return roomStatsList;
        }

        @Override
        protected void onPostExecute(List<RoomStats> roomStatsList) {
            setUpRoomLists(roomStatsList);
        }
    }

    private class ManageRoomAdapter
            extends RecyclerView.Adapter<ManageRoomAdapter.ViewHolder> {

        private static final int TYPE_DEFAULT = 0;
        private static final int TYPE_ITEM = 1;
        private List<RoomStats> roomStatsList;

        public ManageRoomAdapter(List<RoomStats> roomStatsList) {
            this.roomStatsList = roomStatsList;
        }

        @Override
        public ViewHolder onCreateViewHolder(
                ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.manage_room_list_item, parent, false);
            ViewHolder vhItem = new ViewHolder(view, viewType);
            return vhItem;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,
                int position) {
            if (holder.viewType == TYPE_ITEM) {

            } else {
                holder.textView.setText("Add Room");
                holder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
            }

        }

        @Override public int getItemCount() {
            return roomStatsList.size() + 1;
        }

        @Override public int getItemViewType(int position) {
            if (position == roomStatsList.size()) {
                return TYPE_DEFAULT;
            } else {
                return TYPE_ITEM;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView textView;
            private ImageView imageView;
            private View itemView;
            private int viewType;

            public ViewHolder(View itemView, int viewType) {
                super(itemView);
                this.itemView = itemView;
                this.viewType = viewType;
                if (viewType == TYPE_ITEM) {
                    textView = (TextView) itemView.findViewById(R.id.roomName);
                    imageView = (ImageView) itemView.findViewById(R.id.roomIcon);
                } else {
                    textView = (TextView) itemView.findViewById(R.id.roomName);
                    imageView = (ImageView) itemView.findViewById(R.id.roomIcon);
                }
            }
        }
    }

}
