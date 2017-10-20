package com.example.nkoroi.kotlinexpandablerecyclerview.listeners


/**
 * Created by nkoroi on 16/10/17.
 */

interface ExpandCollapseListener {

    /**
     * Called when a group is expanded
     *
     * @param positionStart the flat position of the first child in the [ExpandableGroup]
     * @param itemCount the total number of children in the [ExpandableGroup]
     */
    fun onGroupExpanded(positionStart: Int, itemCount: Int)

    /**
     * Called when a group is collapsed
     *
     * @param positionStart the flat position of the first child in the [ExpandableGroup]
     * @param itemCount the total number of children in the [ExpandableGroup]
     */
    fun onGroupCollapsed(positionStart: Int, itemCount: Int)
}