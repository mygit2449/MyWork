package com.daleelo.Dialog;

import android.app.AlertDialog;
import android.content.Context;

public class DataFoundAlertDialog extends AlertDialog.Builder{
	
	Context context;
	public DataFoundAlertDialog(Context context) 
	{
		super(context);
		this.setMessage("Data available!!");
		this.setTitle("Daleelo");
	}

}
