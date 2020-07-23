package com.appamedix.makula.scenes.medicament

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.widget.Toast
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.medicament.table.MedicamentTableAdapter
import com.appamedix.makula.scenes.medicament.table.MedicamentTableDataSource
import com.appamedix.makula.scenes.medicament.table.inputcell.MedicamentInputCell
import com.appamedix.makula.scenes.medicament.table.inputcell.MedicamentInputCellListener
import com.appamedix.makula.scenes.medicament.table.maincell.MedicamentCell
import com.appamedix.makula.scenes.medicament.table.maincell.MedicamentCellListener
import com.appamedix.makula.scenes.medicament.table.maincell.MedicamentCellModel
import com.appamedix.makula.scenes.medicament.table.maincell.MedicamentCellViewModel
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.worker.datamodel.medicament.*
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import com.appamedix.makula.worker.speech.SpeechSynthesizerListener
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_medicament_scene.*

class MedicamentSceneActivity : BaseActivity(), NavigationViewListener,
        MedicamentCellListener, MedicamentInputCellListener, SpeechSynthesizerListener {

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: MedicamentTableAdapter? = null
    private var tableDataSource: MedicamentTableDataSource? = null

    private var isLandscape: Boolean = false
    private lateinit var displayModel: MedicamentDiaplayModel

    private var synthesizer: SpeechSynthesizer? = null
    private var cellViewModels: ArrayList<BaseCellViewModel>? = null

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicament_scene)

        realm = Realm.getDefaultInstance()

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        // Initialize synthesizer
        synthesizer = SpeechSynthesizer(this)

        requestDisplayData()

        // Setup navigation
        val sceneTitle = getString(R.string.medicamentTitle)
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
        recyclerView = findViewById(R.id.medicament_table)
        (recyclerView!!.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        presentTable()
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
        displayModel = MedicamentDiaplayModel(this, isLandscape)
    }

    private fun presentTable() {
        val dataSource = ViewModelProviders.of(this).get(MedicamentTableDataSource::class.java)
        dataSource.medicamentObjects = realm.getMedicamentObjects()
        dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
            this.cellViewModels = cellViewModels
            tableAdapter = MedicamentTableAdapter(this@MedicamentSceneActivity, cellViewModels!!)
            recyclerView!!.layoutManager = LinearLayoutManager(this@MedicamentSceneActivity)
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
        val cellViewModel = tableAdapter.arrayList[position] as MedicamentCellViewModel
        cellViewModel.selected = !cellViewModel.selected

        tableAdapter.notifyItemChanged(position)
    }

    /**
     * Insert a new cell into specific cell position.
     *
     * @param content: The title for the new cell.
     * @param position: The position to be added.
     */
    private fun insertCell(content: String, position: Int) {
        val tableAdapter = this.tableAdapter ?: return

        val cellModel = MedicamentCellModel(
                content,
                true,
                true,
                isLandscape,
                this)
        val cellViewModel = MedicamentCellViewModel(cellModel)
        tableAdapter.arrayList.add(position, cellViewModel)
        tableAdapter.notifyDataSetChanged()
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

    /* Medicament cell listener */

    override fun deleteButtonClicked(position: Int) {
        val dataSource = tableDataSource ?: return
        val objects = dataSource.medicamentObjects ?: return

        // Stops speaking.
        stopSpeaking()

        val index = if (isLandscape) position - 1 else position
        val medicamentObject = objects[index]

        medicamentObject?.let {
            if (!realm.deleteMedicamentObject(it)) {
                databaseWriteError()
            } else {
                dataSource.medicamentObjects = realm.getMedicamentObjects()
                // Updates speech data.
                dataSource.getMenuCellSpeechData(displayModel).observe(this, Observer { speechData ->
                    synthesizer?.setSpeechData(speechData!!)
                })
            }
        }
    }

    override fun onCellItemClicked(position: Int) {
        val dataSource = tableDataSource ?: return
        val objects = dataSource.medicamentObjects ?: return

        // Stops speaking.
        stopSpeaking()

        val index = if (isLandscape) position - 1 else position
        val medicamentObject = objects[index]

        medicamentObject?.let {
            if (!realm.updateMedicamentObject(it, !it.selected)) {
                databaseWriteError()
                return
            }
            updateCell(position)
        }
    }

    /* Medicament input listener */

    override fun editTextDidEndEditing(content: String, position: Int) {
        if (content.isEmpty()) return
        val dataSource = tableDataSource ?: return

        // Stops speaking.
        stopSpeaking()

        val newObject = realm.createMedicamentObject(content)

        newObject?.let {
            insertCell(content, position)
            dataSource.medicamentObjects = realm.getMedicamentObjects()

            // Updates speech data.
            dataSource.getMenuCellSpeechData(displayModel).observe(this, Observer { speechData ->
                synthesizer?.setSpeechData(speechData!!)
            })
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
            if (viewModels[position].cellType == ViewCellType.MedicamentCell) {
                val medicamentCell = holder as MedicamentCell
                medicamentCell.applySpeechHighlightColor()
            } else {
                val inputCell = holder as MedicamentInputCell
                inputCell.applySpeechHighlightColor()
            }
        }
    }

    override fun speechEnded(data: SpeechSynthesizer.SpeechData) {
        val recyclerView = this.recyclerView ?: return
        val viewModels = cellViewModels ?: return
        val position = data.position ?: 0

        val viewCell = recyclerView.findViewHolderForAdapterPosition(position)
        viewCell?.let { holder ->
            if (viewModels[position].cellType == ViewCellType.MedicamentCell) {
                val medicamentCell = holder as MedicamentCell
                if (medicamentCell.selected) {
                    medicamentCell.applyHighlightColor()
                } else {
                    medicamentCell.applyDefaultColor()
                }
            } else {
                val inputCell = holder as MedicamentInputCell
                inputCell.applySpeechDefaultColor()
            }
        }
    }

    override fun speechFinished() {
        val recyclerView = this.recyclerView ?: return
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)
    }
}
