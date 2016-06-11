package com.silenz.schema;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class DayPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public DayPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DayTabFragment tab1 = new DayTabFragment();
                tab1.setDay("1");
                return tab1;
            case 1:
                DayTabFragment tab2 = new DayTabFragment();
                tab2.setDay("2");
                return tab2;
            case 2:
                DayTabFragment tab3 = new DayTabFragment();
                tab3.setDay("4");
                return tab3;
            case 3:
                DayTabFragment tab4 = new DayTabFragment();
                tab4.setDay("8");
                return tab4;
            case 4:
                DayTabFragment tab5 = new DayTabFragment();
                tab5.setDay("16");
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
