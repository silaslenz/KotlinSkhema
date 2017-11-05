package com.silenz.schema

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import org.joda.time.DateTime

class DayPagerAdapter(fm: FragmentManager, internal var mNumOfTabs: Int, internal var date: DateTime) : FragmentStatePagerAdapter(fm) {

    //call this method to update fragments in ViewPager dynamically
    fun update(date: DateTime) {
        this.date = date
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        if (`object` is UpdateableFragment) {
            `object`.update(date)
        }
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(`object`)
    }

    override fun getItem(position: Int): Fragment? {

        println("Loading item" + position)
        when (position) {
            0 -> {
                val tab1 = DayTabFragment("1", date)
                return tab1
            }
            1 -> {
                val tab2 = DayTabFragment("2", date)

                return tab2
            }
            2 -> {
                val tab3 = DayTabFragment("4", date)

                return tab3
            }
            3 -> {
                val tab4 = DayTabFragment("8", date)

                return tab4
            }
            4 -> {
                val tab5 = DayTabFragment("16", date)

                return tab5
            }
            else -> return null
        }
    }


    override fun getCount(): Int {
        return mNumOfTabs
    }
}
