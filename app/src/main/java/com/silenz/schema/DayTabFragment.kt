package com.silenz.schema

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import com.alexvasilkov.gestures.views.GestureImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.tab_fragment.view.*
import org.joda.time.DateTime
import java.util.*

class DayTabFragment(input: String, date: DateTime) : Fragment(), UpdateableFragment {
    override fun update(date: DateTime) {
        view?.daySchemaImageView?.loadUrl(Schema(SaveMultipleUsers.getLastSchoolId(context), SaveMultipleUsers.getLastUser(context), input, date).getUrlThisDay(context))
    }

    val date = date

    constructor() : this("0", DateTime.now()) {

    }

    fun GestureImageView.loadUrl(url: String) {
        Glide.with(context).load(url).into(this)
    }

    val input = input;
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.tab_fragment, container, false)
        retainInstance = true;

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay


        try {
            view?.daySchemaImageView?.loadUrl(Schema(SaveMultipleUsers.getLastSchoolId(context), SaveMultipleUsers.getLastUser(context), input, date).getUrlThisDay(context))
        } catch (e: InputMismatchException) {
            Log.w("Schedule loading", "Not yet loaded")
        }
        // Image sticks to top of display
        view?.daySchemaImageView?.controller?.settings?.gravity = Gravity.TOP
        view?.daySchemaImageView?.controller?.settings?.maxZoom = 5f


    }

}