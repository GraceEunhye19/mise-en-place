package com.example.miseenplace.data.local

import androidx.room.TypeConverter
import com.example.miseenplace.data.model.TaskItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//for room to save the list as JSON string for dao

class TaskItemTypeConverter {
    private val gson = Gson()

    @TypeConverter  //serialize
    fun fromTaskItemList(items: List<TaskItem>): String {
        return gson.toJson(items)
    }

    @TypeConverter  //deserialize
    fun toTaskItemList(json: String): List<TaskItem> {
        val type = object : TypeToken<List<TaskItem>>() {}.type
        return gson.fromJson(json, type)
    }
}