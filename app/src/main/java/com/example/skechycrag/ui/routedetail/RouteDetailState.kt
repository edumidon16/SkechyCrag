package com.example.skechycrag.ui.routedetail

import com.example.skechycrag.ui.model.CragModel
import com.example.skechycrag.ui.model.MoreInfoRouteModel
import com.example.skechycrag.ui.model.RouteModel
import com.example.skechycrag.ui.search.SearchDetailState

sealed class RouteDetailState {
    data object Start: RouteDetailState()
    data object Loading: RouteDetailState()
    data class Error(val error: Boolean): RouteDetailState()
    data class Success(val cragInfo: List<RouteModel>): RouteDetailState()
}

sealed class CommunityState{
    data object Start: CommunityState()
    data object Loading: CommunityState()
    data object Error: CommunityState()
    data class Success(val moreInfoList: List<MoreInfoRouteModel>): CommunityState()
}