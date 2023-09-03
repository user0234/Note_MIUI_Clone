package com.hellow.notemiuiclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hellow.notemiuiclone.adapter.viewHolder.EditDescriptionCheckBoxItemViewHolder
import com.hellow.notemiuiclone.adapter.viewHolder.EditDescriptionImageItemViewHolder
import com.hellow.notemiuiclone.adapter.viewHolder.EditDescriptionTextItemViewHolder
import com.hellow.notemiuiclone.databinding.EditDescriptionCheckboxItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionImageItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionTextItemBinding
import com.hellow.notemiuiclone.models.NoteDescItem
import com.hellow.notemiuiclone.models.NoteDescItemType

class EditDescriptionItemAdaptor(val callback: Callback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            NoteDescItemType.Text.ordinal -> EditDescriptionTextItemViewHolder(
                EditDescriptionTextItemBinding.inflate(inflater, parent, false),
                callback
            )

            NoteDescItemType.CheckBox.ordinal -> EditDescriptionCheckBoxItemViewHolder(
                EditDescriptionCheckboxItemBinding.inflate(inflater, parent, false), callback
            )

            NoteDescItemType.Image.ordinal -> EditDescriptionImageItemViewHolder(
                EditDescriptionImageItemBinding.inflate(inflater, parent, false), callback
            )


            else -> error("Unknown view type")
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].type.ordinal
    }


    interface Callback {

    }
}