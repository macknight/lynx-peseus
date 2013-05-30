package com.lynx.lib.location.core;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;

import com.lynx.lib.dataservice.NetworkManager;
import com.lynx.lib.dataservice.NetworkManager.NetworkState;
import com.lynx.lib.location.entity.Wifi;

/**
 * 
 * @author chris.liu
 * 
 */
public class WifiManager {
	private LocationCenter locCenter;
	private android.net.wifi.WifiManager wifiManager;
	private NetworkManager networkManager = null;
	private DhcpInfo dhcpInfo = null;
	private final List<Wifi> wifis = new ArrayList<Wifi>();

	public WifiManager(LocationCenter locCenter) {
		this.locCenter = locCenter;

		wifiManager = (android.net.wifi.WifiManager) locCenter.getContext()
				.getSystemService(Context.WIFI_SERVICE);
		networkManager = new NetworkManager(locCenter.getContext());
	}

	public List<Wifi> getWifis() {
		return wifis;
	}

	public DhcpInfo getDhcpInfo() {
		return this.dhcpInfo;
	}

	public boolean startScan() {
		return wifiManager.startScan();
	}

	public void wifiScan() {
		wifis.clear();
		boolean wifiEnable = wifiManager.isWifiEnabled();
		if (!wifiEnable) {
			wifiManager.setWifiEnabled(true);
		}

		dhcpInfo = wifiManager.getDhcpInfo();
		WifiInfo conn_wifi = wifiManager.getConnectionInfo();
		Wifi cur_wifi = null;
		if (networkManager.state() == NetworkState.NETWORK_WIFI
				&& conn_wifi != null) {
			cur_wifi = new Wifi(conn_wifi.getSSID(), conn_wifi.getBSSID(),
					conn_wifi.getRssi());
			wifis.add(cur_wifi);
		}

		List<ScanResult> scan_results = wifiManager.getScanResults();

		if (scan_results != null) {
			for (ScanResult res : scan_results) {
				Wifi the_wifi = new Wifi(res.SSID, res.BSSID, res.level);
				if (cur_wifi != null && the_wifi.equals(cur_wifi))
					continue;
				wifis.add(the_wifi);
			}
		}
		locCenter.sendEmptyMessage(LocationCenter.WIFI_SCAN_FIN);
		wifiManager.setWifiEnabled(wifiEnable);
	}

	public void stop() {
		// do nothing now.
	}
}
