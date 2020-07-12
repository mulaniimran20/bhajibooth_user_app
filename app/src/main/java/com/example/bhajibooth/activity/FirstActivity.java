package com.example.bhajibooth.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.example.bhajibooth.utils.SessionManager;
import com.example.bhajibooth.utils.SessionManager;
import com.example.bhajibooth.R;
import com.example.bhajibooth.utils.SessionManager;
import com.example.bhajibooth.utils.Utiles;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;


public class FirstActivity extends ActivityManagePermission {
    private static int SPLASH_TIME_OUT = 2000;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        sessionManager = new SessionManager(FirstActivity.this);
        new Handler().postDelayed(() -> {
            if (Utiles.internetChack()) {
                if (sessionManager.getBooleanData(SessionManager.LOGIN) || sessionManager.getBooleanData(SessionManager.ISOPEN)) {
                    Intent i = new Intent(FirstActivity.this, HomeActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(FirstActivity.this, InfoActivity.class);
                    startActivity(i);
                }
                finish();
            } else {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(FirstActivity.this);
                builder.setMessage("Please Check Your Internet Connection")
                        .setCancelable(false)
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.e("tem",dialog+""+id);
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }, SPLASH_TIME_OUT);

    }


}
