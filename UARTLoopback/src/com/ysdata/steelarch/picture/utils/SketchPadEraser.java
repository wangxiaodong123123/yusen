package com.ysdata.steelarch.picture.utils;

import com.ysdata.steelarch.picture.interfaces.ISketchPadTool;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;


public class SketchPadEraser implements ISketchPadTool
{
    private static final float TOUCH_TOLERANCE = 0.0f;
    private static final String TAG = "ys200";

    private float m_curX = 0.0f;
    private float m_curY = 0.0f;
    private boolean m_hasDrawn = false;
    private Path m_eraserPath = new Path();
    private Paint m_eraserPaint = new Paint();

    public SketchPadEraser(int eraserSize)
    {
//        m_eraserPaint.setAntiAlias(true);
//        m_eraserPaint.setDither(true);
//        m_eraserPaint.setColor(0xFF000000);
//        m_eraserPaint.setStrokeWidth(eraserSize);
//        m_eraserPaint.setStyle(Paint.Style.STROKE);
//        m_eraserPaint.setStrokeJoin(Paint.Join.ROUND);
//        m_eraserPaint.setStrokeCap(Paint.Cap.SQUARE);
//        m_eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        
    	m_eraserPaint.setAlpha(0);
		// 画笔划过的痕迹就变成透明色了
    	m_eraserPaint.setColor(Color.BLACK); // 此处不能为透明色
    	m_eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
		// 或者
		// mPaint.setAlpha(0);
		// mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

    	m_eraserPaint.setAntiAlias(true);
    	m_eraserPaint.setDither(true);
    	m_eraserPaint.setStyle(Paint.Style.STROKE);
    	m_eraserPaint.setStrokeJoin(Paint.Join.ROUND); // 前圆角
    	m_eraserPaint.setStrokeCap(Paint.Cap.ROUND); // 后圆角
    	m_eraserPaint.setStrokeWidth(eraserSize); // 笔宽
//        m_eraserPaint.setXfermode(null);
    }

    @Override
    public void draw(Canvas canvas)
    {
        if (null != canvas)
        {
            canvas.drawPath(m_eraserPath, m_eraserPaint);
            Log.d(TAG, "eraser draw.");
        }
    }

    @Override
    public boolean hasDraw()
    {
        return m_hasDrawn;
    }

    @Override
    public void cleanAll()
    {
        m_eraserPath.reset();
    }

    @Override
    public void touchDown(float x, float y)
    {
        m_eraserPath.reset();
        m_eraserPath.moveTo(x, y);
        m_curX = x;
        m_curY = y;
    }

    @Override
    public void touchMove(float x, float y)
    {
        float dx = Math.abs(x - m_curX);
        float dy = Math.abs(y - m_curY);
        
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE)
        {
            m_eraserPath.quadTo(m_curX, m_curY, (x + m_curX) / 2, (y + m_curY) / 2);
            
            m_hasDrawn = true;
            m_curX = x;
            m_curY = y;
        }
    }

    @Override
    public void touchUp(float x, float y)
    {
        m_eraserPath.lineTo(x, y);
    }

	@Override
	public Paint getPaint() {
		return m_eraserPaint;
	}

	@Override
	public Path getPath() {
		return m_eraserPath;
	}
}
