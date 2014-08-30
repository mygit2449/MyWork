package com.daleelo.Dialog;

import android.app.AlertDialog;
import android.content.Context;

public class NetworkNotFoundDialog extends AlertDialog.Builder{
	
	Context context;
	public NetworkNotFoundDialog(Context context) 
	{
		super(context);
		this.setMessage("Network not found!!");
		this.setTitle("Daleelo");
		this.setCancelable(false);

	}

}
