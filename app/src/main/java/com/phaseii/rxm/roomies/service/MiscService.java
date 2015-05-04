package com.phaseii.rxm.roomies.service;

import android.widget.EditText;

import com.phaseii.rxm.roomies.model.MiscExpense;

import java.util.List;

/**
 * Created by Snehankur on 4/18/2015.
 */
public interface MiscService {
	public void insertMiscExpenses(EditText description, EditText quantity, EditText amount,
	                        String type, String username);

	public List<MiscExpense> getCurrentTotalMiscExpense();

	public List<String> getMiscMonths(String username);

    public boolean updateUser(String username, String newVal);
}
