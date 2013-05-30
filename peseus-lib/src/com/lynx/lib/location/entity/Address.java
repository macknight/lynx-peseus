package com.lynx.lib.location.entity;

/**
 * 
 * @author chris.liu
 * 
 */
public class Address {
	private String city;
	private String region;
	private String street;

	public Address() {

	}

	public Address(String city, String region, String street) {
		this.city = city;
		this.region = region;
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@Override
	public String toString() {
		return String.format(
				"{\"city\":\"%s\",\"region\":\"%s\",\"street\":\"%s\"}",
				this.city, this.region, this.street);
	}
}
