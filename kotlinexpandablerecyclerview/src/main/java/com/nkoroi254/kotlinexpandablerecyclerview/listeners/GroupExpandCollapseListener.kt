package com.example.nkoroi.kotlinexpandablerecyclerview.listeners

import com.nkoroi254.kotlinexpandablerecyclerview.models.ExpandableGroup

/**
 * Created by nkoroi on 16/10/17.
 */
interface GroupExpandCollapseListener {
    /**
     * Called when a group is expanded
     * @param group the {@link ExpandleGroup} being expanded
     */
    fun onGroupExpanded(group : ExpandableGroup<Any>)

    /**
     * Called when a group is collapsed
     * @param group the {@link ExpandableGroup} being collapsed
     */
    fun onGroupCollapsed(group : ExpandableGroup<Any>)
}