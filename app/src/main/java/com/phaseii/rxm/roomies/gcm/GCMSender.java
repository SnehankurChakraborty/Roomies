package com.phaseii.rxm.roomies.gcm;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Snehankur on 6/27/2015.
 */
public class GCMSender extends AsyncTask<String, Void, Void> {
	public static final String API_KEY = "AIzaSyCQysxKxverYJ4p7MxuN6WI7iilnd-WHA8";

	@Override
	protected Void doInBackground(String... params) {
		if (params.length < 1 || params.length > 2 || params[0] == null) {
			System.err.println("usage: ./gradlew run -Pargs=\"MESSAGE[,DEVICE_TOKEN]\"");
			System.err.println("");
			System.err.println(
					"Specify a test message to broadcast via GCM. If a device's GCM registration token is\n" +
							"specified, the message will only be sent to that device. Otherwise, the message \n" +
							"will be sent to all devices subscribed to the \"global\" topic.");
			System.err.println("");
			System.err.println("Example (Broadcast):\n" +
					"On Windows:   .\\gradlew.bat run -Pargs=\"<Your_Message>\"\n" +
					"On Linux/Mac: ./gradlew run -Pargs=\"<Your_Message>\"");
			System.err.println("");
			System.err.println("Example (Unicast):\n" +
					"On Windows:   .\\gradlew.bat run -Pargs=\"<Your_Message>,<Your_Token>\"\n" +
					"On Linux/Mac: ./gradlew run -Pargs=\"<Your_Message>,<Your_Token>\"");
			System.exit(1);
		}
		try {
			// Prepare JSON containing the GCM message content. What to send and where to send.
			JSONObject jGcmData = new JSONObject();
			JSONObject jData = new JSONObject();
			jData.put("message", params[0].trim());
			// Where to send GCM message.
			if (params.length > 1 && params[1] != null) {
				jGcmData.put("to", params[1].trim());
			} else {
				jGcmData.put("to", "/topics/global");
			}
			// What to send in GCM message.
			jGcmData.put("data", jData);
			System.out.println(jGcmData.toString());
			// Create connection to send GCM Message request.
			URL url = new URL("https://android.googleapis.com/gcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Authorization", "key=" + API_KEY);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);

			// Send GCM message content.
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(jGcmData.toString().getBytes());

			// Read GCM response.
			InputStream inputStream = conn.getInputStream();
			String resp = IOUtils.toString(inputStream);
			System.out.println(resp);
			System.out.println("Check your device/emulator for notification or logcat for " +
					"confirmation of the receipt of the GCM message.");
		} catch (IOException e) {
			System.out.println("Unable to send GCM message.");
			System.out.println("Please ensure that API_KEY has been replaced by the server " +
					"API key, and that the device's registration token is correct (if specified).");
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
