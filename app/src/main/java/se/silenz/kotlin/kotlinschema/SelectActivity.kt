package se.silenz.kotlin.kotlinschema

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.content_select.*
import org.json.JSONObject
import java.text.FieldPosition
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
        var prefs = baseContext.getSharedPreferences(
                "UserData", Context.MODE_PRIVATE)
        println(prefs.getInt("schoolID",0))
        println("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=" + prefs.getInt("schoolID", 0).toString() + "&code="+prefs.getString("schoolCode",""))
        if (prefs.getInt("schoolID",0)!=0 ) {
            Fuel.post("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=" + prefs.getInt("schoolID", 0).toString() + "&code="+prefs.getString("schoolCode",""),
                    listOf()).responseString { request, response, result ->
                run {
                    if (response.httpStatusCode == 200) {
                        val (d, e) = result
                        println(d)
                        urlcode = d?.split("WebViewer/")?.get(1)?.split("/printer")!![0]
                        var prefs = baseContext.getSharedPreferences(
                                "UserData", Context.MODE_PRIVATE)
                        Fuel.post("http://www.novasoftware.se/webviewer/$urlcode/MZDesign1.aspx?schoolid=" + prefs.getInt("schoolID", 0).toString() + "&code="+prefs.getString("schoolCode",""),
                                listOf("__EVENTTARGET" to "TypeDropDownList", "__EVENTARGUMENT" to "", "__LASTFOCUS" to "", "__VIEWSTATE" to "", "TypeDropDownList" to "1", "ScheduleIDDropDownList" to "0", "FreeTextBox" to "", "PeriodDropDownList" to "8", "WeekDropDownList" to "52", "__VIEWSTATE" to "")).responseString { request, response, result ->

                            run {
                                val (d, e) = result
                                println(response.url)
                                val nicedata = d?.split("ScheduleIDDropDownList")?.get(2)?.split("<option value=\"");
                                val spinnerArray = ArrayList<String>()
                                spinnerArray.add(getString(R.string.first_in_id_choise))
                                for (i in 1..15) {
                                    spinnerArray.add(nicedata?.get(i)?.split(">")!![1].split("<")[0])
                                    println(kotlin.Pair(nicedata?.get(i)?.split(">")!![1].split("<")[0], nicedata?.get(i)?.split("\"")!![0]))
                                    ids.put(nicedata?.get(i)?.split(">")!![1].split("<")[0], nicedata?.get(i)?.split("\"")!![0])
                                }


                                val adapter = ArrayAdapter<String>(
                                        this, android.R.layout.simple_spinner_item, spinnerArray)

                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                select_spinner.adapter = adapter
                            }
                        }
                    }
                    else {
                        println(response.httpStatusCode)
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
                if (select_spinner.selectedItem.toString() != getString(R.string.first_in_id_choise)) {
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

    fun getSearchResults(query: String) {
        Fuel.get("http://skhemaf-silenz.rhcloud.com/get?query=" + query).responseJson { request, response, result ->

            run {
                when (result) {
                    is Result.Failure -> {
                        val (d, e) = result
                    }
                    is Result.Success -> {
                        val (d, e) = result
                        println("data is" + d)
                        if (d!!.length() >= 1) {
                            changeListView(d)
                        } else {
                            changeListView(JSONObject())
                        }
                    }
                }
                val (d, e) = result

            }
        }
    }

    fun translateBack(query: String) {
        Fuel.get("http://skhemaf-silenz.rhcloud.com/get?name=" + query).responseJson { request, response, result ->

            run {
                when (result) {
                    is Result.Failure -> {
                        val (d, e) = result
                    }
                    is Result.Success -> {
                        val (d, e) = result
                        val response = d as JSONObject
                        println("data is" + d.toString())
                        var prefs = baseContext.getSharedPreferences(
                                "UserData", Context.MODE_PRIVATE)
                        var editor = prefs.edit();
                        editor.putInt("schoolID", response.get("schoolid") as Int);
                        editor.putString("schoolCode", response.get("code").toString());
                        editor.commit();
                    }
                }
                val (d, e) = result

            }
        }
    }

    fun changeListView(searchResults: JSONObject?) {
        var your_array_list = ArrayList<String>();

        //Add all items to array
        if (searchResults?.length() != 0) {
            (0..searchResults!!.names().length() - 1).forEach { i -> your_array_list.add(searchResults!!.getJSONObject(searchResults.names().getString(i)).getString("name") + " (" + searchResults!!.getJSONObject(searchResults.names().getString(i)).getString("location") + ")") }
        } else {
            your_array_list.add("Nothing found")
        }


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        //        var arrayAdapter =  object: ArrayAdapter<String>(baseContext, android.R.layout.simple_list_item_1, your_array_list){
        //            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        //                var view = convertView
        //                var vi = getSystemService(LAYOUT_INFLATER_SERVICE)
        //                var v = vi.inflate()
        //            }
        //        }
        //
        //        listView.adapter = arrayAdapter;
        listView.adapter = ArrayAdapter<String>(baseContext, android.R.layout.simple_list_item_1, your_array_list)
        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                translateBack(listView.getItemAtPosition(position) as String)
                println(listView.getItemAtPosition(position))
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
                finish()
            }
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        createListeners()
        getNovaIDs()


    }


}
