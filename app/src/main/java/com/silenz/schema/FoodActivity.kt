package com.silenz.schema

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.content_food.*

class FoodActivity : AppCompatActivity() {
    var mAdapter: FoodAdapter? = null
    fun changeDataInList(url: String) {
        Fuel.get(url).responseString { request, response, result ->
            run {
                when (result) {
                    is Result.Success -> {
                        val (pageContent, error) = result
                        pageContent?.let {
                            mAdapter?.clear()
                            for (day in 1..getDaysOnPage(pageContent) - 1) {
                                var dayStr = ""
                                for (item in 1..getNumberOfItemsOnDay(pageContent, day) - 1) {
                                    dayStr += getItemInDay(pageContent, day, item)
                                }
                                val date = getDateOfDay(pageContent, day)
                                mAdapter?.add(getDaynameOfDay(day, pageContent), dayStr, date)
                                Log.i("Food", "Loaded one day")
                            }
                            if (mAdapter?.titleDataset?.size == 0) {
                                if (pageContent.contains("weekReason")) {
                                    mAdapter?.add(getWeekReason(pageContent))
                                } else {
                                    mAdapter?.add("Could not load")
                                }
                            }
                            if (mAdapter?.titleDataset?.size == 0) {

                            }

                        }
                    }
                    is Result.Failure -> {
                        //TODO: Error handeling
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)
        setSupportActionBar(toolbar)



        food_recycler_view.setHasFixedSize(true)

        val mLayoutManager = LinearLayoutManager(this)

        food_recycler_view.layoutManager = mLayoutManager

        var myDataset = arrayOf("")

        mAdapter = FoodAdapter(myDataset)


        food_recycler_view.adapter = mAdapter

        mAdapter?.clear()
        Fuel.get("http://skhemaf-silenz.rhcloud.com/food?query=" + SaveMultipleUsers.getLastSchoolId(baseContext)).responseJson {
            request, response, result ->
            run {
                when (result) {
                    is Result.Failure -> {
                        mAdapter?.clear()
                        mAdapter?.add(getString(R.string.error_network))
                    }
                    is Result.Success -> {
                        val (resultdata, e) = result
                        val pageContent = resultdata?.obj()
                        if (pageContent?.getString("food") != "null" && pageContent?.getString("food") != null) {
                            changeDataInList(pageContent?.getString("food")!!)
                        } else {
                            mAdapter?.clear()
                            mAdapter?.add("Denna skola har ännu ingen matsedel kopplad till sig")
                        }
                    }
                }

            }
        }
    }

}
