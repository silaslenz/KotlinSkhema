package se.silenz.kotlin.kotlinschema

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.content_food.*

class FoodActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
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
        val mAdapter = MyAdapter(myDataset)
        my_recycler_view.adapter = mAdapter

        Fuel.get("http://skolmaten.se/katedralskolan2/").responseString { request, response, result ->
            run {
                val (d, e) = result
                //                println("data is"+d)
                //                println(d!!.split("<td class=\"date\">")?.get(1)?.split("<p class=\"item\">")[position+1]?.split("</p>")?.get(0))
                mAdapter.clear()
                for (i in 1..d!!.split("<td class=\"date\">").size - 1) {

                    var dayStr = ""
                    for (j in 1..d.split("<td class=\"date\">").get(i).split("<p class=\"item\">").size - 1) {
                        dayStr += d.split("<td class=\"date\">").get(i).split("<p class=\"item\">")[j].split("</p>").get(0) + "\n"
                    }
                    mAdapter.add(d.split("<td class=\"date\">")[i].split("<span class=\"weekday\">")[1].split("</span>")[0], dayStr)
                }
            }
        }

    }

}
