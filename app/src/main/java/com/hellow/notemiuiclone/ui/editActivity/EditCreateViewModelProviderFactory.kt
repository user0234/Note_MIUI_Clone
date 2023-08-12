package com.hellow.notemiuiclone.ui.editActivity

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hellow.notemiuiclone.repository.notes.NotesRepository

class EditCreateViewModelProviderFactory(
    val app: Application,
    private val repository: NotesRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return EditCreateViewModel(app, repository) as T
    }
}