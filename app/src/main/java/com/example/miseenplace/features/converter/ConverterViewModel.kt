package com.example.miseenplace.features.converter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class ConverterType {
    LENGTH, COOKING, WEIGHT, CURRENCY
}

class ConverterViewModel: ViewModel(){

    var activeConverter by mutableStateOf<ConverterType?>(null)
    private set

    fun openConverter(type: ConverterType){
        activeConverter = type
    }

    fun closeConverter(){
        activeConverter = null
    }
}