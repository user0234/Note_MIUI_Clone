package com.hellow.notemiuiclone.database.notesDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hellow.notemiuiclone.models.NoteItem
import com.hellow.notemiuiclone.models.NoteStatus

@Dao
interface NotesDao {

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun addNote(noteItem:NoteItem)

     @Update
     suspend fun updateNote(noteItem: NoteItem)

     @Delete
     suspend fun deleteNote(noteItem: NoteItem)

     @Query("Select * From notes Where noteStatus =:status")
     fun getStatusNote(status:NoteStatus):LiveData<List<NoteItem>?>

     @Query("Select * From notes")
     fun getAllNotes():LiveData<List<NoteItem>?>
}