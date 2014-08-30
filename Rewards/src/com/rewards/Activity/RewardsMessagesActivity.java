package com.rewards.Activity;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class RewardsMessagesActivity extends MenuOptionsActivity {
	private LayoutInflater factory = null;
	private View myMessagesView;
    /** Called when the activity is first created. */
	private ExpandableListView mMessagesListView = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        factory = LayoutInflater.from(this);
        
        myMessagesView = factory.inflate(R.layout.messages, null);
        super.mParentLayout.addView(myMessagesView);
        
        super.mMessages_Ibtn.setBackgroundResource(R.drawable.message_ac);
	    super.mMessages_Ibtn.setClickable(false);
	    
	    
	    mMessagesListView = (ExpandableListView)myMessagesView.findViewById(R.id.messages_list_view);

    }
 
    
    public class RewardsMessagesAdaper extends BaseExpandableListAdapter{

    	private ArrayList<String> mGroupItems = null;
    	private ArrayList<ArrayList<String>> mChildItems = null;
    	private Context mContext;
    	
    	public RewardsMessagesAdaper(Context context, ArrayList<String> groupItems, ArrayList<ArrayList<String>> childItems){
    		this.mContext = context;
    		this.mGroupItems = groupItems;
    		this.mChildItems = childItems;
    	}
    	
    	@Override
        public boolean areAllItemsEnabled()
        {
            return true;
        }
    	
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return mChildItems.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.messages_child, null);
			}
			
			TextView child_details = (TextView)convertView.findViewById(R.id.messages_child_TXT_title);
			TextView address_line1 = (TextView)convertView.findViewById(R.id.messages_child_TXT_address_line1);
			TextView address_line2 = (TextView)convertView.findViewById(R.id.messages_child_TXT_address_line2);
			TextView date = (TextView)convertView.findViewById(R.id.messages_child_TXT_datedetails);
			TextView website = (TextView)convertView.findViewById(R.id.messages_child_TXT_website);
			TextView offer = (TextView)convertView.findViewById(R.id.messages_child_TXT_offer);

			return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return mChildItems.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return mGroupItems.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return mGroupItems.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.messages_group, null);
			}
			
			
			TextView title_txt = (TextView)convertView.findViewById(R.id.messages_group_TXT_title);
			TextView description_txt = (TextView)convertView.findViewById(R.id.messages_group_TXT_description);
			TextView date_txt = (TextView)convertView.findViewById(R.id.messages_group_TXT_date);
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
    	
    }
    
}