package com.lynx.lib.location.entity;

public class LTAddress {
	private String city_name;
	private String geo_region;
	private String street;
	
	public LTAddress() {}
	
	public LTAddress(String city_name, String geo_region, String street) {
		this.city_name = city_name;
		this.geo_region = geo_region;
		this.street = street;
	}
	
	public void setCityName(String city_name) {
		this.city_name = city_name;
	}
	
	public String getCityName() {
		return this.city_name;
	}
	
	public void setGeoRegion(String geo_region) {
		this.geo_region = geo_region;
	}
	
	public String getGeoRegion() {
		return this.geo_region;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getStreet() {
		return this.street;
	}

	@Override
	public String toString() {
		return String.format("{city_name:%s,geo_region:%s,street:%s}", this.city_name, this.geo_region, this.street);
	}
}
