package com.example.miseenplace.features.converter.converters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private val weightUnits = listOf("g", "kg", "oz", "lb", "mg")

private fun convertWeight(value: Double, from: String, to: String): Double{  //number, unit, unit
    val toG = mapOf(  //maths
        "g" to 1.0, "kg" to 1000.0, "oz" to 28.3495,
        "lb" to 453.592, "mg" to 0.001
    )
    val inMl = value* (toG[from]?: 1.0)  //everything to grams first
    return inMl/(toG[to]?: 1.0)  //then to the final unit. 1 is put to avoid app crashing
}

@Composable
fun WeightConverter(){
    var input by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("cup") } //default is cups
    var toUnit by remember { mutableStateOf("ml") } //default is ml
    var fromExpanded by remember { mutableStateOf(false) } //default is closed
    var toExpanded by remember { mutableStateOf(false) }  //default is closed

    val result = input.toDoubleOrNull()?.let{  //no letters typed will crash the app
        val converted = convertWeight(it, fromUnit, toUnit) //run the math
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
        weightUnits,
        result
    )
}