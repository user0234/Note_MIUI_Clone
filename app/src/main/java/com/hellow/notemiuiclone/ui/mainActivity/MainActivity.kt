package com.hellow.notemiuiclone.ui.mainActivity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.NoteTabAdaptor
import com.hellow.notemiuiclone.adapter.ReminderSubItemDialogAdaptor
import com.hellow.notemiuiclone.database.notesDatabase.NotesDataBase
import com.hellow.notemiuiclone.database.reminderDatabase.ReminderDatabase
import com.hellow.notemiuiclone.databinding.ActivityMainBinding
import com.hellow.notemiuiclone.databinding.ReminderDialogLayoutBinding
import com.hellow.notemiuiclone.dialogs.CreateReminderDialog
import com.hellow.notemiuiclone.models.NoteItem
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.ReminderStatus
import com.hellow.notemiuiclone.models.ReminderSubItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.repository.reminder.ReminderRepository
import com.hellow.notemiuiclone.ui.editActivity.EditCreateActivity
import com.hellow.notemiuiclone.utils.Utils
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

        TabLayoutMediator(viewBinding.tabLayout,viewBinding.viewPager){ tab, position ->
                           when(position){
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

        createSnackBar("tab count ${viewBinding.tabLayout.tabCount}")
        viewBinding.tabLayout.tabCount
//        viewBinding.tabLayout.addTab(
//            viewBinding.tabLayout.newTab().setIcon(R.drawable.note_icon_yellow)
//        )
//        viewBinding.tabLayout.addTab(
//            viewBinding.tabLayout.newTab().setIcon(R.drawable.task_icon))


        viewBinding.fabCreate.setOnClickListener {

            if (viewBinding.viewPager.currentItem == 0) {
                val intent = Intent(this, EditCreateActivity::class.java)
                val noteId = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")).toString()
                val newNote = NoteItem(idDate = noteId, recentChangeDate = noteId)
                viewModel.saveNote(newNote)
                intent.putExtra(Utils.NOTE_ITEM_CREATE,newNote)
                startActivity(intent)
            } else {
                // create new reminder item using dialogue
             //   showCreateDialog()
                 showCreateDialog2()
            }

        }
    }
    private fun showCreateDialog2(){
        val reminderDialog = object : CreateReminderDialog(
            this@MainActivity,

        ) {
            override fun onItemDone(item: ReminderItem?) {
                if (item != null) {
                    item.TimerTime
                    // set an alarm for that time
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

    /*
    private fun showCreateDialog() {

        val dialog = Dialog(this, R.style.material_dialog)
        val dialogViewBinding = ReminderDialogLayoutBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogViewBinding.root)

        dialog.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialogViewBinding.etMain.requestFocus()

        val reminderSubItemDialogAdaptor = ReminderSubItemDialogAdaptor()
        dialogViewBinding.rvSubItem.adapter = reminderSubItemDialogAdaptor
        dialogViewBinding.rvSubItem.layoutManager = LinearLayoutManager(
            dialogViewBinding.root.context,
            LinearLayoutManager.VERTICAL, false
        )
        dialogViewBinding.rvSubItem.setHasFixedSize(false)

        reminderSubItemDialogAdaptor.setOnItemClickListener { gonext ->

            if (gonext) {
                val reminderSubItemList: MutableList<ReminderSubItem> = mutableListOf()

                reminderSubItemList.addAll(reminderSubItemDialogAdaptor.reminderSubItemDiffer.currentList)

                reminderSubItemList.add(ReminderSubItem("", false,reminderSubItemList.size))


                reminderSubItemDialogAdaptor.reminderSubItemDiffer.submitList(reminderSubItemList)

            } else {

                if (reminderSubItemDialogAdaptor.reminderSubItemDiffer.currentList.size <= 2) {
                    dialogViewBinding.llMainItem.visibility = View.VISIBLE
                    dialogViewBinding.llSubItem.visibility = View.GONE
                    dialogViewBinding.etMain.setText(
                        reminderSubItemDialogAdaptor.reminderSubItemDiffer.currentList[0].name
                    )
                    dialogViewBinding.etMain.requestFocus()
                    dialogViewBinding.checkBoxDialog.isChecked = false

                } else {
                    reminderSubItemDialogAdaptor.reminderSubItemDiffer.submitList(
                        reminderSubItemDialogAdaptor.reminderSubItemDiffer.currentList.subList(
                            0,
                            reminderSubItemDialogAdaptor.reminderSubItemDiffer.currentList.size - 1
                        )
                    )
                }
            }
        }


        dialogViewBinding.etMain.setOnKeyListener { vew, keyCode, event ->
            val currentText = dialogViewBinding.etMain.text.toString()
            if (keyCode == KeyEvent.KEYCODE_ENTER && currentText != "") {

                createSnackBar("enter clicked")
                dialogViewBinding.llMainItem.visibility = View.GONE
                dialogViewBinding.llSubItem.visibility = View.VISIBLE
                reminderSubItemDialogAdaptor.reminderSubItemDiffer.submitList(
                    listOf(
                        ReminderSubItem(currentText, false,0),
                        ReminderSubItem("", false,1)
                    )
                )

            }

            true
        }
        dialogViewBinding.buttonDone.setOnClickListener {
            // create a new reminder from all the data
            dialog.cancel()
        }
        dialogViewBinding.setReminderButton.setOnClickListener {
            // pick date and time for reminder and add a alarm for that time when cancel
        }
        dialogViewBinding.mainCancelable.setOnClickListener {
            dialog.cancel()
        }

        dialog.setOnCancelListener {

            val id = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")).toString()

            val title = if (dialogViewBinding.llMainItem.visibility == View.GONE) {
                "Checklist of subtasks"
            } else {
                dialogViewBinding.etMain.text.toString()
            }
            // take timer time from button setReminderButton
            val timerTime = dialogViewBinding.setReminderButton.text.toString()

            val subItemList = if (dialogViewBinding.llMainItem.visibility == View.GONE) {
                reminderSubItemDialogAdaptor.reminderSubItemDiffer.currentList
            } else {
                reminderSubItemDialogAdaptor.reminderSubItemDiffer.currentList.subList(0, 0)
            }

            val reminderItem = ReminderItem(
                id, title, reminderStatus = ReminderStatus.NotDone, isExpended = false,
                TimerTime = timerTime, checkedCount = 0, itemsList = subItemList
            )

            if(title != ""){
                viewModel.createNewReminder(reminderItem)

            }

        }
        dialog.show()
    }
    */
}