package com.ysdata.grouter.cloud.api;
import com.google.gson.annotations.SerializedName;

public class FloatResponse {
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
	public float data;
	
	public FloatResponse(){
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

	public float getData() {
		return data;
	}

	public void setData(float data) {
		this.data = data;
	}
}
