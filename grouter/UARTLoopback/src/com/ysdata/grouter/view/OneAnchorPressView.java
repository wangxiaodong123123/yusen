package com.ysdata.grouter.view;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.ysdata.grouter.cloud.util.AppUtil;
import com.ysdata.grouter.cloud.util.CacheManager;
import com.ysdata.grouter.cloud.util.ConstDef;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;
import com.ysdata.grouter.element.ManagerCraftParameter;
import com.ysdata.grouter.element.WrcardParameter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation")
public class OneAnchorPressView extends View {

	/*
	 * 控件的中心点
	 */
	int pixel_bias = 20;
	int pixel_baseX, pixel_baseY;
	int pixel_end_scaleX, pixel_end_scaleY;
	int pixel_arrowX, pixel_arrowY;
	float pressure_max = 0;
	float time_max = 0;
	float max_x = 0;
	float max_y = 0;
	float[] value_p = new float[1024];
	float[] value_t = new float[1024];
	int adc_points = 0;
	private final int dx_pix = 20;
	private final int dy_pix = 10;
	
	float design_pressure = 0;
	String device = "panel";
//	private Value mValue;

	public OneAnchorPressView(Context context) {
		super(context);
	}
	
	public OneAnchorPressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ProjectPointDataBaseAdapter mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
        int anchor_id = CacheManager.getAnchorId();
        int project_id = CacheManager.getDbProjectId();
        int subproject_id = CacheManager.getDbSubProjectId();
        String anchor_name = CacheManager.getAnchorName();
        if (mProjectPointBase.openDb(project_id, subproject_id)) {
        	device = CacheManager.getDevice();
        	if (device.equals("panel")) {
        		value_t = mProjectPointBase.getAnchorCollectPressureTimeNodeData(anchor_id);
        		value_p = mProjectPointBase.getAnchorCollectPressureData(anchor_id);
        		adc_points = CacheManager.getLenTimeNode();
        		ManagerCraftParameter mManagerCraftParameter = mProjectPointBase.getMgrCraftParameter(anchor_id);
        		design_pressure = (float)mManagerCraftParameter.getDesignPressure() * 1000;
        	} else {
        		value_t = mProjectPointBase.getWrcardAnchorCollectPressureTimeNodeData(anchor_name);
        		value_p = mProjectPointBase.getWrcardAnchorCollectPressureData(anchor_name);
        		adc_points = CacheManager.getLenTimeNode();
        		WrcardParameter mWrcardParameter = mProjectPointBase.getWrcardCraftParameter(anchor_name);
        		design_pressure = (float)mWrcardParameter.getDesignPress() * 1000;
        	}
        	int index = 0;
        	if (adc_points > 1024) adc_points = 1024;
        	max_y = 0;
        	for (index = 0; index < adc_points; index++) {
        		value_p[index] = value_p[index] * 1000;
        		if (pressure_max < value_p[index]) pressure_max = value_p[index];
        	}
        	AppUtil.log("pressure_max:"+pressure_max);
        	if (adc_points > 0) {
        		max_x = value_t[adc_points-1];
        		max_y = pressure_max;
        		float hold_press_min = 0;
        		float hold_dn_press = (float) (design_pressure * 1.05);
        		float hold_up_press = (float) (design_pressure * ConstDef.HOLD_UP_RATE);
//        	AppUtil.log( "hold_dn_press:"+hold_dn_press+" hold_up_press:"+hold_up_press);
        		for (int i = 0; i < adc_points; i++) {
        			if (value_p[i] >= hold_dn_press) {
        				hold_press_min = value_p[i+1];
        				for (int j = i+2; j < adc_points; j++) {
        					if (value_p[j] <= hold_up_press && hold_press_min > value_p[j]) {
        						hold_press_min = value_p[j];
        					}
        				}
        				break;
        			}
        		}
        		DecimalFormat df = new DecimalFormat("##.##");
        		if (pressure_max >= hold_press_min) {
        			CacheManager.setHoldPressRange(df.format(hold_press_min/1000)+"~"+df.format((float)pressure_max/1000));
        		} else {
        			CacheManager.setHoldPressRange("0~0");
        		}
        	}
        }
    }
	
    /*
     * 控件创建完成之后，在显示之前都会调用这个方法，此时可以获取控件的大小 并得到中心坐标和坐标轴圆心所在的点。
     */
    @Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        pixel_baseX = 2*pixel_bias;				//横轴原点像素
        pixel_baseY = h - 2*pixel_bias;			//纵轴原点像素
        pixel_end_scaleX = w - 5*pixel_bias;			//横轴终点刻度像素
        pixel_end_scaleY = 4*pixel_bias;				//纵轴终点刻度像素
        pixel_arrowX = w - pixel_baseX;			//横轴终点像素
        pixel_arrowY = pixel_bias;				//纵轴终点像素
    }

    /*
     * 自定义控件一般都会重载onDraw(Canvas canvas)方法，来绘制自己想要的图形
     */
	@SuppressLint("DrawAllocation")
	@Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画坐标轴
        if (canvas == null) 
        	return;
        canvas.drawColor(Color.WHITE);      
        
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);

    	// 画直线
    	canvas.drawLine(pixel_baseX, pixel_baseY, pixel_arrowX, pixel_baseY, paint); 	//x
    	canvas.drawLine(pixel_baseX, pixel_baseY, pixel_baseX, pixel_arrowY, paint);	//y
    	
    	// 画X轴箭头
    	int x = pixel_arrowX, y = pixel_baseY;
    	drawTriangle(canvas, new Point(x, y), new Point(x - 10, y - 5),
    			new Point(x - 10, y + 5));
    	canvas.drawText("T(s)", x - 15, y + 18, paint);
    	// 画Y轴箭头
    	x = pixel_baseX;
    	y = pixel_arrowY;
    	drawTriangle(canvas, new Point(x, y), new Point(x - 5, y + 10),
    			new Point(x + 5, y + 10));
    	canvas.drawText("P(mpa)", x + 12, y + 15, paint);
    	
    	// 然后显示坐标
