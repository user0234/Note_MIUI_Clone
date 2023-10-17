package com.hellow.notemiuiclone.ui.editActivity1

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellow.notemiuiclone.adapter.EditDescriptionItemAdaptor
import com.hellow.notemiuiclone.models.noteModels.NoteItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.utils.Event
import com.hellow.notemiuiclone.utils.send
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditCreateViewModel(
    app: Application, private val repository: NotesRepository,
    private val currentItem: NoteItem,
) : ViewModel(), EditDescriptionItemAdaptor.Callback {


    private var _title = MutableLiveData<String>(currentItem.title)
    val titleLiveData: LiveData<String>
        get() = _title

    fun changeTitle(value: String) {
        if (_title.value != value) {
            _title.value = value
        }
    }

    private var _theme = MutableLiveData<Int>(currentItem.themeId)
    val themeLiveData: LiveData<Int>
        get() = _theme

    fun changeTheme(value: Int) {
        if (_theme.value != value) {
            _theme.value = value
        }
    }

    fun onActivityDestroy() {
        if (_title.value == "") {
            viewModelScope.launch {
                repository.deleteNote(currentItem)
            }

        } else {
            currentItem.title = _title.value!!
            currentItem.recentChangeDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")).toString()

            viewModelScope.launch {
                // update the values of this item
                repository.updateNote(currentItem)
            }
        }
    }

    override fun focusLose(pos: Int, text: String) {
        TODO("Not yet implemented")
    }

    override fun focusGain(pos: Int) {
        TODO("Not yet implemented")
    }

    override fun textChanged(pos: Int, text: String) {
        TODO("Not yet implemented")
    }

    override fun newItemAdded(pos: Int, textCurrent: String, textNext: String) {
        TODO("Not yet implemented")
    }

    override fun itemDeleted(pos: Int, text: String) {
        TODO("Not yet implemented")
    }

    override fun checkChanged(pos: Int, isChecked: Boolean) {
        TODO("Not yet implemented")
    }

    fun openImageIngal(content:Context,uri: Uri){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri,"image/png")
        }
        ContextCompat.startActivity(content, intent, null)
    }


    // to handle focus request
    private val _focusEvent = MutableLiveData<Event<FocusChange>>()
    val focusEvent: LiveData<Event<FocusChange>>
        get() = _focusEvent

    data class FocusChange(val itemPos: Int, val pos: Int, val itemExists: Boolean)

    // to focus at the item pointed
    private fun focusItemAt(pos: Int, textPos: Int, itemExists: Boolean) {
        _focusEvent.send(FocusChange(pos, textPos, itemExists))
    }



}