package com.yo4gis.consumersurvey.views.fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;
import com.google.maps.android.data.geojson.GeoJsonPoint;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygon;
import com.google.maps.android.ui.IconGenerator;
import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.databinding.FragmentMapBinding;
import com.yo4gis.consumersurvey.listeners.CommonResponseNavigator;
import com.yo4gis.consumersurvey.network.ApiFactory;
import com.yo4gis.consumersurvey.network.NetworkConnectionInterceptor;
import com.yo4gis.consumersurvey.repositories.MapRepository;
import com.yo4gis.consumersurvey.services.FetchAddressIntentService;
import com.yo4gis.consumersurvey.utilities.AppUtils;
import com.yo4gis.consumersurvey.utilities.GeoJsonify.GeoJsonify;
import com.yo4gis.consumersurvey.utilities.Global;
import com.yo4gis.consumersurvey.viewModel.MapViewModel;
import com.yo4gis.consumersurvey.views.factories.MapFactory;
import com.yo4gis.consumersurvey.views.listeners.BasicFragmentListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, CommonResponseNavigator {
    private FragmentMapBinding binding;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static String TAG = "MAP LOCATION";
    private LatLng mCenterLatLong;
    BottomSheetDialogFragment bottomSheetForm;
    private ArrayList<LatLng> arrayPoints = null;
    private ArrayList<Marker> arrayMarkers = null;
    PolygonOptions polygonOptions;
    Polygon polygon = null;
    List<Polygon> lstPolygon;
    Boolean isBuildingEnabled = false;
    BasicFragmentListener frListener;
    private SharedPreferences myPreference;
    FusedLocationProviderClient client;
    MapFactory factory;
    MapRepository repository;
    MapViewModel model;
    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;
    /**
     * The formatted location address.
     */
    protected String mAddressOutput;
    protected String mAreaOutput;
    protected String mCityOutput;
    protected String mStateOutput;
    protected double lat;
    protected double lon;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    private GeoJsonLayer layerDistribution = null;
    private GeoJsonLayer layerPole = null;
    private GeoJsonLayer layerLtline = null;
    private GeoJsonLayer layerLtcable = null;
    private GeoJsonLayer layerConsumer = null;
    private GeoJsonLayer layerBuilding = null;


    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.map.onCreate(savedInstanceState);
        binding.map.onResume();
        // binding.map.getMapAsync(this);
        initializeView();
    }

    private void initializeView() {
        try {
            repository = new MapRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new MapFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(MapViewModel.class);
        model.navigator = this;
        model.apiGeoJson();
        myPreference = getActivity().getSharedPreferences(AppUtils.MY_PREF, Context.MODE_PRIVATE);
        client = LocationServices.getFusedLocationProviderClient(getActivity());
        getCurrentLocation();

        binding.markerConsumerSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.locationMarker.getVisibility() == View.VISIBLE) {
                    binding.locationMarker.setVisibility(View.GONE);
                } else {
                    binding.locationMarker.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.markerBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*markerRemove.setVisibility(View.VISIBLE);
                markerDraw.setVisibility(View.VISIBLE);
                markerForm.setVisibility(View.VISIBLE);*/

                if (isBuildingEnabled) {

                   /* for (int i = 0; i <lstPolygon.size(); i++) {
                        lstPolygon.remove(i);
                    }
                    lstPolygon.clear();
                    PolygonOptions polygonOptions1 = new PolygonOptions();
                    polygonOptions1.addAll(arrayPoints);
                    polygonOptions1.strokeColor(Color.YELLOW);
                    polygonOptions1.strokeWidth(4);
                    polygonOptions1.fillColor(Color.argb(20, 50, 0, 255));
                    mMap.addPolygon(polygonOptions1);

                    markerBuilding.setBackgroundResource(R.drawable.floating_button);
                    isBuildingEnabled = false;
                    arrayPoints = new ArrayList<>();
                    Intent i=new Intent(getBaseContext(),BuildingFormActivity.class);
                    i.putExtra("address",mStateOutput);
                    i.putExtra("lat",mCenterLatLong.latitude);
                    i.putExtra("lon",mCenterLatLong.longitude);
                    startActivity(i);*/
                    polygonOptions = new PolygonOptions();
                    arrayPoints = new ArrayList<>();
                    arrayMarkers = new ArrayList<>();
                    /*Intent i=new Intent(getActivity(),BuildingFormActivity.class);
                    i.putExtra("address",mStateOutput);
                    i.putExtra("lat",mCenterLatLong.latitude);
                    i.putExtra("lon",mCenterLatLong.longitude);
                    startActivity(i);*/
                    binding.markerBuilding.setBackgroundResource(R.drawable.floating_button);
                    Navigation.findNavController(binding.getRoot()).navigate(MapFragmentDirections.actionMapFragmentToBuildingFormFragment());
                } else {
                    /*lstPolygon = new ArrayList<>();
                    polygonOptions = new PolygonOptions();
                    markerBuilding.setBackgroundResource(R.drawable.floating_button_selected);*/
                    binding.markerBuilding.setBackgroundResource(R.drawable.floating_button_selected);
                    lstPolygon = new ArrayList<>();
                    arrayMarkers = new ArrayList<>();
                    isBuildingEnabled = true;
                }
            }
        });

        /*markerDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayPoints != null && arrayPoints.size() > 0) {
                    polygonOptions = new PolygonOptions();
                    polygonOptions.strokeColor(Color.YELLOW);
                    polygonOptions.strokeWidth(4);
                    polygonOptions.fillColor(Color.GRAY);
                    polygonOptions.fillColor(Color.argb(20, 50, 0, 255));
                    polygonOptions.addAll(arrayPoints).clickable(true);
                    polygon = mMap.addPolygon(polygonOptions);
                    arrayPoints = new ArrayList<>();
                } else {
                    Toast.makeText(Custommap.this, "Please add points", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.markerRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                polygon.remove();
                for (Marker marker:
                        arrayMarkers) {
                    marker.remove();
                }
                polygonOptions = new PolygonOptions();
                arrayPoints = new ArrayList<>();
                arrayMarkers = new ArrayList<>();
            }
        });

        binding.markerForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markerRemove.setVisibility(View.GONE);
                markerDraw.setVisibility(View.GONE);
                markerForm.setVisibility(View.GONE);
                polygonOptions = new PolygonOptions();
                arrayPoints = new ArrayList<>();
                arrayMarkers = new ArrayList<>();
                Intent i=new Intent(getBaseContext(),BuildingFormActivity.class);
                i.putExtra("address",mStateOutput);
                i.putExtra("lat",mCenterLatLong.latitude);
                i.putExtra("lon",mCenterLatLong.longitude);
                startActivity(i);
            }
        });*/
        binding.map.getMapAsync(this);
        mResultReceiver = new AddressResultReceiver(new Handler());

        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!AppUtils.isLocationEnabled(getActivity())) {
                // notify user
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("Location not enabled!");
                dialog.setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        // TODO Auto-generated method stub

                    }
                });
                dialog.show();
            }
            buildGoogleApiClient();
        } else {
            Toast.makeText(getActivity(), "Location not supported in this device", Toast.LENGTH_SHORT).show();
        }


        binding.locationMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.selectedLatLon = mCenterLatLong;
                /*Intent i=new Intent(getActivity(),FormActivity.class);
                i.putExtra("address",mStateOutput);

                i.putExtra("lat",mCenterLatLong.latitude);

                i.putExtra("lon",mCenterLatLong.longitude);
                startActivity(i);*/
