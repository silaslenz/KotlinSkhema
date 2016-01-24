package se.silenz.kotlin.kotlinschema

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_switch.*
import kotlinx.android.synthetic.main.content_switch.*
import org.json.JSONObject
import java.util.*

class SwitchActivity : AppCompatActivity() {

    fun createListeners() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                getSearchResults(s.toString())
            }
        })

        continueWithCurrentButton.setOnClickListener {
            val intent = Intent(baseContext, SelectActivity::class.java)
            startActivity(intent)
        }
        searchButton.setOnClickListener {
            searchUI.visibility = VISIBLE
            chooseLayout.visibility = View.GONE
        }
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
                        editor.putString("schoolName", response.get("name").toString() + " (" + response.get("location").toString() + ")");
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
            (0..searchResults!!.names().length() - 1).forEach { i -> your_array_list.add(searchResults.getJSONObject(searchResults.names().getString(i)).getString("name") + " (" + searchResults.getJSONObject(searchResults.names().getString(i)).getString("location") + ")") }
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
        setContentView(R.layout.activity_switch)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)



        fab.setOnClickListener { view ->
            run {
                finish()
            }
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val prefs = baseContext.getSharedPreferences(
                "UserData", Context.MODE_PRIVATE)
        if (prefs.contains("schoolName") == false) {
            continueWithCurrentButton.visibility = View.GONE
        } else {
            continueWithCurrentButton.text = "Fortsätt med " + prefs.getString("schoolName", "")
        }

        createListeners()


    }

}
