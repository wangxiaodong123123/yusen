package com.ysdata.grouter.picture.utils;

import com.ysdata.grouter.picture.interfaces.ISketchPadTool;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;


public class SketchPadLine implements ISketchPadTool
{
    private static final float TOUCH_TOLERANCE = 4.0f;

    private float downX = 0.0f;
    private float downY = 0.0f;
    private float moveX = 0.0f;
    private float moveY = 0.0f;
    private float upX = 0.0f;
    private float upY = 0.0f;
    private boolean m_hasDrawn = false;
    private Path m_linePath = new Path();
    private Paint m_linePaint = new Paint();
    
    public SketchPadLine(int lineSize, int lineColor)
    {
    	m_linePaint.setAntiAlias(true);
    	m_linePaint.setDither(true);
    	m_linePaint.setColor(lineColor);
    	m_linePaint.setStrokeWidth(lineSize);
    	m_linePaint.setStyle(Paint.Style.STROKE);
    }
    
    @Override
    public void draw(Canvas canvas)
    {
        if (null != canvas)
        {
            canvas.drawLine(downX, downY, moveX, moveY, m_linePaint);
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
    	m_linePath.reset();
    }

    @Override
    public void touchDown(float x, float y)
    {
    	m_linePath.moveTo(x, y);
    	downX = x;
    	downY = y;
    }

    @Override
    public void touchMove(float x, float y)
    {
        moveX = x;
        moveY = y;
        m_hasDrawn = true;
    }

    @Override
    public void touchUp(float x, float y)
    {
    	moveX = x;
    	moveY = y;
    }

	@Override
	public Paint getPaint() {
		return m_linePaint;
	}

	@Override
	public Path getPath() {
		return m_linePath;
	}
}
