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
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.transitionseverywhere.TransitionManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.onClick
import uk.co.senab.photoview.PhotoViewAttacher


class MainActivity : AppCompatActivity() {
    var tabsLoaded: Boolean = false
    val TABVIEW_INDEX_IN_VIEWFLIPPER = 0
    val WEEKVIEW_INDEX_IN_VIEWFLIPPER = 1
    fun loadSchema() {
        val wm = baseContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay

        val preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        if (preferences.getBoolean("weekview", false)) {
            TransitionManager.beginDelayedTransition(main_appbar);
            tabs.visibility = View.GONE
            viewFlipper.displayedChild = WEEKVIEW_INDEX_IN_VIEWFLIPPER


            Picasso.with(applicationContext).load(Schema(SaveMultipleUsers.getLastSchoolId(baseContext), SaveMultipleUsers.getLastUser(baseContext)).getUrlThisWeek(applicationContext)).into(schemaImageView);

        } else {
            TransitionManager.beginDelayedTransition(main_appbar);
            tabs.visibility = View.VISIBLE


            viewFlipper.displayedChild = TABVIEW_INDEX_IN_VIEWFLIPPER
            if (!tabsLoaded) {
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
                tabsLoaded = true
            }
        }

        //TODO: This doesn't do anything
        schemaImageView.setOnMatrixChangeListener { rect ->
            run {
                println(rect.bottom)
                if (rect.bottom > (display.height * 0.7)) {
                    adView.visibility = View.GONE
                } else
                    adView.visibility = View.VISIBLE
            }
        }
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
        fab.onClick {
            val intent = Intent(this, SwitchActivity::class.java)
            startActivity(intent)

        }


//        ad_view.loadAd(adRequest);
        var prefs = baseContext.getSharedPreferences(
                "UserData", Context.MODE_PRIVATE)
        if (!prefs.contains("userID")) {
            val intent = Intent(this, SwitchActivity::class.java)
            startActivity(intent)
        }
        loadSchema() //Load picture into imageview
        PhotoViewAttacher(schemaImageView) //Make imageview scroll and zoom

        val adRequest = AdRequest.Builder().addTestDevice("91BFA35BF06E88B5A3E55F10C761F502").build();
        adView.loadAd(adRequest);


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        menu.findItem(R.id.weekview).isChecked = preferences.getBoolean("weekview", false)
        return super.onCreateOptionsMenu(menu);

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                return true
                val database = FirebaseDatabase.getInstance();
                val myRef = database.getReference("message");

                myRef.setValue("Hello, World!");
            } //TODO: When settings are added.

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
                loadSchema()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
