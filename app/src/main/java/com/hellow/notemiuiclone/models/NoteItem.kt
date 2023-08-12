package com.hellow.notemiuiclone.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity(tableName = "notes")
data class NoteItem(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val idDate: String,
    var title: String = "",
    var folderId: String = "00",
    var noteDescription: List<NoteDescItem> = emptyList(),
    var themeId: Int = 0,
    var noteStatus: NoteStatus = NoteStatus.Alive,
    var recentChangeDate: String,
) : Serializable

