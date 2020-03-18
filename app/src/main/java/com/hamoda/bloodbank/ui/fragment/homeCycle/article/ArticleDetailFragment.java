package com.hamoda.bloodbank.ui.fragment.homeCycle.article;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.data.model.posts.Post.PostsData;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hamoda.bloodbank.data.local.SharedPreferencesManger.loadUserData;


public class ArticleDetailFragment extends Fragment {


    @BindView(R.id.article_detail_fragment_iv_image)
    ImageView articleDetailFragmentIvImage;
    @BindView(R.id.article_detail_fragment_iv_favourite)
    ImageView articleDetailFragmentIvFavourite;
    @BindView(R.id.article_detail_fragment_tv_title)
    TextView articleDetailFragmentTvTitle;
    @BindView(R.id.article_detail_fragment_tv_article_detail)
    TextView articleDetailFragmentTvArticleDetail;

//    private int id;

    private String title;
    private String content;
    private boolean favourites;
    private String image;

    private ClientData clientData;
    private PostsData postsData;

//   private PostsPagination postsPagination;

    public ArticleDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_articles_detail, container, false);
        ButterKnife.bind(this, view);

        title = getArguments().getString("title");
        content = getArguments().getString("content");
        favourites = getArguments().getBoolean("favourites");
        image = getArguments().getString("image");

        setData();

        clientData = loadUserData(getActivity());

//        init();

        return view;
    }

//    private void init() {
//       getClient().displayPostDetails(clientData.getApiToken(), id, 1).enqueue(new Callback<Posts>() {
//           @Override
//           public void onResponse(Call<Posts> call, Response<Posts> response) {
//
//           }
//
//           @Override
//           public void onFailure(Call<Posts> call, Throwable t) {
//
//           }
//       });
//    }

    private void setData() {

        Glide.with(getContext()).load(image).into(articleDetailFragmentIvImage);
        if (favourites) {
            articleDetailFragmentIvFavourite.setImageResource(R.drawable.ic_heart_solid);
        } else {
            articleDetailFragmentIvFavourite.setImageResource(R.drawable.ic_heart_regular);
        }
        articleDetailFragmentTvTitle.setText(title);
        articleDetailFragmentTvArticleDetail.setText(content);
    }


}
