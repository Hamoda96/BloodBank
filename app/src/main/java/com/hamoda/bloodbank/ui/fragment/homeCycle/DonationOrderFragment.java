package com.hamoda.bloodbank.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.adapter.SpinnerAdapter;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.data.model.donation.createNewDonation.CreateNewDonation;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hamoda.bloodbank.data.api.RetrofitClient.getClient;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.hamoda.bloodbank.helper.GeneralRequest.getSpinnerData;
import static com.hamoda.bloodbank.helper.HelperMethod.dismissProgressDialog;


public class DonationOrderFragment extends Fragment {


    @BindView(R.id.donation_order_fragment_til_user_name)
    TextInputLayout donationOrderFragmentTilUserName;
    @BindView(R.id.donation_order_fragment_til_age)
    TextInputLayout donationOrderFragmentTilAge;
    @BindView(R.id.donation_order_fragment_sp_blood_types)
    Spinner donationOrderFragmentSpBloodTypes;
    @BindView(R.id.donation_order_fragment_til_how_many_need)
    TextInputLayout donationOrderFragmentTilHowManyNeed;
    @BindView(R.id.donation_order_fragment_sp_governments)
    Spinner donationOrderFragmentSpGovernments;
    @BindView(R.id.donation_order_fragment_sp_city)
    Spinner donationOrderFragmentSpCity;
    @BindView(R.id.donation_order_fragment_ll_container_city)
    LinearLayout donationOrderFragmentLlContainerCity;
    @BindView(R.id.donation_order_fragment_til_notes)
    TextInputLayout donationOrderFragmentTilNotes;
    @BindView(R.id.donation_order_fragment_btn_send)
    Button donationOrderFragmentBtnSend;
    @BindView(R.id.donation_order_fragment_til_phone)
    TextInputLayout donationOrderFragmentTilPhone;
    @BindView(R.id.donation_order_fragment_txt_hospital_address)
    TextView donationOrderFragmentTxtHospitalAddress;
    @BindView(R.id.donation_order_fragment_til_hospital_name)
    TextInputLayout donationOrderFragmentTilHospitalName;

    private DonationFragment donationFragment;

    public static double latitude = 0;
    public static double longitude = 0;

    private SpinnerAdapter bloodTypesAdapter, governmentsAdapter, citiesAdapter;
    private int bloodTypesSelectedId = 0, governmentSelectedId = 0, citiesSelectedId = 0;
    private AdapterView.OnItemSelectedListener listener;

    private NavController navController;
    ClientData clientData;

    public DonationOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_donation, container, false);
        ButterKnife.bind(this, view);

        clientData = loadUserData(getActivity());

        setSpinner();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

    }

    private void getDataclient() {

        String apiToken = clientData.getApiToken();
        String name = donationOrderFragmentTilUserName.getEditText().getText().toString().trim();
        String age = donationOrderFragmentTilAge.getEditText().getText().toString().trim();
        String bagNum = donationOrderFragmentTilHowManyNeed.getEditText().getText().toString().trim();
        String phone = donationOrderFragmentTilPhone.getEditText().getText().toString().trim();
        String note = donationOrderFragmentTilNotes.getEditText().getText().toString().trim();
        String hospitalName = donationOrderFragmentTilHospitalName.getEditText().getText().toString().trim();

        int bloodType = bloodTypesAdapter.selectedId;
        int city = citiesAdapter.selectedId;

        getClient().createDonationRequest(apiToken,name,age,bloodType, bagNum,hospitalName
                , "21 domait street", city, phone, note, 31.7655, 30.7541).enqueue(new Callback<CreateNewDonation>() {
            @Override
            public void onResponse(Call<CreateNewDonation> call, Response<CreateNewDonation> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        donationFragment.donationData.add(0, response.body().getData());
                        donationFragment.donationAdapter.notifyDataSetChanged();
                        dismissProgressDialog();
                        navController.navigate(R.id.action_donationOrderFragment_to_homeFragment);
                    }
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<CreateNewDonation> call, Throwable t) {
                Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // method to get the data to spinner .. -> blood bank + governments + cities
    private void setSpinner() {

        // blood bank
        bloodTypesAdapter = new SpinnerAdapter(getActivity());
        getSpinnerData(getActivity()
                , getClient().getBloodTypes(), donationOrderFragmentSpBloodTypes, bloodTypesAdapter, bloodTypesSelectedId
                , getString(R.string.blood_type), null, true);


        // cities - make listener to governments to show his own cities
        citiesAdapter = new SpinnerAdapter(getActivity());
        listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    getSpinnerData(getActivity()
                            , getClient().getCities(governmentsAdapter.selectedId), donationOrderFragmentSpCity, citiesAdapter, citiesSelectedId
                            , getString(R.string.city), donationOrderFragmentLlContainerCity, true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        //governments
        governmentsAdapter = new SpinnerAdapter(getActivity());
        getSpinnerData(getActivity()
                , getClient().getGovernorates(), donationOrderFragmentSpGovernments, governmentsAdapter, governmentSelectedId
                , getString(R.string.governorate), null, listener);


    }


    @OnClick({R.id.donation_order_fragment_txt_hospital_address, R.id.donation_order_fragment_btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.donation_order_fragment_txt_hospital_address:
                navController.navigate(R.id.action_donationOrderFragment_to_locationFragment);
                break;
            case R.id.donation_order_fragment_btn_send:
                getDataclient();
                break;
        }
    }


}
