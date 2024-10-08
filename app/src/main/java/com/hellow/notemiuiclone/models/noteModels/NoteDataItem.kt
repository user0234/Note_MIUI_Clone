package com.hellow.notemiuiclone.models.noteModels

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Notes")
data class NoteDataItem(
    @PrimaryKey()
    val id: String,
    var title: String = "",
    var descriptionText: String = "",
    var description: List<NoteSubDataItem> = listOf(
        NoteSubDataItem(
            0,
            NoteSubItemType.String,
            false,
            ""
        )
    ),
    var themeId: Int = 0,
    var priorityColor: Int = 0,
    var recentChangeDate: String
) : Parcelable


