package com.ysdata.grouter.adapter; 

import java.util.ArrayList; 

import com.ysdata.grouter.R;
import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.uart.MyActivityManager;
import com.ysdata.grouter.wireless.client.Format;

import android.content.Context; 
import android.content.Intent;
import android.view.LayoutInflater; 
import android.view.View; 
import android.view.View.OnClickListener;
import android.view.ViewGroup; 
import android.widget.BaseAdapter; 
import android.widget.Button;

//◊‘∂®“Â  ≈‰∆˜ 
public class MileageBrowserAdapter extends BaseAdapter {
	private ArrayList<String> mileageList; 

	Context context;
	class ViewHolder { 
		public Button button; 
	}

	public MileageBrowserAdapter(ArrayList<String> mileageList, Context context) { 
		super(); 
		this.context = context;
		this.mileageList = mileageList;
	} 
	
	@Override 
	public int getCount() { 
		if (null != mileageList) { 
			return mileageList.size(); 
		} else { 
			return 0; 
		} 
	} 
	
	@Override 
	public Object getItem(int position) { 
		return mileageList.get(position); 
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
		viewHolder.button.setText(mileageList.get(position)); 
		
		return convertView; 
	} 
	
	class OnClick implements OnClickListener {  
		@Override
		public void onClick(View v) {
			Button bt = (Button)v.findViewById(R.id.gridview_button_id);
			AppUtil.log("sectionName:"+bt.getText());
			Intent intent = new Intent(Format.ACTION_SEND_MILEAGE_NAME);
			intent.putExtra("sectionName", bt.getText());
			context.sendBroadcast(intent);
			if (MyActivityManager.getTopActivity().equals(context)) {
				MyActivityManager.getTopActivity().finish();
			}
		}
	}
} 
	