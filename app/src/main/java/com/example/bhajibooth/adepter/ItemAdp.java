package com.example.bhajibooth.adepter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bhajibooth.R;
import com.example.bhajibooth.activity.ItemDetailsActivity;
import com.example.bhajibooth.database.DatabaseHelper;
import com.example.bhajibooth.database.MyCart;
import com.example.bhajibooth.model.Price;
import com.example.bhajibooth.model.ProductItem;
import com.example.bhajibooth.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.bhajibooth.retrofit.APIClient;
import com.example.bhajibooth.retrofit.GetResult;

import static com.example.bhajibooth.fragment.ItemListFragment.itemListFragment;
import static com.example.bhajibooth.utils.SessionManager.CURRUNCY;

public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {
    private List<ProductItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context mContext;
    SessionManager sessionManager;
    ProductItem datum;

    public ItemAdp(Context context, List<ProductItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        sessionManager = new SessionManager(mContext);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_custome, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
         datum = mData.get(position);
        Glide.with(mContext).load(APIClient.Base_URL + "/" + datum.getProductImage()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.imgIcon);
        holder.txtTitle.setText("" + datum.getProductName());
        holder.imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, ItemDetailsActivity.class).putExtra("MyClass", datum).putParcelableArrayListExtra("MyList", datum.getPrice()));
            }
        });
        if (datum.getmDiscount() > 0) {
            holder.lvlOffer.setVisibility(View.VISIBLE);
            holder.txtOffer.setText(datum.getmDiscount() + "% Off");
        } else {
            holder.lvlOffer.setVisibility(View.GONE);
        }
        List<String> Arealist = new ArrayList<>();
        for (int i = 0; i < datum.getPrice().size(); i++) {

            Arealist.add(datum.getPrice().get(i).getProductType());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, Arealist);
        dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
        holder.spinner.setAdapter(dataAdapter);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (datum.getmDiscount() > 0) {
                    double res = (Double.parseDouble(datum.getPrice().get(position).getProductPrice()) / 100.0f) * datum.getmDiscount();
                    res = Integer.parseInt(datum.getPrice().get(position).getProductPrice()) - res;
                    holder.txtItemOffer.setText(sessionManager.getStringData(CURRUNCY) + datum.getPrice().get(position).getProductPrice());
                    holder.txtItemOffer.setPaintFlags(holder.txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.txtPrice.setText(sessionManager.getStringData(CURRUNCY) + res);
                    holder.txtItemOffer.setText(sessionManager.getStringData(CURRUNCY) + datum.getPrice().get(position).getProductPrice());
                } else {
                    holder.txtItemOffer.setVisibility(View.GONE);
                    holder.txtPrice.setText(sessionManager.getStringData(CURRUNCY) + datum.getPrice().get(position).getProductPrice());
                }
                setJoinPlayrList(holder.lvlSubitem, datum, datum.getPrice().get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txt_offer)
        TextView txtOffer;
        @BindView(R.id.txt_item_offer)
        TextView txtItemOffer;
        @BindView(R.id.txt_price)
        TextView txtPrice;
        @BindView(R.id.lvl_subitem)
        LinearLayout lvlSubitem;
        @BindView(R.id.lvl_offer)
        LinearLayout lvlOffer;
        @BindView(R.id.spinner)
        Spinner spinner;
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    private void setJoinPlayrList(LinearLayout lnrView, ProductItem datum, Price price) {
        lnrView.removeAllViews();
        final int[] count = {0};
        final DatabaseHelper helper = new DatabaseHelper(lnrView.getContext());
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.custome_prize, null);
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
                    txtcount.setText("" + count[0]);
                    lvl_addremove.setVisibility(View.GONE);
                    lvl_addcart.setVisibility(View.VISIBLE);
                    helper.deleteRData(myCart.getPID(), myCart.getCost());
                } else {
                    txtcount.setVisibility(View.VISIBLE);
                    txtcount.setText("" + count[0]);
                    myCart.setQty(String.valueOf(count[0]));
                    helper.insertData(myCart);
                }
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
                itemListFragment.updateItem();
            }
        });
        lnrView.addView(view);

    }
}