package com.ysdata.blender.wireless.client;

import android.content.Context;

public class Value {
	private float[] Value_500MS_AD;
	private int AD_500MS_Time = 0;
	private float[] Value_20S_AD;
	private int AD_20S_Time = 0;
	
	private String date = "";
	private float mileage = 0;
	private int dev_seq = 0;
	private float capacity_cell = 0;
	private float theore_capacity = 0;
	private float theore_pressure = 0;
	private float measure_capacity = 0;
	private float measure_pressure = 0;
	private int hold_time = 0;
	
    private static Value mValue = null;
    private Context mContext = null;
    
    private Value(Context context)
    {
        mContext = context;
    }
    
    /**
     * 获得Value单例模式的实例
     **/
    public static Value getSingleValue(Context context)
    {
        if (mValue == null)
        {
            synchronized (Value.class)
            {
                if (mValue == null)
                {
                    mValue = new Value(context);
                }
            }
        }
        return mValue;
    }
    
    public void setHoldTime(int hold_time) {
    	this.hold_time = hold_time;
    }
    
    public int getHoldTime() {
    	return hold_time;
    }
	
	public void setAD_500MS_Time(int AD_500MS_Time) {
		this.AD_500MS_Time = AD_500MS_Time;
	}
	public int getAD_500MS_Time() {
		return AD_500MS_Time;
	}	
	
	public void setValue_500MS_AD(float[] Value_500MS_AD) {
		this.Value_500MS_AD = Value_500MS_AD;
	}
	public float[] getValue_500MS_AD() {
		return Value_500MS_AD;
	}	
	
	public void setValue_20S_AD(float[] Value_20S_AD) {
		this.Value_20S_AD = Value_20S_AD;
	}
	public float[] getValue_20S_AD() {
		return Value_20S_AD;
	}
	
	public void setAD_20S_Time(int AD_20S_Time) {
		this.AD_20S_Time = AD_20S_Time;
	}
	public int getAD_20S_Time() {
		return AD_20S_Time;
	}	
	
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	
	public void setMileage(float mileage) {
		this.mileage = mileage;
	}
	public float getMileage() {
		return mileage;
	}
	
	public void setDevSeq(int dev_seq) {
		this.dev_seq = dev_seq;
	}
	public int getDevSeq() {
		return dev_seq;
	}
	
	public void setTheorePressure(float theore_pressure) {
		this.theore_pressure = theore_pressure;
	}
	public float getTheorePressure() {
		return theore_pressure;
	}
	
	public void setTheoreCapacity(float theore_capacity) {
		this.theore_capacity = theore_capacity;
	}
	
	public float getTheoreCapacity() {
		return theore_capacity;
	}
	
	public void setMeasurePressure(float measure_pressure) {
		this.measure_pressure = measure_pressure;
	}
	public float getMeasurePressure() {
		return measure_pressure;
	}
	
	public void setMeasureCapacity(float measure_capacity) {
		this.measure_capacity = measure_capacity;
	}
	public float getMeasureCapacity() {
		return measure_capacity;
	}
	
	public void setSlurryCapacityCell(float capacity_cell) {
		this.capacity_cell = capacity_cell;
	}
	public float getCapacityCell() {
		return capacity_cell;
	}	
}
