package com.ysdata.grouter.cloud.api;
import com.google.gson.annotations.SerializedName;

/**
 * ����ʵ����
 * */
public final class SubProject {
	/**
     * ����Id
     * */
	@SerializedName("subProjectId")
    public int subProjectId;

    /**
     * ������ͬ��Id
     * */
	@SerializedName("csId")
    public int csId;

    /**
     * ��������
     * */
	@SerializedName("subProjectName")
    public String subProjectName;

    /**
     * �����ܳ���
     * */
	@SerializedName("totalLength")
    public double totalLength;

    /**
     * ��ǰʩ�����ȵĽ���
     * */
	@SerializedName("totalLengthProgress")
    public double totalLengthProgress;

    /**
     * ��ʼ���κ�(���)
     * */
	@SerializedName("firstSectionCode")
    public String firstSectionCode;

    /**
     * ��ֹ���κ�(���)
     * */
	@SerializedName("lastSectionCode")
    public String lastSectionCode;

    /**
     * ʵ�����
     * */
	@SerializedName("totalMetresActual")
    public double totalMetresActual;

    /**
     * �ý�������
     * */
	@SerializedName("rebarAxialSpacing")
    public double rebarAxialSpacing;

    /**
     * �ý����
     * */
	@SerializedName("rebarCirclingSpacing")
    public double rebarCirclingSpacing;

    /**
     * ���ê������
     * */
	@SerializedName("totalAnchorHoleDesign")
    public int totalAnchorHoleDesign;

    /**
     * �����ê������
     * */
	@SerializedName("totalAnchorHoleActual")
    public int totalAnchorHoleActual;

    /**
     * �ý��ܸ���
     * */
	@SerializedName("totalSteelCount")
    public int totalSteelCount;

    /**
     * �ý�������
     * */
	@SerializedName("totalSteelLength")
    public int totalSteelLength;

    /**
     * ���ý����
     * */
	@SerializedName("usedSteelCount")
    public int usedSteelCount;

    /**
     * ��ʹ�øֽ���������
     * */
	@SerializedName("usedSteelRatio")
    public double usedSteelRatio;

    /**
     * �����ע����
     * */
	@SerializedName("totalGroutingDesign")
    public double totalGroutingDesign;

    /**
     * �����ע����
     * */
	@SerializedName("totalGroutingActual")
    public double totalGroutingActual;

    /**
     * ��������
     * */
	@SerializedName("subProjectSectionCount")
    public int subProjectSectionCount;

    /**
     * ��������
     * */
	@SerializedName("subProjectPointCount")
    public int subProjectPointCount;

    /**
     * ���ϸ�����
     * */
	@SerializedName("qualifiedNoPassCount")
    public int qualifiedNoPassCount;

    /**
     * �Ƿ��ѿ���
     * */
	@SerializedName("isStarted")
    public boolean IsStarted;

    /**
     * �Ƿ����깤
     * */
	@SerializedName("isCompleted")
    public boolean IsCompleted;

    /**
     * ����ʱ��
     * */
	@SerializedName("createTime")
    public String createTime;
	
	/**
     * ����:����/����
     * */
	@SerializedName("IsDownDirection")
    public int IsDownDirection;
	
    /**
     * �޸�ʱ��
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