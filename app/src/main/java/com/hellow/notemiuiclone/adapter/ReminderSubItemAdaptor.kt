package com.hellow.notemiuiclone.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hellow.notemiuiclone.databinding.ReminderSubItemBinding
import com.hellow.notemiuiclone.models.ReminderSubItem
import com.hellow.notemiuiclone.utils.Utils

class ReminderSubItemAdaptor: RecyclerView.Adapter<ReminderSubItemAdaptor.ReminderSubItemViewHolder>() {

    inner class ReminderSubItemViewHolder(val binding: ReminderSubItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<ReminderSubItem>() {
        override fun areItemsTheSame(oldItem: ReminderSubItem, newItem: ReminderSubItem): Boolean {
            return oldItem.name == newItem.name || oldItem.isDone == newItem.isDone
        }

        override fun areContentsTheSame(oldItem: ReminderSubItem, newItem: ReminderSubItem): Boolean {
            return oldItem == newItem
        }

    }

    val reminderSubItemDiffer = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderSubItemViewHolder {
        return ReminderSubItemViewHolder(
            ReminderSubItemBinding.inflate(
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
        holder.binding.reminderTitleMain.text = currentItem.name
        holder.binding.reminderTitleMain.setTextColor(Color.parseColor(Utils.getStatusTextColor(currentItem.isDone)))

        holder.binding.checkBoxSubMain.setOnClickListener {
               currentItem.isDone = !currentItem.isDone
                onItemClickListener?.let{
                    it(currentItem.isDone,position)
                }
        }

    }
       // position and isChecked

    private var onItemClickListener: ((Boolean,Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Boolean,Int) -> Unit) {
        onItemClickListener = listener
    }

}