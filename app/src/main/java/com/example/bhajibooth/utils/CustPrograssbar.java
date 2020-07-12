package com.example.bhajibooth.utils;

import android.app.ProgressDialog;

public class CustPrograssbar {
    public static ProgressDialog progressDialog;

    public void PrograssCreate() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                return;
            } else {
                progressDialog.setMessage("Progress...");
                progressDialog.show();
            }
        } catch (Exception e) {
            e.toString();
        }
    }

    public void ClosePrograssBar() {
        if (progressDialog != null) {
            try {
                progressDialog.cancel();
            } catch (Exception e) {
                e.toString();
            }
        }

    }
}
