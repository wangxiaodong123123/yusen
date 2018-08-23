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

package com.ysdata.grouter.wireless.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.element.AnchorParameter;
import com.ysdata.grouter.element.WrcardParameter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
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
public class BtCardService {
	
	private int ServerCmd = 0xff;
	private int SetCmd = 0xff;
	private Value mValue = null;
	private ProjectPointDataBaseAdapter mProjectPointBase = null;
	boolean isColse = false;
	int proj_val = -1;
	int eng_val = -1;
	private boolean requestConn = false;	//控制面板请求联网到平板
	private boolean requestComm = false;	//请求控制面板传输数据
	private boolean waitConnAckOK = false;
	private boolean waitCommAckOK = false;
	private boolean isSend = true;
	private boolean isStop = false;
    boolean thread_finish = true;
    
	int ReSendCounter = 3;
	int RecvErrorCounter = 3;
	boolean isRecvError = false;
    public static boolean sendStart = true;
	Context context = null;
	private String BtDeviceAddress;
	String db_mileage_distance = "";
	int db_anchor_id = 0;
	String db_anchor_name = "";
	float design_press_g = 0;
	float cap_unit_hour_g = 0;
	
	int ACK = 0;
	boolean recv_timeout = false;
	
    // Debugging
    private static final String TAG = "ys200";
    private static final boolean D = true;

    // Name for the SDP record when creating server socket
    private static final String NAME = "BluetoothComm";

    // Unique UUID for this application
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");	//change by chongqing jinou	
    //    private static final UUID MY_UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    //    private static final UUID MY_UUID = UUID.fromString("97FDE75B-9792-5B8D-703D-12013D54B981");
    // Member fields
    private final BluetoothAdapter mAdapter;
//    SharePrefOperator mSharePref;
    ProjectDataBaseAdapter mProjectBase;
    private Handler mHandler;
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    
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
    public BtCardService(Context mContext, Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        context = mContext;
        mValue = Value.getSingleValue(context);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
//		mSharePref = SharePrefOperator.getSingleSharePrefOperator(context);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
        mState = BTMESSAGE.STATE_NONE;
        mHandler = handler;
        IntentFilter filter = new IntentFilter();
        filter.addAction(Format.CONTINUE_COLLECT_CARD_NEXT);
        filter.addAction(Format.CONTINUE_COLLECT_CARD_CANCEL);
        context.registerReceiver(BdReceiver, filter);
    }
    
    public void unRegisterReceiver() {
    	context.unregisterReceiver(BdReceiver);
    }
	
