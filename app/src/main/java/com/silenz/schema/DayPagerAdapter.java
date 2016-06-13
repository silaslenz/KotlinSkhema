package com.silenz.schema;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.joda.time.DateTime;

public class DayPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    DateTime date;

    public DayPagerAdapter(FragmentManager fm, int NumOfTabs, DateTime date) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.date = date;
    }

    @Override
    public Fragment getItem(int position) {

        System.out.println("Loading item" + position);
        switch (position) {
            case 0:
                DayTabFragment tab1 = new DayTabFragment("1", date);
                return tab1;
            case 1:
                DayTabFragment tab2 = new DayTabFragment("2", date);

                return tab2;
            case 2:
                DayTabFragment tab3 = new DayTabFragment("4", date);

                return tab3;
            case 3:
                DayTabFragment tab4 = new DayTabFragment("8", date);

                return tab4;
            case 4:
                DayTabFragment tab5 = new DayTabFragment("16", date);

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
