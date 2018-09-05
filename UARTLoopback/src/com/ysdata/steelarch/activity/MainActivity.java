package com.ysdata.steelarch.activity;

import java.util.ArrayList;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.cloud.util.AppUtil;
import com.ysdata.steelarch.cloud.util.UIUtilities;
import com.ysdata.steelarch.database.ProjectDataBaseAdapter;
import com.ysdata.steelarch.database.ProjectPointDataBaseAdapter;
import com.ysdata.steelarch.database.SyncTimeDataBaseAdapter;
import com.ysdata.steelarch.file.LogFileManager;
import com.ysdata.steelarch.service.RemoteDataService;
import com.ysdata.steelarch.storage.ExtSdCheck;
import com.ysdata.steelarch.uart.FT311UARTDeviceManager;
import com.ysdata.steelarch.uart.MyActivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity  extends Activity {
    private Context context = null;
    private ImageButton param_set_mb;
    private ImageButton data_collect_mb;
    private ImageButton data_manager_mb;
    private ImageButton manual_mb;
    private ImageButton admin_mb;
    private TextView main_title_tv;
    /* declare a FT311 UART interface variable */
	private final static int UART_NOT_CONNECT = 1;
	private final static int UART_CONNECTED = 2;
	private int uart_status;
	boolean threadStop = false;
	UsbAccessoryThread usbAccessoryThread;
	public FT311UARTDeviceManager uartInterface;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_phone);
        context = this;
        MyActivityManager.addActivity(MainActivity.this);
        initView();
        extSdCheck();
        ProjectPointDataBaseAdapter mDataBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
    	ProjectDataBaseAdapter mProjDataBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
    	SyncTimeDataBaseAdapter synctimebase = SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context);
    	if (mDataBase != null)
    		mDataBase.closeDb();
    	if (mProjDataBase != null)
    		mProjDataBase.closeDb();
    	if (synctimebase != null)
    		synctimebase.closeDb();
    	
