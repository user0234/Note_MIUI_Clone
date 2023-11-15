package com.hellow.notemiuiclone.ui.editActivity

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hellow.notemiuiclone.adapter.viewHolder.EditAdaptor.EditAdaptor
import com.hellow.notemiuiclone.models.noteModels.NoteItem
import com.hellow.notemiuiclone.models.noteModels.NoteSubItem
import com.hellow.notemiuiclone.models.noteModels.NoteSubItemType
import com.hellow.notemiuiclone.models.noteModels.ThemeItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.utils.Event
import com.hellow.notemiuiclone.utils.LoggingClass
import com.hellow.notemiuiclone.utils.send
import kotlinx.coroutines.launch
import timber.log.Timber

class CreatEditViewModel(
    val app: Application,
    private val repository: NotesRepository,
    private val currentItem: NoteItem,
) : AndroidViewModel(app), EditAdaptor.Callback {

    // to handle focus request
    private val _focusEvent = MutableLiveData<Event<FocusChange>>()
    val focusEvent: LiveData<Event<FocusChange>>
        get() = _focusEvent

    private val _focusGainEvent = MutableLiveData<Boolean>(false)
    val focusGainEvent: LiveData<Boolean>
        get() = _focusGainEvent

    private val _checkBoxVisibilityEvent = MutableLiveData<Event<CheckBoxVisibility>>()
    val checkBoxVisibilityEvent: LiveData<Event<CheckBoxVisibility>>
        get() = _checkBoxVisibilityEvent

    fun changeCheckBoxVisibility() {
        var currentCheck = "String"
        if (listItems[focusPosition].type == NoteSubItemType.CheckBox) {
            currentCheck = "CheckBox"
        }
        Log.i("Check Changed", "Current Check - ${currentCheck}")

        _checkBoxVisibilityEvent.send(CheckBoxVisibility(focusPosition))
        listItems[focusPosition].type =
            if (listItems[focusPosition].type == NoteSubItemType.String) {
                NoteSubItemType.CheckBox
            } else {
                NoteSubItemType.String
            }

        updateListItems()
    }


    private val _editItems =
        MutableLiveData(currentItem.description.toMutableList())
    val editItems: LiveData<out List<NoteSubItem>>
        get() = _editItems

    private val _title =
        MutableLiveData(currentItem.title)
    val title: LiveData<String>
        get() = _title

    fun setTitle(text: String) {
        _title.value = text
    }

    private var themeValue: Int = 0

    fun setThemeValue(value: Int) {
        themeValue = value
    }


    var focusPosition: Int = -1;
    var focusLastPositionForImage: Int = -1
    var imageButtonVisible:Int = -1

    override fun imageItemFocused(pos: Int) {
        hideImageButtons(pos)
    }

    private fun hideImageButtons(pos:Int){
        imageButtonVisible = if(imageButtonVisible!=pos && imageButtonVisible!= -1 &&listItems[pos].type == NoteSubItemType.Image){
            // send an event to hide that in list and set new imageButtonVisible
            triggerHideImageButton(imageButtonVisible)
            pos
        }else{
            pos
        }
    }


    private val _hideImageButtonEvent = MutableLiveData<Event<HideImageButton>>()
    val hideImageButtonEvent: LiveData<Event<HideImageButton>>
        get() = _hideImageButtonEvent


    private fun updateListItems() {
        var i = 0
        while (i < listItems.size) {
            listItems[i].id = i
            i++
        }
        _editItems.value = listItems.toMutableList()
    }

    private val listItems: MutableList<NoteSubItem> = currentItem.description.toMutableList()

    fun getThemeItem(value: Int): ThemeItem {

        return when (app.applicationContext.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                repository.getTheme(value, true)
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                repository.getTheme(value, false)
            }

            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                repository.getTheme(value, false)
            }

            else -> {
                repository.getTheme(value, false)
            }
        }
    }


    private fun focusItemAt(pos: Int, textPos: Int, itemExists: Boolean) {
        _focusEvent.send(FocusChange(pos, textPos, itemExists))
    }

    fun getAllThemes() = repository.getAllTheme()

    fun deleteNote() = viewModelScope.launch {
        repository.deleteNote(currentItem)
    }

    fun updateNote() {
        var descriptionTextValue: String = ""

        for (i in listItems) {
            if (i.textValue.isNotBlank()) {
                descriptionTextValue = i.textValue
                break
            }
        }

        Log.i("Description Text", "text desc - ${descriptionTextValue}")
        val currentNote = this.currentItem
        currentNote.title = title.value ?: ""
        currentNote.description = listItems
        currentNote.descriptionText = descriptionTextValue
        currentNote.themeId = themeValue
        viewModelScope.launch {
            repository.updateNote(currentNote)
        }

    }

    fun returnAllImageUri():ArrayList<Uri>?{
        val uriList: ArrayList<Uri> = ArrayList()

        for (i in listItems) {
            if (i.type == NoteSubItemType.Image) {
                uriList.add(Uri.parse(i.imageUri!!))
            }
        }

        return if(uriList.isEmpty()){
            null
        }else{
            uriList
        }
    }

    fun returnAllText():String {

        var textValue:String = ""
        textValue = title.value ?: ""
        for (i in listItems) {
            if (i.textValue.isNotBlank()) {
                textValue = textValue + "\n" + i.textValue
            }
        }

        return textValue
    }

    override fun newItemAdded(pos: Int, textCurrent: String, textNext: String) {

        val previousItemType = listItems[pos].type
        val item = NoteSubItem(pos + 1, previousItemType, false, textNext)

        Log.i(
            "Item Added",
            "current text :${textCurrent} , next text:${textNext} , type = ${previousItemType}"
        )

        listItems[pos].textValue = textCurrent

        if (pos == listItems.size) {
            listItems.add(item)
            updateListItems()
        } else {
            listItems.add((pos + 1), item)
            updateListItems()
        }

        focusItemAt(pos + 1, textNext.length, false)
    }

    override fun itemDeleted(pos: Int, text: String) {
        LoggingClass.logI("itemDelete started")
        // check if the previous item is an image or audio
        if (listItems[pos - 1].type == NoteSubItemType.Image || listItems[pos - 1].type == NoteSubItemType.Audio) {
            // delete the previous item and keep focus on current item
            val focusLoc = 0
            _deleteImageEvent.send(DeleteImageItem(listItems[pos - 1].imageFileName!!))
            listItems.removeAt(pos - 1)
            updateListItems()
            focusItemAt(pos - 1, focusLoc, true)
        } else {
            val focusLoc = listItems[pos - 1].textValue.length
            Timber.tag("Item Delete").i("text at  %s", listItems[pos - 1].textValue)

            val textValue = listItems[pos - 1].textValue + text
            listItems[pos - 1].textValue = textValue

            Log.i("Item Delete", "final text $textValue , selPos - $focusLoc")

            Log.i("Item Delete", "before remove ${listItems.size}")
            listItems.removeAt(pos)
            Log.i("Item Delete", "after remove ${listItems.size}")
            updateListItems()
            focusItemAt(pos - 1, focusLoc, true)
        }

    }

    fun resetFocusGainEvent() {
        _focusGainEvent.value = false
    }

    fun addNewAudioItem(audioItem: NoteSubItem) {
        LoggingClass.logI("pos${audioItem.id} , name${(audioItem.audioLength!! / 1000)} in second")
        listItems.add(audioItem.id, audioItem)  // added item at the position
        updateListItems()
    }


    override fun focusLose(pos: Int, text: String) {
        // focus lost
        Log.i("Focus Changed", "Focus lost - $pos")
        // listItems[pos].textValue = text
        updateListItems()

        focusPosition = -1
        focusLastPositionForImage = pos
    }

    override fun focusGain(pos: Int) {
        Log.i("Focus Changed", "Focus gain - $pos")
        // set the is focused in activity ui
        focusPosition = pos
        _focusGainEvent.value = true
         if(imageButtonVisible!=-1){
             triggerHideImageButton(imageButtonVisible)
             imageButtonVisible = -1
         }

    }

    fun triggerHideImageButton(pos:Int){
        LoggingClass.logTagI("imageHideTag","imageHide triggered")
        _hideImageButtonEvent.send(HideImageButton(pos))
    }

    override fun checkChanged(pos: Int, isChecked: Boolean) {
        listItems[pos].checkBox = isChecked
    }

    override fun textChanged(pos: Int, text: String) {
        // update the current text
        Log.i("text Value Changed", "Text value - ${text} ,  pos - ${pos}")
        listItems[pos].textValue = text
    }

    // trigger add description event
    private val _triggerAddDescriptionEvent = MutableLiveData<Event<DescriptionText>>()
    val triggerAddDescriptionEvent: LiveData<Event<DescriptionText>>
        get() = _triggerAddDescriptionEvent

    private fun triggerDescriptionDialog(descriptionItem: DescriptionText) {
        _triggerAddDescriptionEvent.send(descriptionItem)
    }

    private val _triggerAddDescriptionToViewEvent = MutableLiveData<Event<DescriptionTextView>>()
    val triggerAddDescriptionToViewEvent: LiveData<Event<DescriptionTextView>>
        get() = _triggerAddDescriptionToViewEvent


    fun setDescriptionToImage(item: String, id: Int) {
        // here we updated the description text in list stored in view model
        listItems[id].imageDescription = item
        updateListItems()

        // here we trigger the update in editAdaptor
        val descItem: DescriptionTextView = DescriptionTextView(id, item)

        _triggerAddDescriptionToViewEvent.send(descItem)

    }

    override fun imageToGallery(fileUri: String, context: Context) {
        openImageIngal(uri = fileUri, content = context)
    }

    // image button functions

    fun openImageIngal(content: Context, uri: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(uri), "image/png")
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        ContextCompat.startActivity(content, intent, null)
    }


    fun addNewImageItemToList(name: String, fileUri: Uri, position: Int) {

        LoggingClass.logI("pos$position , name$name")
        val imageItem =
            NoteSubItem(position, NoteSubItemType.Image, false, "", fileUri.toString(), "", name)
        listItems.add(position, imageItem)  // added item at the position
        updateListItems()
    }

    override fun addDescriptionToImage(id: Int, currentText: String?) {
        val descriptionItem = DescriptionText(id, currentText)
        triggerDescriptionDialog(descriptionItem)
    }

    override fun deleteImageItem(item: NoteSubItem) {
        listItems.removeAt(item.id)
        updateListItems()
        // delete the image from repository
        _deleteImageEvent.send(DeleteImageItem(item.imageFileName!!))
    }

    override fun deleteAudioItem(item: NoteSubItem?) {
        if (item != null) {
            listItems.removeAt(item.id)
            updateListItems()

            _deleteImageEvent.send(
                DeleteImageItem(
                    item.audioFileName!!
                )
            )
        }
    }



    private val _deleteImageEvent = MutableLiveData<Event<DeleteImageItem>>()
    val deleteImageEvent: LiveData<Event<DeleteImageItem>>
        get() = _deleteImageEvent


    data class DeleteImageItem(val name: String)

    data class DescriptionText(val id: Int, var currentText: String?)

    data class DescriptionTextView(val id: Int, var currentText: String)

    data class FocusChange(val itemPos: Int, val pos: Int, val itemExists: Boolean)

    data class CheckBoxVisibility(val pos: Int)
    data class HideImageButton(val pos: Int)

}