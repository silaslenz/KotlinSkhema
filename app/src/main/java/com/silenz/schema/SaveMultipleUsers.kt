package com.silenz.schema

import android.content.Context
import android.content.SharedPreferences

/**
 * Saves history of userdata, split by '|'
 */

object SaveMultipleUsers {

    fun addUser(activity: Context, userNameItem: String, userIdItem: String, schoolIdItem: String, schoolCodeItem: String, schoolNameItem: String) {
        //TODO: Move up (don't readd) items that already exist.
        val sharedPreferences = activity.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        var identifier = "userName"
        addItemInSettings(editor, identifier, sharedPreferences, userNameItem)

        identifier = "userID"
        addItemInSettings(editor, identifier, sharedPreferences, userIdItem)


        identifier = "schoolID"
        addItemInSettings(editor, identifier, sharedPreferences, schoolIdItem)

        identifier = "schoolCode"
        addItemInSettings(editor, identifier, sharedPreferences, schoolCodeItem)

        identifier = "schoolName"
        addItemInSettings(editor, identifier, sharedPreferences, schoolNameItem)


        editor.commit()
    }

    private fun addItemInSettings(editor: SharedPreferences.Editor, identifier: String, sharedPreferences: SharedPreferences, userNameItem: String) {
        var list: String? = sharedPreferences.getString(identifier, null)
        // Append new Favorite item
        list = addItemToList(userNameItem, list)
        // Save in Shared Preferences
        editor.putString(identifier, list)
    }

    private fun addItemToList(item: String, list: String?): String? {
        var userNameList1 = list
        if (userNameList1 != null) {
            userNameList1 = userNameList1 + "'|'" + item
        } else {
            userNameList1 = item
        }
        return userNameList1
    }

    fun getList(activity: Context, identifier: String): List<String> {
        val list = getStringFromPreferences(activity, identifier)
        return convertStringToArray(list)
    }


    fun getLastUserName(activity: Context): String {
        val list = getList(activity, "userName")
        return list[list.size - 1]
    }

    fun getLastUser(activity: Context): String {
        val list = getList(activity, "userID")
        return list[list.size - 1]
    }

    fun getLastSchoolId(activity: Context): String {
        val list = getList(activity, "schoolID")
        return list[list.size - 1]
    }

    fun getLastSchoolCode(activity: Context): String {
        val list = getList(activity, "schoolCode")
        return list[list.size - 1]
    }

    fun getLastSchoolName(activity: Context): String {
        val list = getList(activity, "schoolName")
        return list[list.size - 1]
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