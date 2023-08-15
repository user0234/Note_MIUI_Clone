package com.hellow.notemiuiclone.database.reminderDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.ReminderStatus

@Dao
interface ReminderDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun addReminder(taskItem: ReminderItem)

        @Update
        suspend fun updateReminder(taskItem: ReminderItem)

        @Delete
        suspend fun deleteReminder(taskItem: ReminderItem)

        @Query("Select * From tasks Where reminderStatus =:status")
        fun getStatusReminder(status: ReminderStatus): LiveData<List<ReminderItem>?>

        @Query("Select * From tasks ORDER BY reminderStatus")
        fun getAllReminder(): LiveData<List<ReminderItem>?>

}