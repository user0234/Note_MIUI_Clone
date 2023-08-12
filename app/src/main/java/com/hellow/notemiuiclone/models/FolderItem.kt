package com.hellow.notemiuiclone.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class FolderItem(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val idDate:String,
    val name:String,
  //  val notesList:List<String>,
    ):Serializable