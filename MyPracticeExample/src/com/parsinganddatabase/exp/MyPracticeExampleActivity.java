package com.parsinganddatabase.exp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MyPracticeExampleActivity extends Activity {
    /** Called when the activity is first created. */
	MyExpParserClass myExpParserClass = null;
	ProgressDialog mProgressDialog = null;
	Async mAsync = null;
	MyExampleModel myExampleModel = null;
	ArrayList<MyExampleModel> totalArrayItems = null;
	MyExpDAO mExpDAO = null;
	NewsAdapter mNewsAdapter = null;
	
	ListView mListview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mProgressDialog = ProgressDialog.show(MyPracticeExampleActivity.this, null, "Please wait..");
        mListview = (ListView)findViewById(R.id.list_view);

        totalArrayItems = new ArrayList<MyExampleModel>();
        
        mAsync = new Async("http://newsrss.bbc.co.uk/rss/sportonline_world_edition/front_page/rss.xml", new MyHandler());
        mAsync.start();

        myExampleModel = new MyExampleModel();
        
        mExpDAO = new MyExpDAO(getApplicationContext());
        totalArrayItems = mExpDAO.getDetails();
        Log.v(getClass().getSimpleName(), " array size "+totalArrayItems.size());
        
        mNewsAdapter = new NewsAdapter(MyPracticeExampleActivity.this, totalArrayItems);
        mListview.setAdapter(mNewsAdapter);
        
    }
    
    public class NewsAdapter extends ArrayAdapter<MyExampleModel>{

    	ArrayList<MyExampleModel> feed;
    	Context mContext;
    	
    	public NewsAdapter(Context context, ArrayList<MyExampleModel> feed) {
    		super(context, R.layout.items_list_row, feed);
    		// TODO Auto-generated constructor stub
    		this.mContext = context;
    		this.feed = feed;
    		
    		Log.v("Adapter Class"," "+feed.size());
    	}

    	@Override
    	public int getCount() {
    		// TODO Auto-generated method stub
    		return feed.size();
    	}

    	@Override
    	public MyExampleModel getItem(int position) {
    		// TODO Auto-generated method stub
    		return feed.get(position);
    	}

    	@Override
    	public long getItemId(int position) {
    		// TODO Auto-generated method stub
    		return 0;
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		// TODO Auto-generated method stub
    		
    		if (convertView == null) {
    			
    			LayoutInflater inflate = LayoutInflater.from(mContext);
    			convertView = inflate.inflate(R.layout.items_list_row, null);
    		}
    		
    		TextView title = (TextView)convertView.findViewById(R.id.title);
    		TextView description = (TextView)convertView.findViewById(R.id.description);
    		TextView pubdate = (TextView)convertView.findViewById(R.id.pubdate);
    		
    		title.setText(feed.get(position).getTitle());
    		description.setText(feed.get(position).getDescription());
    		pubdate.setText(feed.get(position).getPubdate());
    		
    		return convertView;
    	}
    	
    }
    public class Async extends Thread{

    	String url;
    	Handler handler;
    	
    	public Async(String url, Handler handler){
    		this.url = "http://newsrss.bbc.co.uk/rss/sportonline_world_edition/front_page/rss.xml";
    		myExpParserClass = new MyExpParserClass(url, handler, MyPracticeExampleActivity.this);
    		Log.e("Check Db ", "requestedURl"+url);
    	}
    	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try 
			{
				myExpParserClass.parse();
			} catch (Exception e) {
				// TODO: handle exception
			}
			super.run();
		}
    	
    }
    
    
    public class MyHandler extends Handler{
    	
    	public void handleMessage(android.os.Message message){
    		
    		if (message.what == 0) {
				mProgressDialog.dismiss();
    			
    			
    			
			}else if (message.what == 1) {
				mProgressDialog.dismiss();

				totalArrayItems = mExpDAO.getDetails();
				Log.v(getClass().getSimpleName(), " news size1111111"+totalArrayItems.size());
				setAdapter(totalArrayItems);
			}
    	}
    }
    
    public void setAdapter(ArrayList<MyExampleModel> totalArrayItems){

    	Log.v(getClass().getSimpleName(), " inside method "+totalArrayItems.size());
    	mNewsAdapter = new NewsAdapter(MyPracticeExampleActivity.this, totalArrayItems);
    	mListview.setAdapter(mNewsAdapter);
    
    }
    
    
}