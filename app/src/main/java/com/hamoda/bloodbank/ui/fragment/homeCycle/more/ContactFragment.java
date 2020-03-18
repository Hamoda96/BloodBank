package com.hamoda.bloodbank.ui.fragment.homeCycle.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.data.model.contact.Contact;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hamoda.bloodbank.data.api.RetrofitClient.getClient;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.hamoda.bloodbank.helper.HelperMethod.dismissProgressDialog;
import static com.hamoda.bloodbank.helper.HelperMethod.showProgressDialog;


public class ContactFragment extends Fragment {


    @BindView(R.id.contact_fragment_iv_facebook)
    ImageView contactFragmentIvFacebook;
    @BindView(R.id.contact_fragment_iv_instagram)
    ImageView contactFragmentIvInstagram;
    @BindView(R.id.contact_fragment_iv_twitter)
    ImageView contactFragmentIvTwitter;
    @BindView(R.id.contact_fragment_iv_youtube)
    ImageView contactFragmentIvYoutube;
    @BindView(R.id.contact_fragment_til_title)
    TextInputLayout contactFragmentTilTitle;
    @BindView(R.id.contact_fragment_til_message)
    TextInputLayout contactFragmentTilMessage;


    ClientData clientData;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);

        clientData = loadUserData(getActivity());

        return view;
    }

    private void init() {

        String apiToken = clientData.getApiToken();
        String title = contactFragmentTilTitle.getEditText().getText().toString().trim();
        String message = contactFragmentTilMessage.getEditText().getText().toString().trim();

        getClient().setConatctMessage(apiToken, title, message).enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {

                if (response.body().getStatus() == 1) {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), "message sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(getActivity(), "sending fail", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @OnClick(R.id.contact_fragment_btn_send)
    public void onViewClicked() {
        showProgressDialog(getActivity(), getString(R.string.sending));
        init();
    }
}
