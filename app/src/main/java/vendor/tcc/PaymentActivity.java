package vendor.tcc;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Config.BaseURL;
import Config.SharedPref;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.DatabaseHandler;
import util.Session_management;
import vendor.tcc.networkconnectivity.NetworkConnection;
import vendor.tcc.networkconnectivity.NetworkError;

import static com.android.volley.VolleyLog.TAG;

public class PaymentActivity extends AppCompatActivity {

    RelativeLayout confirm;
    private DatabaseHandler db_cart;
    private Session_management sessionManagement;
    TextView payble_ammount, my_wallet_ammount, used_wallet_ammount, used_coupon_ammount, order_ammount;
    private String getlocation_id = "";
    private String getstore_id = "";

    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    private Double rewards;
    RadioButton rb_Store, rb_Cod, rb_card, rb_Netbanking, rb_paytm;
    CheckBox checkBox_Wallet, checkBox_coupon;
    EditText et_Coupon;
    String getvalue;
    String text;
    String cp;
    String Used_Wallet_amount;
    String total_amount;
    String order_total_amount;
    RadioGroup radioGroup;
    String Prefrence_TotalAmmount;
    String getwallet;
    LinearLayout Promo_code_layout, Coupon_and_wallet;
    RelativeLayout Apply_Coupon_Code, Relative_used_wallet, Relative_used_coupon;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 300;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionbar.setTitle("Payment");

