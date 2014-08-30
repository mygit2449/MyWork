package com.copperlabs.newxml;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailDescription extends Activity{
	
	TextView nametxt;
	TextView descriptiontxt;
	ImageView myImage;
	private Intent reciverIntent=null;
	private NewXmlModel myModel = null;
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
            setContentView(R.layout.single_item);
            reciverIntent = getIntent();
            myModel = (NewXmlModel) reciverIntent.getExtras().getSerializable("feed");
            nametxt = (TextView)findViewById(R.id.title_text);
 	        descriptiontxt = (TextView)findViewById(R.id.description_text);
 	        myImage = (ImageView)findViewById(R.id.image);
 	        
 	       nametxt.setText(myModel.getTitle());
	       descriptiontxt.setText(myModel.getDescription());
	       try{
				
	    	       String imgURL=myModel.getThumbnail();
					new ImageDownloader().download(imgURL, myImage);
					
				}catch(Exception e){
					e.printStackTrace();
				}
	 }
}
