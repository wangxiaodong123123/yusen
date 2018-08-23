package com.ysdata.grouter.element;


public class MgrAnchorStasticParameter {
	private String anchor_type = "";
	private String anchor_model = "";
	private int design_sums = 0;
	private double design_length = 0;
	private String remark = "";
	
	public MgrAnchorStasticParameter() {

	}
	
	public MgrAnchorStasticParameter(String anchor_type, String anchor_model, int design_sums, 
			double design_length, String remark) {
		this.anchor_type = anchor_type;
		this.anchor_model = anchor_model;
		this.design_sums = design_sums;
		this.design_length = design_length;
		this.remark = remark;
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
	
	public void setDesignLength(double design_length) {
		this.design_length = design_length;
	}
	
	public double getDesignLength() {
		return design_length;
	}
	
	public void setDesignSum(int design_sums) {
		this.design_sums = design_sums;
	}
	
	public int getDesignSum() {
		return design_sums;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getRemark() {
		return remark;
	}
}

