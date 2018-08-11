package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * 合同段实体类
 * */
public final class BlenderActiveData {
	/**
     * 标识ID
     * */
	@SerializedName("id")
    public int id;

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
     * 搅拌机在隧道的位置
     * */
	@SerializedName("dblPosition")
    public double dblPosition;
	
    /**
     * 搅拌效果图文件名
     * */
	@SerializedName("strDesignImage")
    public String strDesignImage;
	
    /**
     * 搅拌现场图文件名
     * */
	@SerializedName("strActiveImage")
    public String strActiveImage;
	

	/**
     * 水泥密度
     * */
	@SerializedName("dblCementDensity")
    public double dblCementDensity;
	
	/**
     * 水密度
     * */
	@SerializedName("dblWaterDensity")
    public double dblWaterDensity;
	
	/**
     * 水泥重量kg：总重量/（1+水灰比）
     * */
	@SerializedName("dblCementCount")
    public double dblCementCount;
	
	/**
     * 水重量kg：总重量-水泥重量
     * */
	@SerializedName("dblCementCount")
    public double dblWaterCount;
	
	/**
     * 拌和体积L：ROUND（水密度/水泥密度+水灰比/水密度，3）
     * */
	@SerializedName("dblVolume")
    public double dblVolume;
	
	/**
     * 混和后密度（比重）：ROUND（总重量/拌和体积,0）
     * */
	@SerializedName("dblMixDensity")
    public double dblMixDensity;
	
	/**
     * 搅拌时间（分钟）
     * */
	@SerializedName("dblTimeSpan")
    public double dblTimeSpan;
	
	/**
     * 此条数据上传时间（如果修改以修改时间为准）
     * */
	@SerializedName("CreateTime")
    public String CreateTime;
	
	/**
     * 搅拌效果图raw data
     * */
	@SerializedName("strDesignImageData")
    public String strDesignImageData;
	
	/**
     * 搅拌现场图raw data
     * */
	@SerializedName("strActiveImageData")
    public String strActiveImageData;
}
