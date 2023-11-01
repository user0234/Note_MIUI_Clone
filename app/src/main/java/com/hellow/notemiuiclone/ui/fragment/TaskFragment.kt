package com.hellow.notemiuiclone.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.ReminderAdaptor
import com.hellow.notemiuiclone.databinding.FragmentTaskBinding
import com.hellow.notemiuiclone.dialogs.EditReminderDialog
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.ReminderStatus
import com.hellow.notemiuiclone.ui.mainActivity.MainActivity
import com.hellow.notemiuiclone.ui.mainActivity.MainActivityViewModel


class TaskFragment : Fragment(R.layout.fragment_task) {

    private lateinit var viewModel: MainActivityViewModel
    lateinit var reminderCompletedAdapter: ReminderAdaptor
    lateinit var reminderUnCompletedAdapter: ReminderAdaptor
    lateinit var binding: FragmentTaskBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = (activity as MainActivity).viewModel
        binding = FragmentTaskBinding.bind(view)
        setUpCompletedRecyclerView()
        setUpUnCompletedRecyclerView()

        viewModel.getAllReminder().observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.emptyImageView.visibility = View.VISIBLE
            } else {
                binding.emptyImageView.visibility = View.GONE
                // list for completed
                val completedList = it.filter { item ->
                    item.reminderStatus == ReminderStatus.DoneWhole
                }

                if (completedList.isEmpty()) {
                    binding.rvTaskListCompleted.visibility = View.GONE
                    binding.llShowCompleted.visibility = View.GONE
                } else {
                    binding.rvTaskListCompleted.visibility = View.GONE
                    binding.llShowCompleted.visibility = View.VISIBLE
                }

                reminderCompletedAdapter.reminderDiffer.submitList(completedList)


                val unCompletedList = it.filter { item ->
                    item.reminderStatus != ReminderStatus.DoneWhole
                }

                if (unCompletedList.isEmpty()) {
                    binding.rvTaskListNotCompleted.visibility = View.GONE
                } else {
                    binding.rvTaskListNotCompleted.visibility = View.VISIBLE
                }
                reminderUnCompletedAdapter.reminderDiffer.submitList(unCompletedList)
            }
        }

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
            openReminderEditDialog2(reminderItem)
        }

        reminderUnCompletedAdapter.setOnMainItemCheckListener { reminderItem, boolean ->

            viewModel.updateReminder(reminderItem)

        }

        reminderUnCompletedAdapter.setOnSubItemCheckListener { reminderItem, boolean ->

            viewModel.updateReminder(reminderItem)

        }

        binding.showHideCompleted.setOnClickListener {

            if (viewModel.getCompleteShowInTasks()) {
                viewModel.setCompleteShowInTasks(false)

                binding.showHideCompleted.animate()
                    .setDuration(80)
                    .rotation(0F)
                    .withEndAction {
                        binding.rvTaskListCompleted.visibility = View.GONE
                    }
                    .start()


            } else {
                viewModel.setCompleteShowInTasks(true)

                binding.showHideCompleted.animate()
                    .setDuration(80)
                    .rotation(-180F)
                    .start()
                binding.rvTaskListCompleted.visibility = View.VISIBLE

            }

        }
        binding.rvTaskListCompleted.visibility = View.GONE
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
        reminderCompletedAdapter.setOnItemClickListener { reminderItem, _ ->
            openReminderEditDialog2(reminderItem)

        }
        reminderCompletedAdapter.setOnMainItemCheckListener { reminderItem, boolean ->

            viewModel.updateReminder(reminderItem)

        }

        reminderCompletedAdapter.setOnSubItemCheckListener { reminderItem, boolean ->

            viewModel.updateReminder(reminderItem)

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

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (recyclerView.layoutManager is GridLayoutManager) {
                    val dragFlags =
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    val swipeFlags = 0
                    makeMovementFlags(dragFlags, swipeFlags)
                } else {
                    val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    val swipeFlags = ItemTouchHelper.LEFT
                    makeMovementFlags(dragFlags, swipeFlags)
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                       return
                    }

                    ItemTouchHelper.LEFT -> {
                        val item = reminderCompletedAdapter.reminderDiffer.currentList[position]
                        viewModel.deleteReminder(item)
                        Snackbar.make(binding.root, "deleted forever", Snackbar.LENGTH_LONG)
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
            attachToRecyclerView(binding.rvTaskListCompleted)
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

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return if (recyclerView.layoutManager is GridLayoutManager) {
                    val dragFlags =
                        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                    val swipeFlags = 0
                    makeMovementFlags(dragFlags, swipeFlags)
                } else {
                    val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                    val swipeFlags = ItemTouchHelper.LEFT
                    makeMovementFlags(dragFlags, swipeFlags)
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        val item = reminderUnCompletedAdapter.reminderDiffer.currentList[position]
                        viewModel.deleteReminder(item)
                        Snackbar.make(binding.root, "deleted forever", Snackbar.LENGTH_LONG)
                            .apply {
                                setAction("StopDelete") {
                                    viewModel.createNewReminder(item)
                                }
                                show()
                            }
                    }
                    ItemTouchHelper.RIGHT -> {
                        return
                    }
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.rvTaskListNotCompleted)
        }
    }


}