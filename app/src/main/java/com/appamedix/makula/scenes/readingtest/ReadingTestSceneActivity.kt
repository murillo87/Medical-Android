package com.appamedix.makula.scenes.readingtest

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
import com.appamedix.makula.scenes.info.InfoModel
import com.appamedix.makula.scenes.info.InfoSceneActivity
import com.appamedix.makula.scenes.readingtest.table.ReadingTestTableAdapter
import com.appamedix.makula.scenes.readingtest.table.ReadingTestTableDataSource
import com.appamedix.makula.scenes.readingtest.table.readingcell.ReadingTestCellListener
import com.appamedix.makula.scenes.readingtest.table.readingcell.ReadingTestCellViewModel
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.InfoType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.worker.datamodel.readingtest.*
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_reading_test_scene.*
import java.util.*

class ReadingTestSceneActivity : BaseActivity(), NavigationViewListener, ReadingTestCellListener {

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: ReadingTestTableAdapter? = null

    private var isLandscape: Boolean = false

    private lateinit var displayModel: ReadingTestDisplayModel
    private var contentData = ReadingTestContentData()

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_test_scene)

        realm = Realm.getDefaultInstance()

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        // Sets up models.
        setContentData()
        requestDisplayData()

        // Setup navigation
        val sceneTitle = getString(R.string.readingTestTitle)
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
        recyclerView = findViewById(R.id.reading_test_table)
        (recyclerView!!.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        presentTable()
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    override fun onBackPressed() {
        // Delete an empty data model.
        if (contentData.readingTestObject != null) {
            val modelObject = contentData.readingTestObject
            if (modelObject?.getMagnitudeTypeLeft() == null && modelObject?.getMagnitudeTypeRight() == null) {
                if (realm.deleteReadingTestObject(modelObject!!)) {
                    Log.e("ReadingTest", "Deleting an empty reading test object failed")
                }
            }
            contentData.readingTestObject = null
        }

        // Route back.
        super.onBackPressed()
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    private fun setContentData() {
        if (contentData.readingTestObject == null) {
            val today = Date()
            val modelResults = realm.getReadingTestObjects(today)

            if (modelResults == null) {
                // No object for the day, create one temporarily.
                contentData.readingTestObject = realm.createReadingTestObject(today)
            } else {
                // Found an existing model for the day.
                contentData.readingTestObject = modelResults.first()
            }
        }
    }

    private fun requestDisplayData() {
        displayModel = ReadingTestDisplayModel(
                this,
                isLandscape,
                contentData.readingTestObject
        )
    }

    /**
     * Apply data source and adapter to the table view, shows table.
     */
    private fun presentTable() {
        val dataSource: ReadingTestTableDataSource = ViewModelProviders.of(this).get(ReadingTestTableDataSource::class.java)
        dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
            tableAdapter = ReadingTestTableAdapter(this@ReadingTestSceneActivity, cellViewModels!!)
            recyclerView!!.layoutManager = LinearLayoutManager(this@ReadingTestSceneActivity)
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
        val cellViewModel = tableAdapter.arrayList[position] as ReadingTestCellViewModel
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
        val startIndex = tableAdapter.arrayList.size - ReadingTestMagnitudeType.Big.rawValue
        val endIndex = tableAdapter.arrayList.size - 1
        for (index in startIndex..endIndex) {
            val cellViewModel = tableAdapter.arrayList[index] as ReadingTestCellViewModel
            if (isLeft) {
                cellViewModel.leftSelected = (index == position)
            } else {
                cellViewModel.rightSelected = (index == position)
            }
        }

        tableAdapter.notifyItemRangeChanged(startIndex, ReadingTestMagnitudeType.Big.rawValue)
    }

    /**
     * Shows database error
     */
    private fun databaseWriteError() {
        Toast.makeText(this, R.string.databaseWriteErrorMessage, Toast.LENGTH_SHORT).show()
    }

    /* Navigation view listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {
        // Route to info scene.
        val infoModel = InfoModel(InfoType.ReadingTest)
        val intent = Intent(this, InfoSceneActivity::class.java)

        intent.putExtra(InfoSceneActivity.INFO_MODEL, infoModel)
        startActivity(intent)
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
    }

    /* Reading test cell listener */

    override fun leftButtonSelected(position: Int) {
        val readingTestObject = contentData.readingTestObject ?: return
        val tableAdapter = this.tableAdapter ?: return
        val magnitudeType = (tableAdapter.arrayList[position] as ReadingTestCellViewModel).magnitudeType

        val newValue = if (readingTestObject.getMagnitudeTypeLeft() == magnitudeType) null else magnitudeType
        if (!realm.updateReadingTestObject(readingTestObject, newValue, true)) {
            databaseWriteError()
            return
        }

        if (newValue == null) {
            updateCell(position, true)
        } else {
            updateRadioCells(position, true)
        }
    }

    override fun rightButtonSelected(position: Int) {
        val readingTestObject = contentData.readingTestObject ?: return
        val tableAdapter = this.tableAdapter ?: return
        val magnitudeType = (tableAdapter.arrayList[position] as ReadingTestCellViewModel).magnitudeType

        val newValue = if (readingTestObject.getMagnitudeTypeRight() == magnitudeType) null else magnitudeType
        if (!realm.updateReadingTestObject(readingTestObject, newValue, false)) {
            databaseWriteError()
            return
        }

        if (newValue == null) {
            updateCell(position, false)
        } else {
            updateRadioCells(position, false)
        }
    }
}
