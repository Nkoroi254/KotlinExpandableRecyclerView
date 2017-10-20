package com.example.nkoroi.kotlinexpandablerecyclerview.listeners

/**
 * Created by nkoroi on 16/10/17.
 */

interface OnGroupClickListener {

    /**
     * @param flatPos the flat position (raw index within the list of visible items in the
     * RecyclerView of a GroupViewHolder)
     * @return false if click expanded group, true if click collapsed group
     */
    fun onGroupClick(flatPos: Int): Boolean
}