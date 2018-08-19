package com.ysdata.steelarch.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.cloud.util.AppUtil;
import com.ysdata.steelarch.cloud.util.CacheManager;
import com.ysdata.steelarch.database.ProjectDataBaseAdapter;
import com.ysdata.steelarch.database.ProjectPointDataBaseAdapter;
import com.ysdata.steelarch.uart.MyActivityManager;
import com.ysdata.steelarch.uart.UartPanelService;
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

public class RxSteelArchParamterActivity extends Activity{
	
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
	private TextView proj_name_tv;
	private TextView eng_name_tv;
	private TextView title_tv;
	TextView dev_connect_status_tv;
	int project_id;
	int subproject_id;
	private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	private UartPanelService mUartPanelService;
	String eng_name;
	String proj_name;
	int click_event = 0;
	Activity activity;
	private int rx_end_steelarch_orderno = 0;
	
	String proj_name_string = "";
	String eng_name_string = "";
	
	int steelarch_orderno = 0;
	boolean isLeft = true;
	int pic_length = 0;

	private static final int CLICK_START_STEELARCH_EVENT = 1;
	private static final int CLICK_END_STEELARCH_EVENT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rx_tx_mix_data);
		context = this;
		MyActivityManager.addActivity(RxSteelArchParamterActivity.this);
		activity = RxSteelArchParamterActivity.this;
		title_tv = (TextView) findViewById(R.id.title);
		title_tv.setText("钢拱架数据采集");
		dev_connect_status_tv = (TextView) findViewById(R.id.dev_connect_status);
		dev_connect_status_tv.setText(R.string.mix_connected);
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
        filter.addAction(Format.SUCCESS_CONNECT_INTERNET);
        filter.addAction(Format.ENG_MATCH);
        filter.addAction(Format.ENG_NOT_MATCH);
        filter.addAction(Format.REQUEST_TRANSFER_DATA_SUCCESS);
        filter.addAction(Format.RECV_STEELARCH_DATA);
        filter.addAction(Format.RECV_ENTRANCE_PIC);
        filter.addAction(Format.RECV_TUNNELFACE_PIC);
        filter.addAction(Format.STEELARCH_COMM_FINISH);
        registerReceiver(SendParamReceiver, filter);
	}
	
	
	private Spinner dlg_eng_spinner;
	private ArrayAdapter<String> eng_adapter;
	EditText start_mileage1;
	EditText start_mileage2;
	EditText end_mileage1;
	EditText end_mileage2;
	private void ParameterInputDialog() {
		LayoutInflater factory = LayoutInflater.from(RxSteelArchParamterActivity.this);
		final View dialogView = factory.inflate(R.layout.panel_mileage_param_dialog, null);
		dialogView.findViewById(R.id.grout_priority_ly).setVisibility(View.GONE);
		AlertDialog.Builder dlg = new AlertDialog.Builder(RxSteelArchParamterActivity.this);
    	dlg.setTitle("选择要采集的起止钢拱架");
    	dlg.setView(dialogView);
    	final Spinner dlg_proj_spinner = (Spinner) dialogView.findViewById(R.id.proj_name_dlg_id);
    	final ImageButton search_start_ibt = (ImageButton) dialogView.findViewById(R.id.search_start_mileage_id);
    	final ImageButton search_end_ibt = (ImageButton) dialogView.findViewById(R.id.search_end_mileage_id);
    	TextView record_tv = (TextView) dialogView.findViewById(R.id.record_tv_id);
    	record_tv.setText("上次采集的结束钢拱架:");
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
		proj_adapter = new ArrayAdapter<String>(RxSteelArchParamterActivity.this, android.R.layout.simple_spinner_item, proj_areas);
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
				eng_adapter = new ArrayAdapter<String>(RxSteelArchParamterActivity.this, android.R.layout.simple_spinner_item, eng_areas);
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
						Intent intent = new Intent(RxSteelArchParamterActivity.this, SteelArchGridViewActivity.class);
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
						Intent intent = new Intent(RxSteelArchParamterActivity.this, SteelArchGridViewActivity.class);
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
									proj_name_string = dlg_proj_name_str;
									eng_name_string = dlg_eng_name_str;
									
									proj_name_tv.setText(proj_name_string);
									eng_name_tv.setText(eng_name_string);
									
									CacheManager.setDbProjectId(project_id);
									CacheManager.setDbSubProjectId(subproject_id);
									mUartPanelService = new UartPanelService(context, mHandler);
									mUartPanelService.setInstance(mUartPanelService);
									text_status.setText("正在联网到控制仪.....");
									
									Intent intent = new Intent(Format.RECV_PANEL_DATA);
									intent.putExtra("steelarch_start_orderno", steelarch_start_orderno);
									intent.putExtra("steelarch_end_orderno", steelarch_end_orderno);
									intent.putExtra("eng_name", dlg_eng_name_str);
									intent.putExtra("proj_name", dlg_proj_name_str);
									rx_end_steelarch_orderno = steelarch_end_orderno;
									context.sendBroadcast(intent);
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
	
	private final Handler mHandler = new Handler() {
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
				case 1:
					break;
					
				case 2:
					break;
					
				case 3:
					break;
			default:
				break;
			}
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
	
    private BroadcastReceiver SendParamReceiver = new BroadcastReceiver() {
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
    		} else if (action.equals(Format.SUCCESS_CONNECT_INTERNET)) {
    			status = SUCCESS_CONNECT_INTERNET;
    		} else if (action.equals(Format.ENG_MATCH)) {
    			text_status.setText("工程匹配成功");
    		} else if (action.equals(Format.ENG_NOT_MATCH)) {
    			text_status.setText("工程匹配失败");
    		} else if (action.equals(Format.REQUEST_TRANSFER_DATA_SUCCESS)) {
    			text_status.setText("准备接收数据......");
    		} else if (action.equals(Format.RECV_STEELARCH_DATA)) {
    			status = DATA_TRANSFERING;
    			steelarch_orderno = intent.getIntExtra("steelarch_orderno_g", 0);
    			String date = intent.getStringExtra("date");
    			String name = intent.getStringExtra("name");
    			isLeft = intent.getBooleanExtra("isLeft", true);
    			double measure_distance = intent.getDoubleExtra("measure_distance", 0);
    			double tunnelface_distance = intent.getDoubleExtra("tunnelface_distance", 0);
    			double secondcar_distance = intent.getDoubleExtra("secondcar_distance", 0);
    			if (isLeft)
    			{
    				text_status.setText("正在采集第"+steelarch_orderno+"根钢拱架左侧数据，编号:"+name+
    						"，测量日期:"+date+"，实测间距:"+measure_distance+"m.离掌子面距离:"+
    						tunnelface_distance+"m.离二次模板台车距离:"+secondcar_distance+"m");
    			}
    			else
    			{
    				text_status.setText("正在采集第"+steelarch_orderno+"根钢拱架右侧数据，编号:"+name+
    						"，测量日期:"+date+"，实测间距:"+measure_distance+"m.离掌子面距离:"+
    						tunnelface_distance+"m.离二次模板台车距离:"+secondcar_distance+"m");
    			}
    		} else if (action.equals(Format.RECV_ENTRANCE_PIC)) {
    			int pic_total_size = intent.getIntExtra("pic_total_size", 0);
    			int pic_size_index = intent.getIntExtra("pic_size_index", 0);
    			if (pic_total_size >= pic_size_index)
    			{
    				if (isLeft)
    				{
    					text_status.setText("正在采集第"+steelarch_orderno+"根钢拱架左侧洞口方向图片数据,图片总长度:"+pic_total_size+"byte，已采集到:"+pic_size_index+"byte");
    				}
    				else
    				{
    					text_status.setText("正在采集第"+steelarch_orderno+"根钢拱架右侧洞口方向图片数据,图片总长度:"+pic_total_size+"byte，已采集到:"+pic_size_index+"byte");
    				}
    			}
    			else 
    			{
    				if (isLeft)
    				{
    					text_status.setText("第"+steelarch_orderno+"根钢拱架左侧洞口方向图片数据采集失败：接收数据长度("+pic_total_size+"byte)"+"超过实际长度("+pic_size_index+"byte)。");
    				}
    				else
    				{
    					text_status.setText("第"+steelarch_orderno+"根钢拱架右侧洞口方向图片数据采集失败：接收数据长度("+pic_total_size+"byte)"+"超过实际长度("+pic_size_index+"byte)。");
    				}
    			}
    		} else if (action.equals(Format.RECV_TUNNELFACE_PIC)) {
    			int pic_total_size = intent.getIntExtra("pic_total_size", 0);
    			int pic_size_index = intent.getIntExtra("pic_size_index", 0);
    			if (pic_total_size >= pic_size_index) 
    			{
    				if (isLeft)
    				{
    					text_status.setText("正在采集第"+steelarch_orderno+"根钢拱架左侧掌子面方向图片数据,图片总长度:"+pic_total_size+"byte，已采集到:"+pic_size_index+"byte");
    				}
    				else {
    					text_status.setText("正在采集第"+steelarch_orderno+"根钢拱架右侧掌子面方向图片数据,图片总长度:"+pic_total_size+"byte，已采集到:"+pic_size_index+"byte");
    				}
    			}
    			else 
    			{
    				if (isLeft) 
    				{
    					text_status.setText("第"+steelarch_orderno+"根钢拱架左侧掌子面方向图片数据采集失败：接收数据长度("+pic_total_size+"byte)"+"超过实际长度("+pic_size_index+"byte)。");
    				}
    				else
    				{
    					text_status.setText("第"+steelarch_orderno+"根钢拱架右侧掌子面方向图片数据采集失败：接收数据长度("+pic_total_size+"byte)"+"超过实际长度("+pic_size_index+"byte)。");
    				}
    			}
    		} else if (action.equals(Format.STEELARCH_COMM_FINISH)) {
    			status = CHECK_SUCCESS;
				text_status.setText("钢拱架数据采集完成!");
    		}
    	};
    };	
	
		@Override
		public void onBackPressed() {
			if (status == DATA_TRANSFERING || status == START_CONNECT || status == SUCCESS_CONNECT
					|| status == SUCCESS_CONNECT_INTERNET) {
					AlertDialog.Builder dialog = new  AlertDialog.Builder(context);    
					dialog.setTitle("提示" );
					dialog.setIcon(android.R.drawable.ic_dialog_info);
					dialog.setMessage("退出将导致数据传输中断，是否继续？" );  
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
	
    @Override
    protected void onDestroy() {
    	if (mProjectBase != null) {
    		mProjectBase.closeDb();
    	}
    	if (mProjectPointBase != null) {
    		mProjectPointBase.closeDb();
    	}
    	MyActivityManager.removeActivity(RxSteelArchParamterActivity.this);
    	super.onDestroy();
    }
	
}
