package com.ysdata.grouter.cloud.api;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PointGroutingDataUploadListResponse {
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
     * ����
     * */
	@SerializedName("data")
	public SubProjectPointGroutingParameter Data;

}