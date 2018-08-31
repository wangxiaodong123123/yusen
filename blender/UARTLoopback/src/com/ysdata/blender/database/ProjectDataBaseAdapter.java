package com.ysdata.blender.database;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.ConstDef;
import com.ysdata.blender.storage.ExtSdCheck;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class ProjectDataBaseAdapter {
	private static final String PROJECT_LIST_TABLE = "project_list_table";
	private static final String PROJECT_LIST_ID = "project_id";
	private static final String PROJCET_LIST_NAME = "project_name";
	
	private static final int VOLUME_PROJECT_LIST_ID = 0;
	private static final int VOLUME_PROJCET_LIST_NAME = 1;
	
	private static final String SUBPROJECT_LIST_TABLE = "subproject_list_table";
	private static final String SUBPROJECT_LIST_ID = "subproject_id";
	private static final String SUBPROJECT_LIST_PROJECT_ID = "project_id";
	private static final String SUBPROJCET_LIST_NAME = "subproject_name";	
	private static final String SUBPROJCET_LIST_DIRECTION = "subproject_direction";	
	
	private static final int VOLUME_SUBPROJECT_LIST_ID = 0;
	private static final int VOLUME_SUBPROJECT_LIST_PROJECT_ID = 1;
	private static final int VOLUME_SUBPROJCET_LIST_NAME = 2;
	private static final int VOLUME_SUBPROJCET_LIST_DIRECTION = 3;
	
    private static final String COMMUNICATE_PROJECT_RECORD_TABLE = "communicate_project_record_table";
    private static final String COMMUNICATE_RECORD_ID = "communicate_record_id";
    private static final String COMMUNICATE_PROJECT_ID = "communicate_project_id";
    private static final String COMMUNICATE_SUBPROJECT_ID = "communicate_subproject_id";
	
	private static final String DATABASE_CREATE_PROJECT_LIST_TABLE = "CREATE TABLE IF NOT EXISTS " + PROJECT_LIST_TABLE + "("
			+ PROJECT_LIST_ID + " INTEGER PRIMARY KEY,"
			+ PROJCET_LIST_NAME + "  VARCHAR(40)"
			+ ");";

	private static final String DATABASE_CREATE_SUBPROJECT_LIST_TABLE = "CREATE TABLE IF NOT EXISTS " + SUBPROJECT_LIST_TABLE + "("
			+ SUBPROJECT_LIST_ID + " INTEGER PRIMARY KEY,"
			+ SUBPROJECT_LIST_PROJECT_ID + " INTEGER,"
			+ SUBPROJCET_LIST_NAME + "  VARCHAR(40),"
			+ SUBPROJCET_LIST_DIRECTION + " INTEGER"
			+ ");";
	
	private static final String DATABASE_CREATE_COMMUNICATE_PROJECT_RECORD_TABLE = "CREATE TABLE IF NOT EXISTS " + COMMUNICATE_PROJECT_RECORD_TABLE + "("
			+ COMMUNICATE_RECORD_ID + " INTEGER PRIMARY KEY,"
			+ COMMUNICATE_PROJECT_ID + "  INTEGER,"
			+ COMMUNICATE_SUBPROJECT_ID + " INTEGER"
			+ ");";
	
    private SQLiteDatabase mDb = null;
    private boolean isOpen = false;
    private Context mContext = null;
    
	private static ProjectDataBaseAdapter mDataBaseAdapter = null;
	private ProjectDataBaseAdapter(Context context) {
		mContext = context;
	}
	/**
	 * DataBaseAdapter
	 **/
	public static ProjectDataBaseAdapter getSingleDataBaseAdapter(Context context) {
		if (mDataBaseAdapter == null)
		{
			synchronized (ProjectDataBaseAdapter.class)
			{
				if (mDataBaseAdapter == null)
				{
					mDataBaseAdapter = new ProjectDataBaseAdapter(context);
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
    	AppUtil.log( "openDb(projdb)-----isOpen:"+isOpen);
    	if (!isOpen) {
			String dir = ExtSDpath + "/" + ConstDef.PROJECT_NAME + "/database";
			AppUtil.log( "open dir:" + dir);
			try{
				File path = new File(dir);
				
				if(!path.exists()){
					path.mkdirs();
				}
				File file = new File(dir+"/name_list.db");
				if(!file.exists()){
					try {
						file.createNewFile();
					} catch (IOException e) {
						AppUtil.log( "creat or open name_list.db failed.exception:" + e);
					}
				}
				mDb = SQLiteDatabase.openOrCreateDatabase(file, null);
				
				try{
					mDb.execSQL(DATABASE_CREATE_PROJECT_LIST_TABLE);
				}catch (SQLException e) {
					AppUtil.log( "creat or open project list table failed.exception:" + e);
				}
				try{
					mDb.execSQL(DATABASE_CREATE_SUBPROJECT_LIST_TABLE);
				}catch (SQLException e) {
					AppUtil.log( "creat or open subproject list table failed.exception:" + e);
				}
				try{
					mDb.execSQL(DATABASE_CREATE_COMMUNICATE_PROJECT_RECORD_TABLE);
				}catch (SQLException e) {
					AppUtil.log( "creat or open communicate_project_record table failed.exception:" + e);
				}
			}catch (SQLiteException e){
				AppUtil.log( "mkdir "+ dir +" failed.exception:" + e);
				return false;
			}
			
			isOpen = true;
    	}
        return true;
    }
    
    public void updateCollectProjectRecord(int project_id, int subproject_id) {
    	if (!isOpen) return;
    	ContentValues value = new ContentValues();
    	value.put(COMMUNICATE_PROJECT_ID, project_id);
    	value.put(COMMUNICATE_SUBPROJECT_ID, subproject_id);
		mDb.update(COMMUNICATE_PROJECT_RECORD_TABLE, value, COMMUNICATE_RECORD_ID + "=" + 1, null);
    }
    
    public void updateUploadProjectRecord(int project_id, int subproject_id) {
    	if (!isOpen) return;
    	ContentValues value = new ContentValues();
    	value.put(COMMUNICATE_PROJECT_ID, project_id);
    	value.put(COMMUNICATE_SUBPROJECT_ID, subproject_id);
		mDb.update(COMMUNICATE_PROJECT_RECORD_TABLE, value, COMMUNICATE_RECORD_ID + "=" + 2, null);
    }
    
    public int getCollectProjectId() {
    	if (!isOpen) return 0;
    	int project_id = 0;
    	Cursor cursor = mDb.rawQuery("select " + COMMUNICATE_PROJECT_ID + " from " + COMMUNICATE_PROJECT_RECORD_TABLE + 
    			" where " + COMMUNICATE_RECORD_ID + "=" + 1, null);
    	if (cursor.moveToNext()) {
    		project_id = cursor.getInt(0);
    		cursor.close();
    	}
    	return project_id;
    }
    public int getCollectSubProjectId() {
    	if (!isOpen) return 0;
    	int subproject_id = 0;
    	Cursor cursor = mDb.rawQuery("select " + COMMUNICATE_SUBPROJECT_ID + " from " + COMMUNICATE_PROJECT_RECORD_TABLE + 
    			" where " + COMMUNICATE_RECORD_ID + "=" + 1, null);
    	if (cursor.moveToNext()) {
    		subproject_id = cursor.getInt(0);
    		cursor.close();
    	}
    	return subproject_id;
    }
    
    public int getUploadProjectId() {
    	if (!isOpen) return 0;
    	int project_id = 0;
    	Cursor cursor = mDb.rawQuery("select " + COMMUNICATE_PROJECT_ID + " from " + COMMUNICATE_PROJECT_RECORD_TABLE + 
    			" where " + COMMUNICATE_RECORD_ID + "=" + 2, null);
    	if (cursor.moveToNext()) {
    		project_id = cursor.getInt(0);
    		cursor.close();
    	}
    	return project_id;
    }
    public int getUploadSubProjectId() {
    	if (!isOpen) return 0;
    	int subproject_id = 0;
    	Cursor cursor = mDb.rawQuery("select " + COMMUNICATE_SUBPROJECT_ID + " from " + COMMUNICATE_PROJECT_RECORD_TABLE + 
    			" where " + COMMUNICATE_RECORD_ID + "=" + 2, null);
    	if (cursor.moveToNext()) {
    		subproject_id = cursor.getInt(0);
    		cursor.close();
    	}
    	return subproject_id;
    }
    
    public void insertCommunicateStateRecord(int id, int project_id, int 
    		subproject_id) {
    	if (!isOpen) return;
    	mDb.execSQL("REPLACE INTO " + COMMUNICATE_PROJECT_RECORD_TABLE + " ("
				+ COMMUNICATE_RECORD_ID + ", "
				+ COMMUNICATE_PROJECT_ID + ", "
				+ COMMUNICATE_SUBPROJECT_ID + 
				") VALUES ("+ id + "," 
				+ project_id + "," 
				+ subproject_id
				+ ")"); 
    }
    
    public void initalCommunicateState() {
    	if (!isOpen) return;
		mDb.delete(COMMUNICATE_PROJECT_RECORD_TABLE, null, null);
		insertCommunicateStateRecord(1, 0, 0);
		insertCommunicateStateRecord(2, 0, 0);
    }
    
    public int getProjectCount() {
    	Cursor cursor = mDb.rawQuery("select * from " + PROJECT_LIST_TABLE, null);  
    	int count = 0;
    	if (cursor != null)
    		count = cursor.getCount(); 
    	if (cursor != null)
    		cursor.close();  
    	AppUtil.log( "project_count:"+count);
    	return count; 
    }
    
    public int getSubProjectCount() {
    	Cursor cursor = mDb.rawQuery("select * from " + SUBPROJECT_LIST_TABLE, null);  
    	int count = 0;
    	if (cursor != null)
    		count = cursor.getCount(); 
    	if (cursor != null)
    		cursor.close();  
    	AppUtil.log( "subproject_count:"+count);
    	return count; 
    }
    
    public void clearAllProject() { 
    	AppUtil.log("==========clearAllProject==========");
    	Cursor cursor = mDb.rawQuery("select * from " + PROJECT_LIST_TABLE, null); 
    	int csId = 0;
    	FileOperator mFileOperator = FileOperator.getSingleFileOperator(mContext);
    	String database_path = ExtSdCheck.getExtSDCardPath() + ConstDef.DATABASE_PATH;
    	while (cursor.moveToNext()) {
    		csId = cursor.getInt(VOLUME_PROJECT_LIST_ID);
			deleteProject(csId);
			deleteSubProjectWhereCsId(csId);
			mFileOperator.DeleteAllDir(database_path+csId);
    	}
    }
    
    public void clearProject(int csId) {
    	AppUtil.log("clearProject================csId:"+csId);
    	FileOperator mFileOperator = FileOperator.getSingleFileOperator(mContext);
    	String database_path = ExtSdCheck.getExtSDCardPath() + ConstDef.DATABASE_PATH;
		deleteSubProjectWhereCsId(csId);
		mFileOperator.DeleteAllDir(database_path+csId);
    }
    
    public void saveProjectRecord(int csId, String name) {
    	AppUtil.log( "========saveProjectRecord========");
    	mDb.execSQL("REPLACE INTO " + PROJECT_LIST_TABLE + " ("
    			+ PROJECT_LIST_ID + ", "
    			+ PROJCET_LIST_NAME +") VALUES ("
    			 + csId + ", ?)",new Object[]{name});   
    }
    
    public void saveSubProectRecord(int subproject_id, int project_id, String name, int direction) {
    	AppUtil.log( "========saveSubProectRecord========");
    	mDb.execSQL("REPLACE INTO " + SUBPROJECT_LIST_TABLE + " ("
    			+ SUBPROJECT_LIST_ID + ", "
    			+ SUBPROJECT_LIST_PROJECT_ID + ", "
    			+ SUBPROJCET_LIST_NAME + ", "
    			+ SUBPROJCET_LIST_DIRECTION + ") VALUES ("
    			 + subproject_id + "," + project_id + ", ?, " + direction + ")",new Object[]{name}); 
    }
    
    public int getSubProjectDirection(int subproject_id) {
    	Cursor cursor = mDb.rawQuery("select * from " + SUBPROJECT_LIST_TABLE + 
    			" where " + SUBPROJECT_LIST_ID + " = " + subproject_id, null);
    	int direction = 1;
    	if (cursor.moveToNext()) {
    		direction = cursor.getInt(VOLUME_SUBPROJCET_LIST_DIRECTION);
    	}
    	if (cursor != null) {
    		cursor.close();
    	}
    	return direction;
    }
    
    public boolean deleteProject(int csId) {
    	return mDb.delete(PROJECT_LIST_TABLE, PROJECT_LIST_ID + "=" + csId, null) == 1 ? true : false;
    }
    
    public boolean deleteSubProjectWhereCsId(int csId) {
    	return mDb.delete(SUBPROJECT_LIST_TABLE, SUBPROJECT_LIST_PROJECT_ID + "=" + csId, null) == 1 ? true : false;
    }
    
    public boolean deleteSubProject(int SubProject_id) {
    	return mDb.delete(SUBPROJECT_LIST_TABLE, SUBPROJECT_LIST_ID + "=" + SubProject_id, null) == 1 ? true : false;
    }
    
    public String getProjectName(int csId) {
    	Cursor cursor = mDb.rawQuery("select * from " + PROJECT_LIST_TABLE + 
    			" where " + PROJECT_LIST_ID + " = " + csId, null);
    	String proj_name = "";
    	if (cursor.moveToNext()) {
    		proj_name = cursor.getString(VOLUME_PROJCET_LIST_NAME);
    	}
    	if (cursor != null) {
    		cursor.close();
    	}
    	return proj_name;
    }
    
	public int getProjectId(String name) {
		AppUtil.log( "========getpRrojectId=======");
		Cursor cursor = mDb.rawQuery("select * from " + PROJECT_LIST_TABLE + 
    			" where " + PROJCET_LIST_NAME + " like ?", new String[]{name});
		int key = -1;
	    if (cursor.moveToNext()) {
	    	key = cursor.getInt(VOLUME_PROJECT_LIST_ID);
	    }
	    if (cursor != null)
			cursor.close();
	    return key;
	}
	
	public int getSubProjectId(String name) {
		AppUtil.log( "========getSubProjectId=======");
		Cursor cursor = mDb.rawQuery("select * from " + SUBPROJECT_LIST_TABLE + 
    			" where " + SUBPROJCET_LIST_NAME + " like ?", new String[]{name});
		int key = -1;
	    if (cursor.moveToNext()) {
	    	key = cursor.getInt(VOLUME_SUBPROJECT_LIST_ID);
	    }
	    if (cursor != null)
			cursor.close();
	    return key;
	}
	
    public String getSubProjectName(int subproject_id) {
    	Cursor cursor = mDb.rawQuery("select * from " + SUBPROJECT_LIST_TABLE + 
    			" where " + SUBPROJECT_LIST_ID + " = " + subproject_id, null);
    	String subproj_name = "";
    	if (cursor.moveToNext()) {
    		subproj_name = cursor.getString(VOLUME_SUBPROJCET_LIST_NAME);
    	}
    	if (cursor != null) {
    		cursor.close();
    	}
    	return subproj_name;
    }
	
	public void getProjectNameList(ArrayList<String> list) {
		AppUtil.log( "========getProjectNameList=======");
		Cursor cursor = mDb.rawQuery("select * from " + PROJECT_LIST_TABLE, null); 
		if (list != null) {
			list.clear();
			if (cursor.getCount() > 0) {
				while(cursor.moveToNext()) {
					list.add(cursor.getString(VOLUME_PROJCET_LIST_NAME));
				}
			}
		}
		if (cursor != null)
			cursor.close();
	}
	
    public void getSubProjectNameList(ArrayList<String> list, int project_id) {
		AppUtil.log( "========getSubProjectNameList=======");
		Cursor cursor = mDb.rawQuery("select * from " + SUBPROJECT_LIST_TABLE + 
				" where " + SUBPROJECT_LIST_PROJECT_ID + " = " + project_id, null);
		if (list != null) {
			list.clear();
			if (cursor.getCount() > 0) {
				while(cursor.moveToNext()) {
					list.add(cursor.getString(VOLUME_SUBPROJCET_LIST_NAME));
				}
			}
		}
		if (cursor != null)
			cursor.close();
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