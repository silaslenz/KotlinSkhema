package com.silenz.schema

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.content_switch.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.listeners.onClick
import org.jetbrains.anko.sdk25.listeners.onItemClick
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SwitchActivity : AppCompatActivity() {

    private fun createListeners() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                    Unit

            override fun afterTextChanged(s: Editable?) = if (s.toString() != "")
                getSearchResults(s.toString())
            else
                changeListView(JSONObject())
        })

        continueCardView.setOnClickListener {
            startActivity(intentFor<SelectActivity>(
                    "schoolID" to SaveMultipleUsers.getLastSchoolId(baseContext),
                    "hasWeek" to SaveMultipleUsers.getLastHasWeek(baseContext).toString(),
                    "schoolCode" to SaveMultipleUsers.getLastSchoolCode(baseContext),
                    "schoolName" to SaveMultipleUsers.getLastSchoolName(baseContext)))
        }

        searchCardView.onClick {
            searchUI.visibility = VISIBLE
            chooseLayout.visibility = View.GONE
        }
    }

    fun getSearchResults(query: String) {
        Fuel.get("http://api.skhema.silenz.se/get?query=" + query).responseJson { request, response, result ->

            run {
                when (result) {
                    is Result.Failure -> {
                        val (d, e) = result
                    }
                    is Result.Success -> {
                        val (d, e) = result

                        println("data is" + d.toString())

                        try {
                            changeListView(d?.obj())
                        } catch (error: JSONException) {
                            Log.w("Switch", "Received an InvocationTargetException, probably empty result")
                            changeListView(JSONObject())
                        }
                    }
                }
                val (d, e) = result

            }
        }
    }

    private fun translateBack(query: String) {
        Log.i("SwitchActivity", "Translating $query back")
        Fuel.get("http://api.skhema.silenz.se/get?name=" + query).responseJson { request, response, result ->

            run {
                when (result) {
                    is Result.Failure -> {
                        Log.e("Switch", "Response failure for " + "https://api.skhema.silenz.se/get?name=" + query)
                        //TODO: Error handling
                    }
                    is Result.Success -> {
                        val (responseTmp, e) = result
                        val response = responseTmp?.obj()
                        val intent = Intent(baseContext, SelectActivity::class.java)
                        Log.i("Switch", "Setting hasweek")
                        intent.putExtra("schoolID", response?.get("schoolid").toString())
                        intent.putExtra("schoolCode", response?.get("code").toString())
                        println(response?.get("hasweek").toString())
                        intent.putExtra("hasWeek", response?.get("hasweek").toString())
                        intent.putExtra("schoolName", response?.get("name").toString() + " (" + response?.get("location").toString() + ")")

                        startActivity(intent)
                    }
                }
            }
        }
    }

    fun changeListView(searchResults: JSONObject?) {
        val arrayList = ArrayList<String>()

        //Add all items to array
        if (searchResults != null && searchResults.length() != 0) {
            (0 until searchResults.names().length()).forEach {
                item ->
                arrayList.add("${searchResults.getJSONObject(searchResults.names().getString(item)).getString("name")} (${searchResults.getJSONObject(searchResults.names().getString(item)).getString("location")})")
            }
        } else {
            arrayList.add("Nothing found")
        }


        schoolSearchListView.adapter = ArrayAdapter<String>(baseContext, R.layout.simple_list_item_1, arrayList)
        schoolSearchListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            translateBack(schoolSearchListView.getItemAtPosition(position) as String)
            println(schoolSearchListView.getItemAtPosition(position))
        }

    }

    private fun populateRecentsListView() {
        //val historicalUsersList = SaveMultipleUsers.getList(baseContext, "userName").reversed() as ArrayList<String>
        var historicalUsersList = emptyArray<String>()
        Log.d("Switch", SaveMultipleUsers.getList(baseContext, "userName").toString())
        for (i in 0 until (SaveMultipleUsers.getList(baseContext, "userName").reversed()).size) {
            historicalUsersList = historicalUsersList.plus((SaveMultipleUsers.getList(baseContext, "userName").reversed())[i] + " (" + (SaveMultipleUsers.getList(baseContext, "schoolName").reversed())[i] + ")")

        }
        println(historicalUsersList)
        recentsListView.adapter = ArrayAdapter<String>(baseContext, R.layout.simple_list_item_1, historicalUsersList)
        recentsListView.onItemClick { adapterView, view, position, id ->
            SaveMultipleUsers.addUser(baseContext,
                    SaveMultipleUsers.getList(baseContext, "userName").reversed()[position],
                    SaveMultipleUsers.getList(baseContext, "userID")[SaveMultipleUsers.getList(baseContext, "userID").size - (position + 1)],
                    SaveMultipleUsers.getList(baseContext, "schoolID")[SaveMultipleUsers.getList(baseContext, "schoolID").size - (position + 1)],
                    SaveMultipleUsers.getList(baseContext, "schoolCode")[SaveMultipleUsers.getList(baseContext, "schoolCode").size - (position + 1)],
                    SaveMultipleUsers.getList(baseContext, "schoolName")[SaveMultipleUsers.getList(baseContext, "schoolName").size - (position + 1)],
                    SaveMultipleUsers.getList(baseContext, "hasWeek")[SaveMultipleUsers.getList(baseContext, "hasWeek").size - (position + 1)])
            val intent = Intent(baseContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            baseContext.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.silenz.schema.R.layout.activity_switch)
        val toolbar = findViewById<Toolbar>(com.silenz.schema.R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val prefs = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)

        if (!prefs.contains("schoolName")) {
            searchUI.visibility = VISIBLE
            chooseLayout.visibility = View.GONE
        } else {
            continueWithCurrentButton.text = resources.getString(com.silenz.schema.R.string.continue_with, SaveMultipleUsers.getLastSchoolName(baseContext))
            populateRecentsListView()
        }

        createListeners()


    }

}
