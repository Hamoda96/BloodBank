package com.hamoda.bloodbank.ui.fragment.splashCycle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.ui.activity.HomeActivity;

import static com.hamoda.bloodbank.helper.Constants.REMEMBER;
import static com.hamoda.bloodbank.helper.Constants.SIGNOUT;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.LoadBoolean;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.SaveData;


public class    SplashFragment extends Fragment {

    Handler handler;
    private int timeSplash = 2500;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_splash, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LoadBoolean(getActivity(), REMEMBER)) {
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                } else if (LoadBoolean(getActivity(), SIGNOUT)) {
                    SaveData(getActivity(), SIGNOUT, false);
                    navController.navigate(R.id.action_splashFragment_to_loginFragment);

                } else {
                    navController.navigate(R.id.action_splashFragment_to_sliderFragment);
                }
            }
        }, timeSplash);

    }
}
