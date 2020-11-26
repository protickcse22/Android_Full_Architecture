package com.androiddevs.mvvmnewsapp.utils

import android.view.View
import android.widget.ProgressBar

fun ProgressBar.showProgressBar() {
    this.visibility = View.VISIBLE
}

fun ProgressBar.hideProgressBar() {
    this.visibility = View.INVISIBLE
}