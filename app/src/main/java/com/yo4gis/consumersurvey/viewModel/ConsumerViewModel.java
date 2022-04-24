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
import com.yo4gis.consumersurvey.model.PostBuilding;
import com.yo4gis.consumersurvey.model.PostConsumerCreate;
import com.yo4gis.consumersurvey.model.ResponseBuilding;
import com.yo4gis.consumersurvey.model.ResponseConsumer;
import com.yo4gis.consumersurvey.repositories.BuildingRepository;
import com.yo4gis.consumersurvey.repositories.ConsumerRepository;
import com.yo4gis.consumersurvey.utilities.AppUtils;
import com.yo4gis.consumersurvey.views.fragments.AlertDialogUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ConsumerViewModel extends ViewModel {

    private NetworkSurveyApp networkSurveyApp;
    ConsumerRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public CommonResponseNavigator navigator;
    SharedPreferences myPref;
    SharedPreferences.Editor myEdit;
    public ConsumerViewModel(Activity activity, ConsumerRepository repository){
        this.activity = activity;
        this.repository = repository;
        networkSurveyApp = NetworkSurveyApp.create(activity);
        myPref = activity.getSharedPreferences(AppUtils.MY_PREF, Context.MODE_PRIVATE);
        myEdit = myPref.edit();
    }

    public void apiConsumerCreate(PostConsumerCreate model){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);

        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        progressBar.setContentView(R.layout.progress_layout);

        String url = AppUtils.TOKEN;
        String s = new Gson().toJson(model);
        System.out.println("data"+s);
        Disposable disposable = repository.postConsumerAPI(url, model)
                .subscribeOn(networkSurveyApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseConsumer>() {
                    @Override public void accept(ResponseConsumer response) throws Exception {
                        if(response != null && response.getStatus() != null && response.getStatus().equals("200") && response.getData() != null){
                            navigator.onSaveconsumer();
                        } else {
                            AlertDialogUtil.alertDialog(networkSurveyApp.getResources().getString(R.string.error),
                                    networkSurveyApp.getResources().getString(R.string.api_error), activity);
                            AlertDialogUtil.btnOk.setText(networkSurveyApp.getResources().getString(R.string.ok));
                            navigator.onEmpty("");
                        }
                        progressBar.dismiss();
                    }
                }, new Consumer<Throwable>() {
                    @Override public void accept(Throwable throwable) throws Exception {
                        progressBar.dismiss();
                        AlertDialogUtil.alertDialog(activity.getResources().getString(R.string.error),
                                throwable.getMessage(), activity);
                    }
                });

        compositeDisposable.add(disposable);
    }


    public void apiBuildingID(){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);

        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.show();
        progressBar.setContentView(R.layout.progress_layout);

        String url = String.format(AppUtils.GET_BUILDING_ID, AppUtils.selectedLatLon.longitude,
                AppUtils.selectedLatLon.latitude);
        Disposable disposable = repository.getBuildingID(url)
                .subscribeOn(networkSurveyApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBuilding>() {
                    @Override public void accept(ResponseBuilding response) throws Exception {
                        if(response != null && response.getStatus() != null && response.getStatus().equals("200") && response.getData() != null){
                            myPref.edit().putString(AppUtils.ACTION_BUILDING_ID, response.getData()).commit();
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
