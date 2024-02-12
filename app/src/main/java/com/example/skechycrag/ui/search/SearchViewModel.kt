package com.example.skechycrag.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skechycrag.data.repository.CragRepository
import com.example.skechycrag.domain.GetCragUseCase
import com.example.skechycrag.ui.login.UserDetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getCragUseCase: GetCragUseCase
):ViewModel() {

    private val _searchState = MutableStateFlow<SearchDetailState>(SearchDetailState.Start)
    val searchState: StateFlow<SearchDetailState> = _searchState

    fun searchByName(cragName: String) {
        _searchState.update { SearchDetailState.Loading }
        viewModelScope.launch {
            var cragInfo = getCragUseCase(cragName)

            if(!cragInfo.isNullOrEmpty()){
                val uiCragInfo = cragInfo.map { dataCragModel ->
                    // Convert data layer CragModel to UI layer CragModel here
                    com.example.skechycrag.ui.model.CragModel(
                        crag_id = dataCragModel.crag_id, //
                        crag_name = dataCragModel.crag_name,
                        number_routes = dataCragModel.number_routes
                    )
                }
                _searchState.update { SearchDetailState.Success(uiCragInfo) }
            }else{
                _searchState.update { SearchDetailState.Error(false) }
            }
        }
    }

}