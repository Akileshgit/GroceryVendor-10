package vendor.tcc;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Search_adapter;
import Adapter.SuggestionAdapter;
import Config.BaseURL;
import Model.Product_model;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;

public class SearchActivity extends AppCompatActivity {
    private AutoCompleteTextView acTextView;
    private RelativeLayout btn_search;
    private RecyclerView rv_search;

    private List<Product_model> product_modelList = new ArrayList<>();
    private Search_adapter adapter_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        acTextView = (AutoCompleteTextView) findViewById(R.id.et_search);
        acTextView.setThreshold(1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionbar.setTitle("Search");

        acTextView.setAdapter(new SuggestionAdapter(SearchActivity.this, acTextView.getText().toString()));

        acTextView.setTextColor(getResources().getColor(R.color.black));
        btn_search = (RelativeLayout) findViewById(R.id.btn_search);
        rv_search = (RecyclerView) findViewById(R.id.rv_search);
        rv_search.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String get_search_txt ="%"+ acTextView.getText().toString() +"%";
                if (TextUtils.isEmpty(get_search_txt)) {
                    Toast.makeText(SearchActivity.this, getResources().getString(R.string.enter_keyword), Toast.LENGTH_SHORT).show();
                } else {
                    if (ConnectivityReceiver.isConnected()) {
                        makeGetProductRequest(get_search_txt);
                    } else {
                        //((MainActivity) getActivity()).onNetworkConnectionChanged(false);
                    }
                }

            }
        });

    }

    /**
     * Method to make json object request where json response starts wtih {
     */
    private void makeGetProductRequest(String search_text) {

        // Tag used to cancel the request
        String tag_json_obj = "json_product_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("search", search_text);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("SearchActivity", response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();

                        product_modelList = gson.fromJson(response.getString("data"), listType);

                        adapter_product = new Search_adapter(product_modelList, SearchActivity.this);
                        rv_search.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();



                            if (product_modelList.isEmpty()) {
                                Toast.makeText(SearchActivity.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("SearchActivity", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(SearchActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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
