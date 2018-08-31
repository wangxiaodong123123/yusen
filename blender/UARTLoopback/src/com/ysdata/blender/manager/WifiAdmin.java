package com.ysdata.blender.manager;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

public class WifiAdmin {
	private WifiManager mWifiManager;
	private WifiInfo mWifiInfo;
	private List<ScanResult> mWifiScanResultList; 
	private List<WifiConfiguration> mWifiConfigurationList; 
	WifiLock mWifiLock; 
	
	public WifiAdmin(Context context) {
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}
	
	public void openWifi() {
		if (!mWifiManager.isWifiEnabled()) {
			mWifiManager.setWifiEnabled(true);
		}
	}
	
	public void closeWifi() {   
		if (mWifiManager.isWifiEnabled()) {   
		    mWifiManager.setWifiEnabled(false);   
		}   
	}  

	public int checkState() {
		return mWifiManager.getWifiState();
	}
	
	public void createWifiLock() {
		mWifiLock = mWifiManager.createWifiLock("AngWifiLock");
	}
	
	public void acquireWifiLock() {
		mWifiLock.acquire();
	}
	
	public void releaseWifiLock() {
		if (mWifiLock.isHeld()) {
			mWifiLock.release();
		}
	}
	
	public void startScan() {
		mWifiManager.startScan();
		mWifiScanResultList = mWifiManager.getScanResults();
		mWifiConfigurationList = mWifiManager.getConfiguredNetworks();
	}
	
	public List<ScanResult> getWifiScanResultList() {
		return mWifiScanResultList;
	}
	
	public List<WifiConfiguration> getWifiConfigurationList() {
		return mWifiConfigurationList;
	}
	
	// ָ�����úõ������������    
	public void connectConfiguration(int index) {   
	    // �����������úõ�������������    
	    if (index > mWifiConfigurationList.size()) {   
	    	return;   
	    }   
	    // �������úõ�ָ��ID������    
	    mWifiManager.enableNetwork(mWifiConfigurationList.get(index).networkId,   
	           true);   
	}  
}
