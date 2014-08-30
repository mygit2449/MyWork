package com.daleelo.Hasanat.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.daleelo.R;

public class ZakatLearnActivity extends Activity{
	

	private ExpandableListView exp_listview;
	private ExpandAdapter      expandAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hasanat_sadaka_learn_layout);
		initializeUI();			
		exp_listview.setAdapter(expandAdapter);	

	}

	

	private void initializeUI() {		
		
		exp_listview = (ExpandableListView)findViewById(R.id.hasanat_expand_listview);	
		expandAdapter= new ExpandAdapter(this);
	}
	

	public class ExpandAdapter extends BaseExpandableListAdapter {
		
		
		 public ExpandAdapter(Context context) {
				// TODO Auto-generated constructor stub
			 this.context = context;
			}

		 Context context;
		
		 private String[] groups = { "", "fuqaraa", "Masakeen", "Ameleena Alayha", "Mu'Allafa Qolobohom","Riqab","Gharimeen","Fi sabi 'Lillah","lbn as'Sabil"}; 
		 private String[][] children = {{""},
				 					  {getResources().getString(R.string.fuqaraa)},
				 					  {getResources().getString(R.string.Masakeen)},
				 					  {getResources().getString(R.string.Ameleena_Alayha)}, 
				 					  {getResources().getString(R.string.MuAllafa_Qolobohom)},
				 					  {getResources().getString(R.string.Riqab)},
				 					  {getResources().getString(R.string.Gharimeen)}, 
				 					  {getResources().getString(R.string.Fi_sabi_Lillah)},
				 					  {getResources().getString(R.string.lbn_as_Sabil)}  };
		 
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			
			 return children[groupPosition][childPosition];

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
				    LayoutInflater inflater =  (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				    convertView = inflater.inflate(R.layout.faqs_child_layout, null);
				   }
				
				TextView mAnswer_Txt = (TextView)convertView.findViewById(R.id.faqs_TXT_answer);
				
				mAnswer_Txt.setText(children[groupPosition][childPosition]);
				
				return convertView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			  int i = 0;
		        try {
		        i = children[groupPosition].length;

		        } catch (Exception e) {
		        }

		        return i; 

		}

		@Override
		public Object getGroup(int groupPosition) {
			  return groups[groupPosition];

		}

		@Override
		public int getGroupCount() {
			return groups.length;

		}

		@Override
		public long getGroupId(int groupPosition) {
			 return groupPosition;

		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			
			if (convertView == null) {
	            LayoutInflater infalInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = infalInflater.inflate(R.layout.faqs_group_layout, null);
	        }

			ImageView mOpenList_Img = (ImageView)convertView.findViewById(R.id.faqs_IMG_indiactor);
			//ImageView mCloseList_Img = (ImageView)convertView.findViewById(R.id.faqs_IMG_indiactorclose);

			TextView mQuestion_Txt = (TextView)convertView.findViewById(R.id.faqs_TXT_title);			
			
//			mQuestion_Txt.setText(""+groups[groupPosition]);
			if(groupPosition==0){
				mOpenList_Img.setVisibility(View.INVISIBLE);
//				mQuestion_Txt.setText(Html.fromHtml("<html><b><font color=\"#3BB9FF\">Zakat:</font></b><font color=\"black\">The third pillar of Islam. It is mandatory when two conditions are met, which are \"Nisab\" and \"Due Date\"</font>\n\n<b><font color=\"#3BB9FF\">Nisab:</font></b> Nisab is a minimum amount of wealth you must own in order to be liable to pay Zakat. It is the price of 3 oz. (85 Grams) of pure gold, on the day in which Zakat is to be paid. (Current Gold Price) – link to this http://www.goldprice.org/ \n\n<b><font color=\"#3BB9FF\">Due Date:</font></b> Zakat is due after a time span of one lunar year (Hijri) passes while you own the Nisab, and if you always possess the minimum Nisab value, then Zakat is due once every year at a date of your choosing.\n\nThe 8 Recipients of Zakat\n\nFrom the Qur’anic verse ordaining zakat, eight classes of recipients have been identified \n\n \"Alms are for the poor and the needy, and those employed to administer the (funds); for those whose hearts have been (recently) reconciled (to the truth); for those in bondage and in debt; in the cause of God; and for the wayfarer: (thus is it) ordained by God, and God is full of knowledge and wisdom.\"</html>"));
				
			}
			return convertView;

		}

		@Override
		public boolean hasStableIds() {			
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {			
			return false;
		}
		
		
	}
}
