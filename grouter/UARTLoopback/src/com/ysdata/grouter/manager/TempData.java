package com.ysdata.grouter.manager;

import java.util.ArrayList;

import android.content.Context;

public class TempData {
    private static TempData mQueryTempData = null;
    private Context mContext = null;
    private int len_time_node = 0;
    private int len_333ms = 0;
    private int len_20s = 0;
    private int press_id = 5;
    private int id_start = 0;
    private int id_end = 0;
    private int suidao_value = 0;
    private String suidao_name = "";
    private int project_id;
    private int subproject_id;
    private int anchor_id = 0;
    private String device = "";
    private String mileage;
    private int mileage_number = 0;
    private int design_hold_time = 0;
    private float hold_cap = 0;
    private float practice_cap = 0;
    private float practice_hold_time = 0;
    private String hold_press_range = "";
    
    /**
     * 获得Value单例模式的实例
     **/
    public static TempData getSingleQueryTempData()
    {
        if (mQueryTempData == null)
        {
            synchronized (TempData.class)
            {
                if (mQueryTempData == null)
                {
                    mQueryTempData = new TempData();
                }
            }
        }
        return mQueryTempData;
    }
    
    public void setAnchorId(int anchor_id) {
    	this.anchor_id = anchor_id;
    }
    
    public int getAnchorId() {
    	return anchor_id;
    }
    
    public void setDevice(String device) {
    	this.device = device;
    }
    
    public String getDevice() {
    	return device;
    }
    
    public void setDbProjectId(int project_id) {
    	this.project_id = project_id;
    }
    
    public int getDbProjectId() {
    	return project_id;
    }
    
    public void setDbSubProjectId(int subproject_id) {
    	this.subproject_id = subproject_id;
    }
    
    public int getDbSubProjectId() {
    	return subproject_id;
    }
    
    public void setCurMileage(String mileage) {
    	this.mileage = mileage;
    }
    
    public String getCurMileage() {
    	return mileage;
    }
    
    public void setMileageNumber(int mileage_number) {
    	this.mileage_number = mileage_number;
    }
    
    public int getMileageNumber() {
    	return mileage_number;
    }
    
    public void setstartID(int id_start) {
    	this.id_start = id_start;
    }
    
    public int getStartID() {
    	return id_start;
    }
    
    public void setSuidaoValue(int suidao_value) {
    	this.suidao_value = suidao_value;
    }
    
    public int getSuidaoValue() {
    	return suidao_value;
    }
    
    
    public void setEndID(int id_end) {
    	this.id_end = id_end;
    }
    
    public int getEndID() {
    	return id_end;
    }
    
    public void setSuidaoName(String suidao_name) {
    	this.suidao_name = suidao_name;
    }
    
    public String getSuidaoName() {
    	return suidao_name;
    }
    
    public void setPressID(int press_id) {
    	this.press_id = press_id;
    }
    
    public int getPressID() {
    	return press_id;
    }
    
    public void setLen333ms(int len_333ms) {
    	this.len_333ms = len_333ms;
    }
    
    public int getLen333ms() {
    	return len_333ms;
    }
    
    public void setLenTimeNode(int len_time_node) {
    	this.len_time_node = len_time_node;
    }
    
    public int getLenTimeNode() {
    	return len_time_node;
    }
    
    public void setLen20s(int len_20s) {
    	this.len_20s = len_20s;
    }
    
    public int getLen20s() {
    	return len_20s;
    }
    
    public void setDesignHoldTime(int design_hold_time) {
    	this.design_hold_time = design_hold_time;
    }
    
    public int getDesignHoldTime() {
    	return design_hold_time;
    }
    
    public void setHoldCap(float hold_cap) {
    	this.hold_cap = hold_cap;
    }
    
    public float getHoldCap() {
    	return hold_cap;
    }
    
    public void setPracticeHoldTime(float practice_hold_time) {
    	this.practice_hold_time = practice_hold_time;
    }
    
    public float getPracticeHoldTime() {
    	return practice_hold_time;
    }
    
    public void setPracticeCap(float practice_cap) {
    	this.practice_cap = practice_cap;
    }
    
    public float getPracticeCap() {
    	return practice_cap;
    }
    
    public void setHoldPressRange(String hold_press_range) {
    	this.hold_press_range = hold_press_range;
    }
    
    public String getHoldPressRange() {
    	return hold_press_range;
    }
}
