package com.hellow.notemiuiclone.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hellow.notemiuiclone.models.noteModels.NoteDataItem

@Dao
interface NotesDao {

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insert(note:NoteDataItem):Long

     @Query("Select * From Notes")
     fun getNotesList(): LiveData<List<NoteDataItem>?>

     @Update
     suspend fun update(note: NoteDataItem)

     @Delete
     suspend fun delete(note: NoteDataItem)
}