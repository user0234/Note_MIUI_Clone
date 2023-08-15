package com.hellow.notemiuiclone.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.ReminderAdaptor
import com.hellow.notemiuiclone.adapter.ReminderSubItemDialogAdaptor
import com.hellow.notemiuiclone.databinding.FragmentTaskBinding
import com.hellow.notemiuiclone.databinding.ReminderDialogLayoutBinding
import com.hellow.notemiuiclone.dialogs.EditReminderDialog
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.ReminderStatus
import com.hellow.notemiuiclone.models.ReminderSubItem
import com.hellow.notemiuiclone.ui.mainActivity.MainActivity
import com.hellow.notemiuiclone.ui.mainActivity.MainActivityViewModel

class TaskFragment : Fragment() {

    private lateinit var viewModel: MainActivityViewModel
    lateinit var reminderCompletedAdapter: ReminderAdaptor
    lateinit var reminderUnCompletedAdapter: ReminderAdaptor
    lateinit var binding: FragmentTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = (activity as MainActivity).viewModel

        setUpCompletedRecyclerView()
        setUpUnCompletedRecyclerView()

        viewModel.getAllReminder().observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.noReminderView.visibility = View.VISIBLE
            } else {
                binding.noReminderView.visibility = View.GONE
                // list for completed
                val completedList = it.filter { item ->
                    item.reminderStatus == ReminderStatus.DoneWhole
                }

                if (completedList.isNullOrEmpty()) {
                    binding.rvTaskListCompleted.visibility = View.GONE
                    binding.llShowCompleted.visibility = View.GONE
                } else {
                    binding.rvTaskListCompleted.visibility = View.VISIBLE
                    binding.llShowCompleted.visibility = View.VISIBLE
                }

                reminderCompletedAdapter.reminderDiffer.submitList(completedList)


                val unCompletedList = it.filter { item ->
                    item.reminderStatus != ReminderStatus.DoneWhole
                }

                if (unCompletedList.isNullOrEmpty()) {
                    binding.rvTaskListNotCompleted.visibility = View.GONE
                } else {
                    binding.rvTaskListNotCompleted.visibility = View.VISIBLE
                }
                reminderUnCompletedAdapter.reminderDiffer.submitList(unCompletedList)
            }
        }

//        viewModel.getCompletedReminders().observe(viewLifecycleOwner) {
//
//            if (it.isNullOrEmpty()) {
//                binding.rvTaskListCompleted.visibility = View.GONE
////                binding.llShowCompleted.visibility = View.GONE
//
//            } else {
//                reminderCompletedAdapter.reminderDiffer.submitList(it)
//                binding.rvTaskListCompleted.visibility = View.VISIBLE
//                binding.llShowCompleted.visibility = View.VISIBLE
//
//            }
//        }

