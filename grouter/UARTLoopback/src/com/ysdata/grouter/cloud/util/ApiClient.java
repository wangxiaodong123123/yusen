package com.ysdata.grouter.cloud.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.google.gson.Gson;
import com.ysdata.grouter.cloud.api.BooleanResponse;
import com.ysdata.grouter.cloud.api.ContractSectionListResponse;
import com.ysdata.grouter.cloud.api.IntegerListResponse;
import com.ysdata.grouter.cloud.api.PointGroutingDataUploadListResponse;
import com.ysdata.grouter.cloud.api.StringResponse;
import com.ysdata.grouter.cloud.api.SubProjectListResponse;
import com.ysdata.grouter.cloud.api.SubProjectPointGroutingParameterListResponse;
import com.ysdata.grouter.cloud.api.SubProjectPointListResponse;
import com.ysdata.grouter.cloud.api.SubProjectPointTimeNodesListResponse;
import com.ysdata.grouter.cloud.api.SubProjectSectionListResponse;
import com.ysdata.grouter.database.SyncTimeDataBaseAdapter;


/**
 * 网络请求获取数据帮助类
 * Created by Administrator on 2015/3/24.
 */
public class ApiClient {

	public final String BASE_URL = "http://120.76.251.106:8003/api/";
	public final String DOWNLOAD_IMAGE_URL = "http://120.76.251.106:8003/api/project/getSectionImage";
		
    private static ApiClient mInstance = null;
    private Context context = null;

    private ApiClient(Context context) {
    	this.context = context;
    }
    
    public static ApiClient getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ApiClient.class) {
                if (mInstance == null) {
                    mInstance = new ApiClient(context);
                }
            }
        }
        return mInstance;
    }
    
    /**
     * 解析json to Java对象
     *
     * @param json   json串
     * @param tClass 解析对象class
     * @param <T>    返回对象类型
     */
    public static <T> T parseJson(String json, Class<T> tClass) {
        try {
            return new Gson().fromJson(json, tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public PointGroutingDataUploadListResponse uploadPointGroutingData(int subproject_id, String pointCode, String endTime, 
    		String data, double full_hole_capacity) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.post(BASE_URL + "project/uploadPointData?subProjectId=" + subproject_id + 
    			"&pointCode=" + toURLEncoder(pointCode) + "&endTime=" + toURLEncoder(endTime)+
    			"&data="+data+"&TotalGrouting="+full_hole_capacity, header, null);
    	return parseJson(result, PointGroutingDataUploadListResponse.class);
    }
    
    public BooleanResponse uploadPointPadDataSmp(int subproject_id, String pointCode, String strPadData) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.post(BASE_URL + "project/uploadPointPadDataSmp?subProjectId=" + subproject_id + 
    			"&pointCode=" + toURLEncoder(pointCode) +
    			"&strPadData="+toURLEncoder(strPadData), header, null);
    	return parseJson(result, BooleanResponse.class);
    }
    
    public BooleanResponse uploadPointPadData(int subproject_id, String pointCode, String endTime, 
    		String data, String strPadData) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.post(BASE_URL + "project/uploadPointPadData?subProjectId=" + subproject_id + 
    			"&pointCode=" + toURLEncoder(pointCode) + "&endTime=" + toURLEncoder(endTime)+
    			"&data="+data+"&strPadData="+strPadData, header, null);
    	return parseJson(result, BooleanResponse.class);
    }
    
/*    public boolean uploadPointGroutingData(int subproject_id, String pointCode, String endTime, 
    		String data) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.post(BASE_URL + "project/uploadPointData?subProjectId=" + subproject_id + 
    			"&pointCode=" + toURLEncoder(pointCode) + "&endTime=" + toURLEncoder(endTime)+
    			"&data="+data, header, null);
    	return parseJson(result, PointGroutingDataUploadListResponse.class).isSuccess;
    }*/
    
    public SubProjectPointGroutingParameterListResponse getProjectPointGroutingDataList(int subproject_id,
    		int pageIndex) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.get(BASE_URL + "project/getPointDataList?subProjectId=" + subproject_id + 
    			"&pageIndex=" + pageIndex, header);
    	return parseJson(result, SubProjectPointGroutingParameterListResponse.class);
    }
    
    public SubProjectPointGroutingParameterListResponse getSyncSubProjectPointGroutingDataList(int subproject_id,
    		int pageIndex) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
