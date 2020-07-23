package com.appamedix.makula.scenes.visusnhd

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.constants.Const
import com.appamedix.makula.scenes.graph.EyeType
import com.appamedix.makula.scenes.graph.GraphModel
import com.appamedix.makula.scenes.graph.GraphSceneActivity
import com.appamedix.makula.scenes.info.InfoModel
import com.appamedix.makula.scenes.info.InfoSceneActivity
import com.appamedix.makula.scenes.visusnhd.table.VisusNhdInputTableAdapter
import com.appamedix.makula.scenes.visusnhd.table.VisusNhdInputTableDataSource
import com.appamedix.makula.scenes.visusnhd.table.pickercell.VisusNhdInputPickerCellListener
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.InfoType
import com.appamedix.makula.types.VisusNhdType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.views.splitcell.SplitCellListener
import com.appamedix.makula.views.splitcell.SplitCellModel
import com.appamedix.makula.views.splitcell.SplitCellViewModel
import com.appamedix.makula.worker.datamodel.histogram.*
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_visus_nhd_input_scene.*
import java.util.*

class VisusNhdInputSceneActivity : BaseActivity(), NavigationViewListener,
        SplitCellListener, VisusNhdInputPickerCellListener {

    companion object {
        const val VISUS_NHD_MODEL = "visus_nhd_model"
    }

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: VisusNhdInputTableAdapter? = null

    private var isLandscape: Boolean = false

    private lateinit var displayModel: VisusNhdInputDisplayModel
    private var contentData = VisusNhdInputContentData()

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visus_nhd_input_scene)

        realm = Realm.getDefaultInstance()

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        // Sets up models.
        setContentData()
        requestDisplayData()

        // Setup navigation
        val sceneTitle = getString(displayModel.sceneType.titleString())
        val navigationCellModel = NavigationCellModel(
                sceneTitle,
                R.color.white,
                false,
                true,
                true,
                ImageButtonType.Back,
                true,
                ImageButtonType.NavInfo,
                this)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)
        navigation.setupView(navigationCellViewModel)

        if (isLandscape) {
            navigation.layoutParams.height = 0
            save_button.textSize = Const.Font.content1Large
        }

        // Set click listener to save button.
        save_button.setOnClickListener {
            saveButtonPressed()
        }
        save_button.isEnabled = false

        // Setup table view.
        recyclerView = findViewById(R.id.visus_nhd_table)
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        presentTable()
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    private fun setContentData() {
        intent.extras?.let {
            val dataModel = it.getParcelable<VisusNhdInputModel>(VISUS_NHD_MODEL)
            contentData.sceneType = dataModel?.sceneType
        }

        // Retrieve existing data for today.
        val today = Date()

        when (contentData.sceneType) {
            VisusNhdType.Visus -> {
                val results = realm.getVisusObjects(today)
                results?.first()?.let {
                    contentData.leftValue = it.valueLeft
                    contentData.rightValue = it.valueRight
                }
            }
            VisusNhdType.Nhd -> {
                val results = realm.getNhdObjects(today)
                results?.first()?.let {
                    contentData.leftValue = it.valueLeft.toInt()
                    contentData.rightValue = it.valueRight.toInt()
                }
            }
        }

        if (contentData.leftValue == null && contentData.rightValue == null) {
            // Preselect the value with the middle of possible values.
            val sceneType = contentData.sceneType ?: VisusNhdType.Visus
            contentData.leftValue = sceneType.middleValue()
            contentData.leftSelected = true
            contentData.rightSelected = false
        }
    }

    private fun requestDisplayData() {
        val sceneType = contentData.sceneType ?: error("No scene type given")
        displayModel = VisusNhdInputDisplayModel(
                this,
                isLandscape,
                sceneType,
                contentData.leftSelected,
                contentData.rightSelected,
                contentData.leftValue,
                contentData.rightValue
        )
    }

    /**
     * Apply data source and adapter to the table view.
     */
    private fun presentTable() {
        val dataSource: VisusNhdInputTableDataSource = ViewModelProviders.of(this)
                .get(VisusNhdInputTableDataSource::class.java)
        dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
            tableAdapter = VisusNhdInputTableAdapter(this@VisusNhdInputSceneActivity, cellViewModels!!)
            recyclerView!!.layoutManager = LinearLayoutManager(this@VisusNhdInputSceneActivity)
            recyclerView!!.adapter = tableAdapter
        })
    }

    /**
     * Shows database error
     */
    private fun databaseWriteError() {
        Toast.makeText(this, R.string.databaseWriteErrorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun saveButtonPressed() {
        val sceneType = contentData.sceneType ?: error("No scene type given")
        val leftValue = contentData.leftValue ?: error("No value given")
        val rightValue = contentData.rightValue ?: error("No value given")

        // Save data
        val today = Date()
        when (sceneType) {
            VisusNhdType.Visus -> {
                val results = realm.getVisusObjects(today)
                if (results != null) {
                    // Update existing entry.
                    results.first()?.let {
                        if (!realm.updateVisusObject(it, today, leftValue, rightValue)) {
                            databaseWriteError()
                            return
                        }
                    }
                } else {
                    // Create new entry.
                    realm.createVisusObject(today, leftValue, rightValue)
                }
            }
            VisusNhdType.Nhd -> {
                val results = realm.getNhdObjects(today)
                if (results != null) {
                    // Update existing entry.
                    results.first()?.let {
                        if (!realm.updateNhdObject(it, today, leftValue, rightValue)) {
                            databaseWriteError()
                            return
                        }
                    }
                } else {
                    // Create new entry.
                    realm.createNhdObject(today, leftValue, rightValue)
                }
            }
        }

        // Route to graph scene.
        var type = EyeType.Left
        if (contentData.rightSelected) type = EyeType.Right

        val graphModel = GraphModel(type, true)
        val intent = Intent(this, GraphSceneActivity::class.java)
        intent.putExtra(GraphSceneActivity.GRAPH_MODEL, graphModel)
        startActivity(intent)
    }

    /* Navigation view listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {
        val sceneType = contentData.sceneType ?: error("No scene type given")

        // Route to info scene
        val infoType = if (sceneType == VisusNhdType.Visus) InfoType.Visus else InfoType.NHD
        val infoModel = InfoModel(infoType)
        val intent = Intent(this, InfoSceneActivity::class.java)

        intent.putExtra(InfoSceneActivity.INFO_MODEL, infoModel)
        startActivity(intent)
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
    }

    /* Split cell listener */

    override fun leftButtonPressed() {
        val sceneType = contentData.sceneType ?: error("No scene type given")

        if (contentData.leftValue == null) {
            // Preselect the value with the middle of possible values.
            contentData.leftValue = sceneType.middleValue()
        }
        if (contentData.leftValue != null && contentData.rightValue != null) {
            // Both values have data, enable confirm button.
            save_button.isEnabled = true
            save_button.setBackgroundColor(ContextCompat.getColor(this, R.color.lightMain))
        }
        contentData.leftSelected = true
        contentData.rightSelected = false

        requestDisplayData()
        presentTable()
    }

    override fun rightButtonPressed() {
        val sceneType = contentData.sceneType ?: error("No scene type given")

        if (contentData.rightValue == null) {
            // Preselect the value with the middle of possible values.
            contentData.rightValue = sceneType.middleValue()
        }
        if (contentData.leftValue != null && contentData.rightValue != null) {
            // Both values have data, enable confirm button.
            save_button.isEnabled = true
            save_button.setBackgroundColor(ContextCompat.getColor(this, R.color.lightMain))
        }
        contentData.leftSelected = false
        contentData.rightSelected = true

        requestDisplayData()
        presentTable()
    }

    /* Visus/NHD input picker cell listener */

    override fun pickerValueChanged(newValue: Int) {
        when {
            contentData.leftSelected -> contentData.leftValue = newValue
            contentData.rightSelected -> contentData.rightValue = newValue
            else -> error("No value selected")
        }

        // Update the table data
        val cellIndex = if (isLandscape) 2 else 1
        val leftValueTitle = displayModel.sceneType.valueOutput(this, contentData.leftValue)
        val rightValueTitle = displayModel.sceneType.valueOutput(this, contentData.rightValue)
        val splitValueCellModel = SplitCellModel(
                leftValueTitle,
                rightValueTitle,
                null,
                contentData.leftSelected,
                contentData.rightSelected,
                isLandscape,
                true,
                R.color.darkMain,
                R.color.lightMain,
                this
        )
        val splitValueCellViewModel = SplitCellViewModel(splitValueCellModel)
        tableAdapter?.let {
            it.arrayList[cellIndex] = splitValueCellViewModel
            it.notifyItemChanged(cellIndex)
        }
    }
}
