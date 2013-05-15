package com.lynx.lib.location.core;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.lynx.lib.location.entity.LTWifi;
import com.lynx.lib.network.LTNetworkInfo;
import com.lynx.lib.network.LTNetworkInfo.LTNetworkStatus;

/**
 * 
 * @author chris.liu
 *
 */
public class LTWifiManager {	
	private LTLocationCenter locCenter;
	private WifiManager wifiManager;
	private LTNetworkInfo networkInfo = null;
	private DhcpInfo dhcpInfo = null;
	private final List<LTWifi> wifis = new ArrayList<LTWifi>();
	
	public LTWifiManager(LTLocationCenter locCenter) {
		this.locCenter = locCenter;
		
		wifiManager = (WifiManager)locCenter.getContext().getSystemService(Context.WIFI_SERVICE);
		networkInfo = new LTNetworkInfo(locCenter.getContext());
	}
	
	public List<LTWifi> getWifis() {
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
		WifiInfo conn_wifi  = wifiManager.getConnectionInfo();
		LTWifi cur_wifi = null;
		if (networkInfo.getNetworkState() == LTNetworkStatus.WIFI_CONNECTED && conn_wifi != null) {
			cur_wifi = new LTWifi(conn_wifi.getSSID(), conn_wifi.getBSSID(), conn_wifi.getRssi());
			wifis.add(cur_wifi);
		}
		
		List<ScanResult> scan_results = wifiManager.getScanResults();
		
		if (scan_results != null) {			
			for (ScanResult res : scan_results) {
				LTWifi the_wifi = new LTWifi(res.SSID, res.BSSID, res.level);
				if (cur_wifi != null && the_wifi.equals(cur_wifi))
					continue;
				wifis.add(the_wifi);
			}
		}
		locCenter.sendEmptyMessage(LTLocationCenter.WIFI_SCAN_FIN);
		wifiManager.setWifiEnabled(wifiEnable); 
	}
	
	public void stop() {
		// do nothing now.
	}
}
