package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * 工段实体类
 * */
public class SubProjectSection {
	/**
     * 工段Id
     * */
	@SerializedName("subProjectSectionId")
    public int subProjectSectionId;

    /**
     * 合同段Id
     * */
	@SerializedName("csId")
    public int csId;

    /**
     * 工程Id
     * */
	@SerializedName("subProjectId")
    public int subProjectId;

    /**
     * 编制人员
     * */
	@SerializedName("linkName")
    public String linkName;

    /**
     * 工段号:K320+598
     * */
    @SerializedName("sectionCode")
    public String sectionCode;

    /**
     * 工段长度:由工段号计算得到，eg：k20+50->20*1000+50
     * */
    @SerializedName("sectionMetre")
    public double sectionMetre;
    
    /**
     * 配合比,水泥占比
     * */
    @SerializedName("mixRatioCement")
    public double MixRatioCement;

    /**
     * 配合比,砂占比
     * */
    @SerializedName("mixRatioSand")
    public double MixRatioSand;

    /**
     * 配合比,水占比
     * */
    @SerializedName("mixRatioWater")
    public double MixRatioWater;

    /**
     * 工点数量
     * */
    @SerializedName("subProjectPointCount")
    public int subProjectPointCount;

    /**
     * 创建时间
     * */
    @SerializedName("createTime")
    public String createTime;
    
    /**
     * 修改时间
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
