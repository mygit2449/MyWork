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

package com.daleelo.Business.Helper;

import java.util.ArrayList;

import android.content.Context;

import com.daleelo.Business.Model.BusinessListModel;
import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;
import com.daleelo.Dashboard.Model.GetDealsInfoModel;
import com.daleelo.Dashboard.Model.GetSpotLightModel;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class BusinessCustomOverlayItem extends OverlayItem {

	public Context context;
	ArrayList<BusinessListModel> mBusinessDataModelList = null;
	ArrayList<GetSpotLightModel> mSpotDataModelList = null;
	ArrayList<GetDealsInfoModel>  mDealDataModelList = null;
	ArrayList<EventsCalenderModel>  mEventDataModelList = null;
	
	String mType, mFrom;
	
	
	public BusinessCustomOverlayItem(GeoPoint point, String title, String snippet,Context context,ArrayList<BusinessListModel> mDataModelList, String mFrom, String mType) {
		super(point, title, snippet);
		this.context = context;
		this.mBusinessDataModelList = mDataModelList;
		this.mFrom = mFrom;
		this.mType = mType;
	}
	
	public BusinessCustomOverlayItem(GeoPoint point, String title, String snippet,Context context,ArrayList<GetSpotLightModel> mDataModelList, 
			String mFrom, String mType, int DummyArgOne) {
		
		super(point, title, snippet);
		this.context = context;
		this.mSpotDataModelList = mDataModelList;
		this.mFrom = mFrom;
		this.mType = mType;
	}
	
	public BusinessCustomOverlayItem(GeoPoint point, String title, String snippet,Context context,ArrayList<GetDealsInfoModel> mDataModelList, 
			String mFrom, String mType, int DummyArgOne, int DummyArgTwo) {
		
		super(point, title, snippet);
		this.context = context;
		this.mDealDataModelList = mDataModelList;
		this.mFrom = mFrom;
		this.mType = mType;
	}
	
	public BusinessCustomOverlayItem(GeoPoint point, String title, String snippet,Context context,ArrayList<EventsCalenderModel> mDataModelList, 
			String mFrom, String mType, int DummyArgOne, int DummyArgTwo, int DummyArgThree) {
		
		super(point, title, snippet);
		this.context = context;
		this.mEventDataModelList = mDataModelList;
		this.mFrom = mFrom;
		this.mType = mType;
	}
}