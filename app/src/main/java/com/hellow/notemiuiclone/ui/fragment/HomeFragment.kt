package com.hellow.notemiuiclone.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.NoteTabAdaptor
import com.hellow.notemiuiclone.databinding.FragmentHomeBinding
import com.hellow.notemiuiclone.dialogs.CreateReminderDialog
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.noteModels.NoteDataItem
import com.hellow.notemiuiclone.ui.editActivity.EditNoteActivity
import com.hellow.notemiuiclone.ui.mainActivity.MainActivity
import com.hellow.notemiuiclone.ui.mainActivity.MainActivityViewModel
import com.hellow.notemiuiclone.utils.Utils
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: MainActivityViewModel by lazy {

        (activity as MainActivity).viewModel
    }
    private lateinit var binding: FragmentHomeBinding
    private lateinit var fragmentSwitchAdaptor: NoteTabAdaptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        setUpOptionMenu()
        setUpFragmentAdaptor()
        setUpFab()
    }

    private fun setUpFab() {
        binding.fabCreate.setOnClickListener {

            handleFabClick()
        }

    }

    private fun handleFabClick() {
        if (binding.viewPager.currentItem == 0) {
            val intent = Intent(requireActivity(), EditNoteActivity::class.java)
            val noteId = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")).toString()
            val note = NoteDataItem(noteId, recentChangeDate = noteId)
            viewModel.addNote(note)
            intent.putExtra(Utils.NOTE_ITEM_LIST, note)
            startActivity(intent)
        } else {
            // create new reminder item using dialogue
            //   showCreateDialog()
            showCreateDialog2()
        }

    }

    private fun showCreateDialog2() {
        val reminderDialog = object : CreateReminderDialog(
            requireContext(),

            ) {
            override fun onItemDone(item: ReminderItem?, time: LocalDateTime) {
                if (item != null) {

                    val startsTime = LocalDateTime.now()

                    val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    val mStartTime = mSimpleDateFormat.parse(time.toString())
                    val mEndTime = mSimpleDateFormat.parse(time.toString())

                    val intent = Intent(Intent.ACTION_INSERT).apply {
                        data = CalendarContract.Reminders.CONTENT_URI
                        putExtra(CalendarContract.Reminders.TITLE, item.title)
                        putExtra(CalendarContract.Reminders.ALL_DAY, false)
                        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, mEndTime)
                        startActivity(this)
                    }
                    // TODO set an alarm for that time
                    viewModel.createNewReminder(item)
                }

            }

        }
        reminderDialog.show()
    }


    private fun setUpFragmentAdaptor() {
        fragmentSwitchAdaptor = NoteTabAdaptor(requireActivity(), 2)
        binding.viewPager.adapter = fragmentSwitchAdaptor
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

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


    }

    private fun setUpOptionMenu() {

        binding.toolBar.setOnMenuItemClickListener { item ->

            when (item.itemId) {

                R.id.settingBt -> {

                    viewModel.openSettingsFragment()


                }

            }

            true
        }


    }


}