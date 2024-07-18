package com.hellow.notemiuiclone.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hellow.notemiuiclone.databinding.ReminderItemBinding
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.ReminderStatus
import com.hellow.notemiuiclone.utils.ConstantValues
class ReminderAdaptor : RecyclerView.Adapter<ReminderAdaptor.ReminderViewHolder>() {

    inner class ReminderViewHolder(val binding: ReminderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    // for faster change in view
    private val differCallBack = object : DiffUtil.ItemCallback<ReminderItem>() {
        override fun areItemsTheSame(oldItem: ReminderItem, newItem: ReminderItem): Boolean {
            return oldItem.idDate == newItem.idDate
        }

        override fun areContentsTheSame(oldItem: ReminderItem, newItem: ReminderItem): Boolean {
            return oldItem == newItem
        }

    }

    val reminderDiffer = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        return ReminderViewHolder(
            ReminderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return reminderDiffer.currentList.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {

        val currentItem = reminderDiffer.currentList[position]

        holder.binding.root.setOnClickListener {
            onItemClickListener?.let {
                it(currentItem, false)
            }
        }

        // set Color to View

        if (currentItem.reminderStatus == ReminderStatus.DoneWhole) {
            holder.binding.apply {
                holder.binding.checkBoxMain.isChecked = true
                reminderTitleMain.setTextColor(Color.parseColor(ConstantValues.getGreyValue(holder.itemView.context)))
            }
        } else {
            holder.binding.apply {
                holder.binding.checkBoxMain.isChecked = false
                reminderTitleMain.setTextColor(Color.parseColor(ConstantValues.getBlackValue(holder.itemView.context)))
            }
        }

        holder.binding.reminderTitleMain.text = if (currentItem.title == "") {
            holder.binding.apply {
                reminderTitleMain.setTextColor(Color.parseColor(ConstantValues.getGreyValue(holder.itemView.context)))
            }
            "Checklist of subtasks"
        } else {
            holder.binding.apply {
                reminderTitleMain.setTextColor(Color.parseColor(ConstantValues.getBlackValue(holder.itemView.context)))
            }
            currentItem.title
        }


        if (currentItem.TimerTime == "Set reminder") {
            holder.binding.reminderTime.visibility = View.GONE
        } else {
            holder.binding.reminderTime.text = currentItem.TimerTime
        }


        if (currentItem.itemsList.isEmpty()) {
            holder.binding.llCountBox.visibility = View.GONE
        } else {
            holder.binding.llCountBox.visibility = View.VISIBLE
            val count = "${currentItem.checkedCount}/${currentItem.itemsList.size}"
            holder.binding.itemCount.text = count


            val adaptor = ReminderSubItemAdaptor()
            holder.binding.rvReminderList.adapter = adaptor
            holder.binding.rvReminderList.layoutManager = LinearLayoutManager(
                holder.itemView.context,
                LinearLayoutManager.VERTICAL, false
            )
            holder.binding.rvReminderList.setHasFixedSize(true)

            adaptor.setOnItemListener {

                onItemClickListener?.let {
                    it(currentItem, true)
                }
            }

            adaptor.setOnItemClickListener { isChecked, subListPosition ->

                if (isChecked) {

                    currentItem.checkedCount += 1
                    if (currentItem.checkedCount == currentItem.itemsList.size) {
                        currentItem.reminderStatus = ReminderStatus.DoneWhole
                        currentItem.isExpended = false
                        currentItem.itemsList[subListPosition].isDone = true


                    } else {
                        reminderDiffer.currentList[holder.adapterPosition].itemsList[subListPosition].isDone =
                            true
                    }
                    onMainItemCheckedListener?.let {
                        it(currentItem, true)
                    }

                } else {

                    if (currentItem.checkedCount == currentItem.itemsList.size) {

                        currentItem.checkedCount -= 1
                        currentItem.reminderStatus = ReminderStatus.NotDone
                        currentItem.itemsList[subListPosition].isDone = false


                    } else {
                        reminderDiffer.currentList[holder.adapterPosition].checkedCount =
                            currentItem.checkedCount - 1
                        reminderDiffer.currentList[holder.adapterPosition].itemsList[subListPosition].isDone =
                            false

                    }
                    onItemClickListener?.let {
                        it(currentItem, true)
                    }
                }

                val count = "${currentItem.checkedCount}/${currentItem.itemsList.size}"
                holder.binding.itemCount.text = count


                adaptor.reminderSubItemDiffer.submitList(currentItem.itemsList)
            }
            adaptor.reminderSubItemDiffer.submitList(currentItem.itemsList)

        }


        holder.binding.ivShowListButton.setOnClickListener {

            if (currentItem.isExpended) {

                holder.binding.ivShowListButton.animate()
                    .setDuration(80)
                    .rotation(0F)
                    .withEndAction {
                        currentItem.isExpended = false
                        holder.binding.rvReminderList.visibility = View.GONE
                    }
                    .start()
            } else {

                holder.binding.ivShowListButton.animate()
                    .setDuration(80)
                    .rotation(180F)
                    .withEndAction {
                        currentItem.isExpended = true
                        holder.binding.rvReminderList.visibility = View.VISIBLE
                    }
                    .start()

            }

        }

        holder.binding.checkBoxMain.setOnClickListener {

            if (currentItem.reminderStatus == ReminderStatus.DoneWhole) {
                currentItem.reminderStatus = ReminderStatus.NotDone
                currentItem.isExpended = false
                currentItem.checkedCount = 0

                currentItem.itemsList.map {
                    it.isDone = false
                }

                onMainItemCheckedListener?.let {
                    it(currentItem, true)
                }

            } else {

                currentItem.reminderStatus = ReminderStatus.DoneWhole
                currentItem.isExpended = false
                currentItem.checkedCount = currentItem.itemsList.size

                currentItem.itemsList.map {
                    it.isDone = true
                }

                onMainItemCheckedListener?.let {
                    it(currentItem, false)
                }
            }
        }

        holder.itemView.setOnTouchListener { v, event ->
            when (event?.action) {

                MotionEvent.ACTION_DOWN -> {
                    holder.itemView.animate()
                        .scaleY(0.83F)
                        .scaleX(0.83F)
                        .setDuration(80)
                        .start()
                }

                MotionEvent.ACTION_UP -> {
                    holder.itemView.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                        .setDuration(80)
                        .start()
                }

                MotionEvent.ACTION_CANCEL -> {
                    holder.itemView.animate()
                        .scaleX(1F)
                        .scaleY(1F)
                        .setDuration(80)
                        .start()
                }
            }
            v?.onTouchEvent(event) ?: true
        }


    }

    //if we should update the element or update the item and open the dialog for edit

    private var onItemClickListener: ((ReminderItem, Boolean) -> Unit)? = null

    fun setOnItemClickListener(listener: (ReminderItem, Boolean) -> Unit) {
        onItemClickListener = listener
    }

    private var onMainItemCheckedListener: ((ReminderItem, Boolean) -> Unit)? = null
    fun setOnMainItemCheckListener(listener: (ReminderItem, Boolean) -> Unit) {
        onMainItemCheckedListener = listener
    }

    private var onSubItemCheckListener: ((ReminderItem, Boolean) -> Unit)? = null
    fun setOnSubItemCheckListener(listener: (ReminderItem, Boolean) -> Unit) {
        onSubItemCheckListener = listener
    }


}