package com.hellow.notemiuiclone.ui.editActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hellow.notemiuiclone.models.NoteItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository

class EditCreateViewModel(app: Application, repository: NotesRepository) : AndroidViewModel(app) {


  private var _title = MutableLiveData<String>("")
 val titleLiveData:LiveData<String>
  get() = _title

 fun changeTitle(value:String){
  if(_title.value!=value){
  _title.value = value
  }
 }

 private var _theme = MutableLiveData<Int>(0)
 val themeLiveData:LiveData<Int>
  get() = _theme

 fun changeTheme(value:Int){
  if(_theme.value!=value){
   _theme.value = value
  }
 }

 private var _currentNoteItem = MutableLiveData<NoteItem>()
   fun getCurrentNoteItem(item: NoteItem){
    if(_currentNoteItem.value!=item){
     _currentNoteItem.value = item
    }
   }





}