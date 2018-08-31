package com.ysdata.blender.adapter;

import java.util.List;

import com.ysdata.blender.R;
import com.ysdata.blender.element.TreeNode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TreeViewAdapter extends BaseAdapter {

	private List<TreeNode> mfilelist;
	private Bitmap mIconCollapse;
	private Bitmap mIconExpand;
	private Context context;
	
	class ViewHolder {
		TextView text;
		ImageView icon;
	}
	
	public TreeViewAdapter(Context context, List<TreeNode> objects) {
		this.context = context;
		mfilelist = objects;
		mIconCollapse = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.triangle_direction_right);
		mIconExpand = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.triangle_direction_down);
	}

	public int getCount() {
		return mfilelist.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder; 
		if (convertView == null) { 
			convertView = LayoutInflater.from(context).inflate(R.layout.project_tree_item, null);
			viewHolder = new ViewHolder(); 
			viewHolder.text = (TextView) convertView.findViewById(R.id.text); 
			viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon); 
			convertView.setTag(viewHolder); 
		} else { 
			viewHolder = (ViewHolder) convertView.getTag(); 
		} 
		
		TreeNode element = mfilelist.get(position);
		int level = element.getLevel();
		viewHolder.icon.setPadding(40 * (level + 0), viewHolder.icon
				.getPaddingTop(), 0, viewHolder.icon.getPaddingBottom());
		viewHolder.text.setText(element.getName());
		if (element.isMhasChild() && !element.isExpanded()) {  
			viewHolder.icon.setImageBitmap(mIconCollapse);  
			viewHolder.icon.setVisibility(View.VISIBLE);  
        } else if (element.isMhasChild() && element.isExpanded()) {  
        	viewHolder.icon.setImageBitmap(mIconExpand);  
        	viewHolder.icon.setVisibility(View.VISIBLE);  
        } else if (!element.isMhasChild()) {  
        	viewHolder.icon.setImageBitmap(mIconCollapse);  
        	viewHolder.icon.setVisibility(View.INVISIBLE);  
        }  
		return convertView;
	}
}
