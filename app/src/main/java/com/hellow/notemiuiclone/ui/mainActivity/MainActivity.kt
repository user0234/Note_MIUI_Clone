package com.hellow.notemiuiclone.ui.mainActivity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.NoteTabAdaptor
import com.hellow.notemiuiclone.database.notesDatabase.NotesDataBase
import com.hellow.notemiuiclone.database.reminderDatabase.ReminderDatabase
import com.hellow.notemiuiclone.databinding.ActivityMainBinding
import com.hellow.notemiuiclone.models.NoteItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.repository.reminder.ReminderRepository
import com.hellow.notemiuiclone.ui.editActivity.EditCreateActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// canvas code https://stackoverflow.com/questions/16650419/draw-in-canvas-by-finger-android
class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel
    var isTabSelected:Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val notesRepository = NotesRepository(NotesDataBase(this)!!)

        val reminderRepository = ReminderRepository(ReminderDatabase(this)!!)

        val viewModelProviderFactory = MainViewModelProviderFactory(application,notesRepository,reminderRepository)

        viewModel = ViewModelProvider(this,viewModelProviderFactory)[MainActivityViewModel::class.java]

        viewModel.tabItemSelectedLiveData.observe(this) {
            isTabSelected = it
        }

        val adapter = NoteTabAdaptor(this, supportFragmentManager, 2)
        viewBinding.viewPager.adapter = adapter

        viewBinding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(viewBinding.tabLayout))

        viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setIcon(R.drawable.note_icon_yellow))
        viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setIcon(R.drawable.task_icon))

        viewBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewBinding.viewPager.currentItem = tab.position
                if(tab.position==1){
                    viewModel.setTabItem(false)
                }else{
                    viewModel.setTabItem(true)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        viewBinding.fabCreate.setOnClickListener {

            if(isTabSelected){
                val intent = Intent(this,EditCreateActivity::class.java)
                val noteId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")).toString()
                val newNote = NoteItem(idDate = noteId, recentChangeDate = noteId)
                viewModel.saveNote(newNote)
                startActivity(intent)
            }else{
                // create new reminder item using dialogue
                showDialog()

            }

        }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.reminder_dialog_layout)
        val mainCheckBox = dialog.findViewById(R.id.checkBox_dialog) as CheckBox
        val etMain = dialog.findViewById(R.id.et_main) as EditText
        val setReminderButton = dialog.findViewById(R.id.reminder_setting) as Button
        val doneButton = dialog.findViewById(R.id.button_done) as Button
        doneButton.setOnClickListener {
           // create a new reminder with data
        }
        setReminderButton.setOnClickListener {
            // show a time and date picker
        }

        dialog.show()
    }
}