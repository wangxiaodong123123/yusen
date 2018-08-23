package com.ysdata.grouter.element;

public class MileageParameter {
	private int seq = 0;
	private String mileage_name = "";
	private int anchor_count = 0;
	private String water_ratio = "";
	private String create_people = "";
	private String create_date = "";
	
	public MileageParameter(int seq, String mileage_name, int anchor_count,  
			String create_people, String create_date, String water_ratio) {
		this.seq = seq;   //page
		this.mileage_name = mileage_name;
		this.anchor_count = anchor_count;
		this.create_people = create_people;
		this.create_date = create_date;
		this.water_ratio = water_ratio;
	}
	
	public int getSeq() {
		return seq;
	}
	
	public String getMileageName() {
		return mileage_name;
	}
	
	public int getAnchorCount() {
		return anchor_count;
	}
	
	public String getCreatePeople() {
		return create_people;
	}
	
	public String getCreateDate() {
		return create_date;
	}
	
	public String getWaterRatio() {
		return water_ratio;
	}
}

