package com.ysdata.steelarch.uart;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class MyActivityManager {
	private static List<Activity> oList = new ArrayList<Activity>();//用于存放所有启动的Activity的集合

	/**
	* 添加Activity
	*/
	public static void addActivity(Activity activity) {
		// 判断当前集合中不存在该Activity
		if (!oList.contains(activity)) {
			oList.add(activity);//把当前Activity添加到集合中
		}
	}

	public static Activity getTopActivity() {
		int size = oList.size();
		if (size > 0) {
			return oList.get(size-1);
		}
		return null;
	}
	
	/**
	* 销毁单个Activity
	*/
	public static void removeActivity(Activity activity) {
		//判断当前集合中存在该Activity
		if (oList.contains(activity)) {
		    oList.remove(activity);//从集合中移除
		    activity.finish();//销毁当前Activity
		}
	}
	
	/**
	* 销毁所有的Activity
	*/
	public static void removeALLActivity() {
	     //通过循环，把集合中的所有Activity销毁
		int size = oList.size();
		for (int i = size-1; i >= 0; i--)
		{
			oList.get(i).finish();
		}
		/*for (Activity activity : oList) {
		     activity.finish();
		}*/
	}
}
