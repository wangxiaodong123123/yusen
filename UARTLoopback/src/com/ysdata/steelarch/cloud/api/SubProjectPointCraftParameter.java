package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * ����ʵ����
 * */
public final class SubProjectPointCraftParameter {
	/**
     * ����Id
     * */
	@SerializedName("id")
    public int id;

    /**
     * ����λ�ñ��:K320+598-1
     * */
	@SerializedName("pointCode")
    public String pointCode;

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
     * ���ڿ������Ĺ������
     * */
/*	@SerializedName("clientOrderNo")
    public int clientOrderNo;*/
	
	 /**
     * ����Id
     * */
	@SerializedName("subProjectSectionId")
    public int subProjectSectionId;

    /**
     * ��������λ��,�ɹ��κż������
     * */
	@SerializedName("sectionMetre")
    public double sectionMetre;

    /**
     * �ý�����
     * */
	@SerializedName("rebarType")
    public String rebarType;

    /**
     * �ý��ͺ�
     * */
	@SerializedName("rebarModel")
    public String rebarModel;

    /**
     * ê�˳��ȣ���λ����/��
     * */
	@SerializedName("rebarLength")
    public double rebarLength;

    /**
     * ע�����ͺ�
     * */
	@SerializedName("groutingPumpModel")
    public String groutingPumpModel;

    /**
     * ע��������(1000 L/H)
     * */
	@SerializedName("groutingPumpFlow")
    public double groutingPumpFlow;

    /**
     * ���ע����
     * */
	@SerializedName("totalGroutingDesign")
    public double totalGroutingDesign;

    /**
     * ��ƿ׿�ѹ��
     * */
	@SerializedName("orificePressureDesign")
    public double orificePressureDesign;

    /**
     * ��Ʊ�ѹʱ��
     * */
	@SerializedName("holdPressureSecondsDesign")
    public int holdPressureSecondsDesign;

    /**
     * �Ƿ������
     * */
	@SerializedName("isFinished")
    public boolean isFinished;

    /**
     * �Ƿ�ϸ�
     * */
	@SerializedName("isPassed")
    public boolean isPassed;

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
	
	/**
     * ����ע����
     * */
	@SerializedName("endTotalGroutingActual")
    public double full_hole_capacity;
	
	/**
     * ����ע��ѹ��
     * */
	@SerializedName("endOrificePressureActual")
    public double full_hole_pressure;
	
	/**
     * �Ͽӵ�/�¿ӵ�
     * */
	@SerializedName("intDownFloor")
	public int grout_priority;
	
	public SubProjectPointCraftParameter()
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
		this.setTotalGroutingDesign(0d);
		this.setOrificePressureDesign(0d);
		this.setHoldPressureSecondsDesign(0);
		this.setFinished(false);
		this.setPassed(false);
		this.setCreateTime("");
		this.setModifyTime("");
		this.setFullHolePressure(0d);
		this.setFullHoleCapacity(0d);
		this.setGroutingPriority(0);
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

	public double getTotalGroutingDesign() {
		return totalGroutingDesign;
	}

	public void setTotalGroutingDesign(double totalGroutingDesign) {
		this.totalGroutingDesign = totalGroutingDesign;
	}

	public double getOrificePressureDesign() {
		return orificePressureDesign;
	}

	public void setOrificePressureDesign(double orificePressureDesign) {
		this.orificePressureDesign = orificePressureDesign;
	}

	public int getHoldPressureSecondsDesign() {
		return holdPressureSecondsDesign;
	}

	public void setHoldPressureSecondsDesign(int holdPressureSecondsDesign) {
		this.holdPressureSecondsDesign = holdPressureSecondsDesign;
	}

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
	
	public void setFullHolePressure(double full_hole_pressure) {
		this.full_hole_pressure = full_hole_pressure;
	}
	
	public void setFullHoleCapacity(double full_hole_capacity) {
		this.full_hole_capacity = full_hole_capacity;
	}
	
	public double getFullHolePressure() {
		return full_hole_pressure;
	}
	
	public double getFullHoleCapacity() {
		return full_hole_capacity;
	}
	
	public int getGroutingPriority() {
		return grout_priority;
	}
	
	public void setGroutingPriority(int grout_priority) {
		this.grout_priority = grout_priority;
	}
}
