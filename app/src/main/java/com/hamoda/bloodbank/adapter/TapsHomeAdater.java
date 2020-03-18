package com.hamoda.bloodbank.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hamoda.bloodbank.ui.fragment.homeCycle.article.ArticlesFragment;
import com.hamoda.bloodbank.ui.fragment.homeCycle.DonationFragment;

public class TapsHomeAdater extends FragmentStatePagerAdapter {


    public TapsHomeAdater(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new ArticlesFragment();
        } else {
            return new DonationFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1) {
            return "المقالات";
        } else {
            return "طلبات التبرع";
        }
    }
}
