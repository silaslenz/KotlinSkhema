package com.silenz.schema

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import org.joda.time.DateTime

class DayPagerAdapter(fm: FragmentManager, private var mNumOfTabs: Int, internal var date: DateTime) : FragmentStatePagerAdapter(fm) {

    //call this method to update fragments in ViewPager dynamically
    fun update(date: DateTime) {
        this.date = date
        notifyDataSetChanged()
    }

    override fun getItemPosition(`object`: Any): Int {
        (`object` as? UpdateableFragment)?.update(date)
        //don't return POSITION_NONE, avoid fragment recreation.
        return super.getItemPosition(`object`)
    }

    override fun getItem(position: Int): Fragment? {
        // Novasoftware positions are not logical
        println("Loading item" + position)
        when (position) {
            0 -> {
                return DayTabFragment("1", date)
            }
            1 -> {

                return DayTabFragment("2", date)
            }
            2 -> {

                return DayTabFragment("4", date)
            }
            3 -> {

                return DayTabFragment("8", date)
            }
            4 -> {

                return DayTabFragment("16", date)
            }
            else -> return null
        }
    }


    override fun getCount(): Int = mNumOfTabs
}
