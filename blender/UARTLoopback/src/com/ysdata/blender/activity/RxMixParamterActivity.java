package com.ysdata.blender.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.ysdata.blender.R;
import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.cloud.util.SharedView;
import com.ysdata.blender.database.ProjectDataBaseAdapter;
import com.ysdata.blender.database.ProjectPointDataBaseAdapter;
import com.ysdata.blender.element.MixCraftParameter;
import com.ysdata.blender.uart.MyActivityManager;
import com.ysdata.blender.uart.UartPanelService;
import com.ysdata.blender.wireless.client.Format;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class RxMixParamterActivity extends Activity{
	
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
	String upload_end_mixdate;
	Activity activity;
	
	int mix_counts = 0;
	int mix_id = 0;
	int pic_length = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rx_tx_mix_data);
		context = this;
		MyActivityManager.addActivity(RxMixParamterActivity.this);
		activity = RxMixParamterActivity.this;
		title_tv = (TextView) findViewById(R.id.title);
		title_tv.setText("�������ݲɼ�");
		dev_connect_status_tv = (TextView) findViewById(R.id.dev_connect_status);
		dev_connect_status_tv.setText(R.string.mix_connected);
		text_status = (TextView) findViewById(R.id.text_status);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		proj_name_tv = (TextView) findViewById(R.id.proj_name_id);
		eng_name_tv = (TextView) findViewById(R.id.eng_name_id);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
		if (mProjectBase.openDb()) {
			mUartPanelService = new UartPanelService(context, mHandler);
			mUartPanelService.setInstance(mUartPanelService);
			SharedView.showProgressBar(activity, "����������������......");
//			mix_counts = 5;
//			ParameterInputDialog();
		} else {
			Toast.makeText(context, "���ݿ��ʧ��", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	private Spinner dlg_eng_spinner;
	private ArrayAdapter<String> eng_adapter;
	private Spinner dlg_start_seq_spinner;
	private Spinner dlg_end_seq_spinner;
	private ArrayAdapter<String> mix_start_seq_adapter;
	private ArrayAdapter<String> mix_end_seq_adapter;
	private TextView end_collect_seq_tv;
	private void ParameterInputDialog() {
		LayoutInflater factory = LayoutInflater.from(RxMixParamterActivity.this);
		final View dialogView = factory.inflate(R.layout.mix_param_collect_dialog, null);
		AlertDialog.Builder dlg = new AlertDialog.Builder(RxMixParamterActivity.this);
    	dlg.setTitle("����Ҫ�ɼ��Ľ������");
    	dlg.setView(dialogView);
    	final Spinner dlg_proj_spinner = (Spinner) dialogView.findViewById(R.id.proj_name_dlg_id);
		dlg_eng_spinner = (Spinner) dialogView.findViewById(R.id.eng_name_dlg_id);
		dlg_start_seq_spinner = (Spinner) dialogView.findViewById(R.id.mix_start_seq_id);
		dlg_end_seq_spinner = (Spinner) dialogView.findViewById(R.id.mix_end_seq_id);
		end_collect_seq_tv = (TextView) dialogView.findViewById(R.id.id_end_collect_mix_seq_tv);
		final ArrayAdapter<String> proj_adapter;
		ArrayList<String> prj_list = new ArrayList<String>();
		final ArrayList<String> eng_list = new ArrayList<String>();
		mProjectBase.getProjectNameList(prj_list);
		int proj_list_size = prj_list.size();
		String[] proj_areas = new String[proj_list_size];
		for (int i = 0; i < proj_list_size; i++) {
			proj_areas[i] = prj_list.get(i);
		}
		proj_adapter = new ArrayAdapter<String>(RxMixParamterActivity.this, android.R.layout.simple_spinner_item, proj_areas);
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
				eng_adapter = new ArrayAdapter<String>(RxMixParamterActivity.this, android.R.layout.simple_spinner_item, eng_areas);
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
				mProjectBase.updateCollectProjectRecord(project_id, subproject_id);
				String[] seq_areas = new String[mix_counts];
				for (int i = 1; i <= mix_counts; i++) {
					seq_areas[i-1] = i+"";
				}
				mix_start_seq_adapter = new ArrayAdapter<String>(RxMixParamterActivity.this, 
						android.R.layout.simple_spinner_item, seq_areas);
				mix_end_seq_adapter = new ArrayAdapter<String>(RxMixParamterActivity.this, 
						android.R.layout.simple_spinner_item, seq_areas);
				mix_start_seq_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mix_end_seq_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				dlg_start_seq_spinner.setAdapter(mix_start_seq_adapter);
				dlg_end_seq_spinner.setAdapter(mix_end_seq_adapter);
				mProjectPointBase.closeDb();
				if (mProjectPointBase.openDb(project_id, subproject_id)) {
					//check last collect record
					int last_id = mProjectPointBase.checkLastMixCollectId();
					end_collect_seq_tv.setText(last_id+"");
					if (mix_counts >= last_id + 1) {
						dlg_start_seq_spinner.setSelection(last_id);
						dlg_end_seq_spinner.setSelection(last_id);
					} else {
						dlg_start_seq_spinner.setSelection(0);
						dlg_end_seq_spinner.setSelection(0);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
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
							int start_seq = Integer.parseInt(dlg_start_seq_spinner.getSelectedItem().toString());
							int end_seq = Integer.parseInt(dlg_end_seq_spinner.getSelectedItem().toString());;
							if (start_seq <= end_seq) {
								proj_name_tv.setText(dlg_proj_name_str);
								eng_name_tv.setText(dlg_eng_name_str);
								CacheManager.setDbProjectId(project_id);
								CacheManager.setDbSubProjectId(subproject_id);
								mUartPanelService.checkEngName(dlg_eng_name_str, start_seq, end_seq);
								try {
									Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
									field.setAccessible(true);
									field.set(dialog, true);// ��mShowing������Ϊfalse����ʾ�Ի����ѹر�
									dialog.dismiss();
								} catch (Exception e) {
									e.printStackTrace();
								}
								dlg_eng_spinner = null;
								eng_adapter = null;
							} else {
								Toast.makeText(context, "��ʼ��Ų��ܴ�����ֹ��ţ�", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(context, "���ݿ��ʧ��", Toast.LENGTH_SHORT).show();
						}
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
			 case Format.HANDLER_REQUEST_INTERNET:
				    status = SUCCESS_CONNECT_INTERNET;
					mix_counts = msg.arg1;
					SharedView.cancelProgressBar();
					if (mix_counts > 0)
					{
						text_status.setText("�����ɹ��������乤������......");
						ParameterInputDialog();
					}
					else {
						Toast.makeText(context, "��⵽��������δ��Ž�������", Toast.LENGTH_LONG).show();
						mUartPanelService.stop();
					}
					break;
					
				case Format.HANDLER_ENG_MATCH:
					text_status.setText("����ƥ��ɹ�");
					break;
					
				case Format.HANDLER_ENG_NOT_MATCH:
					text_status.setText("����ƥ��ʧ��");
					break;
					
				case Format.HANDLER_REQEST_TRANSFER_DATA_SUCCESS:
					text_status.setText("׼����������......");
					break;
					
				case Format.HANDLER_RECV_MIX_DATE:
					status = DATA_TRANSFERING;
					MixCraftParameter mixCraftParameter = (MixCraftParameter) msg.obj;
					mix_id = mixCraftParameter.getId();
					String date = mixCraftParameter.getMixDate() + " " + mixCraftParameter.getStartTime()+
							" ~ " + mixCraftParameter.getEndTime();
					text_status.setText("���ڲɼ���"+mix_id+"�ν������ݣ�ˮ�ұ�:"+mixCraftParameter.getMixRatio()+
							"����������:"+date+"��ˮ������:"+mixCraftParameter.getCementWeight()+"Kg");
					break;
					
				case Format.HANDLER_RECV_GROUP_MIX_PIC_RESULT:
					if (msg.arg1 >= msg.arg2)
						text_status.setText("���ڲɼ���"+mix_id+"�ν���Ч��ͼƬ����,ͼƬ�ܳ���:"+msg.arg1+"byte���Ѳɼ���:"+msg.arg2+"byte");
					else 
						text_status.setText("��"+mix_id+"�ν���Ч��ͼƬ���ݲɼ�ʧ�ܣ��������ݳ���("+msg.arg1+"byte)"+"����ʵ�ʳ���("+msg.arg2+"byte)��");
					break;
					
				case Format.HANDLER_RECV_GROUP_MIX_PIC_SCENE:
					if (msg.arg1 >= msg.arg2)
						text_status.setText("���ڲɼ���"+mix_id+"�ν����ֳ�ͼƬ����,ͼƬ�ܳ���:"+msg.arg1+"byte���Ѳɼ���:"+msg.arg2+"byte");
					else 
						text_status.setText("��"+mix_id+"�ν����ֳ�ͼƬ���ݲɼ�ʧ�ܣ��������ݳ���("+msg.arg1+"byte)"+"����ʵ�ʳ���("+msg.arg2+"byte)��");
					break;
					
				case Format.HANDLER_MIX_COMM_FINISH:
					status = CHECK_SUCCESS;
					text_status.setText("�������ݲɼ����!");
					break;
					
			default:
				break;
			}
		 };
	 };
	 
		@Override
		public void onBackPressed() {
			if (status == DATA_TRANSFERING || status == START_CONNECT || status == SUCCESS_CONNECT
					|| status == SUCCESS_CONNECT_INTERNET) {
					AlertDialog.Builder dialog = new  AlertDialog.Builder(context);    
					dialog.setTitle("��ʾ" );
					dialog.setIcon(android.R.drawable.ic_dialog_info);
					dialog.setMessage("�˳����������ݴ����жϣ���һ�δ������������������ǣ��Ƿ������" );  
					dialog.setNegativeButton("ȡ��", null);
					dialog.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener() {
						
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
    	MyActivityManager.removeActivity(RxMixParamterActivity.this);
    	super.onDestroy();
    }
	
}
