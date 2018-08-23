package com.ysdata.grouter.element;

public class MileageUploadState {
	private String mileageName = "";
	private int state = 0;
	
	public MileageUploadState() {

	}
	
	public MileageUploadState(String mileageName, int state) {
		this.mileageName = mileageName;
		this.state = state;
	}
	
	public void setMileageName(String mileageName) {
		this.mileageName = mileageName;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public String getmileageName() {
    	return mileageName;
    }
	
	public int getState() {
		return state;
	}
}

