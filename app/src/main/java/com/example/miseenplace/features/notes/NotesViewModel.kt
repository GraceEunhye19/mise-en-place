package com.example.miseenplace.features.notes


import android.app.Application
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miseenplace.data.local.MiseEnPlaceDatabase
import com.example.miseenplace.data.model.Note
import com.example.miseenplace.data.model.Task
import com.example.miseenplace.data.model.TaskItem
import com.example.miseenplace.data.repo.NoteRepo
import com.example.miseenplace.data.repo.TaskRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class NoteOrTask{  //i want it to show in the grid together
    data class NoteItem(val note: Note, val timestamp: Long): NoteOrTask()
    data class TaskItem(val task: Task, val timestamp: Long): NoteOrTask()
}
class NotesViewModel(application: Application): AndroidViewModel(application){
    private val db = MiseEnPlaceDatabase.getInstance(application)
    private val noteRepo = NoteRepo(db.noteDao())
    private val taskRepo = TaskRepo(db.taskDao())

    val allItems: StateFlow<List<NoteOrTask>> = combine(
        noteRepo.allNotes,
        taskRepo.allTasks
    ){ notes, tasks ->
        val noteItems = notes.map{ NoteOrTask.NoteItem(it, it.id) }
        val taskItems = tasks.map{ NoteOrTask.TaskItem(it, it.id) }
        (noteItems + taskItems).sortedByDescending {
            when (it){
                is NoteOrTask.NoteItem -> it.timestamp
                is NoteOrTask.TaskItem -> it.timestamp
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isFabExpanded = MutableStateFlow(false)
    val isFabExpanded: StateFlow<Boolean> = _isFabExpanded.asStateFlow()

    //note editor state holder
    val showNoteEditor = MutableStateFlow(false)
    val editingNote = MutableStateFlow<Note?>(null)
    var draftText = MutableStateFlow("")

    //task editor state holder
    val showTaskEditor = MutableStateFlow(false)
    val editingTask = MutableStateFlow<Task?>(null)

    fun toggleFab(){
        _isFabExpanded.value = !_isFabExpanded.value
    }

    fun collapseFab(){
        _isFabExpanded.value = false
    }

    fun openNewNote(){
        collapseFab()
        editingNote.value = null //no notes
        draftText.value = ""
        showNoteEditor.value = true //editor open
    }

    fun openExistingNote(note: Note){
        draftText.value = note.body
        editingNote.value = note //shows note
        showNoteEditor.value = true //editor open
    }

    fun saveNote(body: String) {
        if (body.isBlank()) {
            showNoteEditor.value = false //closes the editor if it's blank
            return
        }
        viewModelScope.launch {
            val existing = editingNote.value
            if( existing != null ){
                noteRepo.updateNote((existing.copy(body = body)))
            } else {
                //val color = noteColors[(_notes.value.size)%noteColors.size] //pick one color and loop back
                val color = noteCardColor
                noteRepo.insertNote(Note(body = body, cardColor = color)) //add to list with the same color
            }
            draftText.value = ""
            editingNote.value = null
            showNoteEditor.value = false
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            noteRepo.deleteNote(note)
        }
    }

    fun closeNoteEditor(){
        draftText.value = ""
        showNoteEditor.value = false //editor close
        editingNote.value = null //no notes
    }

    fun openNewTask(){
        collapseFab()
        editingTask.value = null //no tasks
        showTaskEditor.value = true //editor open
    }

    fun openExistingTask(task: Task){
        editingTask.value = task
        showTaskEditor.value = true //editor open
    }

    fun saveTask(title: String, items: List<TaskItem>) {
        if (title.isBlank() && items.all{it.text.isBlank()}) {
            showTaskEditor.value = false //closes the editor if it's blank
            return
        }
        viewModelScope.launch {
            val existing = editingTask.value
            if( existing != null ){
                taskRepo.updateTask(
                    (existing.copy(title = title, items= items.filter { it.text.isNotBlank() })))
            } else {
                val color = taskCardColor
                taskRepo.insertTask(
                    Task(
                        title = title,
                        items= items.filter { it.text.isNotBlank()},
                        cardColor = color))//add to list with the same color
            }
            //draftText.value = ""
            editingNote.value = null
            showNoteEditor.value = false
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch {
            taskRepo.deleteTask(task)
        }
    }

    fun closeTaskEditor(){
        //draftText.value = ""
        showTaskEditor.value = false //editor close
        editingTask.value = null //no notes
    }
}

//class NotesViewModel: ViewModel(){
//
//    private val _notes = MutableStateFlow<List<Note>>(emptyList())  //start empty, saved notes can be edited
//    val notes: StateFlow<List<Note>> = _notes.asStateFlow()  //the public read-only version for the ui to display, used when needed for safety
//    var showEditor = MutableStateFlow(false) //start closed
//
//    var draftText = MutableStateFlow("")
//
//    var editingNote = MutableStateFlow<Note?>(null) //can be new(null) or an already existing one
//
//    fun openNewNote(){
//        editingNote.value = null //no notes
//        showEditor.value = true //editor open
//    }
//
//    fun openExistingNote(note: Note){
//        editingNote.value = note //shows note
//        showEditor.value = true //editor open
//    }
//
//    fun saveNote(body: String){
//        draftText.value = ""
//        if(body.isBlank()){
//            showEditor.value = false //closes the editor if it's blank
//            return
//        }
//        val existing = editingNote.value
//        if( existing != null ){
//            _notes.value = _notes.value.map {
//                if (it.id == existing.id) it.copy(body = body) else it
//            }
//        } else {
//            //val color = noteColors[(_notes.value.size)%noteColors.size] //pick one color and loop back
//            val color = noteCardColor
//            _notes.value += Note(body = body, cardColor = color) //add to list with the chosen color
//        }
//        editingNote.value = null
//        showEditor.value = false
//    }
//
//    fun deleteNote(note: Note){
//        _notes.value = _notes.value.filter{it.id != note.id}
//    }
//
//    fun closeEditor(){
//        draftText.value = ""
//        showEditor.value = false //editor close
//        editingNote.value = null //no notes
//    }
//}