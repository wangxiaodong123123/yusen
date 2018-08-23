package com.ysdata.grouter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ysdata.grouter.R;
import com.ysdata.grouter.element.DdSubprojsectionParameter;

public class DdSubprojsectionParameterAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<DdSubprojsectionParameter> list;
	static int selectItem = -1;
	TextView tv;
	
	class ViewHolder {
		TextView mileage_name;
		TextView create_date;		
		TextView anchor_count;
		TextView grouted_count;
		TextView pic_dd_state;		
	}
	
	public DdSubprojsectionParameterAdapter(ArrayList<DdSubprojsectionParameter> list, Context context) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.subprojsection_listview_phone, null);
			iViewHolder = new ViewHolder();
			iViewHolder.mileage_name = (TextView)convertView.findViewById(R.id.id_section_name);
			iViewHolder.create_date = (TextView)convertView.findViewById(R.id.id_create_date);
			iViewHolder.anchor_count = (TextView)convertView.findViewById(R.id.id_anchor_count);
			iViewHolder.grouted_count = (TextView)convertView.findViewById(R.id.id_grouted_count);
			iViewHolder.pic_dd_state = (TextView)convertView.findViewById(R.id.id_pic_state);
			convertView.setTag(iViewHolder);
		} else {
			iViewHolder = (ViewHolder) convertView.getTag();
		}
		iViewHolder.mileage_name.setText(list.get(position).getMileageName());
		iViewHolder.create_date.setText(list.get(position).getCreateDate());
		iViewHolder.anchor_count.setText(list.get(position).getAnchorCount()+"");
		iViewHolder.grouted_count.setText(list.get(position).getGroutedCount()+"");
		if (list.get(position).getPicState()) {
			iViewHolder.pic_dd_state.setText("¡Ì");
		} else {
			iViewHolder.pic_dd_state.setText("X");
		}
		if (selectItem == position){

		}
		else{
		
		}
		return convertView;
	}
	
	public ArrayList<DdSubprojsectionParameter> getList() {
		return list;
	}

	public void setList(ArrayList<DdSubprojsectionParameter> list) {
		this.list = list;
	}

	public static int getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(int selectItem) {
		DdSubprojsectionParameterAdapter.selectItem = selectItem;
	}
}	