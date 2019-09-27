package vendor.tcc;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import Model.Support_info_model;
import util.ConnectivityReceiver;

public class TermsConditionsActivity extends AppCompatActivity {
    private TextView tv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);

        tv_info = (TextView) findViewById(R.id.tv_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionbar.setTitle(getResources().getString(R.string.nav_terms));

        String geturl = getIntent().getStringExtra("url");
        //   String title = getArguments().getString("title");

       // ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.condition));

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetInfoRequest(geturl);
        } else {
          //  ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetInfoRequest(String url) {

        // Tag used to cancel the request
        String tag_json_obj = "json_info_req";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TermsConditionsactivity", response.toString());

                try {
                    // Parsing json array response
                    // loop through each json object

                    boolean status = response.getBoolean("responce");
                    if (status) {

                        List<Support_info_model> support_info_modelList = new ArrayList<>();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Support_info_model>>() {
                        }.getType();

                        support_info_modelList = gson.fromJson(response.getString("data"), listType);

                        String desc = support_info_modelList.get(0).getPg_descri();
                        String title = support_info_modelList.get(0).getPg_title();

                        tv_info.setText(Html.fromHtml(desc));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TermsConditionsActivity.this,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TermsConditionsActivity", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(TermsConditionsActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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
