package com.ysdata.grouter.cloud.util;

import com.ysdata.grouter.cloud.api.ContractSection;
import com.ysdata.grouter.cloud.api.ContractSectionListResponse;
import com.ysdata.grouter.cloud.api.StringResponse;
import com.ysdata.grouter.cloud.api.SubProjectListResponse;
import com.ysdata.grouter.cloud.api.SubProjectSectionListResponse;
import com.ysdata.grouter.file.LogFileManager;

import android.util.Log;


public class AppUtil {
	private final static String TAG = "ys200";

	public static void log(String msg) {
		Log.d(TAG, msg);
		LogFileManager.writeToLogfile(msg);
	}
	
	public static void printStringResponse(StringResponse stringResponse) {
		if (stringResponse != null) {
			log("isOk:" + stringResponse.isSuccess + " error:"+stringResponse.errorString +
					" data:"+stringResponse.data);
		}
	}
	
	public static void printContractSectionList(ContractSectionListResponse list) {
		if (list != null) {
			log("total:" + list.total + " pageIndex:" + list.pageIndex + " pageSize:" + list.pageSize +
					" pageCount:" + list.pageCount + " hasMore:" + list.HasMore + " isOk:" + list.isSuccess + 
					" error:" + list.errorString);
			for (int i = 0; i < list.total; i++) {
				ContractSection contractSecton = list.Data.get(i);
				log("csId:" + contractSecton.CsId + " csName:" + contractSecton.csName + " totalLength:" + 
						contractSecton.totalLength + " totalLengthProgress:" + contractSecton.totalLengthProgress + 
						" totalMetresActual:" + contractSecton.totalMetresActual + " subProjectCount:" + 
						contractSecton.subProjectCount + " isStarted:" + contractSecton.IsStarted + " createTime" +
						contractSecton.CreateTimeDisplay);
			}
		}
	}
	
	public static void printSubProjectList(SubProjectListResponse list) {
		
	}
	
	public static void printSubProjectSectionList(SubProjectSectionListResponse list) {
		
	}
}
