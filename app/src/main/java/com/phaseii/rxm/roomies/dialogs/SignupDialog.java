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
import com.phaseii.rxm.roomies.dao.RoomiesDao;
import com.phaseii.rxm.roomies.dao.UserDetailsDaoImpl;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.model.UserDetails;
import com.phaseii.rxm.roomies.util.ActivityUtils;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.util.RoomiesHelper;
import com.phaseii.rxm.roomies.util.ServiceParam;
import com.phaseii.rxm.roomies.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Snehankur on 4/10/2015.
 */
public class SignupDialog extends DialogFragment {

    RoomiesDao userService;
    Toast mToast;

    /**
     * Creates a new instance of signup dialog
     *
     * @return new instance of signup dialog
     */
    public static SignupDialog getInstance() {
        return new SignupDialog();
    }

    /**
     * on create
     *
     * @param savedInstanceState
     * @return dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final Context mContext = getActivity().getBaseContext();
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
                boolean isValidUsername = RoomiesHelper.setError("name", mContext, signupDialog);
                boolean isValidemail = RoomiesHelper.setError("email", mContext, signupDialog);
                boolean isValidPassword = RoomiesHelper.setError("password", mContext, signupDialog);

                if (isValidUsername && isValidemail && isValidPassword) {

                    UserDetails userDetails = new UserDetails();
                    userDetails.setUserAlias(username.getText().toString());
                    userDetails.setUsername(email.getText().toString());
                    userDetails.setPassword(password.getText().toString());

                    Map<ServiceParam, UserDetails> userMap = new HashMap<>();
                    userMap.put(ServiceParam.MODEL, userDetails);

                    userService = new UserDetailsDaoImpl(getActivity().getBaseContext());
                    int userId = userService.insertDetails(userMap);

                    if (userId > 0) {

                        /**
                         * If user details are entered into database sucessfully, store the same
                         * in shared preferences and start the get {@link com.phaseii.rxm.roomies
                         * .activity.GetStartedWizard GetStartedWizard}
                         */
                        SharedPreferences.Editor mEditor = mContext.getSharedPreferences
                                (RoomiesConstants.PREF_ROOMIES_KEY, Context.MODE_PRIVATE).edit();
                        mEditor.putString(RoomiesConstants.PREF_USER_ALIAS,
                                username.getText().toString());
                        mEditor.putString(RoomiesConstants.PREF_USERNAME,
                                email.getText().toString());
                        mEditor.putString(RoomiesConstants.PREF_USER_ID,
                                String.valueOf(userId));
                        mEditor.putBoolean(RoomiesConstants.IS_LOGGED_IN, true);
                        mEditor.apply();
                        ToastUtils.createToast(mContext,
                                "Welcome " + username.getText().toString() + ".\n You are now " +
                                        "registered with Roomies.", mToast);
                        try {
                            ActivityUtils.startActivityHelper(getActivity(), mContext.getResources()
                                    .getString(R.string.GetStartedWizard), null, true);
                        } catch (RoomXpnseMngrException e) {
                            ToastUtils.createToast(mContext, RoomiesConstants.APP_ERROR, mToast);
                        }
                        dialog.dismiss();
                    }
                }
            }
        });

        return dialog;
    }
}
