package com.yo4gis.consumersurvey.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.databinding.FragmentHomeBinding;
import com.yo4gis.consumersurvey.utilities.Global;
import com.yo4gis.consumersurvey.views.listeners.BasicFragmentListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    BasicFragmentListener frListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();
    }

    private void initializeView(){
        binding.txtName.setText(Global.getUsername());
        binding.layoutConsumerSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.lstPoleLatLngs = new ArrayList<>();
                Navigation.findNavController(binding.getRoot()).navigate(HomeFragmentDirections.actionHomeFragmentToSurveyListFragment());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        frListener = (BasicFragmentListener) getActivity();
        frListener.setTitle(getString(R.string.welcome) + " ");
        frListener.isTopBar(true);
    }
}