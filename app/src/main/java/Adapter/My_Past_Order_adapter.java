package Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import Model.My_Past_order_model;
import vendor.tcc.R;

import static android.content.Context.MODE_PRIVATE;

public class My_Past_Order_adapter extends RecyclerView.Adapter<My_Past_Order_adapter.MyViewHolder> {

    private List<My_Past_order_model> modelList;
    private LayoutInflater inflater;
    private Fragment currentFragment;
SharedPreferences preferences;
    private Context context;

    public My_Past_Order_adapter(Context context, List<My_Past_order_model> modemodelList, final Fragment currentFragment) {

        this.context = context;
        this.modelList = modelList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentFragment = currentFragment;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderno, tv_status,  tv_price, tv_item, relativetextstatus;

        public View view1, view2, view3, view4, view5, view6;
        public RelativeLayout relative_background;
        public ImageView Confirm, Out_For_Deliverde, Delivered;
        CardView cardView;
        public TextView tv_methid1;
        public String method;


        public MyViewHolder(View view) {
            super(view);
            tv_orderno = (TextView) view.findViewById(R.id.tv_order_no);
            tv_status = (TextView) view.findViewById(R.id.tv_order_status);
            relativetextstatus = (TextView) view.findViewById(R.id.status);
            tv_price = (TextView) view.findViewById(R.id.tv_order_price);
            tv_item = (TextView) view.findViewById(R.id.tv_order_item);
            cardView = view.findViewById(R.id.card_view);


//            //Payment Method
            tv_methid1 = (TextView) view.findViewById(R.id.method1);
            //Date And Time

            //Oredre Tracking
            view1 = (View) view.findViewById(R.id.view1);
            view2 = (View) view.findViewById(R.id.view2);
            view3 = (View) view.findViewById(R.id.view3);
            view4 = (View) view.findViewById(R.id.view4);
            view5 = (View) view.findViewById(R.id.view5);
            view6 = (View) view.findViewById(R.id.view6);
            relative_background = (RelativeLayout) view.findViewById(R.id.relative_background);

            Confirm = (ImageView) view.findViewById(R.id.confirm_image);
            Out_For_Deliverde = (ImageView) view.findViewById(R.id.delivered_image);
            Delivered = (ImageView) view.findViewById(R.id.cancal_image);

        }
    }

    public My_Past_Order_adapter(List<My_Past_order_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public My_Past_Order_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_past_order_rv, parent, false);
        context = parent.getContext();
        return new My_Past_Order_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        My_Past_order_model mList = modelList.get(position);

        holder.tv_orderno.setText(mList.getSale_id());

        if (mList.getStatus().equals("0")) {
            holder.tv_status.setText(context.getResources().getString(R.string.pending));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.pending));
            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.color_2));
        } else if (mList.getStatus().equals("1")) {
            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.view2.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.orange));
            holder.Confirm.setImageResource(R.color.green);
            holder.tv_status.setText(context.getResources().getString(R.string.confirm));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.confirm));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
        } else if (mList.getStatus().equals("2")) {
            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.purple));
            holder.view2.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.view3.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.view4.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.view5.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.view6.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.Confirm.setImageResource(R.color.green);
            holder.Out_For_Deliverde.setImageResource(R.color.green);
            holder.tv_status.setText(context.getResources().getString(R.string.outfordeliverd));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.outfordeliverd));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
        }else if (mList.getStatus().equals("4")) {
            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.view2.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.view3.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.view4.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.view5.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.view6.setBackgroundColor(context.getResources().getColor(R.color.green));
            holder.Confirm.setImageResource(R.color.green);
            holder.Out_For_Deliverde.setImageResource(R.color.green);
            holder.Delivered.setImageResource(R.color.green);
            holder.tv_status.setText(context.getResources().getString(R.string.delivered));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.delivered));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.green));
        }

        holder.tv_methid1.setText(mList.getPayment_method());


        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");


        holder.tv_price.setText(context.getResources().getString(R.string.currency) + mList.getTotal_amount());
        holder.tv_item.setText(context.getResources().getString(R.string.tv_cart_item) + mList.getTotal_items());
//        holder.tv_pending_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());

    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
