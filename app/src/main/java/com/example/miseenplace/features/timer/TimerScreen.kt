package com.example.miseenplace.features.timer

import android.media.ToneGenerator
import android.media.AudioManager
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miseenplace.R
import kotlin.concurrent.timer

@Composable
fun TimerScreen(viewModel: TimerViewModel = viewModel()){
    val timeLeft by viewModel.timeLeftMillis.collectAsStateWithLifecycle()
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val selectedHrs by viewModel.selectedHrs.collectAsStateWithLifecycle()
    val selectedMins by viewModel.selectedMins.collectAsStateWithLifecycle()
    val selectedSecs by viewModel.selectedSecs.collectAsStateWithLifecycle()


    LaunchedEffect(timerState) {
        if (timerState == TimerState.DONE) {
            val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 200)
            while (timerState == TimerState.DONE){
                toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 1000)
                kotlinx.coroutines.delay(500)
            }
            toneGen.release()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.timer_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.timer_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (timerState != TimerState.IDLE){
            val totalSeconds = (timeLeft / 1000).toInt()
            val h = totalSeconds / 3600
            val m = (totalSeconds % 3600) / 60
            val s = totalSeconds % 60

            val isTicking = timerState == TimerState.RUNNING
            val infiniteTransition = rememberInfiniteTransition(label = "isTicking")
            val brightness by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = if (isTicking) 0.4f else 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "brightness"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF6B9FD4), Color(0xFF3D5F90))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "%02d:%02d:%02d".format(h, m, s),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = if (isTicking) brightness else 1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        if (timerState == TimerState.IDLE){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TimerPickerColumn(
                    "HH",
                    selectedHrs,
                    0..23,
                    {viewModel.setHrs(it)},
                    gradient = listOf(Color(0xFF6B9FD4), Color(0xFF3D5F90)),
                    modifier = Modifier.weight(1f)
                )

                TimerPickerColumn(
                    "MM",
                    selectedMins,
                    0..59,
                    {viewModel.setMins(it)},
                    gradient = listOf(Color(0xFF6B9FD4), Color(0xFF3D5F90)),
                    modifier = Modifier.weight(1f)
                )

                TimerPickerColumn(
                    "SS",
                    selectedSecs,
                    0..59,
                    {viewModel.setSecs(it)},
                    gradient = listOf(Color(0xFF6B9FD4), Color(0xFF3D5F90)),
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        //buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            when (timerState){
                TimerState.IDLE -> {
                    TimerButton(
                    label = stringResource(R.string.start),
                    gradient = listOf(Color(0xFF6B9FD4), Color(0xFF3D5F90)),
                    onClick = { viewModel.start() }
                    )
                }
                TimerState.RUNNING -> {
                    TimerButton(
                        label = stringResource(R.string.pause),
                        gradient = listOf(Color(0xFFE8A598), Color(0xFFC17B6F)),
                        onClick = { viewModel.pause() }
                    )
                    TimerButton(
                        label = stringResource(R.string.reset),
                        gradient = listOf(Color(0xFF8BB98B), Color(0xFF5A8A5A)),
                        onClick = { viewModel.reset() }
                    )
                }
                TimerState.PAUSED -> {
                    TimerButton(
                        label = stringResource(R.string.resume),
                        gradient = listOf(Color(0xFF6B9FD4), Color(0xFF3D5F90)),
                        onClick = { viewModel.resume() }
                    )
                    TimerButton(
                        label = stringResource(R.string.reset),
                        gradient = listOf(Color(0xFF8BB98B), Color(0xFF5A8A5A)),
                        onClick = { viewModel.reset() }
                    )
                }
                TimerState.DONE -> {
                    TimerButton(
                        label = stringResource(R.string.reset),
                        gradient = listOf(Color(0xFF8BB98B), Color(0xFF5A8A5A)),
                        onClick = { viewModel.reset() }
                    )
                }
            }
        }

        if (timerState == TimerState.DONE){
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.time_up),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

    }
}

@Composable
fun TimerPickerColumn(
    label: String,
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    gradient: List<Color>,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Brush.verticalGradient(gradient)),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "%02d".format(value),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable {
                    if (value > range.first) onValueChange(value - 1)
                },
                contentAlignment = Alignment.Center
            ){
                Text("−", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Box(modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable {
                    if (value < range.last) onValueChange(value + 1)
                },
                contentAlignment = Alignment.Center
            ){
                Text("+", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun TimerButton(
    label: String,
    gradient: List<Color>,
    onClick: () -> Unit
){
    Box(
        modifier = Modifier
            .height(56.dp)
            .widthIn(min = 120.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(gradient))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}