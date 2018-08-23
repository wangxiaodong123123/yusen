package com.ysdata.grouter.service;

import com.ysdata.grouter.cloud.api.BooleanResponse;
import com.ysdata.grouter.cloud.util.ApiClient;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.UIUtilities;
import com.ysdata.grouter.database.SharePrefOperator;
import com.ysdata.grouter.uart.MyActivityManager;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.view.WindowManager;

public class ListenNetStateService extends Service{
	SharePrefOperator sharePrefOperator;
	ApiClient apiClient;
	private ConnectivityManager connectivityManager;
    private NetworkInfo info;
	Context context;
	boolean isNetConnected = true;
	boolean isStop = false;
	
	@Override
	public void onCreate() {
		context = this;
		sharePrefOperator = SharePrefOperator.getSingleSharePrefOperator(context);
		apiClient = ApiClient.getInstance(context);
		IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
        AppUtil.log("ListenNetStateService============onCreate.");
		start();
		super.onCreate();
	}
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			 if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				 connectivityManager = (ConnectivityManager)      
	                     getSystemService(Context.CONNECTIVITY_SERVICE);
				 info = connectivityManager.getActiveNetworkInfo();  
				 if(info != null && info.isAvailable()) {
					 isNetConnected = true;
	                AppUtil.log("network connected.");
	                if (MyActivityManager.getTopActivity() != null) {
	                	UIUtilities.showToast(MyActivityManager.getTopActivity(), "���������ӣ�");
	                	if (isStop) {
	                		start();
	                	}
	                }
	            } else {
	            	isNetConnected = false;
	            	AppUtil.log("network disconnected.");
	            	createPublicDialog("�����ѶϿ���");
	            	UIUtilities.showToast(MyActivityManager.getTopActivity(), "�����ѶϿ���");
	            }
			 }
		};
	};
	
	@Override
	public void onDestroy() {
		stop();
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
	
	public void start() {
		if (!isStop) {
//			new isExpiredThread().start();
		}
	}
	
	public void stop() {
		isStop = true;
	}
	
	public void createPublicDialog(String msg) {
		if (MyActivityManager.getTopActivity() != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MyActivityManager.getTopActivity());
			builder.setMessage(msg);
			builder.setTitle("��ʾ" );
			builder.setPositiveButton("ȷ��" ,  null );
			AlertDialog ad = builder.create();
			ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			ad.setCanceledOnTouchOutside(false);
			builder.create();
			builder.show();
		}
	}
	
	private final Handler mHandler = new Handler() {
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
				case 1:
					createPublicDialog("�û��Ѿ����ڣ������µ�¼��");
					break;
				case 2:
					createPublicDialog("�����쳣��");
					break;
				case 3:
					createPublicDialog("�����ѶϿ���");
					break;
			 }
		 }
	};
	
	class isExpiredThread extends Thread {
		 @Override
		public void run() {
			String ticket = sharePrefOperator.getAccoutInfo().getTicket();
			BooleanResponse expired_response;
			while(!isStop) {
				expired_response = apiClient.isTokenExpired(ticket);
				if (isNetConnected) {
					if (expired_response != null && expired_response.isSuccess) {
						if (!expired_response.data) {
							AppUtil.log("========IsExpired=========");
							mHandler.sendEmptyMessage(1);
							isStop = true;
						} else {
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				} 
			}
		}
	 }
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}	





