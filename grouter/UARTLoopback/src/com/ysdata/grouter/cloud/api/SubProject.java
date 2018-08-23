package com.ysdata.grouter.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * 工程实体类
 * */
public final class SubProject {
	/**
     * 工程Id
     * */
	@SerializedName("subProjectId")
    public int subProjectId;

    /**
     * 所属合同段Id
     * */
	@SerializedName("csId")
    public int csId;

    /**
     * 工程名称
     * */
	@SerializedName("subProjectName")
    public String subProjectName;

    /**
     * 工程总长度
     * */
	@SerializedName("totalLength")
    public double totalLength;

    /**
     * 当前施工长度的进度
     * */
	@SerializedName("totalLengthProgress")
    public double totalLengthProgress;

    /**
     * 起始工段号(里程)
     * */
	@SerializedName("firstSectionCode")
    public String firstSectionCode;

    /**
     * 截止工段号(里程)
     * */
	@SerializedName("lastSectionCode")
    public String lastSectionCode;

    /**
     * 实际里程
     * */
	@SerializedName("totalMetresActual")
    public double totalMetresActual;

    /**
     * 置筋轴向间距
     * */
	@SerializedName("rebarAxialSpacing")
    public double rebarAxialSpacing;

    /**
     * 置筋环向间距
     * */
	@SerializedName("rebarCirclingSpacing")
    public double rebarCirclingSpacing;

    /**
     * 设计锚孔数量
     * */
	@SerializedName("totalAnchorHoleDesign")
    public int totalAnchorHoleDesign;

    /**
     * 已完成锚孔数量
     * */
	@SerializedName("totalAnchorHoleActual")
    public int totalAnchorHoleActual;

    /**
     * 置筋总根数
     * */
	@SerializedName("totalSteelCount")
    public int totalSteelCount;

    /**
     * 置筋总米数
     * */
	@SerializedName("totalSteelLength")
    public int totalSteelLength;

    /**
     * 已置筋根数
     * */
	@SerializedName("usedSteelCount")
    public int usedSteelCount;

    /**
     * 已使用钢筋数量比例
     * */
	@SerializedName("usedSteelRatio")
    public double usedSteelRatio;

    /**
     * 设计总注浆量
     * */
	@SerializedName("totalGroutingDesign")
    public double totalGroutingDesign;

    /**
     * 已完成注浆量
     * */
	@SerializedName("totalGroutingActual")
    public double totalGroutingActual;

    /**
     * 工段数量
     * */
	@SerializedName("subProjectSectionCount")
    public int subProjectSectionCount;

    /**
     * 工点数量
     * */
	@SerializedName("subProjectPointCount")
    public int subProjectPointCount;

    /**
     * 不合格数量
     * */
	@SerializedName("qualifiedNoPassCount")
    public int qualifiedNoPassCount;

    /**
     * 是否已开工
     * */
	@SerializedName("isStarted")
    public boolean IsStarted;

    /**
     * 是否已完工
     * */
	@SerializedName("isCompleted")
    public boolean IsCompleted;

    /**
     * 创建时间
     * */
	@SerializedName("createTime")
    public String createTime;
	
	/**
     * 方向:上行/下行
     * */
	@SerializedName("IsDownDirection")
    public int IsDownDirection;
	
    /**
     * 修改时间
     * */
	@SerializedName("modifyTime")
    public String modifyTime;
	
	public SubProject()
	{
		this.setSubProjectId(0);
		this.setCsId(0);
		this.setSubProjectName("");
		this.setTotalLength(0d);
		this.setTotalLength(0d);
		this.setFirstSectionCode("");
		this.setLastSectionCode("");
		this.setTotalMetresActual(0d);
		this.setRebarAxialSpacing(0d);
		this.setRebarCirclingSpacing(0d);
		this.setTotalAnchorHoleDesign(0);
		this.setTotalAnchorHoleActual(0);
		this.setTotalSteelCount(0);
		this.setTotalSteelLength(0);
		this.setUsedSteelCount(0);
		this.setUsedSteelRatio(0d);
		this.setTotalGroutingDesign(0d);
		this.setTotalGroutingActual(0d);
		this.setSubProjectSectionCount(0);
		this.setSubProjectPointCount(0);
		this.setQualifiedNoPassCount(0);
		this.setIsStarted(false);
		this.setIsCompleted(false);
		this.setCreateTime("");
		this.setModifyTime("");
		this.setDirection(0);
	}

