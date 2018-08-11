package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * ����ʵ����
 * */
public final class uploadBlenderActiveData {

	/**
     * ����ID
     * */
	@SerializedName("SubProjectId")
    public int SubProjectId;
	
	/**
     * �������
     * */
	@SerializedName("intOrder")
    public int intOrder;

    /**
     * ��������
     * */
	@SerializedName("strDate")
    public String strDate;

    /**
     * ���迪ʼʱ��
     * */
	@SerializedName("strBeginTime")
    public String strBeginTime;

    /**
     * �������ʱ��
     * */
	@SerializedName("strEndTime")
    public String strEndTime;

    /**
     * ˮ�ұ�
     * */
	@SerializedName("dblMixRatioWater")
    public double dblMixRatioWater;

    /**
     * ��������
     * */
	@SerializedName("dblCount")
    public double dblCount;

    /**
     * ������������λ�ã�Ԥ����
     * */
	@SerializedName("dblPosition")
    public double dblPosition;

    /**
     * ����Ч��ͼ
     * */
	@SerializedName("strDesignImage")
    public String strDesignImage;

    /**
     * �����ֳ�ͼ
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
