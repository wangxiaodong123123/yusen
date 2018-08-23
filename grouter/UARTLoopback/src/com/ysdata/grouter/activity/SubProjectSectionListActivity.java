package com.ysdata.grouter.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.ysdata.grouter.R;
import com.ysdata.grouter.adapter.DdSubprojsectionParameterAdapter;
import com.ysdata.grouter.cloud.api.IntegerListResponse;
import com.ysdata.grouter.cloud.api.SubProjectPointCraftParameter;
import com.ysdata.grouter.cloud.api.SubProjectPointGroutingParameter;
import com.ysdata.grouter.cloud.api.SubProjectPointGroutingParameterListResponse;
import com.ysdata.grouter.cloud.api.SubProjectPointListResponse;
import com.ysdata.grouter.cloud.api.SubProjectPointTimeNodes;
import com.ysdata.grouter.cloud.api.SubProjectSection;
import com.ysdata.grouter.cloud.api.SubProjectSectionListResponse;
import com.ysdata.grouter.cloud.util.ApiClient;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.CacheManager;
import com.ysdata.grouter.cloud.util.ConstDef;
import com.ysdata.grouter.cloud.util.SharedView;
import com.ysdata.grouter.cloud.util.UIUtilities;
import com.ysdata.grouter.database.FileOperator;
import com.ysdata.grouter.database.ProjectDataBaseAdapter;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.database.SyncTimeDataBaseAdapter;
import com.ysdata.grouter.element.DdSubprojsectionParameter;
import com.ysdata.grouter.picture.utils.BitmapUtil;
import com.ysdata.grouter.storage.ExtSdCheck;
import com.ysdata.grouter.uart.MyActivityManager;

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

public class SubProjectSectionListActivity extends Activity {
	
