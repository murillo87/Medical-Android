package com.appamedix.makula.scenes.note

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.constants.Const
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.worker.datamodel.note.*
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import com.appamedix.makula.worker.speech.SpeechSynthesizerListener
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_note_scene.*
import java.util.*

class NoteSceneActivity : BaseActivity(), NavigationViewListener, SpeechSynthesizerListener {

    companion object {
        const val NOTE_MODEL = "note_model"
    }

    private var isLandscape: Boolean = false
    private var synthesizer: SpeechSynthesizer? = null

    private lateinit var noteEntry: EditText
    private var appointmentDate: Date = Date()

    private var noteObject: NoteObject? = null
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_scene)

        realm = Realm.getDefaultInstance()

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        // Initialize synthesizer
        synthesizer = SpeechSynthesizer(this)

        // Get instances of UI elements.
        noteEntry = findViewById(R.id.note_entry)

        // Setup navigation
        val navigationCellModel = NavigationCellModel(
                getString(R.string.noteTitle),
                R.color.white,
                isLandscape,
                false,
                true,
                ImageButtonType.Back,
                true,
                ImageButtonType.Speaker,
                this)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)
        navigation.setupView(navigationCellViewModel)

        requestDisplayData()

        // Apply large style to UI elements in landscape mode.
        if (isLandscape) {
            noteEntry.textSize = Const.Font.content2Large
            navigation.layoutParams.height = 0
        }

        // Register call back listeners.
        noteEntry.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                // Clear the hint text.
                noteEntry.hint = ""
                stopSpeaking()
            }
        }
        noteEntry.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                noteEntry.hint = getText(R.string.notePlaceholder)
                noteEntry.clearFocus()
                stopSpeaking()
                updateNote(noteEntry.text.toString())
                hideKeyboard(view)
            }
            false
        }
    }

    override fun onStop() {
        super.onStop()

        stopSpeaking()
        updateNote(noteEntry.text.toString())
    }

    override fun onDestroy() {
        synthesizer?.destroy()
        realm.close()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    private fun requestDisplayData() {
        intent.extras?.let {
            val dataModel = it.getParcelable<NoteModel>(NOTE_MODEL)
            appointmentDate = dataModel?.date ?: Date()
        }

        val noteObjects = realm.getNoteObjects(appointmentDate)
        if (noteObjects == null) {
            // No object for the day, create one.
            noteObject = realm.createNoteObject(appointmentDate)
            if (noteObject == null) databaseWriteError()
        } else {
            // Found an existing object for the day.
            noteObject = noteObjects.first()
        }

        noteObject?.let {
            noteEntry.setText(it.content)

            // Make speech data.
            val speechData = SpeechSynthesizer.SpeechData(it.content ?: "", -1)
            synthesizer?.setSpeechData(arrayListOf(speechData))
        }
    }

    private fun updateNote(content: String) {
        val noteObject = this.noteObject ?: return

        if (!realm.updateNoteObject(noteObject, content.trim())) {
            databaseWriteError()
        }
    }

    /**
     * Shows database error
     */
    private fun databaseWriteError() {
        Toast.makeText(this, R.string.databaseWriteErrorMessage, Toast.LENGTH_SHORT).show()
    }

    /**
     * Stops synthesizer if speaking.
     */
    private fun stopSpeaking() {
        synthesizer?.let { synthesizer ->
            if (synthesizer.isSpeaking()) {
                synthesizer.stopSpeaking()
            }
        }
    }

    /**
     * Hides the keyboard.
     *
     * @param view: The focused view.
     */
    private fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /* Navigation view listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {
        synthesizer?.let { synthesizer ->
            if (synthesizer.isSpeaking()) {
                synthesizer.stopSpeaking()
            } else {
                synthesizer.startSpeaking()
            }
        }
    }

    /* SpeechSynthesizer listener */

    override fun speechStarted(data: SpeechSynthesizer.SpeechData) {
    }

    override fun speechEnded(data: SpeechSynthesizer.SpeechData) {
    }

    override fun speechFinished() {
    }
}
