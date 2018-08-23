package com.ysdata.grouter.adapter;

import java.util.ArrayList;

import com.ysdata.grouter.R;
import com.ysdata.grouter.element.MgrAnchorStasticParameter;

import android.content.Context;
import android.view.View.OnClickListener; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MgrAnchorStasticParameterAdapter extends BaseAdapter{

	public static final String TAG = "ys200";
	
	private Context context;
	private ArrayList<MgrAnchorStasticParameter> list;
	static int selectItem = -1;
	EditText remark_et;
	TextView remark_tv;
	int tvId;
	int mileage_number;
	boolean isClickListener = true;
	
	class ViewHolder {
		TextView anchor_type;		
		TextView anchor_model;		
		TextView design_anchor_sums;
		TextView design_anchor_length;
		TextView remark;
		Button confirm_bt;
		Button cancel_bt;
	}
	
	public MgrAnchorStasticParameterAdapter(ArrayList<MgrAnchorStasticParameter> list, Context context) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.anchor_stastic_table_listview_phone, null);
			iViewHolder = new ViewHolder();
			iViewHolder.anchor_type = (TextView)convertView.findViewById(R.id.stastic_type);
			iViewHolder.anchor_model = (TextView)convertView.findViewById(R.id.stastic_model);
			iViewHolder.design_anchor_sums = (TextView)convertView.findViewById(R.id.stastic_design_sums);
			iViewHolder.design_anchor_length = (TextView)convertView.findViewById(R.id.stastic_design_length);
			iViewHolder.remark = (TextView)convertView.findViewById(R.id.stastic_remark);
			iViewHolder.confirm_bt = (Button)convertView.findViewById(R.id.confirm_bt);
			iViewHolder.cancel_bt = (Button)convertView.findViewById(R.id.cancel_bt);
			listener = new OnClick();//在这里新建监听对象  
			iViewHolder.remark.setOnClickListener(listener);
			iViewHolder.confirm_bt.setOnClickListener(listener);
			iViewHolder.cancel_bt.setOnClickListener(listener);
			convertView.setTag(iViewHolder);
			convertView.setTag(iViewHolder.anchor_type.getId(), listener);//对监听对象保存
		} else {
			iViewHolder = (ViewHolder) convertView.getTag();
			listener = (OnClick) convertView.getTag(iViewHolder.anchor_type.getId());//重新获得监听对象  
		}
		iViewHolder.anchor_type.setText(list.get(position).getAnchorType());
		iViewHolder.anchor_model.setText(list.get(position).getAnchorModel());
		iViewHolder.design_anchor_sums.setText(list.get(position).getDesignSum()+"");
		iViewHolder.design_anchor_length.setText(list.get(position).getDesignLength()+"");
		iViewHolder.remark.setText(list.get(position).getRemark());
		listener.setData(position, convertView);
		if (selectItem == position){

		}
		else{
		
		}
		return convertView;
	}
	
	public boolean isCellEditable() {
		return !isClickListener;
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
		    	int viewId = v.getId();
		    	if (!isClickListener) {
		    		if (viewId == R.id.stastic_remark)
		    			return;
		    	}
		    	
		    	switch(viewId) {
		    	case R.id.stastic_remark:
		    		remark_tv = (TextView) view.findViewById(R.id.stastic_remark);
		    		remark_et = (EditText) view.findViewById(R.id.stastic_remark_et);
		    		remark_et.setText(remark_tv.getText().toString());
		    		remark_tv.setVisibility(View.INVISIBLE);
		    		remark_et.setVisibility(View.VISIBLE);
		    		view.findViewById(R.id.confirm_bt).setVisibility(View.VISIBLE);
		    		view.findViewById(R.id.cancel_bt).setVisibility(View.VISIBLE);
		    		isClickListener = false;
		    		break;
		    		
		    	case R.id.confirm_bt:
		    		String remark_et_string = remark_et.getText().toString();
		    		if(!remark_et_string.equals("")) {
		    			remark_tv.setText(remark_et_string);
		    			remark_et.setVisibility(View.INVISIBLE);
		    			view.findViewById(R.id.confirm_bt).setVisibility(View.INVISIBLE);
			    		view.findViewById(R.id.cancel_bt).setVisibility(View.INVISIBLE);
			    		remark_tv.setVisibility(View.VISIBLE);
			    		isClickListener = true;
		    		} else {
	        			Toast.makeText(context, "输入不能为空", Toast.LENGTH_SHORT).show();
	        		}
		    		break;
		    	case R.id.cancel_bt:
		    		remark_et.setVisibility(View.INVISIBLE);
	    			view.findViewById(R.id.confirm_bt).setVisibility(View.INVISIBLE);
		    		view.findViewById(R.id.cancel_bt).setVisibility(View.INVISIBLE);
		    		remark_tv.setVisibility(View.VISIBLE);
		    		isClickListener = true;
		    		break;
		    	}
		   }  
		}  

	
	public ArrayList<MgrAnchorStasticParameter> getList() {
		return list;
	}

	public void setList(ArrayList<MgrAnchorStasticParameter> list) {
		this.list = list;
	}

	public static int getSelectItem() {
		return selectItem;
	}

	public void setSelectItem(int selectItem) {
		MgrAnchorStasticParameterAdapter.selectItem = selectItem;
	}
}	