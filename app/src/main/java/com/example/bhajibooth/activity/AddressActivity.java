package com.example.bhajibooth.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bhajibooth.R;
import com.example.bhajibooth.model.Address;
import com.example.bhajibooth.model.Area;
import com.example.bhajibooth.model.AreaD;
import com.example.bhajibooth.model.LoginUser;
import com.example.bhajibooth.model.User;
import com.example.bhajibooth.retrofit.APIClient;
import com.example.bhajibooth.retrofit.GetResult;
import com.example.bhajibooth.utils.SessionManager;
import com.example.bhajibooth.utils.Utiles;
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

import static com.example.bhajibooth.utils.Utiles.isRef;

public class AddressActivity extends BaseActivity implements GetResult.MyListener {
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.ed_type)
    EditText edType;
    @BindView(R.id.ed_landmark)
    EditText edLandmark;
    SessionManager sessionManager;
    @BindView(R.id.ed_hoousno)
    EditText edHoousno;
    @BindView(R.id.ed_society)
    EditText edSociety;
    @BindView(R.id.ed_pinno)
    EditText edPinno;
    String areaSelect;
    List<AreaD> areaDS = new ArrayList<>();
    @BindView(R.id.spinner)
    Spinner spinner;
    User user;
    Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(AddressActivity.this);
        user = sessionManager.getUserDetails("");
        address = (Address) getIntent().getSerializableExtra("MyClass");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaSelect = areaDS.get(position).getName();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        GetArea();
        if (address != null)
            setcountaint(address);
    }
    private void setcountaint(Address address) {
        edUsername.setText("" + address.getName());
        edType.setText("" + address.getName());
        edHoousno.setText("" + address.getHno());
        edSociety.setText("" + address.getSociety());
        edPinno.setText("" + address.getPincode());
        edLandmark.setText("" + address.getLandmark());
        edType.setText("" + address.getType());
    }
    private void GetArea() {
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        Call<JsonObject> call = APIClient.getInterface().getArea((JsonObject) jsonParser.parse(jsonObject.toString()));
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");
    }
    @OnClick(R.id.txt_save)
    public void onViewClicked() {
        if (validation()) {
            if (address != null) {
                UpdateUser(address.getId());
            } else {
                UpdateUser("0");
            }
        }
    }
    private void UpdateUser(String aid) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("aid", aid);
            jsonObject.put("name", edUsername.getText().toString());
            jsonObject.put("hno", edHoousno.getText().toString());
            jsonObject.put("society", edSociety.getText().toString());
            jsonObject.put("area", areaSelect);
            jsonObject.put("landmark", edLandmark.getText().toString());
            jsonObject.put("pincode", edPinno.getText().toString());
            jsonObject.put("type", edType.getText().toString());
            jsonObject.put("mobile", user.getMobile());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("imei", Utiles.getIMEI(AddressActivity.this));
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().UpdateAddress((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                LoginUser response = gson.fromJson(result.toString(), LoginUser.class);
                Toast.makeText(AddressActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.getResult().equals("true")) {
                    isRef = true;
                    finish();
                }
            } else if (callNo.equalsIgnoreCase("2")) {
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
                int spinnerPosition = dataAdapter.getPosition(address.getArea());
                spinner.setSelection(spinnerPosition);
            }
        } catch (Exception e) {
        }
    }
    public boolean validation() {
        if (edUsername.getText().toString().isEmpty()) {
            edUsername.setError("Enter Name");
            return false;
        }
        if (edHoousno.getText().toString().isEmpty()) {
            edHoousno.setError("Enter House No");
            return false;
        }
        if (edSociety.getText().toString().isEmpty()) {
            edSociety.setError("Enter Society");
            return false;
        }
        if (edLandmark.getText().toString().isEmpty()) {
            edLandmark.setError("Enter Landmark");
            return false;
        }
        if (edPinno.getText().toString().isEmpty()) {
            edPinno.setError("Enter Pincode");
            return false;
        }
        return true;
    }
    private static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;
    }
}
