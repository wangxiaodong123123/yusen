package com.ysdata.grouter.activity;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import com.ysdata.grouter.R;
import com.ysdata.grouter.database.FileOperator;
import com.ysdata.grouter.database.SharePrefOperator;
import com.ysdata.grouter.uart.MyActivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class AdminMainListActivity extends Activity {
	private RelativeLayout m_Array[];
	private ImageView mImageView[];
	private int mIndex = 0;
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.admin_main_list);
		mContext = this;
		MyActivityManager.addActivity(AdminMainListActivity.this);
		initView();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MyActivityManager.removeActivity(AdminMainListActivity.this);
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
		m_Array[0]=(RelativeLayout) findViewById(R.id.AdminMain1_relativelayout);
		m_Array[1]=(RelativeLayout) findViewById(R.id.AdminMain2_relativelayout);

		mImageView = new ImageView[2];
		mImageView[0]=(ImageView)findViewById(R.id.AdminMain1_imageview);
		mImageView[1]=(ImageView)findViewById(R.id.AdminMain2_imageview);
		
		m_Array[0].setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				setVisible(0, mIndex);
				Intent intent=new Intent(AdminMainListActivity.this,InputAdminPasswordActivity.class);
				intent.putExtra("action", "recovery");
				startActivity(intent);
				mIndex = 0;
			}
		});
		m_Array[1].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setVisible(1, mIndex);
				mIndex = 1;
				LayoutInflater factory = LayoutInflater.from(AdminMainListActivity.this);
				final View dialogView = factory.inflate(R.layout.dialog_modify_password, null);
            	AlertDialog.Builder dlg = new AlertDialog.Builder(AdminMainListActivity.this);
            	dlg.setTitle("修改密码");
            	dlg.setView(dialogView);
            	dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharePrefOperator mSharePref = SharePrefOperator.getSingleSharePrefOperator(AdminMainListActivity.this);
						EditText old_pwd_et = (EditText) dialogView.findViewById(R.id.modify_password_old_et);
						EditText new_pwd_et = (EditText) dialogView.findViewById(R.id.modify_password_new_et);
						EditText renew_pwd_et = (EditText) dialogView.findViewById(R.id.modify_password_renew_et);
						
						try {
							Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, false);// 将mShowing变量设为false，表示对话框已关闭
							dialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}

						String password = mSharePref.getPassword();
						String old_pwd = old_pwd_et.getText().toString();
						String new_pwd = new_pwd_et.getText().toString();
						String renew_pwd = renew_pwd_et.getText().toString();
						if (old_pwd.equals(password)) {
							 if(Pattern.matches("^[0-9a-zA-Z]{6,16}$", new_pwd)){
								 if (new_pwd.equals(renew_pwd)) {
									 mSharePref.savePassword(new_pwd);
									 Toast.makeText(getApplicationContext(), "密码修改成功！",
												Toast.LENGTH_SHORT).show();
									 try {
											Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
											field.setAccessible(true);
											field.set(dialog, true);// 将mShowing变量设为false，表示对话框已关闭
											dialog.dismiss();
										} catch (Exception e) {
											e.printStackTrace();
										}
								 } else {
									 Toast.makeText(getApplicationContext(), "前后密码不一致，请重新输入！",
												Toast.LENGTH_SHORT).show();
									 new_pwd_et.setText("");
									 renew_pwd_et.setText("");
								 }
							 } else {
								 Toast.makeText(getApplicationContext(), "密码只能为数字和字母，且长度介于6到16之间！",
											Toast.LENGTH_SHORT).show();
								 new_pwd_et.setText("");
								 renew_pwd_et.setText("");
							 }
						} else {
							Toast.makeText(getApplicationContext(), "原始密码输入错误，请重新输入！",
									Toast.LENGTH_SHORT).show();
							old_pwd_et.setText("");
						}
					}
				});
            	dlg.setNegativeButton("取消", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, true);// 将mShowing变量设为false，表示对话框已关闭
							dialog.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
            		
            	});
            	dlg.create();
            	dlg.show();
			}
		});
	}
}
