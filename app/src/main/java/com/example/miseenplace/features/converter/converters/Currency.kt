package com.example.miseenplace.features.converter.converters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private val currencyUnits = listOf("NGN", "USD", "EUR", "GBP", "CAD", "GHS")

private fun convertCurrency(value: Double, from: String, to: String): Double{  //number, unit, unit
    val toUsd = mapOf(  //maths
        "USD" to 1.0, "NGN" to 0.00063, "EUR" to 1.08,
        "GBP" to 1.27, "CAD" to 0.74, "GHS" to 0.069,
    )
    val inMl = value* (toUsd[from]?: 1.0)  //everything to dollars first
    return inMl/(toUsd[to]?: 1.0)  //then to the final unit. 1 is put to avoid app crashing
}

@Composable
fun CurrencyConverter(){
    var input by remember { mutableStateOf("") }
    var fromUnit by remember { mutableStateOf("NGN") } //default is naira
    var toUnit by remember { mutableStateOf("USD") } //default is dollars
    var fromExpanded by remember { mutableStateOf(false) } //default is closed
    var toExpanded by remember { mutableStateOf(false) }  //default is closed

    val result = input.toDoubleOrNull()?.let{  //no letters typed will crash the app
        val converted = convertCurrency(it, fromUnit, toUnit) //run the math
        "%.2f".format(converted) //2d.p
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
        currencyUnits,
        result
    )
}