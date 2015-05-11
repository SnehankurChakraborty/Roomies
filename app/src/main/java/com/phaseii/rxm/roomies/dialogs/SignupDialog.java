package com.phaseii.rxm.roomies.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;
import com.phaseii.rxm.roomies.helper.RoomiesHelper;
import com.phaseii.rxm.roomies.service.UserServiceImpl;

/**
 * Created by Snehankur on 4/10/2015.
 */
public class SignupDialog extends DialogFragment {


	static Context mContext;
	UserServiceImpl user;
	Toast mToast;

	public static SignupDialog getInstance(Context context) {
		mContext = context;
		return new SignupDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View signupDialog = inflater.inflate(R.layout.dialog_signup, null);
		Button positive = (Button) signupDialog.findViewById(R.id.signup);
		final AlertDialog dialog = builder.setView(signupDialog).create();
		positive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText username = (EditText) signupDialog.findViewById(R.id.name);
				EditText email = (EditText) signupDialog.findViewById(R.id.email);
				EditText password = (EditText) signupDialog.findViewById(R.id.password);
				boolean isValidUsername = RoomiesHelper.setError("name", mContext,
						signupDialog);
				boolean isValidemail = RoomiesHelper.setError("email", mContext, signupDialog);
				boolean isValidPassword = RoomiesHelper.setError("password", mContext,
						signupDialog);
				if (isValidUsername && isValidemail && isValidPassword) {
					user = new UserServiceImpl(mContext);
					boolean isUserRegistered = user.registerUser(username, email, password);
					if (isUserRegistered) {
						SharedPreferences sharedPref = mContext.getSharedPreferences
								(RoomiesConstants.ROOM_INFO_FILE_KEY, Context.MODE_PRIVATE);
						SharedPreferences.Editor mEditor = sharedPref.edit();
						mEditor.putString(RoomiesConstants.NAME, username.getText().toString());
						mEditor.putString(RoomiesConstants.EMAIL_ID, email.getText().toString());
						mEditor.putBoolean(RoomiesConstants.IS_LOGGED_IN, true);
						mEditor.apply();
						try {
							RoomiesHelper.startActivityHelper(getActivity(), mContext.getResources()
									.getString(R.string.GetStartedWizard), null, true);
						} catch (RoomXpnseMngrException e) {
							RoomiesHelper.createToast(mContext, RoomiesConstants.APP_ERROR, mToast);
						}
						dialog.dismiss();
					}
				}
			}
		});

		return dialog;
	}
}
