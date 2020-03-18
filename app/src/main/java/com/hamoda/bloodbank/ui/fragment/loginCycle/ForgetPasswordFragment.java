package com.hamoda.bloodbank.ui.fragment.loginCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.hamoda.bloodbank.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ForgetPasswordFragment extends Fragment {


    @BindView(R.id.forget_password_fragment_ed_user_phone)
    TextInputLayout forgetPasswordFragmentEdUserPhone;
    @BindView(R.id.forget_password_fragment_btn_send)
    Button forgetPasswordFragmentBtnSend;

    Unbinder unbinder ;

    NavController navController;


    public ForgetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        unbinder = ButterKnife.bind(this,view);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @OnClick(R.id.forget_password_fragment_btn_send)
    public void onViewClicked() {
        navController.navigate(R.id.action_forgetPasswordFragment_to_changePasswordFragment);
    }


}
