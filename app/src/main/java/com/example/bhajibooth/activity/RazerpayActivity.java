package com.example.bhajibooth.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bhajibooth.R;
import com.example.bhajibooth.model.PaymentItem;
import com.example.bhajibooth.model.User;
import com.example.bhajibooth.utils.SessionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import butterknife.ButterKnife;

import static com.example.bhajibooth.fragment.OrderSumrryFragment.TragectionID;
import static com.example.bhajibooth.fragment.OrderSumrryFragment.paymentsucsses;


public class RazerpayActivity extends AppCompatActivity implements PaymentResultListener {
    SessionManager sessionManager;
    double amount = 0;
    User user;
    PaymentItem paymentItem;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_razorpay);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");
        amount = getIntent().getIntExtra("amount", 0);
        paymentItem = (PaymentItem) getIntent().getSerializableExtra("detail");
        startPayment(String.valueOf(amount));
    }

    public void startPayment(String amount) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID(paymentItem.getCredValue());
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Demo");
            options.put("currency", "INR");
            double total = Double.parseDouble(amount);
            total = total * 100;
            options.put("amount", total);
            JSONObject preFill = new JSONObject();
            preFill.put("email", user.getEmail());
            preFill.put("contact", user.getMobile());
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPaymentSuccess(String s) {
        TragectionID = s;
        paymentsucsses = 1;
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        finish();
    }
}