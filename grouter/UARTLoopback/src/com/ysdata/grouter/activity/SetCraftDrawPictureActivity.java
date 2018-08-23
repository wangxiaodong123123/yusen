package com.ysdata.grouter.activity;

import java.io.File;

import com.ysdata.grouter.R;
import com.ysdata.grouter.cloud.util.ConstDef;
import com.ysdata.grouter.manager.TempData;
import com.ysdata.grouter.picture.utils.BitmapUtil;
import com.ysdata.grouter.storage.ExtSdCheck;
import com.ysdata.grouter.uart.MyActivityManager;
import com.ysdata.grouter.view.DrawPictureView;
import com.ysdata.grouter.wireless.client.Format;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SetCraftDrawPictureActivity extends Activity  implements View.OnClickListener{
	private static final  String TAG = "ys200";
	Context context;
	Button m_importBtn;
	Button m_penBtn;
	Button m_eraserBtn;
	Button m_wordsBtn;
	Button m_lineBtn;
	Button m_clearBtn;
	Button m_saveBtn;
	Button m_redoBtn;
	Button m_undoBtn;
	Handler handler;
	int pressedBtn = 0;
	private DrawPictureView m_sketchPad = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_craft_draw_picture);
		context = this;
		MyActivityManager.addActivity(SetCraftDrawPictureActivity.this);
		initView();
		m_importBtn.setOnClickListener(this);
        m_penBtn.setOnClickListener(this);
        m_eraserBtn.setOnClickListener(this);
        m_wordsBtn.setOnClickListener(this);
        m_lineBtn.setOnClickListener(this);
        m_clearBtn.setOnClickListener(this);
        m_saveBtn.setOnClickListener(this);
        m_redoBtn.setOnClickListener(this);
        m_undoBtn.setOnClickListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Format.ACTION_LOAD_IMAGE_DEMO);
        registerReceiver(LoadImageReceiver, filter);
        m_sketchPad.setStrokeFilePath(getIntent().getStringExtra("image_path"));
	}
	
	private void initView() {
		m_sketchPad = (DrawPictureView) findViewById(R.id.sketchpad);
		m_importBtn = (Button) findViewById(R.id.craft_import);
		m_eraserBtn = (Button) findViewById(R.id.craft_erase);
		m_penBtn = (Button) findViewById(R.id.craft_pen);
		m_wordsBtn = (Button) findViewById(R.id.craft_words);
		m_lineBtn = (Button) findViewById(R.id.craft_line);
		m_clearBtn = (Button) findViewById(R.id.craft_clear);
		m_saveBtn = (Button) findViewById(R.id.craft_save);
		m_redoBtn = (Button) findViewById(R.id.craft_redo);
		m_undoBtn = (Button) findViewById(R.id.craft_undo);
		m_penBtn.setEnabled(false);
		pressedBtn = 1;
	}

	 private BroadcastReceiver LoadImageReceiver = new BroadcastReceiver() {
    	public void onReceive(Context context, Intent intent) {
    		String action = intent.getAction();
    		if (action.equals(Format.ACTION_LOAD_IMAGE_DEMO)) {
    			String image_path = intent.getStringExtra("image_path");
    			Log.d(TAG, "image_path:"+image_path);
    			Bitmap bmp = BitmapFactory.decodeFile(image_path);
    			if (bmp != null) {
    				int src_width = bmp.getWidth();  //缩放
				    int src_height = bmp.getHeight();
				    int newWidth = m_sketchPad.getWidth();
				    int newHeight = m_sketchPad.getHeight();
				    Log.d(TAG, "src_width:"+src_width+" src_height:"+src_height+
				    		" newWidth:"+newWidth+" newHeight:"+newHeight);
				    if (newWidth != 0 && newHeight != 0) {
				    	float scaleWidth = ((float) newWidth) / src_width;
				    	float scaleHeight = ((float) newHeight) / src_height;
				    	Matrix matrix = new Matrix();
				    	matrix.postScale(scaleWidth, scaleHeight);
				    	bmp = Bitmap.createBitmap(bmp, 0, 0, src_width, src_height, matrix,true);
				    	m_sketchPad.drawBitmap(bmp);
				    } 
    			}
    		} 
    	}
	 };	
	
	@Override
	public void onClick(View v) {
        switch(v.getId())
        {
        case R.id.craft_import:
        	onImportClick(v);
        	break;
        	
        case R.id.craft_pen:
            onPenClick(v);
            pressedBtn = 1;
            break;
            
        case R.id.craft_erase:
            onEraseClick(v);
            pressedBtn = 2;
            break;
            
        case R.id.craft_words:
            onTextClick(v);
            pressedBtn = 3;
            break;
            
        case R.id.craft_line:
            onLineClick(v);
            pressedBtn = 4;
            break;     
            
        case R.id.craft_clear:
            onClearClick(v);
            break;  
            
        case R.id.craft_save:
            onSaveClick(v);
            break;
            
        case R.id.craft_redo:
        	onRedoClick(v);
        	break;
        	
        case R.id.craft_undo:
        	onUndoClick(v);
        	break;
        }
    }
	
	private void releaseBtn() {
		if (pressedBtn == 1) {
			m_penBtn.setEnabled(true);
		} else if (pressedBtn == 2) {
			m_eraserBtn.setEnabled(true);
		} else if (pressedBtn == 3) {
			m_wordsBtn.setEnabled(true);
		}  else if (pressedBtn == 4) {
			m_lineBtn.setEnabled(true);
		}
	}
	
	public boolean imageExist(String filePath) {  
		boolean ret = false;
        try{  
            File f = new File(filePath);  
            File[] files = f.listFiles();// 列出所有文件  
          
            // 将所有文件存入list中  
            if(files != null){  
                int count = files.length;// 文件个数  
                for (int i = 0; i < count; i++) {  
                    File file = files[i]; 
                    String filename  = file.getName();
                    if(filename.endsWith("jpg") || filename.endsWith("gif") || filename.endsWith("jpeg") ||
                    		filename.endsWith("bmp") || filename.endsWith("png") || filename.endsWith("JPG") || 
                    		filename.endsWith("BMP") || filename.endsWith("PNG") || filename.endsWith("GIF") || 
                    		filename.endsWith("JPEG"))
                    {
                    	ret = true;
                        break;
                    }
                } 
            }
        } catch(Exception ex){  
            ex.printStackTrace();  
        }  
        return ret;
    }
	
	protected void onImportClick(View v)
	{
		String sdcarddir = ExtSdCheck.getExtSDCardPath();
		if (sdcarddir != null) {
			String image_path =  sdcarddir+"/"+ConstDef.PROJECT_NAME+"/draw/image_demo/";
			File path_dir = new File(image_path);
			if (!path_dir.exists()) {
				path_dir.mkdirs();
			}
			if (imageExist(image_path)) {
				Intent intent = new Intent(SetCraftDrawPictureActivity.this, DrawImageGridViewActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(context, "在外部sd卡的"+ConstDef.PROJECT_NAME+"/draw/image_demo/目录下未检测到示例图片。", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, "未检测到外部sd卡", Toast.LENGTH_SHORT).show();
		}
	}
	
    protected void onPenClick(View v)
    {
        m_sketchPad.setStrokeType(DrawPictureView.STROKE_PEN);
        m_penBtn.setEnabled(false);
        releaseBtn();
//        m_penBtn.setEnabled(false);
//        m_eraserBtn.setEnabled(true);
    }
    
    protected void onEraseClick(View v)
    {
        m_sketchPad.setStrokeType(DrawPictureView.STROKE_ERASER);
        m_eraserBtn.setEnabled(false);
        releaseBtn();
//        m_penBtn.setEnabled(true);
//        m_eraserBtn.setEnabled(false);
    }
    
    protected void onTextClick(View v)
    {
        m_sketchPad.setStrokeType(DrawPictureView.STROKE_TEXT);
        m_wordsBtn.setEnabled(false);
        releaseBtn();
//        m_penBtn.setEnabled(true);
//        m_eraserBtn.setEnabled(false);
    }
    
    protected void onClearClick(View v)
    {
    	AlertDialog.Builder dialog = new  AlertDialog.Builder(context);    
		dialog.setTitle("提示" );
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setMessage("确定要清除吗?" );  
		dialog.setNegativeButton("取消", null);
		dialog.setPositiveButton("确定",  new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				m_sketchPad.clearAllStrokes();
			}
		} );   
		dialog.create();
		dialog.show();
//        m_clearBtn.setEnabled(false);
//        m_redoBtn.setEnabled(false);
//        m_undoBtn.setEnabled(false);
    }
    
    protected void onLineClick(View v)
    {
        m_sketchPad.setStrokeType(DrawPictureView.STROKE_LINE);
        m_lineBtn.setEnabled(false);
        releaseBtn();
//        m_undoBtn.setEnabled(m_sketchPad.canUndo());
//        m_redoBtn.setEnabled(m_sketchPad.canRedo());
    }
    
    protected void onRedoClick(View v) {
    	m_sketchPad.redo();
    }
    
    protected void onUndoClick(View v) {
    	m_sketchPad.undo();
    }
    
    protected void onSaveClick(View v)
    {
    	AlertDialog.Builder dialog = new  AlertDialog.Builder(context);    
		dialog.setTitle("提示" );
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		dialog.setMessage("确定要保存吗?" );  
		dialog.setNegativeButton("取消", null);
		dialog.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				String extSdPath = ExtSdCheck.getExtSDCardPath();
				if (extSdPath != null) {
					String strFilePath = getStrokeFilePath(extSdPath);
					Log.d(TAG, "picture path:"+strFilePath);
					Bitmap bmp = m_sketchPad.getCanvasSnapshot();
					
					if (null != bmp)
					{
						int width = bmp.getWidth();  //缩放
						int height = bmp.getHeight();
						int newWidth = 536;
						int newHeight = 360;
						float scaleWidth = ((float) newWidth) / width;
						float scaleHeight = ((float) newHeight) / height;
						Matrix matrix = new Matrix();
						matrix.postScale(scaleWidth, scaleHeight);
						Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix,true);
						BitmapUtil.saveBitmapToSDCard(newbm, strFilePath);
						BitmapUtil.saveBitmapToSDCard(bmp, getNormalPicturePath(extSdPath));
					}
					context.sendBroadcast(new Intent(Format.ACTION_UPDATE_PICTURE));
					finish();
				} else {
					Toast.makeText(context, "未检测到外部sd卡,无法保存图片", Toast.LENGTH_SHORT).show();
				}
			}
		} );   
		dialog.create();
		dialog.show();
    }
    
    @Override
    protected void onDestroy() {
    	context.unregisterReceiver(LoadImageReceiver);
    	MyActivityManager.removeActivity(SetCraftDrawPictureActivity.this);
    	super.onDestroy();
    }
    
    private String getStrokeFilePath(String extSdPath) {
    	TempData temp = TempData.getSingleQueryTempData();
    	int proj_val = temp.getDbProjectId();
    	int eng_val = temp.getDbSubProjectId();
    	int mileage_number = temp.getMileageNumber();
		File path = new File(extSdPath+ConstDef.DATABASE_PATH+proj_val+"/"+eng_val+"/picture/");
		if (!path.exists()) {
			path.mkdirs();
		}
		return extSdPath+ConstDef.DATABASE_PATH+proj_val+"/"+eng_val+"/picture/"+"picture_"+mileage_number+".png";
    }
    
    private String getNormalPicturePath (String extSdPath) {
    	TempData temp = TempData.getSingleQueryTempData();
    	int proj_val = temp.getDbProjectId();
    	int eng_val = temp.getDbSubProjectId();
    	int mileage_number = temp.getMileageNumber();
		File path = new File(extSdPath+ConstDef.DATABASE_PATH+proj_val+"/"+eng_val+"/picture_normal/");
		if (!path.exists()) {
			path.mkdirs();
		}
		return extSdPath+ConstDef.DATABASE_PATH+proj_val+"/"+eng_val+"/picture_normal/"+"picture_"+mileage_number+".png";
    }
    
/*    private String getStrokeFilePath()
    {
        File sdcarddir = android.os.Environment.getExternalStorageDirectory();
        String strDir = sdcarddir.getPath() + "/DCIM/sketchpad/";
        String strFileName = getStrokeFileName();
        File file = new File(strDir);
        if (!file.exists())
        {
            file.mkdirs();
        }
        
        String strFilePath = strDir + strFileName;
        
        return strFilePath;
    }
    
    private String getStrokeFileName()
    {
        String strFileName = "";
        
        Calendar rightNow = Calendar.getInstance();
        int year = rightNow.get(Calendar.YEAR);
        int month = rightNow.get(Calendar.MONDAY);
        int date = rightNow.get(Calendar.DATE);
        int hour = rightNow.get(Calendar.HOUR);
        int minute = rightNow.get(Calendar.MINUTE);
        int second = rightNow.get(Calendar.SECOND);
        
        strFileName = String.format("%02d%02d%02d%02d%02d%02d.png", year, month, date, hour, minute, second);
        return strFileName;
    }*/
}
