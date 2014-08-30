package com.copperlabs.newxml;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewXmlAdapter extends ArrayAdapter<NewXmlModel>{
    
	private ArrayList<NewXmlModel> feed;
	Context context;
	public NewXmlAdapter(Context context, ArrayList<NewXmlModel> feed) 
	{
		super(context, R.layout.items_list,feed);
		this.context=context;
		this.feed=feed;
	}
	@Override
	public NewXmlModel getItem(int position) {
		return feed.get(position);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		      LayoutInflater inflater= LayoutInflater.from(context);
		      convertView=inflater.inflate(R.layout.items_list, null);
			  TextView titletxt = (TextView)convertView.findViewById(R.id.title_text);
			  TextView descriptiontxt = (TextView)convertView.findViewById(R.id.description_text);
			  TextView pubdatetxt = (TextView)convertView.findViewById(R.id.pudate_text);
			  ImageView titleImage = (ImageView)convertView.findViewById(R.id.image);
				
		titletxt.setText(feed.get(position).getTitle());
		descriptiontxt.setText(feed.get(position).getDescription());
		pubdatetxt.setText(feed.get(position).getPubdate());
		try{
			
			if(feed.get(position).getThumbnail()!=null){
				String imgURL=feed.get(position).getThumbnail();
				new ImageDownloader().download(imgURL, titleImage);
				}
				else
					titleImage.setImageResource(R.drawable.ic_launcher);
			}catch(Exception e){
				e.printStackTrace();
			}
			if(position%2!=0)
			{
				convertView.setBackgroundColor(Color.argb(255, 0, 0, 0));
			}else{
				convertView.setBackgroundColor(Color.argb(100, 255, 255, 255));
			}
		return convertView;
	}
 
}
