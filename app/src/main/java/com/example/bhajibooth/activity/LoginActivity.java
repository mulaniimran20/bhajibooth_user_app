package com.example.bhajibooth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhajibooth.R;
import com.example.bhajibooth.model.LoginUser;
import com.example.bhajibooth.utils.CustPrograssbar;
import com.example.bhajibooth.utils.SessionManager;
import com.example.bhajibooth.utils.Utiles;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import com.example.bhajibooth.retrofit.APIClient;
import com.example.bhajibooth.retrofit.GetResult;

import static com.example.bhajibooth.utils.SessionManager.LOGIN;

public class LoginActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_password)
    EditText edPassword;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(LoginActivity.this);
    }
    @OnClick({R.id.btn_login, R.id.btn_sign, R.id.txt_forgotpassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (validation()) {
                    LoginUser();
                }
                break;
            case R.id.btn_sign:
                startActivity(new Intent(LoginActivity.this, SingActivity.class));
                finish();
                break;
            case R.id.txt_forgotpassword:
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                break;
            default:
                break;
        }
    }
    private void LoginUser() {
        custPrograssbar.PrograssCreate();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", edUsername.getText().toString());
            jsonObject.put("password", edPassword.getText().toString());
            jsonObject.put("imei", Utiles.getIMEI(LoginActivity.this));
            JsonParser jsonParser = new JsonParser();

            Call<JsonObject> call = APIClient.getInterface().getLogin((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public boolean validation() {
        if (edUsername.getText().toString().isEmpty()) {
            edUsername.setError("Enter Mobile No");
            return false;
        }
        if (edPassword.getText().toString().isEmpty()) {
            edPassword.setError("Enter Password");
            return false;
        }
        return true;
    }
    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.ClosePrograssBar();
            Gson gson = new Gson();
            LoginUser response = gson.fromJson(result.toString(), LoginUser.class);
            Toast.makeText(LoginActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG).show();
            if (response.getResult().equals("true")) {
                sessionManager.setUserDetails("", response.getUser());
                sessionManager.setBooleanData(LOGIN,true);
                OneSignal.sendTag("userId", response.getUser().getId());
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        } catch (Exception e) {
            Log.e("error"," --> "+e.toString());
        }
    }
}
