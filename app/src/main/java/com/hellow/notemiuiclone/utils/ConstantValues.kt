package com.hellow.notemiuiclone.utils

import android.content.Context
import android.content.res.Configuration
import com.hellow.notemiuiclone.models.noteModels.ThemeItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

object ConstantValues {

    fun getThemeNoteList(context:Context,themeId:Int):ThemeItem{
        return when (context.applicationContext.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
               return  if(themeId == 0){
                     darkModeTheme
                 }else{
                   themeList[themeId]
                 }
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                themeList[themeId]
            }

            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                themeList[themeId]
            }

            else -> {
                themeList[themeId]
            }
        }
    }
    fun getGreyValue(context:Context):String {
       return when (context.applicationContext.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                 "#626464"
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                "#AEAEAE"
            }

            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                "#AEAEAE"
            }

            else -> {
               "#AEAEAE"
            }
        }
    }

    fun getBlackValue(context:Context):String {
        return when (context.applicationContext.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                "#CBCBCB"
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                "#303030"
            }

            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                "#303030"
            }

            else -> {
                "#303030"
            }
        }
    }

    //BEGINNING-------------- Waveform visualisation constants ----------------------------------
    fun dateConvert(date: String): String {
        val pattern = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")
        val localDateTime = LocalDateTime.parse(date, pattern)
        return localDateTime.format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm a"))
            .toString()
    }

    val themeList:List<ThemeItem> = listOf(
        ThemeItem("#202020","#D1D1D1","#FEFEFE","#FCFCF9","#989898","#FEFEFE"), // default
        ThemeItem("#A07B22","#E3DCBD","#F4F6E5","#FCFDF1","#CFC793","#EFE9D0"), // yellow
        ThemeItem("#4B87C2","#CCDEEF","#E8F4F8","#F0F9FB","#AACAE6","#E8F4F8"), // blue
        ThemeItem("#629F70","#D3EBD5","#EEFDED","#F2FDF2","#B4D5BA","#DBF2DF"), // green
        ThemeItem("#CF5851","#F1D7CD","#FBF2EC","#FCF7F4","#EDB1AF","#F5DFD9"), // red
    )

    private val darkModeTheme:ThemeItem = ThemeItem("#DCDCDC","#2B2B2B","#000000","#141414","#626262","#222222")

    fun getNightModeTheme(num: Int):ThemeItem {
        return if(num == 0){
            darkModeTheme
        }else{
            themeList[num]
        }
    }
    fun getLightModeTheme(num: Int):ThemeItem {
        return themeList[num]
    }

    const val SHORT_RECORD_DP_PER_SECOND = 25

    fun formatTimeIntervalMinSec(length: Long): String {
        val timeUnit = TimeUnit.MILLISECONDS
        val numMinutes = timeUnit.toMinutes(length)
        val numSeconds = timeUnit.toSeconds(length)
        return String.format(Locale.getDefault(), "%02d:%02d", numMinutes, numSeconds % 60)

    }
}