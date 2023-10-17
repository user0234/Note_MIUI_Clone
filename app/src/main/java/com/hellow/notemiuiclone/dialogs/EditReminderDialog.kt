package com.hellow.notemiuiclone.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.ReminderSubItemDialogAdaptor
import com.hellow.notemiuiclone.databinding.DialogReminderLayoutBinding
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.ReminderStatus
import com.hellow.notemiuiclone.models.ReminderSubItem

abstract class EditReminderDialog(
    context: Context,
    private val currentItem: ReminderItem,
) : Dialog(context, R.style.material_dialog) {

    private lateinit var binding: DialogReminderLayoutBinding
    private lateinit var adaptor: ReminderSubItemDialogAdaptor
    var date:String = ""
    var time:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())

        binding = DialogReminderLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCanceledOnTouchOutside(true)
        setCancelable(true)

        setUpDialogBody()
        setUpRecyclerView()
        setUpItemInView()
        setUpListener()
        setUpAdaptorClick()

    }

    private fun setUpListener() {
        binding.etMain.setOnKeyListener { _, keyCode, _ ->
            val currentText = binding.etMain.text.toString()
            if (keyCode == KeyEvent.KEYCODE_ENTER && currentText != "") {

                binding.llMainItem.visibility = View.GONE
                binding.llSubItem.visibility = View.VISIBLE
                adaptor.reminderSubItemDiffer.submitList(
                    listOf(
                        ReminderSubItem(currentText, binding.checkBoxDialog.isChecked, 0),
                        ReminderSubItem("", false, 1)
                    )
                )

            }

            true
        }

        binding.buttonDone.setOnClickListener {
            // create a new reminder from all the data
            val id = currentItem.idDate
            val title = if (binding.llMainItem.visibility == View.GONE) {
                "Checklist of subtasks"
            } else {
                binding.etMain.text.toString()
            }
            // take timer time from button setReminderButton
            val timerTime = binding.setReminderButton.text.toString()

            val subItemList = if (binding.llMainItem.visibility == View.GONE) {
                adaptor.reminderSubItemDiffer.currentList
            } else {
                adaptor.reminderSubItemDiffer.currentList.subList(0, 0)
            }

            val reminderItem = ReminderItem(
                id, title, reminderStatus = ReminderStatus.NotDone, isExpended = false,
                TimerTime = timerTime, checkedCount = 0, itemsList = subItemList
            )

            if(title==""){
                onItemDone(null)
            }else {
                onItemDone(reminderItem)
            }
             cancel()
        }

        binding.mainCancelable.setOnClickListener {
            cancel()
        }

        binding.resetReminderValue.setOnClickListener {
            binding.setReminderButton.text = "Set reminder"
            binding.resetReminderValue.visibility = View.GONE
        }

        binding.setReminderButton.setOnClickListener {
            // pick date and time for reminder and add a alarm for that time when cancel dialog
            datePicker()

        }


    }

    private fun setUpAdaptorClick() {
        adaptor.setOnItemClickListener { gonext ->

            if (gonext) {
                val reminderSubItemList: MutableList<ReminderSubItem> = mutableListOf()

                reminderSubItemList.addAll(adaptor.reminderSubItemDiffer.currentList)

                reminderSubItemList.add(ReminderSubItem("", false, reminderSubItemList.size))

                adaptor.reminderSubItemDiffer.submitList(reminderSubItemList)

            } else {

                if (adaptor.reminderSubItemDiffer.currentList.size <= 2) {
                    binding.llMainItem.visibility = View.VISIBLE
                    binding.llSubItem.visibility = View.GONE
                    binding.etMain.setText(
                        adaptor.reminderSubItemDiffer.currentList[0].name
                    )
                    binding.etMain.requestFocus()
                    binding.checkBoxDialog.isChecked = false

                } else {
                    adaptor.reminderSubItemDiffer.submitList(
                        adaptor.reminderSubItemDiffer.currentList.subList(
                            0,
                            adaptor.reminderSubItemDiffer.currentList.size - 1
                        )
                    )
                }
            }
        }
    }

    private fun setUpItemInView() {

        if (currentItem.itemsList.size > 1) {
            adaptor.reminderSubItemDiffer.submitList(currentItem.itemsList)
            binding.llMainItem.visibility = View.GONE
            binding.llSubItem.visibility = View.VISIBLE

        } else {
            binding.llMainItem.visibility = View.VISIBLE
            binding.llSubItem.visibility = View.GONE
            binding.etMain.setText(currentItem.title)
            binding.etMain.requestFocus()
            binding.checkBoxDialog.isChecked =
                currentItem.reminderStatus == ReminderStatus.DoneWhole

        }

        if(currentItem.TimerTime == "Set reminder"){
            binding.setReminderButton.text = "Set reminder"
            binding.resetReminderValue.visibility = View.GONE
        }else{
            binding.setReminderButton.text = currentItem.TimerTime
            binding.resetReminderValue.visibility = View.VISIBLE
        }
    }

    private fun setUpRecyclerView() {
        adaptor = ReminderSubItemDialogAdaptor()
        binding.rvSubItem.adapter = adaptor
        binding.rvSubItem.layoutManager = LinearLayoutManager(
            binding.root.context,
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvSubItem.setHasFixedSize(false)
    }

    private fun setUpDialogBody() {
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    protected abstract fun onItemDone(item: ReminderItem?)

    private fun datePicker() {

        // Get Current Date

        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            context, { view, year, month, dayOfMonth ->
                date = "$dayOfMonth-${(month + 1)}-$year"
                timePicker(date);

            }, mYear, mMonth, mDay
        )

        datePickerDialog.show()

    }

    private fun timePicker(date:String) {

        val c: Calendar = Calendar.getInstance()
        val mHour = c.get(Calendar.HOUR_OF_DAY)
        val mMin = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context, { _, hourOfDay, minute ->
            time = "$hourOfDay:$minute"

            binding.setReminderButton.text = "$date $time"
            binding.resetReminderValue.visibility = View.VISIBLE
        }, mHour, mMin, false);
        timePickerDialog.show()

    }

}