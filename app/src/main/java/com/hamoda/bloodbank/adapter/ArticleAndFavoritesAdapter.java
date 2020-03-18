package com.hamoda.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.data.model.client.ClientData;
import com.hamoda.bloodbank.data.model.posts.Post.Posts;
import com.hamoda.bloodbank.data.model.posts.Post.PostsData;

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

public class ArticleAndFavoritesAdapter extends RecyclerView.Adapter<ArticleAndFavoritesAdapter.ViewHolder> {


    private Context context;
    private Activity activity;
    List<PostsData> postsData = new ArrayList<>();
    NavController navController;
    private boolean favourites;
    ClientData clientData;


    public ArticleAndFavoritesAdapter(Activity activity, List<PostsData> postsData, NavController navController, boolean favourites) {
        this.context = activity;
        this.activity = activity;
        this.postsData = postsData;
        this.navController = navController;
        this.favourites = favourites;
        clientData = loadUserData(activity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.article_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setFavourites(holder, position);
    }

    private void setFavourites(ViewHolder holder, int position) {
        holder.articleItemIvIsFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putInFavourites(holder, position);
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("title", postsData.get(position).getTitle());
                bundle.putString("content", postsData.get(position).getContent());
                bundle.putBoolean("favourites", postsData.get(position).getIsFavourite());
                bundle.putString("image", postsData.get(position).getThumbnailFullPath());

                if (favourites) {
                    navController.navigate(R.id.action_favouritesArticleFragment_to_articleDetailFragment, bundle);
                } else {
                    navController.navigate(R.id.action_homeFragment_to_articleDetailFragment, bundle);
                }
            }
        });
    }

    private void putInFavourites(ViewHolder holder, int position) {
        // take the favourites from api and set it العكس
        // بجيب الداتا من السيرفر واخليها العكس علشان اقدر اخليها تنضاف كمفضلة عادي
        // 3 مرات - قبل الاستدعاء - عند نجاح العملية وعند فشلها

        postsData.get(position).setIsFavourite(!postsData.get(position).getIsFavourite());

        if (postsData.get(position).getIsFavourite()) {
            holder.articleItemIvIsFavourite.setImageResource(R.drawable.ic_heart_regular);
            Toast.makeText(activity, "Add To Favourites", Toast.LENGTH_LONG).show();
        } else {
            holder.articleItemIvIsFavourite.setImageResource(R.drawable.ic_heart_solid);
            Toast.makeText(activity, "Remove From Favourites", Toast.LENGTH_SHORT).show();
        }

        getClient().addAndRemovePosts(postsData.get(position).getId(), clientData.getApiToken()).enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        notifyDataSetChanged();
                    } else {
                        postsData.get(position).setIsFavourite(!postsData.get(position).getIsFavourite());
                        if (postsData.get(position).getIsFavourite()) {
                            holder.articleItemIvIsFavourite.setImageResource(R.drawable.ic_heart_regular);
                            Toast.makeText(activity, "Add To Favourites", Toast.LENGTH_LONG).show();
                        } else {
                            holder.articleItemIvIsFavourite.setImageResource(R.drawable.ic_heart_solid);
                            Toast.makeText(activity, "Remove From Favourites", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {
                postsData.get(position).setIsFavourite(!postsData.get(position).getIsFavourite());
                if (postsData.get(position).getIsFavourite()) {
                    holder.articleItemIvIsFavourite.setImageResource(R.drawable.ic_heart_regular);
                    Toast.makeText(activity, "Add To Favourites", Toast.LENGTH_LONG).show();

                } else {
                    holder.articleItemIvIsFavourite.setImageResource(R.drawable.ic_heart_solid);
                    Toast.makeText(activity, "Remove From Favourites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setData(ViewHolder holder, int position) {
        holder.articleItemTvTitle.setText(postsData.get(position).getTitle());
            String url = postsData.get(position).getThumbnailFullPath();
        Glide.with(context).load(url).into(holder.articleItemIvPostPhoto);
        if (postsData.get(position).getIsFavourite()) {
            holder.articleItemIvIsFavourite.setImageResource(R.drawable.ic_heart_solid);
        } else {
            holder.articleItemIvIsFavourite.setImageResource(R.drawable.ic_heart_regular);
        }
    }

    @Override
    public int getItemCount() {
        return postsData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.article_item_iv_post_photo)
        PorterShapeImageView articleItemIvPostPhoto;
        @BindView(R.id.article_item_tv_title)
        TextView articleItemTvTitle;
        @BindView(R.id.article_item_iv_is_favourite)
        ImageView articleItemIvIsFavourite;
        @BindView(R.id.article_item_rl_relative_layout)
        RelativeLayout articleItemRlRelativeLayout;

        private View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.article_item_rl_relative_layout)
        public void onViewClicked() {
        }

    }
}
