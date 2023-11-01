package com.hellow.notemiuiclone.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.ReminderSubItemDialogAdaptor
import com.hellow.notemiuiclone.databinding.DialogReminderLayoutBinding
import com.hellow.notemiuiclone.models.ReminderItem
import com.hellow.notemiuiclone.models.ReminderStatus
import com.hellow.notemiuiclone.models.ReminderSubItem
import com.hellow.notemiuiclone.utils.Utils.GreyColor
import com.hellow.notemiuiclone.utils.Utils.YellowColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


abstract class CreateReminderDialog(
    context: Context,
) : Dialog(context, R.style.material_dialog) {


    private lateinit var binding: DialogReminderLayoutBinding
    private lateinit var adaptor: ReminderSubItemDialogAdaptor
    var mainTextValue: String = ""
    var date: String = ""
    var time: String = ""
    var dateTime: LocalDateTime = LocalDateTime.now()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())

        binding = DialogReminderLayoutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setUpDialogBody()
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView()
        setUpAdaptorClick()
        setUpView()
    }

    private fun setUpView() {

        binding.etMain.addTextChangedListener {
            if (it.isNullOrBlank()) {
                // set grey
                binding.buttonDone.setTextColor(Color.parseColor(GreyColor))
            } else {
                // set yellow
                binding.buttonDone.setTextColor(Color.parseColor(YellowColor))
            }
            val text = it.toString()
            if (text.contains("\n") && text.isNotBlank()) {
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

                binding.llSubItem.visibility = View.VISIBLE
                adaptor.reminderSubItemDiffer.submitList(
                    listOf(
                        ReminderSubItem(textCurrent, false, 0),
                        ReminderSubItem(textNext, false, 1)
                    )
                )
                binding.etMain.setText("")
                mainTextValue = ""
                adaptor.setItemFocus(textNext.length)
                binding.checkBoxDialog.visibility = View.GONE
                binding.etMain.hint = "Checklist of subtasks"
            } else {
                if (text.contains("\n")) {
                    binding.etMain.setText("")
                }
            }

        }

        binding.buttonDone.setOnClickListener {
            // create a new reminder from all the data
            cancel()
        }

        binding.mainCancelable.setOnClickListener {
            cancel()
        }
        binding.etMain.requestFocus()
    }

    override fun cancel() {

        val id = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")).toString()

        binding.checkBoxDialog.visibility = View.VISIBLE
        binding.etMain.visibility = View.VISIBLE


        // take timer time from button setReminderButton

        val subItemList = if (binding.llSubItem.visibility == View.VISIBLE) {
            adaptor.reminderSubItemDiffer.currentList
        } else {
            adaptor.reminderSubItemDiffer.currentList.subList(0, 0)
        }
        val title = if (subItemList.isNotEmpty()) {
            ""
        } else {
            binding.etMain.text.toString()
        }
        val reminderItem = ReminderItem(
            id, title, reminderStatus = ReminderStatus.NotDone, isExpended = false,
            TimerTime = "Set reminder", checkedCount = 0, itemsList = subItemList
        );

        // create the new reminder with adding a alarm for that time
        if (title == "" && subItemList.isEmpty()) {
            onItemDone(null, dateTime)
        } else {
            onItemDone(reminderItem, dateTime)
        }
        super.cancel()
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
                    binding.checkBoxDialog.visibility = View.VISIBLE
                    binding.etMain.visibility = View.VISIBLE

                    binding.llSubItem.visibility = View.GONE
                    binding.etMain.setText(
                        adaptor.reminderSubItemDiffer.currentList[0].name
                    )
                    binding.checkBoxDialog.isChecked =
                        adaptor.reminderSubItemDiffer.currentList[0].isDone
                    adaptor.reminderSubItemDiffer.submitList(emptyList())
                    binding.etMain.requestFocus()
                    binding.etMain.setSelection(adaptor.reminderSubItemDiffer.currentList[0].name.length)
                    binding.checkBoxDialog.visibility = View.VISIBLE
                    binding.etMain.hint = "Tap Enter to create subtasks"

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

    private fun setUpDialogBody() {
        window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
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

    protected abstract fun onItemDone(item: ReminderItem?, time: LocalDateTime)

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
                dateTime.plusYears((year - mYear).toLong())
                dateTime.plusMonths((month - mMonth).toLong())
                dateTime.plusDays((dayOfMonth - mDay).toLong())
            }, mYear, mMonth, mDay
        )

        datePickerDialog.show()

    }

    private fun timePicker(date: String) {

        val c: Calendar = Calendar.getInstance()
        val mHour = c.get(Calendar.HOUR_OF_DAY)
        val mMin = c.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context, { _, hourOfDay, minute ->
            time = "$hourOfDay:$minute"

            dateTime.plusHours((hourOfDay - mHour).toLong())
            dateTime.plusMinutes((minute - mMin).toLong())

        }, mHour, mMin, false)
        timePickerDialog.show()

    }
}
