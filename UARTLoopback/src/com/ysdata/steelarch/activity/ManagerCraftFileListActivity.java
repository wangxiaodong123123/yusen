package com.ysdata.steelarch.activity;

import java.util.ArrayList;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.adapter.ManagerCraftParameterAdapter;
import com.ysdata.steelarch.cloud.util.AppUtil;
import com.ysdata.steelarch.cloud.util.CacheManager;
import com.ysdata.steelarch.database.ProjectDataBaseAdapter;
import com.ysdata.steelarch.database.ProjectPointDataBaseAdapter;
import com.ysdata.steelarch.element.ManagerCraftParameter;
import com.ysdata.steelarch.element.MgrStasticParameter;
import com.ysdata.steelarch.uart.MyActivityManager;
import com.ysdata.steelarch.view.CustomListView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerCraftFileListActivity extends Activity {
	Context context;
	
	private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	private ArrayList<ManagerCraftParameter> mgr_list;
	private ManagerCraftParameterAdapter mAdapter;
	private CustomListView mListView;
	int project_id, subproject_id;

	private TextView mgr_title_tv;
	
	private TextView proj_name_tv;
	private TextView eng_name_tv;
	
	String proj_name_string = "";
	String subproj_name_string = "";
	String proj_eng_string = "";
	TextView mileage_tunnel_entrance_tv;
	TextView stastic_date_tv;
	TextView stastic_mileage_tv;
	TextView stastic_distance_tv;
	
	String mileage_tunnel_entrance = "";
	MgrStasticParameter mMgrStasticParameter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_craft_file_list);
		context = this;
		MyActivityManager.addActivity(ManagerCraftFileListActivity.this);
		initView();
		project_id = CacheManager.getDbProjectId();
		subproject_id = CacheManager.getDbSubProjectId();
    	mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
    	mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
    	mProjectPointBase.closeDb();
    	if (mProjectBase.openDb() && mProjectPointBase.openDb(project_id, subproject_id)) {
    		AppUtil.log("project_id:"+project_id+",subproject_id:"+subproject_id);
    		proj_name_string = mProjectBase.getProjectName(project_id);
    		subproj_name_string = mProjectBase.getSubProjectName(subproject_id);
    		proj_eng_string = proj_name_string + "/" + subproj_name_string;
    		mileage_tunnel_entrance = mProjectBase.getSubProjectStartMileage(subproject_id);
    		initData();
    	} else {
    		Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
    	}
	}
	
	private void initView() {
		eng_name_tv = (TextView) findViewById(R.id.mgr_craft_engin_name);
		proj_name_tv = (TextView) findViewById(R.id.mgr_project_name);
		mgr_title_tv = (TextView) findViewById(R.id.mgr_title_id);
		
		mileage_tunnel_entrance_tv = (TextView) findViewById(R.id.mgr_tunel_entry_mileage);
		stastic_date_tv = (TextView) findViewById(R.id.mgr_stastic_date);
		stastic_mileage_tv = (TextView) findViewById(R.id.mgr_stastic_mileage);
		stastic_distance_tv = (TextView) findViewById(R.id.mgr_stastic_mileage_distance);
		
		mListView = (CustomListView)findViewById(R.id.mgr_craft_table_lv);
	}
	
	private void initData() {
		engNameInit();
		mileage_tunnel_entrance_tv.setText(mileage_tunnel_entrance);
		mMgrStasticParameter = mProjectPointBase.getMgrStasticParameter();
		if (mMgrStasticParameter != null) {
			stastic_date_tv.setText(mMgrStasticParameter.getDate());
			stastic_mileage_tv.setText(mMgrStasticParameter.getMileage());
			stastic_distance_tv.setText(mMgrStasticParameter.getMileage_distance());
		}
		tableListInit();
	}
	
	private void tableListInit() {
		mgr_list = new ArrayList<ManagerCraftParameter>();
		mProjectPointBase.getManagerCraftParameterList(mgr_list, 99999);
		mAdapter = new ManagerCraftParameterAdapter(mgr_list, this);
		mListView.setAdapter(mAdapter);
	}
	
	private void engNameInit() {
		mgr_title_tv.setText("数据管理->钢拱架统计表");
		eng_name_tv.setText(subproj_name_string);
		proj_name_tv.setText(proj_name_string);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyActivityManager.removeActivity(ManagerCraftFileListActivity.this);
		super.onDestroy();
	}
}
