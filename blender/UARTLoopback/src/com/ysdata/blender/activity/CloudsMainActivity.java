package com.ysdata.blender.activity;

import com.ysdata.blender.R;
import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.cloud.util.ConstDef;
import com.ysdata.blender.cloud.util.SharedView;
import com.ysdata.blender.cloud.util.UIUtilities;
import com.ysdata.blender.database.FileOperator;
import com.ysdata.blender.database.SharePrefOperator;
import com.ysdata.blender.service.ListenNetStateService;
import com.ysdata.blender.storage.ExtSdCheck;
import com.ysdata.blender.uart.MyActivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class CloudsMainActivity  extends Activity {
    private Context context = null;
    private TextView user_info_tv;
    private ImageButton download_mb;
    private ImageButton upload_mb;
    private Button usr_logout_bt;
    private Intent intent;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cloud_main_phone);
        context = this;
        MyActivityManager.addActivity(CloudsMainActivity.this);
        initView();
        intent = new Intent(this, ListenNetStateService.class);
        AppUtil.log("=========onCreate==========");
        startService(intent);
    }
   
    private void initView() {
    	download_mb = (ImageButton) findViewById(R.id.download_imagebutton_id);
    	upload_mb = (ImageButton) findViewById(R.id.uplaod_imagebutton_id);
    	usr_logout_bt = (Button) findViewById(R.id.usr_logout);
    	user_info_tv = (TextView) findViewById(R.id.user_info);
    	user_info_tv.setText(CacheManager.getUserName() + " 欢迎你!");
    	
    	usr_logout_bt.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new  AlertDialog.Builder(CloudsMainActivity.this)    
				.setTitle("提示" )
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("该操作将清除该用户所有数据，是否继续？" )  
				.setPositiveButton("确定" , new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new ClearUserThread().start();
					}
				} )   
				.setNegativeButton("取消", null)
				.create()
				.show(); 
			}
		});
    	download_mb.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CloudsMainActivity.this, ContractSectionListActivity.class);
				startActivity(intent);
			}
		});
    	upload_mb.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread() {
					@Override
					public void run() {
						startActivity(new Intent(CloudsMainActivity.this, UploadMixParamterActivity.class));
					}
				}.start();
			}
		});
    }
    
    class ClearUserThread extends Thread {
    	@Override
    	public void run() {
    		mHandler.sendEmptyMessage(1);
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		String extsdcard_dir = ExtSdCheck.getExtSDCardPath();
    		FileOperator mFileOperator = FileOperator.getSingleFileOperator(CloudsMainActivity.this);
			boolean ret_delDir = mFileOperator.DeleteAllDir(extsdcard_dir+"/"+ConstDef.PROJECT_NAME) &&
					mFileOperator.DeleteAllDir("/data/data/com.ysdata.blender/shared_prefs");
			SharePrefOperator sharePrefs = SharePrefOperator.getSingleSharePrefOperator(context);
			sharePrefs.initSharePrefs();
			if (ret_delDir) {
				mHandler.sendEmptyMessage(2);
			} else {
				mHandler.sendEmptyMessage(3);
			}
    		super.run();
    	}
    }
    
    private final Handler mHandler = new Handler() {
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
				case 1:
					SharedView.showProgressBar(CloudsMainActivity.this, "正在注销该用户......");
					break;
					
				case 2:
					SharedView.cancelProgressBar();
					UIUtilities.showToast(CloudsMainActivity.this, "注销完成", true);
					if(AccountActivity.accoutActivityInstance != null) {
						AccountActivity.accoutActivityInstance.clearUserText();
					}
					CloudsMainActivity.this.finish();
					break;
					
				case 3:
					SharedView.cancelProgressBar();
					UIUtilities.showToast(CloudsMainActivity.this, "注销失败", true);
			default:
				break;
			}
		 };
	 };
    
    @Override
    protected void onDestroy() {
    	MyActivityManager.removeActivity(CloudsMainActivity.this);
    	stopService(intent);
    	super.onDestroy();
    }
}
