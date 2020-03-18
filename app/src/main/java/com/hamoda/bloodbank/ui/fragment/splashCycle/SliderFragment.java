package com.hamoda.bloodbank.ui.fragment.splashCycle;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.adapter.SliderAdapter;


public class SliderFragment extends Fragment {

    private ViewPager mSliderViewPager;
    private LinearLayout mDotLayout;
    private SliderAdapter sliderAdapter;
    private ImageButton nextPage;
    private int mCurrentPage;

    private TextView[] mDot;

    public SliderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_slider, container, false);
        mSliderViewPager = view.findViewById(R.id.slider_fragment_vp_view_pager);
        mDotLayout = view.findViewById(R.id.slider_fragment_layout_liner);

        sliderAdapter = new SliderAdapter(getContext());
        mSliderViewPager.setAdapter(sliderAdapter);

        addDotIndicater(0);
        mSliderViewPager.setOnPageChangeListener(viewListner);


        return view;
    }


    // this method make 3 point in the slider fragment
    public void addDotIndicater(int position) {
        mDot = new TextView[3];
        mDotLayout.removeAllViews();

        // make the 3 dot
        for (int i = 0; i < mDot.length; i++) {
            mDot[i] = new TextView(getContext());
            mDot[i].setText(Html.fromHtml("&#8226;"));
            mDot[i].setTextSize(60);
            mDot[i].setTextColor(getResources().getColor(R.color.threeDotInSlider));

            mDotLayout.addView(mDot[i]);
        }

        // this to make the selected dot change color
        if (mDot.length > 0) {
            mDot[position].setTextColor(getResources().getColor(R.color.threeDotInSlider_selected));
        }
    }

    //this for listener when you scroll page the dot change with you
    ViewPager.OnPageChangeListener viewListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotIndicater(i);
            mCurrentPage = i;

         // changing the next button text 'NEXT' / 'GOT IT'
            if (i == mDot.length - 1) {
                // last page. make button text to GOT IT
                nextPage.setEnabled(true);
                nextPage.setImageResource(R.drawable.ic_finish);

            } else {
                // still pages are left
                nextPage.setEnabled(true);
                nextPage.setImageResource(R.drawable.ic_arrow_right);


            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);


        nextPage = view.findViewById(R.id.slider_fragment_imb_next);

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // checking for last page
                // if last page home screen will be launched
                int current = mCurrentPage +1;

                if (current < mDot.length) {
                    // move to next screen
                    mSliderViewPager.setCurrentItem(current);
                } else {
                    navController.navigate(R.id.action_sliderFragment_to_loginFragment);
                }
            }
        });

    }


}
