package com.ysdata.grouter.element;

public class AnchorParameter {
	private int orderno = 0;
	private String anchor_name = "";
	private String anchor_type = "";
	private String anchor_model = "";
	private double anchor_len = 0;
	private double design_pressure = 0;
	private int hold_time = 0;
	private double design_cap = 0;
	private double cap_unit_meter = 0;
	private double cap_unit_hour = 0;
	private double full_hole_pressure = 0;
	private double full_hole_capacity = 0;
	private String remark = "";
	private String transfer_sign = "";
	private int grout_priority = 0;
	
	public AnchorParameter(String anchor_name, String anchor_type, String anchor_model, double anchor_len, double anchor_pressure, 
			int hold_time, double design_cap, double cap_unit_meter, double cap_unit_hour, double full_hole_pressure, 
			double full_hole_capacity,	String remark, String tansfer_sign, int grout_priority) {
		this.anchor_name = anchor_name;
		this.anchor_type = anchor_type;
		this.anchor_model = anchor_model;
		this.anchor_len = anchor_len;
		this.design_pressure = anchor_pressure;
		this.hold_time = hold_time;
		this.design_cap = design_cap;
		this.cap_unit_meter = cap_unit_meter;
		this.cap_unit_hour = cap_unit_hour;
		this.full_hole_pressure = full_hole_pressure;
		this.full_hole_capacity = full_hole_capacity;
		this.remark = remark;
		this.transfer_sign = tansfer_sign;
		this.grout_priority = grout_priority;
	}
	
	public AnchorParameter(){
		
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
	
	public void setAnchorLen(double anchor_len) {
		this.anchor_len = anchor_len;
	}
	public double getAnchorLen() {
    	return anchor_len;
    }
	
	public void setAnchorPressure(double anchor_pressure) {
		this.design_pressure = anchor_pressure;
	}
	public double getAnchorDesignPressure() {
    	return design_pressure;
    }
	
	public void setHoldTime(int time) {
		this.hold_time = time;
	}
	public int getHoldTime() {
    	return hold_time;
    }
	
	public void setOrderNo(int orderno) {
		this.orderno = orderno;
	}
	public int getOrderNo() {
    	return orderno;
    }
	
	public void setDesignCap(double design_cap) {
		this.design_cap = design_cap;
	}
	public double getDesignCap() {
    	return design_cap;
    }
	
	public void setUnitMeterCap(double cap_unit_meter) {
		this.cap_unit_meter = cap_unit_meter;
	}
	public double getUnitMeterCap() {
    	return cap_unit_meter;
    }
	
	public void setUnitHourCap(double cap_unit_hour) {
		this.cap_unit_hour = cap_unit_hour;
	}
	
	public double getUnitHourCap() {
    	return cap_unit_hour;
    }
	
	public void setFullHoleCapacity(double full_hole_capacity) {
		this.full_hole_capacity = full_hole_capacity;
	}
	
	public double getFullHoleCapacity() {
    	return full_hole_capacity;
    }
	
	public void setFullHolePressure(double full_hole_pressure) {
		this.full_hole_pressure = full_hole_pressure;
	}
	
	public double getFullHolePressure() {
    	return full_hole_pressure;
    }
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
    	return remark;
    }
	
	public void setTransferSign(String transfer_sign) {
		this.transfer_sign = transfer_sign;
	}
	public String getTransferSign() {
    	return transfer_sign;
    }
	
	public void setGroutingPriority(int grout_priority) {
		this.grout_priority = grout_priority;
	}
	
	public int getGroutingPriority() {
		return grout_priority;
	}
}

