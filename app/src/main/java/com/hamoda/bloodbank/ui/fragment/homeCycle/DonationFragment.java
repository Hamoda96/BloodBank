package com.hamoda.bloodbank.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.adapter.DonationAdapter;
import com.hamoda.bloodbank.adapter.SpinnerAdapter;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.data.model.donation.donationRequests.DonationRequests;
import com.hamoda.bloodbank.data.model.donation.donationRequests.DonationRequestsData;
import com.hamoda.bloodbank.helper.OnEndLess;
import com.hamoda.bloodbank.ui.activity.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hamoda.bloodbank.data.api.RetrofitClient.getClient;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.hamoda.bloodbank.helper.GeneralRequest.getSpinnerData;


public class DonationFragment extends Fragment {


    @BindView(R.id.donation_fragment_sp_governments)
    Spinner donationFragmentSpGovernments;
    @BindView(R.id.donation_fragment_sp_blood_type)
    Spinner donationFragmentSpBloodType;
    @BindView(R.id.donation_fragment_vp_donation_list)
    RecyclerView donationFragmentVpDonationList;
    @BindView(R.id.donation_fragment_srl_swipe_refresh_layout)
    SwipeRefreshLayout donationFragmentSrlSwipeRefreshLayout;
    @BindView(R.id.donation_fragment_btn_search)
    ImageButton donationFragmentBtnSearch;
    @BindView(R.id.donation_fragment_f_floating_create_new_donation)
    FloatingActionButton donationFragmentFFloatingCreateNewDonation;

    public List<DonationRequestsData> donationData = new ArrayList<>();
    private LinearLayoutManager linearLayoutmanger;
    public DonationAdapter donationAdapter;

    private ClientData clientData;
    private SpinnerAdapter governmentsSpinnerAdapter, bloodTypeSpinnerAdapter;
    private int bloodTypesSelectedId = 0, governmentSelectedId = 0;

    private NavController navController ;

    private Integer maxPage = 0;
    private OnEndLess onEndLess;
    private boolean filter = false;
    private HomeActivity activity;

// make instance for fragment
//    private static DonationFragment instance ;
//    public static DonationFragment getInstance(){
//        if (instance ==null) {
//            instance= new DonationFragment();
//        }
//        return instance;
//    }

    public DonationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donation, container, false);
        ButterKnife.bind(this, view);

        clientData = loadUserData(getActivity());

        activity = (HomeActivity) getActivity();

        donationData = new ArrayList<>();

        setPagination();

        setSpinner();


        return view;
    }


    // this method use to load more data when you reach the end of item or load new data
    private void setPagination() {

        // set layout
        linearLayoutmanger = new LinearLayoutManager(getActivity());
        donationFragmentVpDonationList.setLayoutManager(linearLayoutmanger);

        // ready class help to make pagination
        onEndLess = new OnEndLess(linearLayoutmanger, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        if (filter) {
                            //method to load the filter data
                            getDonationFilter(current_page);
                        } else {
                            //method to load the data
                            getDonation(current_page);
                        }
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };

        // set recycler view on scroll to load the other data
        donationFragmentVpDonationList.addOnScrollListener(onEndLess);

        //set adapter
        donationAdapter = new DonationAdapter(getActivity(), donationData, navController);
        donationFragmentVpDonationList.setAdapter(donationAdapter);

        // use to reset the data and load again when you swipe to refresh - user in filter @_^
        donationFragmentSrlSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (filter) {
                    //method to load the filter data
                    getDonationFilter(1);
                } else {
                    //method to load the data
                    getDonation(1);
                }
            }
        });


        if (donationData.size() == 0) {
            getDonation(1);
        }
    }

    private void getDonation(int page) {

        Call<DonationRequests> call = getClient().getDonation(clientData.getApiToken(), page);

        callData(call, page);
    }

    // method to get the data to spinner .. -> blood type + governments
    private void setSpinner() {
        governmentsSpinnerAdapter = new SpinnerAdapter(getActivity());
        getSpinnerData(activity, getClient().getGovernorates(), donationFragmentSpGovernments
                , governmentsSpinnerAdapter, governmentSelectedId, getString(R.string.governorate), null, true);


        bloodTypeSpinnerAdapter = new SpinnerAdapter(getActivity());
        getSpinnerData(activity, getClient().getBloodTypes(), donationFragmentSpBloodType
                , bloodTypeSpinnerAdapter, bloodTypesSelectedId, getString(R.string.blood_type), null, true);

    }


    private void getDonationFilter(int page) {

        filter = true;

        Call<DonationRequests> call = getClient().getFilterDonation(clientData.getApiToken(), bloodTypesSelectedId, governmentSelectedId, page);
        callData(call, page);
    }


    private void callData(Call<DonationRequests> call, int page) {
        call.enqueue(new Callback<DonationRequests>() {
            @Override
            public void onResponse(Call<DonationRequests> call, Response<DonationRequests> response) {
                try {
                    donationFragmentSrlSwipeRefreshLayout.setRefreshing(false);
                    // this condition for reset data  -- because resetData method
                    if (response.body().getStatus() == 1) {
                        if (page == 1) {
                            onEndLess.current_page = 1;
                            onEndLess.previous_page = 1;
                            onEndLess.previousTotal = 0;

                            // set adapter again - because we reset data in swipe
                            donationData = new ArrayList<>();
                            donationAdapter= new DonationAdapter(getActivity(), donationData, navController);
                            donationFragmentVpDonationList.setAdapter(donationAdapter);
                        }
                    }

                    // load the data from api
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        donationData.addAll(response.body().getData().getData());
                        donationAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<DonationRequests> call, Throwable t) {

                donationFragmentSrlSwipeRefreshLayout.setRefreshing(false);
//                HelperMethod.dismissProgressDialog();
            }
        });
    }


    @OnClick({R.id.donation_fragment_btn_search, R.id.donation_fragment_f_floating_create_new_donation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.donation_fragment_btn_search:

                bloodTypesSelectedId = bloodTypeSpinnerAdapter.selectedId;
                governmentSelectedId =governmentsSpinnerAdapter.selectedId;
                if (bloodTypesSelectedId == 0 && governmentSelectedId == 0) {
                    getDonation(1);
                } else {
                    getDonationFilter(1);
                }
                break;
            case R.id.donation_fragment_f_floating_create_new_donation:
                navController.navigate(R.id.action_homeFragment_to_donationOrderFragment);
                break;
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}
