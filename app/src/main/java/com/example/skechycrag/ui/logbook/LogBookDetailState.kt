package com.example.skechycrag.ui.logbook

import com.example.skechycrag.ui.model.UserRouteModel

sealed class LogBookDetailState {
    data object Start: LogBookDetailState()
    data object Loading: LogBookDetailState()
    data class Error(val error: Boolean): LogBookDetailState()
    data class Success(val logBookRoutes: List<UserRouteModel>): LogBookDetailState()
}