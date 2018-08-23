package com.ysdata.grouter.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.ysdata.grouter.R;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.UIUtilities;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.database.SyncTimeDataBaseAdapter;
import com.ysdata.grouter.file.LogFileManager;
import com.ysdata.grouter.service.RemoteDataService;
import com.ysdata.grouter.storage.ExtSdCheck;
import com.ysdata.grouter.uart.FT311UARTDeviceManager;
import com.ysdata.grouter.uart.MyActivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity  extends Activity {
    private Context context = null;
    private ImageButton param_set_mb;
    private ImageButton data_collect_mb;
    private ImageButton data_manager_mb;
    private ImageButton manual_mb;
    private ImageButton admin_mb;
    private TextView main_title_tv;
    /* declare a FT311 UART interface variable */
	public FT311UARTDeviceManager uartInterface;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_phone);
        context = this;
        MyActivityManager.addActivity(MainActivity.this);
        initView();
        extSdCheck();
        ProjectPointDataBaseAdapter mDataBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
    	ProjectDataBaseAdapter mProjDataBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
    	SyncTimeDataBaseAdapter synctimebase = SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context);
    	if (mDataBase != null)
    		mDataBase.closeDb();
    	if (mProjDataBase != null)
    		mProjDataBase.closeDb();
    	if (synctimebase != null)
    		synctimebase.closeDb();
        uartInterface = FT311UARTDeviceManager.getSingleFT311UARTDeviceManager(context);
        LogFileManager.getInstance().create();
