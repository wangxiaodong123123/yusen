package com.ysdata.steelarch.activity;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.cloud.util.AppUtil;
import com.ysdata.steelarch.cloud.util.CacheManager;
import com.ysdata.steelarch.database.ProjectDataBaseAdapter;
import com.ysdata.steelarch.database.ProjectPointDataBaseAdapter;
import com.ysdata.steelarch.element.ManagerCraftParameter;
import com.ysdata.steelarch.element.SteelArchDetailParameter;
import com.ysdata.steelarch.uart.MyActivityManager;

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
	int orderno = 0;

	private TextView mileage_entry_tv;
	private TextView measure_date_tv;
	
	private TextView mgr_title_tv;
	
	private TextView proj_name_tv;
	private TextView eng_name_tv;
	
	private ImageView left_pic_dir_tunnel_entrance_igv;
	private ImageView left_pic_dir_tunnelface_igv;
	private TextView left_order_tv;
	private TextView left_measure_time_tv;
	private TextView left_name_tv;
	private TextView left_design_distance_tv;
	private TextView left_measure_distance_tv;
	private TextView left_steelarch_to_tunnel_entrance_tv;
	private TextView left_steelarch_to_tunnelface_tv;
	
	private ImageView right_pic_dir_tunnel_entrance_igv;
	private ImageView right_pic_dir_tunnelface_igv;
	private TextView right_order_tv;
	private TextView right_measure_time_tv;
	private TextView right_name_tv;
	private TextView right_design_distance_tv;
	private TextView right_measure_distance_tv;
	private TextView right_steelarch_to_tunnel_entrance_tv;
	private TextView right_steelarch_to_tunnelface_tv;	
	
	private ManagerCraftParameter mgrCraftParameter;
	private SteelArchDetailParameter mSteelArchDetailParameter;
	
	String proj_name_string = "";
	String subproj_name_string = "";
	String proj_eng_string = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_craft_file_phone);
		context = this;
		MyActivityManager.addActivity(ManagerCraftFileActivity.this);
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
    		mgrCraftParameter = CacheManager.getMgrCraftParameter();
    		if (mgrCraftParameter == null) {
    			mgrCraftParameter = new ManagerCraftParameter();
    		}
    		initData();
    	} else {
    		Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
    	}
	}
	
	private void showLeftImage() {
		String left_pic_dir_entrance_string = mSteelArchDetailParameter.getLeft_pic_dir_tunnel_entrance();
		String left_pic_dir_face_string = mSteelArchDetailParameter.getLeft_pic_dir_tunnel_face();
		
		if (left_pic_dir_entrance_string != null && left_pic_dir_entrance_string.length() > 0) {
			AppUtil.log("left_pic_dir_entrance_string:"+left_pic_dir_entrance_string);
			String[] str_arr = left_pic_dir_entrance_string.split(" ");
			byte[] int_arr = new byte[str_arr.length];
			for (int i = 0; i < str_arr.length; i++) {
//				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 10);
				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 16); //for test
			}
			Bitmap bm = BitmapFactory.decodeByteArray(int_arr, 0, int_arr.length);
			left_pic_dir_tunnel_entrance_igv.setImageBitmap(bm); 
		}
		
		if (left_pic_dir_face_string != null && left_pic_dir_face_string.length() > 0) {
			String[] str_arr = left_pic_dir_face_string.split(" ");
			byte[] int_arr = new byte[str_arr.length];
			for (int i = 0; i < str_arr.length; i++) {
//				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 10);
				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 16);
			}
			Bitmap bm = BitmapFactory.decodeByteArray(int_arr, 0, int_arr.length);
			left_pic_dir_tunnelface_igv.setImageBitmap(bm); 
		}
	}
	
	private void showRightImage() {
		String right_pic_dir_entrance_string = mSteelArchDetailParameter.getRight_pic_dir_tunnel_entrance();
		String right_pic_dir_face_string = mSteelArchDetailParameter.getRight_pic_dir_tunnel_face();
		
		if (right_pic_dir_entrance_string != null && right_pic_dir_entrance_string.length() > 0) {
			String[] str_arr = right_pic_dir_entrance_string.split(" ");
			byte[] int_arr = new byte[str_arr.length];
			for (int i = 0; i < str_arr.length; i++) {
//				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 10);
				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 16); //for test
			}
			Bitmap bm = BitmapFactory.decodeByteArray(int_arr, 0, int_arr.length);
			right_pic_dir_tunnel_entrance_igv.setImageBitmap(bm); 
		}
		
		if (right_pic_dir_face_string != null && right_pic_dir_face_string.length() > 0) {
			String[] str_arr = right_pic_dir_face_string.split(" ");
			byte[] int_arr = new byte[str_arr.length];
			for (int i = 0; i < str_arr.length; i++) {
//				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 10);
				int_arr[i] = (byte) (int)Integer.valueOf(str_arr[i], 16);
			}
			Bitmap bm = BitmapFactory.decodeByteArray(int_arr, 0, int_arr.length);
			right_pic_dir_tunnelface_igv.setImageBitmap(bm); 
		}
	}
	
	private void initView() {
		eng_name_tv = (TextView) findViewById(R.id.mgr_craft_engin_name);
		proj_name_tv = (TextView) findViewById(R.id.mgr_project_name);
		mgr_title_tv = (TextView) findViewById(R.id.mgr_title_id);
		
		measure_date_tv = (TextView) findViewById(R.id.mgr_measure_date);
		mileage_entry_tv = (TextView) findViewById(R.id.mgr_tunnel_entry_mileage);
		left_order_tv = (TextView) findViewById(R.id.mgr_detail_left_id);
		right_order_tv = (TextView) findViewById(R.id.mgr_detail_right_id);
		left_measure_time_tv = (TextView) findViewById(R.id.mgr_detail_left_time);
		right_measure_time_tv = (TextView) findViewById(R.id.mgr_detail_right_time);
		left_name_tv = (TextView) findViewById(R.id.mgr_detail_left_name);
		right_name_tv = (TextView) findViewById(R.id.mgr_detail_right_name);
		left_design_distance_tv = (TextView) findViewById(R.id.mgr_detail_left_design_distance);
		right_design_distance_tv = (TextView) findViewById(R.id.mgr_detail_right_design_distance);
		left_measure_distance_tv = (TextView) findViewById(R.id.mgr_detail_left_measure_distance);
		right_measure_distance_tv = (TextView) findViewById(R.id.mgr_detail_right_measure_distance);
		left_steelarch_to_tunnel_entrance_tv = (TextView) findViewById(R.id.mgr_detail_left_steelarch_to_entrance_distance);
		right_steelarch_to_tunnel_entrance_tv = (TextView) findViewById(R.id.mgr_detail_right_steelarch_to_entrance_distance);
		left_steelarch_to_tunnelface_tv = (TextView) findViewById(R.id.mgr_detail_left_steelarch_to_tunnelface_distance);
		right_steelarch_to_tunnelface_tv = (TextView) findViewById(R.id.mgr_detail_right_steelarch_to_tunnelface_distance);
		
		left_pic_dir_tunnel_entrance_igv = (ImageView) findViewById(R.id.mgr_left_pic_dir_tunnel_entrance);
		left_pic_dir_tunnelface_igv = (ImageView) findViewById(R.id.mgr_left_pic_dir_tunnelface);
		right_pic_dir_tunnel_entrance_igv = (ImageView) findViewById(R.id.mgr_right_pic_dir_tunnel_entrance);
		right_pic_dir_tunnelface_igv = (ImageView) findViewById(R.id.mgr_right_pic_dir_tunnelface);
	}
	
	private void initData() {
		engNameInit();
		orderno = mgrCraftParameter.getId();
		mSteelArchDetailParameter = mProjectPointBase.getSteelArchDetailParameter(orderno);
		if (mSteelArchDetailParameter != null) {
			tableInit();
			picInitShow();
		}
	}
	
	private void tableInit() {
		mSteelArchDetailParameter.setName(mgrCraftParameter.getName());
		mSteelArchDetailParameter.setDesign_distance(mgrCraftParameter.getDesignSteelarchToSteelarchDistance());
		mSteelArchDetailParameter.setMileageTunnelEntry(mProjectBase.getSubProjectStartMileage(subproject_id));
		measure_date_tv.setText(mSteelArchDetailParameter.getLeftMeasureDate());
		mileage_entry_tv.setText(mSteelArchDetailParameter.getMileageTunnelEntry());
		left_order_tv.setText(mSteelArchDetailParameter.getOrderno()+"");
		left_measure_time_tv.setText(mSteelArchDetailParameter.getLeft_measure_time());
		left_name_tv.setText(mSteelArchDetailParameter.getName());
		left_design_distance_tv.setText(mSteelArchDetailParameter.getDesign_distance()+"");
		left_measure_distance_tv.setText(mSteelArchDetailParameter.getLeft_measure_distance()+"");
		left_steelarch_to_tunnel_entrance_tv.setText(mSteelArchDetailParameter.getSteelarch_to_tunnel_entrance_distance()+"");
		left_steelarch_to_tunnelface_tv.setText(mSteelArchDetailParameter.getLeft_steelarch_to_tunnelface_distance()+"");
		right_order_tv.setText(mSteelArchDetailParameter.getOrderno()+"");
		right_measure_time_tv.setText(mSteelArchDetailParameter.getRight_measure_time());
		right_name_tv.setText(mSteelArchDetailParameter.getName());
		right_design_distance_tv.setText(mSteelArchDetailParameter.getDesign_distance()+"");
		right_measure_distance_tv.setText(mSteelArchDetailParameter.getRight_measure_distance()+"");
		right_steelarch_to_tunnel_entrance_tv.setText(mSteelArchDetailParameter.getSteelarch_to_tunnel_entrance_distance()+"");
		right_steelarch_to_tunnelface_tv.setText(mSteelArchDetailParameter.getRight_steelarch_to_tunnelface_distance()+"");
	}
			  
	private void engNameInit() {
		mgr_title_tv.setText("数据管理->钢拱架间距详情表");
		eng_name_tv.setText(subproj_name_string);
		proj_name_tv.setText(proj_name_string);
	}
	
	private void picInitShow() {
		showLeftImage();
		showRightImage();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyActivityManager.removeActivity(ManagerCraftFileActivity.this);
		super.onDestroy();
	}
}
