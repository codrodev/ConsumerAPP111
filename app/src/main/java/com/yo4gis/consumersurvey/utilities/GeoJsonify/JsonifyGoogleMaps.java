package com.yo4gis.consumersurvey.utilities.GeoJsonify;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.utilities.AppUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.data.Geometry;
import com.google.maps.android.data.geojson.GeoJsonFeature;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.android.data.geojson.GeoJsonLineStringStyle;
import com.google.maps.android.data.geojson.GeoJsonPointStyle;
import com.google.maps.android.data.geojson.GeoJsonPolygon;
import com.google.maps.android.data.geojson.GeoJsonPolygonStyle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

class JsonifyGoogleMaps {
    static void geoJsonifyMap(GoogleMap map, String str, Context context) throws IOException, JSONException {
        GeoJsonLayer layer = null;

        layer = new GeoJsonLayer(map, new JSONObject(str));
        if (layer != null) {
            //layer.getDefaultPolygonStyle().setStrokeColor(Color.parseColor("#3bffba"));
            //layer.getDefaultPolygonStyle().setFillColor(Color.BLUE);
            layer.addLayerToMap();
        }
        if (layer != null) {
            try {
                //map.moveCamera(CameraUpdateFactory.newLatLngBounds(getLayerBoundingBox(layer), 0));
                getLayerBoundingBox(layer);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Log.i("geojson-viewer", "No coordinates available to center the camera.");
            }
        }
    }

    private static LatLngBounds getLayerBoundingBox(GeoJsonLayer layer) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        int x = 1;
        for (GeoJsonFeature feature : layer.getFeatures()) {
            if (feature.hasGeometry()) {
                if (feature.getGeometry().getGeometryType().contains("Point")) {
                    //double magnitude = Double.parseDouble(feature.getProperty("mag"));
                    String id = feature.getProperty("ID");
                    // Get the icon for the feature
                    /*BitmapDescriptor pointIcon = BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.fromResource(R.drawable.user_icon_resized));*/
                    // Create a new point style
                    GeoJsonPointStyle pointStyle = new GeoJsonPointStyle();
                    pointStyle.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.dark_blue));
                    pointStyle.setTitle(id);
                    //pointStyle.setSnippet(id);
                    // Assign the point style to the feature
                    feature.setPointStyle(pointStyle);
                } else if (feature.getGeometry().getGeometryType().contains("Polygon")) {
                    /*TileProvider tileProvider; // ... create a tile provider.
                    TileOverlay tileOverlay = map.addTileOverlay(
                            new TileOverlayOptions().tileProvider(tileProvider));*/

                    GeoJsonPolygonStyle poly = new GeoJsonPolygonStyle();
                    poly.setStrokeColor(Color.YELLOW);
                    poly.setStrokeWidth(3);
                    feature.setPolygonStyle(poly);
                } else if (feature.getGeometry().getGeometryType().contains("LineString")) {
                    GeoJsonLineStringStyle lineString = new GeoJsonLineStringStyle();
                    lineString.setColor(Color.YELLOW);
                    lineString.setWidth(3);
                    feature.setLineStringStyle(lineString);
                }

                Geometry geometry = feature.getGeometry();
                if (geometry instanceof GeoJsonPolygon) {
                    List<? extends List<LatLng>> lists =
                            ((GeoJsonPolygon) geometry).getCoordinates();
                    AppUtils.geoLatLon = lists.get(0).get(0);
                    for (List<LatLng> list : lists) {
                        for (LatLng latLng : list) {
                            builder.include(latLng);

                        }
                    }
                }
                x++;
            }
        }

        return builder.build();
    }
}
