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
import com.hellow.notemiuiclone.models.noteModels.NoteItem
import com.hellow.notemiuiclone.utils.Utils


class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    inner class NotesViewHolder(val binding: NotesListItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    // for faster change in view
    private val differCallBack = object : DiffUtil.ItemCallback<NoteItem>() {
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
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

//        val holderRes = holder.itemView.context.resources
//        val displayDensity = holderRes.displayMetrics.density

        var noteDesc = currentItem.descriptionText

        holder.binding.tvSubtitle.text = noteDesc
        holder.binding.tvTime.text = Utils.dateConvert(currentItem.recentChangeDate)
        holder.binding.root.setCardBackgroundColor(
            (Color.parseColor(
                Utils.backgroundColor(
                    currentItem.themeId
                )
            ))
        )
        holder.binding.tvTime.setTextColor(
            (Color.parseColor(
                Utils.subTitleColor(
                    currentItem.themeId
                )
            ))
        )

        holder.binding.tvTitle.setTextColor(
            (Color.parseColor(
                Utils.titleColor(
                    currentItem.themeId
                )
            ))
        )

        holder.binding.tvSubtitle.setTextColor(
            (Color.parseColor(
                Utils.subTitleColor(
                    currentItem.themeId
                )
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

    private var onItemClickListener: ((NoteItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (NoteItem) -> Unit) {
        onItemClickListener = listener
    }

}