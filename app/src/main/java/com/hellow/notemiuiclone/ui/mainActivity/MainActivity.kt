package com.hellow.notemiuiclone.ui.mainActivity

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.NoteTabAdaptor
import com.hellow.notemiuiclone.database.notesDatabase.NotesDataBase
import com.hellow.notemiuiclone.database.reminderDatabase.ReminderDatabase
import com.hellow.notemiuiclone.databinding.ActivityMainBinding
import com.hellow.notemiuiclone.dialogs.CreateReminderDialog
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.noteModels.NoteItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.repository.reminder.ReminderRepository
import com.hellow.notemiuiclone.ui.editActivity.CreatEditActivity
import com.hellow.notemiuiclone.utils.Utils
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// canvas code https://stackoverflow.com/questions/16650419/draw-in-canvas-by-finger-android
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel
    var isTabSelected: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val notesRepository = NotesRepository(NotesDataBase(this)!!)

        val reminderRepository = ReminderRepository(ReminderDatabase(this)!!)

        val viewModelProviderFactory =
            MainViewModelProviderFactory(application, notesRepository, reminderRepository)

        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[MainActivityViewModel::class.java]

        viewModel.tabItemSelectedLiveData.observe(this) {
            isTabSelected = it
        }

        val adapter = NoteTabAdaptor(this, 2)
        viewBinding.viewPager.adapter = adapter

        TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.note_icon_yellow)
                }

                1 -> {
                    tab.setIcon(R.drawable.task_icon)
                }

                else -> {
                    tab.setIcon(R.drawable.note_icon_yellow)
                }
            }
        }.attach()


        viewBinding.fabCreate.setOnClickListener {

            if (viewBinding.viewPager.currentItem == 0) {
                val intent = Intent(this, CreatEditActivity::class.java)
                val noteId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")).toString()
                val note = NoteItem(noteId, recentChangeDate = noteId)
                viewModel.addNote(note)
                intent.putExtra(Utils.NOTE_ITEM_LIST,note)
                startActivity(intent)
            } else {
                // create new reminder item using dialogue
                //   showCreateDialog()
                showCreateDialog2()
            }

        }
    }

    private fun showCreateDialog2() {
        val reminderDialog = object : CreateReminderDialog(
            this@MainActivity,

            ) {
            override fun onItemDone(item: ReminderItem?,time: LocalDateTime) {
                if (item != null) {

                    val startsTime = LocalDateTime.now()

                    val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val mStartTime = mSimpleDateFormat.parse(time.toString())
                    val mEndTime = mSimpleDateFormat.parse(time.toString())

                    val intent = Intent(Intent.ACTION_INSERT).apply {
                        data = CalendarContract.Reminders.CONTENT_URI
                        putExtra(CalendarContract.Reminders.TITLE, title)
                        putExtra(CalendarContract.Reminders.ALL_DAY, false)
                        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, mEndTime )
                        startActivity(intent)
                    }
                    // TODO set an alarm for that time
                    viewModel.createNewReminder(item)
                }

            }

        }
        reminderDialog.show()
    }


    private fun createSnackBar(value: String) {
        Snackbar.make(viewBinding.root, value, Snackbar.LENGTH_LONG).apply {
            show()
        }
    }

}