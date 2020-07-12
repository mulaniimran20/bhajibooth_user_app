package com.example.bhajibooth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhajibooth.R;
import com.example.bhajibooth.model.Area;
import com.example.bhajibooth.model.AreaD;
import com.example.bhajibooth.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import com.example.bhajibooth.retrofit.APIClient;
import com.example.bhajibooth.retrofit.GetResult;

import static com.example.bhajibooth.utils.SessionManager.AREA;

public class SelectAreaActivity extends AppCompatActivity implements GetResult.MyListener {
    List<AreaD> areaDS = new ArrayList<>();
    @BindView(R.id.spinner)
    Spinner spinner;
    String areaSelect;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(SelectAreaActivity.this);
        GetArea();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaSelect = areaDS.get(position).getName();
                sessionManager.setStringData(AREA, areaSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void GetArea() {
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        Call<JsonObject> call = APIClient.getInterface().getArea((JsonObject) jsonParser.parse(jsonObject.toString()));
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Area area = gson.fromJson(result.toString(), Area.class);
                areaDS = area.getData();
                List<String> Arealist = new ArrayList<>();
                for (int i = 0; i < areaDS.size(); i++) {
                    if (areaDS.get(i).getStatus().equalsIgnoreCase("1")) {
                        Arealist.add(areaDS.get(i).getName());
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

    @OnClick(R.id.btn_next)
    public void onClick() {
        startActivity(new Intent(SelectAreaActivity.this, LoginActivity.class));
    }
}
