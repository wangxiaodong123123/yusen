package com.ysdata.grouter.activity;

import java.util.Set;

import com.ysdata.grouter.R;
import com.ysdata.grouter.database.SharePrefOperator;
import com.ysdata.grouter.uart.FT311UARTDeviceManager;
import com.ysdata.grouter.uart.MyActivityManager;
import com.ysdata.grouter.uart.UartPanelService;
import com.ysdata.grouter.wireless.client.BTMESSAGE;
import com.ysdata.grouter.wireless.client.BtCardService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceListActivity extends Activity{
	private Context mContext;
	private View m_Array[];
	private ImageView mImageView[];
	private int mIndex = 0;
	SharePrefOperator mSharePref;
	private BluetoothAdapter mBluetoothAdapter;
	private UartPanelService mUartPanelService;
	private BtCardService mBtCardService;
	private Handler handler;
	ProgressDialog progressDialog;
	int timeout_sec = 0;
	private static final String CONTROL_PANEL_BT_NAME = "CONT00001";
//	private static final String CONTROL_PANEL_BT_NAME = "HUAWEI P6-U06";
	private static final String CONTROL_RW_DEVICE_BT_NAME = "READ00001";
//	private static final String CONTROL_PANEL_BT_ADDR = "08:7A:4C:56:78:1B";
	private final static String TAG = "ys200";
	String cur_device = "panel";
	
	private final static int UART_NOT_CONNECT = 1;
	private final static int UART_CONNECTED = 2;
	private int uart_status;
	public FT311UARTDeviceManager uartInterface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.dev_list_phone);
		mContext = this;
		uartInterface = FT311UARTDeviceManager.getSingleFT311UARTDeviceManager(mContext);
		MyActivityManager.addActivity(DeviceListActivity.this);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mUartPanelService = new UartPanelService(this, mHandler);
		mUartPanelService.setInstance(mUartPanelService);
		mBtCardService = new BtCardService(this, mHandler);
		handler = new Handler();
		// If BT is not on, request that it be enabled.
		if (!mBluetoothAdapter.isEnabled()) {
			//�����������豸   
			mBluetoothAdapter.enable();
			
		}
		if (uartInterface.accessory_attached) {
    		uart_status = UART_CONNECTED;
    	} else {
    		uart_status = UART_NOT_CONNECT;
    	}
		initView();
	}
	

	Runnable timeout_r = new Runnable() {
		
		@Override
		public void run() {
			timeout_sec++;
			handler.postDelayed(timeout_r, 1000);
			Log.d(TAG, "timeout_sec:"+timeout_sec);
			if (timeout_sec >= 60) { //wait 10s timeout
				progressDialog.dismiss();
				new  AlertDialog.Builder(mContext)    
				.setTitle("��ʾ" )
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("���ӵ���Ԫʽ�������ݴ洢����ʱ�����鵥Ԫʽ�������ݴ洢���Ƿ�����ƽ���������ӵĹ���״̬��" )  
				.setPositiveButton("ȷ��" ,  null )   
				.create()
				.show(); 
				timeout_sec = 0;
				handler.removeCallbacks(timeout_r);
			}
		}
	};
	
	// The Handler that gets information back from the BtService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case BTMESSAGE.MESSAGE_STATE_CHANGE:
                Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BTMESSAGE.STATE_CONNECTED:
                	Log.d(TAG, "=======connected======");
                	if (progressDialog!=null)
						progressDialog.dismiss();
                	timeout_sec = 0;
                	handler.removeCallbacks(timeout_r);
            		Intent intent=new Intent(mContext, CollectSingelCardActivity.class);
            		startActivity(intent);
                    break;
                case BTMESSAGE.STATE_CONNECTING:
                	Log.d(TAG, "=======connecting======");
                    break;
                case BTMESSAGE.STATE_LISTEN:
                case BTMESSAGE.STATE_NONE:
                	Log.d(TAG, "=======not_connected======");
                    break;
                }
                break;
            case BTMESSAGE.MESSAGE_DEVICE_NAME:
                // save the connected device's name
                
                break;
            case BTMESSAGE.MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(BTMESSAGE.TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	
	
	private void setVisible(int cur_index, int pre_index) {
		if (cur_index == pre_index) {
			return;
		}
		mImageView[pre_index].setVisibility(View.GONE);
		mImageView[cur_index].setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		mUartPanelService.stop();
		mBtCardService.stop();
		Log.d(TAG, "========onRestart========");
		super.onRestart();
	}
	
	@Override
	protected void onDestroy() {
		mUartPanelService.unRegisterReceiver();
		mBtCardService.unRegisterReceiver();
		super.onDestroy();
		MyActivityManager.removeActivity(DeviceListActivity.this);
	}
	
	private void initView() {
        mSharePref = SharePrefOperator.getSingleSharePrefOperator(this);
        TextView title=(TextView)findViewById(R.id.dev_list_title);
        title.setText("���ݲɼ�->ͨ���豸");
		m_Array = new RelativeLayout[2];
		m_Array[0]=findViewById(R.id.panel_relativelayout);
		m_Array[1]=findViewById(R.id.sdcard_relativelayout);

		mImageView = new ImageView[2];
		mImageView[0]=(ImageView)findViewById(R.id.panel_imageview);
		mImageView[1]=(ImageView)findViewById(R.id.sdcard_imageview);
		
		m_Array[0].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVisible(0, mIndex);
//				uart_status = UART_CONNECTED;
		        if (uart_status == UART_CONNECTED) {
					mUartPanelService.start();
					Intent intent=new Intent(mContext, CollectPanelActivity.class);
            		startActivity(intent);
		        } else {
		        	AlertDialog.Builder dialog = new  AlertDialog.Builder(mContext);    
		    		dialog.setTitle("��ʾ" );
		    		dialog.setIcon(android.R.drawable.ic_dialog_info);
		    		dialog.setMessage("δ��������ͨ���豸" );  
		    		dialog.setNegativeButton("ȷ��", null);
				dialog.create();
		    		dialog.show();
		        }
				mIndex = 0;
			}
		});
		
		m_Array[1].setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				setVisible(1, mIndex);
				timeout_sec = 0;
				cur_device = "singel_card";
