package com.ysdata.steelarch.activity;


import java.security.NoSuchAlgorithmException;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.cloud.api.BooleanResponse;
import com.ysdata.steelarch.cloud.api.StringResponse;
import com.ysdata.steelarch.cloud.util.ApiClient;
import com.ysdata.steelarch.cloud.util.AppUtil;
import com.ysdata.steelarch.cloud.util.CacheManager;
import com.ysdata.steelarch.cloud.util.EncryptUtils;
import com.ysdata.steelarch.cloud.util.SharedView;
import com.ysdata.steelarch.cloud.util.UIUtilities;
import com.ysdata.steelarch.database.SharePrefOperator;
import com.ysdata.steelarch.element.AccoutInfo;
import com.ysdata.steelarch.service.RemoteDataService;
import com.ysdata.steelarch.uart.MyActivityManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AccountActivity extends Activity{
	Context context;
	EditText usr_et;
	EditText pwd_et;
	Button login_bt;
	LinearLayout login_ly;
	AccoutInfo accoutInfo;
	SharePrefOperator sharePrefOperator;
	ApiClient apiClient;
	RemoteDataService remoteDataService;
	public static AccountActivity accoutActivityInstance;
	String user = "";
	String pwd = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.account_phone);
        context = this;
        accoutActivityInstance = this;
        MyActivityManager.addActivity(AccountActivity.this);
        sharePrefOperator = SharePrefOperator.getSingleSharePrefOperator(context);
        remoteDataService = RemoteDataService.getRemotDataInstance();
        apiClient = ApiClient.getInstance(context);
        accoutInfo = sharePrefOperator.getAccoutInfo();
        login_ly = (LinearLayout) findViewById(R.id.account_login_ly);
        usr_et = (EditText) findViewById(R.id.acount_user);
    	pwd_et = (EditText) findViewById(R.id.acount_pwd);
    	login_bt = (Button) findViewById(R.id.login_bt);
    	login_bt.setOnClickListener(new LoginOnClickListener());
        if (accoutInfo != null) {
        	usr_et.setText(accoutInfo.getUser());
        	pwd_et.setText(accoutInfo.getPwd());
    		SharedView.showProgressBar(AccountActivity.this, "正在验证用户信息......");
    		new UserisExpiredThread().start();
        } 
	}
	
	 private class LoginOnClickListener implements OnClickListener {
		 
		 private AccountActivity mainThis = AccountActivity.this;
		 
		 @Override
		public void onClick(View v) {
			 String usr = usr_et.getText().toString();
			 String pwd = pwd_et.getText().toString();
			 if (remoteDataService.isNetworkConnected(mainThis)) {
				 if (usr.length() == 0 || pwd.length() == 0) {
					  UIUtilities.showToast(mainThis, "用户名或密码不能为空", true);
	             } else {
	            	 String pwd_md5 = "";
	            	 try {
	            		 pwd_md5 = EncryptUtils.md5String(pwd);
					 } catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
						UIUtilities.showToast(mainThis, "你的手机不支持数据加密", true);
						return;
					 }
	            	 SharedView.showProgressBar(mainThis, "正在登陆......");
	            	 new LoginThread(usr, pwd, pwd_md5).start();
	             }
			 } else {
				 UIUtilities.showToast(mainThis, R.string.network_not_detected, true);
			 }
		}
	 }
	 
	 class UserisExpiredThread extends Thread {
		 @Override
		public void run() {
			String ticket = sharePrefOperator.getAccoutInfo().getTicket();
			BooleanResponse expired_response = apiClient.isTokenExpired(ticket);
			if (expired_response != null && expired_response.isSuccess) {
				if (expired_response.data) {
//					mHandler.sendMessage(mHandler.obtainMessage(1, "验证成功"));
					CacheManager.setTicket(ticket);
					CacheManager.setUserName(sharePrefOperator.getAccoutInfo().getUser());
					Intent sIntent = new Intent(AccountActivity.this, CloudsMainActivity.class);
					startActivity(sIntent);
				} else {
					sharePrefOperator.clearAccoutInfo();
					mHandler.sendMessage(mHandler.obtainMessage(2, "用户过期"));
				}
			} else {
				mHandler.sendEmptyMessage(4);
			}
			mHandler.sendEmptyMessage(3);
		}
	 }
	 
	 class LoginThread extends Thread {
		 private String usrName;
		 private String usrPwd;
		 private String usrPwdMd5;
		 public LoginThread(String usr_name, String usr_pwd, String pwd_md5) {
			usrName = usr_name;
			usrPwd = usr_pwd;
			usrPwdMd5 = pwd_md5;
		}
		 @Override
		public void run() {
			 StringResponse login_response = apiClient.login(usrName, usrPwdMd5);
			 AppUtil.printStringResponse(login_response);
			 if (login_response != null && login_response.isSuccess) {
				 sharePrefOperator.saveAccoutInfo(new AccoutInfo(usrName, usrPwd, login_response.getData()));
				 CacheManager.setUserName(usrName);
				 CacheManager.setTicket(login_response.getData());
//				 mHandler.sendMessage(mHandler.obtainMessage(1, "登录成功"));
				 Intent sIntent = new Intent(AccountActivity.this, CloudsMainActivity.class);
				 startActivity(sIntent);
			 } else {
				 mHandler.sendMessage(mHandler.obtainMessage(2, "验证失败"));
			 }
			 mHandler.sendEmptyMessage(3);
		}
	 }
	 
	 private final Handler mHandler = new Handler() {
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
				case 1:
					UIUtilities.showToast(AccountActivity.this, (String)msg.obj, true);
					break;
					
				case 2:
					usr_et.setText("");
					pwd_et.setText("");
					UIUtilities.showToast(AccountActivity.this, (String)msg.obj, true);
					break;
					
				case 3:
					SharedView.cancelProgressBar();
					break;
					
				case 4:
					UIUtilities.showToast(AccountActivity.this, "网络错误", true);
					
			default:
				break;
			}
		 };
	 };
	 
	 public static AccountActivity getAccoutInstance() {
		 return accoutActivityInstance;
	 }
	 
	 public void clearUserText() {
		 usr_et.setText("");
		 pwd_et.setText("");
	 }
	 
	 @Override
	    protected void onDestroy() {
	    	MyActivityManager.removeActivity(AccountActivity.this);
	    	super.onDestroy();
	    }
}
