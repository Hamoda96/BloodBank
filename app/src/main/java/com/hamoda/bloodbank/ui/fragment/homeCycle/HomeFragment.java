package com.hamoda.bloodbank.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.adapter.TapsHomeAdater;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

    TapsHomeAdater tapsHomeAdater;

    @BindView(R.id.home_fragment_tl_tabLayout)
    TabLayout homeFragmentTlTabLayout;
    @BindView(R.id.home_fragment_vp_viewPager)
    ViewPager homeFragmentVpViewPager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        tapsHomeAdater = new TapsHomeAdater(getChildFragmentManager());
        homeFragmentVpViewPager.setAdapter(tapsHomeAdater);
        homeFragmentTlTabLayout.setupWithViewPager(homeFragmentVpViewPager);

        return view;

    }
}
