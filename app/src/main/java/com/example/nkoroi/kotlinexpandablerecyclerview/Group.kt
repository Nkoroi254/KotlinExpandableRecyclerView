package com.example.nkoroi.kotlinexpandablerecyclerview

import com.nkoroi254.kotlinexpandablerecyclerview.models.ExpandableGroup

/**
 * Created by nkoroi on 16/10/17.
 */



class Group(parent : Any, items: List<Artist>, val iconResId: Int) : ExpandableGroup<Artist>(parent, items)  {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Group) return false

        val genre = o as Group?

        return iconResId == genre!!.iconResId

    }

    override fun hashCode(): Int {
        return iconResId
    }
}
