package com.ysdata.grouter.activity;

import java.io.File;
import java.io.IOException;

import com.ysdata.grouter.R;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.CacheManager;
import com.ysdata.grouter.cloud.util.ConstDef;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.element.ManagerCraftParameter;
import com.ysdata.grouter.element.MileageParameter;
import com.ysdata.grouter.element.UploadParameter;
import com.ysdata.grouter.element.WrcardParameter;
import com.ysdata.grouter.picture.utils.BitmapUtil;
import com.ysdata.grouter.storage.ExtSdCheck;
import com.ysdata.grouter.uart.MyActivityManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MgrOneAnchorRecordActivity extends Activity {
	
	TextView project_name_tv;
	TextView start_mileage_val;
	TextView end_mileage_val;
	int mileage_id, anchor_id;
	String device = "panel";
	private MileageParameter mileage_parameter;
	private ManagerCraftParameter mManagerCraftParameter;
	private WrcardParameter mWrcardParameter;
	
	Context context;
	int project_id, subproject_id;
	TextView anchor_name_tv;
	TextView design_anchor_len;
	TextView design_preasure;
	TextView measure_pressure;
	TextView slurry_date;
	TextView slurry_start_date;
	TextView slurry_end_date;
	TextView design_cap;
	TextView practice_cap;
	TextView design_hold_time;
	TextView practice_hold_time;
	TextView anchor_type;
	TextView anchor_model;

	TextView eng_name_tv;
	TextView water_ratio_tv;
//	TextView single_work_company_tv;
//	TextView single_supervision_company_tv;
	
	String proj_name_string = "";
	String subproj_name_string = "";
	String anchor_name = "";
	
	Button print_bt;
	View activity;
	
	TextView title_tv;
	private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mgr_one_anchor_record_phone);
		activity = getWindow().getDecorView();
		context = this;
		MyActivityManager.addActivity(MgrOneAnchorRecordActivity.this);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
    	mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
    	project_id = CacheManager.getDbProjectId();
		subproject_id = CacheManager.getDbSubProjectId();
    	if (mProjectBase.openDb() && mProjectPointBase.openDb(project_id, subproject_id)) {
    		proj_name_string = mProjectBase.getProjectName(project_id);
    		subproj_name_string = mProjectBase.getSubProjectName(subproject_id);
    		mileage_id = getIntent().getIntExtra("mileage_id", 1);
    		anchor_id = getIntent().getIntExtra("anchor_id", 1);
    		anchor_name = getIntent().getStringExtra("anchor_name");
    		AppUtil.log("mileage_id:"+mileage_id + " anchor_id:"+anchor_id);
    		initView();
    		mileage_parameter = mProjectPointBase.getMileageParameterById(mileage_id);
    		device = CacheManager.getDevice();
    		mManagerCraftParameter = mProjectPointBase.getMgrCraftParameter(anchor_id);
    		if (device.equals("wrcard")) {
    			mWrcardParameter = mProjectPointBase.getWrcardCraftParameter(anchor_name);
    		}
    		setView(context);
    	} else {
    		Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
    	}
    	UploadParameter parm = mProjectPointBase.getUploadParameterById(anchor_id);
    	AppUtil.log(parm.getAnchorName() + ":" + parm.getGroutingData());
	}
	
	private void initView() {
		project_name_tv = (TextView) findViewById(R.id.single_project_name);
		anchor_name_tv = (TextView) findViewById(R.id.seq_value);
		slurry_date = (TextView) findViewById(R.id.date_value);
		slurry_start_date = (TextView) findViewById(R.id.start_date_value);
		slurry_end_date = (TextView) findViewById(R.id.end_date_value);
		design_anchor_len = (TextView) findViewById(R.id.design_len_value);
		design_cap = (TextView) findViewById(R.id.thereo_cap_value);
		practice_cap = (TextView) findViewById(R.id.practice_cap_value);
		design_preasure = (TextView) findViewById(R.id.design_pressure_value);
		measure_pressure = (TextView) findViewById(R.id.meassure_pressure_value);
		design_hold_time = (TextView) findViewById(R.id.set_hold_time_value);
		practice_hold_time = (TextView) findViewById(R.id.practice_hold_time_value);
		anchor_type = (TextView) findViewById(R.id.anchor_type);
		anchor_model = (TextView) findViewById(R.id.anchor_model);
		title_tv = (TextView) findViewById(R.id.title_id);
		print_bt = (Button) findViewById(R.id.single_curve_print_bt);
		print_bt.setOnClickListener(new PrintBtClick());
		
		eng_name_tv = (TextView) findViewById(R.id.single_eng_name_tv);
		water_ratio_tv = (TextView) findViewById(R.id.single_water_ratio_tv);
//		single_work_company_tv= (TextView) findViewById(R.id.single_work_company_tv);
//		single_supervision_company_tv= (TextView) findViewById(R.id.single_supervision_company_tv);
	}
	
	private void setView(Context context) {
		title_tv.setText("数据管理->注浆管理记录表->注浆单元检测记录表");
		project_name_tv.setText(proj_name_string);
		eng_name_tv.setText(subproj_name_string);
		if (device.equals("panel")) {
			anchor_name_tv.setText(mManagerCraftParameter.getAnchorName());
			slurry_date.setText(mManagerCraftParameter.getDate());
			slurry_start_date.setText(mManagerCraftParameter.getStartDate());
			slurry_end_date.setText(mManagerCraftParameter.getEndDate());
			design_anchor_len.setText(mManagerCraftParameter.getDesignLen()+"");
			design_cap.setText(mManagerCraftParameter.getThereoCap()+"");
			practice_cap.setText(mManagerCraftParameter.getPracticeCap()+"");
			design_preasure.setText(mManagerCraftParameter.getDesignPressure()+"");
			measure_pressure.setText(mManagerCraftParameter.getMeasurePressure()+"");
			design_hold_time.setText(mManagerCraftParameter.getDesignHoldTime()+"");
			practice_hold_time.setText(mManagerCraftParameter.getPracticeHoldTime()+"");
			anchor_type.setText(mManagerCraftParameter.getAnchorType());
			anchor_model.setText(mManagerCraftParameter.getAnchorModel());
		} else {
			anchor_name_tv.setText(mWrcardParameter.getAnchorName());
			slurry_date.setText(mWrcardParameter.getDate());
			slurry_start_date.setText(mWrcardParameter.getStartDate());
			slurry_end_date.setText(mWrcardParameter.getEndDate());
			design_anchor_len.setText(mWrcardParameter.getAnchorLength()+"");
			design_cap.setText(mWrcardParameter.getThereoCap()+"");
			practice_cap.setText(mWrcardParameter.getPracticeCap()+"");
			design_preasure.setText(mWrcardParameter.getDesignPress()+"");
			measure_pressure.setText(mWrcardParameter.getPracticePress()+"");
			design_hold_time.setText(mWrcardParameter.getDesignHoldTime()+"");
			practice_hold_time.setText(mWrcardParameter.getPracticeHoldTime()+"");
			anchor_type.setText(mWrcardParameter.getAnchorType());
			anchor_model.setText(mWrcardParameter.getAnchorModel());
		}
		water_ratio_tv.setText(mileage_parameter.getWaterRatio());
	}
	
	public Bitmap getCanvasSnapshot()
    {
		activity.setDrawingCacheEnabled(true);
		activity.buildDrawingCache(true);
        return activity.getDrawingCache(true);
    }
	
	class PrintBtClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			String sdcarddir = ExtSdCheck.getExtSDCardPath();
			if (sdcarddir != null) {
				String dir = sdcarddir+"/"+ConstDef.PROJECT_NAME+"/print/"+proj_name_string+"/"+ subproj_name_string+"/注浆工点参数曲线表";
				boolean bmp_create = true;
				File path = new File(dir);
				
				if(!path.exists()){
					path.mkdirs();
				}
				File file = new File(dir+"/"+anchor_name+".png");
				if(!file.exists()) {
					try {
						file.createNewFile();
						bmp_create = true;
					} catch (IOException e) {
						AppUtil.log("creat or open "+anchor_name+".png"+" failed.exception:" + e);
						bmp_create = false;
						Toast.makeText(context, "无法生成打印文件！", Toast.LENGTH_SHORT).show();
					}
				}
				if (bmp_create) {
					Bitmap bmp = getCanvasSnapshot();
					int width = bmp.getWidth();
					int height = bmp.getHeight();
					Bitmap bmp1 = Bitmap.createBitmap(bmp, 0, 60, width, height-60);
					if (BitmapUtil.saveBitmapToSDCard(bmp1, dir+"/"+anchor_name+".png")) {
						Toast.makeText(context, "成功生成打印文件！", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "无法生成打印文件！", Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(context, "未检测到外部sd卡", Toast.LENGTH_SHORT).show();
			}
		}
	} 
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyActivityManager.removeActivity(MgrOneAnchorRecordActivity.this);
		super.onDestroy();
	}
}
