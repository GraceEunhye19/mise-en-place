package com.example.miseenplace.features.converter.converters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private val cookingUnits = listOf("tsp", "tbsp", "cup", "ml", "l", "fl oz")

private fun convertCooking(value: Double, from: String, to: String): Double{  //number, unit, unit
    val toMl = mapOf(  //maths
        "tsp" to 4.92892, "tbsp" to 14.7868, "cup" to 236.588,
        "ml" to 1.0, "l" to 1000.0, "fl oz" to 29.5735
    )
    val inMl = value* (toMl[from]?: 1.0)  //everything to ml first
    return inMl/(toMl[to]?: 1.0)  //then to the final unit. 1 is put to avoid app crashing
}

@Composable
fun CookingConverter(){
    var input by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("cup") } //default is cups
    var toUnit by remember { mutableStateOf("ml") } //default is ml
    var fromExpanded by remember { mutableStateOf(false) } //default is closed
    var toExpanded by remember { mutableStateOf(false) }  //default is closed

    val result = input.toDoubleOrNull()?.let{  //no letters typed will crash the app
        val converted = convertCooking(it, fromUnit, toUnit) //run the math
        "%.4f".format(converted).trimEnd('0').trimEnd('.') //4.dp, trim trailing zeros or .0
    } ?: "" //return blank if input is blank

    ConverterContent(
        input,
        {input = it},
        fromUnit,
        toUnit,
        fromExpanded,
        toExpanded,
        {fromExpanded = it},
        {toExpanded = it},
        {fromUnit = it},
        {toUnit = it},
        cookingUnits,
        result
    )
}