package com.example.skechycrag.ui.showresponse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skechycrag.domain.ReadImageUseCase
import com.example.skechycrag.ui.model.RouteInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowResponseViewModel @Inject constructor(
    private val readImageUseCase: ReadImageUseCase
): ViewModel(){
    fun addNewRoutes(cragName:String, newRoutesList: MutableList<RouteInfo>) {
        viewModelScope.launch {
            val dataNewRoutes = newRoutesList.map { dataNewRoutes ->
                // Convert data layer CragModel to UI layer CragModel here
                com.example.skechycrag.data.model.route.RouteInfo(
                    routeName = dataNewRoutes.routeName,
                    grade = dataNewRoutes.grade
                )
            }
            readImageUseCase.addNewRoutes(cragName,
                dataNewRoutes as MutableList<com.example.skechycrag.data.model.route.RouteInfo>
            )

        }
    }

}