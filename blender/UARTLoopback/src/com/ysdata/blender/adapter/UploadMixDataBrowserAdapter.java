package com.ysdata.blender.adapter; 

import java.util.ArrayList; 

import com.ysdata.blender.R;
import com.ysdata.blender.cloud.util.AppUtil;
import com.ysdata.blender.element.MixUploadState;
import com.ysdata.blender.uart.MyActivityManager;
import com.ysdata.blender.wireless.client.Format;

import android.content.Context; 
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater; 
import android.view.View; 
import android.view.View.OnClickListener;
import android.view.ViewGroup; 
import android.widget.BaseAdapter; 
import android.widget.Button;

//自定义适配器 
public class UploadMixDataBrowserAdapter extends BaseAdapter {
	private ArrayList<MixUploadState> mixDateList; 

	Context context;
	class ViewHolder { 
		public Button button; 
	}

	public UploadMixDataBrowserAdapter(ArrayList<MixUploadState> mileageList, Context context) { 
		super(); 
		this.context = context;
		this.mixDateList = mileageList;
	} 
	
	@Override 
	public int getCount() { 
		if (null != mixDateList) { 
			return mixDateList.size(); 
		} else { 
			return 0; 
		} 
	} 
	
	@Override 
	public Object getItem(int position) { 
		return mixDateList.get(position); 
	} 
	
	@Override 
	public long getItemId(int position) { 
		return position; 
	} 
	
	@Override 
	public View getView(int position, View convertView, ViewGroup parent) { 
		ViewHolder viewHolder; 
		OnClick listener = null;
		if (convertView == null) { 
			convertView = LayoutInflater.from(context).inflate(R.layout.mileage_gridview_item, null);
			viewHolder = new ViewHolder(); 
			viewHolder.button = (Button) convertView.findViewById(R.id.gridview_button_id); 
			convertView.setTag(viewHolder); 
			listener = new OnClick();
			viewHolder.button.setOnClickListener(listener);
		} else { 
			viewHolder = (ViewHolder) convertView.getTag(); 
			listener = (OnClick) convertView.getTag(viewHolder.button.getId());
		} 
//		AppUtil.log( "position:"+position);
		viewHolder.button.setText(mixDateList.get(position).getMixDate());
		if (mixDateList.get(position).getState() == 1) { //未上传
			viewHolder.button.setBackgroundColor(Color.RED);
		} else { //已上传
			viewHolder.button.setBackgroundColor(Color.WHITE);
		}
		
		return convertView; 
	} 
	
	class OnClick implements OnClickListener {  
		@Override
		public void onClick(View v) {
			Button bt = (Button)v.findViewById(R.id.gridview_button_id);
			AppUtil.log("mixdate:"+bt.getText());
			Intent intent = new Intent(Format.ACTION_SEND_MILEAGE_NAME);
			intent.putExtra("mixdate", bt.getText());
			context.sendBroadcast(intent);
			if (MyActivityManager.getTopActivity().equals(context)) {
				MyActivityManager.getTopActivity().finish();
			}
		}
	}
} 
	