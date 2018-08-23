package com.ysdata.grouter.cloud.util;

import com.ysdata.grouter.element.AccoutInfo;

public class CacheManager {
	
	private static CacheManager cacheManager;
	private static String ticket;
	private static String userName;
	private static AccoutInfo accountInfo; 
    private static int press_id = 0;
    private static int id_start = 0;
    private static int id_end = 0;
    private static int project_id;
    private static int subproject_id;
    private static int anchor_id = 0;
    private static int anchor_order_no = 0;
    private static String anchor_name = "";
    private static String mileage_name;
    private static int mileage_number = 0;
    private static int design_hold_time = 0;
    private static float hold_cap = 0;
    private static float practice_cap = 0;
    private static float practice_hold_time = 0;
    private static String hold_press_range = "";
    private static int len_time_node = 0;
    private static String device = "";
    private static String net_error_msg = "";
    private static int response_code = 0;
	
	public static CacheManager getInstance() {
		if (cacheManager == null) {
			cacheManager = new CacheManager();
		}
		return cacheManager;
	}

	public static int getResponseCode() {
		return response_code;
	}
	
	public static String getNetErrorMsg() {
		return net_error_msg;
	}
	
	public static void setResponseCode(int code) {
		response_code = code;
	}
	
	public static void setNetErrorMsg(String _net_error_msg) {
		net_error_msg = _net_error_msg;
	}
	
	public static String getTicket() {
		return ticket;
	}
	
	public static void setTicket(String _ticket) {
		ticket = _ticket;
	}
	
	public static String getUserName() {
		return userName;
	}
	
	public static void setUserName(String _username) {
		userName = _username;
	}
	
	public static AccoutInfo getAccoutInfo() {
		return accountInfo;
	}
	
	public static void setAccoutInfo(AccoutInfo _accoutInfo) {
		accountInfo = _accoutInfo;
	}
	
    public static void setAnchorId(int _anchor_id) {
    	anchor_id = _anchor_id;
    }
    
    public static int getAnchorId() {
    	return anchor_id;
    }
    
    public static void setAnchorName(String _anchor_name) {
    	anchor_name = _anchor_name;
    }
    
    public static String getAnchorName() {
    	return anchor_name;
    }
    
    public static void setAnchorOrderNo(int _anchor_order_no) {
    	anchor_order_no = _anchor_order_no;
    }
    
    public static int getAnchorOrderNo() {
    	return anchor_order_no;
    }
    
    public static void setDbProjectId(int _project_id) {
    	project_id = _project_id;
    }
    
    public static int getDbProjectId() {
    	return project_id;
    }
    
    public static void setDbSubProjectId(int _subproject_id) {
    	subproject_id = _subproject_id;
    }
    
    public static int getDbSubProjectId() {
    	return subproject_id;
    }
    
    public static void setMileageName(String _mileage_name) {
    	mileage_name = _mileage_name;
    }
    
    public static String getMileageName() {
    	return mileage_name;
    }
    
    public static void setMileageNumber(int _mileage_number) {
    	mileage_number = _mileage_number;
    }
    
    public static int getMileageNumber() {
    	return mileage_number;
    }
    
    public static void setstartID(int _id_start) {
    	id_start = _id_start;
    }
    
    public static int getStartID() {
    	return id_start;
    }
    
    public static void setEndID(int _id_end) {
    	id_end = _id_end;
    }
    
    public static int getEndID() {
    	return id_end;
    }
    
    public static void setPressID(int _press_id) {
    	press_id = _press_id;
    }
    
    public static int getPressID() {
    	return press_id;
    }
    
    public static void setDesignHoldTime(int _design_hold_time) {
    	design_hold_time = _design_hold_time;
    }
    
    public static int getDesignHoldTime() {
    	return design_hold_time;
    }
    
    public static void setHoldCap(float _hold_cap) {
    	hold_cap = _hold_cap;
    }
    
    public static float getHoldCap() {
    	return hold_cap;
    }
    
    public static void setPracticeHoldTime(float _practice_hold_time) {
    	practice_hold_time = _practice_hold_time;
    }
    
    public static float getPracticeHoldTime() {
    	return practice_hold_time;
    }
    
    public static void setPracticeCap(float _practice_cap) {
    	practice_cap = _practice_cap;
    }
    
    public static float getPracticeCap() {
    	return practice_cap;
    }
    
    public static void setHoldPressRange(String _hold_press_range) {
    	hold_press_range = _hold_press_range;
    }
    
    public static String getHoldPressRange() {
    	return hold_press_range;
    }
    
    public static void setLenTimeNode(int _len_time_node) {
    	len_time_node = _len_time_node;
    }
    
    public static int getLenTimeNode() {
    	return len_time_node;
    }
    
    public static void setDevice(String _device) {
    	device = _device;
    }
    
    public static String getDevice() {
    	return device;
    }
}
