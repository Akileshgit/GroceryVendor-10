package vendor.tcc;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Adapter.Fruit_adapter;
import Config.BaseURL;
import Config.SharedPref;
import Fonts.CustomTypefaceSpan;
import Fragment.About_us_fragment;
import Fragment.Contact_Us_fragment;
import Fragment.Empty_cart_fragment;
import Fragment.Home_fragment;
import Fragment.Cart_fragment;
import Fragment.Reward_fragment;
import Fragment.Edit_profile_fragment;
import Fragment.Shop_Now_fragment;
import Fragment.Terms_and_Condition_fragment;
import Fragment.Wallet_fragment;
import interfaces.IonBackPressed;
import util.CustomVolleyJsonRequest;
import vendor.tcc.networkconnectivity.NetworkError;
import util.ConnectivityReceiver;
import util.DatabaseHandler;
import util.Session_management;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

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

import Adapter.Product_adapter;
import Config.BaseURL;
import Model.Category_model;
import Model.Product_model;
import Model.Slider_subcat_model;
import vendor.tcc.AppController;
import vendor.tcc.CustomSlider;
import vendor.tcc.MainActivity;
import vendor.tcc.R;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView totalBudgetCount, totalBudgetCount2, totalBudgetCount3, tv_name, powerd_text;
    private ImageView iv_profile;
    private DatabaseHandler dbcart;
    private Session_management sessionManagement;
    private Menu nav_menu;
    private RecyclerView rv_cat;
    private TabLayout tab_cat;
    private List<Category_model> category_modelList = new ArrayList<>();
    private List<Slider_subcat_model> slider_subcat_models = new ArrayList<>();
    private List<String> cat_menu_id = new ArrayList<>();
    private List<Product_model> product_modelList = new ArrayList<>();
    private Product_adapter adapter_product;
    private Fruit_adapter adapter_fruit;
    private String apiResponse;

    ImageView imageView;
    TextView mTitle;
    LinearLayout viewpa;
    Toolbar toolbar;
    String language="";
    LinearLayout My_Order, My_Reward, My_Walllet, My_Cart;
    int padding = 0;
    private TextView txtRegId;
    NavigationView navigationView;
    LinearLayout Change_Store;
    String Store_Count;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void attachBaseContext(Context newBase) {



        newBase = LocaleHelper.onAttach(newBase);

        super.attachBaseContext(newBase);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("MYTAG", "This is your Firebase token" + token);

        sharedPreferences= getSharedPreferences("lan", Context.MODE_PRIVATE);


        editor = sharedPreferences.edit();

        editor.putString("language", "english");



        if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);

                if (key.equals("MainActivity") && value.equals("True")) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra("value", value);
                    startActivity(intent);
                    finish();
                }

            }
            subscribeToPushService();
        }


        Store_Count = SharedPref.getString(MainActivity.this, BaseURL.KEY_STORE_COUNT);


//

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setPadding(padding, toolbar.getPaddingTop(), padding, toolbar.getPaddingBottom());




        setSupportActionBar(toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {

            View view = toolbar.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "Font/Bold.ttf");
                textView.setTypeface(myCustomFont);
            }


        }
        getSupportActionBar().setTitle(getResources().getString(R.string.name));


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }


        dbcart = new DatabaseHandler(this);


        checkConnection();

        sessionManagement = new Session_management(MainActivity.this);



        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(


                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }


        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        nav_menu = navigationView.getMenu();
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);

        viewpa=(LinearLayout) header.findViewById(R.id.viewpa);
        if (sessionManagement.isLoggedIn()) {
            viewpa.setVisibility(View.VISIBLE);
        }


        if (Store_Count.equals("1")) {
            Change_Store.setVisibility(View.INVISIBLE);
        } else if (Store_Count.equals("2")) {
            Change_Store.setVisibility(View.VISIBLE);
            Change_Store.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SelectStore.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
        }
        iv_profile = (ImageView) header.findViewById(R.id.iv_header_img);
        tv_name = (TextView) header.findViewById(R.id.tv_header_name);
        My_Order = (LinearLayout) header.findViewById(R.id.my_orders);

        My_Walllet = (LinearLayout) header.findViewById(R.id.my_wallet);
        My_Cart = (LinearLayout) header.findViewById(R.id.my_cart);

        My_Order.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, My_Order_activity.class);
                startActivity(intent);

            }
        });

        My_Walllet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Fragment fm = new Wallet_fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();*/
                Intent intent = new Intent(MainActivity.this, WalletActivity.class);
                startActivity(intent);
            }
        });

        My_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbcart.getCartCount() > 0) {
                    /*Fragment fm = new Cart_fragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.contentPanel, fm,"CART_FRAGMENT")
                            .addToBackStack("CART_FRAGMENT").commit();
                    getSupportFragmentManager().executePendingTransactions();*/
                    Intent intent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "No Item in Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iv_profile.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
                   /* Fragment fm = new Edit_profile_fragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.contentPanel, fm)
                            .addToBackStack(null).commit();*/
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(i);

                } else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
            }
        });

        updateHeader();

        sideMenu();


        if (savedInstanceState == null) {
            Fragment fm = new Home_fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, fm, "Home_fragment")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
        getSupportFragmentManager().

                addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        try {
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            Fragment fr = getSupportFragmentManager().findFragmentById(R.id.contentPanel);

                            final String fm_name = fr.getClass().getSimpleName();
                            Log.e("backstack: ", ": " + fm_name);
                            if (fm_name.contentEquals("Home_fragment")) {
                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                toggle.setDrawerIndicatorEnabled(true);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                                toggle.syncState();

                            } else if (fm_name.contentEquals("My_order_fragment") ||
                                    fm_name.contentEquals("Thanks_fragment")) {
                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                                toggle.setDrawerIndicatorEnabled(false);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                toggle.syncState();

                                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("apiResponse", apiResponse);
                                        Fragment fm = new Home_fragment();
                                        fm.setArguments(bundle);
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();

                                    }
                                });
                            } else {

                                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                                toggle.setDrawerIndicatorEnabled(false);
                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                toggle.syncState();

                                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        getSupportFragmentManager().popBackStack();
                                    }
                                });
                            }

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });


