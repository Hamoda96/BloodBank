package com.hamoda.bloodbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.hamoda.bloodbank.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;


    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.ic_slider_1,
            R.drawable.ic_slider_2,
            R.drawable.ic_slider_1
    };

    public String[] slide_texts = {
            "هذا النص تجريبي و يمكن استبداله باي نص أخر هذا النص تجريبي و يمكن استبداله باي نص أخر هذا النص تجريبي و يمكن استبداله باي نص أخر ",
            "هذا النص تجريبي و يمكن استبداله باي نص أخر هذا النص تجريبي و يمكن استبداله باي نص أخر هذا النص تجريبي و يمكن استبداله باي نص أخر ",
            "هذا النص تجريبي و يمكن استبداله باي نص أخر هذا النص تجريبي و يمكن استبداله باي نص أخر هذا النص تجريبي و يمكن استبداله باي نص أخر "
    };


    @Override
    public int getCount() {
        return slide_texts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView sliderImage = view.findViewById(R.id.slider_layout_im_images);
        TextView sliderText = view.findViewById(R.id.slider_layout_tv_texts);

        sliderImage.setImageResource(slide_images[position]);
        sliderText.setText(slide_texts[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}

