package com.lynx.lib.location.entity;

/**
 * 
 * @author chris.liu
 * 
 */
public class Location {
	private Coord coord;
	private Address addr;

	public Location() {
	}

	public Location(Coord coord, Address addr) {
		this.coord = coord;
		this.addr = addr;
	}

	public Coord getCoord() {
		return coord;
	}

	public void setCoord(Coord coord) {
		this.coord = coord;
	}

	public Address getAddr() {
		return addr;
	}

	public void setAddr(Address addr) {
		this.addr = addr;
	}

	@Override
	public String toString() {
		return String.format("{coord:%s,addr:%s}", this.coord.toString(),
				this.addr.toString());
	}
}