//				Intent intent=new Intent(mContext, CollectSingelCardActivity.class);
//        		startActivity(intent);
				Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		        //check our device is paired or not.
		        boolean isPaired = false;
		        String bt_adress = "";
		        if (pairedDevices.size() > 0) {
					for (BluetoothDevice device : pairedDevices) {
						String bt_name = device.getName();
						Log.d(TAG, "bt_name:"+bt_name);
						if (bt_name.equals(CONTROL_RW_DEVICE_BT_NAME)) {
							bt_adress = device.getAddress();
							isPaired = true;
							break;
						}
					}
				}
		        if (isPaired) {
		        	progressDialog = ProgressDialog.show(DeviceListActivity.this, 
							"��������", "�������ӵ���Ԫʽ�������ݴ洢������ȴ�...", true, false);  
					handler.post(timeout_r);
		        	if (mBtCardService.getState() == BTMESSAGE.STATE_NONE) {
		        		Log.d(TAG, "bt_address:"+bt_adress);
		        		mBtCardService.setBtDeviceAddress(bt_adress);
						BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(bt_adress);
						mBtCardService.connect(device);
//						mBtCardService.start();
		        	}
		        } else {
		        	AlertDialog.Builder dialog = new  AlertDialog.Builder(mContext);    
		    		dialog.setTitle("��ʾ" );
		    		dialog.setIcon(android.R.drawable.ic_dialog_info);
		    		dialog.setMessage("��δ�뵥Ԫʽ�������ݴ洢������������ԣ��Ƿ�������ԣ�" );  
		    		dialog.setNegativeButton("ȡ��", null);
		    		dialog.setPositiveButton("ȷ��",  new DialogInterface.OnClickListener() {
		    			
		    			@Override
		    			public void onClick(DialogInterface dialog,
		    					int which) {
		    				Intent intent =  new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);  
		    	        	startActivity(intent);
		    			}
		    		} );   
				dialog.create();
		    		dialog.show();
		        }
				mIndex = 1;
			}
		});
	}
}
