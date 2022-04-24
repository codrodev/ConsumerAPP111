package com.yo4gis.consumersurvey.utilities;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.google.android.gms.maps.model.LatLng;
import com.yo4gis.consumersurvey.model.BuildingFormData;
import com.yo4gis.consumersurvey.model.FormData;

import java.util.ArrayList;
import java.util.List;


public class AppUtils {

    public static final String BASE_URL = "http://access.spaceimagingme.com:6092/MSITDEV/";
    public static final String TOKEN = "token";
    public static final String CREATE_POLE = "api/msit/create";
    public static final String GET_ASSIGN_TASK_BY_USER = "api/msit/getassigntaskbyuserid?username=";
    public static final String GET_POLL_LIST = "api/msit/GetPoleList?substionName=%s&feederName=%s&surveryType=%s&featureType=%s";
    public static final String GET_NIN_ID = "api/msit/getNinIdByFeature?substionName=%s&feederName=%s&surveryType=%s&featureType=%s";
    public static final String GET_SECTION_LIST = "api/msit/getsectionlist";
    public static final String GET_GEO_JSON = "api/msit/getgeojson?substaionname=%s&feedername=%s&surveytype=%s&town=%s&dt_name=%s";
    public static final String GET_BUILDING_ID = "api/msit/GetBuildingIdByConsumer?x=%s&y=%s";

    public static final String ACTION_BUILDING_ID = "Building Id";

    public static final String MY_PREF = "NetworkSurveyAppPref";
    public static final String EMPTY = "";
    public static final int MY_PERMISSIONS_REQUEST = 1;
    public static String[] ALL_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,

    };

    public static String IMAGE_EXTN = ".jpg";
    public static String AUTHORITY = "com.yo4gis.networksurvey.fileprovider";
    public static String MEDIA_IMAGE = "IMAGE_";
    public static List<FormData> lstFormData = new ArrayList<>();
    public static FormData formData = new FormData();
    public static BuildingFormData buildingFormData = new BuildingFormData();
    public static LatLng geoLatLon;

    public static LatLng selectedLatLon;
    public static LatLng previousSelectedLatLon;


    public class LocationConstants {
        public static final int SUCCESS_RESULT = 0;

        public static final int FAILURE_RESULT = 1;

        public static final String PACKAGE_NAME = "com.sample.sishin.maplocation";

        public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

        public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

        public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

        public static final String LOCATION_DATA_AREA = PACKAGE_NAME + ".LOCATION_DATA_AREA";
        public static final String LOCATION_DATA_CITY = PACKAGE_NAME + ".LOCATION_DATA_CITY";
        public static final String LOCATION_DATA_STREET = PACKAGE_NAME + ".LOCATION_DATA_STREET";


    }


    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

}
