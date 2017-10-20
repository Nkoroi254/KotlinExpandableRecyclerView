package com.example.nkoroi.kotlinexpandablerecyclerview

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.example.nkoroi.kotlinexpandablerecyclerview.extensions.findView
import com.example.nkoroi.kotlinexpandablerecyclerview.listeners.SimpleItemTouchHelperCallback
import com.nkoroi254.kotlinexpandablerecyclerview.ExpandableRecyclerViewAdapter
import com.nkoroi254.kotlinexpandablerecyclerview.listeners.ItemTouchHelperAdapter
import com.nkoroi254.kotlinexpandablerecyclerview.listeners.ItemTouchHelperViewHolder
import com.nkoroi254.kotlinexpandablerecyclerview.listeners.OnStartDragListener
import com.nkoroi254.kotlinexpandablerecyclerview.models.ExpandableGroup
import com.nkoroi254.kotlinexpandablerecyclerview.viewholders.ChildViewHolder
import com.nkoroi254.kotlinexpandablerecyclerview.viewholders.GroupViewHolder

class MainActivity : AppCompatActivity() , OnStartDragListener {

    private var itemtouchhelper: ItemTouchHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView  = findView<RecyclerView>(R.id.recycler_view)

        val layoutManag = LinearLayoutManager(this)
        val adapter = MyAdapter(GenreDataFactory.makeGroups())
        recyclerView.layoutManager = layoutManag
        val callback = SimpleItemTouchHelperCallback(adapter)
        itemtouchhelper = ItemTouchHelper(callback)
        itemtouchhelper!!.attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter


        val textClick = findViewById<TextView>(R.id.toggle_button)
        textClick.setOnClickListener(View.OnClickListener {
            adapter.toggleGroup(GenreDataFactory.makeClassicGenre())
        })
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
       itemtouchhelper!!.startDrag(viewHolder)
    }


    class MyAdapter(groups: List<ExpandableGroup<*>>) : ExpandableRecyclerViewAdapter<ParentViewHolder, KidViewHolder>(groups), ItemTouchHelperAdapter {

        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            return false
        }

        override fun onItemDismiss(position: Int) {
            notifyDataSetChanged()
        }

        override fun onItemAdd(position: Int) {
            notifyDataSetChanged()
        }


        var artist : String? = null

        var arrow:ImageView? = null
        var artistList:List<Artist> = ArrayList()



        override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.group_items, parent, false)
            return ParentViewHolder(view)
        }

        override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): KidViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.child_items, parent, false)
            return KidViewHolder(view)
        }

        override fun onBindChildViewHolder(holder: KidViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
            artistList = group.children as List<Artist>
            var str : String = group.children!![childIndex].toString()
            holder.childTextView.text = str
        }

        override fun onBindGroupViewHolder(holder: ParentViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
            var genre1 : Genre = group.parent as Genre
            holder.genreName.text = genre1.title
            holder.genreImage.setImageResource(genre1.iconRes)
            arrow = holder.arrow

        }

    }

    class ParentViewHolder(itemView: View) : GroupViewHolder(itemView) {
        val genreName =  itemView.findView<TextView>(R.id.list_item_genre_name)
        val genreImage = itemView.findView<ImageView>(R.id.list_item_genre_icon)
        val arrow = itemView.findView<ImageView>(R.id.list_item_genre_arrow)

        override
        fun expand() {
            animateExpand()
        }

        override
        fun collapse() {
            animateCollapse()
        }

        private fun animateExpand() {
            val rotate = RotateAnimation(360f, 180f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
            rotate.duration = 300
            rotate.fillAfter = true
            arrow!!.setRotation(180f)
        }

        private fun animateCollapse() {
            val rotate = RotateAnimation(180f, 360f, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f)
            rotate.duration = 300
            rotate.fillAfter = true
            arrow!!.setRotation(360f)
        }

    }

    class KidViewHolder(itemView: View) : ChildViewHolder(itemView), ItemTouchHelperViewHolder {



        val childTextView = itemView.findViewById<TextView>(R.id.list_item_artist_name)


        override fun onItemSelected() {

        }

        override fun onItemClear() {

        }
    }

    inline fun  <reified T : View> Activity.findView(id : Int)  = findViewById<T>(id) as T


}


