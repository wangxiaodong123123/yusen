package com.ysdata.blender.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.ysdata.blender.R;
import com.ysdata.blender.adapter.DdBlenderDataParameterAdapter;
import com.ysdata.blender.cloud.api.BlenderActiveData;
import com.ysdata.blender.cloud.api.BlenderActiveDataListResponse;
import com.ysdata.blender.cloud.api.IntegerListResponse;
import com.ysdata.blender.cloud.api.SubProjectPointCraftParameter;
import com.ysdata.blender.cloud.api.SubProjectPointGroutingParameter;
import com.ysdata.blender.cloud.api.SubProjectPointGroutingParameterListResponse;
import com.ysdata.blender.cloud.api.SubProjectPointListResponse;
import com.ysdata.blender.cloud.api.SubProjectPointTimeNodes;
import com.ysdata.blender.cloud.api.SubProjectSection;
import com.ysdata.blender.cloud.api.SubProjectSectionListResponse;
import com.ysdata.blender.cloud.util.ApiClient;
import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.cloud.util.ConstDef;
import com.ysdata.blender.cloud.util.SharedView;
import com.ysdata.blender.cloud.util.UIUtilities;
import com.ysdata.blender.database.FileOperator;
import com.ysdata.blender.database.ProjectDataBaseAdapter;
import com.ysdata.blender.database.ProjectPointDataBaseAdapter;
import com.ysdata.blender.database.SyncTimeDataBaseAdapter;
import com.ysdata.blender.element.DdBlenderDataParameter;
import com.ysdata.blender.file.LogFileManager;
import com.ysdata.blender.picture.utils.BitmapUtil;
import com.ysdata.blender.storage.ExtSdCheck;
import com.ysdata.blender.uart.MyActivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SyncBlenderDataListActivity extends Activity {
	
	private Context context = null;
	TextView title;
	private ListView mListView;
	ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	private ArrayList<DdBlenderDataParameter> list;
	private DdBlenderDataParameterAdapter mAdapter;
	private SyncTimeDataBaseAdapter synctimebase;
	private ApiClient apiClient;
	int subproject_id, project_id;
	TextView user_info_tv;
	Button reload_bt;
	boolean isLoading = true;
	int mileage_count = 0;
	private Activity activity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_blenderdata_list);
        context = this;
        activity = SyncBlenderDataListActivity.this;
        MyActivityManager.addActivity(SyncBlenderDataListActivity.this);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
		synctimebase = SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context);
		apiClient = ApiClient.getInstance(context);
		project_id = CacheManager.getDbProjectId();
		subproject_id = CacheManager.getDbSubProjectId();
		mProjectBase.closeDb();
		mProjectPointBase.closeDb();
		list = new ArrayList<DdBlenderDataParameter>();
		if (mProjectBase.openDb() && mProjectPointBase.openDb(project_id, subproject_id) && synctimebase.openDb()) {
			InitView();
			NetHandler.sendEmptyMessage(MSG_START_DOWNLOAD_START);
		} else {
			Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
		}
    }

    private void updateListView() {
    	mProjectPointBase.getDdBlenderDataList(list);
		if (list != null) {
			mAdapter = new DdBlenderDataParameterAdapter(list, context);
			mListView.setAdapter(mAdapter);
		}
    }
    
	private void clearImages() {
		FileOperator.getSingleFileOperator(context).DeleteAllDir(ExtSdCheck.getExtSDCardPath()+
				ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/");
	}
	
	class SyncBlenderDataThread extends Thread {
    	@Override
    	public void run() {
    		int pageIndex = 1;
    		int pageSize = 10;
    		boolean isSyncSuccess = true; //获取服务端数据数据成功
    		boolean isSyncLocal = false; //本地数据库是否更新
    		BlenderActiveDataListResponse blender_data_list = null;
    		BlenderActiveData blender_data = null;
    		boolean hasMore = true;
    		String sync_time = synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_BLENDER_DATA);
    		
    		if (sync_time.equals("2010-01-01 00:00:00")) {
    			mProjectPointBase.deleteAllBlenderDataRecord();
    		} 
    		isSyncLocal = false;
			pageIndex = 1;
			hasMore = true;
			do {
				blender_data_list = apiClient.getAsyncBlenderDataList(subproject_id, pageIndex, pageSize);
				if (blender_data_list != null && blender_data_list.isSuccess) {
					int blender_data_list_size = 0;
					if (blender_data_list.Data != null) {
						blender_data_list_size = blender_data_list.Data.size();
						if (blender_data_list_size != blender_data_list.total) {
							AppUtil.log("blender_data_list_size:"+blender_data_list_size + " blender_data_list.total:"+
									blender_data_list.total);
						}
					}
					for (int i = 0; i < blender_data_list_size; i++) {
						blender_data = blender_data_list.Data.get(i);
						mProjectPointBase.insertMixCollectRecord(blender_data.intOrder, blender_data.dblMixRatioWater, 
								blender_data.strDate, blender_data.strBeginTime, blender_data.strEndTime, 
								blender_data.dblCementCount, blender_data.dblPosition);
						mProjectPointBase.updateMixCollectPicScene(blender_data.intOrder, blender_data.strActiveImageData);
						mProjectPointBase.updateMixCollectPicResult(blender_data.intOrder, blender_data.strDesignImageData);
					}
					if (blender_data_list_size > 0) {
						isSyncLocal = true;
					}
					isSyncSuccess = true;
					pageIndex++;
					hasMore = blender_data_list.HasMore;
					NetHandler.sendEmptyMessage(MSG_SYNC_UPDATE_LISTVIEW);
				} else {
					if (blender_data_list != null && !blender_data_list.isSuccess) {
						CacheManager.setNetErrorMsg(blender_data_list.errorString);
					}
					isSyncSuccess = false;
					break;
				}
			} while(hasMore);
    		
    		if (isSyncSuccess) {
    			if (isSyncLocal) {
    				synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_BLENDER_DATA, apiClient.getCurrentDateTime());
    			}
    			NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_SUCCESS, isSyncLocal ? 1 : 0));
    		} else {
    			NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_FAILED, isSyncLocal ? 1 : 0));
    		}
    		super.run();
    	}
    }
	
	private final static int MSG_SYNC_SECTION_LIST_SUCCESS = 1;
	private final static int MSG_SYNC_SECTION_PICTURE_SUCCESS = 2;
	private final static int MSG_SYNC_SUCCESS = 3;
	private final static int MSG_SYNC_FAILED = 4;
	private final static int MSG_PROGRESSBAR_CANCEL = 5;
	private final static int MSG_NET_ERROR = 6;
	private final static int MSG_SYNC_SECTION_PICTURE_START = 7;
	private final static int MSG_SYNC_SECTION_POINT_START = 8;
	private final static int MSG_UPDATE_SECTION_POINT_ADAPTER = 9;
	private final static int MSG_SYNC_GROUTING_DATA_SUCCESS = 10;
	private final static int MSG_SYNC_SECTION_LIST_START = 11;
	private final static int MSG_SYNC_SECTION_LIST_PAGE_SUCCESS = 12;
	private final static int MSG_SYNC_PARTS_OF_SECTION_FAILED = 13;
	private final static int MSG_SYNC_DEL_POINTS_SUCCESS = 14;
	private final static int MSG_SYNC_DEL_POINTS_FAILED = 15;
	private final static int MSG_SYNC_INSERT_POINTS_SUCCESS = 16;
	private final static int MSG_SYNC_INSERT_POINTS_FAILED = 17;
	private final static int MSG_SYNC_POINTS_SUCCESS = 18;
	private final static int MSG_SYNC_POINTS_ALL_SUCCESS = 19;
	private final static int MSG_SYNC_POINTS_FAILED = 20;
	private final static int MSG_SYNC_GROUTING_ALL_DATA_SUCCESS = 21;
	private final static int MSG_SYNC_GROUTING_ALL_DATA_FAILED = 22;
	private final static int MSG_INIT_LISTVIEW_START = 23;
	private final static int MSG_INIT_LISTVIEW_SUCCESS = 24;
	private final static int MSG_SYNC_SECTION_PICTURE_FAILED = 25;
	private final static int MSG_RELOAD_BTN_CLICKED = 26;
	private final static int MSG_START_DOWNLOAD_START = 27;
	private final static int MSG_SYNC_UPDATE_LISTVIEW = 28;
	
	
	 private final Handler NetHandler = new Handler() { 
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
			 	case MSG_START_DOWNLOAD_START:
			 		new initListViewThread().start();
			 		break;
			 
			 	case MSG_INIT_LISTVIEW_START:
			 		SharedView.showProgressBar(activity, "正在同步本地工搅拌数......");
			 		break;
			 
			 	case MSG_INIT_LISTVIEW_SUCCESS:
			 		@SuppressWarnings("unchecked")
					ArrayList<DdBlenderDataParameter> mlist = (ArrayList<DdBlenderDataParameter>)msg.obj;
			 		if (mlist != null) {
			 			mAdapter = new DdBlenderDataParameterAdapter(mlist, SyncBlenderDataListActivity.this);
						mListView.setAdapter(mAdapter);
			 		}
			 		new SyncBlenderDataThread().start();
			 		break;
			 		
			 	case MSG_SYNC_UPDATE_LISTVIEW:	
			 		updateListView();
			 		break;
			 		
				case MSG_RELOAD_BTN_CLICKED:
					int record_count = mProjectPointBase.getMixCollectCount();
		    		if (record_count > 0) {
		    			SharedView.showProgressBar(SyncBlenderDataListActivity.this, "正在清除该工程下的搅拌数据......");
		    			
		    			mProjectPointBase.deleteAllBlenderDataRecord();
						clearImages();
						synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_BLENDER_DATA, "2010-01-01 00:00:00");
		    			
						//show list after clearing
						updateListView();
		    			SharedView.showDynamicMessage("本地搅拌数据清除完成！");
		    			try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		    			SharedView.showDynamicMessage("正在同步网络工段列表......");
		    		} else {
		    			SharedView.showProgressBar(SyncBlenderDataListActivity.this, "正在同步网络搅拌数据......");
		    		}
		    		new SyncBlenderDataThread().start();
					break;	
			 
			 	case MSG_SYNC_SECTION_LIST_START:
			 		SharedView.showDynamicMessage("正在同步网络工段列表......");
			 		break;
			 		
				case MSG_SYNC_SECTION_LIST_PAGE_SUCCESS:
					SharedView.showDynamicMessage("正在同步工段列表,已下载到:"+(String)msg.obj);
