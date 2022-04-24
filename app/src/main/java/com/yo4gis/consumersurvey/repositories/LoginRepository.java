package com.yo4gis.consumersurvey.repositories;



import com.yo4gis.consumersurvey.model.PostLogin;
import com.yo4gis.consumersurvey.model.ResponseLogin;
import com.yo4gis.consumersurvey.network.MyApiService;

import io.reactivex.Observable;

public class LoginRepository {
    private MyApiService api;

    public LoginRepository(MyApiService apiService){
        this.api = apiService;
    }


    public Observable<ResponseLogin> postLoginAPI(String url, PostLogin model){
        return api.postLoginAPI(model.getUsername(), model.getPassword(), model.getGrantType());
    }
}
