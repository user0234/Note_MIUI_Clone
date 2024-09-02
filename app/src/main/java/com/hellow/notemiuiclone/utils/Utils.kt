package com.hellow.notemiuiclone.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {

    fun Uri.getName(context: Context): String? {
        val returnCursor = context.contentResolver.query(this, null, null, null, null)
        val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor?.moveToFirst()
        val fileName = nameIndex?.let { returnCursor.getString(it) }
        returnCursor?.close()
        return fileName
    }

    private fun getTimerSec(value: Int): String {
        if (value < 10) {
            return "0$value"
        }

        return "$value"

    }

    private fun getTimerSec(value: Long): String {
        if (value < 10) {
            return "0$value"
        }

        return "$value"

    }

    fun getTimer(value: Long): String {

        if (value < 60) {
            return "0:${getTimerSec(value)}"

        }

        return "${value / 60}:${getTimerSec(value % 60)}"

    }

    fun getTimer(value: Int): String {

        if (value < 60) {
            return "0:${getTimerSec(value)}"

        }

        return "${value / 60}:${getTimerSec(value % 60)}"

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

    const val NOTE_ITEM_LIST = "NoteItemList"

}