package com.yo4gis.consumersurvey.repositories;



import com.yo4gis.consumersurvey.model.PostBuilding;
import com.yo4gis.consumersurvey.model.PostLogin;
import com.yo4gis.consumersurvey.model.ResponseBuilding;
import com.yo4gis.consumersurvey.model.ResponseLogin;
import com.yo4gis.consumersurvey.network.MyApiService;

import io.reactivex.Observable;

public class BuildingRepository {
    private MyApiService api;

    public BuildingRepository(MyApiService apiService){
        this.api = apiService;
    }


   /* public Observable<ResponseBuilding> postBuildingAPI(String url, PostBuilding model){
        return api.postBuildingAPI(model.getBuildingName(), model.getBuildingType(),model.getTown(),model.getSubstation_name(), model.getBuildingGeomString());
    }*/
    public Observable<ResponseBuilding> postBuildingAPI(String url, PostBuilding model){
        return api.postBuildingAPI(model);
    }
}
