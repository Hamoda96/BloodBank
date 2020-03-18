package com.hamoda.bloodbank.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.helper.HelperMethod;


public class HomeContainerFragment extends Fragment {

    NavController navController;
    BottomNavigationView bottomNavigationView;

    public HomeContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_container, container, false);

        //NavBottom dec

        HomeFragment homeFragment = new HomeFragment();
        HelperMethod.replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_fragment_container_fl_container, homeFragment);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

    }
//


}
