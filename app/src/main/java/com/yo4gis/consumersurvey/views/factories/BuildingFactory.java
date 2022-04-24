package com.yo4gis.consumersurvey.views.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.yo4gis.consumersurvey.repositories.BuildingRepository;
import com.yo4gis.consumersurvey.repositories.LoginRepository;
import com.yo4gis.consumersurvey.viewModel.BuildingViewModel;
import com.yo4gis.consumersurvey.viewModel.LoginViewModel;


public class BuildingFactory extends ViewModelProvider.NewInstanceFactory {
    private BuildingRepository repository;
    private Activity appContext;

    public BuildingFactory(Activity context, BuildingRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new BuildingViewModel(appContext, repository);
    }
}