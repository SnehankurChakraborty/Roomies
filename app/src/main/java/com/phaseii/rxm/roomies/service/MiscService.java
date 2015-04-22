package com.phaseii.rxm.roomies.service;

import android.widget.EditText;

import com.phaseii.rxm.roomies.model.MiscExpense;

import java.util.List;

/**
 * Created by Snehankur on 4/18/2015.
 */
public interface MiscService {
	void insertMiscExpenses(EditText description, EditText quantity, EditText amount,
	                        String type, String username);

	List<MiscExpense> getCurrentTotalMiscExpense();

	List<String> getMiscMonths(String username);
}
