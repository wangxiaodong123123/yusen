package com.ysdata.blender.cloud.api;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SubProjectPointGroutingParameterListResponse {
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
     * ��¼����
     * */
	@SerializedName("total")
    public int total;

	/**
     * ҳ��
     * */
	@SerializedName("pageIndex")
    public int pageIndex;

    /**
     * ҳ��С
     * */
	@SerializedName("pageSize")
    public int pageSize;

    /**
     * ҳ��
     * */
	@SerializedName("pageCount")
    public int pageCount;

    /**
     * �Ƿ��и�������
     * */
	@SerializedName("hasMore")
    public boolean HasMore;
	
	/**
     * ����
     * */
	@SerializedName("data")
	public List<SubProjectPointGroutingParameter> Data;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public boolean isHasMore() {
		return HasMore;
	}

	public void setHasMore(boolean hasMore) {
		HasMore = hasMore;
	}

	public List<SubProjectPointGroutingParameter> getData() {
		return Data;
	}

	public void setData(List<SubProjectPointGroutingParameter> data) {
		Data = data;
	}	
}