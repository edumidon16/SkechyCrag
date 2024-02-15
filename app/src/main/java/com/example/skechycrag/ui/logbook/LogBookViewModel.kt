package com.example.skechycrag.ui.logbook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skechycrag.domain.GetLogBookUseCase
import com.example.skechycrag.ui.routedetail.RouteDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogBookViewModel @Inject constructor(
    private val getLogBookUseCase: GetLogBookUseCase
):ViewModel(){

    private val _logBookDetailState = MutableStateFlow<LogBookDetailState>(LogBookDetailState.Start)
    val routeDetailState: StateFlow<LogBookDetailState> = _logBookDetailState

    fun showLogBook(username: String){
        _logBookDetailState.update { LogBookDetailState.Loading }
        viewModelScope.launch {
            var logBookRoutes = getLogBookUseCase(username)

            if (!logBookRoutes.isNullOrEmpty()) {
                val uiLogBookRoutes = logBookRoutes.map { dataLogBookRoutes ->
                    com.example.skechycrag.ui.model.UserRouteModel(
                        crag_name = dataLogBookRoutes.crag_name,
                        route_name = dataLogBookRoutes.route_name,
                        grade = dataLogBookRoutes.grade,
                        type = dataLogBookRoutes.type,
                        tries = dataLogBookRoutes.tries
                    )
                }
                _logBookDetailState.update { LogBookDetailState.Success(uiLogBookRoutes) }
            } else {
                _logBookDetailState.update { LogBookDetailState.Error(false) }
            }
        }
    }

}