package com.ysdata.grouter.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * ��ͬ��ʵ����
 * */
public final class ContractSection {
	/**
     * ��ͬ��Id
     * */
	@SerializedName("csId")
    public int CsId;

    /**
     * ��ͬ������
     * */
	@SerializedName("csName")
    public String csName;

    /**
     * ��ͬ���ܳ���
     * */
	@SerializedName("totalLength")
    public double totalLength;

    /**
     * ��ǰʩ�����ȵĽ���
     * */
	@SerializedName("totalLengthProgress")
    public double totalLengthProgress;

    /**
     * ʵ�����
     * */
	@SerializedName("totalMetresActual")
    public double totalMetresActual;

    /**
     * ��������
     * */
	@SerializedName("subProjectCount")
    public int subProjectCount;

    /**
     * �Ƿ��ѿ���
     * */
	@SerializedName("isStarted")
    public boolean IsStarted;

    /**
     * ����ʱ�� 
     * */
	@SerializedName("createTime")
    public String CreateTimeDisplay;
	
    /**
     * �޸�ʱ�� 
     * */
	@SerializedName("modifyTime")
    public String ModifyTimeDisplay;

	public int getCsId() {
		return CsId;
	}

	public void setCsId(int csId) {
		CsId = csId;
	}

	public String getCsName() {
		return csName;
	}

	public void setCsName(String csName) {
		this.csName = csName;
	}

	public double getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(double totalLength) {
		this.totalLength = totalLength;
	}

	public double getTotalLengthProgress() {
		return totalLengthProgress;
	}

	public void setTotalLengthProgress(double totalLengthProgress) {
		this.totalLengthProgress = totalLengthProgress;
	}

	public double getTotalMetresActual() {
		return totalMetresActual;
	}

	public void setTotalMetresActual(double totalMetresActual) {
		this.totalMetresActual = totalMetresActual;
	}

	public int getSubProjectCount() {
		return subProjectCount;
	}

	public void setSubProjectCount(int subProjectCount) {
		this.subProjectCount = subProjectCount;
	}

	public boolean isIsStarted() {
		return IsStarted;
	}

	public void setIsStarted(boolean isStarted) {
		IsStarted = isStarted;
	}

	public String getCreateTimeDisplay() {
		return CreateTimeDisplay;
	}

	public void setCreateTimeDisplay(String CreateTimeDisplay) {
		this.CreateTimeDisplay = CreateTimeDisplay;
	}
	
	public String getModifyTimeDisplay() {
		return ModifyTimeDisplay;
	}

	public void setModifyTimeDisplay(String ModifyTimeDisplay) {
		this.ModifyTimeDisplay = ModifyTimeDisplay;
	}
}
