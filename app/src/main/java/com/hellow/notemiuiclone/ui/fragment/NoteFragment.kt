package com.hellow.notemiuiclone.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.NotesAdapter
import com.hellow.notemiuiclone.ui.editActivity.EditCreateActivity
import com.hellow.notemiuiclone.ui.mainActivity.MainActivity
import com.hellow.notemiuiclone.ui.mainActivity.MainActivityViewModel
import com.hellow.notemiuiclone.utils.TestModels.note1
import com.hellow.notemiuiclone.utils.TestModels.note2
import com.hellow.notemiuiclone.utils.TestModels.note3
import com.hellow.notemiuiclone.utils.Utils.NOTE_ITEM_LIST

class NoteFragment : Fragment(R.layout.fragment_note) {

    private lateinit var viewModel: MainActivityViewModel
    lateinit var notesAdapter: NotesAdapter
    lateinit var rvNotes: RecyclerView
    lateinit var rvEmptyView: MaterialCardView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        rvNotes = view.findViewById(R.id.rv_note_list)
        rvEmptyView = view.findViewById(R.id.empty_list_card)
        setUpRecyclerView()

        notesAdapter.setOnItemClickListener { note ->
            // go to edit /create activity
            val intent = Intent(view.context, EditCreateActivity::class.java)

            intent.putExtra(NOTE_ITEM_LIST, note)
            startActivity(intent)
        }

        viewModel.saveNote(note1)
        viewModel.saveNote(note2)
        viewModel.saveNote(note3)

        viewModel.getNotes().observe(viewLifecycleOwner) {
            if (it == null) {
                rvNotes.visibility = View.GONE

                rvEmptyView.visibility = View.VISIBLE
            } else {
                if (it.isEmpty()) {
                    rvNotes.visibility = View.GONE
                    rvEmptyView.visibility = View.VISIBLE
                } else {
                    notesAdapter.notesDiffer.submitList(it)
                    rvNotes.visibility = View.VISIBLE
                    rvEmptyView.visibility = View.GONE
                }
            }

        }

        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or
                    ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                val position = viewHolder.adapterPosition
                viewHolder.itemView.clearFocus()
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = notesAdapter.notesDiffer.currentList[position]

                if (direction == ItemTouchHelper.LEFT) {
                    viewModel.deleteNote(note)
                    Snackbar.make(view, "deleted forever", Snackbar.LENGTH_LONG).apply {
                        setAction("StopDelete") {
                            viewModel.saveNote(note)
                        }
                        show()
                    }
                }
                if (direction == ItemTouchHelper.RIGHT) {
                    viewModel.archiveNote(note)
                    Snackbar.make(view, "Note Archived", Snackbar.LENGTH_LONG).apply {
                        setAction("Undo") {
                            viewModel.saveNote(note)
                        }
                        show()
                    }
                }

            }

        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(rvNotes)
        }
    }

    private fun setUpRecyclerView() {
        notesAdapter = NotesAdapter()
        rvNotes.adapter = notesAdapter
        rvNotes.layoutManager = LinearLayoutManager(activity)

    }

}