package com.yo4gis.consumersurvey.model;

public class GeoJsonData {
    private String geoJson;

    private String layerName;

    public String getGeoJson () {
        return geoJson;
    }

    public void setGeoJson (String geoJson) {
        this.geoJson = geoJson;
    }

    public String getLayerName () {
        return layerName;
    }

    public void setLayerName (String layerName) {
        this.layerName = layerName;
    }
}
