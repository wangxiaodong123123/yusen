package com.ysdata.grouter.database;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.CacheManager;
import com.ysdata.grouter.cloud.util.ConstDef;
import com.ysdata.grouter.element.AnchorParameter;
import com.ysdata.grouter.element.DdSubprojsectionParameter;
import com.ysdata.grouter.element.ManagerCraftParameter;
import com.ysdata.grouter.element.MgrAnchorStasticParameter;
import com.ysdata.grouter.element.MileageParameter;
import com.ysdata.grouter.element.MileageUploadState;
import com.ysdata.grouter.element.UploadParameter;
import com.ysdata.grouter.element.WrcardParameter;
import com.ysdata.grouter.storage.ExtSdCheck;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class ProjectPointDataBaseAdapter {
    private static final String ANCHOR_TABLE = "anchor_table";
    private static final String ANCHOR_ID = "anchor_id"; //from remote server
    private static final String ANCHOR_MILEAGE_ID = "mileage_id"; 
    private static final String ANCHOR_SECTION_METRE = "sectionMetre"; //
    private static final String ANCHOR_NAME = "anchor_name"; //
    private static final String ANCHOR_TYPE = "anchor_type"; //
    private static final String ANCHOR_MODEL = "anchor_model"; // 
    private static final String ANCHOR_DESIGN_LENGTH = "design_anchor_len"; //
    private static final String DESIGN_PRESSURE = "design_pressure"; //
    private static final String MEASURE_PRESSURE = "measure_pressure"; //
    private static final String DESIGN_HOLD_TIME = "design_hold_time"; //
    private static final String PRACTICE_HOLD_TIME = "practice_hold_time"; //
    private static final String THEREO_CAPACITY = "thereo_capacity"; //
    private static final String PRACTICE_CAPACITY = "practice_capacity"; //
    private static final String CAP_UNIT_METER = "cap_unit_meter";
    private static final String CAP_UNIT_HOUR = "cap_unit_hour"; //
    private static final String SLURRY_DATE = "slurry_date"; // 
    private static final String START_SLURRY_DATE = "start_slurry_date"; //
    private static final String END_SLURRY_DATE = "end_slurry_date"; // 
    private static final String COLLECT_PRESSURE_TIME_STRING = "collect_pressure_time_string";
    private static final String COLLECT_PRESSURE_STRING = "collect_pressure_string";
    private static final String COLLECT_CAPACITY_TIME_STRING = "collect_capacity_time_string";
    private static final String COLLECT_CAPACITY_STRING = "collect_capacity_string";    
    private static final String COLLECT_DATA_STRING = "collect_data_string";    
    private static final String FULL_HOLE_PRESSURE = "full_hole_pressure";
    private static final String FULL_HOLE_CAPACITY = "full_hole_capacity";
    private static final String ANCHOR_REMARK = "craft_remark";
    private static final String CRAFT_TRANSFER_STATE = "transfer_state";
    private static final String CRAFT_UPLOAD_STATE = "upload_state";
    private static final String MGR_REMARK = "mgr_remark";
    private static final String ANCHOR_ORDERNO = "order_no"; //produced by local
    private static final String ANCHOR_GROUT_PRIORITY = "grout_priority"; 
    
    private static final int VOLUME_ANCHOR_ID = 0;
    private static final int VOLUME_ANCHOR_MILEAGE_ID = 1;
    private static final int VOLUME_ANCHOR_SECTION_METRE = 2;
    private static final int VOLUME_ANCHOR_NAME = 3;
    private static final int VOLUME_ANCHOR_TYPE = 4;
    private static final int VOLUME_ANCHOR_MODEL = 5;
    private static final int VOLUME_ANCHOR_DESIGN_LENGTH = 6;
    private static final int VOLUME_DESIGN_PRESSURE = 7;
    private static final int VOLUME_MEASURE_PRESSURE = 8;
    private static final int VOLUME_DESIGN_HOLD_TIME = 9;
    private static final int VOLUME_PRACTICE_HOLD_TIME = 10;
    private static final int VOLUME_THEREO_CAPACITY = 11;
    private static final int VOLUME_PRACTICE_CAPACITY = 12;
    private static final int VOLUME_CAP_UNIT_METER = 13;
    private static final int VOLUME_CAP_UNIT_HOUR = 14;
    private static final int VOLUME_SLURRY_DATE = 15;
    private static final int VOLUME_START_SLURRY_DATE = 16;
    private static final int VOLUME_END_SLURRY_DATE = 17;
    private static final int VOLUME_COLLECT_PRESSURE_TIME_STRING = 18;
    private static final int VOLUME_COLLECT_PRESSURE_STRING = 19;
    private static final int VOLUME_COLLECT_CAPACITY_TIME_STRING = 20;
    private static final int VOLUME_COLLECT_CAPACITY_STRING = 21;
    private static final int VOLUME_COLLECT_DATA_STRING = 22;
    private static final int VOLUME_FULL_HOLE_PRESSURE = 23;
    private static final int VOLUME_FULL_HOLE_CAPACITY = 24;
    private static final int VOLUME_ANCHOR_REMARK = 25;
    private static final int VOLUME_CRAFT_TRANSFER_STATE = 26;
    private static final int VOLUME_CRAFT_UPLOAD_STATE = 27;
    private static final int VOLUME_MGR_REMARK = 28;
    private static final int VOLUME_ANCHOR_ORDERNO = 29;
    private static final int VOLUME_ANCHOR_GROUT_PRIORITY = 30;
    
    private static final String WRCARD_ANCHOR_TABLE = "wrcard_anchor_table";
    private static final String WRCARD_ANCHOR_ID = "wrcard_anchor_id";
    private static final String WRCARD_MILEAGE_ID = "wrcard_mileage_id";
    private static final String WRCARD_SECTION_METRE = "wrcard_sectionMetre";
    private static final String WRCARD_ANCHOR_NAME = "wrcard_anchor_name";
    private static final String WRCARD_ANCHOR_TYPE = "wrcard_anchor_type";
    private static final String WRCARD_ANCHOR_MODEL = "wrcard_anchor_model";
    private static final String WRCARD_ANCHOR_DESIGN_LENGTH = "wrcard_ANCHOR_DESIGN_LENGTH";
    private static final String WRCARD_DESIGN_PRESSURE = "WRCARD_DESIGN_PRESSURE";
    private static final String WRCARD_MEASURE_PRESSURE = "WRCARD_MEASURE_PRESSURE";
    private static final String WRCARD_DESIGN_HOLD_TIME = "wrcard_design_hold_time";
    private static final String WRCARD_PRACTICE_HOLD_TIME = "wrcard_practice_hold_time";
    private static final String WRCARD_THEREO_CAPACITY = "WRCARD_THEREO_CAPACITY";
    private static final String WRCARD_PRACTICE_CAPACITY = "WRCARD_PRACTICE_CAPACITY";
    private static final String WRCARD_CAP_UNIT_METER = "wrcard_cap_unit_meter";
    private static final String WRCARD_CAP_UNIT_HOUR = "wrcard_cap_unit_hour";
    private static final String WRCARD_SLURRY_DATE = "wrcard_slurry_date";
    private static final String WRCARD_START_SLURRY_DATE = "wrcard_start_slurry_date";
    private static final String WRCARD_END_SLURRY_DATE = "wrcard_end_slurry_date";
    private static final String WRCARD_COLLECT_PRESSURE_TIME_STRING = "wrcard_collect_pressure_time_string";
    private static final String WRCARD_COLLECT_PRESSURE_STRING = "wrcard_collect_pressure_string";
    private static final String WRCARD_COLLECT_CAPACITY_TIME_STRING = "wrcard_collect_capacity_time_string";
    private static final String WRCARD_COLLECT_CAPACITY_STRING = "wrcard_collect_capacity_string";    
    private static final String WRCARD_COLLECT_DATA_STRING = "wrcard_collect_data_string"; 
    private static final String WRCARD_FULL_HOLE_PRESSURE = "wrcard_full_hole_pressure";
    private static final String WRCARD_FULL_HOLE_CAPACITY = "wrcard_full_hole_capacity";
    private static final String WRCARD_REMARK = "wrcard_remark";
    
    private static final int VOLUME_WRCARD_ANCHOR_ID = 0;
    private static final int VOLUME_WRCARD_ANCHOR_MILEAGE_ID = 1;
    private static final int VOLUME_WRCARD_ANCHOR_SECTION_METRE = 2;
    private static final int VOLUME_WRCARD_ANCHOR_NAME = 3;
    private static final int VOLUME_WRCARD_ANCHOR_TYPE = 4;
    private static final int VOLUME_WRCARD_ANCHOR_MODEL = 5;
    private static final int VOLUME_WRCARD_ANCHOR_DESIGN_LENGTH = 6;
    private static final int VOLUME_WRCARD_DESIGN_PRESSURE = 7;
    private static final int VOLUME_WRCARD_MEASURE_PRESSURE = 8;
    private static final int VOLUME_WRCARD_DESIGN_HOLD_TIME = 9;
    private static final int VOLUME_WRCARD_PRACTICE_HOLD_TIME = 10;
    private static final int VOLUME_WRCARD_THEREO_CAPACITY = 11;
    private static final int VOLUME_WRCARD_PRACTICE_CAPACITY = 12;
    private static final int VOLUME_WRCARD_CAP_UNIT_METER = 13;
    private static final int VOLUME_WRCARD_CAP_UNIT_HOUR = 14;
    private static final int VOLUME_WRCARD_SLURRY_DATE = 15;
    private static final int VOLUME_WRCARD_START_SLURRY_DATE = 16;
    private static final int VOLUME_WRCARD_END_SLURRY_DATE = 17;
    private static final int VOLUME_WRCARD_COLLECT_PRESSURE_TIME_STRING = 18;
    private static final int VOLUME_WRCARD_COLLECT_PRESSURE_STRING = 19;
    private static final int VOLUME_WRCARD_COLLECT_CAPACITY_TIME_STRING = 20;
    private static final int VOLUME_WRCARD_COLLECT_CAPACITY_STRING = 21;
    private static final int VOLUME_WRCARD_COLLECT_DATA_STRING = 22;
    private static final int VOLUME_WRCARD_FULL_HOLE_PRESSURE = 23;
    private static final int VOLUME_WRCARD_FULL_HOLE_CAPACITY = 24;
    private static final int VOLUME_WRCARD_REMARK = 25;
    
    private static final String MILEAGE_TABLE = "mileage_table";
    private static final String MILEAGE_ID = "mileage_id";
    private static final String MILEAGE_NAME = "mileage_name";
    private static final String MILEAGE_ANCHOR_COUNT = "anchor_count";
    private static final String MILEAGE_CREATE_PEOPLE = "create_people";
    private static final String MILEAGE_CREATE_DATE = "create_date";
    private static final String MILEAGE_MIX_RATIO_CEMENT = "MixRatioCement";
    private static final String MILEAGE_MIX_RATIO_SAND = "MixRatioSand";
    private static final String MILEAGE_MIX_RATIO_WATER = "mixRatioWater";
    private static final String MILEAGE_SECTION_METRE = "section_metre";
    private static final String MILEAGE_ORDERNO = "orderno";  //page
    
    private static final int VOLUME_MILEAGE_ID = 0;
    private static final int VOLUME_MILEAGE_NAME = 1;
    private static final int VOLUME_MILEAGE_ANCHOR_COUNT = 2;
    private static final int VOLUME_MILEAGE_CREATE_PEOPLE = 3;
    private static final int VOLUME_MILEAGE_CREATE_DATE = 4;
    private static final int VOLUME_MILEAGE_MIX_RATIO_CEMENT = 5;
    private static final int VOLUME_MILEAGE_MIX_RATIO_SAND = 6;
    private static final int VOLUME_MILEAGE_MIX_RATIO_WATER = 7;
    private static final int VOLUME_MILEAGE_SECTION_METRE = 8;
    private static final int VOLUME_MILEAGE_ORDERNO = 9;
    
    private static final String COMMUNICATE_STATE_TABLE = "communicate_state_table";
    private static final String COMMUNICATE_STATE_ID = "communicate_state_id";
    private static final String SEND_END_MILEAGE = "send_end_mileage";
    private static final String COLLECT_END_MILEAGE = "collect_end_mileage";
    private static final String UPLOAD_END_MILEAGE = "upload_end_mileage";

	private static final String DATABASE_CREATE_COMMUNICATE_STATE_TABLE = "CREATE TABLE IF NOT EXISTS " + COMMUNICATE_STATE_TABLE + "("
			+ COMMUNICATE_STATE_ID + " INTEGER PRIMARY KEY,"
			+ SEND_END_MILEAGE + " VARCHAR(20)," 
			+ COLLECT_END_MILEAGE + " VARCHAR(20),"
			+ UPLOAD_END_MILEAGE + " VARCHAR(20)"
			+ ");";
    
	private static final String DATABASE_CREATE_WRCARD_ANCHOR_TABLE = "CREATE TABLE IF NOT EXISTS " + WRCARD_ANCHOR_TABLE +"("
			+ WRCARD_ANCHOR_ID + " INTEGER PRIMARY KEY,"
			+ WRCARD_MILEAGE_ID + " INTEGER,"
			+ WRCARD_SECTION_METRE + " DOUBLE,"
			+ WRCARD_ANCHOR_NAME + " VARCHAR(20),"
			+ WRCARD_ANCHOR_TYPE + "  VARCHAR(20)," 
			+ WRCARD_ANCHOR_MODEL + "  VARCHAR(20)," 
			+ WRCARD_ANCHOR_DESIGN_LENGTH + " DOUBLE,"
			+ WRCARD_DESIGN_PRESSURE + " DOUBLE,"
			+ WRCARD_MEASURE_PRESSURE + " DOUBLE,"
			+ WRCARD_DESIGN_HOLD_TIME + " INTEGER,"
			+ WRCARD_PRACTICE_HOLD_TIME + " INTEGER,"
			+ WRCARD_THEREO_CAPACITY + " DOUBLE,"
			+ WRCARD_PRACTICE_CAPACITY + " DOUBLE,"
			+ WRCARD_CAP_UNIT_METER + " DOUBLE,"
			+ WRCARD_CAP_UNIT_HOUR + " DOUBLE,"
			+ WRCARD_SLURRY_DATE + " VARCHAR(20),"
			+ WRCARD_START_SLURRY_DATE + " VARCHAR(20),"
			+ WRCARD_END_SLURRY_DATE + " VARCHAR(20),"
			+ WRCARD_COLLECT_PRESSURE_TIME_STRING + " TEXT,"
			+ WRCARD_COLLECT_PRESSURE_STRING + " TEXT,"
			+ WRCARD_COLLECT_CAPACITY_TIME_STRING + " TEXT,"
			+ WRCARD_COLLECT_CAPACITY_STRING + " TEXT,"
			+ WRCARD_COLLECT_DATA_STRING + " TEXT,"
			+ WRCARD_FULL_HOLE_PRESSURE + " DOUBLE,"
			+ WRCARD_FULL_HOLE_CAPACITY + " DOUBLE,"
			+ WRCARD_REMARK + " VARCHAR(20)"
			+");";
	
	private static final String DATABASE_CREATE_ANCHOR_TABLE = "CREATE TABLE IF NOT EXISTS " + ANCHOR_TABLE +"("
			+ ANCHOR_ID + " INTEGER PRIMARY KEY,"
			+ ANCHOR_MILEAGE_ID + " INTEGER,"
			+ ANCHOR_SECTION_METRE + " DOUBLE,"
			+ ANCHOR_NAME + " VARCHAR(20),"
			+ ANCHOR_TYPE + "  VARCHAR(20)," 
			+ ANCHOR_MODEL + "  VARCHAR(20)," 
			+ ANCHOR_DESIGN_LENGTH + " DOUBLE,"
			+ DESIGN_PRESSURE + " DOUBLE,"
			+ MEASURE_PRESSURE + " DOUBLE,"
			+ DESIGN_HOLD_TIME + " INTEGER,"
			+ PRACTICE_HOLD_TIME + " INTEGER,"
			+ THEREO_CAPACITY + " DOUBLE,"
			+ PRACTICE_CAPACITY + " DOUBLE,"
			+ CAP_UNIT_METER + " DOUBLE,"
			+ CAP_UNIT_HOUR + " DOUBLE,"
			+ SLURRY_DATE + " VARCHAR(20),"
			+ START_SLURRY_DATE + " VARCHAR(20),"
			+ END_SLURRY_DATE + " VARCHAR(20),"
			+ COLLECT_PRESSURE_TIME_STRING + " TEXT,"
			+ COLLECT_PRESSURE_STRING + " TEXT,"
			+ COLLECT_CAPACITY_TIME_STRING + " TEXT,"
			+ COLLECT_CAPACITY_STRING + " TEXT,"
			+ COLLECT_DATA_STRING + " TEXT,"
			+ FULL_HOLE_PRESSURE + " DOUBLE,"
			+ FULL_HOLE_CAPACITY + " DOUBLE,"
			+ ANCHOR_REMARK + " VARCHAR(20),"
			+ CRAFT_TRANSFER_STATE + " INTEGER,"
			+ CRAFT_UPLOAD_STATE + " INTEGER,"
			+ MGR_REMARK + " VARCHAR(20),"
			+ ANCHOR_ORDERNO + " INTEGER,"
			+ ANCHOR_GROUT_PRIORITY + " INTEGER"
			+");";
	
	private static final String DATABASE_CREATE_MILEAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + MILEAGE_TABLE + "("
			+ MILEAGE_ID + " INTEGER PRIMARY KEY,"
			+ MILEAGE_NAME + " VARCHAR(20)," 
			+ MILEAGE_ANCHOR_COUNT + " INTEGER,"
			+ MILEAGE_CREATE_PEOPLE + " VARCHAR(20),"
			+ MILEAGE_CREATE_DATE + " VARCHAR(20),"
			+ MILEAGE_MIX_RATIO_CEMENT + " DOUBLE,"
			+ MILEAGE_MIX_RATIO_SAND + " DOUBLE,"
			+ MILEAGE_MIX_RATIO_WATER + " DOUBLE,"
			+ MILEAGE_SECTION_METRE + " DOUBLE,"
			+ MILEAGE_ORDERNO + " INTEGER"
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
						mDb.execSQL(DATABASE_CREATE_ANCHOR_TABLE);
					}catch (SQLException e) {
						AppUtil.log( "creat or open craft parameter table failed.exception:" + e);
					}
					try{
						mDb.execSQL(DATABASE_CREATE_MILEAGE_TABLE);
					}catch (SQLException e) {
						AppUtil.log( "creat or open craft mileage table failed.exception:" + e);
					}
					try {
						mDb.execSQL(DATABASE_CREATE_WRCARD_ANCHOR_TABLE);
					}catch (SQLException e) {
						AppUtil.log( "creat or open wrcard parameter table failed.exception:" + e);
					}
					try {
						mDb.execSQL(DATABASE_CREATE_COMMUNICATE_STATE_TABLE);
					}catch (SQLException e) {
						AppUtil.log( "creat or open communicate state table failed.exception:" + e);
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
    
    public void insertCommunicateStateRecord(int id, String send_end_mileage, String 
    		collect_end_mileage, String upload_end_mileage) {
    	if (!isOpen) return;
    	mDb.execSQL("REPLACE INTO " + COMMUNICATE_STATE_TABLE + " ("
				+ COMMUNICATE_STATE_ID + ", "
				+ SEND_END_MILEAGE + ", "
				+ COLLECT_END_MILEAGE + ", "
				+ UPLOAD_END_MILEAGE + 
				") VALUES ("+ 1 + ",?, "
				+ "?, " +  "? " +
				 ")", new Object[]{send_end_mileage, collect_end_mileage, upload_end_mileage}); 
    }
    
    public void updateSendEndMileage(String mileage) {
    	if (!isOpen) return;
    	ContentValues value = new ContentValues();
    	value.put(SEND_END_MILEAGE, mileage);
		mDb.update(COMMUNICATE_STATE_TABLE, value, COMMUNICATE_STATE_ID + "=" + 1, null);
		
    }
    
    public void updateCollectEndMileage(String mileage) {
    	if (!isOpen) return;
    	ContentValues value = new ContentValues();
    	value.put(COLLECT_END_MILEAGE, mileage);
		mDb.update(COMMUNICATE_STATE_TABLE, value, COMMUNICATE_STATE_ID + "=" + 1, null);
    }
    
    public void updateLoadEndMileage(String mileage) {
    	if (!isOpen) return;
    	ContentValues value = new ContentValues();
    	value.put(UPLOAD_END_MILEAGE, mileage);
		mDb.update(COMMUNICATE_STATE_TABLE, value, COMMUNICATE_STATE_ID + "=" + 1, null);
    }
    
    public String getSendEndMileage() {
    	if (!isOpen) return null;
    	String mileage = "";
    	Cursor cursor = mDb.rawQuery("select " + SEND_END_MILEAGE + " from " + COMMUNICATE_STATE_TABLE + 
    			" where " + COMMUNICATE_STATE_ID + "=" + 1, null);
    	if (cursor.moveToNext()) {
    		mileage = cursor.getString(0);
    		cursor.close();
    	}
    	return mileage;
    }
    
    public String getCollectEndMileage() {
    	if (!isOpen) return null;
    	String mileage = "";
    	Cursor cursor = mDb.rawQuery("select " + COLLECT_END_MILEAGE + " from " + COMMUNICATE_STATE_TABLE + 
    			" where " + COMMUNICATE_STATE_ID + "=" + 1, null);
    	if (cursor.moveToNext()) {
    		mileage = cursor.getString(0);
    		cursor.close();
    	}
    	return mileage;
    }
    
    public String getUploadEndMileage() {
    	if (!isOpen) return null;
    	String mileage = "";
    	Cursor cursor = mDb.rawQuery("select " + UPLOAD_END_MILEAGE + " from " + COMMUNICATE_STATE_TABLE + 
    			" where " + COMMUNICATE_STATE_ID + "=" + 1, null);
    	if (cursor.moveToNext()) {
    		mileage = cursor.getString(0);
    		cursor.close();
    	}
    	return mileage;
    }
    
    public void deleteAllMileageRecord() {
    	if (!isOpen) return;
		mDb.delete(MILEAGE_TABLE, null, null);
    }
    
    public void initalCommunicateState() {
    	if (!isOpen) return;
		mDb.delete(COMMUNICATE_STATE_TABLE, null, null);
		insertCommunicateStateRecord(1, "", "", "");
    }
    
    public void deleteAllAnchorRecord() {
    	if (!isOpen) return;
		mDb.delete(ANCHOR_TABLE, null, null);
    }
    
    public void deleteAllWrcardRecord() {
    	if (!isOpen) return;
		mDb.delete(WRCARD_ANCHOR_TABLE, null, null);
    }
    
    public void deleteMileageParameter(int mileage_id) { 
    	if (!isOpen) return;
		mDb.delete(MILEAGE_TABLE, MILEAGE_ID + "=" + mileage_id, null);
    }
    
    public int getMileagePageWhereClauseMileageId(int mileage_id) {
    	if (!isOpen) return 0;
    	int page = 0;
    	Cursor cursor = mDb.rawQuery("select " + MILEAGE_ORDERNO + " from " + MILEAGE_TABLE + 
    			" where " + MILEAGE_ID + "=" + mileage_id, null);
    	if (cursor.moveToNext()) {
    		page = cursor.getInt(0);
    		cursor.close();
    	}
    	return page;
    }
    
    private int getAnchorAndGroutedCount(int mileage_id) {
    	if (!isOpen) return 0;
    	int grouted_count = 0, anchor_count = 0;
    	Cursor cursor = mDb.rawQuery("select "+ COLLECT_PRESSURE_STRING+" from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_MILEAGE_ID + "=" + mileage_id, null);
    	AppUtil.log("getAnchorAndGroutedCount==================mileage_id:"+mileage_id);
    	if (cursor != null) {
    		while(cursor.moveToNext()) {
    			anchor_count++;
    			if (cursor.getString(0) != null && cursor.getString(0).length() > 0) {
    				grouted_count++;
    			}
    		}
    		cursor.close();
    		
    	}
    	AppUtil.log("anchor_count:"+anchor_count + " grouted_count:"+grouted_count);
    	AppUtil.log("========================================================");
    	return anchor_count << 8 | grouted_count;
    }
    
    public ArrayList<Integer> getPictureItemList() {  
    	String sdcarddir = ExtSdCheck.getExtSDCardPath();
		String dirPath = sdcarddir+ConstDef.DATABASE_PATH+CacheManager.getDbProjectId()+"/"+
				CacheManager.getDbSubProjectId()+"/picture/";
    	ArrayList<Integer> items = new ArrayList<Integer>();  
        try{  
            File f = new File(dirPath);  
            File[] files = f.listFiles();// 列出所有文件  
          
            // 将所有文件存入list中  
            if(files != null){  
                int count = files.length;// 文件个数  
                for (int i = 0; i < count; i++) {  
                    File file = files[i]; 
                    String filename  = file.getName();
                    if(filename.endsWith(".png") || filename.startsWith("picture_"))
                    {
                    	String mileage_page_string = filename.split("\\.")[0].substring(8);
                    	if (mileage_page_string.matches("^[0-9]*[1-9][0-9]*$"))
                    		items.add(Integer.parseInt(mileage_page_string));  
                    }
                }  
            }
        } catch(Exception ex) {  
            ex.printStackTrace();  
        }  
        return items;
    }
    
    public ArrayList<DdSubprojsectionParameter> getDdsubprojsectionlist() {
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE, null);
    	ArrayList<DdSubprojsectionParameter> list = new ArrayList<DdSubprojsectionParameter>();
    	String mileage_name, create_date;
    	int anchor_count, grouted_count;
    	int mileage_id, mileage_page;
    	int anchor_grouted_count;
    	boolean pic_state;
    	ArrayList<Integer> items = getPictureItemList();
    	int items_length = items.size();
    	while(cursor.moveToNext()) {
    		mileage_name = cursor.getString(VOLUME_MILEAGE_NAME);
    		create_date = cursor.getString(VOLUME_MILEAGE_CREATE_DATE);
    		mileage_id = cursor.getInt(VOLUME_MILEAGE_ID);
    		mileage_page = cursor.getInt(VOLUME_MILEAGE_ORDERNO);
    		anchor_grouted_count = getAnchorAndGroutedCount(mileage_id);
    		anchor_count = anchor_grouted_count >> 8;
    		grouted_count = anchor_grouted_count & 0xff;
    		pic_state = false;
    		if (items_length > 0) {
    			for (int i = 0; i < items_length; i++) {
    				if (items.get(i) == mileage_page) {
    					pic_state = true;
    					break;
    				}
    			}
    		}
    		list.add(new DdSubprojsectionParameter(mileage_name, create_date, anchor_count, grouted_count, pic_state));
    	}
    	if (cursor != null) cursor.close();
    	return list;
    }
    
    public ArrayList<String> getMileageNameList() {
    	if (!isOpen) return null;
    	ArrayList<String> list = new ArrayList<String>();
    	Cursor cursor = mDb.rawQuery("select "+MILEAGE_NAME+" from " + MILEAGE_TABLE, null);
    	while(cursor.moveToNext()) {
    		list.add(cursor.getString(0));
    	}
    	if (cursor != null)
    		cursor.close();
    	return list;
    }
    
    public int getMileageUploadState(int mileage_id) {
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_MILEAGE_ID + "=" + mileage_id, null);
    	int uploadState = 0;
    	String collectData = "";
    	int grout_priority = 0;
    	boolean hasUpGroutPoints_upload =false;
    	boolean hasDownGroutPoints_upload = false;
    	boolean hasUpGroutPoints_unupload =false;
    	boolean hasDownGroutPoints_unupload = false;
    	int ret = 4;   
    	while(cursor.moveToNext()) {
    		uploadState = cursor.getInt(VOLUME_CRAFT_UPLOAD_STATE);
    		collectData = cursor.getString(VOLUME_COLLECT_DATA_STRING);
    		grout_priority = cursor.getInt(VOLUME_ANCHOR_GROUT_PRIORITY);
    		if (collectData != null && !collectData.equals("")) {
    			if (uploadState == 0) {
    				if (grout_priority == 0) {
    					hasUpGroutPoints_unupload = true;
    				} else {
    					hasDownGroutPoints_unupload = true;
    				}
    			} else {
    				if (grout_priority == 0) {
    					hasUpGroutPoints_upload = true;
    				} else {
    					hasDownGroutPoints_upload = true;
    				}
    			}
    		}
    	}
    	if (cursor != null) cursor.close();
    	if (hasDownGroutPoints_unupload) { //存在未上传的下坑道数据
    		if (hasUpGroutPoints_upload && !hasUpGroutPoints_unupload) { //上坑道注浆数据已上传
    			ret = 3; //下坑道未上传
    		} else { 
    			ret = 1; 
    		}
    	} else if (hasUpGroutPoints_unupload) { //存在未上传的上坑道数据
    		if (!hasDownGroutPoints_unupload && hasDownGroutPoints_upload) {	//下坑道注浆数据已上传
    			ret = 2; //上坑道未上传
    		} else { 
    			ret = 1; 
    		}
    	}
    	
    	return ret;
    }
    
    public ArrayList<MileageUploadState> getMileageUploadStateList() {
    	if (!isOpen) return null;
    	ArrayList<MileageUploadState> list = new ArrayList<MileageUploadState>();
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE, null);
    	String mileageName;
    	int uploadState;
