package com.example.miseenplace.features.converter

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.SoupKitchen
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miseenplace.R


@Composable
fun ConverterScreen(
    viewModel: ConverterViewModel = viewModel()
){
    Box(modifier = Modifier.fillMaxSize()){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.converter_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = stringResource(R.string.converter_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            val cards = listOf(
                ConverterCardData(
                    title = stringResource(R.string.length),
                    subtitle = stringResource(R.string.lengths),
                    icon = Icons.Outlined.Height,
                    gradient = listOf(Color(0xFF6B9FD4), Color(0xFF3D5F90)),
                    type = ConverterType.LENGTH
                ),
                ConverterCardData(
                    title = stringResource(R.string.cooking),
                    subtitle = stringResource(R.string.measurements),
                    icon = Icons.Outlined.SoupKitchen,
                    gradient = listOf(Color(0xFF6B9FD4), Color(0xFF3D5F90)),
                    type = ConverterType.COOKING
                ),
                ConverterCardData(
                    title = stringResource(R.string.weight),
                    subtitle = stringResource(R.string.weights),
                    icon = Icons.Outlined.Scale,
                    gradient = listOf(Color(0xFF6B9FD4), Color(0xFF3D5F90)),
                    type = ConverterType.WEIGHT
                ),
                ConverterCardData(
                    title = stringResource(R.string.currency),
                    subtitle = stringResource(R.string.currencies),
                    icon = Icons.Outlined.MonetizationOn,
                    gradient = listOf(Color(0xFF6B9FD4), Color(0xFF3D5F90)),
                    type = ConverterType.CURRENCY
                )
            )
            //arrangement
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ConverterCard(data = cards[0], onClick =  {viewModel.openConverter(cards[0].type) })
                    ConverterCard(data = cards[2], onClick =  {viewModel.openConverter(cards[2].type) })
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ConverterCard(data = cards[1], onClick =  {viewModel.openConverter(cards[1].type) })
                    ConverterCard(data = cards[3], onClick =  {viewModel.openConverter(cards[3].type) })
                }
            }
        }

        //overlays
        viewModel.activeConverter?.let{type ->
            ConverterOverlay(
                type = type,
                onClose = {viewModel.closeConverter()}
            )
        }
    }
}

data class ConverterCardData(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val gradient: List<Color>,
    val type: ConverterType
)

@Composable
fun ConverterCard(data: ConverterCardData, onClick:()->Unit){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(160.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(Brush.verticalGradient(data.gradient))
        .clickable { onClick() },
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = data.title,
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = data.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = data.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

