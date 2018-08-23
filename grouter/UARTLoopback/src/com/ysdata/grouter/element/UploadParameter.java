package com.ysdata.grouter.element;

public class UploadParameter {
	private String anchor_name = "";
	private String grouting_date = "";
	private String grouting_data = "";
	private String pad_string = "";
	private double full_hole_capacity = 0;
	
	public UploadParameter(String anchor_name, String grouting_date, String grouting_data,
			double full_hole_capacity, String pad_string) {
		this.anchor_name = anchor_name;
		this.grouting_date = grouting_date;
		this.grouting_data = grouting_data;
		this.full_hole_capacity = full_hole_capacity;
		this.pad_string = pad_string;
	}
	
	public UploadParameter(){
		
	}
	
	public void setAnchorName(String anchor_name) {
		this.anchor_name = anchor_name;
	}
	public String getAnchorName() {
    	return anchor_name;
    }
	
	public void setGroutingDate(String grouting_date) {
		this.grouting_date = grouting_date;
	}
	public String getGroutingDate() {
    	return grouting_date;
    }
	
	public void setGroutingData(String grouting_data) {
		this.grouting_data = grouting_data;
	}
	public String getGroutingData() {
    	return grouting_data;
    }
	
	public String getPadString() {
		return pad_string;
	}
	
	public void setPadString(String pad_string) {
		this.pad_string = pad_string;
	}
	
	public void setFullHoleCapacity(double full_hole_capacity) {
		this.full_hole_capacity = full_hole_capacity;
	}
	public double getFullHoleCapacity() {
    	return full_hole_capacity;
    }
}

