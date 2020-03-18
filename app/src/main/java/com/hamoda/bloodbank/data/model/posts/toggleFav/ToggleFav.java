
package com.hamoda.bloodbank.data.model.posts.toggleFav;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ToggleFav {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private ToggleFavData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ToggleFavData getData() {
        return data;
    }

    public void setData(ToggleFavData data) {
        this.data = data;
    }

}
