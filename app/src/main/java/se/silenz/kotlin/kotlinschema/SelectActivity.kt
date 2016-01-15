package se.silenz.kotlin.kotlinschema

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.text.TextWatcher
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.content_select.*
import java.util.*


class SelectActivity : AppCompatActivity() {
    var ids = HashMap<String, String>()

    fun askForType() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("AppCompatDialog")
        builder.setMessage("Lorem ipsum dolor...")
        builder.setPositiveButton("OK", null)
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    fun getNovaIDs() {
        var urlcode = ""
        Fuel.post("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=88470&code=151693",
                listOf()).responseString { request, response, result ->
            run {
                if (response.httpStatusCode == 200) {
                    val (d, e) = result
                    println("something happens")
                    urlcode = d?.split("WebViewer/")?.get(1)?.split("/printer")!![0]
                    Fuel.post("http://www.novasoftware.se/webviewer/$urlcode/MZDesign1.aspx?schoolid=88470&code=151693",
                            listOf("__EVENTTARGET" to "TypeDropDownList", "__EVENTARGUMENT" to "", "__LASTFOCUS" to "", "__VIEWSTATE" to "", "TypeDropDownList" to "1", "ScheduleIDDropDownList" to "0", "FreeTextBox" to "", "PeriodDropDownList" to "8", "WeekDropDownList" to "52", "__VIEWSTATE" to "")).responseString { request, response, result ->

                        run {
                            val (d, e) = result
                            println(response.url)
                            val nicedata = d?.split("ScheduleIDDropDownList")?.get(2)?.split("<option value=\"");
                            val spinnerArray = ArrayList<String>()
                            spinnerArray.add("Välj id")
                            for (i in 1..15) {
                                spinnerArray.add(nicedata?.get(i)?.split(">")!![1].split("<")[0])
                                println(kotlin.Pair(nicedata?.get(i)?.split(">")!![1].split("<")[0], nicedata?.get(i)?.split("\"")!![0]))
                                ids.put(nicedata?.get(i)?.split(">")!![1].split("<")[0], nicedata?.get(i)?.split("\"")!![0])
                                println(ids)
                            }


                            val adapter = ArrayAdapter<String>(
                                      this, android.R.layout.simple_spinner_item, spinnerArray)

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            select_spinner.adapter = adapter
                        }
                    }
                }


            }
        }
    }

    fun createListeners() {
        select_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                var prefs = baseContext.getSharedPreferences(
                        "UserData", Context.MODE_PRIVATE)
                println("something selected")
                if (select_spinner.selectedItem.toString() != ("Välj id")) {
                    var editor = prefs.edit();
                    println("Overwrite stuff")
                    println(select_spinner.selectedItem.toString())
                    println(ids[select_spinner.selectedItem.toString()])
                    editor.putString("userID", ids[select_spinner.selectedItem.toString()]);
                    editor.commit();

                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                println("nothing selected")
                // your code here
            }

        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
               getSearchResults(s.toString())
            }
        })
    }
    fun getSearchResults(query:String) {
        Fuel.post("http://skhema-1190.appspot.com/get?location="+query,
                listOf("__EVENTTARGET" to "TypeDropDownList", "__EVENTARGUMENT" to "", "__LASTFOCUS" to "", "__VIEWSTATE" to "", "TypeDropDownList" to "1", "ScheduleIDDropDownList" to "0", "FreeTextBox" to "", "PeriodDropDownList" to "8", "WeekDropDownList" to "52", "__VIEWSTATE" to "")).responseString { request, response, result ->

            run {
                val (d, e) = result
                changeListView(listOf(d.toString()))
            }
        }
    }
    fun changeListView(searchResults: List<String>){
        var  your_array_list = ArrayList<String>();

        for (s in searchResults){
            your_array_list.add(s)
        }
        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        var arrayAdapter =  ArrayAdapter<String>(baseContext,android.R.layout.simple_list_item_1,your_array_list )

        listView.adapter = arrayAdapter;
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            run {
                finish()
            }
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val spinnerArray = ArrayList<String>()
        getNovaIDs()
        spinnerArray.add("itemx")
        spinnerArray.add("item2")

        val adapter = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        school_spinner.adapter = adapter
        createListeners()
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)


    }


}
