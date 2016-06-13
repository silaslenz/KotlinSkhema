package com.silenz.schema

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.alexvasilkov.gestures.GestureController
import com.alexvasilkov.gestures.State
import com.alexvasilkov.gestures.views.GestureImageView
import com.bumptech.glide.Glide
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment
import com.google.android.gms.ads.AdRequest
import com.transitionseverywhere.TransitionManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.async
import org.jetbrains.anko.onClick
import org.jetbrains.anko.uiThread
import org.joda.time.DateTime
import java.util.*


class MainActivity : AppCompatActivity(), CalendarDatePickerDialogFragment.OnDateSetListener {

    override fun onDateSet(dialog: CalendarDatePickerDialogFragment?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val date = DateTime(year, monthOfYear + 1, dayOfMonth, 10, 0)
        if (preferences.getBoolean("weekview", false)) {
            loadSchema(date)
        } else {
            when (date.dayOfWeek) {
                in 1..5 -> tabs?.getTabAt(date.dayOfWeek - 1)?.select()
                else -> tabs?.getTabAt(0)?.select()
            }

            //Try to send our new date to day-fragments
            adapter?.update(date)
        }

    }

    var adapter: DayPagerAdapter? = null
    var tabsLoaded: Boolean = false
    val TABVIEW_INDEX_IN_VIEWFLIPPER = 0
    val WEEKVIEW_INDEX_IN_VIEWFLIPPER = 1
    fun GestureImageView.loadUrl(url: String) {
        Glide.with(context).load(url).skipMemoryCache(true).into(this)
    }

    fun loadSchema(date: DateTime) {
        //Add adview back, in case it was hidden while zooming.
        adView.visibility = View.VISIBLE

        val preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        if (preferences.getBoolean("weekview", false)) {
            //Animate the disappearance of the tab bar.
            TransitionManager.beginDelayedTransition(main_appbar);
            tabs.visibility = View.GONE

            //Switch to week and load
            viewFlipper.displayedChild = WEEKVIEW_INDEX_IN_VIEWFLIPPER
            schemaImageView.loadUrl(Schema(SaveMultipleUsers.getLastSchoolId(baseContext), SaveMultipleUsers.getLastUser(baseContext), date = date).getUrlThisWeek(applicationContext))

        } else {
            //Animate the appearance of the tab bar.
            TransitionManager.beginDelayedTransition(main_appbar);
            tabs.visibility = View.VISIBLE

            //Switch to dayview and load tabs
            viewFlipper.displayedChild = TABVIEW_INDEX_IN_VIEWFLIPPER
            if (!tabsLoaded) {

                val tabs = findViewById(R.id.tabs) as TabLayout?
                tabs?.addTab(tabs.newTab().setText("Mo"))
                tabs?.addTab(tabs.newTab().setText("Tu"))
                tabs?.addTab(tabs.newTab().setText("We"))
                tabs?.addTab(tabs.newTab().setText("Th"))
                tabs?.addTab(tabs.newTab().setText("Fr"))
                tabs?.tabGravity = TabLayout.GRAVITY_FILL
                val viewPager = findViewById(R.id.viewpager) as ViewPager?
                if (tabs != null) {
                    adapter = DayPagerAdapter(supportFragmentManager, tabs.tabCount, date)
                    viewPager?.adapter = adapter

                }

                viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
                tabs?.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        viewPager?.currentItem = tab.position
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {

                    }

                    override fun onTabReselected(tab: TabLayout.Tab) {

                    }
                })

                when (DateTime.now().dayOfWeek) {
                    in 1..5 -> tabs?.getTabAt(DateTime.now().dayOfWeek - 1)?.select()
                    else -> tabs?.getTabAt(0)?.select()
                }

                tabsLoaded = true
            }
        }


        schemaImageView.controller.addOnStateChangeListener(object : GestureController.OnStateChangeListener {
            var lastzoom: Float = 0f
            override fun onStateChanged(state: State) {
                // Enable swipe to refresh at the top of the image. (Note that state.y is negativ when the image is scrolled down)
                if (state.y == 0f) {
                    swiperefresh.isEnabled = true;
                } else {
                    swiperefresh.isEnabled = false;
                }
                if (lastzoom != 0f && lastzoom <= state.zoom)
                    adView.visibility = View.INVISIBLE
                else
                    adView.visibility = View.VISIBLE
                lastzoom = state.zoom

            }

            override fun onStateReset(oldState: State?, newState: State?) {

            }

        })
    }

    override fun onResume() {
        println("RESUMED")

        super.onResume()
        loadSchema(DateTime.now())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val prefs = baseContext.getSharedPreferences(
                "UserData", Context.MODE_PRIVATE)
        if (!prefs.contains("userID")) {
            changeUser()
        }
        loadSchema(DateTime.now()) //Load picture into imageview

        val adRequest = AdRequest.Builder().addTestDevice("91BFA35BF06E88B5A3E55F10C761F502").addTestDevice("77D6C271CDE15F2739509621D41B407B").build();
        adView.loadAd(adRequest);

        schemaImageView.controller.settings.gravity = Gravity.TOP
        schemaImageView.controller.settings.maxZoom = 5f

        val swipeRefreshLayout = findViewById(R.id.swiperefresh) as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {

            async() {
                Log.w("Glide", "Clearing memory")
                Glide.get(applicationContext).clearDiskCache()

                Log.w("Glide", "Memory cleared")
                uiThread {
                    swipeRefreshLayout.isRefreshing = false
                    loadSchema(DateTime.now())
                    adapter?.update(DateTime.now())
                }
            }
        }

        fab_action_changeuser.onClick {
            changeUser()
        }
        fab_action_changeday.onClick {

            val cdp = CalendarDatePickerDialogFragment()
                    .setOnDateSetListener(this)
                    .setFirstDayOfWeek(Calendar.MONDAY)
                    .setDoneText("Select")
                    .setCancelText("Cancel")
                    .setThemeLight()
            cdp.show(supportFragmentManager, "test");
        }


    }

    private fun changeUser() {
        val intent = Intent(this, SwitchActivity::class.java)
        startActivity(intent)
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
            } //TODO: When settings are added.

            R.id.action_food -> {
                val intent = Intent(this, FoodActivity::class.java)
                startActivity(intent)
            }

            R.id.weekview -> {
                item.isChecked = !item.isChecked
                toggleWeekViewSettings(item)
                loadSchema(DateTime.now())
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun toggleWeekViewSettings(item: MenuItem) {
        val preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("weekview", item.isChecked)
        editor.commit()
    }
}
