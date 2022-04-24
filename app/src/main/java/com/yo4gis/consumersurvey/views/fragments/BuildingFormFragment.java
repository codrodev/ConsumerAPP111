package com.yo4gis.consumersurvey.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.databinding.FragmentBuildingFormBinding;
import com.yo4gis.consumersurvey.listeners.CommonResponseNavigator;
import com.yo4gis.consumersurvey.model.PostBuilding;
import com.yo4gis.consumersurvey.model.PostLogin;
import com.yo4gis.consumersurvey.network.ApiFactory;
import com.yo4gis.consumersurvey.network.NetworkConnectionInterceptor;
import com.yo4gis.consumersurvey.repositories.BuildingRepository;
import com.yo4gis.consumersurvey.repositories.LoginRepository;
import com.yo4gis.consumersurvey.utilities.Global;
import com.yo4gis.consumersurvey.viewModel.BuildingViewModel;
import com.yo4gis.consumersurvey.viewModel.LoginViewModel;
import com.yo4gis.consumersurvey.views.factories.BuildingFactory;
import com.yo4gis.consumersurvey.views.factories.LoginFactory;
import com.yo4gis.consumersurvey.views.listeners.BasicFragmentListener;

import java.util.ArrayList;
import java.util.List;

public class BuildingFormFragment extends Fragment implements CommonResponseNavigator {
    private FragmentBuildingFormBinding binding;
    BasicFragmentListener frListener;
    BuildingFactory factory;
    BuildingRepository repository;
    BuildingViewModel model;
    public BuildingFormFragment() {
        // Required empty public constructor
    }

    public static BuildingFormFragment newInstance(String param1, String param2) {
        BuildingFormFragment fragment = new BuildingFormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_building_form, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();

    }


    private void initializeView(){
        try {
            repository = new BuildingRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new BuildingFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(BuildingViewModel.class);
        model.navigator = this;
        initializeDropDown();
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.apiBuilding(getPostBuilding());
                //Navigation.findNavController(binding.getRoot()).navigate(SignInFragmentDirections.actionSignInFragmentToHomeFragment());
            }
        });
    }
    private void initializeDropDown() {
        ArrayAdapter<CharSequence> adapterStudPole =
                ArrayAdapter.createFromResource(getActivity(), R.array.building_type, android.R.layout.simple_spinner_item);
        adapterStudPole.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding.spinType.setAdapter(adapterStudPole);
    }
    private PostBuilding getPostBuilding(){
        List<LatLng> arrayPolygonPoints= Global.getArrayPolygonPoints();
        StringBuilder str = new StringBuilder("");
        for (LatLng eachstring : arrayPolygonPoints) {
            double lan=eachstring.longitude;
            double lat=eachstring.latitude;
            String finalvalue=String.valueOf(lan)+","+String.valueOf(lat);

            str.append(finalvalue).append("|");
        }
        String pipeseparatedlist = str.toString().replace("lat/lng: (","").replace(")","");
        if (pipeseparatedlist.length() > 0)
            pipeseparatedlist= pipeseparatedlist.substring( 0, pipeseparatedlist.length() - 1);
        PostBuilding buildingObject = new PostBuilding();
        buildingObject.setNin_id(Global.getSelectedAssignedTask().getDtr_nin());
        buildingObject.setSubstation_name(Global.getSelectedAssignedTask().getSubstation11());
        buildingObject.setBuildingGeomString(pipeseparatedlist);
        buildingObject.setBld_name(binding.editBuildingName.getText().toString());
        buildingObject.setAddress(binding.editBuildingAddress.getText().toString());
        buildingObject.setBld_type(binding.spinType.getSelectedItem().toString());
        buildingObject.setBld_num(binding.editBuildingNumber.getText().toString());
        buildingObject.setBld_r_num(binding.editBuildingRefNumber.getText().toString());
        buildingObject.setCity(Global.getSelectedAssignedTask().getTown());
        buildingObject.setCon_status(binding.editBuildingContructionStatus.getText().toString());
        buildingObject.setDist(binding.editBuildingDistric.getText().toString());
        buildingObject.setNo_of_conn(binding.editBuildingNoofConnection.getText().toString());
        buildingObject.setNo_if_flat(binding.editBuildingNoofflat.getText().toString());
        buildingObject.setNo_of_floor(binding.editBuildingNooffloor.getText().toString());
        buildingObject.setNo_of_shop(binding.editBuildingNoofshop.getText().toString());
        buildingObject.setPincode(binding.editBuildingPincode.getText().toString());
        buildingObject.setSoc_name(binding.editBuildingSocityname.getText().toString());
        buildingObject.setSub_loc(binding.editBuildingSublocality.getText().toString());
        buildingObject.setCreatedBy(Global.getUsername());
        buildingObject.setCreatedDate(Global.getCurrentDateAndTime());
        return buildingObject;
    }
    @Override
    public void onResume() {
        super.onResume();
        frListener = (BasicFragmentListener) getActivity();
        frListener.setTitle("Building Create");
        frListener.isTopBar(true);
    }

    @Override
    public void onSuccess() {
        Navigation.findNavController(binding.getRoot()).navigate(BuildingFormFragmentDirections.actionBuildingFormFragment());
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
}