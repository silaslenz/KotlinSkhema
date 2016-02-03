package se.silenz.kotlin.kotlinschema

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.content_select.*
import java.util.*


class SelectActivity : AppCompatActivity() {
    var mAdapter: SelectAdapter? = null
    var ids = HashMap<String, String>()

    fun askForType() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("AppCompatDialog")
        builder.setMessage("Lorem ipsum dolor...")
        builder.setPositiveButton("OK", null)
        builder.setNegativeButton("Cancel", null)
        builder.show()
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
                        val typeHTML = d?.split("id=\"TypeDropDownList\">")?.get(1)?.split("</select>")?.get(0)
                        if (typeHTML != null) {
                            for (i in 1..typeHTML.split("<option").size - 1) {
                                mAdapter.add(typeHTML.split("\">").get(i).split("<")[0])
                                //                                println(typeHTML.split("\">")?.get(i).split("<")[0])
                            }
                        }


                    } else {
                        println(response.httpStatusCode)
                    }


                }
            }
        }
    }

    fun getNovaIDs() {
        var urlcode = ""
        println("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"))
        if ( intent.getStringExtra("schoolID") != "0") {
            Fuel.post("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"),
                    listOf()).responseString { request, response, result ->
                run {
                    if (response.httpStatusCode == 200) {
                        val (d, e) = result
                        println(d)
                        urlcode = d?.split("WebViewer/")?.get(1)?.split("/printer")!![0]
                        var prefs = baseContext.getSharedPreferences(
                                "UserData", Context.MODE_PRIVATE)
                        Fuel.post("http://www.novasoftware.se/webviewer/$urlcode/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"),
                                listOf("__EVENTTARGET" to "TypeDropDownList", "__EVENTARGUMENT" to "", "__LASTFOCUS" to "", "__VIEWSTATE" to "", "TypeDropDownList" to "1", "ScheduleIDDropDownList" to "0", "FreeTextBox" to "", "PeriodDropDownList" to "8", "WeekDropDownList" to "52", "__VIEWSTATE" to "")).responseString { request, response, result ->

                            run {
                                val (d, e) = result
                                println(response.url)
                                val nicedata = d?.split("ScheduleIDDropDownList")?.get(2)?.split("</select>")?.get(0)?.split("<option value=\"");
                                val spinnerArray = ArrayList<String>()
                                spinnerArray.add(getString(R.string.first_in_id_choise))
                                for (i in 1..nicedata!!.size - 1) {

                                    spinnerArray.add(nicedata.get(i).split(">")[1].split("<")[0])
                                    println(kotlin.Pair(nicedata.get(i).split(">")[1].split("<")[0], nicedata.get(i).split("\"")[0]))
                                    ids.put(nicedata.get(i).split(">")[1].split("<")[0], nicedata.get(i).split("\"")[0])
                                }

                                println(spinnerArray);
                                //                                val adapter = ArrayAdapter<String>(
                                //                                        this, android.R.layout.simple_spinner_item, spinnerArray)

                                //                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                //                                select_spinner.adapter = adapter
                            }
                        }

                    } else {
                        println(response.httpStatusCode)
                    }


                }
            }
        }
    }

    //    fun createListeners() {
    //        select_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
    //            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
    //                var prefs = baseContext.getSharedPreferences(
    //                        "UserData", Context.MODE_PRIVATE)
    //                println("something selected")
    //                if (select_spinner.selectedItem.toString() != getString(R.string.first_in_id_choise)) {
    //                    var editor = prefs.edit();
    //                    println("Overwrite stuff")
    //                    println(select_spinner.selectedItem.toString())
    //                    println(ids[select_spinner.selectedItem.toString()])
    //                    editor.putString("userID", ids[select_spinner.selectedItem.toString()])
    //                    editor.putString("userName", select_spinner.selectedItem.toString())
    //
    //                    editor.putInt("schoolID", intent.getStringExtra("schoolID").toInt())
    //                    editor.putString("schoolName", intent.getStringExtra("schoolName"))
    //                    editor.putString("schoolCode", intent.getStringExtra("schoolCode"))
    //                    editor.commit();
    //
    //                }
    //            }
    //
    //            override fun onNothingSelected(parentView: AdapterView<*>) {
    //                println("nothing selected")
    //                // your code here
    //
    //            }
    //
    //        }
    //
    //    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            run {
                val intent = Intent(baseContext, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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
        mAdapter = SelectAdapter(myDataset)
        select_recycler_view.adapter = mAdapter
        println("Got here")

        (mAdapter as SelectAdapter).clear()
        getNovaTypes(mAdapter as SelectAdapter)
        //        getNovaIDs()

    }


}
