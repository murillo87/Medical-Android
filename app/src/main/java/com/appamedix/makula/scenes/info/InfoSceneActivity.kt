package com.appamedix.makula.scenes.info

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.support.v4.content.ContextCompat
import android.text.Annotation
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import android.view.View
import android.widget.Toast
import com.appamedix.makula.BuildConfig
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.constants.Const
import com.appamedix.makula.extensions.batchRequestPermissions
import com.appamedix.makula.extensions.isPermissionGranted
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.InfoType
import com.appamedix.makula.utils.PermissionUtils
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.worker.datamodel.DataModelManager
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import com.appamedix.makula.worker.speech.SpeechSynthesizerListener
import kotlinx.android.synthetic.main.activity_info_scene.*
import java.io.File

class InfoSceneActivity : BaseActivity(), NavigationViewListener,
        SpeechSynthesizerListener, View.OnClickListener {

    companion object {
        const val REQUEST_STORAGE = 100
        const val INFO_MODEL = "info_model"
    }

    private var isLandscape: Boolean = false
    private var sceneType: InfoType? = null
    private var synthesizer: SpeechSynthesizer? = null

    private var exportButtonHeight = 0
    private var contentData = InfoContentData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_scene)

        // Ignore URI exposure.
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        // Check if the external storage is writable.
        if (!isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            batchRequestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE)
        }

        // Initialize synthesizer.
        synthesizer = SpeechSynthesizer(this)

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        intent.extras?.let {
            val infoModel = it.getParcelable<InfoModel>(INFO_MODEL)
            sceneType = infoModel?.sceneType
            contentData.sceneType = sceneType
        }

        // Setup navigation
        var title = ""
        sceneType?.let { title = getString(it.titleString()) }
        val navigationCellModel = NavigationCellModel(
                title,
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

        if (isLandscape) {
            navigation.layoutParams.height = 0
            instruction.textSize = Const.Font.content1Large
            export_button.textSize = Const.Font.headlineLarge
        }
        exportButtonHeight = export_button.layoutParams.height

        if (sceneType != InfoType.Backup) {
            export_button.layoutParams.height = 0
        } else {
            // Register callback for the export button.
            export_button.setOnClickListener(this)
        }

        // Set text
        sceneType?.let {
            instruction.text = getAttributedString(it.contentString()).trimStart()

            // Make speech data.
            val speechDataList = arrayListOf<SpeechSynthesizer.SpeechData>()
            val stringIds = it.speechText()
            for (stringId in stringIds) {
                val speechText = getString(stringId)
                val speechData = SpeechSynthesizer.SpeechData(speechText, -1)
                speechDataList.add(speechData)
            }

            synthesizer?.setSpeechData(speechDataList)
        }
    }

    override fun onStop() {
        super.onStop()
        stopSpeaking()
    }

    override fun onDestroy() {
        synthesizer?.destroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    override fun onClick(v: View?) {
        // Update state.
        if (contentData.processing) return
        contentData.processing = true
        export_button.layoutParams.height = 0

        // Checks external storage availability.
        if (!PermissionUtils().isExternalStorageWritable() ||
                !isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(
                    this,
                    R.string.externalStorageNotAvailable,
                    Toast.LENGTH_SHORT)
                    .show()
            // Reset state.
            contentData.processing = false
            export_button.layoutParams.height = exportButtonHeight

            return
        }

        // Export data.
        val path = DataModelManager.exportData(applicationContext)

        // Reset state.
        contentData.processing = false
        export_button.layoutParams.height = exportButtonHeight

        BuildConfig.APPLICATION_ID

        if (path == null) {
            showBackupError()
        } else {
            // Attach the backup file to email and present to user.
            sendMail(path)
        }
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

    private fun showBackupError() {
        Toast.makeText(this, R.string.backupErrorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun sendMail(attachment: String) {
        val attachFile = File(attachment)
        val path = Uri.parse("file:$attachment")
        val emailIntent = Intent(Intent.ACTION_SEND)
        // set the type to 'email'
        emailIntent.type = "vnd.android.cursor.dir/email"
        if (attachFile.exists()) {
            // the attachment
            emailIntent.putExtra(Intent.EXTRA_STREAM, path)
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        // the mail subject
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "")

        try {
            startActivity(emailIntent)
            //startActivity(Intent.createChooser(emailIntent, "Send email..."))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAttributedString(stringId: Int): SpannableString {
        // Get the text as SpannedString to get the spans attached to the text.
        val contentText = SpannedString(getText(stringId))

        // Get all the annotation spans from the text.
        val annotations = contentText.getSpans(0, contentText.length, Annotation::class.java)

        // Create a copy of the title text as a SpannableString.
        // The constructor copies both the text and the spans.
        val spannableString = SpannableString(contentText)

        // Iterate through all the annotation spans.
        for (annotation in annotations) {
            // Look for the span with the key.
            when (annotation.key) {
                "head" -> {
                    // Create the typeface.
                    val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.white))
                    val styleSpan = TextAppearanceSpan(this,
                            if (isLandscape) R.style.fontHeadLineLarge else R.style.fontHeadLineDefault)

                    // Set the span at the same indices as the annotation.
                    spannableString.setSpan(styleSpan,
                            contentText.getSpanStart(annotation),
                            contentText.getSpanEnd(annotation),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannableString.setSpan(colorSpan,
                            contentText.getSpanStart(annotation),
                            contentText.getSpanEnd(annotation),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                "link" -> {
                    // Create the typeface.
                    val colorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.white))

                    // Set the span at the same indices as the annotation.
                    spannableString.setSpan(colorSpan,
                            contentText.getSpanStart(annotation),
                            contentText.getSpanEnd(annotation),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }

        return spannableString
    }

    /* Navigation view listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {
        sceneType?.let {
            synthesizer?.let { synthesizer ->
                if (synthesizer.isSpeaking()) {
                    synthesizer.stopSpeaking()
                } else {
                    synthesizer.startSpeaking()
                }
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
