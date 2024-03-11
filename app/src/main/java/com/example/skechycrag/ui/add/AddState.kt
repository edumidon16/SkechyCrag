package com.example.skechycrag.ui.add

import com.example.skechycrag.ui.model.RouteInfo

sealed class AddState {
    data object Start : AddState()
    data object Loading : AddState()
    data class Error(val error: String) : AddState()
    data class Success(val routeList: MutableList<RouteInfo>) : AddState()
}