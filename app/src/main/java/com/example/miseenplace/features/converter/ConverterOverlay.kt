package com.example.miseenplace.features.converter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.miseenplace.R
import com.example.miseenplace.features.converter.converters.CookingConverter
import com.example.miseenplace.features.converter.converters.CurrencyConverter
import com.example.miseenplace.features.converter.converters.LengthConverter
import com.example.miseenplace.features.converter.converters.WeightConverter
import com.example.miseenplace.ui.theme.MiseEnPlaceTheme

@Composable
fun ConverterOverlay(type: ConverterType, onClose: () -> Unit){
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
        //contentAlignment = Alignment.Center
        contentAlignment = Alignment.BottomCenter

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when (type) {
                        ConverterType.LENGTH -> stringResource(R.string.length)
                        ConverterType.COOKING -> stringResource(R.string.cooking)
                        ConverterType.WEIGHT -> stringResource(R.string.weight)
                        ConverterType.CURRENCY -> stringResource(R.string.currency)
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (type) {
                ConverterType.LENGTH -> LengthConverter()
                ConverterType.COOKING -> CookingConverter()
                ConverterType.WEIGHT -> WeightConverter()
                ConverterType.CURRENCY -> CurrencyConverter()
            }
        }
    }
}

