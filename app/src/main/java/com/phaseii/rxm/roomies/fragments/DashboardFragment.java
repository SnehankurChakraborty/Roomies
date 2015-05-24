package com.phaseii.rxm.roomies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.model.MiscExpense;
import com.phaseii.rxm.roomies.service.MiscServiceImpl;
import com.phaseii.rxm.roomies.view.DetailExpenseDataAdapter;
import com.phaseii.rxm.roomies.view.ScrollableLayoutManager;

import java.util.List;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.EMAIL_ID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_GOOGLE_FB_LOGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_INFO_FILE_KEY;

public class DashboardFragment extends RoomiesFragment
		implements RoomiesFragment.UpdatableFragment {

	private RecyclerView recyclerView;
	private List<MiscExpense> miscExpenses;
	private static Context mContext;

	public static DashboardFragment getInstance(Context mContext) {
		DashboardFragment.mContext = mContext;
		return new DashboardFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_dashboard, container,
				false);
		String username = mContext.getSharedPreferences(ROOM_INFO_FILE_KEY,
				Context.MODE_PRIVATE).getString(RoomiesConstants.NAME, null);

		boolean isGoogleFBLogin = mContext.getSharedPreferences
				(ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getBoolean(IS_GOOGLE_FB_LOGIN, false);
		if (isGoogleFBLogin) {
			username = mContext.getSharedPreferences
					(ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE).getString(EMAIL_ID, null);
		}
		miscExpenses = new MiscServiceImpl(mContext).getCurrentTotalMiscExpense(username);
		/*Collections.sort(miscExpenses, new Comparator<MiscExpense>() {
			@Override
			public int compare(MiscExpense lhs, MiscExpense rhs) {
				if (lhs.getTransactionDate().after(rhs.getTransactionDate())) {
					return -1;
				} else if (lhs.getTransactionDate().before(rhs.getTransactionDate())) {
					return 1;
				}

				return 0;
			}
		});*/


		/*Collections.sort(miscExpenses, new Comparator<MiscExpense>() {
			@Override
			public int compare(MiscExpense lhs, MiscExpense rhs) {
				if (lhs.getTransactionDate().before(rhs.getTransactionDate())) {
					return 1;
				} else {
					return -1;
				}
			}
		});*/
		recyclerView = (RecyclerView) rootView.findViewById(
				R.id.expense_detail_view);
		RecyclerView.Adapter adapter = new DetailExpenseDataAdapter(miscExpenses,
				getActivity().getBaseContext());
		recyclerView.setAdapter(adapter);
		RecyclerView.LayoutManager layoutManager = new ScrollableLayoutManager(getActivity(),
				LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layoutManager);

		/*RoomiesScrollView scrollViewHelper = (RoomiesScrollView) findViewById(
				R.id.scrollViewHelper);
		scrollViewHelper.setOnScrollViewListener(new RoomiesScrollView.OnScrollViewListener() {
			@Override
			public void onScrollChanged(RoomiesScrollView v, int l, int t, int oldl, int oldt) {
				setTitleAlpha(255 - getAlphaforActionBar(v.getScrollY()));
				cd.setAlpha(getAlphaforActionBar(v.getScrollY()));
				*//*parallaxImage(coloredBackgroundView);*//*
			}

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			private void parallaxImage(View view) {
				Rect rect = new Rect();
				view.getLocalVisibleRect(rect);
				if (lastTopValueAssigned != rect.top) {
					lastTopValueAssigned = rect.top;
					view.setY((float) (rect.top / 2.0));
				}
			}

			private int getAlphaforActionBar(int scrollY) {
				int minDist = 0, maxDist = (int) TypedValue.applyDimension(TypedValue
								.COMPLEX_UNIT_DIP, 250,
						getResources().getDisplayMetrics());
				if (scrollY > maxDist) {
					return 255;
				} else {
					if (scrollY < minDist) {
						return 0;
					} else {
						return (int) ((255.0 / maxDist) * scrollY);
					}
				}
			}

		});*/
		return rootView;

	}


	@Override
	public View getFragmentView() {
		return null;
	}

	@Override
	public void update() {

	}

/*	@Override
	public void update() {
		createCombinedChart();
	}*/


	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_dashboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}*/
}
