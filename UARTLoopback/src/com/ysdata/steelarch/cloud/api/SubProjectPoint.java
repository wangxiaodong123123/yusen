package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * ����ʵ����
 * */
public final class SubProjectPoint {
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
     * ע������
     * */
	@SerializedName("groutingDate")
    public String groutingDate;

    /**
     * ��ʼʱ��
     * */
	@SerializedName("beginTime")
    public String beginTime;

    /**
     * ����ʱ��
     * */
	@SerializedName("endTime")
    public String endTime;

    /**
     * ���ע����
     * */
	@SerializedName("totalGroutingDesign")
    public double totalGroutingDesign;

    /**
     * ʵ��ע����
     * */
	@SerializedName("totalGroutingActual")
    public double totalGroutingActual;

    /**
     * ʵ��ע��ʱ��(s)
     * */
	@SerializedName("totalGroutingSecs")
    public double totalGroutingSecs;

    /**
     * ע��������(Design/Actual)
     * */
	@SerializedName("groutingFullRation")
    public double groutingFullRation;

    /**
     * ��ƿ׿�ѹ��
     * */
	@SerializedName("orificePressureDesign")
    public double orificePressureDesign;

    /**
     * ʵ�ʿ׿�ѹ��
     * */
	@SerializedName("orificePressureActual")
    public double orificePressureActual;

    /**
     * ��Ʊ�ѹʱ��
     * */
	@SerializedName("holdPressureSecondsDesign")
    public int holdPressureSecondsDesign;

    /**
     * ʵ�ʱ�ѹʱ��
     * */
	@SerializedName("holdPressureSecondsActual")
    public int holdPressureSecondsActual;

    /**
     * ʱ��ڵ�����(�������ͼ��)
     * */
/*	@SerializedName("timeNodes")
    public String timeNodes;*/

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
