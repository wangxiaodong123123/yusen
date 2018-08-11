package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * 工点实体类
 * */
public final class uploadBlenderActiveData {

	/**
     * 工程ID
     * */
	@SerializedName("SubProjectId")
    public int SubProjectId;
	
	/**
     * 搅拌序号
     * */
	@SerializedName("intOrder")
    public int intOrder;

    /**
     * 搅拌日期
     * */
	@SerializedName("strDate")
    public String strDate;

    /**
     * 搅拌开始时间
     * */
	@SerializedName("strBeginTime")
    public String strBeginTime;

    /**
     * 搅拌结束时间
     * */
	@SerializedName("strEndTime")
    public String strEndTime;

    /**
     * 水灰比
     * */
	@SerializedName("dblMixRatioWater")
    public double dblMixRatioWater;

    /**
     * 配料总量
     * */
	@SerializedName("dblCount")
    public double dblCount;

    /**
     * 搅拌机在隧道的位置（预留）
     * */
	@SerializedName("dblPosition")
    public double dblPosition;

    /**
     * 搅拌效果图
     * */
	@SerializedName("strDesignImage")
    public String strDesignImage;

    /**
     * 搅拌现场图
     * */
	@SerializedName("strActiveImage")
    public String strActiveImage;
	
	public uploadBlenderActiveData()
	{
		this.setSubProjectId(0);
		this.setIntOrder(0);
		this.setStrDate("");
		this.setStrBeginTime("");
		this.setStrEndTime("");
		this.setDblMixRatioWater(0d);
		this.setDblCount(0d);
		this.setDblPosition(0d);
		this.setStrDesignImage("");
		this.setStrActiveImage("");
	}

	public int getSubProjectId() {
		return SubProjectId;
	}

	public void setSubProjectId(int SubProjectId) {
		this.SubProjectId = SubProjectId;
	}

	public int getIntOrder() {
		return intOrder;
	}

	public void setIntOrder(int intOrder) {
		this.intOrder = intOrder;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public String getStrBeginTime() {
		return strBeginTime;
	}

	public void setStrBeginTime(String strBeginTime) {
		this.strBeginTime = strBeginTime;
	}
	
	public String getStrEndTime() {
		return strEndTime;
	}
	
	public void setStrEndTime(String strEndTime) {
		this.strEndTime = strEndTime;
	}

	public double getDblMixRatioWater() {
		return dblMixRatioWater;
	}

	public void setDblMixRatioWater(double dblMixRatioWater) {
		this.dblMixRatioWater = dblMixRatioWater;
	}

	public double getDblCount() {
		return dblCount;
	}

	public void setDblCount(double dblCount) {
		this.dblCount = dblCount;
	}

	public double getDblPosition() {
		return dblPosition;
	}

	public void setDblPosition(double dblPosition) {
		this.dblPosition = dblPosition;
	}

	public String getStrDesignImage() {
		return strDesignImage;
	}

	public void setStrDesignImage(String strDesignImage) {
		this.strDesignImage = strDesignImage;
	}

	public String getStrActiveImage() {
		return strActiveImage;
	}

	public void setStrActiveImage(String strActiveImage) {
		this.strActiveImage = strActiveImage;
	}
}
