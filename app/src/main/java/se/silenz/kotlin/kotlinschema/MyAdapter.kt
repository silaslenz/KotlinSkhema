package se.silenz.kotlin.kotlinschema

import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import se.silenz.kotlin.kotlinschema.R.layout.my_text_view
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by silenz on 1/30/16.
 */
class MyAdapter// Provide a suitable constructor (depends on the kind of dataset)
(private var mDataset: Array<String>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyAdapter.ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context).inflate(my_text_view, parent, false)
        // set the view's size, margins, paddings and layout parameters

        val vh = ViewHolder(v as CardView)

        return vh
    }

    var titleDataset = arrayOf("");
    var dateDataset = arrayOf("");
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        holder.mTextView.text = mDataset[position]
        holder.titleTextView.text = titleDataset[position]
        holder.titleTextView.setTypeface(null, Typeface.BOLD)
        holder.mTextView.setTextColor(Color.GRAY);
        holder.dateTextView.text = dateDataset[position]


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return mDataset.size
    }

    fun clear() {
        mDataset = arrayOf()
        titleDataset = arrayOf()
        dateDataset = arrayOf()
        notifyDataSetChanged()
    }

    fun add(title: String, content: String, date: Date?) {
        mDataset = mDataset.plus(content)
        titleDataset = titleDataset.plus(title)
        println(date)
        //        if (date?.equals(SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(Date())) as Boolean){
        dateDataset = dateDataset.plus(SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(date))
        notifyItemInserted(itemCount)
    }

    fun add(title: String) {
        mDataset = mDataset.plus("")
        titleDataset = titleDataset.plus(title)
        dateDataset = dateDataset.plus("")
        notifyItemInserted(itemCount)
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder(v: CardView) : RecyclerView.ViewHolder(v) {
        // each data item is just a string in this case
        var mTextView: TextView
        var titleTextView: TextView
        var dateTextView: TextView

        init {
            mTextView = v.findViewById(R.id.mTextView) as TextView
            titleTextView = v.findViewById(R.id.titleTextView) as TextView
            dateTextView = v.findViewById(R.id.dateTextView) as TextView
        }
    }

}
