package com.phaseii.rxm.roomies.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.exception.RoomXpnseMngrException;
import com.phaseii.rxm.roomies.logging.RoomiesLogger;
import com.phaseii.rxm.roomies.manager.RoomExpensesManager;
import com.phaseii.rxm.roomies.manager.RoomStatManager;
import com.phaseii.rxm.roomies.model.RoomExpenses;
import com.phaseii.rxm.roomies.model.RoomStats;
import com.phaseii.rxm.roomies.util.ActivityUtils;
import com.phaseii.rxm.roomies.util.LogUtils;
import com.phaseii.rxm.roomies.util.RoomiesConstants;
import com.phaseii.rxm.roomies.util.ToastUtils;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.phaseii.rxm.roomies.util.RoomiesConstants.APP_ERROR;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.IS_LOGGED_IN;
import static com.phaseii.rxm.roomies.util.RoomiesConstants.PREF_ROOMIES_KEY;

/**
 * Splash activity to load all the details from Database
 * Created by Snehankur on 10/29/2015.
 */
public class SplashActivity extends Activity {

    private Toast mToast;
    private String roomId;
    private List<RoomExpenses> roomExpensesList;
    private List<RoomStats> roomStatsList;
    private RoomiesLogger logger = RoomiesLogger.getInstance();

    /**
     * on create
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger.createEntryLoggingMessage("SplashActivity", "onCreate", null);

        /**
         * show splash screen while user waits for the app to load all the details from db
         * */
        setContentView(R.layout.activity_splash);
        /*ObjectMapper mapper = new ObjectMapper();
        SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
        try {
            mapper.acceptJsonFormatVisitor(Room.class, visitor);

            JsonSchema schema = visitor.finalSchema();

            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schema));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
        logger.createExitLoggingMessage("SplashActivity", "onCreate", null);

    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        logger.createEntryLoggingMessage("SplashActivity", "onResume", null);
        SharedPreferences mSharedPref = getSharedPreferences(PREF_ROOMIES_KEY, Context
                .MODE_PRIVATE);

        /**
         * If not logged in, send user to the LoginActivity
         */
        if (!mSharedPref.getBoolean(IS_LOGGED_IN, false)) {
            try {
                ActivityUtils.startActivityHelper(this, getResources().getString(R.string
                                .GetStartedWizard),
                        null, true);
            } catch (RoomXpnseMngrException e) {
                ToastUtils.createToast(this, APP_ERROR, mToast);
                System.exit(0);
            }
        } else {
            roomId = mSharedPref.getString(RoomiesConstants.PREF_ROOM_ID, null);
            roomExpensesList = new ArrayList<>();
            roomStatsList = new ArrayList<>();
            new RetrieveRoomExpensesTask().execute(roomId);
        }
        logger.createExitLoggingMessage("SplashActivity", "onResume", null);
    }

    /**
     * Retrieve room expenses list corresponding to roomID of the current selected room
     *
     * @param roomExpenses
     */
    private void onRoomExpensesRetrieved(List<RoomExpenses> roomExpenses) {
        logger.info("Room expenses retrieved for roomId : " + roomId + LogUtils.printObject
                (roomExpenses));
        new RetrieveRoomStatsTask().execute(roomId);
        roomExpensesList.addAll(roomExpenses);
    }

    /**
     * Retrieve list of room stats corresponding to last 3 months
     *
     * @param roomStats
     */
    private void onRoomStatsRetrieved(List<RoomStats> roomStats) {
        logger.info("Room stats retrieved : " + LogUtils.printObject(roomStats));
        roomStatsList.addAll(roomStats);
        try {
            Thread.sleep(2000);
            Map<ActivityUtils.Extras, List<? extends Parcelable>> extrasMap = new HashMap<>();
            extrasMap.put(ActivityUtils.Extras.ROOM_EXPENSES, roomExpensesList);
            extrasMap.put(ActivityUtils.Extras.ROOM_STATS, roomStatsList);
            ActivityUtils.startActivityHelper(this, getResources().getString(R.string
                    .HomeScreen_Activity), extrasMap, true);
        } catch (RoomXpnseMngrException e) {
            ToastUtils.createToast(this, APP_ERROR, mToast);
            System.exit(0);
        } catch (InterruptedException e) {
            ToastUtils.createToast(this, APP_ERROR, mToast);
            System.exit(0);
        }

    }

    /**
     *
     */
    private class RetrieveRoomExpensesTask extends AsyncTask<String, Void, List<RoomExpenses>> {

        @Override
        protected List<RoomExpenses> doInBackground(String... params) {
            String roomId = params[0];
            logger.info("Room expenses being retrieved for room id : " + roomId);
            List<RoomExpenses> roomExpensesList = null;
            RoomExpensesManager manager = new RoomExpensesManager(SplashActivity.this);
            roomExpensesList = manager.getRoomExpenses(roomId);
            return roomExpensesList;
        }

        @Override
        protected void onPostExecute(List<RoomExpenses> expenses) {
            onRoomExpensesRetrieved(expenses);
        }
    }

    /**
     *
     */
    private class RetrieveRoomStatsTask extends AsyncTask<String, Void, List<RoomStats>> {

        @Override
        protected List<RoomStats> doInBackground(String... params) {
            String roomId = params[0];
            logger.info("Room stats being retrieved for room id : " + roomId);
            RoomStatManager manager = new RoomStatManager(SplashActivity.this);
            List<String> months = new ArrayList<>();
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

            months.add(new DateFormatSymbols().getMonths()[currentMonth] + String.valueOf(
                    currentYear));
            if ((currentMonth - 1) >= 0) {
                months.add(new DateFormatSymbols().getMonths()[currentMonth - 1] + String.valueOf(
                        currentYear));
            } else {
                months.add(new DateFormatSymbols().getMonths()[11] + String.valueOf(currentYear -
                        1));
            }
            if ((currentMonth - 2) >= 0) {
                months.add(new DateFormatSymbols().getMonths()[currentMonth - 2] + String.valueOf(
                        currentYear));
            } else {
                months.add(new DateFormatSymbols().getMonths()[11] + String.valueOf(
                        currentYear - 1));
            }
            logger.info("Room stats being retrieved for months : " + months.get(0) + "," + months
                    .get(1) + " & " + months.get(2));
            List<RoomStats> roomStatsList = manager.getRoomStatsTrend(months, roomId);
            return roomStatsList;
        }

        @Override
        protected void onPostExecute(List<RoomStats> roomStatsList) {
            onRoomStatsRetrieved(roomStatsList);
        }
    }
}
