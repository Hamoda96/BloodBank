package com.hamoda.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.data.model.donation.donationRequests.DonationRequestsData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.loadUserData;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {

    Context context;
    AppCompatActivity activity;
    List<DonationRequestsData> donationData = new ArrayList<>();
    ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    ClientData clientData;
   private NavController navController;


    public DonationAdapter(Activity activity, List<DonationRequestsData> donationData, NavController navController) {
        this.context = activity;
        this.activity = (AppCompatActivity) activity;
        this.donationData = donationData;
        clientData = loadUserData(activity);
        this.navController = navController;
    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DonationViewHolder(LayoutInflater.from(context).inflate(R.layout.item_donation,
                parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        setData(holder, position);
        setItemSwipe(holder, position);
        holder.position = position;
    }


    private void setData(DonationViewHolder holder, int position) {

        holder.donationAdapterTvName.setText(activity.getString(R.string.patient_name) + ":"
                + donationData.get(position).getClient().getName());

        holder.donationAdapterTvHospital.setText(activity.getString(R.string.hospital) + ":"
                + donationData.get(position).getHospitalAddress());

        holder.donationAdapterTvCity.setText(activity.getString(R.string.cityd) + ":" + donationData.get(position).getCity().getName());

        holder.donationAdapterTvBloodType.setText(donationData.get(position).getBloodType().getName());

    }

    private void setItemSwipe(DonationViewHolder holder, int position) {

        holder.donationAdapterSrlItemSwipe.computeScroll();

        viewBinderHelper.bind(holder.donationAdapterSrlItemSwipe, String.valueOf(donationData.get(position).getId()));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewBinderHelper.openLayout(String.valueOf(donationData.get(position).getId()));
                holder.donationAdapterSrlItemSwipe.computeScroll();
            }
        });


    }


    @Override
    public int getItemCount() {
        return donationData.size();
    }

    public class DonationViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.donation_adapter_tv_call)
        ImageButton donationAdapterTvCall;
        @BindView(R.id.donation_adapter_tv_info)
        ImageButton donationAdapterTvInfo;
        @BindView(R.id.donation_adapter_tv_name)
        TextView donationAdapterTvName;
        @BindView(R.id.donation_adapter_tv_hospital)
        TextView donationAdapterTvHospital;
        @BindView(R.id.donation_adapter_tv_city)
        TextView donationAdapterTvCity;
        @BindView(R.id.donation_adapter_tv_blood_type)
        TextView donationAdapterTvBloodType;
        @BindView(R.id.donation_adapter_srl_item_swipe)
        SwipeRevealLayout donationAdapterSrlItemSwipe;


        private View view;
        private int position;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;

                }
            });
        }

        @OnClick({R.id.donation_adapter_tv_call, R.id.donation_adapter_tv_info})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.donation_adapter_tv_call:
                    break;
                case R.id.donation_adapter_tv_info:
//                    DonationDetailFragment donationDetailFragment = new DonationDetailFragment();
//                    donationDetailFragment.id = donationData.get(position).getId();
//                    HelperMethod.replaceFragment(activity.getSupportFragmentManager(), R.id.home_activity_container_f_frame_container, donationDetailFragment);

                    Bundle bundle = new Bundle();
                    bundle.putInt("id",donationData.get(position).getId());
                    navController.navigate(R.id.action_homeFragment_to_donationDetailFragment,bundle);

                    break;
            }
        }


    }
}
