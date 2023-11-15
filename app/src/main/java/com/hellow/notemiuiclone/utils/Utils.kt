package com.hellow.notemiuiclone.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {

    fun getTimer(value:Int):String{
         if (value < 10){
            return "0:0$value"
         }
        if(value <60){
            return "0:$value"
        }
        return "$value"
    }

    fun dateFormatterNotesList(date: String, currentTIme: String): String {
        val pattern = DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")
        val createdDateTime = LocalDateTime.parse(date, pattern)
        val currentDateTime = LocalDateTime.parse(currentTIme, pattern)
        val isTodayCreated: Boolean =
            createdDateTime.year == currentDateTime.year && createdDateTime.month == currentDateTime.month && createdDateTime.dayOfMonth == currentDateTime.dayOfMonth

        if (createdDateTime > currentDateTime) {
            return createdDateTime.format(pattern)
        }

        if (isTodayCreated) {
            val todayFormat = DateTimeFormatter.ofPattern("hh:mm a")
            return "Today  ${createdDateTime.format(todayFormat)}"
        }
        val isYesterdayCreated =
            createdDateTime.year == currentDateTime.year && createdDateTime.month == currentDateTime.month && createdDateTime.dayOfMonth == (currentDateTime.dayOfMonth - 1)
        if (isYesterdayCreated) {
            val todayFormat = DateTimeFormatter.ofPattern("hh:mm a")
            return "Yesterday  ${createdDateTime.format(todayFormat)}"
        }

        val dayOfTheWeekCreated =
            createdDateTime.year == currentDateTime.year && createdDateTime.month == currentDateTime.month && (currentDateTime.dayOfMonth - createdDateTime.dayOfMonth) < 7
        if (dayOfTheWeekCreated) {
            val todayFormat = DateTimeFormatter.ofPattern("EEE hh:mm a")
            return createdDateTime.format(todayFormat)
        }

        val isSameMonthCreated = createdDateTime.year == currentDateTime.year
        if (isSameMonthCreated) {
            val format = DateTimeFormatter.ofPattern("LLLL dd")
            return createdDateTime.format(format)
        }

        val format = DateTimeFormatter.ofPattern("LLLL dd , yyyy")
        return createdDateTime.format(format)
    }

    const val GreyColor: String = "#656565"
    const val YellowColor: String = "#E5B00D"

    val NOTE_ITEM_LIST = "NoteItemList"

}