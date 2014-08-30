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

package com.daleelo.TripPlanner.Helper;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.daleelo.Masjid.Model.MasjidModel;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class TripPlannerCustomOverlayItem extends OverlayItem {

	public static final int SOURCE = 0;
	public static final int DESTINATION = 1;
	public static final int MIDDLE_ADD = 2;
	public static final int MIDDLE_REMOVE = 3;
	
	public Context context;
	public MasjidModel mMasjidLocationModelObj;
	public Drawable marker;
	public int type_of_item;
	
	public TripPlannerCustomOverlayItem(GeoPoint point, String title, String snippet,Context context) {
		super(point, title, snippet);
		this.context = context;
	}
	public TripPlannerCustomOverlayItem(GeoPoint point, String title, String snippet,Context context,Drawable marker) {
		super(point, title, snippet);
		this.context = context;
		this.marker = marker;
	}
	public TripPlannerCustomOverlayItem(GeoPoint point, String title, String snippet,Context context,int type_of_item) {
		super(point, title, snippet);
		this.type_of_item = type_of_item;
		this.context = context;
	}
	public TripPlannerCustomOverlayItem(GeoPoint point, String title, String snippet,Context context,Drawable marker,int type_of_item ) {
		super(point, title, snippet);
		this.context = context;
		this.marker = marker;
		this.type_of_item = type_of_item;
	}
	public TripPlannerCustomOverlayItem(GeoPoint point, String title, String snippet,Context context,MasjidModel mMasjidLocationModelObj) {
		super(point, title, snippet);
		this.context = context;
		this.mMasjidLocationModelObj=mMasjidLocationModelObj;
		
	}
	
	@Override
	public Drawable getMarker(int stateBitset) {
		
		return marker;
	}
	
}