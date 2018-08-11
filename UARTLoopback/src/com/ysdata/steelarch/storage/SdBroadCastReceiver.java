package com.ysdata.steelarch.storage;

import java.util.List;







import com.ysdata.steelarch.cloud.util.ConstDef;
import com.ysdata.steelarch.database.ProjectDataBaseAdapter;
import com.ysdata.steelarch.database.ProjectPointDataBaseAdapter;
import com.ysdata.steelarch.uart.MyActivityManager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class SdBroadCastReceiver extends BroadcastReceiver {
    
	private boolean checkYs200IsRunning(Context context) {
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		boolean isAppRunning = false;
		String PKG_NAME = ConstDef.PKG_NAME;
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(PKG_NAME) || 
					info.baseActivity.getPackageName().equals(PKG_NAME)) {
				isAppRunning = true;
				break;
			}
		}
		return isAppRunning;
	}
	
	  @Override
	public void onReceive(Context context, Intent intent) {
		  String action = intent.getAction();  
		  if (checkYs200IsRunning(context)) {
			  if(action.equals(Intent.ACTION_MEDIA_EJECT)) {  
				  ProjectPointDataBaseAdapter mDataBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
			        ProjectDataBaseAdapter mProjDataBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
					mDataBase.closeDb();
					mProjDataBase.closeDb();
					ExtSdCheck.ExtSdCard = null;
					Toast.makeText(context, "��⵽sd���γ������ֳ����޷���������", Toast.LENGTH_LONG).show();
			  } else if(action.equals(Intent.ACTION_MEDIA_MOUNTED)) {  
				  ExtSdCheck.getExtSDCardPath();
				  Toast.makeText(context, "��⵽sd�����룬��ʹ�ø�����Ĺ����У������Ƴ�sd�������򲿷ֹ��ܽ��޷���������", 
						  Toast.LENGTH_LONG).show();
			  }  
		  } else {
			  MyActivityManager.removeALLActivity();
			  Log.d("ys200", "ys200 app has exited.");
		  }
	 }
}
