package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Model.Deal_Of_Day_model;
import vendor.tcc.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class Deal_OfDay_Adapter extends RecyclerView.Adapter<Deal_OfDay_Adapter.MyViewHolder> {

    private List<Deal_Of_Day_model> modelList;
    private Context context;
    public int counter;
    SharedPreferences preferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_nmae, product_prize, offer_product_prize, start_time, end_time,offer_textview;
        public ImageView image;
      LinearLayout showproduct;
        public MyViewHolder(View view) {
            super(view);

            product_nmae = (TextView) view.findViewById(R.id.product_name);
            product_prize = (TextView) view.findViewById(R.id.product_prize);
            offer_product_prize = (TextView) view.findViewById(R.id.offer_product_prize);
            start_time = (TextView) view.findViewById(R.id.start_time);
            end_time = (TextView) view.findViewById(R.id.end_time);
            offer_textview = (TextView) view.findViewById(R.id.ofer_textview);
            showproduct=(LinearLayout) view.findViewById(R.id.showproduct);
            product_prize.setPaintFlags(product_prize.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            image = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }

    public Deal_OfDay_Adapter(List<Deal_Of_Day_model> modelList, Activity activity) {
        this.modelList = modelList;
    }

    @Override
    public Deal_OfDay_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_deal_of_the_day, parent, false);

        context = parent.getContext();

        return new Deal_OfDay_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Deal_OfDay_Adapter.MyViewHolder holder, int position) {
        Deal_Of_Day_model mList = modelList.get(position);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");

         if (mList.getStatus().equals("1")) {

            holder.offer_product_prize.setText(context.getResources().getString(R.string.currency) + mList.getDeal_price());
            holder.offer_product_prize.setTextColor(context.getResources().getColor(R.color.green));
            holder.offer_textview.setTextColor(context.getResources().getColor(R.color.green));
            holder.end_time.setText(mList.getEnd_time());
            holder.end_time.setTextColor(context.getResources().getColor(R.color.green));

        } else if (mList.getStatus().equals("0")) {

            holder.offer_product_prize.setText("Expired");
            holder.offer_product_prize.setTextColor(context.getResources().getColor(R.color.color_3));
            holder.offer_textview.setTextColor(context.getResources().getColor(R.color.color_3));
            holder.end_time.setText("Expired");
            holder.end_time.setTextColor(context.getResources().getColor(R.color.color_3));

        }


        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        holder.product_prize.setText(context.getResources().getString(R.string.currency) + mList.getPrice());
        if (language.contains("english")) {
            holder.product_nmae.setText(mList.getProduct_name());
            holder.product_prize.setText( context.getResources().getString(R.string.currency) + mList.getPrice());
        }
        else {
            holder.product_nmae.setText(mList.getProduct_name_arb());
            holder.product_prize.setText(context.getResources().getString(R.string.currency) + mList.getPrice());

        }
        holder.start_time.setText(mList.getStart_time());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


}

