package com.ysdata.blender.element;

public class MixCraftParameter {
	private int id = 0;
	private double mix_ratio = 0;
	private String mix_date;
	private String start_time = "";
	private String end_time = "";
	private double cement_weight = 0;
	private double device_position = 0;
	
	public MixCraftParameter(int id, double mix_ratio, String mix_date,  
			String start_time, String end_time, double cement_weight,
			double device_position) {
		this.id = id;   //page
		this.mix_ratio = mix_ratio;
		this.mix_date = mix_date;
		this.start_time = start_time;
		this.end_time = end_time;
		this.cement_weight = cement_weight;
		this.device_position = device_position;
	}
	
	public int getId() {
		return id;
	}
	
	public double getMixRatio() {
		return mix_ratio;
	}
	
	public String getMixDate() {
		return mix_date;
	}
	
	public String getStartTime() {
		return start_time;
	}
	
	public String getEndTime() {
		return end_time;
	}
	
	public double getCementWeight() {
		return cement_weight;
	}
	
	public double getDevPostion() {
		return device_position;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setMixRatio(double mix_ratio) {
		this.mix_ratio = mix_ratio;
	}
	
	public void setMixDate(String mix_date) {
		this.mix_date = mix_date;
	}
	
	public void setStartTime(String start_time) {
		this.start_time = start_time;
	}
	
	public void setEndTime(String end_time) {
		this.end_time = end_time;
	}
	
	public void setCementWeight(double cement_weight) {
		this.cement_weight = cement_weight;
	}
	
	public void setDevPostion(double device_position) {
		this.device_position = device_position;
	}	
}

