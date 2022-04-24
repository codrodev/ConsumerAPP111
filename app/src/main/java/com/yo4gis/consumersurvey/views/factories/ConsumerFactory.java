package com.yo4gis.consumersurvey.views.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.yo4gis.consumersurvey.repositories.BuildingRepository;
import com.yo4gis.consumersurvey.repositories.ConsumerRepository;
import com.yo4gis.consumersurvey.viewModel.BuildingViewModel;
import com.yo4gis.consumersurvey.viewModel.ConsumerViewModel;


public class ConsumerFactory extends ViewModelProvider.NewInstanceFactory {
    private ConsumerRepository repository;
    private Activity appContext;

    public ConsumerFactory(Activity context, ConsumerRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new ConsumerViewModel(appContext, repository);
    }
}