package com.ysdata.blender.activity;

import java.util.ArrayList;
import java.util.List;

import com.ysdata.blender.R;
import com.ysdata.blender.adapter.DdMixParameterAdapter;
import com.ysdata.blender.cloud.api.MixRatio;
import com.ysdata.blender.cloud.api.MixRatioListResponse;
import com.ysdata.blender.cloud.util.ApiClient;
import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.cloud.util.SharedView;
import com.ysdata.blender.cloud.util.UIUtilities;
import com.ysdata.blender.database.ProjectDataBaseAdapter;
import com.ysdata.blender.database.ProjectPointDataBaseAdapter;
import com.ysdata.blender.element.DdMixParameter;
import com.ysdata.blender.uart.MyActivityManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadMixRatioListActivity extends Activity {
	
	private Context context = null;
	TextView title;
	private ListView mListView;
	ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	private ArrayList<DdMixParameter> list;
	private DdMixParameterAdapter mAdapter;
	private ApiClient apiClient;
	int subproject_id, project_id;
	TextView user_info_tv;
	boolean isLoading = true;
	private Activity activity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dd_mixratio_list);
        context = this;
        activity = DownloadMixRatioListActivity.this;
        MyActivityManager.addActivity(DownloadMixRatioListActivity.this);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
		apiClient = ApiClient.getInstance(context);
		project_id = CacheManager.getDbProjectId();
		list = new ArrayList<DdMixParameter>();
		subproject_id = CacheManager.getDbSubProjectId();
		mProjectBase.closeDb();
		mProjectPointBase.closeDb();
		if (mProjectBase.openDb() && mProjectPointBase.openDb(project_id, subproject_id)) {
			InitView();
			mListView = (ListView)findViewById(R.id.section_list);
			
			/*******************test******************/
//			mProjectPointBase.clearMixDownloadRecord();
//			mProjectPointBase.insertMixDownloadRecord(1, 0.45, "2018年2月7日");
//			mProjectPointBase.insertMixDownloadRecord(2, 0.5, "2018年2月10日");
//			mProjectPointBase.insertMixDownloadRecord(3, 0.47, "2018年2月15日");
			/****************************************/
			
			mProjectPointBase.getDdMixList(list);
			mAdapter = new DdMixParameterAdapter(list, context);
			mListView.setAdapter(mAdapter);
			SharedView.showProgressBar(activity, "正在下载搅拌参数......");
			new getMixRatioParameterThread().start();
		} else {
			Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
		}
    }
    
	private void InitView() {
		title = (TextView)findViewById(R.id.sectionlist_title);
		String proj_name = mProjectBase.getProjectName(project_id);
		String subproj_name = mProjectBase.getSubProjectName(subproject_id);
		title.setText("云服务->"+proj_name+"->"+subproj_name+"->搅拌参数");
		user_info_tv = (TextView) findViewById(R.id.user_info);
		user_info_tv.setText(CacheManager.getUserName() + " 欢迎你!");
	}
    
    class getMixRatioParameterThread extends Thread {
    	@Override
    	public void run() {
    		super.run();
    		MixRatioListResponse response = apiClient.getMixRatioList(subproject_id, 0);
    		if (response != null && response.isSuccess) {
    			mHandler.sendMessage(mHandler.obtainMessage(1, response));
    		} else {
    			mHandler.sendEmptyMessage(4);
    		}
    		
    	}
    }
    
	 private final Handler mHandler = new Handler() {
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
				case 1:
					SharedView.cancelProgressBar();
					UIUtilities.showToast(activity, "水灰比同步成功", true);
					MixRatioListResponse response = (MixRatioListResponse) msg.obj;
					List<MixRatio> ratiolist = response.Data;
					if (ratiolist != null) {
						mProjectPointBase.clearMixDownloadRecord();
						for (int i = 0; i < ratiolist.size(); i++) {
							mProjectPointBase.insertMixDownloadRecord(i+1, 
									ratiolist.get(i).MixRatioWater, ratiolist.get(i).CreateTime);
						}
						mProjectPointBase.getDdMixList(list);
						mListView.setAdapter(mAdapter);
					} else {
						UIUtilities.showToast(activity, "下载空指针异常", true);
					}
					break;
					
				case 2:
					SharedView.cancelProgressBar();
					UIUtilities.showToast(activity, (String)msg.obj, true);
					SharedView.showUserExpiredAlertDialog(activity);
					break;
					
				case 3:
					SharedView.cancelProgressBar();
					break;
					
				case 4:
					SharedView.cancelProgressBar();
					UIUtilities.showToast(activity, "下载失败", true);
					break;
					
				case 5:
					break;
					
			default:
				break;
			}
		 };
	 };
	    
    protected void onDestroy() {
    	MyActivityManager.removeActivity(activity);
    	super.onDestroy();
    };
    
    @Override
    protected void onStart() {
    	AppUtil.log( "onStart.");
    	super.onStart();
    }
    
    @Override
    protected void onRestart() {
    	AppUtil.log( "onRestart.");
    	super.onRestart();
    }
    
    @Override
    protected void onResume() {
    	AppUtil.log( "onResume.");
    	super.onResume();
    }
}

