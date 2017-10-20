package com.nkoroi254.kotlinexpandablerecyclerview

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.example.nkoroi.kotlinexpandablerecyclerview.listeners.ExpandCollapseListener
import com.example.nkoroi.kotlinexpandablerecyclerview.listeners.GroupExpandCollapseListener
import com.example.nkoroi.kotlinexpandablerecyclerview.listeners.OnGroupClickListener
import com.example.nkoroi.kotlinexpandablerecyclerview.models.ExpandableList
import com.nkoroi254.kotlinexpandablerecyclerview.models.ExpandableGroup
import com.nkoroi254.kotlinexpandablerecyclerview.models.ExpandableListPosition
import com.nkoroi254.kotlinexpandablerecyclerview.viewholders.ChildViewHolder
import com.nkoroi254.kotlinexpandablerecyclerview.viewholders.GroupViewHolder


/**
 * Created by nkoroi on 16/10/17.
 */

abstract class ExpandableRecyclerViewAdapter<GVH : GroupViewHolder, CVH : ChildViewHolder>(groups: List<ExpandableGroup<*>>) :
        RecyclerView.Adapter<ViewHolder>(), ExpandCollapseListener, OnGroupClickListener {
    protected var expandableList: ExpandableList
    private val expandCollapseController: ExpandCollapseController

    private var groupClickListener: OnGroupClickListener? = null
    private var expandCollapseListener: GroupExpandCollapseListener? = null

    /**
     * The full list of [ExpandableGroup] backing this RecyclerView
     *
     * @return the list of [ExpandableGroup] that this object was instantiated with
     */
    val groups: List<ExpandableGroup<*>>
        get() = expandableList.groups

    init {
        this.expandableList = ExpandableList(groups)
        this.expandCollapseController = ExpandCollapseController(expandableList, this)
    }

    /**
     * Implementation of Adapter.onCreateViewHolder(ViewGroup, int)
     * that determines if the list item is a group or a child and calls through
     * to the appropriate implementation of either [.onCreateGroupViewHolder]
     * or [.onCreateChildViewHolder]}.
     *
     * @param parent The [ViewGroup] into which the new [android.view.View]
     * will be added after it is bound to an adapter position.
     * @param viewType The view type of the new `android.view.View`.
     * @return Either a new [GroupViewHolder] or a new [ChildViewHolder]
     * that holds a `android.view.View` of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            ExpandableListPosition.GROUP -> {
                val gvh = onCreateGroupViewHolder(parent, viewType)
                gvh.setOnGroupClickListener(this)
                return gvh
            }
            ExpandableListPosition.CHILD -> {
                return onCreateChildViewHolder(parent, viewType)
            }
            else -> throw IllegalArgumentException("viewType is not valid")
        }
    }

    /**
     * Implementation of Adapter.onBindViewHolder(RecyclerView.ViewHolder, int)
     * that determines if the list item is a group or a child and calls through
     * to the appropriate implementation of either [.onBindGroupViewHolder]
     * or [.onBindChildViewHolder].
     *
     * @param holder Either the GroupViewHolder or the ChildViewHolder to bind data to
     * @param position The flat position (or index in the list of [ ][ExpandableList.getVisibleItemCount] in the list at which to bind
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listPos = expandableList.getUnflattenedPosition(position)
        val group = expandableList.getExpandableGroup(listPos)
        when (listPos.type) {
            ExpandableListPosition.GROUP -> {
                onBindGroupViewHolder(holder as GVH, position, group)

                if (isGroupExpanded(group)) {
                    holder.expand()
                } else {
                    holder.collapse()
                }
            }
            ExpandableListPosition.CHILD -> onBindChildViewHolder(holder as CVH, position, group, listPos.childPos)
        }
    }

    /**
     * @return the number of group and child objects currently expanded
     * @see ExpandableList.getVisibleItemCount
     */
    override fun getItemCount(): Int {
        return expandableList.visibleItemCount
    }

    /**
     * Gets the view type of the item at the given position.
     *
     * @param position The flat position in the list to get the view type of
     * @return {@value ExpandableListPosition#CHILD} or {@value ExpandableListPosition#GROUP}
     * @throws RuntimeException if the item at the given position in the list is not found
     */
    override fun getItemViewType(position: Int): Int {
        return expandableList.getUnflattenedPosition(position).type
    }

    /**
     * Called when a group is expanded
     *
     * @param positionStart the flat position of the first child in the [ExpandableGroup]
     * @param itemCount the total number of children in the [ExpandableGroup]
     */
    override fun onGroupExpanded(positionStart: Int, itemCount: Int) {
        //update header
        val headerPosition = positionStart - 1
        notifyItemChanged(headerPosition)

        // only insert if there items to insert
        if (itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount)
            if (expandCollapseListener != null) {
                val groupIndex = expandableList.getUnflattenedPosition(positionStart).groupPos
                expandCollapseListener!!.onGroupExpanded(groups[groupIndex] as ExpandableGroup<Any>)
            }
        }
    }

    /**
     * Called when a group is collapsed
     *
     * @param positionStart the flat position of the first child in the [ExpandableGroup]
     * @param itemCount the total number of children in the [ExpandableGroup]
     */
    override fun onGroupCollapsed(positionStart: Int, itemCount: Int) {
        //update header
        val headerPosition = positionStart - 1
        notifyItemChanged(headerPosition)

        // only remote if there items to remove
        if (itemCount > 0) {
            notifyItemRangeRemoved(positionStart, itemCount)
            if (expandCollapseListener != null) {
                //minus one to return the position of the header, not first child
                val groupIndex = expandableList.getUnflattenedPosition(positionStart - 1).groupPos
                expandCollapseListener!!.onGroupCollapsed(groups[groupIndex] as ExpandableGroup<Any>)
            }
        }
    }

    /**
     * Triggered by a click on a [GroupViewHolder]
     *
     * @param flatPos the flat position of the [GroupViewHolder] that was clicked
     * @return false if click expanded group, true if click collapsed group
     */
    override fun onGroupClick(flatPos: Int): Boolean {
        if (groupClickListener != null) {
            groupClickListener!!.onGroupClick(flatPos)
        }
        return expandCollapseController.toggleGroup(flatPos)
    }

    /**
     * @param flatPos The flat list position of the group
     * @return true if the group is expanded, *after* the toggle, false if the group is now collapsed
     */
    fun toggleGroup(flatPos: Int): Boolean {
        return expandCollapseController.toggleGroup(flatPos)
    }

    /**
     * @param group the [ExpandableGroup] being toggled
     * @return true if the group is expanded, *after* the toggle, false if the group is now collapsed
     */
    fun toggleGroup(group: ExpandableGroup<*>): Boolean {
        return expandCollapseController.toggleGroup(group)
    }

    /**
     * @param flatPos the flattened position of an item in the list
     * @return true if `group` is expanded, false if it is collapsed
     */
    fun isGroupExpanded(flatPos: Int): Boolean {
        return expandCollapseController.isGroupExpanded(flatPos)
    }

    /**
     * @param group the [ExpandableGroup] being checked for its collapsed state
     * @return true if `group` is expanded, false if it is collapsed
     */
    fun isGroupExpanded(group: ExpandableGroup<*>): Boolean {
        return expandCollapseController.isGroupExpanded(group)
    }

    /**
     * Stores the expanded state map across state loss.
     *
     *
     * Should be called from whatever [Activity] that hosts the RecyclerView that [ ] is attached to.
     *
     *
     * This will make sure to add the expanded state map as an extra to the
     * instance state bundle to be used in [.onRestoreInstanceState].
     *
     * @param savedInstanceState The `Bundle` into which to store the
     * expanded state map
     */
    fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBooleanArray(EXPAND_STATE_MAP, expandableList.expandedGroupIndexes)
    }

    /**
     * Fetches the expandable state map from the saved instance state [Bundle]
     * and restores the expanded states of all of the list items.
     *
     *
     * Should be called from [Activity.onRestoreInstanceState]  in
     * the [Activity] that hosts the RecyclerView that this
     * [ExpandableRecyclerViewAdapter] is attached to.
     *
     *
     *
     * @param savedInstanceState The `Bundle` from which the expanded
     * state map is loaded
     */
    fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null || !savedInstanceState.containsKey(EXPAND_STATE_MAP)) {
            return
        }
        expandableList.expandedGroupIndexes = savedInstanceState.getBooleanArray(EXPAND_STATE_MAP)
        notifyDataSetChanged()
    }

    fun setOnGroupClickListener(listener: OnGroupClickListener) {
        groupClickListener = listener
    }

    fun setOnGroupExpandCollapseListener(listener: GroupExpandCollapseListener) {
        expandCollapseListener = listener
    }

    /**
     * Called from [.onCreateViewHolder] when  the list item created is a group
     *
     * @param viewType an int returned by [ExpandableRecyclerViewAdapter.getItemViewType]
     * @param parent the [ViewGroup] in the list for which a [GVH]  is being created
     * @return A [GVH] corresponding to the group list item with the  `ViewGroup` parent
     */
    abstract fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GVH

    /**
     * Called from [.onCreateViewHolder] when the list item created is a child
     *
     * @param viewType an int returned by [ExpandableRecyclerViewAdapter.getItemViewType]
     * @param parent the [ViewGroup] in the list for which a [CVH]  is being created
     * @return A [CVH] corresponding to child list item with the  `ViewGroup` parent
     */
    abstract fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): CVH

    /**
     * Called from onBindViewHolder(RecyclerView.ViewHolder, int) when the list item
     * bound to is a  child.
     *
     *
     * Bind data to the [CVH] here.
     *
     * @param holder The `CVH` to bind data to
     * @param flatPosition the flat position (raw index) in the list at which to bind the child
     * @param group The [ExpandableGroup] that the the child list item belongs to
     * @param childIndex the index of this child within it's [ExpandableGroup]
     */
    abstract fun onBindChildViewHolder(holder: CVH, flatPosition: Int, group: ExpandableGroup<*>,
                                       childIndex: Int)

    /**
     * Called from onBindViewHolder(RecyclerView.ViewHolder, int) when the list item bound to is a
     * group
     *
     *
     * Bind data to the [GVH] here.
     *
     * @param holder The `GVH` to bind data to
     * @param flatPosition the flat position (raw index) in the list at which to bind the group
     * @param group The [ExpandableGroup] to be used to bind data to this [GVH]
     */
    abstract fun onBindGroupViewHolder(holder: GVH, flatPosition: Int, group: ExpandableGroup<*>)

//    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
//        return false
//    }
//
//    override fun onItemAdd(position: Int) {
//        Log.d("Children", "Position dismiss: " + position)
//    }
//
//    override fun onItemDismiss(position: Int) {
//        Log.d("Children", "Position dismiss: " + position)
//    }


    companion object {

        private val EXPAND_STATE_MAP = "expandable_recyclerview_adapter_expand_state_map"
    }
}