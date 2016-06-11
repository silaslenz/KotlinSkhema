package com.silenz.schema

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.tab_fragment.view.*
import java.util.*

class DayTabFragment(input: String) : Fragment() {
    constructor() : this("0") {

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
            Picasso.with(context).load(Schema(SaveMultipleUsers.getLastSchoolId(context), SaveMultipleUsers.getLastUser(context), input).getUrlThisDay(context)).into(view?.daySchemaImageView);
        } catch (e: InputMismatchException) {
            Log.w("Schedule loading", "Not yet loaded")
        }
        // Image sticks to top of display
        view?.daySchemaImageView?.controller?.settings?.gravity = Gravity.TOP
        view?.daySchemaImageView?.controller?.settings?.maxZoom = 5f


    }

}