    private BroadcastReceiver BdReceiver = new BroadcastReceiver() {
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (action.equals(Format.CONTINUE_COLLECT_CARD_NEXT)) {
    			AppUtil.log( "-------CONTINUE_COLLECT_CARD_NEXT-------");
    			sendStart = true;
    			requestConn = false;	//控制面板请求联网到平板
    	    	requestComm = false;	//请求控制面板传输数据
    	    	waitConnAckOK = false;
    	    	waitCommAckOK = false;
//    	    	isSend = true;
    		} else if (action.equals(Format.CONTINUE_COLLECT_CARD_CANCEL)) {
    			AppUtil.log( "-------CONTINUE_COLLECT_CARD_CANCEL-------");
    			stop();
    		}
    	};
    };
    
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
        globalInit();
        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        
        //返回到蓝牙配对界面，在蓝牙断开的情况下，点击已配对设备，可重新连接
        if (mAcceptThread !=null) {mAcceptThread.cancel(); mAcceptThread = null;}
        
        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            setState(BTMESSAGE.STATE_CONNECTING);
            mAcceptThread.start();
        }
    }

    public void setBtDeviceAddress(String address) {
    	BtDeviceAddress = address;
    }
    
    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     * @param device  The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        if (D) AppUtil.log( "connect to: " + device);
        globalInit();
        // Cancel any thread attempting to make a connection
        if (mState == BTMESSAGE.STATE_CONNECTING) {
            if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(BTMESSAGE.STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     * @param socket  The BluetoothSocket on which the connection was made
     * @param device  The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (D) AppUtil.log( "connected");

        // Cancel the thread that completed the connection
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}

        // Cancel the accept thread because we only want to connect to one device
        if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}
        //判断连接的蓝牙设备是否与选择的设备相匹配
    	// Start the thread to manage the connection and perform transmissions
    	mConnectedThread = new ConnectedThread(socket);
    	mConnectedThread.start();
    	// Send the name of the connected device back to the UI Activity
    	Message msg = mHandler.obtainMessage(BTMESSAGE.MESSAGE_DEVICE_NAME);
    	Bundle bundle = new Bundle();
    	bundle.putString(BTMESSAGE.DEVICE_NAME, device.getName());
    	msg.setData(bundle);
    	mHandler.sendMessage(msg);
    	AppUtil.log( "name:"+device.getName()+"  address:"+device.getAddress());
    	setState(BTMESSAGE.STATE_CONNECTED);
    }
    
    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (D) AppUtil.log( "stop");
        isStop = true;
        setState(BTMESSAGE.STATE_NONE);
        if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}
    }
    
    private void reset() {
    	if (mConnectThread != null) {mConnectThread.cancel(); mConnectThread = null;}
        if (mConnectedThread != null) {mConnectedThread.cancel(); mConnectedThread = null;}
        if (mAcceptThread != null) {mAcceptThread.cancel(); mAcceptThread = null;}
        isStop = true;
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != BTMESSAGE.STATE_CONNECTED) return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(out);
    }
    
    public void globalInit() {
    	requestConn = false;	//控制面板请求联网到平板
    	requestComm = false;	//请求控制面板传输数据
    	waitConnAckOK = false;
    	waitCommAckOK = false;
    	sendStart = true;
    	isSend = true;
    	ReSendCounter = 3;
    	RecvErrorCounter = 3;
    	isRecvError = false;
    	isStop = false;
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

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) {
                AppUtil.log( "listen() failed,"+ e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            if (D) AppUtil.log( "BEGIN mAcceptThread:" + this);
            setName("AcceptThread");
            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != BTMESSAGE.STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                	if (D) AppUtil.log( "ServerSocket accept---0");
                    socket = mmServerSocket.accept();
                    if (D) AppUtil.log( "ServerSocket accept---1");
                } catch (IOException e) {
                    AppUtil.log( "accept() failed,"+ e);
                    break;
                }
                if (D) AppUtil.log( "ServerSocket accept---2");
                // If a connection was accepted
                if (socket != null) {
                    synchronized (BtCardService.this) {
                        switch (mState) {
                        case BTMESSAGE.STATE_LISTEN:
                        case BTMESSAGE.STATE_CONNECTING:
                            // Situation normal. Start the connected thread.
                            connected(socket, socket.getRemoteDevice());
                            break;
                        case BTMESSAGE.STATE_NONE:
                        case BTMESSAGE.STATE_CONNECTED:
                            // Either not ready or already connected. Terminate new socket.
                            try {
                                socket.close();
                            } catch (IOException e) {
                                AppUtil.log( "Could not close unwanted socket,"+ e);
                            }
                            break;
                        }
                    }
                }
            }
            if (D) AppUtil.log("END mAcceptThread");
        }

        public void cancel() {
            if (D) AppUtil.log( "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                AppUtil.log( "close() of server failed,"+ e);
            }
        }
    }


    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                AppUtil.log( "create() failed,"+ e);
            }
            mmSocket = tmp;
        }

        public void run() {
            AppUtil.log("BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
            	if (D) AppUtil.log( "client connect to server-----0");
                mmSocket.connect();
                if (D) AppUtil.log( "client connect to server-----1");
            } catch (IOException e) {
            	if (D) AppUtil.log( "client connect ioException.");
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    AppUtil.log( "unable to close() socket during connection failure,"+ e2);
                }
                // Start the service over to restart listening mode
                BtCardService.this.start();
                return;
            }
            if (D) AppUtil.log( "client connect to server-----2");
            // Reset the ConnectThread because we're done
            synchronized (BtCardService.this) {
                mConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                AppUtil.log( "close() of connect socket failed,"+ e);
            }
        }
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
    
    //请求联网到读写器
    private void RequestConn(OutputStream writer) {
    	byte[] SendBuf = new byte[7];
    	int check_sum = 0;
	
		SendBuf[0] = (byte) Format.STORAGE_HEAD;
		SendBuf[1] = Format.STORAGE_BREAK;
		
	  	SendBuf[2] = Format.CMD_PANEL_REQUEST_INTERNET;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 0;
	  	
	  	check_sum = getCrcCheckSum(5, SendBuf);
	  	
	  	SendBuf[5] = (byte) ((check_sum >> 8) & 0xff);
		SendBuf[6] = (byte) (check_sum & 0xff);
	  	
	  	AppUtil.log( "RequestConn");
	  	try {
			writer.write(SendBuf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	  	SetCmd = Format.CMD_PANEL_REQUEST_INTERNET;
    }
    
    private void sendAck(int cmd, OutputStream writer) {
    	
    	byte[] SendBuf = new byte[8];
    	int check_sum = 0;
    	
		SendBuf[0] = (byte) Format.STORAGE_HEAD;
		SendBuf[1] = Format.STORAGE_BREAK;

	  	SendBuf[2] = (byte) cmd;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 1;
	  	SendBuf[5] = 1;
	  	
	  	check_sum = getCrcCheckSum(6, SendBuf);
	  	SendBuf[6] = (byte) ((check_sum >> 8) & 0xff);
	  	SendBuf[7] = (byte) (check_sum & 0xff);
	  	AppUtil.log( "send ack === cmd:" + cmd);
	  	try {
			Thread.sleep(300);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	  	try {
			writer.write(SendBuf);
		} catch (IOException e) {
			e.printStackTrace();
		}   	
    }
    //请求读写器传输局
    private void RequestComm(OutputStream writer) {
    	byte[] SendBuf = new byte[7];
    	int check_sum = 0;
	
		SendBuf[0] = (byte) Format.STORAGE_HEAD;
		SendBuf[1] = Format.STORAGE_BREAK;
		
	  	SendBuf[2] = Format.CMD_REQUEST_COMM;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 0;
	  	
	  	check_sum = getCrcCheckSum(5, SendBuf);
	  	SendBuf[5] = (byte) ((check_sum >> 8) & 0xff);
	  	SendBuf[6] = (byte) (check_sum & 0xff);
	  	
	  	AppUtil.log( "RequestComm");
	  	try {
			writer.write(SendBuf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	  	SetCmd = Format.CMD_REQUEST_COMM;
    }
    
    private void sendNAck(int cmd, OutputStream writer) {
    	
    	byte[] SendBuf = new byte[8];
    	int check_sum = 0;
    	
		SendBuf[0] = (byte) Format.STORAGE_HEAD;
		SendBuf[1] = Format.STORAGE_BREAK;
		
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
	  	try {
			writer.write(SendBuf);
		} catch (IOException e) {
			e.printStackTrace();
		}   	
    }
    
    class ServerAck implements Runnable {
    	
    	@Override
    	public void run() {
    		WaitServerACK(5000);
    	}
    }
    
    private boolean WaitServerACK(int time_out) {
    	recv_timeout = false; //清除超时状态
    	if (ServerCmd != 0xff) ServerCmd = 0xff;  //初始化ServerCmd，防止未接收到服务端的值立即返回
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
    		switch (ServerCmd)
    		{
	    		case Format.CMD_PANEL_REQUEST_INTERNET:
	    			AppUtil.log( "WaitServerACK:Format.CMD_PANEL_REQUEST_INTERNET");
					break;
	    		case Format.CMD_REQUEST_COMM:
	    			AppUtil.log( "WaitServerACK:Format.CMD_REQUEST_COMM");
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
    
/*    private boolean dataCheck(int[] data, int length) {
    	int data_head = data[0];
    	int data_break = data[1];
    	int cmd = data[2];
    	int data_length = (data[3] << 8) + data[4];
    	int check_sum = 0;
    	AppUtil.log( "data check start: cmd " + cmd);
    	for (int i=0; i < length; i++) {
    		AppUtil.log( Integer.toHexString(data[i])+"");
    	}
    	if ((data_head == Format.STORAGE_HEAD) && (data_break == Format.STORAGE_BREAK)) {
    		
    		if (cmd > 10) {
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
    	int data_head = data[0];
    	int data_break = data[1];
    	int cmd = data[2];
    	int data_length = (data[3] << 8) + data[4];
    	int check_sum = 0;
    	AppUtil.log( "data check start: cmd " + cmd);
    	/*for (int i=0; i < length; i++) {
    		AppUtil.log( Integer.toHexString(data[i])+"");
    	}*/
    	if ((data_head == Format.STORAGE_HEAD) && (data_break == Format.STORAGE_BREAK)) {
    		
    		if (cmd > 10) {
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
    
    private void CheckBtState(OutputStream writer) {
    	byte[] SendBuf = new byte[6];
    	
		SendBuf[0] = 35;
		SendBuf[1] = 41;
		
	  	SendBuf[2] = 1;
	  	SendBuf[3] = 0;
	  	SendBuf[4] = 0;
	  	SendBuf[5] = 1;
	  	AppUtil.log( "CheckBtState");
	  	try {
			writer.write(SendBuf);
		} catch (IOException e) {
			e.printStackTrace();
			connectionFailed();
		}
    }
    
    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            AppUtil.log( "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                AppUtil.log( "temp sockets not created,"+ e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

		public void run() {
            AppUtil.log("BEGIN mConnectedThread");
            byte[] recv_temp = new byte[2048];
			int[] recv_data = new int[2048];
			int data_head = 0;
			int data_break = 0;
			int cmd = 0;
			int recv_length = 0;
			int nreadLen = 0;
			int data_len = 0;
			
			requestConn = false;	//平板电脑请求联网
			requestComm = false;	//请求控制面板传输数据
	    	waitConnAckOK = false;
	    	waitCommAckOK = false;
			ReSendCounter = 3;
			RecvErrorCounter = 3;
			isRecvError = false;
			isSend = true;
			
			while (!isStop) {
				if (isSend) {
					if (!requestConn) {
						AppUtil.log( "wait send...");
						while (!sendStart || !thread_finish) {
							if (isStop) {
								break;
							}
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							CheckBtState(mmOutStream);
						}
						if (isStop) {
							break;
						}
						AppUtil.log( "start send...");
						sendStart = false;
						new Thread(){
							@Override
							public void run() {
								thread_finish = false;
								AppUtil.log( "thread ---- run");
								while (ReSendCounter >= 0) {
									AppUtil.log( "thread---ReSendCounter:"+ReSendCounter);
									RequestConn(mmOutStream); //请求联网到读写器
									SetCmd = Format.CMD_PANEL_REQUEST_INTERNET;
									if (WaitServerACK(10000)) {
										AppUtil.log( "recv request_prj_eng ack.");
										waitConnAckOK = true;
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
						isSend = false;
					} else {
						if (!requestComm) {
							new Thread(){
								@Override
								public void run() {
									while (ReSendCounter >= 0) {
										RequestComm(mmOutStream);  //请求读写器传输数据
										SetCmd = Format.CMD_REQUEST_COMM;
										if (WaitServerACK(10000)) {
											waitCommAckOK = true;
											ReSendCounter = 3;
											AppUtil.log( "requestComm:"+requestComm);
											break;
										} else {
											ReSendCounter--;
										}
									}
									thread_finish = true;
								};
							}.start();
							isSend = false;
						}
					}
				} else {
					AppUtil.log( "read data...");
					int max_recv_len = 2048;
					int recv_offset = 0;
					for (int i = 0; i < 2048; i++) {
						recv_temp[i] = 0;
						recv_data[i] = 0;
					}
					boolean socketErr = false;
					while (recv_offset < max_recv_len) {
						try {
							nreadLen = mmInStream.read(recv_temp, recv_offset, max_recv_len - recv_offset);
							if (nreadLen <= 0) {
								AppUtil.log( "disconnected");
								connectionLost();
								
								//add by chongqing jinou
								//在设备连接断开后，返回到配对界面，显示连接中
								/*if(mState != BTMESSAGE.STATE_NONE)
							{
								AppUtil.log( "disconnected");
								// Start the service over to restart listening mode
								BtCardService.this.start();
							}*/
								socketErr = true;
								break;
							}  
						} catch (IOException e) {
							e.printStackTrace();
							AppUtil.log( "disconnected");
							connectionLost();
							
							//add by chongqing jinou
							//在设备连接断开后，返回到配对界面，显示连接中
							/*if(mState != BTMESSAGE.STATE_NONE)
						{
							AppUtil.log( "disconnected");
							// Start the service over to restart listening mode
							BtCardService.this.start();
						}*/
							socketErr = true;
							break;
						}
						
						AppUtil.log( "nreadLen:" + nreadLen);
						recv_offset = recv_offset + nreadLen;
						if (recv_offset >= 5) {
							int len_high = (recv_temp[3] & 0xff) << 8;
							int len_low = recv_temp[4] & 0xff;
//							int read_len = len_high + len_low + 6;
							int read_len = len_high + len_low + 7;
							
							if (recv_offset >= read_len) {
								AppUtil.log( "read_data:" + read_len);
								break;
							}
						}
					}
					
					if (socketErr) break;
					recv_length = recv_offset;
					
					for (int i = 0; i < recv_length; i++) {
						recv_data[i] = recv_temp[i] & 0xff;
//					AppUtil.log( Integer.toHexString(recv_data[i])+" ");
					}
					data_head = recv_data[0];
					data_break = recv_data[1];
					cmd = recv_data[2];
					data_len = recv_length - 7;
					if ((data_head != Format.STORAGE_HEAD) || (data_break != Format.STORAGE_BREAK)) {
						continue;
					}
					if(!dataCheck(recv_data, recv_length)) {
						if (!requestConn) {
							AppUtil.log( "recv CMD_REQUEST_INTERNET ACK error.");
							continue;
						}
						if (requestConn) {
							RecvErrorCounter--;						//连续3次接收到校验失败的数据，保存最后一次数据
							AppUtil.log( "data check failed.RecvErrorCounter:"+RecvErrorCounter);
							sendNAck(cmd, mmOutStream);
						}
						if (RecvErrorCounter > 0) {
							continue;
						}
						else {
							RecvErrorCounter = 3;
							AppUtil.log( "client received invalid data.");
							context.sendBroadcast(new Intent(Format.CHECK_FAILED));
							break; //不保存错误数据
						}
					} else {
						AppUtil.log( "client received valid data.");
					}
					
					if (!requestConn) {
						if (cmd == Format.CMD_PANEL_REQUEST_INTERNET) { 
							if (!recv_timeout) { //add by wxd 20150902
								context.sendBroadcast(new Intent(Format.SUCCESS_CONNECT_INTERNET)); //接收到读写器联网应答
								AppUtil.log( "=====CMD_REQUEST_INTERNET ACK=====");
								ServerCmd = Format.CMD_PANEL_REQUEST_INTERNET;
								for (int i = 0; i < 100; i++) {
									try {
										Thread.sleep(10);  //设置延时使CMD_REQUEST_ACK同步到ServerCmd
									} catch (InterruptedException e) {
										e.printStackTrace();
									}			
									if (waitConnAckOK) {
										requestConn = true; 
										isSend = true;
										waitConnAckOK = false;
										break;
									}
								}
	//							ServerCmd = 0x30;
							}
						}
					} else {
						if (!requestComm) {
							if (cmd == Format.CMD_REQUEST_COMM) {
								if (!recv_timeout) { //add by wxd 20150902
									ServerCmd = Format.CMD_REQUEST_COMM;
									for (int i = 0; i < 100; i++) {
										try {
											Thread.sleep(10);  //设置延时使CMD_REQUEST_COMM同步到ServerCmd
										} catch (InterruptedException e) {
											e.printStackTrace();
										}			
										if (waitCommAckOK) {
											requestComm = true;
											waitCommAckOK = false;
											break;
										}
									}
									context.sendBroadcast(new Intent(Format.REQUEST_TRANSFER_DATA)); //接收到读写器传数据应答
								}
							} 
						} else {
							sendAck(cmd, mmOutStream); //发送数据接收应答
							if (cmd == Format.CMD_CARD_PRESSURE_TIME) {
								if (data_len > 0) {
									/*for (int i = 0; i < data_len+6; i++) {
										AppUtil.log( "recv_data["+i+"]:" + recv_data[i]);
									}*/
									int index = 5;
									int node_len = (recv_data[index++] << 8) + recv_data[index++];
									AppUtil.log( "node_len:"+node_len);
									if (node_len >= 5) {
										if (node_len > 1024) node_len = 1024;
										double[] pressure_time = new double[1024];
										double[] pressure_val = new double[1024];
										double design_press = 0;
										double cap_unit_hour = 0;
										int recv_time_high = 0;
										DecimalFormat df = new DecimalFormat("##.##");
										StringBuffer groutDataSb = new StringBuffer();
//										StringBuffer groutDataSb_ = new StringBuffer();
										int sb_index = 0;
										for (int i = 0; i < node_len; i+=5) {
											sb_index = index;
//											groutDataSb_.append(recv_data[sb_index++]+"-").append(recv_data[sb_index++]+"-")
//											.append(recv_data[sb_index++]+"-").append(recv_data[sb_index++]+"-")
//											.append(recv_data[sb_index++]+"-");
//											AppUtil.log(groutDataSb_.toString());
											sb_index = index;
											recv_time_high = recv_data[index++];
											if ((recv_time_high & 0x80) == 0x80) { //手动停止注浆判断
												pressure_time[i/5] = ((recv_time_high & 0x7f) * 60 + ((recv_data[index++] << 8) + recv_data[index++]) * 0.001);
											} else {
												pressure_time[i/5] =  (recv_time_high * 60 + ((recv_data[index++] << 8) + recv_data[index++]) * 0.001);
											}
											pressure_val[i/5] = (((recv_data[index++] << 8) + recv_data[index++]) * 1.65) / 4095; //Mpa
//											AppUtil.log( "pressure_time["+i/5+"]----------" + pressure_time[i/5]);
//											AppUtil.log( "pressure_val["+i/5+"]----------" + pressure_val[i/5]);
											pressure_time[i/5] = Double.parseDouble(df.format(pressure_time[i/5]));
											pressure_val[i/5] = Double.parseDouble(df.format(pressure_val[i/5]));
											groutDataSb.append(Integer.toHexString((recv_data[sb_index++] & 0x000000FF) | 0xFFFFFF00).substring(6))
											.append(Integer.toHexString((recv_data[sb_index++] & 0x000000FF) | 0xFFFFFF00).substring(6))
											.append(Integer.toHexString((recv_data[sb_index++] & 0x000000FF) | 0xFFFFFF00).substring(6))
											.append(Integer.toHexString((recv_data[sb_index++] & 0x000000FF) | 0xFFFFFF00).substring(6))
											.append(Integer.toHexString((recv_data[sb_index++] & 0x000000FF) | 0xFFFFFF00).substring(6));
//											AppUtil.log(sb_index+":"+groutDataSb.toString());
										}
										
										if (mProjectPointBase.openDb(proj_val, eng_val)) {
											int date_len = recv_length - 9 - node_len;
											if (date_len >= 7) {
												int year = (recv_data[index++] << 8) + recv_data[index++];
												int month = recv_data[index++];
												int day = recv_data[index++];
												int hour = recv_data[index++];
												int minute = recv_data[index++];
												int second = recv_data[index++];
												StringBuffer date = new StringBuffer();
												date.append(year).append('-').append(month).append('-').append(day).append(' ').
												append(hour).append(':').append(minute).append(':').append(second);
												mProjectPointBase.updateWrcardCraftAnchorSlurryDate(db_anchor_name, date.toString(), (int)pressure_time[node_len/5 - 1]);
												AppUtil.log( "slurry_time:" + date.toString());
												WrcardParameter mParameter = mProjectPointBase.getWrcardCraftParameter(db_anchor_name);
												if (mParameter != null) {
													design_press = mParameter.getDesignPress();
													cap_unit_hour = mParameter.getCapUnitHour();
													mProjectPointBase.updateWrcardAnchorCollectData(db_anchor_name, design_press, cap_unit_hour, 
															pressure_time, pressure_val, node_len/5);
													if (groutDataSb != null) {
														mProjectPointBase.updateWrcardCollectData(db_anchor_name, groutDataSb.toString());
													}
												}
											} else {
												AppUtil.log( "date length is error.");
											}
										} else {
											Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
										}
									}
								}
							} else if (cmd == Format.CMD_CARD_CRAFT_PARAMETER) {
								if (data_len > 0) {
									/*for (int i = 0; i < data_len+6; i++) {
									AppUtil.log( "recv_data["+i+"]:" + recv_data[i]);
								}*/
									String seq = "" ;
									String mileage = "";
									String cap_unit_hour = "";
									String design_press = "";
									String anchor_type = "";
									String anchor_model = "";
									String anchor_length = "";
									String thereo_cap = "";
									String device_type = "" ;
									String hold_time = "";
									int anchor_id = 0;
									String proj_name = "";
									String eng_name = "";
									
									int node_len = 0;
									int index = 5;
									anchor_id = (recv_data[index++] << 16) + (recv_data[index++] << 8) + recv_data[index++];
									try {
										byte[] seq_byte = new byte[64];
										node_len = recv_data[index++];
										if (node_len <= 64) {
											for (int i = 0; i < node_len; i++) {
												seq_byte[i] = (byte)recv_data[index++];
											}
											seq = new String(seq_byte, 0, node_len, "GBK");
										}
										
										byte[] mileage_byte = new byte[64];
										node_len = recv_data[index++];
										if (node_len <= 64) {
											for (int i = 0; i < node_len; i++) {
												mileage_byte[i] = (byte)recv_data[index++];
											}
											mileage = new String(mileage_byte, 0, node_len, "GBK");
										}
										
										byte[] cap_unit_hour_byte = new byte[64];
										node_len = recv_data[index++];
										if (node_len <= 64) {
											for (int i = 0; i < node_len; i++) {
												cap_unit_hour_byte[i] = (byte)recv_data[index++];
											}
											cap_unit_hour = new String(cap_unit_hour_byte, 0, node_len, "GBK");
										}
										
										byte[] anchor_type_model_byte = new byte[64];
										node_len = recv_data[index++];
										if (node_len <= 64) {
											for (int i = 0; i < node_len; i++) {
												anchor_type_model_byte[i] = (byte)recv_data[index++];
											}
											String anchor_type_model = new String(anchor_type_model_byte, 0, node_len, "GBK");
											if (anchor_type_model.length() > 0 && anchor_type_model.contains("-")) {
								    			String[] anchor_type_models = anchor_type_model.split("-");
								    			if (anchor_type_models.length == 2)
								    			{
								    				anchor_type = anchor_type_models[0];
								    				anchor_model = anchor_type_models[1];
								    			}
								    		} else {
								    			anchor_type = anchor_type_model;
								    			anchor_model = "";
								    		}
										}
										
										byte[] anchor_length_byte = new byte[64];
										node_len = recv_data[index++];
										if (node_len <= 64) {
											for (int i = 0; i < node_len; i++) {
												anchor_length_byte[i] = (byte)recv_data[index++];
											}
											anchor_length = new String(anchor_length_byte, 0, node_len, "GBK");
										}
										
										byte[] thereo_cap_byte = new byte[64];
										node_len = recv_data[index++];
										if (node_len <= 64) {
											for (int i = 0; i < node_len; i++) {
												thereo_cap_byte[i] = (byte)recv_data[index++];
											}
											thereo_cap = new String(thereo_cap_byte, 0, node_len, "GBK");
										}
										
										byte[] device_type_byte = new byte[64];
										node_len = recv_data[index++];
										if (node_len <= 64) {
											for (int i = 0; i < node_len; i++) {
												device_type_byte[i] = (byte)recv_data[index++];
											}
											device_type = new String(device_type_byte, 0, node_len, "GBK");
										}
										
										byte[] design_press_byte = new byte[64];
										node_len = recv_data[index++];
										if (node_len <= 64) {
											for (int i = 0; i < node_len; i++) {
												design_press_byte[i] = (byte)recv_data[index++];
											}
											design_press = new String(design_press_byte, 0, node_len, "GBK");
										}
										
										byte[] hold_time_byte = new byte[64];
										node_len = recv_data[index++];
										if (node_len <= 64) {
											for (int i = 0; i < node_len; i++) {
												hold_time_byte[i] = (byte)recv_data[index++];
											}
											hold_time = new String(hold_time_byte, 0, node_len, "GBK");
										}
										
										byte[] eng_name_byte = new byte[64];
										node_len = recv_data[index++];
										if (node_len <= 64) {
											for (int i = 0; i < node_len; i++) {
												eng_name_byte[i] = (byte)recv_data[index++];
											}
											eng_name = new String(eng_name_byte, 0, node_len, "GBK");
										}
										
										byte[] proj_name_byte = new byte[64];
										node_len = recv_data[index++];
										if (node_len <= 64) {
											for (int i = 0; i < node_len; i++) {
												proj_name_byte[i] = (byte)recv_data[index++];
											}
											proj_name = new String(proj_name_byte, 0, node_len, "GBK");
										}
									} catch (UnsupportedEncodingException e) {
										e.printStackTrace();
									}
									int proj_val_l = -1;
									int eng_val_l = -1;
									if (mProjectBase.openDb()) {
										proj_val_l = mProjectBase.getProjectId(proj_name);
										if (proj_val_l == -1) {
//											globalInit();
											Intent intent = new Intent(Format.CARD_PROJ_NOT_MATCH);
											intent.putExtra("proj_name", proj_name);
											context.sendBroadcast(intent);
										} else {
											eng_val_l = mProjectBase.getSubProjectId(eng_name);
											if (eng_val_l == -1) {
//												globalInit();
												Intent intent = new Intent(Format.CARD_ENG_NOT_MATCH);
												intent.putExtra("proj_name", proj_name);
												intent.putExtra("eng_name", eng_name);
												context.sendBroadcast(intent);
											} else {
												Intent intent = new Intent(Format.PROJ_ENG_MATCH);
												intent.putExtra("proj_name", proj_name);
												intent.putExtra("eng_name", eng_name);
												intent.putExtra("anchor_seq", seq);
												context.sendBroadcast(intent);
												if (proj_val != proj_val_l && eng_val != eng_val_l) {
													mProjectPointBase.closeDb();
													mProjectPointBase.openDb(proj_val_l, eng_val_l);
													proj_val = proj_val_l;
													eng_val = eng_val_l;
												}
												float cap_unit_hour_f = 0;
												float anchor_length_f = 0;
												float thereo_cap_f = 0;
												float design_press_f = 0;
												int hold_time_i = 0;
												if (cap_unit_hour.matches("^[0-9]+(\\.[0-9]+)?$")) {
													cap_unit_hour_f = Float.parseFloat(cap_unit_hour);
												}
												if (anchor_length.matches("^[0-9]+(\\.[0-9]+)?$")) {
													anchor_length_f = Float.parseFloat(anchor_length);
												}
												if (thereo_cap.matches("^[0-9]+(\\.[0-9]+)?$")) {
													thereo_cap_f = Float.parseFloat(thereo_cap);
												}
												if (design_press.matches("^[0-9]+(\\.[0-9]+)?$")) {
													design_press_f = Float.parseFloat(design_press);
												}
												if (hold_time.matches("[\\d]+")) {
													hold_time_i = Integer.parseInt(hold_time);
												}
												db_anchor_id = anchor_id;
												db_anchor_name = seq;
												design_press_g = design_press_f;
												cap_unit_hour_g = cap_unit_hour_f;
												if (mProjectPointBase.openDb(proj_val, eng_val)) {
													mProjectPointBase.insertWrcardParameter(db_anchor_id, db_anchor_name, cap_unit_hour_f, anchor_type, 
															anchor_model, anchor_length_f, thereo_cap_f, design_press_f, hold_time_i); //need fix
												} else {
													Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
												}
											}
										}
									} else {
										Toast.makeText(context, "数据库打开失败", Toast.LENGTH_SHORT).show();
									}
								}
							}  else if (cmd == Format.CMD_CARD_COMM_STOP) { 
								AppUtil.log( "recv CMD_COMM_STOP");
								context.sendBroadcast(new Intent(Format.CHECK_SUCCESS));
								isSend = true;
								requestConn = false;
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
//								break;
							}
						}
					}
				}
			}
			AppUtil.log( "while break.");
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                AppUtil.log( "Exception during write,"+ e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                AppUtil.log( "close() of connect socket failed,"+ e);
            }
        }
    }
}

