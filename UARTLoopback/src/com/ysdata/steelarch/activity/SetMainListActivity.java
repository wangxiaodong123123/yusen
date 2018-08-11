package com.ysdata.steelarch.activity;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.uart.MyActivityManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SetMainListActivity extends Activity{
	private RelativeLayout m_Array[];
	private ImageView mImageView[];
	private int mIndex = 0;
//	private final static String TAG = "ys200";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.set_main_list);
		MyActivityManager.addActivity(SetMainListActivity.this);
		initView();
	}
	
	private void setVisible(int cur_index, int pre_index) {
		if (cur_index == pre_index) {
			return;
		}
		mImageView[pre_index].setVisibility(View.GONE);
		mImageView[cur_index].setVisibility(View.VISIBLE);
	}
	
	private void initView() {
		m_Array = new RelativeLayout[2];
		m_Array[0]=(RelativeLayout) findViewById(R.id.setMain1_relativelayout);
		m_Array[1]=(RelativeLayout) findViewById(R.id.setMain2_relativelayout);

		mImageView = new ImageView[2];
		mImageView[0]=(ImageView)findViewById(R.id.setMain1_imageview);
		mImageView[1]=(ImageView)findViewById(R.id.setMain2_imageview);
		
		m_Array[0].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVisible(0, mIndex);
				Intent intent=new Intent(SetMainListActivity.this,ContractSectionListActivity.class);
				intent.putExtra("action", "craft_create");
				startActivity(intent);
				mIndex = 0;
			}
		});
		
		m_Array[1].setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				setVisible(1, mIndex);
				Intent intent=new Intent(SetMainListActivity.this,ContractSectionListActivity.class);
				intent.putExtra("action", "craft_transfer");
				startActivity(intent);
				mIndex = 1;
			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyActivityManager.removeActivity(SetMainListActivity.this);
		super.onDestroy();
	}
}
