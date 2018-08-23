package com.ysdata.grouter.cloud.api;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PointGroutingDataUploadListResponse {
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
     * 数据
     * */
	@SerializedName("data")
	public SubProjectPointGroutingParameter Data;

}