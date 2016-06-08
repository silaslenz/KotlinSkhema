package com.silenz.schema

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.google.android.gms.ads.AdRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.onClick

class MainActivity : AppCompatActivity() {

    fun loadSchema() {

        val wm = baseContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
//        Picasso.with(applicationContext).load(Schema(SaveMultipleUsers.getLastSchoolId(baseContext), SaveMultipleUsers.getLastUser(baseContext)).getUrlThisWeek(applicationContext)).into(schemaImageView);
//        schemaImageView.setOnMatrixChangeListener { rect ->
//            run {
//                if (rect.bottom > (display.height * 0.8)) {
//                    ad_view.visibility = View.GONE
//                } else
//                    ad_view.visibility = View.VISIBLE
//            }
//        }


    }


    override fun onResume() {
        println("RESUMED")
        super.onResume()
        loadSchema()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
//        fab.onClick {
//            val intent = Intent(this, SwitchActivity::class.java)
//            startActivity(intent)
//        }


        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4")  // An example device ID
                .build();
//        ad_view.loadAd(adRequest);
        var prefs = baseContext.getSharedPreferences(
                "UserData", Context.MODE_PRIVATE)
        if (!prefs.contains("userID")) {
            val intent = Intent(this, SwitchActivity::class.java)
            startActivity(intent)
        }
        loadSchema() //Load picture into imageview
        //        PhotoViewAttacher(schemaImageView) //Make imageview scroll and zoom

        val tabs = findViewById(R.id.tabs) as TabLayout?
        tabs!!.addTab(tabs.newTab().setText("Mo"))
        tabs.addTab(tabs.newTab().setText("Tu"))
        tabs.addTab(tabs.newTab().setText("We"))
        tabs.addTab(tabs.newTab().setText("Th"))
        tabs.addTab(tabs.newTab().setText("Fr"))
        tabs.tabGravity = TabLayout.GRAVITY_FILL

        val viewPager = findViewById(R.id.viewpager) as ViewPager?
        val adapter: DayPagerAdapter
        adapter = DayPagerAdapter(supportFragmentManager, tabs.tabCount)
        viewPager!!.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)


        return super.onCreateOptionsMenu(menu);

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true //TODO: When settings are added.

            R.id.action_food -> {
                val intent = Intent(this, FoodActivity::class.java)
                startActivity(intent)
            }

            R.id.weekview -> {
                item.isChecked = !item.isChecked
                val preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putBoolean("weekview", item.isChecked)
                editor.commit()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
