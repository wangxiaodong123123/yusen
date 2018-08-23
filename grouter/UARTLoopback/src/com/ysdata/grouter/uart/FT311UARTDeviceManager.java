//User must modify the below package with their package name
package com.ysdata.grouter.uart; 
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;


/******************************FT311 GPIO interface class******************************************/
public class FT311UARTDeviceManager
{
	private static final String TAG = "USB_TO_UART";
	private static final String ACTION_USB_PERMISSION =    "com.UARTLoopback.USB_PERMISSION";
	public UsbManager usbmanager;
	public UsbAccessory usbaccessory;
	public PendingIntent mPermissionIntent;
	public ParcelFileDescriptor filedescriptor = null;
	public FileInputStream inputstream = null;
	public FileOutputStream outputstream = null;
	public boolean mPermissionRequestPending = false;
	public read_thread readThread;

	private byte [] usbdata; 
	private byte []	writeusbdata;
	private byte  [] readBuffer; /*circular buffer*/
	private int readcount;
	private int totalBytes;
	private int writeIndex;
	private int readIndex;
	private byte status;
	final int  maxnumbytes = 65536;
	
	public boolean READ_ENABLE = false;
	public boolean datareceived = false;
	public boolean accessory_attached = false;

	public Context global_context;

	public static String ManufacturerString = "mManufacturer=FTDI";
	public static String ModelString1 = "mModel=FTDIUARTDemo";
	public static String ModelString2 = "mModel=Android Accessory FT312D";
	public static String VersionString = "mVersion=1.0";
	
	private static FT311UARTDeviceManager mFT311UARTDeviceManager = null;
	
	/*constructor*/
	public FT311UARTDeviceManager(Context context){
		super();
		global_context = context;
		/*shall we start a thread here or what*/
		usbdata = new byte[1024]; 
		writeusbdata = new byte[1024];
		/*128(make it 256, but looks like bytes should be enough)*/
		readBuffer = new byte [maxnumbytes];

		readIndex = 0;
		writeIndex = 0;
		/***********************USB handling******************************************/

		usbmanager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		// Log.d("LED", "usbmanager" +usbmanager);
		mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		context.registerReceiver(mUsbReceiver, filter);

		inputstream = null;
		outputstream = null;		
	}
	/**
	 * SingleFT311UARTDeviceManager
	 **/
	public static FT311UARTDeviceManager getSingleFT311UARTDeviceManager(Context context) {
		if (mFT311UARTDeviceManager == null)
		{
			synchronized (FT311UARTDeviceManager.class)
			{
				if (mFT311UARTDeviceManager == null)
				{
					mFT311UARTDeviceManager = new FT311UARTDeviceManager(context);
				}
			}
		}
		return mFT311UARTDeviceManager;
	}

	
	public void SetConfig(int baud, byte dataBits, byte stopBits,
			byte parity, byte flowControl)
	{
		/*prepare the baud rate buffer*/
		writeusbdata[0] = (byte)baud;
		writeusbdata[1] = (byte)(baud >> 8);
		writeusbdata[2] = (byte)(baud >> 16);
		writeusbdata[3] = (byte)(baud >> 24);

		/*data bits*/
		writeusbdata[4] = dataBits;
		/*stop bits*/
		writeusbdata[5] = stopBits;
		/*parity*/
		writeusbdata[6] = parity;
		/*flow control*/
		writeusbdata[7] = flowControl;

		/*send the UART configuration packet*/
		SendPacket((int)8);
	}
	
