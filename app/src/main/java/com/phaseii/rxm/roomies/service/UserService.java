package com.phaseii.rxm.roomies.service;

import android.widget.EditText;

/**
 * Created by Snehankur on 4/10/2015.
 */
public interface UserService {
	String getPassword(String username);

	boolean registerUser(EditText username, EditText email, EditText password);

	void retrieveUserData();

	void completeSetup(String username);

	boolean  isSetupCompleted(String username);
}
