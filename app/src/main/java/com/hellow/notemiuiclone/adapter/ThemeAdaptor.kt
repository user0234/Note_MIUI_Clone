package com.hellow.notemiuiclone.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hellow.notemiuiclone.databinding.ThemeListItemBinding
import com.hellow.notemiuiclone.models.noteModels.ThemeItem

class ThemeAdaptor(private val firstSelectedItem: Int) :
    RecyclerView.Adapter<ThemeAdaptor.ThemeViewHolder>() {

    inner class ThemeViewHolder(val binding: ThemeListItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    var currentSelected: Int = firstSelectedItem


    private val differCallBack = object : DiffUtil.ItemCallback<ThemeItem>() {


        override fun areContentsTheSame(oldItem: ThemeItem, newItem: ThemeItem): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: ThemeItem, newItem: ThemeItem): Boolean {
            return oldItem.editTextColor == newItem.editTextColor
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {

        val binding: ThemeListItemBinding =
            ThemeListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ThemeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        // set the data on and changes per item

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(position)
            }
        }

        holder.binding.ivThemeItem.setBackgroundColor(Color.parseColor(currentItem.backGroundColor))


        if (position == currentSelected) {
            val scale: Float = holder.itemView.context.resources.displayMetrics.density

            holder.binding.root.strokeWidth = 7
        } else {
            holder.binding.root.strokeWidth = 0
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }
}