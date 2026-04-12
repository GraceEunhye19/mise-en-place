package com.example.miseenplace.features.converter.converters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private val lengthUnits = listOf("cm", "m", "km", "in", "ft", "miles")

private fun convertLength(value: Double, from: String, to: String): Double{  //number, unit, unit
    val toCm = mapOf(  //maths
        "cm" to 1.0, "m" to 100.0, "km" to 100000.0,
        "in" to 2.54, "ft" to 30.48, "miles" to 160934.0
    )
    val inCm = value* (toCm[from]?: 1.0)  //everything to cm first
    return inCm/(toCm[to]?: 1.0)  //then to the final unit. 1 is put to avoid app crashing
}

@Composable
fun LengthConverter(){
    var input by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("cm") } //default is cm
    var toUnit by remember { mutableStateOf("m") } //default is m
    var fromExpanded by remember { mutableStateOf(false) } //default is closed
    var toExpanded by remember { mutableStateOf(false) }  //default is closed

    val result = input.toDoubleOrNull()?.let{  //no letters typed will crash the app
        val converted = convertLength(it, fromUnit, toUnit) //run the math
        "%.4f".format(converted).trimEnd('0').trimEnd('.') //4.dp, trim trailing zeros or .0
    } ?: "" //return blank if inout is blank

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
        lengthUnits,
        result
    )
}