package com.hellow.notemiuiclone.ui.editActivity1

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hellow.notemiuiclone.models.noteModels.NoteItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository

class EditCreateViewModelProviderFactory(
    val app: Application,
    private val repository: NotesRepository,
    private val currentItem: NoteItem
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return EditCreateViewModel(app, repository,currentItem) as T
    }
}