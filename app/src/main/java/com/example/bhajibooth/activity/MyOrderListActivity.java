package com.example.bhajibooth.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.bhajibooth.R;
import com.example.bhajibooth.model.MyOrder;
import com.example.bhajibooth.model.Productinfo;
import com.example.bhajibooth.model.RestResponse;
import com.example.bhajibooth.model.User;
import com.example.bhajibooth.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

import static com.example.bhajibooth.utils.SessionManager.CURRUNCY;
import static com.example.bhajibooth.utils.Utiles.ISRATES;
import com.example.bhajibooth.retrofit.APIClient;
import com.example.bhajibooth.retrofit.GetResult;

public class MyOrderListActivity extends BaseActivity implements GetResult.MyListener {

    User user;
    SessionManager sessionManager;
    String oid;
    String id;
    @BindView(R.id.txt_orderid)
    TextView txtOrderid;
    @BindView(R.id.txt_subtotal)
    TextView txtSubtotal;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_delivery)
    TextView txtDelivery;
    @BindView(R.id.txt_timeslot)
    TextView txtTimeslot;
    @BindView(R.id.txt_tax)
    TextView txtTax;
    @BindView(R.id.txt_tattt)
    TextView txt_tattt;
    @BindView(R.id.txt_qty)
    TextView txtQty;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.lvl_items)
    LinearLayout lvlItems;
    @BindView(R.id.lvl_data)
    LinearLayout lvlData;
    @BindView(R.id.txt_stutus)
    TextView txtStutus;
    @BindView(R.id.txt_ptype)
    TextView txtPtype;
    @BindView(R.id.txt_pikupmyself)
    TextView txtPikupmyself;
    @BindView(R.id.txt_Address)
    TextView txtAddress;
    String phonecall;
    @BindView(R.id.time_view)
    StateProgressBar timeView;
    public static MyOrderListActivity orderListActivity = null;

    public static MyOrderListActivity getInstance() {
        return orderListActivity;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("OrderList ");
        getSupportActionBar().setElevation(0);
        sessionManager = new SessionManager(MyOrderListActivity.this);
        user = sessionManager.getUserDetails("");
        String[] descriptionData = {"Pending", "Ready to Ship", "Delivered"};
        timeView.setStateDescriptionData(descriptionData);
        timeView.checkStateCompleted(true);
        Intent intent = getIntent();
        id = intent.getStringExtra("oid");
        getHistry(id);
    }

    private void getHistry(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("uid", user.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getPlist((JsonObject) jsonParser.parse(jsonObject.toString()));
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
                List<Productinfo> list = new ArrayList<>();
                Gson gson = new Gson();
                MyOrder myOrder = gson.fromJson(result.toString(), MyOrder.class);
                if (myOrder.getResult().equals("true")) {
                    list.addAll(myOrder.getProductinfo());
                    if (myOrder.getpMethod().equalsIgnoreCase(getResources().getString(R.string.pic_myslf))) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) lvlData.getLayoutParams();
                        params.setMargins(0, 0, 0, 100);
                        lvlData.setLayoutParams(params);
                        txtPikupmyself.setVisibility(View.VISIBLE);
                    } else {
                        txtPikupmyself.setVisibility(View.GONE);

                    }
                    phonecall = myOrder.getRiderMobile();
                    txtOrderid.setText("" + myOrder.getmOrderid());
                    txtStutus.setText("" + myOrder.getStatus());
                    txtDate.setText("" + myOrder.getOrderDate());
                    txtTimeslot.setText("" + myOrder.getTimesloat());
                    txtDelivery.setText("" + sessionManager.getStringData(CURRUNCY) + myOrder.getdCharge());
                    txtPtype.setText(" " + myOrder.getpMethod() + " ");
                    txtQty.setText("" + list.size());
                    txtAddress.setText("" + myOrder.getAddress());
                    double tex = (Integer.parseInt(myOrder.getTotalAmt()) / 100.0f) * myOrder.getTax();
                    txtTax.setText(sessionManager.getStringData(CURRUNCY) + new DecimalFormat("##.##").format(tex));
                    txt_tattt.setText("Tax(" + myOrder.getTax() + " %):");
                    double a = Integer.parseInt(myOrder.getTotalAmt()) - tex;
                    txtSubtotal.setText("" + sessionManager.getStringData(CURRUNCY) + new DecimalFormat("##.##").format(a));
                    txtTotal.setText("" + sessionManager.getStringData(CURRUNCY) + myOrder.getTotalAmt());
                    oid = myOrder.getmOrderid();
                    if (myOrder.getmIsrated().equalsIgnoreCase("No") && myOrder.getStatus().equalsIgnoreCase("completed")) {
                        item.setVisible(true);
                    } else {
                        item.setVisible(false);
                    }
                    if (myOrder.getStatus().equalsIgnoreCase("processing") || myOrder.getStatus().equalsIgnoreCase(getResources().getString(R.string.pic_myslf))) {
                        itemC.setVisible(true);
                    } else {
                        itemC.setVisible(false);
                    }
                    if ( myOrder.getpMethod().equalsIgnoreCase(getResources().getString(R.string.pic_myslf))) {
                        timeView.setVisibility(View.GONE);
                    }

                    if (myOrder.getStatus().equalsIgnoreCase("Pending")) {
                        timeView.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                    } else if (myOrder.getStatus().equalsIgnoreCase("processing")) {
                        timeView.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    } else if (myOrder.getStatus().equalsIgnoreCase("completed")) {
                        timeView.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    } else if (myOrder.getStatus().equalsIgnoreCase("cancelled")) {
                        timeView.setVisibility(View.GONE);
                    }
                    setOrderList(lvlItems, list);
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(MyOrderListActivity.this, response.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (response.getResult().equalsIgnoreCase("true")) {
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOrderList(LinearLayout lnrView, List<Productinfo> list) {
        lnrView.removeAllViews();
        int a = 0;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(MyOrderListActivity.this);
                a = a + 1;
                View view = inflater.inflate(R.layout.custome_myoder, null);
                ImageView img_icon = view.findViewById(R.id.img_icon);
                TextView txt_name = view.findViewById(R.id.txt_name);
                TextView txt_qty = view.findViewById(R.id.txt_qty);
                TextView txt_weight = view.findViewById(R.id.txt_weight);
                TextView txt_price = view.findViewById(R.id.txt_price);
                Glide.with(MyOrderListActivity.this).load(APIClient.Base_URL + "/" + list.get(i).getProductImage()).thumbnail(Glide.with(MyOrderListActivity.this).load(R.drawable.lodingimage)).into(img_icon);
                txt_name.setText(" " + list.get(i).getProductName());
                txt_qty.setText(" Qty :" + list.get(i).getProductQty());
                txt_weight.setText(" " + list.get(i).getProductWeight());
                double ress = (Double.parseDouble(list.get(i).getProductPrice()) * list.get(i).getDiscount()) / 100;
                ress = Double.parseDouble(list.get(i).getProductPrice()) - ress;
                txt_price.setText(sessionManager.getStringData(CURRUNCY) + ress);
                lnrView.addView(view);
            }
        }
    }

    MenuItem item;
    MenuItem itemC;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rates_menu, menu);
        item = menu.findItem(R.id.item_rates);
        itemC = menu.findItem(R.id.item_cancel);
        item.setVisible(false);
        itemC.setVisible(false);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_rates:
                startActivity(new Intent(MyOrderListActivity.this, RatesActivity.class).putExtra("oid", oid));
                return true;
            case R.id.item_cancel:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phonecall));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);

                    return true;
                }
                startActivity(intent);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (item != null && ISRATES) {
            ISRATES = false;
            item.setVisible(false);
        }

    }
}
