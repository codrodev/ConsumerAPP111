package com.yo4gis.consumersurvey.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.databinding.FragmentSurveyListBinding;
import com.yo4gis.consumersurvey.listeners.CommonResponseNavigator;
import com.yo4gis.consumersurvey.listeners.TaskSelectedListener;
import com.yo4gis.consumersurvey.network.ApiFactory;
import com.yo4gis.consumersurvey.network.NetworkConnectionInterceptor;
import com.yo4gis.consumersurvey.repositories.SurveyListRepository;
import com.yo4gis.consumersurvey.utilities.Global;
import com.yo4gis.consumersurvey.viewModel.SurveyListViewModel;
import com.yo4gis.consumersurvey.views.adapters.SurveyListAdapter;
import com.yo4gis.consumersurvey.views.factories.SurveyListFactory;
import com.yo4gis.consumersurvey.views.listeners.BasicFragmentListener;


public class SurveyListFragment extends Fragment implements CommonResponseNavigator, TaskSelectedListener {
    private FragmentSurveyListBinding binding;
    BasicFragmentListener frListener;
    SurveyListFactory factory;
    SurveyListRepository repository;
    SurveyListViewModel model;
    SurveyListAdapter adapter;

    public SurveyListFragment() {
        // Required empty public constructor
    }

    public static SurveyListFragment newInstance(String param1, String param2) {
        SurveyListFragment fragment = new SurveyListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_survey_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();
    }

    private void initializeView(){
        try {
            repository = new SurveyListRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new SurveyListFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(SurveyListViewModel.class);
        model.navigator = this;
        model.apiSurveyList();
        /*binding.cardTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(binding.getRoot()).navigate(SurveyListFragmentDirections.actionSurveyListFragmentToMapFragment());
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        frListener = (BasicFragmentListener) getActivity();
        frListener.setTitle(getString(R.string.survey_task));
        frListener.isTopBar(true);
    }

    @Override
    public void onSuccess() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        adapter = new SurveyListAdapter(this, Global.getLstAssignedTask());
        binding.recyclerSurveyList.setAdapter(adapter);
        binding.recyclerSurveyList.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onSaveconsumer() {

    }
    @Override
    public void onEmpty(String Msg) {

    }

    @Override
    public void onFailure(String Msg) {

    }

    @Override
    public void onTaskSelected(int position) {
        Global.setSelectedAssignedTask(Global.getLstAssignedTask().get(position));
        Navigation.findNavController(binding.getRoot()).navigate(SurveyListFragmentDirections.actionSurveyListFragmentToMapFragment());
    }
}