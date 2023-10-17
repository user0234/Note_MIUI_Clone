package com.hellow.notemiuiclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hellow.notemiuiclone.adapter.viewHolder.EditDescriptionAudioItemViewHolder
import com.hellow.notemiuiclone.adapter.viewHolder.EditDescriptionCheckBoxItemViewHolder
import com.hellow.notemiuiclone.adapter.viewHolder.EditDescriptionImageItemViewHolder
 import com.hellow.notemiuiclone.adapter.viewHolder.EditAdaptor.EditFocusableViewHolder
import com.hellow.notemiuiclone.databinding.EditDescriptionAudioItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionCheckboxItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionImageItemBinding
import com.hellow.notemiuiclone.models.NoteDescItem
import com.hellow.notemiuiclone.models.NoteDescItemType
import com.hellow.notemiuiclone.ui.editActivity1.EditCreateViewModel

class EditDescriptionItemAdaptor(val callback: Callback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var pendingFocusChange: EditCreateViewModel.FocusChange? = null

    // for faster change in view
    private val differCallBack = object : DiffUtil.ItemCallback<NoteDescItem>() {
        override fun areItemsTheSame(oldItem: NoteDescItem, newItem: NoteDescItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteDescItem, newItem: NoteDescItem): Boolean {
            return oldItem.type == newItem.type
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            NoteDescItemType.Text.ordinal -> EditDescriptionCheckBoxItemViewHolder(
                EditDescriptionCheckboxItemBinding.inflate(inflater, parent, false), callback,false
            )

            NoteDescItemType.CheckBox.ordinal -> EditDescriptionCheckBoxItemViewHolder(
                EditDescriptionCheckboxItemBinding.inflate(inflater, parent, false), callback,true
            )

            NoteDescItemType.Image.ordinal -> EditDescriptionImageItemViewHolder(
                EditDescriptionImageItemBinding.inflate(inflater, parent, false), callback
            )

            NoteDescItemType.Audio.ordinal -> EditDescriptionAudioItemViewHolder(
                EditDescriptionAudioItemBinding.inflate(
                    inflater, parent, false
                ), callback
            )

            else -> error("Unknown view type")
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        when (holder) {

            is EditDescriptionCheckBoxItemViewHolder -> {
                holder.bind(currentItem)
            }

            is EditDescriptionImageItemViewHolder -> {
                holder.bind(currentItem)
            }

            is EditDescriptionAudioItemViewHolder -> {
                holder.bind(currentItem)
            }
        }
         // two things we have 1. if we want to focus on item we can without changing or reloading the recycler
        // 2, if the item doesn't exist then we can change the item view from the save focus

        if(holder is EditFocusableViewHolder){
           //  if(position ==)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].type.ordinal
    }

    interface Callback {


        fun focusLose(pos: Int,text: String)

        fun focusGain(pos: Int)

        fun textChanged(pos:Int,text: String)

        fun newItemAdded(pos: Int, textCurrent: String,textNext:String)

        fun itemDeleted(pos: Int, text: String)

        fun checkChanged(pos: Int, isChecked: Boolean)

    }
}