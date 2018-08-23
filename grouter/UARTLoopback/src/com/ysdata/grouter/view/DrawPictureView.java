package com.ysdata.grouter.view;

import java.util.ArrayList;

import com.ysdata.grouter.R;
import com.ysdata.grouter.picture.interfaces.ISketchPadTool;
import com.ysdata.grouter.picture.utils.CommonDef;
import com.ysdata.grouter.picture.utils.SketchPadEraser;
import com.ysdata.grouter.picture.utils.SketchPadLine;
import com.ysdata.grouter.picture.utils.SketchPadPen;
import com.ysdata.grouter.picture.utils.SketchPadText;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;


/**
 * The SketchPadView class provides method to draw strokes on it like as a canvas or sketch pad.
 * We use touch event to draw strokes, when user touch down and touch move, we will remember these
 * point of touch move and set them to a Path object, then draw the Path object, so that user can see the 
 * strokes are drawing real time. When touch up event is occurring, we draw the path to a bitmap which
 * is hold by a canvas, and then draw the bitmap to canvas to display these strokes to user.
 * 
 * @author Li Hong
 * 
 * @date 2010/07/30
 *
 */
public class DrawPictureView extends View
{
    public static final int STROKE_NONE = 0;
    public static final int STROKE_PEN = 1;
    public static final int STROKE_ERASER = 2;
    public static final int STROKE_LINE = 3;
    public static final int STROKE_TEXT = 4;
    public static final int UNDO_SIZE = 20;

    private boolean m_isEnableDraw = true;
    private boolean m_isTouchUp = false;
    private boolean m_isSetForeBmp = false;
    private int m_bkColor = Color.WHITE;
    private static final String TAG = "ys200";

    private int m_strokeType = STROKE_PEN;
    private int m_strokeColor = Color.BLACK;
    private int m_penSize = CommonDef.SMALL_PEN_WIDTH;
    private int m_lineSize = CommonDef.SMALL_PEN_WIDTH;
    private int m_textSize = CommonDef.TEXT_SIZE;
    private int m_eraserSize = CommonDef.SMALL_ERASER_WIDTH;
    private int m_canvasWidth = 100;
    private int m_canvasHeight = 100;
    private boolean m_canClear = true;
    
	private Bitmap   mEraseMaskBitmap = null;
    private Bitmap mImageBitmap = null;
    private Canvas m_canvas = null;
    private ISketchPadTool m_curTool = null;
    private Context mContext = null;
    Paint m_bitmapPaint = new Paint();
    String image_path = "null";
    private boolean init_bmp_flg = false;
    private Bitmap bg_bmp = null;
    
    private SketchPadUndoStack m_undoStack = null;

    public DrawPictureView(Context context)
    {
        this(context, null);
        mContext = context;
    }
    
