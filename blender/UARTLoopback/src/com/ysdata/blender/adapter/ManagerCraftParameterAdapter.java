package com.ysdata.blender.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ysdata.blender.R;
import com.ysdata.blender.activity.ManagerCraftFileActivity;
import com.ysdata.blender.cloud.util.CacheManager;
import com.ysdata.blender.element.ManagerCraftParameter;

public class ManagerCraftParameterAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<ManagerCraftParameter> list;
	static int selectItem = -1;
	TextView tv;
	
	class ViewHolder {
		TextView mix_id;
		TextView start_time;
		TextView blender_time;
		TextView cement_weigh;
		TextView water_weigh;
		TextView total_weigh;
	}
	
	public ManagerCraftParameterAdapter(ArrayList<ManagerCraftParameter> list, Context context) {
		super();
		this.list = list;
		this.context = context;
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
		OnClick listener = null; 
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.mgr_craft_listview_phone, null);
			iViewHolder = new ViewHolder();
			iViewHolder.mix_id = (TextView)convertView.findViewById(R.id.mgr_blender_seq);
			iViewHolder.start_time = (TextView)convertView.findViewById(R.id.mgr_blender_date);
			iViewHolder.blender_time = (TextView)convertView.findViewById(R.id.mgr_blender_time);
			iViewHolder.cement_weigh = (TextView)convertView.findViewById(R.id.mgr_cement_weigh);
			iViewHolder.water_weigh = (TextView)convertView.findViewById(R.id.mgr_water_weigh);
			iViewHolder.total_weigh = (TextView)convertView.findViewById(R.id.mgr_total_weigh);
			listener = new OnClick();//在这里新建监听对象 
			iViewHolder.mix_id.setOnClickListener(listener);
			convertView.setTag(iViewHolder);
			convertView.setTag(iViewHolder.start_time.getId(), listener);//对监听对象保存
		} else {
			iViewHolder = (ViewHolder) convertView.getTag();
			listener = (OnClick) convertView.getTag(iViewHolder.start_time.getId());//重新获得监听对象  
		}
		iViewHolder.mix_id.setText(list.get(position).getId()+"");
		iViewHolder.start_time.setText(list.get(position).getStartTime());
		iViewHolder.blender_time.setText(list.get(position).getBlenderTime()+"");
		iViewHolder.cement_weigh.setText(list.get(position).getCementWeight()+"");
		iViewHolder.water_weigh.setText(list.get(position).getWaterWeigh()+"");
		iViewHolder.total_weigh.setText(list.get(position).getTotalWeigh()+"");
		
		if (listener != null && convertView != null) {
			listener.setData(position, convertView); 
		}
		
		return convertView;
	}
	
	 class OnClick implements OnClickListener {  
	   int position;  
	   View view;
	      
	   public void setData(int position, View view) {  
	       this.position = position;  
	       this.view = view;
	   }   
	    @Override  
	   public void onClick(View v) {  
	    	CacheManager.setMgrCraftParameter(null);
    		CacheManager.setMgrCraftParameter(list.get(position));
    		Intent intent = new Intent(context, ManagerCraftFileActivity.class);
    		context.startActivity(intent);
	   }  
	}  
	
	public ArrayList<ManagerCraftParameter> getList() {
		return list;
	}

	public void setList(ArrayList<ManagerCraftParameter> list) {
		this.list = list;
	}

	public static int getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(int selectItem) {
		ManagerCraftParameterAdapter.selectItem = selectItem;
	}
}	