//					mileage_count = mProjectPointBase.getMileageCount();
//					updateListView();
					break;
					
				case MSG_SYNC_DEL_POINTS_SUCCESS:
					SharedView.showDynamicMessage("删除工点同步成功,开始同步插入的工点......");
					try {
						Thread.sleep(1000); //delay for our observation
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					SharedView.showDynamicMessage("正在同步插入的工点......");
					break;
					
				case MSG_SYNC_DEL_POINTS_FAILED:
					SharedView.cancelProgressBar();
//					SharedView.showsAlertDialog(activity, "删除工点同步失败,同步终止!");
					if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
						SharedView.showsAlertDialog(activity, "删除工点同步失败,同步终止:用户已过程，请重新登录！");
					} else {
						SharedView.showsAlertDialog(activity, "删除工点同步失败,同步终止:"+CacheManager.getNetErrorMsg());
					}
					isLoading = false;
					break;
					
				case MSG_SYNC_INSERT_POINTS_SUCCESS:
					SharedView.showDynamicMessage("插入工点同步成功,开始同步新增加的工点......");
					try {
						Thread.sleep(1000); //delay for our observation
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					SharedView.showDynamicMessage("插入工点同步成功,正在同步新增加的工点......");
					break;
					
				case MSG_SYNC_INSERT_POINTS_FAILED:
					SharedView.cancelProgressBar();
//					SharedView.showsAlertDialog(activity, "插入工点同步失败,同步终止!");
					if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
						SharedView.showsAlertDialog(activity, "插入工点同步失败,同步终止:用户已过程，请重新登录！");
					} else {
						SharedView.showsAlertDialog(activity, "插入工点同步失败,同步终止:"+CacheManager.getNetErrorMsg());
					}
					isLoading = false;
					break;	
					
				case MSG_SYNC_POINTS_SUCCESS:
					SharedView.showDynamicMessage("正在同步新增加的工点,已下载到:"+(String)msg.obj);
					break;
					
				case MSG_SYNC_GROUTING_DATA_SUCCESS:
					SharedView.showDynamicMessage("正在同步工点注浆数据,已下载到:"+(String)msg.obj);
					break;
					
				case MSG_SYNC_SUCCESS:
					int updateLocal = (Integer) msg.obj;
					if (updateLocal == 1) {
						mProjectPointBase.setCollectOrderNo();
					}
					SharedView.cancelProgressBar();
					SharedView.showsAlertDialog(activity, "搅拌数据同步成功!");
					isLoading = false;
					break;
					
				case MSG_SYNC_FAILED:
					SharedView.cancelProgressBar();
					if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
						SharedView.showsAlertDialog(activity, "搅拌数据同步失败:用户已过程，请重新登录！");
					} else {
						SharedView.showsAlertDialog(activity, "搅拌数据同步失败:"+CacheManager.getNetErrorMsg());
					}
					isLoading = false;
					break;	
					
				case MSG_SYNC_PARTS_OF_SECTION_FAILED:
					updateListView();
					SharedView.cancelProgressBar();
					UIUtilities.showToast(activity, (String)msg.obj, true);
					isLoading = false;
					SharedView.showUserExpiredAlertDialog(SyncBlenderDataListActivity.this);
					break;
					
				case MSG_SYNC_SECTION_PICTURE_START:
					SharedView.showProgressBar(activity, "正在同步工点位置图示......");
