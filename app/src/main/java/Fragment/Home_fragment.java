package Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Deal_OfDay_Adapter;
import Adapter.Fruit_adapter;
import Adapter.Leaf_adapter;
import Adapter.PagerHome_adapter;
import Adapter.Home_Icon_Adapter;
import Adapter.Product_adapter;
import Adapter.Top_Selling_Adapter;
import Adapter.Vegetables_adapter;
import Config.BaseURL;
import Model.Category_model;
import Model.Deal_Of_Day_model;
import Model.Home_Icon_model;
import Model.Product_model;
import Model.Top_Selling_model;
import util.DatabaseHandler;
import vendor.tcc.AppController;
import vendor.tcc.CartActivity;
import vendor.tcc.CustomSlider;
import vendor.tcc.MainActivity;
import vendor.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.RecyclerTouchListener;
import vendor.tcc.SearchActivity;

import static android.content.Context.MODE_PRIVATE;


public class Home_fragment extends Fragment  {
    private static String TAG = Home_fragment.class.getSimpleName();
    private SliderLayout imgSlider, banner_slider, featuredslider;
    private RecyclerView rv_items, rv_top_selling, rv_deal_of_day, rv_headre_icons;
    private List<Category_model> category_modelList = new ArrayList<>();
    private PagerHome_adapter adapter;
    private RecyclerView rv_cat;
    private Leaf_adapter adapter1;
    private Fruit_adapter adapter2;
    private Vegetables_adapter adapter3;
    private TabLayout tab_cat1;
    private TabItem tabChats,tabStatus,tabCalls;
    private List<String> cat_menu_id = new ArrayList<>();
    private ViewPager viewPager;
    private List<Product_model> product_modelList = new ArrayList<>();
    private List<Product_model> fruitsList = new ArrayList<>();
    private List<Product_model> vegitablesList = new ArrayList<>();
    private List<Product_model> leafsList = new ArrayList<>();
    private List<Product_model> selectedCatList = new ArrayList<>();
    private boolean isSubcat = false;
    LinearLayout Search_layout;
    String getid;
    String getcat_title;
    ScrollView scrollView;
    SharedPreferences sharedpreferences;
    private DatabaseHandler db;
    PagerHome_adapter pagerHome_adapter;
    private RelativeLayout mGoToCart;

    //Home Icons
    private Home_Icon_Adapter menu_adapter;
    private List<Home_Icon_model> menu_models = new ArrayList<>();


    //Deal O Day
    private Deal_OfDay_Adapter deal_ofDay_adapter;
    private List<Deal_Of_Day_model> deal_of_day_models = new ArrayList<>();
    LinearLayout Deal_Linear_layout;
    FrameLayout Deal_Frame_layout, Deal_Frame_layout1;


    //Top Selling Products
    private Top_Selling_Adapter top_selling_adapter;
    private List<Top_Selling_model> top_selling_models = new ArrayList<>();


    private ImageView iv_Call, iv_Whatspp, iv_reviews, iv_share_via;
    private TextView timer, tv_total,tv_item;
    Button View_all_deals, View_all_TopSell;
    private String apiResponse;

    private ImageView Top_Selling_Poster, Deal_Of_Day_poster;

    View view;

    public Home_fragment() {

    }

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Product_model>>() {
            }.getType();
            apiResponse = getArguments().getString("apiResponse");
            product_modelList = gson.fromJson(apiResponse, listType);
            if(product_modelList.size()>0)
            {
                for(int i=0;i<product_modelList.size();i++)
                {
                    if(Integer.parseInt(product_modelList.get(i).getCategory_id())==8)
                    {
                        fruitsList.add(product_modelList.get(i));
                    }
                    else if(Integer.parseInt(product_modelList.get(i).getCategory_id())==9)
                    {
                        vegitablesList.add(product_modelList.get(i));
                    }
                    else if(Integer.parseInt(product_modelList.get(i).getCategory_id())==10)
                    {
                        leafsList.add(product_modelList.get(i));
                    }
                }
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
        ((MainActivity) getActivity()).updateHeader();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    ((MainActivity) getActivity()).finish();
                    return true;
                }
                return false;
            }
        });

        TabLayout tab_cat1 = view.findViewById(R.id.tab_layout1);
        TabItem tabChats = view.findViewById(R.id.fruits);
        TabItem tabStatus = view.findViewById(R.id.vegetables);
        TabItem tabCalls = view.findViewById(R.id.leaf);
        viewPager = view.findViewById(R.id.viewpager);
        tv_total = view.findViewById(R.id.tv_cart_total);
        tv_item = view.findViewById(R.id.tv_cart_item);
        mGoToCart = view.findViewById(R.id.btn_cart_checkout);
        db = new DatabaseHandler(getActivity());
        pagerHome_adapter = new PagerHome_adapter(getFragmentManager(), tab_cat1.getTabCount(), apiResponse);
        viewPager.setAdapter(pagerHome_adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_cat1));

        tab_cat1.setSelectedTabIndicatorColor(getActivity().getResources().getColor(R.color.black));
        tab_cat1.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Toast.makeText(getActivity(), "tabSelected:  " + tab.getText(), Toast.LENGTH_SHORT).show();
               /* if(tab.getText().toString().equalsIgnoreCase("fruits"))
                {
                    selectedCatList.addAll(fruitsList);
                }
                else if(tab.getText().toString().equalsIgnoreCase("vegitables"))
                {
                    selectedCatList.addAll(vegitablesList);
                }
                else if(tab.getText().toString().equalsIgnoreCase("leafs"))
                {
                    selectedCatList.addAll(leafsList);
                }*/
                viewPager.setCurrentItem(tab.getPosition());
                pagerHome_adapter.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


                // Reload your recyclerView here
            }
        });



        //Search
        Search_layout = (LinearLayout) view.findViewById(R.id.search_layout);
        Search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), SearchActivity.class);
               startActivity(intent);
            }
        });

        mGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CartActivity.class);
                startActivity(intent);
            }
        });



        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(getActivity(),
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });*/




        return view;
    }

//    public  boolean isPermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (getContext().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v("TAG","Permission is granted");
//                return true;
//            } else {
//
//                Log.v("TAG","Permission is revoked");
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
//                return false;
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v("TAG","Permission is granted");
//            return true;
//        }
//    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//
//            case 1: {
//
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
//                    call_action();
//                } else {
//                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
//    public void call_action(){
//
//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse("tel:" + "919889887711"));
//        startActivity(callIntent);
//    }
private void updateData() {
    tv_total.setText("" + db.getTotalAmount());
    tv_item.setText("" + db.getCartCount());


    //MainActivity.setCartCounter("" + db.getCartCount());
}

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        updateData();
        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_cart"));
    }

}