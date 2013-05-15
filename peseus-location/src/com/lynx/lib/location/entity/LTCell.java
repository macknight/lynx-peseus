package com.lynx.lib.location.entity;

import com.lynx.lib.location.entity.LTLocEnum.LTCellType;


public class LTCell {	
	private LTCellType type;
	private int mcc; // for GSM and CDMA: mobile country code  
	private int mnc; // for GSM: mobile network code; for CDMA: SID
	private int cid; // for GSM: cell id; for CDMA: BID
	private int lac; // for GSM: local area code; for CDMA NID
	private int asu;
	private int lat;
	private int lng;
	
	public LTCell() {}
	
	public LTCell(LTCellType type, int mcc, int mnc, int cid, int lac, int asu, int lat, int lng) {
		this.type = type;
		this.mcc = mcc;
		this.mnc = mnc;
		this.cid = cid;
		this.lac = lac;
		this.asu = asu;
		this.lat = lat;
		this.lng = lng;
	}
	
	public void setType(LTCellType type) {
		this.type = type;
	}
	
	public LTCellType getType() {
		return this.type;
	}
	
	public void setMCC(int mcc) {
		this.mcc = mcc;
	}
	
	public int getMCC() {
		return this.mcc;
	}
	
	public void setMNC(int mnc) {
		this.mnc = mnc;
	}
	
	public int getMNC() {
		return this.mnc;
	}
	
	public void setCID(int cid) {
		this.cid = cid;
	}
	
	public int getCID() {
		return this.cid;
	}
	
	public void setLAC(int lac) {
		this.lac = lac;
	}
	
	public int getLAC() {
		return this.lac;
	}
	
	public void setASU(int asu) {
		this.asu = asu;
	}
	
	public int getASU() {
		return this.asu;
	}

	public void setLat(int lat) {
		this.lat = lat;
	}
	
	public int getLat() {
		return lat;
	}
	
	public void setLng(int lng) {
		this.lng = lng;
	}
	
	public int getLng() {
		return lng;
	}
	
	@Override
	public String toString() {
		return String.format("{type:%s,mcc:%d,mnc:%d,cid:%d,lac:%d,asu:%d,lat:%d,lng:%d}", 
				this.type.getName(), this.mcc, this.mnc, this.cid, this.lac, this.asu, this.lat, this.lng);
	}
}
