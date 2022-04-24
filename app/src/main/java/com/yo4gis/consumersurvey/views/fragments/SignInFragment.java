package com.yo4gis.consumersurvey.views.fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.yo4gis.consumersurvey.R;
import com.yo4gis.consumersurvey.databinding.FragmentSignInBinding;
import com.yo4gis.consumersurvey.listeners.CommonResponseNavigator;
import com.yo4gis.consumersurvey.model.PostLogin;
import com.yo4gis.consumersurvey.network.ApiFactory;
import com.yo4gis.consumersurvey.network.NetworkConnectionInterceptor;
import com.yo4gis.consumersurvey.repositories.LoginRepository;
import com.yo4gis.consumersurvey.utilities.Global;
import com.yo4gis.consumersurvey.viewModel.LoginViewModel;
import com.yo4gis.consumersurvey.views.factories.LoginFactory;
import com.yo4gis.consumersurvey.views.listeners.BasicFragmentListener;


public class SignInFragment extends Fragment implements CommonResponseNavigator {
    private FragmentSignInBinding binding;
    BasicFragmentListener frListener;
    LoginFactory factory;
    LoginRepository repository;
    LoginViewModel model;

    public SignInFragment() {
        // Required empty public constructor
    }

    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initializeView();
    }

    private void initializeView(){
        try {
            repository = new LoginRepository(ApiFactory.getClient(new NetworkConnectionInterceptor(getActivity())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        factory = new LoginFactory(getActivity(), repository);
        model = ViewModelProviders.of(this, factory).get(LoginViewModel.class);
        model.navigator = this;
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.apiLogin(getPostLogin());
                //Navigation.findNavController(binding.getRoot()).navigate(SignInFragmentDirections.actionSignInFragmentToHomeFragment());
            }
        });
    }

    private PostLogin getPostLogin(){
        PostLogin loginObject = new PostLogin();
        loginObject.setUsername(binding.editUserName.getEditText().getText().toString());
        loginObject.setPassword(binding.editPassword.getEditText().getText().toString());
        loginObject.setGrantType("password");

        return loginObject;
    }

    private void observeViewModel() {

    }

    @Override
    public void onResume() {
        super.onResume();
        frListener = (BasicFragmentListener) getActivity();
        frListener.isTopBar(false);
    }

    @Override
    public void onSuccess() {
        Navigation.findNavController(binding.getRoot()).navigate(SignInFragmentDirections.actionSignInFragmentToHomeFragment());
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