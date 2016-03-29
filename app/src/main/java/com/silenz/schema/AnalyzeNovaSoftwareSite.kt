package com.silenz.schema

/**
 * Created by silenz on 3/29/16.
 */
fun hasDropdownselector(sitedata: String): Boolean {
    return sitedata.split("<select name=\"ctl").size > 1
}

fun getUrlCode(sitedata: String): String {
    return sitedata.split("WebViewer/")?.get(1)?.split("/printer")[0]
}

fun getNumberOfDropdownItems(sitedata: String, type: String): Int {
    return sitedata.split("name=\"" + type + "\"")[1].split("</select")[0].split("value=\"").size
}