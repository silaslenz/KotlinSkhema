package com.silenz.schema

import android.app.Activity
import android.content.Context

/**
 * Created by silenz on 3/29/16.
 */

object SaveMultipleUsers {

    fun addUser(activity: Context, userNameItem: String, userIdItem: String, schoolIdItem: String, schoolCodeItem: String, schoolNameItem: String) {
        //TODO: Move up (don't readd) items that already exist.
        val sharedPreferences = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        var userNameList: String? = sharedPreferences.getString("userName", null)
        // Append new Favorite item
        if (userNameList != null) {
            userNameList = userNameList + "'|'" + userNameItem
        } else {
            userNameList = userNameItem
        }
        // Save in Shared Preferences
        editor.putString("userName", userNameList)

        var userIdList: String? = sharedPreferences.getString("userID", null)
        // Append new Favorite item
        if (userIdList != null) {
            userIdList = userIdList + "'|'" + userIdItem
        } else {
            userIdList = userIdItem
        }
        // Save in Shared Preferences
        editor.putString("userID", userIdList)


        var schoolIdList: String? = sharedPreferences.getString("schoolID", null)
        // Append new Favorite item
        if (schoolIdList != null) {
            schoolIdList = schoolIdList + "'|'" + schoolIdItem
        } else {
            schoolIdList = schoolIdItem
        }
        // Save in Shared Preferences
        editor.putString("schoolID", schoolIdList)

        var schoolCodeList: String? = sharedPreferences.getString("schoolCode", null)
        // Append new Favorite item
        if (schoolCodeList != null) {
            schoolCodeList = schoolCodeList + "'|'" + schoolCodeItem
        } else {
            schoolCodeList = schoolCodeItem
        }
        // Save in Shared Preferences
        editor.putString("schoolCode", schoolCodeList)

        var schoolNameList: String? = sharedPreferences.getString("schoolName", null)
        // Append new Favorite item
        if (schoolNameList != null) {
            schoolNameList = schoolNameList + "'|'" + schoolNameItem
        } else {
            schoolNameList = schoolNameItem
        }
        // Save in Shared Preferences
        editor.putString("schoolName", schoolNameList)
        editor.commit()
    }

    fun getUserName(activity: Context): List<String> {
        val list = getStringFromPreferences(activity, "userName")
        return convertStringToArray(list)
    }

    fun getUser(activity: Context): List<String> {
        val list = getStringFromPreferences(activity, "userID")
        return convertStringToArray(list)
    }

    fun getSchoolId(activity: Context): List<String> {
        val list = getStringFromPreferences(activity, "schoolID")
        return convertStringToArray(list)
    }

    fun getSchoolCode(activity: Context): List<String> {
        val list = getStringFromPreferences(activity, "schoolCode")
        return convertStringToArray(list)
    }

    fun getSchoolName(activity: Context): List<String> {
        val list = getStringFromPreferences(activity, "schoolName")
        return convertStringToArray(list)
    }

    fun getLastUserName(activity: Context): String {
        val list = getUserName(activity)
        return list[list.size - 1]
    }

    fun getLastUser(activity: Context): String {
        val list = getUser(activity)
        return list[list.size - 1]
    }

    fun getLastSchoolId(activity: Context): String {
        val list = getSchoolId(activity)
        return list[list.size - 1]
    }

    fun getLastSchoolCode(activity: Context): String {
        val list = getSchoolCode(activity)
        return list[list.size - 1]
    }

    fun getLastSchoolName(activity: Context): String {
        val list = getSchoolName(activity)
        return list[list.size - 1]
    }

    private fun putStringInPreferences(context: Activity, nick: String, key: String): Boolean {
        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, nick)
        editor.commit()
        return true
    }

    private fun getStringFromPreferences(context: Context, key: String): String {
        val defaultValue = "blää "
        val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        var temp = "test"
        if (sharedPreferences.contains(key)) {
            temp = sharedPreferences.getString(key, defaultValue)
        }
        return temp
    }

    private fun convertStringToArray(str: String): List<String> {
        val arr = str.split("'|'")
        return arr
    }
}