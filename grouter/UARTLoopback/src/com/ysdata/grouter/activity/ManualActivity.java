package com.ysdata.grouter.activity;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnDrawListener;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;
import com.ysdata.grouter.R;
import com.ysdata.grouter.uart.MyActivityManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class ManualActivity  extends Activity implements OnPageChangeListener
{
    private Context context = null;
    private PDFView pdfView;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operate_manual_phone);
        context = this;
        MyActivityManager.addActivity(ManualActivity.this);
        initView();
        pdfView = (PDFView) findViewById(R.id.pdfView);
        pdfView.fromAsset("operating specification.pdf")
        .defaultPage(1)
        .onPageChange(this)
        .enableSwipe(true)   //是否允许翻页，默认是允许翻页
        .showMinimap(false)     //pdf放大的时候，是否在屏幕的右上角生成小地图
        .load();
    }
    
    private void initView() {
    	
    }
    
    public static String readTxtFile(String path)
    {
        String content = ""; //文件内容字符串
            //打开文件
            File file = new File(path);
            //如果path是传递过来的参数，可以做一个非目录的判断
           
            try {
                InputStream instream = new FileInputStream(file); 
                if (instream != null) 
                {
                    InputStreamReader inputreader = new InputStreamReader(instream, "gbk");
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while (( line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }                
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e) 
            {

            } 
            catch (IOException e) 
            {

            }
            return content;
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	MyActivityManager.removeActivity(ManualActivity.this);
    	super.onDestroy();
    }

	@Override
	public void onPageChanged(int page, int pageCount) {
		// TODO Auto-generated method stub
//		Toast.makeText( ManualActivity.this , "page= " + page +
//                " pageCount= " + pageCount , Toast.LENGTH_SHORT).show();
	}
    
    /*public String readWord(String file){
        //创建输入流用来读取doc文件
        FileInputStream in;
        String text = null;
        try {
            in = new FileInputStream(new File(file));
            WordExtractor extractor = null;
            //创建WordExtractor
            extractor = new WordExtractor();
            //进行提取对doc文件
            text = extractor.extractText(in);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }*/
}
