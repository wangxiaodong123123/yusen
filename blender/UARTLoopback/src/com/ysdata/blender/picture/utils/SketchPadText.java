package com.ysdata.blender.picture.utils;

import com.ysdata.blender.picture.interfaces.ISketchPadTool;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;


public class SketchPadText implements ISketchPadTool
{
    private float m_curX = 0.0f;
    private float m_curY = 0.0f;
    private boolean m_hasDrawn = false;
    private Paint m_textPaint = new Paint();
    private Path m_textPath = new Path();
    public static String words_text = "";

    public SketchPadText(int textSize, int textColor)
    {
    	m_textPaint.setColor(textColor);
    	m_textPaint.setTextSize(textSize);
    }

    @Override
    public void draw(Canvas canvas)
    {
        if (null != canvas)
        {
        	canvas.drawText(words_text, m_curX, m_curY, m_textPaint);
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
    	
    }

    @Override
    public void touchDown(float x, float y)
    {
        m_curX = x;
        m_curY = y;
        m_hasDrawn = true;
        Log.d("ys200", "sketchPadText===touchDown");
    }

    @Override
    public void touchMove(float x, float y)
    {

    }

    @Override
    public void touchUp(float x, float y)
    {

    }

	@Override
	public Paint getPaint() {
		return m_textPaint;
	}

	@Override
	public Path getPath() {
		return m_textPath;
	}
}
