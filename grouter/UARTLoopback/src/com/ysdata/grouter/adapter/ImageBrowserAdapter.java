package com.ysdata.grouter.adapter; 

import java.util.ArrayList; 
import java.util.List; 

import com.ysdata.grouter.R;

import android.content.Context; 
import android.graphics.Bitmap;
import android.graphics.BitmapFactory; 
import android.graphics.Matrix;
import android.util.Log;

import com.ysdata.grouter.element.Picture;

import android.util.LruCache;
import android.view.LayoutInflater; 
import android.view.View; 
import android.view.ViewGroup; 
import android.widget.BaseAdapter; 
import android.widget.ImageView; 
import android.widget.TextView; 

//自定义适配器 
public class ImageBrowserAdapter extends BaseAdapter {
	private static final String TAG = "ys200";
	private List<Picture> pictures; 
	private LruCache<String, Bitmap> mMemoryCache;

	Context context;
	class ViewHolder { 
		public TextView title; 
		public ImageView image; 
	}

	public ImageBrowserAdapter(ArrayList<String> titles, ArrayList<String> images, Context context) { 
		super(); 
		this.context = context;
		pictures = new ArrayList<Picture>(); 
		int MAXMEMONRY = (int) (Runtime.getRuntime() .maxMemory());
		Log.d(TAG, "MAXMEMONRY:"+MAXMEMONRY);
		mMemoryCache = new LruCache<String, Bitmap>(MAXMEMONRY / 10) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
			// 重写此方法来衡量每张图片的大小，默认返回图片数量。
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}

			 @Override
			protected void entryRemoved(boolean evicted, String key,
			Bitmap oldValue, Bitmap newValue) {
				 Log.v(TAG, "hard cache is full , push to soft cache");
			}
		};
		for (int i = 0; i < images.size(); i++) { 
			Picture picture = new Picture(titles.get(i), images.get(i)); 
			pictures.add(picture); 
			Bitmap bmp = BitmapFactory.decodeFile(pictures.get( 
					i).getImageId());
			if (bmp != null) {
				int width = bmp.getWidth();  //缩放
			    int height = bmp.getHeight();
			    int newWidth = 536;
			    int newHeight = 360;
			    float scaleWidth = ((float) newWidth) / width;
			    float scaleHeight = ((float) newHeight) / height;
			    Matrix matrix = new Matrix();
			    matrix.postScale(scaleWidth, scaleHeight);
			    bmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix,true);
			    mMemoryCache.put(pictures.get(i).getTitle(), bmp);
			}
		} 
	} 
	
	@Override 
	public int getCount() { 
		if (null != pictures) { 
			return pictures.size(); 
		} else { 
			return 0; 
		} 
	} 
	
	@Override 
	public Object getItem(int position) { 
		return pictures.get(position); 
	} 
	
	@Override 
	public long getItemId(int position) { 
		return position; 
	} 
	
	@Override 
	public View getView(int position, View convertView, ViewGroup parent) { 
		ViewHolder viewHolder; 
		if (convertView == null) { 
			convertView = LayoutInflater.from(context).inflate(R.layout.image_gridview_item, null);
			viewHolder = new ViewHolder(); 
			viewHolder.title = (TextView) convertView.findViewById(R.id.image_title); 
			viewHolder.image = (ImageView) convertView.findViewById(R.id.grid_image); 
			convertView.setTag(viewHolder); 
		} else { 
			viewHolder = (ViewHolder) convertView.getTag(); 
		} 
		Log.d(TAG, "position:"+position);
		viewHolder.title.setText(pictures.get(position).getTitle()); 
		if (mMemoryCache.get(pictures.get(position).getTitle()) == null) {
			Bitmap bmp = BitmapFactory.decodeFile(pictures.get( 
					position).getImageId());
			if (bmp != null) {
				int width = bmp.getWidth();  //缩放
			    int height = bmp.getHeight();
			    int newWidth = 536;
			    int newHeight = 360;
			    float scaleWidth = ((float) newWidth) / width;
			    float scaleHeight = ((float) newHeight) / height;
			    Matrix matrix = new Matrix();
			    matrix.postScale(scaleWidth, scaleHeight);
			    bmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix,true);
			    mMemoryCache.put(pictures.get(position).getTitle(), bmp);
			}
			Log.d(TAG, "position:"+position+" cache not exisit.");
		} else {
			Log.d(TAG, "position:"+position+" cache exisit.");
		}
		if (mMemoryCache.get(pictures.get(position).getTitle()) != null) {
			viewHolder.image.setImageBitmap(mMemoryCache.get(pictures.get(position).getTitle())); 
		}
		
		return convertView; 
	} 
	
} 
	