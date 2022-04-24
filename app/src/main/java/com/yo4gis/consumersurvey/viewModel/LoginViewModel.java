package com.yo4gis.consumersurvey.viewModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.yo4gis.consumersurvey.NetworkSurveyApp;
import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.listeners.CommonResponseNavigator;
import com.yo4gis.consumersurvey.model.PostLogin;
import com.yo4gis.consumersurvey.model.ResponseLogin;
import com.yo4gis.consumersurvey.repositories.LoginRepository;
import com.yo4gis.consumersurvey.utilities.AppUtils;
import com.yo4gis.consumersurvey.utilities.Global;
import com.yo4gis.consumersurvey.views.fragments.AlertDialogUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class LoginViewModel extends ViewModel {

    private NetworkSurveyApp networkSurveyApp;
    LoginRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public CommonResponseNavigator navigator;


    public LoginViewModel(Activity activity, LoginRepository repository){
        this.activity = activity;
        this.repository = repository;
        networkSurveyApp = NetworkSurveyApp.create(activity);
    }

    public void apiLogin(PostLogin model){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);

        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //progressBar.setIndeterminateDrawable(activity.getResources().getDrawable(R.drawable.custom_progress));
        progressBar.show();
        progressBar.setContentView(R.layout.progress_layout);

        String url = AppUtils.TOKEN;
        String s = new Gson().toJson(model);
        Disposable disposable = repository.postLoginAPI(url, model)
                .subscribeOn(networkSurveyApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseLogin>() {
                    @Override public void accept(ResponseLogin response) throws Exception {
                        if(response != null && response.getAccessToken() != null && response.getAccessToken().length() > 0){
                            progressBar.dismiss();
                            Global.setAccessToken("bearer" + " " + response.getAccessToken());
                            Global.setUsername(model.getUsername());
                            navigator.onSuccess();
                        } else {
                            AlertDialogUtil.alertDialog(networkSurveyApp.getResources().getString(R.string.error),
                                    networkSurveyApp.getResources().getString(R.string.api_error), activity);
                            AlertDialogUtil.btnOk.setText(networkSurveyApp.getResources().getString(R.string.ok));
                        }
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
