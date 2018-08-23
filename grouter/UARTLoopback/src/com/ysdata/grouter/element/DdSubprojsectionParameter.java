package com.ysdata.grouter.element;

public class DdSubprojsectionParameter {
	private String mileage_name = "";
	private String create_date = "";
	private int anchor_count = 0;
	private int grouted_count = 0;
	private boolean pic_dd_state = false;
	
	public DdSubprojsectionParameter(String mileage_name, String create_date,  
			int anchor_count, int grouted_count, boolean pic_dd_state) {
		this.mileage_name = mileage_name;
		this.create_date = create_date;
		this.anchor_count = anchor_count;
		this.grouted_count = grouted_count;
		this.pic_dd_state = pic_dd_state;
	}
	
	public String getMileageName() {
		return mileage_name;
	}
	
	public String getCreateDate() {
		return create_date;
	}
	
	public int getAnchorCount() {
		return anchor_count;
	}
	
	public int getGroutedCount() {
		return grouted_count;
	}
	
	public boolean getPicState() {
		return pic_dd_state;
	}
}

