
package com.example.bhajibooth.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class BannerItem {

    @SerializedName("bimg")
    private String mBimg;
    @SerializedName("id")
    private String mId;

    @SerializedName("cid")
    private String cid;


    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getBimg() {
        return mBimg;
    }

    public void setBimg(String bimg) {
        mBimg = bimg;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

}
