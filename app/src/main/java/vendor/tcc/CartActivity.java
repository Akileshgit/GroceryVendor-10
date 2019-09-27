package vendor.tcc;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter.Cart_adapter;
import Config.BaseURL;
import CustomViews.DividerDecoration;
import util.ConnectivityReceiver;
import util.DatabaseHandler;
import util.Session_management;
import Fragment.Delivery_fragment;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "CartActivity";
    DividerDecoration mItemDecoration;
    ArrayList<HashMap<String, String>> map;
    private RecyclerView rv_cart;
    private TextView tv_clear, tv_total, tv_item;
    private RelativeLayout btn_checkout, btnShopNow;
    private DatabaseHandler db;
    private Session_management sessionManagement;
    private LinearLayout mEmptyLayout, mBottomLayout;
    // broadcast reciver for receive data
    private BroadcastReceiver mCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String type = intent.getStringExtra("type");

            if (type.contentEquals("update")) {
                updateData();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sessionManagement = new Session_management(CartActivity.this);
        sessionManagement.cleardatetime();

        tv_clear = findViewById(R.id.tv_cart_clear);
        tv_total = findViewById(R.id.tv_cart_total);
        tv_item = findViewById(R.id.tv_cart_item);
        btn_checkout = findViewById(R.id.btn_cart_checkout);
        rv_cart = findViewById(R.id.rv_cart);
        mEmptyLayout = findViewById(R.id.empty_layout);
        mBottomLayout = findViewById(R.id.bottom_layout);
        btnShopNow = findViewById(R.id.btn_shopnow);

        db = new DatabaseHandler(CartActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionbar.setTitle("Cart");

        btn_checkout.setOnClickListener(this);
        btnShopNow.setOnClickListener(this);
        new AsyncTaskRunner().execute();


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tv_cart_clear) {
            // showdialog
            showClearDialog();
        } else if (id == R.id.btn_cart_checkout) {

            if (ConnectivityReceiver.isConnected()) {
                makeGetLimiteRequest();
            } else {

            }

        }else if(id == R.id.btn_shopnow)
        {
            finish();
        }
    }

    // update UI
    private void updateData() {
        tv_total.setText("" + db.getTotalAmount());
        tv_item.setText("" + db.getCartCount());
        if(db.getCartCount()==0)
        {
            rv_cart.setVisibility(View.GONE);
            mBottomLayout.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
        }


        //MainActivity.setCartCounter("" + db.getCartCount());
    }



    private void showClearDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        alertDialog.setMessage(getResources().getString(R.string.sure_del));
        alertDialog.setNegativeButton(getResources().getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // clear cart data
                db.clearCart();
                ArrayList<HashMap<String, String>> map = db.getCartAll();
                Cart_adapter adapter = new Cart_adapter(CartActivity.this, map);
                mItemDecoration = new DividerDecoration(CartActivity.this, R.dimen.item_offset_divider);
                rv_cart.addItemDecoration(mItemDecoration);
                rv_cart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                rv_cart.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                updateData();

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetLimiteRequest() {

        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_LIMITE_SETTING_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        Double total_amount = Double.parseDouble(db.getTotalAmount());


                        try {
                            // Parsing json array response
                            // loop through each json object

                            boolean issmall = false;
                            boolean isbig = false;

                            // arraylist list variable for store data;
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {

                                JSONObject jsonObject = (JSONObject) response
                                        .get(i);
                                int value;

                                if (jsonObject.getString("id").equals("1")) {
                                    value = Integer.parseInt(jsonObject.getString("value"));

                                    if (total_amount < value) {
                                        issmall = true;
                                        Toast.makeText(CartActivity.this, "" + jsonObject.getString("title") + " : " + value, Toast.LENGTH_SHORT).show();
                                    }
                                } else if (jsonObject.getString("id").equals("2")) {
                                    value = Integer.parseInt(jsonObject.getString("value"));

                                    if (total_amount > value) {
                                        isbig = true;
                                        Toast.makeText(CartActivity.this, "" + jsonObject.getString("title") + " : " + value, Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            if (!issmall && !isbig) {
                                if (sessionManagement.isLoggedIn()) {
                                   /* Bundle args = new Bundle();
                                    Fragment fm = new Delivery_fragment();
                                    fm.setArguments(args);
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                            .addToBackStack(null).commit();*/

                                   Intent intent = new Intent(CartActivity.this, DeliveryActivity.class);
                                   startActivity(intent);
                                } else {
                                    //Toast.makeText(getActivity(), "Please login or regiter.\ncontinue", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(CartActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CartActivity.this,
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(CartActivity.this, "Connection Time out", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        registerReceiver(mCart, new IntentFilter("Grocery_cart"));
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            map = db.getCartAll();
            updateData();

            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            if(map.size()>0)
            {
                Cart_adapter adapter = new Cart_adapter(CartActivity.this, map);
                mItemDecoration = new DividerDecoration(CartActivity.this, R.dimen.item_offset_divider);
                rv_cart.addItemDecoration(mItemDecoration);
                rv_cart.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                rv_cart.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else
            {
                rv_cart.setVisibility(View.GONE);
                mBottomLayout.setVisibility(View.GONE);
                mEmptyLayout.setVisibility(View.VISIBLE);

            }

        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CartActivity.this,
                    "ProgressDialog",
                    "Loading...");
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


}

