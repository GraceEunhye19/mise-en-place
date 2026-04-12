package com.example.miseenplace.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.StickyNote2
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNav(
    val route: String,
    val label: String,
    val icon: ImageVector
){
    object Converter: BottomNav("converter", "Unit Converter", Icons.Outlined.SwapVert)
    object Notes: BottomNav("notes", "Notes", Icons.AutoMirrored.Outlined.StickyNote2)
    object Timer: BottomNav("timer", "Timer", Icons.Outlined.Timer)

}