package com.silenz.schema

import android.content.Context
import android.view.WindowManager
import java.util.*

class Schema(var schoolID: String, var userID: String) {
    fun getUrlThisWeek(context: Context): String {
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val week = GregorianCalendar().get(Calendar.WEEK_OF_YEAR)
        return "http://www.novasoftware.se/ImgGen/schedulegenerator.aspx?format=png&schoolid=" + schoolID + "/sv-se&type=1&id=" + userID + "&period=&week=" + week + "&mode=0&printer=0&colors=32&head=0&clock=0&foot=0&day=0&width=" + display.width + "&height=" + display.height / 2
    }

}