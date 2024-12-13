package com.example.agroscan.data.local.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ScanHistoryItem(
    val imageRes: Int, // Drawable resource ID
    val plantName: String,
    val scanResult: String,
    val scanDate: String
) : Parcelable
