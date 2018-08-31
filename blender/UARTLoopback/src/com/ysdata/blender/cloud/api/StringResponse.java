package com.ysdata.blender.cloud.api;

import com.google.gson.annotations.SerializedName;

public class StringResponse {
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
