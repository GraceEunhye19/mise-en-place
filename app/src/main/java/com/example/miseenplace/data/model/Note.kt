package com.example.miseenplace.data.model

import androidx.compose.ui.graphics.Color

data class Note(
    val id: Long = 0,
    //val id: Long = System.currentTimeMillis(),
    val body: String,
    val cardColor: Color
)
