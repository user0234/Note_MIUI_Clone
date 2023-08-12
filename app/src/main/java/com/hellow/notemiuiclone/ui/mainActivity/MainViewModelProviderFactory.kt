package com.hellow.notemiuiclone.ui.mainActivity

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.repository.reminder.ReminderRepository

class MainViewModelProviderFactory(
    val app: Application,
    private val notesRepository: NotesRepository,
    private val reminderRepository: ReminderRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MainActivityViewModel(app,notesRepository,reminderRepository) as T
    }
}