package com.ysdata.steelarch.activity;

import java.io.File;
import java.util.ArrayList;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.adapter.DdSteelArchDataParameterAdapter;
import com.ysdata.steelarch.cloud.api.IntegerListResponse;
import com.ysdata.steelarch.cloud.api.SteelArchCollectData;
import com.ysdata.steelarch.cloud.api.SteelArchCollectDataListResponse;
import com.ysdata.steelarch.cloud.api.SteelArchCraftData;
import com.ysdata.steelarch.cloud.api.SteelArchCraftDataListResponse;
import com.ysdata.steelarch.cloud.util.ApiClient;
import com.ysdata.steelarch.cloud.util.AppUtil;
import com.ysdata.steelarch.cloud.util.CacheManager;
import com.ysdata.steelarch.cloud.util.ConstDef;
import com.ysdata.steelarch.cloud.util.SharedView;
import com.ysdata.steelarch.cloud.util.UIUtilities;
import com.ysdata.steelarch.database.ProjectDataBaseAdapter;
import com.ysdata.steelarch.database.ProjectPointDataBaseAdapter;
import com.ysdata.steelarch.database.SyncTimeDataBaseAdapter;
import com.ysdata.steelarch.element.DdSteelArchDataParameter;
import com.ysdata.steelarch.file.ImageData;
import com.ysdata.steelarch.storage.ExtSdCheck;
import com.ysdata.steelarch.uart.MyActivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SyncSteelArchDataListActivity extends Activity {
	
	private Context context = null;
	TextView title;
	private ListView mListView;
	ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	private ArrayList<DdSteelArchDataParameter> list;
	private DdSteelArchDataParameterAdapter mAdapter;
	private SyncTimeDataBaseAdapter synctimebase;
	private ApiClient apiClient;
	int subproject_id, project_id;
	TextView user_info_tv;
	Button reload_bt;
	boolean isLoading = true;
	int steelarch_count = 0;
	private Activity activity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_steelarchdata_list);
        context = this;
        activity = SyncSteelArchDataListActivity.this;
        MyActivityManager.addActivity(SyncSteelArchDataListActivity.this);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
		synctimebase = SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context);
		apiClient = ApiClient.getInstance(context);
		project_id = CacheManager.getDbProjectId();
		subproject_id = CacheManager.getDbSubProjectId();
		mProjectBase.closeDb();
		mProjectPointBase.closeDb();
		list = new ArrayList<DdSteelArchDataParameter>();
		if (mProjectBase.openDb() && mProjectPointBase.openDb(project_id, subproject_id) && synctimebase.openDb()) {
			InitView();
			SharedView.showProgressBar(activity, "正在读取本地钢拱架数距......");
			produceTestData();
			updateListView();
			SharedView.cancelProgressBar();
//			NetHandler.sendEmptyMessage(MSG_START_DOWNLOAD_START);
		} else {
			Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
		}
    }
    
    private void produceTestData() {
		/***************************test**************************/
		mProjectPointBase.insertSteelArchCraftDataRecord(1, "k100+1", 100001, 2, 1, 3);
		mProjectPointBase.insertSteelArchCraftDataRecord(2, "k100+2", 100002, 1, 2, 3);
		mProjectPointBase.insertSteelArchCraftDataRecord(3, "k100+3", 100003, 1, 3, 3);
		mProjectPointBase.insertSteelArchCraftDataRecord(4, "k100+4", 100004, 1, 4, 3);
		mProjectPointBase.insertSteelArchCraftDataRecord(5, "k100+5", 100005, 1, 5, 3);
		mProjectPointBase.insertSteelArchCraftDataRecord(6, "k100+6.5", 100006.5, 1.5, 1, 3);
		mProjectPointBase.insertSteelArchCraftDataRecord(7, "k100+7", 100007, 0.5, 0.5, 7);
		mProjectPointBase.insertSteelArchCraftDataRecord(8, "k100+8.2", 100008.2, 1.2, 8.2, 3);
		mProjectPointBase.insertSteelArchCraftDataRecord(9, "k100+9", 100009, 0.8, 9, 3);
		mProjectPointBase.insertSteelArchCraftDataRecord(10, "k100+11", 100011, 2, 11, 3);
		mProjectPointBase.insertSteelArchCraftDataRecord(11, "k100+12.3", 100012.3, 1.3, 12.3, 3);
		mProjectPointBase.setSteelArchOrderNo();
		
		/*		
		String pic_string = ImageData.IMAGE_TEST_DATA;
		mProjectPointBase.updateLeftSteelArchCollectRecord(1, "2018-01-02 07:20", 1.2, 7.1, 10.2);
		mProjectPointBase.updateRightSteelArchCollectRecord(1, "2018-01-02 07:30", 1.3, 7.5, 10.8);
		mProjectPointBase.updateLeftSteelArchCollectRecord(2, "2018-01-02 18:20", 1.2, 7.1, 10.2);
		mProjectPointBase.updateRightSteelArchCollectRecord(2, "2018-01-02 18:30", 1.3, 7.5, 10.8);
		mProjectPointBase.updateLeftSteelArchCollectRecord(3, "2018-01-03 07:20", 1.2, 7.1, 10.2);
		mProjectPointBase.updateRightSteelArchCollectRecord(3, "2018-01-03 07:30", 1.3, 7.5, 10.8);
		mProjectPointBase.updateLeftSteelArchCollectRecord(4, "2018-01-04 07:20", 1.2, 7.1, 10.2);
		mProjectPointBase.updateRightSteelArchCollectRecord(4, "2018-01-04 07:30", 1.3, 7.5, 10.8);
		mProjectPointBase.updateLeftSteelArchCollectRecord(5, "2018-01-05 07:20", 1.2, 7.1, 10.2);
		mProjectPointBase.updateRightSteelArchCollectRecord(5, "2018-01-05 07:30", 1.3, 7.5, 10.8);
		mProjectPointBase.updateLeftSteelArchCollectRecord(6, "2018-01-07 09:20", 1.2, 7.1, 10.2);
		mProjectPointBase.updateRightSteelArchCollectRecord(6, "2018-01-07 09:30", 1.3, 7.5, 10.8);	
		mProjectPointBase.updateLeftSteelArchCollectRecord(7, "2018-01-08 09:20", 1.2, 7.1, 10.2);
		mProjectPointBase.updateRightSteelArchCollectRecord(7, "2018-01-08 09:30", 1.3, 7.5, 10.8);				
		mProjectPointBase.updateSteelArchCollectLeftPicDirEntrance(1, pic_string);
		mProjectPointBase.updateSteelArchCollectLeftPicDirTunnelFace(1, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirEntrance(1, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirTunnelFace(1, pic_string);
		mProjectPointBase.updateSteelArchCollectLeftPicDirEntrance(2, pic_string);
		mProjectPointBase.updateSteelArchCollectLeftPicDirTunnelFace(2, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirEntrance(2, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirTunnelFace(2, pic_string);
		mProjectPointBase.updateSteelArchCollectLeftPicDirEntrance(3, pic_string);
		mProjectPointBase.updateSteelArchCollectLeftPicDirTunnelFace(3, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirEntrance(3, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirTunnelFace(3, pic_string);
		mProjectPointBase.updateSteelArchCollectLeftPicDirEntrance(4, pic_string);
		mProjectPointBase.updateSteelArchCollectLeftPicDirTunnelFace(4, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirEntrance(4, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirTunnelFace(4, pic_string);
		mProjectPointBase.updateSteelArchCollectLeftPicDirEntrance(5, pic_string);
		mProjectPointBase.updateSteelArchCollectLeftPicDirTunnelFace(5, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirEntrance(5, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirTunnelFace(5, pic_string);
		mProjectPointBase.updateSteelArchCollectLeftPicDirEntrance(6, pic_string);
		mProjectPointBase.updateSteelArchCollectLeftPicDirTunnelFace(6, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirEntrance(6, pic_string);
		mProjectPointBase.updateSteelArchCollectRightPicDirTunnelFace(6, pic_string);
	*/	
		/*********************************************************/
    }

    private void updateListView() {
    	mProjectPointBase.getDdSteelArchDataList(list);
		if (list != null) {
			mAdapter = new DdSteelArchDataParameterAdapter(list, context);
			mListView.setAdapter(mAdapter);
		}
    }
    
	class SyncSteelArchDataThread extends Thread {
    	@Override
    	public void run() {
    		int pageIndex = 1;
    		int pageSize = 10;
    		boolean isSyncSuccess = true; //获取服务端数据数据成功
    		boolean isSyncLocal = false; //本地数据库是否更新
    		boolean isDdAll = true;
    		SteelArchCraftDataListResponse steelarch_craft_data_list = null;
    		SteelArchCraftData steelarch_craft_data = null;
    		SteelArchCollectDataListResponse steelarch_collect_data_list = null;
    		SteelArchCollectData steelarch_collect_data = null;
    		boolean hasMore = true;
    		
    		String sync_time = synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_STEELARCH_CRAFT_DATA);
    		if (steelarch_count == 0 || sync_time.equals("2010-01-01 00:00:00") ) {
    			mProjectPointBase.deleteSteelArchDataRecord();
    			synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_STEELARCH_CRAFT_DATA, "2010-01-01 00:00:00");
    			synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_STEELARCH_COLLECT_DATA, "2010-01-01 00:00:00");
    			isDdAll = true;
    		} else {
    			isDdAll = false;
    		}
    		isSyncLocal = false;
			pageIndex = 1;
			hasMore = true;
			NetHandler.sendEmptyMessage(MSG_SYNC_DEL_STEELARCH_START);
			if (!isDdAll) {
    			do {
    				IntegerListResponse integer_list = apiClient.getSyncDeleteSteelArchData(subproject_id, pageIndex);
    				if (integer_list != null && integer_list.isSuccess) {
    					int integer_list_size = 0;
    					if (integer_list.Data != null) {
    						integer_list_size = integer_list.Data.size();
    						if (integer_list_size != integer_list.total) {
    							AppUtil.log("integer_list_size:"+integer_list_size + " integer_list.total:"+
    									integer_list.total);
    						}
    					}
    					if (integer_list_size > 0) {
    						for (int i = 0; i < integer_list_size; i++) {
    							mProjectPointBase.deleteSteelArchData(i);
    						}
    						isSyncLocal = true;
    					}
    					isSyncSuccess = true;
    					pageIndex++;
    					hasMore = integer_list.HasMore;
    				} else {
    					if (integer_list != null && !integer_list.isSuccess) {
    						CacheManager.setNetErrorMsg(integer_list.errorString);
    					}
    					isSyncSuccess = false;
    					break;
    				}
    			} while(hasMore);
    		}
			if (isSyncSuccess) {
				synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_STEELARCH_DEL_DATA, apiClient.getCurrentDateTime());
				NetHandler.sendEmptyMessage(MSG_SYNC_DEL_STEELARCH_SUCCESS);
				do {
					steelarch_craft_data_list = apiClient.getAsyncSteelArchCraftDataList(subproject_id, pageIndex, pageSize);
					if (steelarch_craft_data_list != null && steelarch_craft_data_list.isSuccess) {
						int steelarch_craft_data_list_size = 0;
						if (steelarch_craft_data_list.Data != null) {
							steelarch_craft_data_list_size = steelarch_craft_data_list.Data.size();
							if (steelarch_craft_data_list_size != steelarch_craft_data_list.total) {
								AppUtil.log("steelarch_craft_data_list_size:"+steelarch_craft_data_list_size + 
										" steelarch_craft_data_list.total:"+ steelarch_craft_data_list.total);
							}
						}
						for (int i = 0; i < steelarch_craft_data_list_size; i++) {
							steelarch_craft_data = steelarch_craft_data_list.Data.get(i);
							mProjectPointBase.insertSteelArchCraftDataRecord(steelarch_craft_data.id, steelarch_craft_data.name,
									steelarch_craft_data.nameMeter, steelarch_craft_data.designDistance, 
									steelarch_craft_data.entranceDistance, steelarch_craft_data.secondCarWidth);
						}
						if (steelarch_craft_data_list_size > 0) {
							isSyncLocal = true;
						}
						isSyncSuccess = true;
						pageIndex++;
						hasMore = steelarch_craft_data_list.HasMore;
						if (steelarch_craft_data_list_size > 0) {
	    					NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_STEELARCH_CRAFT_DATA_PAGE_SUCCESS, 
	    							steelarch_craft_data_list.Data.get(steelarch_craft_data_list_size-1).name));
	    				}
					} else {
						if (steelarch_craft_data_list != null && !steelarch_craft_data_list.isSuccess) {
							CacheManager.setNetErrorMsg(steelarch_craft_data_list.errorString);
						}
						isSyncSuccess = false;
						break;
					}
				} while(hasMore);
				
				if (isSyncSuccess) {
					synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_STEELARCH_CRAFT_DATA, apiClient.getCurrentDateTime());
					NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_STEELARCH_CRAFT_DATA_SUCCESS, isSyncLocal ? 1 : 0));
					do {
						steelarch_collect_data_list = apiClient.getAsyncSteelArchCollectDataList(subproject_id, pageIndex, pageSize);
						if (steelarch_collect_data_list != null && steelarch_collect_data_list.isSuccess) {
							int steelarch_collect_data_list_size = 0;
							if (steelarch_collect_data_list.Data != null) {
								steelarch_collect_data_list_size = steelarch_collect_data_list.Data.size();
								if (steelarch_collect_data_list_size != steelarch_collect_data_list.total) {
									AppUtil.log("steelarch_collect_data_list_size:"+steelarch_collect_data_list_size + 
											" steelarch_collect_data_list.total:"+ steelarch_collect_data_list.total);
								}
							}
							for (int i = 0; i < steelarch_collect_data_list_size; i++) {
								steelarch_collect_data = steelarch_collect_data_list.Data.get(i);
								mProjectPointBase.updateSteelArchCollectLeftParameterWhereId(steelarch_collect_data.id, 
										steelarch_collect_data.leftMeasureDate, steelarch_collect_data.leftMeasureDistance, 
										steelarch_collect_data.leftSecondCarDistance);
								mProjectPointBase.updateSteelArchCollectRightParameterWhereId(steelarch_collect_data.id, 
										steelarch_collect_data.rightMeasureDate, steelarch_collect_data.rightMeasureDistance, 
										steelarch_collect_data.rightSecondCarDistance);
								mProjectPointBase.updateSteelArchCollectLeftPicDirEntranceWhereId(steelarch_collect_data.id, 
										steelarch_collect_data.leftPicDirEntrance);
								mProjectPointBase.updateSteelArchCollectRightPicDirEntranceWhereId(steelarch_collect_data.id, 
										steelarch_collect_data.rightPicDirEntrance);
								mProjectPointBase.updateSteelArchCollectLeftPicDirTunnelFaceWhereId(steelarch_collect_data.id, 
										steelarch_collect_data.leftPicDirTunnelface);
								mProjectPointBase.updateSteelArchCollectRightPicDirTunnelFaceWhereId(steelarch_collect_data.id, 
										steelarch_collect_data.rightPicDirTunnelface);
							}
							if (steelarch_collect_data_list_size > 0) {
								isSyncLocal = true;
							}
							isSyncSuccess = true;
							pageIndex++;
							hasMore = steelarch_collect_data_list.HasMore;
							if (steelarch_collect_data_list_size > 0) {
		    					NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_STEELARCH_COLLECT_DATA_PAGE_SUCCESS, 
		    							steelarch_collect_data_list.Data.get(steelarch_collect_data_list_size-1).name));
		    				}
						} else {
							if (steelarch_craft_data_list != null && !steelarch_craft_data_list.isSuccess) {
								CacheManager.setNetErrorMsg(steelarch_craft_data_list.errorString);
							}
							isSyncSuccess = false;
							break;
						}
					} while(hasMore);
					if (isSyncSuccess) {
		    			if (isSyncLocal) {
		    				synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_STEELARCH_COLLECT_DATA, apiClient.getCurrentDateTime());
		    			}
		    			NetHandler.sendEmptyMessage(MSG_SYNC_STEELARCH_COLLECT_DATA_SUCCESS);
		    		} else {
		    			NetHandler.sendEmptyMessage(MSG_SYNC_STEELARCH_COLLECT_DATA_FAILED);
		    		}
				} else {
					NetHandler.sendEmptyMessage(MSG_SYNC_STEELARCH_CRAFT_DATA_FAILED);
				}
			} else {
				NetHandler.sendEmptyMessage(MSG_SYNC_DEL_STEELARCH_FAILED);
			}
    		super.run();
    	}
    }
	
	private final static int MSG_NET_ERROR = 1;
	private final static int MSG_INIT_LISTVIEW_START = 2;
	private final static int MSG_INIT_LISTVIEW_SUCCESS = 3;
	private final static int MSG_RELOAD_BTN_CLICKED = 4;
	private final static int MSG_START_DOWNLOAD_START = 5;
	private final static int MSG_SYNC_DEL_STEELARCH_START = 6;
	private final static int MSG_SYNC_DEL_STEELARCH_SUCCESS = 7;
	private final static int MSG_SYNC_STEELARCH_CRAFT_DATA_PAGE_SUCCESS = 8;
	private final static int MSG_SYNC_STEELARCH_CRAFT_DATA_SUCCESS = 9;
	private final static int MSG_SYNC_STEELARCH_COLLECT_DATA_PAGE_SUCCESS = 10;
	private final static int MSG_SYNC_STEELARCH_COLLECT_DATA_SUCCESS = 11;
	private final static int MSG_SYNC_STEELARCH_COLLECT_DATA_FAILED = 12;
	private final static int MSG_SYNC_STEELARCH_CRAFT_DATA_FAILED = 13;
	private final static int MSG_SYNC_DEL_STEELARCH_FAILED = 14;
	
	
	
	 private final Handler NetHandler = new Handler() { 
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
			 	case MSG_START_DOWNLOAD_START:
			 		new initListViewThread().start();
			 		break;
			 
			 	case MSG_INIT_LISTVIEW_START:
			 		SharedView.showProgressBar(activity, "正在读取本地钢拱架数距......");
			 		break;
			 
			 	case MSG_INIT_LISTVIEW_SUCCESS:
			 		@SuppressWarnings("unchecked")
					ArrayList<DdSteelArchDataParameter> mlist = (ArrayList<DdSteelArchDataParameter>)msg.obj;
			 		if (mlist != null) {
			 			mAdapter = new DdSteelArchDataParameterAdapter(mlist, SyncSteelArchDataListActivity.this);
						mListView.setAdapter(mAdapter);
			 		}
			 		steelarch_count = mProjectPointBase.getSteelArchCount();
			 		new SyncSteelArchDataThread().start();
			 		break;
			 		
				case MSG_RELOAD_BTN_CLICKED:
					int record_count = mProjectPointBase.getSteelArchCount();
		    		if (record_count > 0) {
		    			SharedView.showProgressBar(SyncSteelArchDataListActivity.this, "正在清除该工程下的钢拱架数据......");
		    			
		    			mProjectPointBase.deleteSteelArchDataRecord();
						synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_STEELARCH_DEL_DATA, "2010-01-01 00:00:00");
						synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_STEELARCH_CRAFT_DATA, "2010-01-01 00:00:00");
						synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_STEELARCH_COLLECT_DATA, "2010-01-01 00:00:00");
		    			
						//show list after clearing
						updateListView();
		    			SharedView.showDynamicMessage("本地搅拌数据清除完成！");
		    			try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		    			SharedView.showDynamicMessage("正在读取本地钢拱架数据......");
		    		} else {
		    			SharedView.showProgressBar(SyncSteelArchDataListActivity.this, "正在读取本地钢拱架数据......");
		    		}
		    		new SyncSteelArchDataThread().start();
					break;	
			 
			 	case MSG_SYNC_DEL_STEELARCH_START:
			 		SharedView.showDynamicMessage("正在同步删除的钢拱架数据......");
			 		break;
			 		
			 	case MSG_SYNC_DEL_STEELARCH_FAILED:
					SharedView.cancelProgressBar();
					if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
						SharedView.showsAlertDialog(activity, "钢拱架数据删除同步失败,同步终止:用户已过程，请重新登录！");
					} else {
						SharedView.showsAlertDialog(activity, "钢拱架数据删除同步失败,同步终止:"+CacheManager.getNetErrorMsg());
					}
					isLoading = false;
			 		break;	
			 		
			 	case MSG_SYNC_DEL_STEELARCH_SUCCESS:
			 		SharedView.showDynamicMessage("正在同步钢拱架工艺参数......");
			 		break;
			 		
			 	case MSG_SYNC_STEELARCH_CRAFT_DATA_PAGE_SUCCESS:
			 		SharedView.showDynamicMessage("正在同步钢拱架工艺参数,已下载到:"+(String)msg.obj);
			 		break;	
			 		
				case MSG_SYNC_STEELARCH_CRAFT_DATA_FAILED:
					SharedView.cancelProgressBar();
					if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
						SharedView.showsAlertDialog(activity, "钢拱架工艺参数同步失败,同步终止:用户已过程，请重新登录！");
					} else {
						SharedView.showsAlertDialog(activity, "钢拱架工艺参数同步失败,同步终止:"+CacheManager.getNetErrorMsg());
					}
					isLoading = false;
					break;
					
				case MSG_SYNC_STEELARCH_CRAFT_DATA_SUCCESS:
					SharedView.showDynamicMessage("开始同步钢拱架测量数据......");
					break;
					
			 	case MSG_SYNC_STEELARCH_COLLECT_DATA_PAGE_SUCCESS:
			 		SharedView.showDynamicMessage("正在同步钢拱架测量数据,已下载到:"+(String)msg.obj);
			 		break;	
			 		
				case MSG_SYNC_STEELARCH_COLLECT_DATA_FAILED:
					SharedView.cancelProgressBar();
					if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
						SharedView.showsAlertDialog(activity, "钢拱架测量数据同步失败,同步终止:用户已过程，请重新登录！");
					} else {
						SharedView.showsAlertDialog(activity, "钢拱架测量数据同步失败,同步终止:"+CacheManager.getNetErrorMsg());
					}
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
    	MyActivityManager.removeActivity(SyncSteelArchDataListActivity.this);
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
		mListView = (ListView)findViewById(R.id.steelarchdata_listview);
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
			new  AlertDialog.Builder(SyncSteelArchDataListActivity.this)    
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
			mProjectPointBase.getDdSteelArchDataList(list);
			NetHandler.sendMessage(NetHandler.obtainMessage(MSG_INIT_LISTVIEW_SUCCESS, list));
		}
	}
}

