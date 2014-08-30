/***
 * Copyright (c) 2011 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.daleelo.Hasanat.Helper;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balloon.readystatesoftware.mapviewballoons.BalloonOverlayView;
import com.daleelo.R;
import com.daleelo.Hasanat.Activities.GiveDetailDescActivity;
import com.google.android.maps.OverlayItem;

public class HasanatCustomBalloonOverlayView<Item extends OverlayItem> extends BalloonOverlayView<HasanatCustomOverlayItem> {

	private TextView title;
	private TextView snippet;
	private ImageView image;
	
	public HasanatCustomBalloonOverlayView(Context context, int balloonBottomOffset) {
		super(context, balloonBottomOffset);
	}
	
	@Override
	protected void setupView(Context context, final ViewGroup parent) {
		
		// inflate our custom layout into parent
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.business_balloon_overlay, parent);
		
		// setup our fields
		title = (TextView) v.findViewById(R.id.balloon_item_title);
		snippet = (TextView) v.findViewById(R.id.balloon_item_snippet);
        image = (ImageView) v.findViewById(R.id.balloon_close);
        
       
	}
	
	@Override
	protected void setBalloonData(HasanatCustomOverlayItem item, ViewGroup parent) {
		
		// map our custom item data to fields
		final HasanatCustomOverlayItem mitem = item;
		title.setText(item.getTitle());
		snippet.setText(item.getSnippet());
		
		if(mitem.mFrom.equalsIgnoreCase("desp")){
			
			image.setVisibility(View.INVISIBLE);
			
		}
		
		image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				int pos = 0;				
										
				for(int i=0; i < mitem.mBusinessDataModelList.size();i++){
					if(mitem.getTitle().equalsIgnoreCase(mitem.mBusinessDataModelList.get(i).getBusinessTitle())){
							pos = i;
						break;
					}						
				}
				
				mitem.context.startActivity(new Intent(mitem.context,GiveDetailDescActivity.class)
				.putExtra("data", mitem.mBusinessDataModelList)
				.putExtra("pos", pos)
				.putExtra("from", "list"));
						
								
			}
		});	
	}		
}
