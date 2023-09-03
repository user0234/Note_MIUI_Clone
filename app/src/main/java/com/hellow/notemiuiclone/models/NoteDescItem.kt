package com.hellow.notemiuiclone.models

import java.io.Serializable

data class NoteDescItem(
    var id:Int, // position of the item in rc view or note item
    var type:NoteDescItemType = NoteDescItemType.Text,
    var text:String? = "", // text for textType,text for checkBox type, image description for image type,audio Length for audio
    var isChecked:Boolean = false,
    var imageFile:String? = null,
    var audioLocation:String? = null,
) : Serializable
