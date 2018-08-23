package com.ysdata.grouter.activity;

import java.io.File;
import java.util.ArrayList;

import com.ysdata.grouter.R;
import com.ysdata.grouter.adapter.CraftAnchorParameterAdapter;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.CacheManager;
import com.ysdata.grouter.cloud.util.ConstDef;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.element.AnchorParameter;
import com.ysdata.grouter.element.MileageParameter;
import com.ysdata.grouter.storage.ExtSdCheck;
import com.ysdata.grouter.uart.FT311UARTDeviceManager;
import com.ysdata.grouter.uart.MyActivityManager;
import com.ysdata.grouter.uart.UartControlSender;
import com.ysdata.grouter.view.CustomListView;
import com.ysdata.grouter.wireless.client.BTMESSAGE;
import com.ysdata.grouter.wireless.client.Format;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CraftFilePageActivity extends Activity {
	private static final int RIGHT = 0;  
	private static final int LEFT = 1; 
	Context context;
	private ImageView mImageView;
	
	private final static String ACTION_TRNSFER = "craft_transfer";
	
	int project_id, subproject_id;

	private ArrayList<AnchorParameter> mList;
	private CraftAnchorParameterAdapter mAdapter;
	private CustomListView mListView;
	public FT311UARTDeviceManager uartInterface;
	private UartControlSender uartControlSender;
	private ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	
	private TextView anchor_count_tv;
	private TextView water_ratio_tv;
	private TextView craft_mileage_name;
	private TextView craft_people_name;
	private TextView craft_date_name;
	
	private TextView title_tv;
	private Button craft_search_bt;
	private Button craft_transfer_bt;
	
	ProgressDialog progressDialog;
	private final static int UART_NOT_CONNECT = 1;
	private final static int UART_CONNECTED = 2;
	private int uart_status;
	TextView uartStatusTitle;
	boolean isEditState = false;
	boolean isTableRowEditable = false;
	
	private TextView eng_name_tv;
	int mileage_page = 1;
	int mileage_count = 0;
	int anchor_count = 0;
	String mileage_name= "";
	String create_people = "";
	String create_date = "";
	String water_ratio = "";
	private int anchor_count_value = 0;
	private MileageParameter mileage_parameter;
	private GestureDetector gestureDetector;
	int type_position = 0;
	ProgressDialog modifyAnchorAmountProgDialog;
	String proj_name_string = "";
	String subproj_name_string = "";
	String action = "craft_cloud";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.craft_file_page_phone);
		context = this;
		MyActivityManager.addActivity(CraftFilePageActivity.this);
		Intent intent = getIntent();
		action = intent.getStringExtra("action");
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
    	mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
    	uartInterface = FT311UARTDeviceManager.getSingleFT311UARTDeviceManager(context);
    	project_id = CacheManager.getDbProjectId();
    	subproject_id = CacheManager.getDbSubProjectId();
    	title_tv = (TextView) findViewById(R.id.craft_create_title_id);
    	if (uartInterface.accessory_attached) {
    		uart_status = UART_CONNECTED;
    	} else {
    		uart_status = UART_NOT_CONNECT;
    	}
    	mProjectPointBase.closeDb();
    	if (mProjectBase.openDb() && mProjectPointBase.openDb(project_id, subproject_id)) {
    		proj_name_string = mProjectBase.getProjectName(project_id);
    		subproj_name_string = mProjectBase.getSubProjectName(subproject_id);
    		if (action.equals(ACTION_TRNSFER)) {
    			uartStatusTitle = (TextView)findViewById(R.id.btStatus_title_id);
    			if (uart_status == UART_NOT_CONNECT) {
    				uartStatusTitle.setText(R.string.title_not_connected);
    			} else {
    				uartStatusTitle.setText(R.string.title_connected);
    			}
//    			title_tv.setText("注浆工艺编制->注浆参数传输->"+proj_name_string+"->"+
//    					subproj_name_string+"->工艺文件浏览");
    			title_tv.setText("注浆工艺编制->注浆参数传输->工艺文件浏览");
    		}
    		AppUtil.log( "project_id:"+project_id+",subproject_id:"+subproject_id);
        	
    		/*************************************************
    		 * 平板电脑参数传输状态测试
        	mProjectPointBase.updateCraftColumeTransferState(1, 15, 1);
        	mProjectPointBase.updateCraftColumeTransferState(30, 49, 1);
        	mProjectPointBase.updateCraftTransferState(59, 1);
    		 **************************************************/
    		initView();
    		mileage_count = mProjectPointBase.getMileageCount();
//    		mProjectPointBase.updateSendEndMileage("");
//    		mProjectPointBase.updateSendEndMileage("K10+050");
//    		AppUtil.log("==========="+mProjectPointBase.getSendEndMileage());
    		mileage_page = mProjectPointBase.getMileageOrderNoLikeMileageName(mProjectPointBase.
    				getSendEndMileage());
    		if (mileage_page < mileage_count) {
    			mileage_page += 1;
    		}
//    		mileage_page = mileage_count;
    		showMileagePage(mileage_page);
    		gestureDetector = new GestureDetector(CraftFilePageActivity.this, onGestureListener);
    	} else {
    		if (action.equals(ACTION_TRNSFER)) {
				uartStatusTitle = (TextView)findViewById(R.id.btStatus_title_id);
				uartStatusTitle.setVisibility(View.VISIBLE);
				if (uart_status == UART_NOT_CONNECT) {
					uartStatusTitle.setText(R.string.title_not_connected);
				} else {
					uartStatusTitle.setText(R.string.title_connected);
				}
				title_tv.setText("注浆参数传输->工艺文件浏览");
    		}	
    		Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
    	}
    	IntentFilter filter = new IntentFilter();
    	filter.addAction(Format.ACTION_SEND_MILEAGE_NAME);
    	context.registerReceiver(MainBdReceiver, filter);
	}
	
	private BroadcastReceiver MainBdReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String mileage_name = intent.getStringExtra("sectionName");
			String mileage1 = mileage_name.split("\\+")[0];
			String mileage2 = mileage_name.split("\\+")[1];
			AppUtil.log("BroadcastReceiver=============mileage_name:"+mileage_name);
			if (mProjectPointBase.openDb(project_id, subproject_id)) {
				double section_metre = Double.parseDouble(mileage1.substring(1)) * 1000
						+ Double.parseDouble(mileage2);
				int _mileage_page = mProjectPointBase.getMileageOrderNoWhereSectonMetre(section_metre);
				if(_mileage_page > 0 && _mileage_page != mileage_page) {
					mileage_parameter = mProjectPointBase.getMileageParameter(_mileage_page);
					if (mileage_parameter != null) {
						mileage_page = _mileage_page;
						mileage_name = mileage_parameter.getMileageName();
						anchor_count_value = mileage_parameter.getAnchorCount();
						create_people = mileage_parameter.getCreatePeople();
						water_ratio = mileage_parameter.getWaterRatio();
						create_date = mileage_parameter.getCreateDate();
						craft_mileage_name.setText(mileage_name);
						anchor_count_tv.setText(anchor_count_value+"");
						craft_people_name.setText(create_people);
						water_ratio_tv.setText(water_ratio);
						craft_date_name.setText(create_date);
						mList = mProjectPointBase.queryCraftParamDevID(mileage_page);
						mAdapter.setList(mList);
						mAdapter.notifyDataSetChanged();
						showImage(mileage_page);	
					}
				}
			} else {
				Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	private void showMileagePage(int mileage_page) {
		mileage_parameter = mProjectPointBase.getMileageParameter(mileage_page);
		setMileageView(mileage_parameter);
		mList = mProjectPointBase.queryCraftParamDevID(mileage_page);
		mAdapter.setList(mList);
		mAdapter.notifyDataSetChanged();
	}
	
	private void showImage(int mileage_page) {
		String sdcarddir = ExtSdCheck.getExtSDCardPath();
		String fileName = sdcarddir+ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/picture_"+mileage_page+".png";
		File file = new File(fileName);
		if (file.exists()) {
			Bitmap bm = BitmapFactory.decodeFile(fileName); 
			mImageView.setImageBitmap(bm); 
		} else {
			mImageView.setImageBitmap(null);
		}
	}
	
	/**
	 * 
	 */
	private void initView() {
		basisInfoView();
		drawPicInit();
		tableListInit();
		searchMileage();
		if (action.equals(ACTION_TRNSFER)) {
			craft_transfer_bt = (Button) findViewById(R.id.craft_data_transfer_bt);
			//		new sendThread().start();
//			uart_status = UART_CONNECTED;
			if (uart_status == UART_NOT_CONNECT)
			{
				craft_transfer_bt.setBackgroundColor(0xff888888); // color GRAY:0xff888888
				craft_transfer_bt.setClickable(false);
			}
			else
			{
				craft_transfer_bt.setOnClickListener(new TransferBtClick());
			}
		}
	}
	
	private void searchMileage() {
		craft_search_bt = (Button) findViewById(R.id.craft_search_bt);
		craft_search_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String sdcarddir = ExtSdCheck.getExtSDCardPath();
				if (sdcarddir == null) {
					Toast.makeText(context, "未检测到外部sd卡", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(CraftFilePageActivity.this, MileageGridViewActivity.class);
				intent.putExtra("project_name", proj_name_string);
				intent.putExtra("subproject_name", subproj_name_string);
				startActivity(intent);
			}
		});
	}   
	
	private boolean checkMileage1Format(String mileage1_string) {
		if (!mileage1_string.equals("") && (mileage1_string.length() >= 2) && (mileage1_string.startsWith("k") ||
				mileage1_string.startsWith("K"))) {
			String mileage1_val_string = mileage1_string.substring(1);
			System.out.println(mileage1_val_string);
			if (mileage1_val_string.matches("^[0-9]+(\\.[0-9]+)?$")) {
				return true;
			}
		}
		return false;
	}
	
	private void setMileageView(MileageParameter parameter) {
		if (parameter != null) {
			mileage_name = parameter.getMileageName();
			create_people = parameter.getCreatePeople();
			water_ratio = parameter.getWaterRatio();
			create_date = parameter.getCreateDate();
			anchor_count_value = parameter.getAnchorCount();
			craft_mileage_name.setText(mileage_name);
			anchor_count_tv.setText(anchor_count_value+"");
			craft_people_name.setText(create_people);
			water_ratio_tv.setText(water_ratio);
			craft_date_name.setText(create_date);
		}

	}
	
	class sendThread extends Thread {
		@Override
		public void run() {
			byte[] sendbuf = {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 
					0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F, 0x70,
					0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A, 0x41, 0x42, 0x43, 0x44,
					0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52,
					0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5A, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 
					0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F, 0x70,
					0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A, 0x41, 0x42, 0x43, 0x44,
					0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52,
					0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5A, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 
					0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F, 0x70,
					0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A, 0x41, 0x42, 0x43, 0x44,
					0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52,
					0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5A, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 
					0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F, 0x70,
					0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A, 0x41, 0x42, 0x43, 0x44,
					0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52,
					0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5A, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 
					0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F, 0x70,
					0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A, 0x41, 0x42, 0x43, 0x44,
					0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52,
					0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5A, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 
					0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F, 0x70,
					0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A, 0x41, 0x42, 0x43, 0x44,
					0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52,
					0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5A, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x61, 0x62, 
					0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F, 0x70,
					0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7A, 0x41, 0x42, 0x43, 0x44,
					0x45, 0x46, 0x47, 0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F, 0x50, 0x51, 0x52,
					0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5A};
			while (true) {
				uartInterface.WriteData(sendbuf.length, sendbuf);
			}
		}
	}
	
	// The Handler that gets information back from the BtService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case BTMESSAGE.MESSAGE_STATE_CHANGE:
            	AppUtil.log( "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BTMESSAGE.STATE_CONNECTED:
                	AppUtil.log( "=======connected======");
                	if (progressDialog!=null)
						progressDialog.dismiss();
                    break;
                case BTMESSAGE.STATE_CONNECTING:
                	AppUtil.log( "=======connecting======");
                    break;
                case BTMESSAGE.STATE_LISTEN:
                case BTMESSAGE.STATE_NONE:
                	AppUtil.log( "=======not_connected======");
                	uart_status = UART_NOT_CONNECT;
                	uartStatusTitle.setText(R.string.title_not_connected);
    				craft_transfer_bt.setBackgroundColor(0xff888888); // color GRAY:0xff888888
    				craft_transfer_bt.setClickable(false);
                    break;
                }
                break;
            case BTMESSAGE.MESSAGE_DEVICE_NAME:
                // save the connected device's name
                
                break;
            case BTMESSAGE.MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(BTMESSAGE.TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };	
	
	class TransferBtClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (uartControlSender == null)
			{
				uartControlSender = new UartControlSender(context, mHandler, mProjectPointBase);
				uartControlSender.setInstance(uartControlSender);
			}
			uartControlSender.start();
        	Intent intent=new Intent(context, CraftFileTransferActivity.class);
    		startActivity(intent);
		}
	}
	
    protected void onDestroy() {
    	if (mProjectBase != null) {
    		mProjectBase.closeDb();
    	}
    	if (mProjectPointBase != null) {
    		mProjectPointBase.closeDb();
    	}
    	if (uartControlSender != null) {
    		uartControlSender.stop();
    	}
    	context.unregisterReceiver(MainBdReceiver);
    	MyActivityManager.removeActivity(CraftFilePageActivity.this);
    	super.onDestroy();
    };
	
	private GestureDetector.OnGestureListener onGestureListener =   
			new GestureDetector.SimpleOnGestureListener() {  
			@Override  
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
								float velocityY) { 
				AppUtil.log( "onFling response----2.");
				float x = e2.getX() - e1.getX();  
				float y = e2.getY() - e1.getY();
				if (y < 100 & y > -100) {
					if (x > 200) {  
						doResult(RIGHT);  
					} else if (x < -200) {  
						doResult(LEFT);  
					}  
				}
	            return true;  
			}  
	};  

	public boolean onTouchEvent(MotionEvent event) {   
	    return gestureDetector.onTouchEvent(event);  
//		scrollview.onTouchEvent(event); //让ScrollView响应触碰事件
//		return false;
	}  
	
	public void doResult(int action) {  
		if (mProjectPointBase.openDb(project_id, subproject_id)) {
			int mileage_page_max = mProjectPointBase.getMileageCount();
			switch (action) {  
			case LEFT:  
				AppUtil.log( "go left");  
				if (mileage_page_max >= mileage_page+1) {
					mileage_parameter = mProjectPointBase.getMileageParameter(mileage_page+1);
					if (mileage_parameter != null) {
						mileage_page += 1;
						mileage_name = mileage_parameter.getMileageName();
						anchor_count_value = mileage_parameter.getAnchorCount();
						create_people = mileage_parameter.getCreatePeople();
						water_ratio = mileage_parameter.getWaterRatio();
						create_date = mileage_parameter.getCreateDate();
						craft_mileage_name.setText(mileage_name);
						anchor_count_tv.setText(anchor_count_value+"");
						craft_people_name.setText(create_people);
						water_ratio_tv.setText(water_ratio);
						craft_date_name.setText(create_date);
						mList = mProjectPointBase.queryCraftParamDevID(mileage_page);
						mAdapter.setList(mList);
						mAdapter.notifyDataSetChanged();
						showImage(mileage_page);	
					}
				}
				if (mileage_page >= mileage_page_max) {
					Toast.makeText(context, "已翻到最后一个注浆工段页面", Toast.LENGTH_SHORT).show();
				}
				break;  
				
			case RIGHT:  
				AppUtil.log( "go right");  
				if (mileage_page > 1) {
					mileage_parameter = mProjectPointBase.getMileageParameter(mileage_page-1);
					if (mileage_parameter != null) {
						mileage_page -= 1;
						mileage_name = mileage_parameter.getMileageName();
						anchor_count_value = mileage_parameter.getAnchorCount();
						create_people = mileage_parameter.getCreatePeople();
						water_ratio = mileage_parameter.getWaterRatio();
						create_date = mileage_parameter.getCreateDate();
						craft_mileage_name.setText(mileage_name);
						anchor_count_tv.setText(anchor_count_value+"");
						craft_people_name.setText(create_people);
						water_ratio_tv.setText(water_ratio);
						craft_date_name.setText(create_date);
						mList = mProjectPointBase.queryCraftParamDevID(mileage_page);
						mAdapter.setList(mList);
						mAdapter.notifyDataSetChanged();
						showImage(mileage_page);
					}
				}
				if (mileage_page <= 1) {
					Toast.makeText(context, "已翻到第一个注浆工段页面", Toast.LENGTH_SHORT).show();
				}
				break;  
			}  
			AppUtil.log( "mileage_page:"+mileage_page);
		} else {
			Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
		}
    } 
	
	private void basisInfoView() {
		eng_name_tv = (TextView) findViewById(R.id.craft_engin_name);

		craft_mileage_name = (TextView) findViewById(R.id.craft_mileage_name);
		
		craft_people_name = (TextView) findViewById(R.id.craft_people_name);
		
		craft_date_name = (TextView) findViewById(R.id.craft_date_name);
		
		anchor_count_tv = (TextView) findViewById(R.id.craft_anchor_amount_tv);
		
		water_ratio_tv = (TextView) findViewById(R.id.craft_water_ratio_tv);
		
		eng_name_tv.setText(proj_name_string+"->"+subproj_name_string);
		craft_mileage_name.setVisibility(View.VISIBLE);
		if (mileage_parameter != null) {
			craft_mileage_name.setText(mileage_name);
		}
		
		craft_date_name.setVisibility(View.VISIBLE);
		if (mileage_parameter != null) {
			craft_date_name.setText(create_date);
		}
		
		craft_people_name.setVisibility(View.VISIBLE);
		if (mileage_parameter != null) {
			craft_people_name.setText(create_people);
		}
		
		anchor_count_tv.setVisibility(View.VISIBLE);
		if (anchor_count_value > 0) {
			anchor_count_tv.setText(anchor_count_value+"");
		}
		
		water_ratio_tv.setVisibility(View.VISIBLE);
		if (mileage_parameter != null) {
			water_ratio_tv.setText(water_ratio);
		}
	}
	
	private void drawPicInit() {
		mImageView = (ImageView) findViewById(R.id.craft_imageview);
		String sdcarddir = ExtSdCheck.getExtSDCardPath();
		String fileName = sdcarddir+ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/picture_"+mileage_page+".png";
    	File file = new File(fileName);
    	if (file.exists()) {
    		Bitmap bm = BitmapFactory.decodeFile(fileName); 
    		mImageView.setImageBitmap(bm); 
    	}
	}
	
	private void tableListInit() { 
		mList = mProjectPointBase.queryCraftParamDevID(mileage_page);
		mListView = (CustomListView)findViewById(R.id.craft_table_lv);
		mAdapter = new CraftAnchorParameterAdapter(mList,this);
		mListView.setAdapter(mAdapter);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) { 
		gestureDetector.onTouchEvent(ev); //让GestureDetector响应触碰事件
		super.dispatchTouchEvent(ev); //让Activity响应触碰事件

		return false;  
	}
}
