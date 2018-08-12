/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ysdata.steelarch.uart;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ysdata.steelarch.cloud.util.AppUtil;
import com.ysdata.steelarch.cloud.util.CacheManager;
import com.ysdata.steelarch.database.ProjectDataBaseAdapter;
import com.ysdata.steelarch.database.ProjectPointDataBaseAdapter;
import com.ysdata.steelarch.wireless.client.BTMESSAGE;
import com.ysdata.steelarch.wireless.client.Format;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class UartPanelService {
	
	private int ServerCmd = 0xff;
	private int SetCmd = 0xff;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	private ProjectDataBaseAdapter mProjectBase;
	boolean isColse = false;
	private boolean requestConn = false;	//控制面板请求联网到平板
	private boolean requestComm = false;	//请求控制面板传输数据
	private boolean requestPrjEng = false;
	private boolean waitPrjEngAckOK = false;
	private boolean waitComAckOK = false;
	private boolean isPrjEngMatch = false;
	private boolean isStop = false;
	int ReSendCounter = 3;
	int RecvErrorCounter = 3;
	boolean isRecvError = false;
	Context context = null;
	private boolean sendStart = false;
	
	private int steelarch_start_orderno = 0;
	private int steelarch_end_orderno = 0;
	
    String eng_name_g;
	String proj_name_g;
	int proj_val;
	int eng_val;
	int start_seq_g = 0;
	int end_seq_g = 0;
	int steelarch_orderno_g = 0;
	boolean isLeft = true;
	int pic_size_index = 0;
	StringBuffer pic_data_sb = new StringBuffer();
	
    boolean thread_finish = true;
//	int ACK = 0;
	boolean recv_timeout = false;
	boolean readStop = false;
	boolean loop_flag = false;
	int cut_recv_index = 0;
	
    // Debugging
    private static final boolean D = true;

    // Member fields
    private Handler mHandler;
    private ConnectedThread mConnectedThread;
    private FT311UARTDeviceManager uartInterface;
    private static UartPanelService uartPanelService;
    private int mState;
	private static final int CRCTABH[]={
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 
        0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 
        0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 
        0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 
        0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 
        0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 
        0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 
        0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 
        0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 
        0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 
        0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 
        0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 
        0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 
        0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 
        0x80, 0x41, 0x00, 0xC1, 0x81, 0x40
    };
	private static final int CRCTABL[]={     
		    0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06, 
	        0x07, 0xC7, 0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD, 
	        0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09, 
	        0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A, 
	        0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14, 0xD4, 
	        0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3, 
	        0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3, 
	        0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4, 
	        0x3C, 0xFC, 0xFD, 0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A, 
	        0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9, 0x29, 
	        0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED, 
	        0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26, 
	        0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60, 
	        0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67, 
	        0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F, 
	        0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9, 0xA8, 0x68, 
	        0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E, 
	        0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5, 
	        0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71, 
	        0x70, 0xB0, 0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92, 
	        0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54, 0x9C, 0x5C, 
	        0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B, 
	        0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89, 0x4B, 0x8B, 
	        0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C, 
	        0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42, 
	        0x43, 0x83, 0x41, 0x81, 0x80, 0x40
	  }; 

    /**
     * Constructor. Prepares a new BluetoothChat session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
     */
    public UartPanelService(Context mContext, Handler handler) {
        context = mContext;
        uartInterface = FT311UARTDeviceManager.getSingleFT311UARTDeviceManager(mContext);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		mProjectBase.openDb();
	        mHandler = handler;
		 IntentFilter filter = new IntentFilter();
	        filter.addAction(Format.RECV_PANEL_DATA);
	        context.registerReceiver(BdReceiver, filter);
    }

    private BroadcastReceiver BdReceiver = new BroadcastReceiver() {
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (action.equals(Format.RECV_PANEL_DATA)) {
    			steelarch_start_orderno = intent.getIntExtra("steelarch_start_orderno", 0);
    			steelarch_end_orderno = intent.getIntExtra("steelarch_end_orderno", 0);
    			eng_name_g = intent.getStringExtra("eng_name");
    			proj_name_g = intent.getStringExtra("proj_name");
    			AppUtil.log( "steelarch_start_orderno:"+steelarch_start_orderno + " steelarch_end_orderno:"+steelarch_end_orderno);
    			
    			if (steelarch_end_orderno >= steelarch_start_orderno && steelarch_start_orderno > 0) {
    				AppUtil.log( "=======RECV_PANEL_DATA=====");
//    				sendStart = true;
//    				requestPrjEng = false;
//    				requestComm = false;
//    				waitPrjEngAckOK = false;
//    				waitComAckOK = false;
//    				
//    				mConnectedThread = new ConnectedThread();
//    		        mConnectedThread.start();
    				startService();
    			}
    		}
    	};
    };	
    
    
    public void setInstance(UartPanelService uPanel)
    {
    	uartPanelService = uPanel;
    }
    
    public static UartPanelService getInstance()
    {
    	return uartPanelService;
    }
    
    private void startService() {
		requestPrjEng = false;
		requestComm = false;
		waitPrjEngAckOK = false;
		waitComAckOK = false;
		
		mConnectedThread = new ConnectedThread();
        mConnectedThread.start();
    }
    
    private int getCrcCheckSum(int length, byte[] data) {
    	int crch,crcl,index;
    	
    	crch = 0xff;
    	crcl = 0xff;
    	
    	for (int i = 2; i < length; i++) {
    		index = (crch^data[i]) & 0xff;
    		crch = (crcl^CRCTABH[index]) & 0xff;
    		crcl = CRCTABL[index];
    	}
    	
    	return (crch << 8) | crcl;
    }
    
    private int getCrcCheckSum(int length, int[] data) {
    	int crch,crcl,index;
    	
    	crch = 0xff;
    	crcl = 0xff;
    	
    	for (int i = 2; i < length; i++) {
    		index = (crch^data[i]) & 0xff;
    		crch = (crcl^CRCTABH[index]) & 0xff;
    		crcl = CRCTABL[index];
    	}
    	
    	return (crch << 8) | crcl;
    }
    
    public void setActivityParam(Context mContext, Handler handler) {
    	context = mContext;
    	mHandler = handler;
    }
    
    /**
     * Set the current state of the chat connection
     * @param state  An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        if (D) AppUtil.log( "setState() " + mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(BTMESSAGE.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Return the current connection state. */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume() */
    public synchronized void start() {
    	 if (D) AppUtil.log( "start");
         // Cancel any thread attempting to make a connection
         if (mConnectedThread != null) 
         {
         	if (loop_flag) 
         	{
         		mConnectedThread.cancel(); 
         		while (loop_flag);
         	}
         	mConnectedThread = null;
         }
         globalInit();
         
//         mConnectedThread = new ConnectedThread();
//         mConnectedThread.start();
    }

    private void globalInit() {
    	if (D) AppUtil.log("globalInit");
    	requestConn = false;	//控制面板请求联网到平板
    	requestComm = false;	//请求控制面板传输数据
    	requestPrjEng = false;
    	isPrjEngMatch = false;
    	sendStart = false;
    	isStop = false;
    	readStop = false;
    	ReSendCounter = 3;
    	RecvErrorCounter = 3;
    	isRecvError = false;
    	waitPrjEngAckOK = false;
		waitComAckOK = false;
    }
    
    private void reset() {
        if (D) AppUtil.log( "reset");
    	isStop = true;
    	if (mConnectedThread != null) 
        {
        	if (loop_flag) 
        	{
        		mConnectedThread.cancel(); 
        		while (loop_flag);
        	}
        	mConnectedThread = null;
        }
    }
    
    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (D) AppUtil.log( "stop");