//    	dataBaseSelfTest();
        uartInterface = FT311UARTDeviceManager.getSingleFT311UARTDeviceManager(context);
        LogFileManager.getInstance().create();
        if (uartInterface.accessory_attached) {
    		uart_status = UART_CONNECTED;
    	} else {
    		uart_status = UART_NOT_CONNECT;
    		usbAccessoryThread = new UsbAccessoryThread();
    		usbAccessoryThread.start();
    	}
    }
    
    class UsbAccessoryThread extends Thread {
    	public void run() {
			int retry = 0;
			while(retry++ < 10) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (uartInterface.accessory_attached) {
					uart_status = UART_CONNECTED;
					break;
				}
				if (threadStop) break;
			}
		};
    }
    
    private void dataBaseSelfTest() {
    	
    }
    
    private void extSdCheck() {
    	ExtSdCheck.ExtSdCard = null;
    	String extSdPath = ExtSdCheck.getExtSDCardPath();
    	if (extSdPath != null) {
    		/*new  AlertDialog.Builder(MainActivity.this)    
            .setTitle("提示" )
            .setIcon(android.R.drawable.ic_dialog_info)
            .setMessage("在使用该软件的过程中，请勿移除sd卡，否则部分功能将无法正常运行!" )  
            .setPositiveButton("确定" ,  null )   
            .create()
            .show();*/
    	} else {
    		new  AlertDialog.Builder(MainActivity.this)    
            .setTitle("提示" )
            .setIcon(android.R.drawable.ic_dialog_info)
            .setMessage("未检测到外置sd卡，部分功能将无法使用!" )  
            .setPositiveButton("确定" ,  null )   
            .create()
            .show();   
    	}
    }
    
    private void initView() {
    	main_title_tv = (TextView) findViewById(R.id.main_title_id);
    	param_set_mb = (ImageButton) findViewById(R.id.param_set_id);
        data_collect_mb = (ImageButton) findViewById(R.id.data_collect_id);
        data_manager_mb = (ImageButton) findViewById(R.id.data_manager_id);
        manual_mb = (ImageButton) findViewById(R.id.manual_id);
        admin_mb = (ImageButton) findViewById(R.id.admin_id);
        main_title_tv.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				AppUtil.log("========main title long click======");
				LayoutInflater factory = LayoutInflater.from(MainActivity.this);
				final View dialogView = factory.inflate(R.layout.version_info_dialog, null);
	        	AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
	        	dlg.setView(dialogView);
	        	dlg.create();
	        	dlg.show();
				return true;
			}
        });
        param_set_mb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String extSdPath = ExtSdCheck.getExtSDCardPath();
				if (extSdPath == null) {
					Toast.makeText(context, "未检测到外置sd卡", Toast.LENGTH_SHORT).show();
				} else {
//					uart_status = UART_CONNECTED;
					if (uart_status == UART_CONNECTED) {
						Intent intent=new Intent(MainActivity.this, TxSteelArchParamterActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(context, "未检测到钢拱架测距仪", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
        data_collect_mb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String extSdPath = ExtSdCheck.getExtSDCardPath();
				if (extSdPath == null) {
					Toast.makeText(context, "未检测到外置sd卡", Toast.LENGTH_SHORT).show();
				} else {
//					uart_status = UART_CONNECTED;
					if (uart_status == UART_CONNECTED) {
						Intent intent=new Intent(MainActivity.this, RxSteelArchParamterActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(context, "未检测到钢拱架测距仪", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
        data_manager_mb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String extSdPath = ExtSdCheck.getExtSDCardPath();
				if (extSdPath == null) {
					Toast.makeText(context, "未检测到外置sd卡", Toast.LENGTH_SHORT).show();
				} else {
					ProjectDataBaseAdapter mProjectBase = 
							ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
					ArrayList<String> proj_list = new ArrayList<String>();
					if (mProjectBase.openDb()) {
						mProjectBase.getProjectNameList(proj_list);
						mProjectBase.closeDb();
						if (proj_list.size() > 0) {
							Intent intent = new Intent(MainActivity.this, ProjectTreeListView.class);
							intent.putExtra("action", "manager_list");
							startActivity(intent);
						} else {
							Toast.makeText(context, "还未创建任何合同段。", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
        manual_mb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ManualActivity.class);
        		startActivity(intent);
			}
		});
        admin_mb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String extSdPath = ExtSdCheck.getExtSDCardPath();
				if (extSdPath == null) {
					Toast.makeText(context, "未检测到外置sd卡", Toast.LENGTH_SHORT).show();
				} else {
					RemoteDataService remoteDataService = RemoteDataService.getRemotDataInstance();
					if (remoteDataService.isNetworkConnected(context)) {
						Intent admin_intent=new Intent(MainActivity.this, AccountActivity.class);
						startActivity(admin_intent);
					} else {
						UIUtilities.showToast(MainActivity.this, R.string.network_not_detected);
					}
				}	
			}
		});
    }
    
	@Override
	protected void onResume() {
		// Ideally should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onResume();	
		if (!uartInterface.accessory_attached)
		{
			uartInterface.ResumeAccessory();
		}
	}
    
    @Override
    protected void onDestroy() {
    	ProjectPointDataBaseAdapter mDataBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
    	ProjectDataBaseAdapter mProjDataBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
    	SyncTimeDataBaseAdapter synctimebase = SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context);
    	if (mDataBase != null)
    		mDataBase.closeDb();
    	if (mProjDataBase != null)
    		mProjDataBase.closeDb();
    	if (synctimebase != null)
    		synctimebase.closeDb();
    	LogFileManager.close();
    	if (usbAccessoryThread.isAlive()) {
    		threadStop = true;
    		try {
				usbAccessoryThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	uartInterface.DestroyAccessory(true);
    	MyActivityManager.removeActivity(MainActivity.this);
    	super.onDestroy();
    }
}
