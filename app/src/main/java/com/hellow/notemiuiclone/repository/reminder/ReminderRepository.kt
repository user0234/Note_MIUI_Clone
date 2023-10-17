package com.hellow.notemiuiclone.repository.reminder

import androidx.lifecycle.LiveData
import com.hellow.notemiuiclone.database.reminderDatabase.ReminderDatabase
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.ReminderStatus

class ReminderRepository(
    private val database: ReminderDatabase
) :RepositoryReminderBluePrint {
    override suspend fun addReminder(reminder: ReminderItem) {
        database.reminderDao().addReminder(reminder)
    }

    override suspend fun updateReminder(reminder: ReminderItem) {
       database.reminderDao().updateReminder(reminder)
    }

    override suspend fun deleteReminder(reminder: ReminderItem) {
        database.reminderDao().deleteReminder(reminder)
    }

    override fun getCompletedReminders(): LiveData<List<ReminderItem>?> {
       return database.reminderDao().getStatusReminder(ReminderStatus.DoneWhole)
    }

    override fun getUnCompletedReminders(): LiveData<List<ReminderItem>?> {
        return database.reminderDao().getStatusReminder(ReminderStatus.NotDone)
    }

    override fun getAllTheReminder(): LiveData<List<ReminderItem>?> {
       return database.reminderDao().getAllReminder()
    }


}