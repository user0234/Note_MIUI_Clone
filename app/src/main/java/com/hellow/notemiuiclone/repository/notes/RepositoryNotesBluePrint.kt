package com.hellow.notemiuiclone.repository.notes

import androidx.lifecycle.LiveData
import com.hellow.notemiuiclone.models.noteModels.NoteDataItem
import com.hellow.notemiuiclone.models.noteModels.ThemeItem

interface RepositoryNotesBluePrint {

    suspend fun createNote(note: NoteDataItem) : Long

    fun getNotes() :LiveData<List<NoteDataItem>?>

    suspend fun deleteNote(note: NoteDataItem)

    suspend fun updateNote(note: NoteDataItem)

    fun getTheme(num:Int,nightMode:Boolean): ThemeItem

    fun getAllTheme():List<ThemeItem>
}