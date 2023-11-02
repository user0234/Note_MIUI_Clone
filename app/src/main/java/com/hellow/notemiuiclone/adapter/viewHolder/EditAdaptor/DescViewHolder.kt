package com.hellow.notemiuiclone.adapter.viewHolder.EditAdaptor

import android.content.res.Resources
import android.graphics.Color
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.audioPlayer.MyAudioPlayer
import com.hellow.notemiuiclone.databinding.EditDescriptionAudioItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionCheckboxItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionImageItemBinding
import com.hellow.notemiuiclone.models.noteModels.NoteSubItem
import com.hellow.notemiuiclone.models.noteModels.NoteSubItemType
import com.hellow.notemiuiclone.models.noteModels.ThemeItem
import com.hellow.notemiuiclone.utils.LoggingClass
import com.hellow.notemiuiclone.utils.Utils
import com.hellow.notemiuiclone.utils.showKeyboard

sealed interface EditFocusableViewHolder {
    fun setFocus(pos: Int)
    fun showCheckBox()
}

sealed interface EditImageViewHolder {
    fun addDescription(value: String)
    fun hideImageButton()
}

sealed interface EditAudioViewHolder {
    fun stopPlaying()
}

class CheckBoxItemViewHolder(
    binding: EditDescriptionCheckboxItemBinding,
    callback: EditAdaptor.Callback,
) : RecyclerView.ViewHolder(binding.root), EditFocusableViewHolder {

    private val editText = binding.etText
    private val checkBox = binding.checkBox
    private var itemValue: NoteSubItem? = null

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

    fun bind(item: NoteSubItem, themeItem: ThemeItem) {
        itemValue = item
        editText.setText(item.textValue)
        checkBox.isChecked = item.checkBox
        if (item.type == NoteSubItemType.String) {
            checkBox.visibility = View.GONE
        } else {
            checkBox.visibility = View.VISIBLE
        }
        // set themeValue
        editText.setTextColor(Color.parseColor(themeItem.editTextColor))
        editText.setHintTextColor(Color.parseColor(themeItem.hintTextColor))
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
    val binding: EditDescriptionImageItemBinding,
    callback: EditAdaptor.Callback,
) :
    RecyclerView.ViewHolder(binding.root), EditImageViewHolder {

    private val descriptionText = binding.descriptionText
    private val image = binding.imageView
    private var itemValue: NoteSubItem? = null
    private var isShrunk: Boolean = false
    private var buttonList = binding.buttonsView

    init {

        binding.imageView.setOnClickListener {
            LoggingClass.logTagI("focusImage","focus set on image")
                binding.buttonsView.visibility = View.VISIBLE
                binding.imageView.strokeWidth = 5f
            callback.imageItemFocused(itemValue!!.id)
        }

        binding.btViewImage.setOnClickListener {
            binding.buttonsView.visibility = View.GONE
            binding.imageView.strokeWidth = 0f

            // start the gallery with open image
            if (itemValue != null && itemValue!!.imageUri != null)
                callback.imageToGallery(itemValue!!.imageUri!!, binding.root.context)
        }

        binding.btAddDescription.setOnClickListener {
            binding.buttonsView.visibility = View.GONE
            binding.imageView.strokeWidth = 0f
            // start a dialog with text input and set the result in the description item
            if (itemValue != null) {
                callback.addDescriptionToImage(itemValue!!.id, itemValue!!.imageDescription)
            }
        }

        binding.descriptionText.setOnClickListener {
            binding.buttonsView.visibility = View.GONE
            binding.imageView.strokeWidth = 0f


            if (itemValue != null) {
                callback.addDescriptionToImage(itemValue!!.id, itemValue!!.imageDescription)
            }
        }

        binding.btResize.setOnClickListener {
            binding.buttonsView.visibility = View.GONE
            binding.imageView.strokeWidth = 0f

            // TODO shrink the size with animation

            if (isShrunk) {
                binding.imageView.animate()
                    .translationX(0f)
                    .translationY(0f)
                    .withStartAction {
                        val res: Resources = binding.root.context.resources
                        val height =
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                200f, res.displayMetrics
                            ).toInt()
                        binding.root.layoutParams.height = height;
                    }
                    .withEndAction {
                        binding.imageView.animate()
                            .scaleY(1F)
                            .scaleX(1F)

                            .setDuration(100)
                            .start()
                    }
                    .setDuration(100)
                    .start()
                isShrunk = false
                binding.btResize.setImageResource(R.drawable.edit_description_image_compress_item)
            } else {
                binding.imageView.animate()
                    .scaleY(0.5F)
                    .scaleX(0.5F)
                    .withEndAction {
                        binding.imageView.animate()
                            .translationX(-210f)
                            .translationY(-150f)
                            .withEndAction {
                                val res: Resources = binding.root.context.resources
                                val height =
                                    TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        100f, res.displayMetrics
                                    ).toInt()
                                binding.root.layoutParams.height = height
                            }
                            .setDuration(100)
                            .start()
                    }
                    .setDuration(100)
                    .start()

                isShrunk = true

                binding.btResize.setImageResource(R.drawable.edit_description_image_expand_item)
            }
            // make the size small with animations and make the check as small when started
            // TODO put the image at the end of the view

        }

        binding.btDelete.setOnClickListener {
            binding.buttonsView.visibility = View.GONE
            binding.imageView.strokeWidth = 0f
            if (itemValue != null)
                callback.deleteImageItem(itemValue!!)
        }
    }

    fun bind(item: NoteSubItem) {
        itemValue = item

        if (item.imageDescription != null && item.imageDescription != "") {
            descriptionText.text = item.textValue
            descriptionText.visibility = View.VISIBLE
        }

        Log.i("list_image_item", "name:${itemValue?.imageFileName}   uri:${itemValue?.imageUri}  ")

        Glide
            .with(image)
            .load(Uri.parse(item.imageUri))
            .fitCenter()
            .placeholder(R.drawable.image_place_holder)
            .into(image)

    }

    override fun addDescription(value: String) {
        // TODO do this with animation
        if (value.isNotBlank()) {
            descriptionText.text = value
            descriptionText.visibility = View.VISIBLE
        } else {
            descriptionText.visibility = View.GONE
        }

    }

    override fun hideImageButton() {
        LoggingClass.logTagI("imageHideTag","imageHide reach")
        binding.buttonsView.visibility = View.GONE
        binding.imageView.strokeWidth = 0f
    }
}

