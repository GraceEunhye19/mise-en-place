package com.example.miseenplace.data.repo

import androidx.compose.ui.graphics.Color
import com.example.miseenplace.data.local.NoteDao
import com.example.miseenplace.data.local.NotesEntity
import com.example.miseenplace.data.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//birdge between dao and viewmodel
class NoteRepo(private val noteDao: NoteDao){

    val allNotes: Flow<List<Note>> = noteDao.getAllNotes().map{entities ->
        entities.map{ entity ->
            Note(
                entity.id,
                entity.body,
                Color(entity.cardColorValue.toULong())
            )
        }
    }

    suspend fun insertNote(note: Note){
        noteDao.insertNote(
            NotesEntity(
                note.id,
                note.body,
                note.cardColor.value.toLong()
            )
        )
    }

    suspend fun updateNote(note: Note){
        noteDao.updateNote(
            NotesEntity(
                note.id,
                note.body,
                note.cardColor.value.toLong()
            )
        )
    }

    suspend fun deleteNote(note: Note){
        noteDao.deleteNote(
            NotesEntity(
                note.id,
                note.body,
                note.cardColor.value.toLong()
            )
        )
    }
}