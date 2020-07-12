package com.example.bhajibooth.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.bhajibooth.R;
import com.example.bhajibooth.activity.HomeActivity;
import com.example.bhajibooth.activity.ItemDetailsActivity;
import com.example.bhajibooth.adepter.CategoryAdp;
import com.example.bhajibooth.adepter.ReletedItemAdp;
import com.example.bhajibooth.adepter.ReletedItemDaynamicAdp;
import com.example.bhajibooth.model.BannerItem;
import com.example.bhajibooth.model.CatItem;
import com.example.bhajibooth.model.DynamicData;
import com.example.bhajibooth.model.Home;
import com.example.bhajibooth.model.ProductItem;
import com.example.bhajibooth.model.User;
import com.example.bhajibooth.utils.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import com.example.bhajibooth.retrofit.APIClient;
import com.example.bhajibooth.retrofit.GetResult;

import static com.example.bhajibooth.activity.HomeActivity.homeActivity;
import static com.example.bhajibooth.activity.HomeActivity.txtNoti;
import static com.example.bhajibooth.utils.SessionManager.ABOUT_US;
import static com.example.bhajibooth.utils.SessionManager.CONTACT_US;
import static com.example.bhajibooth.utils.SessionManager.CURRUNCY;
import static com.example.bhajibooth.utils.SessionManager.O_MIN;
import static com.example.bhajibooth.utils.SessionManager.PRIVACY;
import static com.example.bhajibooth.utils.SessionManager.RAZ_KEY;
import static com.example.bhajibooth.utils.SessionManager.TAX;
import static com.example.bhajibooth.utils.SessionManager.TREMSCODITION;
import static com.example.bhajibooth.utils.Utiles.ProductDatum;


