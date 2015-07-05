package com.phaseii.rxm.roomies.helper;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.model.RoomDetails;
import com.phaseii.rxm.roomies.model.RoomStats;
import com.phaseii.rxm.roomies.model.UserDetails;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.DELAY_MILLIS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_GOOGLE_FB_LOGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.IS_LOGGED_IN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ELECTRICITY_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_MAID_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_MISCELLANEOUS_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_MONTH_YEAR;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_NO_OF_MEMBERS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_RENT_MARGIN;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ROOM_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_ROOM_ID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_SENDER_ID;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_SETUP_COMPLETED;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_USERNAME;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_USER_ALIAS;
import static com.phaseii.rxm.roomies.helper.RoomiesConstants.PREF_USER_ID;

/**
 * Created by Snehankur on 2/23/2015.
 */
public class RoomiesHelper {

	public static boolean isFieldBlankOrEmpty(EditText editText) {
		boolean isBlankOrEmpty = false;
		String editTextData = editText.getText().toString();
		if (null == editTextData || editTextData.trim().equals("") || editTextData.length() == 0) {
			isBlankOrEmpty = true;
		}
		return isBlankOrEmpty;
	}

	public static void createToast(Context context, String text, Toast mToast) {
		RoomiesHelper helper = new RoomiesHelper();
		if (mToast != null) {
			mToast.cancel();
		}
		int duration = Toast.LENGTH_SHORT;
		mToast = Toast.makeText(context, text, duration);
		mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 400);
		mToast.show();
		helper.delayToast(mToast);
	}

	public static void startActivityHelper(Context context, String activity, Map<String,
			String> extras, boolean isFinish) throws
			RoomXpnseMngrException {
		try {
			Class activityClass = Class.forName(activity);
			Intent intent = new Intent(context, activityClass);
			if (extras != null) {
				Set<String> keySet = extras.keySet();
				Iterator<String> keyIterator = keySet.iterator();
				while (keyIterator.hasNext()) {
					String key = keyIterator.next();
					intent.putExtra(key, extras.get(key));
				}
			}
			if (isFinish) {
				ActivityCompat.finishAffinity((Activity) context);
			}
			context.startActivity(intent);
		} catch (ClassNotFoundException e) {
			throw new RoomXpnseMngrException(activity + " class not found", e);
		}
	}

	private void delayToast(final Toast mToast) {
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mToast.cancel();
			}
		}, DELAY_MILLIS);
	}

	public static void replaceFragment(String tag,
	                                   FragmentActivity activity, RoomiesFragment fragment) {
		FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
		transaction.replace(R.id.container, fragment, tag);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public static void replaceFragment(FragmentActivity activity, RoomiesFragment fragment) {
		FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
		transaction.replace(R.id.container, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public static boolean setError(String feildId, Context context, View view) {
		boolean isValid = true;
		String errorId = feildId + "_error";
		String togglId = feildId + "_toggle";
		Resources resources = context.getResources();
		String packageName = context.getPackageName();
		int resId = resources.getIdentifier(feildId, "id", packageName);
		int resErrorId = resources.getIdentifier(errorId, "id", packageName);
		int resToggleId = resources.getIdentifier(togglId, "id", packageName);
		EditText feild = (EditText) view.findViewById(resId);
		TextView errorFeild = (TextView) view.findViewById(resErrorId);
		if (resToggleId > 0) {
			ToggleButton toggleButton = (ToggleButton) view.findViewById(resToggleId);
			if (toggleButton.isChecked()) {
				if (isFieldBlankOrEmpty(feild)) {
					isValid = false;
					errorFeild.setVisibility(View.VISIBLE);
				} else {
					errorFeild.setVisibility(View.INVISIBLE);
				}
			} else {
				errorFeild.setVisibility(View.INVISIBLE);
			}
		} else {
			if (isFieldBlankOrEmpty(feild)) {
				isValid = false;
				errorFeild.setVisibility(View.VISIBLE);
			} else {
				errorFeild.setVisibility(View.INVISIBLE);
			}
		}
		return isValid;
	}

	public static boolean setNumericError(String feildId, Context context, View view) {
		boolean isValid = true;
		String errorId = feildId + "_error";
		String togglId = feildId + "_toggle";
		Resources resources = context.getResources();
		String packageName = context.getPackageName();
		int resId = resources.getIdentifier(feildId, "id", packageName);
		int resErrorId = resources.getIdentifier(errorId, "id", packageName);
		int resToggleId = resources.getIdentifier(togglId, "id", packageName);
		EditText feild = (EditText) view.findViewById(resId);
		TextView errorFeild = (TextView) view.findViewById(resErrorId);
		if (resToggleId > 0) {
			ToggleButton toggleButton = (ToggleButton) view.findViewById(resToggleId);
			if (toggleButton.isChecked()) {
				if (isFieldBlankOrEmpty(feild) || Float.valueOf(feild.getText().toString()) <= 0f) {
					isValid = false;
					errorFeild.setVisibility(View.VISIBLE);
				} else {
					errorFeild.setVisibility(View.INVISIBLE);
				}
			} else {
				errorFeild.setVisibility(View.INVISIBLE);
			}
		} else {
			if (isFieldBlankOrEmpty(feild) || Float.valueOf(feild.getText().toString()) <= 0f) {
				isValid = false;
				errorFeild.setVisibility(View.VISIBLE);
			} else {
				errorFeild.setVisibility(View.INVISIBLE);
			}
		}
		return isValid;
	}

	public static boolean cacheDBtoPreferences(Context context, UserDetails userDetails,
	                                           RoomDetails roomDetails, RoomStats roomStats,
	                                           boolean isGoogleFBLogin) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_ROOMIES_KEY,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor mEditor = sharedPreferences.edit();
		mEditor.putString(PREF_USER_ID, String.valueOf(userDetails.getUserId()));
		mEditor.putString(PREF_USERNAME, userDetails.getUsername());
		mEditor.putString(PREF_USER_ALIAS, userDetails.getUserAlias());
		mEditor.putString(PREF_SENDER_ID, userDetails.getSenderId());
		mEditor.putBoolean(PREF_SETUP_COMPLETED, userDetails.isSetupCompleted());
		mEditor.putString(PREF_ROOM_ID, String.valueOf(roomDetails.getRoomId()));
		mEditor.putString(PREF_ROOM_ALIAS, roomDetails.getRoomAlias());
		mEditor.putString(PREF_NO_OF_MEMBERS, String.valueOf(roomDetails.getNoOfPersons()));
		mEditor.putString(PREF_RENT_MARGIN, String.valueOf(roomStats.getRentMargin()));
		mEditor.putString(PREF_MAID_MARGIN, String.valueOf(roomStats.getMaidMargin()));
		mEditor.putString(PREF_ELECTRICITY_MARGIN,
				String.valueOf(roomStats.getElectricityMargin()));
		mEditor.putString(PREF_MISCELLANEOUS_MARGIN,
				String.valueOf(roomStats.getMiscellaneousMargin()));
		mEditor.putString(PREF_MONTH_YEAR, roomStats.getMonthYear());
		mEditor.putBoolean(IS_LOGGED_IN, true);
		if (isGoogleFBLogin) {
			mEditor.putBoolean(IS_GOOGLE_FB_LOGIN, true);
		}
		return true;

	}

	public static String[] addElement(String[] array, String element) {
		List<String> result = new ArrayList<>();
		for (String s : array) {
			result.add(s);
		}
		result.add(element);
		return result.toArray(new String[array.length + 1]);
	}

}
