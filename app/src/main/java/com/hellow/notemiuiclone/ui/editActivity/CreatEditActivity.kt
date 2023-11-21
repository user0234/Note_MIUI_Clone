package com.hellow.notemiuiclone.ui.editActivity

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hellow.notemiuiclone.BuildConfig
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.adapter.ThemeAdaptor
import com.hellow.notemiuiclone.adapter.viewHolder.EditAdaptor.EditAdaptor
import com.hellow.notemiuiclone.audioRecorder.AndroidAudioRecorder
import com.hellow.notemiuiclone.database.notesDatabase.NotesDataBase
import com.hellow.notemiuiclone.databinding.ActivityCreatEditBinding
import com.hellow.notemiuiclone.dialogs.AddImageDescriptionDialog
import com.hellow.notemiuiclone.dialogs.ImageGetDialog
import com.hellow.notemiuiclone.models.noteModels.NoteItem
import com.hellow.notemiuiclone.models.noteModels.NoteSubItem
import com.hellow.notemiuiclone.models.noteModels.NoteSubItemType
import com.hellow.notemiuiclone.repository.notes.NotesRepository
import com.hellow.notemiuiclone.utils.ConstantValues
import com.hellow.notemiuiclone.utils.LoggingClass
import com.hellow.notemiuiclone.utils.TakePictureWithUriReturnContract
import com.hellow.notemiuiclone.utils.Utils.NOTE_ITEM_LIST
import com.hellow.notemiuiclone.utils.Utils.getName
import com.hellow.notemiuiclone.utils.hideKeyboard
import com.hellow.notemiuiclone.utils.observeEvent
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import java.io.File
import java.io.IOException
import java.util.Objects
import java.util.UUID


class CreatEditActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    companion object {
        const val PERMISSION_AUDIO_REQUEST_CODE = 10
        const val PERMISSION_IMAGE_REQUEST_CODE = 11
    }

    private lateinit var viewBinding: ActivityCreatEditBinding
    private lateinit var viewModel: CreatEditViewModel
    private lateinit var adaptor: EditAdaptor
    private var noteItemReceived: NoteItem? = null
    private var isFocused = false
    private lateinit var themeAdapter: ThemeAdaptor
    private var imageTaken = false
    private val takeImageResult = registerForActivityResult(TakePictureWithUriReturnContract()) { (isSuccess, imageUri) ->
        if (isSuccess) {
            /***
             * we get the uri value
             */
            val filename = imageUri.getName(this)
            addImageToList(filename?:"noNameError",imageUri)
         }
    }

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { /***
         * we get the uri value
         */
            addImageToNote(uri)
        }
    }
    private lateinit var recorder: AndroidAudioRecorder
    private var isRecording: Boolean = false
    private var currentRecordingItem: NoteSubItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // set up view Binding
        viewBinding = ActivityCreatEditBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // get the note item passed
        if (intent.hasExtra(NOTE_ITEM_LIST)) {
            noteItemReceived = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(
                    NOTE_ITEM_LIST,
                    NoteItem::class.java
                )

            } else {
                intent.getParcelableExtra(NOTE_ITEM_LIST)

            }
            if(noteItemReceived==null){
                // ReInstall the App
                finish()
            }
        } else {
            // kill the activity if note item not found
            finish()
        }

        setUpViewModel()
        setUpToolBar()
        setUpViewData()
        setUpEditListAdaptor()
        setUpLiveData()
        setUpTitleData()
        setUpThemeDataAndAdaptor()
        //  setUpChangeItemType()
        setUpSoftInputButton()
        setUpSoftInputTextButtons()
        viewBinding.softInputAboutView.visibility = View.GONE
        changeThemeInView(noteItemReceived!!.themeId)
    }

    private fun setUpThemeDataAndAdaptor() {
        themeAdapter = ThemeAdaptor(noteItemReceived!!.themeId)

        viewBinding.listBackgroundTheme.adapter = themeAdapter
        viewBinding.listBackgroundTheme.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL, false
        )
        viewBinding.listBackgroundTheme.setHasFixedSize(true)
        themeAdapter.differ.submitList(viewModel.getAllThemes())
        themeAdapter.setOnItemClickListener { item ->
            changeTheme(item)
        }
    }

    private fun changeTheme(value: Int) {

        viewModel.setThemeValue(value)
        themeAdapter.currentSelected = value
        themeAdapter.notifyDataSetChanged()
        viewBinding.listBackgroundTheme.scrollToPosition(value)
        changeThemeInView(value)
    }

    private fun changeThemeInView(value: Int) {
        val item = viewModel.getThemeItem(value)
        // edit text colors
        viewBinding.etTitle.setTextColor(Color.parseColor(item.editTextColor))
        viewBinding.etTitle.setHintTextColor(Color.parseColor(item.hintTextColor))

        // time value color
        viewBinding.tvTime.setTextColor(Color.parseColor(item.hintTextColor))
        // whole background color
        viewBinding.root.setBackgroundColor(Color.parseColor(item.backGroundColor))
        // tool bar color
        viewBinding.toolbar.setBackgroundColor(Color.parseColor(item.toolBarColor))
        viewBinding.toolbar.navigationIcon!!.setColorFilter(
            Color.parseColor(item.editTextColor),
            PorterDuff.Mode.SRC_ATOP
        )
        // theme list background color
        viewBinding.listBackgroundTheme.setBackgroundColor(Color.parseColor(item.toolBarColor))
        // adding color to the keyboard shown item
        viewBinding.softInputAboutView.setBackgroundColor(Color.parseColor(item.toolBarColor))

        adaptor.noteItemTheme = item
        // to update the view theme
        adaptor.notifyDataSetChanged()
    }

    private fun setUpSoftInputTextButtons() {
        viewBinding.btIncreaseSize.setOnClickListener {
            /***
             * this will update the size of text by just send an trigger and the size parameters will be handled by holder itself
             */
                viewModel.increaseTextSize()
        }
        viewBinding.btDecreaseSize.setOnClickListener {
            /***
             * this will update the size of text by just send an trigger and the size parameters will be handled by holder itself
             */
            viewModel.decreaseTextSize()

        }

    }

    private fun setUpSoftInputButton() {
        /***
         * show or hide the check value in itemView
         */
        viewBinding.btCheckBox.setOnClickListener {
            viewModel.changeCheckBoxVisibility()
        }
        viewBinding.btImage.setOnClickListener {
            // TODO capture or get image and then save it to directory then add a new item
            val reminderDialog = object : ImageGetDialog(
                this,
            ) {

                override fun onItemDone(item: Int) {
                    when (item) {
                        0 -> {
                            startCameraToGetImage()
                        }

                        1 -> {
                            startGalleryToGetImage()
                        }
                    }
                }
            }
            reminderDialog.show()
        }

        viewBinding.btAudio.setOnClickListener {

            // TODO start audio recording and get the focus position , change the icon, start record timer in bottom
            if (hasMicPermission()) {
                audioPermissionAreAvailable()
            } else {
                requestMicPermission()
            }
        }

        /***
         * check if the trigger button text are visible
         * if visible then make them invisible and vice versa
         */
        viewBinding.btTextVisibility.setOnClickListener {
             if(viewBinding.triggerButtonsText.visibility == View.VISIBLE){
                 // hide
                 hideTextChangeSoftInput()
              }else{
                  // show
                 showTextChangeSoftInput()
             }
        }
    }

    private fun showTextChangeSoftInput(){
        viewBinding.triggerButtonsText.visibility = View.VISIBLE
        viewBinding.btTextVisibility.setImageResource(R.drawable.baseline_clear_24)
        viewBinding.triggerButtons.visibility = View.GONE
    }

    private fun hideTextChangeSoftInput(){
        viewBinding.triggerButtonsText.visibility = View.GONE
        viewBinding.btTextVisibility.setImageResource(R.drawable.baseline_format_size_24)
        viewBinding.triggerButtons.visibility = View.VISIBLE
    }

    private fun getFileName() = UUID.randomUUID().toString()

    private fun audioPermissionAreAvailable() {
        isRecording = if (isRecording) {
            // Stop recording
            if (currentRecordingItem != null) {
                currentRecordingItem!!.audioLength = recorder.stop()

                viewBinding.softInputAboutView.visibility = View.GONE
                viewBinding.audioRecordVisibility.visibility = View.GONE
                viewBinding.btAudio.setImageResource(R.drawable.baseline_mic_none_24)
                viewModel.addNewAudioItem(currentRecordingItem!!)
                currentRecordingItem = null
            }
            false
        } else {
            // Start recoding
            removeFocusAndAddTimerAtBottom()
            val focusPosition: Int = getFocusPosition()

            if (focusPosition != -1) {
                // stop the playing if anything is playing
                adaptor.stopThePlayer(focusPosition)
                // recoding is started here
                recorder = AndroidAudioRecorder(applicationContext)
                recorder.setonPlayAmplitude { amplitude, time ->
                    // get the updated amplitude and update that in the view
                    viewBinding.audioRecordTimer.addRecordAmp(amplitude!!, time)
                }
                val fileName = getFileName()
                val audioFile = File(filesDir, fileName)

                LoggingClass.logTagI("audioFileName", fileName)
                viewBinding.softInputAboutView.visibility = View.VISIBLE
                viewBinding.audioRecordVisibility.visibility = View.VISIBLE
                viewBinding.btAudio.setImageResource(R.drawable.baseline_stop_24)
                recorder.start(audioFile)
                currentRecordingItem = NoteSubItem(
                    focusPosition, NoteSubItemType.Audio, false, "", 18F, null, null, null,
                    fileName, audioFile.toUri().toString(), null
                )
                true
            } else {
                Toast.makeText(this, "SomeThing went wrong with focus", Toast.LENGTH_LONG).show()
                false
            }

        }
    }

    private fun getFocusPosition() = run {
        if (viewModel.focusLastPositionForImage != -1) {
            viewModel.focusLastPositionForImage
        } else {
            if (viewModel.focusPosition == -1) {
                viewModel.focusPosition
            } else {
                -1
            }
        }
    }


    /***
     * Image taken using camera
     */

    private fun deletePhotoFromInternalStorage(deleteItem: CreatEditViewModel.DeleteImageItem): Boolean {
        return try {
            deleteFile(deleteItem.name)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun startCameraToGetImage() {
        // if this fails then we will use the database to store image separately
        takeImageNewWay()
    }

    private fun takeImageNewWay(){
        val filename = getFileName()
        val fileUri = getNewFileUri(filename)
        takeImageResult.launch(fileUri)
    }


    private fun addImageToList(photoId: String,imageUri:Uri) {
        onMenuDoneButtonClicked() // remove focus

        val focusPosition: Int =
            if (viewModel.focusLastPositionForImage != -1) {
                viewModel.focusLastPositionForImage
            } else {
                if (viewModel.focusPosition == -1) {
                    viewModel.focusPosition
                } else {
                    -1
                }
            }
        if (focusPosition == -1) {
            // something went wrong and we cant get the last focus position somehow
            return
        }
            viewModel.addNewImageItemToList(photoId, imageUri, focusPosition)

    }

    private fun getNewFileUri(fileName:String):Uri {
        val tmpFile = File(getExternalFilesDir("/")!!,"$fileName.png")
        return FileProvider.getUriForFile(applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }


    private fun startGalleryToGetImage() {
        selectImageFromGalleryResult.launch("image/*")
    }

    private fun addImageToNote(uri:Uri){
        val filename = getFileName()
        val fileUri = getNewFileUri(filename)
        copyFile(uri,fileUri)
        addImageToList(filename,fileUri)
    }

    @Throws(IOException::class)
    private fun copyFile(pathFrom: Uri, pathTo: Uri) {
        contentResolver.openInputStream(pathFrom).use { `in` ->
            if (`in` == null) return
            contentResolver.openOutputStream(pathTo).use { out ->
                if (out == null) return
                // Transfer bytes from in to out
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
            }
        }
    }

    private fun setUpTitleData() {
        viewBinding.etTitle.addTextChangedListener {
            // set the text in viewModel
            viewModel.setTitle(it.toString())
        }
        viewBinding.etTitle.setText(noteItemReceived!!.title)
        viewBinding.etTitle.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                titleFocused()
            }
        }
    }

    private fun titleFocused() {
        isFocused = true
        changeThemeVisibility(false)
        setUpToolBar()
        viewBinding.softInputAboutView.visibility = View.GONE
        if (viewModel.imageButtonVisible != -1) {
            viewModel.triggerHideImageButton(viewModel.imageButtonVisible)
            viewModel.imageButtonVisible = -1
        }
    }

    private fun descFocused(focused: Boolean) {
        if (!focused) {
            return
        }
        isFocused = focused
        changeThemeVisibility(false)
        setUpToolBar()
        viewBinding.softInputAboutView.visibility = View.VISIBLE
    }

    private fun triggerObserver(descriptionItem: CreatEditViewModel.DescriptionText) {
        val textValue = descriptionItem.currentText ?: ""
        val reminderDialog = object : AddImageDescriptionDialog(
            this, textValue
        ) {
            override fun onItemDone(item: String) {
                // TODO add dialog text to the adaptor like focus
                viewModel.setDescriptionToImage(item, descriptionItem.id)
            }
        }
        reminderDialog.show()
    }

    private fun setUpLiveData() {

        viewModel.hideImageButtonEvent.observeEvent(this, adaptor::hideImageButton)

        viewModel.deleteImageEvent.observeEvent(this, this::deletePhotoFromInternalStorage)

        // trigger to update the description of the image at position
        viewModel.triggerAddDescriptionToViewEvent.observeEvent(this, adaptor::setDescriptionToView)

        viewModel.focusEvent.observeEvent(this, adaptor::setItemFocus)
        viewModel.focusGainEvent.observe(this) {
            Log.i("Focus Changed", "Focus - $it")
            descFocused(it)
        }
        // start description dialog
        viewModel.triggerAddDescriptionEvent.observeEvent(this, this::triggerObserver)

        viewModel.editItems.observe(this) {
            adaptor.differ.submitList(it)
        }

        viewModel.checkBoxVisibilityEvent.observeEvent(this, adaptor::changeCheckVisibility)

        viewModel.changeTextSizeEvent.observeEvent(this,adaptor::changeTextSize)
    }

    private fun setUpEditListAdaptor() {
        adaptor = EditAdaptor(ConstantValues.themeList[0], viewModel)
        viewBinding.rvDescription.adapter = adaptor

        viewBinding.rvDescription.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        // size will be changing
        viewBinding.rvDescription.setHasFixedSize(false)

    }


    // setUp initial data to the view
    private fun setUpViewData() {

        viewBinding.tvTime.text = ConstantValues.dateConvert(noteItemReceived!!.id)

    }


    private fun setUpViewModel() {
        val notesRepository = NotesRepository(NotesDataBase(this)!!)
        val viewModelProviderFactory =
            CreatEditViewModelProvider(application, notesRepository, noteItemReceived!!)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[CreatEditViewModel::class.java]
    }

    private fun setUpToolBar() {
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        if (isFocused) {
            inflater.inflate(R.menu.edit_note_et_focus_default_theme, menu)
        } else {
            inflater.inflate(R.menu.edit_note_menu_default_theme, menu)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_theme -> {
                changeThemeVisibility(
                    viewBinding.themeCardView.visibility != View.VISIBLE
                )
            }

            R.id.menu_item_delete -> {
                viewModel.deleteNote()
                finish()
            }

            R.id.menu_done -> {
                // remove focus from everything
                onMenuDoneButtonClicked()
            }

            R.id.menu_item_share_text -> {
                // share the note as text
                shareNoteText()

            }

            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getAllTextData(): String {

        return viewModel.returnAllText()

    }

    private fun getAllImageURiList(): ArrayList<Uri>? {
        return viewModel.returnAllImageUri()
    }

    private fun shareNoteText() {
        // TODO Images not working for whats app
        val textValue = getAllTextData()
        val imageUris: ArrayList<Uri>? = getAllImageURiList()

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND_MULTIPLE
            putExtra(Intent.EXTRA_TEXT, textValue)
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            type = "*/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, "${imageUris?.size} sdf")
        startActivity(shareIntent)
    }


    private fun changeThemeVisibility(isVisible: Boolean) {
        if (isVisible) {
            viewBinding.themeCardView.animate()
                .translationY(1F)
                .setDuration(300)
                .withStartAction {
                    viewBinding.themeCardView.visibility = View.VISIBLE
                }
                .start()

        } else {
            viewBinding.themeCardView.animate()
                .translationY(viewBinding.themeCardView.height.toFloat())
                .setDuration(300)
                .withEndAction {
                    viewBinding.themeCardView.visibility = View.GONE
                }
                .start()
        }
    }

    private fun removeFocusAndAddTimerAtBottom() {
        viewModel.resetFocusGainEvent()
        viewBinding.removeFocusText.requestFocus()
        isFocused = false
        setUpToolBar()
        viewBinding.removeFocusText.clearFocus()
        viewBinding.removeFocusText.hideKeyboard()
        viewBinding.softInputAboutView.visibility = View.GONE
    }

    private fun onMenuDoneButtonClicked() {
        viewModel.resetFocusGainEvent()
        viewBinding.removeFocusText.requestFocus()
        isFocused = false
        setUpToolBar()
        viewBinding.removeFocusText.clearFocus()
        viewBinding.removeFocusText.hideKeyboard()
        viewBinding.softInputAboutView.visibility = View.GONE
        hideTextChangeSoftInput()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        when (requestCode) {
            PERMISSION_AUDIO_REQUEST_CODE -> {
                if (hasMicPermission()) {
                    audioPermissionAreAvailable()
                }
            }

            PERMISSION_IMAGE_REQUEST_CODE -> {

            }
        }
    }
    private fun requestMicPermission() {
        EasyPermissions.requestPermissions(
            this, "Audio Permission is required for recording audio",
            PERMISSION_AUDIO_REQUEST_CODE, Manifest.permission.RECORD_AUDIO
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        when (requestCode) {
            PERMISSION_AUDIO_REQUEST_CODE -> {
                SettingsDialog.Builder(this).build().show()
            }

            PERMISSION_IMAGE_REQUEST_CODE -> {

            }
        }
    }
    private fun hasMicPermission() =
        EasyPermissions.hasPermissions(applicationContext, Manifest.permission.RECORD_AUDIO)

    override fun onStop() {
        viewModel.updateNote()
        super.onStop()
    }


}