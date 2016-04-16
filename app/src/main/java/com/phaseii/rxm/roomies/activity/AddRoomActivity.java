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

package com.phaseii.rxm.roomies.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.RoomDetails;
import com.phaseii.rxm.roomies.database.model.RoomExpenses;
import com.phaseii.rxm.roomies.database.model.RoomStats;
import com.phaseii.rxm.roomies.database.model.UserDetails;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.factory.ViewBuilder;
import com.phaseii.rxm.roomies.factory.ViewBuilderFactory;
import com.phaseii.rxm.roomies.service.RoomUserStatManager;
import com.phaseii.rxm.roomies.utils.ActivityUtils;
import com.phaseii.rxm.roomies.utils.Constants;
import com.phaseii.rxm.roomies.utils.DateUtils;
import com.phaseii.rxm.roomies.utils.RoomiesHelper;
import com.phaseii.rxm.roomies.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Snehankur on 1/23/2016.
 */
public class AddRoomActivity extends RoomiesBaseActivity {

    private View stepperRoomNameBody;
    private View stepperRoomExpenseCategoryBody;
    private View stepperRoomExpenseLimitBody;
    private View stepperRoomBudgetBody;
    private View connectorRoomName;
    private View connectorRoomExpenseCategory;
    private View connectorRoomExpenseLimit;
    private TextView bubbleRoomName;
    private TextView bubbleRoomExpenseCategories;
    private TextView bubbleRoomExpenseLimit;
    private TextView bubbleRoomBudget;
    private SeekBar sliderRoomRent;
    private SeekBar sliderRoomMaid;
    private SeekBar sliderRoomElec;
    private SeekBar sliderRoomMisc;
    private EditText roomExpenseLimit;
    private EditText rentBudget;
    private EditText maidBudget;
    private EditText elecBudget;
    private EditText miscBudget;
    private EditText roomName;
    private TextInputLayout roomNameLayout;
    private RoomDetails roomDetails;
    private UserDetails userDetail;
    private RoomStats roomStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public void configureView(Bundle savedInstanceStat) {
        setContentView(R.layout.activity_get_started);
        setUpView();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_started, menu);
        return true;
    }*/

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

    public void setUpView() {
        ViewBuilder builder = ViewBuilderFactory.newInstance(AddRoomActivity.this)
                .newViewBuilder();
        setSupportActionBar((Toolbar) builder.getView(R.id.toolbar_get_started));
        getSupportActionBar().setTitle("Set up Room");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        stepperRoomNameBody = builder.getView(R.id.stepper_room_name_body);
        stepperRoomExpenseCategoryBody = builder.getView(R.id.stepper_room_expense_categories_body);
        stepperRoomExpenseLimitBody = builder.getView(R.id.stepper_room_expense_limit_body);
        stepperRoomBudgetBody = builder.getView(R.id.stepper_room_budget_body);
        connectorRoomName = builder.getView(R.id.stepper_room_name_connector);
        connectorRoomExpenseCategory = builder
                .getView(R.id.stepper_room_expense_categories_connector);
        connectorRoomExpenseLimit = builder.getView(R.id.stepper_room_expense_limit_connector);
        bubbleRoomName = builder.getTextView(R.id.stepper_room_name_bubble);
        bubbleRoomExpenseCategories = builder
                .getTextView(R.id.stepper_room_expense_categories_bubble);
        bubbleRoomExpenseLimit = builder.getTextView(R.id.stepper_room_expense_limit_bubble);
        bubbleRoomBudget = builder.getTextView(R.id.stepper_room_budget_bubble);
        sliderRoomRent = (SeekBar) builder.getView(R.id.room_rent_slider);
        sliderRoomMaid = (SeekBar) builder.getView(R.id.room_maid_slider);
        sliderRoomElec = (SeekBar) builder.getView(R.id.room_elec_slider);
        sliderRoomMisc = (SeekBar) builder.getView(R.id.room_misc_slider);
        roomExpenseLimit = builder.getEditText(R.id.room_budget_limit);
        rentBudget = builder.getEditText(R.id.rent_budget);
        maidBudget = builder.getEditText(R.id.maid_budget);
        elecBudget = builder.getEditText(R.id.elec_budget);
        miscBudget = builder.getEditText(R.id.misc_budget);
        roomName = builder.getEditText(R.id.input_room_name);
        roomNameLayout = (TextInputLayout) builder.getView(R.id.input_layout_room_name);
        builder.setAsToggle(R.id.rent_button, true);
        builder.setAsToggle(R.id.maid_button, true);
        builder.setAsToggle(R.id.elec_button, true);
        builder.setAsToggle(R.id.misc_button, true);
        builder.getView(R.id.rent_button_limit).setSelected(true);
        builder.getView(R.id.maid_button_limit).setSelected(true);
        builder.getView(R.id.elec_button_limit).setSelected(true);
        builder.getView(R.id.misc_button_limit).setSelected(true);
        addListeners(builder.getButton(R.id.continue_room_name));
        addListeners(builder.getButton(R.id.continue_room_categories));
        addListeners(builder.getButton(R.id.continue_room_budget_limit));
        addListeners(builder.getButton(R.id.continue_budget));
        addListeners(builder.getButton(R.id.back_to_room_name));
        addListeners(builder.getButton(R.id.skip_room_categories));
        addListeners(builder.getButton(R.id.back_to_room_expense_limit));
        addListeners(builder.getButton(R.id.back_to_room_categories));

    }

    private void addListeners(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.continue_room_name:
                        if (roomName.getText().toString().trim().length() == 0) {
                            bubbleRoomName.setText("");
                            bubbleRoomName.setBackgroundResource(R.drawable.warning_drawable);
                            roomNameLayout.setError("Please enter a room name");
                        } else {
                            bubbleRoomName.setText("");
                            bubbleRoomName.setBackgroundResource(R.drawable.done_drawable);
                            connectorRoomName.setVisibility(View.VISIBLE);
                            connectorRoomExpenseCategory.setVisibility(View.GONE);
                            stepperRoomNameBody.setVisibility(View.GONE);
                            stepperRoomExpenseCategoryBody.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.continue_room_categories:
                        bubbleRoomExpenseCategories.setText("");
                        bubbleRoomExpenseCategories.setBackgroundResource(R.drawable.done_drawable);
                        stepperRoomExpenseCategoryBody.setVisibility(View.GONE);
                        connectorRoomExpenseLimit.setVisibility(View.GONE);
                        connectorRoomExpenseCategory.setVisibility(View.VISIBLE);
                        stepperRoomExpenseLimitBody.setVisibility(View.VISIBLE);
                        break;
                    case R.id.continue_room_budget_limit:
                        int expenseLimit = 10000;
                        if (roomExpenseLimit.getText().toString().trim().length() > 0) {
                            expenseLimit = Integer.parseInt(roomExpenseLimit.getText().toString());
                        }
                        bubbleRoomExpenseLimit.setText("");
                        bubbleRoomExpenseLimit.setBackgroundResource(R.drawable.done_drawable);
                        connectorRoomExpenseLimit.setVisibility(View.VISIBLE);
                        stepperRoomExpenseLimitBody.setVisibility(View.GONE);
                        stepperRoomBudgetBody.setVisibility(View.VISIBLE);
                        setUpSliders(expenseLimit);
                        break;
                    case R.id.continue_budget:
                        bubbleRoomBudget.setText("");
                        bubbleRoomBudget.setBackgroundResource(R.drawable.done_drawable);
                        storeRoomDetails();
                        break;
                    case R.id.back_to_room_name:
                        bubbleRoomName.setBackgroundResource(R.drawable.stepper_bubble);
                        bubbleRoomName.setText(R.string.one);
                        connectorRoomName.setVisibility(View.GONE);
                        stepperRoomExpenseCategoryBody.setVisibility(View.GONE);
                        stepperRoomNameBody.setVisibility(View.VISIBLE);
                        connectorRoomExpenseCategory.setVisibility(View.VISIBLE);
                        break;
                    case R.id.skip_room_categories:
                        connectorRoomExpenseCategory.setVisibility(View.VISIBLE);
                        stepperRoomExpenseCategoryBody.setVisibility(View.GONE);
                        connectorRoomExpenseLimit.setVisibility(View.GONE);
                        stepperRoomExpenseLimitBody.setVisibility(View.VISIBLE);
                        break;
                    case R.id.back_to_room_categories:
                        bubbleRoomExpenseCategories
                                .setBackgroundResource(R.drawable.stepper_bubble);
                        bubbleRoomExpenseCategories.setText(R.string.two);
                        stepperRoomExpenseLimitBody.setVisibility(View.GONE);
                        connectorRoomExpenseLimit.setVisibility(View.VISIBLE);
                        connectorRoomExpenseCategory.setVisibility(View.GONE);
                        stepperRoomExpenseCategoryBody.setVisibility(View.VISIBLE);
                        break;
                    case R.id.back_to_room_expense_limit:
                        bubbleRoomExpenseLimit.setBackgroundResource(R.drawable.stepper_bubble);
                        bubbleRoomExpenseLimit.setText(R.string.three);
                        stepperRoomBudgetBody.setVisibility(View.GONE);
                        connectorRoomExpenseLimit.setVisibility(View.GONE);
                        stepperRoomExpenseLimitBody.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void storeRoomDetails() {
        String username = getSharedPreferences(Constants.PREF_ROOMIES_KEY, MODE_PRIVATE)
                .getString(Constants.PREF_USERNAME, null);
        if (null != username) {
            roomDetails = new RoomDetails();
            roomDetails.setRoomAlias(roomName.getText().toString());
            roomDetails.setNoOfPersons(0);
            roomStats = new RoomStats();
            roomStats.setMonthYear(DateUtils.getCurrentMonthYear());
            roomStats.setRentMargin(Integer.parseInt(rentBudget.getText().toString()));
            roomStats.setMaidMargin(Integer.parseInt(maidBudget.getText().toString()));
            roomStats.setElectricityMargin(Integer.parseInt(elecBudget.getText().toString()));
            roomStats.setMiscellaneousMargin(Integer.parseInt(miscBudget.getText().toString()));
            new SaveRoomDetails().execute(username);
        } else {
            ToastUtils.createToast(AddRoomActivity.this, Constants.APP_ERROR, null);
            System.exit(0);
        }
    }

    private void setUpSliders(int expenseLimit) {
        sliderRoomRent.setMax(expenseLimit);
        sliderRoomMaid.setMax(expenseLimit);
        sliderRoomElec.setMax(expenseLimit);
        sliderRoomMisc.setMax(expenseLimit);
        sliderRoomRent.setProgress(expenseLimit / 2);
        sliderRoomMaid.setProgress(expenseLimit / 2);
        sliderRoomElec.setProgress(expenseLimit / 2);
        sliderRoomMisc.setProgress(expenseLimit / 2);
        rentBudget.setText(String.valueOf(expenseLimit / 2), TextView.BufferType.EDITABLE);
        maidBudget.setText(String.valueOf(expenseLimit / 2), TextView.BufferType.EDITABLE);
        elecBudget.setText(String.valueOf(expenseLimit / 2), TextView.BufferType.EDITABLE);
        miscBudget.setText(String.valueOf(expenseLimit / 2), TextView.BufferType.EDITABLE);
        sliderRoomRent.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                            boolean fromUser) {
                        if (fromUser) {
                            rentBudget.setText
                                    (String.valueOf(progress),
                                            TextView.BufferType.EDITABLE);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
        rentBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sliderRoomRent.setProgress(Integer.parseInt(s.toString()));
            }
        });
        sliderRoomMaid.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                            boolean fromUser) {
                        if (fromUser) {
                            maidBudget.setText
                                    (String.valueOf(progress),
                                            TextView.BufferType.EDITABLE);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
        maidBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sliderRoomMaid.setProgress(Integer.parseInt(s.toString()));
            }
        });
        sliderRoomElec.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                            boolean fromUser) {
                        if (fromUser) {
                            elecBudget.setText
                                    (String.valueOf(progress),
                                            TextView.BufferType.EDITABLE);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
        elecBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sliderRoomElec.setProgress(Integer.parseInt(s.toString()));
            }
        });
        sliderRoomMisc.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress,
                            boolean fromUser) {
                        if (fromUser) {
                            miscBudget.setText
                                    (String.valueOf(progress),
                                            TextView.BufferType.EDITABLE);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
        miscBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sliderRoomMisc.setProgress(Integer.parseInt(s.toString()));
            }
        });

    }

    /**
     * Background task to save the room details in database
     */
    private class SaveRoomDetails extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(AddRoomActivity.this);
            dialog.setMessage("Saving Room Details");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            RoomUserStatManager manager = new RoomUserStatManager(AddRoomActivity.this);
            return manager.storeRoomDetails(params[0], roomDetails, roomStats);
        }

        @Override
        protected void onPostExecute(Boolean saveSuccessful) {
            dialog.dismiss();
            if (saveSuccessful) {
                try {
                    /**
                     * load into preferences
                     **/
                    RoomiesHelper.cacheDBtoPreferences(AddRoomActivity.this, null, null,
                            roomDetails, roomStats, false);
                    /**
                     * load into activity extras
                     **/
                    Map<ActivityUtils.Extras, List<? extends Parcelable>> extrasMap = new HashMap<>();
                    List<RoomExpenses> roomExpensesList = new ArrayList<>();
                    List<RoomStats> roomStatsList = new ArrayList<>();
                    roomStatsList.add(roomStats);
                    extrasMap.put(ActivityUtils.Extras.ROOM_EXPENSES, roomExpensesList);
                    extrasMap.put(ActivityUtils.Extras.ROOM_STATS, roomStatsList);

                    /**
                     * Start the home screen activity
                     **/
                    ActivityUtils.startActivityHelper(AddRoomActivity.this,
                            getString(R.string.HomeScreen_Activity), extrasMap, true, true);
                } catch (RoomXpnseMngrException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
