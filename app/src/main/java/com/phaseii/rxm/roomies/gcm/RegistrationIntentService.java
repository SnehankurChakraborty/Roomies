package com.phaseii.rxm.roomies.gcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.phaseii.rxm.roomies.helper.RoomiesConstants;

import java.io.IOException;

import static com.phaseii.rxm.roomies.helper.RoomiesConstants.ROOM_INFO_FILE_KEY;

/**
 * Created by Snehankur on 6/27/2015.
 */
public class RegistrationIntentService extends IntentService {

	private static final String TAG = "RegIntentService";
	private static final String[] TOPICS = {"global"};

	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 * Used to name the worker thread, important only for debugging.
	 */
	public RegistrationIntentService() {
		super(TAG);
	}

	public void onCreate() {
		super.onCreate();
		Log.d(TAG, ">>>onCreate()");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, startId, startId);
		Log.i(TAG, "Received start id " + startId + ": " + intent);

		return START_STICKY;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		SharedPreferences sharedPreferences = getSharedPreferences(ROOM_INFO_FILE_KEY,
				Context.MODE_PRIVATE);
		try {
			// In the (unlikely) event that multiple refresh operations occur simultaneously,
			// ensure that they are processed sequentially.
			synchronized (TAG) {
				// [START register_for_gcm]
				// Initially this call goes out to the network to retrieve the token, subsequent calls
				// are local.
				// [START get_token]
				String sender_id="803492808547";
				InstanceID instanceID = InstanceID.getInstance(this);
				String token = instanceID.getToken(sender_id,
						GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

				/*String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
						GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);*/
				// [END get_token]
				Log.i(TAG, "GCM Registration Token: " + token);

				// TODO: Implement this method to send any registration to your app's servers.
				sendRegistrationToServer(token);

				// Subscribe to topic channels
				subscribeTopics(token);

				// You should store a boolean that indicates whether the generated token has been
				// sent to your server. If the boolean is false, send the token to your server,
				// otherwise your server should have already received the token.
				sharedPreferences.edit().putBoolean(RoomiesConstants.SENT_TOKEN_TO_SERVER,
						true).apply();
				// [END register_for_gcm]
			}
		} catch (Exception e) {
			Log.d(TAG, "Failed to complete token refresh", e);
			// If an exception happens while fetching the new token or updating our registration data
			// on a third-party server, this ensures that we'll attempt the update at a later time.
			sharedPreferences.edit().putBoolean(RoomiesConstants.SENT_TOKEN_TO_SERVER,
					false).apply();
		}
	}

	/**
	 * Persist registration to third-party servers.
	 * <p/>
	 * Modify this method to associate the user's GCM registration token with any server-side account
	 * maintained by your application.
	 *
	 * @param token The new token.
	 */
	private void sendRegistrationToServer(String token) {
		RoomiesConstants.setToken(token);
	}

	/**
	 * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
	 *
	 * @param token GCM token
	 * @throws IOException if unable to reach the GCM PubSub service
	 */
	// [START subscribe_topics]
	private void subscribeTopics(String token) throws IOException {
		for (String topic : TOPICS) {
			GcmPubSub pubSub = GcmPubSub.getInstance(this);
			pubSub.subscribe(token, "/topics/" + topic, null);
		}
	}
	// [END subscribe_topics]
}