        Prefrence_TotalAmmount = SharedPref.getString(PaymentActivity.this, BaseURL.TOTAL_AMOUNT);

        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                getvalue = radioButton.getText().toString();
            }
        });


        Typeface font = Typeface.createFromAsset(getAssets(), "Font/Bold.ttf");
        checkBox_Wallet = (CheckBox) findViewById(R.id.use_wallet);
        checkBox_Wallet.setTypeface(font);
        rb_Store = (RadioButton) findViewById(R.id.use_store_pickup);
        rb_Store.setTypeface(font);
        rb_Cod = (RadioButton) findViewById(R.id.use_COD);
        rb_Cod.setTypeface(font);
        rb_card = (RadioButton) findViewById(R.id.use_card);
        rb_card.setTypeface(font);
        rb_Netbanking = (RadioButton) findViewById(R.id.use_netbanking);
        rb_Netbanking.setTypeface(font);
        rb_paytm = (RadioButton) findViewById(R.id.use_wallet_ammount);
        rb_paytm.setTypeface(font);
        checkBox_coupon = (CheckBox) findViewById(R.id.use_coupon);
        checkBox_coupon.setTypeface(font);
        et_Coupon = (EditText) findViewById(R.id.et_coupon_code);
        Promo_code_layout = (LinearLayout) findViewById(R.id.prommocode_layout);
        Apply_Coupon_Code = (RelativeLayout) findViewById(R.id.apply_coupoun_code);
        Apply_Coupon_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coupon_code();

            }
        });

        sessionManagement = new Session_management(PaymentActivity.this);


        Coupon_and_wallet = (LinearLayout) findViewById(R.id.coupon_and_wallet);
        Relative_used_wallet = (RelativeLayout) findViewById(R.id.relative_used_wallet);
        Relative_used_coupon = (RelativeLayout) findViewById(R.id.relative_used_coupon);

        //Show  Wallet
        getwallet = SharedPref.getString(PaymentActivity.this, BaseURL.KEY_WALLET_Ammount);
        my_wallet_ammount = (TextView) findViewById(R.id.user_wallet);
        my_wallet_ammount.setText(getwallet+PaymentActivity.this.getString(R.string.currency));
        db_cart = new DatabaseHandler(PaymentActivity.this);
        /*view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener()

        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment fm = new Home_fragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });*/


        total_amount = getIntent().getStringExtra("total");
        order_total_amount = getIntent().getStringExtra("total");
        getdate = getIntent().getStringExtra("getdate");
        gettime = getIntent().getStringExtra("gettime");
        getlocation_id = getIntent().getStringExtra("getlocationid");
        getstore_id = getIntent().getStringExtra("getstoreid");
        payble_ammount = (TextView) findViewById(R.id.payable_ammount);
        order_ammount = (TextView) findViewById(R.id.order_ammount);
        used_wallet_ammount = (TextView) findViewById(R.id.used_wallet_ammount);
        used_coupon_ammount = (TextView) findViewById(R.id.used_coupon_ammount);
        payble_ammount.setText(total_amount+PaymentActivity.this.getString(R.string.currency));
        order_ammount.setText(order_total_amount+PaymentActivity.this.getString(R.string.currency));


        checkBox_Wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Use_Wallet_Ammont();

                    Coupon_and_wallet.setVisibility(View.VISIBLE);
                    Relative_used_wallet.setVisibility(View.VISIBLE);
                    if (rb_card.isChecked() || rb_Netbanking.isChecked() || rb_paytm.isChecked()) {
                        rb_card.setChecked(false);
                        rb_Netbanking.setChecked(false);
                        rb_paytm.setChecked(false);
                    }
                } else {
                    if (payble_ammount != null) {
                        rb_Cod.setText(getResources().getString(R.string.cash));
                        rb_card.setClickable(true);
                        rb_card.setTextColor(getResources().getColor(R.color.dark_black));
                        rb_Netbanking.setClickable(true);
                        rb_Netbanking.setTextColor(getResources().getColor(R.color.dark_black));
                        rb_paytm.setClickable(true);
                        rb_paytm.setTextColor(getResources().getColor(R.color.dark_black));
                        checkBox_coupon.setClickable(true);
                        checkBox_coupon.setTextColor(getResources().getColor(R.color.dark_black));
                    }
                    final String Ammount = SharedPref.getString(PaymentActivity.this, BaseURL.TOTAL_AMOUNT);
                    final String WAmmount = SharedPref.getString(PaymentActivity.this, BaseURL.KEY_WALLET_Ammount);
                    my_wallet_ammount.setText(WAmmount+PaymentActivity.this.getResources().getString(R.string.currency));
                    payble_ammount.setText(Ammount+getResources().getString(R.string.currency));
                    used_wallet_ammount.setText("");
                    Relative_used_wallet.setVisibility(View.GONE);
                    if (checkBox_coupon.isChecked()) {
                        final String ammount = SharedPref.getString(PaymentActivity.this, BaseURL.COUPON_TOTAL_AMOUNT);
                        payble_ammount.setText(ammount+getResources().getString(R.string.currency));
                    }
                }
            }
        });
        checkBox_coupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Promo_code_layout.setVisibility(View.VISIBLE);
                    Coupon_and_wallet.setVisibility(View.VISIBLE);
                    Relative_used_coupon.setVisibility(View.VISIBLE);
                    if (rb_Store.isChecked() || rb_Cod.isChecked() || rb_card.isChecked() || rb_Netbanking.isChecked() || rb_paytm.isChecked()) {
                        rb_Store.setChecked(false);
                        rb_Cod.setChecked(false);
                        rb_card.setChecked(false);
                        rb_Netbanking.setChecked(false);
                        rb_paytm.setChecked(false);
                    }
                } else {
                    et_Coupon.setText("");
                    Relative_used_coupon.setVisibility(View.GONE);
                    Promo_code_layout.setVisibility(View.GONE);
                }
            }
        });


        confirm = (RelativeLayout) findViewById(R.id.confirm_order);
        confirm.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (ConnectivityReceiver.isConnected()) {

                    //confirm.setEnabled(false);
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    mLastClickTime = now;

                    showConfirmDialog();


                } else {
                   // confirm.setEnabled(true);

                    //((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                }
            }
        });
    }


    private void attemptOrder() {
        ArrayList<HashMap<String, String>> items = db_cart.getCartAll();
        rewards = Double.parseDouble(db_cart.getColumnRewards());
        if (items.size() > 0) {
            JSONArray passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);
                JSONObject jObjP = new JSONObject();
                try {
                    jObjP.put("product_id", map.get("product_id"));
                    jObjP.put("qty", map.get("qty"));
                    jObjP.put("unit_value", map.get("unit_value"));
                    jObjP.put("unit", map.get("unit"));
                    jObjP.put("price", map.get("price"));
                    jObjP.put("rewards", map.get("rewards"));
                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

            if (ConnectivityReceiver.isConnected()) {

                Log.e(TAG, "from:" + gettime + "\ndate:" + getdate +
                        "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + getstore_id + "\ndata:" + passArray.toString());


                makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, getstore_id, passArray);


            }
        }
    }

    private void makeAddOrderRequest(String date, String gettime, String userid, String
            location, String store_id, JSONArray passArray) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", "2019-05-18");
        params.put("time", "12:00 PM - 12:30 PM");
        params.put("user_id", userid);
        params.put("location", location);
        params.put("store_id", store_id);
        params.put("total_ammount",total_amount);
        params.put("payment_method", getvalue);
        params.put("data", passArray.toString());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        String msg = response.getString("data");
                        String msg_arb=response.getString("data_arb");
                        db_cart.clearCart();
                        /*Bundle args = new Bundle();
                        Fragment fm = new Thanks_fragment();
                        args.putString("msg", msg);
                        args.putString("msgarb",msg_arb);
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                .addToBackStack(null).commit();*/

                        Intent intent = new Intent(PaymentActivity.this, ThanksActivity.class);
                        intent.putExtra("msg", msg);
                        intent.putExtra("msgarb",msg_arb);
                        startActivity(intent);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(PaymentActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void Usewalletfororder(String userid, String Wallet) {
        String tag_json_obj = "json_add_order_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", userid);
        params.put("wallet_amount", Wallet);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.Wallet_CHECKOUT, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String status = response.getString("responce");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(PaymentActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void Use_Wallet_Ammont() {
        final String Wallet_Ammount = SharedPref.getString(PaymentActivity.this, BaseURL.KEY_WALLET_Ammount);
        final String Coupon_Ammount = SharedPref.getString(PaymentActivity.this, BaseURL.COUPON_TOTAL_AMOUNT);
        final String Ammount = SharedPref.getString(PaymentActivity.this, BaseURL.TOTAL_AMOUNT);
        if (NetworkConnection.connectionChecking(PaymentActivity.this)) {
            RequestQueue rq = Volley.newRequestQueue(PaymentActivity.this);
            StringRequest postReq = new StringRequest(Request.Method.POST, BaseURL.BASE_URL+"index.php/api/wallet_on_checkout",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("eclipse", "Response=" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONArray Jarray = object.getJSONArray("final_amount");
                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject json_data = Jarray.getJSONObject(i);
                                    String Wallet_amount = json_data.getString("wallet");
                                    Used_Wallet_amount = json_data.getString("used_wallet");
                                    total_amount = json_data.getString("total");
                                    if (total_amount.equals("0")) {
                                        rb_Cod.setText("Home Delivery");
                                        getvalue = rb_Cod.getText().toString();
                                        rb_card.setClickable(false);
                                        rb_card.setTextColor(getResources().getColor(R.color.gray));
                                        rb_Netbanking.setClickable(false);
                                        rb_Netbanking.setTextColor(getResources().getColor(R.color.gray));
                                        rb_paytm.setClickable(false);
                                        rb_paytm.setTextColor(getResources().getColor(R.color.gray));
                                        checkBox_coupon.setClickable(false);
                                        checkBox_coupon.setTextColor(getResources().getColor(R.color.gray));
                                    } else {
                                        if (total_amount != null) {
                                            rb_Cod.setText("Cash On Delivery");
                                            rb_card.setClickable(true);
                                            rb_card.setTextColor(getResources().getColor(R.color.dark_black));
                                            rb_Netbanking.setClickable(true);
                                            rb_Netbanking.setTextColor(getResources().getColor(R.color.dark_black));
                                            rb_paytm.setClickable(true);
                                            rb_paytm.setTextColor(getResources().getColor(R.color.dark_black));
                                            checkBox_coupon.setClickable(true);
                                            checkBox_coupon.setTextColor(getResources().getColor(R.color.dark_black));
                                        }
                                    }
                                    payble_ammount.setText(total_amount+getResources().getString(R.string.currency));
                                    used_wallet_ammount.setText("(" + getResources().getString(R.string.currency) + Used_Wallet_amount + ")");
                                    SharedPref.putString(PaymentActivity.this, BaseURL.WALLET_TOTAL_AMOUNT, total_amount);
                                    my_wallet_ammount.setText(Wallet_amount+getResources().getString(R.string.currency));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error [" + error + "]");
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    if (checkBox_Wallet.isChecked()){
                        params.put("wallet_amount", Wallet_Ammount);
                    }else {
                        params.put("total_amount", Ammount);

                    }

                    if (checkBox_coupon.isChecked()) {
                        params.put("total_amount", Coupon_Ammount);
                    } else {
                        params.put("total_amount", Ammount);

                    }
                    return params;
                }
            };
            rq.add(postReq);
        } else {
            Intent intent = new Intent(PaymentActivity.this, NetworkError.class);
            startActivity(intent);
        }
    }

    private void Coupon_code() {
        final String Ammount = SharedPref.getString(PaymentActivity.this, BaseURL.TOTAL_AMOUNT);
        final String Wallet_Ammount = SharedPref.getString(PaymentActivity.this, BaseURL.WALLET_TOTAL_AMOUNT);
        final String Coupon_code = et_Coupon.getText().toString();
        if (NetworkConnection.connectionChecking(PaymentActivity.this)) {
            RequestQueue rq = Volley.newRequestQueue(PaymentActivity.this);
            StringRequest postReq = new StringRequest(Request.Method.POST, BaseURL.COUPON_CODE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("eclipse", "Response=" + response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                total_amount = obj.getString("Total_amount");
                                String Used_coupon_amount = obj.getString("coupon_value");
                                if (obj.optString("responce").equals("true")) {
                                    payble_ammount.setText(total_amount+getResources().getString(R.string.currency));
                                    SharedPref.putString(PaymentActivity.this, BaseURL.COUPON_TOTAL_AMOUNT, total_amount);
                                    Toast.makeText(PaymentActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                    used_coupon_ammount.setText("(" + PaymentActivity.this.getResources().getString(R.string.currency) + Used_coupon_amount + ")");
                                    Promo_code_layout.setVisibility(View.GONE);

                                } else {
                                    Toast.makeText(PaymentActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                    et_Coupon.setText("");
                                    used_coupon_ammount.setText("");
                                    payble_ammount.setText(total_amount+getResources().getString(R.string.currency));
                                    Promo_code_layout.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error [" + error + "]");
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("coupon_code", Coupon_code);
                    if (checkBox_Wallet.isChecked()) {
                        params.put("payable_amount", Wallet_Ammount);
                    } else {
                        params.put("payable_amount", Ammount);
                    }
                    return params;
                }
            };
            rq.add(postReq);
        } else {
            Toast.makeText(PaymentActivity.this, "Somthing Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }


    private void checked() {
        if (checkBox_Wallet.isChecked()) {
            if (rb_Store.isChecked() || rb_Cod.isChecked()) {
                attemptOrder();
            } else {
                Toast.makeText(PaymentActivity.this, "Please Select One", Toast.LENGTH_SHORT).show();
            }

        }
        if (rb_Store.isChecked()) {
            attemptOrder();
        }
        if (rb_Cod.isChecked()) {

            attemptOrder();
        }
        if (rb_card.isChecked()) {
            Intent myIntent = new Intent(PaymentActivity.this, PaymentGatWay.class);
            if (checkBox_Wallet.isChecked()) {
                myIntent.putExtra("total", total_amount);
            } else {
                myIntent.putExtra("total", Prefrence_TotalAmmount);
                myIntent.putExtra("getdate", getdate);
                myIntent.putExtra("gettime", gettime);
                myIntent.putExtra("getlocationid", getlocation_id);
                myIntent.putExtra("getstoreid", getstore_id);
                myIntent.putExtra("getpaymentmethod", getvalue);
            }
            startActivity(myIntent);
        }
        if (rb_Netbanking.isChecked()) {
            Intent myIntent1 = new Intent(PaymentActivity.this, PaymentGatWay.class);
            if (checkBox_Wallet.isChecked()) {
                myIntent1.putExtra("total", total_amount);

            } else {
                myIntent1.putExtra("total", Prefrence_TotalAmmount);
                myIntent1.putExtra("getdate", getdate);
                myIntent1.putExtra("gettime", gettime);
                myIntent1.putExtra("getlocationid", getlocation_id);
                myIntent1.putExtra("getstoreid", getstore_id);
                myIntent1.putExtra("getpaymentmethod", getvalue);
            }
            startActivity(myIntent1);
        }
        if (rb_paytm.isChecked()) {
            Intent myIntent1 = new Intent(PaymentActivity.this, Paytm.class);
            if (checkBox_Wallet.isChecked()) {
                myIntent1.putExtra("total", total_amount);

            } else {
                myIntent1.putExtra("total", Prefrence_TotalAmmount);
                myIntent1.putExtra("getdate", getdate);
                myIntent1.putExtra("gettime", gettime);
                myIntent1.putExtra("getlocationid", getlocation_id);
                myIntent1.putExtra("getstoreid", getstore_id);
                myIntent1.putExtra("getpaymentmethod", getvalue);
            }
            startActivity(myIntent1);

        }
        if (checkBox_coupon.isChecked()) {
            if (rb_Store.isChecked() || rb_Cod.isChecked()) {
                attemptOrder();
            } else {
                Toast.makeText(PaymentActivity.this, "Select Store Or Cod", Toast.LENGTH_SHORT).show();
            }


        }



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

    private void showConfirmDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, you want to place the order?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (checkBox_Wallet.isChecked()){
                                    getuser_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

                                    Usewalletfororder(getuser_id,Used_Wallet_amount);
                                    checked();

                                }
                                else {
                                    checked();

                                }

                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}
