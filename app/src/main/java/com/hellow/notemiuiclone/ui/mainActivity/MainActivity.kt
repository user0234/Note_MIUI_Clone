package com.hellow.notemiuiclone.ui.mainActivity

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.NoteTabAdaptor
import com.hellow.notemiuiclone.database.AppDataBase
import com.hellow.notemiuiclone.databinding.ActivityMainBinding
import com.hellow.notemiuiclone.dialogs.CreateReminderDialog
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.noteModels.NoteDataItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.repository.reminder.ReminderRepository
import com.hellow.notemiuiclone.ui.editActivity.EditNoteActivity
import com.hellow.notemiuiclone.ui.fragment.HomeFragment
import com.hellow.notemiuiclone.ui.fragment.SettingsFragment
import com.hellow.notemiuiclone.utils.Utils
import com.hellow.notemiuiclone.utils.observeEvent
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// canvas code https://stackoverflow.com/questions/16650419/draw-in-canvas-by-finger-android
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    val viewModel: MainActivityViewModel by lazy {
        val appDataBase = AppDataBase(applicationContext)!!

        val notesRepository = NotesRepository(appDataBase)

        val reminderRepository = ReminderRepository(appDataBase)

        val viewModelProviderFactory =
            MainViewModelProviderFactory(application, notesRepository, reminderRepository)

        ViewModelProvider(this, viewModelProviderFactory)[MainActivityViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        openHomeFragment()
        setUpEventHandler()

    }

    private fun setUpEventHandler() {
        viewModel.handelOpenSettingsFragment.observeEvent(this) {

            openSettingsFragment()

        }
    }

    private fun openSettingsFragment() {

        val fragment = SettingsFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_view, fragment
            )

        fragmentTransaction.commit()

        fragmentTransaction.addToBackStack(fragment.toString())
    }

    private fun openHomeFragment() {

        val fragment = HomeFragment()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_view, fragment
            )

        fragmentTransaction.commit()

        fragmentTransaction.addToBackStack(fragment.toString())
    }
}