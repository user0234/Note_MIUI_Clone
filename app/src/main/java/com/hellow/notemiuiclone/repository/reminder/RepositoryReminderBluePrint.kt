package com.hellow.notemiuiclone.repository.reminder

import androidx.lifecycle.LiveData
import com.hellow.notemiuiclone.models.ReminderItem

interface RepositoryReminderBluePrint {

    suspend fun  addReminder(reminder: ReminderItem)

    suspend fun updateReminder(reminder: ReminderItem)

    suspend fun deleteReminder(reminder: ReminderItem)

    fun getCompletedReminders(): LiveData<List<ReminderItem>?>

    fun getUnCompletedReminders(): LiveData<List<ReminderItem>?>

    fun getAllTheReminder(): LiveData<List<ReminderItem>?>

}