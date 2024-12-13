package com.example.agroscan.data.local.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleItem(
//    val id: Int,
    val title: String,
//    val content: String,
    val image: Int,
    val date_upload: String
) : Parcelable