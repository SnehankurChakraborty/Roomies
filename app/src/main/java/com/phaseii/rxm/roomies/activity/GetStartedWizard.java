package com.phaseii.rxm.roomies.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.phaseii.rxm.roomies.model.RoomStats;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.APP_ERROR;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_ELECTRICITY;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_MAID;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_MISC;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_NAME;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_NO_OF_MEMBERS;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.ROOM_RENT;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.TAG_ROOM_EXPENSE;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.TAG_ROOM_INFO;
import static com.phaseii.rxm.roomies.util.RoomiesHelper.cacheDBtoPreferences;
import static com.phaseii.rxm.roomies.util.RoomiesHelper.createToast;
import static com.phaseii.rxm.roomies.util.RoomiesHelper.getCurrentMonthYear;
import static com.phaseii.rxm.roomies.util.RoomiesHelper.replaceFragment;
import static com.phaseii.rxm.roomies.util.RoomiesHelper.setError;
import static com.phaseii.rxm.roomies.util.RoomiesHelper.startActivityHelper;

public class GetStartedWizard extends FragmentActivity {

    private FragmentTransaction transaction;
    private Toast mToast;
    private EditText roomName;
    private EditText noOfMembers;
    private EditText rent;
    private EditText maid;
    private EditText electricity;
    private EditText miscellaneous;
    private SharedPreferences mSharedPref;
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
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaction.add(R.id.container, new SelectorFragment()).commit();
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
        replaceFragment(TAG_ROOM_INFO, this, roomInfoFragment);
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
        roomName = (EditText) findViewById(R.id.room_name);
        boolean isValidName = setError(ROOM_NAME, this, fragmentView);
        noOfMembers = (EditText) findViewById(R.id.no_of_members);
        boolean isValidNumber = setError(ROOM_NO_OF_MEMBERS, this, fragmentView);
        if (isValidName && isValidNumber) {
            RoomiesFragment roomExpenseFragment = new RoomExpenseFragment();
            replaceFragment(TAG_ROOM_EXPENSE, this, roomExpenseFragment);
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

        rent = (EditText) findViewById(R.id.room_rent);
        boolean isValidRent = setError(ROOM_RENT, this, fragmentView);
        maid = (EditText) findViewById(R.id.room_maid);
        boolean isValidMaid = setError(ROOM_MAID, this, fragmentView);
        electricity = (EditText) findViewById(R.id.room_electricity);
        boolean isValidElec = setError(ROOM_ELECTRICITY, this, fragmentView);
        miscellaneous = (EditText) findViewById(R.id.room_misc);
        boolean isValidMisc = setError(ROOM_MISC, this, fragmentView);
        if (isValidRent && isValidMaid && isValidElec && isValidMisc) {
            if (storeRoomInfo()) {
                cacheDBtoPreferences(this, null, null, roomDetails, roomStats, false);
            }
            try {
                startActivityHelper(this, getString(R.string.HomeScreen_Activity), null, true);
            } catch (RoomXpnseMngrException e) {
                createToast(this, APP_ERROR, mToast);
            }
        }
    }

    /**
     * Stores the data into database and also caches the same in shared preferences
     */
    private boolean storeRoomInfo() {

        manager=new RoomUserStatManager(GetStartedWizard.this);

        roomDetails = new RoomDetails();
        roomDetails.setRoomAlias(roomName.getText().toString());
        roomDetails.setNoOfPersons(Integer.valueOf(noOfMembers.getText().toString()));

        roomStats = new RoomStats();
        roomStats.setRentMargin(Long.valueOf(rent.getText().toString()));
        roomStats.setElectricityMargin(Long.valueOf(electricity.getText().toString()));
        roomStats.setMaidMargin(Long.valueOf(maid.getText().toString()));
        roomStats.setMiscellaneousMargin(Long.valueOf(miscellaneous.getText().toString()));
        roomStats.setMonthYear(getCurrentMonthYear());
        roomStats.setRentSpent(0);
        roomStats.setMaidSpent(0);
        roomStats.setElectricitySpent(0);
        roomStats.setMiscellaneousSpent(0);

        return manager.storeRoomDetails(roomDetails, roomStats);
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
