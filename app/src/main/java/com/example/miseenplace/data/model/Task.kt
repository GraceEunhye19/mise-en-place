package com.example.miseenplace.data.model

import androidx.compose.ui.graphics.Color
data class Task(  //for database
    val id: Long = 0,
    val title: String,
    val items: List<TaskItem>,
    //val cardColor: androidx.compose.ui.graphics.Color,
    val cardColor: Color,
    val timestamp: Long = System.currentTimeMillis()
)