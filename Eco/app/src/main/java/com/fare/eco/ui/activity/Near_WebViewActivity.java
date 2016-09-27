package com.fare.eco.ui.activity;

import com.fare.eco.ui.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Near_WebViewActivity extends Activity {
	
	private WebView webView;
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near__web_view);
		
		path = getIntent().getStringExtra("productUrl");
		if (path == null) {
			path = "http://baidu.com";
		}
		webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl(path);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});
		
		webView.getSettings().setBlockNetworkImage(false);
		webView.getSettings().setJavaScriptEnabled(true); // 显示js样式(加载图片必须)
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setAllowFileAccess(true); // 允许访问文件
	}
}
