package com.yo4gis.consumersurvey.viewModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.yo4gis.consumersurvey.NetworkSurveyApp;
import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.listeners.CommonResponseNavigator;
import com.yo4gis.consumersurvey.model.PostBuilding;
import com.yo4gis.consumersurvey.model.PostLogin;
import com.yo4gis.consumersurvey.model.ResponseBuilding;
import com.yo4gis.consumersurvey.model.ResponseLogin;
import com.yo4gis.consumersurvey.repositories.BuildingRepository;
import com.yo4gis.consumersurvey.repositories.LoginRepository;
import com.yo4gis.consumersurvey.utilities.AppUtils;
import com.yo4gis.consumersurvey.utilities.Global;
import com.yo4gis.consumersurvey.views.fragments.AlertDialogUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BuildingViewModel extends ViewModel {

    private NetworkSurveyApp networkSurveyApp;
    BuildingRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public CommonResponseNavigator navigator;


    public BuildingViewModel(Activity activity, BuildingRepository repository){
        this.activity = activity;
        this.repository = repository;
        networkSurveyApp = NetworkSurveyApp.create(activity);
    }

    public void apiBuilding(PostBuilding model){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);

        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        progressBar.setContentView(R.layout.progress_layout);

        String url = AppUtils.TOKEN;
        String s = new Gson().toJson(model);
        System.out.println("data"+s);
        Disposable disposable = repository.postBuildingAPI(url, model)
                .subscribeOn(networkSurveyApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBuilding>() {
                    @Override public void accept(ResponseBuilding response) throws Exception {
                        if(response != null && response.getStatus() != null && response.getStatus().equals("200") && response.getData() != null){
                            navigator.onSuccess();
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

}
