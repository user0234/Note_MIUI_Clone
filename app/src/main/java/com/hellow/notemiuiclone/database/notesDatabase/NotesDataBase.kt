package com.hellow.notemiuiclone.database.notesDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hellow.notemiuiclone.models.noteModels.NoteConverter
import com.hellow.notemiuiclone.models.noteModels.NoteItem

@Database(entities = [NoteItem::class], version = 2, exportSchema = false)
@TypeConverters(NoteConverter::class)
abstract class NotesDataBase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    // code for single instance
    companion object {
        @Volatile
        private var instance: NotesDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(this) {
            val INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NotesDataBase::class.java,
                "notes_database.db"
            )
                .addTypeConverter(NoteConverter())
                .build()
            instance = INSTANCE

            instance
        }
    }
}