package com.silenz.schema

import java.text.SimpleDateFormat
import java.util.*

/**
 * @param pageContent html from skolmaten.se
 * @param day index of day
 */
fun getDateOfDay(pageContent: String, day: Int) = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).parse(pageContent.split("<td class=\"date\">")[day].split("<span class=\"date\">")[1].split("</span>")[0])

/**
 * @param pageContent html from skolmaten.se
 */
fun getDaysOnPage(pageContent: String) = pageContent.split("<td class=\"date\">").size

/**
 * @param pageContent html from skolmaten.se
 * @param day index of day
 */
fun getNumberOfItemsOnDay(pageContent: String, day: Int) = pageContent.split("<td class=\"date\">")[day].split("<p class=\"item\">").size

/**
 * @param pageContent html from skolmaten.se
 * @param day index of day
 * @param item index of item in day
 */
fun getItemInDay(pageContent: String, day: Int, item: Int) = pageContent.split("<td class=\"date\">")[day].split("<p class=\"item\">")[item].split("</p>")[0] + "\n"

/**
 * @param pageContent html from skolmaten.se
 * @param day index of day
 */
fun getDaynameOfDay(day: Int, pageContent: String) = pageContent.split("<td class=\"date\">")[day].split("<span class=\"weekday\">")[1].split("</span>")[0]

fun getWeekReason(pageContent: String) = pageContent.split("<span class=\"weekReason\">")[1].split("</")[0]