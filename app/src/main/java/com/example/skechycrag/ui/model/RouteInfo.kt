package com.example.skechycrag.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteInfo(val routeName: String, val grade: String) : Parcelable
