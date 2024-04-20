package com.ifs21031.lostfound.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class DelcomLofo (
    val id: Int,
    val title: String,
    val description: String,
    var isFinished: Boolean,
    val cover: String,
) : Parcelable{
}