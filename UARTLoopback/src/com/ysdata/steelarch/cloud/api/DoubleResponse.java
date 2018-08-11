package com.ysdata.steelarch.cloud.api;

import com.google.gson.annotations.SerializedName;

public class DoubleResponse {
	/**
	 * �Ƿ�ִ�гɹ�
	 * */
	@SerializedName("ok")
	public boolean isSuccess;

	/**
     * ������Ϣ
     * */
	@SerializedName("error")
	public String errorString;

    /**
     * ��������
     * */
	@SerializedName("data")
	public double data;
	
	public DoubleResponse(){
		super();
		this.isSuccess = false;
		this.setErrorString("");
		this.setData(0);
	}
	
	public boolean getIsSuccess() {
		return this.isSuccess;
	}
	
	public void setIsSuccess(boolean val) {
		this.isSuccess = val;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	public double getData() {
		return data;
	}

	public void setData(double data) {
		this.data = data;
	}
}