//        viewModel.getInCompleterReminders().observe(viewLifecycleOwner) {
//
//            if (it.isNullOrEmpty()) {
//                binding.rvTaskListNotCompleted.visibility = View.GONE
//
//            } else {
//                reminderUnCompletedAdapter.reminderDiffer.submitList(it)
//                binding.rvTaskListNotCompleted.visibility = View.VISIBLE
//            }
//        }
    }

    private fun setUpUnCompletedRecyclerView() {
        reminderUnCompletedAdapter = ReminderAdaptor()

        binding.rvTaskListNotCompleted.adapter = reminderUnCompletedAdapter
        binding.rvTaskListNotCompleted.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvTaskListNotCompleted.setHasFixedSize(false)
        reminderUnCompletedAdapter.setOnItemClickListener { reminderItem, updateOrUpdateAndOpen ->
            if (updateOrUpdateAndOpen) {
                viewModel.updateReminder(reminderItem)
            } else {
                viewModel.updateReminder(reminderItem)
                // open this in the dialog
                // openReminderEditDialog(reminderItem)

                openReminderEditDialog2(reminderItem)
            }
        }

        binding.showHideCompleted.setOnClickListener {

            if (viewModel.getCompleteShowInTasks()) {
                viewModel.setCompleteShowInTasks(false)

                binding.showHideCompleted.animate()
                    .setDuration(400)
                    .rotation(-180F)
                    .start()
                binding.rvTaskListCompleted.visibility = View.VISIBLE

            } else {
                viewModel.setCompleteShowInTasks(true)

                binding.showHideCompleted.animate()
                    .setDuration(400)
                    .rotation(0F)
                    .start()
                binding.rvTaskListCompleted.visibility = View.GONE

            }

        }

        setUpItemTouchHelperUnCompleted()
    }

    private fun setUpCompletedRecyclerView() {
        reminderCompletedAdapter = ReminderAdaptor()

        binding.rvTaskListCompleted.adapter = reminderCompletedAdapter
        binding.rvTaskListCompleted.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvTaskListCompleted.setHasFixedSize(false)
        reminderCompletedAdapter.setOnItemClickListener { reminderItem, updateOrUpdateAndOpen ->
            if (updateOrUpdateAndOpen) {
                viewModel.updateReminder(reminderItem)
            } else {
                viewModel.updateReminder(reminderItem)
                // open this in the dialog
                //   openReminderEditDialog(reminderItem)
                openReminderEditDialog2(reminderItem)
            }

        }
        setUpItemTouchHelperCompleted()
    }

    private fun openReminderEditDialog2(currentItem: ReminderItem) {

        val reminderDialog = object : EditReminderDialog(
            requireActivity(),
            currentItem
        ) {
            override fun onItemDone(item: ReminderItem?) {
                if (item != null) {
                    viewModel.updateReminder(item)
                }

            }

        }
        reminderDialog.show()

    }

    private fun openReminderEditDialog(currentItem: ReminderItem) {

        val dialog = Dialog(requireActivity(), R.style.material_dialog)
        val dialogViewBinding = ReminderDialogLayoutBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogViewBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        val reminderSubItemDialogAdaptor = ReminderSubItemDialogAdaptor()
        dialogViewBinding.rvSubItem.adapter = reminderSubItemDialogAdaptor
        dialogViewBinding.rvSubItem.layoutManager = LinearLayoutManager(
            dialogViewBinding.root.context,
            LinearLayoutManager.VERTICAL, false
        )
        dialogViewBinding.rvSubItem.setHasFixedSize(false)

        if (currentItem.itemsList.size > 1) {
            reminderSubItemDialogAdaptor.reminderSubItemDiffer.submitList(currentItem.itemsList)
            dialogViewBinding.llMainItem.visibility = View.GONE
            dialogViewBinding.llSubItem.visibility = View.VISIBLE

        } else {
            dialogViewBinding.llMainItem.visibility = View.VISIBLE
            dialogViewBinding.llSubItem.visibility = View.GONE
            dialogViewBinding.etMain.setText(currentItem.title)
            dialogViewBinding.etMain.requestFocus()
            dialogViewBinding.checkBoxDialog.isChecked =
                currentItem.reminderStatus == ReminderStatus.DoneWhole


        }

        reminderSubItemDialogAdaptor.setOnItemClickListener { gonext ->

            if (gonext) {
                val reminderSubItemList: MutableList<ReminderSubItem> = mutableListOf()

                reminderSubItemList.addAll(reminderSubItemDialogAdaptor.reminderSubItemDiffer.currentList)

                reminderSubItemList.add(ReminderSubItem("", false, reminderSubItemList.size))

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

                dialogViewBinding.llMainItem.visibility = View.GONE
                dialogViewBinding.llSubItem.visibility = View.VISIBLE
                reminderSubItemDialogAdaptor.reminderSubItemDiffer.submitList(
                    listOf(
                        ReminderSubItem(currentText, dialogViewBinding.checkBoxDialog.isChecked, 0),
                        ReminderSubItem("", false, 1)
                    )
                )

            }

            true
        }

        dialogViewBinding.buttonDone.setOnClickListener {
            // create a new reminder from all the data
            val id = currentItem.idDate
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

            if (title != "") {
                viewModel.updateReminder(reminderItem)
            } else {
                viewModel.deleteReminder(currentItem)
            }
            dialog.cancel()
        }
        dialogViewBinding.setReminderButton.setOnClickListener {
            // pick date and time for reminder and add a alarm for that time when cancel
        }
        dialogViewBinding.mainCancelable.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

    // item touch helper
    private fun setUpItemTouchHelperCompleted() {
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or
                    ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        val item = reminderCompletedAdapter.reminderDiffer.currentList[position]
                        viewModel.deleteReminder(item)
                        Snackbar.make(binding!!.root, "deleted forever", Snackbar.LENGTH_LONG)
                            .apply {
                                setAction("StopDelete") {
                                    viewModel.createNewReminder(item)
                                }
                                show()
                            }
                    }

                    else -> {
                        return
                    }

                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding!!.rvTaskListCompleted)
        }
    }

    private fun setUpItemTouchHelperUnCompleted() {
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or
                    ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        val item = reminderUnCompletedAdapter.reminderDiffer.currentList[position]
                        viewModel.deleteReminder(item)
                        Snackbar.make(binding!!.root, "deleted forever", Snackbar.LENGTH_LONG)
                            .apply {
                                setAction("StopDelete") {
                                    viewModel.createNewReminder(item)
                                }
                                show()
                            }
                    }

                    ItemTouchHelper.LEFT -> {
                        return
                    }
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding!!.rvTaskListNotCompleted)
        }
    }


}