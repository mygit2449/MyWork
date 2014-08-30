package com.daleelo.Masjid.Helper;

import android.app.AlertDialog;
import android.content.Context;


public class AlertDialogMessage extends AlertDialog.Builder{
	Context context;
	public AlertDialogMessage(Context context,String title,String msg) {
		super(context);
		this.setMessage(msg);
		this.setTitle(title);
		this.setCancelable(false);

	}
	
}
