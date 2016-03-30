package com.silenz.schema

import android.R
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

        continueCardView.setOnClickListener {
            val intent = Intent(baseContext, SelectActivity::class.java)
            intent.putExtra("schoolID", SaveMultipleUsers.getLastSchoolId(baseContext))
            intent.putExtra("schoolCode", SaveMultipleUsers.getLastSchoolCode(baseContext))
            intent.putExtra("schoolName", SaveMultipleUsers.getLastSchoolName(baseContext))
            startActivity(intent)
        }

        searchCardView.setOnClickListener {
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
                        //TODO: Error handling
                    }
                    is Result.Success -> {
                        val (response: JSONObject?, e) = result

                        val intent = Intent(baseContext, SelectActivity::class.java)
                        intent.putExtra("schoolID", response?.get("schoolid").toString())
                        intent.putExtra("schoolCode", response?.get("code").toString())
                        intent.putExtra("schoolName", response?.get("name").toString() + " (" + response?.get("location").toString() + ")")

                        startActivity(intent)
                    }
                }
            }
        }
    }

    fun changeListView(searchResults: JSONObject?) {
        var your_array_list = ArrayList<String>();

        //Add all items to array
        if (searchResults != null && searchResults.length() != 0 ) {
            (0..searchResults.names().length() - 1).forEach {
                item ->
                your_array_list.add("${searchResults.getJSONObject(searchResults.names().getString(item)).getString("name")} (${searchResults.getJSONObject(searchResults.names().getString(item)).getString("location")})")
            }
        } else {
            your_array_list.add("Nothing found")
        }


        schoolSearchListView.adapter = ArrayAdapter<String>(baseContext, R.layout.simple_list_item_1, your_array_list)
        schoolSearchListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            translateBack(schoolSearchListView.getItemAtPosition(position) as String)
            println(schoolSearchListView.getItemAtPosition(position))
        }

    }

    fun populateRecentsListView() {
        val historicalUsersList = SaveMultipleUsers.getUserName(baseContext).reversed() as ArrayList<String>

        println(historicalUsersList)
        recentsListView.adapter = ArrayAdapter<String>(baseContext, R.layout.simple_list_item_1, historicalUsersList)
        recentsListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            println(recentsListView.getItemAtPosition(position))
            println(historicalUsersList.size - (position + 1))
            SaveMultipleUsers.addUser(baseContext,
                    recentsListView.getItemAtPosition(position) as String,
                    SaveMultipleUsers.getUser(baseContext)[SaveMultipleUsers.getUser(baseContext).size - (position + 1)],
                    SaveMultipleUsers.getSchoolId(baseContext)[SaveMultipleUsers.getSchoolId(baseContext).size - (position + 1)],
                    SaveMultipleUsers.getSchoolCode(baseContext)[SaveMultipleUsers.getSchoolCode(baseContext).size - (position + 1)],
                    SaveMultipleUsers.getSchoolName(baseContext)[SaveMultipleUsers.getSchoolName(baseContext).size - (position + 1)])
            val intent = Intent(baseContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            baseContext.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.silenz.schema.R.layout.activity_switch)
        val toolbar = findViewById(com.silenz.schema.R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val prefs = baseContext.getSharedPreferences(
                "UserData", Context.MODE_PRIVATE)
        if (prefs.contains("schoolName") == false) {
            searchUI.visibility = VISIBLE
            chooseLayout.visibility = View.GONE
        } else {
            continueWithCurrentButton.text = "Fortsätt med ${SaveMultipleUsers.getLastSchoolName(baseContext).toString()}"
            populateRecentsListView()
        }

        createListeners()


    }

}