//i.putExtra("lat",20.462521);
                //i.putExtra("lon",85.882988);
                // finish();
                // bottomSheetForm.show(getActivity().getSupportFragmentManager(), bottomSheetForm.getTag());
                Navigation.findNavController(binding.getRoot()).navigate(MapFragmentDirections.actionMapFragmentToConsumerSurveyFormFragment());
            }
        });

        binding.markerMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    Global.setIsMapView(false);
                    binding.markerMapView.setBackground(getResources().getDrawable(R.drawable.satellite_view));
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    Global.setIsMapView(true);
                    binding.markerMapView.setBackground(getResources().getDrawable(R.drawable.map_view));
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        frListener = (BasicFragmentListener) getActivity();
        frListener.isTopBar(false);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //changeMap(mLastLocation);
            Log.d(TAG, "ON connected");

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(100);
            mLocationRequest.setFastestInterval(100);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            if (location != null)
                //changeMap(location);
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, this);
            //drawLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "OnMapReady");
        mMap = googleMap;
        if (Global.isIsMapView()) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            binding.markerMapView.setBackground(getResources().getDrawable(R.drawable.map_view));
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            binding.markerMapView.setBackground(getResources().getDrawable(R.drawable.satellite_view));

        }
        loadGeoJson();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {

                if (isBuildingEnabled) {
                    if (polygon != null) {
                        polygon.remove();
                    }
                    if (arrayPoints == null) {
                        arrayPoints = new ArrayList<>();
                    }
                    arrayPoints.add(latLng);
                    MarkerOptions markerOption = new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pond));
                    Marker marker = mMap.addMarker(markerOption);
                    arrayMarkers.add(marker);
                    polygonOptions = new PolygonOptions();
                    polygonOptions.strokeColor(Color.YELLOW);
                    polygonOptions.strokeWidth(4);
                    polygonOptions.fillColor(Color.GRAY);
                    polygonOptions.fillColor(Color.argb(20, 50, 0, 255));
                    polygonOptions.addAll(arrayPoints).clickable(true);
                    polygon = mMap.addPolygon(polygonOptions);
                    List<LatLng> list = (polygon.getPoints());
                    Global.setArrayPolygonPoints(list);

                    ///Closed on 26th March 2022
                    /*mMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.dark_blue)));
                    arrayPoints.add(latLng);

                    polygonOptions.addAll(arrayPoints);
                    polygonOptions.strokeColor(Color.YELLOW);
                    polygonOptions.strokeWidth(4);
                    polygonOptions.fillColor(Color.GRAY);
                    polygonOptions.fillColor(Color.argb(20, 50, 0, 255));
                    Polygon polygon = mMap.addPolygon(polygonOptions);
                    lstPolygon.add(polygon);*/
                    ///Closed on 26th March 2022


                    //PolygonOptions polygonOptions = new PolygonOptions();

                    /*polygonOptions.addAll(arrayPoints);
                    polygonOptions.strokeColor(Color.YELLOW);
                    polygonOptions.strokeWidth(4);
                    polygonOptions.fillColor(Color.argb(20, 50, 0, 255));

                    lstPolygonOptions.add(polygonOptions);*/


                    //mMap.addPolygon(polygonOptions);

                    //googleMap.addPolygon(polygonOptions);
                    //int color = polygon.getFillColor();
                    //System.out.println("test" + color);
                }
            }
        });

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("Camera position change" + "", cameraPosition + "");
                mCenterLatLong = cameraPosition.target;

                //mMap.clear();

                try {

                    Location mLocation = new Location("");
                    mLocation.setLatitude(mCenterLatLong.latitude);
                    mLocation.setLongitude(mCenterLatLong.longitude);

                    startIntentService(mLocation);
                    // mLocationMarkerText.setText("Lat : " + mCenterLatLong.latitude + "," + "Long : " + mCenterLatLong.longitude);
                    //addMarker();
                    //setUpGeoJSON();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        //addMarker();
    }

    private void loadGeoJson() {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        if (mMap != null) {
            if (Global.getGeoJsonData() != null) {
                /*if(layerDistribution!=null)
                {
                    layerDistribution.removeLayerFromMap();
                }*/
                for (int i = 0; i < Global.getGeoJsonData().size(); i++) {
                    try {
                        String layname = Global.getGeoJsonData().get(i).getLayerName();
                        if (layname.equalsIgnoreCase("dis_transformer")) {
                            GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
                            pointStyle.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.powersubstation));
                            layerDistribution = new GeoJsonLayer(mMap, new JSONObject(Global.getGeoJsonData().get(i).getGeoJson()));
                            if (layerDistribution != null) {
                                layerDistribution.addLayerToMap();
                            }
                            for (GeoJsonFeature feature : layerDistribution.getFeatures()) {
                                String id = feature.getProperty("nin_id");
                                pointStyle.setTitle(id);
                                feature.setPointStyle(pointStyle);

                            }
                        }
                        if (layname.equalsIgnoreCase("pole")) {
                            GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
                            pointStyle.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.dark_blue));
                          //  String  geojsonvalue=Global.getGeoJsonData().get(i).getGeoJson();
                            layerPole = new GeoJsonLayer(mMap, new JSONObject(Global.getGeoJsonData().get(i).getGeoJson()));
                            if (layerPole != null) {
                                layerPole.addLayerToMap();
                            }
                            for (GeoJsonFeature feature : layerPole.getFeatures()) {
                                String id = feature.getProperty("nin_id");
                                pointStyle.setTitle(id);
                                feature.setPointStyle(pointStyle);
                                Geometry geometry = feature.getGeometry();
                                LatLng pnt=((GeoJsonPoint) geometry).getCoordinates();

                                TextView text = new TextView(getActivity());
                                text.setText(id);
                                IconGenerator generator = new IconGenerator(getActivity());
                                //generator.set(getActivity().getDrawable(R.drawable.powersubstation));
                                generator.setContentView(text);
                                Bitmap icon = generator.makeIcon();


                                MarkerOptions markerOption = new MarkerOptions().position(pnt).icon(BitmapDescriptorFactory.fromBitmap(icon));
                                Marker marker = mMap.addMarker(markerOption);
                                marker.showInfoWindow();
                               // arrayMarkers.add(marker);



                            }
                        }
                        if (layname.equalsIgnoreCase("consumer")) {
                            GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
                            pointStyle.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.consumer));
                            layerConsumer = new GeoJsonLayer(mMap, new JSONObject(Global.getGeoJsonData().get(i).getGeoJson()));
                            if (layerConsumer != null) {
                                layerConsumer.addLayerToMap();
                            }
                            for (GeoJsonFeature feature : layerConsumer.getFeatures()) {
                                String id = feature.getProperty("id");
                                pointStyle.setTitle(id);
                                feature.setPointStyle(pointStyle);

                            }
                        }
                        if (layname.equalsIgnoreCase("lt_line")) {

                            GeoJsonLineStringStyle lineString = new GeoJsonLineStringStyle();
                            lineString.setColor(Color.BLUE);
                            lineString.setWidth(3);

                            layerLtline = new GeoJsonLayer(mMap, new JSONObject(Global.getGeoJsonData().get(i).getGeoJson()));
                            if (layerLtline != null) {
                                layerLtline.addLayerToMap();
                            }
                            for (GeoJsonFeature feature : layerLtline.getFeatures()) {
                                String id = feature.getProperty("nin_id");
                                feature.setLineStringStyle(lineString);

                            }
                        }
                        if (layname.equalsIgnoreCase("lt_cable")) {
                            GeoJsonLineStringStyle lineString = new GeoJsonLineStringStyle();
                            lineString.setColor(Color.MAGENTA);
                            lineString.setWidth(3);

                            layerLtcable = new GeoJsonLayer(mMap, new JSONObject(Global.getGeoJsonData().get(i).getGeoJson()));
                            if (layerLtcable != null) {
                                layerLtcable.addLayerToMap();
                            }
                            for (GeoJsonFeature feature : layerLtcable.getFeatures()) {
                                String id = feature.getProperty("nin_id");
                                feature.setLineStringStyle(lineString);

                            }
                        }
                        if (layname.equalsIgnoreCase("building")) {
                            GeoJsonLineStringStyle lineString = new GeoJsonLineStringStyle();
                            lineString.setColor(Color.YELLOW);
                            lineString.setWidth(3);

                            layerBuilding = new GeoJsonLayer(mMap, new JSONObject(Global.getGeoJsonData().get(i).getGeoJson()));
                            if (layerBuilding != null) {
                                layerBuilding.addLayerToMap();
                            }
                            for (GeoJsonFeature feature : layerBuilding.getFeatures()) {
                                String id = feature.getProperty("nin_id");
                                feature.setLineStringStyle(lineString);

                            }
                        }
                        // System.out.print("lName: "+layname);
                        //  GeoJsonify.geoJsonifyMap(mMap, Global.getGeoJsonData().get(i).getGeoJson(), getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
   /* private void loadGeoJson(){
        if(mMap != null) {
            try {
                AssetManager manager = getActivity().getAssets();
                InputStream strem;
                try {
                    strem = getActivity().getAssets().open("cosumer_survey.geojson");
                    int size = strem.available();
                    byte[] buffer = new byte[size];
                    strem.read(buffer);
                    strem.close();
                    String txt = new String(buffer, "UTF-8");
                    GeoJsonify.geoJsonifyMap(mMap, txt, getActivity());
                } catch (Exception ex) {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mCenterLatLong = AppUtils.geoLatLon;
        changeMap();
        drawLine();
    }*/

    /* private void drawLine(){
         if(mMap != null) {
             binding.locationMarker.setVisibility(View.GONE);
             if(AppUtils.formData != null && AppUtils.formData.getLat() != 0) {
                 Polyline polyline = mMap.addPolyline(new PolylineOptions()
                         .add(
                                 new LatLng(AppUtils.formData.getLat(), AppUtils.formData.getLon()),
                                 new LatLng(20.31630670447039, 85.82937955856323))
                         .width(5f)
                         .color(Color.parseColor("#3d5c5c")));

                 mMap.addMarker(new MarkerOptions().position(new LatLng(AppUtils.formData.getLat(),
                         AppUtils.formData.getLon()))
                         .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_icon_resized))
                         .title(AppUtils.formData.getUniqueID()));

                 AppUtils.formData = null;
             }
         }

     }*/
    private void drawLine() {
        if (mMap != null) {
            binding.locationMarker.setVisibility(View.GONE);
            // drawPoleLine();
            if (Global.lstPoleLatLngs != null && Global.lstPoleLatLngs.size() > 0) {
                for (int i = 0; i < Global.lstPoleLatLngs.size(); i++) {
                    if (Global.lstPoleLatLngs.get(i).getTitle() != "") {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Global.lstPoleLatLngs.get(i).getLocation().latitude,
                                Global.lstPoleLatLngs.get(i).getLocation().longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.powerlinepole))
                                .title(Global.lstPoleLatLngs.get(i).getTitle()));
                    } else {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Global.lstPoleLatLngs.get(i).getLocation().latitude,
                                Global.lstPoleLatLngs.get(i).getLocation().longitude))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.powerlinepole)));
                    }
                }
            }

        }

    }

    private void addMarker() {
        if (mMap != null) {
            if (AppUtils.lstFormData != null && AppUtils.lstFormData.size() > 0) {
                for (int i = 0; i < AppUtils.lstFormData.size(); i++) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(AppUtils.lstFormData.get(i).getLat(),
                            AppUtils.lstFormData.get(i).getLon())).title(AppUtils.lstFormData.get(i).getConsumerName()));

                }
            }
        }
    }

    void setUpGeoJSON() {
        if (mMap != null) {
            try {
                GeoJsonLayer layer = new GeoJsonLayer(mMap, R.raw.cosumer_survey, getActivity());
                //addGeoJsonLayerToMap(layer);
                layer.addLayerToMap();
            } catch (IOException e) {
                // Log.e(mLogTag, "GeoJSON file could not be read");
            } catch (JSONException e) {
                // Log.e(mLogTag, "GeoJSON file could not be converted to a JSONObject");
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);

                // TODO call location based filter


                LatLng latLong;


                latLong = place.getLatLng();

                //mLocationText.setText(place.getName() + "");

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLong).zoom(19f).tilt(70).build();

                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));


            }


        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(getActivity(), data);
        } else if (resultCode == RESULT_CANCELED) {

        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //finish();
            }
            return false;
        }
        return true;
    }

    private void changeMap(/*Location location*/) {

        Log.d(TAG, "Reaching map" + mMap);


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(false);
            LatLng latLong;


            //latLong = new LatLng(location.getLatitude(), location.getLongitude());
            latLong = new LatLng(mCenterLatLong.latitude, mCenterLatLong.longitude);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLong).zoom(19f).build();

            mMap.setMyLocationEnabled(true);
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        } else {
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.map_creation_issue), Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public void onSuccess() {
        loadGeoJson();
    }

    @Override
    public void onSaveconsumer() {

    }

    @Override
    public void onEmpty(String Msg) {

    }

    @Override
    public void onFailure(String Msg) {

    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY);

            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET);

            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }

    /**
     * Updates the address in the UI.
     */
    protected void displayAddressOutput() {
        /*mLocationAddress.setText(mStateOutput);
        try {
            if (mCityOutput != null)
                // mLocationText.setText(mAreaOutput+ "");

                mLocationAddress.setText(mStateOutput);
            //mLocationText.setText(mAreaOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    protected void startIntentService(Location mLocation) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        getActivity().startService(intent);
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng lng = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(lng).zoom(18f).build();
                    mMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }
            }
        });
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
}