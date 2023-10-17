package com.hellow.notemiuiclone.adapter.viewHolder

import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.EditDescriptionItemAdaptor
import com.hellow.notemiuiclone.adapter.viewHolder.EditAdaptor.EditFocusableViewHolder
import com.hellow.notemiuiclone.databinding.EditDescriptionAudioItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionCheckboxItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionImageItemBinding
import com.hellow.notemiuiclone.dialogs.AddImageDescriptionDialog
import com.hellow.notemiuiclone.models.NoteDescItem
import com.hellow.notemiuiclone.models.NoteDescItemType
import com.hellow.notemiuiclone.utils.showKeyboard


sealed interface EditFocusableViewHolder2 {
    fun setFocus(pos: Int)
    fun showCheckBox()
}

class EditDescriptionCheckBoxItemViewHolder(
    binding: EditDescriptionCheckboxItemBinding,
    callback: EditDescriptionItemAdaptor.Callback,
    isCheckBox:Boolean  // use this to change where required
) : RecyclerView.ViewHolder(binding.root), EditFocusableViewHolder2
{

    private val editText = binding.etText
    private val checkBox = binding.checkBox
    private var itemValue: NoteDescItem? = null

    init {
        editText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    callback.focusGain(pos)
                }
                // we got focus on this item
            } else {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    callback.focusLose(pos, editText.text.toString())
                }
                // we lost focus from this item

            }
        }
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    callback.textChanged(pos, s.toString())
                }

            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.contains("\n")) {
                    var newLinePos: Int = 0
                    while (newLinePos < text.length) {
                        if (text[newLinePos] == '\n') {
                            break
                        }
                        newLinePos++
                    }
                    val textCurrent = text.substring(0, newLinePos)
                    val textNext = if (newLinePos == text.length - 1) {
                        ""
                    } else {
                        text.substring(newLinePos + 1, text.length)
                    }

                    editText.setText(textCurrent)
                    editText.setSelection(textCurrent.length)
                    val pos = bindingAdapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        callback.newItemAdded(pos, textCurrent, textNext)
                    }
                }
            }

        })
        editText.setOnKeyListener { _, keyCode, keyEvent ->
            val isCursorAtStart =
                editText.selectionStart == 0 && editText.selectionStart == editText.selectionEnd

            if (keyCode == KeyEvent.KEYCODE_DEL && isCursorAtStart) {
                val pos = itemValue?.id ?: bindingAdapterPosition
                val text = editText.text.toString()

                if (pos != RecyclerView.NO_POSITION && pos != 0) {
                    callback.itemDeleted(pos, text)
                }
            }
            return@setOnKeyListener true
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            val pos = bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                callback.checkChanged(pos, isChecked)
            }
        }
    }

    fun bind(item: NoteDescItem) {
        itemValue = item
        editText.setText(item.text)
        checkBox.isChecked = item.isChecked
        if (item.type == NoteDescItemType.CheckBox) {
            checkBox.visibility = View.VISIBLE
        } else {
            checkBox.visibility = View.GONE
        }
    }

    override fun setFocus(pos: Int) {
        editText.requestFocus()
        editText.setSelection(pos)
        editText.showKeyboard()
    }

    override fun showCheckBox() {
        if (checkBox.visibility == View.GONE) {
            checkBox.visibility = View.VISIBLE
        } else {
            checkBox.visibility = View.GONE
        }
    }

}

class EditDescriptionImageItemViewHolder(
    binding: EditDescriptionImageItemBinding,
    callback: EditDescriptionItemAdaptor.Callback,
) :
    RecyclerView.ViewHolder(binding.root)
{

    private val descriptionText = binding.descriptionText
    private val image = binding.btViewImage
    private var itemValue: NoteDescItem? = null

    init {

        binding.imageView.setOnClickListener {
            // make the buttons visible if they are invisible or vice versa
            binding.buttonsView.visibility = View.VISIBLE
        }

        binding.btViewImage.setOnClickListener {
            // start the gallery with open image
//            if (itemValue != null)
//                callback.openImageInDifferentApp(itemValue?.imageFile!!, binding.root.context)
        }

        binding.btAddDescription.setOnClickListener {
            // start a dialog with text input and set the result in the description item
            val reminderDialog = object : AddImageDescriptionDialog(
                binding.root.context,
                binding.descriptionText.text.toString()
                ) {

                override fun onItemDone(item: String) {
                    binding.descriptionText.text = item
                    binding.descriptionText.visibility = View.VISIBLE
                }
            }
            reminderDialog.show()

        }

        binding.btResize.setOnClickListener {
            // make the size small with animations and make the check as small when started

        }
    }

    fun bind(item: NoteDescItem) {
        descriptionText.text = item.text
        if(item.text!!.isNotBlank()){

        }
        Glide
            .with(image)
            .load(item.imageFile)
            .centerCrop()
            .placeholder(R.drawable.image_place_holder)
            .into(image)
    }
}


class EditDescriptionAudioItemViewHolder(
    binding: EditDescriptionAudioItemBinding,
    callback: EditDescriptionItemAdaptor.Callback,
) : RecyclerView.ViewHolder(binding.root) {

  //  private val text = binding.audioSize

    init {
        binding.btPlay.setOnClickListener {
            // play using the uri
        }
        binding.btDelete.setOnClickListener {
            // delete the item using uri
        }
    }

    fun bind(item: NoteDescItem) {
    //    text.text = item.text
    }

}