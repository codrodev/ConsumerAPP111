package com.yo4gis.consumersurvey.repositories;




import com.yo4gis.consumersurvey.model.ResponseAssignedTaskList;
import com.yo4gis.consumersurvey.network.MyApiService;

import io.reactivex.Observable;

public class SurveyListRepository {
    private MyApiService api;

    public SurveyListRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<ResponseAssignedTaskList> getAssignedTask(String url){
        return api.getAssignedTask(url);
    }
}
