package com.daleelo.Hasanat.Activities;

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



public class ZakatFAQActivity extends Activity{

private ExpandableListView mFaqsListView;

	
	
	private final String[] mQuestions = {"What is Zakat due?",
			 "What is Nisab?",
			 "The value of what I own varies throught the year. What should I do?",
			 "What is Zakatable wealth?",
			 "Don’t I have to possess the full amount of Zakatable wealth for a complete lunar year before I have to pay Zakat on it?",
			 "How do I calculate the Zakat I owe?",
			 "Does One Pay Zakat on Jewelry?",
			 "I have a service provider company and all I have there are computers and other equipment that I use. How do I pay my Zakat?",
			 "I have a grocery store, what Zakat do I have to pay?",
			 "How Important Is Local Zakat Distribution?",
			 "Can Zakat Ever Be Substituted by Other Payments?",
			 "Can Zakat Payers Restrict the Use of Their Payments?"};
	
	
	private final String[][] mAnswers = { {"Zakat becomes obliged upon a Muslim after a time span of one lunar" +
										   " year (Hijri) passes while you own the Nisab,"+ 
										   "and if you always possess the minimum Nisab value," +
										   " then Zakat is due once every year at a date of your choosing"},
										  {"Nisab is a minimum amount of wealth you must own in order to be " +
										   	"liable to pay Zakat. It is the value of 3 oz of pure gold."},
										  {"Since most Muslims in the USA own at least the value of Nisab, " +
										  	"you don’t have to keep track of when you acquired this amount." +
										  	"Therefore, to simplify matters, choose a specific date of the lunar" +
										  	" calendar of every year, calculate the total value of your Zakatable" +
										  	" wealth on that date, and give 2.5% of it (or the rate that applies to" +
										  	" the type of wealth you own)."},
										  	
										  {"Zakatable wealth is what you own, whether in your possession or with others," +
										  	" except for the properties and belongings that you use, such as your house," +
										  	" car, clothes, and business equipment. Also, Zakatable wealth is what remains" +
										  	" with you after meeting the needs and expenses of yourself and your family." +
										  	"This wealth can be in the form of cash, gold, stocks, business commodities or" +
										  	" other wealth."},
										  	
										  {"No. You must only possess the value of Nisab for the whole year. Therefore," +
										   " as long as you have a minimum amount of Nisab for the year," +
										   " you evaluate your Zakatable wealth on your Zakat due date, regardless" +
										   " of when you acquired this wealth or part of it."},
										  
			                              {"Click the CALCULATE tool provided here to compute your zakat due."},
			                            
			                              {"According to the majority of schools, Maliki, Shafi and Hanbali, there is no" +
			                               " Zakat on women’s jewelry and ornaments, whether they are made of gold, silver," +
			                               " diamonds, pearls, minerals, or precious stones, so long as they are for personal use." +
			                               "If such jewelry attains a quantity of extravagance or is used as a store of wealth," +
			                               " then they are zakatable. The Hanafi position is that jewelry is zakatable."},
			                             
										  {"There is no Zakat due on the value of the equipment you mentioned, since they are used" +
										   " for business. But because you own this company, you should combine your personal" +
										   " Zakatable wealth with that of the company."},
										   
										  {"You do not have to pay Zakat on your equipment, such as refrigerators, cashier machines," +
										   " and other useable equipment and tools. Even if you own the store property, you don’t" +
										   " have to pay Zakat on it.However, you must pay Zakat on the wholesale price of all" +
										   " inventories and on other Zakatable assets mentioned in the worksheet."},
										
									      {"GENERAL RULE: Zakat must be disbursed in the area where it is collected.The poor and needy" +
											" of a locality where Zakat is collected have priority over all others as recipients." +
								   		    "(Fiqh az-Zakat, 515; for exceptions to this, see question 8).Local distribution of" +
									   		" Zakat from a community’s wealthy to its poor is the Sunnah of the Prophet and," +
									   		" consequently, paramount.The Prophet œ established this precedent with Mu’adh ibn" +
									   		" Jabal in Yemen with unmistakable language, as we have seen.Furthermore, Mu’adh," +
									   		" himself, divided Yemen into local regions and had Zakat collected and distributed" +
									   		" from the wealthy of those internal localities to the poor of the same place of" +
									   		" collection. Many other verified accounts from the Companions to this effect exist." +
									   		" Indeed, the principle of local collection and distribution has been the established" +
									   		" practice implemented by every succeeding Muslim generation, and endorsed by all the " +
									   		"scholars. To take Zakat out of locality and give it in another place when poor still" +
									   		" remain in the locality is a serious violation of Zakat,if there are no extenuating " +
									   		"circumstances. Nor can this obstacle be easily overcome. In a well-known case," +
									   		"‘Umar ibn Al-Khattab was queried about the Zakat of the Bedouins [who were nomadic]." +
									   		" He replied, “By Allah! I shall render sadaqah (here, meaning Zakat) to them until" +
									   		" each one of them becomes the owner of 100 camels, male or female” (Al-Musannaf, 3:205)." +
									   		" That is to say, each one of them would become wealthy before removing the Zakat of" +
									   		" the Bedouins (who were generally very poor) from their locality. This prevention is" +
									   		" particularly strong when it comes to Zakat in-kind (livestock, crops, etc.)." +
									   		" There is some scholarly disagreement if one’s Zakat is monetary and earned in one" +
									   		" place, while its payer lives in another. Most scholars consider it payable in the" +
									   		" place where the payer resides, rather than where it was earned (Fiqh az-Zakat, 511)."},
								   		
		                                  {"The Zakat-Charity is obligatory not optional; it is worship,not a tax. No matter the" +
	                              		   " country one lives in, and whether one’s taxes increase or decrease,there is no" +
	                              		   " substitute for paying Zakat. Zakat is a permanent and continuous Pillar of Islam." +
	                              		   " No tax can ever replace it. No circumstance can ever preclude its payment whenever" +
	                              		   " it comes due.God, Himself, has made the giving of Zakat to the needy and entitled a" +
	                              		   " sign of loyalty to Him.Governments may forgive unpaid taxes, but none can absolve" +
	                              		   " one of due Zakat payments—no matter how far back they accumulate—for Zakat is other" +
	                              		   " people’s money. Muslim scholars, such as the Eleventh-Century Andalusian polymath Ibn Hazm," +
	                              		   " have said that one who has failed to pay Zakat shall have one’s due Zakat calculated at" +
	                              		   " its set percentage rate and then multiplied by the years it was not paid—even " +
	                              		   "if this consumes all of one’s wealth. Other scholars hold that nonpayment of Zakat" +
	                              		   " forfeits one’s right to transact business. Moreover, if a transaction stipulates" +
	                              		   " that a portion of profits shall inure to the benefit of a Zakat fund, nonpayment" +
	                              		   " of that fund nullifies the contract.Zakat is a solemn obligation. The moment it falls" +
	                              		   " due upon one’s wealth, that portion no longer belongs to the wealth-holder.The poor and" +
	                              		   " eligible automatically become its rightful owners. Let no believing man or woman feel" +
	                              		   " content with the wealth God has granted them until they have duly distributed the Zakat" +
	                              		   " due on it to the poor and needy, who are its lawful trustees in the sight of God."},
                              		   
		                                 {"GENERAL RULE: Charities are a sacred trust (amanah) to be spent according to the giver’s " +
		                                  "wishes,provided they are in accordance with the injunctions of the Quran and the specifications" +
		                                  " of the Prophet.Islam enjoins administrators of charity—including Zakat— to honor the" +
		                                  " legitimate purposes intended by its payersfor the charity they have vouchsafed them." +
		                                  " Whether individual, organization, or government agency, Islam considers the appointed" +
		                                  " executor of Zakat and sadaqah charity a temporary trustee (wakîl) over all such funds," +
		                                  " which are deemed “restricted” or “designated,” and which the trustee is constrained by" +
		                                  " Divine Law (Sharî’ah) to disburse as intended by its giver. The trustee is no more than" +
		                                  " a conduit between payer and recipient, empowered only to act as intermediary." +
		                                  " The trustee’s agency powers expand (only to the full breadth of the injunctions" +
		                                  " of the Quran pertaining to the gift) if the payer of Zakat (or sadaqah) imparts his" +
		                                  " or her payment to the trustee with no further designation than identifying it as either" +
		                                  " Zakat or sadaqah, which must be done. In this case, the trustee can disburse such" +
		                                  " unrestricted Zakat (or sadaqah) as is deemed best, again, in accordance with the limitations set by the Quran and defined by the Prophet."}};
		
	
	private FrequentlyAskedQuestionsAdapter mAskedQuestionsAdapter = null;
	
	DisplayMetrics metrics;
	int width, baby_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.faqs);
		mFaqsListView = (ExpandableListView)findViewById(R.id.faqs_ExpandableListview);
		
		mAskedQuestionsAdapter = new FrequentlyAskedQuestionsAdapter(ZakatFAQActivity.this, mQuestions, mAnswers);
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
