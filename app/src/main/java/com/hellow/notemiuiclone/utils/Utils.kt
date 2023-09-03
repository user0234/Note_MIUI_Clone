package com.hellow.notemiuiclone.utils

import com.hellow.notemiuiclone.models.ThemeItemData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
    fun dateConvert(date: String): String {
        val pattern = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")
        val localDateTime = LocalDateTime.parse(date, pattern)
        return localDateTime.format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm a"))
            .toString()

    }

    fun titleColor(id: Int):String {

        return "#000000"
    }

    fun backgroundColor(id:Int):String{

        return "#ffffff"
    }

    fun subTitleColor(id: Int):String{
        return "#3d3d3d"
    }

    fun toolBarColor(id: Int):String {
         return "#ababab"
    }

    val NOTE_ITEM_LIST = "NoteItemList"
    val NOTE_ITEM_CREATE = "NoteItemCreate"

    val themeListData:List<ThemeItemData> = listOf<ThemeItemData>(
        ThemeItemData("#F4E2E2","#000000","#33000000"),
        ThemeItemData( "#ABBCDA","#072963","#333B69DC"),

    )

    fun getStatusTextColor(value:Boolean):String{
        if(value){
            return "#111111"
        }

        return "#656561"
    }

    fun getImage(imageFile: String?): String {
       return ""
    }
}