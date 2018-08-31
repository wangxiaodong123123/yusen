package com.ysdata.blender.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ysdata.blender.storage.ExtSdCheck;

public class LogFileManager {
    private static LogFileManager fileManager = null;
    private final static String TAG = "ys200"; 
    private static BufferedWriter bw = null;
    
    
    /**
     * 获得Value单例模式的实例
     **/
    public static LogFileManager getInstance()
    {
        if (fileManager == null)
        {
            synchronized (LogFileManager.class)
            {
                if (fileManager == null)
                {
                    fileManager = new LogFileManager();
                }
            }
        }
        return fileManager;
    }
    
    @SuppressLint("SimpleDateFormat")
	public void create() {
    	
    	String log_dir_name = ExtSdCheck.getExtSDCardPath()+"/ys200_logfile/blender";
    	File path = new File(log_dir_name);
    	
    	if (!path.exists()) {
    		path.mkdirs();
    	}
    	
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");     
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
		String date = formatter.format(curDate);
    	
    	String file_name = log_dir_name + "/" + date+".log";  
    	File file = new File(file_name);
    	if (!file.exists()) {
    		try {
				file.createNewFile();
			} catch (IOException e) {
				Log.d(TAG, "creat or open " + file_name + " failed.");
				return;
			}
    	}
    	try {
			bw = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
        
    public static void writeToLogfile(String string) {
    	if (bw != null) {
    		try {
    			bw.write(string+"\n");
    			bw.flush();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public static void close() {
    	if (bw != null) {
    		try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
}
