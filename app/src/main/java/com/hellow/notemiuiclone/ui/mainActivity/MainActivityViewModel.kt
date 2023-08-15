package com.hellow.notemiuiclone.ui.mainActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hellow.notemiuiclone.models.NoteItem
import com.hellow.notemiuiclone.models.NoteStatus
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.repository.reminder.ReminderRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(
    app:Application,
    private val notesRepository: NotesRepository,
    private val reminderRepository: ReminderRepository
): AndroidViewModel(app)  {

   private var _completeShownTasks = false
    fun setCompleteShowInTasks(value: Boolean){
        _completeShownTasks = value
    }

    fun getCompleteShowInTasks():Boolean {
        return _completeShownTasks
    }

    private var _tabItemSelected = MutableLiveData<Boolean>(true)
    val tabItemSelectedLiveData: LiveData<Boolean>
        get() = _tabItemSelected

    fun setTabItem(value:Boolean){
        _tabItemSelected.value = value
    }


   fun saveNote (note:NoteItem) = viewModelScope.launch {
        notesRepository.addNote(note)
   }

    fun getNotes() = notesRepository.getAliveNotes()

    fun deleteNote(note:NoteItem) {
        note.noteStatus = NoteStatus.Deleted
        viewModelScope.launch {
            notesRepository.updateNote(note)
        }
    }

    fun archiveNote(note:NoteItem) {
        note.noteStatus = NoteStatus.Archive
        viewModelScope.launch {
            notesRepository.updateNote(note)
        }
    }
    fun createNewReminder(reminder:ReminderItem) {
        viewModelScope.launch {

            reminderRepository.addReminder(reminder)
        }
    }

    fun updateReminder(reminder:ReminderItem) {
        viewModelScope.launch{
            reminderRepository.updateReminder(reminder)
        }
    }

    fun deleteReminder(reminder: ReminderItem) {
        viewModelScope.launch {
            reminderRepository.deleteReminder(reminder)
        }
    }

    fun getCompletedReminders() = reminderRepository.getCompletedReminders()

    fun getInCompleterReminders() = reminderRepository.getUnCompletedReminders()

    fun getAllReminder() = reminderRepository.getAllTheReminder()
}