    public DrawPictureView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        initialize();
    }

    public DrawPictureView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
        initialize();
    }

    public void setDrawStrokeEnable(boolean isEnable)
    {
        m_isEnableDraw = isEnable;
    }
    
    public void setBkColor(int color)
    {
        if (m_bkColor != color)
        {
            m_bkColor = color;
            invalidate();
        }
    }

    public void redo()
    {
        if (null != m_undoStack)
        {
            m_undoStack.redo();
        }
    }

    public void undo()
    {
        if (null != m_undoStack)
        {
            m_undoStack.undo();
        }
    }
    
    public void setStrokeType(int type)
    {
        switch(type)
        {
        case STROKE_PEN:
            m_curTool = new SketchPadPen(m_penSize, m_strokeColor);
            break;
            
        case STROKE_ERASER:
            m_curTool = new SketchPadEraser(m_eraserSize);
            break;
            
        case STROKE_LINE:
        	m_curTool = new SketchPadLine(m_lineSize, m_strokeColor);
        	break;
        	
        case STROKE_TEXT:
        	m_curTool = new SketchPadText(m_textSize, m_strokeColor);
        	break;	
        }

        m_strokeType = type;
    }

    public void setStrokeSize(int size, int type)
    {
        switch(type)
        {
        case STROKE_PEN:
            m_penSize = size;
            break;
            
        case STROKE_ERASER:
            m_eraserSize = size;
            break;
            
        case STROKE_LINE:
        	m_lineSize = size;
        	break;
        	
        case STROKE_TEXT:
        	m_textSize = size;	
        	break;
        }
    }
    
    public void setStrokeColor(int color)
    {
        m_strokeColor = color;
    }

    public int getStrokeSize()
    {
        return m_penSize;
    }

    public int getStrokeColor()
    {
        return m_strokeColor;
    }
    
    public void clearAllStrokes()
    {
        if (m_canClear)
        {
        	// Clear the undo stack.
            m_undoStack.clearAll();
            // Create a new fore bitmap and set to canvas.
            createStrokeBitmap(m_canvasWidth, m_canvasHeight);
            if (mImageBitmap != null) {
            	m_canvas.drawBitmap(mImageBitmap, null, new RectF(0, 0, m_canvasWidth, m_canvasHeight), null);
            }
            invalidate();
            m_canClear = false;
        }
    }

    private void inputWords(final Canvas canvas) {
        LayoutInflater factory = LayoutInflater.from(mContext);
		final View dialogView = factory.inflate(R.layout.input_words_dialog, null);
		AlertDialog.Builder dlg = new AlertDialog.Builder(mContext);
		dlg.setTitle("文字输入");
		dlg.setView(dialogView);
		
		dlg.setPositiveButton("确定", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				EditText words_et = (EditText) dialogView.findViewById(R.id.sketch_words_et);
				String words_text = words_et.getText().toString();
				if (!words_text.equals("")) {
					SketchPadText.words_text=words_text;
					Log.d("ys200", "words_text is not null.");
					m_curTool.draw(canvas);
//					m_isDirty = true;
	                m_canClear = true;
					invalidate();
				} else {
					SketchPadText.words_text="";
					Log.d("ys200", "words_text is null.");
				}
			}
		});
		dlg.setNegativeButton("取消", null);
		dlg.create();
		dlg.show();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
    	Log.d(TAG, "X:"+event.getX()+" Y:"+event.getY());
    	float x = event.getX();
		float y = event.getY();
        if (m_isEnableDraw)
        {
            m_isTouchUp = false;
            switch(event.getAction())
            {
            case MotionEvent.ACTION_DOWN:
                // This will create a new stroke tool instance with specified stroke type.
                // And the created tool will be added to undo stack.
                setStrokeType(m_strokeType);
                Log.d("ys200", "DrawPictureView=====ACTION_DOWN");
                m_curTool.touchDown(x, y);
                if (STROKE_LINE == m_strokeType) {
                	break;
                } else if (STROKE_TEXT == m_strokeType) {
                	inputWords(m_canvas);
                } else {
                	invalidate();
                }
                break;
                
            case MotionEvent.ACTION_MOVE:
            	if (STROKE_TEXT == m_strokeType)
            		break;
                m_curTool.touchMove(x, y);
                // If current stroke type is eraser, draw strokes on bitmap hold by m_canvas.
                if (STROKE_ERASER == m_strokeType)
                {
                    m_curTool.draw(m_canvas);
                }
                
                invalidate();
                m_canClear = true;
                break;
                
            case MotionEvent.ACTION_UP:
            	
            	if (STROKE_TEXT == m_strokeType) {
            		m_undoStack.push(m_curTool);
            		//将一条完整的路径保存下来(相当于入栈操作)   
            		break;
            	}
                m_isTouchUp = true;
                if (m_curTool.hasDraw())
                {
                    // Add to undo stack.
                    m_undoStack.push(m_curTool);
                }
                m_curTool.touchUp(x, y);
//                savePath.add(dp);
                // Draw strokes on bitmap which is hold by m_canvas.
                m_curTool.draw(m_canvas);
                m_canClear = true;
                invalidate();
                break;
            }
        }
        
        // Here must return true if enable to draw, otherwise the stroke may NOT be drawn.
        return true;
    }
    
    protected void setCanvasSize(int width, int height)
    {
        if (width > 0 && height > 0)
        {
            if (m_canvasWidth != width || m_canvasHeight != height)
            {
                m_canvasWidth = width;
                m_canvasHeight = height;

                createStrokeBitmap(m_canvasWidth, m_canvasHeight);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Log.d("ys200", "DrawPictureView=====onDraw");
        super.onDraw(canvas);
        
        if (null != mEraseMaskBitmap) {
        	canvas.drawBitmap(mEraseMaskBitmap, 0, 0, m_bitmapPaint);
        }

        if (null != m_curTool)
        {
            // Do NOT draw current tool stroke real time if stroke type is NOT eraser, because
            // eraser is drawn on bitmap hold by m_canvas.
            if (STROKE_ERASER != m_strokeType && STROKE_TEXT != m_strokeType)
            {
                // We do NOT draw current tool's stroke to canvas when ACTION_UP event is occurring,
                // because the stroke has been drawn to bitmap hold by m_canvas. But the tool will be 
                // drawn if undo or redo operation is performed.
                if (!m_isTouchUp)
                {
                    m_curTool.draw(canvas);
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        
        // If NOT set fore bitmap, call setCanvasSize() method to set canvas size, it will 
        // create a new fore bitmap and set to canvas.
        if (!m_isSetForeBmp)
        {
            setCanvasSize(w, h);
        }
        
        m_canvasWidth = w;
        m_canvasHeight = h;
        
        m_isSetForeBmp = false;
        if (!image_path.equals("null") && !init_bmp_flg) {
        	init_bmp_flg = true;
        	bg_bmp = BitmapFactory.decodeFile(image_path);
        	if (bg_bmp != null) {
        		setBgPicture();
        	}
        }
    }
    
    private void setBgPicture() { 
		int src_width = bg_bmp.getWidth();  //缩放
	    int src_height = bg_bmp.getHeight();
	    Log.d(TAG, "src_width:"+src_width+" src_height:"+src_height+
	    		" m_canvasWidth:"+m_canvasWidth+" m_canvasHeight:"+m_canvasHeight);
	    if (m_canvasWidth != 0 && m_canvasHeight != 0) {
	    	float scaleWidth = ((float) m_canvasWidth) / src_width;
	    	float scaleHeight = ((float) m_canvasHeight) / src_height;
	    	Matrix matrix = new Matrix();
	    	matrix.postScale(scaleWidth, scaleHeight);
	    	bg_bmp = Bitmap.createBitmap(bg_bmp, 0, 0, src_width, src_height, matrix,true);
	    	drawBitmap(bg_bmp);
	    } 
    }
    
    protected void initialize()
    {
        m_canvas = new Canvas();
        m_bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        m_undoStack = new SketchPadUndoStack(this, UNDO_SIZE);
        // Set stroke type and create a stroke tool.
        setStrokeType(STROKE_PEN);
    }

    protected void createStrokeBitmap(int w, int h)
    {
        m_canvasWidth = w;
        m_canvasHeight = h;
        
        mEraseMaskBitmap = Bitmap.createBitmap(m_canvasWidth, m_canvasHeight, Bitmap.Config.ARGB_8888);
        if (null != mEraseMaskBitmap)
        {
        	m_canvas = new Canvas(mEraseMaskBitmap);
            Log.d(TAG, "---createStrokeBitmap---");
        }
    }
    
    public void setStrokeFilePath(String image_path) {
    	this.image_path = image_path;
    }
    
    public void drawBitmap(Bitmap bmp) {
    	Paint paint = new Paint();
    	paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
    	m_canvas.drawPaint(paint);
    	paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
//    	Bitmap bmp = decodeBitmap(image_path);
    	m_canvas.drawBitmap(bmp, null, new RectF(0, 0, m_canvasWidth, m_canvasHeight), null);
    	if (mImageBitmap != null) {
    		mImageBitmap.recycle();
    	}
    	mImageBitmap = bmp;
    	invalidate();
    }
    
    public Bitmap getCanvasSnapshot()
    {
        setDrawingCacheEnabled(true);
        buildDrawingCache(true);
        return getDrawingCache(true);
    }
    
    public class SketchPadUndoStack
    {
        private int m_stackSize = 0;
        private DrawPictureView m_sketchPad = null;
        private ArrayList<ISketchPadTool> m_undoStack = new ArrayList<ISketchPadTool>();
        private ArrayList<ISketchPadTool> m_redoStack = new ArrayList<ISketchPadTool>();
        private ArrayList<ISketchPadTool> m_removedStack = new ArrayList<ISketchPadTool>();
        
        public SketchPadUndoStack(DrawPictureView sketchPad, int stackSize)
        {
            m_sketchPad = sketchPad;
            m_stackSize = stackSize;
        }

        public void push(ISketchPadTool sketchPadTool)
        {
            if (null != sketchPadTool)
            {
                if (m_undoStack.size() == m_stackSize && m_stackSize > 0)
                {
                    ISketchPadTool removedTool = m_undoStack.get(0);
                    m_removedStack.add(removedTool);
                    m_undoStack.remove(0);
                }
                
                m_undoStack.add(sketchPadTool);
            }
        }

        public void clearAll()
        {
            m_redoStack.clear();
            m_undoStack.clear();
            m_removedStack.clear();
        }

        public void undo()
        {
            if (canUndo() && null != m_sketchPad)
            {                
                ISketchPadTool removedTool = m_undoStack.get(m_undoStack.size() - 1);
                m_redoStack.add(removedTool);
                m_undoStack.remove(m_undoStack.size() - 1);

                // Create a new bitmap and set to canvas.
                createStrokeBitmap(m_canvasWidth, m_canvasHeight);
                if (mImageBitmap != null) {
                	m_canvas.drawBitmap(mImageBitmap, null, new RectF(0, 0, m_canvasWidth, m_canvasHeight), null);
                }
                Canvas canvas = m_sketchPad.m_canvas;
                
                // First draw the removed tools from undo stack.
                for (ISketchPadTool sketchPadTool : m_removedStack)
                {
                    sketchPadTool.draw(canvas);
                }
                
                for (ISketchPadTool sketchPadTool : m_undoStack)
                {
                    sketchPadTool.draw(canvas);
                }
                
                m_sketchPad.invalidate();
            }
        }

        public void redo()
        {
            if (canRedo() && null != m_sketchPad)
            {
                ISketchPadTool removedTool = m_redoStack.get(m_redoStack.size() - 1);
                m_undoStack.add(removedTool);
                m_redoStack.remove(m_redoStack.size() - 1);
                
                // Create a new bitmap and set to canvas.
                // Create a new bitmap and set to canvas.
                createStrokeBitmap(m_canvasWidth, m_canvasHeight);
                if (mImageBitmap != null) {
                	m_canvas.drawBitmap(mImageBitmap, null, new RectF(0, 0, m_canvasWidth, m_canvasHeight), null);
                }
                
                Canvas canvas = m_sketchPad.m_canvas;
                
                // First draw the removed tools from undo stack.
                for (ISketchPadTool sketchPadTool : m_removedStack)
                {
                    sketchPadTool.draw(canvas);
                }
                
                for (ISketchPadTool sketchPadTool : m_undoStack)
                {
                    sketchPadTool.draw(canvas);
                }
                
                m_sketchPad.invalidate();
            }
        }

        public boolean canUndo()
        {
            return (m_undoStack.size() > 0);
        }

        public boolean canRedo()
        {
            return (m_redoStack.size() > 0);
        }
    }
}
