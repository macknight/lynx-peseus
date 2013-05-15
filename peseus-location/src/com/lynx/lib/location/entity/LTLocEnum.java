package com.lynx.lib.location.entity;

/**
 * 
 * @author chris.liu
 *
 */
public class LTLocEnum {
	public enum LTMapType {
		GPS("gps"),
		Google("google"),
		Baidu("baidu"),
		Mapbar("mapbar");
		
		private final String name;
		
		LTMapType(String name) {
			this.name = name;
		}
		
		public String getName() { return this.name; }
	}
	
	public enum LTCellType {
		GSM("gsm"),
		CDMA("cdma"),
		UNKNWON("unknown");
		
		private final String name;
		
		LTCellType(String name) {
			this.name = name;
		}
		
		public String getName() { return this.name; }
	}
	
	public enum LTCoordSource {
		GCELL("gCell"),
		GWIFI("gWifi"),
		BCELL("bCell"),
		BWIFI("bWifi"),
		ACELL("aCell"),
		AWIFI("aWifi"),
		NETWORK("network"),
		GPS("gps"),
		UNKNOWN("unknown");
		
		private final String name;
		
		LTCoordSource(String name) {
			this.name = name;
		}
		
		public String getName() { return this.name; }
	}
}
