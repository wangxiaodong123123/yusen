package com.ysdata.steelarch.uart;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class MyActivityManager {
	private static List<Activity> oList = new ArrayList<Activity>();//���ڴ������������Activity�ļ���

	/**
	* ���Activity
	*/
	public static void addActivity(Activity activity) {
		// �жϵ�ǰ�����в����ڸ�Activity
		if (!oList.contains(activity)) {
			oList.add(activity);//�ѵ�ǰActivity��ӵ�������
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
	* ���ٵ���Activity
	*/
	public static void removeActivity(Activity activity) {
		//�жϵ�ǰ�����д��ڸ�Activity
		if (oList.contains(activity)) {
		    oList.remove(activity);//�Ӽ������Ƴ�
		    activity.finish();//���ٵ�ǰActivity
		}
	}
	
	/**
	* �������е�Activity
	*/
	public static void removeALLActivity() {
	     //ͨ��ѭ�����Ѽ����е�����Activity����
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
