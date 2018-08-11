package com.ysdata.steelarch.element;

public class MixUploadState {
	private String mixDate = "";
	private int state = 0;
	private int id = 0;
	
	public MixUploadState() {

	}
	
	public MixUploadState(int id, String mixDate, int state) {
		this.mixDate = mixDate;
		this.state = state;
		this.id = id;
	}
	
	public void setMixDate(String mixDate) {
		this.mixDate = mixDate;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public String getMixDate() {
    	return mixDate;
    }
	
	public int getState() {
		return state;
	}
	
	public int getMixId() {
		return id;
	}
	
	public void setMixId(int id) {
		this.id = id;
	}
}

