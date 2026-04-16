package com.example.miseenplace.data.local

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.miseenplace.data.model.TaskItem

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val itemsJson: String,
    val cardColorValue: Long,
    val timestamp: Long = System.currentTimeMillis()
)