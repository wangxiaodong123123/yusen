package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * 合同段实体类
 * */
public final class SteelArchCollectData {
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
	 * 钢拱架Id
	 * */
	@SerializedName("id")
	public int id;
	
	/**
	 * 钢拱架里程编号
	 * */
	@SerializedName("name")
	public String name;
	
	/**
	 * 左侧测量日期
	 * */
	@SerializedName("leftMeasureDate")
	public String leftMeasureDate;
	
	/**
	 * 右侧测量日期
	 * */
	@SerializedName("rightMeasureDate")
	public String rightMeasureDate;	
	
	/**
	 * 左侧测量间距
	 * */
	@SerializedName("leftMeasureDistance")
	public double leftMeasureDistance;
	
	/**
	 * 右侧测量间距
	 * */
	@SerializedName("rightMeasureDistance")
	public double rightMeasureDistance;		
	
	/**
	 * 左侧离掌子面距离
	 * */
	@SerializedName("leftTunnelfaceDistance")
	public double leftTunnelfaceDistance;
	
	/**
	 * 右侧离掌子面距离
	 * */
	@SerializedName("rightTunnelfaceDistance")
	public double rightTunnelfaceDistance;		
	
	/**
	 * 左侧离模板台车距离
	 * */
	@SerializedName("leftSecondCarDistance")
	public double leftSecondCarDistance;
	
	/**
	 * 右侧离模板台车距离
	 * */
	@SerializedName("rightSecondCarDistance")
	public double rightSecondCarDistance;		
	
	/**
	 * 左侧洞口方向照片
	 * */
	@SerializedName("leftPicDirEntrance")
	public String leftPicDirEntrance;
	
	/**
	 * 右侧洞口方向照片
	 * */
	@SerializedName("rightPicDirEntrance")
	public String rightPicDirEntrance;
	
	/**
	 * 左侧掌子面方向照片
	 * */
	@SerializedName("leftPicDirTunnelface")
	public String leftPicDirTunnelface;
	
	/**
	 * 右侧掌子面方向照片
	 * */
	@SerializedName("rightPicDirTunnelface")
	public String rightPicDirTunnelface;	
}
