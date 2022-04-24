package com.yo4gis.consumersurvey.views.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.yo4gis.consumersurvey.repositories.SurveyListRepository;
import com.yo4gis.consumersurvey.viewModel.SurveyListViewModel;


public class SurveyListFactory extends ViewModelProvider.NewInstanceFactory {
    private SurveyListRepository repository;
    private Activity appContext;

    public SurveyListFactory(Activity context, SurveyListRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new SurveyListViewModel(appContext, repository);
    }
}