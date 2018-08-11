package com.ysdata.steelarch.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.activity.ManagerCraftFileActivity;
import com.ysdata.steelarch.cloud.util.CacheManager;
import com.ysdata.steelarch.element.ManagerCraftParameter;

public class ManagerCraftParameterAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<ManagerCraftParameter> list;
	static int selectItem = -1;
	TextView tv;
	
	class ViewHolder {
		TextView id;
		TextView name;
		TextView design_distance;
		TextView measure_distance;
		TextView steelarch_to_tunnelface_distance;
		TextView secondcar_to_tunnelface_distance;
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
			iViewHolder.id = (TextView)convertView.findViewById(R.id.mgr_steelarch_seq);
			iViewHolder.name = (TextView)convertView.findViewById(R.id.mgr_steelarch_name);
			iViewHolder.design_distance = (TextView)convertView.findViewById(R.id.mgr_design_steelarch_to_steelarch_distance);
			iViewHolder.measure_distance = (TextView)convertView.findViewById(R.id.mgr_measure_steelarch_to_steelarch_distance);
			iViewHolder.steelarch_to_tunnelface_distance = (TextView)convertView.findViewById(R.id.mgr_steelarch_to_tunelface_distance);
			iViewHolder.secondcar_to_tunnelface_distance = (TextView)convertView.findViewById(R.id.mgr_secondcar_to_tunelface_distance);
			listener = new OnClick();//在这里新建监听对象 
			iViewHolder.id.setOnClickListener(listener);
			convertView.setTag(iViewHolder);
			convertView.setTag(iViewHolder.name.getId(), listener);//对监听对象保存
		} else {
			iViewHolder = (ViewHolder) convertView.getTag();
			listener = (OnClick) convertView.getTag(iViewHolder.name.getId());//重新获得监听对象  
		}
		iViewHolder.id.setText(list.get(position).getId()+"");
		iViewHolder.name.setText(list.get(position).getName());
		iViewHolder.design_distance.setText(list.get(position).getDesignSteelarchToSteelarchDistance()+"");
		iViewHolder.measure_distance.setText(list.get(position).getMeasureSteelarchToSteelarchDistance()+"");
		iViewHolder.steelarch_to_tunnelface_distance.setText(list.get(position).getSteelarchToTunnelFaceDistance()+"");
		iViewHolder.secondcar_to_tunnelface_distance.setText(list.get(position).getSecondCarToTunnelFaceDistance()+"");
		
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