class EditDescriptionAudioItemViewHolder(
   val binding: EditDescriptionAudioItemBinding,
    callback: EditAdaptor.Callback,
) : RecyclerView.ViewHolder(binding.root) ,EditAudioViewHolder{

    private val timerText = binding.timer
    var player: MyAudioPlayer? = null
    var playing:Boolean = false
    private var currentItem:NoteSubItem? = null
    init {
        binding.btPlay.setOnClickListener {
            // play using the uri
            if(currentItem!= null){
                if(!playing){
                    player = MyAudioPlayer(binding.root.context.applicationContext)
                    player!!.playFile(Uri.parse(currentItem!!.audioFileUri),binding.visualizer)
                    binding.btPlay.setImageResource(R.drawable.baseline_stop_24)
                    playing = true
                    player!!.setonPlayerStop {
                        binding.btPlay.setImageResource(R.drawable.audio_item_play_button)
                        playing = false
                    }
                    player!!.setonPlayAmplitude { timer ->

                        if (timer != null) {
                            binding.timer.text = Utils.getTimer((timer/1000))
                        }else {
                            binding.timer.text = currentItem!!.audioLength.toString()
                        }
                    }
                }else {
                    player?.stop()
                    binding.btPlay.setImageResource(R.drawable.audio_item_play_button)
                    playing = false
                    player = null
                    binding.timer.text = currentItem!!.audioLength.toString()
                }
            }

        }
        binding.btDelete.setOnClickListener {
            // delete the item using uri
            callback.deleteAudioItem(currentItem!!)
        }

    }

    fun bind(item: NoteSubItem) {
        currentItem = item
        timerText.text = (item.audioLength!!/1000).toString()
    }

    override fun stopPlaying() {
         if(player!=null){
             player?.stop()
             binding.btPlay.setImageResource(R.drawable.audio_item_play_button)
             playing = false
             player = null
             binding.timer.text = currentItem!!.audioLength.toString()
         }
    }


}