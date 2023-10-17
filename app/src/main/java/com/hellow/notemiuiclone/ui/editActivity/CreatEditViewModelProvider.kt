package com.hellow.notemiuiclone.ui.editActivity

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hellow.notemiuiclone.models.noteModels.NoteItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository

class CreatEditViewModelProvider(val app: Application,
                                 private val repository: NotesRepository,
                                 private val currentItem: NoteItem
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreatEditViewModel(app ,repository,currentItem) as T
    }

}