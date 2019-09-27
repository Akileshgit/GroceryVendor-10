package vendor.tcc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ThanksActivity extends AppCompatActivity  implements View.OnClickListener {

    TextView tv_info;
    RelativeLayout btn_home, btn_order;

    SharedPreferences preferences;

    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionbar.setTitle("Thank You!!");

        preferences = getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");

       /* view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
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

        String data = getIntent().getStringExtra("msg");

        tv_info = (TextView) findViewById(R.id.tv_thank_info);
        btn_home = (RelativeLayout) findViewById(R.id.btn_thank_home);
        btn_order = (RelativeLayout) findViewById(R.id.btn_track_order);

        if (language.contains("english")) {
            tv_info.setText(Html.fromHtml(data));
        }


        btn_home.setOnClickListener(this);
        btn_order.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_thank_home) {
           /* Fragment fm = new Home_fragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();*/
            Intent i = new Intent(ThanksActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        if (id == R.id.btn_track_order) {

            Intent myIntent = new Intent(ThanksActivity.this, My_Order_activity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(myIntent);
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
