package com.ysdata.grouter.view;

import java.text.DecimalFormat;

import com.ysdata.grouter.cloud.util.CacheManager;
import com.ysdata.grouter.database.ProjectPointDataBaseAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation")
public class OneAnchorCapView extends View {

	/*
	 * �ؼ������ĵ�
	 */
	int pixel_bias = 20;
	int pixel_baseX, pixel_baseY;
	int pixel_end_scaleX, pixel_end_scaleY;
	int pixel_arrowX, pixel_arrowY;
	float time_max = 0;
	float max_x = 0;
	float max_y = 0;
	float[] value_c = new float[1024];
	float[] value_t = new float[1024];
	int adc_points = 0;
	private final int dx_pix = 20;
	private final int dy_pix = 10;
	
	String device = "panel";
	
//	private Value mValue;

	public OneAnchorCapView(Context context) {
		super(context);
	}
	
	public OneAnchorCapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ProjectPointDataBaseAdapter mProjectPointBase = ProjectPointDataBaseAdapter.getSingleDataBaseAdapter(context);
        int anchor_id = CacheManager.getAnchorId();
        int project_id = CacheManager.getDbProjectId();
        int subproject_id = CacheManager.getDbSubProjectId();
        String anchor_name = CacheManager.getAnchorName();
        if (mProjectPointBase.openDb(project_id, subproject_id)) {
        	device = CacheManager.getDevice();
        	if (device.equals("panel")) {
        		value_t = mProjectPointBase.getAnchorCollectCapacityTimeNodeData(anchor_id);
        		value_c = mProjectPointBase.getAnchorCollectCapacityData(anchor_id);
        		adc_points = CacheManager.getLenTimeNode();
        	} else {
        		value_t = mProjectPointBase.getWrcardAnchorCollectCapacityTimeNodeData(anchor_name);
        		value_c = mProjectPointBase.getWrcardAnchorCollectCapacityData(anchor_name);
        		adc_points = CacheManager.getLenTimeNode();
        	}
        	if (adc_points > 0) {
        		if (adc_points > 1024) adc_points = 1024;
        		for (int index = 0; index < adc_points; index++) {
        			value_c[index] = value_c[index] * 1000;
        			if (max_y < value_c[index]) max_y = value_c[index];
        		}
        		max_x = value_t[adc_points-1];
        	}
        }
    }
	
    /*
     * �ؼ��������֮������ʾ֮ǰ������������������ʱ���Ի�ȡ�ؼ��Ĵ�С ���õ����������������Բ�����ڵĵ㡣
     */
    @Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        pixel_baseX = 2*pixel_bias;				//����ԭ������
        pixel_baseY = h - 2*pixel_bias;			//����ԭ������
        pixel_end_scaleX = w - 5*pixel_bias;			//�����յ�̶�����
        pixel_end_scaleY = 4*pixel_bias;				//�����յ�̶�����
        pixel_arrowX = w - pixel_baseX;			//�����յ�����
        pixel_arrowY = pixel_bias;				//�����յ�����
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
    
    /*
     * �Զ���ؼ�һ�㶼������onDraw(Canvas canvas)�������������Լ���Ҫ��ͼ��
     */
	@SuppressLint("DrawAllocation")
	@Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // ��������
        if (canvas == null) 
        	return;
        canvas.drawColor(Color.WHITE);      
        
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);

    	// ��ֱ��
    	canvas.drawLine(pixel_baseX, pixel_baseY, pixel_arrowX, pixel_baseY, paint); 	//x
    	canvas.drawLine(pixel_baseX, pixel_baseY, pixel_baseX, pixel_arrowY, paint);	//y
    	
    	// ��X���ͷ
    	int x = pixel_arrowX, y = pixel_baseY;
    	drawTriangle(canvas, new Point(x, y), new Point(x - 10, y - 5),
    			new Point(x - 10, y + 5));
    	canvas.drawText("T(s)", x - 15, y + 18, paint);
    	// ��Y���ͷ
    	x = pixel_baseX;
    	y = pixel_arrowY;
    	drawTriangle(canvas, new Point(x, y), new Point(x - 5, y + 10),
    			new Point(x + 5, y + 10));
    	canvas.drawText("Q(L)", x + 12, y + 15, paint);
    	
    	// Ȼ����ʾ����
