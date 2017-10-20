package com.example.nkoroi.kotlinexpandablerecyclerview.extensions

import android.view.View

/**
 * Created by nkoroi on 17/10/17.
 */
inline fun <reified T : View> View.findView(id : Int) = findViewById<T>(id) as T
