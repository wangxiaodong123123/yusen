package com.ysdata.steelarch.activity;

import java.io.File;
import java.util.ArrayList;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.adapter.ImageBrowserAdapter;
import com.ysdata.steelarch.cloud.util.ConstDef;
import com.ysdata.steelarch.storage.ExtSdCheck;
import com.ysdata.steelarch.uart.MyActivityManager;
import com.ysdata.steelarch.wireless.client.Format;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class DrawImageGridViewActivity  extends Activity {
	private static final String TAG = "ys200";

	private Context context;
    ArrayList<String> items;  
    ArrayList<String> paths;  
    private GridView gridView;
    ImageBrowserAdapter adapter;
    ProgressDialog loadImageProgDialog;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_gridview);
        context = this;
        MyActivityManager.addActivity(DrawImageGridViewActivity.this);
        items = new ArrayList<String>();  
        paths = new ArrayList<String>(); 
        String extSdPath = ExtSdCheck.getExtSDCardPath();
        if (extSdPath != null) {
        	String image_path =  extSdPath+"/"+ConstDef.PROJECT_NAME+"/draw/image_demo/";
        	gridView = (GridView) findViewById(R.id.draw_gridview); 
        	gridView.setOnItemClickListener(new OnItemClickListener() {  
        		
        		public void onItemClick(AdapterView<?> parent, View v,  
        				int position, long id) {  
        			Log.d(TAG, "title:"+paths.get(position));
        			Intent intent = new Intent(Format.ACTION_LOAD_IMAGE_DEMO);
        			intent.putExtra("image_path", paths.get(position));
        			context.sendBroadcast(intent);
        			finish();
        		}
        	});  
        	getFileDir(image_path);
        	if (items.size() > 1) {
        		new LoadImageThread().start();
        	}
        } else {
        	Toast.makeText(context, "未检测到外部sd卡,加载示例图片失败", Toast.LENGTH_SHORT).show();
        }
    }
    
    class LoadImageThread extends Thread {
    	@Override
		public void run() {
			super.run();
			loadImageHandler.sendEmptyMessage(LOAD_IMAGE_DIALOG_SHOW);
	    	adapter = new ImageBrowserAdapter(items, paths, context);  
			loadImageHandler.sendEmptyMessage(LOAD_IMAGE_DIALOG_DISMISS);
		}
    }
    
    private final static int LOAD_IMAGE_DIALOG_SHOW = 1;
    private final static int LOAD_IMAGE_DIALOG_DISMISS = 2;
    private final static int SET_ADAPTER = 3;
    
    private Handler loadImageHandler = new Handler () {
    	@Override
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
    		case LOAD_IMAGE_DIALOG_SHOW:
				loadImageProgDialog = ProgressDialog.show(DrawImageGridViewActivity.this, 
						"加载图片", "正在从外部sd卡的/"+
						ConstDef.PROJECT_NAME+"/draw/image_demo/路径下加载示例图片,请等待......", true, false);
				break;
    		case LOAD_IMAGE_DIALOG_DISMISS:
    			gridView.setAdapter(adapter); 
    			
    			try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    			loadImageProgDialog.dismiss();
    			break;
    		case SET_ADAPTER:	
    			
    		}
    	}
    };
    
    public void getFileDir(String filePath) {  
        try{  
            items = new ArrayList<String>();  
            paths = new ArrayList<String>();  
            File f = new File(filePath);  
            File[] files = f.listFiles();// 列出所有文件  
          
            // 将所有文件存入list中  
            if(files != null){  
                int count = files.length;// 文件个数  
                for (int i = 0; i < count; i++) {  
                    File file = files[i]; 
                    String filename  = file.getName();
                    if(filename.endsWith("jpg") || filename.endsWith("gif") || filename.endsWith("jpeg") ||
                    		filename.endsWith("bmp") || filename.endsWith("png") || filename.endsWith("JPG") || 
                    		filename.endsWith("BMP") || filename.endsWith("PNG") || filename.endsWith("GIF") || 
                    		filename.endsWith("JPEG"))
                    {
                        items.add(filename);  
                        paths.add(file.getPath());  
                    }
                }  
            }
        } catch(Exception ex) {  
            ex.printStackTrace();  
        }  
    }

    private void initView() {
    	gridView = (GridView) findViewById(R.id.draw_gridview);  
    	ImageBrowserAdapter adapter = new ImageBrowserAdapter(items, paths, context);  
    	gridView.setAdapter(adapter);  

        gridView.setOnItemClickListener(new OnItemClickListener() {  

           public void onItemClick(AdapterView<?> parent, View v,  
                    int position, long id) {  
        	    Log.d(TAG, "title:"+paths.get(position));
        	    Intent intent = new Intent(Format.ACTION_LOAD_IMAGE_DEMO);
           		intent.putExtra("image_path", paths.get(position));
           		context.sendBroadcast(intent);
           		finish();
            }
        });  
    }
    
    @Override
    protected void onDestroy() {
    	MyActivityManager.removeActivity(DrawImageGridViewActivity.this);
    	super.onDestroy();
    }
}