//        mDataBase.openDb(63, 73);
//        mDataBase.updateCraftUploadState(1, 0);
//        mDataBase.updateCraftUploadState(11, 0);
//        mDataBase.updateCraftUploadState(22, 0);
//        mDataBase.updateCraftUploadState(33, 0);
//        mDataBase.updateCollectData("K130+100-01", "");
//        mDataBase.updateCollectData("K130+100-05", "");
//        mDataBase.updateCollectData("K130+101-03", "");
//        mDataBase.closeDb();
//          AppUtil.log(mDataBase.getUploadParameterById(771321).getGroutingData());
//        caculateHoldTime();
//        AppUtil.log("========getDdsubprojsectionlist=========");
//        mDataBase.openDb(12, 25);
//        mDataBase.getDdsubprojsectionlist();
//        localGroutingDataTest();
//        padStringSaveToDbTest();
    }
    
    class PadStrcls {
    	String name;
    	String padString;
    	double[] press_time;
    	double[] press_val;
    	double cap_unit_hour;
    	double design_press;
    	
    	public PadStrcls(String name, String padString, double[] press_time, double[] press_val,
    			double cap_unit_hour, double design_press) {
    		this.name = name;
    		this.padString = padString;
    		this.press_time = press_time;
    		this.press_val = press_val;
    		this.cap_unit_hour = cap_unit_hour;
    		this.design_press = design_press;
    	}
    }
    
    private void padStringSaveToDbTest() {
    	ArrayList<PadStrcls> list = new ArrayList<MainActivity.PadStrcls>();
    	list.add(new PadStrcls("K162+832.6-01", "0087ff017500b6c5035100b87a01f500c705033a00c9f0020000cc9d034500ceaf020700d15c034300d3cb020600d620034e00d88f020200db7a033a00ddcf020600e0a0034600e33301f400e61e033500e89202090100820335010339020401056f033a010781020801092d0216",
    			new double[] {4.96, 6.59, 6.85, 7.26, 7.45, 7.62, 7.75, 7.91, 8.04, 8.18, 8.3, 8.47, 8.55, 8.69, 8.81, 8.94, 9.04, 9.17, 9.29, 9.45, 9.58, 9.71, 9.82, 9.97, 10.08, 10.24, 10.38, 10.51, 10.63, 10.79, 10.92, 11.09, 11.2, 11.34, 11.5, 11.66, 11.82, 11.98, 12.11, 22.05}, new double[] {
    			0.16, 0.34, 0.13, 0.38, 0.11, 0.47, 0.11, 0.52, 0.13, 0.34, 0.13, 0.4, 0.16, 0.48, 0.0, 0.49, 0.12, 0.4, 0.19, 0.5, 0.0 ,0.46, 0.05, 0.45, 0.07, 0.42, 0.07, 0.38, 0.12, 0.39, 0.1, 0.47, 0.04, 0.42, 0.08, 0.43, 0.06, 0.46, 0.17, 0.0 
    	}, 1000, 0.3));
    	list.add(new PadStrcls("K162+832.6-02", "000d1601790011d5033a004ddc0254", new double[]{3.35, 4.57, 19.93 }, 
    			new double[]{0.15, 0.33, 0.24 }, 1000, 0.3));
    	list.add(new PadStrcls("K162+832.6-03", "80de8000cb00de8200c3", new double[]{56.96}, new double[]{0.08}, 1000, 0.3));
    	list.add(new PadStrcls("K162+832.6-04", "00304500218073b7001c0073b90023", new double[]{12.36, 29.62 }, new double[]{0.01, 0.01 }, 1000, 0.3));
    	list.add(new PadStrcls("K162+832.6-05", "80521a008d00521c0089", new double[]{21.02 }, new double[]{0.06}, 1000, 0.3));
    	list.add(new PadStrcls("K162+832.6-06", "0022ea0024804c1200a5004c1400a8", new double[]{8.94, 19.47, 19.48}, new double[]{0.01, 0.07, 0.07  }, 1000, 0.3));
    	list.add(new PadStrcls("K162+832.6-07", "001bd8002600218c034a005b8c0223", new double[]{7.13, 8.59, 23.44 }, new double[]{0.02, 0.34, 0.22 }, 1000, 0.3));
    	ProjectPointDataBaseAdapter mDataBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
    	mDataBase.openDb(76, 86);
 /*   	int order_no = 1;
    	for (int i = 0; i < list.size(); i++) {
    		mDataBase.updateCollectData(list.get(i).name, list.get(i).padString);
    		if (mDataBase.getUploadParameter(order_no++).getGroutingData().equals(list.get(i).padString)) {
    			AppUtil.log(list.get(i).name + "-----updateCollectData-------ok");
    		} else {
    			AppUtil.log(list.get(i).name + "-----updateCollectData-------error");
    		}
    	}
    	double[] capacity_vals = {2.46, 2.46, 4.79, 4.79, 4.93, 4.93, 5.11, 5.11, 5.33, 5.33, 5.65, 5.65, 5.95, 5.95};
    	mDataBase.updateAnchorCollectCapacity("K162+834.2-02", capacity_vals, capacity_vals.length);*/
    	for (int i = 0; i < list.size(); i++) {
    		mDataBase.updateAnchorCollectData(list.get(i).name, list.get(i).design_press, list.get(i).cap_unit_hour, 
    				list.get(i).press_time, list.get(i).press_val, list.get(i).press_val.length , list.get(i).padString);
    	}
    	mDataBase.closeDb();
    }
    
    private void caculateHoldTime() {
    	double[] pressure_time = new double[1024];
		double[] pressure_val = new double[1024];
		int recv_time_high = 0;
		DecimalFormat df = new DecimalFormat("##.##");
		StringBuffer groutDataSb = new StringBuffer();
		int[] recv_data = {0x80, 0x1d, 0x85, 0x00, 0x08, 0x00, 0x1d, 0x87, 0x00, 0x14};
		int sb_index = 0;
		int node_len = 10;
		int index = 0;
		for (int i = 0; i < node_len; i+=5) {
			sb_index = index;
			recv_time_high = recv_data[index++];
			if ((recv_time_high & 0x80) == 0x80) { //手动停止注浆判断
				pressure_time[i/5] = ((recv_time_high & 0x7f) * 60 + ((recv_data[index++] << 8) + recv_data[index++]) * 0.001);
			} else {
				pressure_time[i/5] =  (recv_time_high * 60 + ((recv_data[index++] << 8) + recv_data[index++]) * 0.001);
			}
			pressure_val[i/5] = (((recv_data[index++] << 8) + recv_data[index++]) * 1.65) / 4095; //Mpa
			pressure_time[i/5] = Double.parseDouble(df.format(pressure_time[i/5]));
			pressure_val[i/5] = Double.parseDouble(df.format(pressure_val[i/5]));
			AppUtil.log( "pressure_time["+i/5+"]----------" + pressure_time[i/5]);
			AppUtil.log( "pressure_val["+i/5+"]----------" + pressure_val[i/5]);
			groutDataSb.append(Integer.toHexString((recv_data[sb_index++] & 0x000000FF) | 0xFFFFFF00).substring(6))
			.append(Integer.toHexString((recv_data[sb_index++] & 0x000000FF) | 0xFFFFFF00).substring(6))
			.append(Integer.toHexString((recv_data[sb_index++] & 0x000000FF) | 0xFFFFFF00).substring(6))
			.append(Integer.toHexString((recv_data[sb_index++] & 0x000000FF) | 0xFFFFFF00).substring(6))
			.append(Integer.toHexString((recv_data[sb_index++] & 0x000000FF) | 0xFFFFFF00).substring(6));
		}
		AppUtil.log(groutDataSb.toString());
    	ProjectPointDataBaseAdapter mDataBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
    	mDataBase.openDb(63, 73);
    	mDataBase.updateAnchorCollectData("K220+329-03", 
    			mDataBase.getCraftParameter("K220+329-03").getAnchorDesignPressure(), 
    			mDataBase.getCraftParameter("K220+329-03").getUnitHourCap(), pressure_time, pressure_val, 
    			node_len/5, groutDataSb.toString());
    }
    
    private void localGroutingDataTest() {
    	ProjectPointDataBaseAdapter mDataBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
    	mDataBase.openDb(85, 118);
    	double[] practice_press = {0.1, 0.12, 0.14, 0.17, 0.2, 0.29, 0.31, 0.25, 0.23, 0.2, 0.19, 
				  0.21, 0.29, 0.33, 0.28, 0.25, 0.23,  0.2,   0.15,   0.14,   0.17,   0.19,   0.21, 
				  0.21,   0.16,   0.13,   0.16,   0.19,   0.2,   0.23,   0.21,   0.15,   0.13,   0.16,   0.18,   0.21,
				  0.22,   0.23,   0.17,   0.15,   0.16,   0.19,   0.21,   0.23,   0.22,   0.16};
		double[] pressure_time = {  0.8,   1.0,   1.2,   1.4,   1.6,   1.8,   2.0,   2.2,   2.4,   2.6,   2.8,   3.0, 
				  3.2,   3.4,   3.6,   3.8,   4.0,   4.2,   4.4,   4.6,   4.8,   5.0,   5.2,   5.4,   5.6,   5.8,
				  6.0,   6.2,   6.4,    6.6,   6.8,   7.0,   7.2,   7.4,   7.6,   7.8,   8.0,
				  8.2,   8.4,   8.6,   8.8,   9.0,   9.2,   9.4,   9.6,   9.8};
		int orderno_max = mDataBase.getAnchorTotalCount();
		String anchor_name = "";
		for (int i = 1; i <= orderno_max; i++) {
			anchor_name = mDataBase.getCraftParameterByOrderno(i).getAnchorName();
			mDataBase.updateCraftUploadState(i, 0);
			mDataBase.updateCraftAnchorSlurryDate(anchor_name, "2018-01-02 08:20:23", (int)pressure_time[45]);
			mDataBase.updateAnchorCollectData(anchor_name, 0.2, 1000, pressure_time, practice_press, 46, "000a2401fc001146046300128402b4001af10469001c41029b0021ae04860022f0029b002b120495002c5402a20031c3044a00330502b200386704690039a902ac00406b04690041ad029800470f0477004851029e004c330464004d5f02c1");
		}
    }
    
    private void extSdCheck() {
    	ExtSdCheck.ExtSdCard = null;
    	String extSdPath = ExtSdCheck.getExtSDCardPath();
    	AppUtil.log("extSdPath:" + extSdPath);
    	if (extSdPath != null) {
    		/*new  AlertDialog.Builder(MainActivity.this)    
            .setTitle("提示" )
            .setIcon(android.R.drawable.ic_dialog_info)
            .setMessage("在使用该软件的过程中，请勿移除sd卡，否则部分功能将无法正常运行!" )  
            .setPositiveButton("确定" ,  null )   
            .create()
            .show();*/
    	} else {
    		new  AlertDialog.Builder(MainActivity.this)    
            .setTitle("提示" )
            .setIcon(android.R.drawable.ic_dialog_info)
            .setMessage("未检测到外置sd卡，部分功能将无法使用!" )  
            .setPositiveButton("确定" ,  null )   
            .create()
            .show();   
    	}
    }
    
    private void initView() {
    	main_title_tv = (TextView) findViewById(R.id.main_title_id);
    	param_set_mb = (ImageButton) findViewById(R.id.param_set_id);
        data_collect_mb = (ImageButton) findViewById(R.id.data_collect_id);
        data_manager_mb = (ImageButton) findViewById(R.id.data_manager_id);
        manual_mb = (ImageButton) findViewById(R.id.manual_id);
        admin_mb = (ImageButton) findViewById(R.id.admin_id);
        main_title_tv.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				AppUtil.log("========main title long click======");
				LayoutInflater factory = LayoutInflater.from(MainActivity.this);
				final View dialogView = factory.inflate(R.layout.version_info_dialog, null);
	        	AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
	        	dlg.setView(dialogView);
	        	dlg.create();
	        	dlg.show();
				return true;
			}
        });
        param_set_mb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String extSdPath = ExtSdCheck.getExtSDCardPath();
				if (extSdPath == null) {
					Toast.makeText(context, "未检测到外置sd卡", Toast.LENGTH_SHORT).show();
				} else {
					Intent intent=new Intent(MainActivity.this, ProjectTreeListView.class);
					intent.putExtra("action", "craft_transfer");
					startActivity(intent);
				}
			}
		});
        data_collect_mb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String extSdPath = ExtSdCheck.getExtSDCardPath();
				if (extSdPath == null) {
					Toast.makeText(context, "未检测到外置sd卡", Toast.LENGTH_SHORT).show();
				} else {
					Intent mIntent = new Intent(MainActivity.this, DeviceListActivity.class);
	        		startActivity(mIntent);
				}
			}
		});
        data_manager_mb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String extSdPath = ExtSdCheck.getExtSDCardPath();
				if (extSdPath == null) {
					Toast.makeText(context, "未检测到外置sd卡", Toast.LENGTH_SHORT).show();
				} else {
					ProjectDataBaseAdapter mProjectBase = 
							ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
					ArrayList<String> proj_list = new ArrayList<String>();
					if (mProjectBase.openDb()) {
						mProjectBase.getProjectNameList(proj_list);
						mProjectBase.closeDb();
						if (proj_list.size() > 0) {
							Intent intent = new Intent(MainActivity.this, ProjectTreeListView.class);
							intent.putExtra("action", "manager_list");
							startActivity(intent);
						} else {
							Toast.makeText(context, "还未创建任何合同段。", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
        manual_mb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ManualActivity.class);
//				Intent intent = new Intent(MainActivity.this, MileageGridViewActivity.class);
        		startActivity(intent);
			}
		});
        admin_mb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String extSdPath = ExtSdCheck.getExtSDCardPath();
				if (extSdPath == null) {
					Toast.makeText(context, "未检测到外置sd卡", Toast.LENGTH_SHORT).show();
				} else {
					RemoteDataService remoteDataService = RemoteDataService.getRemotDataInstance();
					if (remoteDataService.isNetworkConnected(context)) {
						Intent admin_intent=new Intent(MainActivity.this, AccountActivity.class);
						startActivity(admin_intent);
					} else {
						UIUtilities.showToast(MainActivity.this, R.string.network_not_detected);
					}
				}	
			}
		});
    }
    
	@Override
	protected void onResume() {
		// Ideally should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onResume();	
		if (!uartInterface.accessory_attached)
		{
			uartInterface.ResumeAccessory();
		}
	}
    
    @Override
    protected void onDestroy() {
    	ProjectPointDataBaseAdapter mDataBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
    	ProjectDataBaseAdapter mProjDataBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
    	SyncTimeDataBaseAdapter synctimebase = SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context);
    	if (mDataBase != null)
    		mDataBase.closeDb();
    	if (mProjDataBase != null)
    		mProjDataBase.closeDb();
    	if (synctimebase != null)
    		synctimebase.closeDb();
    	LogFileManager.close();
    	uartInterface.DestroyAccessory(true);
    	MyActivityManager.removeActivity(MainActivity.this);
    	super.onDestroy();
    }
}
