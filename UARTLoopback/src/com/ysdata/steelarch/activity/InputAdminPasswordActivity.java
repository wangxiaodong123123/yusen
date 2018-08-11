package com.ysdata.steelarch.activity;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.database.SharePrefOperator;
import com.ysdata.steelarch.uart.MyActivityManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InputAdminPasswordActivity extends Activity {
	EditText input_password_et;
	Button confirm_bt;
	SharePrefOperator mSharePref;
	String action="";
	TextView title_tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_admin_password);
		Intent intent = getIntent();
		MyActivityManager.addActivity(InputAdminPasswordActivity.this);
		action = intent.getStringExtra("action");
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout_id);
		if (action.equals("craft_create")) {
			layout.setBackgroundResource(R.drawable.create_bg);
		} else {
			layout.setBackgroundResource(R.drawable.cloud_bg);
		}
		input_password_et = (EditText) findViewById(R.id.InputAdminPassword_Et);
		confirm_bt = (Button) findViewById(R.id.InputAdminPassword_ConfirmBt);
		mSharePref = SharePrefOperator.getSingleSharePrefOperator(this);
		title_tv = (TextView) findViewById(R.id.InputPassword_title);
		if (action.equals("craft_create")) {
			title_tv.setText("注浆工艺编制->输入密码");
		}
		confirm_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String input_password = input_password_et.getText().toString();
				String save_password = mSharePref.getPassword();
				if (input_password.equals(save_password)) {
					if (action.equals("recovery")) {
						Intent intent=new Intent(InputAdminPasswordActivity.this,RecoveryConfirmActivity.class);
						startActivity(intent);
						finish();
					} else if (action.equals("craft_create")) {
						Intent intent=new Intent(InputAdminPasswordActivity.this,SetMainListActivity.class);
						startActivity(intent);
						finish();
					} else if (action.equals("admin_operate")){
						Intent intent=new Intent(InputAdminPasswordActivity.this,AdminMainListActivity.class);
						startActivity(intent);
						finish();
					}
				} else {
					Toast.makeText(getApplicationContext(), "密码有误，请重新输入！",
							Toast.LENGTH_SHORT).show();
					input_password_et.setText("");
				}
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		MyActivityManager.removeActivity(InputAdminPasswordActivity.this);
		super.onDestroy();
	}
}
