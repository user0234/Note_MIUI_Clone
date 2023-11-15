package com.hellow.notemiuiclone.models.noteModels

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class NoteSubItem(
    var id:Int,
    var type:NoteSubItemType,
    var checkBox:Boolean = false, // false for all the types other then checkbox
    var textValue:String = "",
   // var textSize:Float = 0F,
  //  var isTextBold:Boolean = false,
    var imageUri:String? = null,
    var imageDescription:String? = null,
    var imageFileName:String? = null,
    var audioFileName:String? =null ,
    var audioFileUri:String? = null ,
    var audioLength:Long? = null

 ) : Parcelable