//    	AppUtil.log("=======getMileageUploadStateList========");
    	long startTime, endTime;
    	startTime = System.currentTimeMillis();
    	//mDb.beginTransaction();
    	while(cursor.moveToNext()) {
    		mileageName = cursor.getString(VOLUME_MILEAGE_NAME);
    		uploadState = getMileageUploadState(cursor.getInt(VOLUME_MILEAGE_ID));
    		list.add(new MileageUploadState(mileageName, uploadState));
    	}
    	//mDb.setTransactionSuccessful();
    	//mDb.endTransaction();
    	endTime = System.currentTimeMillis();
    	AppUtil.log( "excute getMileageUploadStateList time:"+(endTime - startTime)+"(ms)");
    	if (cursor != null)
    		cursor.close();
    	return list;
    }
    
    public boolean doAllGroutingPointUpload() {
    	if (!isOpen) return false;
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE, null);
    	int uploadState = 1;
    	while(cursor.moveToNext()) {
    		uploadState = getMileageUploadState(cursor.getInt(VOLUME_MILEAGE_ID));
    		if (uploadState == 0)
    			break;
    	}
    	if (cursor != null) cursor.close();
    	return uploadState == 1 ? true : false;
    }

    public ArrayList<MileageParameter> getMileageParameterList() {
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE, null);
    	ArrayList<MileageParameter> list = new ArrayList<MileageParameter>();
    	String mileage_name, create_person, create_date, mix_ratio;
    	int anchor_count, mileage_id;
    	while(cursor.moveToNext()) {
    		mileage_id = cursor.getInt(VOLUME_MILEAGE_ID);
    		mileage_name = cursor.getString(VOLUME_MILEAGE_NAME);
    		anchor_count = cursor.getInt(VOLUME_MILEAGE_ANCHOR_COUNT);
    		mix_ratio = cursor.getString(VOLUME_MILEAGE_MIX_RATIO_CEMENT) + ":"
					+ cursor.getString(VOLUME_MILEAGE_MIX_RATIO_SAND) + ":"
					+ cursor.getString(VOLUME_MILEAGE_MIX_RATIO_WATER);
    		create_person = cursor.getString(VOLUME_MILEAGE_CREATE_PEOPLE);
    		create_date = cursor.getString(VOLUME_MILEAGE_CREATE_DATE);
    		list.add(new MileageParameter(mileage_id, mileage_name, anchor_count, create_person, 
    				create_date, mix_ratio));
    	}
    	if (cursor != null)
    		cursor.close();
    	return list;
    }
    
    public MileageParameter getMileageParameterById(int mileage_id) {
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE + 
    			" where " + MILEAGE_ID + "=" + mileage_id, null);
    	MileageParameter mileage_parameter = null;
    	if(cursor.moveToNext()) {
    		String mileage_name = cursor.getString(VOLUME_MILEAGE_NAME);
    		int anchor_count = cursor.getInt(VOLUME_MILEAGE_ANCHOR_COUNT);
    		String create_people = cursor.getString(VOLUME_MILEAGE_CREATE_PEOPLE);
    		String create_date = cursor.getString(VOLUME_MILEAGE_CREATE_DATE);
    		String water_ratio = cursor.getString(VOLUME_MILEAGE_MIX_RATIO_CEMENT) + ":"
    							+ cursor.getString(VOLUME_MILEAGE_MIX_RATIO_SAND) + ":"
    							+ cursor.getString(VOLUME_MILEAGE_MIX_RATIO_WATER);
    		mileage_parameter = new MileageParameter(mileage_id, mileage_name, anchor_count, 
    				create_people, create_date, water_ratio);
    		cursor.close();
    	}
		return mileage_parameter;
    }
    
    public MileageParameter getMileageParameter(int mileage_page) {
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE + 
    			" where " + MILEAGE_ORDERNO + "=" + mileage_page, null);
    	MileageParameter mileage_parameter = null;
    	AppUtil.log("getMileageParameter-------mileage_page:"+mileage_page);
    	if(cursor.moveToNext()) {
    		String mileage_name = cursor.getString(VOLUME_MILEAGE_NAME);
    		int anchor_count = cursor.getInt(VOLUME_MILEAGE_ANCHOR_COUNT);
    		String create_people = cursor.getString(VOLUME_MILEAGE_CREATE_PEOPLE);
    		String create_date = cursor.getString(VOLUME_MILEAGE_CREATE_DATE);
    		String water_ratio = cursor.getString(VOLUME_MILEAGE_MIX_RATIO_CEMENT) + ":"
    							+ cursor.getString(VOLUME_MILEAGE_MIX_RATIO_SAND) + ":"
    							+ cursor.getString(VOLUME_MILEAGE_MIX_RATIO_WATER);
    		mileage_parameter = new MileageParameter(mileage_page, mileage_name, anchor_count, 
    				create_people, create_date, water_ratio);
    		cursor.close();
    	}
		return mileage_parameter;
    }
    
    public void setMileageOrderNo(int direction) {
    	if (!isOpen) return;
    	Cursor cursor_id;
    	if (direction < 0) {
    		cursor_id = mDb.rawQuery("select " + MILEAGE_ID + " from " + MILEAGE_TABLE + 
        			" order by " + MILEAGE_SECTION_METRE + " desc", null);
    	} else {
    		cursor_id = mDb.rawQuery("select " + MILEAGE_ID + " from " + MILEAGE_TABLE + 
    				" order by " + MILEAGE_SECTION_METRE + " asc", null);
    	}
    	int orderno = 1;
    	long startTime, endTime;
    	startTime = System.currentTimeMillis();
    	mDb.beginTransaction();
    	AppUtil.log("===========setMileageOrderNo===============");
    	ContentValues value = new ContentValues();
    	while (cursor_id.moveToNext()) {
    		value.put(MILEAGE_ORDERNO, orderno);
    		mDb.update(MILEAGE_TABLE, value, MILEAGE_ID + "=" + cursor_id.getInt(0), null);
    		orderno++;
    	} 
    	AppUtil.log("===========setMileageOrderNo===============");
    	mDb.setTransactionSuccessful(); 
        mDb.endTransaction(); 
        endTime = System.currentTimeMillis();
    	AppUtil.log( "excute setMileageOrderNo time:"+(endTime - startTime)+"(ms)");
    	if (cursor_id != null) {
    		cursor_id.close();
    	}
    }
    
    public void setAnchorOrderNo(int direction) {
    	if (!isOpen) return;
    	Cursor cursor_id;
    	if (direction < 0) {
    		cursor_id = mDb.rawQuery("select " + ANCHOR_ID + " from " + ANCHOR_TABLE + 
        			" order by " + ANCHOR_SECTION_METRE + " desc," + ANCHOR_NAME + " asc", null);
    	} else {
    		cursor_id = mDb.rawQuery("select " + ANCHOR_ID + " from " + ANCHOR_TABLE + 
        			" order by " + ANCHOR_SECTION_METRE + " asc," + ANCHOR_NAME + " asc", null);
    	}
    	int orderno = 1;
    	long startTime, endTime;
    	startTime = System.currentTimeMillis();
    	mDb.beginTransaction();
    	ContentValues value = new ContentValues();
    	while (cursor_id.moveToNext()) {
    		value.put(ANCHOR_ORDERNO, orderno);
    		mDb.update(ANCHOR_TABLE, value, ANCHOR_ID + "=" + cursor_id.getInt(0), null);
//    		mDb.rawQuery("update " + ANCHOR_TABLE + " set " + ANCHOR_ORDERNO + 
//    				"=" + (orderno++) + " where " + ANCHOR_ID + "=" + cursor_id.getInt(0), null);
    		orderno++;
    	} 
    	mDb.setTransactionSuccessful(); 
        mDb.endTransaction(); 
        endTime = System.currentTimeMillis();
    	AppUtil.log( "excute setAnchorOrderNo time:"+(endTime - startTime)+"(ms)");
    	if (cursor_id != null) {
    		cursor_id.close();
    	}
    }
    
    public int getSyncMileagePage(int direction) {
    	int page = 0;
    	Cursor anchor_cursor;
    	if (direction > 0) {
    		anchor_cursor = mDb.rawQuery("select " + ANCHOR_SECTION_METRE + " from " + 
    				ANCHOR_TABLE + " order by " + ANCHOR_SECTION_METRE + " desc limit 0, 1" , null);
    	} else {
    		anchor_cursor = mDb.rawQuery("select " + ANCHOR_SECTION_METRE + " from " + 
    				ANCHOR_TABLE + " order by " + ANCHOR_SECTION_METRE + " asc limit 0, 1" , null);
    	}
    	if (anchor_cursor.moveToNext()) {
    		double sectionMetre = anchor_cursor.getDouble(0);
    		Cursor mileage_cursor = mDb.rawQuery("select " + MILEAGE_ORDERNO + " from " + MILEAGE_TABLE + 
        			" where " + MILEAGE_SECTION_METRE + "=" + sectionMetre, null);
    		if (mileage_cursor.moveToNext()) {
    			page = mileage_cursor.getInt(0);
    			mileage_cursor.close();
    			anchor_cursor.close();
    		}
    	}
    	return page;
    }
    
   /*    
   public String getMileageName(int mileage_page) {
    	Cursor cursor = mDb.rawQuery("select "+ MILEAGE_NAME +" from " + MILEAGE_TABLE + 
    			" order by " + MILEAGE_SECTION_METRE + " asc limit " + (mileage_page-1) 
    			+ "," + mileage_page, null);
    	String mileage_name = "";
    	if (cursor.moveToNext()) {
    		mileage_name = cursor.getString(0);
    		cursor.close();
    	}
		return mileage_name;
    }
    */
    
    public String getMileageNameWhereOrderNo(int mileage_page) {
    	Cursor cursor = mDb.rawQuery("select "+ MILEAGE_NAME +" from " + MILEAGE_TABLE + 
    			" where " + MILEAGE_ORDERNO + "=" + mileage_page, null);
    	String mileage_name = "";
    	if (cursor.moveToNext()) {
    		mileage_name = cursor.getString(0);
    		cursor.close();
    	}
		return mileage_name;
    }
    
    
    public ArrayList<ManagerCraftParameter> getManagerAnchorPage(int mileage_id) {
    	ArrayList<ManagerCraftParameter> mlist = new ArrayList<ManagerCraftParameter>();
    	if (!isOpen) return mlist;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_MILEAGE_ID + "=" + mileage_id, null);
    	int index = 1;
    	int anchor_id_ = 0;
    	String anchor_name, anchor_type,anchor_model,date,start_date, end_date, mgr_remark;
    	double design_len, design_pressure, measure_pressure, thereo_cap, practice_cap;
    	int mileage_id_, design_hold_time, practice_hold_time;
    	while(cursor.moveToNext()) {
    		int anchor_id = cursor.getInt(VOLUME_ANCHOR_ID);   //wxd ?
    		if (index == 1) {
    			anchor_id_ = anchor_id-1;
    		}
    		anchor_name = cursor.getString(VOLUME_ANCHOR_NAME);
    		mileage_id_ = cursor.getInt(VOLUME_ANCHOR_MILEAGE_ID);
    		anchor_type = cursor.getString(VOLUME_ANCHOR_TYPE);
    		anchor_model = cursor.getString(VOLUME_ANCHOR_MODEL);
    		design_len = cursor.getDouble(VOLUME_ANCHOR_DESIGN_LENGTH);
    		design_pressure = cursor.getDouble(VOLUME_DESIGN_PRESSURE);
    		measure_pressure = cursor.getDouble(VOLUME_MEASURE_PRESSURE);
    		thereo_cap = cursor.getDouble(VOLUME_THEREO_CAPACITY);
    		practice_cap = cursor.getDouble(VOLUME_PRACTICE_CAPACITY);
    		date = getYMDformat(cursor.getString(VOLUME_SLURRY_DATE));
    		start_date = getHMSformat(cursor.getString(VOLUME_START_SLURRY_DATE));
    		end_date = getHMSformat(cursor.getString(VOLUME_END_SLURRY_DATE));
    		design_hold_time = cursor.getInt(VOLUME_DESIGN_HOLD_TIME);
    		practice_hold_time = cursor.getInt(VOLUME_PRACTICE_HOLD_TIME);
    		mgr_remark = cursor.getString(VOLUME_MGR_REMARK);
    		AppUtil.log( "id:"+anchor_id+ "   date:"+date + " start_date:"+start_date + " end_date:"+end_date);
    		mlist.add(new ManagerCraftParameter(anchor_id-anchor_id_, anchor_name, mileage_id_, anchor_type, anchor_model, design_len, 
    				design_pressure, measure_pressure, thereo_cap, practice_cap, date, start_date, end_date,
    				design_hold_time, practice_hold_time, mgr_remark, 0));
    		WrcardParameter mWrParameter = getWrcardCraftParameter(anchor_name);
    		if (mWrParameter != null) {
    			mlist.add(new ManagerCraftParameter(anchor_id-anchor_id_, mWrParameter.getAnchorName(), mWrParameter.getMileageId(),  
    					mWrParameter.getAnchorType(), mWrParameter.getAnchorModel(), mWrParameter.getAnchorLength(), mWrParameter.getDesignPress(),
    					mWrParameter.getPracticePress(), mWrParameter.getThereoCap(), 
    					mWrParameter.getPracticeCap(), mWrParameter.getDate(), 
    					mWrParameter.getStartDate(), mWrParameter.getEndDate(),
    					mWrParameter.getDesignHoldTime(), mWrParameter.getPracticeHoldTime(),
    					mWrParameter.getRemark(), 1));
    		}
    		index++;
    	}
    	cursor.close();
		return mlist;
    }
    
    public WrcardParameter getWrcardCraftParameter(String anchor_name) {
    	WrcardParameter mWrcardParameter = null;
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + WRCARD_ANCHOR_TABLE + 
    			" where " + WRCARD_ANCHOR_NAME + " like ?" , new String[]{anchor_name});
    	if(cursor.moveToNext()) {
    		int anchor_id = cursor.getInt(VOLUME_WRCARD_ANCHOR_ID);
    		int mileage_id = cursor.getInt(VOLUME_WRCARD_ANCHOR_MILEAGE_ID);
    		String anchor_type = cursor.getString(VOLUME_WRCARD_ANCHOR_TYPE);
    		String anchor_model = cursor.getString(VOLUME_WRCARD_ANCHOR_MODEL);
    		double anchor_design_len = cursor.getDouble(VOLUME_WRCARD_ANCHOR_DESIGN_LENGTH);
    		double design_press = cursor.getDouble(VOLUME_WRCARD_DESIGN_PRESSURE);
    		double practice_press = cursor.getDouble(VOLUME_WRCARD_MEASURE_PRESSURE);
    		double thereo_cap = cursor.getDouble(VOLUME_WRCARD_THEREO_CAPACITY);
    		double practice_cap = cursor.getDouble(VOLUME_WRCARD_PRACTICE_CAPACITY);
    		String date = getYMDformat(cursor.getString(VOLUME_WRCARD_SLURRY_DATE));
    		String start_date = getHMSformat(cursor.getString(VOLUME_WRCARD_START_SLURRY_DATE));
    		String end_date = getHMSformat(cursor.getString(VOLUME_WRCARD_END_SLURRY_DATE));
    		int design_hold_time = cursor.getInt(VOLUME_WRCARD_DESIGN_HOLD_TIME);
    		int practice_hold_time = cursor.getInt(VOLUME_WRCARD_PRACTICE_HOLD_TIME);
    		String remark = cursor.getString(VOLUME_WRCARD_REMARK);
    		double cap_unit_hour = cursor.getDouble(VOLUME_WRCARD_CAP_UNIT_HOUR);
    		mWrcardParameter = new WrcardParameter(anchor_id, anchor_name, mileage_id, 
    				cap_unit_hour, anchor_type, anchor_model, anchor_design_len, thereo_cap, "", 
    				design_press, practice_press, design_hold_time, practice_hold_time, date, 
    				start_date, end_date, practice_cap, remark);
    	}
    	cursor.close();
		return mWrcardParameter;
    }
    
    public void insertWrcardParameter(int anchor_id, String anchor_name, double cap_unit_hour,
        	String anchor_type, String anchor_model, double anchor_design_len, double thereo_cap,
        	double design_press, int hold_time) {
    		if (!isOpen) return;
    		AppUtil.log( "========insertWrcardParameter=======");
    		mDb.execSQL("REPLACE INTO " + WRCARD_ANCHOR_TABLE + " ("
    				+ WRCARD_ANCHOR_ID + ", " 
    				+ WRCARD_ANCHOR_NAME + ", "
    				+ WRCARD_CAP_UNIT_HOUR + ", "
    				+ WRCARD_ANCHOR_TYPE + ","
    				+ WRCARD_ANCHOR_MODEL + ","
    				+ WRCARD_ANCHOR_DESIGN_LENGTH + ", "
    				+ WRCARD_THEREO_CAPACITY + ", "
    				+ WRCARD_DESIGN_PRESSURE + ", "
    				+ WRCARD_DESIGN_HOLD_TIME + ")" +
    				" VALUES (" + anchor_id +  
    				", ? ," + cap_unit_hour + ", ? , ? ," +
    				anchor_design_len + ", " + thereo_cap + 
    				", " + design_press + ", " +
    				hold_time +")", new Object[]{anchor_name, anchor_type, anchor_model});
    }
    
    String getYMDformat(String date) {
    	if (date != null && date.length() > 0) {
    		if (date.contains("-")) {
    			String[] ymds = date.split("-");
    			if (ymds.length == 3) {
    				String year = ymds[0];
    				String mouth = ymds[1];
    				String day = ymds[2];
    				
    				StringBuffer new_date = new StringBuffer();
    				new_date.append(year).append('-');
    				if (mouth.length() == 1) {
    					new_date.append('0');
    				}
    				new_date.append(mouth).append('-');
    				if (day.length() == 1) {
    					new_date.append('0');
    				}
    				new_date.append(day);
    				return new_date.toString();
    			}
    		}
    	}
    	return date;
    }
    
    String getHMSformat(String date) {
    	if (date != null && date.length() > 0) {
    		if (date.contains(":")) {
    			String[] hmss = date.split(":");
    			if (hmss.length == 3) {
    				String hour = hmss[0];
    				String minute = hmss[1];
    				String second = hmss[2];
    				
    				StringBuffer new_date = new StringBuffer();
    				if (hour.length() == 1) {
    					new_date.append('0');
    				}
    				new_date.append(hour).append(':');
    				if (minute.length() == 1) {
    					new_date.append('0');
    				}
    				new_date.append(minute).append(':');
    				if (second.length() == 1) {
    					new_date.append('0');
    				}
    				new_date.append(second);
    				return new_date.toString();
    			}
    		}
    	}
    	return date;
    }
    
    String getDateFormat(String date) {
    	if (date != null && date.length() > 0) {
    		if (date.contains(" ")) {
    			String[] dates = date.split(" ");
    			if (dates.length == 2) {
    				String ymd = dates[0];
    				String hms = dates[1];
    				if (ymd.contains("-") && hms.contains(":")) {
    					String[] ymds = ymd.split("-");
    					String[] hmss = hms.split(":");
    					if (ymds.length == 3 && hmss.length == 3) {
    						String year = ymds[0];
    						String mouth = ymds[1];
    						String day = ymds[2];
    						String hour = hmss[0];
    						String minute = hmss[1];
    						String second = hmss[2];
    						
    						StringBuffer new_date = new StringBuffer();
    						new_date.append(year).append('-');
    						if (mouth.length() == 1) {
    							new_date.append('0');
    						}
    						new_date.append(mouth).append('-');
    						if (day.length() == 1) {
    							new_date.append('0');
    						}
    						new_date.append(day).append(' ');
    						if (hour.length() == 1) {
    							new_date.append('0');
    						}
    						new_date.append(hour).append(':');
    						if (minute.length() == 1) {
    							new_date.append('0');
    						}
    						new_date.append(minute).append(':');
    						if (second.length() == 1) {
    							new_date.append('0');
    						}
    						new_date.append(second);
    						return new_date.toString();
    					}
    				}
    			}
    		} 
    	}
    	return date;
    }
    
    public int getAnchorStartOrderNoByMileageId(int mileage_id) {
    	if (!isOpen) return 0;
    	int order_no = 0;
    	Cursor cursor = mDb.rawQuery("select " + ANCHOR_ORDERNO + " from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_MILEAGE_ID + "=" + mileage_id + " order by " + ANCHOR_ORDERNO +
    			" asc limit 0, 1", null);
    	if (cursor.moveToNext()) {
    		order_no = cursor.getInt(0);
    		cursor.close();
    	}
    	return order_no;
    }
    
    public int getAnchorStartOrderNoWhereSectionMetre(double section_metre) {
    	if (!isOpen) return 0;
    	int order_no = 0;
    	Cursor cursor = mDb.rawQuery("select " + ANCHOR_ORDERNO + " from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_SECTION_METRE + "=" + section_metre + " order by " + ANCHOR_ORDERNO +
    			" asc limit 0, 1", null);
    	if (cursor.moveToNext()) {
    		order_no = cursor.getInt(0);
    		cursor.close();
    	}
    	return order_no;
    }
    
    public int getAnchorEndOrderNoByMileageId(int mileage_id) {
    	if (!isOpen) return 0;
    	int order_no = 0;
    	Cursor cursor = mDb.rawQuery("select " + ANCHOR_ORDERNO + " from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_MILEAGE_ID + "=" + mileage_id + " order by " + ANCHOR_ORDERNO +
    			" desc limit 0, 1", null);
    	if (cursor.moveToNext()) {
    		order_no = cursor.getInt(0);
    		cursor.close();
    	}
    	return order_no;
    }
    
    public int getAnchorEndOrderNoWhereSectonMetre(double section_metre) {
    	if (!isOpen) return 0;
    	int order_no = 0;
    	Cursor cursor = mDb.rawQuery("select " + ANCHOR_ORDERNO + " from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_SECTION_METRE + "=" + section_metre + " order by " + ANCHOR_ORDERNO +
    			" desc limit 0, 1", null);
    	if (cursor.moveToNext()) {
    		order_no = cursor.getInt(0);
    		cursor.close();
    	}
    	return order_no;
    }
    
    public boolean checkHasUpSectionPoints(int start_anchor_orderno, int end_anchor_orderno) {
    	boolean retVal = false;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_ORDERNO + ">=" + start_anchor_orderno + " and " +
    			ANCHOR_ORDERNO + "<=" + end_anchor_orderno, null);
    	while(cursor.moveToNext()) {
    		if (cursor.getInt(VOLUME_ANCHOR_GROUT_PRIORITY) == 0) {
    			retVal = true;
    			break;
    		}
    	}
    	if (cursor != null) cursor.close();
    	return retVal;
    }
    
    public boolean checkHasDownSectionPoints(int start_anchor_orderno, int end_anchor_orderno) {
    	boolean retVal = false;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_ORDERNO + ">=" + start_anchor_orderno + " and " +
    			ANCHOR_ORDERNO + "<=" + end_anchor_orderno, null);
    	while(cursor.moveToNext()) {
    		if (cursor.getInt(VOLUME_ANCHOR_GROUT_PRIORITY) > 0) {
    			retVal = true;
    			break;
    		}
    	}
    	if (cursor != null) cursor.close();
    	return retVal;
    }    
    
    public boolean checkIsUpSectionPoints(int anchor_orderno) {
    	boolean retVal = false;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_ORDERNO + "=" + anchor_orderno , null);
    	if (cursor.moveToNext()) {
    		if (cursor.getInt(VOLUME_ANCHOR_GROUT_PRIORITY) == 0) {
    			retVal = true;
    		}
    	}
    	if (cursor != null) cursor.close();
    	return retVal;
    }
    
    public boolean checkIsDownSectionPoints(int anchor_orderno) {
    	boolean retVal = false;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_ORDERNO + "=" + anchor_orderno , null);
    	if (cursor.moveToNext()) {
    		if (cursor.getInt(VOLUME_ANCHOR_GROUT_PRIORITY) > 0) {
    			retVal = true;
    		}
    	}
    	if (cursor != null) cursor.close();
    	return retVal;
    }
    
    public ArrayList<ManagerCraftParameter> getMgrCraftParameterPage(int mileage_page) {
    	ArrayList<ManagerCraftParameter> mlist = new ArrayList<ManagerCraftParameter>();
    	if (!isOpen) return mlist;
    	Cursor mileage_cursor = mDb.rawQuery("select " + MILEAGE_ID + " from " + 
    	    	MILEAGE_TABLE + " where " + MILEAGE_ORDERNO + "=" + mileage_page, null);
    	if (mileage_cursor.moveToNext()) {
    		AppUtil.log("getMgrCraftParameterPage-------mileage_id:"+mileage_cursor.getInt(0));
    		int mileage_id = mileage_cursor.getInt(0);
    		Cursor anchor_cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    				" where " + ANCHOR_MILEAGE_ID + "=" + mileage_id + " order by " + 
    	    				ANCHOR_ORDERNO, null);
    		int anchor_id = 0;
    		String anchor_name, anchor_type,anchor_model,date,start_date, end_date, mgr_remark;
    		double design_len, design_pressure, measure_pressure, thereo_cap, practice_cap;
    		int mileage_id_, design_hold_time, practice_hold_time;
    		AppUtil.log("getMgrCraftParameterPage==============mileage_id:"+mileage_id);
    		while(anchor_cursor.moveToNext()) {
    			anchor_id = anchor_cursor.getInt(VOLUME_ANCHOR_ID);   //wxd ?
    			anchor_name = anchor_cursor.getString(VOLUME_ANCHOR_NAME);
    			mileage_id_ = anchor_cursor.getInt(VOLUME_ANCHOR_MILEAGE_ID);
    			anchor_type = anchor_cursor.getString(VOLUME_ANCHOR_TYPE);
    			anchor_model = anchor_cursor.getString(VOLUME_ANCHOR_MODEL);
    			design_len = anchor_cursor.getDouble(VOLUME_ANCHOR_DESIGN_LENGTH);
    			design_pressure = anchor_cursor.getDouble(VOLUME_DESIGN_PRESSURE);
    			measure_pressure = anchor_cursor.getDouble(VOLUME_MEASURE_PRESSURE);
    			thereo_cap = anchor_cursor.getDouble(VOLUME_THEREO_CAPACITY);
    			practice_cap = anchor_cursor.getDouble(VOLUME_PRACTICE_CAPACITY);
    			date = getYMDformat(anchor_cursor.getString(VOLUME_SLURRY_DATE));
    			start_date = getHMSformat(anchor_cursor.getString(VOLUME_START_SLURRY_DATE));
    			end_date = getHMSformat(anchor_cursor.getString(VOLUME_END_SLURRY_DATE));
    			design_hold_time = anchor_cursor.getInt(VOLUME_DESIGN_HOLD_TIME);
    			practice_hold_time = anchor_cursor.getInt(VOLUME_PRACTICE_HOLD_TIME);
    			mgr_remark = anchor_cursor.getString(VOLUME_MGR_REMARK);
    			AppUtil.log( "id:"+anchor_id+ "   date:"+date + " start_date:"+start_date + " end_date:"+end_date);
    			mlist.add(new ManagerCraftParameter(anchor_id, anchor_name, mileage_id_, anchor_type, anchor_model, design_len, 
    					design_pressure, measure_pressure, thereo_cap, practice_cap, date, start_date, end_date,
    					design_hold_time, practice_hold_time, mgr_remark, 0));
    			WrcardParameter mWrParameter = getWrcardCraftParameter(anchor_name);
    			if (mWrParameter != null) {
//    			Log.d(TAG, "WrParameter:"+ (dev_seq -f_seq));
    				mlist.add(new ManagerCraftParameter(anchor_id, mWrParameter.getAnchorName(), mWrParameter.getMileageId(), mWrParameter.getAnchorType(), 
    						mWrParameter.getAnchorModel(), mWrParameter.getAnchorLength(), mWrParameter.getDesignPress(),
    						mWrParameter.getPracticePress(), mWrParameter.getThereoCap(), 
    						mWrParameter.getPracticeCap(), mWrParameter.getDate(), 
    						mWrParameter.getStartDate(), mWrParameter.getEndDate(),
    						mWrParameter.getDesignHoldTime(), mWrParameter.getPracticeHoldTime(),
    						mWrParameter.getRemark(), 1));
    			}
    		}
    		if (anchor_cursor != null)
    			anchor_cursor.close();
    		if (mileage_cursor != null)
    			mileage_cursor.close();
    	}
		return mlist;
    }
	    
	    public ManagerCraftParameter getMgrCraftParameter(String anchor_name) {
	    	ManagerCraftParameter mManagerCraftParameter = null;
	    	if (!isOpen) return null;
	    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
	    			" where " + ANCHOR_NAME + " like ?", new String[]{anchor_name});
	    	if(cursor.moveToNext()) {
	    		int anchor_id = cursor.getInt(VOLUME_ANCHOR_ID);
	    		int mileage_id = cursor.getInt(VOLUME_ANCHOR_MILEAGE_ID);
	    		String anchor_type = cursor.getString(VOLUME_ANCHOR_TYPE);
	    		String anchor_model = cursor.getString(VOLUME_ANCHOR_MODEL);
	    		double design_len = cursor.getDouble(VOLUME_ANCHOR_DESIGN_LENGTH);
	    		double design_pressure = cursor.getDouble(VOLUME_DESIGN_PRESSURE);
	    		double measure_pressure = cursor.getDouble(VOLUME_MEASURE_PRESSURE);
	    		int design_hold_time = cursor.getInt(VOLUME_DESIGN_HOLD_TIME);
	    		int practice_hold_time = cursor.getInt(VOLUME_PRACTICE_HOLD_TIME);
	    		double thereo_cap = cursor.getDouble(VOLUME_THEREO_CAPACITY);
	    		double practice_cap = cursor.getDouble(VOLUME_PRACTICE_CAPACITY);
	    		String date = getYMDformat(cursor.getString(VOLUME_SLURRY_DATE));
	    		String start_date = getHMSformat(cursor.getString(VOLUME_START_SLURRY_DATE));
	    		String end_date = getHMSformat(cursor.getString(VOLUME_END_SLURRY_DATE));
	    		String mgr_remark = cursor.getString(VOLUME_MGR_REMARK);
	    		mManagerCraftParameter = new ManagerCraftParameter(anchor_id, anchor_name, mileage_id, anchor_type, anchor_model,  
	    				design_len, design_pressure, measure_pressure, thereo_cap, practice_cap, date, start_date, end_date,
	    				design_hold_time, practice_hold_time, mgr_remark, 0);
	    		CacheManager.setDesignHoldTime(design_hold_time);
	    	}
	    	if (cursor != null) cursor.close();
			return mManagerCraftParameter;
	    }    
	
    public ManagerCraftParameter getMgrCraftParameter(int anchor_id) { 
    	ManagerCraftParameter mManagerCraftParameter = null;
    	if (!isOpen) return null;
    	if (anchor_id <= 0) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_ID + "=" + anchor_id , null);
    	AppUtil.log("getMgrCraftParameter=========anchor_id:"+anchor_id);
    	if(cursor.moveToNext()) {
    		String anchor_name = cursor.getString(VOLUME_ANCHOR_NAME);
    		int mileage_id = cursor.getInt(VOLUME_ANCHOR_MILEAGE_ID);
    		String anchor_type = cursor.getString(VOLUME_ANCHOR_TYPE);
    		String anchor_model = cursor.getString(VOLUME_ANCHOR_MODEL);
    		double design_len = cursor.getDouble(VOLUME_ANCHOR_DESIGN_LENGTH);
    		double design_pressure = cursor.getDouble(VOLUME_DESIGN_PRESSURE);
    		double measure_pressure = cursor.getDouble(VOLUME_MEASURE_PRESSURE);
    		int design_hold_time = cursor.getInt(VOLUME_DESIGN_HOLD_TIME);
    		int practice_hold_time = cursor.getInt(VOLUME_PRACTICE_HOLD_TIME);
    		double thereo_cap = cursor.getDouble(VOLUME_THEREO_CAPACITY);
    		double practice_cap = cursor.getDouble(VOLUME_PRACTICE_CAPACITY);
    		String date = getYMDformat(cursor.getString(VOLUME_SLURRY_DATE));
    		String start_date = getHMSformat(cursor.getString(VOLUME_START_SLURRY_DATE));
    		String end_date = getHMSformat(cursor.getString(VOLUME_END_SLURRY_DATE));
    		String mgr_remark = cursor.getString(VOLUME_MGR_REMARK);
    		mManagerCraftParameter = new ManagerCraftParameter(anchor_id, anchor_name, mileage_id, anchor_type, anchor_model,  
    				design_len, design_pressure, measure_pressure, thereo_cap, practice_cap, date, start_date, end_date,
    				design_hold_time, practice_hold_time, mgr_remark, 0);
    		CacheManager.setDesignHoldTime(design_hold_time);
    	}
    	if (cursor != null) cursor.close();
		return mManagerCraftParameter;
    }
    
    public ArrayList<MgrAnchorStasticParameter> getMgrAnchorStaticParameterList(int fromOderno, int toOderno) {
    	ArrayList<MgrAnchorStasticParameter> list = new ArrayList<MgrAnchorStasticParameter>();
    	if (!isOpen) return list;
    	ArrayList<String> type_model_list = new ArrayList<String>();
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_ORDERNO + ">=" + fromOderno + " and " +
    			ANCHOR_ORDERNO + "<=" + toOderno, null);
    	int type_model_size;
    	String anchor_type, anchor_model, anchor_type_model;
    	double design_len;
    	while(cursor.moveToNext()) {
    		anchor_type = cursor.getString(VOLUME_ANCHOR_TYPE);
    		anchor_model = cursor.getString(VOLUME_ANCHOR_MODEL);
    		anchor_type_model = anchor_type + "-" + anchor_model;
    		design_len = cursor.getDouble(VOLUME_ANCHOR_DESIGN_LENGTH);
    		type_model_size = type_model_list.size();
    		if (type_model_size == 0) {
    			list.add(new MgrAnchorStasticParameter(anchor_type, anchor_model, 0, 0, ""));
    			type_model_list.add(anchor_type_model);
    		} else {
    			int j;
        		for (j = 0; j < type_model_size; j++) {
        			if (anchor_type_model.equals(type_model_list.get(j))) {
        				break;
        			}
        		}
        		if (j == type_model_size) {
        			list.add(new MgrAnchorStasticParameter(anchor_type, anchor_model, 0, 0, ""));
        			type_model_list.add(anchor_type_model);
        		}
    		}
    		type_model_size = type_model_list.size();
    		for (int i = 0; i < type_model_size; i++) {
    			if (anchor_type_model.equals(type_model_list.get(i))) {
    				if (design_len > 0) {
    					list.get(i).setDesignSum(list.get(i).getDesignSum() + 1);
    					list.get(i).setDesignLength(list.get(i).getDesignLength() + design_len);
    				}
    				break;
    			}
    		}
    	}
    	if (cursor != null) cursor.close();
		return list;
    }
    
    public void updateMileageParamer(int seq, String mileage_name, int anchor_count,
    		String people, String date, double mixRatioCement, double mixRatioSand,
    		double mixRatioWater, double sectionMetre) {
    	if (!isOpen) return;
		mDb.execSQL("REPLACE INTO " + MILEAGE_TABLE + " ("
				+ MILEAGE_ID + ", "
				+ MILEAGE_NAME + ", " 
				+ MILEAGE_CREATE_PEOPLE + ", "
				+ MILEAGE_CREATE_DATE + " ,"
				+ MILEAGE_ANCHOR_COUNT + " ,"
				+ MILEAGE_MIX_RATIO_CEMENT + " ,"
				+ MILEAGE_MIX_RATIO_SAND + " ,"
				+ MILEAGE_MIX_RATIO_WATER + " ,"
				+ MILEAGE_SECTION_METRE +
				") VALUES ("+ seq + ",?, "
				+ "?, " + "?, " 
				+ anchor_count + "," 
				+ mixRatioCement + ","
				+ mixRatioSand + ","
				+ mixRatioWater + ","
				+ sectionMetre + ")", new Object[]{mileage_name, people, date}); 
    }
    
    private String calc(int second){
		  int h = 0;
		  int d = 0;
		  int s = 0;
		  int temp = second % 3600;
		  if(second > 3600)
		  {
			  h = second / 3600;
		      if(temp != 0) {
		    	  if(temp > 60) {
		    		  d = temp / 60;
		    		  if(temp % 60 != 0) {
		    			  s = temp % 60;
		    		  }
		    	  }	else {
		    		 s = temp;
		    	  }
		      } 
		  } else {
			  d = second / 60;
			  if(second % 60 != 0) {
				  s = second % 60;
			  }
		  }
		  return h+":"+d+":"+s;
	 }
    
    public void updateCraftAnchorSlurryDate(int anchor_id, String slurry_date, int seconds) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateCraftAnchorSlurryDate=======");
    	String date = "";
    	String start_date = "";
    	String end_date = "";
    	if (slurry_date.contains(" ")) {
			String[] slurry_dates = slurry_date.split(" ");
			if (slurry_dates.length == 2) {
				date = slurry_dates[0];
				end_date = slurry_dates[1];
				if (end_date.contains(":")) {
					String[] times = end_date.split(":");
					if (times.length == 3) {
						start_date = calc(Integer.parseInt(times[0]) * 3600 + Integer.parseInt(times[1]) * 60 + 
								Integer.parseInt(times[2]) -seconds -2);
					} 
				}
			}
		} else {
			date = slurry_date;
			start_date = end_date = "";
		}
    	ContentValues value = new ContentValues();
		value.put(SLURRY_DATE, date);
		value.put(START_SLURRY_DATE, start_date);
		value.put(END_SLURRY_DATE, end_date);
		mDb.update(ANCHOR_TABLE, value, ANCHOR_ID+"="+anchor_id, null);
    }
    
    public void updateWrcardCraftAnchorSlurryDate(String anchor_name, String slurry_date, int seconds) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateWrcardCraftAnchorSlurryDate=======");
    	String date = "";
    	String start_date = "";
    	String end_date = "";
    	if (slurry_date.contains(" ")) {
			String[] slurry_dates = slurry_date.split(" ");
			if (slurry_dates.length == 2) {
				date = slurry_dates[0];
				end_date = slurry_dates[1];
				if (end_date.contains(":")) {
					String[] times = end_date.split(":");
					if (times.length == 3) {
						start_date = calc(Integer.parseInt(times[0]) * 3600 + Integer.parseInt(times[1]) * 60 + 
								Integer.parseInt(times[2]) -seconds -2);
					} 
				}
			}
		} else {
			date = slurry_date;
			start_date = end_date = "";
		}
    	ContentValues value = new ContentValues();
		value.put(WRCARD_SLURRY_DATE, date);
		value.put(WRCARD_START_SLURRY_DATE, start_date);
		value.put(WRCARD_END_SLURRY_DATE, end_date);
		mDb.update(WRCARD_ANCHOR_TABLE, value, WRCARD_ANCHOR_NAME+" like ?", new String[]{anchor_name});
    }
    
    public void updateCraftAnchorSlurryDate(String anchor_name, String slurry_date, int seconds) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateCraftAnchorSlurryDate=======");
    	String date = "";
    	String start_date = "";
    	String end_date = "";
    	if (slurry_date.contains(" ")) {
			String[] slurry_dates = slurry_date.split(" ");
			if (slurry_dates.length == 2) {
				date = slurry_dates[0];
				end_date = slurry_dates[1];
				if (end_date.contains(":")) {
					String[] times = end_date.split(":");
					if (times.length == 3) {
						start_date = calc(Integer.parseInt(times[0]) * 3600 + Integer.parseInt(times[1]) * 60 + 
								Integer.parseInt(times[2]) -seconds -2);
					} 
				}
			}
		} else {
			date = slurry_date;
			start_date = end_date = "";
		}
    	AppUtil.log(date + " " + start_date + " " + end_date);
    	ContentValues value = new ContentValues();
		value.put(SLURRY_DATE, date);
		value.put(START_SLURRY_DATE, start_date);
		value.put(END_SLURRY_DATE, end_date);
		mDb.update(ANCHOR_TABLE, value, ANCHOR_NAME+" like ?", new String[]{anchor_name});
    }
    
    public void updateMileageAnchorCount(int mileage_id, int anchor_count) {
    	if (!isOpen) return;
    	AppUtil.log( "=========updateMileageAnchorCOUNT========");
    	mDb.execSQL("UPDATE " + MILEAGE_TABLE + " SET " + 
    			MILEAGE_ANCHOR_COUNT + "=" + anchor_count + " WHERE " + 
    			MILEAGE_ID + "=" + mileage_id);
    }
    
    public void updateMileageMixRatioCement(int mileage_id, double mixRatioCement) {
    	if (!isOpen) return;
    	AppUtil.log( "=========updateMileageMixRatioCement========");
    	ContentValues value = new ContentValues();
		value.put(MILEAGE_MIX_RATIO_CEMENT, mixRatioCement);
		mDb.update(MILEAGE_TABLE, value, MILEAGE_ID+"="+mileage_id, null);
    }
    
    public void updateMileageMixRatioSand(int mileage_id, double mixRatioSand) {
    	if (!isOpen) return;
    	AppUtil.log( "=========updateMileageMixRatioSand========");
    	ContentValues value = new ContentValues();
		value.put(MILEAGE_MIX_RATIO_SAND, mixRatioSand);
		mDb.update(MILEAGE_TABLE, value, MILEAGE_ID+"="+mileage_id, null);
    }
    
    public void updateMileageMixRatioWater(int mileage_id, double mixRatioWater) {
    	if (!isOpen) return;
    	AppUtil.log( "=========updateMileageMixRatioWater========");
    	ContentValues value = new ContentValues();
		value.put(MILEAGE_MIX_RATIO_WATER, mixRatioWater);
		mDb.update(MILEAGE_TABLE, value, MILEAGE_ID+"="+mileage_id, null);
    }
    
    public void updateMileageCreatePeople(int mileage_id, String people) {
    	if (!isOpen) return;
    	AppUtil.log( "=========updateMileageCreatePeople========");
    	ContentValues value = new ContentValues();
		value.put(MILEAGE_CREATE_PEOPLE, people);
		mDb.update(MILEAGE_TABLE, value, MILEAGE_ID + "=" + mileage_id, null);
    }
    
    public void updateMileageCreateDate(int mileage_id, String date) {
    	if (!isOpen) return;
    	AppUtil.log( "=========updateMileageCreateDate========");
    	ContentValues value = new ContentValues();
		value.put(MILEAGE_CREATE_DATE, date);
		mDb.update(MILEAGE_TABLE, value, MILEAGE_ID + "=" + mileage_id, null);
    }
    
    public MileageParameter getMileageParameter(String mileage_name) {
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE + 
    			" where " +MILEAGE_NAME+" like ?", new String[]{mileage_name});
    	MileageParameter mileage_parameter = null;
    	if(cursor.moveToNext()) {
    		int mileage_id = cursor.getInt(VOLUME_MILEAGE_ID);
    		String mileage_dist = cursor.getString(VOLUME_MILEAGE_NAME);
    		int anchor_count = cursor.getInt(VOLUME_MILEAGE_ANCHOR_COUNT);
    		String create_people = cursor.getString(VOLUME_MILEAGE_CREATE_PEOPLE);
    		String create_date = cursor.getString(VOLUME_MILEAGE_CREATE_DATE);
    		String water_ratio = cursor.getString(VOLUME_MILEAGE_MIX_RATIO_CEMENT) + ":"
					+ cursor.getString(VOLUME_MILEAGE_MIX_RATIO_SAND) + ":"
					+ cursor.getString(VOLUME_MILEAGE_MIX_RATIO_WATER);
    		mileage_parameter = new MileageParameter(mileage_id, mileage_dist, anchor_count, 
    				create_people, create_date, water_ratio);
    	}
    	if (cursor != null)
    		cursor.close();
		return mileage_parameter;
    }
    
    public int getMileageIdLikeMileageName(String mileage_name) {
    	if (!isOpen) return 0;
    	AppUtil.log( "getMileageIdLikeMileageName=====mileage_name:"+mileage_name);
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE + 
    			" where " +MILEAGE_NAME+" like ?", new String[]{mileage_name});
    	int mileage_id = 0;
    	if(cursor.moveToNext()) {
    		mileage_id = cursor.getInt(VOLUME_MILEAGE_ID);
    	}
    	if (cursor != null)
    		cursor.close();
    	return mileage_id;
    }
    
    public double getMileageSectionMetreWhereOrderno(int orderno) {
    	if (!isOpen) return 0;
    	AppUtil.log( "getMileageSectionMetreWhereOrderno=====orderno:"+orderno);
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE + 
    			" where " +MILEAGE_ORDERNO+" = "+orderno, null);
    	double section_metre = 0;
    	if(cursor.moveToNext()) {
    		section_metre = cursor.getInt(VOLUME_MILEAGE_SECTION_METRE);
    	}
    	if (cursor != null)
    		cursor.close();
    	return section_metre;
    }
    
    public int getMileageIdWhereSectionMetre(double section_metre) {
    	if (!isOpen) return 0;
    	AppUtil.log( "getMileageIdWhereSectionMetre=====section_metre:"+section_metre);
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE + 
    			" where " +MILEAGE_SECTION_METRE+" = "+section_metre, null);
    	int mileage_id = 0;
    	if(cursor.moveToNext()) {
    		mileage_id = cursor.getInt(VOLUME_MILEAGE_ID);
    	}
    	if (cursor != null)
    		cursor.close();
    	return mileage_id;
    }
    
    public int getMileageOrderNoLikeMileageName(String mileage_name) {
    	if (!isOpen) return 0;
    	AppUtil.log( "getMileageOrderNoLikeMileageName=====mileage_name:"+mileage_name);
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE + 
    			" where " +MILEAGE_NAME+" like ?", new String[]{mileage_name});
    	int orderno = 0;
    	if(cursor.moveToNext()) {
    		orderno = cursor.getInt(VOLUME_MILEAGE_ORDERNO);
    	}
    	if (cursor != null)
    		cursor.close();
    	return orderno;
    }
    
    public int getMileageOrderNoWhereSectonId(int sectionId) {
    	if (!isOpen) return 0;
    	AppUtil.log( "getMileageOrderNoWhereSectonId=====sectionId:"+sectionId);
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE + 
    			" where " +MILEAGE_ID+" = "+ sectionId, null);
    	int orderno = 0;
    	if(cursor.moveToNext()) {
    		orderno = cursor.getInt(VOLUME_MILEAGE_ORDERNO);
    	}
    	if (cursor != null)
    		cursor.close();
    	return orderno;
    }
    
    public int getMileageOrderNoWhereSectonMetre(double section_metre) {
    	if (!isOpen) return 0;
    	AppUtil.log( "getMileageOrderNoWhereSectonMetre=====section_metre:"+section_metre);
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE + 
    			" where " +MILEAGE_SECTION_METRE+" = "+ section_metre, null);
    	int orderno = 0;
    	if(cursor.moveToNext()) {
    		orderno = cursor.getInt(VOLUME_MILEAGE_ORDERNO);
    	}
    	if (cursor != null)
    		cursor.close();
    	return orderno;
    }
    
    public int getMileageCount() {  
    	Cursor cursor = mDb.rawQuery("select * from " + MILEAGE_TABLE, null);  
    	int count = 0;
    	if (cursor != null)
    		count = cursor.getCount();  
    	if (cursor != null)
    		cursor.close();  
    	AppUtil.log( "mileage_count:"+count);
    	return count; 
    } 
    
    public void updateAnchorCollectDataFromServer(int anchor_id, String groutingDate,
    		String beginTime, String endTime, double totalGroutingActual, 
    		double orificePressureActual, int holdPressureSecondsActual,
    		double full_hole_capacity, String cap_time_string, 
    		String press_time_string, String cap_string,
    		String press_string, String recvData) {
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			SLURRY_DATE + "=?," +
    			START_SLURRY_DATE + "=?," +
    			END_SLURRY_DATE + "=?," +
    			COLLECT_CAPACITY_TIME_STRING + "=?," +
    			COLLECT_PRESSURE_TIME_STRING + "=?," +
    			COLLECT_CAPACITY_STRING + "=?," +
    			COLLECT_PRESSURE_STRING + "=?," +
    			COLLECT_DATA_STRING + "=?," +
    			PRACTICE_CAPACITY + "=" + totalGroutingActual + ","+ 
    			MEASURE_PRESSURE + "=" + orificePressureActual + ","+ 
    			FULL_HOLE_CAPACITY + "=" + full_hole_capacity + "," +
    			CRAFT_UPLOAD_STATE + "=" + 1 + "," +
    			PRACTICE_HOLD_TIME + "=" + holdPressureSecondsActual +  " WHERE " + 
    			ANCHOR_ID + "=" + anchor_id,
    			new Object[]{groutingDate, beginTime, endTime, cap_time_string,
    				press_time_string, cap_string, press_string, recvData});
    	AppUtil.log(anchor_id+" "+groutingDate+" "+beginTime+" " + endTime+" "+totalGroutingActual + " "
    			+orificePressureActual+" "+holdPressureSecondsActual+" "+full_hole_capacity + " " 
    			+cap_time_string+" "+cap_string+" "+press_string + " " + recvData);
    }
    
    public void updateAnchorCollectData(String anchor_name, double design_pressure, 
    		double cap_unit_hour, double[] value_t, double[] value_p, int adc_points, String collect_data_string) 
    {
    	double hold_dn_press = design_pressure * 1.0;
		double hold_up_press = design_pressure * ConstDef.HOLD_UP_RATE;
		double cap_unit_sec = cap_unit_hour / 3600; //单位为ml
    	boolean isHoldUp = true;
    	double[] new_value_t = new double[1024];
    	double[] new_value_p = new double[1024];
    	double[] value_q = new double[1024];
//    	double[] value_pt = new double[1024];
    	double practice_pressure = 0;
    	int practice_hold_time = 0;
    	double practice_cap = 0;
    	double cur_t = -1;
    	new_value_t[0] = 0;
    	new_value_p[0] = 0;
    	int index = 0;
    	AppUtil.log("=============updateAnchorCollectData==============");
    	AppUtil.log("design_pressure:"+design_pressure + " hold_dn_press:"+hold_dn_press);
    	if (adc_points > 1024) adc_points = 1024;
    	int _adc_points = 0;
    	for (int i = 0; i < adc_points; i++) {
    		if (value_t[i] != cur_t) {
    			new_value_t[_adc_points] = value_t[i];
    			new_value_p[_adc_points] = value_p[i];
    			cur_t = value_t[i];
    			_adc_points++;
    		} else {
    			new_value_t[_adc_points-1] = value_t[i];
    			new_value_p[_adc_points-1] = value_p[i];
    		}
    		if (practice_pressure < value_p[i]) {
    			practice_pressure = value_p[i];
    		}
    	}
    	adc_points = _adc_points;
    	_adc_points = 0;
    	for (index = 0; index < adc_points; index++) {
    		if (new_value_p[index] >= hold_dn_press) {     //达到第一次泄压阀值,该点的值为设计孔口压力
    			if (new_value_p[index] <= new_value_p[index+1]) {  //过滤处理，直到拿到第一次压力上升的最高点
    				continue;
    			} else {
    				value_t[_adc_points] = new_value_t[index];  //取第一次最高点的时间值
    				hold_dn_press = (double) (design_pressure * 1.05);  //后续的泄压压力阀值=设计孔口压力的105%
    				_adc_points++;
    				for (int _index = index; _index < adc_points; _index++) {
						AppUtil.log( " " + value_t[_index] + " " + new_value_p[_index]);
						if (isHoldUp) { //进入泄压流程,取泄压最低点
							if (new_value_p[_index] <= hold_up_press) {	//达到泄压点
								if (new_value_p[_index] >= new_value_p[_index+1]) { //过滤处理，直到拿到泄压过程最低点
									continue;
								} else {
									value_t[_adc_points] = new_value_t[_index];	//取最低点的时间值
        							_adc_points++;
        							isHoldUp = false;
								}
							}
						} else { // //进入保压流程,取保压最高点
    						if (new_value_p[_index] >= hold_dn_press) {  //达到保压点
    							if (new_value_p[_index] <= new_value_p[_index+1]) {  //过滤处理，直到拿到保压过程最高点
    								continue;
    							} else {
    								value_t[_adc_points] = new_value_t[_index];
    								_adc_points++;
    								isHoldUp = true;
    							}
    						}
    					}
    				}
    				if (value_t[_adc_points-1] != new_value_t[adc_points-1]) { //取最后一个点
        				value_t[_adc_points] = new_value_t[adc_points-1];
        				_adc_points += 1;
        			}
        			break;
    			}
    		}
    	}
    	for (int i = 0; i < _adc_points; i++) {
    		if (i == 0) { //第一次的泄压压力点
    			value_q[i] = value_t[0]*cap_unit_sec; 
    		} else if (i % 2 == 1) { //泄压过程，注浆量不变化
    			value_q[i] = value_q[i-1];
    		} else if (i % 2 == 0) { //保压过程，压力计算
    			value_q[i] = value_q[i-1] + (value_t[i]-value_t[i-1])*cap_unit_sec;
    		}
    	}
    	if (adc_points > 0) {
    		if (_adc_points == 0) {  //未达到泄压压力
    			_adc_points = 1;
    			value_q[0] = value_t[adc_points - 1] *  cap_unit_sec;
    			value_t[0] = value_t[adc_points - 1];
    		}
    	}
    	DecimalFormat df = new DecimalFormat("##.##"); 
    	
    	if(_adc_points > 1) {
    		practice_hold_time = (int) Math.rint(Double.parseDouble(df.format(value_t[_adc_points - 1] - value_t[0])));
    		AppUtil.log(value_t[_adc_points - 1] + " "+value_t[0] + " "+practice_hold_time);
    	} else { //未达到保压，手动停止情况
			practice_hold_time = 0;
		}
    	AppUtil.log("cap:"+value_q[_adc_points - 1]);
    	practice_cap = Double.parseDouble(df.format(value_q[_adc_points - 1])); 
    	AppUtil.log("====================================");
    	AppUtil.log(practice_pressure + " " + practice_cap + " " + practice_hold_time);
    	if (!isOpen) return;
    	StringBuffer press_time_sb = new StringBuffer();
    	StringBuffer press_sb = new StringBuffer();
    	for (int i = 0; i < adc_points; i++) {
    		press_time_sb.append(new_value_t[i]).append(' ');
    		press_sb.append(new_value_p[i]).append(' ');
    	}
    	StringBuffer capacity_time_sb = new StringBuffer();
    	StringBuffer capacity_sb = new StringBuffer();
    	for (int i = 0; i < _adc_points; i++) {
    		capacity_time_sb.append(value_t[i]).append(' ');
    		capacity_sb.append(Double.parseDouble(df.format(value_q[i]))).append(' ');
    	}
    	String press_time_string = press_time_sb.toString();
    	String press_string = press_sb.toString();
    	String capacity_time_string = capacity_time_sb.toString();
    	String capacity_string = capacity_sb.toString();
    	
    	AppUtil.log("press_time_string:"+press_time_string);
    	AppUtil.log("press_string:"+press_string);
    	AppUtil.log("capacity_time_string:"+capacity_time_string);
    	AppUtil.log("capacity_string:"+capacity_string);
    	AppUtil.log("collect_data_string:"+collect_data_string);
    	
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			COLLECT_CAPACITY_STRING + "=?," +
    			COLLECT_CAPACITY_TIME_STRING + "=?," +
    			COLLECT_PRESSURE_STRING + "=?," +
    			COLLECT_PRESSURE_TIME_STRING + "=?," +
    			COLLECT_DATA_STRING + "=?," +
    			PRACTICE_HOLD_TIME + "=" + practice_hold_time + "," +
    			PRACTICE_CAPACITY + "=" + practice_cap + ","+ 
    			MEASURE_PRESSURE + "=" + practice_pressure + ","+
    			CRAFT_TRANSFER_STATE + "=" + 0 + " WHERE " + ANCHOR_NAME + " like ?",
    			new Object[]{capacity_string, capacity_time_string, press_string, press_time_string, collect_data_string,
    				anchor_name});
    	AppUtil.log("======================check data=============================");
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " +ANCHOR_NAME + " like ?", new String[]{anchor_name});
    	if(cursor.moveToNext()) {
    		String press_time_string_db = cursor.getString(VOLUME_COLLECT_PRESSURE_TIME_STRING);
    		String press_string_db = cursor.getString(VOLUME_COLLECT_PRESSURE_STRING);
    		String capacity_time_string_db = cursor.getString(VOLUME_COLLECT_CAPACITY_TIME_STRING);
    		String capacity_string_db = cursor.getString(VOLUME_COLLECT_CAPACITY_STRING);
    		String collect_data_string_db = cursor.getString(VOLUME_COLLECT_DATA_STRING);
    		
    		if (!press_time_string.equals(press_time_string_db)) {
    			AppUtil.log("update press_time_string error,update again.");
    			mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    	    			COLLECT_PRESSURE_TIME_STRING + "=?" +
    	    			 " WHERE " + ANCHOR_NAME + " like ?",
    	    			new Object[]{press_time_string, anchor_name});
    		} else {
    			AppUtil.log("update press_time_string ok.");
    		}
    		if (!press_string.equals(press_string_db)) {
    			AppUtil.log("update press_string error,update again.");
    			mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    	    			COLLECT_PRESSURE_STRING + "=?" +
    	    			 " WHERE " + ANCHOR_NAME + " like ?",
    	    			new Object[]{press_string, anchor_name});
    		} else {
    			AppUtil.log("update press_string ok.");
    		}
			if (!capacity_time_string.equals(capacity_time_string_db)) {
				AppUtil.log("update capacity_time_string error,update again.");
				mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    	    			COLLECT_CAPACITY_TIME_STRING + "=?" +
    	    			 " WHERE " + ANCHOR_NAME + " like ?",
    	    			new Object[]{capacity_time_string, anchor_name});
			} else {
				AppUtil.log("update capacity_time_string ok.");
			}
			if (!capacity_string.equals(capacity_string_db)) {
				AppUtil.log("update capacity_string error,update again.");
				mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    	    			COLLECT_CAPACITY_STRING + "=?" +
    	    			 " WHERE " + ANCHOR_NAME + " like ?",
    	    			new Object[]{capacity_string, anchor_name});
			} else {
				AppUtil.log("update capacity_string ok.");
			}
			if (!collect_data_string.equals(collect_data_string_db)) {
				AppUtil.log("update collect_data_string error,update again.");
				mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    	    			COLLECT_DATA_STRING + "=?" +
    	    			 " WHERE " + ANCHOR_NAME + " like ?",
    	    			new Object[]{collect_data_string, anchor_name});
			} else {
				AppUtil.log("update collect_data_string ok.");
			}
    	}
    	AppUtil.log("=======================================================");
    	if (cursor != null) cursor.close();
    }
    
    public void updateWrcardAnchorCollectData(String anchor_name, double design_pressure, 
    		double cap_unit_hour, double[] value_t, double[] value_p, int adc_points) 
    {
    	double hold_dn_press = design_pressure * 1.0;
		double hold_up_press = design_pressure * ConstDef.HOLD_UP_RATE;
		double cap_unit_sec = cap_unit_hour / 3600; //单位为ml
    	boolean isHoldUp = true;
    	double[] new_value_t = new double[1024];
    	double[] new_value_p = new double[1024];
    	double[] value_q = new double[1024];
    	double[] value_pt = new double[1024];
    	double practice_pressure = 0;
    	int practice_hold_time = 0;
    	double practice_cap = 0;
    	double cur_t = -1;
    	new_value_t[0] = 0;
    	new_value_p[0] = 0;
    	int index = 0;
    	if (adc_points > 1024) adc_points = 1024;
    	int _adc_points = 0;
    	for (int i = 0; i < adc_points; i++) {
    		value_pt[i] = value_t[i];
    		if (value_t[i] != cur_t) {
    			new_value_t[_adc_points] = value_t[i];
    			new_value_p[_adc_points] = value_p[i];
    			cur_t = value_t[i];
    			_adc_points++;
    		}
    		if (practice_pressure < value_p[i]) {
    			practice_pressure = value_p[i];
    		}
    	}
    	adc_points = _adc_points;
    	_adc_points = 0;
    	for (index = 0; index < adc_points; index++) {
    		if (new_value_p[index] >= hold_dn_press) {     //达到第一次泄压阀值,该点的值为设计孔口压力
    			if (new_value_p[index] <= new_value_p[index+1]) {  //过滤处理，直到拿到第一次压力上升的最高点
    				continue;
    			} else {
    				value_t[_adc_points] = new_value_t[index];  //取第一次最高点的时间值
    				hold_dn_press = (double) (design_pressure * 1.05);  //后续的泄压压力阀值=设计孔口压力的105%
    				_adc_points++;
    				for (int _index = index; _index < adc_points; _index++) {
						AppUtil.log( " " + value_t[_index] + " " + new_value_p[_index]);
						if (isHoldUp) { //进入泄压流程,取泄压最低点
							if (new_value_p[_index] <= hold_up_press) {	//达到泄压点
								if (new_value_p[_index] >= new_value_p[_index+1]) { //过滤处理，直到拿到泄压过程最低点
									continue;
								} else {
									value_t[_adc_points] = new_value_t[_index];	//取最低点的时间值
        							_adc_points++;
        							isHoldUp = false;
								}
							}
						} else { // //进入保压流程,取保压最高点
    						if (new_value_p[_index] >= hold_dn_press) {  //达到保压点
    							if (new_value_p[_index] <= new_value_p[_index+1]) {  //过滤处理，直到拿到保压过程最高点
    								continue;
    							} else {
    								value_t[_adc_points] = new_value_t[_index];
    								_adc_points++;
    								isHoldUp = true;
    							}
    						}
    					}
    				}
    				if (value_t[_adc_points-1] != new_value_t[adc_points-1]) { //取最后一个点
        				value_t[_adc_points] = new_value_t[adc_points-1];
        				_adc_points += 1;
        			}
        			break;
    			}
    		}
    	}
    	for (int i = 0; i < _adc_points; i++) {
    		if (i == 0) { //第一次的泄压压力点
    			value_q[i] = value_t[0]*cap_unit_sec; 
    		} else if (i % 2 == 1) { //泄压过程，注浆量不变化
    			value_q[i] = value_q[i-1];
    		} else if (i % 2 == 0) { //保压过程，压力计算
    			value_q[i] = value_q[i-1] + (value_t[i]-value_t[i-1])*cap_unit_sec;
    		}
    	}
    	if (adc_points > 0) {
    		if (_adc_points == 0) {  //未达到泄压压力
    			_adc_points = 1;
    			value_q[0] = value_t[adc_points - 1] *  cap_unit_sec;
    			value_t[0] = value_t[adc_points - 1];
    		}
    	}
    	DecimalFormat df = new DecimalFormat("##.##"); 
    	if(_adc_points > 1) {
    		practice_hold_time = (int) Math.rint(Double.parseDouble(df.format(value_t[_adc_points - 1] - value_t[0])));
    	} else { //未达到保压，手动停止情况
			practice_hold_time = 0;
		}
    	practice_cap = Double.parseDouble(df.format(value_q[_adc_points - 1])); 
    	if (!isOpen) return;
    	StringBuffer press_time_sb = new StringBuffer();
    	StringBuffer press_sb = new StringBuffer();
    	for (int i = 0; i < adc_points; i++) {
    		press_time_sb.append(value_pt[i]).append(' ');
    		press_sb.append(value_p[i]).append(' ');
    	}
    	StringBuffer capacity_time_sb = new StringBuffer();
    	StringBuffer capacity_sb = new StringBuffer();
    	for (int i = 0; i < _adc_points; i++) {
    		capacity_time_sb.append(value_t[i]).append(' ');
    		capacity_sb.append(value_q[i]).append(' ');
    	}
    	String press_time_string = press_time_sb.toString();
    	String press_string = press_sb.toString();
    	String capacity_time_string = capacity_time_sb.toString();
    	String capacity_string = capacity_sb.toString();
    	
    	mDb.execSQL("UPDATE " + WRCARD_ANCHOR_TABLE + " SET " + 
    			WRCARD_COLLECT_CAPACITY_STRING + "=?," +
    			WRCARD_COLLECT_CAPACITY_TIME_STRING + "=?," +
    			WRCARD_COLLECT_PRESSURE_STRING + "=?," +
    			WRCARD_COLLECT_PRESSURE_TIME_STRING + "=?," +
    			WRCARD_PRACTICE_HOLD_TIME + "=" + practice_hold_time + "," +
    			WRCARD_PRACTICE_CAPACITY + "=" + practice_cap + ","+ 
    			WRCARD_MEASURE_PRESSURE + "=" + practice_pressure + 
    			" WHERE " + WRCARD_ANCHOR_NAME + " like ?",
    			new Object[]{capacity_string, capacity_time_string, press_string, press_time_string,
    				anchor_name});
    }
    
    public void updateAnchorCollectPressureTime(String anchor_name, double[] data, int len) 
    {
    	if (!isOpen) return;
    	StringBuffer data_sb = new StringBuffer();
    	for (int i = 0; i < len; i++) {
    		data_sb.append(data[i]).append(' ');
    	}
    	String data_str = data_sb.toString();
    	AppUtil.log( "===========updateAnchorCollectPressureTime==============");
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			COLLECT_PRESSURE_TIME_STRING + "=?"  + " WHERE " + 
    			ANCHOR_NAME + " like ?", new Object[]{data_str, anchor_name});
    }
    
    public void updateAnchorCollectPressureTime(int anchor_id, double[] data, int len) 
    {
    	if (!isOpen) return;
    	StringBuffer data_sb = new StringBuffer();
    	for (int i = 0; i < len; i++) {
    		data_sb.append(data[i]).append(' ');
    	}
    	String data_str = data_sb.toString();
    	AppUtil.log( "===========updateAnchorCollectPressureTime==============");
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			COLLECT_PRESSURE_TIME_STRING + "=?"  + " WHERE " + 
    			ANCHOR_ID + "=" + anchor_id, new Object[]{data_str});
    }
    
    
    public void updateAnchorCollectCapacityTime(int anchor_id, double[] data, int len) {
    	if (!isOpen) return;
    	StringBuffer data_sb = new StringBuffer();
    	for (int i = 0; i < len; i++) {
    		data_sb.append(data[i]).append(' ');
    	}
    	String data_str = data_sb.toString();
    	AppUtil.log( "===========updateAnchorCollectCapacityTime==============");
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			COLLECT_CAPACITY_TIME_STRING + "=?"  + " WHERE " + 
    			ANCHOR_ID + "=" + anchor_id, new Object[]{data_str});
    }
    
    public void updateAnchorCollectPressure(int anchor_id, double[] data, int len) {
    	if (!isOpen) return;
    	StringBuffer data_sb = new StringBuffer();
    	for (int i = 0; i < len; i++) {
    		data_sb.append(data[i]).append(' ');
    	}
    	String data_str = data_sb.toString();
    	AppUtil.log( "===========updateAnchorCollectPressure==============");
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			COLLECT_PRESSURE_STRING + "=?"  + " WHERE " + 
    			ANCHOR_ID + "=" + anchor_id, new Object[]{data_str});
    }
    
    public void updateAnchorCollectPressure(String anchor_name, double[] data, int len) {
    	if (!isOpen) return;
    	StringBuffer data_sb = new StringBuffer();
    	for (int i = 0; i < len; i++) {
    		data_sb.append(data[i]).append(' ');
    	}
    	String data_str = data_sb.toString();
    	AppUtil.log( "===========updateAnchorCollectPressure==============");
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			COLLECT_PRESSURE_STRING + "=?"  + " WHERE " + 
    			ANCHOR_NAME + " like ?", new Object[]{data_str, anchor_name});
    }
    
    public void updateAnchorCollectCapacity(int anchor_id, double[] data, int len) {
    	if (!isOpen) return;
    	StringBuffer data_sb = new StringBuffer();
    	for (int i = 0; i < len; i++) {
    		data_sb.append(data[i]).append(' ');
    	}
    	String data_str = data_sb.toString();
    	AppUtil.log( "===========updateAnchorCollectCapacity==============");
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			COLLECT_CAPACITY_STRING + "=?"  + " WHERE " + 
    			ANCHOR_ID + "=" + anchor_id, new Object[]{data_str});
    }
    
    public void updateAnchorCollectCapacity(String anchor_name, double[] data, int len) {
    	if (!isOpen) return;
    	StringBuffer data_sb = new StringBuffer();
    	for (int i = 0; i < len; i++) {
    		data_sb.append(data[i]).append(' ');
    	}
    	String data_str = data_sb.toString();
    	AppUtil.log( "===========updateAnchorCollectCapacity==============");
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			COLLECT_CAPACITY_STRING + "=?"  + " WHERE " + 
    			ANCHOR_NAME + " like ?", new Object[]{data_str, anchor_name});
    }
    
    public float[] getWrcardAnchorCollectPressureTimeNodeData(String anchor_name) { 
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + WRCARD_ANCHOR_TABLE + 
    			" where " +WRCARD_ANCHOR_NAME + " like ?", new String[]{anchor_name});
    	CacheManager.setLenTimeNode(0);
    	if(cursor.moveToNext()) {
    		String sample_time_node = cursor.getString(VOLUME_WRCARD_COLLECT_PRESSURE_TIME_STRING);
    		if (sample_time_node != null) {
    			if (!sample_time_node.equals("") && sample_time_node.length() > 0) {
    				float[] time_node = new float[1024];
    				String[] strs = sample_time_node.split(" ");
    				int len_time_node = strs.length;
    				AppUtil.log( "sample_time_node:"+sample_time_node);
//    				AppUtil.log( "len_time_node:" + len_time_node);
    				CacheManager.setLenTimeNode(len_time_node);
    				for (int i = 0; i < len_time_node; i++) {
    					time_node[i] = Float.parseFloat(strs[i]);
//    					AppUtil.log( " " + time_node[i]);
    				}
    				if (cursor != null)
    					cursor.close();
    				return time_node;
    			}
    		}
    	}
    	if (cursor != null)
    		cursor.close();
    	return null;
    }
    
    public float[] getAnchorCollectPressureTimeNodeData(int anchor_id) { 
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " +ANCHOR_ID + "=" + anchor_id, null);
    	CacheManager.setLenTimeNode(0);
    	if(cursor.moveToNext()) {
    		String sample_time_node = cursor.getString(VOLUME_COLLECT_PRESSURE_TIME_STRING);
    		if (sample_time_node != null) {
    			if (!sample_time_node.equals("") && sample_time_node.length() > 0) {
    				float[] time_node = new float[1024];
    				String[] strs = sample_time_node.split(" ");
    				int len_time_node = strs.length;
    				AppUtil.log( "sample_time_node:"+sample_time_node);
//    				AppUtil.log( "len_time_node:" + len_time_node);
    				CacheManager.setLenTimeNode(len_time_node);
    				for (int i = 0; i < len_time_node; i++) {
    					time_node[i] = Float.parseFloat(strs[i]);
//    					AppUtil.log( " " + time_node[i]);
    				}
    				if (cursor != null)
    					cursor.close();
    				return time_node;
    			}
    		}
    	}
    	if (cursor != null)
    		cursor.close();
    	return null;
    }
    
    public float[] getWrcardAnchorCollectCapacityTimeNodeData(String anchor_name) { 
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + WRCARD_ANCHOR_TABLE + 
    			" where " +WRCARD_ANCHOR_NAME + " like ?", new String[]{anchor_name} );
    	CacheManager.setLenTimeNode(0);
    	if(cursor.moveToNext()) {
    		String sample_time_node = cursor.getString(VOLUME_WRCARD_COLLECT_CAPACITY_TIME_STRING);
    		if (sample_time_node != null) {
    			if (!sample_time_node.equals("") && sample_time_node.length() > 0) {
    				float[] time_node = new float[1024];
    				String[] strs = sample_time_node.split(" ");
    				int len_time_node = strs.length;
    				AppUtil.log( "sample_time_node:"+sample_time_node);
//    				AppUtil.log( "len_time_node:" + len_time_node);
    				CacheManager.setLenTimeNode(len_time_node);
    				for (int i = 0; i < len_time_node; i++) {
    					time_node[i] = Float.parseFloat(strs[i]);
//    					AppUtil.log( " " + time_node[i]);
    				}
    				if (cursor != null)
    					cursor.close();
    				return time_node;
    			}
    		}
    	}
    	if (cursor != null)
    		cursor.close();
    	return null;
    }
    
    public float[] getAnchorCollectCapacityTimeNodeData(int anchor_id) { 
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " +ANCHOR_ID + "=" + anchor_id, null);
    	CacheManager.setLenTimeNode(0);
    	if(cursor.moveToNext()) {
    		String sample_time_node = cursor.getString(VOLUME_COLLECT_CAPACITY_TIME_STRING);
    		if (sample_time_node != null) {
    			if (!sample_time_node.equals("") && sample_time_node.length() > 0) {
    				float[] time_node = new float[1024];
    				String[] strs = sample_time_node.split(" ");
    				int len_time_node = strs.length;
    				AppUtil.log( "sample_time_node:"+sample_time_node);
//    				AppUtil.log( "len_time_node:" + len_time_node);
    				CacheManager.setLenTimeNode(len_time_node);
    				for (int i = 0; i < len_time_node; i++) {
    					time_node[i] = Float.parseFloat(strs[i]);
//    					AppUtil.log( " " + time_node[i]);
    				}
    				if (cursor != null)
    					cursor.close();
    				return time_node;
    			}
    		}
    	}
    	if (cursor != null)
    		cursor.close();
    	return null;
    }
    
    public float[] getWrcardAnchorCollectPressureData(String anchor_name) {
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + WRCARD_ANCHOR_TABLE + 
    			" where " +WRCARD_ANCHOR_NAME + " like ?", new String[]{anchor_name});
    	CacheManager.setLenTimeNode(0);
    	if(cursor.moveToNext()) {
    		String sample_pressure_val = cursor.getString(VOLUME_WRCARD_COLLECT_PRESSURE_STRING);
    		if (sample_pressure_val!=null && !sample_pressure_val.equals("") && sample_pressure_val.length() > 0) {
    			float[] pressure_vals = new float[1024];
    			String[] strs = sample_pressure_val.split(" ");
    			int len_time_node = strs.length;
//    			AppUtil.log( "len_time_node:" + len_time_node);
    			AppUtil.log("pressure_string:"+sample_pressure_val);
    			CacheManager.setLenTimeNode(len_time_node);
    			for (int i = 0; i < len_time_node; i++) {
    				pressure_vals[i] = Float.parseFloat(strs[i]);
//    				AppUtil.log( " " + pressure_vals[i]);
    			}
    			if (cursor != null)
    	    		cursor.close();
    			return pressure_vals;
    		}
    	}
    	if (cursor != null)
    		cursor.close();
    	return null;
    }
    
    public float[] getAnchorCollectPressureData(int anchor_id) {
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " +ANCHOR_ID + "=" + anchor_id, null);
    	CacheManager.setLenTimeNode(0);
    	if(cursor.moveToNext()) {
    		String sample_pressure_val = cursor.getString(VOLUME_COLLECT_PRESSURE_STRING);
    		if (sample_pressure_val!=null && !sample_pressure_val.equals("") && sample_pressure_val.length() > 0) {
    			float[] pressure_vals = new float[1024];
    			String[] strs = sample_pressure_val.split(" ");
    			int len_time_node = strs.length;
//    			AppUtil.log( "len_time_node:" + len_time_node);
    			AppUtil.log("pressure_string:"+sample_pressure_val);
    			CacheManager.setLenTimeNode(len_time_node);
    			for (int i = 0; i < len_time_node; i++) {
    				pressure_vals[i] = Float.parseFloat(strs[i]);
//    				AppUtil.log( " " + pressure_vals[i]);
    			}
    			if (cursor != null)
    	    		cursor.close();
    			return pressure_vals;
    		}
    	}
    	if (cursor != null)
    		cursor.close();
    	return null;
    }
    
    public float[] getWrcardAnchorCollectCapacityData(String anchor_name) {
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + WRCARD_ANCHOR_TABLE + 
    			" where " +WRCARD_ANCHOR_NAME + " like ?", new String[]{anchor_name});
    	CacheManager.setLenTimeNode(0);
    	if(cursor.moveToNext()) {
    		String sample_capacity_val = cursor.getString(VOLUME_WRCARD_COLLECT_CAPACITY_STRING);
    		if (sample_capacity_val!=null && !sample_capacity_val.equals("") && sample_capacity_val.length() > 0) {
    			float[] capacity_vals = new float[1024];
    			String[] strs = sample_capacity_val.split(" ");
    			int len_time_node = strs.length;
//    			AppUtil.log( "len_time_node:" + len_time_node);
    			AppUtil.log("capacity_string:"+sample_capacity_val);
    			CacheManager.setLenTimeNode(len_time_node);
    			for (int i = 0; i < len_time_node; i++) {
    				capacity_vals[i] = Float.parseFloat(strs[i]);
//    				AppUtil.log( " " + capacity_vals[i]);
    			}
    			if (cursor != null)
    	    		cursor.close();
    			return capacity_vals;
    		}
    	}
    	if (cursor != null)
    		cursor.close();
    	return null;
    }
    
    public float[] getAnchorCollectCapacityData(int anchor_id) {
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " +ANCHOR_ID + "=" + anchor_id, null);
    	CacheManager.setLenTimeNode(0);
    	if(cursor.moveToNext()) {
    		String sample_capacity_val = cursor.getString(VOLUME_COLLECT_CAPACITY_STRING);
    		if (sample_capacity_val!=null && !sample_capacity_val.equals("") && sample_capacity_val.length() > 0) {
    			float[] capacity_vals = new float[1024];
    			String[] strs = sample_capacity_val.split(" ");
    			int len_time_node = strs.length;
//    			AppUtil.log( "len_time_node:" + len_time_node);
    			AppUtil.log("capacity_string:"+sample_capacity_val);
    			CacheManager.setLenTimeNode(len_time_node);
    			for (int i = 0; i < len_time_node; i++) {
    				capacity_vals[i] = Float.parseFloat(strs[i]);
//    				AppUtil.log( " " + capacity_vals[i]);
    			}
    			if (cursor != null)
    	    		cursor.close();
    			return capacity_vals;
    		}
    	}
    	if (cursor != null)
    		cursor.close();
    	return null;
    }
    
    public int getAnchorTotalCount() {  
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE,null);  
    	int count = 0;
    	if (cursor != null)
    		count = cursor.getCount();  
    	if (cursor != null)
    		cursor.close();  
    	return count; 
    } 
   
    public void updateCraftUploadState(int orderNo, int state) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateCraftUploadState=======orderNo:"+orderNo);
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			CRAFT_UPLOAD_STATE + "=" + state + " WHERE " + 
    			ANCHOR_ORDERNO + "=" + orderNo);
    }
    
    public void updateCraftUploadState(String anchor_name, int state) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateCraftUploadState=======");
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			CRAFT_UPLOAD_STATE + "=" + state + " WHERE " + 
    			ANCHOR_NAME + " like ?", new String[]{anchor_name});
    }
    
    public void updateCraftTransferState(int rowID, int state) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateCraftTransferState=======rowID:"+rowID);
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			CRAFT_TRANSFER_STATE + "=" + state + " WHERE " + 
    			ANCHOR_ID + "=" + rowID);
    }
    
    public void updateCraftTransferState(String anchor_name, int state) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateCraftTransferState=======");
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			CRAFT_TRANSFER_STATE + "=" + state + " WHERE " + 
    			ANCHOR_NAME + " like ?", new String[]{anchor_name});
    }
    
    public void updateCraftPracticeHoldTime(String anchor_name, int hold_time) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateCraftPracticeHoldTime=======");
    	ContentValues value = new ContentValues();
		value.put(PRACTICE_HOLD_TIME, hold_time);
		value.put(CRAFT_TRANSFER_STATE, 0);
		mDb.update(ANCHOR_TABLE, value, ANCHOR_NAME + " like ?", new String[]{anchor_name});
    }
    
    public void updateCraftColumeTransferState(int fromOrderno, int toOrderno, int state) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateCraftColumeTransferState=======");
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			CRAFT_TRANSFER_STATE + "=" + state + " WHERE " + 
    			ANCHOR_ID + ">=" + fromOrderno + " and " +
    			ANCHOR_ID + "<=" + toOrderno);
    }
    
    public void deleteCraftParamRecord(int id, int mileage_id) {
    	if (!isOpen) return;
		AppUtil.log( "========deleteCraftParamRecord=======");
		mDb.delete(ANCHOR_TABLE, ANCHOR_ID + "=" + id, null);
		Cursor cursor1 = mDb.rawQuery("select * from " + ANCHOR_TABLE + " where "
    			+ ANCHOR_ID +">"+id , null);
    	int seq = 0;
    	ContentValues value1 = new ContentValues();
    	long startTime, endTime;
    	startTime = System.currentTimeMillis();
    	mDb.beginTransaction();
    	while(cursor1.moveToNext()) {
    		seq = cursor1.getInt(VOLUME_ANCHOR_ID);
    		AppUtil.log( "seq=========="+seq);
    		value1.put(ANCHOR_ID, seq-1);
    		mDb.update(ANCHOR_TABLE, value1, ANCHOR_ID+"="+seq, null);
    	}
    	mDb.setTransactionSuccessful(); 
        mDb.endTransaction(); 

    	endTime = System.currentTimeMillis();
    	AppUtil.log( "code1 time:"+(endTime - startTime)+"(ms)");
    	if (cursor1 != null)
    		cursor1.close();
    }
    
    public int getAnchorOrderNo(int anchor_id) {
    	if (!isOpen) return 0;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_ID + "=" + anchor_id, null);
    	int orderno = 0;
    	if(cursor.moveToNext()) {
    		orderno = cursor.getInt(VOLUME_ANCHOR_ORDERNO);
    		cursor.close();
    	}    	
    	return orderno;
    }
    
    public void deleteCraftParamRecord(int anchor_id) {
    	if (!isOpen) return;
		AppUtil.log( "========deleteCraftParamRecord=======");
		mDb.delete(ANCHOR_TABLE, ANCHOR_ID + "=" + anchor_id, null);
    }
    
    public void deleteCraftParameter(int mileage_id) {
    	if (!isOpen) return;
		mDb.delete(ANCHOR_TABLE, ANCHOR_MILEAGE_ID + "=" + mileage_id , null);
    }
    
    public double getMaxSectionMetreFromAnchorTable() {
    	double value = 0;
    	Cursor cursor = mDb.rawQuery("select " + ANCHOR_SECTION_METRE + " from " + 
    			ANCHOR_TABLE + " order by " + ANCHOR_SECTION_METRE + " desc limit 0, 1" , null);
    	if (cursor.moveToNext()) {
    		value = cursor.getDouble(0);
    		cursor.close();
    	}
    	return value;
    }
    
    public void insertCraftParam(int anchor_id, String anchor_name, int mileage_id, String anchor_type, String anchor_model, double design_len, double design_press,
    		int hold_time, double design_cap, double cap_unit_meter, double cap_unit_hour, double full_hole_pressure, String remark, double section_metre, int grout_priority) {
    		if (!isOpen) return;
    		AppUtil.log( "========insertCraftParam=======");
    		AppUtil.log("anchor_name:"+anchor_name + " section_metre:"+section_metre);
    		mDb.execSQL("REPLACE INTO " + ANCHOR_TABLE + " ("
    				+ ANCHOR_ID + ", " 
    				+ ANCHOR_MILEAGE_ID + ", "
    				+ ANCHOR_NAME + ", "
    				+ ANCHOR_TYPE + ", "
    				+ ANCHOR_MODEL + ", "
    				+ ANCHOR_DESIGN_LENGTH + ", "
    				+ DESIGN_PRESSURE + ", "
    				+ DESIGN_HOLD_TIME + ","
    				+ THEREO_CAPACITY + ", "
    				+ CAP_UNIT_METER + ", "
    				+ CAP_UNIT_HOUR + ", "
    				+ FULL_HOLE_PRESSURE + ", "
    				+ ANCHOR_REMARK + ", "
    				+ CRAFT_TRANSFER_STATE + ", "
    				+ CRAFT_UPLOAD_STATE + ", "
    				+ ANCHOR_SECTION_METRE +", "
    				+ ANCHOR_GROUT_PRIORITY +
    				")" +
    				" VALUES (" + anchor_id + "," + mileage_id + ", ? , ? , ? ,"
    				+ design_len + ", " +
    				design_press + ", " + hold_time + ", " + design_cap + ", " +
    				cap_unit_meter + ", " + cap_unit_hour+ ", " + full_hole_pressure +
    				", ? ," + 0 + "," + 0 +"," + section_metre + "," + grout_priority +")", new Object[]{anchor_name, anchor_type, anchor_model, remark});
    }
    
    public AnchorParameter getCraftParameter(int anchor_id) {
    	AnchorParameter mParameter = null;
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_ID + "==" + anchor_id, null);
    	if (cursor.moveToNext()) {
    		String anchor_name = cursor.getString(VOLUME_ANCHOR_NAME);
			String anchor_type = cursor.getString(VOLUME_ANCHOR_TYPE);
			String anchor_model = cursor.getString(VOLUME_ANCHOR_MODEL);
			double design_len = cursor.getDouble(VOLUME_ANCHOR_DESIGN_LENGTH);
			double design_press = cursor.getDouble(VOLUME_DESIGN_PRESSURE);
			int hold_time = cursor.getInt(VOLUME_DESIGN_HOLD_TIME);
			double design_cap = cursor.getDouble(VOLUME_THEREO_CAPACITY);
			double cap_unit_meter = cursor.getDouble(VOLUME_CAP_UNIT_METER);
			double cap_unit_hour = cursor.getDouble(VOLUME_CAP_UNIT_HOUR);
			double full_hole_pressure = cursor.getDouble(VOLUME_FULL_HOLE_PRESSURE);
			double full_hole_capacity = cursor.getDouble(VOLUME_FULL_HOLE_CAPACITY);
			String remark = cursor.getString(VOLUME_ANCHOR_REMARK);
			int grout_priority = cursor.getInt(VOLUME_ANCHOR_GROUT_PRIORITY);
			String transfer_state = "";
			if (cursor.getInt(VOLUME_CRAFT_TRANSFER_STATE) == 1) {
				transfer_state = "*";
			}
    		mParameter = new AnchorParameter(anchor_name, anchor_type, anchor_model, design_len, 
					design_press, hold_time, design_cap, cap_unit_meter, cap_unit_hour, 
					full_hole_pressure, full_hole_capacity, remark, transfer_state, grout_priority);
    		cursor.close();
    	}
    	return mParameter;
    }
    
    public AnchorParameter getCraftParameterByOrderno(int orderno) {
    	AnchorParameter mParameter = null;
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_ORDERNO + "==" + orderno, null);
    	if (cursor.moveToNext()) {
    		String anchor_name = cursor.getString(VOLUME_ANCHOR_NAME);
			String anchor_type = cursor.getString(VOLUME_ANCHOR_TYPE);
			String anchor_model = cursor.getString(VOLUME_ANCHOR_MODEL);
			double design_len = cursor.getDouble(VOLUME_ANCHOR_DESIGN_LENGTH);
			double design_press = cursor.getDouble(VOLUME_DESIGN_PRESSURE);
			int hold_time = cursor.getInt(VOLUME_DESIGN_HOLD_TIME);
			double design_cap = cursor.getDouble(VOLUME_THEREO_CAPACITY);
			double cap_unit_meter = cursor.getDouble(VOLUME_CAP_UNIT_METER);
			double cap_unit_hour = cursor.getDouble(VOLUME_CAP_UNIT_HOUR);
			double full_hole_pressure = cursor.getDouble(VOLUME_FULL_HOLE_PRESSURE);
			double full_hole_capacity = cursor.getDouble(VOLUME_FULL_HOLE_CAPACITY);
			String remark = cursor.getString(VOLUME_ANCHOR_REMARK);
			int grout_priority = cursor.getInt(VOLUME_ANCHOR_GROUT_PRIORITY);
			String transfer_state = "";
			if (cursor.getInt(VOLUME_CRAFT_TRANSFER_STATE) == 1) {
				transfer_state = "*";
			}
    		mParameter = new AnchorParameter(anchor_name, anchor_type, anchor_model, design_len, 
					design_press, hold_time, design_cap, cap_unit_meter, cap_unit_hour, 
					full_hole_pressure, full_hole_capacity, remark, transfer_state, grout_priority);
    		cursor.close();
    	}
    	return mParameter;
    }
    
    public void updateCollectData(String anchor_name, String data) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateCollectData=======");
    	AppUtil.log(anchor_name+":"+data);
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			COLLECT_DATA_STRING + "=?" +
    			 " WHERE " + ANCHOR_NAME + " like ?",
    			new Object[]{data, anchor_name});
    }
    
    public void updateFullHoleParameter(String anchor_name, double full_hole_pressure, 
    		double full_hole_capacity) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateFullHoleParameter=======");
    	AppUtil.log("full_hole_pressure:" + full_hole_pressure + 
				 " full_hole_capacity:" + full_hole_capacity);
    	mDb.execSQL("UPDATE " + ANCHOR_TABLE + " SET " + 
    			FULL_HOLE_PRESSURE + "=" + full_hole_pressure + "," +
    			FULL_HOLE_CAPACITY + "=" + full_hole_capacity + 
    			 " WHERE " + ANCHOR_NAME + " like ?",
    			new Object[]{anchor_name});
    }
    
    public void updateWrcardCollectData(String anchor_name, String data) {
    	if (!isOpen) return;
    	AppUtil.log( "========updateWrcardCollectData=======");
    	mDb.execSQL("UPDATE " + WRCARD_ANCHOR_TABLE + " SET " + 
    			WRCARD_COLLECT_DATA_STRING + "=?" +
    			 " WHERE " + WRCARD_ANCHOR_NAME + " like ?",
    			new Object[]{data, anchor_name});
    }
    
    public UploadParameter getUploadParameter(int order_no) {
    	UploadParameter mParameter = null;
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_ORDERNO + "==" + order_no, null);
    	if (cursor.moveToNext()) {
    		String anchor_name = cursor.getString(VOLUME_ANCHOR_NAME);
			String grouting_date = cursor.getString(VOLUME_SLURRY_DATE) + " " +
					cursor.getString(VOLUME_END_SLURRY_DATE);
			String grouting_data = cursor.getString(VOLUME_COLLECT_DATA_STRING);
			String pad_string = "[pressure_value={"+cursor.getString(VOLUME_COLLECT_PRESSURE_STRING)
					+"} pressure_time={"+cursor.getString(VOLUME_COLLECT_PRESSURE_TIME_STRING)
					+"} capacity_value={"+cursor.getString(VOLUME_COLLECT_CAPACITY_STRING)
					+"} capacity_time={"+cursor.getString(VOLUME_COLLECT_CAPACITY_TIME_STRING)
					+"}]";
			double full_hole_capacity = cursor.getDouble(VOLUME_FULL_HOLE_CAPACITY);
    		mParameter = new UploadParameter(anchor_name, grouting_date, grouting_data, full_hole_capacity, 
    				pad_string);
    		cursor.close();
    	}
    	return mParameter;
    }
    
    public UploadParameter getUploadParameterById(int id) {
    	UploadParameter mParameter = null;
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_ID + "==" + id, null);
    	if (cursor.moveToNext()) {
    		String anchor_name = cursor.getString(VOLUME_ANCHOR_NAME);
			String grouting_date = cursor.getString(VOLUME_SLURRY_DATE) + " " +
					cursor.getString(VOLUME_END_SLURRY_DATE);
			String grouting_data = cursor.getString(VOLUME_COLLECT_DATA_STRING);
			String pad_string = "pressure_value={"+cursor.getString(VOLUME_COLLECT_PRESSURE_STRING)
					+"} pressure_time={"+cursor.getString(VOLUME_COLLECT_PRESSURE_TIME_STRING)
					+"} capacity_value={"+cursor.getString(VOLUME_COLLECT_CAPACITY_STRING)
					+"} capacity_time={"+cursor.getString(VOLUME_COLLECT_CAPACITY_TIME_STRING)
					+"}";
			double full_hole_capacity = cursor.getDouble(VOLUME_FULL_HOLE_CAPACITY);
    		mParameter = new UploadParameter(anchor_name, grouting_date, grouting_data, full_hole_capacity,
    				pad_string);
    		cursor.close();
    	}
    	return mParameter;
    }
    
    public AnchorParameter getCraftParameter(String anchor_name) {
    	AnchorParameter mParameter = null;
    	if (!isOpen) return null;
    	Cursor cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    			" where " + ANCHOR_NAME + " like ? ", new String[]{anchor_name});
    	if (cursor.moveToNext()) {
			String anchor_type = cursor.getString(VOLUME_ANCHOR_TYPE);
			String anchor_model = cursor.getString(VOLUME_ANCHOR_MODEL);
			double design_len = cursor.getDouble(VOLUME_ANCHOR_DESIGN_LENGTH);
			double design_press = cursor.getDouble(VOLUME_DESIGN_PRESSURE);
			int hold_time = cursor.getInt(VOLUME_DESIGN_HOLD_TIME);
			double design_cap = cursor.getDouble(VOLUME_THEREO_CAPACITY);
			double cap_unit_meter = cursor.getDouble(VOLUME_CAP_UNIT_METER);
			double cap_unit_hour = cursor.getDouble(VOLUME_CAP_UNIT_HOUR);
			double full_hole_pressure = cursor.getDouble(VOLUME_FULL_HOLE_PRESSURE);
			double full_hole_capacity = cursor.getDouble(VOLUME_FULL_HOLE_CAPACITY);
			String remark = cursor.getString(VOLUME_ANCHOR_REMARK);
			int grout_priority = cursor.getInt(VOLUME_ANCHOR_GROUT_PRIORITY);
			String transfer_state = "";
			if (cursor.getInt(VOLUME_CRAFT_TRANSFER_STATE) == 1) {
				transfer_state = "*";
			}
    		mParameter = new AnchorParameter(anchor_name, anchor_type, anchor_model, design_len, 
					design_press, hold_time, design_cap, cap_unit_meter, cap_unit_hour, 
					full_hole_pressure, full_hole_capacity, remark, transfer_state, grout_priority);
    		cursor.close();
    	}
    	return mParameter;
    }
    
    public ArrayList<AnchorParameter> queryCraftParamDevID(int mileage_page) {
    	ArrayList<AnchorParameter> mQueryContentList = new ArrayList<AnchorParameter>();
    	if (!isOpen) return mQueryContentList;
    	Cursor mileage_cursor = mDb.rawQuery("select " + MILEAGE_ID + " from " + 
	    	MILEAGE_TABLE + " where " + MILEAGE_ORDERNO + "=" + mileage_page, null);
    	AppUtil.log("queryCraftParamDevID-------mileage_page:"+mileage_page);
    	if (mileage_cursor.moveToNext()) {
    		AppUtil.log("queryCraftParamDevID-------mileage_id:"+mileage_cursor.getInt(0));
    		int mileage_id = mileage_cursor.getInt(0);
    		Cursor anchor_cursor = mDb.rawQuery("select * from " + ANCHOR_TABLE + 
    				" where " + ANCHOR_MILEAGE_ID + "=" + mileage_id + " order by " + 
    				ANCHOR_ORDERNO, null);
    		while (anchor_cursor.moveToNext()) {
    			String anchor_name = anchor_cursor.getString(VOLUME_ANCHOR_NAME);
    			String anchor_type = anchor_cursor.getString(VOLUME_ANCHOR_TYPE);
    			String anchor_model = anchor_cursor.getString(VOLUME_ANCHOR_MODEL);
    			double design_len = anchor_cursor.getDouble(VOLUME_ANCHOR_DESIGN_LENGTH);
    			double design_press = anchor_cursor.getDouble(VOLUME_DESIGN_PRESSURE);
    			int hold_time = anchor_cursor.getInt(VOLUME_DESIGN_HOLD_TIME);
    			double design_cap = anchor_cursor.getDouble(VOLUME_THEREO_CAPACITY);
    			double cap_unit_meter = anchor_cursor.getDouble(VOLUME_CAP_UNIT_METER);
    			double cap_unit_hour = anchor_cursor.getDouble(VOLUME_CAP_UNIT_HOUR);
    			double full_hole_pressure = anchor_cursor.getDouble(VOLUME_FULL_HOLE_PRESSURE);
    			double full_hole_capacity = anchor_cursor.getDouble(VOLUME_FULL_HOLE_CAPACITY);
    			String remark = anchor_cursor.getString(VOLUME_ANCHOR_REMARK);
    			int grout_priority = anchor_cursor.getInt(VOLUME_ANCHOR_GROUT_PRIORITY);
    			String transfer_state = "";
    			
    			if (anchor_cursor.getInt(VOLUME_CRAFT_TRANSFER_STATE) == 1) {
    				transfer_state = "*";
    			}
	    		AppUtil.log( anchor_name + " " + anchor_model + "  "+design_len+" "+design_press+" "+
	    				hold_time+" "+design_cap+" "+cap_unit_meter+" "+cap_unit_hour+" ");
    			mQueryContentList.add(new AnchorParameter(anchor_name, anchor_type, anchor_model, design_len, 
    					design_press, hold_time, design_cap, cap_unit_meter, cap_unit_hour, 
    					full_hole_pressure, full_hole_capacity, remark, transfer_state, grout_priority));
    		}
    		if (anchor_cursor != null)
    			anchor_cursor.close();
    		if (mileage_cursor != null)
    			mileage_cursor.close();
    	}
    	return mQueryContentList;
    }
}