package com.appamedix.makula.scenes.amslertest

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.util.Log
import android.widget.Toast
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.scenes.amslertest.table.AmslerTestTableAdapter
import com.appamedix.makula.scenes.amslertest.table.AmslerTestTableDataSource
import com.appamedix.makula.scenes.info.InfoModel
import com.appamedix.makula.scenes.info.InfoSceneActivity
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.InfoType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.views.radiocell.SplitRadioCellListener
import com.appamedix.makula.views.radiocell.SplitRadioCellViewModel
import com.appamedix.makula.worker.datamodel.amslertest.createAmslerTestObject
import com.appamedix.makula.worker.datamodel.amslertest.deleteAmslerTestObject
import com.appamedix.makula.worker.datamodel.amslertest.getAmslerTestObjects
import com.appamedix.makula.worker.datamodel.amslertest.updateAmslerTestObject
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_amsler_test_scene.*
import java.util.*

class AmslerTestSceneActivity : BaseActivity(), NavigationViewListener, SplitRadioCellListener {

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: AmslerTestTableAdapter? = null

    private var isLandscape: Boolean = false

    private lateinit var displayModel: AmslerTestDisplayModel
    private var contentData = AmslerTestContentData()

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amsler_test_scene)

        realm = Realm.getDefaultInstance()

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        // Sets up models.
        setContentData()
        requestDisplayData()

        // Setup navigation
        val sceneTitle = getString(R.string.amslerTestTitle)
        val navigationCellModel = NavigationCellModel(
                sceneTitle,
                R.color.white,
                false,
                false,
                true,
                ImageButtonType.Back,
                true,
                ImageButtonType.NavInfo,
                this)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)
        navigation.setupView(navigationCellViewModel)

        if (isLandscape) {
            navigation.layoutParams.height = 0
        }

        // Setup table view.
        recyclerView = findViewById(R.id.amsler_test_table)
        (recyclerView!!.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        presentTable()
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    override fun onBackPressed() {
        // Delete an empty data model.
        if (contentData.amslerTestObject != null) {
            val modelObject = contentData.amslerTestObject
            if (modelObject?.getProgressTypeLeft() == null && modelObject?.getProgressTypeRight() == null) {
                if (realm.deleteAmslerTestObject(modelObject!!)) {
                    Log.e("AmslerTest", "Deleting an empty amslertest object failed")
                }
            }
            contentData.amslerTestObject = null
        }

        // Route back.
        super.onBackPressed()
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    private fun setContentData() {
        if (contentData.amslerTestObject == null) {
            val today = Date()
            val modelResults = realm.getAmslerTestObjects(today)

            if (modelResults == null) {
                // No object for the day, create one temporarily.
                contentData.amslerTestObject = realm.createAmslerTestObject(today)
            } else {
                // Found an existing model for the day.
                contentData.amslerTestObject = modelResults.first()
            }
        }
    }

    private fun requestDisplayData() {
        displayModel = AmslerTestDisplayModel(
                this,
                isLandscape,
                contentData.amslerTestObject
        )
    }

    /**
     * Apply data source and adapter to the table view, shows table.
     */
    private fun presentTable() {
        val dataSource: AmslerTestTableDataSource = ViewModelProviders.of(this).get(AmslerTestTableDataSource::class.java)
        dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
            tableAdapter = AmslerTestTableAdapter(this@AmslerTestSceneActivity, cellViewModels!!)
            recyclerView!!.layoutManager = LinearLayoutManager(this@AmslerTestSceneActivity)
            recyclerView!!.adapter = tableAdapter
        })
    }

    /**
     * Reloads a specific cell.
     *
     * @param position: The index for the cell to update.
     * @param isLeft: Whether the left radio button is updated or not.
     */
    private fun updateCell(position: Int, isLeft: Boolean) {
        val tableAdapter = this.tableAdapter ?: return
        val cellViewModel = tableAdapter.arrayList[position] as SplitRadioCellViewModel
        if (isLeft) {
            cellViewModel.leftSelected = false
        } else {
            cellViewModel.rightSelected = false
        }

        tableAdapter.notifyItemChanged(position)
    }

    /**
     * Reloads all split radio cells.
     *
     * @param position: The index of the focused cell.
     * @param isLeft: Whether the left radio button is updated or not.
     */
    private fun updateRadioCells(position: Int, isLeft: Boolean) {
        val tableAdapter = this.tableAdapter ?: return
        val startIndex = tableAdapter.arrayList.size - AmslerTestProgressType.Worse.rawValue
        val endIndex = tableAdapter.arrayList.size - 1
        for (index in startIndex..endIndex) {
            val cellViewModel = tableAdapter.arrayList[index] as SplitRadioCellViewModel
            if (isLeft) {
                cellViewModel.leftSelected = (index == position)
            } else {
                cellViewModel.rightSelected = (index == position)
            }

            tableAdapter.arrayList[index] = cellViewModel
        }

        tableAdapter.notifyItemRangeChanged(startIndex, AmslerTestProgressType.Worse.rawValue)
    }

    /**
     * Shows database error
     */
    private fun databaseWriteError() {
        Toast.makeText(this, R.string.databaseWriteErrorMessage, Toast.LENGTH_SHORT).show()
    }

    /* Navigation cell listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {
        // Route to info scene.
        val infoModel = InfoModel(InfoType.AmslerTest)
        val intent = Intent(this, InfoSceneActivity::class.java)

        intent.putExtra(InfoSceneActivity.INFO_MODEL, infoModel)
        startActivity(intent)
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
    }

    /* Split radio cell listener */

    override fun leftButtonSelected(position: Int) {
        val amslerTestObject = contentData.amslerTestObject ?: return
        val tableAdapter = this.tableAdapter ?: return
        val progressType = (tableAdapter.arrayList[position] as SplitRadioCellViewModel).progressType

        val newValue = if (amslerTestObject.getProgressTypeLeft() == progressType) null else progressType
        if (!realm.updateAmslerTestObject(amslerTestObject, newValue, true)) {
            databaseWriteError()
        }

        if (newValue == null) {
            updateCell(position, true)
        } else {
            updateRadioCells(position, true)
        }
    }

    override fun rightButtonSelected(position: Int) {
        val amslerTestObject = contentData.amslerTestObject ?: return
        val tableAdapter = this.tableAdapter ?: return
        val progressType = (tableAdapter.arrayList[position] as SplitRadioCellViewModel).progressType

        val newValue = if (amslerTestObject.getProgressTypeRight() == progressType) null else progressType
        if (!realm.updateAmslerTestObject(amslerTestObject, newValue, false)) {
            databaseWriteError()
        }

        if (newValue == null) {
            updateCell(position, false)
        } else {
            updateRadioCells(position, false)
        }
    }
}
