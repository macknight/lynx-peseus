package com.lynx.lib.location.entity;

import com.lynx.lib.location.entity.LTLocEnum.LTCoordSource;

/**
 * 
 * @author chris.liu
 *
 */
public class LTCoord {	
	private LTCoordSource source;
	private double lat;
	private double lng;
	private int acc;
	private long elapse;
	
	public LTCoord() {}
	
	public LTCoord(LTCoordSource source, double lat, double lng, int acc, long elapse) {
		this.source = source;
		this.lat = lat;
		this.lng = lng;
		this.acc = acc;
		this.elapse = elapse;
	}
	
	public void setSource(LTCoordSource source) {
		this.source = source;
	}
	
	public LTCoordSource getSource() {
		return source;
	}
	
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public void setAcc(int acc) {
		this.acc = acc;
	}
	
	public int getAcc() {
		return acc;
	}
	
	public void setElapse(long elapse) {
		this.elapse = elapse;
	}
	
	public long getElapse() {
		return elapse;
	}

	@Override
	public String toString() {
		return String.format("{source:%s,lat:%f,lng:%f,acc:%d,elapse:%d}", 
				this.source.toString(), this.lat, this.lng, this.acc, this.elapse);
	}
}
