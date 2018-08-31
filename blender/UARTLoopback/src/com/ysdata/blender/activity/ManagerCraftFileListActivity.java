package com.ysdata.blender.activity;

import java.io.File;
import java.util.ArrayList;

import com.ysdata.blender.R;
import com.ysdata.blender.adapter.ManagerCraftParameterAdapter;
import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.cloud.util.ConstDef;
import com.ysdata.blender.database.ProjectDataBaseAdapter;
import com.ysdata.blender.database.ProjectPointDataBaseAdapter;
import com.ysdata.blender.element.ManagerCraftParameter;
import com.ysdata.blender.picture.utils.BitmapUtil;
import com.ysdata.blender.storage.ExtSdCheck;
import com.ysdata.blender.uart.MyActivityManager;
import com.ysdata.blender.view.CustomListView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
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
	int mix_id = 0;

	private TextView mgr_title_tv;
	
	private TextView proj_name_tv;
	private TextView eng_name_tv;
	
	String proj_name_string = "";
	String subproj_name_string = "";
	String proj_eng_string = "";
	
	private TextView sum_blender_time_tv;
	private TextView sum_cement_weigh_tv;
	private TextView sum_water_weigh_tv;
	private TextView sum_weigh_tv;
	private ScrollView scrollview;
	
	private Button craft_print_bt;
	private ProgressDialogHandler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_craft_file_list);
		context = this;
		MyActivityManager.addActivity(ManagerCraftFileListActivity.this);
		scrollview = (ScrollView)findViewById(R.id.mgr_scrollview);
		 for (int i = 0; i < scrollview.getChildCount(); i++) {
	           scrollview.getChildAt(i).setBackgroundColor(  
	                    Color.parseColor("#ffffff"));  
	       }
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
    		mHandler = new ProgressDialogHandler();
    		initView();
    	} else {
    		Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
    	}
	}
	
	private void initView() {
		engNameInit();
		sum_blender_time_tv = (TextView)findViewById(R.id.mgr_total_blender_time);
		sum_cement_weigh_tv = (TextView)findViewById(R.id.mgr_total_cement_weigh);
		sum_water_weigh_tv = (TextView)findViewById(R.id.mgr_total_water_weigh);
		sum_weigh_tv = (TextView)findViewById(R.id.mgr_total_all_weigh);
		tableListInit();
		printMileage();
	}
	
	private void printMileage() {
		craft_print_bt = (Button) findViewById(R.id.mgr_print_bt);
		craft_print_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String sdcarddir = ExtSdCheck.getExtSDCardPath();
				if (sdcarddir == null) {
					Toast.makeText(context, "未检测到外部sd卡", Toast.LENGTH_SHORT).show();
					return;
				}
				String dir = sdcarddir+"/"+ConstDef.PROJECT_NAME+"/print/" + proj_eng_string + "/数据管理记录表/";
				File path = new File(dir);
				if (!path.exists()) {
					path.mkdirs();
				}
				new PrintSingleLogThread(dir).start();
			}
		});
	}
	
	class PrintSingleLogThread extends Thread {
		String dir = "";
		public PrintSingleLogThread(String dir) {
			this.dir = dir;
		}
		@Override
		public void run() {
			mHandler.sendEmptyMessage(PROGRESSDIALOG_SHOW_MSG_WHAT);
			Bitmap bmp = getBitmapByView(scrollview);
			if (bmp != null)
			{
				if (BitmapUtil.saveBitmapToSDCard(bmp, dir+"/"+"现场水泥(砂)浆搅拌记录表.png")) {
					mHandler.sendEmptyMessage(PRINT_SUCCESS_MSG_WHAT);
				} else {
					mHandler.sendEmptyMessage(PRINT_FAIL_MSG_WHAT);
				}
			}
			mHandler.sendEmptyMessage(PROGRESSDIALOG_DISMISS_MSG_WHAT);
		}
	}
	
	private static final int PROGRESSDIALOG_SHOW_MSG_WHAT = 1;
	private static final int PROGRESSDIALOG_DISMISS_MSG_WHAT = 2;
	private static final int PRINT_FAIL_MSG_WHAT = 3;
	private static final int PRINT_SUCCESS_MSG_WHAT = 4;
	private static final int UPDATE_NEXT_PAGE = 5;
	
	class ProgressDialogHandler extends Handler {
		ProgressDialog progressDialog;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case PROGRESSDIALOG_SHOW_MSG_WHAT:
            	progressDialog = ProgressDialog.show(ManagerCraftFileListActivity.this, 
						"数据管理界面打印", "正在生成打印文件,请等待...", true, false); 
            	break;
            case PROGRESSDIALOG_DISMISS_MSG_WHAT:
            	progressDialog.dismiss();
            	break;
            case PRINT_SUCCESS_MSG_WHAT:
				Toast.makeText(context, "成功生成记录表打印文件！", 
						Toast.LENGTH_SHORT).show();
            	break;
            case PRINT_FAIL_MSG_WHAT:
            	Toast.makeText(context, "无法生成记录表打印文件！", 
						Toast.LENGTH_SHORT).show();
            	break;
            }
        }
	}
	
	private void tableListInit() {
		mgr_list = new ArrayList<ManagerCraftParameter>();
		mProjectPointBase.getManagerCraftParameterList(mgr_list);
		mListView = (CustomListView)findViewById(R.id.mgr_craft_table_lv);
		mAdapter = new ManagerCraftParameterAdapter(mgr_list, this);
		mListView.setAdapter(mAdapter);
		
		double sum_time = 0, sum_water_weigh = 0, sum_cement_weigh = 0, sum_weigh = 0;
		for (int i = 0; i < mgr_list.size(); i++) {
			sum_time += mgr_list.get(i).getBlenderTime();
			sum_water_weigh += mgr_list.get(i).getWaterWeigh();
			sum_cement_weigh += mgr_list.get(i).getCementWeight();
			sum_weigh += mgr_list.get(i).getTotalWeigh();
		}
		sum_blender_time_tv.setText(sum_time+"");
		sum_water_weigh_tv.setText(sum_water_weigh+"");
		sum_cement_weigh_tv.setText(sum_cement_weigh+"");
		sum_weigh_tv.setText(sum_weigh+"");
	}
	
	/**
	    * 截取scrollview的屏幕
	    * **/
	   public Bitmap getBitmapByView(ScrollView scrollView) {
	       int h = 0;
	       Bitmap bitmap = null;
	       // 获取listView实际高度
	       for (int i = 0; i < scrollView.getChildCount(); i++) {
	           h += scrollView.getChildAt(i).getHeight();
//	           scrollView.getChildAt(i).setBackgroundColor(  
//	                    Color.parseColor("#ffffff"));  
	       }
//	       AppUtil.log("实际高度:" + h);
//	       AppUtil.log(" 高度:" + scrollView.getHeight());
	       // 创建对应大小的bitmap
	       bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
	               Bitmap.Config.ARGB_8888);
	       final Canvas canvas = new Canvas(bitmap);
	       scrollView.draw(canvas);
	       // 测试输出
	       /*FileOutputStream out = null;
	       try {
	           out = new FileOutputStream(dir+"/pile_table.png");
	       } catch (FileNotFoundException e) {
	           e.printStackTrace();
	       }
	       try {
	           if (null != out) {
	               bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
	               out.flush();
	               out.close();
	           }
	       } catch (IOException e) {
	           // TODO: handle exception
	       }*/
	       return bitmap;
	   }
	
	private void engNameInit() {
		eng_name_tv = (TextView) findViewById(R.id.mgr_craft_engin_name);
		proj_name_tv = (TextView) findViewById(R.id.mgr_project_name);
		mgr_title_tv = (TextView) findViewById(R.id.mgr_title_id);
		mgr_title_tv.setText("数据管理->搅拌管理记录表");
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
