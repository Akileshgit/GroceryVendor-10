package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import Config.BaseURL;
import Model.Top_Selling_model;
import vendor.tcc.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class Top_Selling_Adapter extends RecyclerView.Adapter<Top_Selling_Adapter.MyViewHolder> {

    private List<Top_Selling_model> modelList;
    private Context context;
SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_nmae, product_prize;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            product_nmae = (TextView) view.findViewById(R.id.product_name);
            product_prize = (TextView) view.findViewById(R.id.product_prize);
            image = (ImageView) view.findViewById(R.id.iv_icon);
        }
    }

    public Top_Selling_Adapter(List<Top_Selling_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Top_Selling_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_top_selling, parent, false);
        context = parent.getContext();
        return new Top_Selling_Adapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(Top_Selling_Adapter.MyViewHolder holder, int position) {
        Top_Selling_model mList = modelList.get(position);
         preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
    String language=preferences.getString("language","");
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        holder.product_prize.setText(context.getResources().getString(R.string.tv_toolbar_price) + context.getResources().getString(R.string.currency) + mList.getPrice());

        if (language.contains("english")) {
            holder.product_nmae.setText(mList.getProduct_name());
        }
        else {
            holder.product_nmae.setText(mList.getProduct_name_arb());

        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

