package com.hellow.notemiuiclone.utils

import com.hellow.notemiuiclone.models.noteModels.ThemeItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

object ConstantValues {

    //BEGINNING-------------- Waveform visualisation constants ----------------------------------
    fun dateConvert(date: String): String {
        val pattern = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")
        val localDateTime = LocalDateTime.parse(date, pattern)
        return localDateTime.format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm a"))
            .toString()
    }

    val themeList:List<ThemeItem> = listOf(
        ThemeItem("#000000","#33000000","#F4E2E2","#FFFFFF","#7C7878"),
        ThemeItem("#634907","#33DC8B3B","#DAC2AB","#F4E1CE","#C5A280"),
        ThemeItem("#072963","#333B69DC","#ABBCDA","#CED9F4","#8097C5"),
        ThemeItem("#1B6307","#3353DC3B","#B0DAAB","#D5F4CE","#82C580"),
        ThemeItem("#630707","#33DC3B3B","#DAABAB","#F4CECE","#C58080"),
    )

    val darkModeTheme:ThemeItem = ThemeItem("#FFFFFF","#33FFFFFF","#000000","#333131","#736666")

    fun logI(value:String){

    }



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
        //		if (length < 0) {
//			length = -length;
//			long numMinutes = timeUnit.toMinutes(length);
//			long numSeconds = timeUnit.toSeconds(length);
//			return "-" + String.format(Locale.getDefault(), "%02d:%02d", numMinutes, numSeconds % 60);
//		} else {
        val numMinutes = timeUnit.toMinutes(length)
        val numSeconds = timeUnit.toSeconds(length)
        return String.format(Locale.getDefault(), "%02d:%02d", numMinutes, numSeconds % 60)
//		}
    }
}