package com.hamoda.bloodbank.ui.fragment.loginCycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.material.textfield.TextInputLayout;
import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.adapter.SpinnerAdapter;
import com.hamoda.bloodbank.data.model.client.Client;
import com.hamoda.bloodbank.helper.DateTxt;
import com.hamoda.bloodbank.ui.activity.HomeActivity;

import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.hamoda.bloodbank.data.api.RetrofitClient.getClient;
import static com.hamoda.bloodbank.helper.Constants.PASSWORD;
import static com.hamoda.bloodbank.helper.GeneralRequest.getSpinnerData;
import static com.hamoda.bloodbank.helper.HelperMethod.disappearKeypad;
import static com.hamoda.bloodbank.helper.HelperMethod.showCalender;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.SaveData;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.saveUserData;


public class RegisterFragment extends Fragment {


    @BindView(R.id.registers_fragment_til_user_name)
    TextInputLayout registersFragmentTilUserName;
    @BindView(R.id.registers_fragment_til_email)
    TextInputLayout registersFragmentTilEmail;
    @BindView(R.id.registers_fragment_tv_birthday)
    TextView registersFragmentTvBirthday;
    @BindView(R.id.registers_fragment_til_birthday)
    TextInputLayout registersFragmentTilBirthday;
    @BindView(R.id.registers_fragment_tv_last_donation_date)
    TextView registersFragmentTvLastDonationDate;
    @BindView(R.id.registers_fragment_til_last_donation_date)
    TextInputLayout registersFragmentTilLastDonationDate;
    @BindView(R.id.registers_fragment_ll_container_city)
    LinearLayout registersFragmentLlContainerCity;
    @BindView(R.id.registers_fragment_til_phone)
    TextInputLayout registersFragmentTilPhone;
    @BindView(R.id.registers_fragment_til_password)
    TextInputLayout registersFragmentTilPassword;
    @BindView(R.id.registers_fragment_til_confirm_password)
    TextInputLayout registersFragmentTilConfirmPassword;
    @BindView(R.id.registers_fragment_sp_blood_types)
    Spinner registersFragmentSpBloodTypes;
    @BindView(R.id.registers_fragment_sp_governments)
    Spinner registersFragmentSpGovernments;
    @BindView(R.id.registers_fragment_sp_city)
    Spinner registersFragmentSpCity;

    private SpinnerAdapter bloodTypesAdapter, governmentsAdapter, citiesAdapter;
    private int bloodTypesSelectedId = 0, governmentSelectedId = 0, citiesSelectedId = 0;
    private AdapterView.OnItemSelectedListener listener;

    NavController navController;
    private DateTxt birthdayDate, lastDonationDate;

    AwesomeValidation awesomeValidation ;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        ButterKnife.bind(this, view);

        setDates();

        setSpinner();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }


    // method to get the data to spinner .. -> blood bank + governments + cities
    private void setSpinner() {

        // blood bank
        bloodTypesAdapter = new SpinnerAdapter(getActivity());
        getSpinnerData(getActivity()
                , getClient().getBloodTypes(), registersFragmentSpBloodTypes, bloodTypesAdapter, bloodTypesSelectedId
                , getString(R.string.blood_type), null,false);


        // cities - make listener to governments to show his own cities
        citiesAdapter = new SpinnerAdapter(getActivity());
        listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    getSpinnerData(getActivity()
                            , getClient().getCities(governmentsAdapter.selectedId), registersFragmentSpCity, citiesAdapter, citiesSelectedId
                            , getString(R.string.city), registersFragmentLlContainerCity,false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        //governments
        governmentsAdapter = new SpinnerAdapter(getActivity());
        getSpinnerData(getActivity()
                , getClient().getGovernorates(), registersFragmentSpGovernments, governmentsAdapter, governmentSelectedId
                , getString(R.string.governorate), null, listener);


    }

    private void onSignUp() {

        String name = registersFragmentTilUserName.getEditText().getText().toString();
        String email = registersFragmentTilEmail.getEditText().getText().toString();
        String phone = registersFragmentTilPhone.getEditText().getText().toString();
        String password = registersFragmentTilPassword.getEditText().getText().toString();
        String confirmPassword = registersFragmentTilConfirmPassword.getEditText().getText().toString();
        String birthday = registersFragmentTvBirthday.getText().toString();
        String lastDonation = registersFragmentTvLastDonationDate.getText().toString();

        int cityId = citiesAdapter.selectedId;
        int bloodTypeId = bloodTypesAdapter.selectedId;

        getClient().onSignUp(name, email, birthday, cityId, phone,
                lastDonation, password, confirmPassword, bloodTypeId).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        SaveData(getActivity(),PASSWORD,password);
                        saveUserData(getActivity(),response.body().getData());

                        Intent intent =new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);

                        Toast.makeText(getActivity(), "xxxxxxxx", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                Toast.makeText(getActivity(), "ffffffff", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setDates() {
        DecimalFormat mFormat = new DecimalFormat("00");
        Calendar calander = Calendar.getInstance();
        String cDay = mFormat.format(Double.valueOf(String.valueOf(calander.get(Calendar.DAY_OF_MONTH))));
        String cMonth = mFormat.format(Double.valueOf(String.valueOf(calander.get(Calendar.MONTH + 1))));
        String cYear = String.valueOf(calander.get(Calendar.YEAR));

        lastDonationDate = new DateTxt(cDay, cMonth, cYear, cDay + "-" + cMonth + "-" + cYear);
        birthdayDate = new DateTxt("01", "01", "1990", "01-01-1990");

    }

    @OnClick({R.id.registers_fragment_tv_birthday, R.id.registers_fragment_tv_last_donation_date, R.id.registers_fragment_btn_register})
    public void onViewClicked(View view) {

        // static method from HelperMethod to make keypad disappear
        disappearKeypad(getActivity(), view);

        switch (view.getId()) {
            case R.id.registers_fragment_tv_birthday:
                showCalender(getActivity(), getString(R.string.birthday), registersFragmentTvBirthday, birthdayDate);
//                showCalender(getActivity(), registersFragmentTvBirthday);
                break;
            case R.id.registers_fragment_tv_last_donation_date:
                showCalender(getActivity(), getString(R.string.last_donation), registersFragmentTvLastDonationDate, lastDonationDate);
                break;
            case R.id.registers_fragment_btn_register:
                onSignUp();
                break;
        }
    }







}
