package com.ysdata.steelarch.cloud.api;

import com.google.gson.annotations.SerializedName;

public class DoubleResponse {
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
