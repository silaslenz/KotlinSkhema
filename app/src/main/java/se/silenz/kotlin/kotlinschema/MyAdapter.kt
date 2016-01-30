package se.silenz.kotlin.kotlinschema

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by silenz on 1/30/16.
 */
class MyAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private val mDataset: Array<String>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context).inflate(R.layout.my_text_view, parent, false)
        // set the view's size, margins, paddings and layout parameters

        val vh = ViewHolder(v as CardView)
        return vh
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.text = mDataset[position]

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataset.size
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder(v: CardView) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var mTextView: TextView

        init {
            mTextView = v.getChildAt(0) as TextView
        }
    }

}
