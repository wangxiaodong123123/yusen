package com.ysdata.grouter.activity;

import java.lang.reflect.Field;

import com.ysdata.grouter.R;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.CacheManager;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.uart.MyActivityManager;
import com.ysdata.grouter.uart.UartControlSender;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CraftFileTransferActivity extends Activity {
	Context context;
	private boolean sendStatus = true;
	TextView btStatusTitle;
	private TextView send_status;
	boolean btConnectStatus = true;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	String eng_name;
	String proj_name;
	int mode = 1;
	int project_id = 0;
	int subproject_id = 0;
	int send_end_mileage_page = 0;
	private static final int CLICK_START_MILEAGE_EVENT = 1;
	private static final int CLICK_END_MILEAGE_EVENT = 2;
	int click_event = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.transfer_panel_param_phone);
		context = this;
		MyActivityManager.addActivity(CraftFileTransferActivity.this);
		IntentFilter filter = new IntentFilter();
        filter.addAction(Format.SUCCESS_SEND_DATA);
        filter.addAction(Format.FAILED_SEND_DATA);
        filter.addAction(Format.DEVICE_CONNECTION_LOST);
        filter.addAction(Format.RECEIVE_DATA_TIMEOUT);
        filter.addAction(Format.RECEIVE_SYSTEM_INFO_ACK_TIMEOUT);
        filter.addAction(Format.RECEIVE_ANCHOR_PARAM_ACK_TIMEOUT);
        filter.addAction(Format.SUCCESS_CONNECT_INTERNET);
        filter.addAction(Format.SENDING_PARAM);
        filter.addAction(Format.SEND_DATA_FINISH);
        filter.addAction(Format.SUCCESS_SEND_SYSTEM_INFO);
        filter.addAction(Format.FAILED_SEND_SYSTEM_INFO);
        filter.addAction(Format.ACTION_SEND_MILEAGE_NAME);
        registerReceiver(SendParamReceiver, filter);
        mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
        ProjectDataBaseAdapter mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
        project_id = CacheManager.getDbProjectId();
        subproject_id = CacheManager.getDbSubProjectId();
		if (mProjectBase.openDb() && mProjectPointBase.openDb(project_id, subproject_id)) {
			btStatusTitle = (TextView)findViewById(R.id.set_param_btStatus);
			btStatusTitle.setText("已连接到控制面板");
			proj_name = mProjectBase.getProjectName(project_id);
			eng_name = mProjectBase.getSubProjectName(subproject_id);
			TextView title_name = (TextView) findViewById(R.id.title_proj_tv);
			title_name.setText(proj_name+"->"+eng_name);
			send_status = (TextView)findViewById(R.id.send_status);
			send_status.setText("正在检测控制面板联网请求......");
			MileageInputDialog();
		} else {
			Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
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
	
	public String getStartDefaultMileage() {
		String _send_end_mileage = mProjectPointBase.getSendEndMileage();
		int _send_end_milage_page = mProjectPointBase.getMileageOrderNoLikeMileageName(_send_end_mileage);
		return mProjectPointBase.getMileageNameWhereOrderNo(_send_end_milage_page+1);
	}
	
	EditText start_mileage1;
	EditText start_mileage2;
	EditText end_mileage1;
	EditText end_mileage2;
	
	private void MileageInputDialog() {
		LayoutInflater factory = LayoutInflater.from(CraftFileTransferActivity.this);
		final View dialogView = factory.inflate(R.layout.transfer_mileage_param_dialog, null);
		AlertDialog.Builder dlg = new AlertDialog.Builder(CraftFileTransferActivity.this);
    	dlg.setTitle("输入要传输的注浆工段");
    	dlg.setIcon(android.R.drawable.ic_dialog_map);
    	dlg.setView(dialogView);
    	
    	start_mileage1 = (EditText) dialogView.findViewById(R.id.start_mileage_et1);
    	start_mileage2 = (EditText) dialogView.findViewById(R.id.start_mileage_et2);
    	end_mileage1 = (EditText) dialogView.findViewById(R.id.end_mileage_et1);
    	end_mileage2 = (EditText) dialogView.findViewById(R.id.end_mileage_et2);
    	final ImageButton search_start_ibt = (ImageButton) dialogView.findViewById(R.id.search_start_mileage_id);
    	final ImageButton search_end_ibt = (ImageButton) dialogView.findViewById(R.id.search_end_mileage_id);
    	final TextView send_end_mileage_tv = (TextView) dialogView.findViewById(R.id.end_send_mileage_id);
    
    	String _send_end_mileage = mProjectPointBase.getSendEndMileage();
		int _send_end_milage_page = mProjectPointBase.getMileageOrderNoLikeMileageName(_send_end_mileage);
		if (_send_end_milage_page > 0) {
			send_end_mileage_tv.setText(_send_end_mileage);
		} else {
			send_end_mileage_tv.setText("无记录");
		}
		String start_default_mileage = mProjectPointBase.getMileageNameWhereOrderNo(_send_end_milage_page+1);
    	if (!start_default_mileage.equals("") && start_default_mileage.contains("+")) {
    		String[] mileages = start_default_mileage.split("\\+"); 
    		start_mileage1.setText(mileages[0]);
    		end_mileage1.setText(mileages[0]);
    		start_mileage2.setText(mileages[1]);
    		end_mileage2.setText(mileages[1]);
    	} 
    	search_start_ibt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				click_event = CLICK_START_MILEAGE_EVENT;
				Intent intent = new Intent(CraftFileTransferActivity.this, MileageGridViewActivity.class);
				intent.putExtra("project_name", proj_name);
				intent.putExtra("subproject_name", eng_name);
				startActivity(intent);
			}
		});
		search_end_ibt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				click_event = CLICK_END_MILEAGE_EVENT;
				Intent intent = new Intent(CraftFileTransferActivity.this, MileageGridViewActivity.class);
				intent.putExtra("project_name", proj_name);
				intent.putExtra("subproject_name", eng_name);
				startActivity(intent);
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
				
				boolean start_mileage_format = checkMileage1Format(start_mileage1.getText().toString()) &&
						(start_mileage2.getText().toString()).matches("^[0-9]+(\\.[0-9]+)?$");
				boolean end_mileage_format = checkMileage1Format(end_mileage1.getText().toString()) &&
						(end_mileage2.getText().toString()).matches("^[0-9]+(\\.[0-9]+)?$");
				
				if (start_mileage_format && end_mileage_format) {
					if (mProjectPointBase.openDb(project_id, subproject_id)) {
						double start_section_metre = Double.parseDouble(start_mileage1.getText().toString().substring(1)) * 1000
								+ Double.parseDouble(start_mileage2.getText().toString());
						double end_section_metre = Double.parseDouble(end_mileage1.getText().toString().substring(1)) * 1000
								+ Double.parseDouble(end_mileage2.getText().toString());
						int mileage_start_orderno = mProjectPointBase.getMileageOrderNoWhereSectonMetre(start_section_metre);
						int mileage_end_orderno = mProjectPointBase.getMileageOrderNoWhereSectonMetre(end_section_metre);
						int anchor_start_orderno = mProjectPointBase.getAnchorStartOrderNoWhereSectionMetre(start_section_metre);
						int anchor_end_orderno = mProjectPointBase.getAnchorEndOrderNoWhereSectonMetre(end_section_metre);
						int num_startmileage_points = mProjectPointBase.getMileageParameter(mileage_start_orderno).getAnchorCount();
						int num_endmileage_points = mProjectPointBase.getMileageParameter(mileage_end_orderno).getAnchorCount();
						if (anchor_start_orderno == 0) {
							Toast.makeText(context, "数据库中无该起始注浆工段！", Toast.LENGTH_SHORT).show();
						} else if (anchor_end_orderno == 0) {
							Toast.makeText(context, "数据库中无该结束注浆工段！", Toast.LENGTH_SHORT).show();
						} else if (anchor_start_orderno > anchor_end_orderno) {
							Toast.makeText(context, "起始注浆工段不能滞后于结束注浆工段！", Toast.LENGTH_SHORT).show();
						} else if (anchor_start_orderno <= anchor_end_orderno) {
							if (num_startmileage_points == 0) {
								Toast.makeText(context, "检测到起始注浆工段无工点数据！", Toast.LENGTH_SHORT).show();
							} else if (num_endmileage_points == 0) {
								Toast.makeText(context, "检测到结束注浆工段无工点数据！", Toast.LENGTH_SHORT).show();
							} else {
								Intent intent = new Intent(Format.SEND_CONTROL_DATA);
								intent.putExtra("anchor_start_orderno", anchor_start_orderno);
								intent.putExtra("anchor_end_orderno", anchor_end_orderno);
								intent.putExtra("mileage_start_orderno", mileage_start_orderno);
								intent.putExtra("mileage_end_orderno", mileage_end_orderno);
								intent.putExtra("eng_name", eng_name);
								intent.putExtra("proj_name", proj_name);
								send_end_mileage_page = mileage_end_orderno;
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
				UartControlSender.getInstance().stop();
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
	
    private BroadcastReceiver SendParamReceiver = new BroadcastReceiver() {
    	public void onReceive(Context context, Intent intent) {
    		sendStatus = true;
    		String action = intent.getAction();
    		if (action.equals(Format.SUCCESS_SEND_DATA)) {
    			
    		} else if (action.equals(Format.FAILED_SEND_DATA)) {
    			
    		} else if (action.equals(Format.SUCCESS_SEND_SYSTEM_INFO)) {
    			send_status.setText("成功发送合同段、工程名称及系统时间");
    		} else if (action.equals(Format.FAILED_SEND_SYSTEM_INFO)) {
    			send_status.setText("未正确接收到合同段、工程名称及系统时间的应答信号");
    		} else if (action.equals(Format.RECEIVE_SYSTEM_INFO_ACK_TIMEOUT)) {
    			send_status.setText("未接收到合同段、工程名称及系统时间的应答信号");
    		} else if (action.equals(Format.RECEIVE_ANCHOR_PARAM_ACK_TIMEOUT)) {
    			Toast.makeText(context, "接收注浆参数的应答信号超时！", Toast.LENGTH_SHORT).show();
    		} else if (action.equals(Format.DEVICE_CONNECTION_LOST)) {
    			btStatusTitle.setText("通信设备已断开");
    			btConnectStatus = false;
    		} else if (action.equals(Format.RECEIVE_DATA_TIMEOUT)) {
    			sendStatus = true;
    			Toast.makeText(context, "数据接收超时！", Toast.LENGTH_SHORT).show();
    		} else if (action.equals(Format.SUCCESS_CONNECT_INTERNET)) {
    			send_status.setText("已接收到控制面板联网请求");
    		} else if (action.equals(Format.SENDING_PARAM)) {
    			String seq = intent.getStringExtra("seq");
    			send_status.setText("正在传送"+seq+"置筋参数到控制面板");
    		} else if (action.equals(Format.SEND_DATA_FINISH)) {
    			send_status.setText("数据传输结束");
    			mProjectPointBase.updateSendEndMileage(mProjectPointBase.
    					getMileageNameWhereOrderNo(send_end_mileage_page));
    			finish();
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
    
    @Override
    protected void onDestroy() {
    	context.unregisterReceiver(SendParamReceiver);
    	super.onDestroy();
    	MyActivityManager.removeActivity(CraftFileTransferActivity.this);
    }
    
    public void onBackPressed() {
		if (btConnectStatus) {
			AlertDialog.Builder dialog = new  AlertDialog.Builder(CraftFileTransferActivity.this);    
			dialog.setTitle("警告" );
			dialog.setIcon(android.R.drawable.ic_dialog_info);
			dialog.setMessage("退出将导致数据传输中断，下一次传输数据需重启控制器，是否继续?" );  
			dialog.setNegativeButton("取消", null);
			dialog.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
//						context.sendBroadcast(new Intent(Format.EXIT_PARAM_SET_ACTIVITY));
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
}
