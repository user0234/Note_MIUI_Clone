package com.hellow.notemiuiclone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.converters.DataBaseConverters
import com.hellow.notemiuiclone.models.noteModels.NoteDataItem
import com.hellow.notemiuiclone.models.noteModels.NoteSubDataItem


@Database(
    entities = [ReminderItem::class, NoteDataItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataBaseConverters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao
    abstract fun notesDao(): NotesDao


    companion object {

        @Volatile
        private var instance: AppDataBase? = null
        private val LOCK = Any()

        // singleton pattern
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            val INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "app_database.db"
            )
                .addTypeConverter(DataBaseConverters())
                .build()
            instance = INSTANCE

            instance
        }

    }

}
