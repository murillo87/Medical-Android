package com.appamedix.makula.scenes.contactdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.contactdetail.table.ContactDetailTableAdapter
import com.appamedix.makula.scenes.contactdetail.table.ContactDetailTableDataSource
import com.appamedix.makula.scenes.contactdetail.table.maincell.ContactDetailCell
import com.appamedix.makula.scenes.contactdetail.table.maincell.ContactDetailCellListener
import com.appamedix.makula.scenes.contactdetail.table.maincell.ContactDetailCellViewModel
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.views.textcell.StaticTextCellView
import com.appamedix.makula.worker.datamodel.contact.ContactObject
import com.appamedix.makula.worker.datamodel.contact.getContactObject
import com.appamedix.makula.worker.datamodel.contact.updateContactObject
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import com.appamedix.makula.worker.speech.SpeechSynthesizerListener
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_contact_detail_scene.*
import java.util.*

class ContactDetailSceneActivity : BaseActivity(), NavigationViewListener,
        ContactDetailCellListener, SpeechSynthesizerListener {

    companion object {
        const val REQUEST_PHONE = 200
        const val CONTACT_DETAIL_MODEL = "contact_detail_model"
    }

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: ContactDetailTableAdapter? = null

    private var isLandscape: Boolean = false
    private var synthesizer: SpeechSynthesizer? = null

    private var contactObject: ContactObject? = null
    private lateinit var displayModel: ContactDetailDisplayModel
    private var cellViewModels: ArrayList<BaseCellViewModel>? = null

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail_scene)

        realm = Realm.getDefaultInstance()

        // Initialize synthesizer
        synthesizer = SpeechSynthesizer(this)

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        requestDisplayData()

        // Setup navigation
        val navigationCellModel = NavigationCellModel(
                getString(R.string.contactDetailTitle),
                R.color.white,
                false,
                true,
                true,
                ImageButtonType.Back,
                true,
                ImageButtonType.Speaker,
                this)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)
        navigation.setupView(navigationCellViewModel)

        if (isLandscape) {
            navigation.layoutParams.height = 0
        }

        // Setup table view.
        recyclerView = findViewById(R.id.contact_detail_table)
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        presentTable()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val event = ev ?: return super.dispatchTouchEvent(ev)
        if (event.action == MotionEvent.ACTION_UP) {
            currentFocus?.let { view ->
                val consumed = super.dispatchTouchEvent(ev)
                val viewTmp = currentFocus
                val viewNew = viewTmp ?: view

                if (viewNew == view) {
                    val rect = Rect()
                    val coordinates = IntArray(2)

                    view.getLocationOnScreen(coordinates)
                    rect.set(coordinates[0], coordinates[1], coordinates[0] + view.width, coordinates[1] + view.height)

                    val x = event.x.toInt()
                    val y = event.y.toInt()

                    if (rect.contains(x, y)) return consumed
                } else if (viewNew is EditText) {
                    return consumed
                }

                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(viewNew.windowToken, 0)
                viewNew.clearFocus()

                return consumed
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun onStop() {
        super.onStop()

        // Scroll to top.
        val recyclerView = this.recyclerView ?: return
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)

        // Stop speaking.
        stopSpeaking()
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
            val contactDetailModel = it.getParcelable<ContactDetailModel>(CONTACT_DETAIL_MODEL)
            val createDate = contactDetailModel?.createDate
            contactObject = realm.getContactObject(createDate ?: Date())
        }
    }

    /**
     * Apply data source and adapter to the table view.
     */
    private fun presentTable() {
        contactObject?.let {
            displayModel = ContactDetailDisplayModel(this, isLandscape, it)
            val dataSource: ContactDetailTableDataSource = ViewModelProviders.of(this).get(ContactDetailTableDataSource::class.java)
            dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
                this.cellViewModels = cellViewModels
                tableAdapter = ContactDetailTableAdapter(this@ContactDetailSceneActivity, cellViewModels!!, this)
                recyclerView!!.layoutManager = LinearLayoutManager(this@ContactDetailSceneActivity)
                recyclerView!!.adapter = tableAdapter
            })

            // Set up speech data.
            dataSource.getMenuCellSpeechData(displayModel).observe(this, Observer { speechData ->
                synthesizer?.setSpeechData(speechData!!)
            })
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

    /* Contact detail cell listener */

    override fun deleteButtonClicked(model: ContactDetailCellViewModel) {
        // Stops speaking.
        stopSpeaking()

        contactObject?.let {
            if (!realm.updateContactObject(it, model.type, null)) {
                databaseWriteError()
            } else {
                presentTable()
            }
        }
    }

    override fun editTextDidEndEditing(model: ContactDetailCellViewModel) {
        // Stops speaking.
        stopSpeaking()

        contactObject?.let {
            if (!realm.updateContactObject(it, model.type, model.title)) {
                databaseWriteError()
            } else {
                presentTable()
            }
        }
    }

    override fun onCellItemClicked(model: ContactDetailCellViewModel) {
        // Stops speaking.
        stopSpeaking()

        when (model.type) {
            ContactInfoType.Mobile -> {
                model.title?.let { sendSMS(it) }
            }
            ContactInfoType.Phone -> {
                model.title?.let { askForPhoneCall(it) }
            }
            ContactInfoType.Email -> {
                model.title?.let { sendEmail(it) }
            }
            ContactInfoType.Web -> {
                model.title?.let { openWeb(it) }
            }
            else -> throw IllegalArgumentException("Unsupported actions")
        }
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

    /* Utilities */

    /**
     * Informs that the user has pressed the cell for the mobile phone to start a SMS.
     * Opens immediately the SMS app on the device.
     *
     * @param mobileNumber: The mobile number to present for the SMS.
     */
    private fun sendSMS(mobileNumber: String) {
        try {
            val smsUri = Uri.parse("smsto:$mobileNumber")
            val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri)
            smsIntent.putExtra("sms_body", "")
            startActivity(smsIntent)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.smsFailureMessage, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    /**
     * Asks the user to confirm that calling the phone number is really intended.
     *
     * @param phoneNumber: The phone number to call.
     */
    private fun askForPhoneCall(phoneNumber: String) {
        val builder = AlertDialog.Builder(this@ContactDetailSceneActivity)
        builder.setTitle(R.string.phoneCallAlertTitle)
        builder.setMessage(R.string.phoneCallAlertMessage)
        builder.setPositiveButton(R.string.phoneCallConfirmButton) { _, _ ->
            makePhoneCall(phoneNumber)
        }
        builder.setNeutralButton(R.string.phoneCallCancelButton) { _, _ -> }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * Starts the phone call immediately.
     *
     * @param phoneNumber: The phone number to call.
     */
    private fun makePhoneCall(phoneNumber: String) {
        try {
            val callUri = Uri.parse("tel:$phoneNumber")
            val callIntent = Intent(Intent.ACTION_DIAL, callUri)
            startActivity(callIntent)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.phoneCallFailureMessage, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    /**
     * Informs that the user has pressed the cell with the website URL to open it.
     * Opens immediately the given URL in the device's web browser.
     *
     * @param webAddress: The URL of the website.
     */
    private fun openWeb(webAddress: String) {
        var urlString = webAddress
        if (!webAddress.startsWith("http://") && !webAddress.startsWith("https://")) {
            urlString = "http://$webAddress"
        }
        val url = Uri.parse(urlString)

        try {
            val urlIntent = Intent(Intent.ACTION_VIEW, url)
            startActivity(urlIntent)
        } catch (e: Exception) {
            Toast.makeText(this, R.string.webFailureMessage, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    /**
     * Informs that the user has pressed the cell for writing an email.
     *
     * @param emailAddress: The receiver's email address.
     */
    private fun sendEmail(emailAddress: String) {
        try {
            val emailUri = Uri.parse("mailto:$emailAddress")
            val emailIntent = Intent(Intent.ACTION_SENDTO, emailUri)
            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(this, R.string.emailFailureMessage, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    /* SpeechSynthesizer listener */

    override fun speechStarted(data: SpeechSynthesizer.SpeechData) {
        val recyclerView = this.recyclerView ?: return
        val viewModels = cellViewModels ?: return
        val position = data.position ?: 0

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(position, 0)

        val viewCell = recyclerView.findViewHolderForAdapterPosition(position)
        viewCell?.let { holder ->
            if (viewModels[position].cellType == ViewCellType.ContactDetailCell) {
                val cell = holder as ContactDetailCell
                cell.applySpeechHighlightColor()
            } else {
                val cell = holder as StaticTextCellView
                cell.applyHighlightColor()
            }
        }
    }

    override fun speechEnded(data: SpeechSynthesizer.SpeechData) {
        val recyclerView = this.recyclerView ?: return
        val viewModels = cellViewModels ?: return
        val position = data.position ?: 0

        val viewCell = recyclerView.findViewHolderForAdapterPosition(position)
        viewCell?.let { holder ->
            if (viewModels[position].cellType == ViewCellType.ContactDetailCell) {
                val cell = holder as ContactDetailCell
                cell.applySpeechDefaultColor()
            } else {
                val cell = holder as StaticTextCellView
                cell.applyDefaultColor()
            }
        }
    }

    override fun speechFinished() {
        val recyclerView = this.recyclerView ?: return
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)
    }
}
