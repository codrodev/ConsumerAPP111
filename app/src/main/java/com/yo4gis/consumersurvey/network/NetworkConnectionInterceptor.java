package com.yo4gis.consumersurvey.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.yo4gis.consumersurvey.utilities.Global;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class NetworkConnectionInterceptor implements Interceptor {

    private Context applicationContext;
    private String credentials;

    public NetworkConnectionInterceptor(Context context){
        applicationContext = context.getApplicationContext();
        this.credentials = null;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        if (!Global.isConnected(applicationContext))
            throw new Exceptions.NoInternetException("Make sure you have active data connection");

        Request.Builder requestBuilder = chain.request().newBuilder();
        requestBuilder.header("Content-Type", "application/json");
        if (Global.getAccessToken() != null && Global.getAccessToken().length() > 0) {
            requestBuilder.header("Authorization", Global.getAccessToken());
        }
        return chain.proceed(requestBuilder.build());
    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null){
            return networkInfo.isConnected()||networkInfo.isConnectedOrConnecting();
        }
        return false;
    }
}
