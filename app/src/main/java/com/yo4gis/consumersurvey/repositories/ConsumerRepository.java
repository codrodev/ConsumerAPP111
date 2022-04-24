package com.yo4gis.consumersurvey.repositories;



import com.yo4gis.consumersurvey.model.PostBuilding;
import com.yo4gis.consumersurvey.model.PostConsumerCreate;
import com.yo4gis.consumersurvey.model.ResponseBuilding;
import com.yo4gis.consumersurvey.model.ResponseConsumer;
import com.yo4gis.consumersurvey.network.MyApiService;

import io.reactivex.Observable;

public class ConsumerRepository {
    private MyApiService api;

    public ConsumerRepository(MyApiService apiService){
        this.api = apiService;
    }

    public Observable<ResponseBuilding> getBuildingID(String url){
        return api.getBuildingID(url);
    }
   /* public Observable<ResponseBuilding> postBuildingAPI(String url, PostBuilding model){
        return api.postBuildingAPI(model.getBuildingName(), model.getBuildingType(),model.getTown(),model.getSubstation_name(), model.getBuildingGeomString());
    }*/
    public Observable<ResponseConsumer> postConsumerAPI(String url, PostConsumerCreate model){
        return api.postConsumerCreateAPI(model);
    }
}
