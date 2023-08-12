package com.hellow.notemiuiclone.utils

import com.hellow.notemiuiclone.models.NoteDescItem
import com.hellow.notemiuiclone.models.NoteDescItemType
import com.hellow.notemiuiclone.models.NoteItem
import com.hellow.notemiuiclone.models.NoteStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TestModels {

    val note1Desc1:NoteDescItem = NoteDescItem(NoteDescItemType.Text,"text one",false,null)
    val note1Desc2:NoteDescItem = NoteDescItem(NoteDescItemType.Text,"test second",false,null)
    val note1Desc3:NoteDescItem = NoteDescItem(NoteDescItemType.Text,"text three",false,null)

    val noteDesItemList: List<NoteDescItem> = listOf<NoteDescItem>(note1Desc1,note1Desc2,note1Desc3)
    val note1:NoteItem = NoteItem("id1","Note Item 1 ","none",noteDesItemList,0,NoteStatus.Alive,
        LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")).toString()
    )

    val note2Desc1:NoteDescItem = NoteDescItem(NoteDescItemType.Text,"text one",false,null)
    val note2Desc2:NoteDescItem = NoteDescItem(NoteDescItemType.Text,"test second",false,null)
    val note2Desc3:NoteDescItem = NoteDescItem(NoteDescItemType.Text,"text three",false,null)


    val note2:NoteItem = NoteItem("id2","Note Item 2 ","none",noteDesItemList,0,NoteStatus.Alive,LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")).toString())

    val note3Desc1:NoteDescItem = NoteDescItem(NoteDescItemType.Text,"text one",false,null )
    val note3Desc2:NoteDescItem = NoteDescItem(NoteDescItemType.Text,"test second",false,null   )
    val note3Desc3:NoteDescItem = NoteDescItem(NoteDescItemType.Text,"text three",false,null )


    val note3:NoteItem = NoteItem("id3","Note Item 3 ","none",noteDesItemList,0,NoteStatus.Alive,LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("EEEE, MMM dd, yyyy HH:mm:ss a")).toString())

}