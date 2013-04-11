package com.lynx.lib.location.entity;

public class LTLocation {
	private LTCoord coord;
	private LTAddress addr;
	
	public LTLocation() {}
	
	public LTLocation(LTCoord coord, LTAddress addr) {
		this.coord = coord;
		this.addr = addr;
	}
	
	public LTCoord getCoord() {
		return coord;
	}

	public void setCoord(LTCoord coord) {
		this.coord = coord;
	}

	public LTAddress getAddr() {
		return addr;
	}

	public void setAddr(LTAddress addr) {
		this.addr = addr;
	}

	@Override
	public String toString() {
		return String.format("{coord:%s,addr:%s}", 
				this.coord.toString(), this.addr.toString());
	}
}
