
package com.example.bhajibooth.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TimeDatum {

    @SerializedName("id")
    private String mId;
    @SerializedName("maxtime")
    private String mMaxtime;
    @SerializedName("mintime")
    private String mMintime;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getMaxtime() {
//        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//        mMaxtime= dateFormat.format(mMaxtime);
        return mMaxtime;
    }

    public void setMaxtime(String maxtime) {
        mMaxtime = maxtime;
    }

    public String getMintime() {
//        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
//        mMintime= dateFormat.format(mMintime);
        return mMintime;
    }

    public void setMintime(String mintime) {
        mMintime = mintime;
    }

}
