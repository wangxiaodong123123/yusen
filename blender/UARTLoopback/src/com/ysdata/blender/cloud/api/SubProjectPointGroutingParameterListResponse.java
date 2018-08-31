package com.ysdata.blender.cloud.api;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SubProjectPointGroutingParameterListResponse {
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
     * 记录总数
     * */
	@SerializedName("total")
    public int total;

	/**
     * 页次
     * */
	@SerializedName("pageIndex")
    public int pageIndex;

    /**
     * 页大小
     * */
	@SerializedName("pageSize")
    public int pageSize;

    /**
     * 页数
     * */
	@SerializedName("pageCount")
    public int pageCount;

    /**
     * 是否含有更多数据
     * */
	@SerializedName("hasMore")
    public boolean HasMore;
	
	/**
     * 数据
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