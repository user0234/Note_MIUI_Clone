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
import com.hellow.notemiuiclone.models.noteModels.NoteSubDataItem
import com.hellow.notemiuiclone.models.noteModels.NoteSubItemType
import com.hellow.notemiuiclone.models.noteModels.ThemeItem
import com.hellow.notemiuiclone.utils.LoggingClass
import com.hellow.notemiuiclone.utils.Utils
import com.hellow.notemiuiclone.utils.showKeyboard

sealed interface EditFocusableViewHolder {
    fun setFocus(pos: Int)
    fun showCheckBox()
    fun changeSize(increase: Boolean)
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
    val callback: EditAdaptor.Callback,
) : RecyclerView.ViewHolder(binding.root), EditFocusableViewHolder {

    private val editText = binding.etText
    private val checkBox = binding.checkBox
    private var itemValue: NoteSubDataItem? = null
    private var isBoldChecked: Boolean = true
    private var currentText: String = ""
    private var currentSize: Float? = null

    init {
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    callback.focusGain(pos)
                }
                // we got focus on this item
            } else {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    callback.focusLose(pos, currentText, currentSize ?: 18F)
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

                /***
                 * todo the boldness we use before and count , where count = 1, before 0
                 */


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
                        callback.newItemAdded(pos, textCurrent, textNext,itemValue)
                    }
                }
            }

        })
        editText.setOnKeyListener { _, keyCode, _ ->
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

    fun bind(item: NoteSubDataItem, themeItem: ThemeItem) {
        itemValue = item
        currentSize = item.textSize
        editText.textSize = item.textSize

        currentText = item.textValue

        editText.setText(currentText)
        checkBox.isChecked = item.checkBox
        if (item.type == NoteSubItemType.String) {
            checkBox.visibility = View.GONE
        } else {
            checkBox.visibility = View.VISIBLE
        }
        // set themeValue
        editText.setTextColor(Color.parseColor(themeItem.editTextColor))
        editText.setHintTextColor(Color.parseColor(themeItem.hintTextColor))

        if (item.id == 0) {
            /***
             * set the hint text for first position
             */
            editText.hint = "Start Typing ..."
        } else {
            /***
             * hide the hint for other position
             */
            editText.hint = ""
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
            itemValue?.type = NoteSubItemType.CheckBox
        } else {
            checkBox.visibility = View.GONE
            itemValue?.type = NoteSubItemType.String

        }
    }

    /***
     * if @param increase is true then we increase not more then x sp
     * else we decrease not less then y sp
     * we change by a factor of 2
     */
    override fun changeSize(increase: Boolean) {
        if (increase) {
            if (currentSize != 26F) {
                val newSize = currentSize?.plus(2F) ?: 18F
                currentSize = newSize
                editText.textSize = newSize
                callback.changeTextSize(newSize)
            }

        } else {
            if (currentSize != 16F) {
                val newSize = currentSize?.minus(2F) ?: 18F
                currentSize = newSize
                editText.textSize = newSize
                callback.changeTextSize(newSize)


            }
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
    private var itemValue: NoteSubDataItem? = null
    private var buttonList = binding.buttonsView

    init {

        binding.imageView.setOnClickListener {
            LoggingClass.logTagI("focusImage", "focus set on image")
            binding.buttonsView.visibility = View.VISIBLE
            val res: Resources = binding.root.context.resources
            val strokeWidth =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10f, res.displayMetrics
                ).toFloat()
            binding.imageView.strokeWidth = strokeWidth
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


            if (binding.imageResizeLayout.visibility == View.VISIBLE) {
                binding.imageResizeLayout.animate()
                    .scaleX(0f)
                    .scaleY(0f)

                    .withEndAction {
                        binding.imageResizeLayout.visibility = View.GONE
                    }
                    .setDuration(300)
                    .start()

            } else {
                binding.imageView.animate()
                    .scaleY(1F)
                    .scaleX(1F)
                    .withStartAction {
                        binding.imageResizeLayout.visibility = View.VISIBLE
                    }
                    .setDuration(300)
                    .start()

            }
        }

        binding.btDelete.setOnClickListener {
            binding.buttonsView.visibility = View.GONE
            binding.imageView.strokeWidth = 0f
            if (itemValue != null)
                callback.deleteImageItem(itemValue!!)
        }
    }

    fun bind(item: NoteSubDataItem) {
        itemValue = item

        if (item.imageDescription != null && item.imageDescription != "") {
            descriptionText.text = item.textValue
            descriptionText.visibility = View.VISIBLE
        }

        Log.i("list_image_item", "name:${itemValue?.imageFileName}   uri:${itemValue?.imageUri}  ")


        // TODO add so we can have better height of the image

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
        LoggingClass.logTagI("imageHideTag", "imageHide reach")
        binding.buttonsView.visibility = View.GONE
        binding.imageView.strokeWidth = 0f
    }
}

class EditDescriptionAudioItemViewHolder(
    val binding: EditDescriptionAudioItemBinding,
    callback: EditAdaptor.Callback,
) : RecyclerView.ViewHolder(binding.root), EditAudioViewHolder {

    private val timerText = binding.timer
    private var player: MyAudioPlayer? = null
    private var playing: Boolean = false
    private var currentItem: NoteSubDataItem? = null

    init {
        binding.btPlay.setOnClickListener {
            // play using the uri
            if (currentItem != null) {
                if (!playing) {
                    player = MyAudioPlayer(binding.root.context.applicationContext)
                    player!!.playFile(Uri.parse(currentItem!!.audioFileUri), binding.visualizer)
                    binding.btPlay.setImageResource(R.drawable.baseline_stop_24)
                    playing = true
                    player!!.setonPlayerStop {
                        binding.btPlay.setImageResource(R.drawable.audio_item_play_button)
                    }
                    player!!.setonPlayAmplitude { timer ->

                        if (timer != null) {
                            binding.timer.text = Utils.getTimer((timer / 1000))
                        } else {
                            binding.timer.text = Utils.getTimer((currentItem?.audioLength ?: 0)/1000)
                        }
                    }
                } else {
                    player?.stop()
                    binding.btPlay.setImageResource(R.drawable.audio_item_play_button)
                    playing = false
                    player = null
                    binding.timer.text = Utils.getTimer((currentItem?.audioLength ?: 0)/1000)
                }
            }

        }
        binding.btDelete.setOnClickListener {
            // delete the item using uri
            callback.deleteAudioItem(currentItem!!)
        }

    }

    fun bind(item: NoteSubDataItem) {
        currentItem = item
        timerText.text = Utils.getTimer((currentItem?.audioLength ?: 0)/1000)
    }

    override fun stopPlaying() {
        if (player != null) {
            player?.stop()
            binding.btPlay.setImageResource(R.drawable.audio_item_play_button)
            playing = false
            player = null
            binding.timer.text = Utils.getTimer((currentItem?.audioLength ?: 0)/1000)
        }
    }


}