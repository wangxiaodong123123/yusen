package com.ysdata.steelarch.database;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ysdata.steelarch.cloud.util.AppUtil;
import com.ysdata.steelarch.cloud.util.ConstDef;
import com.ysdata.steelarch.element.DdSteelArchDataParameter;
import com.ysdata.steelarch.element.ManagerCraftParameter;
import com.ysdata.steelarch.element.MgrStasticParameter;
import com.ysdata.steelarch.element.MixCraftParameter;
import com.ysdata.steelarch.element.MixUploadState;
import com.ysdata.steelarch.element.SteelArchCollectParameter;
import com.ysdata.steelarch.element.SteelArchCraftParameter;
import com.ysdata.steelarch.element.SteelArchDetailParameter;
import com.ysdata.steelarch.storage.ExtSdCheck;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class ProjectPointDataBaseAdapter {	
	private static final String STEELARCH_DATA_TABLE = "steelarch_data";
	private static final String STEELARCH_ORDERNO = "orderno";
	private static final String STEELARCH_ID = "id";
	private static final String STEELARCH_NAME = "name";
	private static final String NAME_TO_METER = "name_to_meter";
	private static final String CRAFT_STEELARCH_TO_STEELARCH_DISTANCE = "craft_steelarch_to_steelarch_distance";
	private static final String CRAFT_STEELARCH_TO_ENTRANCE_DISTANCE = "steelarch_to_entrance_distance";
	private static final String CRAFT_SECOND_CAR_WIDTH = "second_car_width";
	private static final String COLLECT_LEFT_MEASURE_DATE = "left_measure_date";
	private static final String COLLECT_RIGHT_MEASURE_DATE = "right_measure_date";	
	private static final String COLLECT_LEFT_STEELARCH_TO_STEELARCH_DISTANCE = "left_steelarch_to_steelarch_distance";
	private static final String COLLECT_RIGHT_STEELARCH_TO_STEELARCH_DISTANCE = "right_steelarch_to_steelarch_distance";
	private static final String COLLECT_LEFT_STEELARCH_TO_TUNNELFACE_DISTANCE = "left_steelarch_to_tunnelface_distance";
	private static final String COLLECT_RIGHT_STEELARCH_TO_TUNNELFACE_DISTANCE = "right_steelarch_to_tunnelface_distance";
	private static final String COLLECT_LEFT_STEELARCH_TO_SECONDCAR_DISTANCE = "left_steelarch_to_secondcar_distance";
	private static final String COLLECT_RIGHT_STEELARCH_TO_SECONDCAR_DISTANCE = "right_steelarch_to_secondcar_distance";
	private static final String COLLECT_LEFT_PIC_DIR_ENTRANCE = "left_pic_dir_entrance";
	private static final String COLLECT_RIGHT_PIC_DIR_ENTRANCE = "right_pic_dir_entrance";
	private static final String COLLECT_LEFT_PIC_DIR_TUNNELFACE = "left_pic_dir_tunnelface";
	private static final String COLLECT_RIGHT_PIC_DIR_TUNNELFACE = "right_pic_dir_tunnelface";		
	
	private static final int VOLUME_STEELARCH_ID = 0;
	private static final int VOLUME_STEELARCH_ORDERNO = 1;
	private static final int VOLUME_STEELARCH_NAME = 2;
	private static final int VOLUME_NAME_TO_METER = 3;
	private static final int VOLUME_CRAFT_STEELARCH_TO_STEELARCH_DISTANCE = 4;
	private static final int VOLUME_CRAFT_STEELARCH_TO_ENTRANCE_DISTANCE = 5;
	private static final int VOLUME_CRAFT_SECOND_CAR_WIDTH = 6;
	private static final int VOLUME_COLLECT_LEFT_MEASURE_DATE = 7;
	private static final int VOLUME_COLLECT_RIGHT_MEASURE_DATE = 8;	
	private static final int VOLUME_COLLECT_LEFT_STEELARCH_TO_STEELARCH_DISTANCE = 9;
	private static final int VOLUME_COLLECT_RIGHT_STEELARCH_TO_STEELARCH_DISTANCE = 10;
	private static final int VOLUME_COLLECT_LEFT_STEELARCH_TO_TUNNELFACE_DISTANCE = 11;
	private static final int VOLUME_COLLECT_RIGHT_STEELARCH_TO_TUNNELFACE_DISTANCE = 12;
	private static final int VOLUME_COLLECT_LEFT_SECONDCAR_TO_STEELARCH_DISTANCE = 13;
	private static final int VOLUME_COLLECT_RIGHT_SECONDCAR_TO_STEELARCH_DISTANCE = 14;
	private static final int VOLUME_COLLECT_LEFT_PIC_DIR_ENTRANCE = 15;
	private static final int VOLUME_COLLECT_RIGHT_PIC_DIR_ENTRANCE = 16;
	private static final int VOLUME_COLLECT_LEFT_PIC_DIR_TUNNELFACE = 17;
	private static final int VOLUME_COLLECT_RIGHT_PIC_DIR_TUNNELFACE = 18;	
	
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
	
	private static final String DATABASE_CREATE_STEELARCH_TABLE = "CREATE TABLE IF NOT EXISTS " + STEELARCH_DATA_TABLE + "("
			+ STEELARCH_ID + " INTEGER PRIMARY KEY,"
			+ STEELARCH_ORDERNO + " INTEGER," 
			+ STEELARCH_NAME + " VARCHAR(50),"
			+ NAME_TO_METER + " DOUBLE,"
			+ CRAFT_STEELARCH_TO_STEELARCH_DISTANCE + " DOUBLE,"
			+ CRAFT_STEELARCH_TO_ENTRANCE_DISTANCE + " DOUBLE,"
			+ CRAFT_SECOND_CAR_WIDTH + " DOUBLE,"
			+ COLLECT_LEFT_MEASURE_DATE + " DOUBLE,"
			+ COLLECT_RIGHT_MEASURE_DATE + " DOUBLE,"
			+ COLLECT_LEFT_STEELARCH_TO_STEELARCH_DISTANCE + " DOUBLE,"
			+ COLLECT_RIGHT_STEELARCH_TO_STEELARCH_DISTANCE + " DOUBLE,"
			+ COLLECT_LEFT_STEELARCH_TO_TUNNELFACE_DISTANCE + " DOUBLE,"
			+ COLLECT_RIGHT_STEELARCH_TO_TUNNELFACE_DISTANCE + " DOUBLE,"
			+ COLLECT_LEFT_STEELARCH_TO_SECONDCAR_DISTANCE + " DOUBLE,"
			+ COLLECT_RIGHT_STEELARCH_TO_SECONDCAR_DISTANCE + " DOUBLE,"
			+ COLLECT_LEFT_PIC_DIR_ENTRANCE + " TEXT,"
			+ COLLECT_RIGHT_PIC_DIR_ENTRANCE + " TEXT,"
			+ COLLECT_LEFT_PIC_DIR_TUNNELFACE + " TEXT,"
			+ COLLECT_RIGHT_PIC_DIR_TUNNELFACE + " TEXT"
			+ ");";
	
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
						mDb.execSQL(DATABASE_CREATE_STEELARCH_TABLE);
					}catch (SQLException e) {
						AppUtil.log( "creat or open steelarch table failed.exception:" + e);
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
				AppUtil.log( "mkdir database failed.exception:" + e);
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
    
    public void insertSteelArchCraftDataRecord(int id, String name, double nameMeter, 
    		double steelarch_to_steelarch_distance, double steelarch_to_entrance_distance,
    		double secondcar_width) {
    	if (!isOpen) return;
    	mDb.execSQL("REPLACE INTO " + STEELARCH_DATA_TABLE + " ("
				+ STEELARCH_ID + ", "
				+ STEELARCH_NAME + ", "
				+ NAME_TO_METER + ", "
				+ CRAFT_STEELARCH_TO_STEELARCH_DISTANCE + ", "
				+ CRAFT_STEELARCH_TO_ENTRANCE_DISTANCE + ", "
				+ CRAFT_SECOND_CAR_WIDTH 
				+ ") VALUES ("+ id + ",?,"
				+ nameMeter + "," + steelarch_to_steelarch_distance
				+ "," + steelarch_to_entrance_distance
				+ "," + secondcar_width 
				+ ")", new Object[]{name}); 
    }
    
    public void setSteelArchOrderNo() {
    	if (!isOpen) return;
    	Cursor cursor = mDb.rawQuery("select " + STEELARCH_ID + " from " + 
    			STEELARCH_DATA_TABLE + " order by " + NAME_TO_METER + " asc" , null);
    	int orderno = 1;
    	mDb.beginTransaction();
    	AppUtil.log("===========setSteelArchCraftOrderNo===============");
    	ContentValues value = new ContentValues();
    	while (cursor.moveToNext()) {
    		value.put(STEELARCH_ORDERNO, orderno);
    		mDb.update(STEELARCH_DATA_TABLE, value, STEELARCH_ID + "=" + cursor.getInt(0), null);
    		orderno++;
    	} 
    	AppUtil.log("===========setSteelArchCraftOrderNo===============");
    	mDb.setTransactionSuccessful(); 
        mDb.endTransaction(); 
    	if (cursor != null) {
    		cursor.close();
    	}
    }

    public void updateCraftTransferState(int orderno, int state) {

   }	

   public void updateSendEndSteelArch(int orderno) {

   }	
    
    public ArrayList<String> getSteelArchNameList() {
    	ArrayList<String> list = new ArrayList<String>();
    	if (!isOpen) return list;
    	Cursor cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE, null);
    	while (cursor.moveToNext()) {
    		list.add(cursor.getString(VOLUME_STEELARCH_NAME));
    	}
    	if (cursor != null) cursor.close();
    	return list;
    }
    
    public String getSteelArchNameWhereOrderNo(int orderno) {
    	String name = "";
    	if (!isOpen) return name;
    	Cursor cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE + 
    			" where " + STEELARCH_ORDERNO + "=" + orderno, null);
    	if (cursor.moveToNext()) {
    		name = cursor.getString(VOLUME_STEELARCH_NAME);
    	}
    	if (cursor != null) cursor.close();
    	return name;
    }
    
    public int getSteelArchOrderNoWhereNameMetre(double nameMeter) {
    	int orderno = 0;
    	if (!isOpen) return orderno;
    	Cursor cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE + 
    			" where " + NAME_TO_METER + "=" + nameMeter, null);
    	if (cursor.moveToNext()) {
    		orderno = cursor.getInt(VOLUME_STEELARCH_ORDERNO);
    	}
    	if (cursor != null) cursor.close();
    	return orderno;
    }
    
    public void getSteelArchCraftParameterList(List<SteelArchCraftParameter> list) {
    	if (!isOpen) return;
    	if (list != null) {
    		list.clear();
    		Cursor cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE, null);
    		while(cursor.moveToNext()) {
    			list.add(new SteelArchCraftParameter(cursor.getInt(VOLUME_STEELARCH_ID), 
    					cursor.getString(VOLUME_STEELARCH_NAME),
    					cursor.getDouble(VOLUME_CRAFT_STEELARCH_TO_STEELARCH_DISTANCE),
    					cursor.getDouble(VOLUME_CRAFT_STEELARCH_TO_ENTRANCE_DISTANCE),
    					cursor.getDouble(VOLUME_CRAFT_SECOND_CAR_WIDTH),
    					cursor.getDouble(VOLUME_NAME_TO_METER)));
    		}
    		if (cursor != null) cursor.close();
    	}
    }

    public SteelArchCraftParameter getSteelArchCraftParameterWhereOrderNo(int orderno) {
	SteelArchCraftParameter parameter = new SteelArchCraftParameter();	
    	if (!isOpen) return parameter;
    	Cursor cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE +
			" where " + STEELARCH_ORDERNO + "=" + orderno, null);
    	if (cursor.moveToNext()) {
    		parameter = new SteelArchCraftParameter(cursor.getInt(VOLUME_STEELARCH_ID), 
    					cursor.getString(VOLUME_STEELARCH_NAME),
    					cursor.getDouble(VOLUME_CRAFT_STEELARCH_TO_STEELARCH_DISTANCE),
    					cursor.getDouble(VOLUME_CRAFT_STEELARCH_TO_ENTRANCE_DISTANCE),
    					cursor.getDouble(VOLUME_CRAFT_SECOND_CAR_WIDTH),
    					cursor.getDouble(VOLUME_NAME_TO_METER));
    	}
    	if (cursor != null) cursor.close();
    	return parameter;
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
    
    public void clearSteelArchRecord() {
    	if (!isOpen) return;
    	mDb.delete(STEELARCH_DATA_TABLE, null, null);
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
    
    public int getSteelArchCount() {
    	Cursor cursor = mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE, null);  
    	int count = 0;
    	if (cursor != null)
    		count = cursor.getCount();  
    	if (cursor != null)
    		cursor.close();  
    	return count; 
    }
    
    public SteelArchCraftParameter getSteelArchCraftParameter(int orderno) {
    	SteelArchCraftParameter parameter = null;
    	if (!isOpen) return null;
    	Cursor cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE + 
    			" where " + STEELARCH_ORDERNO + "=" + orderno, null);
		if(cursor.moveToNext()) {
			int id = cursor.getInt(VOLUME_STEELARCH_ID);
			String name = cursor.getString(VOLUME_STEELARCH_NAME);
			double steelarch_to_steelarch_distance = cursor.getDouble(VOLUME_CRAFT_STEELARCH_TO_STEELARCH_DISTANCE);
			double steelarch_to_entrance_distance = cursor.getDouble(VOLUME_CRAFT_STEELARCH_TO_ENTRANCE_DISTANCE);
			double second_car_width = cursor.getDouble(VOLUME_CRAFT_SECOND_CAR_WIDTH);
			double nameMeter = cursor.getDouble(VOLUME_NAME_TO_METER);
			parameter = new SteelArchCraftParameter(id, name, steelarch_to_steelarch_distance, steelarch_to_entrance_distance,
					second_car_width, nameMeter);
		}
		if (cursor != null) cursor.close();
		return parameter;
    }
    
    public SteelArchDetailParameter getSteelArchDetailParameter(int orderno) {
    	SteelArchDetailParameter parameter = null;
    	if (!isOpen) return null;
    	Cursor cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE + 
    			" where " + STEELARCH_ORDERNO + "=" + orderno, null);
    	if (cursor.moveToNext()) {
    		String entry_mileage = "k100 + 90";
    		double left_steelarch_to_tunnelface_distance = cursor.getDouble(VOLUME_COLLECT_LEFT_STEELARCH_TO_TUNNELFACE_DISTANCE);
    		double right_steelarch_to_tunnelface_distance = cursor.getDouble(VOLUME_COLLECT_RIGHT_STEELARCH_TO_TUNNELFACE_DISTANCE);
    		double steelarch_to_tunnel_entrance_distance = cursor.getDouble(VOLUME_CRAFT_STEELARCH_TO_ENTRANCE_DISTANCE);
    		double left_measure_distance = cursor.getDouble(VOLUME_COLLECT_LEFT_STEELARCH_TO_STEELARCH_DISTANCE);
    		double right_measure_distance = cursor.getDouble(VOLUME_COLLECT_RIGHT_STEELARCH_TO_STEELARCH_DISTANCE);
    		String left_measure_date = cursor.getString(VOLUME_COLLECT_LEFT_MEASURE_DATE);
    		String left_date = "";
    		String left_time = "";
    		if (left_measure_date != null && left_measure_date.contains(" ")) {
	    		String strs[] = left_measure_date.split(" ");
	    		if (strs.length == 2) {
	    			left_date = strs[0];
	    			left_time = strs[1];
	    		}
	    	}	
    		String right_measure_date = cursor.getString(VOLUME_COLLECT_RIGHT_MEASURE_DATE);
    		String right_date = "";
    		String right_time = "";
    		if (right_measure_date != null && right_measure_date.contains(" ")) {
	    		String strs[] = right_measure_date.split(" ");
	    		if (strs.length == 2) {
	    			right_date = strs[0];
	    			right_time = strs[1];
	    		}
	    	}	
    		String left_pic_dir_tunnelface = cursor.getString(VOLUME_COLLECT_LEFT_PIC_DIR_TUNNELFACE);
    		String left_pic_dir_tunnelentry = cursor.getString(VOLUME_COLLECT_LEFT_PIC_DIR_ENTRANCE);
    		String right_pic_dir_tunnelface = cursor.getString(VOLUME_COLLECT_RIGHT_PIC_DIR_TUNNELFACE);
    		String right_pic_dir_tunnelentry = cursor.getString(VOLUME_COLLECT_RIGHT_PIC_DIR_ENTRANCE);
    		if (left_pic_dir_tunnelface != null)
    			AppUtil.log("left_pic_dir_tunnelface:" + left_pic_dir_tunnelface);
    		else 
    			AppUtil.log("left_pic_dir_tunnelface is null");
    		return new SteelArchDetailParameter(orderno, "", left_date, right_date, entry_mileage, 
    				0, steelarch_to_tunnel_entrance_distance, left_time, 
    				left_measure_distance, left_steelarch_to_tunnelface_distance, right_time, 
    				right_measure_distance, right_steelarch_to_tunnelface_distance,
    				left_pic_dir_tunnelentry, left_pic_dir_tunnelface, 
    				right_pic_dir_tunnelentry, right_pic_dir_tunnelface);
    	}
    	if (cursor != null) cursor.close();
		return parameter;
    }
    
    public SteelArchCollectParameter getSteelArchCollectParameter(int orderno) {
    	SteelArchCollectParameter parameter = null;
    	if (!isOpen) return null;
    	Cursor cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE + 
    			" where " + STEELARCH_ORDERNO + "=" + orderno, null);
		if(cursor.moveToNext()) {
			int id = cursor.getInt(VOLUME_STEELARCH_ID);
			String name = cursor.getString(VOLUME_STEELARCH_NAME);
			double left_steelarch_to_steelarch_distance = cursor.getDouble(VOLUME_COLLECT_LEFT_STEELARCH_TO_STEELARCH_DISTANCE);
			double right_steelarch_to_steelarch_distance = cursor.getDouble(VOLUME_COLLECT_RIGHT_STEELARCH_TO_STEELARCH_DISTANCE);
			double left_steelarch_to_tunnelface_distance = cursor.getDouble(VOLUME_COLLECT_LEFT_STEELARCH_TO_TUNNELFACE_DISTANCE);
			double right_steelarch_to_tunnelface_distance = cursor.getDouble(VOLUME_COLLECT_RIGHT_STEELARCH_TO_TUNNELFACE_DISTANCE);
			double left_secondcar_to_steelarch_distance = cursor.getDouble(VOLUME_COLLECT_LEFT_SECONDCAR_TO_STEELARCH_DISTANCE);
			double right_secondcar_to_steelarch_distance = cursor.getDouble(VOLUME_COLLECT_RIGHT_SECONDCAR_TO_STEELARCH_DISTANCE);
			String left_measure_date = cursor.getString(VOLUME_COLLECT_LEFT_MEASURE_DATE);
			String right_measure_date = cursor.getString(VOLUME_COLLECT_RIGHT_MEASURE_DATE);
			parameter = new SteelArchCollectParameter(id, name, left_steelarch_to_steelarch_distance, left_steelarch_to_tunnelface_distance,
					left_secondcar_to_steelarch_distance, left_measure_date, right_steelarch_to_steelarch_distance, 
					right_steelarch_to_tunnelface_distance, right_secondcar_to_steelarch_distance, right_measure_date);
		}
		if (cursor != null) cursor.close();
		return parameter;
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
    
    public void deleteSteelArchDataRecord() {
    	if (!isOpen) return;
		mDb.delete(STEELARCH_DATA_TABLE, null, null);
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
    
    public MgrStasticParameter getMgrStasticParameter() {
    	MgrStasticParameter parameter = null;
    	if (!isOpen) return null;
    	int table_size = getSteelArchCount();
    	AppUtil.log("table_size:" + table_size);
    	if (table_size > 0) {
    		Cursor start_cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE + 
    				" where " + STEELARCH_ORDERNO + "=" + 1, null);
    		
    		Cursor end_cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE + 
    				" where " + STEELARCH_ORDERNO + "=" + table_size, null);
    		if (start_cursor.moveToNext() && end_cursor.moveToNext()) {
    			parameter = new MgrStasticParameter(start_cursor.getString(VOLUME_COLLECT_LEFT_MEASURE_DATE) + "~" + 
    					end_cursor.getString(VOLUME_COLLECT_RIGHT_MEASURE_DATE), 
    					start_cursor.getString(VOLUME_STEELARCH_NAME) + "~" +
    							end_cursor.getString(VOLUME_STEELARCH_NAME), 
    							(end_cursor.getDouble(VOLUME_NAME_TO_METER) - start_cursor.getDouble(VOLUME_NAME_TO_METER))+"");
    			start_cursor.close();
    			end_cursor.close();
    		}
    	}
		return parameter;
    }
    
    public void getManagerCraftParameterList(List<ManagerCraftParameter> list, double start_mileage_meter) {
    	list.clear();
    	Cursor cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE+ " order by " 
    						+ STEELARCH_ORDERNO + " asc", null);
    	double prev_name_meter = start_mileage_meter;
    	java.text.DecimalFormat   df   =new   java.text.DecimalFormat("##.##"); 
    	while (cursor.moveToNext()) {
    		int orderno = cursor.getInt(VOLUME_STEELARCH_ORDERNO);
    		String name = cursor.getString(VOLUME_STEELARCH_NAME); 
    		double name_meter = cursor.getDouble(VOLUME_NAME_TO_METER);
    		double design_distance = name_meter - prev_name_meter;
    		prev_name_meter = name_meter;
    		double steelarch_to_steelarch_distance = (cursor.getDouble(VOLUME_COLLECT_LEFT_STEELARCH_TO_STEELARCH_DISTANCE)
    				+ cursor.getDouble(VOLUME_COLLECT_RIGHT_STEELARCH_TO_STEELARCH_DISTANCE)) / 2;
    		double steelarch_to_tunnelface_distance = (cursor.getDouble(VOLUME_COLLECT_LEFT_STEELARCH_TO_TUNNELFACE_DISTANCE)
    				+ cursor.getDouble(VOLUME_COLLECT_RIGHT_STEELARCH_TO_TUNNELFACE_DISTANCE)) / 2;
    		double secondcar_to_tunnelface_distance = (cursor.getDouble(VOLUME_COLLECT_LEFT_SECONDCAR_TO_STEELARCH_DISTANCE)
    				+ cursor.getDouble(VOLUME_COLLECT_RIGHT_SECONDCAR_TO_STEELARCH_DISTANCE)) / 2 
    				+ cursor.getDouble(VOLUME_CRAFT_SECOND_CAR_WIDTH);
    		list.add(new ManagerCraftParameter(orderno, name, Double.parseDouble(df.format(design_distance)), steelarch_to_steelarch_distance, 
    				secondcar_to_tunnelface_distance, steelarch_to_tunnelface_distance));
    	}
    	if (cursor != null) cursor.close();
    }
    
    public void getDdSteelArchDataList(List<DdSteelArchDataParameter> list) {
    	list.clear();
    	Cursor cursor =  mDb.rawQuery("select * from " + STEELARCH_DATA_TABLE+ " order by " 
    						+ STEELARCH_ORDERNO + " asc", null);
    	while (cursor.moveToNext()) {
    		int id = cursor.getInt(VOLUME_STEELARCH_ORDERNO);
    		String name = cursor.getString(VOLUME_STEELARCH_NAME); 
    		String date = cursor.getString(VOLUME_COLLECT_LEFT_MEASURE_DATE); 
    		String right_date = cursor.getString(VOLUME_COLLECT_RIGHT_MEASURE_DATE);
    		if (date != null && right_date != null){
    			if (date.compareTo(right_date) > 0) {
        			date = right_date;
        		}
    		}
    		double steelarch_to_steelarch_distance = (cursor.getDouble(VOLUME_COLLECT_LEFT_STEELARCH_TO_STEELARCH_DISTANCE)
    				+ cursor.getDouble(VOLUME_COLLECT_RIGHT_STEELARCH_TO_STEELARCH_DISTANCE)) / 2;
    		double steelarch_to_tunnelface_distance = (cursor.getDouble(VOLUME_COLLECT_LEFT_STEELARCH_TO_TUNNELFACE_DISTANCE)
    				+ cursor.getDouble(VOLUME_COLLECT_RIGHT_STEELARCH_TO_TUNNELFACE_DISTANCE)) / 2;
    		double secondcar_to_tunnelface_distance = (cursor.getDouble(VOLUME_COLLECT_LEFT_SECONDCAR_TO_STEELARCH_DISTANCE)
    				+ cursor.getDouble(VOLUME_COLLECT_RIGHT_SECONDCAR_TO_STEELARCH_DISTANCE)) / 2 
    				+ cursor.getDouble(VOLUME_CRAFT_SECOND_CAR_WIDTH);
    		list.add(new DdSteelArchDataParameter(id, name, date, steelarch_to_steelarch_distance, 
    				secondcar_to_tunnelface_distance, steelarch_to_tunnelface_distance));
    	}
    	if (cursor != null) cursor.close();
    }
    
    public String getSteelArchLeftPicDirEntrance(int orderno) {
    	String pic_string = "";
    	Cursor cursor = mDb.rawQuery("select "+ COLLECT_LEFT_PIC_DIR_ENTRANCE + " from " + STEELARCH_DATA_TABLE + 
    			" where " + STEELARCH_ID + "=" + orderno, null);
    	if (cursor.moveToNext()) {
    		pic_string = cursor.getString(0);
    		cursor.close();
    	}
    	return pic_string;   	
    }
    
    public String getSteelArchLeftPicDirTunnelFace(int orderno) {
    	String pic_string = "";
    	Cursor cursor = mDb.rawQuery("select "+ COLLECT_LEFT_PIC_DIR_TUNNELFACE + " from " + STEELARCH_DATA_TABLE + 
    			" where " + STEELARCH_ID + "=" + orderno, null);
    	if (cursor.moveToNext()) {
    		pic_string = cursor.getString(0);
    		cursor.close();
    	}
    	return pic_string;   	
    }
    
    public String getSteelArchRightPicDirEntrance(int orderno) {
    	String pic_string = "";
    	Cursor cursor = mDb.rawQuery("select "+ COLLECT_RIGHT_PIC_DIR_ENTRANCE + " from " + STEELARCH_DATA_TABLE + 
    			" where " + STEELARCH_ID + "=" + orderno, null);
    	if (cursor.moveToNext()) {
    		pic_string = cursor.getString(0);
    		cursor.close();
    	}
    	return pic_string;   	
    }
    
    public String getSteelArchRightPicDirTunnelFace(int orderno) {
    	String pic_string = "";
    	Cursor cursor = mDb.rawQuery("select "+ COLLECT_RIGHT_PIC_DIR_TUNNELFACE + " from " + STEELARCH_DATA_TABLE + 
    			" where " + STEELARCH_ID + "=" + orderno, null);
    	if (cursor.moveToNext()) {
    		pic_string = cursor.getString(0);
    		cursor.close();
    	}
    	return pic_string;   	
    }    
    public String getMixResultPicString(int id) {
    	String pic_string = "";
    	Cursor cursor = mDb.rawQuery("select "+ MIX_COLLECT_PIC_RESULT + " from " + MIX_COLLECT_TABLE + 
    			" where " + MIX_COLLECT_ID + "=" + id, null);
    	if (cursor.moveToNext()) {
    		pic_string = cursor.getString(0);
    		cursor.close();
    	}
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
    
    public void insertLeftSteelArchCollectRecord(int orderno, String date, double measure_distance, 
    		double tunnelface_distance, double secondcar_distance) {
    	if (!isOpen) return;
    	mDb.execSQL("UPDATE " + STEELARCH_DATA_TABLE + " SET "
				+ COLLECT_LEFT_MEASURE_DATE + " =?,"
				+ COLLECT_LEFT_STEELARCH_TO_STEELARCH_DISTANCE + "=" + measure_distance + ","
				+ COLLECT_LEFT_STEELARCH_TO_TUNNELFACE_DISTANCE + "=" + tunnelface_distance + ","
				+ COLLECT_LEFT_STEELARCH_TO_SECONDCAR_DISTANCE + "=" + secondcar_distance + " WHERE " 
				+ STEELARCH_ORDERNO + "=" + orderno, new Object[]{date}); 
    }
    
    public void insertRightSteelArchCollectRecord(int orderno, String date, double measure_distance, 
    		double tunnelface_distance, double secondcar_distance) {
    	if (!isOpen) return;
    	mDb.execSQL("UPDATE " + STEELARCH_DATA_TABLE + " SET "
				+ COLLECT_RIGHT_MEASURE_DATE + " =?,"
				+ COLLECT_RIGHT_STEELARCH_TO_STEELARCH_DISTANCE + "=" + measure_distance + ","
				+ COLLECT_RIGHT_STEELARCH_TO_TUNNELFACE_DISTANCE + "=" + tunnelface_distance + ","
				+ COLLECT_RIGHT_STEELARCH_TO_SECONDCAR_DISTANCE + "=" + secondcar_distance + " WHERE " 
				+ STEELARCH_ORDERNO + "=" + orderno, new Object[]{date}); 
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
    
    public void updateSteelArchCollectLeftPicDirEntrance(int orderno, String pic_string) {
    	if (!isOpen) return;
    	ContentValues value = new ContentValues();
    	AppUtil.log("=========updateSteelArchCollectLeftPicDirEntrance=========");
    	AppUtil.log("pic_string:");
    	AppUtil.log(pic_string);
    	AppUtil.log("=========updateSteelArchCollectLeftPicDirEntrance=========");
    	value.put(COLLECT_LEFT_PIC_DIR_ENTRANCE, pic_string);
    	mDb.update(STEELARCH_DATA_TABLE, value, STEELARCH_ORDERNO + "=" + orderno, null);
    }
    
    public void updateSteelArchCollectRightPicDirEntrance(int orderno, String pic_string) {
    	if (!isOpen) return;
    	ContentValues value = new ContentValues();
    	AppUtil.log("=========updateSteelArchCollectRightPicDirEntrance=========");
    	AppUtil.log("pic_string:");
    	AppUtil.log(pic_string);
    	AppUtil.log("=========updateSteelArchCollectRightPicDirEntrance=========");
    	value.put(COLLECT_RIGHT_PIC_DIR_ENTRANCE, pic_string);
    	mDb.update(STEELARCH_DATA_TABLE, value, STEELARCH_ORDERNO + "=" + orderno, null);
    }
    
    public void updateSteelArchCollectLeftPicDirTunnelFace(int orderno, String pic_string) {
    	if (!isOpen) return;
    	ContentValues value = new ContentValues();
    	AppUtil.log("=========updateSteelArchCollectLeftPicDirTunnelFace=========");
    	AppUtil.log("pic_string:");
    	AppUtil.log(pic_string);
    	AppUtil.log("=========updateSteelArchCollectLeftPicDirTunnelFace=========");
    	value.put(COLLECT_LEFT_PIC_DIR_TUNNELFACE, pic_string);
    	mDb.update(STEELARCH_DATA_TABLE, value, STEELARCH_ORDERNO + "=" + orderno, null);
    }
    
    public void updateSteelArchCollectRightPicDirTunnelFace(int orderno, String pic_string) {
    	if (!isOpen) return;
    	ContentValues value = new ContentValues();
    	AppUtil.log("=========updateSteelArchCollectRightPicDirTunnelFace=========");
    	AppUtil.log("pic_string:");
    	AppUtil.log(pic_string);
    	AppUtil.log("=========updateSteelArchCollectRightPicDirTunnelFace=========");
    	value.put(COLLECT_RIGHT_PIC_DIR_TUNNELFACE, pic_string);
    	mDb.update(STEELARCH_DATA_TABLE, value, STEELARCH_ORDERNO + "=" + orderno, null);
    }
    
    public void updateSteelArchCollectLeftParameter(int orderno, String date, double steelarch_to_steelarch_distance,
    		double secondcar_to_steelarch_distance) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateSteelArchCollectLeftParameter=======");
    	ContentValues value = new ContentValues();
		value.put(COLLECT_LEFT_MEASURE_DATE, date);
		value.put(COLLECT_LEFT_STEELARCH_TO_STEELARCH_DISTANCE, steelarch_to_steelarch_distance);
		value.put(COLLECT_LEFT_STEELARCH_TO_SECONDCAR_DISTANCE, secondcar_to_steelarch_distance);
		mDb.update(STEELARCH_DATA_TABLE, value, STEELARCH_ORDERNO+" = " + orderno, null);
    }
    
    public void updateSteelArchCollectRightParameter(int orderno, String date, double steelarch_to_steelarch_distance,
    		double secondcar_to_steelarch_distance) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateSteelArchCollectRightParameter=======");
    	ContentValues value = new ContentValues();
		value.put(COLLECT_RIGHT_MEASURE_DATE, date);
		value.put(COLLECT_RIGHT_STEELARCH_TO_STEELARCH_DISTANCE, steelarch_to_steelarch_distance);
		value.put(COLLECT_RIGHT_STEELARCH_TO_SECONDCAR_DISTANCE, secondcar_to_steelarch_distance);
		mDb.update(STEELARCH_DATA_TABLE, value, STEELARCH_ORDERNO+" = " + orderno, null);
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