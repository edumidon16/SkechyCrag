package com.example.skechycrag.data.repository

import com.example.skechycrag.data.model.crag.CragModel
import com.example.skechycrag.data.network.CragServices
import javax.inject.Inject

class CragRepository @Inject constructor(
    private val cragServices: CragServices
) {
    suspend fun getCragInfo(name: String): List<CragModel> {
        return cragServices.getCragInfo(name)
    }
}