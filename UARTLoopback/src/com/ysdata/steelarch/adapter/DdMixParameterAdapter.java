package com.ysdata.steelarch.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.element.DdMixParameter;

public class DdMixParameterAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<DdMixParameter> list;
	static int selectItem = -1;
	TextView tv;
	
	class ViewHolder {
		TextView mix_ratio;
		TextView create_time;
	}
	
	public DdMixParameterAdapter(ArrayList<DdMixParameter> list, Context context) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.dd_mixratio_item, null);
			iViewHolder = new ViewHolder();
			iViewHolder.mix_ratio = (TextView)convertView.findViewById(R.id.id_mix_ratio);
			iViewHolder.create_time = (TextView)convertView.findViewById(R.id.id_create_time);
			convertView.setTag(iViewHolder);
		} else {
			iViewHolder = (ViewHolder) convertView.getTag();
		}
		iViewHolder.mix_ratio.setText(list.get(position).getMixRatio()+"");
		iViewHolder.create_time.setText(list.get(position).getCreateTime());
		
		return convertView;
	}
	
	public ArrayList<DdMixParameter> getList() {
		return list;
	}

	public void setList(ArrayList<DdMixParameter> list) {
		this.list = list;
	}

	public static int getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(int selectItem) {
		DdMixParameterAdapter.selectItem = selectItem;
	}
}	