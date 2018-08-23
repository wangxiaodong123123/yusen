package com.ysdata.grouter.activity;

import com.ysdata.grouter.R;
import com.ysdata.grouter.cloud.util.BaseUrl;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewActivity extends Activity{
	
	WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_ly);
		
		webView = (WebView) findViewById(R.id.webview_id);
		WebSettings s = webView.getSettings();    
		s.setBuiltInZoomControls(true);        
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);         
		s.setUseWideViewPort(true);        
		s.setLoadWithOverviewMode(true);       
		s.setSavePassword(true);        
		s.setSaveFormData(true);        
		s.setJavaScriptEnabled(true);  
		s.setGeolocationEnabled(true);      
		s.setDomStorageEnabled(true);    

		webView.loadUrl(BaseUrl.CRAFT_URL);
	}
}
