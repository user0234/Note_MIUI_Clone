package com.hellow.notemiuiclone.models.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hellow.notemiuiclone.models.NoteDescItem
import com.hellow.notemiuiclone.models.NoteStatus

@ProvidedTypeConverter
class NoteConverter {

    private val gson = Gson()
    @TypeConverter
    fun fromListNoteDescription(value: List<NoteDescItem>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toListNoteDescription(value: String): List<NoteDescItem> {
        return gson.fromJson(value, object : TypeToken<List<NoteDescItem>>() {
        }.type)
    }

    @TypeConverter
    fun fromNoteStatus(value: NoteStatus): String {
        return value.name
    }

    @TypeConverter
    fun toNoteStatus(value: String): NoteStatus {
        return NoteStatus.valueOf(value)
    }

}