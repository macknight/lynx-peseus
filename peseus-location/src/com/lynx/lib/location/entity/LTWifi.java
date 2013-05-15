package com.lynx.lib.location.entity;

public class LTWifi {
	private String ssid;
	private String mac;
	private int dBm;
	
	public LTWifi(String ssid, String mac, int dBm) {
		this.ssid = ssid;
		this.mac = mac;
		this.dBm = dBm;
	}
	
	public void setSsid(String ssid) {
		this.ssid = ssid;
	}
	
	public String getSsid() { 
		return this.ssid; 
	}
	
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public String getMac() { 
		return this.mac; 
	}
	
	public void setDBm(int dBm) {
		this.dBm = dBm;
	}
	
	public int getDBm() { 
		return this.dBm; 
	}
	
	@Override
	public String toString() {
		return String.format("{ssid:%s,mac:%s,dBm:%d}", 
				this.ssid, this.mac, this.dBm);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LTWifi) {
			LTWifi wifi = (LTWifi)obj;
			if (this.mac.equals(wifi.mac)) {
				return true;
			}
		}
		return false;
	}
}
