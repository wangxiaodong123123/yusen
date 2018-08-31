package com.ysdata.blender.activity;

import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.database.ProjectDataBaseAdapter;
import com.ysdata.blender.uart.MyActivityManager;
import com.ysdata.blender.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadSelectionActivity extends Activity{
	private Context mContext;
	private View m_Array[];
	private ImageView mImageView[];
	private int mIndex = 0;
	int subproject_id, project_id;
	TextView user_info_tv;
	TextView title;
	ProjectDataBaseAdapter mProjectBase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.download_selection_phone);
		mContext = this;
		MyActivityManager.addActivity(DownloadSelectionActivity.this);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(mContext);
		project_id = CacheManager.getDbProjectId();
		subproject_id = CacheManager.getDbSubProjectId();
		mProjectBase.closeDb();
		if (mProjectBase.openDb()) {
			initView();
		} else {
			Toast.makeText(mContext, "数据库打开失败.", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void setVisible(int cur_index, int pre_index) {
		if (cur_index == pre_index) {
			return;
		}
		mImageView[pre_index].setVisibility(View.GONE);
		mImageView[cur_index].setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyActivityManager.removeActivity(DownloadSelectionActivity.this);
	}
	
	private void initView() {
        title = (TextView)findViewById(R.id.dev_list_title);
		String proj_name = mProjectBase.getProjectName(project_id);
		String subproj_name = mProjectBase.getSubProjectName(subproject_id);
		title.setText("云服务->"+proj_name+"->"+subproj_name+"->下载选择");
		user_info_tv = (TextView) findViewById(R.id.user_info);
		user_info_tv.setText(CacheManager.getUserName() + " 欢迎你!");
		m_Array = new RelativeLayout[2];
		m_Array[0]=findViewById(R.id.panel_relativelayout);
		m_Array[1]=findViewById(R.id.sdcard_relativelayout);

		mImageView = new ImageView[2];
		mImageView[0]=(ImageView)findViewById(R.id.panel_imageview);
		mImageView[1]=(ImageView)findViewById(R.id.sdcard_imageview);
		
		m_Array[0].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVisible(0, mIndex);
				mIndex = 0;
				Intent intent=new Intent(DownloadSelectionActivity.this, DownloadMixRatioListActivity.class);
				startActivity(intent);
			}
		});
		
		m_Array[1].setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				setVisible(1, mIndex);
				mIndex = 1;
				Intent intent=new Intent(DownloadSelectionActivity.this, SyncBlenderDataListActivity.class);
				startActivity(intent);
			}
		});
	}
}
