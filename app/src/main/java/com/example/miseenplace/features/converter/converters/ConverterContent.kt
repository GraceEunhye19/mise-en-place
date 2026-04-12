package com.example.miseenplace.features.converter.converters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.miseenplace.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterContent(
    input: String, //the number typed
    onInputChange: (String) -> Unit, //what happens if the number types changes
    fromUnit: String, //unit selected in the "from" dropdown
    toUnit: String,  //unit selected in the "to" dropdown
    fromExpanded: Boolean, //is from expanded
    toExpanded: Boolean, //is to expanded
    onFromExpandedChange: (Boolean) -> Unit,  //what to do if from is tapped opened or closed
    onToExpandedChange: (Boolean) -> Unit, //what to do if to is tapped opened or closed
    onFromUnitChange: (String) -> Unit, //new from unit
    onToUnitChange: (String) -> Unit,  //new to unit
    units: List<String>, //list of options for both
    result: String //final converted answer
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        //input field
        OutlinedTextField(
            value = input,
            onValueChange = onInputChange,
            label = {
                Text(stringResource(R.string.enter_value)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), //numbers with decimals
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //from
            ExposedDropdownMenuBox(
                expanded = fromExpanded,
                onFromExpandedChange,
                modifier = Modifier.weight(1f)
            ) {
                //the text
                OutlinedTextField(
                    value = fromUnit, //the unit for from
                    onValueChange = {},
                    readOnly = true, //unchangable here
                    label = {Text(stringResource(R.string.from))},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded)},
                    modifier = Modifier.menuAnchor()
                )
                //the menu
                ExposedDropdownMenu(
                    expanded = fromExpanded,
                    {onFromExpandedChange(false)},
                    modifier = Modifier.weight(1f)
                ) {
                    //the units
                    units.forEach { unit ->
                        DropdownMenuItem(
                            text = {Text(unit)},
                            onClick = {
                                onFromUnitChange(unit)
                                onFromExpandedChange(false)
                            }
                        )
                    }
                }
            }

            //arrow. not swap icon because you can't swap units between to and from
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRightAlt,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )

            //to
            ExposedDropdownMenuBox(
                expanded = toExpanded,
                onToExpandedChange,
                modifier = Modifier.weight(1f)
            ) {
                //the text
                OutlinedTextField(
                    value = toUnit, //the unit for to
                    onValueChange = {},
                    readOnly = true, //unchangable here
                    label = {Text(stringResource(R.string.to))},
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toExpanded)},
                    modifier = Modifier.menuAnchor()
                )
                //the menu
                ExposedDropdownMenu(
                    expanded = toExpanded,
                    {onToExpandedChange(false)},
                    modifier = Modifier.weight(1f)
                ) {
                    //the units
                    units.forEach { unit ->
                        DropdownMenuItem(
                            text = {Text(unit)},
                            onClick = {
                                onToUnitChange(unit)
                                onToExpandedChange(false)
                            }
                        )
                    }
                }
            }
        }

        //results, if not empty
        if (result.isNotEmpty()){
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.result),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    //the result itself
                    Text(
                        text = "$result $toUnit",  //the answer and the unit
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}