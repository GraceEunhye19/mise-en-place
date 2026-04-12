package com.example.miseenplace.features.notes


import androidx.lifecycle.ViewModel
import com.example.miseenplace.data.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class NotesViewModel: ViewModel(){

    private val _notes = MutableStateFlow<List<Note>>(emptyList())  //start empty, saved notes can be edited
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()  //the public read-only version for the ui to display, used when needed for safety
    var showEditor = MutableStateFlow(false) //start closed

    var draftText = MutableStateFlow("")

    var editingNote = MutableStateFlow<Note?>(null) //can be new(null) or an already existing one

    fun openNewNote(){
        editingNote.value = null //no notes
        showEditor.value = true //editor open
    }

    fun openExistingNote(note: Note){
        editingNote.value = note //shows note
        showEditor.value = true //editor open
    }

    fun saveNote(body: String){
        draftText.value = ""
        if(body.isBlank()){
            showEditor.value = false //closes the editor if it's blank
            return
        }
        val existing = editingNote.value
        if( existing != null ){
            _notes.value = _notes.value.map {
                if (it.id == existing.id) it.copy(body = body) else it
            }
        } else {
            //val color = noteColors[(_notes.value.size)%noteColors.size] //pick one color and loop back
            val color = noteCardColor
            _notes.value += Note(body = body, cardColor = color) //add to list with the chosen color
        }
        editingNote.value = null
        showEditor.value = false
    }

    fun deleteNote(note: Note){
        _notes.value = _notes.value.filter{it.id != note.id}
    }

    fun closeEditor(){
        draftText.value = ""
        showEditor.value = false //editor close
        editingNote.value = null //no notes
    }
}