package com.hellow.notemiuiclone.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.NotesAdapter
import com.hellow.notemiuiclone.databinding.FragmentNoteBinding
import com.hellow.notemiuiclone.models.noteModels.NoteDataItem
import com.hellow.notemiuiclone.ui.editActivity.EditNoteActivity
import com.hellow.notemiuiclone.ui.mainActivity.MainActivity
import com.hellow.notemiuiclone.ui.mainActivity.MainActivityViewModel
import com.hellow.notemiuiclone.utils.Utils.NOTE_ITEM_LIST
import com.hellow.notemiuiclone.utils.sharedPref.SharedPrefFunctions.getSharedPrefNotesSort

class NoteFragment : Fragment(R.layout.fragment_note) {

    private lateinit var binding: FragmentNoteBinding

    private lateinit var notesAdapter: NotesAdapter
    private val viewModel: MainActivityViewModel by lazy {
        (activity as MainActivity).viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoteBinding.bind(view)
        setupData()
        setUpRecyclerView(view)

    }

    private fun setUpTouchHelper(view: View) {
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or
                    ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                viewHolder.itemView.clearFocus()
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = notesAdapter.notesDiffer.currentList[position]

                if (direction == ItemTouchHelper.LEFT) {
                    viewModel.deleteNote(note)
                    Snackbar.make(view, "deleted", Snackbar.LENGTH_LONG).apply {
                        setAction("StopDelete") {
                            viewModel.addNote(note)
                        }
                        show()
                    }
                }
                if (direction == ItemTouchHelper.RIGHT) {
                    viewModel.deleteNote(note)
                    Snackbar.make(view, "deleted", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            viewModel.addNote(note)
                        }
                        show()
                    }
                }

            }

        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.rvNoteList)
        }
    }

    private fun handleNotesData(notesItem: List<NoteDataItem>?): List<NoteDataItem>? {
        if (notesItem.isNullOrEmpty()) {
            binding.rvNoteList.visibility = View.GONE
            binding.emptyListCard.visibility = View.VISIBLE
            return null
        } else {

            val sortBy = getSharedPrefNotesSort(requireContext())

            binding.rvNoteList.visibility = View.VISIBLE
            binding.emptyListCard.visibility = View.GONE

            return when (sortBy) {

                0 -> {

                    notesItem.sortedBy { item ->
                        item.title
                    }
                }

                1 -> {

                    notesItem.sortedBy { item ->
                        item.recentChangeDate
                    }
                }

                else -> {

                    notesItem.sortedBy { item ->
                        item.id
                    }
                }

            }

        }

    }

    private fun setupData() {
        binding.searchBarView.searchBarTv.text = "Search Notes"

        viewModel.getNotes().observe(viewLifecycleOwner)
        { notesItem ->

            notesAdapter.notesDiffer.submitList(handleNotesData(notesItem))

        }

    }

    private fun setUpRecyclerView(view: View) {
        notesAdapter = NotesAdapter()
        binding.rvNoteList.apply {
            adapter = notesAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(false)
        }
        notesAdapter.setOnItemClickListener { note ->
            // go to edit /create activity
            val intent = Intent(view.context, EditNoteActivity::class.java)
            intent.putExtra(NOTE_ITEM_LIST, note)
            startActivity(intent)
        }

        setUpTouchHelper(view)
    }

}