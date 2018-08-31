package com.ysdata.blender.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * ��ͬ��ʵ����
 * */
public final class MixRatio {
	/**
     * ��ͬ��Id
     * */
	@SerializedName("SubProjectId")
    public int SubProjectId;

    /**
     * ��ͬ������
     * */
	@SerializedName("MixRatioWater")
    public double MixRatioWater;

    /**
     * ��ͬ���ܳ���
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
