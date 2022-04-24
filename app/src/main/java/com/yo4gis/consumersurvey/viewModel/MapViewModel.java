package com.yo4gis.consumersurvey.viewModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.yo4gis.consumersurvey.NetworkSurveyApp;
import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.listeners.CommonResponseNavigator;
import com.yo4gis.consumersurvey.model.ResponseGeoJson;
import com.yo4gis.consumersurvey.repositories.MapRepository;
import com.yo4gis.consumersurvey.utilities.AppUtils;
import com.yo4gis.consumersurvey.utilities.Global;
import com.yo4gis.consumersurvey.views.fragments.AlertDialogUtil;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MapViewModel extends ViewModel {

    private NetworkSurveyApp networkSurveyApp;
    MapRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public CommonResponseNavigator navigator;
    SharedPreferences myPref;


    public MapViewModel(Activity activity, MapRepository repository){
        this.activity = activity;
        this.repository = repository;
        networkSurveyApp = NetworkSurveyApp.create(activity);
        myPref = activity.getSharedPreferences(AppUtils.MY_PREF, Context.MODE_PRIVATE);
    }

    public void apiGeoJson(){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);

        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.show();
        progressBar.setContentView(R.layout.progress_layout);

        String url = String.format(AppUtils.GET_GEO_JSON, Global.getSelectedAssignedTask().getSubstation11(),
                Global.getSelectedAssignedTask().getFeeder11(), "C",Global.getSelectedAssignedTask().getTown(),Global.getSelectedAssignedTask().getDt_name());
        Disposable disposable = repository.getGeoJsonFeature(url)
                .subscribeOn(networkSurveyApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseGeoJson>() {
                    @Override public void accept(ResponseGeoJson response) throws Exception {
                        if(response != null && response.getStatus() != null && response.getStatus().equals("200") && response.getData() != null){
                            String s = new Gson().toJson(response.getData());
                            Global.setGeoJsonData(response.getData());
                            navigator.onSuccess();
                        } else {
                            navigator.onEmpty("");
                        }
                        progressBar.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        progressBar.dismiss();
                        AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.error),
                                throwable.getMessage(), activity);
                        navigator.onFailure(throwable.getMessage());
                    }
                });

        compositeDisposable.add(disposable);
    }
}
