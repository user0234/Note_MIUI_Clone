package com.hellow.notemiuiclone.adapter

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
import com.hellow.notemiuiclone.utils.Utils.getStatusTextColor

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

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {

        val currentItem = reminderDiffer.currentList[position]

        holder.binding.reminderTitleMain.text = currentItem.title

        if (currentItem.TimerTime == "") {
            holder.binding.reminderTime.visibility = View.GONE
        } else {
            holder.binding.reminderTime.text = currentItem.TimerTime
        }

        if (currentItem.reminderStatus == ReminderStatus.NotDone) {
            holder.binding.checkBoxMain.isChecked = false
            holder.binding.reminderTitleMain.setTextColor(Color.parseColor(getStatusTextColor(false)))
        } else {
            holder.binding.reminderTitleMain.setTextColor(
                Color.parseColor(
                    getStatusTextColor(true)
                )
            )
            holder.binding.checkBoxMain.isChecked = true
        }

        if (currentItem.itemsList.isEmpty()) {
            holder.binding.llCountBox.visibility = View.GONE
        } else {
            holder.binding.llCountBox.visibility = View.VISIBLE
            holder.binding.itemCount.text =
                "${currentItem.checkedCount}/${currentItem.itemsList.size}"

            val adaptor = ReminderSubItemAdaptor()
                holder.binding.rvReminderList.adapter = adaptor
                 holder.binding.rvReminderList.layoutManager = LinearLayoutManager(
                     holder.itemView.context,
                     LinearLayoutManager.VERTICAL,false)
                 holder.binding.rvReminderList.setHasFixedSize(true)
               adaptor.setOnItemClickListener { isChecked,subListPosition ->

                   if(isChecked){

                       currentItem.checkedCount = currentItem.checkedCount + 1
                       if(currentItem.checkedCount == currentItem.itemsList.size){
                           currentItem.reminderStatus = ReminderStatus.DoneWhole
                           currentItem.isExpended = false
                           currentItem.itemsList[subListPosition].isDone = true

                           onItemClickListener?.let {
                               it(currentItem,true)
                           }
                       }else{
                           currentItem.itemsList[subListPosition].isDone = true
                       }

                   }else{
                       if(currentItem.checkedCount == currentItem.itemsList.size){
                           currentItem.checkedCount = currentItem.checkedCount - 1
                           currentItem.reminderStatus = ReminderStatus.NotDone
                           currentItem.itemsList[subListPosition].isDone = false

                           onItemClickListener?.let {
                               it(currentItem,true)
                           }
                       } else{
                           currentItem.checkedCount = currentItem.checkedCount - 1
                           currentItem.itemsList[subListPosition].isDone = false
                       }

                   }
                   holder.binding.itemCount.text =
                       "${currentItem.checkedCount}/${currentItem.itemsList.size}"

                   adaptor.reminderSubItemDiffer.submitList(currentItem.itemsList)
               }
             adaptor.reminderSubItemDiffer.submitList(currentItem.itemsList)

        }

        holder.binding.ivShowListButton.setOnClickListener {

            if (currentItem.isExpended) {

                currentItem.isExpended = false
                holder.binding.ivShowListButton.animate()
                    .setDuration(400)
                    .rotation(-180F)
                    .start()
                holder.binding.rvReminderList.visibility = View.VISIBLE

            } else {

                currentItem.isExpended = true
                holder.binding.ivShowListButton.animate()
                    .setDuration(400)
                    .rotation(0F)
                    .start()
                holder.binding.rvReminderList.visibility = View.GONE

            }

        }

        holder.binding.checkBoxMain.setOnClickListener {

            if (currentItem.reminderStatus == ReminderStatus.DoneWhole) {
                currentItem.reminderStatus = ReminderStatus.NotDone
                currentItem.isExpended = false
                currentItem.checkedCount = 0

                for (each in currentItem.itemsList){
                    each.isDone = true
                }

                onItemClickListener?.let {
                    it(currentItem,true)
                }

            } else {

                currentItem.reminderStatus = ReminderStatus.DoneWhole
                currentItem.isExpended = false
                currentItem.checkedCount = currentItem.itemsList.size

                for (each in currentItem.itemsList){
                    each.isDone = false
                }

                onItemClickListener?.let {
                    it(currentItem,true)
                }
            }
        }

        holder.itemView.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {

                    MotionEvent.ACTION_DOWN -> {
                        holder.itemView.animate()
                            .scaleY(0.83F)
                            .scaleX(0.83F)
                            .setDuration(300)
                            .start()
                    }

                    MotionEvent.ACTION_UP -> {
                        holder.itemView.animate()
                            .scaleX(1F)
                            .scaleY(1F)
                            .setDuration(300)
                            .start()
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        holder.itemView.animate()
                            .scaleX(1F)
                            .scaleY(1F)
                            .setDuration(300)
                            .start()
                    }
                }
                return v?.onTouchEvent(event) ?: true
            }

        })

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                 it(currentItem,false)
            }
        }
    }

    //if we should update the element or update the item and open the dialog for edit

    private var onItemClickListener: ((ReminderItem,Boolean) -> Unit)? = null

    fun setOnItemClickListener(listener: (ReminderItem,Boolean) -> Unit) {
        onItemClickListener = listener
    }
}