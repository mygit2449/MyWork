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

package com.daleelo.Masjid.Helper;

import java.util.ArrayList;

import android.content.Context;

import com.daleelo.Masjid.Model.MasjidModel;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MasjidCustomOverlayItem extends OverlayItem {

	public Context context;
	ArrayList<MasjidModel> mDataModelList = null;
	String mFrom;
	
	public MasjidCustomOverlayItem(GeoPoint point, String title, String snippet,Context context) {
		super(point, title, snippet);
		this.context = context;
	}
	
	public MasjidCustomOverlayItem(GeoPoint point, String title, String snippet,Context context,ArrayList<MasjidModel> mDataModelList,
			 String mFrom) {
		
		super(point, title, snippet);
		this.context = context;
		this.mDataModelList = mDataModelList;
		this.mFrom = mFrom;
		
	}
	
}