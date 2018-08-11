package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * 工点实体类
 * */
public final class SubProjectPoint {
	/**
     * 工点Id
     * */
	@SerializedName("id")
    public int id;

    /**
     * 工点位置编号:K320+598-1
     * */
	@SerializedName("pointCode")
    public String pointCode;

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
     * 所在控制器的工点序号
     * */
/*	@SerializedName("clientOrderNo")
    public int clientOrderNo;*/
	
	 /**
     * 工程Id
     * */
	@SerializedName("subProjectSectionId")
    public int subProjectSectionId;

    /**
     * 所属工段位置,由工段号计算而来
     * */
	@SerializedName("sectionMetre")
    public double sectionMetre;

    /**
     * 置筋类型
     * */
	@SerializedName("rebarType")
    public String rebarType;

    /**
     * 置筋型号
     * */
	@SerializedName("rebarModel")
    public String rebarModel;

    /**
     * 锚杆长度，单位：米/根
     * */
	@SerializedName("rebarLength")
    public double rebarLength;

    /**
     * 注浆泵型号
     * */
	@SerializedName("groutingPumpModel")
    public String groutingPumpModel;

    /**
     * 注浆泵流量(1000 L/H)
     * */
	@SerializedName("groutingPumpFlow")
    public double groutingPumpFlow;

    /**
     * 注浆日期
     * */
	@SerializedName("groutingDate")
    public String groutingDate;

    /**
     * 开始时间
     * */
	@SerializedName("beginTime")
    public String beginTime;

    /**
     * 结束时间
     * */
	@SerializedName("endTime")
    public String endTime;

    /**
     * 设计注浆量
     * */
	@SerializedName("totalGroutingDesign")
    public double totalGroutingDesign;

    /**
     * 实际注浆量
     * */
	@SerializedName("totalGroutingActual")
    public double totalGroutingActual;

    /**
     * 实际注浆时间(s)
     * */
	@SerializedName("totalGroutingSecs")
    public double totalGroutingSecs;

    /**
     * 注浆饱满率(Design/Actual)
     * */
	@SerializedName("groutingFullRation")
    public double groutingFullRation;

    /**
     * 设计孔口压力
     * */
	@SerializedName("orificePressureDesign")
    public double orificePressureDesign;

    /**
     * 实际孔口压力
     * */
	@SerializedName("orificePressureActual")
    public double orificePressureActual;

    /**
     * 设计保压时间
     * */
	@SerializedName("holdPressureSecondsDesign")
    public int holdPressureSecondsDesign;

    /**
     * 实际保压时间
     * */
	@SerializedName("holdPressureSecondsActual")
    public int holdPressureSecondsActual;

    /**
     * 时间节点数据(用于描绘图形)
     * */
/*	@SerializedName("timeNodes")
    public String timeNodes;*/

    /**
     * 是否已完成
     * */
	@SerializedName("isFinished")
    public boolean isFinished;

    /**
     * 是否合格
     * */
	@SerializedName("isPassed")
    public boolean isPassed;

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
	
	public SubProjectPoint()
	{
		this.setId(0);
		this.setPointCode("");
		this.setCsId(0);
		this.setSubProjectId(0);
		this.setSubProjectSectionId(0);
		this.setSectionMetre(0d);
		this.setRebarType("");
		this.setRebarModel("");
		this.setRebarLength(0d);
		this.setGroutingPumpModel("");
		this.setGroutingPumpFlow(0d);
		this.setGroutingDate("");
		this.setBeginTime("");
		this.setEndTime("");
		this.setTotalGroutingDesign(0d);
		this.setTotalGroutingActual(0d);
		this.setTotalGroutingSecs(0d);
		this.setGroutingFullRation(0d);
		this.setOrificePressureDesign(0d);
		this.setOrificePressureActual(0d);
		this.setHoldPressureSecondsDesign(0);
		this.setHoldPressureSecondsActual(0);
		this.setFinished(false);
		this.setPassed(false);
		this.setCreateTime("");
		this.setModifyTime("");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPointCode() {
		return pointCode;
	}

	public void setPointCode(String pointCode) {
		this.pointCode = pointCode;
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
	
	public int getSubProjectSectionId() {
		return subProjectSectionId;
	}
	
	public void setSubProjectSectionId(int subProjectSectionId) {
		this.subProjectSectionId = subProjectSectionId;
	}

	/*public int getClientOrderNo() {
		return clientOrderNo;
	}

	public void setClientOrderNo(int clientOrderNo) {
		this.clientOrderNo = clientOrderNo;
	}
*/
	public double getSectionMetre() {
		return sectionMetre;
	}

	public void setSectionMetre(double sectionMetre) {
		this.sectionMetre = sectionMetre;
	}

	public String getRebarType() {
		return rebarType;
	}

	public void setRebarType(String rebarType) {
		this.rebarType = rebarType;
	}

	public String getRebarModel() {
		return rebarModel;
	}

	public void setRebarModel(String rebarModel) {
		this.rebarModel = rebarModel;
	}

	public double getRebarLength() {
		return rebarLength;
	}

	public void setRebarLength(double rebarLength) {
		this.rebarLength = rebarLength;
	}

	public String getGroutingPumpModel() {
		return groutingPumpModel;
	}

	public void setGroutingPumpModel(String groutingPumpModel) {
		this.groutingPumpModel = groutingPumpModel;
	}

	public double getGroutingPumpFlow() {
		return groutingPumpFlow;
	}

	public void setGroutingPumpFlow(double groutingPumpFlow) {
		this.groutingPumpFlow = groutingPumpFlow;
	}

	public String getGroutingDate() {
		return groutingDate;
	}

	public void setGroutingDate(String groutingDate) {
		this.groutingDate = groutingDate;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public double getTotalGroutingSecs() {
		return totalGroutingSecs;
	}

	public void setTotalGroutingSecs(double totalGroutingSecs) {
		this.totalGroutingSecs = totalGroutingSecs;
	}

	public double getGroutingFullRation() {
		return groutingFullRation;
	}

	public void setGroutingFullRation(double groutingFullRation) {
		this.groutingFullRation = groutingFullRation;
	}

	public double getOrificePressureDesign() {
		return orificePressureDesign;
	}

	public void setOrificePressureDesign(double orificePressureDesign) {
		this.orificePressureDesign = orificePressureDesign;
	}

	public double getOrificePressureActual() {
		return orificePressureActual;
	}

	public void setOrificePressureActual(double orificePressureActual) {
		this.orificePressureActual = orificePressureActual;
	}

	public int getHoldPressureSecondsDesign() {
		return holdPressureSecondsDesign;
	}

	public void setHoldPressureSecondsDesign(int holdPressureSecondsDesign) {
		this.holdPressureSecondsDesign = holdPressureSecondsDesign;
	}

	public int getHoldPressureSecondsActual() {
		return holdPressureSecondsActual;
	}

	public void setHoldPressureSecondsActual(int holdPressureSecondsActual) {
		this.holdPressureSecondsActual = holdPressureSecondsActual;
	}

/*	public String getTimeNodes() {
		return timeNodes;
	}

	public void setTimeNodes(String timeNodes) {
		this.timeNodes = timeNodes;
	}*/

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public boolean isPassed() {
		return isPassed;
	}

	public void setPassed(boolean isPassed) {
		this.isPassed = isPassed;
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
