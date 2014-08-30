package com.daleelo.Dialog;

import android.app.AlertDialog;
import android.content.Context;

public class ServerErrorDialog extends AlertDialog.Builder{
	
	Context context;
	public ServerErrorDialog(Context context) 
	{
		super(context);
		this.setMessage("Invalied responce from server!! Try again later");
		this.setTitle("Daleelo");
		this.setCancelable(false);

	}

}
