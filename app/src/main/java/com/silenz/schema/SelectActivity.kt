package com.silenz.schema

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.content_select.*
import org.apache.commons.lang3.StringEscapeUtils


class SelectActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    var mAdapter: SelectAdapter? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.search_menu, menu)

        var item = menu.findItem(R.id.action_search);
        var searchView = MenuItemCompat.getActionView(item) as SearchView
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu)


    }

    override fun onQueryTextChange(query: String): Boolean {
        mAdapter?.filter(query)
        println(query)
        // Here is where we are going to implement our filter logic
        return false;
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    fun getNovaTypes(mAdapter: SelectAdapter) {
        var urlcode = ""
        println("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"))
        if ( intent.getStringExtra("schoolID") != "0") {
            Fuel.post("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"),
                    listOf()).responseString { request, response, result ->
                run {
                    mAdapter.clearAll()

                    if (response.httpStatusCode == 200) {
                        val (d, e) = result
                        if (d != null) {
                            if (d.split("id=\"TypeDropDownList\">").size > 1) {
                                val typeHTML = d.split("id=\"TypeDropDownList\">")[1].split("</select>")[0]
                                for (i in 2..typeHTML.split("<option").size - 1) {
                                    mAdapter.add(StringEscapeUtils.unescapeHtml4(typeHTML.split("\">")[i].split("<")[0]), typeHTML.split("value=\"")[i].split("\"")[0])
                                    println("stuff is" + typeHTML.split("\">")[i].split("<")[0])
                                    //                                println(typeHTML.split("\">")?.get(i).split("<")[0])
                                }
                            } else if (d.split("<select name=").size > 1) {
                                println("Found dropdowns")

                                for (i in 1..d.split("<select name=").size - 1) {
                                    if (d.split("<select name=\"")[i].split("\"")[0].contains("ctl")) {
                                        mAdapter.add(StringEscapeUtils.unescapeHtml4(
                                                d.split("<select name=")[i]
                                                        .split("<option ")[1]
                                                        .split(">")[1]
                                                        .split("</")[0]
                                                        .replace("(", "")
                                                        .replace(")", "")),

                                                d.split("<select name=\"")[i]
                                                        .split("\"")[0])
                                    }
                                }
                                println("first: " + StringEscapeUtils.unescapeHtml4(d
                                        .split("<select name=")[1]
                                        .split("<option ")[1]
                                        .split(">")[1]
                                        .split("</")[0]
                                        .replace("(", "")
                                        .replace(")", "")))
                            }
                        }


                    } else {

                    }

                    mAdapter.add(getString(R.string.write_your_own_id), "custom_id")

                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        select_recycler_view.setHasFixedSize(true)

        // use a linear layout manager
        val mLayoutManager = LinearLayoutManager(this)
        select_recycler_view.layoutManager = mLayoutManager
        var myDataset = arrayOf("")
        // specify an adapter (see also next example)
        mAdapter = SelectAdapter(this, intent)
        select_recycler_view.adapter = mAdapter

        (mAdapter as SelectAdapter).clear()
        getNovaTypes(mAdapter as SelectAdapter)
        //        getNovaIDs()

    }


}
