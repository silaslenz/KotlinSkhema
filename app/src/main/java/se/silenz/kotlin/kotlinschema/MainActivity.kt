package se.silenz.kotlin.kotlinschema

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    fun loadSchema() {
        var prefs = baseContext.getSharedPreferences(
                "UserData", Context.MODE_PRIVATE)
        val wm = baseContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        Picasso.with(applicationContext).load(Schema(prefs.getString("schoolID", "").toString(), prefs.getString("userID", "")).getUrlThisWeek(applicationContext)).into(schemaImageView);
        schemaImageView.setOnMatrixChangeListener { rect ->
            run {
                if (rect.bottom > (display.height * 0.8)) {
                    ad_view.visibility = View.GONE
                } else
                    ad_view.visibility = View.VISIBLE
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
        fab.setOnClickListener { view ->
            run {
                val intent = Intent(this, SwitchActivity::class.java)
                startActivity(intent)
            }
        }


        val mAdView = findViewById(R.id.ad_view) as AdView;
        //        val adRequest = AdRequest.Builder().build();
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("AC98C820A50B4AD8A2106EDE96FB87D4")  // An example device ID
                .build();
        mAdView.loadAd(adRequest);
        var prefs = baseContext.getSharedPreferences(
                "UserData", Context.MODE_PRIVATE)
        if (!prefs.contains("userID")) {
            val intent = Intent(this, SwitchActivity::class.java)
            startActivity(intent)
        }
        loadSchema() //Load picture into imageview
        //        PhotoViewAttacher(schemaImageView) //Make imageview scroll and zoom

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)


        return super.onCreateOptionsMenu(menu);

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }
        if (id == R.id.action_food) {

            val intent = Intent(this, FoodActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}
