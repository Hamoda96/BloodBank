package com.hamoda.bloodbank.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.data.model.donation.donationDetails.DonationDetails;
import com.hamoda.bloodbank.data.model.donation.donationDetails.DonationDetailsData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hamoda.bloodbank.data.api.RetrofitClient.getClient;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.loadUserData;


public class DonationDetailFragment extends Fragment {

    public int id;
    @BindView(R.id.donation_detail_tv_name)
    TextView donationDetailTvName;
    @BindView(R.id.donation_detail_tv_age)
    TextView donationDetailTvAge;
    @BindView(R.id.donation_detail_tv_blood_type)
    TextView donationDetailTvBloodType;
    @BindView(R.id.donation_detail_tv_num_bags)
    TextView donationDetailTvNumBags;
    @BindView(R.id.donation_detail_tv_hospital_name)
    TextView donationDetailTvHospitalName;
    @BindView(R.id.donation_detail_tv_hospital_address)
    TextView donationDetailTvHospitalAddress;
    @BindView(R.id.donation_detail_tv_phone)
    TextView donationDetailTvPhone;
    @BindView(R.id.donation_detail_tv_notes)
    TextView donationDetailTvNotes;


    private ClientData clientData;
    private DonationDetailsData detailsData;

    public DonationDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation_detail, container, false);

        ButterKnife.bind(this, view);

        id =getArguments().getInt("id");

        clientData = loadUserData(getActivity());

        getData();
        return view;
    }

    private void getData() {
        getClient().displayDonationDetails(clientData.getApiToken(), id).enqueue(new Callback<DonationDetails>() {
            @Override
            public void onResponse(Call<DonationDetails> call, Response<DonationDetails> response) {
                try {
                       detailsData = response.body().getData();
                       setData();
                } catch (Exception e) {
                }


            }

            @Override
            public void onFailure(Call<DonationDetails> call, Throwable t) {
                Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
                donationDetailTvName.setText(detailsData.getPatientName());
                donationDetailTvAge.setText(detailsData.getPatientAge());
                donationDetailTvBloodType.setText(detailsData.getBloodType().getName());
                donationDetailTvNumBags.setText(detailsData.getBagsNum());
                donationDetailTvHospitalName.setText(detailsData.getHospitalName());
                donationDetailTvHospitalAddress.setText(detailsData.getHospitalAddress());
                donationDetailTvPhone.setText(detailsData.getPhone());
                donationDetailTvNotes.setText(detailsData.getNotes());
    }


    @OnClick(R.id.donation_detail_fragment_btn_call)
    public void onViewClicked() {
    }
}
