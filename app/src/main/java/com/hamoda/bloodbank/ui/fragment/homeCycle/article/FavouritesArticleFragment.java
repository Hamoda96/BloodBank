package com.hamoda.bloodbank.ui.fragment.homeCycle.article;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.adapter.ArticleAndFavoritesAdapter;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.data.model.posts.Post.Posts;
import com.hamoda.bloodbank.data.model.posts.Post.PostsData;
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
import static com.hamoda.bloodbank.helper.HelperMethod.dismissProgressDialog;
import static com.hamoda.bloodbank.helper.HelperMethod.showProgressDialog;


public class FavouritesArticleFragment extends Fragment {


    @BindView(R.id.article_favourites_fragment_rv_recycler_view)
    RecyclerView articleFavouritesFragmentRvRecyclerView;
    @BindView(R.id.article_favourites_fragment_srl_swipe_refresh_layout)
    SwipeRefreshLayout articleFavouritesFragmentSrlSwipeRefreshLayout;

    private ClientData clientData;
    private NavController navController;
    private ArticleAndFavoritesAdapter adapter;
    private List<PostsData> postsData = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private Integer maxPage = 0;
    private OnEndLess onEndLess;
    private boolean favourites =true;


    public FavouritesArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites_article, container, false);
        ButterKnife.bind(this, view);

        clientData = loadUserData(getActivity());
        postsData = new ArrayList<>();
        init();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }


    private void init() {

        showProgressDialog(getActivity(), String.valueOf(R.string.wait));
        // set layout
        linearLayoutManager = new LinearLayoutManager(getActivity());
        articleFavouritesFragmentRvRecyclerView.setLayoutManager(linearLayoutManager);

        // ready class help to make pagination
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        getPosts(current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };

        // set recycler view on scroll to load the other data
        articleFavouritesFragmentRvRecyclerView.addOnScrollListener(onEndLess);

        //set adapter
        adapter = new ArticleAndFavoritesAdapter(getActivity(), postsData, navController, favourites);
        articleFavouritesFragmentRvRecyclerView.setAdapter(adapter);


        // use to reset the data and load again when you swipe to refresh - user in filter @_^
        articleFavouritesFragmentSrlSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                    //method to load the data
                    getPosts(1);

            }
        });

        if (postsData.size() == 0) {
            //method to load the data
            getPosts(1);
        }

    }

    private void getPosts(int page) {
        Call<Posts> call = getClient().getFavouritesPosts(clientData.getApiToken(), page);
        startCall(call, page);
    }

    private void startCall(Call<Posts> call, int page) {
        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                try {
                    dismissProgressDialog();
                    articleFavouritesFragmentSrlSwipeRefreshLayout.setRefreshing(false);
                    if (response.body().getStatus() == 1) {
                        if (page == 1) {
                            onEndLess.current_page = 1;
                            onEndLess.previous_page = 1;
                            onEndLess.previousTotal = 0;

                            // set adapter again - because we reset data in swipe
                            postsData = new ArrayList<>();
                            adapter = new ArticleAndFavoritesAdapter(getActivity(), postsData, navController, favourites);
                            articleFavouritesFragmentRvRecyclerView.setAdapter(adapter);
                        }
                    }

                    // load the data from api
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        //first data for pagination , 2th data is the data for the first page
                        postsData.addAll(response.body().getData().getData());
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                articleFavouritesFragmentSrlSwipeRefreshLayout.setRefreshing(false);
                dismissProgressDialog();
            }
        });
    }
}
