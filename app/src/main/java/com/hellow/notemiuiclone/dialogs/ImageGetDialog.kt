package com.hellow.notemiuiclone.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.databinding.DialogAddImageBinding
import com.hellow.notemiuiclone.databinding.DialogImageDescriptionBinding

abstract class ImageGetDialog (context: Context
) : Dialog(context, R.style.material_dialog){

    private lateinit var binding: DialogAddImageBinding
    private var buttonId:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())
        binding = DialogAddImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpDialogBody()
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpView()

    }

    fun setUpView() {
        binding.btCamera.setOnClickListener {
            // if the text is not empty then finish the dialog and send text
            buttonId = 0
            cancel()
        }
        binding.mainCancelable.setOnClickListener {
             cancel()
        }
        binding.btGallery.setOnClickListener {
            // if the text is not empty then finish the dialog and send text
            buttonId = 1
            cancel()
        }
    }

    override fun cancel() {
       onItemDone(buttonId)
        super.cancel()
    }

    private fun setUpDialogBody() {
        window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    protected abstract fun onItemDone(item: Int)
}