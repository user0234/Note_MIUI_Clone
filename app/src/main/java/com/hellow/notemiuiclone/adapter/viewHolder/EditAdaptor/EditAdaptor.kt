package com.hellow.notemiuiclone.adapter.viewHolder.EditAdaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hellow.notemiuiclone.databinding.EditDescriptionAudioItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionCheckboxItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionImageItemBinding
import com.hellow.notemiuiclone.models.noteModels.NoteSubItem
import com.hellow.notemiuiclone.models.noteModels.NoteSubItemType
import com.hellow.notemiuiclone.models.noteModels.ThemeItem
import com.hellow.notemiuiclone.ui.editActivity.CreatEditViewModel

class EditAdaptor(themeItem: ThemeItem, private val callback: Callback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        /***
         * Log tags
         */
        const val CHECK_TAG = "check tag value"
        const val SIZE_TAG = "size tag"
    }

    private var pendingFocusChange: CreatEditViewModel.FocusChange? = null

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    private val differCallBack = object : DiffUtil.ItemCallback<NoteSubItem>() {
        override fun areItemsTheSame(oldItem: NoteSubItem, newItem: NoteSubItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteSubItem, newItem: NoteSubItem): Boolean {
            return oldItem.type == newItem.type && oldItem.id == newItem.id && newItem.checkBox == oldItem.checkBox && newItem.textValue.length == oldItem.textValue.length && newItem.textValue == oldItem.textValue
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)
    var focusItemPosition: Int = -1
    var noteItemTheme: ThemeItem = themeItem

    // this to keep up the theme info to change text color
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            NoteSubItemType.CheckBox.ordinal -> CheckBoxItemViewHolder(
                EditDescriptionCheckboxItemBinding.inflate(inflater, parent, false),
                callback
            )

            NoteSubItemType.Image.ordinal -> EditDescriptionImageItemViewHolder(
                EditDescriptionImageItemBinding.inflate(inflater, parent, false),
                callback
            )

            NoteSubItemType.String.ordinal -> CheckBoxItemViewHolder(
                EditDescriptionCheckboxItemBinding.inflate(inflater, parent, false),
                callback
            )

            NoteSubItemType.Audio.ordinal -> EditDescriptionAudioItemViewHolder(
                EditDescriptionAudioItemBinding.inflate(inflater, parent, false), callback
            )

            else -> {
                error("Unknown view type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem: NoteSubItem = differ.currentList[position]
        when (holder) {
            is CheckBoxItemViewHolder -> {
                holder.bind(currentItem, noteItemTheme)
            }

            is EditDescriptionImageItemViewHolder -> {
                holder.bind(currentItem)
            }

            is EditDescriptionAudioItemViewHolder -> {
                holder.bind(currentItem)
            }

        }

        if (holder is EditFocusableViewHolder && position == pendingFocusChange?.itemPos) {
            // Apply pending focus change event.
            holder.setFocus(pendingFocusChange!!.pos)
            pendingFocusChange = null
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].type.ordinal
    }

    fun changeCheckVisibility(checkboxVisible: CreatEditViewModel.CheckBoxVisibility) {

        Log.i(CHECK_TAG,"pending focus - ${pendingFocusChange?.pos?:"null"} , check pos - ${checkboxVisible.pos}")
        val rcv = recyclerView ?: return
        val viewHolder = rcv.findViewHolderForAdapterPosition(checkboxVisible.pos)
        if (viewHolder is EditFocusableViewHolder) {
            viewHolder.showCheckBox()
        } else {
            if(pendingFocusChange!=null){
                val viewHolder2 = rcv.findViewHolderForAdapterPosition(pendingFocusChange!!.pos)
                if (viewHolder2 is EditFocusableViewHolder) {
                    viewHolder2.showCheckBox()
                }else{
                    return
                }
            }
        }
    }

    fun changeTextSize(changeSizeType:CreatEditViewModel.ChangeSizeEventData){
        Log.i(SIZE_TAG,"pending focus - ${pendingFocusChange?.pos?:"null"} , check pos - ${changeSizeType.pos}")

        val rcv = recyclerView ?: return
        val viewHolder = rcv.findViewHolderForAdapterPosition(changeSizeType.pos)
        if (viewHolder is EditFocusableViewHolder) {
            viewHolder.changeSize(changeSizeType.change)
        } else {
            if(pendingFocusChange!=null){
                val viewHolder2 = rcv.findViewHolderForAdapterPosition(pendingFocusChange!!.pos)
                if (viewHolder2 is EditFocusableViewHolder) {
                    viewHolder2.changeSize(changeSizeType.change)
                }else{
                    return
                }
            }
        }
    }

    fun setDescriptionToView(descItem: CreatEditViewModel.DescriptionTextView) {
        val rcv = recyclerView ?: return
        val viewHolder = rcv.findViewHolderForAdapterPosition(descItem.id)
        if (viewHolder is EditImageViewHolder) {
            viewHolder.addDescription(descItem.currentText)
        } else {
            return
        }
    }

    fun setItemFocus(focus: CreatEditViewModel.FocusChange) {
        val rcv = recyclerView ?: return

        // If item to focus on doesn't exist yet, save it for later.
        if (!focus.itemExists) {
            pendingFocusChange = focus
            return
        }

        val viewHolder = rcv.findViewHolderForAdapterPosition(focus.itemPos)
        if (viewHolder is EditFocusableViewHolder) {
            viewHolder.setFocus(focus.pos)
        } else {
            // No item view holder for that position.
            // Not supposed to happen, but if it does, just save it for later.
            pendingFocusChange = focus
        }
    }

    fun hideImageButton(pos: CreatEditViewModel.HideImageButton) {
        // hide the image button of the current item
        val rcv = recyclerView ?: return
        val viewHolder = rcv.findViewHolderForAdapterPosition(pos.pos)
        if (viewHolder is EditImageViewHolder) {
            viewHolder.hideImageButton()
        }
    }

    fun stopThePlayer(pos:Int){
        val rcv = recyclerView ?: return
        val viewHolder = rcv.findViewHolderForAdapterPosition(pos)
        if (viewHolder is EditAudioViewHolder) {
            viewHolder.stopPlaying()
        }
    }

    interface Callback {

        fun newItemAdded(pos: Int, textCurrent: String, textNext: String)

        fun itemDeleted(pos: Int, text: String)

        fun focusLose(pos: Int, text: String,textSize:Float)

        fun changeTextSize(textSize: Float)

        fun focusGain(pos: Int)

        fun checkChanged(pos: Int, isChecked: Boolean)

        fun textChanged(pos: Int, text: String)

        fun imageToGallery(fileUri: String, context: Context)

        fun addDescriptionToImage(id: Int, currentText: String?)

        fun deleteImageItem(item: NoteSubItem)

        fun deleteAudioItem(item: NoteSubItem?)

        fun imageItemFocused(pos: Int)

    }

}

