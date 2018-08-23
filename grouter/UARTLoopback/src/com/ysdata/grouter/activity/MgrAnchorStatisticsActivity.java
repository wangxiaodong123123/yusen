package com.ysdata.grouter.activity;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.ysdata.grouter.R;
import com.ysdata.grouter.adapter.MgrAnchorStasticParameterAdapter;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.CacheManager;
import com.ysdata.grouter.cloud.util.ConstDef;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.element.MgrAnchorStasticParameter;
import com.ysdata.grouter.manager.TempData;
import com.ysdata.grouter.picture.utils.BitmapUtil;
import com.ysdata.grouter.storage.ExtSdCheck;
import com.ysdata.grouter.uart.MyActivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MgrAnchorStatisticsActivity extends Activity {
	
	TextView static_title_tv;
	TextView eng_name;
	TextView proj_name_tv;
	TextView start_mileage_val;
	TextView end_mileage_val;
	
	TextView anchor_type_tv;
	TextView anchor_model_tv;
	TextView design_anchor_amount;
	TextView design_anchor_length;
	TextView title_remark;
	TextView sum_remark;
	EditText sum_remark_et;
	Button print_bt;
	Button sum_confirm_bt;
	Button sum_cancel_bt;
	
	TextView sum_design_anchor_amount;
	TextView sum_design_anchor_length;
	
	String proj_name_string = "";
	String eng_name_string = "";
	String start_mileage = "";
	String end_mileage = "";
	
	Context context;
	ProjectDataBaseAdapter mProjNameBase;
	int project_id, subproject_id;
	private ProjectPointDataBaseAdapter mDataBase;
	
	private ArrayList<MgrAnchorStasticParameter> mList;
	ArrayList<MgrAnchorStasticParameter> print_list;
	private MgrAnchorStasticParameterAdapter mAdapter;
	private ListView mListView;
	
	boolean isSumRemarkEditable = false;
	View activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mgr_anchor_statistics_phone);
		activity = getWindow().getDecorView();
		context = this;
		MyActivityManager.addActivity(MgrAnchorStatisticsActivity.this);
    	mProjNameBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(this);
    	mDataBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(this);
    	print_list = new ArrayList<MgrAnchorStasticParameter>();
    	project_id = CacheManager.getDbProjectId();
    	subproject_id = CacheManager.getDbSubProjectId();
    	AppUtil.log( "project_id:"+project_id+",subproject_id:"+subproject_id);
		initView();
		if (mDataBase.openDb(project_id, subproject_id) && mProjNameBase.openDb()) {
			proj_name_string = mProjNameBase.getProjectName(project_id);
			eng_name_string = mProjNameBase.getSubProjectName(subproject_id);
			setView(context);
		} else {
			Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void initView() {
		proj_name_tv = (TextView) findViewById(R.id.stas_project_name);
		eng_name = (TextView) findViewById(R.id.stas_eng_name);
		start_mileage_val = (TextView) findViewById(R.id.stas_start_mileage_value);
		end_mileage_val = (TextView) findViewById(R.id.stas_end_mileage_value);
		
		sum_design_anchor_amount = (TextView) findViewById(R.id.sum_design_anchor_amount);
		sum_design_anchor_length = (TextView) findViewById(R.id.sum_design_anchor_length);
		title_remark = (TextView) findViewById(R.id.title_remark);
		static_title_tv = (TextView) findViewById(R.id.mgr_static_title_id);
		sum_remark = (TextView) findViewById(R.id.sum_remark);
		sum_remark_et = (EditText) findViewById(R.id.sum_remark_et);
		sum_confirm_bt = (Button) findViewById(R.id.sum_confirm_bt);
		sum_cancel_bt = (Button) findViewById(R.id.sum_cancel_bt);
		sum_remark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sum_remark.setVisibility(View.INVISIBLE);
				sum_remark_et.setVisibility(View.VISIBLE);
				sum_remark_et.setText(sum_remark.getText().toString());
				sum_confirm_bt.setVisibility(View.VISIBLE);
				sum_cancel_bt.setVisibility(View.VISIBLE);
				isSumRemarkEditable = true;
			}
		});
		sum_confirm_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String sum_remark_et_string = sum_remark_et.getText().toString();
				if (!sum_remark_et_string.equals("")) {
					sum_remark.setText(sum_remark_et_string);
				} else {
					Toast.makeText(MgrAnchorStatisticsActivity.this, "输入不能为空",
							Toast.LENGTH_SHORT).show();
				}
				sum_remark_et.setVisibility(View.INVISIBLE);
				sum_confirm_bt.setVisibility(View.INVISIBLE);
				sum_cancel_bt.setVisibility(View.INVISIBLE);
				sum_remark.setVisibility(View.VISIBLE);
				isSumRemarkEditable = false;
			}
		});
		sum_cancel_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sum_remark_et.setVisibility(View.INVISIBLE);
				sum_confirm_bt.setVisibility(View.INVISIBLE);
				sum_cancel_bt.setVisibility(View.INVISIBLE);
				sum_remark.setVisibility(View.VISIBLE);
				isSumRemarkEditable = false;
			}
		});
		title_remark.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mAdapter.isCellEditable()) { //单元格处于编辑状态
					return;
				}
				LayoutInflater factory = LayoutInflater.from(MgrAnchorStatisticsActivity.this);
				final View dialogView = factory.inflate(R.layout.modify_title_colume_dialog, null);
				AlertDialog.Builder dlg = new AlertDialog.Builder(MgrAnchorStatisticsActivity.this);
				dlg.setTitle("修改备注");
				dlg.setView(dialogView);
				
				dlg.setPositiveButton("修改", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText title_et = (EditText) dialogView.findViewById(R.id.title_et);
						String title_et_string = title_et.getText().toString();
						if (!title_et_string.equals("")) {
							int size = mList.size();
							for (int i = 0; i < size; i++) {
								mList.get(i).setRemark(title_et_string);
							}
							mAdapter.notifyDataSetChanged();
							sum_remark.setText(title_et_string);
						} else {
							Toast.makeText(MgrAnchorStatisticsActivity.this, "输入不能为空",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
				dlg.setNegativeButton("取消", null);
				dlg.create();
				dlg.show();
			}
		});
		print_bt = (Button) findViewById(R.id.stas_print_bt);
		print_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String sdcarddir = ExtSdCheck.getExtSDCardPath();
				if (sdcarddir != null) {
					boolean bmp_create = true;
					String dir = sdcarddir+"/"+ConstDef.PROJECT_NAME+"/print/"+proj_name_string+"/"+ 
							eng_name_string+"/数据统计表";
					File path = new File(dir);
					if(!path.exists()) {
						path.mkdirs();
					}
					String print_file = dir + "/" + start_mileage_val.getText().toString()+
							"~" + end_mileage_val.getText().toString()+".png";
					File file = new File(print_file);
					if(!file.exists()){
						try {
							file.createNewFile();
							bmp_create = true;
						} catch (IOException e) {
							AppUtil.log( "creat or open "+start_mileage_val.getText().toString()+
									"~" + end_mileage_val.getText().toString()+".png"+" failed.exception:" + e);
							bmp_create = false;
							Toast.makeText(context, "无法生成打印文件！", Toast.LENGTH_SHORT).show();
						}
					}
					if (bmp_create) {
						Bitmap bmp = getCanvasSnapshot();
						int width = bmp.getWidth();
						int height = bmp.getHeight();
						Bitmap bmp1 = Bitmap.createBitmap(bmp, 0, 85, width, height-85);
						if (BitmapUtil.saveBitmapToSDCard(bmp1, print_file)) {
							Toast.makeText(context, "成功生成打印文件！", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, "无法生成打印文件！", Toast.LENGTH_SHORT).show();
						}
					}
				} else {
					Toast.makeText(context, "未检测到外部sd卡", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public Bitmap getCanvasSnapshot()
    {
		activity.setDrawingCacheEnabled(true);
		activity.buildDrawingCache(true);
        return activity.getDrawingCache(true);
    }
	
	private void setView(Context context) {
		static_title_tv.setText("数据管理->注浆管理记录表->置筋数量统计");
		proj_name_tv.setText(proj_name_string);
		eng_name.setText(eng_name_string);
		Intent intent = getIntent();
		start_mileage= intent.getStringExtra("start_mileage");
		start_mileage_val.setText(start_mileage);
		end_mileage= intent.getStringExtra("end_mileage");
		end_mileage_val.setText(end_mileage);
		int start_orderno= intent.getIntExtra("start_orderno", 0);
		int end_orderno= intent.getIntExtra("end_orderno", 0);
		mList = mDataBase.getMgrAnchorStaticParameterList(start_orderno, end_orderno);
		mListView = (ListView)findViewById(R.id.anchor_stastic_table_lv);
		mAdapter = new MgrAnchorStasticParameterAdapter(mList,this);
		mListView.setAdapter(mAdapter);
		AppUtil.log( "start_orderno:"+start_orderno+" end_orderno:"+end_orderno+" size:"+mList.size());
		int mSize = mList.size();
		int sum_design_anchor_amount_val = 0;
		float sum_design_anchor_length_val = 0;
		DecimalFormat df = new DecimalFormat("##.##");
		for (int i = 0; i < mSize; i++) {
			mList.get(i).setDesignLength(Float.parseFloat(df.format(mList.get(i).getDesignLength())));
			print_list.add(mList.get(i));
		}
		for (int i = 0; i < mSize; i++) {
			sum_design_anchor_amount_val += mList.get(i).getDesignSum();
			sum_design_anchor_length_val += mList.get(i).getDesignLength();
		}
		sum_design_anchor_length_val = Float.parseFloat(df.format(sum_design_anchor_length_val));
		sum_design_anchor_amount.setText(sum_design_anchor_amount_val+"");
		sum_design_anchor_length.setText(sum_design_anchor_length_val+"");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyActivityManager.removeActivity(MgrAnchorStatisticsActivity.this);
		super.onDestroy();
	}
}
