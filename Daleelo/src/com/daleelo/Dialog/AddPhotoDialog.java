package com.daleelo.Dialog;

import android.app.AlertDialog;
import android.content.Context;

public class AddPhotoDialog extends AlertDialog.Builder{
	
	Context context;
	public AddPhotoDialog(Context context) 
	{
		super(context);
		this.setMessage("Photo");
		this.setTitle("ADD PHOTO");
		
	}
}
