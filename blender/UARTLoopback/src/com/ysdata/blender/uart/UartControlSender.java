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

package com.ysdata.blender.uart;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.database.ProjectPointDataBaseAdapter;
import com.ysdata.blender.wireless.client.BTMESSAGE;
import com.ysdata.blender.wireless.client.Format;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
public class UartControlSender {
	
	private int ServerCmd = 0xff;
	private int SetCmd = 0xff;
	boolean isColse = false;
	String eng_name_g;
	String proj_name_g;
	String mix_ratio_string_g;
	int project_id, subproject_id;
	private boolean requestConn = false;	//控制面板请求联网到平板
	int ReSendCounter = 3;
	int RecvErrorCounter = 3;
	boolean isRecvError = false;
    private boolean isSend = false;
    public static boolean sendStart = false;
    private boolean isStop = false;
    boolean thread_finish = true;
	Context context = null;
	int ACK = 0;
	boolean recv_timeout = false;
	boolean readStop = false;
	boolean loop_flag = false;
	int cut_recv_index = 0;
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
	
    // Debugging
    private static final boolean D = true;

    // Name for the SDP record when creating server socket

    // Member fields
    private ProjectPointDataBaseAdapter mProjectPointBase;
    private FT311UARTDeviceManager uartInterface;
    private ConnectedThread mConnectedThread;
    private int mState;
    private static UartControlSender uartControlSender;
    
    private Handler mHandler;

    /**
     * Constructor. Prepares a new BluetoothChat session.
     * @param context  The UI Activity Context
     * @param handler  A Handler to send messages back to the UI Activity
     */
    public UartControlSender(Context mContext, Handler handler, ProjectPointDataBaseAdapter data_base) {
    	context = mContext;
        uartInterface = FT311UARTDeviceManager.getSingleFT311UARTDeviceManager(mContext);
        mState = BTMESSAGE.STATE_CONNECTED;
        mHandler = handler;
        mProjectPointBase = data_base;
        project_id = CacheManager.getDbProjectId();
        subproject_id = CacheManager.getDbSubProjectId();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Format.SEND_CONTROL_DATA);
        filter.addAction(Format.EXIT_PARAM_SET_ACTIVITY);
        context.registerReceiver(BdReceiver, filter);
    }
    
    public void setInstance(UartControlSender uSender)
    {
    	uartControlSender = uSender;
    }
    
    public static UartControlSender getInstance()
    {
    	return uartControlSender;
    }
    
    private BroadcastReceiver BdReceiver = new BroadcastReceiver() {
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (action.equals(Format.SEND_CONTROL_DATA)) {
    			eng_name_g = intent.getStringExtra("eng_name");
				proj_name_g = intent.getStringExtra("proj_name");
				mix_ratio_string_g = intent.getStringExtra("mix_ratio_string");
    			
				AppUtil.log( "=======SEND_CONTROL_DATA=====");
				sendStart = true;
				
		        mConnectedThread = new ConnectedThread();
		        mConnectedThread.start();
    		}
    	};
    };
	
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
        globleInit();
        
//        mConnectedThread = new ConnectedThread();
//        mConnectedThread.start();
    }

    public synchronized void reset() {
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
//        isStop = true;
        if (mConnectedThread != null) 
        {
        	if (loop_flag) 
        	{
        		mConnectedThread.cancel(); 
        		while (loop_flag);
        	}
        	mConnectedThread = null;
        }
        globleInit();
//        if (mSendControlThread != null) {mSendControlThread.cancel(); mSendControlThread = null;}
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
     * Indicate that the connection attempt failed and notify the UI Activity.
     */   
    private void connectionFailed() {
//        setState(BTMESSAGE.STATE_LISTEN);
        reset();
        // Send a failure message back to the Activity
//        Message msg = mHandler.obtainMessage(BTMESSAGE.MESSAGE_TOAST);
//        Bundle bundle = new Bundle();
//        bundle.putString(BTMESSAGE.TOAST, "Unable to connect device");
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);
    }

    private void uartPrint(byte flag){
    	byte[] SendBuf = new byte[6];
    	SendBuf[0] = 0x77;
    	SendBuf[1] = 0x77;
    	SendBuf[2] = 0x77;
    	SendBuf[3] = 0x77;
    	SendBuf[4] = 0x77;
    	SendBuf[5] = flag;
    	uartInterface.WriteData(SendBuf.length, SendBuf);
    }
    
    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
