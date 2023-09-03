package com.hellow.notemiuiclone.ui.editActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hellow.notemiuiclone.models.NoteItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditCreateViewModel(app: Application,private val repository: NotesRepository, private val currentItem:NoteItem) : AndroidViewModel(app) {



    private var _title = MutableLiveData<String>(currentItem.title)
 val titleLiveData:LiveData<String>
  get() = _title

 fun changeTitle(value:String){
  if(_title.value!=value){
  _title.value = value
  }
 }

 private var _theme = MutableLiveData<Int>(currentItem.themeId)
 val themeLiveData:LiveData<Int>
  get() = _theme

 fun changeTheme(value:Int){
  if(_theme.value!=value){
   _theme.value = value
  }
 }

 fun onActivityDestroy() {
        if(_title.value == ""){
         viewModelScope.launch {
          repository.deleteNote(currentItem)
         }

        }else{
         currentItem.title = _title.value!!
         currentItem.recentChangeDate = LocalDateTime.now()
          .format(DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")).toString()

         viewModelScope.launch {
          // update the values of this item
          repository.updateNote(currentItem)
         }
        }
 }

}