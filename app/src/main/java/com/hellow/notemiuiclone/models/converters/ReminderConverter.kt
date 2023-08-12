package com.hellow.notemiuiclone.models.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hellow.notemiuiclone.models.ReminderStatus
import com.hellow.notemiuiclone.models.ReminderSubItem

    @ProvidedTypeConverter
    class ReminderConverter {
        private val gson = Gson()

        @TypeConverter
        fun fromListTaskItem(value: List<ReminderSubItem>): String {
            return gson.toJson(value)
        }

        @TypeConverter
        fun toListTaskItem(value: String): List<ReminderSubItem> {
            return gson.fromJson(value, object : TypeToken<List<ReminderSubItem>>() {
            }.type)
        }

        @TypeConverter
        fun fromNoteStatus(value: ReminderStatus): String {
            return value.name
        }

        @TypeConverter
        fun toNoteStatus(value: String): ReminderStatus {
            return ReminderStatus.valueOf(value)
        }

    }
