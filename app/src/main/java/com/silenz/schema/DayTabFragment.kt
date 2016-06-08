package com.silenz.schema

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tab_fragment.*
import kotlinx.android.synthetic.main.tab_fragment.view.*

class DayTabFragment() : Fragment() {
    var globalTabnum: String = "0"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.tab_fragment, container, false)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay

        Picasso.with(context).load(Schema(SaveMultipleUsers.getLastSchoolId(context), SaveMultipleUsers.getLastUser(context), globalTabnum).getUrlThisWeek(context)).into(view.schemaImageView);

        return view
    }

    fun setText(tabnum:String){
        globalTabnum = tabnum
    }
}