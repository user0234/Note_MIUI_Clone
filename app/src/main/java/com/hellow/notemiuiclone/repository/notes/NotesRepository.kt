package com.hellow.notemiuiclone.repository.notes

import androidx.lifecycle.LiveData
import com.hellow.notemiuiclone.database.notesDatabase.NotesDataBase
import com.hellow.notemiuiclone.models.NoteItem
import com.hellow.notemiuiclone.models.NoteStatus

class NotesRepository(
    private val database: NotesDataBase
) : RepositoryNotesBluePrint {


    override suspend fun addNote(note: NoteItem) {
        database.notesDao().addNote(note)
    }

    override suspend fun updateNote(note: NoteItem) {
        database.notesDao().updateNote(note)
    }

    override suspend fun deleteNote(note: NoteItem) {
        database.notesDao().deleteNote(note)
     }

    override fun getAliveNotes(): LiveData<List<NoteItem>?> {
        return database.notesDao().getStatusNote(NoteStatus.Alive)
    }

    override fun getDeletedNotes(): LiveData<List<NoteItem>?> {
      return  database.notesDao().getStatusNote(NoteStatus.Deleted)
    }

    override fun getArchivedNotes(): LiveData<List<NoteItem>?> {
       return database.notesDao().getStatusNote(NoteStatus.Archive)
    }

}