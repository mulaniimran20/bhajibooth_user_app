package com.example.bhajibooth.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.example.bhajibooth.R;
import com.example.bhajibooth.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.bhajibooth.utils.SessionManager.ABOUT_US;

public class AboutsActivity extends BaseActivity {

    @BindView(R.id.txt_about)
    TextView txtAbout;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abouts);
        ButterKnife.bind(this);
        sessionManager=new SessionManager(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtAbout.setText(Html.fromHtml(sessionManager.getStringData(ABOUT_US), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtAbout.setText(Html.fromHtml(sessionManager.getStringData(ABOUT_US)));
        }
    }
}
