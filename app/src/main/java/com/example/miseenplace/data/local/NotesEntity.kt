package com.example.miseenplace.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val body: String,
    val cardColorValue: Long,
    val timestamp: Long = System.currentTimeMillis()
)