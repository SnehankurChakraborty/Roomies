package com.phaseii.rxm.roomies.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Snehankur on 6/6/2015.
 */
public class RoomiesConnectDialog extends DialogFragment{

	private IntentFilter intentFilter=new IntentFilter();
	private WifiP2pManager manager;
	private Channel channel;
	private Activity mActivity;

	private static final String TXTRECORD_PROP_AVAILABLE = "available";
	public static final String SERVICE_INSTANCE = "_roomies";
	public static final String SERVICE_REG_TYPE = "_presence._tcp";
	public static final int MESSAGE_READ = 0x400 + 1;
	public static final int MY_HANDLE = 0x400 + 2;
	public static final int SERVER_PORT = 4545;

	private WifiP2pDnsSdServiceRequest serviceRequest;
	private WifiP2pDnsSdServiceInfo service;
	private WiFiDirectBroadcastReceiver receiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity=getActivity();
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		intentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		manager = (WifiP2pManager) mActivity.getSystemService(Context.WIFI_P2P_SERVICE);
		channel = manager.initialize(mActivity, mActivity.getMainLooper(), null);
	}

	@Override
	public void onResume() {
		super.onResume();
		receiver = new WiFiDirectBroadcastReceiver(manager, channel, mActivity);
		mActivity.registerReceiver(receiver, intentFilter);
	}

	@Override
	public void onPause() {
		super.onPause();
		mActivity.unregisterReceiver(receiver);
	}

	private void startRegistrationAndDiscovery() {
		Map<String, String> record = new HashMap<String, String>();
		record.put(TXTRECORD_PROP_AVAILABLE, "visible");

		service = WifiP2pDnsSdServiceInfo.newInstance(
				SERVICE_INSTANCE, SERVICE_REG_TYPE, record);
		manager.addLocalService(channel, service, new WifiP2pManager.ActionListener() {

			@Override
			public void onSuccess() {

			}

			@Override
			public void onFailure(int error) {

			}
		});

		discoverService();

	}

	private void discoverService() {

        /*
         * Register listeners for DNS-SD services. These are callbacks invoked
         * by the system when a service is actually discovered.
         */

		manager.setDnsSdResponseListeners(channel,
				new WifiP2pManager.DnsSdServiceResponseListener() {

					@Override
					public void onDnsSdServiceAvailable(String instanceName,
					                                    String registrationType, WifiP2pDevice srcDevice) {

						// A service has been discovered. Is this our app?

						if (instanceName.equalsIgnoreCase(SERVICE_INSTANCE)) {

							// update the UI and add the item the discovered
							// device.
							/*WiFiDirectServicesList fragment = (WiFiDirectServicesList) getFragmentManager()
									.findFragmentByTag("services");
							if (fragment != null) {
								WiFiDevicesAdapter adapter = ((WiFiDevicesAdapter) fragment
										.getListAdapter());
								WiFiP2pService service = new WiFiP2pService();
								service.device = srcDevice;
								service.instanceName = instanceName;
								service.serviceRegistrationType = registrationType;
								adapter.add(service);
								adapter.notifyDataSetChanged();
								Log.d(TAG, "onBonjourServiceAvailable "
										+ instanceName);
							}*/
						}

					}
				}, new WifiP2pManager.DnsSdTxtRecordListener() {

					/**
					 * A new TXT record is available. Pick up the advertised
					 * buddy name.
					 */
					@Override
					public void onDnsSdTxtRecordAvailable(
							String fullDomainName, Map<String, String> record,
							WifiP2pDevice device) {

					}
				});

		// After attaching listeners, create a service request and initiate
		// discovery.
		serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
		manager.addServiceRequest(channel, serviceRequest,
				new WifiP2pManager.ActionListener() {

					@Override
					public void onSuccess() {

					}

					@Override
					public void onFailure(int arg0) {

					}
				});
		manager.discoverServices(channel, new WifiP2pManager.ActionListener() {

			@Override
			public void onSuccess() {

			}

			@Override
			public void onFailure(int arg0) {


			}
		});
	}

	private class WiFiDirectBroadcastReceiver extends BroadcastReceiver{
		private WifiP2pManager manager;
		private Channel channel;
		private Activity activity;

		/**
		 * @param manager WifiP2pManager system service
		 * @param channel Wifi p2p channel
		 * @param activity activity associated with the receiver
		 */
		public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel,
		                                   Activity activity) {
			super();
			this.manager = manager;
			this.channel = channel;
			this.activity = activity;
		}

		/*
		 * (non-Javadoc)
		 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
		 * android.content.Intent)
		 */
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

				if (manager == null) {
					return;
				}

				NetworkInfo networkInfo = (NetworkInfo) intent
						.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

				if (networkInfo.isConnected()) {

					// we are connected with the other device, request connection
					// info to find group owner IP

					manager.requestConnectionInfo(channel,
							(WifiP2pManager.ConnectionInfoListener) activity);
				} else {
					// It's a disconnect
				}
			} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
					.equals(action)) {
				WifiP2pDevice device = (WifiP2pDevice) intent
						.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);

			}
		}
	}
}
