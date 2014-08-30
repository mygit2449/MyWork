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

import java.util.ArrayList;

import android.content.Context;

import com.daleelo.Hasanat.Model.BusinessDetailModel;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class HasanatCustomOverlayItem extends OverlayItem {

	public Context context;
	ArrayList<BusinessDetailModel> mBusinessDataModelList = null;
	
	String mFrom;
	
	
	public HasanatCustomOverlayItem(GeoPoint point, String title, String snippet,Context context,ArrayList<BusinessDetailModel> mDataModelList, String mFrom) {
		super(point, title, snippet);
		this.context = context;
		this.mBusinessDataModelList = mDataModelList;
		this.mFrom = mFrom;
	}


	public HasanatCustomOverlayItem(GeoPoint point, String title,
			String snippet, MoreInformationMapActivity moreInformationMapActivity) {
		// TODO Auto-generated constructor stub
		super(point, title, snippet);
	}
	
	
}