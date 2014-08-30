package com.copperlabs.newxml;
import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class MyNewXmlActivity extends ListActivity{
	private static String TAG=MyNewXmlActivity.class.getSimpleName();
	private ProgressDialog progressdialog;
	AsyncNewsFeedParser mAsyncNewsFeedParser = null;
	private ArrayList<NewXmlModel> resultantItems = null, resulantItemsDump = null;
	private DataParsing mDataParsing = null;
	NewXmlModel myModel = null;
	private NewXmlAdapter mNewXmlAdapter = null, mNewXmlAdapterDump = null;
	 private Context mContext;
	 String url;
	 EditText filter_edit;
	 boolean main_list = true;
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.main);
        
        filter_edit = (EditText)findViewById(R.id.filter);
        filter_edit.setVisibility(View.VISIBLE);
        progressdialog = ProgressDialog.show(MyNewXmlActivity.this, "","Please wait...", true);
        resultantItems = new ArrayList<NewXmlModel>();
        resulantItemsDump = new ArrayList<NewXmlModel>();
        mNewXmlAdapter = new NewXmlAdapter(this, resultantItems);
        Log.v(TAG, "Array Size "+resultantItems.size());
        mNewXmlAdapterDump = new NewXmlAdapter(this, resulantItemsDump);
        setListAdapter(mNewXmlAdapter);
        
        mAsyncNewsFeedParser = new AsyncNewsFeedParser("http://newsrss.bbc.co.uk/rss/sportonline_world_edition/front_page/rss.xml",new FeedParserHandler());
        mAsyncNewsFeedParser.start();
        myModel = new NewXmlModel();
       if(resultantItems!=null)
        {	   
 	       for (int i = 0; i < resultantItems.size(); i++)
 	       {
 			Log.v("data",""+resultantItems.get(i).title);
 		   } 
        }    
        
       
       filter_edit.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			resulantItemsDump.clear();
			mNewXmlAdapterDump.clear();
			int text_length = filter_edit.getText().length();
			if (text_length == 0) {
				main_list = true;
				setListAdapter(mNewXmlAdapter);
			}else {
				main_list = false;
				for (int i = 0; i < resultantItems.size(); i++) {
					String name = ((String)resultantItems.get(i).getTitle().toString().trim().toLowerCase());
					int search_item = name.indexOf(filter_edit.getText().toString().trim().toLowerCase());
					if (search_item == 0) {
						resulantItemsDump.add(resultantItems.get(i));
					}
				}
				setListAdapter(mNewXmlAdapterDump);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	});
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
    	super.onListItemClick(l, v, position, id);
    	myModel = resultantItems.get(position);
    	startActivity(new Intent(mContext,DetailDescription.class).putExtra("feed", myModel));
    }
    
    
    class AsyncNewsFeedParser extends Thread
	{
		String url;
		Handler handler;
		public AsyncNewsFeedParser(String url,Handler handler) 
		{
			this.url="http://newsrss.bbc.co.uk/rss/sportonline_world_edition/front_page/rss.xml";
			Log.e(TAG, "requestedURl"+url);
			mDataParsing= new DataParsing(url,handler);
		}
		@Override
		public void run() {
			try
			{
				mDataParsing.parse();
			}catch(Exception e){
				e.printStackTrace();
			}
			super.run();
		}
		
	}
	
	class FeedParserHandler extends Handler
	{
		
		public void handleMessage(android.os.Message msg)
		{
			progressdialog.dismiss();

			if(msg.what==0)
			{
				myModel=(NewXmlModel) msg.getData().getSerializable("Datafeed");
				mNewXmlAdapter.add(myModel);
				Log.d(TAG, "IN"+myModel);
			}
			
			Log.e(TAG, "IN THE HANDLER");
		}
	}
}