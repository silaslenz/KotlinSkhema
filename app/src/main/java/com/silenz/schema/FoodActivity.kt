package com.silenz.schema

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.result.Result
import kotlinx.android.synthetic.main.content_food.*

class FoodActivity : AppCompatActivity() {
    private var mAdapter: FoodAdapter? = null
    private fun changeDataInList(url: String) {
        Fuel.get(url).responseString { request, response, result ->
            run {
                when (result) {
                    is Result.Success -> {
                        val (pageContent, error) = result
                        pageContent?.let {
                            mAdapter?.clear()
                            for (day in 1 until getDaysOnPage(pageContent)) {
                                var dayStr = ""
                                for (item in 1 until getNumberOfItemsOnDay(pageContent, day)) {
                                    dayStr += getItemInDay(pageContent, day, item)
                                }
                                try {
                                    val date = getDateOfDay(pageContent, day)
                                    mAdapter?.add(getDaynameOfDay(day, pageContent), dayStr, date)
                                    Log.i("Food", "Loaded one day")
                                }
                                catch (e: Exception){
                                    Log.w("Food",e)
                                }


                            }
                            if (mAdapter?.titleDataset?.size == 0) {
                                if (pageContent.contains("week-reason")) {
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

        food_recycler_view.setHasFixedSize(true)

        val mLayoutManager = LinearLayoutManager(this)

        food_recycler_view.layoutManager = mLayoutManager

        val myDataset = arrayOf("")

        mAdapter = FoodAdapter(myDataset)


        food_recycler_view.adapter = mAdapter

        mAdapter?.clear()
        Fuel.get("https://api.skhema.silenz.se/food?query=" + SaveMultipleUsers.getLastSchoolId(baseContext)).responseJson {
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
                            changeDataInList(pageContent.getString("food")!!)
                        } else {
                            mAdapter?.clear()
                            mAdapter?.add(getString(R.string.no_food_yet))
                        }
                    }
                }

            }
        }
    }

}
