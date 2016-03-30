package com.silenz.schema

/**
 * Helper functions to get data from novasoftware pages.
 */
fun hasDropdownselector(pageContent: String): Boolean {
    return pageContent.split("<select name=\"ctl").size > 1
}

fun getUrlCode(pageContent: String): String {
    return pageContent.split("WebViewer/")[1].split("/printer")[0]
}

fun getNumberOfDropdownItems(pageContent: String, type: String): Int {
    return pageContent.split("name=\"" + type + "\"")[1].split("</select")[0].split("value=\"").size
}