package com.example.skechycrag.domain

import com.example.skechycrag.data.model.crag.CragModel
import com.example.skechycrag.data.repository.CragRepository
import javax.inject.Inject

class GetCragUseCase @Inject constructor(
    private val repository: CragRepository
) {
    suspend operator fun invoke(name:String):List<CragModel>{
        return repository.getCragInfo(name)
    }
}