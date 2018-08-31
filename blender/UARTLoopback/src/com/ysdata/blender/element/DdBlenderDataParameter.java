package com.ysdata.blender.element;

public class DdBlenderDataParameter {
	private int id = 0;
	private String start_time = "";
	private double blender_time = 0;
	private double cement_weigh = 0;
	private double water_weigh = 0;
	private double total_weigh = 0;
	
	public DdBlenderDataParameter(int id, String start_time, double blender_time,  
			double cement_weigh, double water_weigh, double total_weigh) {
		this.id = id;   //page
		this.start_time = start_time;
		this.blender_time = blender_time;
		this.cement_weigh = cement_weigh;
		this.water_weigh = water_weigh;
		this.total_weigh = total_weigh;
	}
	
	public DdBlenderDataParameter() {
		
	}
	
	public int getId() {
		return id;
	}
	
	public String getStartTime() {
		return start_time;
	}
	
	public double getBlenderTime() {
		return blender_time;
	}
	
	public double getCementWeight() {
		return cement_weigh;
	}
	
	public double getWaterWeigh() {
		return water_weigh;
	}
	
	public double getTotalWeigh() {
		return total_weigh;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setStartTime(String start_time) {
		this.start_time = start_time;
	}
	
	public void setBlenderTime(double blender_time) {
		this.blender_time = blender_time;
	}
	
	public void setCementWeigh(double cement_weigh) {
		this.cement_weigh = cement_weigh;
	}
	
	public void setWaterWeigh(double water_weigh) {
		this.water_weigh = water_weigh;
	}	
	
	public void setTotalWeigh(double total_weigh) {
		this.total_weigh = total_weigh;
	}
}

