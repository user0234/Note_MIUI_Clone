package com.hellow.notemiuiclone.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hellow.notemiuiclone.models.noteModels.NoteItem

@Dao
interface NotesDao {

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insert(note:NoteItem):Long

     @Query("Select * From Notes")
     fun getNotesList(): LiveData<List<NoteItem>?>

     @Update
     suspend fun update(note: NoteItem)

     @Delete
     suspend fun delete(note: NoteItem)
}