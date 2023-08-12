package com.hellow.notemiuiclone.database.reminderDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hellow.notemiuiclone.database.notesDatabase.NotesDao
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.converters.ReminderConverter

@Database(entities = [ReminderItem::class], version = 1, exportSchema = false)
@TypeConverters(ReminderConverter::class)
abstract class ReminderDatabase: RoomDatabase() {

        abstract fun reminderDao(): ReminderDao

        companion object {

            @Volatile
            private var instance: ReminderDatabase? = null
            private val LOCK = Any()

            // singleton pattern
            operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
                val INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ReminderDatabase::class.java,
                    "task_database.db")
                    .addTypeConverter(ReminderConverter())
                    .build()
                instance = INSTANCE

                instance
            }

        }

    }
