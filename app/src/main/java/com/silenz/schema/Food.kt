package com.silenz.schema

import java.text.SimpleDateFormat
import java.util.*

/**
 * @param pageContent html from skolmaten.se
 * @param day index of day
 */
fun getDateOfDay(pageContent: String, day: Int): Date = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).parse(pageContent.split("<span class=\"weekday\">")[day].split("<span class=\"date\">")[1].split("</span>")[0])

/**
 * @param pageContent html from skolmaten.se
 */
fun getDaysOnPage(pageContent: String) = pageContent.split("<span class=\"weekday\">").size

/**
 * @param pageContent html from skolmaten.se
 * @param day index of day
 */
fun getNumberOfItemsOnDay(pageContent: String, day: Int) = pageContent.split("<span class=\"weekday\">")[day].split("<div class=\"items\">")[1].split("</div>")[0].split("<span>").size

/**
 * @param pageContent html from skolmaten.se
 * @param day index of day
 * @param item index of item in day
 */
fun getItemInDay(pageContent: String, day: Int, item: Int) = pageContent.split("<span class=\"weekday\">")[day].split("<span>")[item].split("</span>")[0] + "\n"

/**
 * @param pageContent html from skolmaten.se
 * @param day index of day
 */
fun getDaynameOfDay(day: Int, pageContent: String) = pageContent.split("<span class=\"weekday\">")[day].split("</span>")[0]

fun getWeekReason(pageContent: String) = pageContent.split("<span class=\"week-reason reason\">")[1].split("</")[0]