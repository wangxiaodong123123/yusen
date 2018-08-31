package com.ysdata.blender.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.ysdata.blender.R;
import com.ysdata.blender.cloud.api.BooleanResponse;
import com.ysdata.blender.cloud.api.IntResponse;
import com.ysdata.blender.cloud.util.ApiClient;
import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.cloud.util.ConstDef;
import com.ysdata.blender.cloud.util.SharedView;
import com.ysdata.blender.cloud.util.UIUtilities;
import com.ysdata.blender.database.ProjectDataBaseAdapter;
import com.ysdata.blender.database.ProjectPointDataBaseAdapter;
import com.ysdata.blender.element.MixCraftParameter;
import com.ysdata.blender.uart.MyActivityManager;
import com.ysdata.blender.wireless.client.Format;

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

public class UploadMixParamterActivity extends Activity{
	
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
	private static final int CLICK_START_MIXDATE_EVENT = 1;
	private static final int CLICK_END_MIXDATE_EVENT = 2;
	int click_event = 0;
	String upload_end_mixdate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_mix_data);
		context = this;
		MyActivityManager.addActivity(UploadMixParamterActivity.this);
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
	EditText start_mix_date;
	EditText end_mix_date;
	int project_position = -1;
	int subproject_position = -1;
	String record_project_name;
	String record_subproject_name;
	private void ParameterInputDialog() {
		LayoutInflater factory = LayoutInflater.from(UploadMixParamterActivity.this);
		final View dialogView = factory.inflate(R.layout.mix_param_upload_dialog, null);
		AlertDialog.Builder dlg = new AlertDialog.Builder(UploadMixParamterActivity.this);
    	dlg.setTitle("输入要上传的搅拌数据");
    	dlg.setView(dialogView);
    	final Spinner dlg_proj_spinner = (Spinner) dialogView.findViewById(R.id.proj_name_dlg_id);
    	final ImageButton search_start_ibt = (ImageButton) dialogView.findViewById(R.id.id_search_start_mix_date_ib);
    	final ImageButton search_end_ibt = (ImageButton) dialogView.findViewById(R.id.id_search_end_mix_date_ib);
    	TextView record_tv = (TextView) dialogView.findViewById(R.id.record_tv_id);
    	final TextView collect_end_mix_date_tv = (TextView) dialogView.findViewById(R.id.id_end_collect_mix_date_tv);
    	record_tv.setText("上次上传结束搅拌日期:");
    	start_mix_date = (EditText) dialogView.findViewById(R.id.id_start_mix_date_et);
		end_mix_date = (EditText) dialogView.findViewById(R.id.id_end_mix_date_et);
		dlg_eng_spinner = (Spinner) dialogView.findViewById(R.id.eng_name_dlg_id);
		final ArrayAdapter<String> proj_adapter;
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
		proj_adapter = new ArrayAdapter<String>(UploadMixParamterActivity.this, android.R.layout.simple_spinner_item, proj_areas);
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
				eng_adapter = new ArrayAdapter<String>(UploadMixParamterActivity.this, android.R.layout.simple_spinner_item, eng_areas);
				eng_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				dlg_eng_spinner.setAdapter(eng_adapter);
				if (subproject_position > -1) {
					dlg_eng_spinner.setSelection(subproject_position);
					subproject_position = -1;
				}
				else {
					dlg_eng_spinner.setSelection(0);
				}
				start_mix_date.setText("");
				end_mix_date.setText("");
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
				start_mix_date.setText("");
				end_mix_date.setText("");
				int project_id = mProjectBase.getProjectId(dlg_proj_name_str); 
				int subproject_id = mProjectBase.getSubProjectId(dlg_eng_name_str);
				mProjectBase.updateUploadProjectRecord(project_id, subproject_id);
				mProjectPointBase.closeDb();
				if (mProjectPointBase.openDb(project_id, subproject_id)) {
					String _upload_end_mix_date = mProjectPointBase.getUploadEndMixDate();
					int _upload_end_mix_page = mProjectPointBase.getMixOrderNoLikeDate(
							_upload_end_mix_date);
					AppUtil.log("_upload_end_mix_date-----------" + _upload_end_mix_date+
							" _upload_end_milage_page:" + _upload_end_mix_page);
					if (_upload_end_mix_page > 0) {
						collect_end_mix_date_tv.setText(_upload_end_mix_date);
					} else {
						collect_end_mix_date_tv.setText("无记录");
					}
					
					String start_default_mix_date = mProjectPointBase.getMixDateWhereOrderNo(
							_upload_end_mix_page+1);
					AppUtil.log(_upload_end_mix_date + " " + _upload_end_mix_date +
							" " + start_default_mix_date);
					if (!start_default_mix_date.equals("")) {
						start_mix_date.setText(start_default_mix_date);
						end_mix_date.setText(start_default_mix_date);
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
						click_event = CLICK_START_MIXDATE_EVENT;
						subproject_name = dlg_eng_spinner.getSelectedItem().toString();
						Intent intent = new Intent(UploadMixParamterActivity.this, UploadMixDateGridViewActivity.class);
						intent.putExtra("action", "cloud");
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
						click_event = CLICK_END_MIXDATE_EVENT;
						subproject_name = dlg_eng_spinner.getSelectedItem().toString();
						Intent intent = new Intent(UploadMixParamterActivity.this, UploadMixDateGridViewActivity.class);
						intent.putExtra("action", "cloud");
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
							int mixdata_start_orderno = mProjectPointBase.getMixOrderNoLikeDate(start_mix_date.getText().toString());
							int mixdata_end_orderno = mProjectPointBase.getMixOrderNoLikeDate(end_mix_date.getText().toString());
							if (mixdata_start_orderno == 0) {
								Toast.makeText(context, "数据库中无该起始搅拌日期！", Toast.LENGTH_SHORT).show();
							} else if (mixdata_end_orderno == 0) {
								Toast.makeText(context, "数据库中无该结束搅拌日期！", Toast.LENGTH_SHORT).show();
							} else if (mixdata_start_orderno > mixdata_end_orderno) {
								Toast.makeText(context, "起始搅拌日期不能滞后于结束教版日期！", Toast.LENGTH_SHORT).show();
							} else if (mixdata_start_orderno <= mixdata_end_orderno) {
								proj_name = dlg_proj_name_str;
								eng_name = dlg_eng_name_str;
								proj_name_tv.setText(proj_name);
								eng_name_tv.setText(eng_name);
								upload_end_mixdate = mProjectPointBase.getMixDateWhereOrderNo(mixdata_end_orderno);
								new UploadGroutingDataThread(mixdata_start_orderno, mixdata_end_orderno).start();
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
						} else {
							Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
						}
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
			String mix_date = intent.getStringExtra("mixdate");
			if (click_event == CLICK_START_MIXDATE_EVENT) {
				if (start_mix_date != null) {
					start_mix_date.setText(mix_date);
				}
			} else if (click_event == CLICK_END_MIXDATE_EVENT) {
				if (end_mix_date != null) {
					end_mix_date.setText(mix_date);
				}
			}
			click_event = 0;
		};
	};
	
	int uploadOrder;
	class UploadGroutingDataThread extends Thread {
		private int mix_start_orderno;
		private int mix_end_orderno;
		
		public UploadGroutingDataThread(int mix_start_orderno, int mix_end_orderno)
		{
			this.mix_start_orderno = mix_start_orderno;
			this.mix_end_orderno = mix_end_orderno;
		}
		
		@Override
		public void run() {
			ApiClient apiClient = ApiClient.getInstance(context);
			boolean result = false;
			int retry = 0;
			IntResponse intResponse = null;
			BooleanResponse booleanResponse = null;
			String strDate;
			String strBeginTime;
			String strEndTime;
			double dblMixRatioWater;
			double dblCount;
			double dblPosition;
			String strDesignImage;
			String strActiveImage;
			int responseId;
			boolean isExpired = false;
			
			MixCraftParameter mixCraftParameter;
			while(mix_start_orderno <= mix_end_orderno) {
				mixCraftParameter = mProjectPointBase.getMixCraftParameter(mix_start_orderno);
				if (mixCraftParameter != null) {
					uploadOrder = mixCraftParameter.getId();
					strDate = mixCraftParameter.getMixDate();
					strBeginTime = mixCraftParameter.getStartTime();
					strEndTime = mixCraftParameter.getEndTime();
					dblMixRatioWater = mixCraftParameter.getMixRatio();
					dblCount = mixCraftParameter.getCementWeight();
					dblPosition = mixCraftParameter.getDevPostion();
					strDesignImage = mProjectPointBase.getMixResultPicString(uploadOrder);
					strActiveImage = mProjectPointBase.getMixScenePicString(uploadOrder);
					
					retry = 0;
					while(retry++ < 3) {
						mHandler.sendMessage(mHandler.obtainMessage(1, retry));
						intResponse = apiClient.uploadBlenderActiveUploadData(subproject_id, uploadOrder, strDate, 
								strBeginTime, strEndTime, dblMixRatioWater, dblCount, dblPosition);
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
						if (strDesignImage != null && strDesignImage.length() > 0) {
							booleanResponse = apiClient.uploadBlendeDesignImage(responseId, strDesignImage);
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
						if (strActiveImage != null && strActiveImage.length() > 0) {
							booleanResponse = apiClient.uploadBlendeActiveImage(responseId, strActiveImage);
							if (booleanResponse != null) {
								result = booleanResponse.isSuccess;
								if (!result) {
									if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
										isExpired = true;
										break;
									}
									continue;
								} else {
									mix_start_orderno++;
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
						mHandler.sendMessage(mHandler.obtainMessage(4));
						break;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if (mix_start_orderno > mix_end_orderno) {
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
						text_status.setText("正在上传第"+uploadOrder+"次搅拌数据……");
					else 
						text_status.setText("第"+uploadOrder+"次搅拌数据上传失败，正在进行第"+retry+"次重传……");
					break;
					
				case 2:
					int retry1 = (Integer)msg.obj;
					if (retry1 > 1)
						text_status.setText("第"+uploadOrder+"次搅拌效果图上传失败，正在进行第"+retry1+"次重传……");
					break;
					
				case 3:
					int retry2 = (Integer)msg.obj;
					if (retry2 > 1)
						text_status.setText("第"+uploadOrder+"次搅拌现场图上传失败，正在进行第"+retry2+"次重传……");
					break;	
					
				case 4:
					text_status.setText("第"+(Integer)msg.obj+"次搅拌数据上传失败,Error:" + CacheManager.getNetErrorMsg());
					break;
					
				case 5:
					SharedView.showUserExpiredAlertDialog(UploadMixParamterActivity.this);
					break;
					
				case 6:
					UIUtilities.showToast(UploadMixParamterActivity.this, "网络错误", true);
					break;
					
				case 7:
					UIUtilities.showToast(UploadMixParamterActivity.this, "用户已过期，请重新登陆！", true);
					break;
					
				case 8:
					text_status.setText("数据上传成功！");
					break;
					
				case 9:
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
    	MyActivityManager.removeActivity(UploadMixParamterActivity.this);
    	super.onDestroy();
    }
	
}
