package com.ysdata.grouter.element;

public class MileageParam {
	private int seq = 0;
	private String mileage = "";
	private int anchor_amount = 0;
	private String up_down_xing = "";
	
	public MileageParam(int seq, String mileage, int anchor_amount, 
			String up_down_xing) {
		this.seq = seq;
		this.mileage = mileage;
		this.anchor_amount = anchor_amount;
		this.up_down_xing = up_down_xing;
	}
	
	public MileageParam() {

	}
	
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	
	public void setAnchorAmount(int anchor_amount) {
		this.anchor_amount = anchor_amount;
	}
	
	public void setUpDownXing(String up_down_xing) {
		this.up_down_xing = up_down_xing;
	}
	
	public int getSeq() {
    	return seq;
    }
	
	public String getMileage() {
		return mileage;
	}
	
	public int getAnchorAmount() {
		return anchor_amount;
	}
	
	public String getUpDownXing() {
		return up_down_xing;
	}
}