	private Context context = null;
	TextView title;
	private ListView mListView;
	ProjectDataBaseAdapter mProjectBase;
	private ProjectPointDataBaseAdapter mProjectPointBase;
	private ArrayList<DdSubprojsectionParameter> list;
	private DdSubprojsectionParameterAdapter mAdapter;
	private SyncTimeDataBaseAdapter synctimebase;
	private ApiClient apiClient;
	int subproject_id, project_id;
	TextView user_info_tv;
	Button reload_bt;
	boolean isLoading = true;
	int mileage_count = 0;
	int subproject_direction = 1;
	private Activity activity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subprojectsection_list_phone);
        context = this;
        activity = SubProjectSectionListActivity.this;
        MyActivityManager.addActivity(SubProjectSectionListActivity.this);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
		synctimebase = SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context);
		apiClient = ApiClient.getInstance(context);
		project_id = CacheManager.getDbProjectId();
		subproject_id = CacheManager.getDbSubProjectId();
		mProjectBase.closeDb();
		mProjectPointBase.closeDb();
		if (mProjectBase.openDb() && mProjectPointBase.openDb(project_id, subproject_id) && synctimebase.openDb()) {
			subproject_direction = mProjectBase.getSubProjectDirection(subproject_id);
			InitView();
			DownloadAlter();
//			new initListViewThread().start();
		} else {
			Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
		}
    }

    private void DownloadAlter() {
    	new  AlertDialog.Builder(SubProjectSectionListActivity.this)    
        .setTitle("提示" )
        .setIcon(android.R.drawable.ic_dialog_info)
        .setMessage("下载后的工艺文件将无法被用户修改，是否继续?")  
        .setPositiveButton("确定" , new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				NetHandler.sendEmptyMessage(MSG_START_DOWNLOAD_MODE);
			}
		} )   
        .setNegativeButton("取消", null)
        .create()
        .show();
    }
    
    private void updateListView() {
    	list = mProjectPointBase.getDdsubprojsectionlist();
		if (list != null) {
			mAdapter = new DdSubprojsectionParameterAdapter(list, context);
			mListView.setAdapter(mAdapter);
		}
    }
    
    private String getStrokeFilePath(String extSdPath, int section_page) {
		File path = new File(extSdPath+ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/");
		if (!path.exists()) {
			path.mkdirs();
		}
		return extSdPath+ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/"+"picture_"+section_page+".png";
    }
    
    private ArrayList<Integer> getImagePageList(String extSdPath) {
    	ArrayList<Integer> list = new ArrayList<Integer>();
    	File path = new File(extSdPath+ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/");
    	File[] file_lists = path.listFiles();
    	String imagePageString;
    	if (file_lists != null) {
    		for (int i = 0; i < file_lists.length; i++) {
    			if (file_lists[i].getName().startsWith("picture_") && file_lists[i].getName().endsWith(".png")) {
    				imagePageString = file_lists[i].getName().split("_")[1].split("\\.")[0];
    				if (imagePageString.matches("^[0-9]+(\\.[0-9]+)?$")) {
    					list.add(Integer.parseInt(imagePageString));
    				}
    			}
    		}
    	}
    	return list;
    }
    
	private void saveImageToLocal(Bitmap bmp, int section_page) {
		String extSdPath = ExtSdCheck.getExtSDCardPath();
		AppUtil.log("=======saveImageToLocal=========section_page:"+section_page);
		if (extSdPath != null) {
			String strFilePath = getStrokeFilePath(extSdPath, section_page);
			
			if (null != bmp)
			{
				int width = bmp.getWidth();  //缩放
				int height = bmp.getHeight();
				int newWidth = 536;
				int newHeight = 360;
				float scaleWidth = ((float) newWidth) / width;
				float scaleHeight = ((float) newHeight) / height;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix,true);
				BitmapUtil.saveBitmapToSDCard(newbm, strFilePath);
			}
		}	
	}
	
	private void clearImages() {
		FileOperator.getSingleFileOperator(context).DeleteAllDir(ExtSdCheck.getExtSDCardPath()+
				ConstDef.DATABASE_PATH+project_id+"/"+subproject_id+"/picture/");
	}
	
	class SyncCraftSectionPointsThread extends Thread {
    	@Override
    	public void run() {
    		int mileage_index = mProjectPointBase.getSyncMileagePage(subproject_direction); //获取工点表的最后一个里程的序号
    		int pageIndex = 1;
    		boolean isSyncSuccess = true; //获取服务端数据数据成功
    		boolean isSyncLocal = false; //本地数据库是否更新
    		boolean isDdAll = true;
    		SubProjectPointListResponse subproject_point_list = null;
    		SubProjectPointCraftParameter subproject_point = null;
    		boolean hasMore = true;
    		String sync_time = synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECTPOINT);
    		
    		if (mileage_index == 0 || sync_time.equals("2010-01-01 00:00:00")) {
    			mProjectPointBase.deleteAllWrcardRecord();
    			mProjectPointBase.deleteAllAnchorRecord();
    			mProjectPointBase.initalCommunicateState();
    			isDdAll = true;
    		} else {
    			isDdAll = false;
    		}
    		AppUtil.log("mileage_index:"+mileage_index + " mileage_count:"+mileage_count);
//    		NetHandler.sendEmptyMessage(MSG_SYNC_DEL_POINTS_START);
    		if (!isDdAll) {
    			do {
    				IntegerListResponse integer_list = apiClient.getSyncDeleteSubProjectPoints(subproject_id, pageIndex);
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
    							mProjectPointBase.deleteCraftParamRecord(integer_list.Data.get(i));
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
    			NetHandler.sendEmptyMessage(MSG_SYNC_DEL_POINTS_SUCCESS);
    			hasMore = true;
//    			isSyncLocal = false;
    			pageIndex = 1;
    			if (!isDdAll) {
    				do {
    					subproject_point_list = apiClient.getSyncSubProjectPoints(subproject_id, pageIndex);
    					if (subproject_point_list != null && subproject_point_list.isSuccess) {
    						double sectionMetreMax = mProjectPointBase.getMaxSectionMetreFromAnchorTable();
    						int subproject_point_list_size = 0;
	    					if (subproject_point_list.Data != null) {
	    						subproject_point_list_size = subproject_point_list.Data.size();
	    						if (subproject_point_list_size != subproject_point_list.total) {
	    							AppUtil.log("subproject_point_list_size:"+subproject_point_list_size + " subproject_point_list.total:"+
	    									subproject_point_list.total);
	    						}
	    					}
	    					for (int i = 0; i < subproject_point_list_size; i++) {
	    						subproject_point = subproject_point_list.Data.get(i);
	    						if (subproject_point.sectionMetre <= sectionMetreMax) {
	    							mProjectPointBase.insertCraftParam(subproject_point.id, subproject_point.pointCode, subproject_point.subProjectSectionId, subproject_point.rebarType, 
	    									subproject_point.rebarModel, subproject_point.rebarLength, subproject_point.orificePressureDesign, 
	    									subproject_point.holdPressureSecondsDesign, subproject_point.totalGroutingDesign, 
	    									0, subproject_point.groutingPumpFlow, subproject_point.full_hole_pressure, "", subproject_point.sectionMetre, subproject_point.grout_priority);
	    							isSyncLocal = true;
	    						}
	    					}
	    					isSyncSuccess = true;
	    					pageIndex++;
	    					hasMore = subproject_point_list.HasMore;
    					} else {
    						if (subproject_point_list != null && !subproject_point_list.isSuccess) {
    							CacheManager.setNetErrorMsg(subproject_point_list.errorString);
    						}
    						isSyncSuccess = false;
    						break;
    					}
    				}while(hasMore);
    			} 
    		} else {
    			NetHandler.sendEmptyMessage(MSG_SYNC_DEL_POINTS_FAILED);
    			return;
    		}
    		mileage_index = mProjectPointBase.getSyncMileagePage(subproject_direction); //考虑到追加工段的情况，故再取一次
    		AppUtil.log("mileage_index==================="+mileage_index);
    		String sectionCode = null;
    		if (isSyncSuccess) {
	    		//isSyncLocal = false;
	    		int mileage_id;
	    		NetHandler.sendEmptyMessage(MSG_SYNC_INSERT_POINTS_SUCCESS);
	    		while (++mileage_index <= mileage_count) {
    				sectionCode = mProjectPointBase.getMileageNameWhereOrderNo(mileage_index);
    				mileage_id = mProjectPointBase.getMileageIdLikeMileageName(sectionCode);
    				AppUtil.log("sectionCode:"+sectionCode+" mileage_id:"+mileage_id);
    				pageIndex = 1;
    				hasMore = true;
    				do {
    					subproject_point_list = apiClient.getPointlist(subproject_id, sectionCode, pageIndex);
    					if (subproject_point_list != null && subproject_point_list.isSuccess) {
    						int subproject_point_list_size = 0;
    						if (subproject_point_list.Data != null) {
    							subproject_point_list_size = subproject_point_list.Data.size();
    							if (subproject_point_list_size != subproject_point_list.total) {
    								AppUtil.log("subproject_point_list_size:"+subproject_point_list_size + " subproject_point_list.total:"+
    										subproject_point_list.total);
    							}
    						}
    						for (int i = 0; i < subproject_point_list_size; i++) {
    							subproject_point = subproject_point_list.Data.get(i);
    							mProjectPointBase.insertCraftParam(subproject_point.id, subproject_point.pointCode, subproject_point.subProjectSectionId, subproject_point.rebarType, 
    									subproject_point.rebarModel, subproject_point.rebarLength, subproject_point.orificePressureDesign, 
    									subproject_point.holdPressureSecondsDesign, subproject_point.totalGroutingDesign, 
    									0, subproject_point.groutingPumpFlow, subproject_point.full_hole_pressure, "", subproject_point.sectionMetre, subproject_point.grout_priority);
    							
        					}
    						if (subproject_point_list_size > 0) {
    							isSyncLocal = true;
    						}
    						isSyncSuccess = true;
    						pageIndex++;
    						hasMore = subproject_point_list.HasMore;
    					} else {
    						if (subproject_point_list != null && !subproject_point_list.isSuccess) {
    							CacheManager.setNetErrorMsg(subproject_point_list.errorString);
    						}
    						isSyncSuccess = false;
    						break;
    					}
    				} while(hasMore);
    				if (!isSyncSuccess) {
    					break;
    				}
    				NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_POINTS_SUCCESS, sectionCode));
    			}
    		} else {
    			NetHandler.sendEmptyMessage(MSG_SYNC_INSERT_POINTS_FAILED);
    			return;
    		}
    		if (isSyncSuccess) {
    			if (isSyncLocal) {
    				synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECTPOINT, apiClient.getCurrentDateTime());
    			}
    			NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_POINTS_ALL_SUCCESS, isSyncLocal ? 1 : 0));
    		} else {
    			NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_POINTS_FAILED, isSyncLocal ? 1 : 0));
    		}
