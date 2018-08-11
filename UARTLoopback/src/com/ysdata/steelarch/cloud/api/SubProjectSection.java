package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * ����ʵ����
 * */
public class SubProjectSection {
	/**
     * ����Id
     * */
	@SerializedName("subProjectSectionId")
    public int subProjectSectionId;

    /**
     * ��ͬ��Id
     * */
	@SerializedName("csId")
    public int csId;

    /**
     * ����Id
     * */
	@SerializedName("subProjectId")
    public int subProjectId;

    /**
     * ������Ա
     * */
	@SerializedName("linkName")
    public String linkName;

    /**
     * ���κ�:K320+598
     * */
    @SerializedName("sectionCode")
    public String sectionCode;

    /**
     * ���γ���:�ɹ��κż���õ���eg��k20+50->20*1000+50
     * */
    @SerializedName("sectionMetre")
    public double sectionMetre;
    
    /**
     * ��ϱ�,ˮ��ռ��
     * */
    @SerializedName("mixRatioCement")
    public double MixRatioCement;

    /**
     * ��ϱ�,ɰռ��
     * */
    @SerializedName("mixRatioSand")
    public double MixRatioSand;

    /**
     * ��ϱ�,ˮռ��
     * */
    @SerializedName("mixRatioWater")
    public double MixRatioWater;

    /**
     * ��������
     * */
    @SerializedName("subProjectPointCount")
    public int subProjectPointCount;

    /**
     * ����ʱ��
     * */
    @SerializedName("createTime")
    public String createTime;
    
    /**
     * �޸�ʱ��
     * */
	@SerializedName("modifyTime")
    public String modifyTime;
    
    public SubProjectSection(){
    	this.setSubProjectSectionId(0);
    	this.setCsId(0);
    	this.setSubProjectId(0);
    	this.setLinkName("");
    	this.setSectionCode("");
    	this.setSectionMetre(0d);
    	this.setMixRatioCement(0d);
    	this.setMixRatioSand(0d);
    	this.setMixRatioWater(0d);
    	this.setSubProjectPointCount(0);
    	this.setCreateTime("");
    	this.setModifyTime("");
    }

	public int getSubProjectSectionId() {
		return subProjectSectionId;
	}

	public void setSubProjectSectionId(int subProjectSectionId) {
		this.subProjectSectionId = subProjectSectionId;
	}

	public int getCsId() {
		return csId;
	}

	public void setCsId(int csId) {
		this.csId = csId;
	}

	public int getSubProjectId() {
		return subProjectId;
	}

	public void setSubProjectId(int subProjectId) {
		this.subProjectId = subProjectId;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}
	
	public double getSectionMetre() {
		return sectionMetre;
	}
	
	public void setSectionMetre(double sectionMetre) {
		this.sectionMetre = sectionMetre;
	}

	public double getMixRatioCement() {
		return MixRatioCement;
	}

	public void setMixRatioCement(double mixRatioCement) {
		MixRatioCement = mixRatioCement;
	}

	public double getMixRatioSand() {
		return MixRatioSand;
	}

	public void setMixRatioSand(double mixRatioSand) {
		MixRatioSand = mixRatioSand;
	}

	public double getMixRatioWater() {
		return MixRatioWater;
	}

	public void setMixRatioWater(double mixRatioWater) {
		MixRatioWater = mixRatioWater;
	}

	public int getSubProjectPointCount() {
		return subProjectPointCount;
	}

	public void setSubProjectPointCount(int subProjectPointCount) {
		this.subProjectPointCount = subProjectPointCount;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
}
