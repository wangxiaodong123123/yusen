package com.ysdata.steelarch.cloud.api;
import com.google.gson.annotations.SerializedName;

public class BooleanResponse {
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
