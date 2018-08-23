package com.ysdata.grouter.cloud.api;
import com.google.gson.annotations.SerializedName;

public class IntResponse{
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
	public int data;
	
	public IntResponse(){
		super();
		this.setIsSuccess(false);
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

	public int getData() {
		return this.data;
	}

	public void setData(int data) {
		this.data = data;
	}
}
