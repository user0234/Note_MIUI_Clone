package com.hellow.notemiuiclone.repository.notes

import androidx.lifecycle.LiveData
import com.hellow.notemiuiclone.models.NoteItem

interface RepositoryNotesBluePrint {

    suspend fun  addNote(note:NoteItem)

    suspend fun updateNote(note: NoteItem)

    suspend fun deleteNote(note: NoteItem)

    fun getAliveNotes():LiveData<List<NoteItem>?>

    fun getDeletedNotes():LiveData<List<NoteItem>?>

    fun getArchivedNotes():LiveData<List<NoteItem>?>
}