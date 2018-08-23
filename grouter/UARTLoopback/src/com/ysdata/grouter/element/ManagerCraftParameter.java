package com.ysdata.grouter.element;


public class ManagerCraftParameter {
	private int anchor_id = 0;
	private String anchor_name = "";
	private int mileage_id = 0;
	private String anchor_type = "";
	private String anchor_model = "";
	private double design_len = 0;
	private double design_pressure = 0;
	private double measure_pressure = 0;
	private double thereo_cap = 0;
	private double practice_cap = 0;
	private String date = "";
	private String start_date = "";
	private String end_date = "";
	private int design_hold_time = 0;
	private int practice_hold_time = 0;
	private String remark = "";
	private int wrcard_sign = 0;
	
	public ManagerCraftParameter() {

	}
	public ManagerCraftParameter(int anchor_id, String anchor_name, int mileage_id, String anchor_type, String anchor_model, 
			double design_len, double design_pressure, double measure_pressure, double thereo_cap, double practice_cap, 
			String date, String start_date, String end_date, int design_hold_time, int practice_hold_time,
			String remark, int wrcard_sign) {
		this.anchor_id = anchor_id;
		this.anchor_name = anchor_name;
		this.mileage_id = mileage_id;
		this.anchor_type = anchor_type;
		this.anchor_model = anchor_model;
		this.design_len = design_len;
		this.design_pressure = design_pressure;
		this.measure_pressure = measure_pressure;
		this.thereo_cap = thereo_cap;
		this.practice_cap = practice_cap;
		this.date = date;
		this.start_date = start_date;
		this.end_date = end_date;
		this.design_hold_time = design_hold_time;
		this.practice_hold_time = practice_hold_time;
		this.remark = remark;
		this.wrcard_sign = wrcard_sign;
	}
	
	public void setAnchorId(int anchor_id) {
		this.anchor_id = anchor_id;
	}
	
	public int getAnchorId() {
		return anchor_id;
	}
	
	public void setAnchorName(String name) {
		anchor_name = name;
	}
	
	public String getAnchorName() {
		return anchor_name;
	}
	
	public void setMileageId(int mileage_id) {
		this.mileage_id = mileage_id;
	}
	
	public int getMileageId() {
		return mileage_id;
	}
	
	public void setAnchorType(String type) {
		anchor_type = type;
	}
	
	public String getAnchorType() {
		return anchor_type;
	}
	
	public void setAnchorModel(String model) {
		anchor_type = model;
	}
	
	public String getAnchorModel() {
		return anchor_model;
	}
	
	public void setDesignLen(double design_len) {
		this.design_len = design_len;
	}
	
	public double getDesignLen() {
		return design_len;
	}
	
	public void setMeasurePressure(double measure_pressure) {
		this.measure_pressure = measure_pressure;
	}
	
	public double getMeasurePressure() {
		return measure_pressure;
	}
	
	public void setDesignPressure(double design_pressure) {
		this.design_pressure = design_pressure;
	}
	
	public double getDesignPressure() {
		return design_pressure;
	}
	
	public void setThereoCap(double thereo_cap) {
		this.thereo_cap = thereo_cap;
	}
	
	public double getThereoCap() {
		return thereo_cap;
	}
	
	public void setPracticeCap(double practice_cap) {
		this.practice_cap = practice_cap;
	}
	
	public double getPracticeCap() {
		return practice_cap;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setStartDate(String start_date) {
		this.start_date = start_date;
	}
	
	public String getStartDate() {
		return start_date;
	}
	public void setEndDate(String end_date) {
		this.end_date = end_date;
	}
	
	public String getEndDate() {
		return end_date;
	}
	
	public void setDesignHoldTime(int design_hold_time) {
		this.design_hold_time = design_hold_time;
	}
	
	public int getDesignHoldTime() {
		return design_hold_time;
	}
	
	public void setPracticeHoldTime(int practice_hold_time) {
		this.practice_hold_time = practice_hold_time;
	}
	
	public int getPracticeHoldTime() {
		return practice_hold_time;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setWrcardSign(int wrcard_sign) {
		this.wrcard_sign = wrcard_sign;
	}
	
	public int getWrcardSign() {
		return wrcard_sign;
	}
}

