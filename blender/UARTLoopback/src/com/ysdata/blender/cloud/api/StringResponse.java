package com.ysdata.blender.cloud.api;

import com.google.gson.annotations.SerializedName;

public class StringResponse {
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
	public String data;
	
	public StringResponse(){
		super();
		this.isSuccess = false;
		this.setErrorString("");
		this.setData("");
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
