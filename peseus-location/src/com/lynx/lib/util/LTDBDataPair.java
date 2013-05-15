package com.lynx.lib.util;

public class LTDBDataPair {
	private String name;
	private String val;
	private long addTime;
	
	public LTDBDataPair() {}
	
	public LTDBDataPair(String name, String val, long addTime) {
		this.name = name;
		this.val = val;
		this.addTime = addTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}
}
