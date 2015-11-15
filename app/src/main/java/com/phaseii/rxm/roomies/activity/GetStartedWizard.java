package com.phaseii.rxm.roomies.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.fragments.RoomiesFragment;
import com.phaseii.rxm.roomies.manager.RoomUserStatManager;
import com.phaseii.rxm.roomies.model.RoomDetails;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.model.RoomStats;
import com.phaseii.rxm.roomies.model.UserDetails;
import com.phaseii.rxm.roomies.util.ActivityUtils;
import com.phaseii.rxm.roomies.util.DateUtils;
import com.phaseii.rxm.roomies.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.APP_ERROR;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_ELECTRICITY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_MAID;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_MISC;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_NAME;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_NO_OF_MEMBERS;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_RENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.TAG_PERSONAL_INFO;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.TAG_ROOM_EXPENSE;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.TAG_ROOM_INFO;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.USER_EMAIL;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.USER_NAME;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.USER_PHONE;
import static com.phaseii.rxm.roomies.util.RoomiesHelper.cacheDBtoPreferences;
import static com.phaseii.rxm.roomies.util.RoomiesHelper.setError;


public class GetStartedWizard extends FragmentActivity {

    private FragmentTransaction transaction;
    private Toast mToast;
    private SharedPreferences mSharedPref;
    private UserDetails userDetails;
    private RoomDetails roomDetails;
    private RoomStats roomStats;
    private RoomUserStatManager manager;

