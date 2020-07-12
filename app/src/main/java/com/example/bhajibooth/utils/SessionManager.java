package com.example.bhajibooth.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.bhajibooth.model.Address;
import com.example.bhajibooth.model.User;
import com.google.gson.Gson;

public class SessionManager {
    private final SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;
    public static String LOGIN = "login";
    public static String ISOPEN = "isopen";
    public static String USERDATA = "Userdata";
    public static String ADDRESS1 = "address";
    public static boolean ISCART=false;
    public static String AREA = "area";
    public static String CURRUNCY = "currncy";
    public static String PRIVACY = "privacy_policy";
    public static String TREMSCODITION = "tremcodition";
    public static String ABOUT_US = "about_us";
    public static String CONTACT_US = "contact_us";
    public static String O_MIN = "o_min";
    public static String RAZ_KEY = "raz_key";
    public static String TAX = "tax";

    public SessionManager(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public void setStringData(String key, String val) {
        mEditor.putString(key, val);
        mEditor.commit();
    }

    public String getStringData(String key) {
        return mPrefs.getString(key, "");
    }

    public void setBooleanData(String key, Boolean val) {
        mEditor.putBoolean(key, val);
        mEditor.commit();
    }

    public boolean getBooleanData(String key) {
        return mPrefs.getBoolean(key, false);
    }

    public void setIntData(String key, int val) {
        mEditor.putInt(key, val);
        mEditor.commit();
    }

    public int getIntData(String key) {
        return mPrefs.getInt(key, 0);
    }

    public void setUserDetails(String key, User val) {
        mEditor.putString(USERDATA, new Gson().toJson(val));
        mEditor.commit();
    }

    public User getUserDetails(String key) {
        return new Gson().fromJson(mPrefs.getString(USERDATA, ""), User.class);
    }

    public void setAddress(String key, Address val) {
        mEditor.putString(ADDRESS1, new Gson().toJson(val));
        mEditor.commit();
    }

    public Address getAddress(String key) {
        return new Gson().fromJson(mPrefs.getString(ADDRESS1, ""), Address.class);
    }
    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }
}
