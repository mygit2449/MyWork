package com.daleelo.Dialog;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogMsg extends AlertDialog.Builder{
	
	Context context;
	public AlertDialogMsg(Context context,String msg) 
	{
		super(context);
		this.setMessage(msg);
		this.setTitle("Twitter");
		this.setCancelable(false);
	}
}
