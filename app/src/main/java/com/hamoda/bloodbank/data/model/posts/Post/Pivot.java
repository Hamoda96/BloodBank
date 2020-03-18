
package com.hamoda.bloodbank.data.model.posts.Post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pivot {

    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("post_id")
    @Expose
    private String postId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

}
