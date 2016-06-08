//package com.truiton.designsupporttabs
//
//import android.os.Bundle
//import android.support.design.widget.TabLayout
//import android.support.v4.view.ViewPager
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.Toolbar
//import android.view.Menu
//import android.view.MenuItem
//
//import com.silenz.schema.DayPagerAdapter
//import com.silenz.schema.R
//
//
//class MainActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        val toolbar = findViewById(R.id.toolbar) as Toolbar?
//        setSupportActionBar(toolbar)
//
//        val tabLayout = findViewById(R.id.tab_layout) as TabLayout?
//        tabLayout!!.addTab(tabLayout.newTab().setText("Tab 1"))
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"))
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"))
//        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
//
//        val viewPager = findViewById(R.id.pager) as ViewPager?
//        val adapter = DayPagerAdapter(supportFragmentManager, tabLayout.tabCount)
//        viewPager!!.adapter = adapter
//        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
//        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                viewPager.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//
//            }
//        })
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        if (id == R.id.action_settings) {
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }
//}
