package com.ysdata.blender.activity;

import java.util.ArrayList;

import com.ysdata.blender.R;
import com.ysdata.blender.adapter.UploadMixDataBrowserAdapter;
import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.SharedView;
import com.ysdata.blender.database.ProjectDataBaseAdapter;
import com.ysdata.blender.database.ProjectPointDataBaseAdapter;
import com.ysdata.blender.element.MixUploadState;
import com.ysdata.blender.uart.MyActivityManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class UploadMixDateGridViewActivity  extends Activity {

	private Context context;
    ArrayList<MixUploadState> mixUploadStateList;
    private GridView gridView;
    UploadMixDataBrowserAdapter adapter;
    ProgressDialog loadImageProgDialog;
    private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	String action = "mgr";
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mixdate_gridview);
        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        LinearLayout ly_bg = (LinearLayout) findViewById(R.id.layout_bg);
        if (action.equals("mgr")) {
        	ly_bg.setBackgroundResource(R.drawable.manager_bg);
        } else if (action.equals("cloud")) {
        	ly_bg.setBackgroundResource(R.drawable.cloud_bg);
        }
        context = this;
        MyActivityManager.addActivity(UploadMixDateGridViewActivity.this);
        mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
        mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
        mixUploadStateList = new ArrayList<MixUploadState>();
    	gridView = (GridView) findViewById(R.id.mixdate_gridview_id);
    	if (mProjectBase.openDb()) {
    		int project_id = mProjectBase.getProjectId(intent.getStringExtra("project_name"));
    		int subproject_id = mProjectBase.getSubProjectId(intent.getStringExtra("subproject_name"));
    		if (mProjectPointBase.openDb(project_id, subproject_id)) {
    			new getSectionsUploadStateThread().start();
    		} else {
    			Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
    		}
    	} else {
    		Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
    	}
    }
    
    class getSectionsUploadStateThread extends Thread {
    	@Override
    	public void run() {
    		mHandler.sendEmptyMessage(1);
    		mixUploadStateList = mProjectPointBase.getMixDateUploadStateList();
    		mProjectPointBase.closeDb();
    		mHandler.sendEmptyMessage(2);
    		super.run();
    	}
    }
    
    private final Handler mHandler = new Handler() { 
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
    		case 1:
    			SharedView.showProgressBar(UploadMixDateGridViewActivity.this, "正在获取搅拌列表......");
    			break;
    			
    		case 2:
    			SharedView.cancelProgressBar();
    			adapter = new UploadMixDataBrowserAdapter(mixUploadStateList, context);
    	    	gridView.setAdapter(adapter); 
    			break;
    		default:
    			break;
    		}
    	};
    };
    
   @Override
    protected void onDestroy() {
	   AppUtil.log("========onDestroy========");
    	MyActivityManager.removeActivity(UploadMixDateGridViewActivity.this);
    	super.onDestroy();
    }
}
