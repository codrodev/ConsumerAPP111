package com.yo4gis.consumersurvey;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;


import com.yo4gis.consumersurvey.network.ApiFactory;
import com.yo4gis.consumersurvey.network.MyApiService;
import com.yo4gis.consumersurvey.network.NetworkConnectionInterceptor;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


public class NetworkSurveyApp extends Application {

    private MyApiService apiService;
    private NetworkConnectionInterceptor networkConnectionInterceptor;
    private Scheduler scheduler;


    private static NetworkSurveyApp networkSurveyApp = null;

    public static synchronized NetworkSurveyApp getInstance() {
        return networkSurveyApp;
    }

    private static NetworkSurveyApp get(Context context) {
        return (NetworkSurveyApp) context.getApplicationContext();
    }

    public static NetworkSurveyApp create(Context context) {
        return NetworkSurveyApp.get(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        networkSurveyApp = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        networkConnectionInterceptor = new NetworkConnectionInterceptor(networkSurveyApp);

        //addLanguage();
    }

    public MyApiService getApiService() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        if(apiService == null){
            apiService = ApiFactory.getClient(networkConnectionInterceptor);
        }

        return apiService;
    }
    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }

}
