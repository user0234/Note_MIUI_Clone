package com.hellow.notemiuiclone.ui.mainActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hellow.notemiuiclone.models.noteModels.NoteDataItem
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.repository.reminder.ReminderRepository
import com.hellow.notemiuiclone.ui.editActivity.CreatEditViewModel.DescriptionText
import com.hellow.notemiuiclone.utils.Event
import com.hellow.notemiuiclone.utils.send
import kotlinx.coroutines.launch

class MainActivityViewModel(
    app: Application,
    private val notesRepository: NotesRepository,
    private val reminderRepository: ReminderRepository
) : AndroidViewModel(app) {

    private var _completeShownTasks = false
    fun setCompleteShowInTasks(value: Boolean) {
        _completeShownTasks = value
    }

    fun getCompleteShowInTasks(): Boolean {
        return _completeShownTasks
    }

    private var _tabItemSelected = MutableLiveData<Boolean>(true)
    val tabItemSelectedLiveData: LiveData<Boolean>
        get() = _tabItemSelected

    // open settings fragment


    private val _openSettingsFragment = MutableLiveData<Event<Boolean>>()
    val handelOpenSettingsFragment: LiveData<Event<Boolean>>
        get() = _openSettingsFragment

    fun openSettingsFragment() {
        _openSettingsFragment.send(true)

    }


    fun addNote(note: NoteDataItem) = viewModelScope.launch {
        notesRepository.createNote(note)
    }

    fun getNotes() = notesRepository.getNotes()

    fun deleteNote(note: NoteDataItem) {
        // note.noteStatus = NoteStatus.Deleted
        viewModelScope.launch {
            notesRepository.deleteNote(note)
        }
    }

    fun archiveNote(note: NoteDataItem) {
        viewModelScope.launch {
            notesRepository.updateNote(note)
        }
    }

    fun createNewReminder(reminder: ReminderItem) {
        viewModelScope.launch {

            reminderRepository.addReminder(reminder)
        }
    }

    fun updateReminder(reminder: ReminderItem) {
        viewModelScope.launch {
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