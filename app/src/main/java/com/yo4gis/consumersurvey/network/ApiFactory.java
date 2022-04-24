package com.yo4gis.consumersurvey.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yo4gis.consumersurvey.utilities.AppUtils;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    public static MyApiService getClient(NetworkConnectionInterceptor networkConnectionInterceptor) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        TLSSocketFactory tlsSocketFactory = new TLSSocketFactory();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()

                .addInterceptor(networkConnectionInterceptor).connectTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .writeTimeout(600,TimeUnit.SECONDS)


                .sslSocketFactory(tlsSocketFactory, Objects.requireNonNull(tlsSocketFactory.getTrustManager()))
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(AppUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MyApiService.class);
    }
}
