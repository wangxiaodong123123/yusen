package com.ysdata.grouter.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.ysdata.grouter.R;
import com.ysdata.grouter.cloud.api.BooleanResponse;
import com.ysdata.grouter.cloud.api.PointGroutingDataUploadListResponse;
import com.ysdata.grouter.cloud.util.ApiClient;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.CacheManager;
import com.ysdata.grouter.cloud.util.ConstDef;
import com.ysdata.grouter.cloud.util.SharedView;
import com.ysdata.grouter.cloud.util.UIUtilities;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.element.UploadParameter;
import com.ysdata.grouter.uart.MyActivityManager;
import com.ysdata.grouter.wireless.client.Format;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UploadGroutingPointActivity extends Activity{
	
	private Context context;
	private TextView text_status;
	private TextView proj_name_tv;
	private TextView eng_name_tv;
	TextView user_info_tv;
	int project_id;
	int subproject_id;
	String anchor_seq;
	private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	String eng_name;
	String proj_name;
	String mutex_lock;
	boolean uploadContinue = false;
	boolean choiceItemsStatus = false;
	boolean dialogClick = false;
	private static final int CLICK_START_MILEAGE_EVENT = 1;
	private static final int CLICK_END_MILEAGE_EVENT = 2;
	int click_event = 0;
	String upload_end_mileage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_grouting_point_phone);
		context = this;
		MyActivityManager.addActivity(UploadGroutingPointActivity.this);
		user_info_tv = (TextView) findViewById(R.id.user_info);
		user_info_tv.setText(CacheManager.getUserName() + " 欢迎你!");
		text_status = (TextView) findViewById(R.id.text_status);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		proj_name_tv = (TextView) findViewById(R.id.proj_name_id);
		eng_name_tv = (TextView) findViewById(R.id.eng_name_id);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
		mutex_lock = new String("lock");
		if (mProjectBase.openDb()) {
			ParameterInputDialog();
		} else {
			Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
		}
		IntentFilter filter = new IntentFilter();
        filter.addAction(Format.ACTION_SEND_MILEAGE_NAME);
        context.registerReceiver(BdReceiver, filter);
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
	
	private Spinner dlg_eng_spinner;
	private Spinner dlg_grout_priority_spinner;
	private ArrayAdapter<String> eng_adapter;
	EditText start_mileage1;
	EditText start_mileage2;
	EditText end_mileage1;
	EditText end_mileage2;
	int project_position = -1;
	int subproject_position = -1;
	String record_project_name;
	String record_subproject_name;
	private static final int UPLOAD_MODE_ALL = 1;
	private static final int UPLOAD_MODE_UP = 2;
	private static final int UPLOAD_MODE_DOWN = 3;
	private void ParameterInputDialog() {
		LayoutInflater factory = LayoutInflater.from(UploadGroutingPointActivity.this);
		final View dialogView = factory.inflate(R.layout.panel_mileage_param_dialog_phone, null);
		AlertDialog.Builder dlg = new AlertDialog.Builder(UploadGroutingPointActivity.this);
    	dlg.setTitle("输入要接收的置筋参数");
    	dlg.setView(dialogView);
    	final Spinner dlg_proj_spinner = (Spinner) dialogView.findViewById(R.id.proj_name_dlg_id);
    	final ImageButton search_start_ibt = (ImageButton) dialogView.findViewById(R.id.search_start_mileage_id);
    	final ImageButton search_end_ibt = (ImageButton) dialogView.findViewById(R.id.search_end_mileage_id);
    	TextView record_tv = (TextView) dialogView.findViewById(R.id.record_tv_id);
    	final TextView collect_end_mileage_tv = (TextView) dialogView.findViewById(R.id.end_collect_mileage_id);
    	record_tv.setText("上次上传结束工段:");
    	start_mileage1 = (EditText) dialogView.findViewById(R.id.start_mileage_et1);
		start_mileage2 = (EditText) dialogView.findViewById(R.id.start_mileage_et2);
		end_mileage1 = (EditText) dialogView.findViewById(R.id.end_mileage_et1);
		end_mileage2 = (EditText) dialogView.findViewById(R.id.end_mileage_et2);
		dlg_eng_spinner = (Spinner) dialogView.findViewById(R.id.eng_name_dlg_id);
		dlg_grout_priority_spinner = (Spinner) dialogView.findViewById(R.id.grout_priority_dlg_id);
		final ArrayAdapter<String> proj_adapter;
		final ArrayAdapter<String> grout_priority_adapter;
		ArrayList<String> prj_list = new ArrayList<String>();
		final ArrayList<String> eng_list = new ArrayList<String>();
		mProjectBase.getProjectNameList(prj_list);
		int record_project_id = mProjectBase.getUploadProjectId();
		int record_subproject_id = mProjectBase.getUploadSubProjectId();
		record_project_name = mProjectBase.getProjectName(record_project_id);
		record_subproject_name = mProjectBase.getSubProjectName(record_subproject_id);
		int proj_list_size = prj_list.size();
		String[] proj_areas = new String[proj_list_size];
		for (int i = 0; i < proj_list_size; i++) {
			proj_areas[i] = prj_list.get(i);
			if (project_position == -1 && proj_areas[i].equals(record_project_name)) {
				project_position = i;
			}
		}
		proj_adapter = new ArrayAdapter<String>(UploadGroutingPointActivity.this, android.R.layout.simple_spinner_item, proj_areas);
		proj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dlg_proj_spinner.setAdapter(proj_adapter);
		if (!record_project_name.equals("") && !record_subproject_name.equals("")) {
			if (project_position > -1) {
				dlg_proj_spinner.setSelection(project_position);
				project_position = -1;
			}
			else {
				dlg_proj_spinner.setSelection(0);
			}
		} else {
			dlg_proj_spinner.setSelection(0);
		}
		grout_priority_adapter = new ArrayAdapter<String>(UploadGroutingPointActivity.this, android.R.layout.simple_spinner_item, new String[]{"全部上传", "上坑道", "下坑道"});
		grout_priority_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dlg_grout_priority_spinner.setAdapter(grout_priority_adapter);
		dlg_proj_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				String proj_name_v = (String) proj_adapter.getItem(position);
				mProjectBase.getSubProjectNameList(eng_list, mProjectBase.getProjectId(proj_name_v));
				int eng_list_size = eng_list.size();
				String[] eng_areas = new String[eng_list_size];
				for (int i = 0; i < eng_list_size; i++) {
					eng_areas[i] = eng_list.get(i);
					if (subproject_position == -1 && eng_areas[i].equals(record_subproject_name)) {
						subproject_position = i;
					}
				}
				eng_adapter = new ArrayAdapter<String>(UploadGroutingPointActivity.this, android.R.layout.simple_spinner_item, eng_areas);
				eng_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				dlg_eng_spinner.setAdapter(eng_adapter);
				if (subproject_position > -1) {
					dlg_eng_spinner.setSelection(subproject_position);
					subproject_position = -1;
				}
				else {
					dlg_eng_spinner.setSelection(0);
				}
				start_mileage1.setText("");
				start_mileage2.setText("");
				end_mileage1.setText("");
				end_mileage2.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		dlg_eng_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int position, long id) {
				String dlg_proj_name_str = dlg_proj_spinner.getSelectedItem().toString();
				String dlg_eng_name_str = dlg_eng_spinner.getSelectedItem().toString();
				AppUtil.log("dlg_eng_spinner--------------"+dlg_eng_name_str);
				start_mileage1.setText("");
				start_mileage2.setText("");
				end_mileage1.setText("");
				end_mileage2.setText("");
				int project_id = mProjectBase.getProjectId(dlg_proj_name_str); 
				int subproject_id = mProjectBase.getSubProjectId(dlg_eng_name_str);
				mProjectBase.updateUploadProjectRecord(project_id, subproject_id);
				mProjectPointBase.closeDb();
				if (mProjectPointBase.openDb(project_id, subproject_id)) {
					String _upload_end_mileage = mProjectPointBase.getUploadEndMileage();
					int _upload_end_milage_page = mProjectPointBase.getMileageOrderNoLikeMileageName(
							_upload_end_mileage);
					AppUtil.log("_collect_end_mileage-----------"+_upload_end_mileage+
							" _collect_end_milage_page:"+_upload_end_milage_page);
					if (_upload_end_milage_page > 0) {
						collect_end_mileage_tv.setText(_upload_end_mileage);
					} else {
						collect_end_mileage_tv.setText("无记录");
					}
					
					String start_default_mileage = mProjectPointBase.getMileageNameWhereOrderNo(
							_upload_end_milage_page+1);
					AppUtil.log(_upload_end_mileage + " " + _upload_end_milage_page +
							" " + start_default_mileage);
					if (!start_default_mileage.equals("") && start_default_mileage.contains("+")) {
						String[] mileages = start_default_mileage.split("\\+"); 
						start_mileage1.setText(mileages[0]);
						end_mileage1.setText(mileages[0]);
						start_mileage2.setText(mileages[1]);
						end_mileage2.setText(mileages[1]);
					} 
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		search_start_ibt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String project_name = "";
				String subproject_name = "";
				if (dlg_proj_spinner.getSelectedItem() != null) {
					project_name = dlg_proj_spinner.getSelectedItem().toString();
					if (dlg_eng_spinner.getSelectedItem() != null) {
						click_event = CLICK_START_MILEAGE_EVENT;
						subproject_name = dlg_eng_spinner.getSelectedItem().toString();
						Intent intent = new Intent(UploadGroutingPointActivity.this, UploadMileageGridViewActivity.class);
						intent.putExtra("project_name", project_name);
						intent.putExtra("subproject_name", subproject_name);
						startActivity(intent);
					} else {
						Toast.makeText(context, "无效的工程", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(context, "无效的合同段", Toast.LENGTH_SHORT).show();
				}
			}
		});
		search_end_ibt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String project_name = "";
				String subproject_name = "";
				if (dlg_proj_spinner.getSelectedItem() != null) {
					project_name = dlg_proj_spinner.getSelectedItem().toString();
					if (dlg_eng_spinner.getSelectedItem() != null) {
						click_event = CLICK_END_MILEAGE_EVENT;
						subproject_name = dlg_eng_spinner.getSelectedItem().toString();
						Intent intent = new Intent(UploadGroutingPointActivity.this, UploadMileageGridViewActivity.class);
						intent.putExtra("project_name", project_name);
						intent.putExtra("subproject_name", subproject_name);
						startActivity(intent);
					} else {
						Toast.makeText(context, "无效的工程", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(context, "无效的合同段", Toast.LENGTH_SHORT).show();
				}
			}
		});
    	dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
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
				
				String dlg_proj_name_str = null;
				if (dlg_proj_spinner.getSelectedItem() != null) {
					dlg_proj_name_str = dlg_proj_spinner.getSelectedItem().toString();
				} else {
					Toast.makeText(context, "合同段不能为空，请重新选择！", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String dlg_eng_name_str = null;
				if (dlg_eng_spinner.getSelectedItem() != null) {
					dlg_eng_name_str = dlg_eng_spinner.getSelectedItem().toString();
				} else {
					Toast.makeText(context, "工程不能为空，请重新选择！", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String dlg_grout_priority_name_str = null;
				if (dlg_grout_priority_spinner.getSelectedItem() != null) {
					dlg_grout_priority_name_str = dlg_grout_priority_spinner.getSelectedItem().toString();
				} else {
					Toast.makeText(context, "坑道不能为空，请重新选择！", Toast.LENGTH_SHORT).show();
					return;
				}
				
				boolean start_mileage_format = checkMileage1Format(start_mileage1.getText().toString()) &&
						(start_mileage2.getText().toString()).matches("^[0-9]+(\\.[0-9]+)?$");
				boolean end_mileage_format = checkMileage1Format(end_mileage1.getText().toString()) &&
						(end_mileage2.getText().toString()).matches("^[0-9]+(\\.[0-9]+)?$");
				
				if (start_mileage_format && end_mileage_format) {
					double start_section_metre = Double.parseDouble(start_mileage1.getText().toString().substring(1)) * 1000
							+ Double.parseDouble(start_mileage2.getText().toString());
					double end_section_metre = Double.parseDouble(end_mileage1.getText().toString().substring(1)) * 1000
							+ Double.parseDouble(end_mileage2.getText().toString());
					AppUtil.log("dlg_proj_name_str:"+dlg_proj_name_str+ " dlg_eng_name_str:"+dlg_eng_name_str);
					
					project_id = mProjectBase.getProjectId(dlg_proj_name_str);
					if (project_id == -1) {
						Toast.makeText(context, "数据库中无该合同段名！", Toast.LENGTH_SHORT).show();
					} else {
						subproject_id = mProjectBase.getSubProjectId(dlg_eng_name_str);
						if (subproject_id == -1) {
							Toast.makeText(context, "工程选择错误！", Toast.LENGTH_SHORT).show();
						} else {
							mProjectPointBase.closeDb();
							if (mProjectPointBase.openDb(project_id, subproject_id)) {
								int mileage_start_orderno = mProjectPointBase.getMileageOrderNoWhereSectonMetre(start_section_metre);
								int mileage_end_orderno = mProjectPointBase.getMileageOrderNoWhereSectonMetre(end_section_metre);
								int anchor_start_orderno = mProjectPointBase.getAnchorStartOrderNoWhereSectionMetre(start_section_metre);
								int anchor_end_orderno = mProjectPointBase.getAnchorEndOrderNoWhereSectonMetre(end_section_metre);
								int grout_priority_mode = 0;
								if (anchor_start_orderno == 0) {
									Toast.makeText(context, "数据库中无该起始注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (anchor_end_orderno == 0) {
									Toast.makeText(context, "数据库中无该结束注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (anchor_start_orderno > anchor_end_orderno) {
									Toast.makeText(context, "起始注浆工段不能滞后于结束注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (anchor_start_orderno <= anchor_end_orderno) {
									
									if(dlg_grout_priority_name_str.equals("上坑道")) {
										if (mProjectPointBase.checkHasUpSectionPoints(anchor_start_orderno, anchor_end_orderno)) {
											grout_priority_mode = UPLOAD_MODE_UP;
										} else {
											Toast.makeText(context, "该区间内无上坑道数据", Toast.LENGTH_SHORT).show();
										}
									} else if (dlg_grout_priority_name_str.equals("下坑道")) {
										if (mProjectPointBase.checkHasDownSectionPoints(anchor_start_orderno, anchor_end_orderno)) {
											grout_priority_mode = UPLOAD_MODE_DOWN;
										} else {
											Toast.makeText(context, "该区间内无下坑道数据", Toast.LENGTH_SHORT).show();
										}
									} else if (dlg_grout_priority_name_str.equals("全部上传")) {
										grout_priority_mode = UPLOAD_MODE_ALL;
									}
									if (grout_priority_mode != 0) {
										proj_name = dlg_proj_name_str;
										eng_name = dlg_eng_name_str;
										proj_name_tv.setText(proj_name);
										eng_name_tv.setText(eng_name);
										upload_end_mileage = mProjectPointBase.getMileageNameWhereOrderNo(mileage_end_orderno);
										new UploadGroutingDataThread(anchor_start_orderno, anchor_end_orderno,
												mileage_start_orderno, mileage_end_orderno, grout_priority_mode).start();
										try {
											Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
											field.setAccessible(true);
											field.set(dialog, true);// 将mShowing变量设为false，表示对话框已关闭
											dialog.dismiss();
										} catch (Exception e) {
											e.printStackTrace();
										}
										dlg_eng_spinner = null;
										eng_adapter = null;
									}
								}
							} else {
								Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
							}
						}
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
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
    		
    	});
    	dlg.create();
    	dlg.setCancelable(false);
    	dlg.show();
	}
	
	private BroadcastReceiver BdReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String mileage_name = intent.getStringExtra("sectionName");
			if (click_event == CLICK_START_MILEAGE_EVENT) {
				if (start_mileage1 != null && start_mileage2 != null) {
					start_mileage1.setText(mileage_name.split("\\+")[0]);
					start_mileage2.setText(mileage_name.split("\\+")[1]);
				}
			} else if (click_event == CLICK_END_MILEAGE_EVENT) {
				if (end_mileage1 != null && end_mileage2 != null) {
					end_mileage1.setText(mileage_name.split("\\+")[0]);
					end_mileage2.setText(mileage_name.split("\\+")[1]);
				}
			}
			click_event = 0;
		};
	};
	
	private int grout_priority_mode;
	class UploadGroutingDataThread extends Thread {
		private int anchor_start_orderno;
		private int anchor_end_orderno;
		private int mileage_start_orderno;
		private int mileage_end_orderno;
		
		public UploadGroutingDataThread(int anchor_start_orderno, int anchor_end_orderno,
				int mileage_start_orderno, int mileage_end_orderno, int upload_mode)
		{
			this.anchor_start_orderno = anchor_start_orderno;
			this.anchor_end_orderno = anchor_end_orderno;
			this.mileage_start_orderno = mileage_start_orderno;
			this.mileage_end_orderno = mileage_end_orderno;
			grout_priority_mode = upload_mode;
		}
		
		@Override
		public void run() {
			ApiClient apiClient = ApiClient.getInstance(context);
			boolean result = false;
			UploadParameter uploadData = null;
			String prev_anchor_name = "";
			int retry = 3;
			PointGroutingDataUploadListResponse response = null;
			while (anchor_start_orderno <= anchor_end_orderno) {
				if (grout_priority_mode == UPLOAD_MODE_UP) {
					if (!mProjectPointBase.checkIsUpSectionPoints(anchor_start_orderno)) {
						anchor_start_orderno++;
						continue;
					}
				} else if (grout_priority_mode == UPLOAD_MODE_DOWN) {
					if (!mProjectPointBase.checkIsDownSectionPoints(anchor_start_orderno)) {
						anchor_start_orderno++;
						continue;
					}
				}
				uploadData = mProjectPointBase.getUploadParameter(anchor_start_orderno);
				mHandler.sendMessage(mHandler.obtainMessage(1, uploadData.getAnchorName()));
				result = false;
				if (uploadData != null) {
					response = apiClient.uploadPointGroutingData(subproject_id, uploadData.getAnchorName(),
							uploadData.getGroutingDate(), uploadData.getGroutingData(), uploadData.getFullHoleCapacity());
					if (response != null) {
						result = response.isSuccess;
					}
					AppUtil.log(uploadData.getAnchorName() + " " + uploadData.getGroutingDate() + 
							" " + uploadData.getGroutingData() + " " + uploadData.getPadString());
				} 
				if (result) {
					prev_anchor_name = uploadData.getAnchorName();
					mProjectPointBase.updateCraftUploadState(anchor_start_orderno, 1);
					anchor_start_orderno++;
					retry = 3;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					if (response != null) {
						if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
							break;
						}
					} else {
						break;
					}
					if (retry-- > 0) 
					{
						if (uploadData.getGroutingData() != null && !uploadData.getGroutingData().equals(""))
							continue;
						else
							if (!choiceItemsStatus)
								mHandler.sendMessage(mHandler.obtainMessage(6, uploadData.getAnchorName()+"未注浆"));
							else {
								anchor_start_orderno++;
								retry = 3;
								mHandler.sendMessage(mHandler.obtainMessage(7, uploadData.getAnchorName()));
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								continue;
							}
					} else {
						mHandler.sendMessage(mHandler.obtainMessage(6, uploadData.getAnchorName()+"上传失败:"
								+response.errorString));
							
					}
					while (!dialogClick) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						/*try {
							mutex_lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}*/
					}
					dialogClick = false;
					if (!uploadContinue) {
						break;
					} else {
						anchor_start_orderno++;
						retry = 3;
					}
				}
			}
			if (anchor_start_orderno == anchor_end_orderno+1) {
				mProjectPointBase.updateLoadEndMileage(upload_end_mileage);
				mHandler.sendEmptyMessage(2);
			} else {
				if (!uploadContinue) {
					mHandler.sendMessage(mHandler.obtainMessage(9, prev_anchor_name));
				} else {
					if (response != null) {
						CacheManager.setNetErrorMsg(response.errorString);
						mHandler.sendMessage(mHandler.obtainMessage(3, uploadData.getAnchorName()));
					} else {
						mHandler.sendMessage(mHandler.obtainMessage(8, uploadData.getAnchorName()));
					}
				}
			}
		}
	}
	
	private final Handler mHandler = new Handler() {
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
				case 1:
					if (grout_priority_mode == UPLOAD_MODE_UP)
						text_status.setText("正在上传上坑道"+(String)msg.obj+"注浆数据……");
					else if (grout_priority_mode == UPLOAD_MODE_DOWN)
						text_status.setText("正在上传下坑道"+(String)msg.obj+"注浆数据……");
					else
						text_status.setText("正在上传"+(String)msg.obj+"注浆数据……");
					break;
					
				case 2:
					if (grout_priority_mode == UPLOAD_MODE_UP)
						text_status.setText("上坑道数据上传成功！");
					else if (grout_priority_mode == UPLOAD_MODE_DOWN)
						text_status.setText("下坑道数据上传成功！");
					else
						text_status.setText("全部数据上传成功！");
					break;
					
				case 3:
					text_status.setText("上传终止,"+(String)msg.obj+"上传失败:"+CacheManager.getNetErrorMsg());
					SharedView.showUserExpiredAlertDialog(UploadGroutingPointActivity.this);
					break;
					
				case 4:
					UIUtilities.showToast(UploadGroutingPointActivity.this, "网络错误", true);
					break;
					
				case 5:
					UIUtilities.showToast(UploadGroutingPointActivity.this, "用户已过期，请重新登陆！", true);
					break;
					
				case 6:
					LayoutInflater factory = LayoutInflater.from(UploadGroutingPointActivity.this);
					final View dialogView = factory.inflate(R.layout.upload_ungrounting_dialog, null);
					final TextView message_tv = (TextView) dialogView.findViewById(R.id.alter_tv_id);
					final CheckBox checkBox = (CheckBox) dialogView.findViewById(R.id.checkbox_id);
					AlertDialog.Builder dialog = new  AlertDialog.Builder(UploadGroutingPointActivity.this);
					dialog.setTitle("提示" );
					dialog.setView(dialogView);
					message_tv.setText("检测到工点:"+(String)msg.obj+"，是否继续?" ); 
					checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							choiceItemsStatus = isChecked;
						}
					});
					dialog.setNegativeButton("取消",   new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialogClick = true;
							uploadContinue = false;
//							mutex_lock.notify();
							try {
								Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, true);// 将mShowing变量设为false，表示对话框已关闭
								dialog.dismiss();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} );  	
					dialog.setPositiveButton("继续",  new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							dialogClick = true;
							uploadContinue = true;
//							mutex_lock.notify();
							try {
								Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
								field.setAccessible(true);
								field.set(dialog, true);// 将mShowing变量设为false，表示对话框已关闭
								dialog.dismiss();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} );   
					dialog.create();
					dialog.setCancelable(false);
					dialog.show();
					break;
					
				case 7:
					text_status.setText("检测到工点"+(String)msg.obj+"未注浆");
					break;
					
				case 8:
					text_status.setText("上传终止,"+(String)msg.obj+"response error:用户可能已过期，尝试重新登录！");
					break;
					
				case 9:
					String anchor_name = (String)msg.obj;
					if (anchor_name.equals("")) {
						text_status.setText("上传结束");
					} else {
						text_status.setText("上传结束,终止到"+anchor_name);
					}
					break;
			default:
				break;
			}
		 };
	 };
	
    @Override
    protected void onDestroy() {
    	if (mProjectBase != null) {
    		mProjectBase.closeDb();
    	}
    	if (mProjectPointBase != null) {
    		mProjectPointBase.closeDb();
    	}
    	context.unregisterReceiver(BdReceiver);
    	MyActivityManager.removeActivity(UploadGroutingPointActivity.this);
    	super.onDestroy();
    }
	
}
