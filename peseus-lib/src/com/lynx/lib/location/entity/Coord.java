package com.lynx.lib.location.entity;

/**
 * 
 * @author chris.liu
 * 
 */
public class Coord {
	private CoordSource source;
	private double lat;
	private double lng;
	private int acc;
	private long elapse;

	public Coord() {
	}

	public Coord(CoordSource source, double lat, double lng, int acc,
			long elapse) {
		this.source = source;
		this.lat = lat;
		this.lng = lng;
		this.acc = acc;
		this.elapse = elapse;
	}

	public void setSource(CoordSource source) {
		this.source = source;
	}

	public CoordSource getSource() {
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
		return String
				.format("{\"source\":%d,\"lat\":%f,\"lng\":%f,\"acc\":%d,\"elapse\":%d}",
						this.source.ordinal(), this.lat, this.lng, this.acc,
						this.elapse);
	}

	public enum CoordSource {
		UNKNOWN, GPS, NETWORK, GCELL, GWIFI, BCELL, BWIFI, ACELL, AWIFI;
	}
}
