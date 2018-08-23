package com.ysdata.grouter.element;


public class WrcardParameter {
	private int anchor_id = 0;
	private int mileage_id = 0;
	private String anchor_name = "";
	private double cap_unit_hour = 0;
	private String anchor_type = "";
	private String anchor_model = "";
	private double anchor_length = 0;
	private double thereo_cap = 0;
	private String device_type = "";
	private double design_press = 0;
	private double practice_press = 0;
	private int design_hold_time = 0;
	private int practice_hold_time = 0;
	private String date = "";
	private String start_date = "";
	private String end_date = "";
	private double practice_cap = 0;
	private String remark = "";
	
	public WrcardParameter() {

	}
	
	public WrcardParameter(int anchor_id, String anchor_name, int mileage_id, double cap_unit_hour,
			String anchor_type, String anchor_model, double anchor_length, double thereo_cap, String device_type,
			double design_press, double practice_press, int design_hold_time, int practice_hold_time, String date, 
			String start_date, String end_date, double practice_cap, String remark) {
		this.anchor_id = anchor_id;
		this.anchor_name = anchor_name;
		this.mileage_id = mileage_id;
		this.cap_unit_hour = cap_unit_hour;
		this.anchor_type = anchor_type;
		this.anchor_model = anchor_model;
		this.anchor_length = anchor_length;
		this.thereo_cap = thereo_cap;
		this.device_type = device_type;
		this.design_press = design_press;
		this.practice_press = practice_press;
		this.design_hold_time = design_hold_time;
		this.practice_hold_time = practice_hold_time;
		this.date = date;
		this.start_date = start_date;
		this.end_date = end_date;
		this.practice_cap = practice_cap;
		this.remark = remark;
	}
	
	public void setAnchorId(int anchor_id) {
		this.anchor_id = anchor_id;
	}
	
	public int getAnchorId() {
		return anchor_id;
	}
	
	public void setMileageId(int mileage_id) {
		this.mileage_id = mileage_id;
	}
	
	public int getMileageId() {
		return mileage_id;
	}
	
	public void setAnchorName(String anchor_name) {
		this.anchor_name = anchor_name;
	}
	
	public String getAnchorName() {
		return anchor_name;
	}
	
	public void setAnchorType(String anchor_type) {
		this.anchor_type = anchor_type;
	}
	
	public String getAnchorType() {
		return anchor_type;
	}
	
	public void setAnchorModel(String anchor_model) {
		this.anchor_model = anchor_model;
	}
	
	public String getAnchorModel() {
		return anchor_model;
	}
	
	public void setAnchorLength(double anchor_length) {
		this.anchor_length = anchor_length;
	}
	
	public double getAnchorLength() {
		return anchor_length;
	}
	
	public void setCapUnitHour(double cap_unit_hour) {
		this.cap_unit_hour = cap_unit_hour;
	}
	
	public double getCapUnitHour() {
		return cap_unit_hour;
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
	
	public void setDeviceType(String device_type) {
		this.device_type = device_type;
	}
	
	public String getDeviceType() {
		return device_type;
	}
	
	
	public void setDesignPress(double design_press) {
		this.design_press = design_press;
	}
	
	public double getDesignPress() {
		return design_press;
	}
	
	
	public void setPracticePress(double practice_press) {
		this.practice_press = practice_press;
	}
	
	public double getPracticePress() {
		return practice_press;
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
}

