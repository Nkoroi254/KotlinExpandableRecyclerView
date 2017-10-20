package com.nkoroi254.kotlinexpandablerecyclerview.viewholders


import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.nkoroi.kotlinexpandablerecyclerview.listeners.OnGroupClickListener
import com.nkoroi254.kotlinexpandablerecyclerview.models.ExpandableGroup



/**
 * Created by nkoroi on 16/10/17.
 */

/**
 * ViewHolder for the [ExpandableGroup.title] in a [ExpandableGroup]
 *
 * The current implementation does now allow for sub [View] of the parent view to trigger
 * a collapse / expand. *Only* click events on the parent [View] will trigger a collapse or
 * expand
 */
abstract class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var listener: OnGroupClickListener? = null

    init {
        itemView.setOnClickListener(this)
    }

    override
    fun onClick(v: View) {
        if (listener != null) {
            listener!!.onGroupClick(adapterPosition)
        }
    }

    fun setOnGroupClickListener(listener: OnGroupClickListener) {
        this.listener = listener
    }

    open fun expand() {}

    open fun collapse() {}
}