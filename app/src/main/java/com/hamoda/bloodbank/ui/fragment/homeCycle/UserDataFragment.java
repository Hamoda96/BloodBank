package com.hamoda.bloodbank.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.adapter.SpinnerAdapter;
import com.hamoda.bloodbank.data.model.client.Client;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.helper.DateTxt;

import java.text.DecimalFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hamoda.bloodbank.data.api.RetrofitClient.getClient;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.LoadData;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.SaveData;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.saveUserData;
import static com.hamoda.bloodbank.helper.Constants.PASSWORD;
import static com.hamoda.bloodbank.helper.GeneralRequest.getSpinnerData;
import static com.hamoda.bloodbank.helper.HelperMethod.dismissProgressDialog;
import static com.hamoda.bloodbank.helper.HelperMethod.showCalender;
import static com.hamoda.bloodbank.helper.HelperMethod.showProgressDialog;


public class UserDataFragment extends Fragment {


    @BindView(R.id.edit_user_data_fragment_til_user_name)
    TextInputLayout editUserDataFragmentTilUserName;
    @BindView(R.id.edit_user_data_fragment_til_email)
    TextInputLayout editUserDataFragmentTilEmail;
    @BindView(R.id.edit_user_data_fragment_tv_birthday)
    TextView editUserDataFragmentTvBirthday;
    @BindView(R.id.edit_user_data_fragment_til_birthday)
    TextInputLayout editUserDataFragmentTilBirthday;
    @BindView(R.id.edit_user_data_fragment_tv_last_donation_date)
    TextView editUserDataFragmentTvLastDonationDate;
    @BindView(R.id.edit_user_data_fragment_til_last_donation_date)
    TextInputLayout editUserDataFragmentTilLastDonationDate;
    @BindView(R.id.edit_user_data_fragment_sp_blood_types)
    Spinner editUserDataFragmentSpBloodTypes;
    @BindView(R.id.edit_user_data_fragment_sp_governments)
    Spinner editUserDataFragmentSpGovernments;
    @BindView(R.id.edit_user_data_fragment_sp_city)
    Spinner editUserDataFragmentSpCity;
    @BindView(R.id.edit_user_data_fragment_ll_container_city)
    LinearLayout editUserDataFragmentLlContainerCity;
    @BindView(R.id.edit_user_data_fragment_til_phone)
    TextInputLayout editUserDataFragmentTilPhone;
    @BindView(R.id.edit_user_data_fragment_til_password)
    TextInputLayout editUserDataFragmentTilPassword;
    @BindView(R.id.edit_user_data_fragment_til_confirm_password)
    TextInputLayout editUserDataFragmentTilConfirmPassword;

    private ClientData clientData;
    private int bloodTypesSelectedId = 0, governmentSelectedId = 0, citiesSelectedId = 0;
    private SpinnerAdapter bloodTypesAdapter, governmentsAdapter, citiesAdapter;
    private AdapterView.OnItemSelectedListener listener;
    private DateTxt birthdayDate, lastDonationDate;


    public UserDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_data, container, false);

        ButterKnife.bind(this, view);

        // load the user data from SharedPreferences to show it
        clientData = loadUserData(getActivity());

        showData();

        setDates();

        getSpinner();

        return view;
    }

    private void editData() {
        String name = editUserDataFragmentTilUserName.getEditText().getText().toString();
        String email = editUserDataFragmentTilEmail.getEditText().getText().toString();
        String phone = editUserDataFragmentTilPhone.getEditText().getText().toString();
        String password = editUserDataFragmentTilPassword.getEditText().getText().toString();
        String confirmPassword = editUserDataFragmentTilConfirmPassword.getEditText().getText().toString();
        String birthday = editUserDataFragmentTvBirthday.getText().toString();
        String lastDonation = editUserDataFragmentTvLastDonationDate.getText().toString();

        int cityId = citiesAdapter.selectedId;
        int bloodTypeId = bloodTypesAdapter.selectedId;

        getClient().editProfileData(name
                , email
                , birthday
                , cityId
                , phone
                , lastDonation
                , password
                , confirmPassword
                , bloodTypeId
                , clientData.getApiToken()).enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {

                SaveData(getActivity(),PASSWORD,password);
                saveUserData(getActivity(), response.body().getData());

                if (response.body().getStatus()==1) {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), getString(R.string.edited_successfully), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(getActivity(), getString(R.string.not_successfully), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // method to get the data to spinner .. -> blood bank + governments + cities
    private void getSpinner() {

        // blood bank
        bloodTypesAdapter = new SpinnerAdapter(getActivity());
        getSpinnerData(getActivity()
                , getClient().getBloodTypes(), editUserDataFragmentSpBloodTypes, bloodTypesAdapter, bloodTypesSelectedId
                , getString(R.string.blood_type), null,true);


        // cities - make listener to governments to show his own cities
        citiesAdapter = new SpinnerAdapter(getActivity());
        listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    getSpinnerData(getActivity()
                            , getClient().getCities(governmentsAdapter.selectedId), editUserDataFragmentSpCity, citiesAdapter, citiesSelectedId
                            , getString(R.string.city), editUserDataFragmentLlContainerCity,true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        //governments
        governmentsAdapter = new SpinnerAdapter(getActivity());
        getSpinnerData(getActivity()
                , getClient().getGovernorates(), editUserDataFragmentSpGovernments, governmentsAdapter, governmentSelectedId
                , getString(R.string.governorate), null, listener);


    }

    private void showData() {
        bloodTypesSelectedId = clientData.getClient().getBloodType().getId();
        governmentSelectedId = clientData.getClient().getCity().getGovernorate().getId();
        citiesSelectedId = clientData.getClient().getCity().getId();

        editUserDataFragmentTilUserName.getEditText().setText(clientData.getClient().getName());
        editUserDataFragmentTilEmail.getEditText().setText(clientData.getClient().getEmail());
        editUserDataFragmentTilPhone.getEditText().setText(clientData.getClient().getPhone());
        editUserDataFragmentTilPassword.getEditText().setText(LoadData(getActivity(), PASSWORD));
        editUserDataFragmentTilConfirmPassword.getEditText().setText(LoadData(getActivity(), PASSWORD));

        editUserDataFragmentTvBirthday.setText(clientData.getClient().getBirthDate());
        editUserDataFragmentTvLastDonationDate.setText(clientData.getClient().getDonationLastDate());

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


    @OnClick({R.id.edit_user_data_fragment_tv_birthday, R.id.edit_user_data_fragment_tv_last_donation_date, R.id.edit_user_data_fragment_btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_user_data_fragment_tv_birthday:
                showCalender(getActivity(), getString(R.string.birthday), editUserDataFragmentTvBirthday, birthdayDate);
                break;
            case R.id.edit_user_data_fragment_tv_last_donation_date:
                showCalender(getActivity(), getString(R.string.last_donation), editUserDataFragmentTvLastDonationDate, lastDonationDate);
                break;
            case R.id.edit_user_data_fragment_btn_register:
                showProgressDialog(getActivity(),getResources().getString(R.string.wait));
                editData();
                break;
        }
    }
}