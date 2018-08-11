package com.ysdata.steelarch.activity;

import java.util.ArrayList;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.cloud.api.IntegerListResponse;
import com.ysdata.steelarch.cloud.api.SubProjectListResponse;
import com.ysdata.steelarch.cloud.util.ApiClient;
import com.ysdata.steelarch.cloud.util.AppUtil;
import com.ysdata.steelarch.cloud.util.CacheManager;
import com.ysdata.steelarch.cloud.util.ConstDef;
import com.ysdata.steelarch.cloud.util.SharedView;
import com.ysdata.steelarch.cloud.util.UIUtilities;
import com.ysdata.steelarch.database.FileOperator;
import com.ysdata.steelarch.database.ProjectDataBaseAdapter;
import com.ysdata.steelarch.database.SyncTimeDataBaseAdapter;
import com.ysdata.steelarch.storage.ExtSdCheck;
import com.ysdata.steelarch.uart.MyActivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class SubProjectListActivity extends Activity {
	
	private Context context = null;
	private ArrayList<String> mList = new ArrayList<String>();
	TextView title;
	private MyAdapter mAdapter;
	private ListView mListView;
	private ProjectDataBaseAdapter mProjectBase;
	private SyncTimeDataBaseAdapter synctimebase;
	private ApiClient apiClient;
	int subproject_id, project_id;
	public static SubProjectListActivity mEngNameListActivity;
	TextView user_info_tv;
	int subproject_count = 0;
	Button reload_bt;
	boolean isLoading = true;
	boolean isUpdateTime = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subproject_list_cloud_phone);
        context = this;
        MyActivityManager.addActivity(SubProjectListActivity.this);
		mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
		synctimebase = SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context);
		apiClient = ApiClient.getInstance(context);
		if (mProjectBase.openDb() && synctimebase.openDb()) {
			project_id = CacheManager.getDbProjectId();
			mProjectBase.getSubProjectNameList(mList, project_id);
			InitView();
			SharedView.showProgressBar(SubProjectListActivity.this, "正在同步网络工程列表......");
			new GetSubprojectListThread().start();
			subproject_count = mList.size();
		} else {
			Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
		}
        
    }
    
    class GetSubprojectListThread extends Thread {
    	@Override
    	public void run() {
    		isLoading = true;
    		SubProjectListResponse subproject_list = null;
    		String sync_time = synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECT);
    		AppUtil.log("sync_time===================="+sync_time);
    		if (subproject_count == 0 || sync_time.equals("2010-01-01 00:00:00")) {
    			if (subproject_count > 0) {
    				mProjectBase.clearProject(project_id);
    				synctimebase.clearProjectSyncTime(project_id);
    			}
    			subproject_list = apiClient.getSubProjectlist(project_id);
    		} else {
    			IntegerListResponse integer_list = apiClient.getSyncDeleteSubProjects(project_id);
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
    					FileOperator mFileOperator = FileOperator.getSingleFileOperator(SubProjectListActivity.this);
    					String database_path = ExtSdCheck.getExtSDCardPath() + ConstDef.DATABASE_PATH;
    					int subproject_id;
    					for (int i = 0; i < integer_list_size; i++) {
    						subproject_id = integer_list.Data.get(i);
    						mProjectBase.deleteSubProject(subproject_id);
    						mFileOperator.DeleteAllDir(database_path+project_id+"/"+subproject_id);
    					}
    					isUpdateTime = true;
    				}
    				subproject_list = apiClient.getSyncSubProjects(project_id);
    			} else {
    				if (integer_list != null && !integer_list.isSuccess) {
    					CacheManager.setNetErrorMsg(integer_list.errorString);
    				}
    				mHandler.sendMessage(mHandler.obtainMessage(2, "网络工程列表同步失败:"+CacheManager.getNetErrorMsg()));
    				return;
    			}
    		}
    		if (subproject_list != null && subproject_list.isSuccess) {
//    			AppUtil.printSubProjectList(subproject_list);
    			mHandler.sendMessage(mHandler.obtainMessage(1, subproject_list));
    		} else {
    			if (subproject_list != null && !subproject_list.isSuccess) {
    				CacheManager.setNetErrorMsg(subproject_list.errorString);
    			}
    			AppUtil.log("subproject_list == null");
    			mHandler.sendMessage(mHandler.obtainMessage(2, "网络工程列表同步失败:"+CacheManager.getNetErrorMsg()));
    		}