//    	String result = HttpUtils.get(BASE_URL + "sync/getSyncSubProjectPointsData?subProjectId=" + 
//    			subproject_id + "&lastSyncTime=" +
//				toURLEncoder(SharePrefOperator.getSingleSharePrefOperator(context).getSyncTime(
//						ConstDef.KEY_SHAREPREF_SUBPROJECTPOINT_GROUTING_DATA_SYNCTIME)) + "&pageIndex=" + pageIndex, header);
    	String result = HttpUtils.get(BASE_URL + "sync/getSyncSubProjectPointsData?subProjectId=" + 
    			subproject_id + "&lastSyncTime=" +
				toURLEncoder(SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context).getSyncTime(
    					ConstDef.SYNCTIME_TYPE_SUBPROJECTPOINT_GROUTING_DATA)) + "&pageIndex=" + pageIndex, header);
    	return parseJson(result, SubProjectPointGroutingParameterListResponse.class);
    }
    
    public SubProjectPointTimeNodesListResponse getPointTimeNodesById(int subproject_Id, int pointId) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.get(BASE_URL + "project/getPointTimeNodesById/" + subproject_Id + 
    			"/" + pointId, header);
    	return parseJson(result, SubProjectPointTimeNodesListResponse.class);
    }
    
    public SubProjectPointListResponse getPointlist(int subproject_Id, String sectionCode, int pageIndex) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.get(BASE_URL + "project/getPointlist?subProjectId=" + subproject_Id + 
    			"&sectionCode=" + toURLEncoder(sectionCode) + "&pageIndex=" + pageIndex, header);
    	return parseJson(result, SubProjectPointListResponse.class);
    }
    
    public IntegerListResponse getSyncDeleteSubProjectPoints(int subproject_Id, int pageIndex) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
//    	String result = HttpUtils.get(BASE_URL + "sync/getSyncDeleteSubProjectPoints?subProjectId=" + 
//    			subproject_Id + "&lastSyncTime=" +
//				toURLEncoder(SharePrefOperator.getSingleSharePrefOperator(context).getSyncTime(
//						ConstDef.KEY_SHAREPREF_SUBPROJECTPOINT_SYNCTIME)) + "&pageIndex=" + pageIndex, header);
    	String result = HttpUtils.get(BASE_URL + "sync/getSyncDeleteSubProjectPoints?subProjectId=" + 
    			subproject_Id + "&lastSyncTime=" +
				toURLEncoder(SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context).getSyncTime(
    					ConstDef.SYNCTIME_TYPE_SUBPROJECTPOINT)) + "&pageIndex=" + pageIndex, header);
    	return parseJson(result, IntegerListResponse.class);
    }
    
    public SubProjectPointListResponse getSyncSubProjectPoints(int subproject_Id, int pageIndex) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
//    	String result = HttpUtils.get(BASE_URL + "sync/getSyncSubProjectPoints?subProjectId=" + 
//    			subproject_Id + "&lastSyncTime=" +
//				toURLEncoder(SharePrefOperator.getSingleSharePrefOperator(context).getSyncTime(
//						ConstDef.KEY_SHAREPREF_SUBPROJECTPOINT_SYNCTIME)) + "&pageIndex=" + pageIndex, header);
    	String result = HttpUtils.get(BASE_URL + "sync/getSyncSubProjectPoints?subProjectId=" + 
    			subproject_Id + "&lastSyncTime=" +
				toURLEncoder(SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context).getSyncTime(
    					ConstDef.SYNCTIME_TYPE_SUBPROJECTPOINT)) + "&pageIndex=" + pageIndex, header);
    	return parseJson(result, SubProjectPointListResponse.class);
    }
    
    public IntegerListResponse getSyncSubProjectSectionImages(int subproject_Id, int pageIndex) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
