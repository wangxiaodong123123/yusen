package com.ysdata.blender.picture.interfaces;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public interface ISketchPadTool
{
    public void draw(Canvas canvas);
    public boolean hasDraw();
    public void cleanAll();
    public void touchDown(float x, float y);
    public void touchMove(float x, float y);
    public void touchUp(float x, float y);
    public Paint getPaint();
    public Path getPath();
}
