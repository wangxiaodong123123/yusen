package com.ysdata.steelarch.element;

public class MgrStasticParameter {
	private String date;
	private String mileage;
	private String mileage_distance;
	
	public MgrStasticParameter(String date, String mileage, String mileage_distance) {
		// TODO Auto-generated constructor stub
		setMileage_distance(date);
		setMileage_distance(mileage_distance);
		setDate(date);
	}
	
	/**
	 * @return the mileage_distance
	 */
	public String getMileage_distance() {
		return mileage_distance;
	}
	/**
	 * @param mileage_distance the mileage_distance to set
	 */
	public void setMileage_distance(String mileage_distance) {
		this.mileage_distance = mileage_distance;
	}
	/**
	 * @return the mileage
	 */
	public String getMileage() {
		return mileage;
	}
	/**
	 * @param mileage the mileage to set
	 */
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
}

