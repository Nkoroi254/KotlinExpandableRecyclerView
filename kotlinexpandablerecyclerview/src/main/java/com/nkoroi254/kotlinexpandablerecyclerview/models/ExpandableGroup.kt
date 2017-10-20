package com.nkoroi254.kotlinexpandablerecyclerview.models

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import java.util.*


/**
 * Created by nkoroi on 16/10/17.
 */
open class ExpandableGroup<T : Any> : Parcelable {
    var parent: Any? = null
        private set
    var children: List<T>? = null
        private set

    val itemCount: Int
        get() = if (children == null) 0 else children!!.size


    constructor(parent: Any, children: List<T>) {
        this.parent = parent
        this.children = children
    }

    override fun toString(): String {
        return "ExpandableGroup{" +
                "parent='" + parent.toString() + '\'' +
                ", children=" + children +
                '}'
    }

    protected constructor(`in`: Parcel) {
        parent = `in`.readString()
        val haschildren = `in`.readByte()
        val size = `in`.readInt()
        if (haschildren.toInt() == 0x01) {
            children = ArrayList(size)
            val type = `in`.readSerializable() as Class<*>
            `in`.readList(children, type.classLoader)
        } else {
            children = null
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(parent.toString())
        if (children == null) {
            dest.writeByte(0x00.toByte())
            dest.writeInt(0)
        } else {
            dest.writeByte(0x01.toByte())
            dest.writeInt(children!!.size)
            val objectsType = children!![0].javaClass
            dest.writeSerializable(objectsType)
            dest.writeList(children)
        }
    }

    companion object {

        val CREATOR: Creator<ExpandableGroup<*>> = object : Creator<ExpandableGroup<*>> {
            override fun createFromParcel(parcel : Parcel): ExpandableGroup<*> {
                return ExpandableGroup<Any>(parcel)
            }

            override fun newArray(size: Int): Array<ExpandableGroup<*>?> {
                return arrayOfNulls(size)
            }
        }
    }
}