package com.example.bhajibooth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhajibooth.R;
import com.example.bhajibooth.activity.HomeActivity;
import com.example.bhajibooth.adepter.SubCategoryAdp;
import com.example.bhajibooth.model.SubCategory;
import com.example.bhajibooth.model.SubcatItem;
import com.example.bhajibooth.model.User;
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
import butterknife.Unbinder;
import retrofit2.Call;
import com.example.bhajibooth.retrofit.APIClient;
import com.example.bhajibooth.retrofit.GetResult;

import static com.example.bhajibooth.activity.HomeActivity.homeActivity;

public class SubCategoryFragment extends Fragment implements GetResult.MyListener, SubCategoryAdp.RecyclerTouchListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.txt_titel)
    TextView txtTitel;
    SubCategoryAdp adapter;
    List<SubcatItem> categoryList;
    Unbinder unbinder;
    SessionManager sessionManager;
    User user;
    int CID = 0;

    public SubCategoryFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SubCategoryFragment newInstance(String param1, String param2) {
        SubCategoryFragment fragment = new SubCategoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle b = getArguments();
        CID = b.getInt("id");
        String titel = b.getString("titel");
        if (titel != null) {
            txtTitel.setText("" + titel);
        } else {
            txtTitel.setVisibility(View.GONE);
        }
        HomeActivity.getInstance().setFrameMargin(60);
        categoryList = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails("");
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getSubCategory();
        return view;
    }

    @Override
    public void onClickItem(View v, int cid, int scid) {
        homeActivity.ShowMenu();
        Bundle args = new Bundle();
        args.putInt("cid", cid);
        args.putInt("scid", scid);
        Fragment fragment = new ItemListFragment();
        fragment.setArguments(args);
        HomeActivity.getInstance().callFragment(fragment);
    }

    @Override
    public void onLongClickItem(View v, int position) {
    }

    private void getSubCategory() {
        HomeActivity.custPrograssbar.PrograssCreate();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("category_id", CID);
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getSubcategory((JsonObject) jsonParser.parse(jsonObject.toString()));
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
            HomeActivity.custPrograssbar.ClosePrograssBar();
            if (callNo.equalsIgnoreCase("1") && result.toString() != null) {
                Gson gson = new Gson();
                SubCategory category = gson.fromJson(result.toString(), SubCategory.class);
                categoryList = category.getData();
                if (categoryList.size() != 0) {
                    adapter = new SubCategoryAdp(getActivity(), categoryList, this);
                    recyclerView.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.getInstance().serchviewShow();
        HomeActivity.getInstance().setFrameMargin(60);
        if (user != null)
            HomeActivity.getInstance().titleChange("Hello " + user.getName());

    }
}
