package com.hellow.notemiuiclone.adapter.viewHolder

import android.view.KeyEvent
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.EditDescriptionItemAdaptor
import com.hellow.notemiuiclone.databinding.EditDescriptionCheckboxItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionImageItemBinding
import com.hellow.notemiuiclone.databinding.EditDescriptionTextItemBinding
import com.hellow.notemiuiclone.models.NoteDescItem
import com.hellow.notemiuiclone.utils.Utils


class EditDescriptionTextItemViewHolder(binding: EditDescriptionTextItemBinding,
                                        callback: EditDescriptionItemAdaptor.Callback) :
    RecyclerView.ViewHolder(binding.root) {

    private val contentEdt = binding.etText

    init {
      contentEdt.setOnKeyListener(object : View.OnKeyListener {

          // this will be used to delete the current item
          override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
              return true
          }

      })

        // this will be used to let everyone know which item has focus in the list
        contentEdt.setOnFocusChangeListener {  _, hasFocus ->

            if(hasFocus){

            }
        }

        // this will be used add new item to the list of this same type
        contentEdt.addTextChangedListener {

        }

    }

    // this will be used to set text to the existing views
    fun bind(item: NoteDescItem){
         contentEdt.setText(item.text)
    }

}

class EditDescriptionCheckBoxItemViewHolder(binding: EditDescriptionCheckboxItemBinding,
                                            callback: EditDescriptionItemAdaptor.Callback) :
    RecyclerView.ViewHolder(binding.root) {

    private val contentEdt = binding.etText
    private val checkBox = binding.checkBox

    init {
         checkBox.setOnCheckedChangeListener { _, isChecked ->
             if(isChecked){

             }else{

             }

         }

        contentEdt.setOnKeyListener(object : View.OnKeyListener {

            // this will be used to delete the current item
            override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
                return true
            }

        })

        // this will be used to let everyone know which item has focus in the list
        contentEdt.setOnFocusChangeListener {  _, hasFocus ->

            if(hasFocus){

            }
        }

        // this will be used add new item to the list of this same type
        contentEdt.addTextChangedListener {

        }

    }

    // this will be used to set text to the existing views
    fun bind(item: NoteDescItem) {
        contentEdt.setText(item.text)
        checkBox.isChecked = item.isChecked
    }

}

class EditDescriptionImageItemViewHolder(binding: EditDescriptionImageItemBinding,
                                         callback: EditDescriptionItemAdaptor.Callback) :
    RecyclerView.ViewHolder(binding.root)  {
    private val descriptionText = binding.descriptionText
    private val image = binding.btViewImage

    init {

        binding.btViewImage.setOnClickListener {
            // make the buttons visible if they are invisible or vice versa
        }

        binding.btViewImage.setOnClickListener {
            // start the gallery with open image
        }

        binding.btAddDescription.setOnClickListener {
            // start a dialog with text input and set the result in the description item
        }

        binding.btResize.setOnClickListener {
            // make the size small with animations and make the check as small when started

        }
    }

    fun bind(item: NoteDescItem) {
        descriptionText.text = item.text

      val imageValue:String =  Utils.getImage(item.imageFile)

        Glide
            .with(image)
            .load(imageValue)
            .centerCrop()
            .placeholder(R.drawable.image_place_holder)
            .into(image)
    }
    }