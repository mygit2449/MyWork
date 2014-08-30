package com.daleelo.Utilities;

public class Urls {
	
//	BASE URL	
	
	public static final String BASE_URL = "http://www.daleelo.com/service/daleeloservice.asmx/";
	
//	USER	
	
	public static final String CHANGE_PASSWORD = BASE_URL+"UpdatePassword?userid=%s&password=%s";
	public static final String FORGOT_PASSWORD = BASE_URL+"Forgotpassword?Email=%s";	

	
//	HOME PAGE
	
	public static final String HOME_PAGE_DASHBOARD_ITEMS_URL = BASE_URL+"GetDashboardItems?CityName=%s&latitude=%s&longitude=%s&Range=%s";
	public static final String HOME_PAGE_CATEGORIES_URL = BASE_URL+"GetHomepageCategories";
	public static final String HOME_PAGE_MORE_CATEGORIES_URL = BASE_URL+"GetMastercategores";	
	
//	BUSINESS PAGE
	
	public static final String BUSINESS_DATA_URL = BASE_URL+"GetBusinessdetailsByFilters?BusinesName=%s&BusnessOwner=%s&CategoryId=%s&SubCategory=%s&CityName=%s&Latitude=%s&Longitude=%s&Range=%s&startvalue=%s&endvalue=%s&OrderBy=%s&DealsOnly=%s";
	public static final String BUSINESS_FEATURED_DATA_URL = BASE_URL+"GetFeatureBusinessdetails?CategoryId=%s&CityName=%s&Latitude=%s&Longitude=%s&Range=%s&startvalue=%s&endvalue=%s";
	public static final String BUSINESS_DETAILS_BY_ID_URL = BASE_URL+"GetBusinessdetailsByBusinessId?BusinessId=%s";
	public static final String BUSINESS_DEALS_BY_ID_URL = BASE_URL+"GetDealsByBusinessId?BusinessId=%s";
	public static final String BUSINESS_GET_REVIEWS_URL = BASE_URL+"GetReviewsByBusiness?BusinessId=%s";
	public static final String BUSINESS_ADD_REVIEW_URL = BASE_URL+"AddReviews?ReviewContent=%s&Rating=%s&PostedBy=%s&Businessid=%s&ReviewTittle=%s";
	
//	QIBLAH PAGE
	
	public static final String PRAYER_TIMING_URL = "http://www.islamicfinder.org/prayer_service.php?country=%s&city=%s&state=%s" +
	"&zipcode=&latitude=%s&longitude=%s&timezone=%s&HanfiShafi=1&pmethod=%s&" +
	"fajrTwilight1=10&fajrTwilight2=10&ishaTwilight=10&ishaInterval=30&" +
	"dhuhrInterval=1&maghribInterval=1&dayLight=0&simpleFormat=xml&monthly=1&month=";	
	
	public static final String PRAYER_DAILY_TIMING_URL = "http://www.islamicfinder.org/prayer_service.php?country=%s&city=%s&state=%s" +
	"&zipcode=&latitude=%s&longitude=%s&timezone=%s&HanfiShafi=1&pmethod=%s&" +
	"fajrTwilight1=10&fajrTwilight2=10&ishaTwilight=10&ishaInterval=30&" +
	"dhuhrInterval=1&maghribInterval=1&dayLight=0&simpleFormat=xml";
	
	public static final String TIME_ZONE_URL = "http://www.earthtools.org/timezone/%s/%s";
	public static final String TIME_ZONE_URL_ONE = "http://ws.geonames.org/timezone?lat=%s&lng=%s";
	
//	SPOTLIGHT PAGE
	public static final String SPOTLIGHT_BY_CITY_URL = BASE_URL+"GetSpotLightItemsByCity?CityName=%s&Type=%s&latitude=%s&longitude=%s&Range=%s";

//	DEALS PAGE
	public static final String DEALS_BY_CITY_URL = BASE_URL+"GetDealsByCity?CityName=%s&Latitude=%s&Longitude=%s&Type=%s&Range=%s";
	public static final String DEALS_BY_CATEGORY_URL = BASE_URL+"GetDealsBycategory?CategoryId=%s&CityName=%s&Latitude=%s&Longitude=%s&Type=%s&Range=%s";
	
//	EVENTS PAGE	
	
	public static final String DASHBOARD_EVENTS_URL = BASE_URL+"GetEventsByCity?CityName=%s&Latitude=%s&Longitude=%s&Range=%s";
	public static final String EVENT_DETAILS_BY_ID_URL = BASE_URL+"GetEventsByEventid?Event_Id=%s";
	public static final String GET_EVENTS_BANNERS = BASE_URL+"GetCalanderBanners";

	
//	CLASSIFIED PAGE
	
	public static final String CLASSIFIED_POST_IMG = "http://daleelo.com/Addcsimages.ashx";	
	public static final String CLASSIFIEDS_BY_CITY_URL = BASE_URL+"GetClassifiedByCity?categoryId=%s&CityName=%s&latitude=%s&longitude=%s&Range=%s&startvalue=%s&endvalue=%s";
	public static final String CLASSIFIEDS_BY_FILTER_URL = BASE_URL+"ClasifiedbyFilters?Sortfor=%s&ClassifiedSection=%s&ClassifiedCategory=%s&Natinaltype=%s&CityName=%s&latitude=%s&longitude=%s&Range=%s&PriceMin=%s&PriceMax=%s";
	
//	MASJID PAGE
	
