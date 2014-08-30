package com.daleelo.Main.Model;

import java.io.Serializable;
import java.util.ArrayList;

import com.daleelo.DashBoardClassified.Model.GetClassifiedItemsModel;
import com.daleelo.DashBoardEvents.Model.EventsCalenderModel;
import com.daleelo.Dashboard.Model.GetDealsInfoModel;
import com.daleelo.Dashboard.Model.GetSpotLightModel;

public class MainGetTodayDetailsModel implements Serializable{
	
	private String hasItem;
	private ArrayList<GetClassifiedItemsModel> ClassfiedItemsModel;	
	private ArrayList<GetDealsInfoModel> DealInfoModel;
	private ArrayList<EventsCalenderModel> EventsDetailsModel;
	private ArrayList<GetSpotLightModel> SpotLightModel;
	
	public String getHasItem() {
		return hasItem;
	}
	public void setHasItem(String hasItem) {
		this.hasItem = hasItem;
	}
	public ArrayList<GetClassifiedItemsModel> getClassfiedItemsModel() {
		return ClassfiedItemsModel;
	}
	public void setClassfiedItemsModel(
			ArrayList<GetClassifiedItemsModel> classfiedItemsModel) {
		ClassfiedItemsModel = classfiedItemsModel;
	}
	public ArrayList<GetDealsInfoModel> getDealInfoModel() {
		return DealInfoModel;
	}
	public void setDealInfoModel(ArrayList<GetDealsInfoModel> dealInfoModel) {
		DealInfoModel = dealInfoModel;
	}
	public ArrayList<EventsCalenderModel> getEventsDetailsModel() {
		return EventsDetailsModel;
	}
	public void setEventsDetailsModel(
			ArrayList<EventsCalenderModel> eventsDetailsModel) {
		EventsDetailsModel = eventsDetailsModel;
	}
	public ArrayList<GetSpotLightModel> getSpotLightModel() {
		return SpotLightModel;
	}
	public void setSpotLightModel(ArrayList<GetSpotLightModel> spotLightModel) {
		SpotLightModel = spotLightModel;
	}
	
	
		
}
