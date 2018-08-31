package com.ysdata.blender.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.ysdata.blender.cloud.util.ConstDef;
import com.ysdata.blender.storage.ExtSdCheck;

import android.content.Context;
import android.util.Log;

public class FileOperator {
    private Context mContext = null;
    
	private static FileOperator mFileOperator = null;
	public static final String TAG = "ys200";
	private FileOperator(Context context) {
		mContext = context;
	}
	/**
	 * FileOperator
	 **/
	public static FileOperator getSingleFileOperator(Context context) {
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
	
	public void creatDbProjDir(int proj_val) {
		String ExtSDpath = ExtSdCheck.getExtSDCardPath();
		File db_dir = new File(ExtSDpath +ConstDef.DATABASE_PATH);
		
		if(!db_dir.exists()){
			db_dir.mkdirs();
		}
		File db_proj_dir = new File(ExtSDpath + ConstDef.DATABASE_PATH+proj_val);
		if(!db_proj_dir.exists()){
			db_proj_dir.mkdirs();
		}
	}
	
	public void creatDbEngDir(int proj_val,int eng_val) {
		String ExtSDpath = ExtSdCheck.getExtSDCardPath();
		String proj_path = ExtSDpath + ConstDef.DATABASE_PATH+proj_val;
		if (new File(proj_path).exists()) {
			File db_eng_dir = new File(proj_path+"/"+eng_val);
			if(!db_eng_dir.exists()){
				db_eng_dir.mkdirs();
			}
		}
	}
	
	public void creatDbLiChengDir(int proj_val,int eng_val, int licheng_val) {
		String ExtSDpath = ExtSdCheck.getExtSDCardPath();
		String proj_path = ExtSDpath + ConstDef.DATABASE_PATH+proj_val;
		String eng_path = proj_path+"/"+eng_val;
		if (new File(proj_path).exists()) {
			if (new File(eng_path).exists()) {
				File db_eng_dir = new File(proj_path+"/"+eng_val);
				if(!db_eng_dir.exists()){
					db_eng_dir.mkdirs();
				}
			}
		}
	}
	
	public boolean DeleteAllDir(String file) {
		if (DeleteChildDir(file)) {
			File mFile = new File(file);
			if (mFile.exists()) {
				return mFile.delete();
			}
			return true;
		}
		return false;
	}
	
    private boolean DeleteChildDir(String file) {
    	File mFile = new File(file);
    	if (mFile.exists() && mFile.isDirectory()) {
    		String[] list_child = mFile.list();
    		int length = list_child.length;
    		if (length == 0) {
    			mFile.delete();
    		} else {
    			File delFile[] = mFile.listFiles();
    			for (int i = 0; i < length; i++) {
    				if (delFile[i].isDirectory()) {
    					boolean sucess = DeleteChildDir(delFile[i].getAbsolutePath());
    					if (!sucess)
    						return false;
    				}
    				delFile[i].delete();
    			}
    		}
    	}
    	return true;
    }
    public boolean copy(String fromFile, String toFile)
    {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if(!root.exists())
        {
            return false;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();
         
        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if(!targetDir.exists())
        {
            targetDir.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for(int i= 0;i<currentFiles.length;i++)
        {
            if(currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                if (!copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/"))
                {
                	return false;
                }
                 
            }else//如果当前项为文件则进行文件拷贝
            {
                if (!CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName()))
                {
                	return false;
                }
            }
        }
        return true;
    }
     
   
    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    private boolean CopySdcardFile(String fromFile, String toFile)
    {
         
        try
        {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0)
            {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;
             
        } 
        catch (Exception ex)
        {
        	Log.d(TAG, "CopySdcardFile error.file:"+fromFile);
            return false;
        }
    }
}
