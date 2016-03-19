package com.silenz.schema

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.content_food.*
import java.text.SimpleDateFormat
import java.util.*

class FoodActivity : AppCompatActivity() {
    var mAdapter: FoodAdapter? = null
    fun changeDataInList(url: String) {
        Fuel.get(url).responseString { request, response, result ->
            run {
                val (pagecontent, e) = result
                mAdapter?.clear()
                for (day in 1..pagecontent!!.split("<td class=\"date\">").size - 1) {
                    //Loop through all available days

                    var dayStr = ""
                    for (item in 1..pagecontent.split("<td class=\"date\">")[day].split("<p class=\"item\">").size - 1) {
                        dayStr += pagecontent.split("<td class=\"date\">")[day].split("<p class=\"item\">")[item].split("</p>")[0] + "\n"
                    }
                    var date = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).parse(pagecontent.split("<td class=\"date\">")[day].split("<span class=\"date\">")[1].split("</span>")[0])
                    mAdapter?.add(pagecontent.split("<td class=\"date\">")[day].split("<span class=\"weekday\">")[1].split("</span>")[0],
                            dayStr, date)
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)



        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        my_recycler_view.setHasFixedSize(true)

        // use a linear layout manager
        val mLayoutManager = LinearLayoutManager(this)
        my_recycler_view.layoutManager = mLayoutManager
        var myDataset = arrayOf("Loading")
        // specify an adapter (see also next example)
        mAdapter = FoodAdapter(myDataset)
        my_recycler_view.adapter = mAdapter

        val prefs = baseContext.getSharedPreferences(
                "UserData", Context.MODE_PRIVATE)
        Fuel.get("http://skhemaf-silenz.rhcloud.com/food?query=" + prefs.getString("schoolID", "").toString()).responseJson { request, response, result ->

            run {
                when (result) {
                    is Result.Failure -> {
                        val (d, e) = result
                    }
                    is Result.Success -> {
                        val (d, e) = result
                        println("data is" + d)
                        println(d!!.getString("food"))
                        if (d.getString("food") != "null") {
                            changeDataInList(d.getString("food"))
                        } else {
                            mAdapter?.clear()
                            mAdapter?.add("Denna skola har ännu ingen matsedel kopplad till sig")
                        }
                    }
                }
                val (d, e) = result

            }
        }
    }

}
