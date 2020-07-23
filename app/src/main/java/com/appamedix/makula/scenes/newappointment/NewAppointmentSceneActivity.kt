package com.appamedix.makula.scenes.newappointment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.global.listener.CellItemListener
import com.appamedix.makula.scenes.datepicker.AppointmentDatePickerModel
import com.appamedix.makula.scenes.datepicker.AppointmentDatePickerSceneActivity
import com.appamedix.makula.scenes.newappointment.table.NewAppointmentTableAdapter
import com.appamedix.makula.scenes.newappointment.table.NewAppointmentTableDataSource
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.views.textcell.StaticTextCellView
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import com.appamedix.makula.worker.speech.SpeechSynthesizerListener
import kotlinx.android.synthetic.main.activity_new_appointment_scene.*

class NewAppointmentSceneActivity : BaseActivity(), NavigationViewListener,
        CellItemListener, SpeechSynthesizerListener {

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: NewAppointmentTableAdapter? = null

    private var isLandscape: Boolean = false
    private var synthesizer: SpeechSynthesizer? = null
    private lateinit var displayModel: NewAppointmentDisplayModel

    private var tableCellData: ArrayList<BaseCellViewModel>? = null
    private var tableDataSource: NewAppointmentTableDataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_appointment_scene)

        // Initialize synthesizer
        synthesizer = SpeechSynthesizer(this)

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        requestDisplayData()

        // Setup navigation
        val navigationCellModel = NavigationCellModel(
                getString(R.string.newAppointmentTitle),
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
        recyclerView = findViewById(R.id.main_table)
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        // Apply data source and adapter to the table view.
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
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    private fun requestDisplayData() {
        displayModel = NewAppointmentDisplayModel(
                this,
                isLandscape)
    }

    private fun presentTable() {
        val dataSource = ViewModelProviders.of(this).get(NewAppointmentTableDataSource::class.java)
        dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
            tableCellData = cellViewModels
            tableAdapter = NewAppointmentTableAdapter(this@NewAppointmentSceneActivity, cellViewModels!!, this)
            recyclerView!!.layoutManager = LinearLayoutManager(this@NewAppointmentSceneActivity)
            recyclerView!!.adapter = tableAdapter
        })

        // Set up speech data.
        dataSource.getMenuCellSpeechData(displayModel).observe(this, Observer { speechData ->
            synthesizer?.setSpeechData(speechData!!)
        })

        tableDataSource = dataSource
    }

    /* Cell click listener */

    override fun onItemClicked(model: BaseCellViewModel, type: ViewCellType) {
        val dataSource = tableDataSource ?: return
        val cellData = tableCellData ?: return

        val menuIndex = when (isLandscape) {
            true -> cellData.indexOf(model) - 1
            false -> cellData.indexOf(model)
        }
        if (type == ViewCellType.StaticTextCell) {
            val appointmentType = dataSource.tableData[menuIndex]
            val datePickerModel = AppointmentDatePickerModel(appointmentType)
            val intent = Intent(this, AppointmentDatePickerSceneActivity::class.java)
            intent.putExtra(AppointmentDatePickerSceneActivity.DATA_MODEL, datePickerModel)
            startActivity(intent)
        }
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
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
        val position = data.position ?: 0

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(position, 0)

        val viewCell = recyclerView.findViewHolderForAdapterPosition(position)
        viewCell?.let { holder ->
            (holder as StaticTextCellView).applyHighlightColor()
        }
    }

    override fun speechEnded(data: SpeechSynthesizer.SpeechData) {
        val recyclerView = this.recyclerView ?: return
        val position = data.position ?: 0

        val viewCell = recyclerView.findViewHolderForAdapterPosition(position)
        viewCell?.let { holder ->
            (holder as StaticTextCellView).applyDefaultColor()
        }
    }

    override fun speechFinished() {
        val recyclerView = this.recyclerView ?: return
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)
    }
}
