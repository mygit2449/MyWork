package com.daleelo.More.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.daleelo.R;

public class FrequentlyAskedQuestions extends Activity{

	private ExpandableListView mFaqsListView;

	
	
	private final String[] mQuestions = {"How do I use My Stuff?",
			 "How do I change my Location and Set Radius?",
			 "What are Spotlights and Deals?",
			 "How do I Filter lists?",
			 "What is the difference between green dots or bars and dark gray dots or bars on the Events Calendar?",
			 "How do I use the Hasanat section?",
			 "How do I Advertise my business in Daleelo?"};
	
	private ImageView mOpenList_Img, mClose_Img;
	

	
	
	private final String[][] mAnswers = { {"Daleelo’s My Stuff allows you to browse saved Businesses listings, Spotlights, Deals, Events, Classifieds posts, Hasanat listings, and Masajid listings for later reference. To do so, simply visit the page you would like to save and hit the Save button. That’s it! \n\n When you want to revisit one of your saved pages, just tap the My Stuff button on Daleelo’s homepage or the My Stuff icon at the bottom of each page (next to the Home icon). \n\n Want to delete saved listings from your My Stuff page? Tap the Edit button and then the red delete button that appears next to each listing. Once you’re finished deleting, tap the Done button."},
			  {"Changing your location on Daleelo is as simple as tapping the location text at the top of each page. You will be able to choose any US location and view all of its corresponding Business listings, Spotlights, Deals, Events, Classifieds posts, Hasanat listings, and Masajid listings. \n\nDaleelo also allows you to change your mile radius for the entire application from the get-go. Just tap the More button at the bottom of the Daleelo homepage and select Set Radius. Choose your desired mile radius and you’re good to go! Now every page you browse will display listings according to the mile radius you chose. You may still change your mile radius individually on each page by going to the category’s Filter page."},
			  {"The Daleelo Team knows that today’s shopper wants to know about all the deals in their area. So, we’ve created the Daleelo Spotlights and Deals sections. Spotlights are a way for businesses to let customers know what new and exciting arrivals they may have. Deals are discounts and/or specials businesses may be holding. Both help the Daleelo user be aware of the best deals in their community."},
			  {"When browsing through Daleelo, we want to help you find exactly what you are looking for. So, we added Filter settings to every appropriate page. \n\nJust look for the Filter button or the Settings icon at the top right of each page.Filter settings will allow you to sort listings by Name or Distance, change your mile radius using the Distance bar, view businesses with Deals only, search by Business Name or even Business Owner, and categorize your search even further through a Category dropdown menu when appropriate."},
			  {"Events are displayed easily on our Calendars with dots in the month view and bars in the week and day views. We have sorted our events into two categories: Regular Events and Fundraising Events. A dark gray dot or bar symbolizes a regular event, and a green dot or bar symbolizes a fundraising event."},
			  {"Daleelo’s Hasanat section was created to provide a way for our users to give their Zakat and Sadaqa donations easily and effortlessly. It has been separated into four sections: Zakat, Sadaqa, Fundraising Calendar, and Organizations. \n\nZakat will include specific organizations that our users can pay their Zakat through. Please visit our Zakat section to learn more about Zakat and read through our Zakat FAQ.\n\n Sadaqa will have a number of listings that range from organizations to youth centers to community projects. These are causes, not necessarily organizations. \n\nSo, we added an Organizations section as well so that Daleelo users can view all organizations they can donate to. \n\n Fundraising Calendar is essentially the Daleelo events calendar, but it only displays Fundraising events that our users may donate through the event page at the event and even before."},
			  {"Visit our Advertise page by tapping the Advertise button at the bottom of the Daleelo homepage and fill out your Business User Information and Payment Information. Daleelo will contact you shortly after submitting with more details."}};
	
	
	private FrequentlyAskedQuestionsAdapter mAskedQuestionsAdapter = null;
	
	DisplayMetrics metrics;
	int width, baby_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faqs);
		mFaqsListView = (ExpandableListView)findViewById(R.id.faqs_ExpandableListview);
		
		mAskedQuestionsAdapter = new FrequentlyAskedQuestionsAdapter(FrequentlyAskedQuestions.this, mQuestions, mAnswers);
		mFaqsListView.setAdapter(mAskedQuestionsAdapter);
		
		mFaqsListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				int len = mAskedQuestionsAdapter.getGroupCount();

		        for(int i=0; i<len; i++) {
		            if(i != groupPosition) {
		            	mFaqsListView.collapseGroup(i);
		            }
		        }

			}
		});
	}
	public int GetDipsFromPixel(float pixels)
    {
     // Get the screen's density scale
     final float scale = getResources().getDisplayMetrics().density;
     // Convert the dps to pixels, based on density scale
     return (int) (pixels * scale + 0.5f);
    }
	
	public class FrequentlyAskedQuestionsAdapter extends BaseExpandableListAdapter{

		private String[] mGroupItem;
		private String[][] mChildItems;
		private Context mContext;
		
		public FrequentlyAskedQuestionsAdapter(Context context,  String[] mGroupItem, String[][] mChildItems){
			this.mContext = context;
			this.mGroupItem = mGroupItem;
			this.mChildItems = mChildItems;
		}
		
		@Override
        public boolean areAllItemsEnabled()
        {
            return true;
        }
		
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return  mChildItems[groupPosition][childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return mChildItems[groupPosition].length;
		}
		
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			
			
			if (convertView == null) {
			    LayoutInflater inflater =  (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    convertView = inflater.inflate(R.layout.faqs_child_layout, null);
			   }
			
			TextView mAnswer_Txt = (TextView)convertView.findViewById(R.id.faqs_TXT_answer);
			
			mAnswer_Txt.setText(mChildItems[groupPosition][childPosition]);
			
			return convertView;
		}

		

		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return mGroupItem[groupPosition];
		}

		public int getGroupCount() {
			// TODO Auto-generated method stub
			return mGroupItem.length;
		}

		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.faqs_group_layout, null);
	        }

			ImageView mOpenList_Img = (ImageView)convertView.findViewById(R.id.faqs_IMG_indiactor);
			//ImageView mCloseList_Img = (ImageView)convertView.findViewById(R.id.faqs_IMG_indiactorclose);
			mOpenList_Img.setImageResource(isExpanded?R.drawable.faq_orangebtn:R.drawable.faq_bluebtn);
			TextView mQuestion_Txt = (TextView)convertView.findViewById(R.id.faqs_TXT_title);
			
			mQuestion_Txt.setText(""+mGroupItem[groupPosition]);
			return convertView;
		}

		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
	
}
