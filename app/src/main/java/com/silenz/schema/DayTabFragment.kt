package com.silenz.schema

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import com.alexvasilkov.gestures.GestureController
import com.alexvasilkov.gestures.State
import com.alexvasilkov.gestures.views.GestureImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
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
        view?.loadingPanel?.visibility = View.VISIBLE
        Glide
                .with(context)
                .load(url)
                .skipMemoryCache(true)
                .listener(
                        object : RequestListener<String, GlideDrawable> {
                            override fun onException(e: Exception, model: String, target: Target<GlideDrawable>,
                                                     isFirstResource: Boolean): Boolean {
                                view?.noNetWorkError?.visibility = View.VISIBLE
                                view?.loadingPanel?.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                                view?.loadingPanel?.visibility = View.GONE
                                return false
                            }
                        })
                .into(this)
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

        // Only enable pull to refresh on top
        view?.daySchemaImageView?.controller?.addOnStateChangeListener(object : GestureController.OnStateChangeListener {
            var lastzoom: Float = 0f
            override fun onStateChanged(state: State) {
                // Enable swipe to refresh at the top of the image. (Note that state.y is negativ when the image is scrolled down)
                try {
                    if (state.y == 0f) {
                        activity.swiperefresh.isEnabled = true;
                    } else {
                        activity.swiperefresh.isEnabled = false;
                    }
                } catch (e: NullPointerException) {
                    Log.w("DayTabFragment", "swiperefresh not yet instantiated")
                }

                lastzoom = state.zoom

            }

            override fun onStateReset(oldState: State?, newState: State?) {

            }

        })
    }

}