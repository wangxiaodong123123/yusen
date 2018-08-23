package com.ysdata.grouter.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RemoteDataService {
private static RemoteDataService remoteDataService;
private static final String GET = "GET";
private static final String POST = "POST";
	
	private RemoteDataService() {
		
	}
	
	public static RemoteDataService getRemotDataInstance() {
		if(remoteDataService == null) {
			remoteDataService = new RemoteDataService();
		}
		return remoteDataService;
	}
	
	/**
	 * login 
	 * @param email
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static boolean login(String user, String password) throws Exception {
		String url = "";
		URL urlPath = new URL(url);
		HttpURLConnection httpurlconnection = (HttpURLConnection) urlPath.openConnection();
		String params = "email=" + user + "&password=" + password;
//		String params = "username=" + URLEncoder.encode(user, "UTF-8")  
//				        + "&userpass=" + URLEncoder.encode(password, "UTF-8"); 
		InputStream ins = getStreamFromServer(httpurlconnection, POST ,params);
		return ins == null ? false : true; 
	}
	
	/**
	 * check network
	 * @return
	 * @throws Exception
	 */
	 public boolean isNetworkConnected(Context context) {    
		 if (context != null) {    
			 ConnectivityManager mConnectivityManager = (ConnectivityManager) context    
		       .getSystemService(Context.CONNECTIVITY_SERVICE);    
		     NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();   
		     if (mNetworkInfo != null) {    
		         return mNetworkInfo.isAvailable();    
		     }    
		 }    
		 return false;    
	 } 
	 
	/**
	 * get inputStream from server by the url
	 * @param url 
	 *        params post params
	 * @return
	 * @throws IOException
	 */
	 public static InputStream getStreamFromServer(String urlPath, String method, String parameters) {
		 InputStream in = null;
		 URL url = null;
		 HttpURLConnection httpurlconnection = null;
		try {
			url = new URL(urlPath);
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setConnectTimeout(30000);
			httpurlconnection.setReadTimeout(30000);
		    httpurlconnection.setDoOutput(true);
			httpurlconnection.setDoInput(true);
			httpurlconnection.setRequestMethod(method);
			
			 
			if(parameters != null && !"".equals(parameters)) {
				httpurlconnection.setUseCaches( false);
				httpurlconnection.getOutputStream().write(parameters.getBytes());
				httpurlconnection.getOutputStream().flush();
				httpurlconnection.getOutputStream().close();
			}
			if(httpurlconnection.getResponseCode() == 200) {
				 in = httpurlconnection.getInputStream();
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}
		 
	 public static InputStream getStreamFromServer(HttpURLConnection httpurlconnection, String method, String parameters) {
		 HttpURLConnection conn = httpurlconnection;
		 InputStream in = null;
		 try {
			 conn.setConnectTimeout(15000);
			 conn.setReadTimeout(15000);
			 //set request head
			 conn.setDoOutput(true);
			 conn.setDoInput(true);
			 conn.setRequestMethod(method);
			 
			 if(parameters != null && !"".equals(parameters)) {
				 conn.getOutputStream().write(parameters.getBytes());
				 conn.getOutputStream().flush();
				 conn.getOutputStream().close();
	    	 }
			 if(conn.getResponseCode() == 200) {
				 in = httpurlconnection.getInputStream();
			 } 
		 } catch (Exception e) {
			e.printStackTrace();
		 }
		 return in;
	}	 
	 
	/**
	 * convert stream to bytes
	 * @param stream
	 * @return
	 * @throws Exception
	 */
	protected static String streamToString(InputStream stream) throws Exception {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[128];
		for (int n; (n = stream.read(b)) != -1;) {
		    out.append(new String(b, 0, n));
		}
		stream.close();
		return out.toString();
	} 
}