	public int getSubProjectId() {
		return subProjectId;
	}

	public void setSubProjectId(int subProjectId) {
		this.subProjectId = subProjectId;
	}

	public int getCsId() {
		return csId;
	}

	public void setCsId(int csId) {
		this.csId = csId;
	}

	public String getSubProjectName() {
		return subProjectName;
	}

	public void setSubProjectName(String subProjectName) {
		this.subProjectName = subProjectName;
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

	public String getFirstSectionCode() {
		return firstSectionCode;
	}

	public void setFirstSectionCode(String firstSectionCode) {
		this.firstSectionCode = firstSectionCode;
	}

	public String getLastSectionCode() {
		return lastSectionCode;
	}

	public void setLastSectionCode(String lastSectionCode) {
		this.lastSectionCode = lastSectionCode;
	}

	public double getTotalMetresActual() {
		return totalMetresActual;
	}

	public void setTotalMetresActual(double totalMetresActual) {
		this.totalMetresActual = totalMetresActual;
	}

	public double getRebarAxialSpacing() {
		return rebarAxialSpacing;
	}

	public void setRebarAxialSpacing(double rebarAxialSpacing) {
		this.rebarAxialSpacing = rebarAxialSpacing;
	}

	public double getRebarCirclingSpacing() {
		return rebarCirclingSpacing;
	}

	public void setRebarCirclingSpacing(double rebarCirclingSpacing) {
		this.rebarCirclingSpacing = rebarCirclingSpacing;
	}

	public int getTotalAnchorHoleDesign() {
		return totalAnchorHoleDesign;
	}

	public void setTotalAnchorHoleDesign(int totalAnchorHoleDesign) {
		this.totalAnchorHoleDesign = totalAnchorHoleDesign;
	}

	public int getTotalAnchorHoleActual() {
		return totalAnchorHoleActual;
	}

	public void setTotalAnchorHoleActual(int totalAnchorHoleActual) {
		this.totalAnchorHoleActual = totalAnchorHoleActual;
	}
	
	public int getDirection() {
		return IsDownDirection;
	}

	public void setDirection(int direction) {
		this.IsDownDirection = direction;
	}

	public int getTotalSteelCount() {
		return totalSteelCount;
	}

	public void setTotalSteelCount(int totalSteelCount) {
		this.totalSteelCount = totalSteelCount;
	}

	public int getTotalSteelLength() {
		return totalSteelLength;
	}

	public void setTotalSteelLength(int totalSteelLength) {
		this.totalSteelLength = totalSteelLength;
	}

	public int getUsedSteelCount() {
		return usedSteelCount;
	}

	public void setUsedSteelCount(int usedSteelCount) {
		this.usedSteelCount = usedSteelCount;
	}

	public double getUsedSteelRatio() {
		return usedSteelRatio;
	}

	public void setUsedSteelRatio(double usedSteelRatio) {
		this.usedSteelRatio = usedSteelRatio;
	}

	public double getTotalGroutingDesign() {
		return totalGroutingDesign;
	}

	public void setTotalGroutingDesign(double totalGroutingDesign) {
		this.totalGroutingDesign = totalGroutingDesign;
	}

	public double getTotalGroutingActual() {
		return totalGroutingActual;
	}

	public void setTotalGroutingActual(double totalGroutingActual) {
		this.totalGroutingActual = totalGroutingActual;
	}

	public int getSubProjectSectionCount() {
		return subProjectSectionCount;
	}

	public void setSubProjectSectionCount(int subProjectSectionCount) {
		this.subProjectSectionCount = subProjectSectionCount;
	}

	public int getSubProjectPointCount() {
		return subProjectPointCount;
	}

	public void setSubProjectPointCount(int subProjectPointCount) {
		this.subProjectPointCount = subProjectPointCount;
	}

	public int getQualifiedNoPassCount() {
		return qualifiedNoPassCount;
	}

	public void setQualifiedNoPassCount(int qualifiedNoPassCount) {
		this.qualifiedNoPassCount = qualifiedNoPassCount;
	}

	public boolean isIsStarted() {
		return IsStarted;
	}

	public void setIsStarted(boolean isStarted) {
		IsStarted = isStarted;
	}

	public boolean isIsCompleted() {
		return IsCompleted;
	}

	public void setIsCompleted(boolean isCompleted) {
		IsCompleted = isCompleted;
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