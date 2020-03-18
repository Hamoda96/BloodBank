package com.hamoda.bloodbank.ui.fragment.homeCycle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.ui.activity.ContainerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hamoda.bloodbank.helper.Constants.SIGNOUT;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.SaveData;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.clean;


public class MoreFragment extends Fragment {


    @BindView(R.id.more_fragment_tv_fav)
    TextView moreFragmentTvFav;
    @BindView(R.id.more_fragment_tv_contact_us)
    TextView moreFragmentTvContactUs;
    @BindView(R.id.more_fragment_tv_about_app)
    TextView moreFragmentTvAboutApp;
    @BindView(R.id.more_fragment_tv_rate_on_store)
    TextView moreFragmentTvRateOnStore;
    @BindView(R.id.more_fragment_tv_notification_setting)
    TextView moreFragmentTvNotificationSetting;
    @BindView(R.id.more_fragment_tv_sign_out)
    TextView moreFragmentTvSignOut;

    NavController navController ;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @OnClick({R.id.more_fragment_tv_fav, R.id.more_fragment_tv_contact_us, R.id.more_fragment_tv_about_app, R.id.more_fragment_tv_rate_on_store, R.id.more_fragment_tv_notification_setting, R.id.more_fragment_tv_sign_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more_fragment_tv_fav:
                navController.navigate(R.id.action_moreFragment_to_favouritesArticleFragment);
                break;
            case R.id.more_fragment_tv_contact_us:
                navController.navigate(R.id.action_moreFragment_to_contactFragment);
                break;
            case R.id.more_fragment_tv_about_app:
                navController.navigate(R.id.action_moreFragment_to_aboutUsFragment);
                break;
            case R.id.more_fragment_tv_rate_on_store:
                break;
            case R.id.more_fragment_tv_notification_setting:
                break;
            case R.id.more_fragment_tv_sign_out:
                signOut();
                break;
        }
    }

    private void signOut() {
        clean(getActivity());
        Intent intent = new Intent(getActivity(), ContainerActivity.class);
        SaveData(getActivity(),SIGNOUT,true);
        startActivity(intent);
    }
}
