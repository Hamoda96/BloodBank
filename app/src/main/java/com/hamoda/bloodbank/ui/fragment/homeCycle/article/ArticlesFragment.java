package com.hamoda.bloodbank.ui.fragment.homeCycle.article;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.adapter.ArticleAndFavoritesAdapter;
import com.hamoda.bloodbank.adapter.SpinnerAdapter;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.data.model.posts.Post.Posts;
import com.hamoda.bloodbank.data.model.posts.Post.PostsData;
import com.hamoda.bloodbank.helper.OnEndLess;

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


public class ArticlesFragment extends Fragment {


    @BindView(R.id.article_fragment_rv_recycler_view)
    RecyclerView articleFragmentRvRecyclerView;
    @BindView(R.id.article_fragment_srl_swipe_refresh_layout)
    SwipeRefreshLayout articleFragmentSrlSwipeRefreshLayout;
    @BindView(R.id.article_fragment_btn_search)
    ImageButton articleFragmentBtnSearch;
    @BindView(R.id.article_fragment_et_words_search)
    EditText articleFragmentEtWordsSearch;
    @BindView(R.id.article_fragment_s_cat)
    Spinner articleFragmentSCat;

    private boolean favourites = false;
    private ClientData clientData;
    private NavController navController;
    private ArticleAndFavoritesAdapter adapter;
    private List<PostsData> postsData = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private Integer maxPage = 0;
    private OnEndLess onEndLess;

    private boolean filter = false;
    private SpinnerAdapter categoriesAdapter;
    private int categoriesSelectedId = 0;
    private String searchWord = "";

    public ArticlesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_articles, container, false);
        ButterKnife.bind(this, view);

        clientData = loadUserData(getActivity());

        postsData = new ArrayList<>();

        searchWord = articleFragmentEtWordsSearch.getText().toString();

        setSpinner();

        init();

        return view;
    }

    // set spinner of category
    private void setSpinner() {
        categoriesAdapter = new SpinnerAdapter(getActivity());
        getSpinnerData(getActivity(), getClient().getCategories(), articleFragmentSCat, categoriesAdapter
                , categoriesSelectedId, getString(R.string.categories), null, true);
    }

    private void init() {

        // set layout
        linearLayoutManager = new LinearLayoutManager(getActivity());
        articleFragmentRvRecyclerView.setLayoutManager(linearLayoutManager);

        // ready class help to make pagination
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;

                        if (filter) {
                            // load filter Data
                            getPostsFilter(current_page);
                        } else {
                            //method to load the data
                            getPosts(current_page);
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
        articleFragmentRvRecyclerView.addOnScrollListener(onEndLess);

        //set adapter
        adapter = new ArticleAndFavoritesAdapter(getActivity(), postsData, navController, favourites);
        articleFragmentRvRecyclerView.setAdapter(adapter);


        // use to reset the data and load again when you swipe to refresh - user in filter @_^
        articleFragmentSrlSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (filter) {
                    // load filter Data
                    getPostsFilter(1);
                } else {
                    //method to load the data
                    getPosts(1);
                }
            }
        });

        if (postsData.size() == 0) {
            //method to load the data
            getPosts(1);
        }

    }

    private void getPostsFilter(int page) {
        filter = true;

        Call<Posts> call = getClient().getPostsFilter(clientData.getApiToken(), page, searchWord, categoriesSelectedId);
        startCall(call, page);
    }

    private void getPosts(int page) {
        Call<Posts> call = getClient().getAllPosts(clientData.getApiToken(), page);
        startCall(call, page);
    }

    private void startCall(Call<Posts> call, int page) {
        call.enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                try {

                    articleFragmentSrlSwipeRefreshLayout.setRefreshing(false);
                    if (response.body().getStatus() == 1) {
                        if (page == 1) {
                            onEndLess.current_page = 1;
                            onEndLess.previous_page = 1;
                            onEndLess.previousTotal = 0;

                            // set adapter again - because we reset data in swipe
                            postsData = new ArrayList<>();
                            adapter = new ArticleAndFavoritesAdapter(getActivity(), postsData, navController, favourites);
                            articleFragmentRvRecyclerView.setAdapter(adapter);
                        }
                    }

                    // load the data from api
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        postsData.addAll(response.body().getData().getData());
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                articleFragmentSrlSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    @OnClick(R.id.article_fragment_btn_search)
    public void onViewClicked() {
        categoriesSelectedId = categoriesAdapter.selectedId;
        if (categoriesSelectedId == 0) {
            getPosts(1);
        } else {
            getPostsFilter(1);
        }
    }
}
