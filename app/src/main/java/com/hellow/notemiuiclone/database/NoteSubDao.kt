package com.hellow.notemiuiclone.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hellow.notemiuiclone.models.noteModels.NoteDataItem
import com.hellow.notemiuiclone.models.noteModels.NoteSubDataItem

@Dao
interface NoteSubDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteSubDataItem):Long

    @Query("Select * From Notes_sub")
    fun getAllSubNoteList(): LiveData<List<NoteSubDataItem>?>

    @Query("Select * From Notes_sub WHERE parentId =:id")
    fun getSubNoteForIdList(id:Int): LiveData<List<NoteSubDataItem>?>

    @Query("Select * From Notes_sub WHERE textValue =:query")
    fun getSubNoteForQueryList(query:String): LiveData<List<NoteSubDataItem>?>

    @Update
    suspend fun update(note: NoteSubDataItem)

    @Delete
    suspend fun delete(note: NoteSubDataItem)

}