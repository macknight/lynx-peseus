package com.cop.carmonitor.entity;

/**
 * 
 * @author chris.liu
 * 
 */
public class OilBill {
	private double oil;
	private double cost;
	private long addtime;
	
	public OilBill(double oil, double cost, long addtime) {
		this.oil = oil;
		this.cost = cost;
		this.addtime = addtime;
	}

	public double getOil() {
		return oil;
	}

	public void setOil(double oil) {
		this.oil = oil;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public long getAddtime() {
		return addtime;
	}

	public void setAddtime(long addtime) {
		this.addtime = addtime;
	}

}
