package com.example.bhajibooth.retrofit;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST(APIClient.APPEND_URL + "cat.php")
    Call<JsonObject> getCat(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "subcategory.php")
    Call<JsonObject> getSubcategory(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "product.php")
    Call<JsonObject> getGetProduct(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "home.php")
    Call<JsonObject> getHome(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "login.php")
    Call<JsonObject> getLogin(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "forgot.php")
    Call<JsonObject> getForgot(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "pinmatch.php")
    Call<JsonObject> getPinmatch(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "register.php")
    Call<JsonObject> getRegister(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "timeslot.php")
    Call<JsonObject> getTimeslot(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "paymentgateway.php")
    Call<JsonObject> getpaymentgateway(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "search.php")
    Call<JsonObject> getSearch(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "profile.php")
    Call<JsonObject> UpdateProfile(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "order.php")
    Call<JsonObject> Order(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "history.php")
    Call<JsonObject> getHistory(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "plist.php")
    Call<JsonObject> getPlist(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "feed.php")
    Call<JsonObject> SendFeed(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "rate.php")
    Call<JsonObject> Rates(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "status.php")
    Call<JsonObject> getStatus(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "noti.php")
    Call<JsonObject> getNoti(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "notiset.php")
    Call<JsonObject> getNotiSet(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "ocancle.php")
    Call<JsonObject> getOdercancle(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "area.php")
    Call<JsonObject> getArea(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "code.php")
    Call<JsonObject> getCode(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "n_read.php")
    Call<JsonObject> readNoti(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "alist.php")
    Call<JsonObject> getAddress(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "add_del.php")
    Call<JsonObject> deleteAddress(@Body JsonObject object);

    @POST(APIClient.APPEND_URL + "address.php")
    Call<JsonObject> UpdateAddress(@Body JsonObject object);
}
