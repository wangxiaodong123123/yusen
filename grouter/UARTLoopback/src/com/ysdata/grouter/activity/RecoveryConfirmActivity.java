package com.ysdata.grouter.activity;

import com.ysdata.grouter.R;
import com.ysdata.grouter.cloud.util.ConstDef;
import com.ysdata.grouter.database.FileOperator;
import com.ysdata.grouter.storage.ExtSdCheck;
import com.ysdata.grouter.uart.MyActivityManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RecoveryConfirmActivity extends BaseActivity {
	private Handler handler;
	ProgressDialog progressDialog;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recovery_confirm);
		context = this;
		MyActivityManager.addActivity(RecoveryConfirmActivity.this);
		initView();
	}

	private void initView() {
		TextView content = (TextView)findViewById(R.id.recovery_conf_content);
		content.setText("�����ʼ�������������Ŀ�����ݣ�������ͬ�����ơ��������ơ������ļ����������ݣ��Ƿ������");
		handler = new Handler();	
		setM_Id(new int[] {R.id.recovery_confirm_bt, R.id.recovery_cancel_bt });
		m_Array = new Button[2];
		setM_Array(new Button[m_Id.length]);
		setmLength(m_Id.length);
		m_Array[0] = findViewById(R.id.recovery_confirm_bt);
		m_Array[1] = findViewById(R.id.recovery_cancel_bt);

		m_Array[0].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				progressDialog = ProgressDialog.show(RecoveryConfirmActivity.this, 
						"�����ʼ��", "���ڽ��г����ʼ������,��ȴ�...", true, false);   
				handler.postDelayed(runnable, 100);
			}
		});

		m_Array[1].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
//			SharePrefOperator mShPrefOperator = SharePrefOperator.getSingleSharePrefOperator(context);
//			mShPrefOperator.clearAllSharedPref();
			FileOperator mFileOperator = FileOperator.getSingleFileOperator(RecoveryConfirmActivity.this);
			String ext_ang_dir = ExtSdCheck.getExtSDCardPath();
			boolean ret_delDir = mFileOperator.DeleteAllDir(ext_ang_dir+"/"+ConstDef.PROJECT_NAME) &&
					mFileOperator.DeleteAllDir("/data/data/"+ConstDef.PKG_NAME+"/shared_prefs");
			if (ret_delDir) {
				if (progressDialog!=null)
					progressDialog.dismiss();
				 Toast.makeText(getApplicationContext(), "�����ʼ���ɹ���",
							Toast.LENGTH_SHORT).show();
			} else {
				if (progressDialog!=null)
					progressDialog.dismiss();
				 Toast.makeText(getApplicationContext(), "�����ʼ��ʧ�ܣ�",
							Toast.LENGTH_SHORT).show();
			}
			finish();
		}
	};
	
	protected void onDestroy() {
		MyActivityManager.removeActivity(RecoveryConfirmActivity.this);
		super.onDestroy();
	};
}

