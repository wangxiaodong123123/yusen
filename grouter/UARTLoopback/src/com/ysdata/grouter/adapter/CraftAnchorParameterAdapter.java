package com.ysdata.grouter.adapter;

import java.util.ArrayList;

import com.ysdata.grouter.R;
import com.ysdata.grouter.element.AnchorParameter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CraftAnchorParameterAdapter extends BaseAdapter{

	public static final String TAG = "ys200";
	
	private Context context;
	private ArrayList<AnchorParameter> list;
	static int selectItem = -1;
	TextView tv;
	int tvId;
	private String mileage = "";
	
	class ViewHolder {
		TextView anchor_name;
		TextView anchor_type;
		TextView anchor_model;		
		TextView design_anchor_len;
		TextView design_pressure;
		TextView hold_time;
		TextView design_cap;
		TextView cap_unit_meter;
		TextView cap_unit_hour;
		TextView remark;
		TextView transfer_sign;
	}
	
	public CraftAnchorParameterAdapter(ArrayList<AnchorParameter> list, Context context) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.craft_table_listview_phone, null);
			iViewHolder = new ViewHolder();
			iViewHolder.anchor_name = (TextView)convertView.findViewById(R.id.craft_anchor_seq);
			iViewHolder.anchor_type = (TextView)convertView.findViewById(R.id.craft_anchor_type);
			iViewHolder.anchor_model = (TextView)convertView.findViewById(R.id.craft_anchor_model);
			iViewHolder.design_anchor_len = (TextView)convertView.findViewById(R.id.craft_design_anchor_len);
			iViewHolder.design_pressure = (TextView)convertView.findViewById(R.id.craft_design_pressure);
			iViewHolder.hold_time = (TextView)convertView.findViewById(R.id.craft_hold_time);
			iViewHolder.design_cap = (TextView)convertView.findViewById(R.id.craft_design_cap);
			iViewHolder.cap_unit_meter = (TextView)convertView.findViewById(R.id.craft_cap_unit_meter);
			iViewHolder.cap_unit_hour = (TextView)convertView.findViewById(R.id.craft_cap_unit_hour);
			iViewHolder.remark = (TextView)convertView.findViewById(R.id.craft_remark);
			iViewHolder.transfer_sign = (TextView)convertView.findViewById(R.id.craft_transfer_sign);
			convertView.setTag(iViewHolder);
		} else {
			iViewHolder = (ViewHolder) convertView.getTag();
		}
		iViewHolder.anchor_name.setText(list.get(position).getAnchorName());
		iViewHolder.anchor_type.setText(list.get(position).getAnchorType());
		iViewHolder.anchor_model.setText(list.get(position).getAnchorModel());
		iViewHolder.design_anchor_len.setText(list.get(position).getAnchorLen()+"");
		iViewHolder.design_pressure.setText(list.get(position).getAnchorDesignPressure()+"");
		iViewHolder.hold_time.setText(list.get(position).getHoldTime()+"");
		iViewHolder.design_cap.setText(list.get(position).getDesignCap()+"");
		iViewHolder.cap_unit_meter.setText(list.get(position).getUnitMeterCap()+"");
		iViewHolder.cap_unit_hour.setText(list.get(position).getUnitHourCap()+"");
		iViewHolder.remark.setText(list.get(position).getRemark());
		iViewHolder.transfer_sign.setText(list.get(position).getTransferSign());
		
//		Log.d(TAG, "---refresh listView----position:"+position);
		return convertView;
	}
	
	public ArrayList<AnchorParameter> getList() {
		return list;
	}

	public void setList(ArrayList<AnchorParameter> list) {
		this.list = list;
	}

	public static int getSelectItem() {
		return selectItem;
	}

	@SuppressWarnings("static-access")
	public void setSelectItem(int selectItem) {
		CraftAnchorParameterAdapter.selectItem = selectItem;
	}
}


