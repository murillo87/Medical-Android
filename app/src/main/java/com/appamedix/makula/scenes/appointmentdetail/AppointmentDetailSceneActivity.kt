package com.appamedix.makula.scenes.appointmentdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.global.listener.CellItemListener
import com.appamedix.makula.scenes.appointmentdetail.table.AppointmentDetailTableAdapter
import com.appamedix.makula.scenes.appointmentdetail.table.AppointmentDetailTableDataSource
import com.appamedix.makula.scenes.note.NoteModel
import com.appamedix.makula.scenes.note.NoteSceneActivity
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.SceneId
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.utils.DateUtils
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.views.radiocell.SplitRadioCell
import com.appamedix.makula.views.splitcell.SplitCell
import com.appamedix.makula.views.textcell.StaticTextCellView
import com.appamedix.makula.views.textcell.StaticTextCellViewModel
import com.appamedix.makula.worker.datamodel.appointment.deleteData
import com.appamedix.makula.worker.datamodel.appointment.getAppointmentObjects
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import com.appamedix.makula.worker.speech.SpeechSynthesizerListener
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_appointment_detail_scene.*
import java.util.*

class AppointmentDetailSceneActivity : BaseActivity(), NavigationViewListener,
        CellItemListener, SpeechSynthesizerListener {

    companion object {
        const val DETAIL_MODEL = "appointment_detail_model"
    }

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: AppointmentDetailTableAdapter? = null

    private var isLandscape: Boolean = false
    private var synthesizer: SpeechSynthesizer? = null

    private lateinit var displayModel: AppointmentDetailDisplayModel
    private var appointmentDate: Date = Date()
    private var cellViewModels: ArrayList<BaseCellViewModel>? = null

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_detail_scene)

        realm = Realm.getDefaultInstance()

        // Initialize synthesizer
        synthesizer = SpeechSynthesizer(this)

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        // Sets up display model.
        requestDisplayData()

        // Setup navigation
        val navigationCellModel = NavigationCellModel(
                displayModel.title,
                displayModel.titleColor,
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
        recyclerView = findViewById(R.id.detail_table)
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        presentTable()
    }

    override fun onStop() {
        super.onStop()

        // Scroll to top.
        val recyclerView = this.recyclerView ?: return
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)

        // Stop synthesizer if speaking.
        synthesizer?.let { synthesizer ->
            if (synthesizer.isSpeaking()) {
                synthesizer.stopSpeaking()
            }
        }
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
        // Get focus date from intent.
        intent.extras?.let {
            val detailModel = it.getParcelable<AppointmentDetailModel>(DETAIL_MODEL)
            appointmentDate = detailModel?.date ?: Date()
        }

        // Determine the title's color depending on the appointment.
        var titleColor = R.color.white
        val appointments = realm.getAppointmentObjects(appointmentDate)
        if (appointments.size == 1) {
            appointments.first()?.let {
                titleColor = it.getAppointmentType().defaultColor()
            }
        }

        // Prepare display model.
        val titleString = DateUtils.toStringWithWeekday(this, appointmentDate)
        displayModel = AppointmentDetailDisplayModel(
                this,
                appointmentDate,
                isLandscape,
                titleString,
                titleColor
        )
    }

    /**
     * Apply data source and adapter to the table view.
     */
    private fun presentTable() {
        val dataSource: AppointmentDetailTableDataSource = ViewModelProviders.of(this)
                .get(AppointmentDetailTableDataSource::class.java)
        dataSource.realm = realm
        dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
            this.cellViewModels = cellViewModels
            tableAdapter = AppointmentDetailTableAdapter(this@AppointmentDetailSceneActivity, cellViewModels!!, this)
            recyclerView!!.layoutManager = LinearLayoutManager(this@AppointmentDetailSceneActivity)
            recyclerView!!.adapter = tableAdapter
        })

        // Set up speech data.
        val speechData = dataSource.getMainCellSpeechData(displayModel)
        synthesizer?.setSpeechData(speechData)
    }

    /* Cell click listener */

    override fun onItemClicked(model: BaseCellViewModel, type: ViewCellType) {
        if (type == ViewCellType.StaticTextCell) {
            val cellModel = model as StaticTextCellViewModel
            cellItemClicked(cellModel)
        }
    }

    private fun cellItemClicked(cellItem: StaticTextCellViewModel) {
        when (cellItem.toSceneId) {
            SceneId.Note -> {
                // Route to note scene
                val noteModel = NoteModel(appointmentDate)
                val intent = Intent(this, NoteSceneActivity::class.java)
                intent.putExtra(NoteSceneActivity.NOTE_MODEL, noteModel)
                startActivity(intent)
                overridePendingTransition(R.anim.left_in, R.anim.left_out)
            }
            SceneId.Delete -> {
                val builder = AlertDialog.Builder(this@AppointmentDetailSceneActivity)
                builder.setTitle(R.string.detailDeleteAlertTitle)
                builder.setMessage(R.string.detailDeleteAlertMessage)
                builder.setPositiveButton(R.string.detailDeleteAlertConfirm) { _, _ ->
                    deleteConfirmed()
                }
                builder.setNeutralButton(R.string.detailDeleteAlertCancel) { _, _ -> }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
            else -> return
        }
    }

    private fun deleteConfirmed() {
        // Delete data.
        realm.deleteData(displayModel.date)

        // Update display.
        val navTitle: TextView = navigation.findViewById(R.id.nav_title)
        navTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
        requestDisplayData()
        presentTable()
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
        val recyclerView = this.recyclerView ?: return
        val viewModels = cellViewModels ?: return
        val position = data.position ?: 0

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(position, 0)

        val viewCell = recyclerView.findViewHolderForAdapterPosition(position)
        viewCell?.let { holder ->
            when (viewModels[position].cellType) {
                ViewCellType.StaticTextCell -> {
                    val textCell = holder as StaticTextCellView
                    textCell.applyHighlightColor()
                }
                ViewCellType.SplitCell -> {
                    val splitCell = holder as SplitCell
                    splitCell.applySpeechHighlightColor()
                }
                else -> {
                    val radioCell = holder as SplitRadioCell
                    radioCell.applySpeechHighlightColor()
                }
            }
        }
    }

    override fun speechEnded(data: SpeechSynthesizer.SpeechData) {
        val recyclerView = this.recyclerView ?: return
        val viewModels = cellViewModels ?: return
        val position = data.position ?: 0

        val viewCell = recyclerView.findViewHolderForAdapterPosition(position)
        viewCell?.let { holder ->
            when (viewModels[position].cellType) {
                ViewCellType.StaticTextCell -> {
                    val textCell = holder as StaticTextCellView
                    textCell.applyDefaultColor()
                }
                ViewCellType.SplitCell -> {
                    val splitCell = holder as SplitCell
                    splitCell.applySpeechDefaultColor()
                }
                else -> {
                    val radioCell = holder as SplitRadioCell
                    radioCell.applySpeechDefaultColor()
                }
            }
        }
    }

    override fun speechFinished() {
        val recyclerView = this.recyclerView ?: return
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)
    }
}
