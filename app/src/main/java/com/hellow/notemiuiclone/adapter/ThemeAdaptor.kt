package com.hellow.notemiuiclone.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hellow.notemiuiclone.databinding.ThemeListItemBinding
import com.hellow.notemiuiclone.models.ThemeItemData

class ThemeAdaptor : RecyclerView.Adapter<ThemeAdaptor.ThemeViewHolder>() {

    inner class ThemeViewHolder(val binding:ThemeListItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<ThemeItemData>() {
        override fun areItemsTheSame(oldItem: ThemeItemData, newItem: ThemeItemData): Boolean {
            return oldItem.backgroundColor == newItem.backgroundColor
        }

        override fun areContentsTheSame(oldItem: ThemeItemData, newItem: ThemeItemData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
    private var currentSelected:Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ThemeAdaptor.ThemeViewHolder {
       val binding:ThemeListItemBinding = ThemeListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ThemeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ThemeAdaptor.ThemeViewHolder, position: Int) {
        val currentItem =  differ.currentList[position]
         holder.itemView.setOnClickListener {
             currentSelected = holder.adapterPosition
             onItemClickListener?.let { it(position) }
         }

          holder.binding.ivThemeItem.setBackgroundColor(Color.parseColor(currentItem.backgroundColor))

        if(position == currentSelected){
            holder.binding.root.strokeWidth = 7
        }else{
            holder.binding.root.strokeWidth = 0
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }
}