//        if (sessionManagement.getUserDetails().
//                get(BaseURL.KEY_ID) != null && !sessionManagement.getUserDetails().
//                get(BaseURL.KEY_ID).equalsIgnoreCase())
//
//        {
//            MyFirebaseRegister fireReg = new MyFirebaseRegister(this);
//            fireReg.RegisterUser(sessionManagement.getUserDetails().get(BaseURL.KEY_ID));
//        }

        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        if(user_id!=null)
        {
            makeGetProductRequest(user_id);
        }
        else
        {
            makeGetProductRequest("123");
        }


    }

    private void makeGetCategoryRequest(final String parent_id) {
        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", parent_id);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Category_model>>() {}.getType();
                        category_modelList = gson.fromJson(response.getString("data"), listType);
                        if (!category_modelList.isEmpty()) {
                            tab_cat.setVisibility(View.VISIBLE);
                            cat_menu_id.clear();
                            for (int i = 0; i < category_modelList.size(); i++) {
                                cat_menu_id.add(category_modelList.get(i).getId());
                                sharedPreferences =getSharedPreferences("lan", MODE_PRIVATE);

                                language=sharedPreferences.getString("language","");
                                if (language.contains("english")) {
                                    tab_cat.addTab(tab_cat.newTab().setText(category_modelList.get(i).getTitle()));
                                }
                                else {
                                    tab_cat.addTab(tab_cat.newTab().setText(category_modelList.get(i).getArb_title()));

                                }
                            }
                        } else {
                            makeGetProductRequest(parent_id);
                        }

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
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    //Get Shop By Catogary Products
    private void makeGetProductRequest(String cat_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cat_id", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_PRODUCT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        apiResponse = response.getString("data");
                        product_modelList = gson.fromJson(response.getString("data"), listType);
                        adapter_product = new Product_adapter(product_modelList, MainActivity.this);
                        rv_cat.setAdapter(adapter_product);
                        adapter_product.notifyDataSetChanged();
                       /* if (getActivity() != null) {
                            if (product_modelList.isEmpty()) {
                                //  Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }*/

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
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void updateHeader() {
        if (sessionManagement.isLoggedIn()) {
            String getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
            String getimage = sessionManagement.getUserDetails().get(BaseURL.KEY_IMAGE);
            String getemail = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
            SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
            String previouslyEncodedImage = shre.getString("image_data", "");
            if (!previouslyEncodedImage.equalsIgnoreCase("")) {
                byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
                iv_profile.setImageBitmap(bitmap);
            }
//            Glide.with(this)
//                    .load(BaseURL.IMG_PROFILE_URL + getimage)
//                    .placeholder(R.drawable.icon)
//                    .crossFade()
//                    .into(iv_profile);
            tv_name.setText(getname);

        }
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "Font/Bold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    public void sideMenu() {

        if (sessionManagement.isLoggedIn()) {
            //  tv_number.setVisibility(View.VISIBLE);
            nav_menu.findItem(R.id.nav_logout).setVisible(true);
           // nav_menu.findItem(R.id.nav_powerd).setVisible(true);

//            nav_menu.findItem(R.id.nav_user).setVisible(true);
        } else {

            //tv_number.setVisibility(View.GONE);
            tv_name.setText(getResources().getString(R.string.btn_login));
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });
            nav_menu.findItem(R.id.nav_logout).setVisible(false);

            //            nav_menu.findItem(R.id.nav_user).setVisible(false);
        }
    }

    public void setFinish() {
        finish();
    }

    public void setCartCounter(String totalitem) {
        try {
            totalBudgetCount.setText(totalitem);
        }catch (Exception e){

        }
    }

    public void setTitle(String title) {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem item = menu.findItem(R.id.action_cart);
        item.setVisible(true);

        View count = item.getActionView();
        count.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menu.performIdentifierAction(item.getItemId(), 0);
            }
        });

        totalBudgetCount = (TextView) count.findViewById(R.id.actionbar_notifcation_textview);
        totalBudgetCount.setText("" + dbcart.getCartCount());
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_cart) {
            if (dbcart.getCartCount() > 0) {
                /*Fragment fm = new Cart_fragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().add(R.id.contentPanel, fm, "CART_FRAGMENT")
                        .addToBackStack("CART_FRAGMENT").commit();
                getSupportFragmentManager().executePendingTransactions();*/
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("ResourceType")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fm = null;
        Bundle args = new Bundle();
         if (id == R.id.nav_my_profile) {
           /* fm = new Edit_profile_fragment();*/
             Intent i = new Intent(MainActivity.this, ProfileActivity.class);
             startActivity(i);
        } else if (id == R.id.nav_support) {
            String smsNumber = "917349722228";
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
            sendIntent.putExtra("jid",     PhoneNumberUtils.stripSeparators(smsNumber)+"@s.whatsapp.net");//phone number without "+" prefix

             startActivity(sendIntent);


        } else if (id == R.id.nav_policy) {
            /*fm = new Terms_and_Condition_fragment();
            args.putString("url", BaseURL.GET_TERMS_URL);
            args.putString("title", getResources().getString(R.string.nav_terms));
            fm.setArguments(args);*/

            Intent intent = new Intent(MainActivity.this, TermsConditionsActivity.class);
            intent.putExtra("url",BaseURL.GET_TERMS_URL);
            intent.putExtra("title",getResources().getString(R.string.nav_terms));
            startActivity(intent);


        } else if (id == R.id.nav_logout) {
            sessionManagement.logoutSession();
             Intent intent = new Intent(this, LoginActivity.class);
             intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
             finish();
             startActivity(intent);


        } /*else if (id == R.id.nav_powerd) {
            // stripUnderlines(textView);
            String url = "https://fritado.com/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            finish();
        }*/

        if (fm != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Exception ignored) {
        }

    }

    private void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        textView.setText(s);
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getPackageName() + " APP");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    public void reviewOnApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }


    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;

        if (!isConnected) {
            Intent intent = new Intent(MainActivity.this, NetworkError.class);
            startActivity(intent);
        }
    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(BaseURL.PREFS_NAME, 0);
        String regId = pref.getString("regId", null);
        Log.e(TAG, "Firebase reg id: " + regId);
        if (!TextUtils.isEmpty(regId)) {
            // txtRegId.setText("Firebase Reg Id: " + regId);
        } else {
            // txtRegId.setText("Firebase Reg Id is not received yet!");
        }
    }

    private void subscribeToPushService() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");

        Log.d("Tecmanic", "Subscribed");
//        Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();

        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        Log.d("Tecmanic", token);
        //      Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        // register connection status listener
//        AppController.getInstance().setConnectivityListener(this);
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(BaseURL.REGISTRATION_COMPLETE));
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(BaseURL.PUSH_NOTIFICATION));
//        NotificationUtils.clearNotifications(getApplicationContext());
//    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
        String user_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        if(user_id!=null)
        {
            makeGetProductRequest(user_id);
        }
        else
        {
            makeGetProductRequest("123");
        }


    }

    @Override public void onBackPressed() {
        getSupportFragmentManager().popBackStack();
       /* Cart_fragment cartFragment = (Cart_fragment) getSupportFragmentManager().findFragmentByTag("CART_FRAGMENT");
        if (cartFragment != null && cartFragment.isVisible()) {
            FragmentManager fm = getSupportFragmentManager();
            fm.popBackStack ("CART_FRAGMENT", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }*/
    }
}
