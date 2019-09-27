package Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import Fragment.Fruit_fragment;
import Fragment.Vegetables_fragment;
import Fragment.Leaf_fragment;

import java.util.List;

import Config.BaseURL;
import Model.Category_model;

public class PagerHome_adapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    String apiResponse;

    public PagerHome_adapter(FragmentManager fm, int NumOfTabs, String apiResponse) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.apiResponse = apiResponse;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Vegetables_fragment tab1 = new Vegetables_fragment();
                return tab1;
            case 1:
                Leaf_fragment tab2 = new Leaf_fragment();
                return tab2;
            case 2:
                Fruit_fragment tab3 = new Fruit_fragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}


