package com.hamoda.bloodbank.ui.fragment.homeCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.adapter.NotificationAdapter;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.data.model.getNotification.GetNotification;
import com.hamoda.bloodbank.data.model.getNotification.getNotificationData;
import com.hamoda.bloodbank.helper.OnEndLess;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hamoda.bloodbank.data.api.RetrofitClient.getClient;
import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.loadUserData;


public class NotificationFragment extends Fragment {


    @BindView(R.id.notification_fragment_rv_recycler_view)
    RecyclerView notificationFragmentRvRecyclerView;
    @BindView(R.id.notification_fragment_srl_swipe_refresh_layout)
    SwipeRefreshLayout notificationFragmentSrlSwipeRefreshLayout;

    public List<getNotificationData> notificationData = new ArrayList<>();
    private LinearLayoutManager linearLayoutmanger;
    public NotificationAdapter notificationAdapter;
    private ClientData clientData;
    private NavController navController;
    private Integer maxPage = 0;
    private OnEndLess onEndLess;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);

        clientData = loadUserData(getActivity());

        notificationData = new ArrayList<>();

        setData();

        return view;
    }

    private void setData() {

        // set layout
        linearLayoutmanger = new LinearLayoutManager(getActivity());
        notificationFragmentRvRecyclerView.setLayoutManager(linearLayoutmanger);

        // ready class help to make pagination
        onEndLess = new OnEndLess(linearLayoutmanger, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        getNotifiction(current_page);

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };

        // set recycler view on scroll to load the other data
        notificationFragmentRvRecyclerView.addOnScrollListener(onEndLess);

        //set adapter
        notificationAdapter = new NotificationAdapter((AppCompatActivity) getActivity(), notificationData, navController);
        notificationFragmentRvRecyclerView.setAdapter(notificationAdapter);

        // use to reset the data and load again when you swipe to refresh - user in filter @_^
        notificationFragmentSrlSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getNotifiction(1);
            }
        });


        if (notificationData.size() == 0) {
            getNotifiction(1);
        }
    }

    private void getNotifiction(int page) {
        Call<GetNotification> call = getClient().getAllNotification("W4mx3VMIWetLcvEcyF554CfxjZHwdtQldbdlCl2XAaBTDIpNjKO1f7CHuwKl", page);
        startCall(call,page);
    }

    private void startCall(Call<GetNotification> call, int page) {
        call.enqueue(new Callback<GetNotification>() {
            @Override
            public void onResponse(Call<GetNotification> call, Response<GetNotification> response) {
                try {
                    notificationFragmentSrlSwipeRefreshLayout.setRefreshing(false);

                    // this condition for reset data  -- because resetData method
                    if (response.body().getStatus() == 1) {
                        if (page == 1) {
                            onEndLess.current_page = 1;
                            onEndLess.previous_page = 1;
                            onEndLess.previousTotal = 0;

                            // set adapter again - because we reset data in swipe
                            notificationData = new ArrayList<>();
                            notificationAdapter= new NotificationAdapter((AppCompatActivity) getActivity(), notificationData, navController);
                            notificationFragmentRvRecyclerView.setAdapter(notificationAdapter);
                        }
                    }

                    // load the data from api
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        notificationData.addAll(response.body().getData().getData());
                        notificationAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "data send",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<GetNotification> call, Throwable t) {
                notificationFragmentSrlSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


}
