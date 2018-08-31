package com.ysdata.blender.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * 合同段实体类
 * */
public final class MixRatio {
	/**
     * 合同段Id
     * */
	@SerializedName("SubProjectId")
    public int SubProjectId;

    /**
     * 合同段名称
     * */
	@SerializedName("MixRatioWater")
    public double MixRatioWater;

    /**
     * 合同段总长度
     * */
	@SerializedName("CreateTime")
    public String CreateTime;

	public int getSubProjectId() {
		return SubProjectId;
	}

	public void setSubProjectId(int SubProjectId) {
		this.SubProjectId = SubProjectId;
	}

	public double getMixRatioWater() {
		return MixRatioWater;
	}

	public void setCsName(double MixRatioWater) {
		this.MixRatioWater = MixRatioWater;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String CreateTime) {
		this.CreateTime = CreateTime;
	}
}
