package com.ysdata.blender.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.ysdata.blender.R;
import com.ysdata.blender.cloud.util.ApiClient;
import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.cloud.util.SharedView;
import com.ysdata.blender.cloud.util.UIUtilities;
import com.ysdata.blender.database.ProjectDataBaseAdapter;
import com.ysdata.blender.database.ProjectPointDataBaseAdapter;
import com.ysdata.blender.element.DdMixParameter;
import com.ysdata.blender.uart.MyActivityManager;
import com.ysdata.blender.uart.UartControlSender;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TxMixParamterActivity extends Activity{
	
	private Context context;
	private TextView text_status;
	private TextView proj_name_tv;
	private TextView eng_name_tv;
	TextView dev_connect_status_tv;
	int project_id;
	int subproject_id;
	String proj_name_string;
	String eng_name_string;
	String mix_ratio_string;
	private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	private UartControlSender uartControlSender;
	int click_event = 0;
	String upload_end_mixdate;
	
	private boolean sendStatus = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rx_tx_mix_data);
		context = this;
		MyActivityManager.addActivity(TxMixParamterActivity.this);
		dev_connect_status_tv = (TextView) findViewById(R.id.dev_connect_status);
		dev_connect_status_tv.setText(R.string.mix_connected);
		text_status = (TextView) findViewById(R.id.text_status);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		proj_name_tv = (TextView) findViewById(R.id.proj_name_id);
		eng_name_tv = (TextView) findViewById(R.id.eng_name_id);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
		
		IntentFilter filter = new IntentFilter();
        filter.addAction(Format.SUCCESS_CONNECT_INTERNET);
        filter.addAction(Format.SUCCESS_SEND_MIX_CRAFT_DATA);
        filter.addAction(Format.FAILED_SEND_MIX_CRAFT_DATA);
        filter.addAction(Format.RECEIVE_MIX_CRAFT_ACK_TIMEOUT);
        registerReceiver(SendParamReceiver, filter);
        
        text_status.setText("正在检测搅拌仪联网请求......");
		
		if (mProjectBase.openDb()) {
			ParameterInputDialog();
		} else {
			Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
		}
	}
	
    private BroadcastReceiver SendParamReceiver = new BroadcastReceiver() {
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (action.equals(Format.SUCCESS_SEND_MIX_CRAFT_DATA)) {
    			text_status.setText("已成功发送搅拌参数!");
    			sendStatus = false;
    		} else if (action.equals(Format.RECEIVE_MIX_CRAFT_ACK_TIMEOUT)) {
    			text_status.setText("接收搅拌参数应答信号超时!");
    			sendStatus = false;
    		} else if (action.equals(Format.FAILED_SEND_MIX_CRAFT_DATA)) {
    			text_status.setText("搅拌参数发送失败!");
    			sendStatus = true;
    		} else if (action.equals(Format.SUCCESS_CONNECT_INTERNET)) {
    			text_status.setText("已接收到搅拌仪联网请求!");
    		}
    	};
    };
	
	private Spinner dlg_eng_spinner;
	private ArrayAdapter<String> eng_adapter;
	private Spinner dlg_mix_ratio_spinner;
	private ArrayAdapter<String> mix_ratio_adapter;
	private void ParameterInputDialog() {
		LayoutInflater factory = LayoutInflater.from(TxMixParamterActivity.this);
		final View dialogView = factory.inflate(R.layout.mix_param_send_dialog, null);
		AlertDialog.Builder dlg = new AlertDialog.Builder(TxMixParamterActivity.this);
    	dlg.setTitle("输入要传输的搅拌参数");
    	dlg.setView(dialogView);
    	final Spinner dlg_proj_spinner = (Spinner) dialogView.findViewById(R.id.proj_name_dlg_id);
		dlg_eng_spinner = (Spinner) dialogView.findViewById(R.id.eng_name_dlg_id);
		dlg_mix_ratio_spinner = (Spinner) dialogView.findViewById(R.id.mix_ratio_dlg_id);
		final ArrayAdapter<String> proj_adapter;
		ArrayList<String> prj_list = new ArrayList<String>();
		final ArrayList<String> eng_list = new ArrayList<String>();
		final ArrayList<String> mix_ratio_list = new ArrayList<String>();
		mProjectBase.getProjectNameList(prj_list);
		int proj_list_size = prj_list.size();
		String[] proj_areas = new String[proj_list_size];
		for (int i = 0; i < proj_list_size; i++) {
			proj_areas[i] = prj_list.get(i);
		}
		proj_adapter = new ArrayAdapter<String>(TxMixParamterActivity.this, android.R.layout.simple_spinner_item, proj_areas);
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
				eng_adapter = new ArrayAdapter<String>(TxMixParamterActivity.this, android.R.layout.simple_spinner_item, eng_areas);
				eng_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				dlg_eng_spinner.setAdapter(eng_adapter);
				dlg_eng_spinner.setSelection(0);
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
				int project_id = mProjectBase.getProjectId(dlg_proj_name_str); 
				int subproject_id = mProjectBase.getSubProjectId(dlg_eng_name_str);
				mProjectBase.updateUploadProjectRecord(project_id, subproject_id);
				mProjectPointBase.closeDb();
				if (mProjectPointBase.openDb(project_id, subproject_id)) {
					mProjectPointBase.getDdMixRatioList(mix_ratio_list);
					if (mix_ratio_list != null) {
						int mix_ratio_list_size = mix_ratio_list.size();
						String[] mix_ratio_areas = new String[mix_ratio_list_size];
						for (int i = 0; i < mix_ratio_list_size; i++) {
							mix_ratio_areas[i] = mix_ratio_list.get(i);
						}
						mix_ratio_adapter = new ArrayAdapter<String>(TxMixParamterActivity.this, android.R.layout.simple_spinner_item, mix_ratio_areas);
						mix_ratio_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						dlg_mix_ratio_spinner.setAdapter(mix_ratio_adapter);
						dlg_mix_ratio_spinner.setSelection(0);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
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
							String dlg_mix_ratio_string = dlg_mix_ratio_spinner.getSelectedItem().toString();
							if (!dlg_mix_ratio_string.equals("")) {
								try {
									Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
									field.setAccessible(true);
									field.set(dialog, true);
									dialog.dismiss();
								} catch (Exception e) {
									e.printStackTrace();
								}
								dlg_eng_spinner = null;
								eng_adapter = null;
								proj_name_string = dlg_proj_name_str;
								eng_name_string = dlg_eng_name_str;
								mix_ratio_string = dlg_mix_ratio_string;
								
								proj_name_tv.setText(proj_name_string);
								eng_name_tv.setText(eng_name_string);
								
								CacheManager.setDbProjectId(project_id);
								CacheManager.setDbSubProjectId(subproject_id);
								uartControlSender = new UartControlSender(context, mHandler, mProjectPointBase);
								uartControlSender.setInstance(uartControlSender);
								uartControlSender.start();
								
								Intent intent = new Intent(Format.SEND_CONTROL_DATA);
								intent.putExtra("eng_name", eng_name_string);
								intent.putExtra("proj_name", proj_name_string);
								intent.putExtra("mix_ratio_string", mix_ratio_string);
								context.sendBroadcast(intent);
							} else {
								Toast.makeText(context, "水灰比出错！", Toast.LENGTH_SHORT).show();
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
					field.set(dialog, true);
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
	
	class TransferThread extends Thread {
		@Override
		public void run() {
			super.run();

		}
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
					SharedView.showUserExpiredAlertDialog(TxMixParamterActivity.this);
					break;
					
				case 4:
					UIUtilities.showToast(TxMixParamterActivity.this, "网络错误", true);
					break;
					
				case 5:
					UIUtilities.showToast(TxMixParamterActivity.this, "用户已过期，请重新登陆！", true);
					break;
					
				case 6:
					break;
					
				case 7:
					break;
					
				case 8:
					text_status.setText("上传终止,"+(String)msg.obj+"response error:用户可能已过期，尝试重新登录！");
					break;
					
				case 9:
					break;
			default:
				break;
			}
		 };
	 };
	 
	    public void onBackPressed() {
			if (sendStatus) {
				AlertDialog.Builder dialog = new  AlertDialog.Builder(TxMixParamterActivity.this);    
				dialog.setTitle("警告" );
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setMessage("正在传输数据，退出将导致数据传输中断，是否继续?" );  
				dialog.setNegativeButton("取消", null);
				dialog.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						UartControlSender.getInstance().stop();
						finish();
					}
				} );   
				dialog.create();
				dialog.show();
			} else {
				super.onBackPressed();
			}
		}
	
    @Override
    protected void onDestroy() {
    	if (mProjectBase != null) {
    		mProjectBase.closeDb();
    	}
    	if (mProjectPointBase != null) {
    		mProjectPointBase.closeDb();
    	}
    	if (uartControlSender != null) {
    		uartControlSender.stop();
    	}
    	MyActivityManager.removeActivity(TxMixParamterActivity.this);
    	super.onDestroy();
    }
	
}
