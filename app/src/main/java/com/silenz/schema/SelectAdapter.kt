package com.silenz.schema

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kittinunf.fuel.Fuel
import org.apache.commons.lang3.StringEscapeUtils
import org.jetbrains.anko.editText

data class NovaType(val name: String, val id: String)


class SelectAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val baseContext: Context, private val intent: Intent) : RecyclerView.Adapter<SelectAdapter.ViewHolder>(), View.OnClickListener {
    override fun onClick(v: View?) {
        val itemtag = (((v as ViewGroup).getChildAt(0)as ViewGroup).getChildAt(0)as TextView).tag.toString()
        val name = ((v.getChildAt(0)as ViewGroup).getChildAt(0)as TextView).text.toString()
        if (itemtag.contains("{")) { //All ids are on the form {str}
            Log.i("SelectAdapter",  intent.getStringExtra("schoolID"))
            Log.i("SelectAdapter",  intent.getStringExtra("schoolCode"))
            Log.i("SelectAdapter",  intent.getStringExtra("schoolName"))
            Log.i("SelectAdapter",  intent.getStringExtra("hasWeek"))
            SaveMultipleUsers.addUser(baseContext, name, itemtag, intent.getStringExtra("schoolID"), intent.getStringExtra("schoolCode"), intent.getStringExtra("schoolName"), intent.getStringExtra("hasWeek"))
            val intent = Intent(baseContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            baseContext.startActivity(intent)
        } else {
            Log.i("SelectAdapter", "Item tag is " + itemtag)
            if (itemtag == "custom_id") {
                Log.i("SelectAdapter", "Inputting a custom id")

                val builder = AlertDialog.Builder(baseContext)
                builder.setTitle(R.string.manual_input_title)
                builder.setMessage(R.string.manual_input_description)
                val text = TextInputLayout(baseContext)
                text.setPadding(10, 10, 10, 10)
                text.hint = baseContext.resources.getString(R.string.manual_input_hint)
                text.editText()

                builder.setView(text)

                builder.setPositiveButton("OK", { dialogInterface, i ->
                    println(text.editText?.text?.toString())
                    if (text.editText != null) {
                        SaveMultipleUsers.addUser(baseContext, text.editText?.text?.toString()!!, text.editText?.text?.toString()!!, intent.getStringExtra("schoolID"), intent.getStringExtra("schoolCode"), intent.getStringExtra("schoolName"), intent.getStringExtra("hasWeek"))
                        val intent = Intent(baseContext, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        baseContext.startActivity(intent)
                    } else {
                        Log.e("SelectAdapter", "Edittext was null")
                    }
                })

                builder.setNegativeButton("Cancel", { dialogInterface, i -> })
                builder.show()

            } else {
                Log.i("SelectAdapter", "Loading IDs for current type")
                getNovaIDs(itemtag)
            }

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
        for ((name, id) in titleDatasetClone) {
            if (name.toLowerCase().contains(query.toLowerCase()))
                addWithoutClean(name, id)
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


    class ViewHolder(v: CardView) : RecyclerView.ViewHolder(v) {
        var titleTextView: TextView

        init {
            titleTextView = v.findViewById(R.id.titleTextView) as TextView
        }
    }


    fun
            getNovaIDs(type: String) {
        if (type == "custom_id") {

        }
        var urlcode = ""
        clearAll()
        println("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"))
        if (intent.getStringExtra("schoolID") != "0") {

            Fuel.post("http://www.novasoftware.se/webviewer/(S(ol3bnszsognoda45gmbo5hba))/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"),
                    listOf()).responseString { request, response, result ->
                run {
                    if (response.httpStatusCode == 200) {
                        val (d, e) = result
                        if (d != null) {
                            if (hasDropdownselector(d)) {
                                //Has multiple lists of different types
                                for (i in 2..getNumberOfDropdownItems(d, type) - 1) {
                                    val nicedata = extractDropdown(d, type, 1)
                                    val name = getName(i, nicedata)
                                    val id = getId(i, nicedata)
                                    add(name, id)
                                }
                            } else {
                                //Has type->list selector type
                                urlcode = getUrlCode(d)

                                // Send a request for a new page, with our selected "type". This page contains a new dropdown.
                                Fuel.post("http://www.novasoftware.se/webviewer/$urlcode/MZDesign1.aspx?schoolid=" + intent.getStringExtra("schoolID") + "&code=" + intent.getStringExtra("schoolCode"),
                                        listOf("__EVENTTARGET" to "TypeDropDownList", "__EVENTARGUMENT" to "", "__LASTFOCUS" to "", "__VIEWSTATE" to "", "TypeDropDownList" to type, "ScheduleIDDropDownList" to "0", "FreeTextBox" to "", "PeriodDropDownList" to "8", "WeekDropDownList" to "52", "__VIEWSTATE" to "")).responseString { request, response, result ->

                                    run {
                                        val (d, e) = result
                                        val nicedata = d?.split("ScheduleIDDropDownList")?.get(2)?.split("</select>")?.get(0)?.split("<option value=\"")
                                        for (i in 1..nicedata!!.size - 1) {
                                            val name = StringEscapeUtils.unescapeHtml4(nicedata[i].split(">")[1].split("<")[0])
                                            val id = nicedata[i].split("\"")[0]
                                            add(name, id)
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
