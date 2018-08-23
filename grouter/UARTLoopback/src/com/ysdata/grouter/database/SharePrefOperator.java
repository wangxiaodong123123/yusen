package com.ysdata.grouter.database;

import com.ysdata.grouter.cloud.util.ConstDef;
import com.ysdata.grouter.element.AccoutInfo;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefOperator {
	
    private Context mContext = null;
	private static SharePrefOperator mSharePrefOperator = null;
	private SharePrefOperator(Context context) {
		mContext = context;
	}
	/**
	 * DataBaseAdapter
	 **/
	public static SharePrefOperator getSingleSharePrefOperator(Context context) {
		if (mSharePrefOperator == null)
		{
			synchronized (SharePrefOperator.class)
			{
				if (mSharePrefOperator == null)
				{
					mSharePrefOperator = new SharePrefOperator(context);
				}
			}
		}
		return mSharePrefOperator;
	}
	
	public void saveMgrMileageNumber(int proj_val, int eng_val, int seq) {
		SharedPreferences mgr_mileage_number = mContext.getSharedPreferences("mgr_mileage_number", Context.MODE_PRIVATE);
		SharedPreferences.Editor shareEditor = mgr_mileage_number.edit();
		String key = proj_val+"-"+eng_val;
		shareEditor.putInt(key, seq);
		shareEditor.commit();
	}
	
	public int getMgrMileageNumber(int proj_val, int eng_val) {
		SharedPreferences mgr_mileage_number = mContext.getSharedPreferences("mgr_mileage_number", Context.MODE_PRIVATE);
		String key = proj_val+"-"+eng_val;
		return mgr_mileage_number.getInt(key, 0);
	}
	
	private void clearSyncTime() {
		SharedPreferences sp_time = mContext.getSharedPreferences("SyncTime", Context.MODE_PRIVATE);
		SharedPreferences.Editor shareEditor = sp_time.edit();
		shareEditor.remove(ConstDef.KEY_SHAREPREF_CONTRACTSECTION_SYNCTIME);
		shareEditor.remove(ConstDef.KEY_SHAREPREF_SUBPROJECT_SYNCTIME);
		shareEditor.remove(ConstDef.KEY_SHAREPREF_SUBPROJECTPOINT_GROUTING_DATA_SYNCTIME);
		shareEditor.remove(ConstDef.KEY_SHAREPREF_SUBPROJECTPOINT_SYNCTIME);
		shareEditor.remove(ConstDef.KEY_SHAREPREF_SUBPROJECTSECTION_SYNCTIME);
		shareEditor.remove(ConstDef.KEY_SHAREPREF_IMAGES_SYNCTIME);
		shareEditor.commit();
	}
	
	public void saveSyncTime(String key, String time) {
		SharedPreferences sp_time = mContext.getSharedPreferences("SyncTime", Context.MODE_PRIVATE);
		SharedPreferences.Editor shareEditor = sp_time.edit();
		shareEditor.remove(key);
		shareEditor.putString(key, time);
		shareEditor.commit();
	}
	
	public String getSyncTime(String key) {
		SharedPreferences sp_time = mContext.getSharedPreferences("SyncTime", Context.MODE_PRIVATE);
		return sp_time.getString(key, "2010-01-01 00:00:00");
	}
	
	public void savePassword(String password) {
		SharedPreferences sp_admin = mContext.getSharedPreferences("admin_password", Context.MODE_PRIVATE);
		
		if (sp_admin.getAll().size() == 0) {
			SharedPreferences.Editor shareEditor = sp_admin.edit();
			shareEditor.putString("password", password);
			shareEditor.commit();
		} else {
			SharedPreferences.Editor shareEditor = sp_admin.edit();
			shareEditor.remove("password");
			shareEditor.putString("password", password);
			shareEditor.commit();
		}
	}
	
	public String getPassword() {
		SharedPreferences sp_admin = mContext.getSharedPreferences("admin_password", Context.MODE_PRIVATE);
		if (sp_admin.getAll().size() == 0) {
			SharedPreferences.Editor shareEditor = sp_admin.edit();
			shareEditor.putString("password", "abc123");
			shareEditor.commit();
		}
		return sp_admin.getString("password", "abc123");
	}
	
	public void saveAccoutInfo(AccoutInfo accoutInfo) {
		SharedPreferences sp_acount = mContext.getSharedPreferences("accout_info", Context.MODE_PRIVATE);
		
		if (sp_acount.getAll().size() == 0) {
			SharedPreferences.Editor shareEditor = sp_acount.edit();
			shareEditor.putString("user", accoutInfo.getUser());
			shareEditor.putString("pwd", accoutInfo.getPwd());
			shareEditor.putString("ticket", accoutInfo.getTicket());
			shareEditor.commit();
		} 
	}
	public void initSharePrefs() {
		clearAccoutInfo();
		clearSyncTime();
	}
	
	public void clearAccoutInfo() {
		SharedPreferences sp_acount = mContext.getSharedPreferences("accout_info", Context.MODE_PRIVATE);
		SharedPreferences.Editor shareEditor = sp_acount.edit();
		if (sp_acount.getAll().size() > 0)
		{
			shareEditor.remove("user");
			shareEditor.remove("pwd");
			shareEditor.remove("ticket");
			shareEditor.commit();
		}
	}
	
	public AccoutInfo getAccoutInfo() {
		SharedPreferences sp_acount = mContext.getSharedPreferences("accout_info", Context.MODE_PRIVATE);
		if (sp_acount.getAll().size() == 0) {
			return null;
		}
		AccoutInfo accoutInfo = new AccoutInfo();
		accoutInfo.setUser(sp_acount.getString("user", ""));
		accoutInfo.setPwd(sp_acount.getString("pwd", ""));
		accoutInfo.setTicket(sp_acount.getString("ticket", ""));
		return accoutInfo;
	}
}
