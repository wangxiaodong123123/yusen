package com.ysdata.grouter.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * 合同段实体类
 * */
public final class ContractSection {
	/**
     * 合同段Id
     * */
	@SerializedName("csId")
    public int CsId;

    /**
     * 合同段名称
     * */
	@SerializedName("csName")
    public String csName;

    /**
     * 合同段总长度
     * */
	@SerializedName("totalLength")
    public double totalLength;

    /**
     * 当前施工长度的进度
     * */
	@SerializedName("totalLengthProgress")
    public double totalLengthProgress;

    /**
     * 实际里程
     * */
	@SerializedName("totalMetresActual")
    public double totalMetresActual;

    /**
     * 工程数量
     * */
	@SerializedName("subProjectCount")
    public int subProjectCount;

    /**
     * 是否已开工
     * */
	@SerializedName("isStarted")
    public boolean IsStarted;

    /**
     * 创建时间 
     * */
	@SerializedName("createTime")
    public String CreateTimeDisplay;
	
    /**
     * 修改时间 
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
