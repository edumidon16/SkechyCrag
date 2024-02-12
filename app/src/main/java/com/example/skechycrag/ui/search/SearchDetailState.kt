package com.example.skechycrag.ui.search

import com.example.skechycrag.ui.model.CragModel

sealed class SearchDetailState {
    data object Start: SearchDetailState()
    data object Loading: SearchDetailState()
    data class Error(val error: Boolean): SearchDetailState()
    data class Success(val cragInfo: List<CragModel>): SearchDetailState()
}
