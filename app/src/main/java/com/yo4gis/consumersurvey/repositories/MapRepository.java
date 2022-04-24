package com.yo4gis.consumersurvey.repositories;



import com.yo4gis.consumersurvey.model.ResponseGeoJson;
import com.yo4gis.consumersurvey.network.MyApiService;

import io.reactivex.Observable;

public class MapRepository {
    private MyApiService api;

    public MapRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<ResponseGeoJson> getGeoJsonFeature(String url){
        return api.getGeoJsonFeature(url);
    }
}
