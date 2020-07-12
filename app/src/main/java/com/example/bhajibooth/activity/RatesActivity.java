package com.example.bhajibooth.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bhajibooth.R;
import com.example.bhajibooth.model.User;
import com.example.bhajibooth.utils.CustPrograssbar;
import com.example.bhajibooth.utils.SessionManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import com.example.bhajibooth.retrofit.APIClient;
import com.example.bhajibooth.retrofit.GetResult;

import static com.example.bhajibooth.utils.Utiles.ISRATES;

public class RatesActivity extends BaseActivity implements GetResult.MyListener {

    @BindView(R.id.ed_feedback)
    EditText edFeedback;
    User user;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    @BindView(R.id.lvl_vgood)
    LinearLayout lvlVgood;
    @BindView(R.id.lvl_good)
    LinearLayout lvlGood;
    @BindView(R.id.lvl_notgood)
    LinearLayout lvlNotgood;
    String id;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sessionManager = new SessionManager(RatesActivity.this);
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();
        Intent intent = getIntent();
        id = intent.getStringExtra("oid");
    }

    private void UserFeedBack() {
        custPrograssbar.PrograssCreate();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msg", edFeedback.getText().toString());
            jsonObject.put("rate", String.valueOf(selectRate));
            jsonObject.put("uid", user.getId());
            jsonObject.put("oid", id);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().Rates((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        custPrograssbar.ClosePrograssBar();
        Log.e("Feedback Respons", ":" + result);
        if (result != null) {
            try {
                JSONObject object = new JSONObject(result.toString());
                Toast.makeText(RatesActivity.this, "" + object.getString("ResponseMsg"), Toast.LENGTH_SHORT).show();
                if (object.getString("Result").equals("true")) {
                    edFeedback.setText("");
                    ISRATES = true;
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validation() {
        if (edFeedback.getText().toString().isEmpty()) {
            edFeedback.setError("Enter FeedBack");
            return false;
        }
        if (selectRate == 0) {
            Toast.makeText(RatesActivity.this, "Pleas select Rates..", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    int selectRate = 0;

    @OnClick({R.id.img_vgood, R.id.img_good, R.id.img_notgood, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_vgood:
                selectRate = 1;
                imgClick(1);
                break;
            case R.id.img_good:
                selectRate = 2;
                imgClick(2);
                break;
            case R.id.img_notgood:
                selectRate = 3;
                imgClick(3);
                break;
            case R.id.btn_submit:
                if (validation()) {
                    UserFeedBack();
                }
                break;
            default:
                break;
        }
    }

    public void imgClick(int item) {
        switch (item) {
            case 1:
                lvlGood.setBackgroundResource(R.drawable.rounded_search);
                lvlVgood.setBackgroundResource(R.drawable.rounded_search1);
                lvlNotgood.setBackgroundResource(R.drawable.rounded_search);
                break;
            case 2:
                lvlGood.setBackgroundResource(R.drawable.rounded_search1);
                lvlVgood.setBackgroundResource(R.drawable.rounded_search);
                lvlNotgood.setBackgroundResource(R.drawable.rounded_search);
                break;
            case 3:
                lvlGood.setBackgroundResource(R.drawable.rounded_search);
                lvlVgood.setBackgroundResource(R.drawable.rounded_search);
                lvlNotgood.setBackgroundResource(R.drawable.rounded_search1);
                break;
            default:
                break;
        }

    }
}
