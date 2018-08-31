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
import com.ysdata.blender.element.DdBlenderDataParameter;
import com.ysdata.blender.element.ManagerCraftParameter;

public class DdBlenderDataParameterAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<DdBlenderDataParameter> list;
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
	
	public DdBlenderDataParameterAdapter(ArrayList<DdBlenderDataParameter> list, Context context) {
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.blenderdata_listview, null);
			iViewHolder = new ViewHolder();
			iViewHolder.mix_id = (TextView)convertView.findViewById(R.id.id_dd_seq);
			iViewHolder.start_time = (TextView)convertView.findViewById(R.id.id_dd_date);
			iViewHolder.blender_time = (TextView)convertView.findViewById(R.id.id_dd_time);
			iViewHolder.cement_weigh = (TextView)convertView.findViewById(R.id.id_dd_cement_weigh);
			iViewHolder.water_weigh = (TextView)convertView.findViewById(R.id.id_dd_water_weigh);
			iViewHolder.total_weigh = (TextView)convertView.findViewById(R.id.id_dd_weigh);
			convertView.setTag(iViewHolder);
		} else {
			iViewHolder = (ViewHolder) convertView.getTag();
		}
		iViewHolder.mix_id.setText(list.get(position).getId()+"");
		iViewHolder.start_time.setText(list.get(position).getStartTime());
		iViewHolder.blender_time.setText(list.get(position).getBlenderTime()+"");
		iViewHolder.cement_weigh.setText(list.get(position).getCementWeight()+"");
		iViewHolder.water_weigh.setText(list.get(position).getWaterWeigh()+"");
		iViewHolder.total_weigh.setText(list.get(position).getTotalWeigh()+"");
		
		return convertView;
	}
	
	public ArrayList<DdBlenderDataParameter> getList() {
		return list;
	}

	public void setList(ArrayList<DdBlenderDataParameter> list) {
		this.list = list;
	}

	public static int getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(int selectItem) {
		DdBlenderDataParameterAdapter.selectItem = selectItem;
	}
}	