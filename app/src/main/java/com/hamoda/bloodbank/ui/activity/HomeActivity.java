package com.hamoda.bloodbank.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hamoda.bloodbank.R;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.home_activity_container_bn_bottomNavigation);
        navController = Navigation.findNavController(this, R.id.fragment2);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


    }

}
