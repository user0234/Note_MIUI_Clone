package com.hellow.notemiuiclone.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hellow.notemiuiclone.databinding.NotesListItemBinding
import com.hellow.notemiuiclone.models.noteModels.NoteDataItem
import com.hellow.notemiuiclone.utils.ConstantValues
import com.hellow.notemiuiclone.utils.Utils
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    inner class NotesViewHolder(val binding: NotesListItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    // for faster change in view
    private val differCallBack = object : DiffUtil.ItemCallback<NoteDataItem>() {
        override fun areItemsTheSame(oldItem: NoteDataItem, newItem: NoteDataItem): Boolean {
            return oldItem.id == newItem.id && oldItem.recentChangeDate == newItem.recentChangeDate && oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: NoteDataItem, newItem: NoteDataItem): Boolean {
            return oldItem == newItem && oldItem.recentChangeDate == newItem.recentChangeDate
        }

    }

    val notesDiffer = AsyncListDiffer(this, differCallBack)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): NotesAdapter.NotesViewHolder {
        return NotesViewHolder(
            NotesListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotesAdapter.NotesViewHolder, position: Int) {

        val currentItem = notesDiffer.currentList[position]
        holder.binding.tvTitle.text = currentItem.title

        val noteDesc = currentItem.descriptionText

        holder.binding.tvSubtitle.text = noteDesc

        val currentTime =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a"))
                .toString()
        holder.binding.tvTime.text =
            Utils.dateFormatterNotesList(currentItem.recentChangeDate, currentTime)

        val themeItem = ConstantValues.getThemeNoteList(holder.itemView.context,currentItem.themeId)
        holder.binding.root.setCardBackgroundColor(
            (Color.parseColor(
                themeItem.noteBackgroundColor
            ))
        )
        holder.binding.tvTime.setTextColor(
            (Color.parseColor(
                themeItem.timeTextColor
            ))
        )

        holder.binding.tvTitle.setTextColor(
            (Color.parseColor(
                themeItem.editTextColor
            ))
        )

        holder.binding.tvSubtitle.setTextColor(
            (Color.parseColor(
                themeItem.timeTextColor
            ))
        )
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(currentItem)
            }
        }

        holder.itemView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {

                    MotionEvent.ACTION_DOWN -> {

                        holder.itemView.animate()
                            .scaleY(0.83F)
                            .scaleX(0.83F)
                            .setDuration(70)
                            .start()

                    }

                    MotionEvent.ACTION_UP -> {

                        holder.itemView.animate()
                            .scaleX(1F)
                            .scaleY(1F)
                            .setDuration(70)
                            .start()
                    }

                    MotionEvent.ACTION_CANCEL -> {

                        holder.itemView.animate()
                            .scaleX(1F)
                            .scaleY(1F)
                            .setDuration(70)
                            .start()

                    }
                }
                return v?.onTouchEvent(event) ?: true
            }

        })
    }

    override fun getItemCount(): Int {
        return notesDiffer.currentList.size
    }

    private var onItemClickListener: ((NoteDataItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (NoteDataItem) -> Unit) {
        onItemClickListener = listener
    }

}