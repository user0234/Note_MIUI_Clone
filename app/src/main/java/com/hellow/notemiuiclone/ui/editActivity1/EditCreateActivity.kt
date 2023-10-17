package com.hellow.notemiuiclone.ui.editActivity1

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.ThemeAdaptor
import com.hellow.notemiuiclone.database.notesDatabase.NotesDataBase
import com.hellow.notemiuiclone.databinding.ActivityEditCreateBinding
import com.hellow.notemiuiclone.models.noteModels.NoteItem
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.utils.Utils.NOTE_ITEM_CREATE
import com.hellow.notemiuiclone.utils.Utils.NOTE_ITEM_LIST
import com.hellow.notemiuiclone.utils.Utils.themeListData

class EditCreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditCreateBinding
    private lateinit var viewModel: EditCreateViewModel
    private var isNoteNew: Boolean = false
    private lateinit var themeAdapter: ThemeAdaptor
    private lateinit var currentItem: NoteItem
    private var isFocusTitle: Boolean = false
    private var isFocusDescriptionItem: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpIntentData()

        setUpViewModel()
        setUpToolBar()
        setUpTheme()

        setUpTitleViewListeners()

    }

    override fun onStop() {
        super.onStop()
        viewModel.onActivityDestroy()

    }

    private fun hideKeyboard() {

    }

    private fun showKeyBoard() {

    }

    private fun setUpIntentData() {
        val intentValue: Intent = intent
        if (intentValue.hasExtra(NOTE_ITEM_LIST) || intentValue.hasExtra(NOTE_ITEM_CREATE)) {

            if (intentValue.hasExtra(NOTE_ITEM_CREATE)) {
                isNoteNew = true
                currentItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        "noteItem",
                        NoteItem::class.java
                    )!!

                } else {
                    intent.getParcelableExtra("noteItem")!!

                }

            }

            if (intentValue.hasExtra(NOTE_ITEM_LIST)) {
                currentItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        "noteItem",
                        NoteItem::class.java
                    )!!

                } else {
                    intent.getParcelableExtra("noteItem")!!

                }
            }
        }
    }

    private fun setUpViewModel() {
        val repository: NotesRepository = NotesRepository(NotesDataBase(this)!!)
        val viewModelProviderFactory: EditCreateViewModelProviderFactory =
            EditCreateViewModelProviderFactory(
                application, repository, currentItem
            )
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[EditCreateViewModel::class.java]
    }

    private fun setUpTitleViewListeners() {

        binding.etTitle.setText(currentItem.title)
        binding.etTitle.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {

                (applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                    view,
                    InputMethodManager.SHOW_FORCED
                )
                binding.appBar.visibility = View.GONE
            } else {
                viewModel.changeTitle(binding.etTitle.text.toString())
            }
        }
        binding.etTitle.addTextChangedListener {
            viewModel.changeTitle(it.toString())
        }
    }

    private fun setUpToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.title = if (isNoteNew) {
            "Create"
        } else {
            "Modify"
        }
    }

    private fun setUpTheme() {
        themeAdapter = ThemeAdaptor(2)
        binding.listBackgroundTheme.adapter = themeAdapter
        binding.listBackgroundTheme.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )

        binding.listBackgroundTheme.setHasFixedSize(true)
        themeAdapter.differ.submitList(null)
        themeAdapter.setOnItemClickListener {
            viewModel.changeTheme(it)
        }

        viewModel.themeLiveData.observe(this) {
            setThemeToView(it)
            setUpToolBar()
        }

    }

    private fun setThemeToView(themeNumber: Int) {
        val themeItem = themeListData[themeNumber]
        // todo change icon for the change in theme for the button but for menu change the menu
        binding.btEmptyThemeDefault.setImageResource(R.drawable.audio_add_icon_theme_2)
        binding.root.setBackgroundColor(Color.parseColor(themeItem.backgroundColor))
        binding.listBackgroundTheme.setBackgroundColor(Color.parseColor(themeItem.primaryTextColor))
        binding.descriptionItemAddItemBarDefaultTheme.setBackgroundColor(Color.parseColor(themeItem.backgroundColor))
        binding.toolbar.setBackgroundColor(Color.parseColor(themeItem.primaryTextColor))

        // to set color to the back button in the action bar
        //    viewBinding.toolbar.navigationIcon!!.setColorFilter(Color.parseColor(item.editTextColor), PorterDuff.Mode.SRC_ATOP);

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_share_text -> {
                 // Todo add the whole text of description item in one text as sub text flag and title as title flag
            }

            R.id.menu_item_theme -> {
                 // TODO check if theme is visible or not and then show or hide
            }

            R.id.menu_item_delete -> {
                // TODO mark the note item as deleted // status
            }

            R.id.menu_done -> {
               // TODO remove the focus from all the Edit text
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater

        when (themeAdapter.currentSelected) {
            0 -> {
                if (isFocusTitle || isFocusDescriptionItem) {
                    inflater.inflate(R.menu.edit_note_et_focus_default_theme, menu)
                } else {
                    inflater.inflate(R.menu.edit_note_menu_default_theme, menu)
                }
            }

            1 -> {
                if (isFocusTitle || isFocusDescriptionItem) {
                    inflater.inflate(R.menu.edit_note_et_focus_theme_2, menu)
                } else {
                    inflater.inflate(R.menu.edit_note_menu_theme_2, menu)
                }
            }
        }

        return true
    }
}