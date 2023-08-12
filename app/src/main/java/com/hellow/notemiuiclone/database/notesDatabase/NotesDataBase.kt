package com.hellow.notemiuiclone.database.notesDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hellow.notemiuiclone.models.NoteItem
import com.hellow.notemiuiclone.models.converters.NoteConverter

@Database(entities = [NoteItem::class], version = 1, exportSchema = false)
@TypeConverters(NoteConverter::class)
abstract class NotesDataBase : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {

        @Volatile
        private var instance: NotesDataBase? = null
        private val LOCK = Any()

        // singleton pattern
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            val INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NotesDataBase::class.java,
                "notes_database.db")
                .addTypeConverter(NoteConverter())
                .build()
            instance = INSTANCE

            instance
        }

    }

}