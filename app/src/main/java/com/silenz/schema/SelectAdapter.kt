package com.silenz.schema

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kittinunf.fuel.Fuel
import java.util.*

data class NovaType(val name: String, val id: String)


class SelectAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val baseContext: Context, private val intent: Intent) : RecyclerView.Adapter<SelectAdapter.ViewHolder>(), View.OnClickListener {
    override fun onClick(v: View?) {
        val itemtag = (((v as ViewGroup).getChildAt(0)as ViewGroup).getChildAt(0)as TextView).tag.toString()
        if (itemtag.contains("{")) {//All ids are on the form {str}
            var prefs = baseContext.getSharedPreferences(
                    "UserData", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString("userID", itemtag)
            editor.putString("schoolID", intent.getStringExtra("schoolID"))
            editor.putString("schoolCode", intent.getStringExtra("schoolCode"))
            editor.putString("schoolName", intent.getStringExtra("schoolName"))
            editor.commit()
            val intent = Intent(baseContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            baseContext.startActivity(intent)
        } else {
            println(itemtag)
            getNovaIDs(itemtag)
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context).inflate(R.layout.switch_item_view, parent, false)
        // set the view's size, margins, paddings and layout parameters
        v.setOnClickListener(this)
        val vh = ViewHolder(v as CardView)

        return vh
    }
    var titleDataset = arrayOf(NovaType("", ""))

    var titleDatasetClone = arrayOf(NovaType("", ""))
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        holder.titleTextView.text = titleDataset[position].name
        holder.titleTextView.setTypeface(null, Typeface.BOLD)
        holder.titleTextView.tag = titleDataset[position].id
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return titleDataset.size
    }

    fun clear() {
        titleDataset = arrayOf()
        notifyDataSetChanged()
    }

    fun clearAll() {
        titleDatasetClone = arrayOf()
        titleDataset = arrayOf()
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        clear()
        for (item in titleDatasetClone) {
            if (item.name.toLowerCase().contains(query.toLowerCase()))
                addWithoutClean(item.name, item.id)
        }
    }

    fun addWithoutClean(title: String, id: String) {
        titleDataset = titleDataset.plus(NovaType(title, id))
        notifyItemInserted(itemCount)
    }

    fun add(title: String, id: String) {
        if (titleDatasetClone.size > titleDataset.size)
            titleDataset = titleDatasetClone
        titleDataset = titleDataset.plus(NovaType(title, id))
        titleDatasetClone = titleDataset
        notifyItemInserted(itemCount)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder(v: CardView) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var titleTextView: TextView

        init {
            titleTextView = v.findViewById(R.id.titleTextView) as TextView
        }
    }


    fun getNovaIDs(type: String) {
        if (type=="custom_id"){

        }
        var urlcode = ""
        clearAll()
        println("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"))
        if ( intent.getStringExtra("schoolID") != "0") {
            Fuel.post("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"),
                    listOf()).responseString { request, response, result ->
                run {
                    if (response.httpStatusCode == 200) {
                        val (d, e) = result
                        println(d)
                        urlcode = d?.split("WebViewer/")?.get(1)?.split("/printer")!![0]
                        var prefs = baseContext.getSharedPreferences(
                                "UserData", Context.MODE_PRIVATE)
                        Fuel.post("http://www.novasoftware.se/webviewer/$urlcode/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"),
                                listOf("__EVENTTARGET" to "TypeDropDownList", "__EVENTARGUMENT" to "", "__LASTFOCUS" to "", "__VIEWSTATE" to "", "TypeDropDownList" to type, "ScheduleIDDropDownList" to "0", "FreeTextBox" to "", "PeriodDropDownList" to "8", "WeekDropDownList" to "52", "__VIEWSTATE" to "")).responseString { request, response, result ->

                            run {
                                val (d, e) = result
                                println(response.url)
                                val nicedata = d?.split("ScheduleIDDropDownList")?.get(2)?.split("</select>")?.get(0)?.split("<option value=\"");
                                val spinnerArray = ArrayList<String>()
                                spinnerArray.add("Välj id")
                                for (i in 1..nicedata!!.size - 1) {

                                    spinnerArray.add(nicedata[i].split(">")[1].split("<")[0])
                                    println(kotlin.Pair(nicedata[i].split(">")[1].split("<")[0], nicedata[i].split("\"")[0]))
                                    val name = nicedata[i].split(">")[1].split("<")[0]
                                    val id = nicedata[i].split("\"")[0]
                                    add(name, id)
                                }

                                println(spinnerArray);
                                //                                val adapter = ArrayAdapter<String>(
                                //                                        this, android.R.layout.simple_spinner_item, spinnerArray)

                                //                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                //                                select_spinner.adapter = adapter
                            }
                        }

                    } else {
                        println(response.httpStatusCode)
                    }


                }
            }
        }
    }

}