//    	canvas.drawText("0", pixel_baseX - 10, pixel_baseY + 15, paint);
    	
//    	drawHorizontalScale(canvas, pixel_baseX, pixel_end_scaleX, scalesX, max_x);
//    	drawVerticalScale(canvas, pixel_baseY, pixel_end_scaleY, scalesY, max_y);
    	Paint pt = new Paint();
        pt.setStrokeWidth(2);
        pt.setColor(Color.BLACK);
    	drawPicture(canvas, value_t, value_p, adc_points ,pt);
    }
	
	private float getListMinAbs(ArrayList<Float> list, float next_val) {
		float abs_val = Math.abs(list.get(0) - next_val);
		for (int i = 1; i < list.size(); i++) {
			if (Math.abs(list.get(i) - next_val) < abs_val) {
				abs_val = Math.abs(list.get(i) - next_val);
			}
		}
		return abs_val;
	}
	
	void drawTextAngle(Canvas canvas ,String text , float x ,float y,Paint paint ,float angle){  
		if(angle != 0){  
		    canvas.rotate(angle, x, y);   
		}  
		canvas.drawText(text, x, y, paint);  
		if(angle != 0){  
		    canvas.rotate(-angle, x, y);   
		}  
	}
	
    private void drawPicture(Canvas canvas, float[] x, float[] y, int adc_points, Paint p) {
        float start_x = pixel_baseX;
        float start_y = pixel_baseY;
        float end_y = getPathStartY(pixel_baseY, pixel_end_scaleY, max_y, max_y);
        float end_x = getPathStartX(pixel_baseX, pixel_end_scaleX, max_x, max_x);
        float next_x = 0;
        float next_y = 0;
        float start_dx = start_x;
        float start_dy = start_y;
        
        Path path = new Path();       
        Paint pt_dot = new Paint();   
        pt_dot.setStyle(Style.STROKE);   
        pt_dot.setColor(Color.BLACK);   
        pt_dot.setStrokeWidth(1);   
        PathEffect effects = new DashPathEffect(new float[] {5, 5, 5, 5}, 1);   
        pt_dot.setPathEffect(effects);  
        Paint pt_dot_red = new Paint();   
        pt_dot_red.setStyle(Style.STROKE);   
        pt_dot_red.setColor(Color.RED);   
        pt_dot_red.setStrokeWidth(1);   
        PathEffect effects_red = new DashPathEffect(new float[] {5, 5, 5, 5}, 1);   
        pt_dot_red.setPathEffect(effects_red); 
        float red_y = getPathStartY(pixel_baseY, pixel_end_scaleY, design_pressure, max_y);
        float red_x = pixel_arrowX;
        path.reset();
        path.moveTo(pixel_baseX - 10, red_y);
        path.lineTo(red_x, red_y);
        canvas.drawPath(path, pt_dot_red);
        DecimalFormat df = new DecimalFormat("##.##");
        ArrayList<Float> liney_list = new ArrayList<Float>();
        liney_list.add(start_dy);
        for (int i = 0; i < adc_points; i++) {
        	next_x = getPathStartX(pixel_baseX, pixel_end_scaleX, x[i], max_x);
        	next_y = getPathStartY(pixel_baseY, pixel_end_scaleY, y[i], max_y);
        	if (next_x == start_x && y[i] != max_y) {
        		continue;
        	}
        	canvas.drawLine(start_x, start_y, next_x, next_y, p);
        	AppUtil.log( "("+x[i]+","+y[i]+"),("+next_x+","+next_y+")");
//			if ((Math.abs(next_y - start_dy) >= dy_pix) /*&& (test == false)*/) {
			if (getListMinAbs(liney_list, next_y) >= dy_pix && (next_y - end_y) >= dy_pix) {
				if (y[i] != design_pressure) {
	        		path.reset();
					path.moveTo(pixel_baseX, next_y);  
					path.lineTo(next_x, next_y);
					canvas.drawPath(path, pt_dot);  
        		}
				canvas.drawText(df.format((float)y[i]/1000), pixel_baseX - 30, 
						next_y + 5, p);
//				start_dy = next_y;
				liney_list.add(next_y);
			}	else if (next_y == end_y && getListMinAbs(liney_list, next_y) >= dy_pix) {
				if (y[i] != design_pressure) {
					path.reset();
					path.moveTo(pixel_baseX, next_y);  
					path.lineTo(next_x, next_y);
					canvas.drawPath(path, pt_dot);  
        		}
				canvas.drawText(df.format((float)y[i]/1000), pixel_baseX - 30, 
						next_y + 5, p);
				liney_list.add(next_y);
			}
    			
			if (Math.abs(next_x - start_dx) >= dx_pix && (end_x - next_x) >= dx_pix) {
				path.reset();
				path.moveTo(next_x, pixel_baseY);  
				path.lineTo(next_x, next_y);
				canvas.drawPath(path, pt_dot);  
//				canvas.drawText(""+x[i], next_x - 5, pixel_baseY + 15, p); //显示时间刻度
				drawTextAngle(canvas, ""+x[i], next_x - 5, pixel_baseY + 15, p, 45);
				start_dx = next_x;
			} else if (next_x == end_x && start_x != end_x) {
				path.reset();
				path.moveTo(next_x, pixel_baseY);  
				path.lineTo(next_x, next_y);
				canvas.drawPath(path, pt_dot);  
//				canvas.drawText(""+x[i], next_x - 5, pixel_baseY + 15, p); //显示时间刻度
				drawTextAngle(canvas, ""+x[i], next_x - 5, pixel_baseY + 15, p, 45);
			}
        	start_x = next_x;
        	start_y = next_y;
        }
        path.close();
    }
    
    private float getPathStartX(int base, float end, float value, float vmax) {
//    	AppUtil.log( "base:"+base + ", end:"+end+", vmax:"+vmax+", value:"+value);
    	return base + ((end - base)/vmax)*value;
    }
    
    private float getPathStartY(int base, float end, float value, float vmax) {
    	return base - ((base - end)/vmax)*value;
    }
  
    /**
     * 画三角形 用于画坐标轴的箭头
     */
    private void drawTriangle(Canvas canvas, Point p1, Point p2, Point p3) {
        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.close();

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        // 绘制这个多边形
        canvas.drawPath(path, paint);
    }
}
