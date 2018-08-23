package com.ysdata.grouter.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * 工段实体类
 * */
public class SubProjectPointTimeNodes {
	/**
     * 时间点
     * */
	@SerializedName("Time")
    public double time;

    /**
     * 压力值
     * */
	@SerializedName("Pressure")
    public double pressure;

    /**
     * 是否为压力低点
     * */
	@SerializedName("IsMinPressureNode")
    public boolean isMinPressureNode;

    /**
     * 是否为压力高点
     * */
	@SerializedName("IsMaxPressureNode")
    public boolean isMaxPressureNode;

    /**
     * 是否为注浆时间节点
     * */
    @SerializedName("IsGroutingNode")
    public boolean isGroutingNode;

    /**
     * 从上一次泄压时间点到本次压力达到1.05倍时的时间，单位：秒
     * */
    @SerializedName("CurrGroutingSecs")
    public double currGroutingSecs;
    
    /**
     * 从上一次泄压时间点到本次压力达到1.05倍时的注浆量
     * */
    @SerializedName("CurrGrouting")
    public double currGrouting;

    /**
     * 累计注浆量，用于描绘随时间的Y轴数值
     * */
    @SerializedName("Grouting")
    public double grouting;

    
    public SubProjectPointTimeNodes(){
    	this.setTime(0);
    	this.setPressure(0D);
    	this.setGrouting(0D);
    }

	public double getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	public double Grouting() {
		return grouting;
	}

	public void setGrouting(double grouting) {
		this.grouting = grouting;
	}
}
