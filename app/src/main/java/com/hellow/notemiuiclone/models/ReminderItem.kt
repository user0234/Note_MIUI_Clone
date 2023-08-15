package com.hellow.notemiuiclone.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity(tableName = "Tasks")
data class ReminderItem(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val idDate: String,


    var title: String,
    val itemsList: List<ReminderSubItem>,
    var reminderStatus: ReminderStatus,
    var TimerTime: String,
    var isExpended:Boolean = false,
    var checkedCount:Int = 0,
) : Serializable