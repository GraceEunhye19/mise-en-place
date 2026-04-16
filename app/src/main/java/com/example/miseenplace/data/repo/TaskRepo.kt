package com.example.miseenplace.data.repo

import androidx.compose.ui.graphics.Color
import com.example.miseenplace.data.local.TaskDao
import com.example.miseenplace.data.local.TaskEntity
import com.example.miseenplace.data.model.Task
import com.example.miseenplace.data.model.TaskItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//also does its own json conversion cuz the taskitemtypeconversion is only for the dao layer
class TaskRepo (private val taskDao: TaskDao){

    private val gson = Gson()

    val allTasks: Flow<List<Task>> = taskDao.getAllTasks().map { entities ->
        entities.map { entity ->
            val type = object : TypeToken<List<TaskItem>>() {}.type
            Task(
                entity.id,
                entity.title,
                gson.fromJson(entity.itemsJson, type),
                Color(entity.cardColorValue.toULong()),
                entity.timestamp
            )
        }
    }

    private fun Task.toEntity() = TaskEntity(
        id,
        title,
        gson.toJson(items),
        cardColor.value.toLong(),
        timestamp
    )

    suspend fun insertTask(task: Task){
        taskDao.insertTask(task.toEntity())
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task.toEntity())
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task.toEntity())
    }

}