package com.appamedix.makula.scenes.diagnosis

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.widget.Toast
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.scenes.diagnosis.table.DiagnosisTableAdapter
import com.appamedix.makula.scenes.diagnosis.table.DiagnosisTableDataSource
import com.appamedix.makula.scenes.diagnosis.table.DiagnosisType
import com.appamedix.makula.scenes.diagnosis.table.maincell.DiagnosisCell
import com.appamedix.makula.scenes.diagnosis.table.maincell.DiagnosisCellListener
import com.appamedix.makula.scenes.diagnosis.table.maincell.DiagnosisCellViewModel
import com.appamedix.makula.scenes.info.InfoModel
import com.appamedix.makula.scenes.info.InfoSceneActivity
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.InfoType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.worker.datamodel.diagnosis.getDiagnosisObjects
import com.appamedix.makula.worker.datamodel.diagnosis.updateDiagnosisObject
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import com.appamedix.makula.worker.speech.SpeechSynthesizerListener
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_diagnosis_scene.*

class DiagnosisSceneActivity : BaseActivity(), NavigationViewListener,
        DiagnosisCellListener, SpeechSynthesizerListener {

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: DiagnosisTableAdapter? = null
    private var tableDataSource: DiagnosisTableDataSource? = null

    private var isLandscape: Boolean = false
    private lateinit var displayModel: DiagnosisDisplayModel

    private var synthesizer: SpeechSynthesizer? = null
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnosis_scene)

        realm = Realm.getDefaultInstance()

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        // Initialize synthesizer
        synthesizer = SpeechSynthesizer(this)

        requestDisplayData()

        // Setup navigation
        val sceneTitle = getString(R.string.diagnosisTitle)
        val navigationCellModel = NavigationCellModel(
                sceneTitle,
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
        recyclerView = findViewById(R.id.diagnosis_table)
        (recyclerView!!.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        presentTable()
    }

    override fun onStop() {
        super.onStop()

        // Scroll to top.
        val recyclerView = this.recyclerView ?: return
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)

        // Stops speaking.
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
        displayModel = DiagnosisDisplayModel(this, isLandscape)
    }

    private fun presentTable() {
        val dataSource = ViewModelProviders.of(this).get(DiagnosisTableDataSource::class.java)
        dataSource.diagnosisObjects = realm.getDiagnosisObjects()
        dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
            tableAdapter = DiagnosisTableAdapter(this@DiagnosisSceneActivity, cellViewModels!!)
            recyclerView!!.layoutManager = LinearLayoutManager(this@DiagnosisSceneActivity)
            recyclerView!!.adapter = tableAdapter
        })

        // Set up speech data.
        dataSource.getMenuCellSpeechData(displayModel).observe(this, Observer { speechData ->
            synthesizer?.setSpeechData(speechData!!)
        })

        tableDataSource = dataSource
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
     * Reloads a specific cell.
     *
     * @param position: The index for the cell to update.
     */
    private fun updateCell(position: Int) {
        val tableAdapter = this.tableAdapter ?: return
        val cellViewModel = tableAdapter.arrayList[position] as DiagnosisCellViewModel
        cellViewModel.selected = !cellViewModel.selected

        tableAdapter.notifyItemChanged(position)
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

    /* Diagnosis cell listener */

    override fun infoButtonPressed(type: DiagnosisType) {
        // Decide info type.
        val infoType: InfoType = when (type) {
            DiagnosisType.AMD -> InfoType.AMD
            DiagnosisType.DMO -> InfoType.DMO
            DiagnosisType.RVV -> InfoType.RVV
            DiagnosisType.MCNV -> InfoType.MCNV
        }

        // Route to info scene.
        val infoModel = InfoModel(infoType)
        val intent = Intent(this, InfoSceneActivity::class.java)

        intent.putExtra(InfoSceneActivity.INFO_MODEL, infoModel)
        startActivity(intent)
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
    }

    override fun cellItemClicked(position: Int) {
        val dataSource = tableDataSource ?: return
        val objects = dataSource.diagnosisObjects ?: return

        // Stops speaking.
        stopSpeaking()

        val index = if (isLandscape) position - 1 else position
        val diagnosisObject = objects[index]

        diagnosisObject?.let {
            if (!realm.updateDiagnosisObject(it, !it.selected)) {
                databaseWriteError()
                return
            }
            updateCell(position)
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
            val diagnosisCell = holder as DiagnosisCell
            diagnosisCell.applySpeechHighlightColor()
        }
    }

    override fun speechEnded(data: SpeechSynthesizer.SpeechData) {
        val recyclerView = this.recyclerView ?: return
        val position = data.position ?: 0

        val viewCell = recyclerView.findViewHolderForAdapterPosition(position)
        viewCell?.let { holder ->
            val diagnosisCell = holder as DiagnosisCell
            if (diagnosisCell.selected) {
                diagnosisCell.applyHighlightColor()
            } else {
                diagnosisCell.applyDefaultColor()
            }
        }
    }

    override fun speechFinished() {
        val recyclerView = this.recyclerView ?: return
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)
    }
}
