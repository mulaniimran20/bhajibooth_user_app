package com.example.bhajibooth.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultHome {

    @SerializedName("Banner")
    private List<BannerItem> bannerItems;

    @SerializedName("Catlist")
    private List<CatItem> catItems;

    @SerializedName("Productlist")
    private List<ProductItem> productItems;

    @SerializedName("dynamic_section")
    private List<DynamicData> dynamicData;

    @SerializedName("Remain_notification")
    private int RemainNotification;

    @SerializedName("Main_Data")
    @Expose
    private Main_DataHome Main_Data;

    public Main_DataHome getMain_Data() {
        return Main_Data;
    }

    public void setMain_Data(Main_DataHome main_Data) {
        Main_Data = main_Data;
    }


    public List<DynamicData> getDynamicData() {
        return dynamicData;
    }

    public void setDynamicData(List<DynamicData> dynamicData) {
        this.dynamicData = dynamicData;
    }



    public int getRemainNotification() {
        return RemainNotification;
    }

    public void setRemainNotification(int remainNotification) {
        RemainNotification = remainNotification;
    }

    public List<BannerItem> getBannerItems() {
        return bannerItems;
    }

    public void setBannerItems(List<BannerItem> bannerItems) {
        this.bannerItems = bannerItems;
    }

    public List<CatItem> getCatItems() {
        return catItems;
    }

    public void setCatItems(List<CatItem> catItems) {
        this.catItems = catItems;
    }

    public List<ProductItem> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItem> productItems) {
        this.productItems = productItems;
    }
}
