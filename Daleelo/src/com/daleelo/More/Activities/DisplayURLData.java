package com.daleelo.More.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.daleelo.R;

public class DisplayURLData extends Activity{
	
	private WebView mWebView;
	
	private Intent receiverIntent;
	
	private String url, mTitle_Str;
	
	private TextView mTitle_Txt;
	
	
	public void onCreate(Bundle savedInstanceState){
		
	super.onCreate(savedInstanceState);
	setContentView(R.layout.more_display_url);
	
	receiverIntent = getIntent();
	
	mTitle_Str = receiverIntent.getExtras().getString("title");
	url = receiverIntent.getExtras().getString("fileUrl");
	mWebView = (WebView)findViewById(R.id.more_display_url_Webview);
	mWebView.loadUrl(url);
	
	mTitle_Txt = (TextView)findViewById(R.id.more_display_url_TXT_title);
	
	mTitle_Txt.setText(mTitle_Str);
	
	}

}
