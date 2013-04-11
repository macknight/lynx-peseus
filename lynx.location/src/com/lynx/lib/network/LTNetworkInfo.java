package com.lynx.lib.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * 
 * @author chris.liu
 *
 */
public class LTNetworkInfo {
	
	private ConnectivityManager conn_manager;
	
	public LTNetworkInfo(Context context) {
		conn_manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	public LTNetworkStatus getNetworkState() {
		// wifi
		State state = conn_manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		
		if (state == State.CONNECTED)
			return LTNetworkStatus.WIFI_CONNECTED;
		else if (state == State.CONNECTING)
			return LTNetworkStatus.WIFI_CONNECTING;
		
		// mobile
		state  = conn_manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (state == State.CONNECTED || state == State.CONNECTING)
			return LTNetworkStatus.MOBI_CONNECTED;
		else if (state == State.CONNECTING) 
			return LTNetworkStatus.MOBI_CONNECTING;
		
		return LTNetworkStatus.NONE;
	}
	
	public enum LTNetworkStatus {
		NONE(0),
		WIFI_CONNECTED(1),
		WIFI_CONNECTING(2),
		MOBI_CONNECTED(3),
		MOBI_CONNECTING(4);
		
		private int status;
		
		LTNetworkStatus(int status) {
			this.status = status;
		}
		
		public int getStatus() {
			return status;
		}
	}
}
