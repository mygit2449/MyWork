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
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.balloon.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.balloon.readystatesoftware.mapviewballoons.BalloonOverlayView;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MasjidCustomItemizedOverlay<Item extends OverlayItem> extends BalloonItemizedOverlay<MasjidCustomOverlayItem> {

	private ArrayList<MasjidCustomOverlayItem> m_overlays = new ArrayList<MasjidCustomOverlayItem>();
	private Context c;
	
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, false);
	}

	public MasjidCustomItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
     	c = mapView.getContext();
	}

	public void addOverlay(MasjidCustomOverlayItem overlay) {
	    m_overlays.add(overlay);	    
	    populate();
	}
	
	public void removeOverlay(){
		m_overlays.clear();		
	}

	@Override
	protected MasjidCustomOverlayItem createItem(int i) {
		return m_overlays.get(i);
	}
	
	
	@Override
	public int size() {
		return m_overlays.size();
	}
	
	
	@Override
	protected BalloonOverlayView<MasjidCustomOverlayItem> createBalloonOverlayView() {
		
		return new MasjidCustomBalloonOverlayView<MasjidCustomOverlayItem>(
				getMapView().getContext(), getBalloonBottomOffset()+50);
	}

}
