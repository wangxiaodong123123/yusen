package com.ysdata.grouter.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.ysdata.grouter.R;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.uart.MyActivityManager;
import com.ysdata.grouter.uart.UartPanelService;
import com.ysdata.grouter.wireless.client.Format;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

public class CollectPanelActivity extends Activity{
	
	private final int START_CONNECT = 0;
	private final int SUCCESS_CONNECT = 2;
	private final int CHECK_SUCCESS = 3;
	private final int CHECK_FAILED = 4;
	private final int DEVICE_LOST = 5;
	private final int DATA_TRANSFERING = 6;
	private final int SUCCESS_CONNECT_INTERNET = 7;
	
	private int status = START_CONNECT;
	private Context context;
	private TextView text_status;
	private TextView status_title;
	private TextView bt_connect_status;
	private TextView proj_name_tv;
	private TextView eng_name_tv;
	private TextView anchor_seq_tv;
	int project_id;
	int subproject_id;
	String anchor_seq;
	private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	String eng_name;
	String proj_name;
	int collect_end_mileage_page = 0;
	private static final int CLICK_START_MILEAGE_EVENT = 1;
	private static final int CLICK_END_MILEAGE_EVENT = 2;
	int click_event = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transfer_status_phone);
		context = this;
		MyActivityManager.addActivity(CollectPanelActivity.this);
		text_status = (TextView) findViewById(R.id.text_status);
		status_title = (TextView) findViewById(R.id.recv_status_title);
		bt_connect_status = (TextView) findViewById(R.id.bt_connect_status);
		status_title.setText("数据采集->集中式数据存储器->采样状态");
		bt_connect_status.setText("已连接到有线通信设备");
		text_status.setText("正在联网到集中式数据存储器......");
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		proj_name_tv = (TextView) findViewById(R.id.proj_name_id);
		eng_name_tv = (TextView) findViewById(R.id.eng_name_id);
		anchor_seq_tv = (TextView) findViewById(R.id.anchor_seq_id);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
		if (mProjectBase.openDb()) {
			ParameterInputDialog();
		} else {
			Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
		}
		registerIntent();
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
	
	public String getStartDefaultMileage() {
		String _collect_end_mileage = mProjectPointBase.getCollectEndMileage();
		int _collect_end_milage_page = mProjectPointBase.getMileageOrderNoLikeMileageName(_collect_end_mileage);
		return mProjectPointBase.getMileageNameWhereOrderNo(_collect_end_milage_page+1);
	}
	
	private Spinner dlg_eng_spinner;
	private ArrayAdapter<String> eng_adapter;
	EditText start_mileage1;
	EditText start_mileage2;
	EditText end_mileage1;
	EditText end_mileage2;
	int project_position = -1;
	int subproject_position = -1;
	String record_project_name;
	String record_subproject_name;
	private void ParameterInputDialog() {
		LayoutInflater factory = LayoutInflater.from(CollectPanelActivity.this);
		final View dialogView = factory.inflate(R.layout.panel_mileage_param_dialog_phone, null);
		dialogView.findViewById(R.id.grout_priority_ly).setVisibility(View.GONE);
		start_mileage1 = (EditText) dialogView.findViewById(R.id.start_mileage_et1);
		start_mileage2 = (EditText) dialogView.findViewById(R.id.start_mileage_et2);
		final TextView collect_end_mileage_tv = (TextView) dialogView.findViewById(R.id.end_collect_mileage_id);
		final ImageButton search_start_ibt = (ImageButton) dialogView.findViewById(R.id.search_start_mileage_id);
    	final ImageButton search_end_ibt = (ImageButton) dialogView.findViewById(R.id.search_end_mileage_id);
		end_mileage1 = (EditText) dialogView.findViewById(R.id.end_mileage_et1);
		end_mileage2 = (EditText) dialogView.findViewById(R.id.end_mileage_et2);
		AlertDialog.Builder dlg = new AlertDialog.Builder(CollectPanelActivity.this);
    	dlg.setTitle("输入要接收的置筋参数");
    	dlg.setIcon(android.R.drawable.ic_dialog_map);
    	dlg.setView(dialogView);
    	final Spinner dlg_proj_spinner = (Spinner) dialogView.findViewById(R.id.proj_name_dlg_id);
		dlg_eng_spinner = (Spinner) dialogView.findViewById(R.id.eng_name_dlg_id);
		final ArrayAdapter<String> proj_adapter;
		ArrayList<String> prj_list = new ArrayList<String>();
		final ArrayList<String> eng_list = new ArrayList<String>();
		mProjectBase.getProjectNameList(prj_list);
		int proj_list_size = prj_list.size();
		String[] proj_areas = new String[proj_list_size];
		int record_project_id = mProjectBase.getCollectProjectId();
		int record_subproject_id = mProjectBase.getCollectSubProjectId();
		record_project_name = mProjectBase.getProjectName(record_project_id);
		record_subproject_name = mProjectBase.getSubProjectName(record_subproject_id);
		for (int i = 0; i < proj_list_size; i++) {
			proj_areas[i] = prj_list.get(i);
			if (project_position == -1 && proj_areas[i].equals(record_project_name)) {
				project_position = i;
			}
		}
		proj_adapter = new ArrayAdapter<String>(CollectPanelActivity.this, android.R.layout.simple_spinner_item, proj_areas);
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
				start_mileage1.setText("");
				start_mileage2.setText("");
				end_mileage1.setText("");
				end_mileage2.setText("");
				String proj_name_v = (String) proj_adapter.getItem(position);
				AppUtil.log("dlg_proj_spinner--------------"+proj_name_v);
				mProjectBase.getSubProjectNameList(eng_list, mProjectBase.getProjectId(proj_name_v));
				int eng_list_size = eng_list.size();
				String[] eng_areas = new String[eng_list_size];
				for (int i = 0; i < eng_list_size; i++) {
					eng_areas[i] = eng_list.get(i);
					if (subproject_position == -1 && eng_areas[i].equals(record_subproject_name)) {
						subproject_position = i;
					}
				}
				eng_adapter = new ArrayAdapter<String>(CollectPanelActivity.this, android.R.layout.simple_spinner_item, eng_areas);
				eng_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				dlg_eng_spinner.setAdapter(eng_adapter);
				if (subproject_position > -1) {
					dlg_eng_spinner.setSelection(subproject_position);
					subproject_position = -1;
				}
				else {
					dlg_eng_spinner.setSelection(0);
				}
				if (dlg_eng_spinner.getSelectedItem() == null) {
					start_mileage1.setText("");
					start_mileage2.setText("");
					end_mileage1.setText("");
					end_mileage2.setText("");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
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
				mProjectBase.updateCollectProjectRecord(project_id, subproject_id);
				mProjectPointBase.closeDb();
				if (mProjectPointBase.openDb(project_id, subproject_id)) {
					String _collect_end_mileage = mProjectPointBase.getCollectEndMileage();
					int _collect_end_milage_page = mProjectPointBase.getMileageOrderNoLikeMileageName(
							_collect_end_mileage);
					AppUtil.log("_collect_end_mileage-----------"+_collect_end_mileage+
							" _collect_end_milage_page:"+_collect_end_milage_page);
					if (_collect_end_milage_page > 0) {
						collect_end_mileage_tv.setText(_collect_end_mileage);
					} else {
						collect_end_mileage_tv.setText("无记录");
					}
					
					String start_default_mileage = mProjectPointBase.getMileageNameWhereOrderNo(
							_collect_end_milage_page+1);
					AppUtil.log(_collect_end_mileage + " " + _collect_end_milage_page +
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
				// TODO Auto-generated method stub
				
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
						AppUtil.log(project_name + " " + subproject_name);
						Intent intent = new Intent(CollectPanelActivity.this, MileageGridViewActivity.class);
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
						AppUtil.log(project_name + " " + subproject_name);
						Intent intent = new Intent(CollectPanelActivity.this, MileageGridViewActivity.class);
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
								double start_section_metre = Double.parseDouble(start_mileage1.getText().toString().substring(1)) * 1000
										+ Double.parseDouble(start_mileage2.getText().toString());
								double end_section_metre = Double.parseDouble(end_mileage1.getText().toString().substring(1)) * 1000
										+ Double.parseDouble(end_mileage2.getText().toString());
								int mileage_start_orderno = mProjectPointBase.getMileageOrderNoWhereSectonMetre(start_section_metre);
								int mileage_end_orderno = mProjectPointBase.getMileageOrderNoWhereSectonMetre(end_section_metre);
								int anchor_start_orderno = mProjectPointBase.getAnchorStartOrderNoWhereSectionMetre(start_section_metre);
								int anchor_end_orderno = mProjectPointBase.getAnchorEndOrderNoWhereSectonMetre(end_section_metre);
								if (anchor_start_orderno == 0) {
									Toast.makeText(context, "数据库中无该起始注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (anchor_end_orderno == 0) {
									Toast.makeText(context, "数据库中无该结束注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (anchor_start_orderno > anchor_end_orderno) {
									Toast.makeText(context, "起始注浆工段不能滞后于结束注浆工段！", Toast.LENGTH_SHORT).show();
								} else if (anchor_start_orderno <= anchor_end_orderno) {
									proj_name = dlg_proj_name_str;
									eng_name = dlg_eng_name_str;
									proj_name_tv.setText(proj_name);
									eng_name_tv.setText(eng_name);
									Intent intent = new Intent(Format.RECV_PANEL_DATA);
									intent.putExtra("anchor_start_orderno", anchor_start_orderno);
									intent.putExtra("anchor_end_orderno", anchor_end_orderno);
									intent.putExtra("mileage_start_orderno", mileage_start_orderno);
									intent.putExtra("mileage_end_orderno", mileage_end_orderno);
									intent.putExtra("eng_name", eng_name);
									intent.putExtra("proj_name", proj_name);
									collect_end_mileage_page = mileage_end_orderno;
									context.sendBroadcast(intent);
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
				UartPanelService.getInstance().aterDilogCancel();
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
	
    @Override
    protected void onDestroy() {
    	AppUtil.log("============onDestroy============");
    	unregisterReceiver(updateReceiver);
    	if (mProjectBase != null) {
    		mProjectBase.closeDb();
    	}
    	if (mProjectPointBase != null) {
    		mProjectPointBase.closeDb();
    	}
    	super.onDestroy();
    	MyActivityManager.removeActivity(CollectPanelActivity.this);
    }
	
	@Override
	public void onBackPressed() {
		if (status == DATA_TRANSFERING || status == START_CONNECT || status == SUCCESS_CONNECT
				|| status == SUCCESS_CONNECT_INTERNET) {
//			Toast.makeText(getApplicationContext(), "数据通信中，禁止任何操作！",
//				     Toast.LENGTH_SHORT).show();
				AlertDialog.Builder dialog = new  AlertDialog.Builder(context);    
				dialog.setTitle("提示" );
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setMessage("退出将导致数据传输中断，下一次传输数据需重启控制器，是否继续？" );  
				dialog.setNegativeButton("取消", null);
				dialog.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						UartPanelService.getInstance().aterDilogCancel();
						finish();
					}
				} );   
				dialog.create();
				dialog.show();
		} else {
			finish();
		}
	};
	
    public void registerIntent() {
    	IntentFilter updateFilter = new IntentFilter();
    	updateFilter.addAction(Format.CHECK_SUCCESS);
    	updateFilter.addAction(Format.CHECK_FAILED);
    	updateFilter.addAction(Format.DEVICE_CONNECTION_LOST);
    	updateFilter.addAction(Format.DATA_COMM_PARAM);
    	updateFilter.addAction(Format.SUCCESS_CONNECT_INTERNET);
    	updateFilter.addAction(Format.REQUEST_TRANSFER_DATA);
    	updateFilter.addAction(Format.SEND_ANCHOR_PARAMETER);
    	updateFilter.addAction(Format.PROJ_ENG_MATCH);
    	updateFilter.addAction(Format.PROJ_ENG_NOT_MATCH);
    	updateFilter.addAction(Format.ACTION_SEND_MILEAGE_NAME);
    	context.registerReceiver(updateReceiver, updateFilter);
    }
    
    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
    	@Override
		public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (action.equals(Format.SUCCESS_CONNECT_INTERNET)) {
    			status = SUCCESS_CONNECT_INTERNET;
    			text_status.setText("联网成功，请求传输合同段与工程名称......");
    			AppUtil.log("SUCCESS_CONNECT_INTERNET");
    		} else if (action.equals(Format.CHECK_SUCCESS)) {
    			status = CHECK_SUCCESS;
    			text_status.setText("数据接收完成，全部数据校验成功！");
    			mProjectPointBase.updateCollectEndMileage(mProjectPointBase.
    					getMileageNameWhereOrderNo(collect_end_mileage_page));
    			AppUtil.log("CHECK_SUCCESS");
    		} else if (action.equals(Format.CHECK_FAILED)) {
    			status = CHECK_FAILED;
    			text_status.setText("接收的数据有误，校验失败！"); //当前显示置筋的下一个置筋数据
    			AppUtil.log("CHECK_FAILED");
    		} else if (action.equals(Format.DEVICE_CONNECTION_LOST)) {
    			if (status != CHECK_SUCCESS) {
    				text_status.setText("通信异常，数据未接收完成！");
    			}
    			status = DEVICE_LOST;
    			bt_connect_status.setText("有线通信设备已断开");
    			AppUtil.log("DEVICE_CONNECTION_LOST");
    		} else if (action.equals(Format.SEND_ANCHOR_PARAMETER)) {
    			anchor_seq = intent.getStringExtra("anchor_seq");
    			anchor_seq_tv.setText(anchor_seq);
    			status = DATA_TRANSFERING;
    			text_status.setText("正在接收数据，请不要强制断开有线连接......");
    		} else if (action.equals(Format.PROJ_ENG_MATCH)) {
    			text_status.setText("合同段与工程相匹配，正在请求发送数据......");
    		} else if (action.equals(Format.PROJ_ENG_NOT_MATCH)) {
    			text_status.setText("合同段与工程不相匹配");
    		} else if (action.equals(Format.REQUEST_TRANSFER_DATA)) {
    			text_status.setText("准备接收数据......");
    		} else if (action.equals(Format.ACTION_SEND_MILEAGE_NAME)) {
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
    		}
    	};
    };
}
