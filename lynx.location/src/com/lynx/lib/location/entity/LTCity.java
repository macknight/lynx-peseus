package com.lynx.lib.location.entity;

public class LTCity {
	private int id;
	private String name;
	private String abbr; // abbreviation
	
	public LTCity() {}
	
	public LTCity(int id, String name, String abbr) {
		this.id = id;
		this.name = name;
		this.abbr = abbr;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setAddr(String abbr) {
		this.abbr = abbr;
	}
	
	public String getAbbr() {
		return this.abbr;
	}

	@Override
	public String toString() {
		return String.format("{id:%d,name:%s,abbr:%s}", this.id, this.name, this.abbr);
	}
}
