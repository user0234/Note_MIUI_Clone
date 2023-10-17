package com.hellow.notemiuiclone.repository.notes

import androidx.lifecycle.LiveData
import com.hellow.notemiuiclone.models.noteModels.NoteItem
import com.hellow.notemiuiclone.models.noteModels.ThemeItem

interface RepositoryNotesBluePrint {

    suspend fun createNote(note: NoteItem) : Long

    fun getNotes() :LiveData<List<NoteItem>?>

    suspend fun deleteNote(note: NoteItem)

    suspend fun updateNote(note: NoteItem)

    fun getTheme(num:Int,nightMode:Boolean): ThemeItem

    fun getAllTheme():List<ThemeItem>
}