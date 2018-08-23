package com.ysdata.grouter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends Activity {
	public View m_Array[];

	public int mIndex = 0;
	public int mLength = 0;
	public int m_Id[];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void setIbFocus(){		
		m_Array[mIndex].setFocusable(true);
		m_Array[mIndex].setFocusableInTouchMode(true);
		m_Array[mIndex].requestFocus();
		for (int i = 0 ; i<m_Array.length;i++){
			if (i!=mIndex){
				m_Array[i].setFocusable(false);
			}
		}
	}
	public void setIbCheck(){		
		m_Array[mIndex].performClick();

	}
	
	public View[] getM_Array() {
		return m_Array;
	}
	public void setM_Array(View[] m_Array) {
		this.m_Array = m_Array;
	}
	public int getmIndex() {
		return mIndex;
	}
	public void setmIndex(int mIndex) {
		this.mIndex = mIndex;
	}
	public int[] getM_Id() {
		return m_Id;
	}
	public void setM_Id(int[] m_Id) {
		this.m_Id = m_Id;
	}

	public int getmLength() {
		return mLength;
	}
	public void setmLength(int mLength) {
		this.mLength = mLength;
	}

}