//      isStop = true;
      if (mConnectedThread != null) 
      {
      	if (loop_flag) 
      	{
      		mConnectedThread.cancel(); 
      		while (loop_flag);
      	}
      	mConnectedThread = null;
      }
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        setState(BTMESSAGE.STATE_LISTEN);
        reset();
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(BTMESSAGE.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(BTMESSAGE.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        //setState(STATE_LISTEN);
    	reset();
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(BTMESSAGE.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(BTMESSAGE.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        setState(BTMESSAGE.STATE_NONE);
        context.sendBroadcast(new Intent(Format.DEVICE_CONNECTION_LOST));
    }

    /*private void RequestComm(OutputStream writer) {
    	byte[] SendBuf = new byte[6];
	
		SendBuf[0] = (byte) Format.PANEL_HEAD;
		SendBuf[1] = Format.PANEL_BREAK;
		
	  	SendBuf[2] = Format.CMD_MIX_REQUEST_COMM_SEQ;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 0;
	  	SendBuf[5] = Format.CMD_MIX_REQUEST_COMM_SEQ;
	  	AppUtil.log( "RequestComm");
	  	try {
			writer.write(SendBuf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	  	SetCmd = Format.CMD_MIX_REQUEST_COMM_SEQ;
    }*/
    
    private void RequestComm() {
    	int check_sum = 0;
    	byte[] SendBuf = new byte[13];
		SendBuf[0] = (byte) Format.PANEL_HEAD;
		SendBuf[1] = (byte) Format.PANEL_BREAK;
		
	  	SendBuf[2] = Format.CMD_STEELARCH_REQUEST_COMM_SEQ;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 4;
	  	SendBuf[5] = (byte) (steelarch_start_orderno >> 8);
	  	SendBuf[6] = (byte) (steelarch_start_orderno & 0xff);
	  	SendBuf[7] = (byte) (steelarch_end_orderno >> 8);
	  	SendBuf[8] = (byte) (steelarch_end_orderno & 0xff);
	  	
	  	check_sum = getCrcCheckSum(9, SendBuf);
	  	
	  	SendBuf[9] = (byte) ((check_sum >> 8) & 0xff);
		SendBuf[10] = (byte) (check_sum & 0xff);
	  	
	  	AppUtil.log( "RequestComm----start_seq_g:"+start_seq_g + " end_seq_g:"+end_seq_g);
	  	uartInterface.WriteData(SendBuf.length, SendBuf);
	  	SetCmd = Format.CMD_STEELARCH_REQUEST_COMM_SEQ;
    }
    
    private void RequestRecvPrjEngName() {
		byte[] SendBuf = new byte[7];
		int check_sum = 0;
	
		SendBuf[0] = (byte) Format.PANEL_HEAD;
		SendBuf[1] = (byte) Format.PANEL_BREAK;
		
	  	SendBuf[2] = Format.CMD_STEELARCH_REQUEST_ENG;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 0;
	  	
	  	check_sum = getCrcCheckSum(5, SendBuf);
	  	SendBuf[5] = (byte) ((check_sum >> 8) & 0xff);
	  	SendBuf[6] = (byte) (check_sum & 0xff);
	  	AppUtil.log( "RequestRecvPrjEngName");
	  	uartInterface.WriteData(SendBuf.length, SendBuf);
	  	SetCmd = Format.CMD_STEELARCH_REQUEST_ENG;
    }
    
    private void sendAck(int cmd) {
    	
    	byte[] SendBuf = new byte[8];
    	int check_sum = 0;
    	
		SendBuf[0] = (byte) Format.PANEL_HEAD;
		SendBuf[1] = (byte) Format.PANEL_BREAK;

	  	SendBuf[2] = (byte) cmd;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 1;
	  	SendBuf[5] = 1;
	  	check_sum = getCrcCheckSum(6, SendBuf);
	  	SendBuf[6] = (byte) ((check_sum >> 8) & 0xff);
	  	SendBuf[7] = (byte) (check_sum & 0xff);
	  	AppUtil.log( "send ack === cmd:" + cmd);
	  	/*try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}*/
	  	uartInterface.WriteData(SendBuf.length, SendBuf);  	
    }
    
    private void uartPrint(byte flag) {
    	byte[] SendBuf = new byte[6];
    	SendBuf[0] = 0x77;
    	SendBuf[1] = 0x77;
    	SendBuf[2] = 0x77;
    	SendBuf[3] = 0x77;
    	SendBuf[4] = 0x77;
    	SendBuf[5] = flag;
    	uartInterface.WriteData(SendBuf.length, SendBuf);
    }
    
    private void sendConnectAck() {
    	
    	byte[] SendBuf = new byte[7];
    	int check_sum = 0;
    	
		SendBuf[0] = (byte) Format.PANEL_HEAD;
		SendBuf[1] = (byte) Format.PANEL_BREAK;

	  	SendBuf[2] = Format.CMD_PANEL_REQUEST_INTERNET;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 0;
	  	
	  	check_sum = getCrcCheckSum(5, SendBuf);
	  	SendBuf[5] = (byte) ((check_sum >> 8) & 0xff);
	  	SendBuf[6] = (byte) (check_sum & 0xff);
	  	AppUtil.log( "sendConnectAck");
	  	try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	  	uartInterface.WriteData(SendBuf.length, SendBuf);	
    }
    
    private void sendNAck(int cmd) {
    	
    	byte[] SendBuf = new byte[8];
    	int check_sum = 0;
    	
		SendBuf[0] = (byte) Format.PANEL_HEAD;
		SendBuf[1] = (byte) Format.PANEL_BREAK;
		
	  	SendBuf[2] = (byte) cmd;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 1;
	  	SendBuf[5] = 0;
	  	
	  	check_sum = getCrcCheckSum(6, SendBuf);
	  	SendBuf[6] = (byte) ((check_sum >> 8) & 0xff);
	  	SendBuf[7] = (byte) (check_sum & 0xff);
	  	AppUtil.log( "send nack === cmd:" + cmd);
	  	try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	  	uartInterface.WriteData(SendBuf.length, SendBuf);	
    }
    
    class ServerAck implements Runnable {
    	
    	@Override
    	public void run() {
    		WaitServerACK(10000);
    	}
    }
    
    private boolean WaitServerACK(int time_out) {
    	recv_timeout = false; //清除超时状态
    	if (ServerCmd != 0xff) ServerCmd = 0xff; //初始化ServerCmd，防止未接收到服务端的值立即返回，修改短时间内连续执行RequestComm的bug
    	ExecutorService executor = Executors.newSingleThreadExecutor();
    	Future<Integer> future = executor.submit(
    	   new Callable<Integer>() {
    	       @Override
			public Integer call() {
    	    	   while(ServerCmd == 0xff) {
    	    		   try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
    	    	   } // if true -- wait
    	    	   return ServerCmd;
    	   }});
    	
    	try {
    		future.get(time_out, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			future.cancel(true);  
		} catch (ExecutionException e) {
			future.cancel(true);  
		} catch (TimeoutException e) {
			future.cancel(true); 
			AppUtil.log( "WaitServerACK==========Timeout");
			recv_timeout = true; 
		}
    	executor.shutdown();
    	AppUtil.log( "ServerCmd:"+ServerCmd+" SetCmd:"+SetCmd);
    	if (ServerCmd == SetCmd) {
    		AppUtil.log( "recv ack. cmd:" + ServerCmd);
    		switch (ServerCmd) {
    		case Format.CMD_STEELARCH_REQUEST_ENG:
    			AppUtil.log( "WaitServerACK:Format.CMD_STEELARCH_REQUEST_ENG");
    			break;
    		case Format.CMD_STEELARCH_REQUEST_COMM_SEQ:
    			AppUtil.log( "WaitServerACK:Format.CMD_STEELARCH_REQUEST_COMM_SEQ");
    			break;
    		}
    		SetCmd = 0xff;
    		ServerCmd = 0xff;
    		recv_timeout = false;
    		return true;
    	}
    	SetCmd = 0xff;
    	ServerCmd = 0xff;
    	return false;
    }
    
    private boolean dataCheck(int[] data, int length) {
    	int[] _data = new int[1024];
    	int index = 0;
    	int data_length = 0;
    	int start_point = 0;
    	while(index < length) {
    		if ((data[index++] == Format.PANEL_HEAD) && (data[index++] == Format.PANEL_BREAK)) {
    			start_point = index - 2;
    			cut_recv_index = start_point;
    			data_length =  (data[start_point+3] << 8) + data[start_point+4];
    			_data = Arrays.copyOfRange(data, start_point, length);
    			if (_dataCheck(_data, length-start_point)) {
    				return true;
    			}
    			index += data_length + 5;
    			continue;
    		}
    	} 
    	return false;
    }
    
    private boolean _dataCheck(int[] data, int length) {
    	AppUtil.log("_dataCheck==============recvData:");
    	for (int i = 0; i < length; i++) {
    		AppUtil.log(Integer.toHexString(data[i]).toString());
    	}
    	int data_head = data[0];
    	int data_break = data[1];
    	int cmd = data[2];
    	int data_length = (data[3] << 8) + data[4];
    	int check_sum = 0;
    	AppUtil.log( "data check start: cmd " + cmd);
    	if ((data_head == Format.PANEL_HEAD) && (data_break == Format.PANEL_BREAK)) {
    		if (SetCmd != 0xff && SetCmd != cmd) {
    			AppUtil.log( "cmd check failed");
    			return false;
    		}
    		
    		if (data_length != length - 7) {
    			AppUtil.log( "data len check failed. data_full_len:"+length+"  data_len:"+data_length);
    			length = data_length+7;
    		}
    		
    		check_sum = getCrcCheckSum(length - 2, data);
    		
    		if (check_sum != ((data[length-2] << 8) | data[length-1])) {
    			AppUtil.log( "check_sum check failed");
    			return false;
    		}
    		
    		AppUtil.log( "data check success.");
    		return true;
    	} else {
    		return false;
    	}
    }
    
    private float IntToPoint(int data) {
    	if (data < 10)
    		return (float)data/10;
    	else if (data < 100)
    		return (float)data/100;
    	else if (data < 1000)
    		return (float)data/1000;
    	return 0;
    }
    
    private void CheckBtState() {
    	byte[] SendBuf = new byte[6];
    	
		SendBuf[0] = 35;
		SendBuf[1] = 41;
		
	  	SendBuf[2] = 1;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 0;
	  	SendBuf[5] = 1;
	  	AppUtil.log( "CheckBtState");
	  	uartInterface.WriteData(SendBuf.length, SendBuf);
    }
    
    public void aterDilogCancel() {
    	if (mConnectedThread != null) 
        {
        	if (loop_flag) 
        	{
        		mConnectedThread.cancel(); 
        		while (loop_flag);
        	}
        	mConnectedThread = null;
        }
//	    requestConn = false;
//		requestPrjEng = false;
//		waitComAckOK = false;
//		waitPrjEngAckOK = false;
//		sendStart = false;
    }
    
    public void checkEngName(String eng_name, int start_seq, int end_seq) {
    	eng_name_g = eng_name;
    	sendStart = true;
    	start_seq_g = start_seq;
    	end_seq_g = end_seq;
    }
	
    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {

        public ConnectedThread() {
            AppUtil.log( "create ConnectedThread");
        }

        public void run() {
            AppUtil.log( "BEGIN mConnectedThread");
            byte[] recv_temp = new byte[2048];
			int[] recv_data = new int[2048];
			int cmd = 0;
			int data_head = 0;
			int data_break = 0;
			int recv_length = 0;
			int nreadLen = 0;
			int data_len = 0;
			boolean isSend = false;
			
			requestConn = false;	//控制面板请求联网到平板
			requestComm = false;	//请求控制面板传输数据
			ReSendCounter = 3;
			RecvErrorCounter = 3;
			isRecvError = false;
			
//          int bytes;
            
			while (!isStop) {
				if (!loop_flag) loop_flag = true;
				if (isSend) {
					ReSendCounter = 3;
					if (requestConn) {
						if (!requestPrjEng) {		
							AppUtil.log( "wait send...");
							while (!sendStart || !thread_finish) {
								if (isStop) {
									break;
								}
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
//								CheckBtState();
							}
							if (isStop) {
								break;
							}
							AppUtil.log( "start send...");
							sendStart = false;
							isSend = false;
							AppUtil.log( "thread ---- ready");
							new Thread(){
								@Override
								public void run() {
									thread_finish = false;
									AppUtil.log( "thread ---- run");
									//	sendComm = true;
									while (ReSendCounter > 0) {
										AppUtil.log( "thread---ReSendCounter:"+ReSendCounter);
										RequestRecvPrjEngName();//应答控制面板联网后，请求控制面板传数据
										SetCmd = Format.CMD_STEELARCH_REQUEST_ENG;
										if (WaitServerACK(10000)) {
											AppUtil.log( "recv request_prj_eng ack.");
											waitPrjEngAckOK = true;
											ReSendCounter = 3;
											break;
										} else {
											AppUtil.log( "recv request_prj_eng ack timeout 10s.");
											ReSendCounter--;
										}
									}
									thread_finish = true;
								};
							}.start();
						} else {
							if (!requestComm && isPrjEngMatch) {
								AppUtil.log( "isSend====="+isSend);
								isSend = false;
								new Thread(){
									@Override
									public void run() {
										//							sendComm = true;
										while (ReSendCounter > 0) {
											RequestComm();//应答控制面板联网后，请求控制面板传数据
											SetCmd = Format.CMD_STEELARCH_REQUEST_COMM_SEQ;
											AppUtil.log( "WaitServerACK---start");
											if (WaitServerACK(10000)) {
												AppUtil.log( "recv date in time.");
												waitComAckOK = true;
												ReSendCounter = 3;
												break;
											} else {
												ReSendCounter--;
												AppUtil.log( "recv date timeout---ReSendCounter:"+ReSendCounter);
											}
										}
										thread_finish = true;
									};
								}.start();
							}
						}
					}
				} else {
					int max_recv_len = 2048;
					int recv_offset = 0;
					for (int i = 0; i < 2048; i++) {
						recv_temp[i] = 0;
						recv_data[i] = 0;
					}
					int status;
					int[] actualNumBytes = new int[1];
					while (recv_offset < max_recv_len) {
						if(readStop) break;
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
						}
						status = uartInterface.ReadData(max_recv_len, recv_temp, actualNumBytes);
						if (status == 0x00 && actualNumBytes[0] > 0) {
							nreadLen = actualNumBytes[0];
//							uartInterface.WriteData(actualNumBytes[0], recv_temp); //uart test
							AppUtil.log( "nreadLen:" + nreadLen);
							recv_offset = recv_offset + nreadLen;
							if (recv_offset >= 5) {
								int len_high = (recv_temp[3] & 0xff) << 8;
								int len_low = recv_temp[4] & 0xff;
//								int read_len = len_high + len_low + 6;
								int read_len = len_high + len_low + 7; //crc8->crc16
								
								if (recv_offset >= read_len) {
									AppUtil.log( "read_data:" + read_len);
									break;
								}
							}
						}
					}
					if(readStop) break;
					recv_length = recv_offset;
					AppUtil.log( "recv_length:" + recv_length);
					for (int i = 0; i < recv_length; i++) {
						recv_data[i] = recv_temp[i] & 0xff;
//						AppUtil.log( Integer.toHexString(recv_data[i])+" ");
					}
					data_head = recv_data[0];
					data_break = recv_data[1];
					cmd = recv_data[2];
//					data_len = recv_length - 6;
					data_len = (recv_data[3] << 8) + recv_data[4];
					
					if ((data_head != Format.PANEL_HEAD) || (data_break != Format.PANEL_BREAK)) {
						continue;
					}
					cut_recv_index = 0;
					if(!dataCheck(recv_data, recv_length)) {
						
						if (!requestConn) {
							AppUtil.log( "receive request connect internt error.");
							continue;      //若未接收到联网请求，继续接收
						}
						
						if (requestComm) {
							RecvErrorCounter--;						//连续3次接收到校验失败的数据，结束通信并提示
							AppUtil.log( "data check failed.RecvErrorCounter:"+RecvErrorCounter+ " cmd:"+cmd);
							sendNAck(cmd);
						}
						if (RecvErrorCounter >= 0) {
							continue;
						} else {
							RecvErrorCounter = 3;
							AppUtil.log( "client received invalid data."); 
							context.sendBroadcast(new Intent(Format.CHECK_FAILED));
							break; //不保存错误数据
						}
					} else {
						AppUtil.log( "client received valid data.");  
						cmd = recv_data[cut_recv_index+2];
					}
					
					if (!requestConn) {						//判断是否已应答控制面板联网请求
						if (cmd == Format.CMD_PANEL_REQUEST_INTERNET) {
							AppUtil.log( "============CMD_PANEL_REQUEST_INTERNET=============");
							context.sendBroadcast(new Intent(Format.SUCCESS_CONNECT_INTERNET));
							sendConnectAck();  //应答控制面板联网
							requestConn = true; 
							isSend = true;
							try {
								Thread.sleep(1000);	//在应答控制面板联网及请求控制面板传输数据间加入延时，防止服务器接收数据混乱
								AppUtil.log( "sleep 1000ms");
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {
						if (!requestPrjEng) {
							if (cmd == Format.CMD_STEELARCH_REQUEST_ENG) {
								if (!recv_timeout) { //add by wxd 20150902
									AppUtil.log( "============CMD_STEELARCH_REQUEST_ENG=============");
									ServerCmd = Format.CMD_STEELARCH_REQUEST_ENG; //接收控制面板主工程名称与子工程名称
									for (int i = 0; i < 100; i++) {
										try {
											Thread.sleep(10);  //设置延时使CMD_REQUEST_ACK同步到ServerCmd
										} catch (InterruptedException e) {
											e.printStackTrace();
										}			
										if (waitPrjEngAckOK) {
											requestPrjEng = true;
											waitPrjEngAckOK = false;
											isSend = true;
											break;
										}
									}
									int index = 5 + cut_recv_index;
									int node_len = 0;
									try {
										byte[] engName_byte = new byte[64];
										node_len = recv_data[index++];
										for (int i = 0; i < node_len; i++) {
											engName_byte[i] = (byte)recv_data[index++];
										}
										String eng_name = new String(engName_byte, 0, node_len, "GBK");
										//与输入的项目及工程进行匹配
										if (eng_name.equals(eng_name_g)) {
											isPrjEngMatch = true;
											if (mProjectBase.openDb()) {
												int proj_val_l = CacheManager.getDbProjectId();
												int eng_val_l = CacheManager.getDbSubProjectId();
												proj_val = proj_val_l;
												eng_val = eng_val_l;
												mProjectPointBase.closeDb();
												mProjectPointBase.openDb(proj_val_l, eng_val_l);
												context.sendBroadcast(new Intent(Format.ENG_MATCH));
											} else {
												Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
											}
										} else {
											AppUtil.log("eng_name from mixDev:" + eng_name);
											AppUtil.log("pad eng_name:" + eng_name_g);
											context.sendBroadcast(new Intent(Format.ENG_NOT_MATCH));
										}
									} catch (UnsupportedEncodingException e) {
										e.printStackTrace();
									}
								}
							}
						}
						if (!requestComm) {
							if (cmd == Format.CMD_STEELARCH_REQUEST_COMM_SEQ) {
								if (!recv_timeout) { //add by wxd 20150902
									AppUtil.log( "============CMD_STEELARCH_REQUEST_COMM_SEQ=============");
									ServerCmd = Format.CMD_STEELARCH_REQUEST_COMM_SEQ; //接收控制面板发数据应答
									for (int i = 0; i < 100; i++) {
										try {
											Thread.sleep(10);  //设置延时使CMD_MIX_REQUEST_COMM_SEQ同步到ServerCmd
										} catch (InterruptedException e) {
											e.printStackTrace();
										}			
										if (waitComAckOK) {
											requestComm = true;
											waitComAckOK = false;
											break;
										}
									}
									context.sendBroadcast(new Intent(Format.REQUEST_TRANSFER_DATA_SUCCESS));
								}
							} 
						} else {
							if (cmd == Format.CMD_STEELARCH_DATA) {
								AppUtil.log("======CMD_STEELARCH_DATA=======");
								if (data_len > 0) {
									int index = 5 + cut_recv_index;
									try {
										//搅拌序号
										steelarch_orderno_g = (recv_data[index++] << 16) + (recv_data[index++] << 8) + recv_data[index++];
										AppUtil.log("steelarch_orderno_g:"+steelarch_orderno_g);
										//搅拌日期
										int year = (recv_data[index++] << 8) + recv_data[index++];
										int month = recv_data[index++];
										int day = recv_data[index++];
										int hour = recv_data[index++];
										int min = recv_data[index++];
										
										StringBuffer date = new StringBuffer();
										date.append(year).append('-');
										if (month < 10) {
											date.append(0);
										} 
										date.append(month).append('-');
										
										if (day < 10) {
											date.append(0);
										} 
										date.append(day);
										
										date.append(' ');
										if (hour < 10) {
											date.append(0);
										}
										date.append(hour).append(':');
										if (min < 10) {
											date.append(0);
										}
										date.append(min);
										AppUtil.log("date:"+date.toString());
										
										//钢拱架里程编号
										byte[] name_arr = new byte[64];
										int name_len = recv_data[index++];
										for (int i = 0; i < name_len; i++) {
											name_arr[i] = (byte) recv_data[index++];
										}
										String name_string = "";
										name_string = new String(name_arr, 0, name_len, "GBK");
										AppUtil.log("name:"+name_string);
										
										//钢拱架间距
										byte[] measure_distance_arr = new byte[64];
										int measure_distance_len = recv_data[index++];
//										measure_distance_len = 4;
										for (int i = 0; i < measure_distance_len; i++) {
											measure_distance_arr[i] = (byte) recv_data[index++];
										}
										String measure_distance_string = "";
										measure_distance_string = new String(measure_distance_arr, 0, measure_distance_len, "GBK");
										
										double measure_distance = Double.parseDouble(measure_distance_string);
										AppUtil.log("mmeasure_distance:"+measure_distance);
										
										//掌子面距离
										byte[] tunnelface_distance_arr = new byte[64];
										int tunnelface_distance_len = recv_data[index++];
//										measure_distance_len = 4;
										for (int i = 0; i < tunnelface_distance_len; i++) {
											tunnelface_distance_arr[i] = (byte) recv_data[index++];
										}
										String tunnelface_distance_string = "";
										tunnelface_distance_string = new String(tunnelface_distance_arr, 0, measure_distance_len, "GBK");
										
										double tunnelface_distance = Double.parseDouble(tunnelface_distance_string);
										AppUtil.log("tunnelface_distance:"+tunnelface_distance);										

										//二次模板台车距离
										byte[] secondcar_distance_arr = new byte[64];
										int secondcar_distance_len = recv_data[index++];
//										measure_distance_len = 4;
										for (int i = 0; i < secondcar_distance_len; i++) {
											secondcar_distance_arr[i] = (byte) recv_data[index++];
										}
										String secondcar_distance_string = "";
										secondcar_distance_string = new String(secondcar_distance_arr, 0, secondcar_distance_len, "GBK");
										
										double secondcar_distance = Double.parseDouble(secondcar_distance_string);
										AppUtil.log("secondcar_distance:"+secondcar_distance);		
										
										isLeft = (byte) recv_data[index++] == 0 ? true : false;
										
										sendAck(cmd); //接收到正确数据，发送数据接收应答
										
										Intent intent = new Intent(Format.RECV_STEELARCH_DATA);
										intent.putExtra("steelarch_orderno", steelarch_orderno_g);
										intent.putExtra("date", date.toString());
										intent.putExtra("name", name_string);
										intent.putExtra("isLeft", isLeft);
										intent.putExtra("measure_distance", measure_distance);
										intent.putExtra("tunnelface_distance", tunnelface_distance);
										intent.putExtra("secondcar_distance", secondcar_distance);
										context.sendBroadcast(intent);
										
										if (mProjectPointBase.openDb(proj_val, eng_val)) {
											if (isLeft) {
												mProjectPointBase.insertLeftSteelArchCollectRecord(steelarch_orderno_g, date.toString(), 
														measure_distance, tunnelface_distance, secondcar_distance);
											} else {
												mProjectPointBase.insertRightSteelArchCollectRecord(steelarch_orderno_g, date.toString(), 
														measure_distance, tunnelface_distance, secondcar_distance);
											}
											
										} else {
											Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							} else if (cmd == Format.CMD_STEELARCH_ENTRANCE_PIC) {
								AppUtil.log("======CMD_STEELARCH_ENTRANCE_PIC=======");
								if (data_len > 0) {
									/*for (int i = 0; i < data_len+6; i++) {
										AppUtil.log( "recv_data["+i+"]:" + recv_data[i]);
									}*/
									int index = 5 + cut_recv_index;
									try {
										int pic_total_size = (recv_data[index++] << 8) + recv_data[index++];
										int pic_group_id = recv_data[index++];
										int pic_group_size = (recv_data[index++] << 8) + recv_data[index++];
										if (pic_group_id == 1) {
											pic_size_index = 0;
											pic_data_sb.delete(0, pic_data_sb.length());
										}
										sendAck(cmd); //接收到正确数据，发送数据接收应答
										pic_size_index += pic_group_size;
										AppUtil.log("pic_total_size:"+pic_total_size+" pic_group_id:"+pic_group_id + " pic_size_index:"+pic_size_index);
										Intent intent = new Intent(Format.RECV_ENTRANCE_PIC);
										intent.putExtra("pic_total_size", pic_total_size);
										intent.putExtra("pic_size_index", pic_size_index);
										context.sendBroadcast(intent);
										if (pic_size_index <= pic_total_size) {
											for (int i = 0; i < pic_group_size; i++) {
												pic_data_sb.append(recv_data[index++]).append(' ');
											}
 										} else {
 											break;
 										}
										if (pic_size_index == pic_total_size) {
 											if (mProjectPointBase.openDb(proj_val, eng_val)) {
 												if (isLeft) {
 													mProjectPointBase.updateSteelArchCollectLeftPicDirEntrance(steelarch_orderno_g, pic_data_sb.toString());
 												} else {
 													mProjectPointBase.updateSteelArchCollectRightPicDirEntrance(steelarch_orderno_g, pic_data_sb.toString());
 												}
 											} else {
 												Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
 											}
 											pic_size_index = 0;
 											pic_data_sb.delete(0, pic_data_sb.length());
 										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else if (cmd == Format.CMD_STEELARCH_TUNNELFACE_PIC) {
								AppUtil.log("======CMD_STEELARCH_TUNNELFACE_PIC=======");
								if (data_len > 0) {
									/*for (int i = 0; i < data_len+6; i++) {
										AppUtil.log( "recv_data["+i+"]:" + recv_data[i]);
									}*/
									int index = 5 + cut_recv_index;
									try {
										int pic_total_size = (recv_data[index++] << 8) + recv_data[index++];
										int pic_group_id = recv_data[index++];
										int pic_group_size = (recv_data[index++] << 8) + recv_data[index++];
										AppUtil.log("pic_total_size:"+pic_total_size+" pic_group_id:"+pic_group_id);
										if (pic_group_id == 1) {
											pic_size_index = 0;
											pic_data_sb.delete(0, pic_data_sb.length());
										}
										sendAck(cmd); //接收到正确数据，发送数据接收应答
										pic_size_index += pic_group_size;
										AppUtil.log("pic_total_size:"+pic_total_size+" pic_group_id:"+pic_group_id + " pic_size_index:"+pic_size_index);
										Intent intent = new Intent(Format.RECV_TUNNELFACE_PIC);
										intent.putExtra("pic_total_size", pic_total_size);
										intent.putExtra("pic_size_index", pic_size_index);
										context.sendBroadcast(intent);
										if (pic_size_index <= pic_total_size) {
											for (int i = 0; i < pic_group_size; i++) {
												pic_data_sb.append(recv_data[index++]).append(' ');
											}
 										} else {
 											break;
 										}
										
										if (pic_size_index == pic_total_size) {
 											if (mProjectPointBase.openDb(proj_val, eng_val)) {
 												if (isLeft) {
 													mProjectPointBase.updateSteelArchCollectLeftPicDirTunnelFace(steelarch_orderno_g, pic_data_sb.toString());
 												} else {
 													mProjectPointBase.updateSteelArchCollectRightPicDirTunnelFace(steelarch_orderno_g, pic_data_sb.toString());
 												}
 											} else {
 												Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
 											}
 											pic_size_index = 0;
 											pic_data_sb.delete(0, pic_data_sb.length());
 										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else if (cmd == Format.CMD_STEELARCH_COMM_STOP) {
								AppUtil.log( "recv CMD_RECEV_STOP");
								if (mProjectPointBase.openDb(proj_val, eng_val)) {
									mProjectPointBase.setCollectOrderNo();
								}
								sendAck(cmd); //接收到正确数据，发送数据接收应答
								context.sendBroadcast(new Intent(Format.STEELARCH_COMM_FINISH));
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								isSend = true;
								requestConn = true;
								requestPrjEng = false;
								waitComAckOK = false;
								waitPrjEngAckOK = false;
								sendStart = false;
								break; //add 20161115
							}
						}
					}
				}
				
			}
			loop_flag = false;
            AppUtil.log( "while break.");			
        }

        public void cancel() {
        	if (SetCmd != 0xff) {
        		SetCmd = ServerCmd = 0xff;
        		ReSendCounter = 0;
        	}
        	readStop = true;
        	isStop = true;
        }
    }
    
    
}

