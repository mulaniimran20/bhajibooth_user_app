
package com.example.bhajibooth.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Address implements Serializable {


    @SerializedName("area")
    private String area;
    @SerializedName("hno")
    private String hno;
    @SerializedName("id")
    private String id;
    @SerializedName("landmark")
    private String landmark;
    @SerializedName("name")
    private String name;
    @SerializedName("pincode")
    private String pincode;
    @SerializedName("society")
    private String society;
    @SerializedName("status")
    private String status;
    @SerializedName("type")
    private String type;
    @SerializedName("uid")
    private String uid;
    @SerializedName("delivery_charge")
    private float deliveryCharge;


    public float getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(int deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getHno() {
        return hno;
    }

    public void setHno(String hno) {
        this.hno = hno;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