//    		AppUtil.log("=========MSG_PROGRESSBAR_CANCEL========");
//    		NetHandler.sendEmptyMessage(MSG_PROGRESSBAR_CANCEL);
    		super.run();
    	}
    }
	
	class SyncSectionImagesThread extends Thread {
		@Override
		public void run() {
			Bitmap bitmap = null;
			boolean isDdAll = false;
			int pageIndex = 1;
			boolean isSyncSuccess = false;
			boolean isLocalUpdate = false;
			int sectionId = 0;
			int imagePage = 0;
			boolean hasMore = false;
			boolean hasImage = false;
			ArrayList<Integer> local_imagePage_list = new ArrayList<Integer>();
			IntegerListResponse integer_list = null;
			String extSdPath = ExtSdCheck.getExtSDCardPath();
			String sync_time = synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_IMAGES);
			if (sync_time.equals("2010-01-01 00:00:00")) { 
				clearImages();
    			isDdAll = true;
    		} else {
    			isDdAll = false;
    			if (extSdPath != null) {
    				local_imagePage_list = getImagePageList(extSdPath);
    			}
    		}
			do {
				integer_list = apiClient.getSectionExpImageList(subproject_id, pageIndex);
				if (integer_list != null && integer_list.isSuccess) {
					int integer_list_size = 0;
					if (integer_list.Data != null) {
						integer_list_size = integer_list.Data.size();
						if (integer_list_size != integer_list.total) {
							AppUtil.log("integer_list_size:"+integer_list_size + " integer_list.total:"+
									integer_list.total);
						}
					}
					for (int i = 0; i < integer_list_size; i++) {
						sectionId = integer_list.Data.get(i);
						if (!isDdAll) {
							//Check if the picture has been downloaded
							imagePage = mProjectPointBase.getMileageOrderNoWhereSectonId(sectionId);
							for (int j = 0; j < local_imagePage_list.size(); j++) {
								if (imagePage == local_imagePage_list.get(j)) {
									hasImage = true;
									local_imagePage_list.remove(j);
									break;
								}
							}
						}
						if (!hasImage) {
							try {
								bitmap = Glide.with(context).load(apiClient.DOWNLOAD_IMAGE_URL + "/" + subproject_id
										+ "/" + sectionId).asBitmap()
										.into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							}
							if (bitmap != null) {
								saveImageToLocal(bitmap, mProjectPointBase.getMileageOrderNoWhereSectonId(sectionId));
								isLocalUpdate = true;
							}
							hasImage = false;
						}
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
			if (isSyncSuccess) {
				if (isLocalUpdate) {
					synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_IMAGES, apiClient.getCurrentDateTime());
				}
				NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_SECTION_PICTURE_SUCCESS, isLocalUpdate ? 1 : 0));
			} else {
				NetHandler.sendEmptyMessage(MSG_SYNC_SECTION_PICTURE_FAILED);
			}
		}
	}
	
	class SyncTimeNodeThread extends Thread {
    	@Override
    	public void run() {
    		int pageIndex = 1;
    		SubProjectPointGroutingParameterListResponse subproject_point_grouting_parameter_list= null;
    		SubProjectPointGroutingParameter subproject_point_grouting_parameter = null;
    		List<SubProjectPointTimeNodes> point_timenodes_list = null;
    		SubProjectPointTimeNodes point_timenodes = null;
    		StringBuffer time_sb = new StringBuffer();
    		StringBuffer pressure_sb = new StringBuffer();
    		StringBuffer capacity_sb = new StringBuffer();
    		StringBuffer time_capacity_sb = new StringBuffer();
    		int anchor_id = 0;
    		String groutingDate = "";
    		String beginTime = "";
    		String endTime = "";
    		String recvData = "";
    		double totalGroutingActual = 0;
    		double orificePressureActual = 0;
    		boolean isDdAll = false;
    		boolean hasMore = false;
    		boolean isSyncSuccess = false;
    		boolean isLocalUpate = false;
    		int holdPressureSecondsActual = 0;
    		double full_hole_capacity = 0;
    		String sync_date = synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECTPOINT_GROUTING_DATA);
    		AppUtil.log("======sync_date====="+sync_date);
    		if (sync_date.equals("2010-01-01 00:00:00")) {
    			isDdAll = true;
    		} else {
    			isDdAll = false;
    		}
    		do {
    			if (isDdAll) {
    				AppUtil.log("======getProjectPointGroutingDataList=====");
        			subproject_point_grouting_parameter_list = apiClient.
        					getProjectPointGroutingDataList(subproject_id, pageIndex);
    			} else {
    				AppUtil.log("======getSyncSubProjectPointGroutingDataList=====");
        			subproject_point_grouting_parameter_list = apiClient.
        					getSyncSubProjectPointGroutingDataList(subproject_id, pageIndex);
    			}
    			if (subproject_point_grouting_parameter_list != null && subproject_point_grouting_parameter_list.isSuccess) {
    				int size = 0;
    				if (subproject_point_grouting_parameter_list.Data != null) {
    					size = subproject_point_grouting_parameter_list.Data.size();
    					if (size != 
    							subproject_point_grouting_parameter_list.total) {
    						AppUtil.log("subproject_point_grouting_parameter_list.total:"+
    								subproject_point_grouting_parameter_list.total+" actual size:"+size);
    					}
    				}
    				for (int i = 0; i < size; i++) {
    					subproject_point_grouting_parameter = subproject_point_grouting_parameter_list.
    							Data.get(i);
    					anchor_id = subproject_point_grouting_parameter.id;
    					groutingDate = subproject_point_grouting_parameter.groutingDate;
    					beginTime = subproject_point_grouting_parameter.beginTime;
    					endTime = subproject_point_grouting_parameter.endTime;
    					totalGroutingActual = subproject_point_grouting_parameter.totalGroutingActual;
    					orificePressureActual = subproject_point_grouting_parameter.orificePressureActual;
    					holdPressureSecondsActual = subproject_point_grouting_parameter.holdPressureSecondsActual;
    					point_timenodes_list = subproject_point_grouting_parameter.getData();
    					recvData = subproject_point_grouting_parameter.recvData;
    					for (int j = 0; j < point_timenodes_list.size(); j++) {
    						point_timenodes = point_timenodes_list.get(j);
    						time_sb.append(point_timenodes.time).append(' ');
    						pressure_sb.append(point_timenodes.pressure).append(' ');
    						if (point_timenodes.grouting != 0) {
    							capacity_sb.append(point_timenodes.grouting).append(' ');
    							time_capacity_sb.append(point_timenodes.time).append(' ');
    						}
    					}
    					mProjectPointBase.updateAnchorCollectDataFromServer(anchor_id, groutingDate, beginTime, endTime, totalGroutingActual, orificePressureActual, 
    							holdPressureSecondsActual, full_hole_capacity, time_capacity_sb.toString(), time_sb.toString(), 
    							capacity_sb.toString(), pressure_sb.toString(), recvData);
    					time_sb.setLength(0);
    					pressure_sb.setLength(0);
    					capacity_sb.setLength(0);
    					time_capacity_sb.setLength(0);
    				}
    				pageIndex++;
    				hasMore = subproject_point_grouting_parameter_list.HasMore;
    				isSyncSuccess = true;
    				if (size > 0) {
    					isLocalUpate = true;
    					NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_GROUTING_DATA_SUCCESS, 
    							subproject_point_grouting_parameter_list.Data.get(size-1).pointCode));
    				}
    			} else {
    				if (subproject_point_grouting_parameter_list != null && !subproject_point_grouting_parameter_list.isSuccess) {
    					CacheManager.setNetErrorMsg(subproject_point_grouting_parameter_list.errorString);
    				}
    				isSyncSuccess = false;
    				break;
    			}
    		} while(hasMore);
    		if (isSyncSuccess) { 
    			if (isLocalUpate) {
    				synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECTPOINT_GROUTING_DATA, apiClient.getCurrentDateTime());
    			}
				NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_GROUTING_ALL_DATA_SUCCESS, isLocalUpate ? 1 : 0));
    		} else {
    			NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_GROUTING_ALL_DATA_FAILED, isLocalUpate ? 1 : 0));
    		}
    		super.run();
    	}
    }
    
	private final static int MSG_SYNC_SECTION_LIST_SUCCESS = 1;
	private final static int MSG_SYNC_SECTION_PICTURE_SUCCESS = 2;
	private final static int MSG_SYNC_SECTION_POINT_SUCCESS = 3;
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
	private final static int MSG_START_DOWNLOAD_MODE = 27;
	
	class SyncSubProjectSectionListThread extends Thread { 
    	@Override
    	public void run() {
    		boolean isDdAll = true;
    		boolean isSyncSuccess = false;
    		boolean isLocalUpdate = false;
    		isLoading = true;
    		/*try { //解决在同步数据过程中引起的黑屏问题
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
    		NetHandler.sendEmptyMessage(MSG_SYNC_SECTION_LIST_START);
    		SubProjectSectionListResponse subproject_section_list = null;
    		IntegerListResponse integer_list = null;
//    		ArrayList<SubProjectSectionListResponse> subproject_section_list_list = new ArrayList<SubProjectSectionListResponse>();
    		int pageIndex = 1;
    		boolean hasMore = true;
    		String sync_time = synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECTSECTION);
    		if (mileage_count == 0 || sync_time.equals("2010-01-01 00:00:00")) {
    			if (mileage_count > 0) {
    				synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECTPOINT, "2010-01-01 00:00:00");
					synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECTPOINT_GROUTING_DATA, "2010-01-01 00:00:00");
					synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_IMAGES, "2010-01-01 00:00:00");
    				mProjectPointBase.deleteAllWrcardRecord();
    				mProjectPointBase.deleteAllAnchorRecord();
    				mProjectPointBase.deleteAllMileageRecord();
    				clearImages();
    			}
    			isDdAll = true;
    		} else {
    			isDdAll = false;
    		}
    		do {
    			if (isDdAll) {
    				subproject_section_list = apiClient.getSubProjectSectionlist(subproject_id, pageIndex);
    			} else {
    				integer_list = apiClient.getSyncDeleteSubProjectSections(subproject_id, pageIndex);
    				if (integer_list != null && integer_list.isSuccess) {
    					int integer_list_size = 0;
    					if (integer_list.Data != null) {
    						integer_list_size = integer_list.Data.size();
    						if (integer_list_size != integer_list.total) {
    							AppUtil.log("integer_list_size:"+integer_list_size + " integer_list.total:"+
    									integer_list.total);
    						}
    					}
    					for (int i = 0; i < integer_list_size; i++) {
    						mProjectPointBase.deleteMileageParameter(integer_list.Data.get(i));
    					}
    					subproject_section_list = apiClient.getSyncSubProjectSections(subproject_id, pageIndex);
    				} else {
    					if (integer_list != null && !integer_list.isSuccess) {
    						CacheManager.setNetErrorMsg(integer_list.errorString);
    					}
    					NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_FAILED, "网络工段列表同步失败:"+CacheManager.getNetErrorMsg()));
    					isSyncSuccess = false;
    					break;
    				}
    			}
    			if (subproject_section_list != null && subproject_section_list.isSuccess) {
    				int subproject_section_list_size = 0;
    				if ((integer_list != null && integer_list.total > 0) || subproject_section_list.total > 0) {
    					SubProjectSection subProjectSection = null;
    					subproject_section_list_size = 0;
    					if (subproject_section_list.Data != null) {
    						subproject_section_list_size = subproject_section_list.Data.size();
    						if (subproject_section_list_size != subproject_section_list.total) {
    							AppUtil.log("subproject_section_list_size:"+subproject_section_list_size + " subproject_section_list.total:"+
    									subproject_section_list.total);
    						}
    					}
    					for (int i = 0; i < subproject_section_list_size; i++) {
    						subProjectSection = subproject_section_list.Data.get(i);
    						mProjectPointBase.updateMileageParamer(subProjectSection.subProjectSectionId, 
    								subProjectSection.sectionCode, subProjectSection.subProjectPointCount, 
    								subProjectSection.linkName, subProjectSection.createTime, subProjectSection.MixRatioCement, 
    								subProjectSection.MixRatioSand, subProjectSection.MixRatioWater, subProjectSection.sectionMetre);
    					}
    					if (subproject_section_list_size > 0 && !isLocalUpdate) {
    						isLocalUpdate = true;
    					}
//    					mProjectPointBase.setMileageOrderNo();
    				}
    				isSyncSuccess = true;
    				hasMore = subproject_section_list.HasMore;
    				pageIndex++;
    				if (subproject_section_list_size > 0) {
    					NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_SECTION_LIST_PAGE_SUCCESS, 
    							subproject_section_list.Data.get(subproject_section_list_size-1).sectionCode));
    				}
    				AppUtil.log("hasMore=================="+hasMore + "  pageIndex=================="+pageIndex);
    			} else {
    				if (subproject_section_list != null && !subproject_section_list.isSuccess) {
    					CacheManager.setNetErrorMsg(subproject_section_list.errorString);
    				}
    				if (pageIndex > 1) {
    					NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_PARTS_OF_SECTION_FAILED, "网络工段列表部分未同步成功:"+CacheManager.getNetErrorMsg()));
    				} else {
    					NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_FAILED, "网络工段列表同步失败:"+CacheManager.getNetErrorMsg()));
    				}
    				isSyncSuccess = false;
    				break;
    			}
    		} while(hasMore);
    		if (isSyncSuccess) {
    			synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECTSECTION, apiClient.getCurrentDateTime());
    			NetHandler.sendMessage(NetHandler.obtainMessage(MSG_SYNC_SECTION_LIST_SUCCESS, isLocalUpdate ? 1 : 0));
    		}
//    		NetHandler.sendEmptyMessage(MSG_PROGRESSBAR_CANCEL);
    		super.run();
    	}
    }
	
	 private final Handler NetHandler = new Handler() { 
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
			 	case MSG_START_DOWNLOAD_MODE:
			 		new initListViewThread().start();
			 		break;
			 
			 	case MSG_INIT_LISTVIEW_START:
			 		SharedView.showProgressBar(activity, "正在同步本地工段数据到列表......");
			 		break;
			 
			 	case MSG_INIT_LISTVIEW_SUCCESS:
			 		@SuppressWarnings("unchecked")
					ArrayList<DdSubprojsectionParameter> mlist = (ArrayList<DdSubprojsectionParameter>)msg.obj;
			 		if (mlist != null) {
			 			mAdapter = new DdSubprojsectionParameterAdapter(mlist, SubProjectSectionListActivity.this);
						mListView.setAdapter(mAdapter);
			 		}
//			 		mileage_count = mProjectBase.getSubProjectCount();
			 		mileage_count = mProjectPointBase.getMileageCount();
			 		/*SharedView.showDynamicMessage("正在同步网络工段列表......");
			 		try {
						Thread.sleep(1000); //delay for our observation
					} catch (InterruptedException e) {
						e.printStackTrace();
					}*/
			 		new SyncSubProjectSectionListThread().start();
			 		break;
			 
			 	case MSG_SYNC_SECTION_LIST_START:
			 		SharedView.showDynamicMessage("正在同步网络工段列表......");
			 		break;
			 		
				case MSG_SYNC_SECTION_LIST_PAGE_SUCCESS:
					SharedView.showDynamicMessage("正在同步工段列表,已下载到:"+(String)msg.obj);
//					mileage_count = mProjectPointBase.getMileageCount();
//					updateListView();
					break;
					
				case MSG_SYNC_SECTION_LIST_SUCCESS:
					SharedView.showDynamicMessage("工段列表下载成功,正在同步到本地数据库......");
					int isLocalUpdate5 = (Integer)msg.obj;
					if (isLocalUpdate5 == 1) {
						mProjectPointBase.setMileageOrderNo(subproject_direction);
						updateListView();
						mileage_count = mProjectPointBase.getMileageCount();
					}
					SharedView.showDynamicMessage("工段列表同步成功,开始同步工点位置图示......");
					try {
						Thread.sleep(1000); //delay for our observation
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					AppUtil.log("================sync images=============");
					SharedView.showDynamicMessage("正在同步工点位置图示......");
					new SyncSectionImagesThread().start();
					break;	
					
				case MSG_SYNC_SECTION_PICTURE_SUCCESS:
					SharedView.showDynamicMessage("工点位置图示同步成功,开始同步工点工艺参数......");
					int isLocalUpdate6 = (Integer)msg.obj;
					if (isLocalUpdate6 == 1) {
						updateListView();
					}
					try {
						Thread.sleep(1000); //delay for our observation
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					AppUtil.log("================sync sectonPoints=============");
					SharedView.showDynamicMessage("正在同步需要删除的工点......");
					new SyncCraftSectionPointsThread().start();
					break;	
					
				case MSG_SYNC_SECTION_PICTURE_FAILED:	
					SharedView.cancelProgressBar();
//					SharedView.showsAlertDialog(activity, "工点位置图示同步失败,同步终止!");
					if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
						SharedView.showsAlertDialog(activity, "工点位置图示同步失败,同步终止:用户已过期，请重新登录！");
					} else {
						SharedView.showsAlertDialog(activity, "工点位置图示同步失败,同步终止:"+CacheManager.getNetErrorMsg());
					}
					isLoading = false;
					break;
					
				case MSG_RELOAD_BTN_CLICKED:
//					mileage_count = mProjectBase.getSubProjectCount();
					mileage_count = mProjectPointBase.getMileageCount();
		    		if (mileage_count > 0) {
		    			SharedView.showProgressBar(SubProjectSectionListActivity.this, "正在清除该工程下的工段......");
		    			
		    			mProjectPointBase.deleteAllWrcardRecord();
						mProjectPointBase.deleteAllAnchorRecord();
						mProjectPointBase.deleteAllMileageRecord();
						clearImages();
						synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECTSECTION, "2010-01-01 00:00:00");
						synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECTPOINT, "2010-01-01 00:00:00");
						synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECTPOINT_GROUTING_DATA, "2010-01-01 00:00:00");
						synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_IMAGES, "2010-01-01 00:00:00");
		    			
						//show list after clearing
						updateListView();
		    			SharedView.showDynamicMessage("本地工段数据清除完成！");
		    			try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		    			SharedView.showDynamicMessage("正在同步网络工段列表......");
		    		} else {
		    			SharedView.showProgressBar(SubProjectSectionListActivity.this, "正在同步网络工程列表......");
		    		}
		    		new SyncSubProjectSectionListThread().start();
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
					
				case MSG_SYNC_POINTS_FAILED:
					int isLocalUpdate2 = (Integer)msg.obj;
					if (isLocalUpdate2 == 1) {
						mProjectPointBase.setAnchorOrderNo(subproject_direction);
						updateListView();
					}
					SharedView.cancelProgressBar();
