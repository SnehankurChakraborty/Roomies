package com.phaseii.rxm.roomies.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.service.RoomService;
import com.phaseii.rxm.roomies.service.RoomServiceImpl;
import com.phaseii.rxm.roomies.service.UserService;
import com.phaseii.rxm.roomies.service.UserServiceImpl;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.APP_ERROR;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_GOOGLE_FB_LOGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_LOGGED_IN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_SETUP_COMPLETED;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MAID_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.MISC_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.RENT_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_BUDGET_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_ELECTRICITY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_INFO_FILE_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_MAID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_MISC;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_NAME;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_NO_OF_MEMBERS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_RENT;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.TAG_ROOM_EXPENSE;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.TAG_ROOM_INFO;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.TOTAL;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.TOTAL_MARGIN;
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
	SharedPreferences mSharedPref;

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
		AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
		builder.setMessage(R.string.join_message)
				.setTitle(R.string.join_title).setPositiveButton("ok",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
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
				startActivityHelper(this, getString(R.string.HomeScreen_Activity), null, true);
			} catch (RoomXpnseMngrException e) {
				createToast(this, APP_ERROR, mToast);
			}


		}
	}


	private void storeRoomInfo() {

		mSharedPref = getSharedPreferences(
				ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mSharedPref.edit();

		mEditor.putString("ROOM_ALIAS", roomName.getText().toString());
		mEditor.putString("ROOM_NO_OF_MEMBERS", noOfMembers.getText().toString());
		mEditor.putBoolean(IS_LOGGED_IN, true);
		float total = Float.valueOf(rent.getText().toString()) + Float.valueOf(maid.getText()
				.toString()) + Float.valueOf(electricity.getText().toString()) + Float.valueOf(
				miscellaneous.getText().toString());

		mEditor.putFloat(TOTAL, total);
		mEditor.apply();


		SharedPreferences sharedPreferences = getSharedPreferences(ROOM_BUDGET_FILE_KEY,
				MODE_PRIVATE);
		mEditor = sharedPreferences.edit();
		String rentVal = TextUtils.isEmpty(rent.getText().toString()) ? "0" : rent.getText()
				.toString();
		String maidVal = TextUtils.isEmpty(maid.getText().toString()) ? "0" : maid.getText()
				.toString();
		String miscVal = TextUtils.isEmpty(miscellaneous.getText().toString()) ? "0" :
				miscellaneous.getText().toString();
		String elecVal = TextUtils.isEmpty(electricity.getText().toString()) ? "0" :
				electricity.getText().toString();
		String totalVal = String.valueOf(
				Float.valueOf(rentVal) + Float.valueOf(maidVal) + Float.valueOf
						(elecVal) + Float.valueOf(miscVal));
		mEditor.putString(RENT_MARGIN, rentVal);
		mEditor.putString(MAID_MARGIN, maidVal);
		mEditor.putString(ELECTRICITY_MARGIN, elecVal);
		mEditor.putString(MISC_MARGIN, miscVal);
		mEditor.putString(TOTAL_MARGIN, totalVal);
		mEditor.apply();


		RoomService room = new RoomServiceImpl(this);
		UserService user = new UserServiceImpl(this);

		String username = mSharedPref.getString(RoomiesConstants.NAME, null);
		if (mSharedPref.getBoolean(IS_GOOGLE_FB_LOGIN, false)) {
			username = mSharedPref.getString(RoomiesConstants.EMAIL_ID, null);
		}

		room.insertRoomDetails(null, null, null,
				username, roomName.getText().toString());
		if (!mSharedPref.getBoolean(IS_GOOGLE_FB_LOGIN, false)) {
			user.completeSetup(mSharedPref.getString(RoomiesConstants.NAME, null));
		}else{
			mEditor=mSharedPref.edit();
			mEditor.putBoolean(IS_SETUP_COMPLETED,true);
			mEditor.apply();
		}
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
			return rootView;

		}

		@Override
		public View getFragmentView() {
			return rootView;
		}
	}
}
