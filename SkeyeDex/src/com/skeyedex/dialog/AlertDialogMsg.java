package com.skeyedex.dialog;

import com.skeyedex.R;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogMsg extends AlertDialog.Builder{
	
	Context context;
	public AlertDialogMsg(Context context, String title, String msg) 
	{
		
		super(context);
		this.setMessage(msg);
		this.setTitle(title);
		this.setIcon(R.drawable.login_alert);
		this.setCancelable(false);
          
	}

}
