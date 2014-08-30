package com.skeyedex.GroupList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.skeyedex.R;
import com.skeyedex.Model.EmailModel;
import com.skeyedex.Model.SMSmodel;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class YestardayMessagesAdapter extends BaseAdapter{

	
	private static final String LOG_TAG = TodayMessagesAdapter.class.getSimpleName();
	
	private static String MONTH[] = { "Jan", "Feb", "Mar", "April", "May","June", "July", "Aug", "Sept", "Oct", "Nov", "Dec" };
	
	private HashMap<String, Object> mSelectedEmail, mSelectedSms; 
	private String selectType = "manual";
	
	Object obj;
	Context context;
	ArrayList arrMessages;
	ArrayList<Object> newEmail = new ArrayList<Object>();
	
	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;

	private static final int TYPE_MAX_COUNT = 2;
	
	public YestardayMessagesAdapter(Context context, ArrayList _allMessages, HashMap<String, Object> checkEmailMap, HashMap<String, Object> checkSmsMap)
	{
		
		this.context=context;
		this.arrMessages = _allMessages;
		
		mSelectedEmail = checkEmailMap;
		mSelectedSms = checkSmsMap;
		
	}
	
	 @Override
	 public boolean isEnabled(int position) { 
		 Object obj = arrMessages.get(position);
		 if(obj instanceof String) 
		 {
			 	return	 false; 
		 } 
		 return true;
	 
	 }

	 @Override 
	 public int getItemViewType(int position){
		 Object obj = arrMessages.get(position);
		 if(obj instanceof String) {
			 return  TYPE_SEPARATOR; 
		 }
	   return TYPE_ITEM;
	}


	 @Override 
	  public int getViewTypeCount() { 
		  return TYPE_MAX_COUNT;
	  }
	  

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrMessages.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			
				int type = getItemViewType(position);
				RelativeLayout email_relative;
				Date emDate = null;
			
				String email_date = "";
				String email_time = "";
			  	String allsubject = "";
				String alltime ="";
				String msg_sender = "";
	
				if (convertView == null)
				{
					LayoutInflater inflater = LayoutInflater.from(context);
					
					if (type == TYPE_ITEM) 
					{
						convertView = inflater.inflate(R.layout.group_messages_list, null);
					}else {
						convertView = inflater.inflate(R.layout.email_seperator, null);
					}
		
			   	}
			
	   
			
			obj = arrMessages.get(position);
			
			if (type == TYPE_ITEM)
			{
				
				    email_relative = (RelativeLayout)convertView.findViewById(R.id.group_listview_Rel);
					TextView subject = (TextView)convertView.findViewById(R.id.emaillistrow_txt_partofemail);
					TextView sender = (TextView)convertView.findViewById(R.id.emaillistrow_txt_sender);
					TextView date = (TextView)convertView.findViewById(R.id.emaillistrow_txt_datetime);
					TextView time = (TextView)convertView.findViewById(R.id.emaillistrow_txt_datetime);
					
					ImageView sms_image = (ImageView)convertView.findViewById(R.id.emaillistrow_icon_txt);
					ImageView icon_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_indicator);
					ImageView events_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_yellow);
					ImageView business_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_blue);
					ImageView media_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_green);
					ImageView general_image = (ImageView) convertView.findViewById(R.id.emaillistrow_icon_white);
				
					email_relative.setBackgroundResource(R.drawable.gl_purple_bar);
					
					CheckBox email_check = (CheckBox)convertView.findViewById(R.id.emaillistrow_chk_check);
					email_check.setTag(position);
					
					if(obj instanceof EmailModel)
					{
							
							 Log.v(LOG_TAG, " The object is of type Email Model");
							 EmailModel emailModel = (EmailModel)arrMessages.get(position);
							 allsubject= emailModel.getSubject();
							 msg_sender = emailModel.getEmail_Sender();
							 SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							 DateFormat stringTime = new SimpleDateFormat("MMM dd"); 
							 icon_image.setImageResource(((EmailModel) arrMessages.get(position)).getImage_resourceId());
			
							 //checking the selected position of email
							 if (((EmailModel) arrMessages.get(position)).isSelected())
								{
									
									selectType =  "auto";
									Log.e("LogCat Email", "select  true");
									email_check.setChecked(true);
									
								}else 
								{
									selectType =  "auto";
									Log.e("LogCat Email", "select  false");
									email_check.setChecked(false);
								}
								
								selectType = "manual";
								
							 try
								{
									emDate = formatter.parse(emailModel.getDate_time());
									email_time = stringTime.format(emDate);
									
									Log.i("Em Date",""+email_date);
									
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				
								alltime= ""+email_time;
								
								
								events_image.setVisibility(View.INVISIBLE);
								if (emailModel.getEvents_status() == 1) {
									events_image.setVisibility(View.VISIBLE);
								}
								business_image.setVisibility(View.INVISIBLE);
								if (emailModel.getBusiness_status() == 1) {
									business_image.setVisibility(View.VISIBLE);
								}
								media_image.setVisibility(View.INVISIBLE);
								if (emailModel.getMedia_status() == 1) {
									media_image.setVisibility(View.VISIBLE);
								}
								general_image.setVisibility(View.INVISIBLE);
								if (emailModel.getGeneral_status() == 1) {
									general_image.setVisibility(View.VISIBLE);
								}
								
							if (emailModel.getStatus()==0) 
							{
									
									sender.setTypeface(Typeface.DEFAULT_BOLD);
									subject.setTypeface(Typeface.DEFAULT_BOLD);
									date.setTypeface(Typeface.DEFAULT_BOLD);
									
							}else {
								
									sender.setTypeface(Typeface.DEFAULT);
									subject.setTypeface(Typeface.DEFAULT);
									date.setTypeface(Typeface.DEFAULT);
									
								}
								sms_image.setVisibility(View.INVISIBLE);
				}else{		
					
					 SMSmodel smsModel = (SMSmodel) arrMessages.get(position);
					 allsubject= smsModel.getBody();
					 alltime = getMessageDate(smsModel.getDate());
					 Log.v(LOG_TAG, " The object is of type Sms model");
					 sms_image.setVisibility(View.VISIBLE);
					 events_image.setVisibility(View.INVISIBLE);
					 business_image.setVisibility(View.INVISIBLE);
					 media_image.setVisibility(View.INVISIBLE);
					 general_image.setVisibility(View.INVISIBLE);
					 
					 //checking the selected position of sms
					 if (((SMSmodel) arrMessages.get(position)).isSelected())
						{
							
							selectType =  "auto";
							Log.e("LogCat SMS", "select  true");
							email_check.setChecked(true);
							
						}else 
						{
							selectType =  "auto";
							Log.e("LogCat SMS", "select  false");
							email_check.setChecked(false);
						}
						
						selectType = "manual";
						
					 if (smsModel.getStatus()==0) 
						{
								
								sender.setTypeface(Typeface.DEFAULT_BOLD);
								subject.setTypeface(Typeface.DEFAULT_BOLD);
								date.setTypeface(Typeface.DEFAULT_BOLD);
						}else {
								sender.setTypeface(Typeface.DEFAULT);
								subject.setTypeface(Typeface.DEFAULT);
								date.setTypeface(Typeface.DEFAULT);
							}
			     	}
			
				subject.setText(allsubject);
				time.setText(alltime);
				sender.setText(msg_sender);
			
				
				//check box selection for emails and sms's
				email_check.setOnCheckedChangeListener(new OnCheckedChangeListener()
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
					{
						obj = arrMessages.get(position);
						
						if(selectType == "manual")
						{
							if (obj instanceof EmailModel) 
							{
							
								Log.v(LOG_TAG, " obj type is email");
								((EmailModel) arrMessages.get((Integer) buttonView.getTag())).setSelected(isChecked);
								Log.e("", "Email "+((EmailModel) arrMessages.get((Integer) buttonView.getTag())).getEmail_id());
								
								ArrayList<Object> email = (ArrayList<Object>) mSelectedEmail.get(((EmailModel) arrMessages.get((Integer) buttonView.getTag())).getEmail_id());
								
								if(isChecked) 
								{										
									
									if(email == null )
									{
										
											Log.e("LogCat", "email  null");
											//ArrayList<Object> newEmail = new ArrayList<Object>();
											newEmail.add(arrMessages.get((Integer) buttonView.getTag()));
										    mSelectedEmail.put(((EmailModel) arrMessages.get((Integer) buttonView.getTag())).getEmail_id() , newEmail);
										
									}else
									{
										
										Log.e("LogCat", "email  not null"+((EmailModel) arrMessages.get((Integer) buttonView.getTag())).getEmail_id());
										email.add(arrMessages.get((Integer) buttonView.getTag()));
										mSelectedEmail.put(((EmailModel) arrMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);								
										
									}
									
								}else 
								{
								
										ArrayList<EmailModel> emails ;
										emails =(ArrayList<EmailModel>) mSelectedEmail.put(((EmailModel) arrMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);
										
										for(int i = 0 ; i< emails.size(); i++){
											
											Log.e("LogCat", " email some"+emails.get(i).getDate_time());
										}
										
										email.remove(arrMessages.get((Integer) buttonView.getTag()));
										
										mSelectedEmail.put(((EmailModel) arrMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);		
										
										
										emails =(ArrayList<EmailModel>) mSelectedEmail.put(((EmailModel) arrMessages.get((Integer) buttonView.getTag())).getEmail_id() , email);
										
										for(int i = 0 ; i< emails.size(); i++){
											
											Log.e("LogCat", " email some delete"+emails.get(i).getDate_time());
										}
									
								}
								
							}else //sms checking
							  {
								
								Log.v(LOG_TAG, " obj type is SMS");
								((SMSmodel) arrMessages.get((Integer) buttonView.getTag())).setSelected(isChecked);
								Log.e("", "Sms "+((SMSmodel) arrMessages.get((Integer) buttonView.getTag())).getSender());
								
								ArrayList<Object> sms = (ArrayList<Object>) mSelectedSms.get(((SMSmodel) arrMessages.get((Integer) buttonView.getTag())).getSender());  // saving sms based on sender
								
								if(isChecked)
								{										
									
									if(sms == null )
									{
										
											Log.e("LogCat", "sms  null");
											ArrayList<Object> newSms = new ArrayList<Object>();
											newSms.add(arrMessages.get((Integer) buttonView.getTag()));
											mSelectedSms.put(((SMSmodel) arrMessages.get((Integer) buttonView.getTag())).getSender() , newSms);
										
									}else
									{
												
										Log.e("LogCat", "sms  not null"+((SMSmodel) arrMessages.get((Integer) buttonView.getTag())).getSender());
										sms.add(arrMessages.get((Integer) buttonView.getTag()));
										mSelectedSms.put(((SMSmodel) arrMessages.get((Integer) buttonView.getTag())).getSender(), sms);								
									}
									
								}else
								 {
									
										ArrayList<SMSmodel> smsArr ;
										smsArr =(ArrayList<SMSmodel>) mSelectedSms.put(((SMSmodel) arrMessages.get((Integer) buttonView.getTag())).getSender() , sms);
										
										for(int i = 0 ; i< smsArr.size(); i++){
											
											Log.e("LogCat", " sms some"+smsArr.get(i).getBody());
										}
										
										sms.remove(arrMessages.get((Integer) buttonView.getTag()));
										
										mSelectedSms.put(((SMSmodel) arrMessages.get((Integer) buttonView.getTag())).getSender() , sms);		
										
										
										smsArr =(ArrayList<SMSmodel>) mSelectedSms.put(((SMSmodel) arrMessages.get((Integer) buttonView.getTag())).getSender(), sms);
										
										for(int i = 0 ; i< smsArr.size(); i++){
											
											Log.e("LogCat", " sms some delete 1"+smsArr.get(i).getBody());
										}
										
								}
							 }
					     }
						selectType = "manual";
					}
				});
				
			}else if (type == TYPE_SEPARATOR) {
				ImageView icon = (ImageView) convertView.findViewById(R.id.email_seperator_indicator_img);
				icon.setImageResource(R.drawable.blk_y_icon);
				convertView.setBackgroundResource(R.drawable.purple_bar);
				
		   	}
		return convertView;

	}
		
		private String getMessageDate(long timeinmills) 
		{
				Calendar calander = Calendar.getInstance();
				int messageDate, messageMonth;
		
				calander.setTimeInMillis(timeinmills);
				messageDate = calander.get(Calendar.DATE);
				messageMonth = calander.get(Calendar.MONTH);
		
				Date currentDate = new Date(System.currentTimeMillis());
		
				if (calander.get(Calendar.DATE) == currentDate.getDate())
					return "TODAY";
				else
					return MONTH[messageMonth] + " " + ((messageDate>9) ?  (messageDate) : "0" + messageDate);
		}

	

}
