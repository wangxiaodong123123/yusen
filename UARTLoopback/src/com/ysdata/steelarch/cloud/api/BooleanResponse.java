package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

public class BooleanResponse {
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
	public boolean data;
	
	public BooleanResponse(){
		super();
		this.isSuccess = false;
		this.setErrorString("");
		this.setData(false);
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

	public boolean getData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}
}
