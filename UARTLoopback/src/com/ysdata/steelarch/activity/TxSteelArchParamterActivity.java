package com.ysdata.steelarch.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.cloud.util.AppUtil;
import com.ysdata.steelarch.cloud.util.CacheManager;
import com.ysdata.steelarch.database.ProjectDataBaseAdapter;
import com.ysdata.steelarch.database.ProjectPointDataBaseAdapter;
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

public class TxSteelArchParamterActivity extends Activity{
	
	private Context context;
	private TextView text_status;
	private TextView proj_name_tv;
	private TextView eng_name_tv;
	TextView dev_connect_status_tv;
	int project_id;
	int subproject_id;
	String proj_name_string;
	String eng_name_string;
	private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	private UartControlSender uartControlSender;
	private static final int CLICK_START_STEELARCH_EVENT = 1;
	private static final int CLICK_END_STEELARCH_EVENT = 2;
	int click_event = 0;
	
	private boolean sendStatus = false;
	private boolean devConnectStatus = true;
	int tx_end_steelarch_orderno = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rx_tx_mix_data);
		context = this;
		MyActivityManager.addActivity(TxSteelArchParamterActivity.this);
		dev_connect_status_tv = (TextView) findViewById(R.id.dev_connect_status);
		dev_connect_status_tv.setText(R.string.mix_connected);
		text_status = (TextView) findViewById(R.id.text_status);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		proj_name_tv = (TextView) findViewById(R.id.proj_name_id);
		eng_name_tv = (TextView) findViewById(R.id.eng_name_id);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
		
		 IntentFilter filter = new IntentFilter();
	        filter.addAction(Format.SUCCESS_SEND_DATA);
	        filter.addAction(Format.FAILED_SEND_DATA);
	        filter.addAction(Format.DEVICE_CONNECTION_LOST);
	        filter.addAction(Format.RECEIVE_DATA_TIMEOUT);
	        filter.addAction(Format.RECEIVE_SYSTEM_INFO_ACK_TIMEOUT);
	        filter.addAction(Format.RECEIVE_STEELARCH_PARAM_ACK_TIMEOUT);
	        filter.addAction(Format.SUCCESS_CONNECT_INTERNET);
	        filter.addAction(Format.SENDING_PARAM);
	        filter.addAction(Format.SEND_DATA_FINISH);
	        filter.addAction(Format.SUCCESS_SEND_SYSTEM_INFO);
	        filter.addAction(Format.FAILED_SEND_SYSTEM_INFO);
	        filter.addAction(Format.ACTION_SEND_MILEAGE_NAME);
	        registerReceiver(SendParamReceiver, filter);
	        
	        text_status.setText("���ڼ��ֹ��ܲ������������......");
			
		if (mProjectBase.openDb()) {
			ParameterInputDialog();
		} else {
			Toast.makeText(context, "���ݿ��ʧ��", Toast.LENGTH_SHORT).show();
		}
	}
	
    private BroadcastReceiver SendParamReceiver = new BroadcastReceiver() {
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (action.equals(Format.SUCCESS_SEND_DATA)) {
    			
    		} else if (action.equals(Format.FAILED_SEND_DATA)) {
    			sendStatus = false;
    		} else if (action.equals(Format.SUCCESS_SEND_SYSTEM_INFO)) {
    			text_status.setText("�ɹ����͹������Ƽ�ϵͳʱ��");
    		} else if (action.equals(Format.FAILED_SEND_SYSTEM_INFO)) {
    			text_status.setText("���յ��������Ƽ�ϵͳʱ��ķ�Ӧ���ź�");
    			sendStatus = false;
    		} else if (action.equals(Format.RECEIVE_SYSTEM_INFO_ACK_TIMEOUT)) {
    			text_status.setText("δ���յ��������Ƽ�ϵͳʱ���Ӧ���ź�");
    			sendStatus = false;
    		} else if (action.equals(Format.RECEIVE_STEELARCH_PARAM_ACK_TIMEOUT)) {
    			Toast.makeText(context, "���ոֹ��ܲ�����Ӧ���źų�ʱ��", Toast.LENGTH_SHORT).show();
    			sendStatus = false;
    		} else if (action.equals(Format.DEVICE_CONNECTION_LOST)) {
    			dev_connect_status_tv.setText("ͨ���豸�ѶϿ�");
    			devConnectStatus = false;
    			sendStatus = false;
    		} else if (action.equals(Format.RECEIVE_DATA_TIMEOUT)) {
    			sendStatus = false;
    			Toast.makeText(context, "���ݽ��ճ�ʱ��", Toast.LENGTH_SHORT).show();
    		} else if (action.equals(Format.SUCCESS_CONNECT_INTERNET)) {
    			text_status.setText("�ѽ��յ����������������");
    		} else if (action.equals(Format.SENDING_PARAM)) {
    			String name = intent.getStringExtra("name");
    			int orderno = intent.getIntExtra("orderno", 0);	
				text_status.setText("���ڴ��͸ֹ��ܲ����������������:" + orderno + ",���:" + name);
    		} else if (action.equals(Format.SEND_DATA_FINISH)) {
    			text_status.setText("���ݴ������");
    			mProjectPointBase.updateSendEndSteelArch(tx_end_steelarch_orderno);
    			sendStatus = false;
    		} else if (action.equals(Format.ACTION_SEND_MILEAGE_NAME)) {
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
    	};
    };
	
	private Spinner dlg_eng_spinner;
	private ArrayAdapter<String> eng_adapter;
	EditText start_mileage1;
	EditText start_mileage2;
	EditText end_mileage1;
	EditText end_mileage2;
	private void ParameterInputDialog() {
		LayoutInflater factory = LayoutInflater.from(TxSteelArchParamterActivity.this);
		final View dialogView = factory.inflate(R.layout.panel_mileage_param_dialog, null);
		dialogView.findViewById(R.id.grout_priority_ly).setVisibility(View.GONE);
		AlertDialog.Builder dlg = new AlertDialog.Builder(TxSteelArchParamterActivity.this);
    	dlg.setTitle("ѡ��Ҫ�������ֹ�ֹ���");
    	dlg.setView(dialogView);
    	final Spinner dlg_proj_spinner = (Spinner) dialogView.findViewById(R.id.proj_name_dlg_id);
    	final ImageButton search_start_ibt = (ImageButton) dialogView.findViewById(R.id.search_start_mileage_id);
    	final ImageButton search_end_ibt = (ImageButton) dialogView.findViewById(R.id.search_end_mileage_id);
    	TextView record_tv = (TextView) dialogView.findViewById(R.id.record_tv_id);
    	record_tv.setText("�ϴδ���Ľ����ֹ���:");
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
		proj_adapter = new ArrayAdapter<String>(TxSteelArchParamterActivity.this, android.R.layout.simple_spinner_item, proj_areas);
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
				eng_adapter = new ArrayAdapter<String>(TxSteelArchParamterActivity.this, android.R.layout.simple_spinner_item, eng_areas);
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
						Intent intent = new Intent(TxSteelArchParamterActivity.this, SteelArchGridViewActivity.class);
						intent.putExtra("project_name", project_name);
						intent.putExtra("subproject_name", subproject_name);
						startActivity(intent);
					} else {
						Toast.makeText(context, "��Ч�Ĺ���", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(context, "��Ч�ĺ�ͬ��", Toast.LENGTH_SHORT).show();
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
						Intent intent = new Intent(TxSteelArchParamterActivity.this, SteelArchGridViewActivity.class);
						intent.putExtra("project_name", project_name);
						intent.putExtra("subproject_name", subproject_name);
						startActivity(intent);
					} else {
						Toast.makeText(context, "��Ч�Ĺ���", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(context, "��Ч�ĺ�ͬ��", Toast.LENGTH_SHORT).show();
				}
			}
		});
    	dlg.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				try {
					Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, false);// ��mShowing������Ϊfalse����ʾ�Ի����ѹر�
					dialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				String dlg_proj_name_str = null;
				if (dlg_proj_spinner.getSelectedItem() != null) {
					dlg_proj_name_str = dlg_proj_spinner.getSelectedItem().toString();
				} else {
					Toast.makeText(context, "��ͬ�β���Ϊ�գ�������ѡ��", Toast.LENGTH_SHORT).show();
					return;
				}
				
				String dlg_eng_name_str = null;
				if (dlg_eng_spinner.getSelectedItem() != null) {
					dlg_eng_name_str = dlg_eng_spinner.getSelectedItem().toString();
				} else {
					Toast.makeText(context, "���̲���Ϊ�գ�������ѡ��", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(context, "���ݿ����޸ú�ͬ������", Toast.LENGTH_SHORT).show();
					} else {
						subproject_id = mProjectBase.getSubProjectId(dlg_eng_name_str);
						if (subproject_id == -1) {
							Toast.makeText(context, "����ѡ�����", Toast.LENGTH_SHORT).show();
						} else {
							mProjectPointBase.closeDb();
							if (mProjectPointBase.openDb(project_id, subproject_id)) {
								int steelarch_start_orderno = mProjectPointBase.getSteelArchOrderNoWhereNameMetre(start_section_metre);
								int steelarch_end_orderno = mProjectPointBase.getSteelArchOrderNoWhereNameMetre(end_section_metre);
								if (steelarch_start_orderno == 0) {
									Toast.makeText(context, "���ݿ����޸���ʼ�ֹ��ܣ�", Toast.LENGTH_SHORT).show();
								} else if (steelarch_end_orderno == 0) {
									Toast.makeText(context, "���ݿ����޸ý����ֹ��ܣ�", Toast.LENGTH_SHORT).show();
								} else if (steelarch_start_orderno > steelarch_end_orderno) {
									Toast.makeText(context, "��ʼ�ֹ��ܲ����ͺ��ڽ����ֹ��ܣ�", Toast.LENGTH_SHORT).show();
								} else if (steelarch_start_orderno <= steelarch_end_orderno) {
									dlg_eng_spinner = null;
									eng_adapter = null;
									proj_name_string = dlg_proj_name_str;
									eng_name_string = dlg_eng_name_str;
									
									proj_name_tv.setText(proj_name_string);
									eng_name_tv.setText(eng_name_string);
									
									CacheManager.setDbProjectId(project_id);
									CacheManager.setDbSubProjectId(subproject_id);
									uartControlSender = new UartControlSender(context, mHandler, mProjectPointBase);
									uartControlSender.setInstance(uartControlSender);
									uartControlSender.start();
									
									sendStatus = true;
									
									Intent intent = new Intent(Format.SEND_CONTROL_DATA);
									intent.putExtra("steelarch_start_orderno", steelarch_start_orderno);
									intent.putExtra("steelarch_end_orderno", steelarch_end_orderno);
									intent.putExtra("eng_name", dlg_eng_name_str);
									intent.putExtra("proj_name", dlg_proj_name_str);
									tx_end_steelarch_orderno = steelarch_end_orderno;
									context.sendBroadcast(intent);
									try {
										Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
										field.setAccessible(true);
										field.set(dialog, true);// ��mShowing������Ϊfalse����ʾ�Ի����ѹر�
										dialog.dismiss();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								Toast.makeText(context, "���ݿ��ʧ��", Toast.LENGTH_SHORT).show();
							}
						}
					}
				} else {
					if (!start_mileage_format) {
						Toast.makeText(context, "��ʼ�ֹ������Ƹ�ʽ������󣬲ο���ʽ��k12+1.5", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, "�����ֹ������Ƹ�ʽ������󣬲ο���ʽ��k12+1.5", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
    	
    	dlg.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);// ��mShowing������Ϊfalse����ʾ�Ի����ѹر�
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
	 
    public void onBackPressed() {
		if (sendStatus) {
			AlertDialog.Builder dialog = new  AlertDialog.Builder(TxSteelArchParamterActivity.this);    
			dialog.setTitle("����" );
			dialog.setIcon(android.R.drawable.ic_dialog_info);
			dialog.setMessage("���ڴ������ݣ��˳����������ݴ����жϣ��Ƿ����?" );  
			dialog.setNegativeButton("ȡ��", null);
			dialog.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener() {
				
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
    	MyActivityManager.removeActivity(TxSteelArchParamterActivity.this);
    	super.onDestroy();
    }
	
}
