package com.hamoda.bloodbank.ui.fragment.loginCycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.data.model.client.Client;
import com.hamoda.bloodbank.ui.activity.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hamoda.bloodbank.data.api.RetrofitClient.getClient;
import static com.hamoda.bloodbank.helper.Constants.PASSWORD;
import static com.hamoda.bloodbank.helper.Constants.REMEMBER;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.SaveData;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.saveUserData;


public class LoginFragment extends Fragment {


    @BindView(R.id.login_fragment_et_user_phone)
    TextInputLayout loginFragmentEtUserPhone;
    @BindView(R.id.login_fragment_et_password)
    TextInputLayout loginFragmentEtPassword;
    @BindView(R.id.login_fragment_tv_forget_password)
    TextView loginFragmentTvForgetPassword;
    @BindView(R.id.login_fragment_cb_remember_me)
    CheckBox loginFragmentCbRememberMe;
    @BindView(R.id.login_fragment_btn_login)
    Button loginFragmentBtnLogin;
    @BindView(R.id.login_fragment_tv_register)
    TextView loginFragmentTvRegister;

    Unbinder unbinder;

    NavController navController;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }



    private void userLogin(){
         String userPhone =loginFragmentEtUserPhone.getEditText().getText().toString();
         String userPassword =loginFragmentEtPassword.getEditText().getText().toString();
         boolean rememberMe =loginFragmentCbRememberMe.isChecked();

         getClient().onLogin(userPhone, userPassword).enqueue(new Callback<Client>() {
             @Override
             public void onResponse(Call<Client> call, Response<Client> response) {

                 if (response.body().getStatus() == 1) {

                     SaveData(getActivity(),REMEMBER,rememberMe);
                     SaveData(getActivity(),PASSWORD,userPassword);
                     saveUserData(getActivity(), response.body().getData());

                     Intent intent = new Intent(getActivity(), HomeActivity.class);
                     startActivity(intent);
                     getActivity().finish();
                 }
             }

             @Override
             public void onFailure(Call<Client> call, Throwable t) {
                 Toast.makeText(getActivity(), "fffffffff", Toast.LENGTH_SHORT).show();
             }
         });

    }

    @OnClick({R.id.login_fragment_tv_forget_password, R.id.login_fragment_btn_login, R.id.login_fragment_tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_fragment_tv_forget_password:
                navController.navigate(R.id.action_loginFragment_to_forgetPasswordFragment);
                break;
            case R.id.login_fragment_btn_login:
                userLogin();
                break;
            case R.id.login_fragment_tv_register:
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
                break;
        }
    }
}