//    	canvas.drawText("0", pixel_baseX - 10, pixel_baseY + 15, paint);
     	Paint pt = new Paint();
        pt.setStrokeWidth(2);
        pt.setColor(Color.BLACK);
        drawPicture(canvas, value_t, value_c, adc_points ,pt);
    }
	
	private void drawPicture(Canvas canvas, float[] x, float[] y, int adc_points, Paint p) {
        float start_x = pixel_baseX;
        float start_y = pixel_baseY;
        float next_x = 0;
        float next_y = 0;
        float start_dx = start_x;
        float start_dy = start_y;
        
        float end_x = getPathStartX(pixel_baseX, pixel_end_scaleX, max_x, max_x);
    	float end_y = getPathStartY(pixel_baseY, pixel_end_scaleY, max_y, max_y);
        
        Path path = new Path();       
        Paint pt_dot = new Paint();   
        pt_dot.setStyle(Style.STROKE);   
        pt_dot.setColor(Color.BLACK);   
        pt_dot.setStrokeWidth(1);   
        PathEffect effects = new DashPathEffect(new float[] {5, 5, 5, 5}, 1);   
        pt_dot.setPathEffect(effects);  
        
        DecimalFormat df = new DecimalFormat("##.##");
        for (int i = 0; i < adc_points; i++) {
        	next_x = getPathStartX(pixel_baseX, pixel_end_scaleX, x[i], max_x);
        	next_y = getPathStartY(pixel_baseY, pixel_end_scaleY, y[i], max_y);
//        	AppUtil.log( "cap:("+x[i]+","+y[i]+"),("+next_x+","+next_y+")");
//    		AppUtil.log( "y["+i+"]:"+y[i]+"  "+df.format((float)(y[i]/1000)));
//        	AppUtil.log( "start_y:"+start_y+" next_y:"+next_y);
        	canvas.drawLine(start_x, start_y, next_x, next_y, p);
    		if (Math.abs(next_x - start_dx) >= dx_pix && (end_x - next_x) >= dx_pix) {
    			path.reset();
    			path.moveTo(next_x, pixel_baseY);  
    			path.lineTo(next_x, next_y);
    			canvas.drawPath(path, pt_dot);  //����ʱ��������
//    			canvas.drawText(""+x[i], next_x - 5, pixel_baseY + 15, p); //��ʾʱ��̶�
    			drawTextAngle(canvas, ""+x[i], next_x - 5, pixel_baseY + 15, p, 45);
    			start_dx = next_x;
    		} else if (end_x == next_x && start_dx != end_x) {
    			path.reset();
    			path.moveTo(next_x, pixel_baseY);  
    			path.lineTo(next_x, next_y);
    			canvas.drawPath(path, pt_dot);  //����ʱ��������
//    			canvas.drawText(""+x[i], next_x - 5, pixel_baseY + 15, p); //��ʾʱ��̶�
    			drawTextAngle(canvas, ""+x[i], next_x - 5, pixel_baseY + 15, p, 45);
    			start_dx = next_x;
    		}
    		
    		if (Math.abs(next_y - start_dy) >= dy_pix && (next_y - end_y) >= 10) {
//  	      		AppUtil.log( "next_x:"+next_x+" next_y:"+next_y);
    			path.reset();
    			path.moveTo(pixel_baseX, next_y);  
    			path.lineTo(next_x, next_y);
    			canvas.drawPath(path, pt_dot); 
    			canvas.drawText(df.format((float)y[i]/1000), pixel_baseX - 35, //��ʾע�����̶�,��mLת��ΪL
    					next_y + 5, p);
//	        		canvas.drawText((int)y[i]+"", pixel_baseX - 50, //��ʾע�����̶�
//        				next_y + 5, p);
    			start_dy = next_y;
    		} else if (end_y == next_y && start_y != end_y) {
    			path.reset();
    			path.moveTo(pixel_baseX, next_y);  
    			path.lineTo(next_x, next_y);
    			canvas.drawPath(path, pt_dot); 
    			canvas.drawText(df.format((float)y[i]/1000), pixel_baseX - 35, //��ʾע�����̶�,��mLת��ΪL
    					next_y + 5, p);
    		}
        	start_y = next_y;
        	start_x = next_x;
        }
        path.close();
//        if (adc_points != 0)
//        	canvas.drawLine(start_x, start_y, start_x, pixel_baseY, p);
    }
	
    private float getPathStartX(int base, float end, float value, float vmax) {
//    	AppUtil.log( "base:"+base + ", end:"+end+", vmax:"+vmax+", value:"+value);
    	return base + ((end - base)/vmax)*value;
    }
    
    private float getPathStartY(int base, float end, float value, float vmax) {
    	return base - ((base - end)/vmax)*value;
    }
  
    /**
     * �������� ���ڻ�������ļ�ͷ
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
        // ������������
        canvas.drawPath(path, paint);
    }
}