	public byte WriteData(int numBytes, byte[] buffer)
	{
		status = 0x00;
		int pos = 0;
		int w_len;
		byte[] tbuf = new byte[1024];
		while (pos < numBytes) {
			if (pos > 0) 
			{
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (numBytes - pos >= 1024)
			{
				w_len = 1024;
			}
			else 
			{
				w_len = numBytes - pos;
			}
			for (int i = 0; i < w_len; i++) {
				tbuf[i] = buffer[pos+i];
			}
			SendData(w_len, tbuf);
			pos += w_len;
		}
		return status;
	}
			
	/*write data*/
	public byte SendData(int numBytes, byte[] buffer) 
	{
		status = 0x00; /*success by default*/
		/*
		 * if num bytes are more than maximum limit
		 */
		if(numBytes < 1){
			/*return the status with the error in the command*/
			return status;
		}

		/*check for maximum limit*/
		if(numBytes > 1024){
			numBytes = 1024;
		}

		/*prepare the packet to be sent*/
		for(int count = 0;count<numBytes;count++)
		{	
			writeusbdata[count] = buffer[count];
		}

		if(numBytes != 64)
		{
			SendPacket(numBytes);
		}
		else
		{
			byte temp = writeusbdata[63];
			SendPacket(63);
			writeusbdata[0] = temp;
			SendPacket(1);
		}

		return status;
	}	
	
	/*read data*/
	public byte ReadData(int numBytes,byte[] buffer, int [] actualNumBytes)
	{
		status = 0x00; /*success by default*/

		/*should be at least one byte to read*/
		if((numBytes < 1) || (totalBytes == 0)){
			actualNumBytes[0] = 0;
			status = 0x01;
			return status;
		}

		/*check for max limit*/
		if(numBytes > totalBytes)
			numBytes = totalBytes;

		/*update the number of bytes available*/
		totalBytes -= numBytes;

		actualNumBytes[0] = numBytes;	

		/*copy to the user buffer*/	
		for(int count = 0; count<numBytes;count++)
		{
			buffer[count] = readBuffer[readIndex];
			readIndex++;
			/*shouldnt read more than what is there in the buffer,
			 * 	so no need to check the overflow
			 */
			readIndex %= maxnumbytes;
		}
		return status;
	}	
	
	/*usb input data handler*/
	private class read_thread  extends Thread 
	{
		FileInputStream instream;

		read_thread(FileInputStream stream ){
			instream = stream;
			this.setPriority(Thread.MAX_PRIORITY);
		}

		public void run()
		{		
			while(READ_ENABLE == true)
			{
				while(totalBytes > (maxnumbytes - 1024))
				{
					try 
					{
						Thread.sleep(50);
					}
					catch (InterruptedException e) {e.printStackTrace();}
				}

				try
				{
					if(instream != null)
					{	
						readcount = instream.read(usbdata,0,1024);
						if(readcount > 0)
						{
							for(int count = 0;count<readcount;count++)
							{					    			
								readBuffer[writeIndex] = usbdata[count];
								writeIndex++;
								writeIndex %= maxnumbytes;
							}

							if(writeIndex >= readIndex)
								totalBytes = writeIndex-readIndex;
							else
								totalBytes = (maxnumbytes-readIndex)+writeIndex;

//					    		Log.e(">>@@","totalBytes:"+totalBytes);
						}
					}
				}
				catch (IOException e){e.printStackTrace();}
			}
		}
	}	

	/*method to send on USB*/
	private void SendPacket(int numBytes)
	{	
		try {
			if(outputstream != null){
				outputstream.write(writeusbdata, 0,numBytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*resume accessory*/
	public int ResumeAccessory()
	{
		// Intent intent = getIntent();
		if (inputstream != null && outputstream != null) {
			return 1;
		}

		UsbAccessory[] accessories = usbmanager.getAccessoryList();
		if(accessories != null)
		{
			Toast.makeText(global_context, "Accessory Attached", Toast.LENGTH_SHORT).show();
		}
		else
		{
			// return 2 for accessory detached case
			//Log.e(">>@@","ResumeAccessory RETURN 2 (accessories == null)");
			accessory_attached = false;
			return 2;
		}

		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if( -1 == accessory.toString().indexOf(ManufacturerString))
			{
				Toast.makeText(global_context, "Manufacturer is not matched!", Toast.LENGTH_SHORT).show();
				return 1;
			}

			if( -1 == accessory.toString().indexOf(ModelString1) && -1 == accessory.toString().indexOf(ModelString2))
			{
				Toast.makeText(global_context, "Model is not matched!", Toast.LENGTH_SHORT).show();
				return 1;
			}

			if( -1 == accessory.toString().indexOf(VersionString))
			{
				Toast.makeText(global_context, "Version is not matched!", Toast.LENGTH_SHORT).show();
				return 1;
			}

			Toast.makeText(global_context, "Manufacturer, Model & Version are matched!", Toast.LENGTH_SHORT).show();
			accessory_attached = true;

			if (usbmanager.hasPermission(accessory)) {
				OpenAccessory(accessory);
			} 
			else
			{
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						Toast.makeText(global_context, "Request USB Permission", Toast.LENGTH_SHORT).show();
						usbmanager.requestPermission(accessory,
								mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			}
		} else {}
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		SetConfig(115200, (byte)8, (byte)1, (byte)0, (byte)0);

		return 0;
	}

	/*destroy accessory*/
	public void DestroyAccessory(boolean bConfiged){
		
		if(true == bConfiged){
			READ_ENABLE = false;  // set false condition for handler_thread to exit waiting data loop
			writeusbdata[0] = 0;  // send dummy data for instream.read going
			SendPacket(1);
		}
		else
		{
			SetConfig(115200,(byte)1,(byte)8,(byte)0,(byte)0);  // send default setting data for config
			try{Thread.sleep(10);}
			catch(Exception e){}

			READ_ENABLE = false;  // set false condition for handler_thread to exit waiting data loop
			writeusbdata[0] = 0;  // send dummy data for instream.read going
			SendPacket(1);
			if(true == accessory_attached)
			{

			}
		}

		try{Thread.sleep(10);}
		catch(Exception e){}			
		CloseAccessory();		
	}

	/*********************helper routines*************************************************/		

	public void OpenAccessory(UsbAccessory accessory)
	{
		filedescriptor = usbmanager.openAccessory(accessory);
		if(filedescriptor != null){
			usbaccessory = accessory;

			FileDescriptor fd = filedescriptor.getFileDescriptor();

			inputstream = new FileInputStream(fd);
			outputstream = new FileOutputStream(fd);
			/*check if any of them are null*/
			if(inputstream == null || outputstream==null){
				return;
			}

			if(READ_ENABLE == false){
				READ_ENABLE = true;
				readThread = new read_thread(inputstream);
				readThread.start();
			}
		}
	}

	private void CloseAccessory()
	{
		try{
			if(filedescriptor != null)
				filedescriptor.close();

		}catch (IOException e){}

		try {
			if(inputstream != null)
				inputstream.close();
		} catch(IOException e){}

		try {
			if(outputstream != null)
				outputstream.close();

		}catch(IOException e){}
		/*FIXME, add the notfication also to close the application*/

		filedescriptor = null;
		inputstream = null;
		outputstream = null;
		accessory_attached = false;
		
		MyActivityManager.removeALLActivity();
		System.exit(0);
	}

	/***********USB broadcast receiver*******************************************/
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			String action = intent.getAction();
			Toast.makeText(global_context, action, Toast.LENGTH_SHORT).show();
			if (ACTION_USB_PERMISSION.equals(action)) 
			{
				Toast.makeText(global_context, "ACTION_USB_PERMISSION", Toast.LENGTH_SHORT).show();
				synchronized (this)
				{
					UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
					{
						Toast.makeText(global_context, "Allow USB Permission", Toast.LENGTH_SHORT).show();
						OpenAccessory(accessory);
					} 
					else 
					{
						Toast.makeText(global_context, "Deny USB Permission", Toast.LENGTH_SHORT).show();
						Log.d(TAG, "permission denied for accessory "+ accessory);

					}
					mPermissionRequestPending = false;
				}
			} 
			else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) 
			{
				Toast.makeText(global_context, "ACTION_USB_ACCESSORY_DETACHED", Toast.LENGTH_SHORT).show();
				DestroyAccessory(true);
				//CloseAccessory();
			}else
			{
				Log.d("LED", "....");
			}
		}	
	};
}