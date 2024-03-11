package com.example.skechycrag.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skechycrag.domain.ReadImageUseCase
import com.example.skechycrag.ui.model.RouteInfo
import com.example.skechycrag.ui.routedetail.RouteDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val readImageUseCase: ReadImageUseCase
): ViewModel(){

    private val _addState = MutableStateFlow<AddState>(AddState.Start)
    val addState: StateFlow<AddState> = _addState

    fun readInfo(image: String) {
        _addState.update { AddState.Start }
        viewModelScope.launch {
            val routeInfo = readImageUseCase(image)

            if(!routeInfo.isNullOrEmpty()){
                val uiRouteInfo = routeInfo.map { data ->
                    // Convert data layer CragModel to UI layer CragModel here
                    com.example.skechycrag.ui.model.RouteInfo(
                        routeName = data.routeName,
                        grade = data.grade
                    )
                }
                _addState.update { AddState.Success(uiRouteInfo as MutableList<RouteInfo>) }
            }else{
                _addState.update { AddState.Error("empty") }
            }
        }
    }
}