package com.ysdata.blender.activity;

import com.ysdata.blender.R;
import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.database.ProjectDataBaseAdapter;
import com.ysdata.blender.database.ProjectPointDataBaseAdapter;
import com.ysdata.blender.element.ManagerCraftParameter;
import com.ysdata.blender.element.MixCraftParameter;
import com.ysdata.blender.uart.MyActivityManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerCraftFileActivity extends Activity {
	Context context;
	
	private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	int project_id, subproject_id;
	int mix_id = 0;

	private TextView mix_date_tv;
	private TextView mix_ratio_tv;
	
	private ImageView mix_result_igv;
	private ImageView mix_scene_igv;
	
	private TextView mgr_title_tv;
	
	private TextView proj_name_tv;
	private TextView eng_name_tv;
	
	private TextView mix_id_tv;
	private TextView mix_start_time_tv;
	private TextView mix_end_time_tv;
	private TextView mix_cement_weigh_tv;
	private TextView mix_water_weigh_tv;
	private TextView mix_weigh_tv;
	
	
	private ManagerCraftParameter mgrCraftParameter;
	private MixCraftParameter mixCraftParameter;
	
	String proj_name_string = "";
	String subproj_name_string = "";
	String proj_eng_string = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_craft_file_phone);
		context = this;
		MyActivityManager.addActivity(ManagerCraftFileActivity.this);
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
    		mgrCraftParameter = CacheManager.getMgrCraftParameter();
    		if (mgrCraftParameter == null) {
    			mgrCraftParameter = new ManagerCraftParameter();
    		}
    		initView();
    	} else {
    		Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
    	}
	}
	
	
	private void showImage() {
		String pic_result_string = mProjectPointBase.getMixResultPicString(mix_id);
		String pic_scene_string = mProjectPointBase.getMixScenePicString(mix_id);
		
		if (pic_result_string != null && pic_result_string.length() > 0) {
			String[] str_arr = pic_result_string.split(" ");
			byte[] int_arr = new byte[str_arr.length];
			for (int i = 0; i < str_arr.length; i++) {
				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 10);
//				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 16); //for test
			}
			Bitmap bm = BitmapFactory.decodeByteArray(int_arr, 0, int_arr.length);
			mix_result_igv.setImageBitmap(bm); 
		}
		
		if (pic_scene_string != null && pic_scene_string.length() > 0) {
			String[] str_arr = pic_scene_string.split(" ");
			byte[] int_arr = new byte[str_arr.length];
			for (int i = 0; i < str_arr.length; i++) {
				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 10);
//				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 16);
			}
			Bitmap bm = BitmapFactory.decodeByteArray(int_arr, 0, int_arr.length);
			mix_scene_igv.setImageBitmap(bm); 
		}
	}
	
	private void initView() {
		engNameInit();
		mixDataInit();
		picInitShow();
		tableInit();
	}
	
	private void tableInit() {
		mix_id_tv = (TextView) findViewById(R.id.mgr_detail_id);
		mix_start_time_tv = (TextView) findViewById(R.id.mgr_detail_start_time);
		mix_end_time_tv = (TextView) findViewById(R.id.mgr_detail_end_time);
		mix_cement_weigh_tv = (TextView) findViewById(R.id.mgr_detail_cement_weigh);
		mix_water_weigh_tv = (TextView) findViewById(R.id.mgr_detail_water_weigh);
		mix_weigh_tv = (TextView) findViewById(R.id.mgr_detail_weigh);
		
		if (mgrCraftParameter != null) {
			mix_id_tv.setText(mgrCraftParameter.getId()+"");
			mix_cement_weigh_tv.setText(mgrCraftParameter.getCementWeight()+"");
			mix_water_weigh_tv.setText(mgrCraftParameter.getWaterWeigh()+"");
			mix_weigh_tv.setText(mgrCraftParameter.getTotalWeigh()+"");
		}
		if (mixCraftParameter != null) {
			mix_start_time_tv.setText(mixCraftParameter.getStartTime());
			mix_end_time_tv.setText(mixCraftParameter.getEndTime());
		}
	}
			  
	private void engNameInit() {
		eng_name_tv = (TextView) findViewById(R.id.mgr_craft_engin_name);
		proj_name_tv = (TextView) findViewById(R.id.mgr_project_name);
		mgr_title_tv = (TextView) findViewById(R.id.mgr_title_id);
		mgr_title_tv.setText("数据管理->搅拌详情表");
		eng_name_tv.setText(subproj_name_string);
		proj_name_tv.setText(proj_name_string);
	}
	
	private void mixDataInit() {
		mix_date_tv = (TextView) findViewById(R.id.mgr_mix_date_id);
		mix_ratio_tv = (TextView) findViewById(R.id.mgr_mix_ratio);
		
		if (mgrCraftParameter != null) {
			mix_id = mgrCraftParameter.getId();
			mixCraftParameter = mProjectPointBase.getMixCraftParameterByMixId(mix_id);
			if (mixCraftParameter != null) {
				mix_ratio_tv.setText(mixCraftParameter.getMixRatio()+"");
				mix_date_tv.setText(mixCraftParameter.getMixDate());
			}
		}
	}
	
	private void picInitShow() {
		mix_result_igv = (ImageView) findViewById(R.id.mgr_mix_result_pic);
		mix_scene_igv = (ImageView) findViewById(R.id.mgr_mix_scene_pic);
		showImage();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyActivityManager.removeActivity(ManagerCraftFileActivity.this);
		super.onDestroy();
	}
}
