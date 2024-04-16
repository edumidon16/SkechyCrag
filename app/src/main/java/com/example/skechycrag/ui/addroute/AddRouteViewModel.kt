package com.example.skechycrag.ui.addroute

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skechycrag.domain.AddNewRouteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRouteViewModel @Inject constructor(
    private val addNewRouteUseCase: AddNewRouteUseCase
): ViewModel(){

    fun addRoute(cragName : String, routeName : String, routeGrade : String, type:String){
        viewModelScope.launch {
            addNewRouteUseCase(cragName, routeName, routeGrade, type)
        }
    }
}