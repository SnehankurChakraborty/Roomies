package com.phaseii.rxm.roomies.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.activity.HomeScreenActivity;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.fragments.HomeFragment;
import com.phaseii.rxm.roomies.fragments.SavingsFragment;
import com.phaseii.rxm.roomies.fragments.TrendFragment;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;

import java.io.File;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.APP_ERROR;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.HOME_FRAGMENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_BUDGET_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_EXPENDITURE_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_INFO_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.SAVINGS_FRAGMENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.TREND_FRAGMENT;

/**
 * Created by Snehankur on 4/4/2015.
 */
public class RoomiesRecyclerViewAdapter
		extends RecyclerView.Adapter<RoomiesRecyclerViewAdapter.ViewHolder> {

	Context mContext;
	private View headerView;


	public RoomiesRecyclerViewAdapter(String Titles[], int Icons[], String Name,
	                                  String Email, int Profile, Context mContext) {
		mNavTitles = Titles;
		mIcons = Icons;
		name = Name;
		email = Email;
		profile = Profile;
		this.mContext = mContext;
	}

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	private String mNavTitles[];
	private int mIcons[];
	private String name;
	private int profile;
	private String email;

	public static class ViewHolder extends RecyclerView.ViewHolder {

		int holderId;
		TextView textView;
		ImageView imageView;
		ImageView profileFrame;
		TextView name;
		TextView email;
		View itemView;

		public ViewHolder(View itemView, int ViewType, final Context mContext) {
			super(itemView);
			this.itemView = itemView;
			itemView.setClickable(true);
			itemView.setOnClickListener(new View.OnClickListener() {
			Toast mToast;

				@Override
				public void onClick(View v) {
					int pos = getPosition();
					if (pos == 5) {
						SharedPreferences mSharedPref = mContext.getSharedPreferences(
								ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
						SharedPreferences.Editor mEditor = mSharedPref.edit();
						mEditor.clear();
						mEditor.apply();
						mSharedPref = mContext.getSharedPreferences(
								ROOM_BUDGET_FILE_KEY, Context.MODE_PRIVATE);
						mEditor = mSharedPref.edit();
						mEditor.clear();
						mEditor.apply();
						mSharedPref = mContext.getSharedPreferences(
								ROOM_EXPENDITURE_FILE_KEY, Context.MODE_PRIVATE);
						mEditor = mSharedPref.edit();
						mEditor.clear();
						mEditor.apply();
						try {
							((HomeScreenActivity)mContext).revokeGplusAccess();
							RoomiesHelper.startActivityHelper(mContext,
									mContext.getResources()
											.getString(R.string.HomeScreen_Activity), null, true);

						} catch (RoomXpnseMngrException e) {
							RoomiesHelper.createToast(mContext, APP_ERROR, mToast);
							System.exit(0);
						}
					} else if (pos == 1) {
						((HomeScreenActivity) mContext).nextFragment(
								new HomeFragment(), HOME_FRAGMENT);
					} else if (pos == 4) {

						try {
							RoomiesHelper.startActivityHelper(mContext, mContext.getResources()
									.getString(R.string.ProfileActivity), null, false);
						} catch (RoomXpnseMngrException e) {
							RoomiesHelper.createToast(mContext, RoomiesConstants.APP_ERROR, mToast);
						}
					} else if (pos == 2) {
						((HomeScreenActivity) mContext).nextFragment(
								new TrendFragment(), TREND_FRAGMENT);
					} else if (pos == 3) {
						((HomeScreenActivity) mContext).nextFragment(
								new SavingsFragment(), SAVINGS_FRAGMENT);
					}
				}
			});
			if (ViewType == TYPE_ITEM) {
				textView = (TextView) itemView.findViewById(R.id.rowText);
				imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
				holderId = 1;
			} else {
				name = (TextView) itemView.findViewById(R.id.name);
				email = (TextView) itemView.findViewById(R.id.email);
				profileFrame = (ImageView) itemView.findViewById(R.id.profileFrame);
				final String username = mContext.getSharedPreferences(RoomiesConstants
						.ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).
						getString(RoomiesConstants.NAME, null);
				profileFrame.setOnClickListener(new View.OnClickListener() {
					Toast mToast = null;

					@Override
					public void onClick(View v) {
						try {
							RoomiesHelper.startActivityHelper(mContext, mContext.getResources()
									.getString(R.string.ProfileActivity), null, false);
						} catch (RoomXpnseMngrException e) {
							RoomiesHelper.createToast(mContext, RoomiesConstants.APP_ERROR, mToast);
						}
					}
				});

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeFile(new File(mContext.getFilesDir(),
						username + mContext.getResources().getString(
								R.string.PROFILEJPG)).getAbsolutePath(), options);
				((HomeScreenActivity) mContext).updateProfilePic(bitmap, R.drawable.ic_profile_pic);
				holderId = 0;
			}
		}

	}


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_ITEM) {
			View v = LayoutInflater.from(parent.getContext()).
					inflate(R.layout.drawer_list_item, parent, false);
			ViewHolder vhItem = new ViewHolder(v,
					viewType,
					parent.getContext());
			return vhItem;
		} else if (viewType == TYPE_HEADER) {
			headerView = LayoutInflater.from(parent.getContext()).
					inflate(R.layout.drawer_header, parent, false);
			ViewHolder vhHeader = new ViewHolder(headerView,
					viewType, parent.getContext());
			return vhHeader;
		}
		return null;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		if (viewHolder.holderId == 1) {
			viewHolder.textView.setText(mNavTitles[position - 1]);
			viewHolder.imageView.setImageResource(mIcons[position - 1]);
		} else {
			viewHolder.name.setText(name);
			viewHolder.email.setText(email);
		}
	}


	@Override
	public int getItemCount() {
		return mNavTitles.length + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position))
			return TYPE_HEADER;

		return TYPE_ITEM;
	}

	public View getHeaderView() {
		return headerView;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}


}
