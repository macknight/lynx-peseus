package com.lynx.lib.dataservice;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * 
 * @author chris.liu
 * 
 */
public class NetworkUtil {

	public static NetworkState state(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// wifi
		State state = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (state == State.CONNECTED || state == State.CONNECTING)
			return NetworkState.NETWORK_WIFI;

		// mobi
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NetworkState.NETWORK_MOBI;
		}

		return NetworkState.NETWORK_NONE;
	}

	public static enum NetworkState {
		NETWORK_NONE, NETWORK_WIFI, NETWORK_MOBI;
	}
}