    /**
     * on create
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_wizard);
        mSharedPref = getSharedPreferences(PREF_ROOMIES_KEY, MODE_PRIVATE);

        if (savedInstanceState == null) {
            ActivityUtils.replaceFragmentWithTag(TAG_PERSONAL_INFO, this, new
                    PersonalInfoFragment(), R.id.container);
        }
    }


    public void personalInfoCompleted(View view) {
        View fragmentView = ((PersonalInfoFragment) getSupportFragmentManager().findFragmentByTag
                (TAG_PERSONAL_INFO)).getFragmentView();
        EditText name = (EditText) findViewById(R.id.user_name);
        boolean isValidName = setError(USER_NAME, this, fragmentView);
        EditText phoneNumber = (EditText) findViewById(R.id.user_phone);
        boolean isValidPhone = setError(USER_PHONE, this, fragmentView);
        EditText email = (EditText) findViewById(R.id.user_email);
        boolean isValidEmail = setError(USER_EMAIL, this, fragmentView);
        if (isValidName && isValidPhone && isValidEmail) {
            userDetails = new UserDetails();
            userDetails.setUserAlias(name.getText().toString());
            userDetails.setUsername(email.getText().toString());
            RoomiesFragment selectorFragment = new SelectorFragment();
            ActivityUtils.replaceFragmentWithTag(TAG_ROOM_INFO, this, selectorFragment, R.id
                    .container);
        }
    }

    /**
     * Invoked when user clicks join room.
     *
     * @param view
     */
    public void joinRoomSelected(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
        builder.setMessage(R.string.join_message)
                .setTitle(R.string.join_title).setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    /**
     * Invoked when user clicks create room. The
     * {@link com.phaseii.rxm.roomies.activity.GetStartedWizard.SelectorFragment} is replaced by
     * {@link com.phaseii.rxm.roomies.activity.GetStartedWizard.RoomInfoFragment} fragment.
     *
     * @param view
     * @param view
     */
    public void createRoomSelected(View view) {
        RoomiesFragment roomInfoFragment = new RoomInfoFragment();
        ActivityUtils.replaceFragmentWithTag(TAG_ROOM_INFO, this, roomInfoFragment, R.id.container);
    }

    /**
     * Invoked when user clicks submit on RoomInfoFragment. The
     * {@link com.phaseii.rxm.roomies.activity.GetStartedWizard.RoomInfoFragment} is replaced by
     * {@link com.phaseii.rxm.roomies.activity.GetStartedWizard.RoomExpenseFragment} fragment.
     *
     * @param view
     */
    public void roomInfoCompleted(View view) {
        View fragmentView = ((RoomInfoFragment) getSupportFragmentManager().findFragmentByTag
                (TAG_ROOM_INFO)).getFragmentView();
        EditText roomName = (EditText) findViewById(R.id.room_name);
        boolean isValidName = setError(ROOM_NAME, this, fragmentView);
        EditText noOfMembers = (EditText) findViewById(R.id.no_of_members);
        boolean isValidNumber = setError(ROOM_NO_OF_MEMBERS, this, fragmentView);
        if (isValidName && isValidNumber) {
            roomDetails = new RoomDetails();
            roomDetails.setRoomAlias(roomName.getText().toString());
            roomDetails.setNoOfPersons(Integer.valueOf(noOfMembers.getText().toString()));
            RoomiesFragment roomExpenseFragment = new RoomExpenseFragment();
            ActivityUtils.replaceFragmentWithTag(TAG_ROOM_EXPENSE, this, roomExpenseFragment, R
                    .id.container);
        }
    }

    /**
     * Invoked when user clicks submit on RoomExpenseFragment. Checks for null and format of
     * the entered data and calls method to store into database and also cached into the shared
     * preferences.
     *
     * @param view
     */

    public void roomExpenseCompleted(View view) {
        View fragmentView = ((RoomExpenseFragment) getSupportFragmentManager().findFragmentByTag
                (TAG_ROOM_EXPENSE)).getFragmentView();

        EditText rent = (EditText) findViewById(R.id.room_rent);
        boolean isValidRent = setError(ROOM_RENT, this, fragmentView);
        EditText maid = (EditText) findViewById(R.id.room_maid);
        boolean isValidMaid = setError(ROOM_MAID, this, fragmentView);
        EditText electricity = (EditText) findViewById(R.id.room_electricity);
        boolean isValidElec = setError(ROOM_ELECTRICITY, this, fragmentView);
        EditText miscellaneous = (EditText) findViewById(R.id.room_misc);
        boolean isValidMisc = setError(ROOM_MISC, this, fragmentView);
        if (isValidRent && isValidMaid && isValidElec && isValidMisc) {
            roomStats = new RoomStats();
            roomStats.setRentMargin(Long.valueOf(rent.getText().toString()));
            roomStats.setElectricityMargin(Long.valueOf(electricity.getText().toString()));
            roomStats.setMaidMargin(Long.valueOf(maid.getText().toString()));
            roomStats.setMiscellaneousMargin(Long.valueOf(miscellaneous.getText().toString()));
            roomStats.setMonthYear(DateUtils.getCurrentMonthYear());
            roomStats.setRentSpent(0);
            roomStats.setMaidSpent(0);
            roomStats.setElectricitySpent(0);
            roomStats.setMiscellaneousSpent(0);
            if (storeRoomInfo()) {
                cacheDBtoPreferences(this, null, userDetails, roomDetails, roomStats, false);
            }
            try {
                List<RoomExpenses> roomExpensesList = new ArrayList<>();
                List<RoomStats> roomStatsList = new ArrayList<>();
                roomStatsList.add(roomStats);
                Map<ActivityUtils.Extras, List<? extends Parcelable>> extrasMap = new HashMap<>();
                extrasMap.put(ActivityUtils.Extras.ROOM_EXPENSES, roomExpensesList);
                extrasMap.put(ActivityUtils.Extras.ROOM_STATS, roomStatsList);
                ActivityUtils.startActivityHelper(this, getResources().getString(R.string
                        .HomeScreen_Activity), extrasMap, true);
            } catch (RoomXpnseMngrException e) {
                ToastUtils.createToast(this, APP_ERROR, mToast);
            }
        }
    }

    /**
     * Stores the data into database and also caches the same in shared preferences
     */
    private boolean storeRoomInfo() {

        manager = new RoomUserStatManager(GetStartedWizard.this);
        return manager.storeRoomDetails(userDetails, roomDetails, roomStats);
    }

    /**
     * ********************************************
     * A selector fragment containing a simple view.
     * ********************************************
     */

    public static class PersonalInfoFragment extends RoomiesFragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
                savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_personal_info, container, false);
            return rootView;
        }

        @Override
        public View getFragmentView() {
            return rootView;
        }
    }

    /**
     * ***************************************
     * A selector fragment containing a simple view.
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
     * A room information fragment containing a simple view.
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
     * A room expense fragment containing a simple view.
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
