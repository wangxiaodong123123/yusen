package com.ysdata.blender.database;
import java.io.File;
import java.io.IOException;

import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.cloud.util.ConstDef;
import com.ysdata.blender.storage.ExtSdCheck;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class SyncTimeDataBaseAdapter {
	
	private static final String SYNC_TIME_TABLE = "sync_time_table";
	private static final String SYNC_TIME_NAME = "sync_time_name";
	private static final String SYNC_TIME_TIME = "sync_time_time";
	
	private static final int VOLUME_SYNC_TIME_NAME = 0;
	private static final int VOLUME_SYNC_TIME_TIME = 1;
	
	private static final String DATABASE_CREATE_SYNC_TIME_TABLE = "CREATE TABLE IF NOT EXISTS " + SYNC_TIME_TABLE + "("
			+ SYNC_TIME_NAME + " VARCHAR(20),"
			+ SYNC_TIME_TIME + "  VARCHAR(40)"
			+ ");";

    private SQLiteDatabase mDb = null;
    private boolean isOpen = false;
    private Context mContext = null;
    
	private static SyncTimeDataBaseAdapter mDataBaseAdapter = null;
	private SyncTimeDataBaseAdapter(Context context) {
		mContext = context;
	}
	/**
	 * DataBaseAdapter
	 **/
	public static SyncTimeDataBaseAdapter getSingleDataBaseAdapter(Context context) {
		if (mDataBaseAdapter == null)
		{
			synchronized (SyncTimeDataBaseAdapter.class)
			{
				if (mDataBaseAdapter == null)
				{
					mDataBaseAdapter = new SyncTimeDataBaseAdapter(context);
				}
			}
		}
		return mDataBaseAdapter;
	}
	
    /**
     * Open database
     * @return
     * @throws SQLException
     */
    @SuppressLint("SdCardPath")
	public boolean openDb() throws SQLException {//Need about 250ms
    	if(mContext == null) return false;
    	String ExtSDpath = ExtSdCheck.getExtSDCardPath();
    	if (ExtSDpath == null) {
    		if (isOpen) {
    			closeDb();
    		}
    		AppUtil.log( "openDb error-----ExtSDpath = null.");
    		return false;
    	}
    	AppUtil.log( "openDb(syncTimeBase)-----isOpen:"+isOpen);
    	if (!isOpen) {
			String dir = ExtSDpath + ConstDef.DATABASE_PATH;
			AppUtil.log( "open dir:" + dir);
			try{
				File path = new File(dir);
				
				if(!path.exists()){
					path.mkdirs();
				}
				File file = new File(dir+"/sync_time_list.db");
				if(!file.exists()){
					try {
						file.createNewFile();
					} catch (IOException e) {
						AppUtil.log( "creat or open sync_time_list.db failed.exception:" + e);
					}
				}
				mDb = SQLiteDatabase.openOrCreateDatabase(file, null);
				
				try{
					mDb.execSQL(DATABASE_CREATE_SYNC_TIME_TABLE);
				}catch (SQLException e) {
					AppUtil.log( "creat or open sync_time list table failed.exception:" + e);
				}
			}catch (SQLiteException e){
				AppUtil.log( "mkdir "+ dir +" failed.exception:" + e);
				return false;
			}
			
			isOpen = true;
    	}
        return true;
    }
    
    public void saveSyncTime(int synctime_type, String sync_time) {
    	if (!isOpen) return;
    	String sync_name = getSyncTimeName(synctime_type);
    	AppUtil.log("saveSyncTime------sync_name:"+sync_name+" sync_time:"+sync_time);
    	Cursor cursor = mDb.rawQuery("select * from " + SYNC_TIME_TABLE + 
    			" where " +SYNC_TIME_NAME + " like ?", new String[]{sync_name});
    	if (cursor.moveToNext()) {
    		ContentValues values = new ContentValues();
    		values.put(SYNC_TIME_TIME, sync_time);
    		mDb.update(SYNC_TIME_TABLE, values, SYNC_TIME_NAME + " like ?", new String[]{sync_name});
    	} else {
    		mDb.execSQL("REPLACE INTO " + SYNC_TIME_TABLE + " ("
    				+ SYNC_TIME_NAME + ", "
    				+ SYNC_TIME_TIME +") VALUES ("
    				+ "?" + ", ?)",new Object[]{sync_name, sync_time});   
    	}
    	if (cursor != null) cursor.close();
    }
    
    public void clearAllSyncTimeRecord() {
    	AppUtil.log("=========clearAllSyncTimeRecord=========");
    	mDb.delete(SYNC_TIME_TABLE, null, null);
    }
    
    public void clearProjectSyncTime(int csId) {
    	AppUtil.log("clearProjectSyncTime==================csId:"+csId);
    	mDb.delete(SYNC_TIME_TABLE, SYNC_TIME_NAME + " like ?", new String[]{csId+"_"+"%"});
    }
    
    public void clearSubProjectSyncTime(int csId, int subproject_id) {
    	AppUtil.log("clearSubProjectSyncTime==================csId:"+csId+" subproject_id:"+subproject_id);
    	mDb.delete(SYNC_TIME_TABLE, SYNC_TIME_NAME + " like ?", new String[]{csId+"_"+subproject_id+"_"+"%"});
    }
   
    public String getSyncTime(int synctime_type) {
    	if (!isOpen) return null;
    	String sync_name = getSyncTimeName(synctime_type);
    	Cursor cursor = mDb.rawQuery("select * from " + SYNC_TIME_TABLE + 
    			" where " +SYNC_TIME_NAME + " like ?", new String[]{sync_name});
    	String sync_time = "2010-01-01 00:00:00";
    	if (cursor.moveToNext()) {
    		sync_time = cursor.getString(VOLUME_SYNC_TIME_TIME);
    	}
    	AppUtil.log("getSyncTime------sync_name:"+sync_name+" sync_time:"+sync_time);
    	if (cursor != null) cursor.close();
    	return sync_time;
    }
    
    public String getSyncTimeName(int synctime_type) {
    	if (synctime_type == ConstDef.SYNCTIME_TYPE_CONTRACTSECTION) {
    		CacheManager.setDbProjectId(0);
    		CacheManager.setDbSubProjectId(0);
    	} else if (synctime_type == ConstDef.SYNCTIME_TYPE_SUBPROJECT) {
    		CacheManager.setDbSubProjectId(0);
    	}
    	return CacheManager.getDbProjectId() + "_" + 
    			CacheManager.getDbSubProjectId() +"_"+
    			 synctime_type;
    }
    
    /**
     * Close database
     */
    public void closeDb() {
    	AppUtil.log( "closeDb(projdb)====isOpen:"+isOpen);
    	if (isOpen) {
    		if (mDb != null) {
    			mDb.close();
    		}
    		mDb = null;
    		isOpen = false;
    	}
    }
}