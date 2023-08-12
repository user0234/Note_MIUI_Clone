package com.hellow.notemiuiclone.models

import java.io.Serializable

data class NoteDescItem(
    var type:NoteDescItemType = NoteDescItemType.Text,
    var text:String? = "",
    var isTick:Boolean = false,
    var imageFile:String? = null
) : Serializable
