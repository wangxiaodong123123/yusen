package com.ysdata.grouter.activity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.ysdata.grouter.R;
import com.ysdata.grouter.adapter.ManagerCraftParameterAdapter;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.CacheManager;
import com.ysdata.grouter.cloud.util.ConstDef;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.database.SharePrefOperator;
import com.ysdata.grouter.element.ManagerCraftParameter;
import com.ysdata.grouter.element.MileageParameter;
import com.ysdata.grouter.picture.utils.BitmapUtil;
import com.ysdata.grouter.storage.ExtSdCheck;
import com.ysdata.grouter.uart.MyActivityManager;
import com.ysdata.grouter.view.CustomListView;
import com.ysdata.grouter.wireless.client.Format;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerCraftFileActivity extends Activity {
	private static final int RIGHT = 0;  
	private static final int LEFT = 1; 
	Context context;
	private ImageView mImageView;
	
	SharePrefOperator mSharePref;
	private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	int project_id, subproject_id;

	private ArrayList<ManagerCraftParameter> mgr_list;
	private ManagerCraftParameterAdapter mAdapter;
	private CustomListView mListView;
	
	private TextView craft_mileage_name;
	private View craft_mileage_line;
	
	private TextView create_people;
	private TextView create_date;
	private TextView water_ratio_tv;
	
	private TextView mgr_title_tv;
	
	private Button mgr_search_bt;
	private Button craft_print_bt;
	private Button craft_multi_print_bt;
	private Button craft_statistics_bt;
	
	private ProgressDialogHandler mHandler;
	int timeout_sec = 0;
	
	private TextView proj_name_tv;
	private TextView eng_name_tv;
	int mileage_page = 0;
	String mileage_name= "";
	private MileageParameter mileage_parameter;
	private GestureDetector gestureDetector;
	
	String proj_name_string = "";
	String subproj_name_string = "";
	String proj_eng_string = "";
	private ScrollView scrollview;
	private static final int CLICK_PRINT_START_MILEAGE_EVENT = 1;
	private static final int CLICK_PRINT_END_MILEAGE_EVENT = 2;
	private static final int CLICK_CAL_START_MILEAGE_EVENT = 3;
	private static final int CLICK_CAL_END_MILEAGE_EVENT = 4;
	private static final int CLICK_SEARCH_MILEAGE_BT = 5;
	int click_event = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manager_craft_file_phone);
		context = this;
		MyActivityManager.addActivity(ManagerCraftFileActivity.this);
		scrollview = (ScrollView)findViewById(R.id.mgr_scrollview);
		 for (int i = 0; i < scrollview.getChildCount(); i++) {
	           scrollview.getChildAt(i).setBackgroundColor(  
	                    Color.parseColor("#ffffff"));  
	       }
		project_id = CacheManager.getDbProjectId();
		subproject_id = CacheManager.getDbSubProjectId();
    	mSharePref = SharePrefOperator.getSingleSharePrefOperator(this);
    	mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
    	mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
    	mProjectPointBase.closeDb();
    	if (mProjectBase.openDb() && mProjectPointBase.openDb(project_id, subproject_id)) {
    		mHandler = new ProgressDialogHandler();
    		AppUtil.log("project_id:"+project_id+",subproject_id:"+subproject_id);
    		proj_name_string = mProjectBase.getProjectName(project_id);
    		subproj_name_string = mProjectBase.getSubProjectName(subproject_id);
    		proj_eng_string = proj_name_string + "/" + subproj_name_string;
    		/**************************************************************
    		 * 读写器数据接收标志测试
	    	mProjectPointBase.insertWrcardParameter(4, "K12+1.0-4", "", (float)0.0, "", (float)0.0, (float)0.0, "", (float)0.0, 0);
	    	mProjectPointBase.insertWrcardParameter(5, "K12+1.0-5", "", (float)0.0, "", (float)0.0, (float)0.0, "", (float)0.0, 0);
	    	mProjectPointBase.insertWrcardParameter(6, "K12+1.0-6", "", (float)0.0, "", (float)0.0, (float)0.0, "", (float)0.0, 0);
    		 *************************************************************/
    		mileage_page = mSharePref.getMgrMileageNumber(project_id, subproject_id);
    		if (mileage_page == 0) {
    			mileage_page = 1;
    		}
    		mileage_parameter = mProjectPointBase.getMileageParameter(mileage_page);
    		if (mileage_parameter != null) {
    			mileage_name = mileage_parameter.getMileageName();
    		}
    		initView();
    		gestureDetector = new GestureDetector(ManagerCraftFileActivity.this,onGestureListener);  
    	} else {
    		Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
    	}
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(Format.ACTION_SEND_MILEAGE_NAME);
    	context.registerReceiver(MainBdReceiver, filter);
	}
	
	private BroadcastReceiver MainBdReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String mileage_name = intent.getStringExtra("sectionName");
			String mileage1 = mileage_name.split("\\+")[0];
			String mileage2 = mileage_name.split("\\+")[1];
			AppUtil.log("BroadcastReceiver=============mileage_name:"+mileage_name);
			if (mProjectPointBase.openDb(project_id, subproject_id)) {
				if (click_event == CLICK_SEARCH_MILEAGE_BT) {
					AppUtil.log("========CLICK_SEARCH_MILEAGE_BT===========");
					double section_metre = Double.parseDouble(mileage1.substring(1)) * 1000
							+ Double.parseDouble(mileage2);
					int _mileage_page = mProjectPointBase.getMileageOrderNoWhereSectonMetre(section_metre);
					if(_mileage_page > 0 && _mileage_page != mileage_page) {
						mileage_parameter = mProjectPointBase.getMileageParameter(_mileage_page);
						mileage_parameter = mProjectPointBase.getMileageParameter(_mileage_page);
						if (mileage_parameter != null) {
							mileage_page = _mileage_page;
							mSharePref.saveMgrMileageNumber(project_id, subproject_id, mileage_page);
							mileage_name = mileage_parameter.getMileageName();
							craft_mileage_name.setText(mileage_name);
							create_people.setText(mileage_parameter.getCreatePeople());
							water_ratio_tv.setText(mileage_parameter.getWaterRatio());
							create_date.setText(mileage_parameter.getCreateDate());
							String fileName = ExtSdCheck.getExtSDCardPath()+ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/picture_"+mileage_page+".png";
							File file = new File(fileName);
							if (file.exists()) {
								Bitmap bm = BitmapFactory.decodeFile(fileName); 
								mImageView.setImageBitmap(bm); 
							} else {
								mImageView.setImageBitmap(null);
							}
							mgr_list = mProjectPointBase.getMgrCraftParameterPage(mileage_page);
							mAdapter.setList(mgr_list);
							mAdapter.notifyDataSetChanged();
						}
					}
				} else if (click_event == CLICK_PRINT_START_MILEAGE_EVENT) {
					AppUtil.log("========CLICK_PRINT_START_MILEAGE_EVENT===========");
					if (print_start_mileage1 != null && print_start_mileage2 != null) {
						print_start_mileage1.setText(mileage1);
						print_start_mileage2.setText(mileage2);
    				}
				} else if (click_event == CLICK_PRINT_END_MILEAGE_EVENT) {
					AppUtil.log("========CLICK_PRINT_END_MILEAGE_EVENT===========");
					if (print_end_mileage1 != null && print_end_mileage2 != null) {
						print_end_mileage1.setText(mileage1);
						print_end_mileage2.setText(mileage2);
    				}
				} else if (click_event == CLICK_CAL_START_MILEAGE_EVENT) {
					AppUtil.log("========CLICK_CAL_START_MILEAGE_EVENT===========");
					if (cal_start_mileage1 != null && cal_start_mileage2 != null) {
						cal_start_mileage1.setText(mileage1);
						cal_start_mileage2.setText(mileage2);
    				} else {
    				}
				} else if (click_event == CLICK_CAL_END_MILEAGE_EVENT) {
					AppUtil.log("========CLICK_CAL_END_MILEAGE_EVENT===========");
					if (cal_end_mileage1 != null && cal_end_mileage2 != null) {
						cal_end_mileage1.setText(mileage1);
						cal_end_mileage2.setText(mileage2);
    				}
				}
			} else {
				Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
			}
			click_event = 0;
		};
	};
	
	private void initView() {
		engNameInit();
		anchorMileageInit();
		displayPicInit();
		tableListInit();
		searchMileage();
		printMileage();
		printMultiMileage();
		anchorStatistics();
	}
			  
	private GestureDetector.OnGestureListener onGestureListener =   
			new GestureDetector.SimpleOnGestureListener() {  
		@Override  
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
							float velocityY) {  
			float x = e2.getX() - e1.getX();  
			float y = e2.getY() - e1.getY();
			if (y < 100 & y > -100) {
				if (x > 100) {  
					doResult(RIGHT);  
				} else if (x < -100) {  
					doResult(LEFT);  
				} 
			}
            return true;  
		}  
	};  

	public boolean onTouchEvent(MotionEvent event) {  
	        return gestureDetector.onTouchEvent(event);  
	}  
	
	public void doResult(int action) {  
		if (mProjectPointBase.openDb(project_id, subproject_id)) {
			int mileage_page_max = mProjectPointBase.getMileageCount();
			switch (action) {  
			case LEFT:  
				AppUtil.log("go left");  
				if (mileage_page_max >= mileage_page+1) {
					mileage_parameter = mProjectPointBase.getMileageParameter(mileage_page+1);
					if (mileage_parameter != null) {
						mileage_page += 1;
						mSharePref.saveMgrMileageNumber(project_id, subproject_id, mileage_page);
						mileage_name = mileage_parameter.getMileageName();
						craft_mileage_name.setText(mileage_name);
						create_people.setText(mileage_parameter.getCreatePeople());
						water_ratio_tv.setText(mileage_parameter.getWaterRatio());
						create_date.setText(mileage_parameter.getCreateDate());
						String sdcarddir = ExtSdCheck.getExtSDCardPath();
						String fileName = sdcarddir+ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/picture_"+mileage_page+".png";
						File file = new File(fileName);
						if (file.exists()) {
							Bitmap bm = BitmapFactory.decodeFile(fileName); 
							mImageView.setImageBitmap(bm); 
						} else {
							mImageView.setImageBitmap(null);
						}
						mgr_list = mProjectPointBase.getMgrCraftParameterPage(mileage_page);
						mAdapter.setList(mgr_list);
						mAdapter.notifyDataSetChanged();
					}
				}
				break;  
				
			case RIGHT:  
				AppUtil.log("go right");  
				if (mileage_page > 1) {
					mileage_parameter = mProjectPointBase.getMileageParameter(mileage_page-1);
					if (mileage_parameter != null) {
						mileage_page -= 1;
						mSharePref.saveMgrMileageNumber(project_id, subproject_id, mileage_page);
						mileage_name = mileage_parameter.getMileageName();
						craft_mileage_name.setText(mileage_name);
						create_people.setText(mileage_parameter.getCreatePeople());
						create_date.setText(mileage_parameter.getCreateDate());
						water_ratio_tv.setText(mileage_parameter.getWaterRatio());
//			    		String sdcarddir = android.os.Environment.getExternalStorageDirectory().getPath();
						String sdcarddir = ExtSdCheck.getExtSDCardPath();
						String fileName = sdcarddir+ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/picture_"+mileage_page+".png";
						File file = new File(fileName);
						if (file.exists()) {
							Bitmap bm = BitmapFactory.decodeFile(fileName); 
							mImageView.setImageBitmap(bm); 
						} else {
							mImageView.setImageBitmap(null);
						}
						mgr_list = mProjectPointBase.getMgrCraftParameterPage(mileage_page);
						mAdapter.setList(mgr_list);
						mAdapter.notifyDataSetChanged();
					}
				}
				break;  
			}  
		} else {
			Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
		}
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
	
   private void searchMileage() {
		mgr_search_bt = (Button) findViewById(R.id.mgr_search_bt);
		mgr_search_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String sdcarddir = ExtSdCheck.getExtSDCardPath();
				if (sdcarddir == null) {
					Toast.makeText(context, "未检测到外部sd卡", Toast.LENGTH_SHORT).show();
					return;
				}
				click_event = CLICK_SEARCH_MILEAGE_BT;
				Intent intent = new Intent(ManagerCraftFileActivity.this, MileageGridViewActivity.class);
				intent.putExtra("project_name", proj_name_string);
				intent.putExtra("subproject_name", subproj_name_string);
				startActivity(intent);
			}
		});
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
				if (BitmapUtil.saveBitmapToSDCard(bmp, dir+"/"+craft_mileage_name.getText().toString()+".png")) {
					Message msg = mHandler.obtainMessage(PRINT_SUCCESS_MSG_WHAT, mileage_parameter.getMileageName());
					mHandler.sendMessage(msg);
				} else {
					Message msg = mHandler.obtainMessage(PRINT_SUCCESS_MSG_WHAT, mileage_parameter.getMileageName());
					mHandler.sendMessage(msg);
				}
			}
			mHandler.sendEmptyMessage(PROGRESSDIALOG_DISMISS_MSG_WHAT);
		}
	}
	
	private boolean checkMileage1Format(String mileage1_string) {
		if (!mileage1_string.equals("") && (mileage1_string.length() >= 2) && (mileage1_string.startsWith("k") ||
				mileage1_string.startsWith("K"))) {
			String mileage1_val_string = mileage1_string.substring(1);
			System.out.println(mileage1_val_string);
			if (mileage1_val_string.matches("^[0-9]+(\\.[0-9]+)?$")) {
				return true;
			}
		}
		return false;
	}
	
	EditText print_start_mileage1;
	EditText print_start_mileage2;
	EditText print_end_mileage1;
	EditText print_end_mileage2;
	
	private void printMultiMileage() {
		craft_multi_print_bt = (Button) findViewById(R.id.mgr_multi_print_bt);
		craft_multi_print_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(ManagerCraftFileActivity.this);
				final View dialogView = factory.inflate(R.layout.mgr_eng_calculate_dialog, null);
				print_start_mileage1 = (EditText) dialogView.findViewById(R.id.start_mileage_et1);
				print_start_mileage2 = (EditText) dialogView.findViewById(R.id.start_mileage_et2);
				print_end_mileage1 = (EditText) dialogView.findViewById(R.id.end_mileage_et1);
				print_end_mileage2 = (EditText) dialogView.findViewById(R.id.end_mileage_et2);
				final ImageButton search_start_ibt = (ImageButton) dialogView.findViewById(R.id.search_start_mileage_id);
		    	final ImageButton search_end_ibt = (ImageButton) dialogView.findViewById(R.id.search_end_mileage_id);
	        	AlertDialog.Builder dlg = new AlertDialog.Builder(ManagerCraftFileActivity.this);
	        	dlg.setTitle("批量打印");
	        	dlg.setIcon(android.R.drawable.ic_dialog_map);
	        	dlg.setView(dialogView);
	        	search_start_ibt.setOnClickListener(new View.OnClickListener() {
	    			
	    			@Override
	    			public void onClick(View v) {
	    				click_event = CLICK_PRINT_START_MILEAGE_EVENT;
	    				Intent intent = new Intent(ManagerCraftFileActivity.this, MileageGridViewActivity.class);
	    				intent.putExtra("project_name", proj_name_string);
	    				intent.putExtra("subproject_name", subproj_name_string);
	    				startActivity(intent);
	    			}
	    		});
	    		search_end_ibt.setOnClickListener(new View.OnClickListener() {
	    			
	    			@Override
	    			public void onClick(View v) {
	    				click_event = CLICK_PRINT_END_MILEAGE_EVENT;
	    				Intent intent = new Intent(ManagerCraftFileActivity.this, MileageGridViewActivity.class);
	    				intent.putExtra("project_name", proj_name_string);
	    				intent.putExtra("subproject_name", subproj_name_string);
	    				startActivity(intent);
	    			}
	    		});
	        	dlg.setPositiveButton("打印", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String sdcarddir = ExtSdCheck.getExtSDCardPath();
						if (sdcarddir == null) {
							Toast.makeText(context, "未检测到外部sd卡", Toast.LENGTH_SHORT).show();
							return;
						}
						
						boolean start_mileage_format = checkMileage1Format(print_start_mileage1.getText().toString()) &&
								(print_start_mileage2.getText().toString()).matches("^[0-9]+(\\.[0-9]+)?$");
						boolean end_mileage_format = checkMileage1Format(print_end_mileage1.getText().toString()) &&
								(print_end_mileage2.getText().toString()).matches("^[0-9]+(\\.[0-9]+)?$");
						
						if (start_mileage_format && end_mileage_format) {
							if (mProjectPointBase.openDb(project_id, subproject_id)) {
								double start_section_metre = Double.parseDouble(print_start_mileage1.getText().toString().substring(1)) * 1000
										+ Double.parseDouble(print_start_mileage2.getText().toString());
								double end_section_metre = Double.parseDouble(print_end_mileage1.getText().toString().substring(1)) * 1000
										+ Double.parseDouble(print_end_mileage2.getText().toString());
								int start_orderno = mProjectPointBase.getMileageOrderNoWhereSectonMetre(start_section_metre);
								int end_orderno = mProjectPointBase.getMileageOrderNoWhereSectonMetre(end_section_metre);
								if (start_orderno == 0) {
									Toast.makeText(ManagerCraftFileActivity.this, "数据库中无该起始注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (end_orderno == 0) {
									Toast.makeText(ManagerCraftFileActivity.this, "数据库中无该结束注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (start_orderno > end_orderno) {
									Toast.makeText(ManagerCraftFileActivity.this, "起始注浆工段不能滞后于结束注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (start_orderno <= end_orderno) {
									if (end_orderno - start_orderno <= 49) {
										String dir = sdcarddir+"/"+ConstDef.PROJECT_NAME+"/print/" + proj_eng_string + "/数据管理记录表/";
										File path = new File(dir);
										if (!path.exists()) {
											path.mkdirs();
										}
										new MultiPrintThread(dir, start_orderno, end_orderno).start();
									} else {
										String end_mileage_string = mProjectPointBase.getMileageParameter(start_orderno+49).getMileageName();
										Toast.makeText(context, "该操作一次最多只允许打印50个注浆工段的参数,最大注浆工段为:"+
												end_mileage_string, Toast.LENGTH_SHORT).show();
									}
								}
							} else {
								Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
							}
						} else {
							if (!start_mileage_format) {
								Toast.makeText(context, "起始注浆工段格式输入错误，参考格式：k12+1.5", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(context, "结束注浆工段格式输入错误，参考格式：k12+1.5", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
	        	dlg.setNegativeButton("取消", new DialogInterface.OnClickListener() {

	    			@Override
	    			public void onClick(DialogInterface dialog, int which) {
	    				try {
	    					Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
	    					field.setAccessible(true);
	    					field.set(dialog, true);// 将mShowing变量设为false，表示对话框已关闭
	    					dialog.dismiss();
	    				} catch (Exception e) {
	    					e.printStackTrace();
	    				}
	    			}
	        	});
	        	dlg.create();
//	        	dlg.setCancelable(false);
	        	dlg.show();
			}
		});
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
            	progressDialog = ProgressDialog.show(ManagerCraftFileActivity.this, 
						"数据管理界面打印", "正在生成打印文件,请等待...", true, false); 
            	break;
            case PROGRESSDIALOG_DISMISS_MSG_WHAT:
            	progressDialog.dismiss();
            	break;
            case PRINT_SUCCESS_MSG_WHAT:
            	String pile_number_string = (String) msg.obj;
				Toast.makeText(context, "成功生成注浆工段:"+pile_number_string+"的打印文件！", 
						Toast.LENGTH_SHORT).show();
            	break;
            case UPDATE_NEXT_PAGE:
            	int mileage_page_tmp = (Integer) msg.obj;
				mileage_parameter = mProjectPointBase.getMileageParameter(mileage_page_tmp);
				if (mileage_parameter != null) {
					mileage_page = mileage_page_tmp;
					mSharePref.saveMgrMileageNumber(project_id, subproject_id, mileage_page);
					mileage_name = mileage_parameter.getMileageName();
					craft_mileage_name.setText(mileage_name);
					create_people.setText(mileage_parameter.getCreatePeople());
					water_ratio_tv.setText(mileage_parameter.getWaterRatio());
					create_date.setText(mileage_parameter.getCreateDate());
					String sdcarddir = ExtSdCheck.getExtSDCardPath();
					String fileName = sdcarddir+ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/picture_"+mileage_page+".png";
					File file = new File(fileName);
					if (file.exists()) {
						Bitmap bm = BitmapFactory.decodeFile(fileName); 
						mImageView.setImageBitmap(bm); 
					} else {
						mImageView.setImageBitmap(null);
					}
					mgr_list = mProjectPointBase.getMgrCraftParameterPage(mileage_page_tmp);
					mAdapter.setList(mgr_list);
					mAdapter.notifyDataSetChanged();
				}
            	break;
            case PRINT_FAIL_MSG_WHAT:
            	String pile_number_string1 = (String) msg.obj;
            	Toast.makeText(context, "无法生成注浆工段:"+pile_number_string1+"的打印文件！", 
						Toast.LENGTH_SHORT).show();
            	break;
            }
        }
	}
	
	class MultiPrintThread extends Thread {
		String dir = "";
		int start_pile_page = 0;
		int end_pile_page = 0;
		public MultiPrintThread(String dir, int start_pile_page, int end_pile_page) {
			this.dir = dir;
			this.start_pile_page = start_pile_page;
			this.end_pile_page = end_pile_page;
		}
		@Override
		public void run() {
			mHandler.sendEmptyMessage(PROGRESSDIALOG_SHOW_MSG_WHAT);
			int pile_page_tmp = start_pile_page;
			Bitmap bmp = null;
			mileage_parameter = mProjectPointBase.getMileageParameter(start_pile_page);
			AppUtil.log("start_pile_page:"+start_pile_page + "  end_pile_page:"+end_pile_page);
			while (pile_page_tmp <= end_pile_page) 
			{
				bmp = getBitmapByView(scrollview);
				if (BitmapUtil.saveBitmapToSDCard(bmp, dir+mileage_parameter.getMileageName()
						+".png")) {
					Message msg = mHandler.obtainMessage(PRINT_SUCCESS_MSG_WHAT, mileage_parameter.getMileageName());
					mHandler.sendMessage(msg);
				} else {
					Message msg = mHandler.obtainMessage(PRINT_FAIL_MSG_WHAT, mileage_parameter.getMileageName());
					mHandler.sendMessage(msg);
					break;
				}
				pile_page_tmp++;
				if (pile_page_tmp <= end_pile_page) {
					Message msg = mHandler.obtainMessage(UPDATE_NEXT_PAGE, pile_page_tmp);
					mHandler.sendMessage(msg);
					try {
						Thread.sleep(2000); //实现界面绘制与scrollview截取的同步
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				Thread.sleep(2000); //最后一个toast显示时，对话框不立刻消失
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mHandler.sendEmptyMessage(PROGRESSDIALOG_DISMISS_MSG_WHAT);
		}
	}
	
	EditText cal_start_mileage1;
	EditText cal_start_mileage2;
	EditText cal_end_mileage1;
	EditText cal_end_mileage2;
	
	private void anchorStatistics() {
		craft_statistics_bt = (Button) findViewById(R.id.mgr_anchor_statistics_bt);
		craft_statistics_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(ManagerCraftFileActivity.this);
				final View dialogView = factory.inflate(R.layout.mgr_eng_calculate_dialog, null);
	        	AlertDialog.Builder dlg = new AlertDialog.Builder(ManagerCraftFileActivity.this);
	        	cal_start_mileage1 = (EditText) dialogView.findViewById(R.id.start_mileage_et1);
				cal_start_mileage2 = (EditText) dialogView.findViewById(R.id.start_mileage_et2);
				cal_end_mileage1 = (EditText) dialogView.findViewById(R.id.end_mileage_et1);
				cal_end_mileage2 = (EditText) dialogView.findViewById(R.id.end_mileage_et2);
	        	final ImageButton search_start_ibt = (ImageButton) dialogView.findViewById(R.id.search_start_mileage_id);
		    	final ImageButton search_end_ibt = (ImageButton) dialogView.findViewById(R.id.search_end_mileage_id);
	        	dlg.setTitle("工程数据统计");
	        	dlg.setIcon(android.R.drawable.ic_dialog_map);
	        	dlg.setView(dialogView);
	        	search_start_ibt.setOnClickListener(new View.OnClickListener() {
	    			
	    			@Override
	    			public void onClick(View v) {
	    				click_event = CLICK_CAL_START_MILEAGE_EVENT;
	    				Intent intent = new Intent(ManagerCraftFileActivity.this, MileageGridViewActivity.class);
	    				intent.putExtra("project_name", proj_name_string);
	    				intent.putExtra("subproject_name", subproj_name_string);
	    				startActivity(intent);
	    			}
	    		});
	    		search_end_ibt.setOnClickListener(new View.OnClickListener() {
	    			
	    			@Override
	    			public void onClick(View v) {
	    				click_event = CLICK_CAL_END_MILEAGE_EVENT;
	    				Intent intent = new Intent(ManagerCraftFileActivity.this, MileageGridViewActivity.class);
	    				intent.putExtra("project_name", proj_name_string);
	    				intent.putExtra("subproject_name", subproj_name_string);
	    				startActivity(intent);
	    			}
	    		});
	        	dlg.setPositiveButton("计算", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, false);// 将mShowing变量设为false，表示对话框已关闭
							dialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						boolean start_mileage_format = checkMileage1Format(cal_start_mileage1.getText().toString()) &&
								(cal_start_mileage2.getText().toString()).matches("^[0-9]+(\\.[0-9]+)?$");
						boolean end_mileage_format = checkMileage1Format(cal_end_mileage1.getText().toString()) &&
								(cal_end_mileage2.getText().toString()).matches("^[0-9]+(\\.[0-9]+)?$");
						
						if (start_mileage_format && end_mileage_format) {
							if (mProjectPointBase.openDb(project_id, subproject_id)) {
								double start_section_metre = Double.parseDouble(cal_start_mileage1.getText().toString().substring(1)) * 1000
										+ Double.parseDouble(cal_start_mileage2.getText().toString());
								double end_section_metre = Double.parseDouble(cal_end_mileage1.getText().toString().substring(1)) * 1000
										+ Double.parseDouble(cal_end_mileage2.getText().toString());
								int start_orderno = mProjectPointBase.getAnchorStartOrderNoWhereSectionMetre(start_section_metre);
								int end_orderno = mProjectPointBase.getAnchorEndOrderNoWhereSectonMetre(end_section_metre);
								if (start_orderno == 0) {
									Toast.makeText(ManagerCraftFileActivity.this, "数据库中无该起始注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (end_orderno == 0) {
									Toast.makeText(ManagerCraftFileActivity.this, "数据库中无该结束注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (start_orderno > end_orderno) {
									Toast.makeText(ManagerCraftFileActivity.this, "起始注浆工段不能滞后于结束注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (start_orderno < end_orderno) {
									try {
										Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
										field.setAccessible(true);
										field.set(dialog, true);// 将mShowing变量设为false，表示对话框已关闭
										dialog.dismiss();
									} catch (Exception e) {
										e.printStackTrace();
									}
									String start_mileage_str = cal_start_mileage1.getText().toString()+"+"+
									Double.parseDouble(cal_start_mileage2.getText().toString());
									String end_mileage_str = cal_end_mileage1.getText().toString()+"+"+
									Double.parseDouble(cal_end_mileage2.getText().toString());
									Intent intent = new Intent(context, MgrAnchorStatisticsActivity.class);
									intent.putExtra("start_orderno", start_orderno);
									intent.putExtra("start_mileage", start_mileage_str);
									intent.putExtra("end_orderno", end_orderno);
									intent.putExtra("end_mileage", end_mileage_str);
									startActivity(intent);
								}
							} else {
								Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
							}
						} else {
							if (!start_mileage_format) {
								Toast.makeText(context, "起始注浆工段格式输入错误，参考格式：k12+1.5", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(context, "结束注浆工段格式输入错误，参考格式：k12+1.5", Toast.LENGTH_SHORT).show();
							}
						}
					}
				});
	        	dlg.setNegativeButton("取消", new DialogInterface.OnClickListener() {

	    			@Override
	    			public void onClick(DialogInterface dialog, int which) {
	    				try {
	    					Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
	    					field.setAccessible(true);
	    					field.set(dialog, true);// 将mShowing变量设为false，表示对话框已关闭
	    					dialog.dismiss();
	    				} catch (Exception e) {
	    					e.printStackTrace();
	    				}
	    			}
	        	});
	        	dlg.create();
	        	dlg.show();
			}
		});
	}
	
	private void anchorMileageInit() {
		craft_mileage_name = (TextView) findViewById(R.id.mgr_craft_mileage_name);
		craft_mileage_line = findViewById(R.id.mgr_craft_mileage_line);
		craft_mileage_line.setVisibility(View.VISIBLE);
		create_people = (TextView) findViewById(R.id.mgr_craft_people_name);
		water_ratio_tv = (TextView) findViewById(R.id.mgr_water_ratio_name);
		create_date = (TextView) findViewById(R.id.mgr_craft_date_name);
		if (mileage_parameter != null) {
			craft_mileage_name.setText(mileage_name);
			create_people.setText(mileage_parameter.getCreatePeople());
			create_date.setText(mileage_parameter.getCreateDate());
			water_ratio_tv.setText(mileage_parameter.getWaterRatio());
		}
	}
	
	
	private void displayPicInit() {
		mImageView = (ImageView) findViewById(R.id.mgr_craft_imageview);
//		String sdcarddir = android.os.Environment.getExternalStorageDirectory().getPath();
		String sdcarddir = ExtSdCheck.getExtSDCardPath();
		if (sdcarddir != null) {
			String fileName = sdcarddir+ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/picture_"+mileage_page+".png";
			File file = new File(fileName);
			if (file.exists()) {
				Bitmap bm = BitmapFactory.decodeFile(fileName); 
				mImageView.setImageBitmap(bm); 
			}
		}
	}
	
	private void engNameInit() {
		eng_name_tv = (TextView) findViewById(R.id.mgr_craft_engin_name);
		proj_name_tv = (TextView) findViewById(R.id.mgr_project_name);
		mgr_title_tv = (TextView) findViewById(R.id.mgr_title_id);
		mgr_title_tv.setText("数据管理->注浆管理记录表");
		eng_name_tv.setText(subproj_name_string);
		proj_name_tv.setText(proj_name_string);
	}
	
	private void tableListInit() {
		mgr_list = new ArrayList<ManagerCraftParameter>();
		mgr_list = mProjectPointBase.getMgrCraftParameterPage(mileage_page);
		mListView = (CustomListView)findViewById(R.id.mgr_craft_table_lv);
		mAdapter = new ManagerCraftParameterAdapter(mgr_list, this);
		mListView.setAdapter(mAdapter);
		/*mListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mAdapter.isCellEditable())
					return true;
				return false;
			}
		});*/
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) { 
		gestureDetector.onTouchEvent(ev); //让GestureDetector响应触碰事件
		super.dispatchTouchEvent(ev); //让Activity响应触碰事件

		return false;  
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyActivityManager.removeActivity(ManagerCraftFileActivity.this);
		context.unregisterReceiver(MainBdReceiver);
		super.onDestroy();
	}
}
