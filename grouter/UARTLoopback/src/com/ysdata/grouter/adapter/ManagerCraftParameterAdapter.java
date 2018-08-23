package com.ysdata.grouter.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysdata.grouter.R;
import com.ysdata.grouter.activity.MgrOneAnchorRecordActivity;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.CacheManager;
import com.ysdata.grouter.element.ManagerCraftParameter;

public class ManagerCraftParameterAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<ManagerCraftParameter> list;
//	private ArrayList<Float> mCapUmeterList;
	static int selectItem = -1;
	TextView tv;
	
	class ViewHolder {
		TextView anchor_name;
		TextView anchor_type;		
		TextView anchor_model;		
		TextView design_anchor_len;
		TextView date;
		TextView start_date;
		TextView end_date;
		TextView thereo_cap;
		TextView practice_cap;
		TextView design_pressure;
		TextView measure_pressure;
		TextView design_hold_time;
		TextView practice_hold_time;
		TextView remark;
		ImageView wrcard_sign;
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
			iViewHolder.anchor_name = (TextView)convertView.findViewById(R.id.mgr_seq);
			iViewHolder.anchor_type = (TextView)convertView.findViewById(R.id.mgr_anchor_type);
			iViewHolder.anchor_model = (TextView)convertView.findViewById(R.id.mgr_anchor_model);
			iViewHolder.design_anchor_len = (TextView)convertView.findViewById(R.id.mgr_design_len);
			iViewHolder.date = (TextView)convertView.findViewById(R.id.mgr_date);
			iViewHolder.start_date = (TextView)convertView.findViewById(R.id.mgr_start_date);
			iViewHolder.end_date = (TextView)convertView.findViewById(R.id.mgr_end_date);
			iViewHolder.thereo_cap = (TextView)convertView.findViewById(R.id.mgr_thereo_cap);
			iViewHolder.practice_cap = (TextView)convertView.findViewById(R.id.mgr_practice_cap);
			iViewHolder.design_pressure = (TextView)convertView.findViewById(R.id.mgr_design_pressure);
			iViewHolder.measure_pressure = (TextView)convertView.findViewById(R.id.mgr_measure_pressure);
			iViewHolder.design_hold_time = (TextView)convertView.findViewById(R.id.mgr_design_hold_time);
			iViewHolder.practice_hold_time = (TextView)convertView.findViewById(R.id.mgr_practice_hold_time);
			iViewHolder.remark = (TextView)convertView.findViewById(R.id.mgr_remark);
			iViewHolder.wrcard_sign = (ImageView)convertView.findViewById(R.id.mgr_wrcard_sign);
			listener = new OnClick();//在这里新建监听对象 
			iViewHolder.anchor_name.setOnClickListener(listener);
			convertView.setTag(iViewHolder);
			convertView.setTag(iViewHolder.anchor_type.getId(), listener);//对监听对象保存
		} else {
			iViewHolder = (ViewHolder) convertView.getTag();
			listener = (OnClick) convertView.getTag(iViewHolder.anchor_type.getId());//重新获得监听对象  
		}
		iViewHolder.anchor_name.setText(list.get(position).getAnchorName());
		iViewHolder.anchor_type.setText(list.get(position).getAnchorType());
		iViewHolder.anchor_model.setText(list.get(position).getAnchorModel());
		iViewHolder.design_anchor_len.setText(list.get(position).getDesignLen()+"");
		iViewHolder.date.setText(list.get(position).getDate());
		iViewHolder.start_date.setText(list.get(position).getStartDate());
		iViewHolder.end_date.setText(list.get(position).getEndDate());
		iViewHolder.thereo_cap.setText(list.get(position).getThereoCap()+"");
		iViewHolder.practice_cap.setText(list.get(position).getPracticeCap()+"");
		iViewHolder.design_pressure.setText(list.get(position).getDesignPressure()+"");
		iViewHolder.measure_pressure.setText(list.get(position).getMeasurePressure()+"");
		iViewHolder.design_hold_time.setText(list.get(position).getDesignHoldTime()+"");
		iViewHolder.practice_hold_time.setText(list.get(position).getPracticeHoldTime()+"");
		iViewHolder.date.setText(list.get(position).getDate());
		iViewHolder.start_date.setText(list.get(position).getStartDate());
		iViewHolder.end_date.setText(list.get(position).getEndDate());
		iViewHolder.remark.setText(list.get(position).getRemark());
		if (list.get(position).getWrcardSign() == 1) {
			iViewHolder.wrcard_sign.setVisibility(View.VISIBLE);
		} else {
			iViewHolder.wrcard_sign.setVisibility(View.INVISIBLE);
		}
		if (listener != null && convertView != null) {
			listener.setData(position, convertView); 
		}
		if (selectItem == position){

		}
		else{
		
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
	    		CacheManager.setAnchorId(list.get(position).getAnchorId());
	    		AppUtil.log("anchor_id:"+list.get(position).getAnchorId());
	    		CacheManager.setHoldCap(0);
	    		CacheManager.setHoldPressRange("");
	    		CacheManager.setPracticeHoldTime(0);
	    		CacheManager.setDesignHoldTime(0);
	    		CacheManager.setAnchorName(list.get(position).getAnchorName());
	    		Intent intent = new Intent(context, MgrOneAnchorRecordActivity.class);
	    		intent.putExtra("anchor_id", list.get(position).getAnchorId());
	    		intent.putExtra("anchor_name", list.get(position).getAnchorName());
	    		if (list.get(position).getWrcardSign() == 1) {
	    			intent.putExtra("mileage_id", list.get(position-1).getMileageId());
	    			CacheManager.setDevice("wrcard");
	    		} else {
	    			intent.putExtra("mileage_id", list.get(position).getMileageId());
	    			CacheManager.setDevice("panel");
	    		}
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