package com.example.skechycrag.ui.routedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skechycrag.domain.GetRouteDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteDetailViewModel @Inject constructor(
    private val getRouteDetailUseCase: GetRouteDetailUseCase
):ViewModel() {

    private val _routeDetailState = MutableStateFlow<RouteDetailState>(RouteDetailState.Start)
    val routeDetailState: StateFlow<RouteDetailState> = _routeDetailState

    fun searchByName(cragName: String) {
        _routeDetailState.update { RouteDetailState.Loading }
        viewModelScope.launch {
            var routeDetail = getRouteDetailUseCase(cragName)

            if (!routeDetail.isNullOrEmpty()) {
                val uiRouteDetail = routeDetail.map { dataRouteModel ->
                    // Convert data layer CragModel to UI layer CragModel here
                    com.example.skechycrag.ui.model.RouteModel(
                        crag_name = dataRouteModel.crag_name,
                        route_name = dataRouteModel.route_name,
                        grade = dataRouteModel.grade,
                        type = dataRouteModel.type
                    )
                }
                _routeDetailState.update { RouteDetailState.Success(uiRouteDetail) }
            } else {
                _routeDetailState.update { RouteDetailState.Error(false) }
            }
        }
    }
}