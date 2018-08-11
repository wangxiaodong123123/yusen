package com.ysdata.steelarch.activity;

import java.util.ArrayList;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.cloud.api.ContractSectionListResponse;
import com.ysdata.steelarch.cloud.api.IntegerListResponse;
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

public class ContractSectionListActivity extends Activity{
	public static ContractSectionListActivity mProjectNameListActivity;
	private Context context = null;
	private ArrayList<String> mList = new ArrayList<String>();
	private MyAdapter mAdapter;
	private ListView mListView;
	private TextView title;
	private ProjectDataBaseAdapter mProjectBase;
	private SyncTimeDataBaseAdapter synctimebase;
	private ApiClient apiClient;
	TextView user_info_tv;
	Button reload_bt;
	int project_count = 0;
	boolean isLoading = true;
	boolean isUpdateTime = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contract_section_list_cloud_phone);
        context = this;
        MyActivityManager.addActivity(ContractSectionListActivity.this);
        mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
        synctimebase = SyncTimeDataBaseAdapter.getSingleDataBaseAdapter(context);
        apiClient = ApiClient.getInstance(context);
        if (mProjectBase.openDb() && synctimebase.openDb()) {
        	mProjectBase.getProjectNameList(mList);
        	project_count = mList.size();
        	InitView();
    		SharedView.showProgressBar(ContractSectionListActivity.this, "正在同步网络合同段列表......");
    		new GetContractSectionListThread().start();
        } else {
        	Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
        }
    }
    
    class GetContractSectionListThread extends Thread {
    	@Override
    	public void run() {
    		isLoading = true;
    		ContractSectionListResponse contract_section_list = null;
    		String sync_time = synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_CONTRACTSECTION);
    		if (project_count == 0 || sync_time.equals("2010-01-01 00:00:00")) {
    			if (project_count > 0) {
    				mProjectBase.clearAllProject();
    				synctimebase.clearAllSyncTimeRecord();
    			}
    			contract_section_list = apiClient.getContractSectionlist();
    			mProjectBase.initalCommunicateState();
    		} else {
    			IntegerListResponse integer_list = apiClient.getSyncDeleteContractSections();
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
    					int csId;
    					FileOperator mFileOperator = FileOperator.getSingleFileOperator(ContractSectionListActivity.this);
    					String database_path = ExtSdCheck.getExtSDCardPath() + ConstDef.DATABASE_PATH;
    					for (int i = 0; i < integer_list_size; i++) {
    						csId = integer_list.Data.get(i);
    						mProjectBase.deleteProject(csId);
    						mProjectBase.deleteSubProjectWhereCsId(csId);
    						mFileOperator.DeleteAllDir(database_path+csId);
    					}
    					isUpdateTime = true;
    				}
    				contract_section_list = apiClient.getSyncContractSections();
    			} else {
    				if (integer_list != null && !integer_list.isSuccess) {
    					CacheManager.setNetErrorMsg(integer_list.errorString);
    				}
    				mHandler.sendMessage(mHandler.obtainMessage(2, "网络合同段同步失败:"+ CacheManager.getNetErrorMsg()));
    				return;
    			}
    		}
    		if (contract_section_list != null && contract_section_list.isSuccess) {
    			mHandler.sendMessage(mHandler.obtainMessage(1, contract_section_list));
    		} else {
    			AppUtil.log("contract_section_list == null");
    			if (contract_section_list != null && !contract_section_list.isSuccess) {
    				CacheManager.setNetErrorMsg(contract_section_list.errorString);
    			}
    			mHandler.sendMessage(mHandler.obtainMessage(2, "网络合同段列表同步失败:"+ CacheManager.getNetErrorMsg()));
    		}
    		super.run();
    		isLoading = false;
    	}
    }
    
    private boolean CheckProjectRename(String csName) {
    	for (int i = 0; i < mList.size(); i++) {
    		if (mList.get(i).equals(csName)) 
    			return true;
    	}
    	return false;
    }
    
	 private final Handler mHandler = new Handler() {
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
				case 1:
					ContractSectionListResponse object = (ContractSectionListResponse)msg.obj;
					String csName;
					int csId;
					int object_size = 0;
					if (object.Data != null) {
						object_size = object.Data.size();
						if (object_size != object.total) {
							AppUtil.log("object_size:"+object_size+" object.total:"+object.total);
						}
					}
					for (int i = 0; i < object_size; i++) {
						csName = object.Data.get(i).csName;
						csId = object.Data.get(i).CsId;
						mProjectBase.saveProjectRecord(csId, csName);
					}
					if (object_size > 0) {
						isUpdateTime = true;
					}
					if (isUpdateTime) {
						mProjectBase.getProjectNameList(mList);
						mListView.setAdapter(mAdapter);
		    			new Thread() {
		    				public void run() {
		    					AppUtil.log("synctime=============="+synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_CONTRACTSECTION));
		    					synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_CONTRACTSECTION, 
		    							apiClient.getCurrentDateTime());
		    					AppUtil.log("synctime=============="+synctimebase.getSyncTime(ConstDef.SYNCTIME_TYPE_CONTRACTSECTION));
		    				};
		    			}.start();
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					SharedView.cancelProgressBar();
					UIUtilities.showToast(ContractSectionListActivity.this, "网络合同段列表同步成功", true);
					break;
					
				case 2:
					SharedView.cancelProgressBar();
					UIUtilities.showToast(ContractSectionListActivity.this, (String)msg.obj, true);
					SharedView.showUserExpiredAlertDialog(ContractSectionListActivity.this);
					break;
					
				case 3:
					SharedView.cancelProgressBar();
					break;
					
				case 4:
					SharedView.cancelProgressBar();
					UIUtilities.showToast(ContractSectionListActivity.this, "网络错误", true);
					break;
					
				case 5:
					synctimebase.saveSyncTime(ConstDef.SYNCTIME_TYPE_CONTRACTSECTION, "2010-01-01 00:00:00");
					project_count = mProjectBase.getProjectCount();
		    		if (project_count > 0) {
		    			SharedView.showProgressBar(ContractSectionListActivity.this, "正在清除本地合同段数据......");
	    				mProjectBase.clearAllProject();
	    				synctimebase.clearAllSyncTimeRecord();
	    				mProjectBase.initalCommunicateState();
	    				//show list after clearing
	    				mProjectBase.getProjectNameList(mList);
						mListView.setAdapter(mAdapter);
		    			SharedView.showDynamicMessage("本地合同段数据清除完成！");
		    			try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		    			SharedView.showDynamicMessage("正在同步网络合同段列表......");
		    		} else {
		    			SharedView.showProgressBar(ContractSectionListActivity.this, "正在同步网络合同段列表......");
		    		}
	        		new GetContractSectionListThread().start();
					break;
					
			default:
				break;
			}
		 };
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
		mListView = (ListView)findViewById(R.id.proj_list);
		mAdapter = new MyAdapter(mList, context);
		title = (TextView) findViewById(R.id.proj_list_title);
		title.setText("云服务->合同段列表");
		user_info_tv = (TextView) findViewById(R.id.user_info);
		user_info_tv.setText(CacheManager.getUserName() + " 欢迎你!");
		reload_bt = (Button) findViewById(R.id.reload_bt);
		reload_bt.setOnClickListener(new ReloadClickListener());
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mAdapter.setSelectItem(position);
				mAdapter.notifyDataSetChanged();
    			if (mProjectBase.openDb()) {
    				int project_id = mProjectBase.getProjectId(mList.get(position));
    				CacheManager.setDbProjectId(project_id);
					Intent intent=new Intent(ContractSectionListActivity.this,SubProjectListActivity.class);
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
			new  AlertDialog.Builder(ContractSectionListActivity.this)    
            .setTitle("提示" )
            .setIcon(android.R.drawable.ic_dialog_info)
            .setMessage("该操作首先会清除所有已下载的合同段数据，然后才进行重新下载，建议用户在数据下载失败的情况下使用该功能，是否操作?" )  
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
	
    @Override
    protected void onDestroy() {
    	if (mProjectBase != null) {
    		mProjectBase.closeDb();
    	}
    	if (synctimebase != null) {
    		synctimebase.closeDb();
    	}
    	MyActivityManager.removeActivity(ContractSectionListActivity.this);
    	super.onDestroy();
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
			TextView project_name;
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
				convertView = LayoutInflater.from(context).inflate(R.layout.proj_list_item, null);
				@SuppressWarnings("deprecation")
				LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				convertView.setLayoutParams(params);
				iViewHolder = new ViewHolder();
				iViewHolder.project_name = (TextView)convertView.findViewById(R.id.project_name);
				iViewHolder.layout = (LinearLayout)convertView.findViewById(R.id.proj_item_layout);
				convertView.setTag(iViewHolder);
			} else {
				iViewHolder = (ViewHolder) convertView.getTag();
			}
			iViewHolder.project_name.setText(list.get(position));
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
