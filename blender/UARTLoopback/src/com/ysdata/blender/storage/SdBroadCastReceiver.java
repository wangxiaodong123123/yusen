package com.ysdata.blender.storage;

import java.util.List;







import com.ysdata.blender.cloud.util.ConstDef;
import com.ysdata.blender.database.ProjectDataBaseAdapter;
import com.ysdata.blender.database.ProjectPointDataBaseAdapter;
import com.ysdata.blender.uart.MyActivityManager;

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
					Toast.makeText(context, "检测到sd卡拔出，部分程序将无法正常运行", Toast.LENGTH_LONG).show();
			  } else if(action.equals(Intent.ACTION_MEDIA_MOUNTED)) {  
				  ExtSdCheck.getExtSDCardPath();
				  Toast.makeText(context, "检测到sd卡插入，在使用该软件的过程中，请勿移除sd卡，否则部分功能将无法正常运行", 
						  Toast.LENGTH_LONG).show();
			  }  
		  } else {
			  MyActivityManager.removeALLActivity();
			  Log.d("ys200", "ys200 app has exited.");
		  }
	 }
}
