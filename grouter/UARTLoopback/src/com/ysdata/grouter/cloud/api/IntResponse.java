package com.ysdata.grouter.cloud.api;
import com.google.gson.annotations.SerializedName;

public class IntResponse{
	/**
	 * 是否执行成功
	 * */
	@SerializedName("ok")
	public boolean isSuccess;

	/**
     * 错误信息
     * */
	@SerializedName("error")
	public String errorString;

    /**
     * 返回数据
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
