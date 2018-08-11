package com.ysdata.steelarch.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

import com.ysdata.steelarch.wireless.client.Format;

public class FileOperator {
    private static FileOperator mFileOperator = null;
    private Context mContext = null;
    public static final String TAG = "ys200";
    public static int len_file = 0;
    public static int len_500ms = 0;
    public static int len_20s = 0;
    public static int len_param = 0;
    
    private FileOperator(Context context)
    {
        mContext = context;
    }
    
    /**
     * 获得Value单例模式的实例
     **/
    public static FileOperator getSingleFileOperator(Context context)
    {
        if (mFileOperator == null)
        {
            synchronized (FileOperator.class)
            {
                if (mFileOperator == null)
                {
                    mFileOperator = new FileOperator(context);
                }
            }
        }
        return mFileOperator;
    }
    
    public File creatOrOpenAngFile(int num) {
    	File path = new File("/mnt/sdcard/ang/save/");
    	
    	if (!path.exists()) {
    		path.mkdirs();
    	}
    	
    	File file = new File("/mnt/sdcard/ang/save/"+"file_"+num);
    	if (!file.exists()) {
    		try {
				file.createNewFile();
			} catch (IOException e) {
//				e.printStackTrace();
				Log.d(TAG, "creat or open file_" + num + " failed.");
				return null;
			}
    	}
    	
    	return file;
    }
        
    public void writeFile(File file, int[] data, int len, boolean isAppend) {
    	try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, isAppend));
			for (int i = 0; i < len; i++) {
				bw.write(Integer.toHexString(data[i])+ " ");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public int[] getDataFromFile(File file) {
    	int[] buffer = new int[4096];
    	try {
    		String str = "";
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((str=br.readLine())!=null) {
				String[] strs = str.split(" ");
				len_file = strs.length;
				for (int i = 0; i < len_file; i++) {
					buffer[i] = Integer.valueOf(strs[i], 16);
//					Log.d(TAG, "" + Integer.toHexString(buffer[i]));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return buffer;
    }
    
    public int[] getParamFromBuffer(int[] buffer, int len) {
    	int[] temp = new int[64];
    	int data_head = 0;
    	int data_break = 0;
    	int data_len = 0;
    	int cmd = 0;
    	int index = 0;
    	for (int i = 0; i < len; i++) {
        	data_head = buffer[i];
        	data_break = buffer[i+1];
        	cmd = buffer[i+2];
    		if ((data_head == Format.PANEL_HEAD) && (data_break == Format.PANEL_BREAK) && 
    				(cmd == Format.CMD_SET_PARAM)) {
    			index = i + 5;
    			data_len = (buffer[i+3] << 8) + buffer[i+4];
    			len_param = data_len;
    			break;
    		}
    	}
    	
    	if (index == 0) {
    		return null;
    	}
    	
    	for (int i = 0; i < data_len; i++) {
    		temp[i] = buffer[index];
    		index++;
    	}
    	
    	return temp;
    }

    public int[] get500msDataFromFile(int[] buffer, int len) {
    	int[] temp = new int[1024];
    	int data_head = 0;
    	int data_break = 0;
    	int data_len = 0;
    	int cmd = 0;
    	int index = 0;
    	for (int i = 0; i < len; i++) {
        	data_head = buffer[i];
        	data_break = buffer[i+1];
        	cmd = buffer[i+2];
    		if ((data_head == Format.PANEL_HEAD) && (data_break == Format.PANEL_BREAK) && 
    				(cmd == Format.CMD_500MS_ADC)) {
    			index = i + 5;
    			data_len = (buffer[i+3] << 8) + buffer[i+4];
    			len_500ms = data_len;
    			break;
    		}
    	}
    	
    	if (index == 0) {
    		return null;
    	}
//    	Log.d(TAG, "500MS DATA.");
    	for (int i = 0; i < data_len; i++) {
    		temp[i] = buffer[index];
//    		Log.d(TAG, Integer.toHexString(temp[i]));
    		index++;
    	}
    	
    	return temp;	
    }
    
    public int[] get20sDataFromFile(int[] buffer, int len) {
    	int[] temp = new int[1024];
    	int data_head = 0;
    	int data_break = 0;
    	int data_len = 0;
    	int cmd = 0;
    	int index = 0;
    	for (int i = 0; i < len; i++) {
        	data_head = buffer[i];
        	data_break = buffer[i+1];
        	cmd = buffer[i+2];
    		if ((data_head == Format.PANEL_HEAD) && (data_break == Format.PANEL_BREAK) && 
    				(cmd == Format.CMD_20S_ADC)) {
    			index = i + 5;
    			data_len = (buffer[i+3] << 8) + buffer[i+4];
    			len_20s = data_len;
    			break;
    		}
    	}
    	
    	if (index == 0) {
    		return null;
    	}
//    	Log.d(TAG, "20S DATA.");
    	for (int i = 0; i < data_len; i++) {
    		temp[i] = buffer[index];
//    		Log.d(TAG, Integer.toHexString(temp[i]));
    		index++;
    	}
    	
    	return temp;	
    }
    
    public void writeAdValue(File file, float[] fs, int len, float time) {
    	try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(time + " ");
			for (int i = 0; i < len; i++) {
				bw.write(fs[i] + " ");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void writeParam(File file, String str) {
    	try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(str);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public float[] readAdValue(File file) {
    	float[] temp = new float[2048];
    	try {
    		String str = "";
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((str=br.readLine())!=null) {
				String[] strs = str.split(" ");
				Log.d(TAG, "strs.length:" + strs.length);
				for (int i = 0; i < strs.length; i++) {
					temp[i] = Float.parseFloat(strs[i]);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return temp;
    }
 
    public String readParam(File file) {
    	String str = "";
    	try {
    		String temp = "";
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((temp=br.readLine())!=null) {
				str = temp;
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return str;
    }
}
