package com.daleelo.Hasanat.Activities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.daleelo.R;
import com.daleelo.Hasanat.Model.CategoryModel;

public class OrganizationFilterActivity extends Activity implements OnClickListener, OnCheckedChangeListener{

	private ArrayList<CategoryModel> mCategoryList;
	private Button mbtn_done;
	private RelativeLayout mrel_chk;
	private CheckBox[] chkbxes;
	private RadioGroup mFilterRadioGroup;
	private RadioButton mSortType, mRBName, mRBDistance, mRBBestMatch;
	private ArrayList<String> mData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organization_filter);
		initializeUI();
	}
	
	private void initializeUI(){
		
		mCategoryList = (ArrayList<CategoryModel>) getIntent().getExtras().get("category");
		mData = (ArrayList<String>) getIntent().getSerializableExtra("data");
		
		mbtn_done = (Button)findViewById(R.id.organization_filter_BTN_done);
		mrel_chk = (RelativeLayout)findViewById(R.id.organization_filter_REL_chk);
		mFilterRadioGroup = (RadioGroup)findViewById(R.id.organization_filter_radiogroup);
		mRBName = (RadioButton)findViewById(R.id.organization_filter_radio_name);
		mRBDistance = (RadioButton)findViewById(R.id.organization_filter_radio_distance);
		
		mbtn_done.setOnClickListener(this);
		
		chkbxes = new CheckBox[(mCategoryList.size()+1)];
		
		
		if(mData.get(7).equalsIgnoreCase("1")){
			
			mRBName.setChecked(true);
			
		}else{			
			
			mRBDistance.setChecked(true);	
			
		}
		setCheckboxes();
	}
	
	private void setCheckboxes(){
		
		for(int i=0; i<(mCategoryList.size()+1); i++){
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			chkbxes[i] = new CheckBox(this);
			chkbxes[i].setTextColor(R.color.black);
			chkbxes[i].setButtonDrawable(R.drawable.customised_filter_checkbox);
			chkbxes[i].setId(i+1);
			chkbxes[i].setOnCheckedChangeListener(this);
			
			
			if(i == 0){
				
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				chkbxes[i].setText("All");
				chkbxes[i].setLayoutParams(params);
				
			}else if(i == 1){
				
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				chkbxes[i].setText(mCategoryList.get(i-1).getCategoryName());
				chkbxes[i].setLayoutParams(params);
				
			}else {
				
				chkbxes[i].setText(mCategoryList.get(i-1).getCategoryName());
				
				if(( (i) % 2) == 0){
					
					params.addRule(RelativeLayout.BELOW, chkbxes[(i)-1].getId());
					chkbxes[i].setLayoutParams(params);
					
				}else{
					
					params.addRule(RelativeLayout.BELOW, chkbxes[(i)-2].getId());
					params.addRule(RelativeLayout.ALIGN_LEFT, chkbxes[(i)-2].getId());
					chkbxes[i].setLayoutParams(params);
					
				}
				
			}
			mrel_chk.addView(chkbxes[i]);
		}
		chkbxes[0].setChecked(true);
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		switch (buttonView.getId()) {
		case 1:
			
			if(isChecked){
				
				for(int i=1; i< chkbxes.length;i++){
					
					chkbxes[i].setChecked(false);
				}
			}
			
			break;
			
		default:
			
			if(isChecked)
				chkbxes[0].setChecked(false);	
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.organization_filter_BTN_done:
			Set<String> selectedCategory = new HashSet<String>();
			Set<String> selectedCategoryNameSet = new HashSet<String>();
			
			for(int i=0; i< chkbxes.length; i++){

				if(chkbxes[i].isChecked()){
					if(i == 0){
							
						selectedCategory.add("0");
						selectedCategoryNameSet.add("Organization");
							
					}else{
							
						selectedCategory.add(mCategoryList.get(i-1).getCategoryId());
						selectedCategoryNameSet.add(mCategoryList.get(i-1).getCategoryName());
							
					}
				}
			}
			
			String selectedCategoryId = selectedCategory.toString().substring(1,selectedCategory.toString().length()-1 );
			String selectedCategoryNames = selectedCategoryNameSet.toString().substring(1,selectedCategoryNameSet.toString().length()-1 );
			
			mData.remove(1);
			mData.add(1,selectedCategoryId);
			Log.e("selectedCategory",selectedCategoryId);
						
			if(mFilterRadioGroup.getCheckedRadioButtonId() == R.id.organization_filter_radio_name){
				
				mData.remove(7);
				mData.add(7,"1");
				
			}else{
				
				mData.remove(7);
				mData.add(7,"2");
				
			}
			
			
			Intent CategoryIntent = new Intent();
			CategoryIntent.putExtra("selectedCategory", selectedCategoryId);
			CategoryIntent.putExtra("selectedCategorynames", selectedCategoryNames);
			CategoryIntent.putExtra("data", mData);
			
			setResult(RESULT_OK, CategoryIntent);
			OrganizationFilterActivity.this.finish();
			
			break;

		default:
			break;
		}
		
	}
	
		
}