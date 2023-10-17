package com.hellow.notemiuiclone.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.databinding.DialogImageDescriptionBinding
import com.hellow.notemiuiclone.utils.showKeyboard

abstract class AddImageDescriptionDialog(context: Context,val text:String
) : Dialog(context, R.style.material_dialog){

    private lateinit var binding: DialogImageDescriptionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        binding = DialogImageDescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpDialogBody()
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpView()

    }

     fun setUpView() {
         binding.buttonDone.setOnClickListener {
             // if the text is not empty then finish the dialog and send text
             cancel()
         }
         binding.mainCancelable.setOnClickListener {
             binding.editText.setText("")
             cancel()
         }
         binding.editText.setText(text)
         binding.editText.requestFocus()
         binding.editText.showKeyboard()
         binding.editText.setSelection(text.length)

     }

    override fun cancel() {

        val textValue = binding.editText.text.toString()
        if(textValue.isBlank()){
            // send null
            onItemDone("")
        }else{
            // send text and finish
            onItemDone(textValue)
        }
        super.cancel()
    }

    private fun setUpDialogBody() {
        window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    protected abstract fun onItemDone(item: String)
}