package com.hellow.notemiuiclone.models.noteModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ThemeItem(
    val editTextColor: String,
    val hintTextColor: String,
    val backGroundColor: String,
    val toolBarColor: String,
    val timeTextColor:String,
    val noteBackgroundColor:String
) : Parcelable