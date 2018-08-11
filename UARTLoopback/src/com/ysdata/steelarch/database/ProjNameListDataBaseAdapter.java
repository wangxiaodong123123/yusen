package com.ysdata.steelarch.database;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.ysdata.steelarch.cloud.util.ConstDef;
import com.ysdata.steelarch.storage.ExtSdCheck;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class ProjNameListDataBaseAdapter {
	private static final String TAG = "cs200";
	
	private static final String NAME_LIST_TABLE = "name_list_table";
	private static final String NAME_LIST_ID = "id";
	private static final String NAME_LIST_PROJ_KEY = "seq";
	private static final String NAME_LIST_ROUTE = "route";
	private static final String NAME_LIST_ENG = "eng";
	
	private static final String DATABASE_CREATE_NAME_LIST_TABLE = "CREATE TABLE IF NOT EXISTS " + NAME_LIST_TABLE + "("
			+ NAME_LIST_ID + " INTEGER PRIMARY KEY,"
			+ NAME_LIST_PROJ_KEY + " INTEGER,"
			+ NAME_LIST_ROUTE + "  VARCHAR(40),"
			+ NAME_LIST_ENG + " TEXT"
			+ ");";

    private SQLiteDatabase mDb = null;
    private boolean isOpen = false;
    private Context mContext = null;
    
	private static ProjNameListDataBaseAdapter mDataBaseAdapter = null;
	private ProjNameListDataBaseAdapter(Context context) {
		mContext = context;
	}
	/**
	 * DataBaseAdapter
	 **/
	public static ProjNameListDataBaseAdapter getSingleDataBaseAdapter(Context context) {
		if (mDataBaseAdapter == null)
		{
			synchronized (ProjNameListDataBaseAdapter.class)
			{
				if (mDataBaseAdapter == null)
				{
					mDataBaseAdapter = new ProjNameListDataBaseAdapter(context);
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
    		Log.d(TAG, "openDb error-----ExtSDpath = null.");
    		return false;
    	}
    	Log.d(TAG, "openDb(projdb)-----isOpen:"+isOpen);
    	if (!isOpen) {
			String dir = ExtSDpath + ConstDef.DATABASE_PATH;
			Log.d(TAG, "open dir:" + dir);
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
						Log.d(TAG, "creat or open name_list.db failed.exception:" + e);
					}
				}
				mDb = SQLiteDatabase.openOrCreateDatabase(file, null);
				
				try{
					mDb.execSQL(DATABASE_CREATE_NAME_LIST_TABLE);
				}catch (SQLException e) {
					Log.d(TAG, "creat or open name list table failed.exception:" + e);
				}
			}catch (SQLiteException e){
				Log.d(TAG, "mkdir "+ dir +" failed.exception:" + e);
				return false;
			}
			
			isOpen = true;
    	}
        return true;
    }
    
    public int saveProjectName(String name, int csId) {
    	Log.d(TAG, "========saveProjectName========");
    	Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE, null); 
    	int key = 1;
    	
    	if (cursor.moveToLast()) {
    		key = cursor.getInt(1) + 1;
    	} 
    	mDb.execSQL("REPLACE INTO " + NAME_LIST_TABLE + " ("
    			+ NAME_LIST_ID + ", "
    			+ NAME_LIST_PROJ_KEY + ", " 
    			+ NAME_LIST_ROUTE +") VALUES ("
    			+ csId + ", " + key + ", ?)",new Object[]{name});   
    	if (cursor != null) {
    		cursor.close();  
    	}
    	return key;
    }
    
    public void saveEngName(int proj_val, String eng_name, int subproject_id) {
    	Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    			" where " + NAME_LIST_PROJ_KEY + " = " + proj_val, null);
    	if (cursor.moveToNext()) {
    		String eng_name_string = cursor.getString(3);
    		if (eng_name_string == null) {
    			eng_name_string = "";
    		}
    		StringBuffer eng_names = new StringBuffer(eng_name_string);
    		if (eng_name_string.equals("")) {
    			eng_names.append(eng_name+"_"+subproject_id);
    		} else {
    			eng_names.append(",").append(eng_name+"_"+subproject_id);
    		}
    		ContentValues value = new ContentValues();
    		int id = cursor.getInt(0);
    		value.put(NAME_LIST_ENG, eng_names.toString());
			mDb.update(NAME_LIST_TABLE, value, "id = "+id, null);
    	}
    	if (cursor != null) {
    		cursor.close();
    	}
    }
    
    public boolean deleteProjectName(String name) {
    	boolean ret_val = false;
    	Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    			" where " +NAME_LIST_ROUTE+" like ?", new String[]{name});
		if (cursor.moveToNext()) {
    		int id = cursor.getInt(0);
    		mDb.delete(NAME_LIST_TABLE, NAME_LIST_ID + "=" + id, null);
    		Cursor cursor1 = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    				" where " + NAME_LIST_ID + ">" + id , null);
    		ContentValues value = new ContentValues();
    		int id1 = 0;
    		while (cursor1.moveToNext()) {
    			id1 = cursor1.getInt(0);
    			value.put(NAME_LIST_ID, id1-1);
    			mDb.update(NAME_LIST_TABLE, value, "id = "+id1, null);
    		} 
    		if (cursor1 != null)
        		cursor1.close();
    		ret_val = true;
    	}
    	if (cursor != null)
    		cursor.close();
    	return ret_val;
    }
    
    public boolean deleteEngName(int proj_val, String eng_name) {
    	boolean ret_val = false;
    	Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    			" where " + NAME_LIST_PROJ_KEY + " = " + proj_val, null);
    	if (cursor.moveToNext()) {
    		String eng_name_with_key_arr[] = cursor.getString(3).split(",");
    		String eng_name_with_nokey = "";
    		int id = cursor.getInt(0);
    		StringBuffer sb = new StringBuffer();
    		int size = eng_name_with_key_arr.length;
    		boolean isFirstAppend = true;
    		for (int i = 0; i < size; i++) {
    			eng_name_with_nokey = eng_name_with_key_arr[i].substring(0, eng_name_with_key_arr[i].lastIndexOf("_"));
    			if (eng_name_with_nokey.equals(eng_name)) 
    				continue;
    			if (isFirstAppend) {
    				sb.append(eng_name_with_key_arr[i]);
    				isFirstAppend = false;
    			} else {
    				sb.append(",").append(eng_name_with_key_arr[i]);
    			}
    		}
    		ContentValues value = new ContentValues();
    		value.put(NAME_LIST_ENG, sb.toString());
    		mDb.update(NAME_LIST_TABLE, value, "id = "+id, null);
    		ret_val = true;
    	}
    	if (cursor != null) {
    		cursor.close();
    	}
    	return ret_val;
    }
    
    public boolean renameProjectName(String proj_name_old, String proj_name_new) {
    	Log.d(TAG, "========renameProjectName=======");
    	boolean ret_val = false;
		Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    			" where " +NAME_LIST_ROUTE+" like ?", new String[]{proj_name_old});
		if (cursor.moveToNext()) {
			int id = cursor.getInt(0);
			ContentValues value = new ContentValues();
			value.put(NAME_LIST_ROUTE, proj_name_new);
			mDb.update(NAME_LIST_TABLE, value, "id = " + id, null);
			ret_val = true;
		}
		if (cursor != null)
			cursor.close();
		return ret_val;
    }
    
    public boolean renameEngName(int proj_val, String eng_old_name, String eng_new_name) {
    	boolean ret_val = false;
    	Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    			" where " + NAME_LIST_PROJ_KEY + " = " + proj_val, null);
    	if (cursor.moveToNext()) {
    		String eng_name_with_key_arr[] = cursor.getString(3).split(",");
    		String eng_name_with_nokey = "";
    		int id = cursor.getInt(0);
    		StringBuffer sb = new StringBuffer();
    		int size = eng_name_with_key_arr.length;
    		boolean isFirstAppend = true;
    		for (int i = 0; i < size; i++) {
    			eng_name_with_nokey = eng_name_with_key_arr[i].substring(0, eng_name_with_key_arr[i].lastIndexOf("_"));
    			if (eng_name_with_nokey.equals(eng_old_name)) {
    				if (isFirstAppend) {
    					sb.append(eng_name_with_key_arr[i].replace(eng_old_name, eng_new_name));
        				isFirstAppend = false;
    				} else {
    					sb.append(",").append(eng_name_with_key_arr[i].replace(eng_old_name, eng_new_name));
    				}
    				continue;
    			}
    			if (isFirstAppend) {
    				sb.append(eng_name_with_key_arr[i]);
    				isFirstAppend = false;
    			} else {
    				sb.append(",").append(eng_name_with_key_arr[i]);
    			}
    		}
    		ContentValues value = new ContentValues();
    		value.put(NAME_LIST_ENG, sb.toString());
    		mDb.update(NAME_LIST_TABLE, value, "id = "+id, null);
    		ret_val = true;
    	}
    	if (cursor != null) {
    		cursor.close();
    	}
    	return ret_val;
    }
    
    public String getProjectName(int proj_val) {
    	Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    			" where " + NAME_LIST_PROJ_KEY + " = " + proj_val, null);
    	String proj_name = null;
    	if (cursor.moveToNext()) {
    		proj_name = cursor.getString(2);
    	}
    	if (cursor != null) {
    		cursor.close();
    	}
    	return proj_name;
    }
    
	public int getProjectNameKey(String name) {
		Log.d(TAG, "========getpRrojectNameKey=======");
		Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    			" where " +NAME_LIST_ROUTE+" like ?", new String[]{name});
		int key = 0;
	    if (cursor.moveToNext()) {
	    	key = cursor.getInt(1);
	    }
	    if (cursor != null)
			cursor.close();
	    return key;
	}
	
	public int getContractSectionId(int proj_val) {
		Log.d(TAG, "========getContractSectionId=======");
		Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    			" where " + NAME_LIST_PROJ_KEY + " = " + proj_val, null);
		int id = 0;
	    if (cursor.moveToNext()) {
	    	id = cursor.getInt(0);
	    }
	    if (cursor != null)
			cursor.close();
	    return id;
	}
	
	public int getEngNameKey(int proj_val, String eng_name) {
		Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
				" where " + NAME_LIST_PROJ_KEY + " = " + proj_val, null);
		int key = 0;
		if (cursor.moveToNext()) {
    		String eng_name_with_key_arr[] = cursor.getString(3).split(",");
    		String eng_name_with_nokey = "";
    		int last_index = 0;
    		int size = eng_name_with_key_arr.length;
    		for (int i = 0; i < size; i++) {
    			last_index =  eng_name_with_key_arr[i].lastIndexOf("_");
    			eng_name_with_nokey = eng_name_with_key_arr[i].substring(0, last_index);
    			if (eng_name_with_nokey.equals(eng_name)) {
    				key = Integer.parseInt(eng_name_with_key_arr[i].substring(last_index+1, eng_name_with_key_arr[i].length()));
    				break;
    			}
    		}
    	}
		if (cursor != null)
			cursor.close();
		return key;
	}
	
    public String getEngName(int proj_val, int eng_val) {
    	Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    			" where " + NAME_LIST_PROJ_KEY + " = " + proj_val, null);
    	String eng_name_with_nokey = "";
    	if (cursor.moveToNext()) {
    		String eng_name_with_key_arr[] = cursor.getString(3).split(",");
    		
    		int eng_key = 0;
    		int last_index = 0;
    		int size = eng_name_with_key_arr.length;
    		for (int i = 0; i < size; i++) {
    			last_index =  eng_name_with_key_arr[i].lastIndexOf("_");
    			eng_key = Integer.parseInt(eng_name_with_key_arr[i].substring(last_index+1, eng_name_with_key_arr[i].length()));
    			
    			if (eng_key == eng_val) {
    				eng_name_with_nokey = eng_name_with_key_arr[i].substring(0, last_index);
    				break;
    			}
    		}
    	}
    	if (cursor != null) {
    		cursor.close();
    	}
    	return eng_name_with_nokey;
    }
	
	public void getProjectNameList(ArrayList<String> list) {
		Log.d(TAG, "========getProjectNameList=======");
		Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE, null); 
		if (list != null) {
			list.clear();
			if (cursor.getCount() > 0) {
				while(cursor.moveToNext()) {
					list.add(cursor.getString(2));
				}
			}
		}
		if (cursor != null)
			cursor.close();
	}
	
	public boolean checkProjectRename(String proj_name) {
		ArrayList<String> list = new ArrayList<String>();
		boolean ret_val = false;
		getProjectNameList(list);
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (proj_name.equals(list.get(i))) {
				ret_val = true;
				break;
			}
		}
		return ret_val;
	}
	
	public boolean checkEngRename(int proj_val, String eng_name) {
		ArrayList<String> list = new ArrayList<String>();
		boolean ret_val = false;
		getEngNameList(proj_val, list);
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (eng_name.equals(list.get(i))) {
				ret_val = true;
				break;
			}
		}
		return ret_val;
	}
	
    public void getEngNameList(int proj_val, ArrayList<String> eng_list) {
    	Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    			" where " + NAME_LIST_PROJ_KEY + " = " + proj_val, null);
    	eng_list.clear();
    	if (cursor.moveToNext()) {
    		String eng_name_string = cursor.getString(3);
    		Log.d(TAG, "getEngNameList-----eng_name_string:"+eng_name_string);
    		if (eng_name_string != null && eng_name_string.length() > 0) {
    			String eng_name_with_key_arr[] = eng_name_string.split(",");
    			
    			int size = eng_name_with_key_arr.length;
    			for (int i = 0; i < size; i++) {
    				Log.d(TAG, "eng_name_with_key_arr["+i+"]:"+eng_name_with_key_arr[i]);
    				eng_list.add(eng_name_with_key_arr[i].substring(0, eng_name_with_key_arr[i].lastIndexOf("_")));
    			}
    		}
    	}
    	if (cursor != null) {
    		cursor.close();
    	}
    }
    
    public void getEngNameList(String proj_name, ArrayList<String> eng_list) {
    	Cursor cursor = mDb.rawQuery("select * from " + NAME_LIST_TABLE + 
    			" where " + NAME_LIST_ROUTE + " like ?", new String[]{proj_name});
    	eng_list.clear();
    	if (cursor.moveToNext()) {
    		String eng_name_string = cursor.getString(3);
    		if (eng_name_string != null && eng_name_string.length() > 0) {
    			String eng_name_with_key_arr[] = eng_name_string.split(",");
    			
    			int size = eng_name_with_key_arr.length;
    			for (int i = 0; i < size; i++) {
    				
    				eng_list.add(eng_name_with_key_arr[i].substring(0, eng_name_with_key_arr[i].lastIndexOf("_")));
    			}
    		}
    	}
    	if (cursor != null) {
    		cursor.close();
    	}
    }
    
    /**
     * Close database
     */
    public void closeDb() {
    	Log.d(TAG, "closeDb(projdb)====isOpen:"+isOpen);
    	if (isOpen) {
    		if (mDb != null) {
    			mDb.close();
    		}
    		mDb = null;
    		isOpen = false;
    	}
    }
}