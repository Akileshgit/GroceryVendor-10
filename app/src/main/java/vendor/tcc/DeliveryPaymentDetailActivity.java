package vendor.tcc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import Config.BaseURL;
import Config.SharedPref;
import util.ConnectivityReceiver;
import util.DatabaseHandler;
import util.Session_management;

public class DeliveryPaymentDetailActivity extends AppCompatActivity {

    private TextView tv_timeslot, tv_address, tv_total;
    private LinearLayout btn_order;

    private String getlocation_id = "";
    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    private String getstore_id = "";

    private int deli_charges;
    Double total;
    SharedPreferences preferences;
    private DatabaseHandler db_cart;
    private Session_management sessionManagement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_payment_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionbar.setTitle("Review");

        db_cart = new DatabaseHandler(DeliveryPaymentDetailActivity.this);
        sessionManagement = new Session_management(DeliveryPaymentDetailActivity.this);

        tv_timeslot = (TextView) findViewById(R.id.textTimeSlot);
        tv_address = (TextView) findViewById(R.id.txtAddress);
        //tv_item = (TextView) view.findViewById(R.id.textItems);
        //tv_total = (TextView) view.findViewById(R.id.textPrice);
        tv_total = (TextView) findViewById(R.id.txtTotal);

        btn_order = (LinearLayout) findViewById(R.id.btn_order_now);

        getdate = getIntent().getStringExtra("getdate");

        preferences = getSharedPreferences("lan", MODE_PRIVATE);
        String language=preferences.getString("language","");
        if (language.contains("spanish")) {
            gettime = getIntent().getStringExtra("time");

            gettime=gettime.replace("PM","م");
            gettime=gettime.replace("AM","ص");

        }else {
            gettime = getIntent().getStringExtra("time");

        }
        getlocation_id = getIntent().getStringExtra("location_id");
        getstore_id = getIntent().getStringExtra("store_id");
        deli_charges = Integer.parseInt(getIntent().getStringExtra("deli_charges"));
        String getaddress = getIntent().getStringExtra("address");

        tv_timeslot.setText(getdate + " " + gettime);
        tv_address.setText(getaddress);

        total = Double.parseDouble(db_cart.getTotalAmount()) + deli_charges;

        //tv_total.setText("" + db_cart.getTotalAmount());
        //tv_item.setText("" + db_cart.getWishlistCount());
        tv_total.setText(getResources().getString(R.string.tv_cart_item) + db_cart.getCartCount() + "\n" +
                getResources().getString(R.string.amount) + db_cart.getTotalAmount() + "\n" +
                getResources().getString(R.string.delivery_charge) + deli_charges + "\n" +
                getResources().getString(R.string.total_amount) +
                db_cart.getTotalAmount() + " + " + deli_charges + " = " + total+ getResources().getString(R.string.currency));


        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectivityReceiver.isConnected()) {
                    Intent intent = new Intent(DeliveryPaymentDetailActivity.this, PaymentActivity.class);
                    intent.putExtra("total", String.valueOf(total));
                    intent.putExtra("getdate", getdate);
                    intent.putExtra("gettime", gettime);
                    intent.putExtra("getlocationid", getlocation_id);
                    intent.putExtra("getstoreid", getstore_id);
                    startActivity(intent);

                    SharedPref.putString(DeliveryPaymentDetailActivity.this, BaseURL.TOTAL_AMOUNT, String.valueOf(total));
                } else {
                    //((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
