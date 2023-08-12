package com.hellow.notemiuiclone.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hellow.notemiuiclone.adapter.ReminderAdaptor
import com.hellow.notemiuiclone.databinding.FragmentTaskBinding
import com.hellow.notemiuiclone.ui.mainActivity.MainActivity
import com.hellow.notemiuiclone.ui.mainActivity.MainActivityViewModel

class TaskFragment : Fragment() {

    private lateinit var viewModel: MainActivityViewModel
    lateinit var reminderCompletedAdapter: ReminderAdaptor
    lateinit var reminderUnCompletedAdapter: ReminderAdaptor
    private var binding: FragmentTaskBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTaskBinding.inflate(layoutInflater, container, false)
        viewModel = (activity as MainActivity).viewModel

        setUpCompletedRecyclerView()
        setUpUnCompletedRecyclerView()

        viewModel.getAllReminder().observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding!!.noReminderView.visibility = View.VISIBLE
            } else {
                binding!!.noReminderView.visibility = View.GONE
            }
        }

        viewModel.getCompletedReminders().observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) {
                binding!!.rvTaskListCompleted.visibility = View.GONE
                binding!!.llShowCompleted.visibility = View.GONE

            } else {
                binding!!.rvTaskListCompleted.visibility = View.VISIBLE
                binding!!.llShowCompleted.visibility = View.VISIBLE
                reminderCompletedAdapter.reminderDiffer.submitList(it)
            }
        }

        viewModel.getInCompleterReminders().observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) {
                binding!!.rvTaskListNotCompleted.visibility = View.GONE

            } else {
                binding!!.rvTaskListCompleted.visibility = View.VISIBLE
                reminderUnCompletedAdapter.reminderDiffer.submitList(it)
            }
        }
        return binding!!.root
    }

    private fun setUpUnCompletedRecyclerView() {
        reminderUnCompletedAdapter = ReminderAdaptor()

        binding!!.rvTaskListNotCompleted.adapter = reminderUnCompletedAdapter
        binding!!.rvTaskListNotCompleted.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
        binding!!.rvTaskListNotCompleted.setHasFixedSize(true)
        reminderUnCompletedAdapter.setOnItemClickListener { reminderItem, updateOrUpdateAndOpen ->
            if (updateOrUpdateAndOpen) {
                viewModel.updateReminder(reminderItem)
            } else {
                viewModel.updateReminder(reminderItem)
                // open this in the dialog

            }
        }

        setUpItemTouchHelperUnCompleted()
    }

    private fun setUpCompletedRecyclerView() {
        reminderCompletedAdapter = ReminderAdaptor()

        binding!!.rvTaskListCompleted.adapter = reminderCompletedAdapter
        binding!!.rvTaskListCompleted.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
        binding!!.rvTaskListCompleted.setHasFixedSize(true)
        reminderCompletedAdapter.setOnItemClickListener { reminderItem, updateOrUpdateAndOpen ->
            if (updateOrUpdateAndOpen) {
                viewModel.updateReminder(reminderItem)
            } else {
                viewModel.updateReminder(reminderItem)
                // open this in the dialog
            }

        }
        setUpItemTouchHelperCompleted()
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
                return true
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

                    ItemTouchHelper.LEFT -> {
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}