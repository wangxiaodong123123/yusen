package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * 合同段实体类
 * */
public final class SteelArchCraftData {
	/**
     * 线路ID
     * */
	@SerializedName("ProjectId")
    public int ProjectId;
	
	/**
     * 合同段ID
     * */
	@SerializedName("CsId")
    public int CsId;
	
	/**
     * 工程ID
     * */
	@SerializedName("SubProjectId")
    public int SubProjectId;
	
	/**
     * 工程名称
     * */
	@SerializedName("SubProjectName")
    public String SubProjectName;
	
	/**
	 * 钢拱架id 
	 * */
	@SerializedName("id")
	public int id;
	
	/**
	 * 钢拱架里程编号
	 * */
	@SerializedName("name")
	public String name;
	
    /**
     * 钢拱架设计间距
     * */
	@SerializedName("designDistance")
    public double designDistance;
	
    /**
     * 钢拱架编号距离
     * */
	@SerializedName("nameMeter")
    public double nameMeter;
	
    /**
     * 钢拱架离洞口距离
     * */
	@SerializedName("entranceDistance")
    public double entranceDistance;
	
    /**
     * 模板台车宽度
     * */
	@SerializedName("secondCarWidth")
    public double secondCarWidth;
}
