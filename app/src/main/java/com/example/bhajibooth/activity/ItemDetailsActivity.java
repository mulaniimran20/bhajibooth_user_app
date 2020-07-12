package com.example.bhajibooth.activity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.bhajibooth.retrofit.APIClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.bhajibooth.R;
import com.example.bhajibooth.database.DatabaseHelper;
import com.example.bhajibooth.database.MyCart;
import com.example.bhajibooth.model.Price;
import com.example.bhajibooth.model.ProductItem;
import com.example.bhajibooth.retrofit.APIClient;
import com.example.bhajibooth.utils.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.bhajibooth.fragment.ItemListFragment.itemListFragment;
import static com.example.bhajibooth.utils.SessionManager.CURRUNCY;

public class ItemDetailsActivity extends AppCompatActivity {
    ProductItem productItem;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_cart)
    ImageView imgCart;
    @BindView(R.id.txt_tcount)
    TextView txtTcount;
    @BindView(R.id.lvl_cart)
    RelativeLayout lvlCart;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_desc)
    TextView txtDesc;
    @BindView(R.id.lvl_pricelist)
    LinearLayout lvlPricelist;
    @BindView(R.id.btn_addtocart)
    TextView btnAddtocart;
    ArrayList<Price> priceslist;
    DatabaseHelper databaseHelper;
    SessionManager sessionManager;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.txt_item_offer)
    TextView txtItemOffer;
    @BindView(R.id.txt_seler)
    TextView txtSeler;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabview)
    TabLayout tabview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        databaseHelper = new DatabaseHelper(ItemDetailsActivity.this);
        productItem = (ProductItem) getIntent().getParcelableExtra("MyClass");
        priceslist = getIntent().getParcelableArrayListExtra("MyList");
        if (productItem != null) {
            txtTitle.setText("" + productItem.getProductName());
            txtDesc.setText("" + productItem.getShortDesc());
            txtSeler.setText("" + productItem.getSellerName());
            List<String> Arealist = new ArrayList<>();
            for (int i = 0; i < priceslist.size(); i++) {
                Arealist.add(priceslist.get(i).getProductType());
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, Arealist);
            spinner.setAdapter(dataAdapter);
            updateItem();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (productItem.getmDiscount() > 0) {
                    double res = (Double.parseDouble(priceslist.get(position).getProductPrice()) / 100.0f) * productItem.getmDiscount();
                    res = Integer.parseInt(priceslist.get(position).getProductPrice()) - res;
                    txtItemOffer.setText(sessionManager.getStringData(CURRUNCY) + priceslist.get(position).getProductPrice());
                    txtItemOffer.setPaintFlags(txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    txtPrice.setText(sessionManager.getStringData(CURRUNCY) + res);
                    txtItemOffer.setText(sessionManager.getStringData(CURRUNCY) + priceslist.get(position).getProductPrice());
                } else {
                    txtItemOffer.setVisibility(View.GONE);
                    txtPrice.setText(sessionManager.getStringData(CURRUNCY) + priceslist.get(position).getProductPrice());
                }
                setJoinPlayrList(lvlPricelist, productItem, priceslist.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> myList = new ArrayList<String>();
        myList.add(productItem.getProductImage());
        if (productItem.getProductRelatedImage() != null && productItem.getProductRelatedImage().length() != 0) {
            myList.addAll(Arrays.asList(productItem.getProductRelatedImage().split(",")));
            tabview.setupWithViewPager(viewPager, true);
        }
        MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(this, myList);
        viewPager.setAdapter(myCustomPagerAdapter);
    }

    public void updateItem() {
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            txtTcount.setText("0");
        } else {
            txtTcount.setText("" + res.getCount());
        }
    }

    @OnClick({R.id.img_back, R.id.lvl_cart, R.id.btn_addtocart})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.lvl_cart:
                fragment();
                break;
            case R.id.btn_addtocart:
                finish();
                break;
            default:
                break;
        }
    }

    private void setJoinPlayrList(LinearLayout lnrView, ProductItem datum, Price price) {

        lnrView.removeAllViews();
        final int[] count = {0};
        final DatabaseHelper helper = new DatabaseHelper(lnrView.getContext());
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custome_additem, null);
        final TextView txtcount = view.findViewById(R.id.txtcount);
        final LinearLayout lvl_addremove = view.findViewById(R.id.lvl_addremove);
        final LinearLayout lvl_addcart = view.findViewById(R.id.lvl_addcart);
        LinearLayout img_mins = view.findViewById(R.id.img_mins);
        LinearLayout img_plus = view.findViewById(R.id.img_plus);
        final MyCart myCart = new MyCart();
        myCart.setPID(datum.getId());
        myCart.setImage(datum.getProductImage());
        myCart.setTitle(datum.getProductName());
        myCart.setWeight(price.getProductType());
        myCart.setCost(price.getProductPrice());
        myCart.setDiscount(datum.getmDiscount());
        int qrt = helper.getCard(myCart.getPID(), myCart.getCost());
        if (qrt != -1) {
            count[0] = qrt;
            txtcount.setText("" + count[0]);
            lvl_addremove.setVisibility(View.VISIBLE);
            lvl_addcart.setVisibility(View.GONE);
        } else {
            lvl_addremove.setVisibility(View.GONE);
            lvl_addcart.setVisibility(View.VISIBLE);

        }
        img_mins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count[0] = Integer.parseInt(txtcount.getText().toString());

                count[0] = count[0] - 1;
                if (count[0] <= 0) {
                    lvl_addremove.setVisibility(View.GONE);
                    lvl_addcart.setVisibility(View.VISIBLE);
                    txtcount.setText("0");
                    helper.deleteRData(myCart.getPID(), myCart.getCost());
                } else {
                    txtcount.setVisibility(View.VISIBLE);
                    txtcount.setText("" + count[0]);
                    myCart.setQty(String.valueOf(count[0]));
                    helper.insertData(myCart);
                }
                updateItem();
                if (itemListFragment != null)
                    itemListFragment.updateItem();
            }
        });

        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count[0] = Integer.parseInt(txtcount.getText().toString());

                count[0] = count[0] + 1;
                txtcount.setText("" + count[0]);
                myCart.setQty(String.valueOf(count[0]));
                Log.e("INsert", "--> " + helper.insertData(myCart));
                updateItem();
                if (itemListFragment != null)
                    itemListFragment.updateItem();
            }
        });
        lvl_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvl_addcart.setVisibility(View.GONE);
                lvl_addremove.setVisibility(View.VISIBLE);
                count[0] = Integer.parseInt(txtcount.getText().toString());

                count[0] = count[0] + 1;
                txtcount.setText("" + count[0]);
                myCart.setQty(String.valueOf(count[0]));
                Log.e("INsert", "--> " + helper.insertData(myCart));
                updateItem();
                if (itemListFragment != null)
                    itemListFragment.updateItem();
            }
        });
        lnrView.addView(view);

    }

    public void fragment() {
        SessionManager.ISCART = true;
        finish();

    }

    public class MyCustomPagerAdapter extends PagerAdapter {
        Context context;
        List<String> imageList;
        LayoutInflater layoutInflater;

        public MyCustomPagerAdapter(Context context, List<String> bannerDatumList) {
            this.context = context;
            this.imageList = bannerDatumList;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.item_image, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            Glide.with(ItemDetailsActivity.this).load(APIClient.Base_URL + "/" + imageList.get(position)).placeholder(R.drawable.empty).into(imageView);
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
