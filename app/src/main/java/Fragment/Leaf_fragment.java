package Fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.daimajia.slider.library.SliderLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import Adapter.Leaf_adapter;


import Adapter.Vegetables_adapter;
import Config.BaseURL;
import CustomViews.DividerDecoration;
import Model.Category_model;
import Model.Product_model;
import Model.Slider_subcat_model;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.Session_management;
import vendor.tcc.AppController;
import vendor.tcc.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Leaf_fragment extends Fragment {
    private static String TAG = Product_fragment.class.getSimpleName();
    private RecyclerView rv_cat2;

    private List<Category_model> category_modelList = new ArrayList<>();
    private List<Slider_subcat_model> slider_subcat_models = new ArrayList<>();
    private List<String> cat_menu_id = new ArrayList<>();
    private List<Product_model> product_modelList = new ArrayList<>();
    private List<Product_model> leafsList = new ArrayList<>();
    private Leaf_adapter adapter_leaf;
    private SliderLayout banner_slider;
    String language;
    SharedPreferences preferences;
    DividerDecoration mItemDecoration;


    public Leaf_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaf, container, false);
        rv_cat2 = (RecyclerView) view.findViewById(R.id.rv_subcategory);
        rv_cat2.setLayoutManager(new LinearLayoutManager(getActivity()));

        Session_management sessionManagement = new Session_management(getActivity());
        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {

            //Shop by Catogary

            makeGetProductRequest(user_id);

        }

        /**/





        return view;
    }



    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetProductRequest(String parent_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", parent_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        if(product_modelList.size()>0)
                        {
                            leafsList.clear();
                            for(int i=0;i<product_modelList.size();i++)
                            {
                                if(Integer.parseInt(product_modelList.get(i).getCategory_id())==10)
                                {
                                    leafsList.add(product_modelList.get(i));
                                }
                            }
                        }
                        adapter_leaf = new Leaf_adapter(leafsList, getActivity());
                        mItemDecoration = new DividerDecoration(getActivity(), R.dimen.item_offset_divider);
                        rv_cat2.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv_cat2.addItemDecoration(mItemDecoration);
                        rv_cat2.setAdapter(adapter_leaf);
                        adapter_leaf.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }








}

