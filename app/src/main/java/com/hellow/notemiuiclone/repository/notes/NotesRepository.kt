package com.hellow.notemiuiclone.repository.notes

import com.hellow.notemiuiclone.database.AppDataBase
import com.hellow.notemiuiclone.models.noteModels.NoteItem
import com.hellow.notemiuiclone.models.noteModels.ThemeItem
import com.hellow.notemiuiclone.utils.ConstantValues

class NotesRepository(
    private val dataBase: AppDataBase
) : RepositoryNotesBluePrint {

    override suspend fun createNote(note: NoteItem) = dataBase.notesDao().insert(note)

    override fun getNotes() = dataBase.notesDao().getNotesList()

    override suspend fun deleteNote(note: NoteItem) = dataBase.notesDao().delete(note)

    override suspend fun updateNote(note: NoteItem) = dataBase.notesDao().update(note)

    override fun getTheme(num:Int, nightMode:Boolean): ThemeItem {
        return if(nightMode){
            ConstantValues.getNightModeTheme(num)
        }else{
            ConstantValues.getLightModeTheme(num)
        }
    }

    override fun getAllTheme():List<ThemeItem>{
        return  ConstantValues.themeList
    }

}