//    	String result = HttpUtils.get(BASE_URL + "sync/getSyncSubProjectSectionImages?subProjectId=" + 
//    			subproject_Id + "&lastSyncTime=" +
//				toURLEncoder(SharePrefOperator.getSingleSharePrefOperator(context).getSyncTime(
//						ConstDef.KEY_SHAREPREF_IMAGES_SYNCTIME)) + "&pageIndex=" + pageIndex, header);
    	String result = HttpUtils.get(BASE_URL + "sync/getSyncSubProjectSectionImages?subProjectId=" + 
    			subproject_Id + "&lastSyncTime=" +
				toURLEncoder(SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context).getSyncTime(
    					ConstDef.SYNCTIME_TYPE_IMAGES)) + "&pageIndex=" + pageIndex, header);
    	return parseJson(result, IntegerListResponse.class);
    }
    
    public IntegerListResponse getSectionExpImageList(int subproject_Id, int pageIndex) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.get(BASE_URL + "project/getSectionExpImageList/" + subproject_Id + 
    			"/" + pageIndex, header);
    	return parseJson(result, IntegerListResponse.class);
    }
    
    public IntegerListResponse getSyncSectionExpImageList(int subproject_Id, int pageIndex) {
    	return null;
    }
    
    public SubProjectSectionListResponse getSubProjectSectionlist(int subproject_Id, int pageIndex) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.get(BASE_URL + "project/getSubProjectSectionList/" + subproject_Id + "/" + pageIndex, header);
    	return parseJson(result, SubProjectSectionListResponse.class);
    }
    
    public IntegerListResponse getSyncDeleteSubProjectSections(int subproject_Id, int pageIndex) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
//    	String result = HttpUtils.get(BASE_URL + "sync/getSyncDeleteSubProjectSections?subProjectId=" + 
//    			subproject_Id + "&lastSyncTime=" +
//				toURLEncoder(SharePrefOperator.getSingleSharePrefOperator(context).getSyncTime(
//						ConstDef.KEY_SHAREPREF_SUBPROJECTSECTION_SYNCTIME)) + "&pageIndex=" + pageIndex, header);
    	String result = HttpUtils.get(BASE_URL + "sync/getSyncDeleteSubProjectSections?subProjectId=" + 
    			subproject_Id + "&lastSyncTime=" +
				toURLEncoder(SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context).getSyncTime(
    					ConstDef.SYNCTIME_TYPE_SUBPROJECTSECTION)) + "&pageIndex=" + pageIndex, header);
    	return parseJson(result, IntegerListResponse.class);
    }
    
    public SubProjectSectionListResponse getSyncSubProjectSections(int subproject_Id, int pageIndex) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.get(BASE_URL + "sync/getSyncSubProjectSections?subProjectId=" + 
    			subproject_Id + "&lastSyncTime=" +
				toURLEncoder(SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context).getSyncTime(
    					ConstDef.SYNCTIME_TYPE_SUBPROJECTSECTION)) + "&pageIndex=" + pageIndex, header);
    	return parseJson(result, SubProjectSectionListResponse.class);
    }
    
    public SubProjectListResponse getSubProjectlist(int csId) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.get(BASE_URL + "project/getSubProjectlist/" + csId, header);
    	return parseJson(result, SubProjectListResponse.class);
    }
    
    public IntegerListResponse getSyncDeleteSubProjects(int csId) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
