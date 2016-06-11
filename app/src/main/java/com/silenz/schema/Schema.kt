package com.silenz.schema

import android.content.Context
import android.view.WindowManager
import kotlinx.android.synthetic.main.tab_fragment.view.*
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTime
import java.util.*

class Schema(var schoolID: String, var userID: String, val day: String = "0") {

    // Get the schedule url for the specified day.
    fun getUrlThisDay(context: Context): String {
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val week = DateTime.now().weekOfWeekyear

        val scale = context.getResources().getDisplayMetrics().density;
        println()
        println(scale)
        assert(day!="0")
        return "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=" + schoolID + "/sv-se&type=1&id=" + userID + "&period=&week=" + week + "&mode=0&printer=0&colors=32&head=0&clock=0&foot=0&day=" + day + "&width=" + (display.width/scale).toInt().toString() + "&height=" + ((display.height/scale)*0.7).toInt().toString()
    }

    // Get url for the specified week
    fun getUrlThisWeek(context: Context): String {
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val week = DateTime.now().weekOfWeekyear
        val scale = context.getResources().getDisplayMetrics().density/1.3;

        return "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=" + schoolID + "/sv-se&type=1&id=" + userID + "&period=&week=" + week + "&mode=0&printer=0&colors=32&head=0&clock=0&foot=0&day=0&width=" + (display.width/scale).toInt().toString() + "&height=" + ((display.height/scale)*0.8).toInt().toString()
    }
}