	public static final String MOSQUE_BY_FILTER_URL = BASE_URL+"GetMosqueByFilters?categoryIds=%s&SubCategory=%s&CountryID=%s&CityName=%s&latitude=%s&longitude=%s&Range=%s&OrderBy=%s";
	public static final String MOSQUE_BY_CATEGORY_URL =  BASE_URL+"GetMosqueByCategories?categoryId=%s&CityName=%s&latitude=%s&longitude=%s&Range=%s&startvalue=%s&endvalue=%s";
	
//  HASANAT PAGE
	
	public static final String FEATURED_MOSQUE_URL = BASE_URL+"GetFaturedtMosques?categoryId=%s&CityName=%s&latitude=%s&longitude=%s&Range=%s&startvalue=%s&endvalue=%s";
	public static final String HASANAT_CATEGORY_URL = BASE_URL+"GetHasanatCategories";
	public static final String GET_MOSQUE_DETAILS_BY_ID = BASE_URL+"GetMosquedetailsBusinessId?BusinessId=%s";
	public static final String GET_HASANATH_BANNERS = BASE_URL+"GetHasanathBanners";
	

//	ZAKAT PAGE
	
	public static final String ZAKAT_CATEGORY_URL =  BASE_URL+"GetZakathaCategories";
	public static final String GET_ZAKAT_BANNERS = BASE_URL+"GetZakataBanners";
	
			
//	SADAQA PAGE	
	
//	public static final String MOSQUE_BY_FILTER_URL = BASE_URL+"GetMosqueByFilters?categoryIds=%s&SubCategory=%s&CountryID=%s&CityName=%s&latitude=%s&longitude=%s&Range=%s&OrderBy=%s";
	public static final String SADAKA_CATEGORY_URL = BASE_URL+"GetSadakaCategories";
			
//  ORGANIZATION PAGE	
	
	public static final String ORGANIZATIONS_CATEGORY_URL = BASE_URL+"GetOrganizations";
		
//  GIVE PAGE	
			
			
//	TRIPPLANNER PAGE
			
	public static final String TRIPPLANNER_URL	=	BASE_URL+"TripplannerDetails?StartLocation=%s&EndLocation=%s&Categories=%s&Rangeinfo=%s";

	
//	ADVERTISEMENT PAGE
	
	
//	MORE SECTION
	
	
//	OTHER
	
	public static final String COUNTRY_URL = BASE_URL+"GetCountrys";
	public static final String GLOBAL_SEARCH = BASE_URL+"GetGlobalSearchBusiness?PrimarykeyWord=%s&SecoundaryKeyWord=%s&Cityname=%s&Latitude=%s&Longitude=%s&Range=%s";
	
//	IMAGE URLS
	
	public static final String CAT_IMG_URL = "http://www.daleelo.com/images/CA/%s";
	public static final String SL_IMG_URL = "http://www.daleelo.com/images/SI/%s";
	public static final String CI_IMG_URL = "http://www.daleelo.com/images/CI/%s";	
	public static final String DEAL_IMG_URL = "http://www.daleelo.com/images/DI/%s";
	public static final String HASANAT_BANNER_IMG_URL = "http://www.daleelo.com/images/HB/%s";
	public static final String CALENDER_BANNER_IMG_URL = "http://www.daleelo.com/images/HC/%s";
	
//	CITIES URL BASED ON STATE CODE
	
	public static String CITIES_BY_STATE = BASE_URL+"GetCitiesByState?stateCode=%s";
	
//	SUB CATEGORIES URL BASED ON CATEGORY ID
	
	public static String SUBCATEGORIES_BY_CATEGORYID = BASE_URL+"GetSubCategories?CategoryId=%s";

//	VALIDATE BUSINESS URL
	
	public static String VALIDATE_BUSINESS_URL = BASE_URL+"ValidateBusiness?Business_Title=%s&Address_Line1=%s&Address_Line2=%s&CityName=%s&StateCode=%s&CategoryID=%s&NationalType=%s";
	
//	POST BUSINESS URL
	
	public static String POST_BUSINESS_ADD_URL = BASE_URL+"AddBusinessDetailsByMobilee?BusinessTitle=%s&" +
			"Tobeplaced=%s&UserID=%s&Category_Id=%s&CategoryMasterId=%s&Location=%s&CityID=%s&" +
			"Zipcode=%s&Business_Phone=%s&Fax=%s" +
			"&Email=%s&Lat=%s&Long=%s&PaymentResponse=%s&Noofpayments=%s&Billdate=%s&" +
			"billcycletype=%s&PayAmount=%s&nationaltype=%s&PostFrom=%s";

//	STATES URL
	
	public static String STATES_URL = BASE_URL+"GetStates";
	
//	GLOBAL SEARCH URL
	
	public static String GLOBALSEARCH_URL = BASE_URL + "GetGlobalSearchBusiness?PrimarykeyWord=%s&SecoundaryKeyWord=$s&Cityname=%s&AddressInfo=%s";
	
//	SHARE URL
	
	public static String SHARE_BUSINESS_URL = "http://daleelo.com/BusinessInfo.aspx?Businessid=%s";

}





//public static String GOOGLE_GEOCODING_API = "http://maps.googleapis.com/maps/api/geocode/xml?latlng=%s,%s&sensor=true";
//Services Url  http://65.19.149.190/dws/daleeloservice.asmx
//Add Classified http://65.19.149.190/acpd/AddClassified.ashx
//Testing page http://65.19.149.190/acpd/AddClassified.aspx
//Update Classfied http://65.19.149.190/acpd/EditClassified.ashx
//Testing page http://65.19.149.190/acpd/EditClassified.aspx
//http://daleelo.com/AddCsInfo.aspx
