package com.example.bhajibooth.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bhajibooth.R;
import com.example.bhajibooth.activity.HomeActivity;
import com.example.bhajibooth.activity.LoginActivity;
import com.example.bhajibooth.database.DatabaseHelper;
import com.example.bhajibooth.database.MyCart;
import com.example.bhajibooth.retrofit.APIClient;
import com.example.bhajibooth.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.example.bhajibooth.utils.SessionManager.CURRUNCY;
import static com.example.bhajibooth.utils.SessionManager.LOGIN;
import static com.example.bhajibooth.utils.SessionManager.O_MIN;


public class CardFragment extends Fragment {

    @BindView(R.id.lvlmucard)
    LinearLayout lvlmucard;
    Unbinder unbinder;
    DatabaseHelper databaseHelper;
    List<MyCart> myCarts;
    @BindView(R.id.txt_item)
    TextView txtItem;
    @BindView(R.id.totleAmount)
    TextView totleAmount;
    @BindView(R.id.txt_countinue)
    TextView txtCountinue;
    @BindView(R.id.txt_empty)
    TextView txtEmpty;
    SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        unbinder = ButterKnife.bind(this, view);
        databaseHelper = new DatabaseHelper(getActivity());
        sessionManager = new SessionManager(getActivity());
        HomeActivity.getInstance().serchviewShow();
        myCarts = new ArrayList<>();
        Cursor res = databaseHelper.getAllData();
        if (res.getCount() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
            txtCountinue.setVisibility(View.GONE);

        }
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setID(res.getString(0));
            rModel.setPID(res.getString(1));
            rModel.setImage(res.getString(2));
            rModel.setTitle(res.getString(3));
            rModel.setWeight(res.getString(4));
            rModel.setCost(res.getString(5));
            rModel.setQty(res.getString(6));
            rModel.setDiscount(res.getInt(7));
            myCarts.add(rModel);
        }
        setJoinPlayrList(lvlmucard, myCarts);
        return view;
    }

    double total = 0;

    private void setJoinPlayrList(final LinearLayout lnrView, final List<MyCart> myCarts) {
        lnrView.removeAllViews();
        final int[] count = {0};
        final double[] totalAmount = {0};
        final DatabaseHelper helper = new DatabaseHelper(lnrView.getContext());
        if (myCarts != null && myCarts.size() > 0) {
            for (int i = 0; i < myCarts.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final MyCart cart = myCarts.get(i);
                final View view = inflater.inflate(R.layout.custome_mycard, null);
                ImageView img_icon = view.findViewById(R.id.img_icon);
                ImageView img_delete = view.findViewById(R.id.img_delete);
                TextView txt_title = view.findViewById(R.id.txt_title);
                TextView txt_gram = view.findViewById(R.id.txt_gram);
                TextView txt_price = view.findViewById(R.id.txt_price);
                final TextView txtcount = view.findViewById(R.id.txtcount);
                final LinearLayout img_mins = view.findViewById(R.id.img_mins);
                LinearLayout img_plus = view.findViewById(R.id.img_plus);

                Glide.with(getActivity()).load(APIClient.Base_URL + "/" + cart.getImage()).thumbnail(Glide.with(getActivity()).load(R.drawable.lodingimage)).into(img_icon);
                double res = (Integer.parseInt(cart.getCost()) * myCarts.get(i).getDiscount()) / 100;
                res = Integer.parseInt(cart.getCost()) - res;
                txt_gram.setText("  " + cart.getWeight() + "  ");
                txt_price.setText(sessionManager.getStringData(CURRUNCY) + res);
                txt_title.setText("" + cart.getTitle());

                final MyCart myCart = new MyCart();
                myCart.setPID(cart.getPID());
                myCart.setImage(cart.getImage());
                myCart.setTitle(cart.getTitle());
                myCart.setWeight(cart.getWeight());
                myCart.setCost(cart.getCost());
                myCart.setDiscount(cart.getDiscount());
                int qrt = helper.getCard(myCart.getPID(), myCart.getCost());
                if (qrt != -1) {
                    count[0] = qrt;
                    txtcount.setText("" + count[0]);
                    txtcount.setVisibility(View.VISIBLE);
                } else {
                    txtcount.setVisibility(View.INVISIBLE);
                    img_mins.setVisibility(View.INVISIBLE);
                }
                double ress = (Double.parseDouble(myCart.getCost()) / 100.0f) * myCart.getDiscount();
                ress = Integer.parseInt(myCart.getCost()) - ress;
                double temp = ress * qrt;
                totalAmount[0] = totalAmount[0] + temp;
                img_mins.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count[0] = Integer.parseInt(txtcount.getText().toString());
                        count[0] = count[0] - 1;
                        if (count[0] <= 0) {
                            txtcount.setVisibility(View.INVISIBLE);
                            img_mins.setVisibility(View.INVISIBLE);
                            txtcount.setText("" + count[0]);
                            helper.deleteRData(myCart.getPID(), myCart.getCost());
                            lnrView.removeView(view);
                            myCarts.remove(cart);
                            totalAmount[0] = totalAmount[0] - Integer.parseInt(myCart.getCost());
                            Toast.makeText(getActivity(), "" + myCart.getTitle() + " " + myCart.getWeight() + " is Remove", Toast.LENGTH_LONG).show();
                            if (totalAmount[0] == 0) {
                                txtCountinue.setVisibility(View.GONE);
                            }
                            updateItem();
                        } else {
                            txtcount.setVisibility(View.VISIBLE);
                            txtcount.setText("" + count[0]);
                            myCart.setQty(String.valueOf(count[0]));
                            totalAmount[0] = totalAmount[0] - Integer.parseInt(myCart.getCost());
                            helper.insertData(myCart);
                            updateItem();
                        }
                    }
                });
                img_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtcount.setVisibility(View.VISIBLE);
                        img_mins.setVisibility(View.VISIBLE);
                        count[0] = Integer.parseInt(txtcount.getText().toString());
                        totalAmount[0] = totalAmount[0] + Integer.parseInt(myCart.getCost());
                        count[0] = count[0] + 1;
                        txtcount.setText("" + count[0]);
                        myCart.setQty(String.valueOf(count[0]));
                        updateItem();
                    }
                });
                img_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog myDelete = new AlertDialog.Builder(getActivity())
                                .setTitle("Delete")
                                .setMessage("Do you want to Delete")
                                .setIcon(R.drawable.ic_delete)
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        Log.d("sdj", "" + whichButton);
                                        dialog.dismiss();
                                        totalAmount[0] = totalAmount[0] - Integer.parseInt(myCart.getCost());
                                        helper.deleteRData(myCart.getPID(), myCart.getCost());
                                        myCarts.remove(cart);
                                        updateItem();
                                        lnrView.removeView(view);
                                    }

                                })
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("sdj", "" + which);
                                        dialog.dismiss();
                                    }
                                })
                                .create();
                        myDelete.show();
                    }
                });
                lnrView.addView(view);
            }
        }
        total = totalAmount[0];
        updateItem();
    }

    public void updateItem() {
        Cursor res = databaseHelper.getAllData();
        double totalRs = 0;
        double ress = 0;
        int totalItem = 0;
        if (res.getCount() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
        }
        while (res.moveToNext()) {
            MyCart rModel = new MyCart();
            rModel.setCost(res.getString(5));
            rModel.setQty(res.getString(6));
            rModel.setDiscount(res.getInt(7));
            ress = (Integer.parseInt(res.getString(5)) * rModel.getDiscount()) / 100;
            ress = Integer.parseInt(res.getString(5)) - ress;
            double temp = Integer.parseInt(res.getString(6)) * ress;
            totalRs = totalRs + temp;
            totalItem = totalItem + Integer.parseInt(res.getString(6));

        }
        total = Double.parseDouble(String.valueOf(totalRs));
        txtItem.setText(totalItem + " Items");
        totleAmount.setText(sessionManager.getStringData(CURRUNCY) + totalRs);
        HomeActivity.getInstance().setFrameMargin(60);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.txt_countinue)
    public void onViewClicked() {

        if (sessionManager.getBooleanData(LOGIN)) {
            if (sessionManager.getIntData(O_MIN) <= total) {
                HomeActivity.getInstance().serchviewHide();
                HomeActivity.getInstance().titleChange("Placed Order Now");
                PlaceOrderFragment fragment = new PlaceOrderFragment();
                HomeActivity.getInstance().callFragment(fragment);
            } else {
                Toast.makeText(getActivity(), "Minimum order value of " + sessionManager.getStringData(CURRUNCY) + " " + sessionManager.getIntData(O_MIN), Toast.LENGTH_SHORT).show();
            }
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.getInstance().serchviewShow();
        HomeActivity.getInstance().setFrameMargin(60);
        HomeActivity.getInstance().titleChange("MyCart");

    }
}