//					SharedView.showsAlertDialog(activity, "新增工点同步失败!");
					if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
						SharedView.showsAlertDialog(activity, "新增工点同步失败:用户已过程，请重新登录！");
					} else {
						SharedView.showsAlertDialog(activity, "新增工点同步失败:"+CacheManager.getNetErrorMsg());
					}
					isLoading = false;
					break;	
					
				case MSG_SYNC_POINTS_ALL_SUCCESS:
					int isLocalUpdate1 = (Integer)msg.obj;
					if (isLocalUpdate1 == 1) {
						SharedView.showDynamicMessage("工点参数下载成功,正在同步到本地数据库......");
						mProjectPointBase.setAnchorOrderNo(subproject_direction);
						updateListView();
					}
					SharedView.showDynamicMessage("工点参数同步成功,开始同步工点注浆数据......");
					try {
						Thread.sleep(1000); //delay for our observation
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					SharedView.showDynamicMessage("正在同步工点注浆数据......");
					new SyncTimeNodeThread().start();
					break;
					
				case MSG_SYNC_GROUTING_DATA_SUCCESS:
					SharedView.showDynamicMessage("正在同步工点注浆数据,已下载到:"+(String)msg.obj);
					break;
					
				case MSG_SYNC_GROUTING_ALL_DATA_SUCCESS:
					int isLocalUpdate3 = (Integer)msg.obj;
					if (isLocalUpdate3 == 1) {
						updateListView();
					}
					SharedView.cancelProgressBar();
					SharedView.showsAlertDialog(activity, "工段、工点工艺参数及工点注浆数据同步成功!");
					isLoading = false;
					break;
					
				case MSG_SYNC_GROUTING_ALL_DATA_FAILED:
					int isLocalUpdate4 = (Integer)msg.obj;
					if (isLocalUpdate4 == 1) {
						updateListView();
					}
					SharedView.cancelProgressBar();
//					SharedView.showsAlertDialog(activity, "工点注浆数据同步失败!");
					if (CacheManager.getNetErrorMsg().equals(ConstDef.EXPIRED_RESPONSE_STRING)) {
						SharedView.showsAlertDialog(activity, "工点注浆数据同步失败:用户已过程，请重新登录！");
					} else {
						SharedView.showsAlertDialog(activity, "工点注浆数据同步失败:"+CacheManager.getNetErrorMsg());
					}
					isLoading = false;
					break;	
					
				case MSG_SYNC_PARTS_OF_SECTION_FAILED:
					updateListView();
					SharedView.cancelProgressBar();
					UIUtilities.showToast(activity, (String)msg.obj, true);
					isLoading = false;
					SharedView.showUserExpiredAlertDialog(SubProjectSectionListActivity.this);
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
					
				case MSG_SYNC_FAILED:
					SharedView.cancelProgressBar();
					UIUtilities.showToast(activity, (String)msg.obj, true);
					isLoading = false;
					SharedView.showUserExpiredAlertDialog(SubProjectSectionListActivity.this);
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
    	MyActivityManager.removeActivity(SubProjectSectionListActivity.this);
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
		mListView = (ListView)findViewById(R.id.section_list);
		title = (TextView)findViewById(R.id.sectionlist_title);
		String proj_name = mProjectBase.getProjectName(project_id);
		String subproj_name = mProjectBase.getSubProjectName(subproject_id);
		title.setText("云服务->"+proj_name+"->"+subproj_name+"->工段列表");
		user_info_tv = (TextView) findViewById(R.id.user_info);
		user_info_tv.setText(CacheManager.getUserName() + " 欢迎你!");
		reload_bt = (Button) findViewById(R.id.reload_bt);
		reload_bt.setOnClickListener(new ReloadClickListener());
	}
	
	class ReloadClickListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (isLoading) return;
			boolean doAllUpload = mProjectPointBase.doAllGroutingPointUpload();
			String msg = "";
			if (doAllUpload) {
				msg = "该操作首先会清除该工程下所有已下载的工段数据，然后才进行重新下载，建议用户在数据下载失败的情况下使用该功能，是否继续?";
			} else {
				msg = "检测到部分注浆数据未上传到平台，建议上传后，再使用该功能，是否继续?";
			}
			new  AlertDialog.Builder(SubProjectSectionListActivity.this)    
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
			list = mProjectPointBase.getDdsubprojsectionlist();
			NetHandler.sendMessage(NetHandler.obtainMessage(MSG_INIT_LISTVIEW_SUCCESS, list));
		}
	}
}

