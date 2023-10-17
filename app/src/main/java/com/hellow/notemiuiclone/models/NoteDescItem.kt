package com.hellow.notemiuiclone.models

import android.net.Uri
import java.io.Serializable

data class NoteDescItem(
    var id: Int, // position of the item in List
    var type: NoteDescItemType = NoteDescItemType.Text,
    var text: String = "", // text for textType,text for checkBox type, image description for image type,audio Length for audio
    var isChecked: Boolean = false,
    var imageDescriptionText: String = "",
    var audioText: String = "",
    var imageFile: Uri? = null,
    var audioFile: Uri? = null,
) : Serializable
