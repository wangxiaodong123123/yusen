package com.ysdata.grouter.element;

public class CraftEngArchSetParam {
	private int craft_anchor_seq = 0;
	private String craft_anchor_type = "";
	private String craft_anchor_model = "";
	private float craft_anchor_len = 0;
	private float craft_design_pressure = 0;
	private int craft_hold_time = 0;
	private float craft_design_cap = 0;
	private float craft_cap_unit_meter = 0;
	private float craft_cap_unit_hour = 0;
	private String craft_remark = "";
	private String craft_transfer_sign = "";
	
	public CraftEngArchSetParam(int anchor_seq, String anchor_type, String anchor_model, float anchor_len, float anchor_pressure, 
			int hold_time, float theore_cap, float cap_unit_meter, float cap_unit_hour, String remark, String tansfer_sign) {
		craft_anchor_seq = anchor_seq;
		craft_anchor_type = anchor_type;
		craft_anchor_model = anchor_model;
		craft_anchor_len = anchor_len;
		craft_design_pressure = anchor_pressure;
		craft_hold_time = hold_time;
		craft_design_cap = theore_cap;
		craft_cap_unit_meter = cap_unit_meter;
		craft_cap_unit_hour = cap_unit_hour;
		craft_remark = remark;
		craft_transfer_sign = tansfer_sign;
	}
	
	public CraftEngArchSetParam(){
		
	}
	
	public void setSeq(int seq) {
		craft_anchor_seq = seq;
	}
	public int getSeq() {
    	return craft_anchor_seq;
    }
	
	public void setAnchorType(String anchor_type) {
		craft_anchor_type = anchor_type;
	}
	public String getAnchorType() {
    	return craft_anchor_type;
    }
	
	public void setAnchorModel(String anchor_model) {
		craft_anchor_model = anchor_model;
	}
	public String getAnchorModel() {
    	return craft_anchor_model;
    }
	
	public void setAnchorLen(float anchor_len) {
		craft_anchor_len = anchor_len;
	}
	public float getAnchorLen() {
    	return craft_anchor_len;
    }
	
	public void setAnchorPressure(float anchor_pressure) {
		craft_design_pressure = anchor_pressure;
	}
	public float getAnchorDesignPressure() {
    	return craft_design_pressure;
    }
	
	public void setHoldTime(int time) {
		craft_hold_time = time;
	}
	public int getHoldTime() {
    	return craft_hold_time;
    }
	
	public void setDesignCap(float design_cap) {
		craft_design_cap = design_cap;
	}
	public float getDesignCap() {
    	return craft_design_cap;
    }
	
	public void setUnitMeterCap(float cap_unit_meter) {
		craft_cap_unit_meter = cap_unit_meter;
	}
	public float getUnitMeterCap() {
    	return craft_cap_unit_meter;
    }
	
	public void setUnitHourCap(float cap_unit_hour) {
		craft_cap_unit_hour = cap_unit_hour;
	}
	public float getUnitHourCap() {
    	return craft_cap_unit_hour;
    }
	
	public void setRemark(String remark) {
		craft_remark = remark;
	}
	public String getRemark() {
    	return craft_remark;
    }
	
	public void setTransferSign(String transfer_sign) {
		craft_transfer_sign = transfer_sign;
	}
	public String getTransferSign() {
    	return craft_transfer_sign;
    }
}

