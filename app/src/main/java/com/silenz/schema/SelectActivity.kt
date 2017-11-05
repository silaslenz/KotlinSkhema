package com.silenz.schema

import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.content_select.*
import org.apache.commons.lang3.StringEscapeUtils


class SelectActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private var mAdapter: SelectAdapter? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.search_menu, menu)

        val item = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(item) as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)


    }

    override fun onQueryTextChange(query: String): Boolean {
        mAdapter?.filter(query)
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean = false

    private fun getNovaTypes(mAdapter: SelectAdapter) {
        if (intent.getStringExtra("schoolID") != "0") {
            Fuel.post("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"),
                    listOf()).responseString { request, response, result ->
                run {
                    mAdapter.clearAll()

                    if (response.httpStatusCode == 200) {
                        val (d, e) = result
                        if (d != null) {
                            if (d.split("id=\"TypeDropDownList\">").size > 1) {
                                val typeHTML = d.split("id=\"TypeDropDownList\">")[1].split("</select>")[0]
                                for (i in 2 until typeHTML.split("<option").size) {
                                    mAdapter.add(StringEscapeUtils.unescapeHtml4(typeHTML.split("\">")[i].split("<")[0]), typeHTML.split("value=\"")[i].split("\"")[0])
                                    println("stuff is" + typeHTML.split("\">")[i].split("<")[0])
                                    //                                println(typeHTML.split("\">")?.get(i).split("<")[0])
                                }
                            } else if (d.split("<select name=").size > 1) {
                                Log.i("SelectActivity", "Site uses multiple dropdowns for different types")

                                (1 until d.split("<select name=").size)
                                        .filter { d.split("<select name=\"")[it].split("\"")[0].contains("ctl") }
                                        .forEach {
                                            mAdapter.add(StringEscapeUtils.unescapeHtml4(
                                                    d.split("<select name=")[it]
                                                            .split("<option ")[1]
                                                            .split(">")[1]
                                                            .split("</")[0]
                                                            .replace("(", "")
                                                            .replace(")", "")),

                                                    d.split("<select name=\"")[it]
                                                            .split("\"")[0])
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
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Improves performance if layout does not change
        select_recycler_view.setHasFixedSize(true)

        // use a linear layout manager
        val mLayoutManager = LinearLayoutManager(this)
        select_recycler_view.layoutManager = mLayoutManager
        // specify an adapter
        mAdapter = SelectAdapter(this, intent)
        select_recycler_view.adapter = mAdapter

        (mAdapter as SelectAdapter).clear()
        getNovaTypes(mAdapter as SelectAdapter)

    }


}
