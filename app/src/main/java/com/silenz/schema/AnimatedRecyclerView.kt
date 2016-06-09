package com.silenz.schema

/**
 * Source: http://stackoverflow.com/questions/32239881/cardview-within-recyclerview-animation-while-populating
 */

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

/**
 * @author Leo on 2015/09/03
 */
class AnimatedRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {
    private var mScrollable: Boolean = false

    init {
        mScrollable = false
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return !mScrollable || super.dispatchTouchEvent(ev)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        for (i in 0..childCount - 1) {
            animate(getChildAt(i), i)

            if (i == childCount - 1) {
                handler.postDelayed({ mScrollable = true }, (i * 100).toLong())
            }
        }
    }

    private fun animate(view: View, pos: Int) {
        view.animate().cancel()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        view.translationX = (-display.width).toFloat()
        view.alpha = 0.5f
        view.animate().alpha(1.0f).translationX(0f).setDuration(300).startDelay = (pos * 50).toLong()
    }
}