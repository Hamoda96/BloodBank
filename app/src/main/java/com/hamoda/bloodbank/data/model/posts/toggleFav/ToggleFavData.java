
package com.hamoda.bloodbank.data.model.posts.toggleFav;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ToggleFavData {

    @SerializedName("post_id")
    @Expose
    private List<String> postId = null;

    public List<String> getPostId() {
        return postId;
    }

    public void setPostId(List<String> postId) {
        this.postId = postId;
    }

}
