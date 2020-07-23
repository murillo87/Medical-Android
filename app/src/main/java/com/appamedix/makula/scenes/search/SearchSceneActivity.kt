package com.appamedix.makula.scenes.search

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
import com.appamedix.makula.scenes.info.InfoModel
import com.appamedix.makula.scenes.info.InfoSceneActivity
import com.appamedix.makula.scenes.search.table.SearchTableAdapter
import com.appamedix.makula.scenes.search.table.SearchTableDataSource
import com.appamedix.makula.scenes.search.table.inputcell.SearchInputCellListener
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import kotlinx.android.synthetic.main.activity_search_scene.*

class SearchSceneActivity : BaseActivity(), NavigationViewListener,
        SearchInputCellListener, CellItemListener {

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: SearchTableAdapter? = null
    private var tableDataSource: SearchTableDataSource? = null

    private var isLandscape: Boolean = false
    private lateinit var displayModel: SearchDisplayModel

    private var searchString: String = ""
    private var cellViewModels: ArrayList<BaseCellViewModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_scene)

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        requestDisplayData()

        // Setup navigation
        val sceneTitle = getString(R.string.searchTitle)
        val navigationCellModel = NavigationCellModel(
                sceneTitle,
                R.color.white,
                false,
                true,
                true,
                ImageButtonType.Back,
                false,
                ImageButtonType.Speaker,
                this)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)
        navigation.setupView(navigationCellViewModel)

        if (isLandscape) {
            navigation.layoutParams.height = 0
        }

        // Setup table view.
        recyclerView = findViewById(R.id.search_table)
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        presentTable()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    private fun requestDisplayData() {
        displayModel = SearchDisplayModel(this, searchString, isLandscape)
    }

    private fun presentTable() {
        val dataSource = ViewModelProviders.of(this).get(SearchTableDataSource::class.java)
        dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
            this.cellViewModels = cellViewModels
            tableAdapter = SearchTableAdapter(this@SearchSceneActivity, cellViewModels!!, this)
            recyclerView!!.layoutManager = LinearLayoutManager(this@SearchSceneActivity)
            recyclerView!!.adapter = tableAdapter
        })
        tableDataSource = dataSource
    }

    /* Navigation view listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {
    }

    /* Search input cell listener */

    override fun editTextDidEndEditing(content: String) {
        searchString = content
        requestDisplayData()
        presentTable()
    }

    /* Cell item click listener */

    override fun onItemClicked(model: BaseCellViewModel, type: ViewCellType) {
        val dataSource = tableDataSource ?: return
        val viewModels = cellViewModels ?: return
        var index = viewModels.indexOf(model)
        index -= if (isLandscape) 2 else 1

        if (type == ViewCellType.StaticTextCell) {
            val infoType = dataSource.infoEntries[index]

            // Route to info scene.
            val infoModel = InfoModel(infoType)
            val intent = Intent(this, InfoSceneActivity::class.java)
            intent.putExtra(InfoSceneActivity.INFO_MODEL, infoModel)
            startActivity(intent)
            overridePendingTransition(R.anim.left_in, R.anim.left_out)
        }
    }
}
