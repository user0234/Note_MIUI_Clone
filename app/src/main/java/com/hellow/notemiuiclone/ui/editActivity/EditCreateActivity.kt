package com.hellow.notemiuiclone.ui.editActivity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellow.notemiuiclone.adapter.ThemeAdaptor
import com.hellow.notemiuiclone.database.notesDatabase.NotesDataBase
import com.hellow.notemiuiclone.databinding.ActivityEditCreateBinding
import com.hellow.notemiuiclone.models.NoteItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.utils.Utils.NOTE_ITEM_CREATE
import com.hellow.notemiuiclone.utils.Utils.NOTE_ITEM_LIST
import com.hellow.notemiuiclone.utils.Utils.themeListData

class EditCreateActivity : AppCompatActivity() {

    private lateinit var binding:ActivityEditCreateBinding
    private lateinit var viewModel:EditCreateViewModel
    private  var isNew:Boolean = false
    private lateinit var themeAdapter:ThemeAdaptor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = ActivityEditCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository: NotesRepository = NotesRepository(NotesDataBase(this)!!)
        val viewModelProviderFactory:EditCreateViewModelProviderFactory = EditCreateViewModelProviderFactory(
            application,repository)
         viewModel = ViewModelProvider(this,viewModelProviderFactory)[EditCreateViewModel::class.java]

        val intentValue:Intent = intent

        if(intentValue.hasExtra(NOTE_ITEM_LIST)|| intentValue.hasExtra(NOTE_ITEM_CREATE)) {

            if(intentValue.hasExtra(NOTE_ITEM_CREATE)){
                isNew = true
               val currentNoteItem = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intentValue.getSerializableExtra(NOTE_ITEM_CREATE, NoteItem::class.java)!!
                }else{
                    intentValue.getSerializableExtra(NOTE_ITEM_CREATE) as NoteItem
                }
                viewModel.getCurrentNoteItem(currentNoteItem)
            }

            if(intentValue.hasExtra(NOTE_ITEM_LIST)){
              val currentNoteItem = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intentValue.getSerializableExtra(NOTE_ITEM_LIST, NoteItem::class.java)!!
                }else{
                    intentValue.getSerializableExtra(NOTE_ITEM_LIST) as NoteItem
                }
                viewModel.getCurrentNoteItem(currentNoteItem)
            }


        }
        setUpToolBar()
        setUpTheme()

        viewModel.themeLiveData.observe(this) {
            setThemeToView(it)
            setUpToolBar()
        }
        setUpTitleViewListeners()
        binding.etTitle.setOnFocusChangeListener{ view,hasFocus ->
               if(hasFocus){

                   (applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                       view,
                       InputMethodManager.SHOW_FORCED
                   )
                   binding.appBar.visibility = View.GONE
               }else{

               }
        }
        binding.etTitle.addTextChangedListener {
              viewModel.changeTitle(it.toString())
        }
    }

    private fun setUpTitleViewListeners(){

    }

    private fun setUpToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    private fun setUpTheme(){
         themeAdapter = ThemeAdaptor()
         binding.listBackgroundTheme.adapter = themeAdapter
         binding.listBackgroundTheme.layoutManager = LinearLayoutManager(this,
             LinearLayoutManager.HORIZONTAL,false)
         binding.listBackgroundTheme.setHasFixedSize(true)
        themeAdapter.differ.submitList(themeListData)
         themeAdapter.setOnItemClickListener {
                viewModel.changeTheme(it)
         }
     }

    private fun setThemeToView(themeNumber:Int){
              val themeItem = themeListData[themeNumber]

              binding.root.setBackgroundColor(Color.parseColor(themeItem.backgroundColor))
              binding.listBackgroundTheme.setBackgroundColor(Color.parseColor(themeItem.backgroundColorSecondary))
              binding.addNewItemCard.setBackgroundColor(Color.parseColor(themeItem.backgroundColorSecondary))
              binding.toolbar.setBackgroundColor(Color.parseColor(themeItem.backgroundColorSecondary))
    }
}