//        setState(BTMESSAGE.STATE_LISTEN);
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

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    
    
    
    private void sendMixCraftData() {
    	AppUtil.log( "------sendMixCraftData------");
    	SimpleDateFormat formatter = new SimpleDateFormat   ("yyyy-MM-dd-HH-mm");     
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间     
		String str = formatter.format(curDate);    
		AppUtil.log( str);
		String []strs = str.split("-");
		int year = Integer.parseInt(strs[0]);
		int month = Integer.parseInt(strs[1]);
		int day = Integer.parseInt(strs[2]);
		int hour = Integer.parseInt(strs[3]);
		int minute = Integer.parseInt(strs[4]);
		String eng_name = eng_name_g;
    	
    	int index = 0;
    	int length = 0;
    	byte[] SendBuf = new byte[1024];
    	SendBuf[0] = (byte) Format.PANEL_HEAD;
		SendBuf[1] = (byte) Format.PANEL_BREAK;
		SendBuf[2] = Format.CMD_MIX_CRAFT_DATA;
		index = 5;
		SendBuf[index++] = (byte) (year >> 8);
		SendBuf[index++] = (byte) (year & 0xff);
		SendBuf[index++] = (byte) month;
		SendBuf[index++] = (byte) day;
		SendBuf[index++] = (byte) hour;
		SendBuf[index++] = (byte) minute;
		try {
			byte[] mixRatio_byte;
			mixRatio_byte = mix_ratio_string_g.getBytes("GBK");
			length = mix_ratio_string_g.length();
			SendBuf[index++] = (byte) length;
			for (int i = 0; i < length; i++) {
				SendBuf[index++] = mixRatio_byte[i];
			}
			byte[] engName_byte;
			engName_byte = eng_name.getBytes("GBK");
			length = engName_byte.length;
			SendBuf[index++] = (byte) length;
			for (int i = 0; i < length; i++) {
				SendBuf[index++] = engName_byte[i];
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		SendBuf[3] = (byte) ((index - 5) >> 8);
		SendBuf[4] = (byte) ((index - 5) & 0xff);
		
		int check_sum = getCrcCheckSum(index, SendBuf);
		
		SendBuf[index] = (byte) ((check_sum >> 8) & 0xff);
		SendBuf[index+1] = (byte) (check_sum & 0xff);
		/*for (int i = 0; i < index+2; i++) {
			AppUtil.log((SendBuf[i]&0xff)+ "");
		}*/
		uartInterface.WriteData(index+2, SendBuf);
    }
    
    

/*    private void sendStopComm() {
    	byte[] SendBuf = new byte[6];
    	
		SendBuf[0] = (byte) Format.PANEL_HEAD;
		SendBuf[1] = Format.PANEL_BREAK;
		
	  	SendBuf[2] = Format.CMD_COMM_STOP;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 0;
	  	SendBuf[5] = Format.CMD_COMM_STOP;
	  	AppUtil.log( "stop comm to pannel");
	  	uartInterface.WriteData(SendBuf.length, SendBuf);
	  	SetCmd = Format.CMD_COMM_STOP;
    }*/
    
    private void sendStopComm() {
    	byte[] SendBuf = new byte[7];
    	int check_sum = 0;
    	
		SendBuf[0] = (byte) Format.PANEL_HEAD;
		SendBuf[1] = (byte) Format.PANEL_BREAK;
		
	  	SendBuf[2] = Format.CMD_COMM_STOP;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 0;
	  	check_sum = getCrcCheckSum(5, SendBuf);
	  	SendBuf[5] = (byte) ((check_sum >> 8) & 0xff);
	  	SendBuf[6] = (byte) (check_sum & 0xff);
	  	AppUtil.log( "stop comm to pannel");
	  	uartInterface.WriteData(SendBuf.length, SendBuf);
	  	SetCmd = Format.CMD_COMM_STOP;
    }
    
    
    private void sendConnectAck() {
    	
    	byte[] SendBuf = new byte[7];
    	int check_sum = 0;
    	
		SendBuf[0] = (byte) Format.PANEL_HEAD;
		SendBuf[1] = (byte) Format.PANEL_BREAK;

	  	SendBuf[2] = Format.CMD_MIX_REQUEST_INTERNET;
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
    
    class ServerAck implements Runnable {
    	
    	@Override
    	public void run() {
    		WaitServerACK(10000);
    	}
    }
    
    private boolean WaitServerACK(int time_out) {
    	recv_timeout = false; //清除超时状态
    	if (ServerCmd != 0xff) ServerCmd = 0xff; //初始化ServerCmd，防止未接收到服务端的值立即返回
    	ExecutorService executor = Executors.newSingleThreadExecutor();
    	Future<Integer> future = executor.submit(
    	   new Callable<Integer>() {
    	       @Override
			public Integer call() {
//    	    	   while(ServerCmd == 0xff) {
    	    	   AppUtil.log( "WaitServerACK-----SetCmd:"+SetCmd+ " ServerCmd:"+ServerCmd);
    	    	   while(ServerCmd != SetCmd) {
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
    	if (ServerCmd == SetCmd) {
    		AppUtil.log( "recv ack. cmd:" + ServerCmd);
    		SetCmd = 0xff;
    		ServerCmd = 0xff;
    		recv_timeout = false;
    		return true;
    	}
    	SetCmd = 0xff;
    	ServerCmd = 0xff;
    	return false;
    }
    
    /*private boolean dataCheck(int[] data, int length) {
    	int data_head = data[0];
    	int data_break = data[1];
    	int cmd = data[2];
    	int data_length = (data[3] << 8) + data[4];
    	int check_sum = 0;
    	AppUtil.log( "data check start: cmd " + cmd);
    	if ((data_head == Format.PANEL_HEAD) && (data_break == Format.PANEL_BREAK)) {
    		if (cmd > 8) {
    			AppUtil.log( "cmd check failed");
    			return false;
    		}
    		
    		if (data_length != length - 6) {
    			AppUtil.log( "data len check failed. data_full_len:"+length+"  data_len:"+data_length);
    			length = data_length+6;
    		}
    		
    		for (int i = 2; i < length - 1; i++) {
    			check_sum += data[i];
    		}
    		check_sum &= 0xff;
    		
    		if (check_sum != data[length-1]) {
    			AppUtil.log( "check_sum check failed");
    			return false;
    		}
    		
    		AppUtil.log( "data check success.");
    		return true;
    	} else {
    		return false;
    	}
    }*/
    
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
    	AppUtil.log("_dataCheck========recvData:");
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
    		if (cmd > 8 || (SetCmd != 0xff && SetCmd != cmd)) {
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
    		return true;
    	} else {
    		return false;
    	}
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
			
			requestConn = false;	//控制面板请求联网到平板
			isRecvError = false;
			ACK = 1;
			
			while (!isStop) {
				if (!loop_flag) loop_flag = true;
				if (isSend) {
					ReSendCounter = 3;
					AppUtil.log( "wait send...");
					//1、等待传输里程参数的输入
					//2、等待数据发送线程运行结束
					while (!sendStart || !thread_finish) {
						if (isStop) {
							break;
						}
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
//						CheckBtState(); //add 20150827 for testing bt state
					}
					if (isStop) {
						break;
					}
					AppUtil.log( "start send...");
					sendStart = false;
					isSend = false;
					AppUtil.log( "thread ---- ready");
						new Thread() {
							@Override
							public void run() {
								thread_finish = false;
								AppUtil.log( "thread ---- run");
								while (ReSendCounter > 0) {
									AppUtil.log( "thread---ReSendCounter:"+ReSendCounter);
									sendMixCraftData();
									SetCmd = Format.CMD_MIX_CRAFT_DATA;
									AppUtil.log( "WaitServerACK---start");
									if (WaitServerACK(10000) && (ACK == 1)) {
										AppUtil.log( "recv date in time.");
										ACK = 0;
										ReSendCounter = 3;
										break;
									} else {
										ReSendCounter--;
										if (ACK == 0) {
											AppUtil.log( "recv date nack.");
										} else {
											AppUtil.log( "recv date timeout---ReSendCounter:"+ReSendCounter);
										}
										if (ReSendCounter <= 0) {
											AppUtil.log( "data recv timeout.");
											connectionFailed();
											context.sendBroadcast(new Intent(Format.RECEIVE_MIX_CRAFT_ACK_TIMEOUT));
											break;
										}
									}
								}
								thread_finish = true;
							};
						}.start();
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
//						AppUtil.log( Integer.toHexString(recv_data[i]).toString());
					}
					data_head = recv_data[0];
					data_break = recv_data[1];
					cmd = recv_data[2];
					if ((data_head != Format.PANEL_HEAD) || (data_break != Format.PANEL_BREAK)) {
						continue;
					}
					cut_recv_index = 0;
					if(!dataCheck(recv_data, recv_length)) {
						AppUtil.log("crc check failed.");
						if (!requestConn) {
							AppUtil.log( "receive request connect internt error.");
							continue;      //若未接收到联网请求，继续接收
						}
						
						/*if (requestComm) {
							RecvErrorCounter--;						//连续3次接收到校验失败的数据，保存最后一次数据
							AppUtil.log( "data check failed.RecvErrorCounter:"+RecvErrorCounter);
							sendNAck(cmd, mmOutStream);
						}*/
						if (RecvErrorCounter >= 0) {
							continue;
						} else {
							RecvErrorCounter = 3;
							isRecvError = true;				//接收到错误数据并保存
							AppUtil.log( "save error data.");
						}
					} else {
						/*for (int i = 0; i < recv_length; i++) {
							AppUtil.log( Integer.toHexString(recv_data[i]).toString());
						}*/
						AppUtil.log( "crc check success.");
						cmd = recv_data[cut_recv_index+2];
						isRecvError = false;
					}
					
					if (!requestConn) {
						if (cmd == Format.CMD_MIX_REQUEST_INTERNET) { //接收控制面板联网到平板请求
							if (ACK != 1) ACK = 1;
							AppUtil.log( "recv CMD_PANEL_REQUEST_INTERNET");
							context.sendBroadcast(new Intent(Format.SUCCESS_CONNECT_INTERNET));
//							uartPrint((byte) 1);
							sendConnectAck();  //应答控制面板联网
							try {
								Thread.sleep(100);	//在应答控制面板联网及请求控制面板传输数据间加入延时，防止服务器接收数据混乱
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							isSend = true;
							requestConn = true; 
						}
					} else {
						if (!recv_timeout) {  //在数据发送超时后与下一次数据发送前接收到的应答不做处理，防止连续处理多个应答信号
							if (cmd == Format.CMD_MIX_CRAFT_DATA) {
								AppUtil.log( "recv CMD_CRAFT_DATA");
								if (recv_data[cut_recv_index+5] == 1) {
									AppUtil.log( "pad send mix craft data to panel success.");
									context.sendBroadcast(new Intent(Format.SUCCESS_SEND_MIX_CRAFT_DATA));
									isSend = true;
									sendStart = true;
									ACK = 1;
								} else {
									AppUtil.log( "pad send mix craft data to panel failed.");
									context.sendBroadcast(new Intent(Format.FAILED_SEND_MIX_CRAFT_DATA));
//								isSend = true; //如果接收到NACK，程序继续接收面板数据  comment by wxd 20150912
									ACK = 0;
								}
//								isSystemInfo = false;  //comment by wxd 20150912
								ServerCmd = Format.CMD_MIX_CRAFT_DATA;
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								requestConn = false;
								isSend = false;
								sendStart = false;
								ACK = 1;
								break;
							}
						}
					}
				}
			}
			loop_flag = false;
			AppUtil.log( "while break.");	
        }
        
        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void cancel() {
        	if (SetCmd != 0xff) {
        		SetCmd = ServerCmd = 0xff;
        		ReSendCounter = 0;
        	}
        	readStop = true;
        	isStop = true;
        }
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
    	requestConn = false;
		isSend = false;
		sendStart = false;
		ACK = 1;
    }
    
    private void globleInit() {
    	requestConn = false;	//控制面板请求联网到平板
    	ReSendCounter = 3;
    	RecvErrorCounter = 3;
    	isRecvError = false;
        isSend = false;
        sendStart = false;
        isStop = false;
        readStop = false;
        ACK = 1;
    }
}