//    	String result = HttpUtils.get(BASE_URL + "sync/getSyncDeleteSubProjects?csId=" + csId + "&lastSyncTime=" +
//				toURLEncoder(SharePrefOperator.getSingleSharePrefOperator(context).getSyncTime(
//						ConstDef.KEY_SHAREPREF_SUBPROJECT_SYNCTIME)), header);
    	String result = HttpUtils.get(BASE_URL + "sync/getSyncDeleteSubProjects?csId=" + csId + "&lastSyncTime=" +
				toURLEncoder(SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context).getSyncTime(
    					ConstDef.SYNCTIME_TYPE_SUBPROJECT)), header);
    	return parseJson(result, IntegerListResponse.class);
    }
    
    public SubProjectListResponse getSyncSubProjects(int csId) {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
//    	String result = HttpUtils.get(BASE_URL + "sync/getSyncSubProjects?csId=" + csId + "&lastSyncTime=" +
//				toURLEncoder(SharePrefOperator.getSingleSharePrefOperator(context).getSyncTime(
//						ConstDef.KEY_SHAREPREF_SUBPROJECT_SYNCTIME)), header);
    	String result = HttpUtils.get(BASE_URL + "sync/getSyncSubProjects?csId=" + csId + "&lastSyncTime=" +
				toURLEncoder(SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context).getSyncTime(
    					ConstDef.SYNCTIME_TYPE_SUBPROJECT)), header);
    	return parseJson(result, SubProjectListResponse.class);
    }
    
    public ContractSectionListResponse getContractSectionlist() {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
    	String result = HttpUtils.get(BASE_URL + "project/getContractSectionlist", header);
    	return parseJson(result, ContractSectionListResponse.class);
    }
    
    public IntegerListResponse getSyncDeleteContractSections() {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
//    	String result = HttpUtils.get(BASE_URL + "sync/getSyncDeleteContractSections?lastSyncTime=" + 
//				toURLEncoder(SharePrefOperator.getSingleSharePrefOperator(context).getSyncTime(
//						ConstDef.KEY_SHAREPREF_CONTRACTSECTION_SYNCTIME)), header);
    	String result = HttpUtils.get(BASE_URL + "sync/getSyncDeleteContractSections?lastSyncTime=" + 
    			toURLEncoder(SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context).getSyncTime(
    					ConstDef.SYNCTIME_TYPE_CONTRACTSECTION)), header);
    	return parseJson(result, IntegerListResponse.class);
    }
    
    public ContractSectionListResponse getSyncContractSections() {
    	Map<String, String> header = new HashMap<String, String>();
    	header.put("TICKET", CacheManager.getTicket());
//    	String result = HttpUtils.get(BASE_URL + "sync/getSyncContractSections?lastSyncTime=" + 
//				toURLEncoder(SharePrefOperator.getSingleSharePrefOperator(context).getSyncTime(
//						ConstDef.KEY_SHAREPREF_CONTRACTSECTION_SYNCTIME)), header);
    	String result = HttpUtils.get(BASE_URL + "sync/getSyncContractSections?lastSyncTime=" + 
    			toURLEncoder(SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context).getSyncTime(
    					ConstDef.SYNCTIME_TYPE_CONTRACTSECTION)), header);
    	return parseJson(result, ContractSectionListResponse.class);
    }

    /**
     * 验证登录是否过期
     */
    public BooleanResponse isTokenExpired(String ticket) {
        String result = HttpUtils.post(BASE_URL + "user/isTicketExpired/" + ticket, null, null);
        return parseJson(result, BooleanResponse.class);
    }

    /**
     * 登录
     *
     * @param userName 用户名 sgd
     * @param userPass  密码 123456 MD5后的密码是：E10ADC3949BA59ABBE56E057F20F883E
     */
    public StringResponse login(String userName, String userPass) {
        String result = HttpUtils.post(BASE_URL + "user/login?userName=" + userName + "&userPass=" + userPass, null, null);
        return parseJson(result, StringResponse.class);
    }
    
    public String getCurrentDateTime() {
    	String time = HttpUtils.get(BASE_URL + "other/getDateTime", null);
    	if (time.length() >= 1)
    		return time.substring(1, time.length()-1);
    	return "";
    }
    
    public String toURLEncoder(String paramString) {
    	if (paramString == null || paramString.equals("")) return "";
    		          
        try  
	    {  
           String str = new String(paramString.getBytes(), "UTF-8");  
           str = URLEncoder.encode(str, "UTF-8");  
           return str;  
	    }  
	    catch (Exception localException)  
        {  

        }  
	    return "";  
    }
}