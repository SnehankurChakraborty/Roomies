/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phaseii.rxm.roomies.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.UserDetails;

/**
 * Created by Snehankur on 2/29/2016.
 */
public class EditProfileDialog extends DialogFragment {

    EditProfileListener listener;

    @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (EditProfileListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() + " must implement EditProfileListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_edit_profile, null);
        final UserDetails userDetails = listener.getUserDetails();
        ((EditText) dialogView.findViewById(R.id.edit_name)).setText(userDetails.getUserAlias());
        ((EditText) dialogView.findViewById(R.id.edit_phone))
                .setText(String.valueOf(userDetails.getPhone()));
        ((EditText) dialogView.findViewById(R.id.edit_location)).setText(userDetails.getLocation());
        ((EditText) dialogView.findViewById(R.id.edit_about_me)).setText(userDetails.getAboutMe());

        builder.setView(dialogView)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveDetails(dialogView, userDetails);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditProfileDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void saveDetails(View dialogView, UserDetails savedUserDetails) {
        UserDetails userDetails = null;
        String name = ((EditText) dialogView.findViewById(R.id.edit_name)).getText().toString();
        String phone = ((EditText) dialogView.findViewById(R.id.edit_phone))
                .getText().toString();
        String location = ((EditText) dialogView.findViewById(R.id.edit_location)).getText()
                .toString();
        String aboutMe = ((EditText) dialogView.findViewById(R.id.edit_about_me)).getText()
                .toString();
        if (!name.equals(savedUserDetails.getUserAlias())) {
            userDetails = new UserDetails();
            userDetails.setUserAlias(name);
        }
        if (!phone.equals(savedUserDetails.getPhone())) {
            if (null == userDetails) {
                userDetails = new UserDetails();
            }
            userDetails.setPhone(phone);
        }
        if (!location.equals(savedUserDetails.getLocation())) {
            if (null == userDetails) {
                userDetails = new UserDetails();
            }
            userDetails.setLocation(location);
        }
        if (!aboutMe.equals(savedUserDetails.getAboutMe())) {
            if (null == userDetails) {
                userDetails = new UserDetails();
            }
            userDetails.setAboutMe(aboutMe);
        }
        if (null != userDetails) {
            listener.onSaved(userDetails);
        }
    }

    public interface EditProfileListener {
        public UserDetails getUserDetails();

        public void onSaved(UserDetails userDetails);
    }
}
