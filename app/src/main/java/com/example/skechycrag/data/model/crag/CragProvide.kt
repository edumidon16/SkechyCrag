package com.example.skechycrag.data.model.crag

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CragProvide @Inject constructor(){
    var cragInfo : List<CragModel> = emptyList()
}