package com.yo4gis.consumersurvey.viewModel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.ViewModel;

import com.yo4gis.consumersurvey.NetworkSurveyApp;
import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.listeners.CommonResponseNavigator;
import com.yo4gis.consumersurvey.model.AssignedTask;
import com.yo4gis.consumersurvey.model.ResponseAssignedTaskList;
import com.yo4gis.consumersurvey.repositories.SurveyListRepository;
import com.yo4gis.consumersurvey.utilities.AppUtils;
import com.yo4gis.consumersurvey.utilities.Global;
import com.yo4gis.consumersurvey.views.fragments.AlertDialogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class SurveyListViewModel extends ViewModel {

    private NetworkSurveyApp networkSurveyApp;
    SurveyListRepository repository;
    Activity activity;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public CommonResponseNavigator navigator;


    public SurveyListViewModel(Activity activity, SurveyListRepository repository){
        this.activity = activity;
        this.repository = repository;
        networkSurveyApp = NetworkSurveyApp.create(activity);
    }

    public void apiSurveyList(){
        final ProgressDialog progressBar = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
        progressBar.setCancelable(true);

        progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressBar.show();
        progressBar.setContentView(R.layout.progress_layout);

        String url = AppUtils.GET_ASSIGN_TASK_BY_USER + Global.getUsername();
        Disposable disposable = repository.getAssignedTask(url)
                .subscribeOn(networkSurveyApp.subscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseAssignedTaskList>() {
                    @Override public void accept(ResponseAssignedTaskList response) throws Exception {
                        if(response != null && response.getStatus() != null && response.getStatus().equals("200") &&
                            response.getData() != null && response.getData().size() > 0){
                            List<AssignedTask> lstAssignedTask =new ArrayList<>();
                            for(int i =0;i<response.getData().size();i++)
                            {
                                String surveytype=response.getData().get(i).getSurveyType();
                                if(surveytype.equalsIgnoreCase("C"))
                                {
                                    AssignedTask assign=response.getData().get(i);
                                    lstAssignedTask.add(assign);
                                    Global.setLstAssignedTask(lstAssignedTask);
                                }

                            }

                            progressBar.dismiss();
                            navigator.onSuccess();
                        } else {
                            progressBar.dismiss();
                            AlertDialogUtil.alertDialog(networkSurveyApp.getResources().getString(R.string.no_task),
                                    networkSurveyApp.getResources().getString(R.string.no_task_assigned), activity);
                            AlertDialogUtil.btnOk.setText(networkSurveyApp.getResources().getString(R.string.ok));
                            navigator.onEmpty("");
                        }
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
