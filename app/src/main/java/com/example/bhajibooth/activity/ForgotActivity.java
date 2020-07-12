package com.example.bhajibooth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhajibooth.R;
import com.example.bhajibooth.model.CCode;
import com.example.bhajibooth.model.Contry;
import com.example.bhajibooth.model.RestResponse;
import com.example.bhajibooth.utils.CustPrograssbar;
import com.example.bhajibooth.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import com.example.bhajibooth.retrofit.APIClient;
import com.example.bhajibooth.retrofit.GetResult;

import static com.example.bhajibooth.utils.Utiles.ISVARIFICATION;

public class ForgotActivity extends AppCompatActivity implements GetResult.MyListener {
    @BindView(R.id.btn_send)
    TextView btnSend;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    @BindView(R.id.ed_mobile)
    EditText edMobile;
    @BindView(R.id.spinner)
    Spinner spinner;
    List<CCode> cCodes = new ArrayList<>();
    String codeSelect;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(ForgotActivity.this);

        GetCode();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                codeSelect = cCodes.get(position).getCcode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("tem", parent.toString());
            }
        });
    }

    @OnClick({R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                ForgotPassword();
                break;
            default:
                break;
        }
    }

    private void GetCode() {
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        Call<JsonObject> call = APIClient.getInterface().getCode((JsonObject) jsonParser.parse(jsonObject.toString()));
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");
    }

    public boolean validation() {
        if (edMobile.getText().toString().isEmpty()) {
            edMobile.setError("Enter Mobile");
            return false;
        }
        return true;
    }

    private void ForgotPassword() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", edMobile.getText().toString());
            jsonObject.put("ccode", codeSelect);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getForgot((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
            custPrograssbar.PrograssCreate();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.ClosePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                if (response.getResult().equals("true")) {
                    ISVARIFICATION = 0;
                    startActivity(new Intent(ForgotActivity.this, VerifyPhoneActivity.class).putExtra("code", codeSelect).putExtra("phone", edMobile.getText().toString()));
                } else {
                    Toast.makeText(ForgotActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG).show();

                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Contry contry = gson.fromJson(result.toString(), Contry.class);
                cCodes = contry.getData();
                List<String> Arealist = new ArrayList<>();
                for (int i = 0; i < cCodes.size(); i++) {
                    if (cCodes.get(i).getStatus().equalsIgnoreCase("1")) {
                        Arealist.add(cCodes.get(i).getCcode());
                    }
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Arealist);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
