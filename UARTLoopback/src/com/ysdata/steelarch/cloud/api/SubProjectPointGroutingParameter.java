package com.ysdata.steelarch.cloud.api;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * ����ʵ����
 * */
public class SubProjectPointGroutingParameter {
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
     * ʵ�ʿ׿�ѹ��
     * */
	@SerializedName("orificePressureActual")
    public double orificePressureActual;

    /**
     * ʵ�ʱ�ѹʱ��
     * */
	@SerializedName("holdPressureSecondsActual")
    public int holdPressureSecondsActual;
	
	/**
     * ��������
     * */
	@SerializedName("updateDataTime")
    public String updateDataTime;
	
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
     * ע��ѹ��ʱ���
     * */
	@SerializedName("recvData")
    public String recvData;

	 /**
     * ʱ��ѹ��ע����
     * */
	@SerializedName("timeNodes")
    public List<SubProjectPointTimeNodes> timeNodes;
	
	public SubProjectPointGroutingParameter()
	{
		this.setId(0);
		this.setPointCode("");
		this.setGroutingDate("");
		this.setBeginTime("");
		this.setEndTime("");
		this.setRecvData("");
		this.setTotalGroutingActual(0d);
		this.setTotalGroutingSecs(0d);
		this.setGroutingFullRation(0d);
		this.setOrificePressureActual(0d);
		this.setHoldPressureSecondsActual(0);
		this.setUpdateDataTime("");
		this.setFullHolePressure(0d);
		this.setFullHoleCapacity(0d);
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


	public String getGroutingDate() {
		return groutingDate;
	}
	
	public String setRecvData() {
		return recvData;
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

	public double getOrificePressureActual() {
		return orificePressureActual;
	}

	public void setOrificePressureActual(double orificePressureActual) {
		this.orificePressureActual = orificePressureActual;
	}

	public int getHoldPressureSecondsActual() {
		return holdPressureSecondsActual;
	}

	public void setHoldPressureSecondsActual(int holdPressureSecondsActual) {
		this.holdPressureSecondsActual = holdPressureSecondsActual;
	}
	
	public String getUpdateDataTime() {
		return updateDataTime;
	}
	
	public void setUpdateDataTime(String updateDataTime) {
		this.updateDataTime = updateDataTime;
	}
	
	public void setRecvData(String recvData) {
		this.recvData = recvData;
	}
	
	public List<SubProjectPointTimeNodes> getData() {
		return timeNodes;
	}

	public void setData(List<SubProjectPointTimeNodes> data) {
		timeNodes = data;
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
}
