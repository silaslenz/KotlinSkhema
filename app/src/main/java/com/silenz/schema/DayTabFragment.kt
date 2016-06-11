package com.silenz.schema

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.alexvasilkov.gestures.Settings
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tab_fragment.*
import kotlinx.android.synthetic.main.tab_fragment.view.*

class DayTabFragment() : Fragment() {
    var globalTabDay: String = "0"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.tab_fragment, container, false)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay

        Picasso.with(context).load(Schema(SaveMultipleUsers.getLastSchoolId(context), SaveMultipleUsers.getLastUser(context), globalTabDay).getUrlThisDay(context)).into(view.daySchemaImageView);

        // Image sticks to top of display
        view.daySchemaImageView.controller.settings.gravity = Gravity.TOP
        view.daySchemaImageView.controller.settings.maxZoom = 5f

        return view
    }

    fun setDay(tabDay:String){
        globalTabDay = tabDay
    }
}