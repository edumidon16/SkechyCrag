package com.example.skechycrag.ui.routedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skechycrag.domain.AddAlertUseCase
import com.example.skechycrag.domain.GetMoreInfoRouteUseCase
import com.example.skechycrag.domain.GetRouteDetailUseCase
import com.example.skechycrag.ui.model.MoreInfoRouteModel
import com.example.skechycrag.ui.model.UserRouteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteDetailViewModel @Inject constructor(
    private val getRouteDetailUseCase: GetRouteDetailUseCase,
    private val getMoreInfoRouteUseCase: GetMoreInfoRouteUseCase,
    private val addAlertUseCase: AddAlertUseCase
) : ViewModel() {

    private val _routeDetailState = MutableStateFlow<RouteDetailState>(RouteDetailState.Start)
    val routeDetailState: StateFlow<RouteDetailState> = _routeDetailState

    private val _moreInfoState = MutableLiveData<CommunityState>()
    val moreInfoState: LiveData<CommunityState> = _moreInfoState

    private val _averageGrade = MutableLiveData<String>()
    val averageGrade: LiveData<String> get() = _averageGrade

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
                        type = dataRouteModel.type,
                        community_grade = dataRouteModel.community_grade
                    )
                }
                _routeDetailState.update { RouteDetailState.Success(uiRouteDetail) }
            } else {
                _routeDetailState.update { RouteDetailState.Error(false) }
            }
        }
    }

    fun addRouteToLogBook(route: UserRouteModel) {
        viewModelScope.launch {
            val addRoute = com.example.skechycrag.data.model.user.UserRouteModel(
                crag_name = route.crag_name,
                route_name = route.route_name,
                type = route.type,
                grade = route.grade,
                comment = route.comment,
                tries = route.tries
            )
            getRouteDetailUseCase.addRoute(addRoute)
        }
    }
    fun moreInfoRoute(routeName: String, bookGrade: String) {
        _moreInfoState.value = CommunityState.Loading
        viewModelScope.launch {
            // Update LiveData to Loading or reset it before fetching new data
            _moreInfoState.postValue(CommunityState.Loading)

            val moreInfoRoute = getMoreInfoRouteUseCase(routeName, bookGrade)

            if (!moreInfoRoute.isNullOrEmpty()) {
                val uiMoreInfoRouteDetail = moreInfoRoute.map { dataMoreInfoRouteModel ->
                    MoreInfoRouteModel(
                        username = dataMoreInfoRouteModel.username,
                        comment = dataMoreInfoRouteModel.comment,
                        grade = dataMoreInfoRouteModel.grade,
                        alert = dataMoreInfoRouteModel.alert
                    )
                }
                _moreInfoState.postValue(CommunityState.Success(uiMoreInfoRouteDetail))
            } else {
                _moreInfoState.postValue(CommunityState.Error)
            }
        }
    }

    fun getCommunityGrade() {
        viewModelScope.launch {
            val communityGrade = getMoreInfoRouteUseCase.communityGrade
            _averageGrade.value = communityGrade
        }
    }

    fun addAlert(alertMessage: String, routeName:String) {
        viewModelScope.launch {
            addAlertUseCase(alertMessage, routeName)
        }

    }
}