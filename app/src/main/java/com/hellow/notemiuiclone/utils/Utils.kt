package com.hellow.notemiuiclone.utils

import com.hellow.notemiuiclone.models.ThemeItemData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
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

    const val GreyColor: String = "#3B3B3A"
    const val YellowColor: String = "#5BFFE819"

    fun titleColor(id: Int): String {

        return "#000000"
    }

    fun backgroundColor(id: Int): String {

        return "#ffffff"
    }

    fun subTitleColor(id: Int): String {
        return "#3d3d3d"
    }

    fun toolBarColor(id: Int): String {
        return "#ababab"
    }

    val NOTE_ITEM_LIST = "NoteItemList"
    val NOTE_ITEM_CREATE = "NoteItemCreate"

    val themeListData: List<ThemeItemData> = listOf<ThemeItemData>(
        ThemeItemData("#F4E2E2", "#000000", "#33000000"),
        ThemeItemData("#ABBCDA", "#072963", "#333B69DC"),

        )

    fun getStatusTextColor(value: Boolean): String {
        if (!value) {
            return "#111111"
        }
        return "#656561"
    }
}