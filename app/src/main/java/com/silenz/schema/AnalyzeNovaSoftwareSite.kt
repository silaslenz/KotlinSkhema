package com.silenz.schema

import org.apache.commons.lang3.StringEscapeUtils

/**
 * Helper functions to get data from novasoftware pages.
 */
fun hasDropdownselector(pageContent: String): Boolean =
        pageContent.split("<select name=\"ctl").size > 1

fun getUrlCode(pageContent: String): String =
        pageContent.split("WebViewer/")[1].split("/printer")[0]

fun getNumberOfDropdownItems(pageContent: String, type: String): Int =
        pageContent.split("name=\"" + type + "\"")[1].split("</select")[0].split("value=\"").size

fun extractDropdown(d: String, type: String, place: Int) = d.split("name=\"" + type + "\"")[place].split("</select")[0] //Place is 1-indexed

fun getName(place: Int, nicedata: String): String = StringEscapeUtils.unescapeHtml4(nicedata.split(">")[place * 2].split("<")[0])


fun getId(place: Int, nicedata: String) = nicedata.split("value=\"")[place].split("\"")[0]

