package com.hellow.notemiuiclone.adapter

import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hellow.notemiuiclone.databinding.DialogReminderSubItemBinding
import com.hellow.notemiuiclone.models.ReminderSubItem
import com.hellow.notemiuiclone.utils.showKeyboard

class ReminderSubItemDialogAdaptor :
    RecyclerView.Adapter<ReminderSubItemDialogAdaptor.ReminderSubItemViewHolder>() {

    inner class ReminderSubItemViewHolder(val binding: DialogReminderSubItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<ReminderSubItem>() {
        override fun areItemsTheSame(oldItem: ReminderSubItem, newItem: ReminderSubItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ReminderSubItem,
            newItem: ReminderSubItem,
        ): Boolean {
            return oldItem == newItem
        }

    }

    val reminderSubItemDiffer = AsyncListDiffer(this, differCallBack)

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderSubItemViewHolder {
        return ReminderSubItemViewHolder(
            DialogReminderSubItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return reminderSubItemDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: ReminderSubItemViewHolder, position: Int) {
        val currentItem = reminderSubItemDiffer.currentList[position]
        holder.binding.checkBoxSubMain.isChecked = currentItem.isDone
        holder.binding.etSubText.setText(currentItem.name)
        if (position == reminderSubItemDiffer.currentList.size - 1) {
            holder.binding.etSubText.requestFocus()
            holder.binding.etSubText.setSelection(currentItem.name.length)
        }
        holder.binding.etSubText.addTextChangedListener {
            reminderSubItemDiffer.currentList[holder.adapterPosition].name = it.toString()

            val text = it.toString()
            if (text.contains("\n") && text.isNotBlank()) {
                reminderSubItemDiffer.currentList[holder.adapterPosition].isDone = false
                onItemClickListener?.let {
                    it(true)
                }
            } else {
                if (text.contains("\n")) {
                    holder.binding.etSubText.setText("")
                }
            }

        }
        holder.binding.etSubText.setOnKeyListener { vew, keyCode, event ->


            if (keyCode == KeyEvent.KEYCODE_ENTER && holder.binding.etSubText.text.toString() != "") {
                reminderSubItemDiffer.currentList[holder.adapterPosition].isDone = false
                onItemClickListener?.let {
                    it(true)
                }
            }
            if (keyCode == KeyEvent.KEYCODE_DEL && holder.binding.etSubText.text.toString() == "") {
                reminderSubItemDiffer.currentList[holder.adapterPosition].isDone = false

                onItemClickListener?.let {
                    it(false)
                }
            }

            true
        }

    }

    fun setItemFocus(position: Int) {
        val rcv = recyclerView ?: return

        // If item to focus on doesn't exist yet, save it for later.

        val viewHolder = rcv.findViewHolderForAdapterPosition(1) as ReminderSubItemViewHolder?
        if (viewHolder != null) {
            viewHolder.binding.etSubText.requestFocus()
            viewHolder.binding.etSubText.setSelection(position)
            viewHolder.binding.etSubText.showKeyboard()
        }

    }

    // position and isChecked

    private var onItemClickListener: ((Boolean) -> Unit)? = null

    fun setOnItemClickListener(listener: (Boolean) -> Unit) {
        onItemClickListener = listener
    }

}