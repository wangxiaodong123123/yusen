package com.ysdata.steelarch.activity;

import java.io.File;
import java.util.ArrayList;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.adapter.TreeViewAdapter;
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
import com.ysdata.steelarch.database.ProjectPointDataBaseAdapter;
import com.ysdata.steelarch.database.SyncTimeDataBaseAdapter;
import com.ysdata.steelarch.element.TreeNode;
import com.ysdata.steelarch.storage.ExtSdCheck;
import com.ysdata.steelarch.uart.MyActivityManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ProjectTreeListView extends Activity {
	private ArrayList<TreeNode> mConstractSectionNode = new ArrayList<TreeNode>();
	private ArrayList<TreeNode> mSubProjectNode = new ArrayList<TreeNode>();
	ArrayList<String> constractSection_list = new ArrayList<String>();
	ArrayList<String> subprject_list = new ArrayList<String>();
	private TreeViewAdapter treeViewAdapter = null;
	private Context context = null;
	private ListView mListView;
	TextView title;
	int constractSection_count = 0;
	int project_count = 0;
	private ProjectDataBaseAdapter mProjectBase;
	private final static int ID_TREE_ROOT = 0;
	private final static int LEVEL_CONSTRACT_SECTION = 1;
	private final static int LEVEL_SUBPROJECT = 2;
	Activity localActivity;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        localActivity = ProjectTreeListView.this;
        setContentView(R.layout.project_tree_phone);
        MyActivityManager.addActivity(localActivity);
        mProjectBase = ProjectDataBaseAdapter.getSingleDataBaseAdapter(context);
        treeViewAdapter = new TreeViewAdapter(this,	mConstractSectionNode);
        mListView = (ListView)findViewById(R.id.project_list);
        title = (TextView)findViewById(R.id.projectTree_title);
		title.setText("数据管理->项目列表");
		if (mProjectBase.openDb()) {
			updateConstractSectionView();
			constractSection_count = constractSection_list.size();
			mListView.setOnItemClickListener(new TreeViewItemClickListener());
        } else {
        	Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
        }
    } 
    
    private void updateConstractSectionView() {
    	int constractSection_id;
    	int subproject_id;
    	constractSection_list.clear();
    	subprject_list.clear();
    	mConstractSectionNode.clear();
    	mSubProjectNode.clear();
    	mProjectBase.getProjectNameList(constractSection_list);
    	for (String constractSection : constractSection_list ) {
    		constractSection_id = mProjectBase.getProjectId(constractSection);
    		mProjectBase.getSubProjectNameList(subprject_list, constractSection_id);
    		mConstractSectionNode.add(new TreeNode(constractSection_id, constractSection, false, 
    				subprject_list.size() > 0, ID_TREE_ROOT, LEVEL_CONSTRACT_SECTION, false));
    		for (String subproject : subprject_list) {
    			subproject_id = mProjectBase.getSubProjectId(subproject);
    			mSubProjectNode.add(new TreeNode(subproject_id, subproject, true, 
    				false, constractSection_id, LEVEL_SUBPROJECT, false));
    		}
    	}
    	mListView.setAdapter(treeViewAdapter);
    }
    
    class TreeViewItemClickListener implements OnItemClickListener {
    	@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position,
    			long id) {
    		if (!mConstractSectionNode.get(position).isMhasChild()) {
    			TreeNode node = mConstractSectionNode.get(position);
    			if (node.getLevel() == LEVEL_SUBPROJECT) {
    				if (mProjectBase.openDb()) {
    					String extSdPath = ExtSdCheck.getExtSDCardPath();
    					int subproject_id = node.getId();
    					int project_id = node.getParentId();
    					CacheManager.setDbProjectId(project_id);
    					CacheManager.setDbSubProjectId(subproject_id);
    					
						String dir = extSdPath + ConstDef.DATABASE_PATH+project_id+"/"+subproject_id;
						File eng_dir_file = new File(dir);
						String file_lists[] = eng_dir_file.list();
						boolean dB_exsit = false;
						if (file_lists != null) {
							for (int i = 0; i < file_lists.length; i++) {
								if (file_lists[i].equals("YS200.db")) {
									dB_exsit = true;
									break;
								}
							}
							if (dB_exsit) {
								ProjectPointDataBaseAdapter mDataBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
								mDataBase.closeDb();
								mDataBase.openDb(project_id, subproject_id);
								if (mDataBase.getSteelArchCount() > 0) {
									Intent intent=new Intent(localActivity, ManagerCraftFileListActivity.class);
									startActivity(intent);
								} else {
									Toast.makeText(context, "该工程下无搅拌数据", Toast.LENGTH_SHORT).show();
									mDataBase.closeDb();
								}
							}  else {
								Toast.makeText(context, "该工程下无搅拌数据", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(context, "该工程下无搅拌数据", Toast.LENGTH_SHORT).show();
						}
    				} else {
    					Toast.makeText(context, "数据库打开失败.", Toast.LENGTH_SHORT).show();
    				}
    			}
    		} else {
    			if (mConstractSectionNode.get(position).isExpanded()) {
    				mConstractSectionNode.get(position).setExpanded(false);
    				TreeNode pdfOutlineElement=mConstractSectionNode.get(position);
    				ArrayList<TreeNode> temp=new ArrayList<TreeNode>();
    				
    				for (int i = position+1; i < mConstractSectionNode.size(); i++) {
    					if (pdfOutlineElement.getLevel()>=mConstractSectionNode.get(i).getLevel()) {
    						break;
    					}
    					temp.add(mConstractSectionNode.get(i));
    				}
    				
    				mConstractSectionNode.removeAll(temp);
    				
    				treeViewAdapter.notifyDataSetChanged();
    				/*fileExploreAdapter = new TreeViewAdapter(this, R.layout.outline,
    					mConstractSectionNode);*/
    				
    				//setListAdapter(fileExploreAdapter);
    				
    			} else {
    				mConstractSectionNode.get(position).setExpanded(true);
    				int level = mConstractSectionNode.get(position).getLevel();
    				int nextLevel = level + 1;
    				
    				for (TreeNode pdfOutlineElement : mSubProjectNode) {
    					int j=1;
    					if (pdfOutlineElement.getParentId()==mConstractSectionNode.get(position).getId()) {
    						pdfOutlineElement.setLevel(nextLevel);
    						pdfOutlineElement.setExpanded(false);
    						mConstractSectionNode.add(position+j, pdfOutlineElement);
    						j++;
    					}			
    				}
    				treeViewAdapter.notifyDataSetChanged();
    				/*fileExploreAdapter = new TreeViewAdapter(this, R.layout.outline,
    					mConstractSectionNode);*/
    				
    				//setListAdapter(fileExploreAdapter);
    			}
    		}
    	}
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	if (mProjectBase != null) {
    		mProjectBase.closeDb();
    	}
    	MyActivityManager.removeActivity(localActivity);
    	super.onDestroy();
    }
}
