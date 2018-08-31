package com.ysdata.blender.element;

public class DdMixParameter {
	private String create_time = "";
	private double mix_ratio = 0;
	
	public DdMixParameter(String create_time, double mix_ratio) {
		this.create_time = create_time;
		this.mix_ratio = mix_ratio;
	}
	
	public String getCreateTime () {
		return create_time;
	}
	
	public double getMixRatio() {
		return mix_ratio;
	}
	
	public void setCrateTime(String create_time) {
		this.create_time = create_time;
	}
	
	public void setMixRatio(double mix_ratio) {
		this.mix_ratio = mix_ratio;
	}
}

