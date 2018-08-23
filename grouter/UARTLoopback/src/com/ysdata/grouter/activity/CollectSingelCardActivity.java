package com.ysdata.grouter.activity;

import com.ysdata.grouter.R;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
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
import android.widget.TextView;

public class CollectSingelCardActivity extends Activity{
	
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
	private ProjectPointDataBaseAdapter mProjectPointBase;
	int project_id;
	int subproject_id;
	String anchor_seq;
	String eng_name;
	String proj_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transfer_status_phone);
		context = this;
		MyActivityManager.addActivity(CollectSingelCardActivity.this);
		text_status = (TextView) findViewById(R.id.text_status);
		status_title = (TextView) findViewById(R.id.recv_status_title);
		bt_connect_status = (TextView) findViewById(R.id.bt_connect_status);
		status_title.setText("数据采集->单元式定点数据存储器->采样状态");
		bt_connect_status.setText("蓝牙已连接");
		text_status.setText("正在等待单元式定点数据存储器的联网......");
		
		proj_name_tv = (TextView) findViewById(R.id.proj_name_id);
		eng_name_tv = (TextView) findViewById(R.id.eng_name_id);
		anchor_seq_tv = (TextView) findViewById(R.id.anchor_seq_id);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
        mProjectPointBase.closeDb();
//		title_name.setText(proj_name+"->"+eng_name);
		registerIntent();
	}
	
    @Override
    protected void onDestroy() {
    	unregisterReceiver(updateReceiver);
    	if (mProjectPointBase != null) {
    		mProjectPointBase.closeDb();
    	}
    	ProjectDataBaseAdapter mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
    	if (mProjectBase != null) {
    		mProjectBase.closeDb();
    	}
    	super.onDestroy();
    	MyActivityManager.removeActivity(CollectSingelCardActivity.this);
    }
	
	@Override
	public void onBackPressed() {
		if (status == DATA_TRANSFERING || status == START_CONNECT || status == SUCCESS_CONNECT) {
//			Toast.makeText(getApplicationContext(), "数据通信中，禁止任何操作！",
//				     Toast.LENGTH_SHORT).show();
			AlertDialog.Builder dialog = new  AlertDialog.Builder(context);    
			dialog.setTitle("提示" );
			dialog.setIcon(android.R.drawable.ic_dialog_info);
			dialog.setMessage("退出将导致数据传输中断,是否继续？" );  
			dialog.setNegativeButton("取消", null);
			dialog.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					finish();
				}
			} );   
			dialog.create();
			dialog.show();
		} else {
			if (status != DEVICE_LOST) {
				AlertDialog.Builder dialog = new  AlertDialog.Builder(context);    
				dialog.setTitle("提示" );
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setMessage("退出将断开蓝牙连接，是否继续？" );  
				dialog.setNegativeButton("取消", null);
				dialog.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						finish();
					}
				} );   
				dialog.create();
				dialog.show();
			} else {
				finish();
			}
		}
	};
	
    public void registerIntent() {
    	IntentFilter updateFilter = new IntentFilter();
    	updateFilter.addAction(Format.CHECK_SUCCESS);
    	updateFilter.addAction(Format.CHECK_FAILED);
    	updateFilter.addAction(Format.DEVICE_CONNECTION_LOST);
    	updateFilter.addAction(Format.SUCCESS_CONNECT_INTERNET);
    	updateFilter.addAction(Format.REQUEST_TRANSFER_DATA);
    	updateFilter.addAction(Format.CARD_ENG_NOT_MATCH);
    	updateFilter.addAction(Format.CARD_PROJ_NOT_MATCH);
    	updateFilter.addAction(Format.PROJ_ENG_MATCH);
    	context.registerReceiver(updateReceiver, updateFilter);
    }
    
    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
    	@Override
		public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (action.equals(Format.SUCCESS_CONNECT_INTERNET)) {
    			status = SUCCESS_CONNECT_INTERNET;
    			text_status.setText("联网成功，请求传输数据......");
    			AppUtil.log("SUCCESS_CONNECT_INTERNET");
    		} else if (action.equals(Format.CHECK_SUCCESS)) {
    			status = CHECK_SUCCESS;
    			text_status.setText("数据接收完成，校验成功！");
    			AppUtil.log( "CHECK_SUCCESS");
    			alertDialog();
    		} else if (action.equals(Format.CHECK_FAILED)) {
    			status = CHECK_FAILED;
    			text_status.setText("数据接收完成，部分数据校验失败！");
    			AppUtil.log( "CHECK_FAILED");
    		} else if (action.equals(Format.DEVICE_CONNECTION_LOST)) {
    			if (status != CHECK_SUCCESS) {
    				text_status.setText("通信异常，数据未接收完成！");
    			}
    			status = DEVICE_LOST;
    			bt_connect_status.setText("蓝牙已断开");
    			AppUtil.log( "DEVICE_CONNECTION_LOST");
    		} else if (action.equals(Format.REQUEST_TRANSFER_DATA)) {
    			text_status.setText("准备接受数据......");
    		} else if (action.equals(Format.CARD_PROJ_NOT_MATCH)) {
    			text_status.setText("数据库中不存在\""+intent.getStringExtra("proj_name")+"\"此合同段");
    		} else if (action.equals(Format.CARD_ENG_NOT_MATCH)) {
    			text_status.setText("在\""+intent.getStringExtra(proj_name)+"\"合同段下不存在\""+intent.getStringExtra("proj_name")+"\"工程");
    		} else if (action.equals(Format.PROJ_ENG_MATCH)) {
    			text_status.setText("正在接收数据，请勿断开与单元式定点数据存储器的连接......");
    			String proj_name_extra = intent.getStringExtra("proj_name");
    			String eng_name_extra = intent.getStringExtra("eng_name");
    			String anchor_seq_extra = intent.getStringExtra("anchor_seq");
    			proj_name_tv.setText(proj_name_extra);
    			eng_name_tv.setText(eng_name_extra);
    			anchor_seq_tv.setText(anchor_seq_extra);
    		}
    	};
    };
    
    private void alertDialog() {
    	AlertDialog.Builder dialog = new AlertDialog.Builder(CollectSingelCardActivity.this);    
		dialog.setTitle("提示" );
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setMessage("该单元存储器数据已采集完毕，是否继续下一个?" );  
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				context.sendBroadcast(new Intent(Format.CONTINUE_COLLECT_CARD_CANCEL));
			}
		} );   
		dialog.setPositiveButton("继续",  new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				context.sendBroadcast(new Intent(Format.CONTINUE_COLLECT_CARD_NEXT));
			}
		} );   
		dialog.setCancelable(false);
		dialog.create();
		dialog.show();
    }
}
