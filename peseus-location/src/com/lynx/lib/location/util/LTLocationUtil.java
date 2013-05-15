package com.lynx.lib.location.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lynx.lib.location.entity.LTCoord;

public class LTLocationUtil {
	
	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	public static final double EARTH_RADIUS = 6371000;
	
	public static double latToRadians(double lat) {
		return lat * Math.PI / 180;
	}
	
	public static double distanceTo(LTCoord coord1, LTCoord coord2) {
		return distanceTo(coord1.getLat(), coord1.getLng(), coord2.getLat(), coord2.getLng());
	}
	
	public static double distanceTo(double a, double b, double c, double d) {
		double lat1 = a / 180 * Math.PI;
		double lon1 = b / 180 * Math.PI;
		double lat2 = c / 180 * Math.PI;
		double lon2 = d / 180 * Math.PI;
		double dlat = lat2 - lat1;
		double dlon = lon2 - lon1;
		double tmpA = Math.sin(dlat / 2) * Math.sin(dlat / 2) + Math.cos(lat1)
				* Math.cos(lat2) * Math.sin(dlon / 2) * Math.sin(dlon / 2);
		double tempC = 2.0 * Math.atan2(Math.sqrt(tmpA), Math.sqrt(1.0 - tmpA));
		return (int)(Math.ceil(EARTH_RADIUS * tempC));
	}
	
	public static String intToIpAddress(int param) {
		return (param & 0xFF) + "." + (param >> 8 & 0xFF) + "." + (param >> 16 & 0xFF) + "." + (param >> 24);
	}
	
	public static String getCurrentTime() {
		return sdf.format(new Date());
	}
	
	public static double format(double dout, int n) {
		double p = Math.pow(10, n);
		return Math.round(dout * p) / p;
	}
}
