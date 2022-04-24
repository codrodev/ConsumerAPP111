package com.yo4gis.consumersurvey.views.factories;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.yo4gis.consumersurvey.repositories.MapRepository;
import com.yo4gis.consumersurvey.viewModel.MapViewModel;


public class MapFactory extends ViewModelProvider.NewInstanceFactory {
    private MapRepository repository;
    private Activity appContext;

    public MapFactory(Activity context, MapRepository repository) {
        this.repository = repository;
        this.appContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        return (T) new MapViewModel(appContext, repository);
    }
}