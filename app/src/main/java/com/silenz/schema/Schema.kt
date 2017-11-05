package com.silenz.schema

import android.content.Context
import android.util.Log
import android.view.WindowManager
import org.joda.time.DateTime
import java.util.*

class Schema(private var schoolID: String, private var userID: String, private val day: String = "0", val date: DateTime) {

    // Get the schedule url for the specified day.
    fun getUrlThisDay(context: Context): String {
        Log.i("Schema", "LOADING DAY: " + day)


        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        var week = ""
        week = if (SaveMultipleUsers.getLastHasWeek(context)) {
            date.weekOfWeekyear.toString()
        } else {
            ""
        }


        val scale = context.resources.displayMetrics.density

        assert(day!="0")
        if (day == "0")
            throw InputMismatchException("Pls... This would load the full week. Make sure you set a day before calling this function.")
        val url = "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=" + schoolID + "/sv-se&type=1&id=" + userID + "&period=&week=" + week + "&mode=0&printer=0&colors=32&head=0&clock=0&foot=0&day=" + day + "&width=" + (display.width / scale).toInt().toString() + "&height=" + ((display.height / scale) * 0.7).toInt().toString()
        Log.i("Schema", "URL: " + url)
        return url
    }

    // Get url for the specified week
    fun getUrlThisWeek(context: Context): String {
        println("LOADING WEEK")
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        var week = ""
        week = if (SaveMultipleUsers.getLastHasWeek(context)) {
            date.weekOfWeekyear.toString()
        } else {
            ""
        }
        val scale = context.resources.displayMetrics.density / 1.3

        return "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=" + schoolID + "/sv-se&type=1&id=" + userID + "&period=&week=" + week + "&mode=0&printer=0&colors=32&head=0&clock=0&foot=0&day=0&width=" + (display.width/scale).toInt().toString() + "&height=" + ((display.height/scale)*0.8).toInt().toString()
    }
}