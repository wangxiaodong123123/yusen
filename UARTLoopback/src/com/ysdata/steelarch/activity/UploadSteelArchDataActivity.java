package com.ysdata.steelarch.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.cloud.api.BooleanResponse;
import com.ysdata.steelarch.cloud.api.IntResponse;
import com.ysdata.steelarch.cloud.api.SteelArchCollectData;
import com.ysdata.steelarch.cloud.util.ApiClient;
import com.ysdata.steelarch.cloud.util.AppUtil;
import com.ysdata.steelarch.cloud.util.CacheManager;
import com.ysdata.steelarch.cloud.util.ConstDef;
import com.ysdata.steelarch.cloud.util.SharedView;
import com.ysdata.steelarch.cloud.util.UIUtilities;
import com.ysdata.steelarch.database.ProjectDataBaseAdapter;
import com.ysdata.steelarch.database.ProjectPointDataBaseAdapter;
import com.ysdata.steelarch.element.MixCraftParameter;
import com.ysdata.steelarch.element.SteelArchCollectParameter;
import com.ysdata.steelarch.uart.MyActivityManager;
import com.ysdata.steelarch.uart.UartControlSender;
import com.ysdata.steelarch.wireless.client.Format;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UploadSteelArchDataActivity extends Activity{
	
	private Context context;
	private TextView text_status;
	private TextView proj_name_tv;
	private TextView eng_name_tv;
	TextView user_info_tv;
	int project_id;
	int subproject_id;
	private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	String eng_name;
	String proj_name;
	private static final int CLICK_START_STEELARCH_EVENT = 1;
	private static final int CLICK_END_STEELARCH_EVENT = 2;
	int click_event = 0;
	int upload_end_steelarch_orderno;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_mix_data);
		context = this;
		MyActivityManager.addActivity(UploadSteelArchDataActivity.this);
		user_info_tv = (TextView) findViewById(R.id.user_info);
		user_info_tv.setText(CacheManager.getUserName() + " 欢迎你!");
		text_status = (TextView) findViewById(R.id.text_status);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		proj_name_tv = (TextView) findViewById(R.id.proj_name_id);
		eng_name_tv = (TextView) findViewById(R.id.eng_name_id);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
		if (mProjectBase.openDb()) {
			ParameterInputDialog();
		} else {
			Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
		}
		IntentFilter filter = new IntentFilter();
        filter.addAction(Format.ACTION_SEND_MILEAGE_NAME);
        context.registerReceiver(BdReceiver, filter);
	}
	
	private Spinner dlg_eng_spinner;
	private ArrayAdapter<String> eng_adapter;
	EditText start_mileage1;
	EditText start_mileage2;
	EditText end_mileage1;
	EditText end_mileage2;
	private void ParameterInputDialog() {
		LayoutInflater factory = LayoutInflater.from(context);
		final View dialogView = factory.inflate(R.layout.panel_mileage_param_dialog, null);
		dialogView.findViewById(R.id.grout_priority_ly).setVisibility(View.GONE);
		AlertDialog.Builder dlg = new AlertDialog.Builder(context);
    	dlg.setTitle("选择要上传的起止钢拱架");
    	dlg.setView(dialogView);
    	final Spinner dlg_proj_spinner = (Spinner) dialogView.findViewById(R.id.proj_name_dlg_id);
    	final ImageButton search_start_ibt = (ImageButton) dialogView.findViewById(R.id.search_start_mileage_id);
    	final ImageButton search_end_ibt = (ImageButton) dialogView.findViewById(R.id.search_end_mileage_id);
    	TextView record_tv = (TextView) dialogView.findViewById(R.id.record_tv_id);
    	record_tv.setText("上次上传的结束钢拱架:");
    	start_mileage1 = (EditText) dialogView.findViewById(R.id.start_mileage_et1);
		start_mileage2 = (EditText) dialogView.findViewById(R.id.start_mileage_et2);
		end_mileage1 = (EditText) dialogView.findViewById(R.id.end_mileage_et1);
		end_mileage2 = (EditText) dialogView.findViewById(R.id.end_mileage_et2);
		dlg_eng_spinner = (Spinner) dialogView.findViewById(R.id.eng_name_dlg_id);
		final ArrayAdapter<String> proj_adapter;
		ArrayList<String> prj_list = new ArrayList<String>();
		final ArrayList<String> eng_list = new ArrayList<String>();
		mProjectBase.getProjectNameList(prj_list);
		int proj_list_size = prj_list.size();
		String[] proj_areas = new String[proj_list_size];
		for (int i = 0; i < proj_list_size; i++) {
			proj_areas[i] = prj_list.get(i);
		}
		proj_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, proj_areas);
		proj_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dlg_proj_spinner.setAdapter(proj_adapter);
		dlg_proj_spinner.setSelection(0);
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
				}
				eng_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, eng_areas);
				eng_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				dlg_eng_spinner.setAdapter(eng_adapter);
				dlg_eng_spinner.setSelection(0);
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
					String start_default_mileage = mProjectPointBase.getSteelArchNameWhereOrderNo(1);
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
						click_event = CLICK_START_STEELARCH_EVENT;
						subproject_name = dlg_eng_spinner.getSelectedItem().toString();
						Intent intent = new Intent(context, SteelArchGridViewActivity.class);
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
						click_event = CLICK_END_STEELARCH_EVENT;
						subproject_name = dlg_eng_spinner.getSelectedItem().toString();
						Intent intent = new Intent(context, SteelArchGridViewActivity.class);
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
								int steelarch_start_orderno = mProjectPointBase.getSteelArchOrderNoWhereNameMetre(start_section_metre);
								int steelarch_end_orderno = mProjectPointBase.getSteelArchOrderNoWhereNameMetre(end_section_metre);
								if (steelarch_start_orderno == 0) {
									Toast.makeText(context, "数据库中无该起始钢拱架！", Toast.LENGTH_SHORT).show();
								} else if (steelarch_end_orderno == 0) {
									Toast.makeText(context, "数据库中无该结束钢拱架！", Toast.LENGTH_SHORT).show();
								} else if (steelarch_start_orderno > steelarch_end_orderno) {
									Toast.makeText(context, "起始钢拱架不能滞后于结束钢拱架！", Toast.LENGTH_SHORT).show();
								} else if (steelarch_start_orderno <= steelarch_end_orderno) {
									dlg_eng_spinner = null;
									eng_adapter = null;
									proj_name_tv.setText(dlg_proj_name_str);
									eng_name_tv.setText(dlg_eng_name_str);
									upload_end_steelarch_orderno = steelarch_end_orderno;
									new UploadSteelArchDataThread(steelarch_start_orderno, steelarch_end_orderno).start();
									try {
										Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
										field.setAccessible(true);
										field.set(dialog, true);// 将mShowing变量设为false，表示对话框已关闭
										dialog.dismiss();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
							}
						}
					}
				} else {
					if (!start_mileage_format) {
						Toast.makeText(context, "起始钢拱架名称格式输入错误，参考格式：k12+1.5", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "结束钢拱架名称格式输入错误，参考格式：k12+1.5", Toast.LENGTH_SHORT).show();
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
			String action = intent.getAction();
			if (action.equals(Format.ACTION_SEND_MILEAGE_NAME)) {
    			String mileage_name = intent.getStringExtra("sectionName");
    			if (click_event == CLICK_START_STEELARCH_EVENT) {
    				if (start_mileage1 != null && start_mileage2 != null) {
    					start_mileage1.setText(mileage_name.split("\\+")[0]);
    					start_mileage2.setText(mileage_name.split("\\+")[1]);
    				}
    			} else if (click_event == CLICK_END_STEELARCH_EVENT) {
    				if (end_mileage1 != null && end_mileage2 != null) {
    					end_mileage1.setText(mileage_name.split("\\+")[0]);
    					end_mileage2.setText(mileage_name.split("\\+")[1]);
    				}
    			}
    			click_event = 0;
    		}
			click_event = 0;
		};
	};
	
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
	
	String steelArchName;;
	class UploadSteelArchDataThread extends Thread {
		private int steelarch_start_orderno;
		private int steelarch_end_orderno;
		
		public UploadSteelArchDataThread(int steelarch_start_orderno, int steelarch_end_orderno)
		{
			this.steelarch_start_orderno = steelarch_start_orderno;
			this.steelarch_end_orderno = steelarch_end_orderno;
		}
		
		@Override
		public void run() {
			ApiClient apiClient = ApiClient.getInstance(context);
			boolean result = false;
			int retry = 0;
			IntResponse intResponse = null;
			BooleanResponse booleanResponse = null;
			String name;
			int id;
			String left_measure_date;
			String right_measure_date;
			double left_measure_distance;
			double right_measure_distance;
			double left_tunnelface_distance;
			double right_tunnelface_distance;
			double left_secondcar_distance;
			double right_secondcar_distance;
			String left_pic_dir_tunnelface;
			String right_pic_dir_tunnelface;
			String left_pic_dir_entrance;
			String right_pic_dir_entrance;		
			int responseId;
			boolean isExpired = false;
			
			SteelArchCollectParameter mSteelArchCollectParameter;
			while(steelarch_start_orderno <= steelarch_end_orderno) {
				mSteelArchCollectParameter = mProjectPointBase.getSteelArchCollectParameter(steelarch_start_orderno);
				if (mSteelArchCollectParameter != null) {
					id = mSteelArchCollectParameter.getId();
					name = mSteelArchCollectParameter.getName();
					steelArchName = name;
					left_measure_date = mSteelArchCollectParameter.getLeftMeasureDate();
					right_measure_date = mSteelArchCollectParameter.getRightMeasureDate();
					left_measure_distance = mSteelArchCollectParameter.getLeftSteelarchToSteelarchDistance();
					right_measure_distance = mSteelArchCollectParameter.getRightSteelarchToSteelarchDistance();
					left_tunnelface_distance = mSteelArchCollectParameter.getLeftSteelarchToTunnelFaceDistance();
					right_tunnelface_distance = mSteelArchCollectParameter.getRightSteelarchToTunnelFaceDistance();
					left_secondcar_distance = mSteelArchCollectParameter.getLeftSecondCarToSteelarchDistance();
					right_secondcar_distance = mSteelArchCollectParameter.getRightSecondCarToSteelarchDistance();
					left_pic_dir_tunnelface = mProjectPointBase.getSteelArchLeftPicDirTunnelFace(steelarch_start_orderno);
					right_pic_dir_tunnelface = mProjectPointBase.getSteelArchRightPicDirTunnelFace(steelarch_start_orderno);
					left_pic_dir_entrance = mProjectPointBase.getSteelArchLeftPicDirEntrance(steelarch_start_orderno);
					right_pic_dir_entrance = mProjectPointBase.getSteelArchRightPicDirEntrance(steelarch_start_orderno);
					retry = 0;
					while(retry++ < 3) {
						mHandler.sendMessage(mHandler.obtainMessage(1, retry));
						intResponse = apiClient.uploadSteelArchUploadData(subproject_id, id, name, 
								left_measure_date, right_measure_date, left_measure_distance, right_measure_distance, 
								left_tunnelface_distance, right_tunnelface_distance, left_secondcar_distance,
								right_secondcar_distance);
						if (intResponse != null) {
							result = intResponse.isSuccess;
							if (!result) {
								if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
									isExpired = true;
									break;
								}
								continue;
							}
						} else {
							result = false;
							continue;
						}
						break;
					}
					if (!result) {
						break;
					}
					
					retry = 0;
					responseId = intResponse.data;	
					while(retry++ < 3) {
						mHandler.sendMessage(mHandler.obtainMessage(2, retry));
						if (left_pic_dir_tunnelface != null && left_pic_dir_tunnelface.length() > 0) {
							booleanResponse = apiClient.uploadSteelArchLeftPicDirTunnelface(responseId, left_pic_dir_tunnelface);
							if (booleanResponse != null) {
								result = booleanResponse.isSuccess;
								if (!result) {
									if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
										isExpired = true;
										break;
									}
									continue;
								}
							} else {
								result = false;
								continue;
							}
							break;
						} else {
							result = true;
						}
						break;
					}
					if (!result) break;
					
					retry = 0;
					while(retry++ < 3) {
						mHandler.sendMessage(mHandler.obtainMessage(3, retry));
						if (left_pic_dir_entrance != null && left_pic_dir_entrance.length() > 0) {
							booleanResponse = apiClient.uploadSteelArchLeftPicDirEntrance(responseId, left_pic_dir_entrance);
							if (booleanResponse != null) {
								result = booleanResponse.isSuccess;
								if (!result) {
									if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
										isExpired = true;
										break;
									}
									continue;
								}
							} else {
								result = false;
								continue;
							}
							break;
						} else {
							result = true;
						}
						break;
					}
					
					retry = 0;
					while(retry++ < 3) {
						mHandler.sendMessage(mHandler.obtainMessage(4, retry));
						if (right_pic_dir_entrance != null && right_pic_dir_entrance.length() > 0) {
							booleanResponse = apiClient.uploadSteelArchRightPicDirEntrance(responseId, right_pic_dir_entrance);
							if (booleanResponse != null) {
								result = booleanResponse.isSuccess;
								if (!result) {
									if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
										isExpired = true;
										break;
									}
									continue;
								}
							} else {
								result = false;
								continue;
							}
							break;
						} else {
							result = true;
						}
						break;
					}
					
					retry = 0;
					while(retry++ < 3) {
						mHandler.sendMessage(mHandler.obtainMessage(5, retry));
						if (right_pic_dir_entrance != null && right_pic_dir_entrance.length() > 0) {
							booleanResponse = apiClient.uploadSteelArchRightPicDirEntrance(responseId, right_pic_dir_entrance);
							if (booleanResponse != null) {
								result = booleanResponse.isSuccess;
								if (!result) {
									if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
										isExpired = true;
										break;
									}
									continue;
								} else {
									steelarch_start_orderno++;
								}
							} else {
								result = false;
								continue;
							}
							break;
						} else {
							result = true;
						}
						break;
					}					
					
					if (isExpired) {
						mHandler.sendMessage(mHandler.obtainMessage(7));
						break;
					}
					if (retry >= 3 || !result) {
						mHandler.sendMessage(mHandler.obtainMessage(6));
						break;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if (steelarch_start_orderno > steelarch_end_orderno) {
				mHandler.sendEmptyMessage(8);
			}
		}
	}
	
	private final Handler mHandler = new Handler() {
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
				case 1:
					int retry = (Integer)msg.obj;
					if (retry == 1)
						text_status.setText("正在上传"+steelArchName+"测量数据……");
					else 
						text_status.setText(steelArchName+"测量数据上传失败，正在进行第"+retry+"次重传……");
					break;
					
				case 2:
					int retry1 = (Integer)msg.obj;
					if (retry1 > 1)
						text_status.setText(steelArchName+"左侧掌子面方向照片上传失败，正在进行第"+retry1+"次重传……");
					break;
					
				case 3:
					int retry2 = (Integer)msg.obj;
					if (retry2 > 1)
						text_status.setText(steelArchName+"左侧入口方向照片上传失败，正在进行第"+retry2+"次重传……");
					break;	
					
				case 4:
					int retry3 = (Integer)msg.obj;
					if (retry3 > 1)
						text_status.setText(steelArchName+"右侧掌子面方向照片上传失败，正在进行第"+retry3+"次重传……");
					break;	
					
				case 5:
					int retry4 = (Integer)msg.obj;
					if (retry4 > 1)
						text_status.setText(steelArchName+"右侧掌子面方向照片上传失败，正在进行第"+retry4+"次重传……");
					break;	
					
				case 6:
					text_status.setText(steelArchName+"数据上传失败,Error:" + CacheManager.getNetErrorMsg());
					break;
					
				case 7:
					UIUtilities.showToast(UploadSteelArchDataActivity.this, "用户已过期，请重新登陆！", true);
					break;
					
				case 8:
					text_status.setText("数据上传成功！");
					break;
					
				case 9:
					SharedView.showUserExpiredAlertDialog(UploadSteelArchDataActivity.this);
					break;
					
				case 10:
					text_status.setText("上传终止,"+(String)msg.obj+"response error:用户可能已过期，尝试重新登录！");
					break;
					
				case 11:
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
    	MyActivityManager.removeActivity(UploadSteelArchDataActivity.this);
    	super.onDestroy();
    }
	
}