//    		mHandler.sendEmptyMessage(3);
    		super.run();
    		isLoading = false;
    	}
    }
    
	 private final Handler mHandler = new Handler() {
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
				case 1:
					SubProjectListResponse object = (SubProjectListResponse)msg.obj;
					String subProjectName;
					int subProjectId;
					int direction = 1;
					int object_size = 0;
					if (object.Data != null) {
						object_size = object.Data.size();
						if (object_size != object.total) {
							AppUtil.log("object_size:"+object_size+" object.total:"+object.total);
						}
					}
					for (int i = 0; i < object_size; i++) {
						subProjectName = object.Data.get(i).subProjectName;
						subProjectId = object.Data.get(i).subProjectId;
						if (object.Data.get(i).IsDownDirection == 1) {
							direction = -1;
						} else {
							direction = 1;
						}
						mProjectBase.saveSubProectRecord(subProjectId, project_id, subProjectName, direction, "k99+999");
					}
					if (object_size > 0) {
						isUpdateTime = true;
					}
					if (isUpdateTime) {
						mProjectBase.getSubProjectNameList(mList, project_id);
						mListView.setAdapter(mAdapter);
						new Thread() {
		    				public void run() {
		    					AppUtil.log("synctime=============="+synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECT));
		    					synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECT, apiClient.getCurrentDateTime());
		    					AppUtil.log("synctime=============="+synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECT));
		    				};
		    			}.start();
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					SharedView.cancelProgressBar();
					UIUtilities.showToast(SubProjectListActivity.this, "网络工程数据获取成功", true);
					break;
					
				case 2:
					SharedView.cancelProgressBar();
					UIUtilities.showToast(SubProjectListActivity.this, (String)msg.obj, true);
					SharedView.showUserExpiredAlertDialog(SubProjectListActivity.this);
					break;
					
				case 3:
					SharedView.cancelProgressBar();
					break;
					
				case 4:
					SharedView.cancelProgressBar();
					UIUtilities.showToast(SubProjectListActivity.this, "网络错误", true);
					break;
					
				case 5:
					synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_SUBPROJECT, "2010-01-01 00:00:00");
					subproject_count = mProjectBase.getSubProjectCount();
		    		if (subproject_count > 0) {
		    			SharedView.showProgressBar(SubProjectListActivity.this, "正在清除本地工程......");
	    				mProjectBase.clearProject(project_id);
	    				synctimebase.clearProjectSyncTime(project_id);
	    				//show list after clearing
	    				mProjectBase.getSubProjectNameList(mList, project_id);
						mListView.setAdapter(mAdapter);
		    			SharedView.showDynamicMessage("本地工程数据清除完成！");
		    			try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		    			SharedView.showDynamicMessage("正在同步网络工程列表......");
		    		} else {
		    			SharedView.showProgressBar(SubProjectListActivity.this, "正在同步网络工程列表......");
		    		}
	        		new GetSubprojectListThread().start();
	        		break;
					
			default:
				break;
			}
		 };
	 };
    
    protected void onDestroy() {
    	MyActivityManager.removeActivity(SubProjectListActivity.this);
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
		mListView = (ListView)findViewById(R.id.eng_list);
		mAdapter = new MyAdapter(mList, context);
		title = (TextView)findViewById(R.id.englist_title);
		String proj_name = mProjectBase.getProjectName(project_id);
		title.setText("云服务->"+proj_name+"->工程列表");
		user_info_tv = (TextView) findViewById(R.id.user_info);
		user_info_tv.setText(CacheManager.getUserName() + " 欢迎你!");
		reload_bt = (Button) findViewById(R.id.reload_bt);
		reload_bt.setOnClickListener(new ReloadClickListener());
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AppUtil.log( "===========onItemClick===========");
				mAdapter.setSelectItem(position);
				mAdapter.notifyDataSetChanged();
				if (mProjectBase.openDb()) {
					subproject_id = mProjectBase.getSubProjectId(mList.get(position));
					CacheManager.setDbSubProjectId(subproject_id);
					Intent intent=new Intent(SubProjectListActivity.this, SyncSteelArchDataListActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		mListView.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				AppUtil.log( "===========onItemSelected===========");
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
			
		});
		
		mListView.setAdapter(mAdapter);
	}
	
	class ReloadClickListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (isLoading) return;
			new  AlertDialog.Builder(SubProjectListActivity.this)    
            .setTitle("提示" )
            .setIcon(android.R.drawable.ic_dialog_info)
            .setMessage("该操作首先会清除该合同段下所有已下载的工程，然后才进行重新下载，建议用户在数据下载失败的情况下使用该功能，是否操作?" )  
            .setPositiveButton("确定" , new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					isLoading = true;
					new reloadClickThread().start();
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
			mHandler.sendEmptyMessage(5);
		}
	}
	
	class MyAdapter extends BaseAdapter {

		
		private Context context;
		private ArrayList<String> list;
		int selectItem = -1;
		
		public MyAdapter(ArrayList<String> list, Context context) {
			super();
			this.list = list;
			this.context = context;
		}
		
		class ViewHolder {
			TextView eng_name;
			LinearLayout layout;
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder iViewHolder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.engname_list_item, null);
				@SuppressWarnings("deprecation")
				LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				convertView.setLayoutParams(params);
				iViewHolder = new ViewHolder();
				iViewHolder.eng_name = (TextView)convertView.findViewById(R.id.eng_name);
				iViewHolder.layout = (LinearLayout)convertView.findViewById(R.id.eng_item_layout);
				convertView.setTag(iViewHolder);
			} else {
				iViewHolder = (ViewHolder) convertView.getTag();
			}
			iViewHolder.eng_name.setText(list.get(position));
			if (selectItem == position) {
				iViewHolder.layout.setBackgroundResource(R.drawable.item_sel);
			}
			else{
				iViewHolder.layout.setBackgroundResource(Color.TRANSPARENT);
			}

			return convertView;
		}
		
		public ArrayList<String> getList() {
			return list;
		}

		public void setList(ArrayList<String> list) {
			this.list = list;
		}

		public int getSelectItem() {
			return selectItem;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}
	}
}