public class HomeFragment extends Fragment implements CategoryAdp.RecyclerTouchListener, ReletedItemAdp.ItemClickListener, GetResult.MyListener, ReletedItemDaynamicAdp.ItemClickListener {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabview)
    TabLayout tabview;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_releted)
    RecyclerView recyclerReleted;
    @BindView(R.id.lvl_selected)
    LinearLayout lvlSelected;
    Unbinder unbinder;
    private Context mContext;
    CategoryAdp adapter;
    ReletedItemAdp adapterReletedi;
    List<CatItem> categoryList;
    List<BannerItem> bannerDatumList;
    public  HomeFragment HomeListFragment;
    SessionManager sessionManager;
    User user;
    List<DynamicData> dynamicDataList = new ArrayList<>();
    ReletedItemAdp reletedItemAdp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        bannerDatumList = new ArrayList<>();
        sessionManager = new SessionManager(mContext);
        HomeListFragment = this;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(mContext);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerReleted.setLayoutManager(mLayoutManager1);
        categoryList = new ArrayList<>();
        adapter = new CategoryAdp(mContext, categoryList, this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapterReletedi = new ReletedItemAdp(mContext, ProductDatum, this);
        recyclerReleted.setItemAnimator(new DefaultItemAnimator());
        recyclerReleted.setAdapter(adapterReletedi);
        user = sessionManager.getUserDetails("");
        getHome();
        return view;
    }

    private void setJoinPlayrList(LinearLayout lnrView, List<DynamicData> dataList) {

        lnrView.removeAllViews();
        for (int i = 0; i < dataList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_home_item, null);
            TextView itemTitle = view.findViewById(R.id.itemTitle);
            RecyclerView recycler_view_list = view.findViewById(R.id.recycler_view_list);
            itemTitle.setText(dataList.get(i).getTitle());
            ReletedItemDaynamicAdp itemAdp = new ReletedItemDaynamicAdp(mContext, dataList.get(i).getDynamicItems(), this);
            recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            recycler_view_list.setAdapter(itemAdp);
            lnrView.addView(view);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClickItem(String titel, int position) {
        homeActivity.ShowMenu();
        Bundle args = new Bundle();
        args.putInt("id", position);
        args.putString("titel", titel);
        Fragment fragment = new SubCategoryFragment();
        fragment.setArguments(args);
        HomeActivity.getInstance().callFragment(fragment);
    }
    @Override
    public void onLongClickItem(View v, int position) {
        Log.e("posiotn",""+position);
    }
    @Override
    public void onItemClick(ProductItem productItem, int position) {
        mContext.startActivity(new Intent(mContext, ItemDetailsActivity.class).putExtra("MyClass", productItem).putParcelableArrayListExtra("MyList", productItem.getPrice()));
    }
    @OnClick({R.id.txt_viewll, R.id.txt_viewllproduct})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_viewll:
                CategoryFragment fragment = new CategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("arraylist", (Serializable) categoryList);
                fragment.setArguments(bundle);
                HomeActivity.getInstance().callFragment(fragment);
                break;
            case R.id.txt_viewllproduct:
                PopularFragment fragmentp = new PopularFragment();
                HomeActivity.getInstance().callFragment(fragmentp);
                break;
            default:

                break;
        }
    }


    public class MyCustomPagerAdapter extends PagerAdapter {
        Context context;
        List<BannerItem> bannerDatumList;
        LayoutInflater layoutInflater;

        public MyCustomPagerAdapter(Context context, List<BannerItem> bannerDatumList) {
            this.context = context;
            this.bannerDatumList = bannerDatumList;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return bannerDatumList.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.item_banner, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            System.out.println(APIClient.Base_URL + "/" + bannerDatumList.get(position).getBimg());
            Glide.with(mContext).load(APIClient.Base_URL + "/" + bannerDatumList.get(position).getBimg()).placeholder(R.drawable.empty).into(imageView);
            container.addView(itemView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!bannerDatumList.get(position).getCid().equalsIgnoreCase("0")) {
                        homeActivity.ShowMenu();
                        Bundle args = new Bundle();
                        args.putInt("id", Integer.parseInt(bannerDatumList.get(position).getCid()));
                        Fragment fragment = new SubCategoryFragment();
                        fragment.setArguments(args);
                        HomeActivity.getInstance().callFragment(fragment);
                    }
                }
            });
            return itemView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
    private void getHome() {
        HomeActivity.custPrograssbar.PrograssCreate();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getHome((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "homepage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.getInstance().setdata();
        HomeActivity.getInstance().setFrameMargin(60);
        HomeActivity.getInstance().serchviewShow();
        if (user != null)
            HomeActivity.getInstance().titleChange("Hello " + user.getName());

        if (dynamicDataList != null) {
            setJoinPlayrList(lvlSelected, dynamicDataList);
        }
        if (reletedItemAdp != null) {
            reletedItemAdp.notifyDataSetChanged();
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void  callback(JsonObject result, String callNo) {
        try {
            result.toString();
            HomeActivity.custPrograssbar.ClosePrograssBar();
            Gson gson = new Gson();
            Home home = gson.fromJson(result.toString(), Home.class);

            categoryList.addAll(home.getResultHome().getCatItems());
            adapter.notifyDataSetChanged();

            bannerDatumList.addAll(home.getResultHome().getBannerItems());
            MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(mContext, bannerDatumList);
            viewPager.setAdapter(myCustomPagerAdapter);
            tabview.setupWithViewPager(viewPager, true);

            reletedItemAdp = new ReletedItemAdp(mContext, home.getResultHome().getProductItems(), this);
            recyclerReleted.setAdapter(reletedItemAdp);
            if (home.getResultHome().getRemainNotification() <= 0) {
                txtNoti.setVisibility(View.GONE);
            } else {
                txtNoti.setVisibility(View.VISIBLE);
                txtNoti.setText("" + home.getResultHome().getRemainNotification());
            }
            sessionManager.setStringData(CURRUNCY, home.getResultHome().getMain_Data().getCurrency());
            sessionManager.setStringData(PRIVACY, home.getResultHome().getMain_Data().getPrivacy_policy());
            sessionManager.setStringData(ABOUT_US, home.getResultHome().getMain_Data().getAbout_us());
            sessionManager.setStringData(CONTACT_US, home.getResultHome().getMain_Data().getContact_us());
            sessionManager.setStringData(TREMSCODITION, home.getResultHome().getMain_Data().getTerms());
            sessionManager.setIntData(O_MIN, home.getResultHome().getMain_Data().getO_min());
            sessionManager.setStringData(RAZ_KEY, home.getResultHome().getMain_Data().getRaz_key());
            sessionManager.setStringData(TAX, home.getResultHome().getMain_Data().getTax());

            ProductDatum = home.getResultHome().getProductItems();
            dynamicDataList = home.getResultHome().getDynamicData();
            setJoinPlayrList(lvlSelected, dynamicDataList);
        } catch (Exception e) {
            e.toString();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