//					new SyncSectionImageThread().start();
					break;
					
				case MSG_SYNC_SECTION_POINT_START:
					SharedView.showProgressBar(activity, "正在同步网络工点数据......");
					break;
					
				case MSG_UPDATE_SECTION_POINT_ADAPTER:
					break;
					
				case MSG_PROGRESSBAR_CANCEL:
					SharedView.cancelProgressBar();
					isLoading = false;
					break;
					
				case MSG_NET_ERROR:
					UIUtilities.showToast(activity, "网络错误", true);
					break;
					
			default:
				break;
			}
		 };
	 };
	 
	    public ArrayList<Integer> getPictureItemList() {  
	    	String sdcarddir = ExtSdCheck.getExtSDCardPath();
			String dirPath = sdcarddir+ConstDef.DATABASE_PATH+CacheManager.getDbProjectId()+"/"+
					CacheManager.getDbSubProjectId()+"/picture/";
	    	ArrayList<Integer> items = new ArrayList<Integer>();  
	        try{  
	            File f = new File(dirPath);  
	            File[] files = f.listFiles();// 列出所有文件  
	          
	            // 将所有文件存入list中  
	            if(files != null){  
	                int count = files.length;// 文件个数  
	                for (int i = 0; i < count; i++) {  
	                    File file = files[i]; 
	                    String filename  = file.getName();
	                    if(filename.endsWith(".png") || filename.startsWith("picture_"))
	                    {
	                    	String mileage_page_string = filename.split("\\.")[0].substring(8);
	                    	if (mileage_page_string.matches("^[0-9]*[1-9][0-9]*$"))
	                    		items.add(Integer.parseInt(mileage_page_string));  
	                    }
	                }  
	            }
	        } catch(Exception ex) {  
	            ex.printStackTrace();  
	        }  
	        return items;
	    }
	    
    protected void onDestroy() {
    	MyActivityManager.removeActivity(SyncBlenderDataListActivity.this);
    	super.onDestroy();
    };
    
    @Override
    protected void onStart() {
    	AppUtil.log( "onStart.");
    	super.onStart();
    }
    
    @Override
    protected void onRestart() {
    	AppUtil.log( "onRestart.");
    	super.onRestart();
    }
    
    @Override
    protected void onResume() {
    	AppUtil.log( "onResume.");
    	super.onResume();
    }
    
	private void InitView() {
		mListView = (ListView)findViewById(R.id.blenderdata_listview);
		title = (TextView)findViewById(R.id.sectionlist_title);
		String proj_name = mProjectBase.getProjectName(project_id);
		String subproj_name = mProjectBase.getSubProjectName(subproject_id);
		title.setText("云服务->"+proj_name+"->"+subproj_name+"->搅拌数据");
		user_info_tv = (TextView) findViewById(R.id.user_info);
		user_info_tv.setText(CacheManager.getUserName() + " 欢迎你!");
		reload_bt = (Button) findViewById(R.id.reload_bt);
		reload_bt.setOnClickListener(new ReloadClickListener());
	}
	
	class ReloadClickListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (isLoading) return;
			boolean doAllUpload = true;
			String msg = "";
			if (doAllUpload) {
				msg = "该操作首先会清除该工程下所有已下载的搅拌数据，然后才进行重新下载，建议用户在数据同步失败的情况下使用该功能，是否继续?";
			} else {
				msg = "检测到部分搅拌数据未上传到平台，建议上传后，再使用该功能，是否继续?";
			}
			new  AlertDialog.Builder(SyncBlenderDataListActivity.this)    
            .setTitle("提示" )
            .setIcon(android.R.drawable.ic_dialog_info)
            .setMessage(msg )  
            .setPositiveButton("继续" , new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					isLoading = true;
					new reloadClickThread().start();
					AppUtil.log("=======ReloadClickListener==========");
				}
			} )   
            .setNegativeButton("取消", null)
            .create()
            .show();
		}
	}
	
	class reloadClickThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			NetHandler.sendEmptyMessage(MSG_RELOAD_BTN_CLICKED);
		}
	}
	
	class initListViewThread extends Thread {
		@Override
		public void run() {
			super.run();
			NetHandler.sendEmptyMessage(MSG_INIT_LISTVIEW_START);
			mProjectPointBase.getDdBlenderDataList(list);
			NetHandler.sendMessage(NetHandler.obtainMessage(MSG_INIT_LISTVIEW_SUCCESS, list));
		}
	}
}

