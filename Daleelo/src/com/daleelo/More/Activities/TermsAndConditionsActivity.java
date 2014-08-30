package com.daleelo.More.Activities;

import com.daleelo.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class TermsAndConditionsActivity extends Activity{

	private WebView mTermsAndConditions_Web;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.termsandconditions);
		mTermsAndConditions_Web = (WebView)findViewById(R.id.termsandconditions_Webview);

		mTermsAndConditions_Web.loadUrl("file:///android_asset/DaleeloPrivacyPolicy.html");


	}

	
}
