package com.phaseii.rxm.roomies.helper;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
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

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.*;

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

	public static void startActivityHelper(Context context, String activity) throws
			RoomXpnseMngrException {
		try {
			Class activityClass = Class.forName(activity);
			Intent intent = new Intent(context, activityClass);
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
			}else{
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


}
