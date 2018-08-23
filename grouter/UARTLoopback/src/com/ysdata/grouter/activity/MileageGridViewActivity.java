package com.ysdata.grouter.activity;

import java.util.ArrayList;

import com.ysdata.grouter.R;
import com.ysdata.grouter.adapter.MileageBrowserAdapter;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.uart.MyActivityManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

public class MileageGridViewActivity  extends Activity {

	private Context context;
    ArrayList<String> mileageList;  
    private GridView gridView;
    MileageBrowserAdapter adapter;
    ProgressDialog loadImageProgDialog;
    private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mileage_gridview_phone);
        context = this;
        MyActivityManager.addActivity(MileageGridViewActivity.this);
        mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
        mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
        mileageList = new ArrayList<String>(); 
    	gridView = (GridView) findViewById(R.id.mileage_gridview_id);
    	/*gridView.setOnItemClickListener(new OnItemClickListener() {  
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AppUtil.log( "sectionName:"+mileageList.get(position));
			}
    	}); */
    	/*for (int i = 0; i < 1000; i++) {
    		mileageList.add("k100+"+i);
    	}*/
    	Intent intent = getIntent();
    	if (mProjectBase.openDb()) {
    		int project_id = mProjectBase.getProjectId(intent.getStringExtra("project_name"));
    		int subproject_id = mProjectBase.getSubProjectId(intent.getStringExtra("subproject_name"));
    		if (mProjectPointBase.openDb(project_id, subproject_id)) {
    			mileageList = mProjectPointBase.getMileageNameList();
    			mProjectPointBase.closeDb();
    		} else {
    			Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
    		}
    	} else {
    		Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
    	}
    	adapter = new MileageBrowserAdapter(mileageList, context);
    	gridView.setAdapter(adapter); 
    }
    
   @Override
    protected void onDestroy() {
	   AppUtil.log("========onDestroy========");
    	MyActivityManager.removeActivity(MileageGridViewActivity.this);
    	super.onDestroy();
    }
}
