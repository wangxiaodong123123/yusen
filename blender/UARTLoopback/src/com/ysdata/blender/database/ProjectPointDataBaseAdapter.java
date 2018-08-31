package com.ysdata.blender.database;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.ConstDef;
import com.ysdata.blender.element.DdBlenderDataParameter;
import com.ysdata.blender.element.DdMixParameter;
import com.ysdata.blender.element.ManagerCraftParameter;
import com.ysdata.blender.element.MixCraftParameter;
import com.ysdata.blender.element.MixUploadState;
import com.ysdata.blender.storage.ExtSdCheck;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class ProjectPointDataBaseAdapter {
	private static final String MIX_DOWNLOAD_TABLE = "stir_download";
	private static final String MIX_DOWNLOAD_ID = "id";
	private static final String MIX_DOWNLOAD_RATIO = "ratio";
	private static final String MIX_DOWNLOAD_CREATE_TIME = "create_time";
	
	private static final int VOLUME_MIX_DOWNLOAD_ID = 0;
	private static final int VOLUME_MIX_DOWNLOAD_RATIO = 1;
	private static final int VOLUME_MIX_DOWNLOAD_CREATE_TIME = 2;
	
	private static final String MIX_COLLECT_TABLE = "stir_collect";
	private static final String MIX_COLLECT_ID = "id";
	private static final String MIX_COLLECT_RATIO = "ratio";
	private static final String MIX_COLLECT_DATE = "date";
	private static final String MIX_COLLECT_START_TIME = "start_time";
	private static final String MIX_COLLECT_END_TIME = "end_time";
	private static final String MIX_COLLECT_CEMENT_WEIGHT = "cement_weight";
	private static final String MIX_COLLECT_POSITION = "position";
	private static final String MIX_COLLECT_PIC_RESULT = "pic_result";
	private static final String MIX_COLLECT_PIC_SCENE = "pic_scene";
	private static final String MIX_COLLECT_ORDERNO = "order_no";
	
	private static final int VOLUME_MIX_COLLECT_ID = 0;
	private static final int VOLUME_MIX_COLLECT_RATIO = 1;
	private static final int VOLUME_MIX_COLLECT_DATE = 2;
	private static final int VOLUME_MIX_COLLECT_START_TIME = 3;
	private static final int VOLUME_MIX_COLLECT_END_TIME = 4;
	private static final int VOLUME_MIX_COLLECT_CEMENT_WEIGHT = 5;
	private static final int VOLUME_MIX_COLLECT_POSITION = 6;
	private static final int VOLUME_MIX_COLLECT_PIC_RESULT = 7;
	private static final int VOLUME_MIX_COLLECT_PIC_SCENE = 8;
	private static final int VOLUME_MIX_COLLECT_ORDERNO = 9;
	
	private static final String DATABASE_CREATE_MIX_DOWNLOAD_TABLE = "CREATE TABLE IF NOT EXISTS " + MIX_DOWNLOAD_TABLE + "("
			+ MIX_DOWNLOAD_ID + " INTEGER PRIMARY KEY,"
			+ MIX_DOWNLOAD_RATIO + " DOUBLE," 
			+ MIX_DOWNLOAD_CREATE_TIME + " VARCHAR(50)"
			+ ");";
	
	private static final String DATABASE_CREATE_MIX_COLLECT_TABLE = "CREATE TABLE IF NOT EXISTS " + MIX_COLLECT_TABLE + "("
			+ MIX_COLLECT_ID + " INTEGER PRIMARY KEY,"
			+ MIX_COLLECT_RATIO + " DOUBLE," 
			+ MIX_COLLECT_DATE + " VARCHAR(50),"
			+ MIX_COLLECT_START_TIME + " VARCHAR(50),"
			+ MIX_COLLECT_END_TIME + " VARCHAR(50),"
			+ MIX_COLLECT_CEMENT_WEIGHT + " DOUBLE,"
			+ MIX_COLLECT_POSITION + " DOUBLE,"
			+ MIX_COLLECT_PIC_RESULT + " TEXT,"
			+ MIX_COLLECT_PIC_SCENE + " TEXT,"
			+ MIX_COLLECT_ORDERNO + " INTEGER"
			+ ");";

    private SQLiteDatabase mDb = null;
    private boolean isOpen = false;
    private Context mContext = null;
    
	private static ProjectPointDataBaseAdapter mDataBaseAdapter = null;
	private ProjectPointDataBaseAdapter(Context context) {
		mContext = context;
	}
	/**
	 * DataBaseAdapter
	 **/
	public static ProjectPointDataBaseAdapter getSingleDataBaseAdapter(Context context) {
		if (mDataBaseAdapter == null)
		{
			synchronized (ProjectPointDataBaseAdapter.class)
			{
				if (mDataBaseAdapter == null)
				{
					mDataBaseAdapter = new ProjectPointDataBaseAdapter(context);
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
	public boolean openDb(int project_id, int subproject_id) throws SQLException {//Need about 250ms
    	if(mContext == null) return false;
    	String ExtSDpath = ExtSdCheck.getExtSDCardPath();
    	if (ExtSDpath == null) {
    		if (isOpen) {
    			closeDb();
    		}
    		AppUtil.log( "openDb error-----ExtSDpath = null.");
    		return false;
    	}
    	if (!isOpen) {
			String dir = ExtSDpath + ConstDef.DATABASE_PATH+project_id+"/"+subproject_id;
			int dbVer = 0;
			AppUtil.log( "open dir:" + dir);
			try{
				File path = new File(dir);
				
				if(!path.exists()){
					path.mkdirs();
				}
				File file = new File(dir+"/YS200.db");
				if(!file.exists()){
					try {
						file.createNewFile();
					} catch (IOException e) {
						AppUtil.log( "creat or open ys200.db failed.exception:" + e);
					}
					mDb = SQLiteDatabase.openOrCreateDatabase(file, null);
					mDb.setVersion(ConstDef.DB_VERSION);
					try{
						mDb.execSQL(DATABASE_CREATE_MIX_DOWNLOAD_TABLE);
					}catch (SQLException e) {
						AppUtil.log( "creat or open craft mix_download table failed.exception:" + e);
					}
					try{
						mDb.execSQL(DATABASE_CREATE_MIX_COLLECT_TABLE);
					}catch (SQLException e) {
						AppUtil.log( "creat or open craft mix_collect table failed.exception:" + e);
					}
				} else {
					mDb = SQLiteDatabase.openOrCreateDatabase(file, null);
					dbVer = mDb.getVersion();
					if (dbVer < ConstDef.DB_FIRST_VERSION) {
						isOpen = false;
						AppUtil.log("database version is too low to use.");
						return false;
					}
					if (dbVer < ConstDef.DB_VERSION) {
						
					}
				}
				
			}catch (SQLiteException e) {
				AppUtil.log( "mkdir '/sdcard/ang/database' failed.exception:" + e);
				return false;
			}
			
			isOpen = true;
    	}
        return true;
    }

    /**
     * Close database
     */
    public void closeDb() {
    	AppUtil.log( "closeDb====isOpen:"+isOpen);
    	if (isOpen) {
    		if (mDb != null) {
    			mDb.close();
    		}
    		mDb = null;
    		isOpen = false;
    	}
    }
    
    public void insertMixDownloadRecord(int id, double ratio, String create_time) {
    	if (!isOpen) return;
    	mDb.execSQL("REPLACE INTO " + MIX_DOWNLOAD_TABLE + " ("
				+ MIX_DOWNLOAD_ID + ", "
				+ MIX_DOWNLOAD_RATIO + ", "
				+ MIX_DOWNLOAD_CREATE_TIME + 
				") VALUES ("+ id + ","
				+ ratio +  ",? " +
				 ")", new Object[]{create_time}); 
    }
    
    public void getDdMixList(List<DdMixParameter> list) {
    	if (!isOpen) return;
    	if (list != null) {
    		list.clear();
    		Cursor cursor =  mDb.rawQuery("select * from " + MIX_DOWNLOAD_TABLE, null);
    		while(cursor.moveToNext()) {
    			list.add(new DdMixParameter(cursor.getString(VOLUME_MIX_DOWNLOAD_CREATE_TIME), 
    					cursor.getDouble(VOLUME_MIX_DOWNLOAD_RATIO)));
    		}
    		if (cursor != null) cursor.close();
    	}
    }
    
    public void getDdMixRatioList(List<String> list) {
    	if (!isOpen) return;
    	if (list != null) {
    		list.clear();
    		Cursor cursor =  mDb.rawQuery("select * from " + MIX_DOWNLOAD_TABLE, null);
    		while(cursor.moveToNext()) {
    			list.add(cursor.getDouble(VOLUME_MIX_DOWNLOAD_RATIO)+"");
    		}
    		if (cursor != null) cursor.close();
    	}
    }
    
    public void clearMixDownloadRecord() {
    	if (!isOpen) return;
		mDb.delete(MIX_DOWNLOAD_TABLE, null, null);
    }
    
    public String getUploadEndMixDate() {
    	String end_mix_date = "";
    	if (!isOpen) return end_mix_date;
    	
    	return end_mix_date;
    }
    
    public int getMixOrderNoLikeDate(String mixdate) {
    	String date = "";
    	String start_time = "";
    	int order_no = 0;
    	if (mixdate.contains(" ")) {
    		String strs[] = mixdate.split(" ");
    		if (strs.length == 2) {
    			date = strs[0];
    			start_time = strs[1];
    			Cursor cursor = mDb.rawQuery("select "+MIX_COLLECT_ORDERNO+" from " + MIX_COLLECT_TABLE + 
    	    			" where " +MIX_COLLECT_DATE+" like ? and " + 
    					MIX_COLLECT_START_TIME+" like ?", new String[]{date, start_time});
    			if (cursor.moveToNext()) {
    				order_no = cursor.getInt(0);
    			}
    			if (cursor != null) cursor.close();
    		}
    	}
    	return order_no;
    }
    
    public int getMixOrderNoLikeId(String id) {
    	int order_no = 0;
    	Cursor cursor = mDb.rawQuery("select " + MIX_COLLECT_ORDERNO + " from " + MIX_COLLECT_TABLE + 
    			" where " + MIX_COLLECT_ID + " = " + id, null); 
    	if (cursor.moveToNext()) {
    		order_no = cursor.getInt(0);
    	}
    	if (cursor != null) cursor.close();
    	return order_no;
    }
    
    public String getMixDateWhereOrderNo(int order) {
    	String mixdate = "";
    	Cursor cursor = mDb.rawQuery("select * from " + MIX_COLLECT_TABLE + 
    			" where " + MIX_COLLECT_ORDERNO + " = " + order, null); 
    	if (cursor.moveToNext()) {
    		mixdate = cursor.getString(VOLUME_MIX_COLLECT_DATE) + " " + cursor.getString(VOLUME_MIX_COLLECT_START_TIME);
    	}
    	if (cursor != null) cursor.close();
    	return mixdate;
    }
    
    public ArrayList<MixUploadState> getMixDateUploadStateList() {
    	ArrayList<MixUploadState> list = new ArrayList<MixUploadState>();
    	Cursor cursor = mDb.rawQuery("select * from " + MIX_COLLECT_TABLE, null);  
    	while(cursor.moveToNext()) {
    		list.add(new MixUploadState(cursor.getInt(VOLUME_MIX_COLLECT_ID),
    				cursor.getString(VOLUME_MIX_COLLECT_DATE) + " " + 
    				cursor.getString(VOLUME_MIX_COLLECT_START_TIME), 0));
    	}
    	if (cursor != null) cursor.close();
    	return list;
    }
    
    public int getMixDataTotalCount() {
    	Cursor cursor = mDb.rawQuery("select * from " + MIX_COLLECT_TABLE, null);  
    	int count = 0;
    	if (cursor != null)
    		count = cursor.getCount();  
    	if (cursor != null)
    		cursor.close();  
    	return count; 
    }
    
    public int getMixCollectCount() {
    	Cursor cursor = mDb.rawQuery("select * from " + MIX_COLLECT_TABLE, null);  
    	int count = 0;
    	if (cursor != null)
    		count = cursor.getCount();  
    	if (cursor != null)
    		cursor.close();  
    	return count; 
    }
    
    public MixCraftParameter getMixCraftParameter(int orderno) {
    	MixCraftParameter parameter = null;
    	if (!isOpen) return null;
    	Cursor cursor =  mDb.rawQuery("select * from " + MIX_COLLECT_TABLE + 
    			" where " + MIX_COLLECT_ORDERNO + "=" + orderno, null);
		if(cursor.moveToNext()) {
			int id = cursor.getInt(VOLUME_MIX_COLLECT_ID);
			double mix_ratio = cursor.getDouble(VOLUME_MIX_COLLECT_RATIO);
			String mix_date = cursor.getString(VOLUME_MIX_COLLECT_DATE);
			String start_time = cursor.getString(VOLUME_MIX_COLLECT_START_TIME);
			String end_time = cursor.getString(VOLUME_MIX_COLLECT_END_TIME);
			double cement_weight = cursor.getDouble(VOLUME_MIX_COLLECT_CEMENT_WEIGHT);
			double position = cursor.getDouble(VOLUME_MIX_COLLECT_POSITION);
			parameter = new MixCraftParameter(id, mix_ratio, mix_date, start_time, end_time, 
					cement_weight, position);
		}
		if (cursor != null) cursor.close();
		return parameter;
    }
    
    public void deleteAllBlenderDataRecord() {
    	if (!isOpen) return;
		mDb.delete(MIX_COLLECT_TABLE, null, null);
    }
    
    public MixCraftParameter getMixCraftParameterByMixId(int mix_id) {
    	MixCraftParameter parameter = null;
    	if (!isOpen) return null;
    	Cursor cursor =  mDb.rawQuery("select * from " + MIX_COLLECT_TABLE + 
    			" where " + MIX_COLLECT_ID + "=" + mix_id, null);
		if(cursor.moveToNext()) {
			int id = cursor.getInt(VOLUME_MIX_COLLECT_ID);
			double mix_ratio = cursor.getDouble(VOLUME_MIX_COLLECT_RATIO);
			String mix_date = cursor.getString(VOLUME_MIX_COLLECT_DATE);
			String start_time = cursor.getString(VOLUME_MIX_COLLECT_START_TIME);
			String end_time = cursor.getString(VOLUME_MIX_COLLECT_END_TIME);
			double cement_weight = cursor.getDouble(VOLUME_MIX_COLLECT_CEMENT_WEIGHT);
			double position = cursor.getDouble(VOLUME_MIX_COLLECT_POSITION);
			parameter = new MixCraftParameter(id, mix_ratio, mix_date, start_time, end_time, 
					cement_weight, position);
		}
		if (cursor != null) cursor.close();
		return parameter;
    }
    
    private int stringTimeToSeconds(String time) {
    	if (time.contains(":")) {
    		String[] strs = time.split(":");
    		if (strs.length == 2)
    			return Integer.parseInt(strs[0])*60 + Integer.parseInt(strs[1]);
    		
    	} 
    	return 0;
    }
    
    public void getManagerCraftParameterList(List<ManagerCraftParameter> list) {
    	list.clear();
    	Cursor cursor =  mDb.rawQuery("select * from " + MIX_COLLECT_TABLE+ " order by " 
    						+ MIX_COLLECT_ID + " asc", null);
    	while (cursor.moveToNext()) {
    		int id = cursor.getInt(VOLUME_MIX_COLLECT_ID);
    		String start_time = cursor.getString(VOLUME_MIX_COLLECT_DATE) + " " + 
    				cursor.getString(VOLUME_MIX_COLLECT_START_TIME);
    		int startTimeSeconds = stringTimeToSeconds(cursor.getString(VOLUME_MIX_COLLECT_START_TIME));
    		int endTimeSeconds = stringTimeToSeconds(cursor.getString(VOLUME_MIX_COLLECT_END_TIME));
    		int blender_time = 0;
    		if (endTimeSeconds >= startTimeSeconds) {
    			blender_time = endTimeSeconds - startTimeSeconds;
    		} else {
    			blender_time = 24 * 60 + endTimeSeconds - startTimeSeconds;
    		}
    		double mix_ratio = cursor.getDouble(VOLUME_MIX_COLLECT_RATIO);
    		double cement_weight = (int)cursor.getDouble(VOLUME_MIX_COLLECT_CEMENT_WEIGHT);
    		double water_weight = (int)(cement_weight * mix_ratio + 0.5);
    		double weight = (int)(cement_weight + water_weight);
    		list.add(new ManagerCraftParameter(id, start_time, blender_time, cement_weight, water_weight, weight));
    	}
    	if (cursor != null) cursor.close();
    }
    
    public void getDdBlenderDataList(List<DdBlenderDataParameter> list) {
    	list.clear();
    	Cursor cursor =  mDb.rawQuery("select * from " + MIX_COLLECT_TABLE+ " order by " 
    						+ MIX_COLLECT_ID + " asc", null);
    	while (cursor.moveToNext()) {
    		int id = cursor.getInt(VOLUME_MIX_COLLECT_ID);
    		String start_time = cursor.getString(VOLUME_MIX_COLLECT_DATE) + " " + 
    				cursor.getString(VOLUME_MIX_COLLECT_START_TIME);
    		int startTimeSeconds = stringTimeToSeconds(cursor.getString(VOLUME_MIX_COLLECT_START_TIME));
    		int endTimeSeconds = stringTimeToSeconds(cursor.getString(VOLUME_MIX_COLLECT_END_TIME));
    		int blender_time = 0;
    		if (endTimeSeconds >= startTimeSeconds) {
    			blender_time = endTimeSeconds - startTimeSeconds;
    		} else {
    			blender_time = 24 * 60 + endTimeSeconds - startTimeSeconds;
    		}
    		double mix_ratio = cursor.getDouble(VOLUME_MIX_COLLECT_RATIO);
    		double cement_weight = (int)cursor.getDouble(VOLUME_MIX_COLLECT_CEMENT_WEIGHT);
    		double water_weight = (int)(cement_weight * mix_ratio + 0.5);
    		double weight = (int)(cement_weight + water_weight);
    		list.add(new DdBlenderDataParameter(id, start_time, blender_time, cement_weight, water_weight, weight));
    	}
    	if (cursor != null) cursor.close();
    }
    
    public String getMixResultPicString(int id) {
    	String pic_string = "";
    	Cursor cursor = mDb.rawQuery("select "+ MIX_COLLECT_PIC_RESULT + " from " + MIX_COLLECT_TABLE + 
    			" where " + MIX_COLLECT_ID + "=" + id, null);
    	if (cursor.moveToNext()) {
    		pic_string = cursor.getString(0);
    		cursor.close();
    	}
//    	AppUtil.log("getMixResultPicString=====id:"+id);
//    	if (pic_string != null)
//    		AppUtil.log(pic_string);
    	return pic_string;
    }
    
    public String getMixScenePicString(int id) {
    	String pic_string = "";
    	Cursor cursor = mDb.rawQuery("select "+ MIX_COLLECT_PIC_SCENE + " from " + MIX_COLLECT_TABLE + 
    			" where " + MIX_COLLECT_ID + "=" + id, null);
    	if (cursor.moveToNext()) {
    		pic_string = cursor.getString(0);
    		cursor.close();
    	}
//    	AppUtil.log("getMixScenePicString=====id:"+id);
//    	if (pic_string != null)
//    		AppUtil.log(pic_string);
    	return pic_string;
    }
    
    public void insertMixCollectRecord(int id, double ratio, String date, String start_time,
    		String end_time, double cement_weight, double position) {
    	if (!isOpen) return;
    	mDb.execSQL("REPLACE INTO " + MIX_COLLECT_TABLE + " ("
				+ MIX_COLLECT_ID + ", "
				+ MIX_COLLECT_RATIO + ", "
				+ MIX_COLLECT_DATE + ", "
				+ MIX_COLLECT_START_TIME + ","
				+ MIX_COLLECT_END_TIME + ","
				+ MIX_COLLECT_CEMENT_WEIGHT + ", "
				+ MIX_COLLECT_POSITION + 
				") VALUES ("+ id + ","
				+ ratio +  ",? " + ",? "
				+ ",? " + "," + cement_weight + ","
				+ position + 
				 ")", new Object[]{date, start_time, end_time}); 
    }
    
    public void setCollectOrderNo() {
    	if (!isOpen) return;
    	Cursor cursor = mDb.rawQuery("select " + MIX_COLLECT_ID + " from " + 
    			MIX_COLLECT_TABLE + " order by " + MIX_COLLECT_ID + " asc" , null);
    	int orderno = 1;
    	mDb.beginTransaction();
    	AppUtil.log("===========setCollectOrderNo===============");
    	ContentValues value = new ContentValues();
    	while (cursor.moveToNext()) {
    		value.put(MIX_COLLECT_ORDERNO, orderno);
    		mDb.update(MIX_COLLECT_TABLE, value, MIX_COLLECT_ID + "=" + cursor.getInt(0), null);
    		orderno++;
    	} 
    	AppUtil.log("===========setCollectOrderNo===============");
    	mDb.setTransactionSuccessful(); 
        mDb.endTransaction(); 
    	if (cursor != null) {
    		cursor.close();
    	}
    }
    
    public int checkLastMixCollectId() {
    	int id = 0;
    	Cursor cursor = mDb.rawQuery("select " + MIX_COLLECT_ID + " from " + 
    			MIX_COLLECT_TABLE + " order by " + MIX_COLLECT_ID + " desc limit 0, 1" , null);
    	if (cursor.moveToNext()) {
    		id = cursor.getInt(0);
    		cursor.close();
    	}
    	return id;
    }
    
    public void updateMixCollectPicResult(int id, String pic_string) {
    	if (!isOpen) return;
    	ContentValues value = new ContentValues();
    	AppUtil.log("=========updateMixCollectPicResult=========");
    	AppUtil.log("pic_string:");
    	AppUtil.log(pic_string);
    	AppUtil.log("=========updateMixCollectPicResult=========");
    	value.put(MIX_COLLECT_PIC_RESULT, pic_string);
    	mDb.update(MIX_COLLECT_TABLE, value, MIX_COLLECT_ID + "=" + id, null);
    }
    
    public void updateMixCollectPicScene(int id, String pic_string) {
    	if (!isOpen) return;
    	ContentValues value = new ContentValues();
    	AppUtil.log("=========updateMixCollectPicScene=========");
    	AppUtil.log("pic_string:");
    	AppUtil.log(pic_string);
    	AppUtil.log("=========updateMixCollectPicScene=========");
    	value.put(MIX_COLLECT_PIC_SCENE, pic_string);
    	mDb.update(MIX_COLLECT_TABLE, value, MIX_COLLECT_ID + "=" + id, null);
    }
}