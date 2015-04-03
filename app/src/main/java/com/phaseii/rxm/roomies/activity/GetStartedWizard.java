package com.phaseii.rxm.roomies.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.*;

import static com.phaseii.rxm.roomies.helper.RoomiesHelper.createToast;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.replaceFragment;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.setError;
import static com.phaseii.rxm.roomies.helper.RoomiesHelper.startActivityHelper;

public class GetStartedWizard extends FragmentActivity {

	FragmentTransaction transaction;
	Toast mToast;
	EditText roomName;
	EditText noOfMembers;
	EditText rent;
	EditText maid;
	EditText electricity;
	EditText miscellaneous;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_started_wizard);
		if (savedInstanceState == null) {
			transaction = getSupportFragmentManager().beginTransaction();
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			transaction.add(R.id.container, new StartFragment()).commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_get_started_wizard, menu);
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
	}

	public void getStartedClicked(View view) {
		RoomiesFragment selectorFragment = new SelectorFragment();
		replaceFragment(this, selectorFragment);
	}

	public void joinRoomSelected(View view) {

	}

	public void createRoomSelected(View view) {
		RoomiesFragment roomInfoFragment = new RoomInfoFragment();
		replaceFragment(TAG_ROOM_INFO, this, roomInfoFragment);
	}

	public void roomInfoCompleted(View view) {
		View fragmentView = ((RoomInfoFragment) getSupportFragmentManager().findFragmentByTag
				(TAG_ROOM_INFO)).getFragmentView();
		roomName = (EditText) findViewById(R.id.room_name);
		boolean isValidName = setError(ROOM_NAME, this, fragmentView);
		noOfMembers = (EditText) findViewById(R.id.no_of_members);
		boolean isValidNumber = setError(ROOM_NO_OF_MEMBERS, this, fragmentView);
		if (isValidName && isValidNumber) {
			RoomiesFragment roomExpenseFragment = new RoomExpenseFragment();
			replaceFragment(TAG_ROOM_EXPENSE, this, roomExpenseFragment);
		}
	}

	public void roomExpenseCompleted(View view) {
		View fragmentView = ((RoomExpenseFragment) getSupportFragmentManager().findFragmentByTag
				(TAG_ROOM_EXPENSE)).getFragmentView();

		rent = (EditText) findViewById(R.id.room_rent);
		boolean isValidRent = setError(ROOM_RENT, this, fragmentView);
		maid = (EditText) findViewById(R.id.room_maid);
		boolean isValidMaid = setError(ROOM_MAID, this, fragmentView);
		electricity = (EditText) findViewById(R.id.room_electricity);
		boolean isValidElec = setError(ROOM_ELECTRICITY, this, fragmentView);
		miscellaneous = (EditText) findViewById(R.id.room_misc);
		boolean isValidMisc = setError(ROOM_MISC, this, fragmentView);
		if (isValidRent && isValidMaid && isValidElec && isValidMisc) {
			storeRoomInfo();
			try {
				startActivityHelper(this, getString(R.string.HomeScreen_Activity));
			} catch (RoomXpnseMngrException e) {
				createToast(this, APP_ERROR, mToast);
			}


		}
	}


	private void storeRoomInfo() {
		SharedPreferences mSharedPref = getSharedPreferences(
				ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mSharedPref.edit();
		mEditor.putString("ROOM_ALIAS", roomName.getText().toString());
		mEditor.putString("ROOM_NO_OF_MEMBERS", noOfMembers.getText().toString());
		mEditor.apply();
		SharedPreferences sharedPreferences = getSharedPreferences(ROOM_EXPENSE_FILE_KEY,
				MODE_PRIVATE);
		mEditor = sharedPreferences.edit();
		mEditor.putString(RENT,
				TextUtils.isEmpty(rent.getText().toString()) ? "0" : rent.getText().toString());
		mEditor.putString(MAID,
				TextUtils.isEmpty(maid.getText().toString()) ? "0" : maid.getText().toString());
		mEditor.putString(ELECTRICITY, TextUtils.isEmpty(
				electricity.getText().toString()) ? "0" : electricity.getText().toString());
		mEditor.putString(MISC, TextUtils.isEmpty(
				miscellaneous.getText().toString()) ? "0" : miscellaneous.getText().toString());
		mEditor.apply();
	}


	/**
	 * ***************************************
	 * A start fragment containing a simple view.
	 * *****************************************
	 */
	public static class StartFragment extends RoomiesFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_start, container,
					false);
			return rootView;
		}

		public View getFragmentView() {
			return rootView;
		}
	}


	/**
	 * ***************************************
	 * A start fragment containing a simple view.
	 * *****************************************
	 */


	public static class SelectorFragment extends RoomiesFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_selector, container,
					false);

			return rootView;
		}

		@Override
		public View getFragmentView() {
			return rootView;
		}

	}


	/**
	 * ***************************************
	 * A start fragment containing a simple view.
	 * *****************************************
	 */

	public static class RoomInfoFragment extends RoomiesFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_room_info, container,
					false);
			return rootView;
		}

		@Override
		public View getFragmentView() {
			return rootView;
		}
	}


	/**
	 * ***************************************
	 * A start fragment containing a simple view.
	 * *****************************************
	 */

	public static class RoomExpenseFragment extends RoomiesFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_room_expense, container,
					false);
			final EditText roomRent = (EditText) rootView.findViewById(R.id.room_rent);
			final EditText roomMaid = (EditText) rootView.findViewById(R.id.room_maid);
			final EditText roomElectricity = (EditText) rootView.findViewById(
					R.id.room_electricity);
			final EditText roomMisc = (EditText) rootView.findViewById(R.id.room_misc);

			/*final ToggleButton toggleRent = (ToggleButton) rootView.findViewById(
					R.id.room_rent_toggle);
			final ToggleButton toggleMaid = (ToggleButton) rootView.findViewById(
					R.id.room_maid_toggle);
			final ToggleButton toggleElectricity = (ToggleButton) rootView.findViewById(R.id
					.room_electricity_toggle);
			final ToggleButton toggleMisc = (ToggleButton) rootView.findViewById(
					R.id.room_misc_toggle);*/
			/*toggleRent.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!toggleRent.isChecked()) {
						roomRent.setEnabled(false);
						roomRent.setText("");
					} else {
						roomRent.setEnabled(true);
					}
				}
			});
			toggleMaid.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!toggleMaid.isChecked()) {
						roomMaid.setEnabled(false);
						roomMaid.setText("");
					} else {
						roomMaid.setEnabled(true);
					}
				}
			});
			toggleElectricity.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!toggleElectricity.isChecked()) {
						roomElectricity.setEnabled(false);
						roomElectricity.setText("");
					} else {
						roomElectricity.setEnabled(true);
					}
				}
			});
			toggleMisc.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!toggleMisc.isChecked()) {
						toggleMisc.setChecked(true);
					}
				}
			});*/
			return rootView;

		}

		@Override
		public View getFragmentView() {
			return rootView;
		}
	}
}
