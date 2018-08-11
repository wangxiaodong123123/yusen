package com.ysdata.steelarch.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ysdata.steelarch.R;
import com.ysdata.steelarch.element.DdSteelArchDataParameter;

public class DdSteelArchDataParameterAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<DdSteelArchDataParameter> list;
	static int selectItem = -1;
	TextView tv;
	
	class ViewHolder {
		TextView id;
		TextView name;
		TextView date;
		TextView steelarch_to_steelarch_distance;
		TextView steelarch_to_tunnelface_distance;
		TextView secondcar_to_tunnelface_distance;
	}
	
	public DdSteelArchDataParameterAdapter(ArrayList<DdSteelArchDataParameter> list, Context context) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.steelarchdata_listview, null);
			iViewHolder = new ViewHolder();
			iViewHolder.id = (TextView)convertView.findViewById(R.id.id_dd_seq);
			iViewHolder.name = (TextView)convertView.findViewById(R.id.id_dd_name);
			iViewHolder.date = (TextView)convertView.findViewById(R.id.id_dd_date);
			iViewHolder.steelarch_to_steelarch_distance = (TextView)convertView.findViewById(R.id.id_dd_steelarch_to_steelarch_distance);
			iViewHolder.steelarch_to_tunnelface_distance = (TextView)convertView.findViewById(R.id.id_dd_steelarch_to_tunnelface_distance);
			iViewHolder.secondcar_to_tunnelface_distance = (TextView)convertView.findViewById(R.id.id_dd_secondcar_to_tunnelface_distance);
			convertView.setTag(iViewHolder);
		} else {
			iViewHolder = (ViewHolder) convertView.getTag();
		}
		iViewHolder.id.setText(list.get(position).getId()+"");
		iViewHolder.name.setText(list.get(position).getName());
		iViewHolder.date.setText(list.get(position).getDate());
		iViewHolder.steelarch_to_steelarch_distance.setText(list.get(position).getSteelarchToSteelarchDistance()+"");
		iViewHolder.steelarch_to_tunnelface_distance.setText(list.get(position).getSteelarchToTunnelFaceDistance()+"");
		iViewHolder.secondcar_to_tunnelface_distance.setText(list.get(position).getSecondCarToTunnelFaceDistance()+"");
		
		return convertView;
	}
	
	public ArrayList<DdSteelArchDataParameter> getList() {
		return list;
	}

	public void setList(ArrayList<DdSteelArchDataParameter> list) {
		this.list = list;
	}

	public static int getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(int selectItem) {
		DdSteelArchDataParameterAdapter.selectItem = selectItem;
	}
}	