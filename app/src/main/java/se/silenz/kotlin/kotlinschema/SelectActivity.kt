package se.silenz.kotlin.kotlinschema

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.content_select.*


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
                    if (response.httpStatusCode == 200) {
                        val (d, e) = result
                        println(d)
                        if (d != null) {
                            if (d.split("id=\"TypeDropDownList\">").size > 1) {
                                val typeHTML = d.split("id=\"TypeDropDownList\">").get(1).split("</select>").get(0)
                                if (typeHTML != null) {
                                    mAdapter.clearAll()
                                    for (i in 2..typeHTML.split("<option").size - 1) {
                                        mAdapter.add(typeHTML.split("\">")[i].split("<")[0], typeHTML.split("value=\"")[i].split("\"")[0])
                                        //                                println(typeHTML.split("\">")?.get(i).split("<")[0])
                                    }
                                }
                            }
                        }


                    } else {
                        println(response.httpStatusCode)
                    }


                }
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            run {
                val intent = Intent(baseContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
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
        println("Got here")

        (mAdapter as SelectAdapter).clear()
        getNovaTypes(mAdapter as SelectAdapter)
        //        getNovaIDs()

    }


}
